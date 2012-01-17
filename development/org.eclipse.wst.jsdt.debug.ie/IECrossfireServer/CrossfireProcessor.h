/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


#pragma once

#include <queue>

#include "CrossfireEvent.h"
#include "CrossfireRequest.h"
#include "CrossfireResponse.h"
#include "JSONParser.h"
#include "Value.h"
#include "Logger.h"

class CrossfireProcessor {

public:
	CrossfireProcessor();
	~CrossfireProcessor();
	bool createEventPacket(CrossfireEvent* eventObj, std::wstring** _value);
	bool createResponsePacket(CrossfireResponse* response, std::wstring** _value);
	int parseRequestPacket(std::wstring* msg, CrossfireRequest** _value, wchar_t** _message);

private:
	JSONParser* m_jsonParser;
	unsigned int m_nextEventSeq;

	/* constants */
	static const wchar_t* HEADER_CONTENTLENGTH;
	static const wchar_t* LINEBREAK;
	static const size_t LINEBREAK_LENGTH;
	static const wchar_t* NAME_ARGUMENTS;
	static const wchar_t* NAME_BODY;
	static const wchar_t* NAME_CODE;
	static const wchar_t* NAME_COMMAND;
	static const wchar_t* NAME_CONTEXTID;
	static const wchar_t* NAME_EVENT;
	static const wchar_t* NAME_MESSAGE;
	static const wchar_t* NAME_REQUESTSEQ;
	static const wchar_t* NAME_RUNNING;
	static const wchar_t* NAME_SEQ;
	static const wchar_t* NAME_STATUS;
	static const wchar_t* NAME_TYPE;
	static const wchar_t* VALUE_EVENT;
	static const wchar_t* VALUE_REQUEST;
	static const wchar_t* VALUE_RESPONSE;
};
