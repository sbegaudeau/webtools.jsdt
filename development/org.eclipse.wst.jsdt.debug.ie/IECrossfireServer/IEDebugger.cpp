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
#include "IEDebugger.h"

/* initialize constants */
const wchar_t* IEDebugger::ABOUT_BLANK = L"about:blank";

IEDebugger::IEDebugger() {
	m_adviseCookies = new std::map<IDebugApplicationNode*, DWORD>;
	m_context = NULL;
	m_rootNode = NULL;
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
		m_context->executionBreak(pDebugAppThread, br, pScriptErrorDebug);
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

	/* don't register about:blank as a script */
	CComBSTR bstrUrl = NULL;
	HRESULT hr = prddpChild->GetName(DOCUMENTNAMETYPE_TITLE, &bstrUrl);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.onAddChild(): GetName() failed", hr);
	} else {
		if (wcscmp(bstrUrl, ABOUT_BLANK) != 0) {
			m_context->scriptInitialized(prddpChild, false);
		}
	}

	advise(prddpChild, false, false);
	return S_OK;
}

STDMETHODIMP IEDebugger::onRemoveChild(IDebugApplicationNode *prddpChild) {
	unadvise(prddpChild, false, false);
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

bool IEDebugger::advise(IDebugApplicationNode* node, bool isRoot, bool recurse) {
	CComPtr<IConnectionPointContainer> connectionPointContainer = NULL;
	HRESULT hr = node->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.advise(): QI(IID_IConnectionPointContainer) failed", hr);
		return false;
	}
	CComPtr<IConnectionPoint> nodeConnectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugApplicationNodeEvents, &nodeConnectionPoint);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.advise(): FindConnectionPoint() failed", hr);
		return false;
	}

	DWORD connectionPointCookie = 0;
	hr = nodeConnectionPoint->Advise(static_cast<IIEDebugger*>(this), &connectionPointCookie);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.advise(): Advise() failed", hr);
		return false;
	}

	if (isRoot) {
		m_rootCookie = connectionPointCookie;
		m_rootNode = node;
	} else {
		m_adviseCookies->insert(std::pair<IDebugApplicationNode*,DWORD>(node, connectionPointCookie));
	}
	node->AddRef();

	if (recurse) {
		CComPtr<IEnumDebugApplicationNodes> nodes = NULL;
		hr = node->EnumChildren(&nodes);
		if (FAILED(hr)) {
			Logger::error("IEDebugger.advise(): EnumChildren() failed", hr);
		} else {
			IDebugApplicationNode* current = NULL;
			ULONG count = 0;
			hr = nodes->Next(1, &current, &count);
			while (SUCCEEDED(hr) && count) {
				advise(current, false, true);
				current->Release();
				hr = nodes->Next(1, &current, &count);
			}
		}
	}
	return true;
}

void IEDebugger::setContext(CrossfireContext* value) {
	if (m_context == value) {
		return;
	}
	m_context = value;

	if (m_rootNode) {
		unadvise(m_rootNode, true, true);
	}

	if (value) {
		CComPtr<IRemoteDebugApplication> application = NULL;
		HRESULT hr = m_context->getDebugApplication(&application);
		if (SUCCEEDED(hr)) {
			CComPtr<IDebugApplicationNode> rootNode = NULL;
			hr = application->GetRootNode(&rootNode);
			if (FAILED(hr)) {
				Logger::error("IEDebugger.setContext(): GetRootNode() failed", hr);
				return;
			}
			advise(rootNode, true, true);
		}
	}
}

bool IEDebugger::unadvise(IDebugApplicationNode* node, bool isRoot, bool recurse) {
	CComPtr<IConnectionPointContainer> connectionPointContainer = NULL;
	HRESULT hr = node->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.unadvise(): QI(IID_IConnectionPointContainer) failed", hr);
		return false;
	}
	CComPtr<IConnectionPoint> nodeConnectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugApplicationNodeEvents, &nodeConnectionPoint);
	if (FAILED(hr)) {
		Logger::error("IEDebugger.unadvise(): FindConnectionPoint() failed", hr);
		return false;
	}

	DWORD cookie = 0;
	if (isRoot) {
		cookie = m_rootCookie;
		m_rootNode->Release();
		m_rootNode = NULL;
	} else {
		std::map<IDebugApplicationNode*, DWORD>::iterator iterator = m_adviseCookies->find(node);
		if (iterator != m_adviseCookies->end()) {
			cookie = iterator->second;
			iterator->first->Release();
			m_adviseCookies->erase(iterator);
		}
	}

	if (cookie) {
		hr = nodeConnectionPoint->Unadvise(cookie);
		if (FAILED(hr)) {
			Logger::error("IEDebugger.unadvise(): Unadvise() failed", hr);
		}
	}

	if (recurse) {
		CComPtr<IEnumDebugApplicationNodes> nodes = NULL;
		hr = node->EnumChildren(&nodes);
		if (FAILED(hr)) {
			Logger::error("IEDebugger.unadvise(): EnumChildren() failed", hr);
		} else {
			IDebugApplicationNode* current = NULL;
			ULONG count = 0;
			hr = nodes->Next(1, &current, &count);
			while (SUCCEEDED(hr) && count) {
				unadvise(current, false, true);
				current->Release();
				hr = nodes->Next(1, &current, &count);
			}
		}
	}
	return true;
}


