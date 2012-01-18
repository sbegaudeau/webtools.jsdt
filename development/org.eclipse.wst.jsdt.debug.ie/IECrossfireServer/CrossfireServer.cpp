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


#include "stdafx.h"
#include "CrossfireServer.h"

/* initialize constants */
const wchar_t* CrossfireServer::ABOUT_BLANK = L"about:blank";
const UINT CrossfireServer::ServerStateChangeMsg = RegisterWindowMessage(L"IECrossfireServerStateChanged");
const wchar_t* CrossfireServer::WindowClass = L"_IECrossfireServer";

const wchar_t* CrossfireServer::HANDSHAKE = L"CrossfireHandshake\r\n";
const wchar_t* CrossfireServer::HEADER_CONTENTLENGTH = L"Content-Length:";
const wchar_t* CrossfireServer::LINEBREAK = L"\r\n";
const size_t CrossfireServer::LINEBREAK_LENGTH = 2;

const wchar_t* CrossfireServer::COMMAND_CHANGEBREAKPOINTS = L"changeBreakpoints";
const wchar_t* CrossfireServer::COMMAND_DELETEBREAKPOINTS = L"deleteBreakpoints";
const wchar_t* CrossfireServer::COMMAND_GETBREAKPOINTS = L"getBreakpoints";
const wchar_t* CrossfireServer::COMMAND_SETBREAKPOINTS = L"setBreakpoints";

/* command: createContext */
const wchar_t* CrossfireServer::COMMAND_CREATECONTEXT = L"createContext";

/* command: disableTools */
const wchar_t* CrossfireServer::COMMAND_DISABLETOOLS = L"disableTools";

/* command: enableTools */
const wchar_t* CrossfireServer::COMMAND_ENABLETOOLS = L"enableTools";

/* command: getTools */
const wchar_t* CrossfireServer::COMMAND_GETTOOLS = L"getTools";

/* command: listContexts */
const wchar_t* CrossfireServer::COMMAND_LISTCONTEXTS = L"listContexts";
const wchar_t* CrossfireServer::KEY_CONTEXTS = L"contexts";
const wchar_t* CrossfireServer::KEY_CURRENT = L"current";

/* command: version */
const wchar_t* CrossfireServer::COMMAND_VERSION = L"version";
const wchar_t* CrossfireServer::KEY_VERSION = L"version";
const wchar_t* CrossfireServer::VERSION_STRING = L"0.3a10";

/* event: closed */
const wchar_t* CrossfireServer::EVENT_CLOSED = L"closed";

/* event: onContextCreated */
const wchar_t* CrossfireServer::EVENT_CONTEXTCREATED = L"onContextCreated";

/* event: onContextDestroyed */
const wchar_t* CrossfireServer::EVENT_CONTEXTDESTROYED = L"onContextDestroyed";

/* event: onContextLoaded */
const wchar_t* CrossfireServer::EVENT_CONTEXTLOADED = L"onContextLoaded";

/* event: onContextSelected */
const wchar_t* CrossfireServer::EVENT_CONTEXTSELECTED = L"onContextSelected";
const wchar_t* CrossfireServer::KEY_OLDCONTEXTID = L"oldContextId";
const wchar_t* CrossfireServer::KEY_OLDURL = L"oldUrl";

/* shared */
const wchar_t* CrossfireServer::KEY_CONTEXTID = L"contextId";
const wchar_t* CrossfireServer::KEY_TOOLS = L"tools";
const wchar_t* CrossfireServer::KEY_URL = L"url";


CrossfireServer::CrossfireServer() {
	m_bpManager = new CrossfireBPManager();
	m_connection = NULL;
	m_connectionWarningShown = false;
	m_contexts = new std::map<DWORD, CrossfireContext*>;
	m_currentContextPID = 0;
	m_handshakeReceived = false;
	m_inProgressPacket = new std::wstring;
	m_lastRequestSeq = -1;
	m_browsers = new std::map<DWORD, IBrowserContext*>;
	m_pendingEvents = new std::vector<CrossfireEvent*>;
	m_port = -1;
	m_processingRequest = false;
	m_processor = new CrossfireProcessor();
	m_windowHandle = 0;

	/* create a message-only window to help clients detect the server's presence */
	HINSTANCE module = GetModuleHandle(NULL);
	WNDCLASS ex;
	ex.style = 0;
	ex.lpfnWndProc = WndProc;
	ex.cbClsExtra = 0;
	ex.cbWndExtra = 0;
	ex.hInstance = module;
	ex.hIcon = NULL;
	ex.hCursor = NULL;
	ex.hbrBackground = NULL;
	ex.lpszMenuName = NULL;
	ex.lpszClassName = WindowClass;
	RegisterClass(&ex);
	m_messageWindow = CreateWindow(WindowClass, NULL, 0, 0, 0, 0, 0, HWND_MESSAGE, NULL, module, NULL);
}

CrossfireServer::~CrossfireServer() {
	delete m_bpManager;

	std::map<DWORD,CrossfireContext*>::iterator iterator = m_contexts->begin();
	while (iterator != m_contexts->end()) {
		delete iterator->second;
		iterator++;
	}
	delete m_contexts;

	std::map<DWORD, IBrowserContext*>::iterator iterator2 = m_browsers->begin();
	while (iterator2 != m_browsers->end()) {
		iterator2->second->Release();
		iterator2++;
	}
	delete m_browsers;

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

	if (m_messageWindow) {
		DestroyWindow(m_messageWindow);
		UnregisterClass(WindowClass, GetModuleHandle(NULL));
	}

//	if (m_windowHandle) {
		CComPtr<ICrossfireServerClass> serverClass = NULL;
		HRESULT hr = CoGetClassObject(CLSID_CrossfireServer, CLSCTX_ALL, 0, IID_ICrossfireServerClass, (LPVOID*)&serverClass);
		if (FAILED(hr)) {
			Logger::error("~CrossfireServer: CoGetClassObject() failed", hr);
			return;
		}

		hr = serverClass->RemoveServer(/*m_windowHandle*/ 0);
		if (FAILED(hr)) {
			Logger::error("~CrossfireServer: RemoveServer() failed", hr);
			return;
		}
//	}
}


/* ICrossfireServer */

HRESULT STDMETHODCALLTYPE CrossfireServer::contextCreated(DWORD processId, DWORD threadId, OLECHAR* url) {
	if (!isConnected()) {
		return S_FALSE;
	}

	/*
	 * When navigating from one page to another, a script node is initialized for the
	 * new page before notification of the navigation is received.  As a result this
	 * first script node for the new page becomes associated with the context of the
	 * old page.  The workaround for this is to detect the case of a page re-navigation
	 * within a given process and copy the last initialized script node to the newly-
	 * created context.
	 */
	IDebugApplicationNode* scriptNode = NULL;

	/* If there's already a context for the specified processId then destroy it */
	std::map<DWORD, CrossfireContext*>::iterator iterator = m_contexts->find(processId);
	if (iterator != m_contexts->end()) {
		scriptNode = iterator->second->getLastInitializedScriptNode();
		if (scriptNode) {
			scriptNode->AddRef();
		}
		contextDestroyed(processId);
	}

	CrossfireContext* context = new CrossfireContext(processId, threadId, url, this);
	m_contexts->insert(std::pair<DWORD,CrossfireContext*> (processId, context));
	if (scriptNode) {
		context->scriptInitialized(scriptNode, true);
		scriptNode->Release();
	}
	eventContextCreated(context);

	/*
	 * Attempt to detect the case of the user not launching IE as the Administrator user, and
	 * display an error message if appropriate.  Do not show the error more than once, and do
	 * not show it for the about:blank page, because IE's initial about:blank page fails to
	 * provide a remote debug thread even when IE is launched as the Administrator user.
	 */
	if (!m_connectionWarningShown && wcscmp(url, ABOUT_BLANK) != 0) {
		CComPtr<IRemoteDebugApplication> application = NULL;
		if (!context->getDebugApplication(&application)) {
			DWORD processId = context->getProcessId();
			IBrowserContext* listener = m_browsers->at(processId);
			if (!listener) {
				Logger::error("CrossfireServer.contextCreated(): the specified processId is not listening to the server");
			} else {
				if (SUCCEEDED(listener->displayMessage(L"Crossfire Server for Internet Explorer failed to connect to the loaded page, so this page and possibly subsequent ones will not be debuggable.  A common cause of this problem is launching Internet Explorer as a user other than the Administrator."))) {
					m_connectionWarningShown = true;
				}
			}
		}
	}

	return S_OK;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::contextDestroyed(DWORD processId) {
	if (!isConnected()) {
		return S_FALSE;
	}

	std::map<DWORD, CrossfireContext*>::iterator iterator = m_contexts->find(processId);
	if (iterator == m_contexts->end()) {
		Logger::log("CrossfireServer.contextDestroyed(): unknown context", processId);
		return S_FALSE;
	}
	CrossfireContext* context = iterator->second;
	eventContextDestroyed(context);
	m_contexts->erase(iterator);
	delete context;
	return S_OK;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::contextLoaded(DWORD processId) {
	if (!isConnected()) {
		return S_FALSE;
	}

	std::map<DWORD, CrossfireContext*>::iterator iterator = m_contexts->find(processId);
	if (iterator == m_contexts->end()) {
		Logger::log("CrossfireServer.contextLoaded(): unknown context", processId);
		return S_FALSE;
	}
	CrossfireContext* context = iterator->second;
	eventContextLoaded(context);
	return S_OK;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::getPort(unsigned int* value) {
	*value = m_port;
	return S_OK;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::getState(int* value) {
	if (m_connection == NULL) {
		*value = STATE_DISCONNECTED;
	} else {
		*value = m_connection->isConnected() ? STATE_CONNECTED : STATE_LISTENING;
	}
	return S_OK;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::registerBrowser(DWORD processId, IBrowserContext* browser) {
	if (!isConnected()) {
		return S_FALSE;
	}

	browser->AddRef();
	m_browsers->insert(std::pair<DWORD,IBrowserContext*>(processId, browser));
	return S_OK;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::removeBrowser(DWORD processId) {
	if (!isConnected()) {
		return S_FALSE;
	}

	std::map<DWORD,IBrowserContext*>::iterator iterator = m_browsers->find(processId);
	if (iterator == m_browsers->end()) {
		Logger::error("CrossfireServer.removeBrowser(): browser not registered", processId);
		return S_FALSE;
	}
	iterator->second->Release();
	m_browsers->erase(iterator);
	return S_OK;
}

HRESULT STDMETHODCALLTYPE CrossfireServer::setCurrentContext(DWORD processId) {
	if (!isConnected()) {
		return S_FALSE;
	}

	if (processId == m_currentContextPID) {
		return S_OK; /* nothing to do */
	}

	std::map<DWORD, CrossfireContext*>::iterator iterator = m_contexts->find(processId);
	if (iterator == m_contexts->end()) {
		Logger::error("CrossfireServer.setCurrentContext(): unknown context", processId);
		return S_FALSE;
	}

	if (m_currentContextPID) {
		CrossfireContext* newContext = iterator->second;
		iterator = m_contexts->find(m_currentContextPID);
		if (iterator == m_contexts->end()) {
			Logger::error("CrossfireServer.setCurrentContext(): unknown old context", m_currentContextPID);
		} else {
			CrossfireContext* oldContext = iterator->second;
			eventContextSelected(newContext, oldContext);
		}
	}

	m_currentContextPID = processId;
	return S_OK;
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
		HWND current = FindWindowEx(HWND_MESSAGE, NULL, NULL, NULL);
		while (current) {
			PostMessage(current, ServerStateChangeMsg, STATE_LISTENING, m_port);
			current = FindWindowEx(HWND_MESSAGE, current, NULL, NULL);
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

	HWND current = FindWindowEx(HWND_MESSAGE, NULL, NULL, NULL);
	while (current) {
		PostMessage(current, ServerStateChangeMsg, STATE_DISCONNECTED, 0);
		current = FindWindowEx(HWND_MESSAGE, current, NULL, NULL);
	}

	return S_OK;
}


/* CrossfireServer */

void CrossfireServer::connected() {
	HWND current = FindWindowEx(HWND_MESSAGE, NULL, NULL, NULL);
	while (current) {
		PostMessage(current, ServerStateChangeMsg, STATE_CONNECTED, m_port);
		current = FindWindowEx(HWND_MESSAGE, current, NULL, NULL);
	}
}

void CrossfireServer::disconnected() {
	reset();
	HWND current = FindWindowEx(HWND_MESSAGE, NULL, NULL, NULL);
	while (current) {
		PostMessage(current, ServerStateChangeMsg, STATE_DISCONNECTED, 0);
		current = FindWindowEx(HWND_MESSAGE, current, NULL, NULL);
	}
}

CrossfireBPManager* CrossfireServer::getBreakpointManager() {
	return m_bpManager;
}

CrossfireContext* CrossfireServer::getContext(wchar_t* contextId) {
	std::map<DWORD,CrossfireContext*>::iterator iterator = m_contexts->begin();
	while (iterator != m_contexts->end()) {
		if (wcscmp(iterator->second->getName(), contextId) == 0) {
			return iterator->second;
		}
		iterator++;
	}
	return NULL;
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

CrossfireContext* CrossfireServer::getRequestContext(CrossfireRequest* request) {
	std::wstring* contextId = request->getContextId();
	if (!contextId) {
		return NULL;
	}

	wchar_t* searchString = (wchar_t*)contextId->c_str();
	return getContext(searchString);
}

bool CrossfireServer::isConnected() {
	int state;
	getState(&state);
	return state == STATE_CONNECTED;
}

bool CrossfireServer::performRequest(CrossfireRequest* request) {
	wchar_t* command = request->getName();
	Value* arguments = request->getArguments();
	Value* responseBody = NULL;
	wchar_t* message = NULL;
	int code = CODE_OK;

	if (wcscmp(command, COMMAND_CREATECONTEXT) == 0) {
		code = commandCreateContext(arguments, &responseBody, &message);
	} else if (wcscmp(command, COMMAND_DISABLETOOLS) == 0) {
		code = commandDisableTools(arguments, &responseBody, &message);
	} else if (wcscmp(command, COMMAND_ENABLETOOLS) == 0) {
		code = commandEnableTools(arguments, &responseBody, &message);
	} else if (wcscmp(command, COMMAND_GETTOOLS) == 0) {
		code = commandGetTools(arguments, &responseBody, &message);
	} else if (wcscmp(command, COMMAND_LISTCONTEXTS) == 0) {
		code = commandListContexts(arguments, &responseBody, &message);
	} else if (wcscmp(command, COMMAND_VERSION) == 0) {
		code = commandVersion(arguments, &responseBody, &message);
	} else if (wcscmp(command, COMMAND_CHANGEBREAKPOINTS) == 0) {
		CrossfireContext* context = getRequestContext(request);
		CrossfireContext** contexts = NULL;
		if (context) {
			getContextsArray(&contexts);
		}
		code = m_bpManager->commandChangeBreakpoints(arguments, (IBreakpointTarget**)contexts, &responseBody, &message);
		if (contexts) {
			delete contexts;
		}
	} else if (wcscmp(command, COMMAND_DELETEBREAKPOINTS) == 0) {
		CrossfireContext* context = getRequestContext(request);
		CrossfireContext** contexts = NULL;
		if (context) {
			getContextsArray(&contexts);
		}
		code = m_bpManager->commandDeleteBreakpoints(arguments, (IBreakpointTarget**)contexts, &responseBody, &message);
		if (contexts) {
			delete contexts;
		}
	} else if (wcscmp(command, COMMAND_GETBREAKPOINTS) == 0) {
		CrossfireContext* context = getRequestContext(request);
		IBreakpointTarget* target = context ? (IBreakpointTarget*)context : (IBreakpointTarget*)m_bpManager;
		code = m_bpManager->commandGetBreakpoints(arguments, target, &responseBody, &message);
	} else if (wcscmp(command, COMMAND_SETBREAKPOINTS) == 0) {
		CrossfireContext* context = getRequestContext(request);
		CrossfireContext** contexts = NULL;
		if (context) {
			getContextsArray(&contexts);
		}
		code = m_bpManager->commandSetBreakpoints(arguments, (IBreakpointTarget**)contexts, &responseBody, &message);
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
	response.setCode(code);
	response.setMessage(message);
	if (message) {
		free(message);
	}
	if (code == CODE_OK) {
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

	std::wstring tools = string.substr(start, index - start);
	m_handshakeReceived = true;

	std::wstring handshake(HANDSHAKE);
	/* for now don't claim support for any tools */
	handshake.append(std::wstring(L"\r\n"));
	m_connection->send(handshake.c_str());

	/*
	* Some events may have been queued in the interval between the initial connection
	* and the handshake.  These events can be sent now that the handshake is complete.
	*/
	sendPendingEvents();

	return true;
}

void CrossfireServer::sendPendingEvents() {
	if (m_pendingEvents->size() > 0) {
		Sleep(500); // TODO HACK!
		std::vector<CrossfireEvent*>::iterator iterator = m_pendingEvents->begin();
		while (iterator != m_pendingEvents->end()) {
			sendEvent(*iterator);
			delete *iterator;
			iterator++;
		}
		m_pendingEvents->clear();
	}
}

void CrossfireServer::received(wchar_t* msg) {
	if (!m_handshakeReceived) {
		if (wcsncmp(msg, HANDSHAKE, wcslen(HANDSHAKE)) == 0) {
			processHandshake(msg);
			m_lastRequestSeq = -1;
		} else {
			Logger::error("Crossfire content received before handshake, not processing it");
		}
		return;
	}

	m_inProgressPacket->append(std::wstring(msg));
	std::wstring packet;
	do {
		wchar_t* parseErrorMessage = NULL;
		bool errorMessageRequiresFree = false;
		int code = CODE_OK;
		packet.clear();

		if (m_inProgressPacket->find(HEADER_CONTENTLENGTH) != 0) {
			code = CODE_MALFORMED_PACKET;
			parseErrorMessage = L"request packet does not start with 'Content-Length:', not processing it";
			m_inProgressPacket->clear();
			break;
		}

		size_t endIndex = m_inProgressPacket->find(wchar_t('\r'));
		if (endIndex == std::wstring::npos) {
			code = CODE_MALFORMED_PACKET;
			parseErrorMessage = L"request packet does not contain '\r', not processing it";
			m_inProgressPacket->clear();
			break;
		}

		size_t headerLength = wcslen(HEADER_CONTENTLENGTH);
		std::wstring lengthString = m_inProgressPacket->substr(headerLength, endIndex - headerLength);
		int lengthValue = _wtoi(lengthString.c_str());
		if (!lengthValue) {
			code = CODE_MALFORMED_PACKET;
			parseErrorMessage = L"request packet does not have a valid 'Content-Length' value, not processing it";
			m_inProgressPacket->clear();
			break;
		}

		if (m_inProgressPacket->find(L"\r\n", endIndex) != endIndex) {
			code = CODE_MALFORMED_PACKET;
			parseErrorMessage = L"request packet does not follow initial '\\r' with '\\n', not processing it";
			m_inProgressPacket->clear();
			break;
		}

		// TODO for now just skip over "tool:" lines, though these should really be validated

		size_t toolEnd = m_inProgressPacket->find(L"\r\n\r\n", endIndex);
		if (toolEnd == std::wstring::npos) {
			code = CODE_MALFORMED_PACKET;
			parseErrorMessage = L"request packet does not contain '\\r\\n\\r\\n' to delimit its header, not processing it";
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
			code = m_processor->parseRequestPacket(&packet, &request, &parseErrorMessage);
			if (code != CODE_OK) {
				errorMessageRequiresFree = true;
			} else {
				unsigned int seq = request->getSeq();
				if (!(m_lastRequestSeq == -1 || seq == m_lastRequestSeq + 1)) {
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
					} else {
						if (!context->performRequest(request)) {
							Logger::error("request command was unknown to the server and to the specified context, not processing it");
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
				sendPendingEvents();
			}
		}

		if (code) {
			CrossfireResponse response;
			response.setCode(CODE_MALFORMED_PACKET);
			response.setMessage(parseErrorMessage);
			if (errorMessageRequiresFree) {
				free(parseErrorMessage);
			}
			Value emptyBody;
			emptyBody.setType(TYPE_OBJECT);
			response.setBody(&emptyBody);
			sendResponse(&response);
		}
	} while (packet.length() > 0 && m_inProgressPacket->length() > 0);
}

void CrossfireServer::reset() {
	delete m_bpManager;
	m_bpManager = new CrossfireBPManager();

	std::map<DWORD, IBrowserContext*>::iterator iterator = m_browsers->begin();
	while (iterator != m_browsers->end()) {
		iterator->second->Release();
		iterator++;
	}
	m_browsers->clear();

	m_connection->close();
	delete m_connection;
	m_connection = NULL;

	std::map<DWORD, CrossfireContext*>::iterator iterator2 = m_contexts->begin();
	while (iterator2 != m_contexts->end()) {
		delete iterator2->second;
		iterator2++;
	}
	m_contexts->clear();

	std::vector<CrossfireEvent*>::iterator iterator3 = m_pendingEvents->begin();
	while (iterator3 != m_pendingEvents->end()) {
		delete *iterator3;
		iterator3++;
	}
	m_pendingEvents->clear();

	m_currentContextPID = 0;
	m_handshakeReceived = false;
	m_inProgressPacket->clear();
	m_lastRequestSeq = -1;
	m_port = -1;
	m_processingRequest = false;
}

void CrossfireServer::sendEvent(CrossfireEvent* eventObj) {
	/*
	 * If a request is being processed, or if the client handshake has not
	 * been received yet, then events to be sent to the client should be
	 * queued and sent after these conditions have passed.
	 */
	if (m_processingRequest || !m_handshakeReceived) {
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

void CrossfireServer::setWindowHandle(unsigned long value) {
	m_windowHandle = value;
}

LRESULT CALLBACK CrossfireServer::WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam) {
	return DefWindowProc(hWnd, message, wParam, lParam);
}

/* commands */

int CrossfireServer::commandCreateContext(Value* arguments, Value** _responseBody, wchar_t** _message) {
	Value* value_url = arguments->getObjectValue(KEY_URL);
	if (!value_url || value_url->getType() != TYPE_STRING) {
		*_message = _wcsdup(L"'createContext' request does not have a valid 'url' value");
		return CODE_INVALID_ARGUMENT;
	}
	std::wstring* url = value_url->getStringValue();

	std::wstring* contextId = NULL;
	Value* value_contextId = arguments->getObjectValue(KEY_CONTEXTID);
	if (value_contextId) {
		int type = value_contextId->getType();
		if (type != TYPE_NULL) {
			if (type != TYPE_STRING) {
				*_message = _wcsdup(L"'createContext' request has an invalid 'contextId' value");
				return CODE_INVALID_ARGUMENT;
			}
			contextId = value_contextId->getStringValue();
		}
	}

	CrossfireContext* context = NULL;
	if (contextId) {
		context = getContext((wchar_t*)contextId->c_str());
		if (!context) {
			*_message = _wcsdup(L"'createContext' request specified an unknown 'contextId' value");
			return CODE_COMMAND_FAILED;
		}
		DWORD processId = context->getProcessId();
		IBrowserContext* listener = m_browsers->at(processId);
		if (!listener) {
			Logger::error("commandCreateContext(): the specified processId is not listening to the server");
			return CODE_UNEXPECTED_EXCEPTION;
		}
		if (FAILED(listener->navigate((OLECHAR*)url->c_str(), false))) {
			return CODE_COMMAND_FAILED;
		}
	} else {
		/* go through the listeners to find one that can create the context in a new tab */

		std::map<DWORD,IBrowserContext*>::iterator iterator = m_browsers->begin();
		while (iterator != m_browsers->end()) {
			IBrowserContext* listener = iterator->second;
			if (SUCCEEDED(listener->navigate((OLECHAR*)url->c_str(), true))) {
				break;
			}
		}
		if (iterator == m_browsers->end()) {
			/* none of the listeners could create the context successfully */
			return CODE_COMMAND_FAILED;
		}
	}

	Value* result = new Value();
	result->setType(TYPE_OBJECT);
	*_responseBody = result;
	return CODE_OK;
}

int CrossfireServer::commandDisableTools(Value* arguments, Value** _responseBody, wchar_t** _message) {
	Value* value_tools = arguments->getObjectValue(KEY_TOOLS);
	if (!value_tools || value_tools->getType() != TYPE_ARRAY) {
		*_message = _wcsdup(L"'disableTools' request does not have a valid 'tools' value");
		return CODE_INVALID_ARGUMENT;
	}

	Value** values = NULL;
	value_tools->getArrayValues(&values);
	int index = 0;
	Value* currentValue = values[index];
	while (currentValue) {
		if (currentValue->getType() != TYPE_STRING) {
			*_message = _wcsdup(L"'disableTools' request contains an invalid 'tools' value");
			delete[] values;
			return CODE_INVALID_ARGUMENT;
		}
		// TODO do something here
		currentValue = values[++index];
	}
	delete[] values;

//	Value* result = new Value();
//	*_responseBody = result;
	return CODE_COMMAND_NOT_IMPLEMENTED; // TODO implement
}

int CrossfireServer::commandEnableTools(Value* arguments, Value** _responseBody, wchar_t** _message) {
	Value* value_tools = arguments->getObjectValue(KEY_TOOLS);
	if (!value_tools || value_tools->getType() != TYPE_ARRAY) {
		*_message = _wcsdup(L"'enableTools' request does not have a valid 'tools' value");
		return CODE_INVALID_ARGUMENT;
	}

	Value** values = NULL;
	value_tools->getArrayValues(&values);
	int index = 0;
	Value* currentValue = values[index];
	while (currentValue) {
		if (currentValue->getType() != TYPE_STRING) {
			*_message = _wcsdup(L"'enableTools' request contains an invalid 'tools' value");
			delete[] values;
			return CODE_INVALID_ARGUMENT;
		}
		// TODO do something here
		currentValue = values[++index];
	}
	delete[] values;

//	Value* result = new Value();
//	result->addObjectValue(KEY_TOOLS, &toolsArray);
//	*_responseBody = result;
	return CODE_COMMAND_NOT_IMPLEMENTED; // TODO implement
}

int CrossfireServer::commandGetTools(Value* arguments, Value** _responseBody, wchar_t** _message) {
	Value* value_tools = arguments->getObjectValue(KEY_TOOLS);
	if (value_tools) {
		if (value_tools->getType() != TYPE_ARRAY) {
			*_message = _wcsdup(L"'getTools' request has an invalid 'tools' value");
			return CODE_INVALID_ARGUMENT;
		}
	}

	Value toolsArray;
	toolsArray.setType(TYPE_ARRAY);
	Value* result = new Value();
	result->addObjectValue(KEY_TOOLS, &toolsArray);
	*_responseBody = result;
	return CODE_OK;
}

int CrossfireServer::commandListContexts(Value* arguments, Value** _responseBody, wchar_t** _message) {
	Value contexts;
	contexts.setType(TYPE_ARRAY);
	std::map<DWORD,CrossfireContext*>::iterator iterator = m_contexts->begin();
	while (iterator != m_contexts->end()) {
		CrossfireContext* context = iterator->second;
		Value value_context;
		value_context.addObjectValue(KEY_CONTEXTID, &Value(context->getName()));
		value_context.addObjectValue(KEY_URL, &Value(context->getUrl()));
		value_context.addObjectValue(KEY_CURRENT, &Value((bool)(context->getProcessId() == m_currentContextPID)));
		contexts.addArrayValue(&value_context);
		iterator++;
	}

	Value* result = new Value();
	result->addObjectValue(KEY_CONTEXTS, &contexts);
	*_responseBody = result;
	return CODE_OK;
}

int CrossfireServer::commandVersion(Value* arguments, Value** _responseBody, wchar_t** _message) {
	Value* result = new Value();
	result->addObjectValue(KEY_VERSION, &Value(VERSION_STRING));
	*_responseBody = result;
	return CODE_OK;
}

/* events */

void CrossfireServer::eventClosed() {
	CrossfireEvent eventObj;
	eventObj.setName(EVENT_CLOSED);
	sendEvent(&eventObj);
}

void CrossfireServer::eventContextCreated(CrossfireContext* context) {
	CrossfireEvent eventObj;
	eventObj.setName(EVENT_CONTEXTCREATED);
	Value body;
	body.addObjectValue(KEY_URL, &Value(&std::wstring(context->getUrl())));
	body.addObjectValue(KEY_CONTEXTID, &Value(&std::wstring(context->getName())));
	eventObj.setBody(&body);
	sendEvent(&eventObj);
}

void CrossfireServer::eventContextDestroyed(CrossfireContext* context) {
	CrossfireEvent eventObj;
	eventObj.setName(EVENT_CONTEXTDESTROYED);
	Value body;
	body.addObjectValue(KEY_CONTEXTID, &Value(&std::wstring(context->getName())));
	eventObj.setBody(&body);
	sendEvent(&eventObj);
}

void CrossfireServer::eventContextLoaded(CrossfireContext* context) {
	CrossfireEvent eventObj;
	eventObj.setName(EVENT_CONTEXTLOADED);
	Value body;
	body.addObjectValue(KEY_URL, &Value(&std::wstring(context->getUrl())));
	body.addObjectValue(KEY_CONTEXTID, &Value(&std::wstring(context->getName())));
	eventObj.setBody(&body);
	sendEvent(&eventObj);
}

void CrossfireServer::eventContextSelected(CrossfireContext* context, CrossfireContext* oldContext) {
	CrossfireEvent eventObj;
	eventObj.setName(EVENT_CONTEXTSELECTED);
	Value body;
	body.addObjectValue(KEY_OLDCONTEXTID, &Value(&std::wstring(oldContext->getName())));
	body.addObjectValue(KEY_OLDURL, &Value(&std::wstring(oldContext->getUrl())));
	body.addObjectValue(KEY_CONTEXTID, &Value(&std::wstring(context->getName())));
	body.addObjectValue(KEY_URL, &Value(&std::wstring(context->getUrl())));
	eventObj.setBody(&body);
	sendEvent(&eventObj);
}
