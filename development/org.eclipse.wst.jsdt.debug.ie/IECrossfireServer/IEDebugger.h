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
#include "CrossfireContext.h"
#include "Logger.h"

class ATL_NO_VTABLE IEDebugger :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<IEDebugger, &CLSID_IEDebugger>,
	public IApplicationDebugger,
    public IApplicationDebuggerUI,
    public IDebugApplicationNodeEvents,
	public IDebugSessionProvider,
	public IIEDebugger {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_IEDEBUGGER)
	BEGIN_COM_MAP(IEDebugger)
		COM_INTERFACE_ENTRY2(IUnknown, IApplicationDebugger)
		COM_INTERFACE_ENTRY(IIEDebugger)
		COM_INTERFACE_ENTRY(IApplicationDebugger)
		COM_INTERFACE_ENTRY(IApplicationDebuggerUI)
		COM_INTERFACE_ENTRY(IDebugApplicationNodeEvents)
		COM_INTERFACE_ENTRY(IDebugSessionProvider)
		COM_INTERFACE_ENTRY(IIEDebugger)
	END_COM_MAP()
	DECLARE_PROTECT_FINAL_CONSTRUCT()

	HRESULT FinalConstruct() {
		return S_OK;
	}

	void FinalRelease() {
	}

public:
	IEDebugger();
	virtual ~IEDebugger();

	/* interface definitions from activdbg.h */

    /* IApplicationDebugger */
    HRESULT STDMETHODCALLTYPE QueryAlive();
    HRESULT STDMETHODCALLTYPE CreateInstanceAtDebugger( 
        /* [in] */ REFCLSID rclsid,
        /* [in] */ IUnknown *pUnkOuter,
        /* [in] */ DWORD dwClsContext,
        /* [in] */ REFIID riid,
        /* [iid_is][out] */ IUnknown **ppvObject);
    HRESULT STDMETHODCALLTYPE onDebugOutput( 
        /* [in] */ LPCOLESTR pstr);
    HRESULT STDMETHODCALLTYPE onHandleBreakPoint( 
        /* [in] */ IRemoteDebugApplicationThread *prpt,
        /* [in] */ BREAKREASON br,
        /* [in] */ IActiveScriptErrorDebug *pError);
    HRESULT STDMETHODCALLTYPE onClose();
    HRESULT STDMETHODCALLTYPE onDebuggerEvent( 
        /* [in] */ REFIID riid,
        /* [in] */ IUnknown *punk);

    /* IApplicationDebuggerUI */
    HRESULT STDMETHODCALLTYPE BringDocumentToTop( 
        /* [in] */ IDebugDocumentText *pddt);
    HRESULT STDMETHODCALLTYPE BringDocumentContextToTop( 
        /* [in] */ IDebugDocumentContext *pddc);

    /* IDebugApplicationNodeEvents */
    HRESULT STDMETHODCALLTYPE onAddChild( 
        /* [in] */ IDebugApplicationNode *prddpChild);
    HRESULT STDMETHODCALLTYPE onRemoveChild( 
        /* [in] */ IDebugApplicationNode *prddpChild);
    HRESULT STDMETHODCALLTYPE onDetach();
    HRESULT STDMETHODCALLTYPE onAttach( 
        /* [in] */ IDebugApplicationNode *prddpParent);

	/* IDebugSessionProvider */
    HRESULT STDMETHODCALLTYPE StartDebugSession( 
        /* [in] */ IRemoteDebugApplication *pda);

	/* IEDebugger */
	void setContext(CrossfireContext* value);

private:
	bool advise(IDebugApplicationNode* node, bool isRoot, bool recurse);
	bool unadvise(IDebugApplicationNode* node, bool isRoot, bool recurse);

	std::map<IDebugApplicationNode*, DWORD>* m_adviseCookies;
	CrossfireContext* m_context;
	DWORD m_rootCookie;
	IDebugApplicationNode* m_rootNode;

	/* constants */
	static const wchar_t* ABOUT_BLANK;
};

OBJECT_ENTRY_AUTO(__uuidof(IEDebugger), IEDebugger)
