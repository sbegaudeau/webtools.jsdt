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

#include "resource.h"
#include "activdbg.h"

#include "CrossfireContext.h"

class ATL_NO_VTABLE PendingScriptLoad :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<PendingScriptLoad, &CLSID_PendingScriptLoad>,
	public IDispatchImpl<IPendingScriptLoad, &IID_IPendingScriptLoad, &LIBID_IECrossfireServerLib, 1, 0>,
	public IDebugDocumentTextEvents {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_PENDINGSCRIPTLOAD)
	DECLARE_NOT_AGGREGATABLE(PendingScriptLoad)
	BEGIN_COM_MAP(PendingScriptLoad)
		COM_INTERFACE_ENTRY(IPendingScriptLoad)
		COM_INTERFACE_ENTRY(IDispatch)
		COM_INTERFACE_ENTRY(IDebugDocumentTextEvents)
	END_COM_MAP()
	DECLARE_PROTECT_FINAL_CONSTRUCT()

	HRESULT FinalConstruct() {
		return S_OK;
	}

	void FinalRelease() {
	}

public:
	PendingScriptLoad();
	virtual ~PendingScriptLoad();

	IDebugApplicationNode* getApplicationNode();

	/* IDebugDocumentTextEvents */
	HRESULT STDMETHODCALLTYPE onDestroy();
	HRESULT STDMETHODCALLTYPE onInsertText(
		/* [in] */ ULONG cCharacterPosition,
		/* [in] */ ULONG cNumToInsert);
	HRESULT STDMETHODCALLTYPE onRemoveText(
		/* [in] */ ULONG cCharacterPosition,
		/* [in] */ ULONG cNumToRemove);
	HRESULT STDMETHODCALLTYPE onReplaceText(
		/* [in] */ ULONG cCharacterPosition,
		/* [in] */ ULONG cNumToReplace);
	HRESULT STDMETHODCALLTYPE onUpdateTextAttributes(
		/* [in] */ ULONG cCharacterPosition,
		/* [in] */ ULONG cNumToUpdate);
	HRESULT STDMETHODCALLTYPE onUpdateDocumentAttributes(
		/* [in] */ TEXT_DOC_ATTR textdocattr);

	/* PendingScriptLoad */
	bool attach(IDebugApplicationNode* applicationNode, CrossfireContext* context);
	bool detach();

private:
	IDebugApplicationNode* m_applicationNode;
	CrossfireContext* m_context;
	DWORD m_cookie;
};

OBJECT_ENTRY_AUTO(__uuidof(PendingScriptLoad), PendingScriptLoad)
