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
	~IEDebugger();

	/* interface definitions from activdbg.h */

    /* IApplicationDebugger */
    virtual HRESULT STDMETHODCALLTYPE QueryAlive();
    virtual HRESULT STDMETHODCALLTYPE CreateInstanceAtDebugger( 
        /* [in] */ REFCLSID rclsid,
        /* [in] */ IUnknown *pUnkOuter,
        /* [in] */ DWORD dwClsContext,
        /* [in] */ REFIID riid,
        /* [iid_is][out] */ IUnknown **ppvObject);
    virtual HRESULT STDMETHODCALLTYPE onDebugOutput( 
        /* [in] */ LPCOLESTR pstr);
    virtual HRESULT STDMETHODCALLTYPE onHandleBreakPoint( 
        /* [in] */ IRemoteDebugApplicationThread *prpt,
        /* [in] */ BREAKREASON br,
        /* [in] */ IActiveScriptErrorDebug *pError);
    virtual HRESULT STDMETHODCALLTYPE onClose();
    virtual HRESULT STDMETHODCALLTYPE onDebuggerEvent( 
        /* [in] */ REFIID riid,
        /* [in] */ IUnknown *punk);

    /* IApplicationDebuggerUI */
    virtual HRESULT STDMETHODCALLTYPE BringDocumentToTop( 
        /* [in] */ IDebugDocumentText *pddt);
    virtual HRESULT STDMETHODCALLTYPE BringDocumentContextToTop( 
        /* [in] */ IDebugDocumentContext *pddc);

    /* IDebugApplicationNodeEvents */
    virtual HRESULT STDMETHODCALLTYPE onAddChild( 
        /* [in] */ IDebugApplicationNode *prddpChild);
    virtual HRESULT STDMETHODCALLTYPE onRemoveChild( 
        /* [in] */ IDebugApplicationNode *prddpChild);
    virtual HRESULT STDMETHODCALLTYPE onDetach();
    virtual HRESULT STDMETHODCALLTYPE onAttach( 
        /* [in] */ IDebugApplicationNode *prddpParent);

	/* IDebugSessionProvider */
    virtual HRESULT STDMETHODCALLTYPE StartDebugSession( 
        /* [in] */ IRemoteDebugApplication *pda);

	/* IEDebugger */
	virtual void setContext(CrossfireContext* value);

private:
	virtual bool advise(IDebugApplicationNode* node, bool isRoot, bool recurse);
	virtual bool unadvise(IDebugApplicationNode* node, bool isRoot, bool recurse);

	std::map<IDebugApplicationNode*, DWORD>* m_adviseCookies;
	CrossfireContext* m_context;
	DWORD m_rootCookie;
	IDebugApplicationNode* m_rootNode;

	/* constants */
	static const wchar_t* ABOUT_BLANK;
};

OBJECT_ENTRY_AUTO(__uuidof(IEDebugger), IEDebugger)
