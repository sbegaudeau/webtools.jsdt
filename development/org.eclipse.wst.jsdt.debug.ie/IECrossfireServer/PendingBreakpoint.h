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

#include "resource.h"
#include "activdbg.h"

//#include "CrossfireBreakpoint.h"
#include "CrossfireContext.h"
//#include "IECrossfireServer.h"
//#include "IBreakpointTarget.h"

class ATL_NO_VTABLE PendingBreakpoint :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<PendingBreakpoint, &CLSID_PendingBreakpoint>,
	public IDispatchImpl<IPendingBreakpoint, &IID_IPendingBreakpoint, &LIBID_IECrossfireServerLib, 1, 0>,
	public IDebugDocumentTextEvents {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_PENDINGBREAKPOINT)
	DECLARE_NOT_AGGREGATABLE(PendingBreakpoint)
	BEGIN_COM_MAP(PendingBreakpoint)
		COM_INTERFACE_ENTRY(IPendingBreakpoint)
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
	PendingBreakpoint();
	~PendingBreakpoint();

	/* IDebugDocumentTextEvents */
	virtual HRESULT STDMETHODCALLTYPE onDestroy();
	virtual HRESULT STDMETHODCALLTYPE onInsertText(
		/* [in] */ ULONG cCharacterPosition,
		/* [in] */ ULONG cNumToInsert);
	virtual HRESULT STDMETHODCALLTYPE onRemoveText(
		/* [in] */ ULONG cCharacterPosition,
		/* [in] */ ULONG cNumToRemove);
	virtual HRESULT STDMETHODCALLTYPE onReplaceText(
		/* [in] */ ULONG cCharacterPosition,
		/* [in] */ ULONG cNumToReplace);
	virtual HRESULT STDMETHODCALLTYPE onUpdateTextAttributes(
		/* [in] */ ULONG cCharacterPosition,
		/* [in] */ ULONG cNumToUpdate);
	virtual HRESULT STDMETHODCALLTYPE onUpdateDocumentAttributes(
		/* [in] */ TEXT_DOC_ATTR textdocattr);

	/* PendingBreakpoint */
	virtual bool init(/*CrossfireBreakpoint* breakpoint*/ IDebugApplicationNode* node, IDebugDocument* document, /*IBreakpointTarget**/ CrossfireContext* target);
	virtual bool init(CrossfireBreakpoint* breakpoint /*IDebugApplicationNode* node*/, IDebugDocument* document, IBreakpointTarget* /*CrossfireContext**/ context);

private:
	void unadvise();

	CrossfireBreakpoint* m_breakpoint;
	IDebugApplicationNode* m_node;
	DWORD m_cookie;
	IDebugDocument* m_document;
	IBreakpointTarget* m_target;
	CrossfireContext* m_context;
};

OBJECT_ENTRY_AUTO(__uuidof(PendingBreakpoint), PendingBreakpoint)
