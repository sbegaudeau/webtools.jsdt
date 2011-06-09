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
#include <Shlobj.h>
#include <ShlGuid.h>

#include "IECrossfireExtension.h"
#include "Logger.h"

enum {
	STATE_DISCONNECTED,
	STATE_LISTENING,
	STATE_CONNECTED,
};

class ATL_NO_VTABLE CExplorerBar :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<CExplorerBar, &CLSID_ExplorerBar>,
	public IClassFactory,
	public IObjectWithSite,
	public IPersistStream,
	public IDeskBand,
	public IInputObject,
	public ICrossfireServerListener  {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_EXPLORERBAR)
	DECLARE_NOT_AGGREGATABLE(CExplorerBar)
	BEGIN_COM_MAP(CExplorerBar)
		COM_INTERFACE_ENTRY_IID(IID_IInputObject, IInputObject)
//		COM_INTERFACE_ENTRY(IExplorerBar)
		COM_INTERFACE_ENTRY(IClassFactory)
		COM_INTERFACE_ENTRY(IObjectWithSite)
		COM_INTERFACE_ENTRY(IPersistStream)
		COM_INTERFACE_ENTRY(IDeskBand)
	//	COM_INTERFACE_ENTRY(IInputObject)
		COM_INTERFACE_ENTRY(ICrossfireServerListener)
	END_COM_MAP()
	DECLARE_PROTECT_FINAL_CONSTRUCT()

	HRESULT FinalConstruct() {
		return S_OK;
	}

	void FinalRelease() {
	}

public:
	CExplorerBar();
	~CExplorerBar();

	/* IClassFactory */
	virtual HRESULT STDMETHODCALLTYPE CreateInstance(IUnknown *pUnkOuter, REFIID riid, void **ppvObject);
    virtual HRESULT STDMETHODCALLTYPE LockServer(BOOL fLock);

	/* IObjectWithSite */
	virtual HRESULT STDMETHODCALLTYPE GetSite(REFIID riid, LPVOID *ppvReturn);
	virtual HRESULT STDMETHODCALLTYPE SetSite(IUnknown *pUnkSite);

	/* IPersistStream */
	virtual HRESULT STDMETHODCALLTYPE GetClassID(CLSID *pClassID);
	virtual HRESULT STDMETHODCALLTYPE IsDirty();
	virtual HRESULT STDMETHODCALLTYPE Load(IStream *pStm);
	virtual HRESULT STDMETHODCALLTYPE Save(IStream *pStm, BOOL fClearDirty);
	virtual HRESULT STDMETHODCALLTYPE GetSizeMax(ULARGE_INTEGER *pcbSize);

	/* IDeskBand */
	virtual HRESULT STDMETHODCALLTYPE GetWindow(HWND *phwnd);
	virtual HRESULT STDMETHODCALLTYPE ContextSensitiveHelp(BOOL fEnterMode);
	virtual HRESULT STDMETHODCALLTYPE ShowDW(BOOL fShow);
	virtual HRESULT STDMETHODCALLTYPE CloseDW(DWORD dwReserved);        
	virtual HRESULT STDMETHODCALLTYPE ResizeBorderDW(LPCRECT prcBorder, IUnknown *punkToolbarSite, BOOL fReserved);    
	virtual HRESULT STDMETHODCALLTYPE GetBandInfo(DWORD dwBandID, DWORD dwViewMode, DESKBANDINFO *pdbi);

	/* IInputObject */
    virtual HRESULT STDMETHODCALLTYPE UIActivateIO(BOOL fActivate, LPMSG lpMsg);
    virtual HRESULT STDMETHODCALLTYPE HasFocusIO();
    virtual HRESULT STDMETHODCALLTYPE TranslateAcceleratorIO(LPMSG lpMsg);

	/* ICrossfireServerListener */
	virtual HRESULT STDMETHODCALLTYPE navigate(OLECHAR* href, boolean openNewTab);
	virtual HRESULT STDMETHODCALLTYPE serverStateChanged(int state, unsigned int port);

private:
	bool createWindow();
	void createControls();
	bool initServer(bool startIfNeeded);
	void layoutControls();
	bool onCommand(HWND hWnd, WPARAM wParam, LPARAM lParam);
	bool onNCCreate(HWND hWnd, WPARAM wParam, LPARAM lParam);
//	bool onPaint(HWND hWnd, WPARAM wParam, LPARAM lParam);
	LRESULT onWndProc(HWND hWnd, UINT uMessage, WPARAM wParam, LPARAM lParam);
	void setErrorText(wchar_t* text);

	static LRESULT CALLBACK WndProc(HWND hWnd, UINT uMessage, WPARAM wParam, LPARAM lParam);

	bool m_bFocus;
	HINSTANCE g_hInst;
	HWND m_hWnd;
	HWND m_hWndParent;
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

	/* constants */
	static const wchar_t* PREFERENCE_DISABLEIEDEBUG;
	static const int SEPARATOR_WIDTH = 5;
	static const int SPACING_WIDTH = 10;
};

OBJECT_ENTRY_AUTO(__uuidof(ExplorerBar), CExplorerBar)
