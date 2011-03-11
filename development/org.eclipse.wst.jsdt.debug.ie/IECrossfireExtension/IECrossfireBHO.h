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
#include <exdispid.h>
#include <shlguid.h>

#include "IECrossfireExtension.h"
#include "Logger.h"

class ATL_NO_VTABLE IECrossfireBHO :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<IECrossfireBHO, &CLSID_IECrossfireBHO>,
	public IObjectWithSiteImpl<IECrossfireBHO>,
	public IIECrossfireBHO,
	public IDispEventImpl<1, IECrossfireBHO, &DIID_DWebBrowserEvents2, &LIBID_SHDocVw, 1, 1> {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_IECrossfireBHO)
	DECLARE_NOT_AGGREGATABLE(IECrossfireBHO)
	BEGIN_COM_MAP(IECrossfireBHO)
		COM_INTERFACE_ENTRY(IIECrossfireBHO)
		COM_INTERFACE_ENTRY(IObjectWithSite)
	END_COM_MAP()
	DECLARE_PROTECT_FINAL_CONSTRUCT()

	HRESULT FinalConstruct() {
		return S_OK;
	}

	void FinalRelease() {
	}

public:
	IECrossfireBHO();
	~IECrossfireBHO();

	/* IObjectWithSite */
	virtual HRESULT STDMETHODCALLTYPE GetSite(REFIID riid, LPVOID *ppvReturn);
	virtual HRESULT STDMETHODCALLTYPE SetSite(IUnknown *pUnkSite);

	/* DWebBrowserEvents2 */
	BEGIN_SINK_MAP(IECrossfireBHO)
		SINK_ENTRY_EX(1, DIID_DWebBrowserEvents2, DISPID_BEFORENAVIGATE2, OnBeforeNavigate2)
		SINK_ENTRY_EX(1, DIID_DWebBrowserEvents2, DISPID_DOCUMENTCOMPLETE, OnDocumentComplete)
		SINK_ENTRY_EX(1, DIID_DWebBrowserEvents2, DISPID_NAVIGATECOMPLETE2, OnNavigateComplete2)
	END_SINK_MAP()
    void STDMETHODCALLTYPE OnBeforeNavigate2(IDispatch* pDisp, VARIANT* URL, VARIANT* Flags, VARIANT* TargetFrameName, VARIANT* PostData, VARIANT* Headers, VARIANT_BOOL* Cancel);
	void STDMETHODCALLTYPE OnDocumentComplete(IDispatch* pDisp, VARIANT* URL);
	void STDMETHODCALLTYPE OnNavigateComplete2(IDispatch* pDisp, VARIANT* URL);

private:
	virtual bool serverIsActive();
	virtual HRESULT initServer();
	bool startDebugging();
	bool stopDebugging();

	ICrossfireServer* m_server;
	bool m_eventsHooked;
	IWebBrowser2* m_webBrowser;

	/* constants */
	static const wchar_t* DEBUG_START;
	static const wchar_t* DEBUG_STOP;
	static const wchar_t* PREFERENCE_DISABLEIEDEBUG;
};

OBJECT_ENTRY_AUTO(__uuidof(IECrossfireBHO), IECrossfireBHO)
