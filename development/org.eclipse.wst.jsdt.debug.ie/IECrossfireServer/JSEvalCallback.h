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


#pragma once

#include "activdbg.h"
#include "resource.h"
#include "CrossfireResponse.h"
#include "IECrossfireServer.h"
#include "Logger.h"

class ATL_NO_VTABLE JSEvalCallback :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<JSEvalCallback, &CLSID_JSEvalCallback>,
//	public IJSEvalCallback,
	public IDebugExpressionCallBack {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_JSEVALCALLBACK)
	DECLARE_NOT_AGGREGATABLE(JSEvalCallback)
	BEGIN_COM_MAP(JSEvalCallback)
		COM_INTERFACE_ENTRY2(IUnknown, IDebugExpressionCallBack)
//		COM_INTERFACE_ENTRY(IJSEvalCallback)
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
	~JSEvalCallback();

	/* IDebugExpressionCallBack */
	virtual HRESULT STDMETHODCALLTYPE onComplete();

	/* JSEvalCallback */
	virtual void init(CrossfireResponse* response, IDebugExpression* expression);

private:
	IDebugExpression* m_expression;
	CrossfireResponse* m_response;
};

OBJECT_ENTRY_AUTO(__uuidof(JSEvalCallback), JSEvalCallback)
