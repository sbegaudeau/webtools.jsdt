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
#include "PendingScriptLoad.h"

PendingScriptLoad::PendingScriptLoad() {
	m_applicationNode = NULL;
	m_context = NULL;
	m_cookie = 0;
}

PendingScriptLoad::~PendingScriptLoad() {
	detach();
}

IDebugApplicationNode* PendingScriptLoad::getApplicationNode() {
	return m_applicationNode;
}

/* IDebugDocumentTextEvents */

STDMETHODIMP PendingScriptLoad::onDestroy() {
	return S_OK;
}

STDMETHODIMP PendingScriptLoad::onInsertText(ULONG cCharacterPosition, ULONG cNumToInsert) {
	if (m_context) {
		m_context->scriptLoaded(m_applicationNode, cNumToInsert != 0);
	}

	return S_OK;
}

STDMETHODIMP PendingScriptLoad::onRemoveText(ULONG cCharacterPosition, ULONG cNumToRemove) {
	return S_OK;
}

STDMETHODIMP PendingScriptLoad::onReplaceText(ULONG cCharacterPosition, ULONG cNumToReplace) {
	return S_OK;
}

STDMETHODIMP PendingScriptLoad::onUpdateTextAttributes(ULONG cCharacterPosition, ULONG cNumToUpdate) {
	return S_OK;
}

STDMETHODIMP PendingScriptLoad::onUpdateDocumentAttributes(TEXT_DOC_ATTR textdocattr) {
	return S_OK;
}

/* PendingScriptLoad */

bool PendingScriptLoad::attach(IDebugApplicationNode* applicationNode, CrossfireContext* context) {
	CComPtr<IDebugDocument> document = NULL;
	HRESULT hr = applicationNode->GetDocument(&document);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad.attach(): GetDocument() failed", hr);
		return false;
	}

	CComPtr<IConnectionPointContainer> connectionPointContainer = NULL;
	hr = document->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		/*
		 * It is expected that this will fail for nodes that represent code evaluations
		 * (eg.- anonymous code, eval code, script blocks).
		 */
		if (hr != E_NOINTERFACE) {
			Logger::error("PendingScriptLoad.attach(): QI(IConnectionPointContainer) failed", hr);
		}
		return false;
	}

	CComPtr<IConnectionPoint> connectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugDocumentTextEvents, &connectionPoint);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad.attach(): FindConnectionPoint() failed", hr);
		return false;
	}

	hr = connectionPoint->Advise(static_cast<IPendingScriptLoad*>(this), &m_cookie);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad.attach(): Advise() failed", hr);
		return false;
	}

	applicationNode->AddRef();
	m_applicationNode = applicationNode;
	m_context = context;
	return true;
}

bool PendingScriptLoad::detach() {
	if (!m_cookie) {
		return true;
	}

	CComPtr<IDebugDocument> document = NULL;
	HRESULT hr = m_applicationNode->GetDocument(&document);
	if (FAILED(hr)) {
		/* The node is already destroyed, so nothing to unadvise on */
	} else {
		CComPtr<IConnectionPointContainer> connectionPointContainer = NULL;
		hr = document->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
		if (FAILED(hr)) {
			Logger::error("PendingScriptLoad.detach(): QI(IID_IConnectionPointContainer) failed", hr);
		} else {
			CComPtr<IConnectionPoint> connectionPoint = NULL;
			hr = connectionPointContainer->FindConnectionPoint(IID_IDebugDocumentTextEvents,&connectionPoint);
			if (FAILED(hr)) {
				Logger::error("PendingScriptLoad.detach(): FindConnectionPoint() failed", hr);
			} else {
				hr = connectionPoint->Unadvise(m_cookie);
				if (FAILED(hr)) {
					Logger::error("PendingScriptLoad.detach(): Unadvise() failed", hr);
				}
			}
		}
	}

	if (m_applicationNode) {
		m_applicationNode->Release();
		m_applicationNode = NULL;
	}
	m_context = NULL;
	m_cookie = 0;
	return true;
}
