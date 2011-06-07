/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


#include "stdafx.h"
#include "CrossfireProcessor.h"

/* initialize statics */
const wchar_t* CrossfireProcessor::HEADER_CONTENTLENGTH = L"Content-Length:";
const wchar_t* CrossfireProcessor::LINEBREAK = L"\r\n";
const size_t CrossfireProcessor::LINEBREAK_LENGTH = 2;
const wchar_t* CrossfireProcessor::NAME_ARGUMENTS = L"arguments";
const wchar_t* CrossfireProcessor::NAME_BODY = L"body";
const wchar_t* CrossfireProcessor::NAME_COMMAND = L"command";
const wchar_t* CrossfireProcessor::NAME_CONTEXTID = L"contextId";
const wchar_t* CrossfireProcessor::NAME_DATA = L"data";
const wchar_t* CrossfireProcessor::NAME_EVENT = L"event";
const wchar_t* CrossfireProcessor::NAME_REQUESTSEQ = L"request_seq";
const wchar_t* CrossfireProcessor::NAME_RUNNING = L"running";
const wchar_t* CrossfireProcessor::NAME_SEQ = L"seq";
const wchar_t* CrossfireProcessor::NAME_SUCCESS = L"success";
const wchar_t* CrossfireProcessor::NAME_TYPE = L"type";
const wchar_t* CrossfireProcessor::VALUE_EVENT = L"event";
const wchar_t* CrossfireProcessor::VALUE_REQUEST = L"request";
const wchar_t* CrossfireProcessor::VALUE_RESPONSE = L"response";

CrossfireProcessor::CrossfireProcessor() {
	m_jsonParser = new JSONParser();
	m_nextEventSeq = 0;
}

CrossfireProcessor::~CrossfireProcessor() {
	delete m_jsonParser;
}

bool CrossfireProcessor::createEventPacket(CrossfireEvent* eventObj, std::wstring** _value) {
	*_value = NULL;
	Value value_packet;

	/* packet type */
	value_packet.addObjectValue(NAME_TYPE, &Value(VALUE_EVENT));

	/* event type */
	if (!eventObj->getName()) {
		Logger::error("CrossfireProcessor.createEventPacket(): event does not have a name");
		return false;
	}
	value_packet.addObjectValue(NAME_EVENT, &Value(eventObj->getName()));

	/* context id */
	if (eventObj->getContextId()) {
		value_packet.addObjectValue(NAME_CONTEXTID, &Value(eventObj->getContextId()));
	}

	/* data */
	Value* dataValue = eventObj->getData();
	if (dataValue) {
		if (dataValue->getType() != TYPE_OBJECT) {
			Logger::error("CrossfireProcessor.createEventPacket(): event has data object of wrong type");
			return false;
		}
		value_packet.addObjectValue(NAME_DATA, dataValue);
	}

	/* seq */
	value_packet.setObjectValue(NAME_SEQ, &Value((double)m_nextEventSeq++));

	std::wstring* content = NULL;
	m_jsonParser->stringify(&value_packet, &content);
	wchar_t length[9];
	_ltow_s((unsigned int)content->length() + LINEBREAK_LENGTH, length, 9, 10); /* trailing linebreak */
	std::wstring* eventString = new std::wstring;
	eventString->append(HEADER_CONTENTLENGTH);
	eventString->append(length);
	eventString->append(LINEBREAK);
	eventString->append(LINEBREAK);
	eventString->append(*content);
	eventString->append(LINEBREAK);
	delete content;

	*_value = eventString;
	return true;
}

bool CrossfireProcessor::createResponsePacket(CrossfireResponse *response, std::wstring **_value) {
	static unsigned int s_nextResponseSeq = 0;
	*_value = NULL;
	Value value_packet;

	/* packet type */
	value_packet.addObjectValue(NAME_TYPE, &Value(VALUE_RESPONSE));

	/* command */
	if (!response->getName()) {
		Logger::error("CrossfireProcessor.createResponsePacket(): response does not have a name");
		return false;
	}
	value_packet.addObjectValue(NAME_COMMAND, &Value(response->getName()));

	/* contextId */
	if (response->getContextId()) {
		value_packet.addObjectValue(NAME_CONTEXTID, &Value(response->getContextId()));
	}

	/* request seq */
	if (response->getRequestSeq() < 0) {
		Logger::error("CrossfireProcessor.createResponsePacket(): response does not have a request seq value");
		return false;
	}
	value_packet.addObjectValue(NAME_REQUESTSEQ, &Value((double)response->getRequestSeq()));

	/* running */
	value_packet.addObjectValue(NAME_RUNNING, &Value(response->getRunning()));

	/* success */
	value_packet.addObjectValue(NAME_SUCCESS, &Value(response->getSuccess()));

	/* body */
	Value* bodyValue = response->getBody();
	if (!bodyValue || bodyValue->getType() != TYPE_OBJECT) {
		Logger::error("CrossfireProcessor.createResponsePacket(): response does not have a body value of type object");
		return false;
	}
	value_packet.addObjectValue(NAME_BODY, bodyValue);

	/* seq */
	value_packet.setObjectValue(NAME_SEQ, &Value((double)s_nextResponseSeq++));
	std::wstring* content = NULL;
	m_jsonParser->stringify(&value_packet, &content);
	wchar_t length[9];
	_ltow_s((unsigned int)content->length() + LINEBREAK_LENGTH, length, 9, 10); /* trailing linebreak */
	std::wstring* responseString = new std::wstring;
	responseString->append(HEADER_CONTENTLENGTH);
	responseString->append(length);
	responseString->append(LINEBREAK);
	responseString->append(LINEBREAK);
	responseString->append(*content);
	responseString->append(LINEBREAK);
	delete content;

	*_value = responseString;
	return true;
}

bool CrossfireProcessor::parseRequestPacket(std::wstring* msg, CrossfireRequest** _value) {
	*_value = NULL;

	size_t startIndex = msg->find(L"\r\n\r\n") + 2 * LINEBREAK_LENGTH; /* header linebreak */
	size_t msgLength = msg->length();

	if (msg->at(msgLength - 2) != wchar_t('\r')) {
		Logger::error("Request packet does not contain terminating '\\r'");
		return false;
	}
	if (msg->at(msgLength - 1) != wchar_t('\n')) {
		Logger::error("Request packet does not contain terminating '\\n'");
		return false;
	}

	std::wstring content = msg->substr(startIndex, msgLength - startIndex - LINEBREAK_LENGTH); /* trailing linebreak */

	Value* value_request = NULL;
	m_jsonParser->parse(&content, &value_request);
	if (!value_request) {
		Logger::error("Failure occurred while parsing Request packet content");
		return false;
	}

	Value* value_type = value_request->getObjectValue(NAME_TYPE);
	if (!value_type || value_type->getType() != TYPE_STRING || value_type->getStringValue()->compare(VALUE_REQUEST) != 0) {
		Logger::error("Request packet does not contain a 'type' value of \"request\"");
		delete value_request;
		return false;
	}

	Value* value_seq = value_request->getObjectValue(NAME_SEQ);
	if (!value_seq || value_seq->getType() != TYPE_NUMBER || value_seq->getNumberValue() < 0) {
		Logger::error("Request packet does not contain a 'seq' Number value >= 0");
		delete value_request;
		return false;
	}

	Value* value_command = value_request->getObjectValue(NAME_COMMAND);
	if (!value_command || value_command->getType() != TYPE_STRING) {
		Logger::error("Request packet does not contain a String 'command' value");
		delete value_request;
		return false;
	}

	Value* value_contextId = value_request->getObjectValue(NAME_CONTEXTID);
	if (value_contextId && value_contextId->getType() != TYPE_STRING) {
		Logger::error("Request packet contains a non-string 'context_id' value");
		delete value_request;
		return false;
	}

	Value* value_arguments = value_request->getObjectValue(NAME_ARGUMENTS);
	if (value_arguments && value_arguments->getType() != TYPE_OBJECT) {
		Logger::error("Request packet contains a non-object 'arguments' value");
		delete value_request;
		return false;
	}

	CrossfireRequest* result = new CrossfireRequest();
	result->setName((wchar_t*)value_command->getStringValue()->c_str());
	result->setSeq((unsigned int)value_seq->getNumberValue());
	if (value_contextId) {
		result->setContextId(value_contextId->getStringValue());
	}
	if (value_arguments) {
		result->setArguments(value_arguments);
	}

	*_value = result;
	delete value_request;
	return true;
}
