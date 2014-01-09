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
#include <Shlobj.h>
#include <ShlGuid.h>

#include "IECrossfireExtension.h"
#include "Util.h"
#include "Logger.h"

enum {
	STATE_DISCONNECTED,
	STATE_LISTENING,
	STATE_CONNECTED,
};

class ATL_NO_VTABLE ExplorerBar :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<ExplorerBar, &CLSID_ExplorerBar>,
	public IClassFactory,
	public IObjectWithSite,
	public IPersistStream,
	public IDeskBand,
	public IInputObject {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_EXPLORERBAR)
	DECLARE_NOT_AGGREGATABLE(ExplorerBar)
	BEGIN_COM_MAP(ExplorerBar)
		COM_INTERFACE_ENTRY_IID(IID_IInputObject, IInputObject)
//		COM_INTERFACE_ENTRY(IExplorerBar)
		COM_INTERFACE_ENTRY(IClassFactory)
		COM_INTERFACE_ENTRY(IObjectWithSite)
		COM_INTERFACE_ENTRY(IPersistStream)
		COM_INTERFACE_ENTRY(IDeskBand)
	//	COM_INTERFACE_ENTRY(IInputObject)
	END_COM_MAP()
	DECLARE_PROTECT_FINAL_CONSTRUCT()

	HRESULT FinalConstruct() {
		return S_OK;
	}

	void FinalRelease() {
	}

public:
	ExplorerBar();
	virtual ~ExplorerBar();

	/* IClassFactory */
	HRESULT STDMETHODCALLTYPE CreateInstance(IUnknown *pUnkOuter, REFIID riid, void **ppvObject);
    HRESULT STDMETHODCALLTYPE LockServer(BOOL fLock);

	/* IObjectWithSite */
	HRESULT STDMETHODCALLTYPE GetSite(REFIID riid, LPVOID *ppvReturn);
	HRESULT STDMETHODCALLTYPE SetSite(IUnknown *pUnkSite);

	/* IPersistStream */
	HRESULT STDMETHODCALLTYPE GetClassID(CLSID *pClassID);
	HRESULT STDMETHODCALLTYPE IsDirty();
	HRESULT STDMETHODCALLTYPE Load(IStream *pStm);
	HRESULT STDMETHODCALLTYPE Save(IStream *pStm, BOOL fClearDirty);
	HRESULT STDMETHODCALLTYPE GetSizeMax(ULARGE_INTEGER *pcbSize);

	/* IDeskBand */
	HRESULT STDMETHODCALLTYPE GetWindow(HWND *phwnd);
	HRESULT STDMETHODCALLTYPE ContextSensitiveHelp(BOOL fEnterMode);
	HRESULT STDMETHODCALLTYPE ShowDW(BOOL fShow);
	HRESULT STDMETHODCALLTYPE CloseDW(DWORD dwReserved);        
	HRESULT STDMETHODCALLTYPE ResizeBorderDW(LPCRECT prcBorder, IUnknown *punkToolbarSite, BOOL fReserved);    
	HRESULT STDMETHODCALLTYPE GetBandInfo(DWORD dwBandID, DWORD dwViewMode, DESKBANDINFO *pdbi);

	/* IInputObject */
    HRESULT STDMETHODCALLTYPE UIActivateIO(BOOL fActivate, LPMSG lpMsg);
    HRESULT STDMETHODCALLTYPE HasFocusIO();
    HRESULT STDMETHODCALLTYPE TranslateAcceleratorIO(LPMSG lpMsg);

private:
	bool createWindow();
	void createControls();
	bool initServer(bool startIfNeeded);
	void layoutControls();
	bool onCommand(HWND hWnd, WPARAM wParam, LPARAM lParam);
	bool onNCCreate(HWND hWnd, WPARAM wParam, LPARAM lParam);
//	bool onPaint(HWND hWnd, WPARAM wParam, LPARAM lParam);
	LRESULT onWndProc(HWND hWnd, UINT uMessage, WPARAM wParam, LPARAM lParam);
	void onServerStateChanged(WPARAM wParam, LPARAM lParam);
	void setErrorText(wchar_t* text);

	bool m_bFocus;
	HINSTANCE g_hInst;
	HWND m_hWnd;
	HWND m_hWndParent;
	HWND m_messageWindow;
	POINT m_portTextSize;
	IInputObjectSite* m_pSite;
	ICrossfireServer* m_server;
	unsigned int m_serverPort;
	int m_serverState;
	WNDCLASS wc;

	/* controls */
	HWND m_button;
	HWND m_errorLabel;
	HWND m_portLabel;
	HWND m_portText;
	HWND m_portUpDown;
	HWND m_separator;
	HWND m_statusLabel;

	static LRESULT CALLBACK WndProc(HWND hWnd, UINT uMessage, WPARAM wParam, LPARAM lParam);

	/* constants */
	static const UINT ServerStateChangeMsg;
	static const wchar_t* ServerWindowClass;
	static const wchar_t* WindowClass;

	static const int SEPARATOR_WIDTH = 5;
	static const int SPACING_WIDTH = 10;
};

OBJECT_ENTRY_AUTO(__uuidof(ExplorerBar), ExplorerBar)
