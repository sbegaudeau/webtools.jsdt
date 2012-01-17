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
#include <exdispid.h>
#include <shlguid.h>
#include <string>

#include "IECrossfireExtension.h"
#include "Util.h"
#include "Logger.h"

enum {
	STATE_DISCONNECTED,
	STATE_LISTENING,
	STATE_CONNECTED,
};

class ATL_NO_VTABLE IECrossfireBHO :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<IECrossfireBHO, &CLSID_IECrossfireBHO>,
	public IObjectWithSiteImpl<IECrossfireBHO>,
	public IIECrossfireBHO,
	public IBrowserContext,
	public IDispEventImpl<1, IECrossfireBHO, &DIID_DWebBrowserEvents2, &LIBID_SHDocVw, 1, 1> {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_IECrossfireBHO)
	DECLARE_NOT_AGGREGATABLE(IECrossfireBHO)
	BEGIN_COM_MAP(IECrossfireBHO)
		COM_INTERFACE_ENTRY(IIECrossfireBHO)
		COM_INTERFACE_ENTRY(IBrowserContext)
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
	virtual ~IECrossfireBHO();

	/* DWebBrowserEvents2 */
	BEGIN_SINK_MAP(IECrossfireBHO)
		SINK_ENTRY_EX(1, DIID_DWebBrowserEvents2, DISPID_BEFORENAVIGATE2, OnBeforeNavigate2)
		SINK_ENTRY_EX(1, DIID_DWebBrowserEvents2, DISPID_DOCUMENTCOMPLETE, OnDocumentComplete)
		SINK_ENTRY_EX(1, DIID_DWebBrowserEvents2, DISPID_NAVIGATECOMPLETE2, OnNavigateComplete2)
		SINK_ENTRY_EX(1, DIID_DWebBrowserEvents2, DISPID_WINDOWSTATECHANGED, OnWindowStateChanged)
	END_SINK_MAP()
    void STDMETHODCALLTYPE OnBeforeNavigate2(IDispatch* pDisp, VARIANT* URL, VARIANT* Flags, VARIANT* TargetFrameName, VARIANT* PostData, VARIANT* Headers, VARIANT_BOOL* Cancel);
	void STDMETHODCALLTYPE OnDocumentComplete(IDispatch* pDisp, VARIANT* URL);
	void STDMETHODCALLTYPE OnNavigateComplete2(IDispatch* pDisp, VARIANT* URL);
	void STDMETHODCALLTYPE OnWindowStateChanged(LONG dwFlags, LONG dwValidFlagMask);

	/* IBrowserContext */
	HRESULT STDMETHODCALLTYPE displayMessage(OLECHAR* url);
	HRESULT STDMETHODCALLTYPE navigate(OLECHAR* url, boolean openNewTab);

	/* IObjectWithSite */
	HRESULT STDMETHODCALLTYPE GetSite(REFIID riid, LPVOID *ppvReturn);
	HRESULT STDMETHODCALLTYPE SetSite(IUnknown *pUnkSite);

private:
	bool displayHTML(wchar_t* htmlText);
//	int getServerState();
	bool initServer(bool startIfNeeded);
	void onServerStateChanged(WPARAM wParam, LPARAM lParam);
	bool startDebugging(unsigned int port);

	bool m_contextCreated;
	bool m_eventsHooked;
	bool m_firstNavigate;
	wchar_t* m_htmlToDisplay;
	bool m_isCurrentContext;
	wchar_t* m_lastUrl;
	HWND m_messageWindow;
	ICrossfireServer* m_server;
	int m_serverState;
	IWebBrowser2* m_webBrowser;

	static LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);

	/* constants */
	static const UINT ServerStateChangeMsg;
	static const wchar_t* ServerWindowClass;
	static const wchar_t* WindowClass;

	static const wchar_t* ABOUT_BLANK;
	static const wchar_t* DEBUG_START;
};

OBJECT_ENTRY_AUTO(__uuidof(IECrossfireBHO), IECrossfireBHO)
