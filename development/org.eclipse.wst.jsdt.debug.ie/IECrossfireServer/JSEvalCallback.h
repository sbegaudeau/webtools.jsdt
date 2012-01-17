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


#pragma once

#include "activdbg.h"
#include "resource.h"
#include "IECrossfireServer.h"
#include "IJSEvalHandler.h"
#include "Logger.h"

class ATL_NO_VTABLE JSEvalCallback :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<JSEvalCallback, &CLSID_JSEvalCallback>,
	public IDebugExpressionCallBack {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_JSEVALCALLBACK)
	BEGIN_COM_MAP(JSEvalCallback)
		COM_INTERFACE_ENTRY2(IUnknown, IDebugExpressionCallBack)
		COM_INTERFACE_ENTRY(IDebugExpressionCallBack)
	END_COM_MAP()
	DECLARE_PROTECT_FINAL_CONSTRUCT()

	HRESULT FinalConstruct() {
		return S_OK;
	}

	void FinalRelease() {
	}

public:
	JSEvalCallback();
	virtual ~JSEvalCallback();

	/* IDebugExpressionCallBack */
	HRESULT STDMETHODCALLTYPE onComplete();

	/* JSEvalCallback */
	bool start(IDebugExpression* expression, IJSEvalHandler* handler, void* data);

private:
	IDebugExpression* m_expression;
	IJSEvalHandler* m_handler;
	void* m_data;
};

OBJECT_ENTRY_AUTO(__uuidof(JSEvalCallback), JSEvalCallback)
