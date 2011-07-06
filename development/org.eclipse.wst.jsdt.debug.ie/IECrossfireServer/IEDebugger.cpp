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
	Logger::log("onDebugOutput invoked");
	return S_OK;
}

STDMETHODIMP IEDebugger::onHandleBreakPoint(IRemoteDebugApplicationThread *pDebugAppThread, BREAKREASON br, IActiveScriptErrorDebug *pScriptErrorDebug) {
	if (m_context) {
		m_context->breakpointHit(pDebugAppThread, br, pScriptErrorDebug);
	} else {
		// TODO should probably resume in this case?
	}
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
	if (!m_context) {
		return S_OK;
	}

	m_context->scriptInitialized(prddpChild);

	CComPtr <IConnectionPointContainer> connectionPointContainer = NULL;
	HRESULT hr = prddpChild->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onAddChild(): QI(IID_IConnectionPointContainer) failed", hr);
		return S_FALSE;
	}
	CComPtr <IConnectionPoint> nodeConnectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugApplicationNodeEvents, &nodeConnectionPoint);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onAddChild(): FindConnectionPoint() failed", hr);
		return S_FALSE;
	}

	DWORD connectionPointCookie = 0;
	hr = nodeConnectionPoint->Advise(static_cast<IIEDebugger*>(this), &connectionPointCookie);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onAddChild(): Advise() failed", hr);
		return S_FALSE;
	}

	CComBSTR url = NULL;
	hr = prddpChild->GetName(DOCUMENTNAMETYPE_TITLE, &url);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onAddChild(): GetName() failed", hr);
		return S_FALSE;
	}
	m_adviseCookies->insert(std::pair<std::wstring,DWORD>(std::wstring(url), connectionPointCookie));
	return S_OK;
}

STDMETHODIMP IEDebugger::onRemoveChild(IDebugApplicationNode *prddpChild) {
	CComBSTR url = NULL;
	if (FAILED(prddpChild->GetName(DOCUMENTNAMETYPE_TITLE, &url))) {
		return S_OK;
	}

	CComPtr <IConnectionPointContainer> connectionPointContainer = NULL;
	HRESULT hr = prddpChild->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onRemoveChild(): QI(IID_IConnectionPointContainer) failed", hr);
		return S_FALSE;
	}
	CComPtr <IConnectionPoint> nodeConnectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugApplicationNodeEvents, &nodeConnectionPoint);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onRemoveChild(): FindConnectionPoint() failed", hr);
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

