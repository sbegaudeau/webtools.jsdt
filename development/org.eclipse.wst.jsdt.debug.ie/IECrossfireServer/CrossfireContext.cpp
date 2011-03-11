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


#include "StdAfx.h"
#include "CrossfireContext.h"

#include "JSONParser.h"

/* command: backtrace */
const wchar_t* CrossfireContext::COMMAND_BACKTRACE = L"backtrace";
const wchar_t* CrossfireContext::KEY_FRAMES = L"frames";
const wchar_t* CrossfireContext::KEY_FROMFRAME = L"fromFrame";
const wchar_t* CrossfireContext::KEY_FUNC = L"func";
const wchar_t* CrossfireContext::KEY_INDEX = L"index";
const wchar_t* CrossfireContext::KEY_TOFRAME = L"toFrame";
const wchar_t* CrossfireContext::KEY_TOTALFRAMES = L"totalFrames";

/* command: continue */
const wchar_t* CrossfireContext::COMMAND_CONTINUE = L"continue";

/* command: evaluate */
const wchar_t* CrossfireContext::COMMAND_EVALUATE = L"evaluate";
const wchar_t* CrossfireContext::KEY_EXPRESSION = L"expression";
const wchar_t* CrossfireContext::KEY_FRAME = L"frame";

/* command: frame */
const wchar_t* CrossfireContext::COMMAND_FRAME = L"frame";
const wchar_t* CrossfireContext::JSVALUE_BOOLEAN = L"Boolean";
const wchar_t* CrossfireContext::JSVALUE_FUNCTION = L"\"function\"";
const wchar_t* CrossfireContext::JSVALUE_NUMBER = L"Number";
const wchar_t* CrossfireContext::JSVALUE_NULL = L"Null";
const wchar_t* CrossfireContext::JSVALUE_STRING = L"String";
const wchar_t* CrossfireContext::JSVALUE_TRUE = L"true";
const wchar_t* CrossfireContext::JSVALUE_UNDEFINED = L"Undefined";
const wchar_t* CrossfireContext::KEY_INCLUDESCOPES = L"includeScopes";
const wchar_t* CrossfireContext::KEY_LOCALS = L"locals";
const wchar_t* CrossfireContext::KEY_NUMBER = L"number";
const wchar_t* CrossfireContext::KEY_THIS = L"this";
const wchar_t* CrossfireContext::KEY_VALUE = L"value";
const wchar_t* CrossfireContext::VALUE_BOOLEAN = L"boolean";
const wchar_t* CrossfireContext::VALUE_FUNCTION = L"function";
const wchar_t* CrossfireContext::VALUE_NUMBER = L"number";
const wchar_t* CrossfireContext::VALUE_OBJECT = L"object";
const wchar_t* CrossfireContext::VALUE_STRING = L"string";
const wchar_t* CrossfireContext::VALUE_UNDEFINED = L"undefined";

/* command: inspect */
const wchar_t* CrossfireContext::COMMAND_INSPECT = L"inspect";

/* command: lookup */
const wchar_t* CrossfireContext::COMMAND_LOOKUP = L"lookup";

/* command: scope */
const wchar_t* CrossfireContext::COMMAND_SCOPE = L"scope";

/* command: scopes */
const wchar_t* CrossfireContext::COMMAND_SCOPES = L"scopes";

/* command: script */
const wchar_t* CrossfireContext::COMMAND_SCRIPT = L"script";
const wchar_t* CrossfireContext::KEY_SOURCE = L"source";

/* command: scripts */
const wchar_t* CrossfireContext::COMMAND_SCRIPTS = L"scripts";
const wchar_t* CrossfireContext::KEY_COMPILATIONTYPE = L"compilationType";
const wchar_t* CrossfireContext::KEY_COLUMNOFFSET = L"columnOffset";
const wchar_t* CrossfireContext::KEY_ID = L"id";
const wchar_t* CrossfireContext::KEY_LINECOUNT = L"lineCount";
const wchar_t* CrossfireContext::KEY_LINEOFFSET = L"lineOffset";
const wchar_t* CrossfireContext::KEY_SCRIPT = L"script";
const wchar_t* CrossfireContext::KEY_SCRIPTS = L"scripts";
const wchar_t* CrossfireContext::KEY_SOURCESTART = L"sourceStart";
const wchar_t* CrossfireContext::KEY_SOURCELENGTH = L"sourceLength";
const wchar_t* CrossfireContext::VALUE_TOPLEVEL = L"top-level";

/* command: source */
const wchar_t* CrossfireContext::COMMAND_SOURCE = L"source";

/* command: suspend */
const wchar_t* CrossfireContext::COMMAND_SUSPEND = L"suspend";
const wchar_t* CrossfireContext::KEY_STEPACTION = L"stepaction";
const wchar_t* CrossfireContext::VALUE_IN = L"in";
const wchar_t* CrossfireContext::VALUE_NEXT = L"next";
const wchar_t* CrossfireContext::VALUE_OUT = L"out";

/* event: onScript */
const wchar_t* CrossfireContext::EVENT_ONSCRIPT = L"onScript";

/* breakpoint objects */
const wchar_t* CrossfireContext::BPTYPE_LINE = L"line";
const wchar_t* CrossfireContext::KEY_LOCATION = L"location";

/* shared */
const wchar_t* CrossfireContext::KEY_BREAKPOINT = L"breakpoint";
const wchar_t* CrossfireContext::KEY_CONTEXTID = L"contextId";
const wchar_t* CrossfireContext::KEY_HANDLE = L"handle";
const wchar_t* CrossfireContext::KEY_INCLUDESOURCE = L"includeSource";
const wchar_t* CrossfireContext::KEY_LINE = L"line";
const wchar_t* CrossfireContext::KEY_TYPE = L"type";
const wchar_t* CrossfireContext::KEY_URL = L"url";

CrossfireContext::CrossfireContext(DWORD threadId, CrossfireServer* server) {
	CComObject<IEDebugger>* result = NULL;
	HRESULT hr = CComObject<IEDebugger>::CreateInstance(&result);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext(): CoCreateInstance(IIEDebugger) failed", hr);
		return;
	}
	m_debugger = result;

	m_breakpoints = new std::map<unsigned int, CrossfireBreakpoint*>;
	m_cpcApplicationNodeEvents = 0;
	m_server = server;
	m_debugApplicationThread = NULL;
	m_debuggerHooked = false;
	m_href = NULL;
	m_name = NULL;
	m_nextObjectHandle = 1;
	m_objects = new std::map<unsigned int, JSObject*>;
	m_pendingBreakpoints = new std::vector<PendingBreakpoint*>;
	m_running = true;
	m_threadId = threadId;

	IEDebugger* ieDebugger = static_cast<IEDebugger*>(m_debugger);
	ieDebugger->setContext(this);
	hookDebugger();
}

CrossfireContext::~CrossfireContext() {
	if (m_breakpoints) {
		std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->begin();
		while (iterator != m_breakpoints->end()) {
			delete iterator->second;
			iterator++;
		}
		delete m_breakpoints;
	}

	if (m_pendingBreakpoints) {
		std::vector<PendingBreakpoint*>::iterator iterator = m_pendingBreakpoints->begin();
		while (iterator != m_pendingBreakpoints->end()) {
			(*iterator)->Release();
			iterator++;
		}
		delete m_pendingBreakpoints;
	}


	if (m_cpcApplicationNodeEvents) {
		CComPtr<IRemoteDebugApplication> debugApplication = NULL;
		if (!getDebugApplication(&debugApplication)) {
			Logger::error("~CrossfireContext(): cannot Unadvise() the root node");
		} else {
			CComPtr<IDebugApplicationNode> rootNode = NULL;
			HRESULT hr = debugApplication->GetRootNode(&rootNode);
			if (FAILED(hr)) {
				Logger::error("~CrossfireContext(): GetRootNode() failed", hr);
			} else {
				CComPtr<IConnectionPointContainer> connectionPointContainer = NULL;
				hr = rootNode->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
				if (FAILED(hr)) {
					Logger::error("~CrossfireContext(): QI(IConnectionPointContainer) failed", hr);
				} else {
					CComPtr<IConnectionPoint> connectionPoint = NULL;
					hr = connectionPointContainer->FindConnectionPoint(IID_IDebugApplicationNodeEvents, &connectionPoint);
					if (FAILED(hr)) {
						Logger::error("~CrossfireContext(): FindConnectionPoint() failed", hr);
					} else {
						hr = connectionPoint->Unadvise(m_cpcApplicationNodeEvents);
						if (FAILED(hr)) {
							Logger::error("~CrossfireContext(): Unadvise() failed", hr);
						}
					}
				}
			}
		}
	}

	if (m_href) {
		free(m_href);
	}
	if (m_name) {
		free(m_name);
	}
	if (m_objects) {
		std::map<unsigned int, JSObject*>::iterator iterator = m_objects->begin();
		while (iterator != m_objects->end()) {
			JSObject* jsObject = iterator->second;
			jsObject->debugProperty->Release();
			delete jsObject->name;
			if (jsObject->objects) {
				delete jsObject->objects;
			}
			jsObject->stackFrame->Release();
			delete jsObject;
			iterator++;
		}
		delete m_objects;
	}
	if (m_debuggerHooked && unhookDebugger()) {
		/*
		* Hooking our debugger and then unhooking it later appears to change its
		* refcount by a net value of -1 instead of the expected 0.  To work around
		* this we do not Release our debugger below if it has been hooked and unhooked,
		* even though this Release should be required in order to offset the refcount
		* that it starts with when instantiated.
		*/
	} else if (m_debugger) {
		m_debugger->Release();
	}
	if (m_debugApplicationThread) {
		m_debugApplicationThread->Release();
	}
}

bool CrossfireContext::clearBreakpoint(unsigned int handle) {
	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
	if (iterator == m_breakpoints->end()) {
		Logger::error("clearBreakpoint: unknown breakpoint handle", handle);
		return false;
	}
	CrossfireLineBreakpoint* breakpoint = (CrossfireLineBreakpoint*)iterator->second;

	CComPtr<IDebugApplicationNode> node = NULL;
	if (!findNode((wchar_t*)breakpoint->getUrl()->c_str(), NULL, &node)) {
		Logger::error("CrossfireContext.clearBreakpoint(): unknown target url");
		return false;
	}

	CComPtr<IDebugDocument> document = NULL;
	HRESULT hr = node->GetDocument(&document);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.clearBreakpoint(): GetDocument() failed", hr);
		return false;
	}

	CComPtr<IDebugDocumentText> documentText = NULL;
	hr = document->QueryInterface(IID_IDebugDocumentText,(void**)&documentText);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.clearBreakpoint(): QI(IDebugDocumentText) failed", hr);
		return false;
	}

	ULONG characterPosition = 0;
	hr = documentText->GetPositionOfLine(breakpoint->getLine() - 1, &characterPosition);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.clearBreakpoint(): GetPositionOfLine() failed [1]", hr);
		return false;
	}

	CComPtr<IDebugDocumentContext> documentContext = NULL;
	hr = documentText->GetContextOfPosition(characterPosition, 1, &documentContext);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.clearBreakpoint(): GetContextOfPosition() failed", hr);
		return false;
	}

	CComPtr<IEnumDebugCodeContexts> codeContexts = NULL;
	hr = documentContext->EnumCodeContexts(&codeContexts);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.clearBreakpoint(): EnumCodeContexts() failed", hr);
		return false;
	}

	ULONG fetched = 0;
	IDebugCodeContext* codeContext = NULL;
	hr = codeContexts->Next(1, &codeContext, &fetched);
	if (FAILED(hr) || fetched == 0) {
		Logger::error("CrossfireContext.clearBreakpoint(): Next() failed", hr);
		return false;
	}

	hr = codeContext->SetBreakPoint(BREAKPOINT_DELETED);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.clearBreakpoint(): SetBreakPoint() failed", hr);
		return false;
	}

	delete iterator->second;
	m_breakpoints->erase(iterator);
	return true;
}

bool CrossfireContext::findNode(wchar_t* name, IDebugApplicationNode* startNode, IDebugApplicationNode** _value) {
	*_value = NULL;

	if (!startNode) {
		CComPtr<IRemoteDebugApplication> application = NULL;
		if (!getDebugApplication(&application)) {
			return false;
		}

		CComPtr<IDebugApplicationNode> rootNode = NULL;
		HRESULT hr = application->GetRootNode(&startNode);
		if (FAILED(hr)) {
			Logger::error("CrossfireContext::findNode: GetRootNode() failed", hr);
			return false;
		}
	}

	CComBSTR url = NULL;
	if (SUCCEEDED(startNode->GetName(DOCUMENTNAMETYPE_URL, &url))) {
		if (wcscmp(std::wstring(url).c_str(), name) == 0) {
			startNode->AddRef();
			*_value = startNode;
			return true;
		}
	}

	CComPtr<IEnumDebugApplicationNodes> children = NULL;
	HRESULT	hr = startNode->EnumChildren(&children);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.findNode(): EnumChildren() failed", hr);
		return false;
	}

	ULONG fetched = 0;
	do {
		CComPtr<IDebugApplicationNode> current = NULL;
		hr = children->Next(1, &current, &fetched);
		if (FAILED(hr)) {
			Logger::error("CrossfireContext.findNode(): Next() failed", hr);
			continue;
		}
		if (fetched) {
			IDebugApplicationNode* node = NULL;
			if (findNode(name, current, &node)) {
				*_value = node;
				return true;
			}
		}
	} while (fetched);

	return false;
}

bool CrossfireContext::getBreakpoint(unsigned int handle, CrossfireBreakpoint** _value) {
	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
	if (iterator == m_breakpoints->end()) {
		Logger::error("getBreakpoint: unknown breakpoint handle", handle);
		return false;
	}
	CrossfireBreakpoint* breakpoint = iterator->second;
	breakpoint->clone(_value);
	return true;
}

bool CrossfireContext::getBreakpoints(CrossfireBreakpoint*** ___values) {
	size_t size = m_breakpoints->size();
	CrossfireBreakpoint** breakpoints = new CrossfireBreakpoint*[size + 1];

	std::map<unsigned int, CrossfireBreakpoint*>::iterator it = m_breakpoints->begin();
	std::map<unsigned int, CrossfireBreakpoint*>::iterator end = m_breakpoints->end();
	int index = 0;
	while (it != end) {
		it->second->clone(&breakpoints[index++]);
		it++;
	}
	breakpoints[index] = NULL;

	*___values = breakpoints;
	return true;
}

bool CrossfireContext::getDebugApplication(IRemoteDebugApplication** _value) {
	*_value = NULL;

	CComPtr<IRemoteDebugApplicationThread> thread = NULL;
	if (!getDebugApplicationThread(&thread)) {
		return false;
	}

	HRESULT hr = thread->GetApplication(_value);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.getDebugApplication: GetApplication() failed", hr);
		return false;
	}

	return true;
}

bool CrossfireContext::getDebugApplicationThread(IRemoteDebugApplicationThread** _value) {
	*_value = NULL;

	if (m_debugApplicationThread) {
		m_debugApplicationThread->AddRef();
		*_value = m_debugApplicationThread;
		return true;
	}

	CComPtr<IMachineDebugManager> mdm;
	HRESULT hr = mdm.CoCreateInstance(CLSID_MachineDebugManager, NULL, CLSCTX_ALL);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.getDebugApplicationThread(): CoCreateInstance(CLSID_MachineDebugManager) failed", hr);
		return false;
	}

	CComPtr<IEnumRemoteDebugApplications> applications;
	hr = mdm->EnumApplications(&applications);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.getDebugApplicationThread(): EnumApplications() failed", hr);
		return false;
	}

	int counter = 0;
//Logger::error("CrossfireContext.getDebugApplicationThread(): starting loop, looking for", (int)m_threadId);
	ULONG fetchedApps = 0;
	do {
		CComPtr<IRemoteDebugApplication> currentApplication;
		hr = applications->Next(1, &currentApplication, &fetchedApps);
		if (FAILED(hr)) {
			fetchedApps = 1; /* continue to try to enum more applications */
//Logger::error("CrossfireContext.getDebugApplicationThread(): Next()[1] failed, keep trying", hr);
		} else if (fetchedApps) {
			CComPtr<IEnumRemoteDebugApplicationThreads> threads;
			hr = currentApplication->EnumThreads(&threads);
//if (FAILED(hr)) {
//Logger::error("CrossfireContext.getDebugApplicationThread(): EnumThreads() failed, keep trying");
//}
			if (SUCCEEDED(hr)) {
				ULONG fetchedThreads = 0;
				do {
					CComPtr<IRemoteDebugApplicationThread> currentThread;
					hr = threads->Next(1, &currentThread, &fetchedThreads);
					if (FAILED(hr)) {
						fetchedThreads = 1; /* continue to try to enum more threads */
//Logger::error("CrossfireContext.getDebugApplicationThread(): Next()[2] failed, keep trying");
					} else if (fetchedThreads) {
						DWORD currentThreadId;
						hr = currentThread->GetSystemThreadId(&currentThreadId);
						if (FAILED(hr)) {
							Logger::error("CrossfireContext.getDebugApplicationThread(): GetSystemThreadId() failed", hr);
						} else if (m_threadId == currentThreadId) {
							CComPtr<IDebugApplicationNode> rootNode = NULL;
							hr = currentApplication->GetRootNode(&rootNode);
							if (FAILED(hr)) {
								Logger::error("CrossfireContext.getDebugApplicationThread(): GetRootNode() failed", hr);
							} else {
								CComPtr<IConnectionPointContainer> connectionPointContainer = NULL;
								hr = rootNode->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
								if (FAILED(hr)) {
									Logger::error("CrossfireContext.getDebugApplicationThread(): QI(IConnectionPointContainer) failed", hr);
								} else {
									CComPtr<IConnectionPoint> connectionPoint = NULL;
									hr = connectionPointContainer->FindConnectionPoint(IID_IDebugApplicationNodeEvents, &connectionPoint);
									if (FAILED(hr)) {
										Logger::error("CrossfireContext.getDebugApplicationThread(): FindConnectionPoint() failed", hr);
									} else {
										hr = connectionPoint->Advise(m_debugger, &m_cpcApplicationNodeEvents);
										if (FAILED(hr)) {
											Logger::error("CrossfireContext.getDebugApplicationThread(): Advise() failed", hr);
										} else {
											Logger::log("CrossfireContext.getDebugApplicationThread() found the thread and advised its root node successfully");
											m_debugApplicationThread = currentThread;
											m_debugApplicationThread->AddRef();
											break;
										}
									}
								}
							}
						}
//else {
//	Logger::error("CrossfireContext.getDebugApplicationThread(): they didn't match, got", (int)currentThreadId);
//}
					}
				} while (fetchedThreads);
			}
		}
	} while (!m_debugApplicationThread && fetchedApps);

	if (!m_debugApplicationThread) {
		Logger::log("CrossfireContext.getDebugApplicationThread() did not find the thread or did not advise its root node");
		return false;
	}

	m_debugApplicationThread->AddRef();
	*_value = m_debugApplicationThread;
	return true;
}

wchar_t* CrossfireContext::getHref() {
	return m_href;
}

wchar_t* CrossfireContext::getName() {
	return m_name;
}

bool CrossfireContext::hookDebugger() {
	if (m_debuggerHooked) {
		return true;
	}

	CComPtr<IRemoteDebugApplication> application = NULL;
	if (!getDebugApplication(&application)) {
		return false;
	}

	CComPtr<IApplicationDebugger> currentDebugger = NULL;
	HRESULT hr = application->GetDebugger(&currentDebugger);
	if (SUCCEEDED(hr) && currentDebugger) {
		Logger::log("CrossfireContext.hookDebugger(): the application already has a debugger");
		return false;
	}

	IEDebugger* ieDebugger = static_cast<IEDebugger*>(m_debugger);
	hr = application->ConnectDebugger(ieDebugger);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.hookDebugger(): ConnectDebugger() failed", hr);
		return false;
	}

	Logger::log("CrossfireContext.hookDebugger(): debugger successfully hooked");
	m_debuggerHooked = true;
	return true;
}

void CrossfireContext::installBreakpoints(std::vector<Value*>* breakpoints) {
	std::vector<Value*>::iterator iterator = breakpoints->begin();
	while (iterator != breakpoints->end()) {
		Value* current = *iterator;
		std::wstring* type = current->getObjectValue(KEY_TYPE)->getStringValue();
		if (type->compare(std::wstring(BPTYPE_LINE)) == 0) {
			Value* value_location = current->getObjectValue(KEY_LOCATION);
			Value* value_url = value_location->getObjectValue(KEY_LINE);
			if (value_url && value_url->getType() == TYPE_STRING) {

			}
		}
		iterator++;
	}
}

bool CrossfireContext::performRequest(CrossfireRequest* request) {
	if (!m_debuggerHooked) {
		hookDebugger();
	}
	wchar_t* command = request->getName();
	Value* arguments = request->getArguments();
	Value* responseBody = NULL;
	bool success = false;

	if (wcscmp(command, COMMAND_BACKTRACE) == 0) {
		success = commandBacktrace(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_CONTINUE) == 0) {
		success = commandContinue(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_EVALUATE) == 0) {
		success = commandEvaluate(arguments, request->getSeq(), &responseBody);
	} else if (wcscmp(command, COMMAND_FRAME) == 0) {
		success = commandFrame(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_LOOKUP) == 0) {
		success = commandLookup(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_SCRIPT) == 0) {
		success = commandScript(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_SCRIPTS) == 0) {
		success = commandScripts(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_SCOPE) == 0) {
		success = commandScope(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_SCOPES) == 0) {
		success = commandScopes(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_SOURCE) == 0) {
		success = commandSource(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_SUSPEND) == 0) {
		success = commandSuspend(arguments, &responseBody);
	} else if (wcscmp(command, COMMAND_INSPECT) == 0) {
		success = false;
	} else {
		return false;	/* command not handled */
	}

	CrossfireResponse response;
	response.setName(command);
	response.setRequestSeq(request->getSeq());
	response.setRunning(m_running);
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
	m_server->sendResponse(&response);
	return true;
}

bool CrossfireContext::scriptLoaded(std::wstring* url, IDebugApplicationNode *applicationNode, bool isRetry) {
	CrossfireBPManager* bpManager = m_server->getBreakpointManager();
	/*
	 * Incoming IBreakpointTarget method invocations should always be for this
	 * application node, so store it temporarily so that's it's easily accessible,
	 * rather than repeatedly looking it up for each IBreakpointTarget invocation.
	 */
	m_currentScriptNode = applicationNode;
	bpManager->setBreakpointsForScript(url, this);
	m_currentScriptNode = NULL;

	Value* script = NULL;
	if (createValueForScript(applicationNode, false, &script)) {
		CrossfireEvent onScriptEvent;
		onScriptEvent.setName(EVENT_ONSCRIPT);
		Value data;
		data.addObjectValue(KEY_SCRIPT, script);
		delete script;
		onScriptEvent.setData(&data);
		sendEvent(&onScriptEvent);
		return true;
	}

	if (isRetry) {
		return false;
	}

	/*
	 * The script's content has been loaded yet, so create a listener object
	 * that will call this method again when this script has been loaded
	 */
	CComPtr<IDebugDocument> document = NULL;
	HRESULT hr = applicationNode->GetDocument(&document);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.scriptLoaded(): GetDocument() failed", hr);
		return false;
	}

	CComObject<PendingBreakpoint>* pendingBreakpoint = NULL;
	hr = CComObject<PendingBreakpoint>::CreateInstance(&pendingBreakpoint);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.scriptLoaded(): CreateInstance(CLSID_PendingBreakpoint) failed [1]", hr);
		return false;
	}

	pendingBreakpoint->AddRef(); /* CComObject::CreateInstance gives initial ref count of 0 */
	if (pendingBreakpoint->init(applicationNode, document, this)) {
		m_pendingBreakpoints->push_back(pendingBreakpoint);
	} else {
		pendingBreakpoint->Release();
	}

	return false;
}

void CrossfireContext::sendEvent(CrossfireEvent* eventObj) {
	eventObj->setContextId(&std::wstring(m_name));
	m_server->sendEvent(eventObj);

	// TODO REMOVE!  This is done just to prevent errors from leaving IE hanging forever
	if (wcscmp(eventObj->getName(), L"onConsoleError") == 0) {
		Logger::log("sending onConsoleError, so doing auto-resume, don't forget to remove me!!!");
		CComPtr<IRemoteDebugApplicationThread> applicationThread = NULL;
		if (!getDebugApplicationThread(&applicationThread)) {
			Logger::error("failed to do auto-resume: getDebugApplicationThread() failed");
			return;
		}
		CComPtr<IRemoteDebugApplication> application = NULL;
		HRESULT hr = applicationThread->GetApplication(&application);
		if (FAILED(hr)) {
			Logger::error("failed to do auto-resume: GetApplication() failed", hr);
			return;
		}

		hr = application->ResumeFromBreakPoint(applicationThread, BREAKRESUMEACTION_CONTINUE, ERRORRESUMEACTION_SkipErrorStatement);
		if (FAILED(hr)) {
			Logger::error("failed to do auto-resume: ResumeFromBreakPoint() failed", hr);
		}
	}
	// -------------------------------------------------
}

bool CrossfireContext::setBreakpointCondition(unsigned int handle, std::wstring* condition) {
	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
	if (iterator == m_breakpoints->end()) {
		Logger::error("setBreakpointCondition: unknown breakpoint handle", handle);
		return false;
	}
	CrossfireBreakpoint* breakpoint = iterator->second;
	breakpoint->setCondition(condition);
	return true;
}

bool CrossfireContext::setBreakpointEnabled(unsigned int handle, bool enabled) {
	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
	if (iterator == m_breakpoints->end()) {
		Logger::error("setBreakpointEnabled: unknown breakpoint handle", handle);
		return false;
	}
	CrossfireLineBreakpoint* breakpoint = (CrossfireLineBreakpoint*)iterator->second;

	CComPtr<IDebugApplicationNode> node = NULL;
	if (!findNode((wchar_t*)breakpoint->getUrl()->c_str(), NULL, &node)) {
		Logger::error("CrossfireContext.setBreakpointEnabled(): unknown target url");
		return false;
	}

	CComPtr<IDebugDocument> document = NULL;
	HRESULT hr = node->GetDocument(&document);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setBreakpointEnabled(): GetDocument() failed", hr);
		return false;
	}

	CComPtr<IDebugDocumentText> documentText = NULL;
	hr = document->QueryInterface(IID_IDebugDocumentText,(void**)&documentText);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setBreakpointEnabled(): QI(IDebugDocumentText) failed", hr);
		return false;
	}

	ULONG characterPosition = 0;
	hr = documentText->GetPositionOfLine(breakpoint->getLine() - 1, &characterPosition);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setBreakpointEnabled(): GetPositionOfLine() failed [1]", hr);
		return false;
	}

	CComPtr<IDebugDocumentContext> documentContext = NULL;
	hr = documentText->GetContextOfPosition(characterPosition, 1, &documentContext);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setBreakpointEnabled(): GetContextOfPosition() failed", hr);
		return false;
	}

	CComPtr<IEnumDebugCodeContexts> codeContexts = NULL;
	hr = documentContext->EnumCodeContexts(&codeContexts);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setBreakpointEnabled(): EnumCodeContexts() failed", hr);
		return false;
	}

	ULONG fetched = 0;
	IDebugCodeContext* codeContext = NULL;
	hr = codeContexts->Next(1, &codeContext, &fetched);
	if (FAILED(hr) || fetched == 0) {
		Logger::error("CrossfireContext.setBreakpointEnabled(): Next() failed", hr);
		return false;
	}

	hr = codeContext->SetBreakPoint(enabled ? BREAKPOINT_ENABLED : BREAKPOINT_DISABLED);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setBreakpointEnabled(): SetBreakPoint() failed", hr);
		return false;
	}

	breakpoint->setEnabled(enabled);
	return true;
}

void CrossfireContext::setHref(wchar_t* value) {
	if (m_href) {
		free(m_href);
	}
	m_href = _wcsdup(value);

	if (!m_debuggerHooked) {
		hookDebugger();
	}
}

bool CrossfireContext::setLineBreakpoint(CrossfireLineBreakpoint *breakpoint, bool isRetry) {
	// TODO uncomment the following once Refreshes cause new contexts to be created

//	unsigned int handle = breakpoint->getHandle();
//	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
//	if (iterator != m_breakpoints->end()) {
//		/* this breakpoint is already set on this context */
//		return true;
//	}

	CComPtr<IDebugApplicationNode> node = NULL;
	if (m_currentScriptNode) {
		node = m_currentScriptNode;
	} else {
		if (!findNode((wchar_t*)breakpoint->getUrl()->c_str(), NULL, &node)) {
			Logger::error("CrossfireContext.setLineBreakpoint(): unknown target url");
			return false;
		}
	}

	CComPtr<IDebugDocument> document = NULL;
	HRESULT hr = node->GetDocument(&document);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setLineBreakpoint(): GetDocument() failed", hr);
		return false;
	}

	CComPtr<IDebugDocumentText> documentText = NULL;
	hr = document->QueryInterface(IID_IDebugDocumentText,(void**)&documentText);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setLineBreakpoint(): QI(IDebugDocumentText) failed", hr);
		return false;
	}

	ULONG characterPosition = 0;
	hr = documentText->GetPositionOfLine(breakpoint->getLine() - 1, &characterPosition);
	if (FAILED(hr)) {
		/*
		 * In this context E_INVALIDARG failures are often caused by the target document
		 * not being adequately loaded yet.  If this is the first attempt to set this
		 * breakpoint then create a pending breakpoint that will attempt to hook itself
		 * later if it is detected that the document load may have completed.
		 */
		if (!isRetry && hr == E_INVALIDARG) {
			CComObject<PendingBreakpoint>* pendingBreakpoint = NULL;
			HRESULT hr = CComObject<PendingBreakpoint>::CreateInstance(&pendingBreakpoint);
			if (FAILED(hr)) {
				Logger::error("CrossfireContext.setLineBreakpoint(): CreateInstance(CLSID_PendingBreakpoint) failed [1]", hr);
				return false;
			}

			pendingBreakpoint->AddRef(); /* CComObject::CreateInstance gives initial ref count of 0 */
			if (pendingBreakpoint->init(breakpoint, document, this)) {
				m_pendingBreakpoints->push_back(pendingBreakpoint);
			} else {
				pendingBreakpoint->Release();
			}
		} else {
			Logger::error("CrossfireContext.setLineBreakpoint(): GetPositionOfLine() failed [1]", hr);
		}
		return false;
	}

	CComPtr<IDebugDocumentContext> documentContext = NULL;
	hr = documentText->GetContextOfPosition(characterPosition, 1, &documentContext);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setLineBreakpoint(): GetContextOfPosition() failed", hr);
		return false;
	}

	CComPtr<IEnumDebugCodeContexts> codeContexts = NULL;
	hr = documentContext->EnumCodeContexts(&codeContexts);
	if (FAILED(hr)) {
		/*
		 * In this context E_INVALIDARG failures are often caused by the target document
		 * not being adequately loaded yet.  If this is the first attempt to set this
		 * breakpoint then create a pending breakpoint that will attempt to hook itself
		 * later if it is detected that the document load may have completed.
		 */
		if (!isRetry && hr == E_INVALIDARG) {
			CComObject<PendingBreakpoint>* pendingBreakpoint = NULL;
			HRESULT hr = CComObject<PendingBreakpoint>::CreateInstance(&pendingBreakpoint);
			if (FAILED(hr)) {
				Logger::error("CrossfireContext.setLineBreakpoint(): CreateInstance(CLSID_PendingBreakpoint) failed [2]", hr);
				return false;
			}

			pendingBreakpoint->AddRef(); /* CComObject::CreateInstance gives initial ref count of 0 */
			if (pendingBreakpoint->init(breakpoint, document, this)) {
				m_pendingBreakpoints->push_back(pendingBreakpoint);
			} else {
				pendingBreakpoint->Release();
			}
		} else {
			Logger::error("CrossfireContext.setLineBreakpoint(): EnumCodeContexts() failed", hr);
		}
		return false;
	}

	ULONG fetched = 0;
	CComPtr<IDebugCodeContext> codeContext = NULL;
	hr = codeContexts->Next(1, &codeContext, &fetched);
	if (FAILED(hr) || fetched == 0) {
		Logger::error("CrossfireContext.setLineBreakpoint(): Next() failed", hr);
		return false;
	}

	hr = codeContext->SetBreakPoint(breakpoint->isEnabled() ? BREAKPOINT_ENABLED : BREAKPOINT_DISABLED);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setLineBreakpoint(): SetBreakPoint() failed", hr);
		return false;
	}

	/* Determine the line number the breakpoint was set on (it may not match the requested line number) */

	CComPtr<IDebugDocumentContext> bpDocumentContext = NULL;
	hr = codeContext->GetDocumentContext(&bpDocumentContext);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setLineBreakpoint(): GetDocumentContext() failed", hr);
		return false;
	}

	characterPosition = 0;
	ULONG numChars = 0;
	hr = documentText->GetPositionOfContext(bpDocumentContext, &characterPosition, &numChars);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setLineBreakpoint(): GetPositionOfContext() failed", hr);
		return false;
	}

	ULONG bpLineNumber = 0;
	ULONG offset = 0;
	hr = documentText->GetLineOfPosition(characterPosition, &bpLineNumber, &offset);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.setLineBreakpoint(): GetLineOfPosition() failed", hr);
		return false;
	}

	breakpoint->setLine(bpLineNumber + 1);
	breakpoint->setContextId(&std::wstring(getName()));
	CrossfireLineBreakpoint* copy = NULL;
	breakpoint->clone((CrossfireBreakpoint**)&copy);
	m_breakpoints->insert(std::pair<unsigned int, CrossfireBreakpoint*>(breakpoint->getHandle(), copy));
	return true;
}

void CrossfireContext::setName(wchar_t* value) {
	if (m_name) {
		free(m_name);
	}
	m_name = _wcsdup(value);
}

void CrossfireContext::setRunning(bool value) {
	m_running = value;
}

bool CrossfireContext::unhookDebugger() {
	if (!m_debuggerHooked) {
		return true;
	}

	CComPtr<IRemoteDebugApplication> application = NULL;
	if (!getDebugApplication(&application)) {
		return false;
	}
	HRESULT hr = application->DisconnectDebugger();
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.unhookDebugger(): DisconnectDebugger() failed", hr);
	}
	return SUCCEEDED(hr);
}

/* commands */

bool CrossfireContext::commandBacktrace(Value* arguments, Value** _responseBody) {
	unsigned int fromFrame = 0;
	Value* value_fromFrame = arguments->getObjectValue(KEY_FROMFRAME);
	if (value_fromFrame) {
		if (value_fromFrame->getType() != TYPE_NUMBER || (unsigned int)value_fromFrame->getNumberValue() < 0) {
			Logger::error("'backtrace' command has an invalid 'fromFrame' value");
			return false;
		}
		fromFrame = (unsigned int)value_fromFrame->getNumberValue();
	}

	Value framesArray;
	framesArray.setType(TYPE_ARRAY);
	int index = 0;
	for (index = fromFrame; ; index++) {
		arguments->setObjectValue(KEY_NUMBER, &Value((double)index));
		Value* frame = NULL;
		if (!commandFrame(arguments, &frame)) {
			index--; /* decrement since this attempt was not successful */
			break;
		}
		framesArray.addArrayValue(frame);
		delete frame;
	}

	int totalFrames = index - fromFrame + 1;
	if (totalFrames == 0) {
		*_responseBody = new Value();
		(*_responseBody)->setType(TYPE_OBJECT);
		return false;
	}

	Value* result = new Value();
	result->addObjectValue(KEY_CONTEXTID, &Value(m_name));
	result->addObjectValue(KEY_FRAMES, &framesArray);
	result->addObjectValue(KEY_FROMFRAME, &Value((double)fromFrame));
	result->addObjectValue(KEY_TOFRAME, &Value((double)index));
	result->addObjectValue(KEY_TOTALFRAMES, &Value((double)totalFrames));
	*_responseBody = result;
	return true;
}

bool CrossfireContext::commandContinue(Value* arguments, Value** _responseBody) {
	BREAKRESUMEACTION action;
	Value* value_action = NULL;
	if (arguments) {
		value_action = arguments->getObjectValue(KEY_STEPACTION);
	}
	if (!value_action) {
		action = BREAKRESUMEACTION_CONTINUE;
	} else {
		if (value_action->getType() != TYPE_STRING) {
			Logger::error("'continue' command has invalid 'stepaction' value");
			return false;
		}
		std::wstring* actionString = value_action->getStringValue();
		if (actionString->compare(VALUE_IN) == 0) {
			action = BREAKRESUMEACTION_STEP_INTO;
		} else if (actionString->compare(VALUE_NEXT) == 0) {
			action = BREAKRESUMEACTION_STEP_OVER;
		} else if (actionString->compare(VALUE_OUT) == 0) {
			action = BREAKRESUMEACTION_STEP_OUT;
		} else {
			Logger::error("'continue' command has invalid 'stepaction' value");
			return false;
		}
	}

	CComPtr<IRemoteDebugApplicationThread> applicationThread = NULL;
	if (!getDebugApplicationThread(&applicationThread)) {
		return false;
	}
	CComPtr<IRemoteDebugApplication> application = NULL;
	HRESULT hr = applicationThread->GetApplication(&application);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandContinue(): GetApplication() failed", hr);
		return false;
	}

	hr = application->ResumeFromBreakPoint(applicationThread, action, ERRORRESUMEACTION_SkipErrorStatement);
	if (SUCCEEDED(hr)) {
		*_responseBody = new Value();
		(*_responseBody)->setType(TYPE_OBJECT);
		m_running = true;
	} else {
		Logger::error("CrossfireContext.commandContinue(): ResumeFromBreakPoint() failed", hr);
	}
	return SUCCEEDED(hr);
}

bool CrossfireContext::commandEvaluate(Value* arguments, unsigned int requestSeq, Value** _responseBody) {
	unsigned int frame = 0;
	Value* value_frame = arguments->getObjectValue(KEY_FRAME);
	if (value_frame) {
		if (value_frame->getType() != TYPE_NUMBER || (unsigned int)value_frame->getNumberValue() < 0) {
			Logger::error("'evaluate' command has invalid 'frame' value");
			return false;
		}
		frame = (unsigned int)value_frame->getNumberValue();
	}

	Value* value_expression = arguments->getObjectValue(KEY_EXPRESSION);
	if (!value_expression || value_expression->getType() != TYPE_STRING) {
		Logger::error("'evaluate' command does not have a valid 'expression' value");
		return false;
	}

	CComPtr<IRemoteDebugApplicationThread> applicationThread = NULL;
	if (!getDebugApplicationThread(&applicationThread)) {
		return false;
	}

	CComPtr<IEnumDebugStackFrames> stackFrames = NULL;
	HRESULT hr = applicationThread->EnumStackFrames(&stackFrames);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandEvaluate(): EnumStackFrames() failed", hr);
		return false;
	}

	if (frame > 0) {
		hr = stackFrames->Skip(frame);
		if (FAILED(hr)) {
			Logger::error("CrossfireContext.commandEvaluate(): Skip() failed", hr);
			return false;
		}
	}

	ULONG fetched = 0;
	DebugStackFrameDescriptor stackFrameDescriptor;
	hr = stackFrames->Next(1, &stackFrameDescriptor, &fetched);
	if (FAILED(hr) || fetched == 0) {
		Logger::error("CrossfireContext.commandEvaluate(): Next() failed", hr);
		return false;
	}

	IDebugStackFrame* stackFrame = stackFrameDescriptor.pdsf;
	CComPtr<IDebugExpressionContext> expressionContext = NULL;
	hr = stackFrame->QueryInterface(IID_IDebugExpressionContext, (void**)&expressionContext);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandEvaluate(): QI(IDebugExpressionContext) failed", hr);
		return false;
	}

	IDebugExpression* expression = NULL;
	hr = expressionContext->ParseLanguageText(
		value_expression->getStringValue()->c_str(),
		10,
		OLESTR(""),
		DEBUG_TEXT_ISEXPRESSION | DEBUG_TEXT_RETURNVALUE | DEBUG_TEXT_ALLOWBREAKPOINTS | DEBUG_TEXT_ALLOWERRORREPORT,
		&expression);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandEvaluate(): ParseLanguageText failed", hr);
		return false;
	}

	JSEvalCallback* callback = NULL;
	hr = CoCreateInstance(CLSID_JSEvalCallback, NULL, CLSCTX_INPROC_SERVER, IID_IJSEvalCallback, (LPVOID*)&callback);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandEvaluate(): CoCreateInstance(CLSID_JSEvalCallback) failed", hr);
		return false;
	}

	// TODO finish this, note that "expression" and "callback" need to be released by someone)
	/*
	CrossfireResponse response;
	response.setCommand(COMMAND_EVALUATE);
	response.setRequestSeq(requestSeq);
	response.setRunning(true); // TODO better way to determine this?
	response.setSuccess(true);
	//callback->init(&response, crossfire, expression);

	hr = expression->Start(static_cast<IDebugExpressionCallBack*>(callback));
	if (FAILED(hr)) {
		Logger::error("'evaluate' Start() failed", hr);
		callback->Release();
		return false;
	}
	*/
	return true;
}

bool CrossfireContext::commandFrame(Value* arguments, Value** _responseBody) {
	Value* value_includeScopes = arguments->getObjectValue(KEY_INCLUDESCOPES);
	if (!value_includeScopes || value_includeScopes->getType() != TYPE_BOOLEAN) {
		Logger::error("'frame' command does not have a valid 'includeScopes' value");
		return false;
	}
	bool includeScopes = value_includeScopes->getBooleanValue();

	unsigned int index = 0;
	Value* value_number = arguments->getObjectValue(KEY_NUMBER);
	if (value_number) {
		if (value_number->getType() != TYPE_NUMBER || (unsigned int)value_number->getNumberValue() < 0) {
			Logger::error("'frame' command has an invalid 'number' value");
			return false;
		}
		index = (unsigned int)value_number->getNumberValue();
	}

	CComPtr<IRemoteDebugApplicationThread> applicationThread = NULL;
	if (!getDebugApplicationThread(&applicationThread)) {
		return false;
	}

	CComPtr<IEnumDebugStackFrames> stackFrames = NULL;
	HRESULT hr = applicationThread->EnumStackFrames(&stackFrames);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): EnumStackFrames() failed", hr);
		return false;
	}

	if (index > 0) {
		hr = stackFrames->Skip(index);
		if (FAILED(hr)) {
			Logger::error("CrossfireContext.commandFrame(): Skip() failed", hr);
			return false;
		}
	}

	ULONG fetched = 0;
	DebugStackFrameDescriptor stackFrameDescriptor;
	hr = stackFrames->Next(1, &stackFrameDescriptor, &fetched);
	if (FAILED(hr) || fetched == 0) {
		Logger::error("CrossfireContext.commandFrame(): Next() failed", hr);
		return false;
	}

	IDebugStackFrame* stackFrame = stackFrameDescriptor.pdsf;
	CComBSTR description = NULL;
	hr = stackFrame->GetDescriptionString(true, &description);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): GetDescriptionString() failed", hr);
		return false;
	}

	/* determine the current line number */

	CComPtr<IDebugCodeContext> codeContext = NULL;
	hr = stackFrame->GetCodeContext(&codeContext);
	if (FAILED(hr)) {
		/* fails if the current position is not within a user document, which is valid */
		return false;
	}

	CComPtr<IDebugDocumentContext> documentContext = NULL;
	hr = codeContext->GetDocumentContext(&documentContext);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): GetDocumentContext() failed", hr);
		return false;
	}

	CComPtr<IDebugDocument> document = NULL;
	hr = documentContext->GetDocument(&document);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): GetDocument() failed", hr);
		return false;
	}

	CComBSTR url = NULL;
	hr = document->GetName(DOCUMENTNAMETYPE_URL, &url);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): GetName() failed", hr);
		return false;
	}

	CComPtr<IDebugDocumentText> documentText = NULL;
	hr = document->QueryInterface(IID_IDebugDocumentText, (void**)&documentText);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): QI(IDebugDocumentText) failed", hr);
		return false;
	}

	ULONG position, numChars;
	hr = documentText->GetPositionOfContext(documentContext, &position, &numChars);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): GetPositionOfContext() failed", hr);
		return false;
	}

	ULONG lineNumber, column;
	hr = documentText->GetLineOfPosition(position, &lineNumber, &column);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): GetLineOfPosition() failed", hr);
		return false;
	}

	/* get the locals */

	CComPtr<IDebugProperty> debugProperty = NULL;
	hr = stackFrame->GetDebugProperty(&debugProperty);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): GetDebugProperty() failed", hr);
		return false;
	}

	// TODO this frameObject is currently created temporarily, but to
	// get the full benefit of handle reuse it should be stored and
	// reused for cases where the frame command is invoked on the same
	// frame multiple times
	JSObject frameObject;
	frameObject.debugProperty = debugProperty;
	frameObject.isFunction = false;
	frameObject.name = NULL;
	frameObject.objects = NULL;
	frameObject.parentHandle = 0;
	frameObject.stackFrame = stackFrame;
	Value* locals = NULL;
	if (!createValueForObject(&frameObject, &locals)) {
		return false;
	}

	/* get "this" */

	CComPtr<IDebugExpressionContext> expressionContext = NULL;
	hr = stackFrame->QueryInterface(IID_IDebugExpressionContext, (void**)&expressionContext);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): QI(IDebugExpressionContext) failed", hr);
		return false;
	}

	CComPtr<IDebugExpression> expression = NULL;
	hr = expressionContext->ParseLanguageText(
		OLESTR("this"),
		10,
		L"",
		DEBUG_TEXT_RETURNVALUE,
		&expression);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): ParseLanguageText failed [2]", hr);
		return false;
	}

	hr = expression->Start(NULL);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): Start failed", hr);
		return false;
	}
Logger::error("Evaluating this");
	while (expression->QueryIsComplete() == S_FALSE) {
Logger::error("zzz");
		::Sleep(10);
	}
Logger::error("Get result for this");
	HRESULT evalResult;
	CComPtr<IDebugProperty> debugProperty2 = NULL;
	hr = expression->GetResultAsDebugProperty(&evalResult, &debugProperty2);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandFrame(): GetResultAsDebugProperty failed", hr);
		return false;
	}
	if (FAILED(evalResult)) {
		Logger::error("CrossfireContext.commandFrame(): evaluation of GetResultAsDebugProperty failed", evalResult);
		return false;
	}

	// TODO this thisObject is currently created temporarily, but to
	// get the full benefit of handle reuse it should be stored and
	// reused for cases where the frame command is invoked on the same
	// frame multiple times
	JSObject thisObject;
	thisObject.debugProperty = debugProperty2;
	thisObject.isFunction = false;
	thisObject.name = NULL;
	thisObject.objects = NULL;
	thisObject.parentHandle = 0;
	thisObject.stackFrame = stackFrame;

	Value* value_this = NULL;
	if (createValueForObject(&thisObject, &value_this)) {
		locals->addObjectValue(KEY_THIS, value_this);
		delete value_this;
	} else {
		/* create an empty "this" value */
		Value value_thisChildren;
		value_thisChildren.setType(TYPE_OBJECT);
		Value value_this2;
		value_this2.addObjectValue(KEY_TYPE, &Value(VALUE_OBJECT));
		value_this2.addObjectValue(KEY_VALUE, &value_thisChildren);
		locals->addObjectValue(KEY_THIS, &value_this2);
	}

	Value* result = new Value();
	result->addObjectValue(KEY_CONTEXTID, &Value(m_name));
	result->addObjectValue(KEY_FUNC, &Value(description));
	result->addObjectValue(KEY_INDEX, &Value((double)index));
	result->addObjectValue(KEY_LINE, &Value((double)lineNumber + 1));
	result->addObjectValue(KEY_LOCALS, locals);
	result->addObjectValue(KEY_SCRIPT, &Value(url));
	delete locals;
	*_responseBody = result;
	return true;
}

bool CrossfireContext::commandLookup(Value* arguments, Value** _responseBody) {
	Value* value_handle = arguments->getObjectValue(KEY_HANDLE);
	if (!value_handle || value_handle->getType() != TYPE_NUMBER || (unsigned int)value_handle->getNumberValue() < 1) {
		Logger::error("'lookup' command does not have a valid 'handle' value");
		return false;
	}

	bool includeSource = false;
	Value* value_includeSource = arguments->getObjectValue(KEY_INCLUDESOURCE);
	if (value_includeSource) {
		if (value_includeSource->getType() != TYPE_BOOLEAN) {
			Logger::error("'lookup' command has an invalid 'includeSource' value");
			return false;
		}
		includeSource = value_includeSource->getBooleanValue();
	}

	unsigned int handle = (unsigned int)value_handle->getNumberValue();

	std::map<unsigned int, JSObject*>::iterator iterator = m_objects->find(handle);
	if (iterator == m_objects->end()) {
		Logger::error("'lookup' handle value is not a known object handle");
		return false;
	}
	JSObject* object = iterator->second;

	Value* result = NULL;
	if (!createValueForObject(object, &result)) {
		return false;
	}

	*_responseBody = result;
	return true;
}

bool CrossfireContext::createValueForObject(JSObject* object, Value** _value) {
	*_value = NULL;
	IDebugProperty* debugProperty = object->debugProperty;

	CComPtr<IEnumDebugPropertyInfo> enumPropertyInfo = NULL;
	HRESULT hr = debugProperty->EnumMembers(
		/*DBGPROP_INFO_NAME | DBGPROP_INFO_TYPE | DBGPROP_INFO_VALUE | DBGPROP_INFO_DEBUGPROP*/ 0x3F,
		10,
		IID_IDebugPropertyEnumType_LocalsPlusArgs,
		&enumPropertyInfo);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.createValueForObject(): EnumMembers() failed", hr);
		return false;
	}

	/* The IDebugExpressionContext will be needed for each encountered object/function */
	CComPtr<IDebugExpressionContext> expressionContext = NULL;
	hr = object->stackFrame->QueryInterface(IID_IDebugExpressionContext, (void**)&expressionContext);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.createValueForObject(): QI(IDebugExpressionContext) failed", hr);
		return false;
	}

	Value membersCollection;
	membersCollection.setType(TYPE_OBJECT);

	std::map<wchar_t*, unsigned int>* newObjectsTable = NULL;
	ULONG fetched;
	do {
		DebugPropertyInfo propertyInfo;
		HRESULT hr = enumPropertyInfo->Next(1, &propertyInfo, &fetched);
		if (SUCCEEDED(hr) && fetched) {
			BSTR type = propertyInfo.m_bstrType;
			Value local;
			if (wcscmp(type, JSVALUE_NULL) == 0) {
				local.setType(TYPE_NULL);
			} else if (wcscmp(type, JSVALUE_UNDEFINED) == 0) {
				local.setValue(VALUE_UNDEFINED);
			} else {
				BSTR stringValue = propertyInfo.m_bstrValue;
				if (wcscmp(type, JSVALUE_NUMBER) == 0) {
					wchar_t* endPtr = 0;
					double value = wcstod(stringValue, &endPtr);
					local.addObjectValue(KEY_TYPE, &Value(VALUE_NUMBER));
					local.addObjectValue(KEY_VALUE, &Value(value));
				} else if (wcscmp(type, JSVALUE_BOOLEAN) == 0) {
					local.addObjectValue(KEY_TYPE, &Value(VALUE_BOOLEAN));
					if (wcscmp(stringValue, JSVALUE_TRUE) == 0) {
						local.addObjectValue(KEY_VALUE, &Value(true));
					} else {
						local.addObjectValue(KEY_VALUE, &Value(false));
					}
				} else if (wcscmp(type, JSVALUE_STRING) == 0) {
					std::wstring string(stringValue);
					string = string.substr(1, string.length() - 2);
					local.addObjectValue(KEY_TYPE, &Value(VALUE_STRING));
					local.addObjectValue(KEY_VALUE, &Value(&string));
				} else {
					bool isFunction = (propertyInfo.m_dwAttrib & /*DBGPROP_ATTRIB_VALUE_IS_EXPANDABLE*/ 0x10) == 0;
					if (!newObjectsTable) {
						newObjectsTable = new std::map<wchar_t*, unsigned int>;
					}

					wchar_t* name = propertyInfo.m_bstrName;
					unsigned int handle = 0;
					if (object->objects) {
						std::map<wchar_t*, unsigned int>::iterator iterator = object->objects->find(name);
						if (iterator != object->objects->end()) {
							std::map<unsigned int, JSObject*>::iterator iterator2 = m_objects->find(iterator->second);
							if (iterator2 != m_objects->end()) {
								JSObject* existingObject = iterator2->second;
								if (existingObject->isFunction == isFunction) {
									handle = iterator2->first;
								}
							}
						}
					}
					if (!handle) {
						JSObject* newObject = new JSObject();
						IDebugProperty* objectProperty = propertyInfo.m_pDebugProp;
						objectProperty->AddRef();
						newObject->debugProperty = objectProperty;
						newObject->isFunction = isFunction;
						newObject->name = _wcsdup(name);
						newObject->objects = NULL;
						newObject->parentHandle = 0;
						newObject->stackFrame = object->stackFrame;
						newObject->stackFrame->AddRef();
						handle = m_nextObjectHandle++;
						m_objects->insert(std::pair<unsigned int, JSObject*>(handle, newObject));
					}
					newObjectsTable->insert(std::pair<wchar_t*, unsigned int>(name, handle));
					local.setObjectValue(KEY_TYPE, isFunction ? &Value(VALUE_FUNCTION) : &Value(VALUE_OBJECT));
					local.addObjectValue(KEY_HANDLE, &Value((double)handle));
				}
			}
			membersCollection.addObjectValue(propertyInfo.m_bstrName, &local);
		}
	} while (fetched);

	if (object->objects) {
		std::map<wchar_t*, unsigned int>::iterator iterator = object->objects->begin();
		while (iterator != object->objects->end()) {
			delete iterator->first;
			iterator++;
		}
		delete object->objects;
	}
	object->objects = newObjectsTable;

	Value* result = new Value();
	result->setObjectValue(KEY_TYPE, object->isFunction ? &Value(VALUE_FUNCTION) : &Value(VALUE_OBJECT));
	result->setObjectValue(KEY_VALUE, &membersCollection);
	*_value = result;
	return true;
}

bool CrossfireContext::commandScope(Value* arguments, Value** _responseBody) {
	return false;
}

bool CrossfireContext::commandScopes(Value* arguments, Value** _responseBody) {
	return false;
}

bool CrossfireContext::commandScript(Value* arguments, Value** _responseBody) {
	Value* value_url = arguments->getObjectValue(KEY_URL);
	if (!value_url || value_url->getType() != TYPE_STRING) {
		Logger::error("'script' command does not have a valid 'url' value");
		return false;
	}
	std::wstring* url = value_url->getStringValue();

	Value* value_includeSource = arguments->getObjectValue(KEY_INCLUDESOURCE);
	if (!value_includeSource || value_includeSource->getType() != TYPE_BOOLEAN) {
		Logger::error("'script' command does not have a valid 'includeSource' value");
		return false;
	}
	bool includeSource = value_includeSource->getBooleanValue();

	CComPtr<IDebugApplicationNode> node = NULL;
	if (!findNode((wchar_t*)url->c_str(), NULL, &node)) {
		Logger::error("'script' command specified an unknown script url");
		return false;
	}

	Value* script = NULL;
	if (!createValueForScript(node, includeSource, &script)) {
		return false;
	}

	Value* result = new Value();
	result->addObjectValue(KEY_CONTEXTID, &Value(m_name));
	result->addObjectValue(KEY_SCRIPT, script);
	*_responseBody = result;
	delete script;
	return true;
}

bool CrossfireContext::createValueForScript(IDebugApplicationNode* node, bool includeSource, Value** _value) {
	*_value = NULL;

	CComBSTR url = L"NULL";
	HRESULT hr = node->GetName(DOCUMENTNAMETYPE_URL, &url);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.createValueForScript(): GetName() failed", hr);
		return false;
	}

	CComPtr<IDebugDocument> document = NULL;
	hr = node->GetDocument(&document);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.createValueForScript(): GetDocument() failed", hr);
		return false;
	}

	CComPtr<IDebugDocumentText> documentText = NULL;
	hr = document->QueryInterface(IID_IDebugDocumentText,(void**)&documentText);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.createValueForScript(): QI(IDebugDocumentText) failed", hr);
		return false;
	}

	ULONG numLines = 0;
	ULONG numChars = 0;
	hr = documentText->GetSize(&numLines, &numChars);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.createValueForScript(): GetSize() failed", hr);
		return false;
	}

	ULONG line1start = 0;
	hr = documentText->GetPositionOfLine(1, &line1start);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.createValueForScript(): GetPositionOfLine() failed [2]", hr);
		return false;
	}

	wchar_t* line0chars = new wchar_t[line1start + 1];
	ULONG charsRead = 0;
	hr = documentText->GetText(0, line0chars, NULL, &charsRead, line1start);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.createValueForScript(): GetText() failed", hr);
		return false;
	}
	line0chars[line1start] = NULL;

	Value* result = new Value();
	result->addObjectValue(KEY_ID, &Value(url));
	result->addObjectValue(KEY_LINEOFFSET, &Value((double)0)); // TODO right?
	result->addObjectValue(KEY_COLUMNOFFSET, &Value((double)0)); // TODO right?
	result->addObjectValue(KEY_SOURCESTART, &Value(line0chars));
	result->addObjectValue(KEY_SOURCELENGTH, &Value((double)numLines)); // TODO wrong
	result->addObjectValue(KEY_LINECOUNT, &Value((double)numLines));
	result->addObjectValue(KEY_COMPILATIONTYPE, &Value(VALUE_TOPLEVEL)); // TODO right?
	delete[] line0chars;

	if (includeSource) {
		wchar_t* sourceChars = new wchar_t[numChars + 1];
		charsRead = 0;
		hr = documentText->GetText(0, sourceChars, NULL, &charsRead, numChars);
		if (FAILED(hr)) {
			Logger::error("CrossfireContext.createValueForScript(): GetText()[2] failed", hr);
			return false;
		}
		sourceChars[numChars] = NULL;
		result->addObjectValue(KEY_SOURCE, &Value(sourceChars));
		delete[] sourceChars;
	}

	*_value = result;
	return true;
}

bool CrossfireContext::commandScripts(Value* arguments, Value** _responseBody) {
	Value* value_includeSource = arguments->getObjectValue(KEY_INCLUDESOURCE);
	if (!value_includeSource || value_includeSource->getType() != TYPE_BOOLEAN) {
		Logger::error("'scripts' command does not have a valid 'includeSource' value");
		return false;
	}
	bool includeSource = value_includeSource->getBooleanValue();

	CComPtr<IRemoteDebugApplication> application = NULL;
	if (!getDebugApplication(&application)) {
		return false;
	}

	CComPtr<IDebugApplicationNode> rootNode = NULL;
	HRESULT hr = application->GetRootNode(&rootNode);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandScripts(): GetRootNode() failed", hr);
		return false;
	}

	Value scripts;
	scripts.setType(TYPE_ARRAY);
	addScriptsToArray(rootNode, includeSource, &scripts);

	Value* result = new Value();
	result->addObjectValue(KEY_CONTEXTID, &Value(m_name));
	result->addObjectValue(KEY_SCRIPTS, &scripts);
	*_responseBody = result;
	return true;
}

void CrossfireContext::addScriptsToArray(IDebugApplicationNode* node, bool includeSource, Value* scriptsArray) {
	Value* value_script = NULL;
	if (createValueForScript(node, includeSource, &value_script)) {
		Value script;
		script.addObjectValue(KEY_SCRIPT, value_script);
		scriptsArray->addArrayValue(&script);
		delete value_script;
	}

	CComPtr<IEnumDebugApplicationNodes> nodes = NULL;
	HRESULT hr = node->EnumChildren(&nodes);
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.addScriptsToArray(): EnumChildren() failed", hr);
		return;
	}

	ULONG fetched = 0;
	do {
		CComPtr<IDebugApplicationNode> current = NULL;
		hr = nodes->Next(1, &current, &fetched);
		if (FAILED(hr)) {
			Logger::error("CrossfireContext.addScriptsToArray(): Next() failed", hr);
		} else {
			if (fetched) {
				addScriptsToArray(current, includeSource, scriptsArray);
			}
		}
	} while (fetched);
}

bool CrossfireContext::commandSource(Value* arguments, Value** _responseBody) {
	arguments->setObjectValue(KEY_INCLUDESOURCE, &Value(true));
	return commandScripts(arguments, _responseBody);
}

bool CrossfireContext::commandSuspend(Value* arguments, Value** _responseBody) {
	CComPtr<IRemoteDebugApplication> application = NULL;
	if (!getDebugApplication(&application)) {
		return false;
	}

	HRESULT hr = application->CauseBreak();
	if (FAILED(hr)) {
		Logger::error("CrossfireContext.commandSuspend(): CauseBreak() failed", hr);
		return false;
	}

	*_responseBody = new Value();
	(*_responseBody)->setType(TYPE_OBJECT);
	return true;
}
