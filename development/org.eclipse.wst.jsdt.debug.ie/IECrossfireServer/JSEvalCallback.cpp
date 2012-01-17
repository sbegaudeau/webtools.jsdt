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
#include "JSEvalCallback.h"

JSEvalCallback::JSEvalCallback() {
	m_expression = NULL;
}

JSEvalCallback::~JSEvalCallback() {
	if (m_expression) {
		if (FAILED(m_expression->QueryIsComplete())) {
			m_expression->Abort();
		}
		m_expression->Release();
	}
}

/* IDebugExpressionCallBack */

STDMETHODIMP JSEvalCallback::onComplete() {
	HRESULT evalResult;
	IDebugProperty* result = NULL;
	HRESULT hr = m_expression->GetResultAsDebugProperty(&evalResult, &result);
	if (FAILED(hr)) {
		Logger::error("JSEvalCallback.onComplete(): GetResultAsDebugProperty() failed", hr);
		return S_OK;
	}
	if (FAILED(evalResult)) {
		Logger::error("JSEvalCallback.onComplete(): evaluation of GetResultAsDebugProperty() failed", evalResult);
		return S_OK;
	}
	m_handler->evalComplete(result, m_data);
	result->Release();
	return S_OK;
}

/* JSEvalCallback */

bool JSEvalCallback::start(IDebugExpression* expression, IJSEvalHandler* handler, void* data) {
	HRESULT hr = expression->Start(this);
	if (FAILED(hr)) {
		Logger::error("JSEvalCallback.start(): Start() failed", hr);
		return false;
	}

	if (m_expression) {
		if (FAILED(m_expression->QueryIsComplete())) {
			m_expression->Abort();
		}
		m_expression->Release();
	}
	m_expression = expression;
	m_expression->AddRef();
	m_data = data;
	m_handler = handler;
	return true;
}
