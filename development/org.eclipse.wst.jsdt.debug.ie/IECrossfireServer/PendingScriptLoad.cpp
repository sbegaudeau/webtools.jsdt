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
#include "PendingScriptLoad.h"

PendingScriptLoad::PendingScriptLoad() {
	m_context = NULL;
	m_cookie = 0;
	m_applicationNode = NULL;
}

PendingScriptLoad::~PendingScriptLoad() {
	if (m_cookie) {
		unadvise();
	}
	if (m_applicationNode) {
		m_applicationNode->Release();
	}
}

IDebugApplicationNode* PendingScriptLoad::getApplicationNode() {
	return m_applicationNode;
}

/* IDebugDocumentTextEvents */

STDMETHODIMP PendingScriptLoad::onDestroy() {
	//Logger::error("PendingScriptLoad::onDestroy");
	unadvise();
	return S_OK;
}

STDMETHODIMP PendingScriptLoad::onInsertText(ULONG cCharacterPosition, ULONG cNumToInsert) {
	CComBSTR url = NULL;
	HRESULT hr = m_applicationNode->GetName(DOCUMENTNAMETYPE_URL, &url);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad::onInsertText(): GetName() failed", hr);
		return S_OK;
	}

	if (m_context->scriptLoaded(&std::wstring(url), NULL, true)) {
		unadvise();
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

bool PendingScriptLoad::init(IDebugApplicationNode* applicationNode, CrossfireContext* context) {
	CComPtr<IDebugDocument> document = NULL;
	HRESULT hr = applicationNode->GetDocument(&document);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad.init(): GetDocument() failed", hr);
		return false;
	}

	CComPtr<IConnectionPointContainer> connectionPointContainer = NULL;
	hr = document->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad.init(): QI(IConnectionPointContainer) failed", hr);
		return false;
	}

	CComPtr<IConnectionPoint> connectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugDocumentTextEvents, &connectionPoint);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad.init(): FindConnectionPoint failed", hr);
		return false;
	}

	hr = connectionPoint->Advise(static_cast<IPendingScriptLoad*>(this), &m_cookie);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad.init(): Advise failed", hr);
		return false;
	}

	applicationNode->AddRef();
	m_applicationNode = applicationNode;
	m_context = context;
	return true;
}

void PendingScriptLoad::unadvise() {
	CComPtr<IDebugDocument> document = NULL;
	HRESULT hr = m_applicationNode->GetDocument(&document);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad.unadvise(): GetDocument() failed", hr);
		return;
	}

	CComPtr <IConnectionPointContainer> connectionPointContainer = NULL;
	hr = document->QueryInterface(IID_IConnectionPointContainer, (void**)&connectionPointContainer);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad.unadvise() failed to QI for IID_IConnectionPointContainer", hr);
		return;
	}
	CComPtr <IConnectionPoint> connectionPoint = NULL;
	hr = connectionPointContainer->FindConnectionPoint(IID_IDebugDocumentTextEvents,&connectionPoint);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad.unadvise(): FindConnectionPoint failed", hr);
		return;
	}
	hr = connectionPoint->Unadvise(m_cookie);
	if (FAILED(hr)) {
		Logger::error("PendingScriptLoad.unadvise(): Unadvise failed", hr);
		return;
	}

	m_cookie = 0;
}
