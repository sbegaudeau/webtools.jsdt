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
#include "CrossfireServer.h"

/* initialize statics */
const wchar_t* CrossfireServer::CONTEXTID_PREAMBLE = L"xfIE::";
const wchar_t* CrossfireServer::HANDSHAKE = L"CrossfireHandshake\r\n";
const wchar_t* CrossfireServer::HEADER_CONTENTLENGTH = L"Content-Length:";
const wchar_t* CrossfireServer::LINEBREAK = L"\r\n";
const size_t CrossfireServer::LINEBREAK_LENGTH = 2;

const wchar_t* CrossfireServer::COMMAND_CHANGEBREAKPOINT = L"changebreakpoint";
const wchar_t* CrossfireServer::COMMAND_CLEARBREAKPOINT = L"clearbreakpoint";
const wchar_t* CrossfireServer::COMMAND_GETBREAKPOINT = L"getbreakpoint";
const wchar_t* CrossfireServer::COMMAND_GETBREAKPOINTS = L"getbreakpoints";
const wchar_t* CrossfireServer::COMMAND_SETBREAKPOINT = L"setbreakpoint";

/* command: listContexts */
const wchar_t* CrossfireServer::COMMAND_LISTCONTEXTS = L"listcontexts";
const wchar_t* CrossfireServer::KEY_CONTEXTS = L"contexts";
const wchar_t* CrossfireServer::KEY_CURRENT = L"current";

/* command: version */
const wchar_t* CrossfireServer::COMMAND_VERSION = L"version";
const wchar_t* CrossfireServer::KEY_VERSION = L"version";
const wchar_t* CrossfireServer::VERSION_STRING = L"0.3";

/* event: closed */
const wchar_t* CrossfireServer::EVENT_CLOSED = L"closed";

/* event: onContextChanged */
const wchar_t* CrossfireServer::EVENT_CONTEXTCHANGED = L"onContextChanged";
const wchar_t* CrossfireServer::KEY_NEWHREF = L"new-href";

/* event: onContextCreated */
const wchar_t* CrossfireServer::EVENT_CONTEXTCREATED = L"onContextCreated";

/* event: onContextDestroyed */
const wchar_t* CrossfireServer::EVENT_CONTEXTDESTROYED = L"onContextDestroyed";

/* event: onContextLoaded */
const wchar_t* CrossfireServer::EVENT_CONTEXTLOADED = L"onContextLoaded";

/* shared */
const wchar_t* CrossfireServer::KEY_HREF = L"href";


CrossfireServer::CrossfireServer() {
	m_bpManager = new CrossfireBPManager();
	m_connection = NULL;
	m_contexts = new std::map<DWORD, CrossfireContext*>;
	m_currentContext = NULL;
	m_handshakeReceived = false;
	m_inProgressPacket = new std::wstring;
	m_lastRequestSeq = -1;
	m_listeners = new std::vector<ICrossfireServerListener*>;
	m_pendingEvents = new std::vector<CrossfireEvent*>;
	m_port = -1;
	m_processingRequest = false;
	m_processor = new CrossfireProcessor();
	m_windowHandle = 0;
}

CrossfireServer::~CrossfireServer() {
	delete m_bpManager;

	std::map<DWORD,CrossfireContext*>::iterator iterator = m_contexts->begin();
	while (iterator != m_contexts->end()) {
		delete iterator->second;
		iterator++;
	}
	delete m_contexts;

	std::vector<ICrossfireServerListener*>::iterator iterator2 = m_listeners->begin();
	while (iterator2 != m_listeners->end()) {
		(*iterator2)->Release();
		iterator2++;
	}
	delete m_listeners;

	std::vector<CrossfireEvent*>::iterator iterator3 = m_pendingEvents->begin();
	while (iterator3 != m_pendingEvents->end()) {
		if (m_connection) {
			sendEvent(*iterator3);
		}
		delete *iterator3;
		iterator3++;
	}
	delete m_pendingEvents;

	delete m_inProgressPacket;
	delete m_processor;
	if (m_connection) {
		delete m_connection;
	}

	if (m_windowHandle) {
		CComPtr<ICrossfireServerClass> serverClass = NULL;
		HRESULT hr = CoGetClassObject(CLSID_CrossfireServer, CLSCTX_ALL, 0, IID_ICrossfireServerClass, (LPVOID*)&serverClass);
		if (FAILED(hr)) {
			Logger::error("~CrossfireServer: CoGetClassObject() failed", hr);
			return;
		}
		hr = serverClass->RemoveServer(m_windowHandle);
		if (FAILED(hr)) {
			Logger::error("~CrossfireServer: RemoveServer() failed", hr);
			return;
		}
	}
}

HRESULT STDMETHODCALLTYPE CrossfireServer::addListener(ICrossfireServerListener* listener) {
	listener->AddRef();
	m_listeners->push_back(listener);
	return S_OK;
}

void CrossfireServer::connected() {
	std::vector<ICrossfireServerListener*>::iterator iterator = m_listeners->begin();
	while (iterator != m_listeners->end()) {
		(*iterator)->ServerStateChanged(STATE_CONNECTED, m_port);
		iterator++;
	}
}

HRESULT STDMETHODCALLTYPE CrossfireServer::contextCreated(DWORD processId, OLECHAR* href) {
	if (m_port == -1) {
		return S_FALSE;
	}

	HRESULT hr = registerContext(processId, href);
	if (SUCCEEDED(hr)) {
		std::map<DWORD, CrossfireContext*>::iterator iterator = m_contexts->find(processId);
		CrossfireContext* context = iterator->second;
		eventContextCreated(context);
	}
	return hr;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::contextDestroyed(DWORD processId) {
	if (m_port == -1) {
		return S_FALSE;
	}

	std::map<DWORD, CrossfireContext*>::iterator iterator = m_contexts->find(processId);
	if (iterator == m_contexts->end()) {
		Logger::error("CrossfireServer.contextDestroyed(): unknown context", processId);
		return S_FALSE;
	}
	CrossfireContext* context = iterator->second;
	eventContextDestroyed(context);
	m_contexts->erase(iterator);
	delete context;
	return S_OK;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::contextLoaded(DWORD processId) {
	if (m_port == -1) {
		return S_FALSE;
	}

	std::map<DWORD, CrossfireContext*>::iterator iterator = m_contexts->find(processId);
	if (iterator == m_contexts->end()) {
		Logger::error("CrossfireServer.contextLoaded(): unknown context", processId);
		return S_FALSE;
	}
	CrossfireContext* context = iterator->second;
	eventContextLoaded(context);
	return S_OK;
}

void CrossfireServer::disconnected() {
	std::vector<ICrossfireServerListener*>::iterator iterator = m_listeners->begin();
	while (iterator != m_listeners->end()) {
		(*iterator)->ServerStateChanged(STATE_DISCONNECTED, -1);
		iterator++;
	}
	reset();
}

CrossfireBPManager* CrossfireServer::getBreakpointManager() {
	return m_bpManager;
}

void CrossfireServer::getContextsArray(CrossfireContext*** _value) {
	CrossfireContext** result = new CrossfireContext*[m_contexts->size() + 1];
	int index = 0;
	std::map<DWORD,CrossfireContext*>::iterator iterator = m_contexts->begin();
	while (iterator != m_contexts->end()) {
		result[index++] = iterator->second;
		iterator++;
	}
	result[index] = NULL;
	*_value = result;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::getPort(unsigned int* value) {
	*value = m_port;
	return S_OK;
}

CrossfireContext* CrossfireServer::getRequestContext(CrossfireRequest* request) {
	std::wstring* contextId = request->getContextId();
	if (!contextId) {
		return NULL;
	}

	wchar_t* searchString = (wchar_t*)contextId->c_str();
	std::map<DWORD,CrossfireContext*>::iterator iterator = m_contexts->begin();
	while (iterator != m_contexts->end()) {
		if (wcscmp(iterator->second->getName(), searchString) == 0) {
			return iterator->second;
		}
		iterator++;
	}
	return NULL;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::getState(int* value) {
	if (m_connection == NULL) {
		*value = STATE_DISCONNECTED;
	} else {
		*value = m_connection->isConnected() ? STATE_CONNECTED : STATE_LISTENING;
	}
	return S_OK;
}

bool CrossfireServer::performRequest(CrossfireRequest* request) {
	wchar_t* command = request->getName();
	Value* arguments = request->getArguments();
	Value* responseBody = NULL;
	bool success = false;
	if (wcscmp(command, COMMAND_VERSION) == 0) {
		success = commandVersion(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_CHANGEBREAKPOINT) == 0) {
		if (request->getContextId()) {
			Logger::error("'changeBreakpoint' command should not specify a context_id");
			return false;
		}
		CrossfireContext** contexts = NULL;
		getContextsArray(&contexts);
		success = m_bpManager->commandChangeBreakpoint(arguments, (IBreakpointTarget**)contexts, &responseBody);
		delete contexts;
	} else if (wcscmp(command, COMMAND_CLEARBREAKPOINT) == 0) {
		if (request->getContextId()) {
			Logger::error("'clearBreakpoint' command should not specify a context_id");
			return false;
		}
		CrossfireContext** contexts = NULL;
		getContextsArray(&contexts);
		success = m_bpManager->commandClearBreakpoint(arguments, (IBreakpointTarget**)contexts, &responseBody);
		delete contexts;
	} else if (wcscmp(command, COMMAND_LISTCONTEXTS) == 0) {
		success = commandListContexts(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_GETBREAKPOINT) == 0) {
		CrossfireContext* context = getRequestContext(request);
		IBreakpointTarget* target = context ? (IBreakpointTarget*)context : (IBreakpointTarget*)m_bpManager;
		success = m_bpManager->commandGetBreakpoint(arguments, target, &responseBody);
	} else if (wcscmp(command, COMMAND_GETBREAKPOINTS) == 0) {
		CrossfireContext* context = getRequestContext(request);
		IBreakpointTarget* target = context ? (IBreakpointTarget*)context : (IBreakpointTarget*)m_bpManager;
		success = m_bpManager->commandGetBreakpoints(arguments, target, &responseBody);
	} else if (wcscmp(command, COMMAND_SETBREAKPOINT) == 0) {
		CrossfireContext* context = getRequestContext(request);
		CrossfireContext** contexts = NULL;
		if (context) {
			getContextsArray(&contexts);
		}
		success = m_bpManager->commandSetBreakpoint(arguments, (IBreakpointTarget**)contexts, &responseBody);
		if (contexts) {
			delete contexts;
		}
	} else {
		return false;	/* command not handled */
	}

	CrossfireResponse response;
	response.setName(command);
	response.setRequestSeq(request->getSeq());
	response.setRunning(true);
	response.setSuccess(success);
	if (success) {
		response.setBody(responseBody);
	} else {
		Value emptyBody;
		emptyBody.setType(TYPE_OBJECT);
		response.setBody(&emptyBody);
	}
	if (responseBody) {
		delete responseBody;
	}
	sendResponse(&response);
	return true;
}

bool CrossfireServer::processHandshake(wchar_t* msg) {
	std::wstring string(msg);
	size_t start = wcslen(HANDSHAKE);
	size_t index = string.find_first_of(std::wstring(L"\r\n"), start);
	if (index == std::wstring::npos) {
		Logger::error("Invalid handshake packet, does not contain terminating '\\r\\n'");
		return false;
	}
	if (index != string.length() - 2) {
		Logger::error("Invalid handshake packet, length extends beyond terminating '\\r\\n'");
		return false;
	}

	m_handshakeReceived = true;

	std::wstring tools = string.substr(start, index - start);
	Logger::log("Handshake tools requested");
	Logger::log((wchar_t*)tools.c_str());

	std::wstring handshake(HANDSHAKE);
	/* for now don't claim support for any tools */
	handshake.append(std::wstring(L"\r\n"));
	m_connection->send(handshake.c_str());
	return true;
}

void CrossfireServer::received(wchar_t* msg) {
	if (!m_handshakeReceived) {
		if (wcsncmp(msg, HANDSHAKE, wcslen(HANDSHAKE)) == 0) {
			processHandshake(msg);
		} else {
			Logger::error("Crossfire content received before handshake, not processing it");
		}
		return;
	}

	m_inProgressPacket->append(std::wstring(msg));
	std::wstring packet;
	do {
		packet.clear();
		if (m_inProgressPacket->find(HEADER_CONTENTLENGTH) != 0) {
			Logger::error("request packet does not start with 'Content-Length:', not processing it");
			Logger::log(m_inProgressPacket);
			m_inProgressPacket->clear();
			break;
		}

		size_t endIndex = m_inProgressPacket->find(wchar_t('\r'));
		if (endIndex == std::wstring::npos) {
			Logger::error("request packet does not contain '\r', not processing it");
			Logger::log(m_inProgressPacket);
			m_inProgressPacket->clear();
			break;
		}

		size_t headerLength = wcslen(HEADER_CONTENTLENGTH);
		std::wstring lengthString = m_inProgressPacket->substr(headerLength, endIndex - headerLength);
		int lengthValue = _wtoi(lengthString.c_str());
		if (!lengthValue) {
			Logger::error("request packet does not have a valid 'Content-Length' value, not processing it");
			Logger::log(m_inProgressPacket);
			m_inProgressPacket->clear();
			break;
		}

		if (m_inProgressPacket->find(L"\r\n", endIndex) != endIndex) {
			Logger::error("request packet does not follow initial '\\r' with '\\n', not processing it");
			Logger::log(m_inProgressPacket);
			m_inProgressPacket->clear();
			break;
		}

		// TODO for now just skip over "tool:" lines, though these should really be validated

		size_t toolEnd = m_inProgressPacket->find(L"\r\n\r\n", endIndex);
		if (toolEnd == std::wstring::npos) {
			Logger::error("request packet does not contain '\\r\\n\\r\\n' to delimit its header, not processing it");
			Logger::log(m_inProgressPacket);
			m_inProgressPacket->clear();
			break;
		}
		size_t toolsLength = toolEnd - endIndex;
		size_t targetLength = wcslen(HEADER_CONTENTLENGTH) + lengthString.length() + 3 * LINEBREAK_LENGTH + toolsLength + lengthValue;

		if (targetLength <= m_inProgressPacket->length()) {
			packet.assign(m_inProgressPacket->substr(0, targetLength));
			m_inProgressPacket->erase(0, targetLength);
		}

		if (packet.length()) {
			CrossfireRequest* request = NULL;
			if (!m_processor->parseRequestPacket(&packet, &request)) {
				Logger::error("invalid request packet received, not processing it");
				Logger::log(&packet);
			} else {
				unsigned int seq = request->getSeq();
				if (seq <= m_lastRequestSeq) {
					// TODO handle out-of-order packets
					Logger::log("packet received out of sequence, still processing it");
				}
				m_lastRequestSeq = seq;
				m_processingRequest = true;
				if (!performRequest(request)) {
					/*
					 * the request's command was not handled by the server,
					 * so try to delegate to the specified context, if any
					 */
					CrossfireContext* context = getRequestContext(request);
					if (!context) {
						Logger::error("request command was unknown to the server and a valid context id was not provided, not processing it");
						Logger::log(&packet);
					} else {
						if (!context->performRequest(request)) {
							Logger::error("request command was unknown to the server and to the specified context, not processing it");
							Logger::log(&packet);
						}
					}
				}
				m_processingRequest = false;
				delete request;

				/*
				 * Debugger events may have been received in response to the request that was
				 * just processed.  These events can be sent now that processing of the request
				 * is complete.
				 */
				if (m_pendingEvents->size() > 0) {

					// TODO HACK! helps JSDT with stepping
					Sleep(500);

					std::vector<CrossfireEvent*>::iterator iterator = m_pendingEvents->begin();
					while (iterator != m_pendingEvents->end()) {
						sendEvent(*iterator);
						delete *iterator;
						iterator++;
					}
					m_pendingEvents->clear();
				}
			}
		}
	} while (packet.length() > 0 && m_inProgressPacket->length() > 0);
}

HRESULT STDMETHODCALLTYPE CrossfireServer::registerContext(DWORD processId, OLECHAR* href) {
	if (m_port == -1) {
		return S_FALSE;
	}

	static int s_contextCounter = 0;

	CrossfireContext* context = NULL;
	std::map<DWORD, CrossfireContext*>::iterator iterator = m_contexts->find(processId);
	if (iterator == m_contexts->end()) {
		context = new CrossfireContext(processId, this);
		m_contexts->insert(std::pair<DWORD,CrossfireContext*>(processId, context));
	} else {
		Logger::error("CrossfireServer.registerContext(): a context already exists for process", processId);
		return S_FALSE;
	}

	std::wstringstream stream;
	stream << CONTEXTID_PREAMBLE;
	stream << processId;
	stream << "-";
	stream << s_contextCounter++;
	context->setName((wchar_t*)stream.str().c_str());
	context->setUrl(href);
	return S_OK;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::removeListener(ICrossfireServerListener* listener) {
	// TODO
	return S_OK;
}

void CrossfireServer::reset() {
	m_connection->close();
	delete m_connection;
	m_connection = NULL;

	std::map<DWORD,CrossfireContext*>::iterator iterator = m_contexts->begin();
	while (iterator != m_contexts->end()) {
		delete iterator->second;
		iterator++;
	}
	m_contexts->clear();
	m_currentContext = NULL;
	m_handshakeReceived = false;
	m_inProgressPacket->clear();
	m_lastRequestSeq = -1;
	m_port = -1;
}

void CrossfireServer::sendEvent(CrossfireEvent* eventObj) {
	/*
	 * If a request is being processed then events to be sent to
	 * the client should be queued and sent after the response to
	 * the request has been sent first.
	 */
	if (m_processingRequest) {
		CrossfireEvent* copy = NULL;
		eventObj->clone((CrossfirePacket**)&copy);
		m_pendingEvents->push_back(copy);
		return;
	}

	std::wstring* string = NULL;
	if (!m_processor->createEventPacket(eventObj, &string)) {
		Logger::error("CrossfireServer.sendEvent(): Invalid event packet, not sending it");
		return;
	}

	m_connection->send(string->c_str());
	delete string;
}

void CrossfireServer::sendResponse(CrossfireResponse* response) {
	std::wstring* string = NULL;
	if (!m_processor->createResponsePacket(response, &string)) {
		Logger::error("CrossfireServer.sendResponse(): Invalid response packet, not sending it");
		return;
	}
	m_connection->send(string->c_str());
	delete string;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::setCurrentContext(DWORD processId) {
	if (m_port == -1) {
		return S_FALSE;
	}

	std::map<DWORD, CrossfireContext*>::iterator iterator = m_contexts->find(processId);
	if (iterator == m_contexts->end()) {
		Logger::error("CrossfireServer.setCurrentContext(): unknown context", processId);
		return S_FALSE;
	}
	CrossfireContext* context = iterator->second;
	CrossfireContext* oldContext = m_currentContext;
	m_currentContext = context;
	eventContextChanged(context, oldContext);
	return S_OK;
}

void CrossfireServer::setWindowHandle(unsigned long value) {
	m_windowHandle = value;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::start(unsigned int port, unsigned int debugPort) {
	if (m_connection) {
		return S_FALSE;
	}

	m_connection = new WindowsSocketConnection(this);
	if (!m_connection->init(port)) {
		delete m_connection;
		return S_FALSE;
	}
	m_port = port;
	if (m_connection->acceptConnection()) {
		std::vector<ICrossfireServerListener*>::iterator iterator = m_listeners->begin();
		while (iterator != m_listeners->end()) {
			(*iterator)->ServerStateChanged(STATE_LISTENING, m_port);
			iterator++;
		}
	}
	return S_OK;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::stop() {
	if (!m_connection) {
		return S_FALSE;
	}
	if (m_connection->isConnected()) {
		eventClosed();
	}
	reset();

	std::vector<ICrossfireServerListener*>::iterator iterator = m_listeners->begin();
	while (iterator != m_listeners->end()) {
		(*iterator)->ServerStateChanged(STATE_DISCONNECTED, m_port);
		iterator++;
	}

	return S_OK;
}

/* commands */

bool CrossfireServer::commandListContexts(Value* arguments, Value** _responseBody) {
	Value contexts;
	contexts.setType(TYPE_ARRAY);
	std::map<DWORD,CrossfireContext*>::iterator iterator = m_contexts->begin();
	while (iterator != m_contexts->end()) {
		CrossfireContext* context = iterator->second;
		Value value_context;
		value_context.addObjectValue(/*KEY_CONTEXTID*/L"context_id", &Value(context->getName()));
		value_context.addObjectValue(KEY_HREF, &Value(context->getUrl()));
		value_context.addObjectValue(KEY_CURRENT, &Value((bool)(context == m_currentContext)));
		contexts.addArrayValue(&value_context);
		iterator++;
	}

	Value* result = new Value();
	result->addObjectValue(KEY_CONTEXTS, &contexts);
	*_responseBody = result;
	return true;
}

bool CrossfireServer::commandVersion(Value* arguments, Value** _responseBody) {
	Value* result = new Value();
	result->addObjectValue(KEY_VERSION, &Value(VERSION_STRING));
	*_responseBody = result;
	return true;
}

/* events */

void CrossfireServer::eventClosed() {
	CrossfireEvent eventObj;
	eventObj.setName(EVENT_CLOSED);
	sendEvent(&eventObj);
}

void CrossfireServer::eventContextChanged(CrossfireContext* newContext, CrossfireContext* oldContext) {
	CrossfireEvent eventObj;
	eventObj.setName(EVENT_CONTEXTCHANGED);
	eventObj.setContextId(&std::wstring(newContext->getName()));
	Value data;
	data.addObjectValue(KEY_NEWHREF, &Value(&std::wstring(newContext->getUrl())));
	if (oldContext) {
		data.addObjectValue(KEY_HREF, &Value(&std::wstring(oldContext->getUrl())));
	}
	eventObj.setData(&data);
	sendEvent(&eventObj);
}

void CrossfireServer::eventContextCreated(CrossfireContext* context) {
	CrossfireEvent eventObj;
	eventObj.setName(EVENT_CONTEXTCREATED);
	eventObj.setContextId(&std::wstring(context->getName()));
	Value data;
	data.addObjectValue(KEY_HREF, &Value(&std::wstring(context->getUrl())));
	eventObj.setData(&data);
	sendEvent(&eventObj);
}

void CrossfireServer::eventContextDestroyed(CrossfireContext* context) {
	CrossfireEvent eventObj;
	eventObj.setName(EVENT_CONTEXTDESTROYED);
	eventObj.setContextId(&std::wstring(context->getName()));
	sendEvent(&eventObj);
}

void CrossfireServer::eventContextLoaded(CrossfireContext* context) {
	CrossfireEvent eventObj;
	eventObj.setName(EVENT_CONTEXTLOADED);
	eventObj.setContextId(&std::wstring(context->getName()));
	Value data;
	data.addObjectValue(KEY_HREF, &Value(&std::wstring(context->getUrl())));
	eventObj.setData(&data);
	sendEvent(&eventObj);
}
