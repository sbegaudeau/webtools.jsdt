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
#include "PendingBreakpoint.h"

PendingBreakpoint::PendingBreakpoint() {
	m_breakpoint = NULL;
	m_node = NULL;
	m_cookie = 0;
	m_document = NULL;
	m_target = NULL;
}

PendingBreakpoint::~PendingBreakpoint() {
	if (m_breakpoint) {
		delete m_breakpoint;
	}
	if (m_node) {
		m_node->Release();
	}
	if (m_cookie) {
		unadvise();
	}
	if (m_document) {
		m_document->Release();
	}
}

/* IDebugDocumentTextEvents */

STDMETHODIMP PendingBreakpoint::onDestroy() {
	//Logger::error("PendingBreakpoint::onDestroy");
	unadvise();
	return S_OK;
}

STDMETHODIMP PendingBreakpoint::onInsertText(ULONG cCharacterPosition, ULONG cNumToInsert) {
	if (m_target) {
		// TODO for now always assuming line breakpoint
		if (m_target->setLineBreakpoint((CrossfireLineBreakpoint*)m_breakpoint, true)) {
			unadvise();
		}
	} else {
		CComBSTR url = NULL;
		HRESULT hr = m_document->GetName(DOCUMENTNAMETYPE_URL, &url);
		if (FAILED(hr)) {
			Logger::error("PendingBreakpoint::onInsertText(): GetName() failed", hr);
			return S_OK;
		}
		
		CComPtr<IDebugApplicationNode> node = NULL;
		m_context->findNode(url, NULL, &node);
		if (m_context->scriptLoaded(&std::wstring(url), node, true)) {
			unadvise();
		}
	}
	return S_OK;
}

STDMETHODIMP PendingBreakpoint::onRemoveText(ULONG cCharacterPosition, ULONG cNumToRemove) {
	return S_OK;
}

STDMETHODIMP PendingBreakpoint::onReplaceText(ULONG cCharacterPosition, ULONG cNumToReplace) {
	return S_OK;
}

STDMETHODIMP PendingBreakpoint::onUpdateTextAttributes(ULONG cCharacterPosition, ULONG cNumToUpdate) {
	return S_OK;
}

STDMETHODIMP PendingBreakpoint::onUpdateDocumentAttributes(TEXT_DOC_ATTR textdocattr) {
	return S_OK;
}

/* PendingBreakpoint */

bool PendingBreakpoint::init(/*CrossfireBreakpoint* breakpoint*/ IDebugApplicationNode* node, IDebugDocument* document, /*IBreakpointTarget**/ CrossfireContext* context) {
	CComPtr <IConnectionPointContainer> connectionPointContainer = NULL;
	HRESULT hr = document->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		Logger::error("PendingBreakpoint.init(): QI(IConnectionPointContainer) failed", hr);
		return false;
	}

	CComPtr<IConnectionPoint> connectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugDocumentTextEvents, &connectionPoint);
	if (FAILED(hr)) {
		Logger::error("PendingBreakpoint.init(): FindConnectionPoint failed", hr);
		return false;
	}

	hr = connectionPoint->Advise(static_cast<IPendingBreakpoint*>(this), &m_cookie);
	if (FAILED(hr)) {
		Logger::error("PendingBreakpoint.init(): Advise failed", hr);
		return false;
	}

	//breakpoint->clone(&m_breakpoint);
	node->AddRef();
	m_node = node;
	m_document = document;
	m_document->AddRef();
	m_context = context;
	return true;
}

bool PendingBreakpoint::init(CrossfireBreakpoint* breakpoint /*IDebugApplicationNode* node*/, IDebugDocument* document, IBreakpointTarget* /*CrossfireContext**/ target) {
	CComPtr <IConnectionPointContainer> connectionPointContainer = NULL;
	HRESULT hr = document->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		Logger::error("PendingBreakpoint.init(): QI(IConnectionPointContainer) failed", hr);
		return false;
	}

	CComPtr<IConnectionPoint> connectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugDocumentTextEvents, &connectionPoint);
	if (FAILED(hr)) {
		Logger::error("PendingBreakpoint.init(): FindConnectionPoint failed", hr);
		return false;
	}

	hr = connectionPoint->Advise(static_cast<IPendingBreakpoint*>(this), &m_cookie);
	if (FAILED(hr)) {
		Logger::error("PendingBreakpoint.init(): Advise failed", hr);
		return false;
	}

	breakpoint->clone(&m_breakpoint);
	//node->AddRef();
	m_document = document;
	m_document->AddRef();
	m_target = target;
	return true;
}

void PendingBreakpoint::unadvise() {
	CComPtr <IConnectionPointContainer> connectionPointContainer = NULL;
	HRESULT hr = m_document->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		Logger::error("PendingBreakpoint.unadvise() failed to QI for IID_IConnectionPointContainer", hr);
		return;
	}
	CComPtr <IConnectionPoint> connectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugDocumentTextEvents,&connectionPoint);
	if (FAILED(hr)) {
		Logger::error("PendingBreakpoint.unadvise(): FindConnectionPoint failed", hr);
		return;
	}
	hr = connectionPoint->Unadvise(m_cookie);
	if (FAILED(hr)) {
		Logger::error("PendingBreakpoint.unadvise(): Unadvise failed", hr);
		return;
	}

	m_cookie = 0;
}
