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
#include "IEDebugger.h"

/* event: onBreak */
const wchar_t* IEDebugger::EVENT_ONBREAK = L"onBreak";
const wchar_t* IEDebugger::KEY_LINE = L"line";
const wchar_t* IEDebugger::KEY_URL = L"url";

/* event: onConsoleError */
const wchar_t* IEDebugger::EVENT_ONCONSOLEERROR = L"onConsoleError";
const wchar_t* IEDebugger::KEY_FILENAME = L"fileName";
const wchar_t* IEDebugger::KEY_LINENUMBER = L"lineNumber";
const wchar_t* IEDebugger::KEY_MESSAGE = L"message";
const wchar_t* IEDebugger::KEY_NAME = L"name";
const wchar_t* IEDebugger::KEY_SOURCE = L"source";
const wchar_t* IEDebugger::KEY_STACK = L"stack";

/* event: onConsoleLog */
const wchar_t* IEDebugger::EVENT_ONCONSOLELOG = L"onConsoleLog";

/* event: onResume */
const wchar_t* IEDebugger::EVENT_ONRESUME = L"onResume";

/* event: onScript */
const wchar_t* IEDebugger::EVENT_ONSCRIPT = L"onScript";
const wchar_t* IEDebugger::KEY_CONTEXTHREF = L"context_href";
const wchar_t* IEDebugger::KEY_HREF = L"href";


IEDebugger::IEDebugger() {
	m_context = NULL;
	m_adviseCookies = new std::multimap<std::wstring, DWORD>;
}

IEDebugger::~IEDebugger() {
	delete m_adviseCookies;
}

/* IApplicationDebugger */

STDMETHODIMP IEDebugger::QueryAlive(void) {
	Logger::log("QueryAlive invoked");
    return S_OK;
}

STDMETHODIMP IEDebugger::CreateInstanceAtDebugger(REFCLSID rclsid, IUnknown *pUnkOuter, DWORD dwClsContext, REFIID riid, IUnknown **ppvObject) {
	Logger::log("CreateInstanceAtDebugger invoked");
    return E_NOTIMPL;
}

STDMETHODIMP IEDebugger::onDebugOutput(LPCOLESTR pstr) {
	CrossfireEvent onConsoleLogEvent;
	onConsoleLogEvent.setName(EVENT_ONCONSOLELOG);
	Value data;
	data.addObjectValue(L"0", &Value(pstr));
	onConsoleLogEvent.setData(&data);
	m_context->sendEvent(&onConsoleLogEvent);
	return S_OK;
}

STDMETHODIMP IEDebugger::onHandleBreakPoint(IRemoteDebugApplicationThread *pDebugAppThread, BREAKREASON br, IActiveScriptErrorDebug *pScriptErrorDebug) {
	m_context->setRunning(false);

	if (br == BREAKREASON_ERROR) {
		return handleError(pScriptErrorDebug);
	}

	CComPtr<IEnumDebugStackFrames> stackFrames = NULL;
	HRESULT hr = pDebugAppThread->EnumStackFrames(&stackFrames);
	if (FAILED(hr)) {
		Logger::error("'onHandleBreakPoint' EnumStackFrames() failed", hr);
		return S_OK;
	}

	DebugStackFrameDescriptor stackFrameDescriptor;
	ULONG numFetched = 0;
	hr = stackFrames->Next(1,&stackFrameDescriptor,&numFetched);
	if (FAILED(hr) || numFetched != 1) {
		Logger::error("'onHandleBreakPoint' EnumStackFrames->Next() failed", hr);
		return S_OK;
	}

	IDebugStackFrame* frame = stackFrameDescriptor.pdsf;
	CComPtr<IDebugCodeContext> codeContext = NULL;
	hr = frame->GetCodeContext(&codeContext);
	// TODO This fails if the current position is not in a user document (eg.- following
	// a return).  Not sure what to do here (send an event with no url/line?  Or no event?)
	if (FAILED(hr)) {
		Logger::error("'onHandleBreakPoint' GetCodeContext() failed", hr);
		return S_OK;
	}

	CComPtr<IDebugDocumentContext> documentContext = NULL;
	hr = codeContext->GetDocumentContext(&documentContext);
	if (FAILED(hr)) {
		Logger::error("'onHandleBreakPoint' GetDocumentContext() failed", hr);
		return S_OK;
	}

	CComPtr<IDebugDocument> document = NULL;
	hr = documentContext->GetDocument(&document);
	if (FAILED(hr)) {
		Logger::error("'onHandleBreakPoint' GetDocument() failed", hr);
		return S_OK;
	}

	CComPtr<IDebugDocumentText> documentText = NULL;
	hr = document->QueryInterface(IID_IDebugDocumentText,(void**)&documentText);
	if (FAILED(hr)) {
		Logger::error("'onHandleBreakPoint' QueryInterface() failed", hr);
		return S_OK;
	}

	ULONG position, numChars;
	hr = documentText->GetPositionOfContext(documentContext, &position, &numChars);
	if (FAILED(hr)) {
		Logger::error("'onHandleBreakPoint' GetPositionOfContext() failed", hr);
		return S_OK;
	}

	ULONG lineNumber, column;
	hr = documentText->GetLineOfPosition(position, &lineNumber, &column);
	if (FAILED(hr)) {
		Logger::error("'onHandleBreakPoint' GetLineOfContext() failed", hr);
		return S_OK;
	}

	CComBSTR bstrUrl;
	hr = document->GetName(DOCUMENTNAMETYPE_URL, &bstrUrl);
	if (FAILED(hr)) {
		Logger::error("'onHandleBreakPoint' GetName() failed", hr);
		return S_OK;
	}

	/* Evaluate the the hit breakpoint's condition if it has one. */
	if (br == BREAKREASON_BREAKPOINT) {

	}

	CrossfireEvent onBreakEvent;
	onBreakEvent.setName(EVENT_ONBREAK);
	Value data;
	data.addObjectValue(KEY_LINE, &Value((double)lineNumber + 1));
	data.addObjectValue(KEY_URL, &Value(bstrUrl));
	onBreakEvent.setData(&data);
	m_context->sendEvent(&onBreakEvent);
	return S_OK;
}

HRESULT IEDebugger::handleError(IActiveScriptErrorDebug *pScriptErrorDebug) {
	Value error;
	CComBSTR sourceLine = NULL;
	if (SUCCEEDED(pScriptErrorDebug->GetSourceLineText(&sourceLine))) {
		error.addObjectValue(KEY_SOURCE, &Value(sourceLine));
	} else {
		error.addObjectValue(KEY_SOURCE, &Value(L"")); // TODO should this just be omitted?
	}

	EXCEPINFO excepInfo;
	HRESULT hr = pScriptErrorDebug->GetExceptionInfo(&excepInfo);
	if (FAILED(hr)) {
		Logger::error("handleError GetExceptionInfo() failed", hr);
		return S_OK;
	}

	if (excepInfo.bstrDescription) {
		error.addObjectValue(KEY_NAME, &Value(excepInfo.bstrDescription));
		error.addObjectValue(KEY_MESSAGE, &Value(excepInfo.bstrDescription)); // TODO can do better than this?
	} else {
		error.addObjectValue(KEY_NAME, &Value(L"")); // TODO should these just be omitted?
		error.addObjectValue(KEY_MESSAGE, &Value(L"")); // TODO can do better than this?
	}

	CComPtr<IDebugDocumentContext> documentContext = NULL;
	hr = pScriptErrorDebug->GetDocumentContext(&documentContext);
	if (SUCCEEDED(hr)) {
		CComPtr<IDebugDocument> document = NULL;
		hr = documentContext->GetDocument(&document);
		if (SUCCEEDED(hr)) {
			CComBSTR bstrUrl;
			hr = document->GetName(DOCUMENTNAMETYPE_URL, &bstrUrl);
			if (SUCCEEDED(hr)) {
				error.addObjectValue(KEY_FILENAME, &Value(bstrUrl));
			}
		}
	}
	if (FAILED(hr)) {			
		/* fall back to trying the excepInfo struct */
		if (excepInfo.bstrSource) {
			error.addObjectValue(KEY_FILENAME, &Value(excepInfo.bstrSource));
		} else {
			error.addObjectValue(KEY_FILENAME, &Value(L"")); // TODO should this just be omitted?
		}
	}

	DWORD sourceContext;
	ULONG lineNumber;
	LONG charPosition;
	hr = pScriptErrorDebug->GetSourcePosition(&sourceContext, &lineNumber, &charPosition);
	if (FAILED(hr)) {
		Logger::error("handleError GetSourcePosition() failed", hr);
		return S_OK;
	}
	error.addObjectValue(KEY_LINENUMBER, &Value((double)lineNumber));
	Logger::error("scode", excepInfo.scode);
	Logger::error("wcode", excepInfo.wCode);

	CrossfireEvent onConsoleErrorEvent;
	onConsoleErrorEvent.setName(EVENT_ONCONSOLEERROR);
	Value data;
	data.addObjectValue(L"0", &error);
	onConsoleErrorEvent.setData(&data);
	m_context->sendEvent(&onConsoleErrorEvent);
	return S_OK;
}

STDMETHODIMP IEDebugger::onClose(void) {
	Logger::log("onClose invoked");
	return E_NOTIMPL;
}

STDMETHODIMP IEDebugger::onDebuggerEvent(REFIID riid, IUnknown *punk) {
	Logger::log("onDebuggerEvent invoked");
    return E_NOTIMPL;
}

/* IApplicationDebuggerUI */

STDMETHODIMP IEDebugger::BringDocumentToTop(IDebugDocumentText *pddt) {
	Logger::log("BringDocumentToTop invoked");
    return S_OK;
}

STDMETHODIMP IEDebugger::BringDocumentContextToTop(IDebugDocumentContext *pddc) {
	Logger::log("BringDocumentContextToTop invoked");
    return S_OK;
}

/* IDebugApplicationNodeEvents */

STDMETHODIMP IEDebugger::onAddChild(IDebugApplicationNode *prddpChild) {
	CComBSTR url = NULL;
	if (FAILED(prddpChild->GetName(DOCUMENTNAMETYPE_URL, &url))) {
		return S_OK;
	}

	m_context->scriptLoaded(&std::wstring(url), prddpChild, false);

	CComPtr <IConnectionPointContainer> connectionPointContainer = NULL;
	HRESULT hr = prddpChild->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onAddChild failed to QI for IID_IConnectionPointContainer", hr);
		return S_FALSE;
	}
	CComPtr <IConnectionPoint> nodeConnectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugApplicationNodeEvents, &nodeConnectionPoint);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onAddChild FindConnectionPoint failed", hr);
		return S_FALSE;
	}

	DWORD connectionPointCookie = 0;
	hr = nodeConnectionPoint->Advise(static_cast<IIEDebugger*>(this), &connectionPointCookie);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onAddChild Advise failed", hr);
		return S_FALSE;
	}
	m_adviseCookies->insert(std::pair<std::wstring,DWORD>(std::wstring(url), connectionPointCookie));
	return S_OK;
}

STDMETHODIMP IEDebugger::onRemoveChild(IDebugApplicationNode *prddpChild) {
	CComBSTR url = NULL;
	if (FAILED(prddpChild->GetName(DOCUMENTNAMETYPE_URL, &url))) {
		return S_OK;
	}

	CComPtr <IConnectionPointContainer> connectionPointContainer = NULL;
	HRESULT hr = prddpChild->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onRemoveChild() failed to QI for IID_IConnectionPointContainer", hr);
		return S_FALSE;
	}
	CComPtr <IConnectionPoint> nodeConnectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugApplicationNodeEvents,&nodeConnectionPoint);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onRemoveChild() FindConnectionPoint failed", hr);
		return S_FALSE;
	}

	std::pair<std::multimap<std::wstring,DWORD>::iterator,std::multimap<std::wstring,DWORD>::iterator> range;
	range = m_adviseCookies->equal_range(std::wstring(url));
	std::multimap<std::wstring,DWORD>::iterator it;
	for (it = range.first; it != range.second; ++it) {
		if (SUCCEEDED(nodeConnectionPoint->Unadvise(it->second))) {
			m_adviseCookies->erase(it);
			break;
		}
	}

	return S_OK;
}

STDMETHODIMP IEDebugger::onDetach(void) {
    return S_OK;
}

STDMETHODIMP IEDebugger::onAttach(IDebugApplicationNode *prddpParent) {
    return S_OK;
}

/* IDebugSessionProvider */

STDMETHODIMP IEDebugger::StartDebugSession(IRemoteDebugApplication *pda) {
	Logger::log("StartDebugSession invoked");
    return S_OK;
}

/* IEDebugger */

void IEDebugger::setContext(CrossfireContext* value) {
	m_context = value;
}

