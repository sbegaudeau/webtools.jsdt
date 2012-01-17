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


#include "stdafx.h"
#include "ExplorerBar.h"

/* initialize constants */
const UINT ExplorerBar::ServerStateChangeMsg = RegisterWindowMessage(L"IECrossfireServerStateChanged");
const wchar_t* ExplorerBar::ServerWindowClass = L"_IECrossfireServer";
const wchar_t* ExplorerBar::WindowClass = L"_ExplorerBarMessageWindow";


ExplorerBar::ExplorerBar() {
	m_hWnd = m_hWndParent = 0;
	m_pSite = NULL;
	m_server = NULL;
	m_serverPort = 0;
	m_serverState = STATE_DISCONNECTED;

	HKEY key;
	LONG result = RegOpenKeyEx(HKEY_CURRENT_USER, L"Software\\IBM\\IECrossfireServer", 0, KEY_QUERY_VALUE, &key);
	if (result != ERROR_SUCCESS && result != ERROR_FILE_NOT_FOUND) {
		Logger::error("ExplorerBar ctor: RegOpenKeyEx() failed", result);
	} else {
		DWORD size = sizeof(unsigned int);
		result = RegQueryValueEx(key, L"LastPort", NULL, NULL, (LPBYTE)&m_serverPort, &size);
		if (result != ERROR_SUCCESS) {
			Logger::error("ExplorerBar ctor: RegQueryValueEx() failed", result);
		}
		RegCloseKey(key);
	}
	if (!m_serverPort) {
		m_serverPort = 5000;
	}

	/* create a message-only window to receive server state change notifications */
	HINSTANCE module = GetModuleHandle(NULL);
	WNDCLASS ex;
	ex.style = 0;
	ex.lpfnWndProc = WndProc;
	ex.cbClsExtra = 0;
	ex.cbWndExtra = 0;
	ex.hInstance = module;
	ex.hIcon = NULL;
	ex.hCursor = NULL;
	ex.hbrBackground = NULL;
	ex.lpszMenuName = NULL;
	ex.lpszClassName = WindowClass;
	RegisterClass(&ex);
	m_messageWindow = CreateWindow(WindowClass, NULL, 0, 0, 0, 0, 0, HWND_MESSAGE, NULL, module, NULL);
	if (!m_messageWindow) {
		Logger::error("ExplorerBar ctor(): failed to create message-only window", GetLastError());
	} else {
		SetWindowLongPtr(m_messageWindow, GWL_USERDATA, (__int3264)(LONG_PTR)this);
	}
}

ExplorerBar::~ExplorerBar() {
	if (m_server) {
		m_server->Release();
	}
	if (m_messageWindow) {
		DestroyWindow(m_messageWindow);
		UnregisterClass(WindowClass, GetModuleHandle(NULL));
	}
}


/* IClassFactory */

STDMETHODIMP ExplorerBar::CreateInstance(IUnknown *pUnkOuter, REFIID riid, void **ppvObject) {
	CComObject<ExplorerBar>* explorerBar = NULL;
	HRESULT hr = CComObject<ExplorerBar>::CreateInstance(&explorerBar);
	if (FAILED(hr)) {
		Logger::error("ExplorerBar.CreateInstance(): CreateInstance() failed", hr);
		return S_FALSE;
	}
	explorerBar->AddRef();
	hr = explorerBar->QueryInterface(riid, ppvObject);
	explorerBar->Release();
	return hr;
}

STDMETHODIMP ExplorerBar::LockServer(BOOL fLock) {
	return S_OK;
}

/* IObjectWithSite */

STDMETHODIMP ExplorerBar::SetSite(IUnknown *punkSite) {
	if (m_pSite) {
		m_pSite->Release();
		m_pSite = NULL;
	}
	if (punkSite) {
		initServer(false);
		IOleWindow *pOleWindow;
		m_hWndParent = NULL;
		if (SUCCEEDED(punkSite->QueryInterface(IID_IOleWindow, (LPVOID*)&pOleWindow))) {
			pOleWindow->GetWindow(&m_hWndParent);
			pOleWindow->Release();
		}
		if (!m_hWndParent) {
			return E_FAIL;
		}
		if (!createWindow()) {
			return E_FAIL;
		}
		if (FAILED(punkSite->QueryInterface(IID_IInputObjectSite, (LPVOID*)&m_pSite))) {
			return E_FAIL;
		}
	}
	return S_OK;
}

STDMETHODIMP ExplorerBar::GetSite(REFIID riid, LPVOID *ppvReturn) {
	*ppvReturn = NULL;
	if (m_pSite) {
		return m_pSite->QueryInterface(riid, ppvReturn);
	}
	return E_FAIL;
}

/* IPersistStream */

STDMETHODIMP ExplorerBar::GetClassID(CLSID *pClassID) {
	*pClassID = CLSID_ExplorerBar;
	return S_OK;
}

STDMETHODIMP ExplorerBar::IsDirty() {
	return S_FALSE;
}

STDMETHODIMP ExplorerBar::Load(IStream *pStm) {
	return S_OK;
}

STDMETHODIMP ExplorerBar::Save(IStream *pStm, BOOL fClearDirty) {
	return S_OK;
}

STDMETHODIMP ExplorerBar::GetSizeMax(ULARGE_INTEGER *pcbSize) {
	return E_NOTIMPL;
}

/* IDeskBand */

STDMETHODIMP ExplorerBar::GetWindow(HWND *phwnd) {
	*phwnd = m_hWnd;
	return S_OK;
}

STDMETHODIMP ExplorerBar::ContextSensitiveHelp(BOOL fEnterMode) {
	return E_NOTIMPL;
}

STDMETHODIMP ExplorerBar::ShowDW(BOOL fShow) {
	if (m_hWnd) {
		ShowWindow(m_hWnd, fShow ? SW_SHOW : SW_HIDE);
	}
    return S_OK;
}

STDMETHODIMP ExplorerBar::CloseDW(DWORD dwReserved) {
	ShowWindow(m_hWnd, SW_HIDE);
	if (IsWindow(m_hWnd)) {
		DestroyWindow(m_hWnd);
	}
	m_hWnd = NULL;
	return S_OK;
}

STDMETHODIMP ExplorerBar::ResizeBorderDW(LPCRECT prcBorder, IUnknown *punkToolbarSite, BOOL fReserved) {
	return E_NOTIMPL;
}

STDMETHODIMP ExplorerBar::GetBandInfo(DWORD dwBandID, DWORD dwViewMode, DESKBANDINFO *pdbi) {
	if (pdbi) {
		if (pdbi->dwMask & DBIM_MINSIZE) {
			pdbi->ptMinSize.x = 0;
			pdbi->ptMinSize.y = 50;
		}

		if (pdbi->dwMask & DBIM_MAXSIZE) {
			pdbi->ptMaxSize.x = -1;
			pdbi->ptMaxSize.y = -1;
		}

		if (pdbi->dwMask & DBIM_INTEGRAL) {
			pdbi->ptIntegral.x = 1;
			pdbi->ptIntegral.y = 1;
		}

		if (pdbi->dwMask & DBIM_ACTUAL) {
			pdbi->ptMinSize.x = 0;
			pdbi->ptMinSize.y = 50;
		}

		if (pdbi->dwMask & DBIM_TITLE) {
			wchar_t* title = L"Crossfire";
			wcsncpy_s(pdbi->wszTitle, 256, title, wcslen(title));
        }

		if (pdbi->dwMask & DBIM_MODEFLAGS) {
			pdbi->dwModeFlags = DBIMF_NORMAL | DBIMF_VARIABLEHEIGHT;
		}

		if (pdbi->dwMask & DBIM_BKCOLOR) {
			pdbi->dwMask &= ~DBIM_BKCOLOR;
		}
		return S_OK;
	}

	return E_INVALIDARG;
}

/* IInputObject */

STDMETHODIMP ExplorerBar::UIActivateIO(BOOL fActivate, LPMSG pMsg) {
	if (fActivate) {
		SetFocus(m_hWnd);
	}
	return S_OK;
}

STDMETHODIMP ExplorerBar::HasFocusIO() {
	return m_bFocus ? S_OK : S_FALSE;
}

STDMETHODIMP ExplorerBar::TranslateAcceleratorIO(LPMSG pMsg) {
	return E_NOTIMPL;
}

/* ExplorerBar */

void ExplorerBar::createControls() {
	m_statusLabel = CreateWindowEx(
		0,
		WC_STATIC,
		NULL,
		WS_CHILD | WS_VISIBLE | WS_CLIPSIBLINGS | SS_NOTIFY | SS_LEFTNOWORDWRAP,
		0, 0, 0, 0,
		m_hWnd,
		0,
		g_hInst,
		(LPVOID)this);

	m_separator = CreateWindowEx(
		0,
		WC_STATIC,
		NULL,
		WS_CHILD | WS_VISIBLE | WS_CLIPSIBLINGS | SS_NOTIFY | SS_OWNERDRAW,
		0, 0, 0, 0,
		m_hWnd,
		0,
		g_hInst,
		(LPVOID)this);

	m_portLabel = CreateWindowEx(
		0,
		WC_STATIC,
		NULL,
		WS_CHILD | WS_VISIBLE | WS_CLIPSIBLINGS | SS_NOTIFY | SS_LEFTNOWORDWRAP,
		0, 0, 0, 0,
		m_hWnd,
		0,
		g_hInst,
		(LPVOID)this);
	SetWindowText(m_portLabel, L"Port:");

	m_portText = CreateWindowEx(
        WS_EX_CLIENTEDGE,
        WC_EDIT,
        NULL,
        WS_CHILD | WS_VISIBLE | ES_AUTOHSCROLL | WS_CLIPSIBLINGS | WS_TABSTOP | ES_NUMBER,
        0, 0, 0, 0,
        m_hWnd,
        0,
		g_hInst,
		(LPVOID)this);
	SendMessage(m_portText, EM_SETLIMITTEXT, 5, 0);

	m_portUpDown = CreateWindowEx(
        0,
		UPDOWN_CLASS,
        NULL,
        WS_CHILD | WS_VISIBLE | UDS_AUTOBUDDY | UDS_WRAP | UDS_ALIGNRIGHT | UDS_SETBUDDYINT | UDS_NOTHOUSANDS,
        0, 0, 0, 0,
        m_hWnd,
        0,
		g_hInst,
		(LPVOID)this);
	SendMessage(m_portUpDown, UDM_SETRANGE32, 1000, 65534);

	m_button = CreateWindowEx(
        0,
		WC_BUTTON,
        NULL,
		WS_CHILD | WS_VISIBLE | WS_CLIPSIBLINGS | BS_PUSHBUTTON | WS_TABSTOP,
        0, 0, 0, 0,
        m_hWnd,
        0,
		g_hInst,
		(LPVOID)this);

	m_errorLabel = CreateWindowEx(
		0,
		WC_STATIC,
		NULL,
		WS_CHILD | WS_VISIBLE | WS_CLIPSIBLINGS | SS_NOTIFY | SS_LEFTNOWORDWRAP,
		0, 0, 0, 0,
		m_hWnd,
		0,
		g_hInst,
		(LPVOID)this);

	/*
	 * Calculate the size of the port text and cache it.  This is done
	 * because its preferred size does not change, and to provide a
	 * baseline for vertical centering of the other controls.
	 */
	HDC hDC = GetDC(m_portText);
	LRESULT newFont = SendMessage(m_portText, WM_GETFONT, 0, 0);
	HGDIOBJ oldFont = SelectObject(hDC, (HGDIOBJ)newFont);
	TEXTMETRICW tm;
	GetTextMetrics(hDC, &tm);
	LONG height = tm.tmHeight;
	RECT rect;
	DrawText(hDC, L"000000", 6, &rect, DT_CALCRECT | DT_EDITCONTROL | DT_NOPREFIX);
	LONG width = rect.right - rect.left;
	if (newFont != 0) {
		SelectObject(hDC, oldFont);
	}
	ReleaseDC(m_portText, hDC);

	/* Factor in the size of the text control's trim */
	SetRect(&rect, 0, 0, width, height);
	LONG style = GetWindowLong(m_portText, GWL_STYLE);
	LONG exStyle = GetWindowLong(m_portText, GWL_EXSTYLE);
	AdjustWindowRectEx(&rect, style, false, exStyle);
	width = rect.right - rect.left;
	height = rect.bottom - rect.top;
	width += 2; height += 2;
	m_portTextSize.x = width;
	m_portTextSize.y = height;

	/* subclass the separator in order to owner-draw it */
//	SetWindowLongPtr(m_separator, GWL_USERDATA, (__int3264)(LONG_PTR)this);
//	staticProc = (WNDPROC)SetWindowLongPtr(m_separator, GWLP_WNDPROC, (__int3264)(LONG_PTR)WndProc);

//	Logger::error("handle: m_button", (int)m_button);
//	Logger::error("handle: m_errorLabel", (int)m_errorLabel);
//	Logger::error("handle: m_portLabel", (int)m_portLabel);
//	Logger::error("handle: m_portText", (int)m_portText);
//	Logger::error("handle: m_portUpDown", (int)m_portUpDown);
//	Logger::error("handle: m_separator", (int)m_separator);
//	Logger::error("handle: m_statusLabel", (int)m_statusLabel);
//	Logger::error("handle: m_hWnd", (int)m_hWnd);
//	Logger::error("handle: m_hWndParent", (int)m_hWndParent);
}

bool ExplorerBar::createWindow() {
	if (!m_hWnd) {
		if (!m_hWndParent) {
			return false;
		}

		LPCTSTR CLASSNAME = L"CrossfireExplorerBar";
		if (!GetClassInfo(g_hInst, CLASSNAME, &wc)) {
			ZeroMemory(&wc, sizeof(wc));
			wc.style = /*CS_HREDRAW | CS_VREDRAW |*/ CS_GLOBALCLASS;
			wc.lpfnWndProc = (WNDPROC)WndProc;
			wc.cbClsExtra = 0;
			wc.cbWndExtra = 0;
			wc.hInstance = g_hInst;
			wc.hIcon = NULL;
			wc.hCursor = LoadCursor(NULL, IDC_ARROW);
			wc.hbrBackground = (HBRUSH)GetStockObject(WHITE_BRUSH);
			wc.lpszMenuName = NULL;
			wc.lpszClassName = CLASSNAME;

			if (!RegisterClass(&wc)) {
				// If RegisterClass fails, CreateWindow below will fail.
			}
		}

		RECT rc;
		GetClientRect(m_hWndParent, &rc);

		CreateWindowEx(
			0,
			CLASSNAME,
			NULL,
			WS_CHILD | WS_CLIPSIBLINGS | WS_VISIBLE,
			rc.left,
			rc.top,
			rc.right - rc.left,
			rc.bottom - rc.top,
			m_hWndParent,
			NULL,
			g_hInst,
			(LPVOID)this);

		if (NULL != m_hWnd) {
			createControls();
			initServer(false);
			layoutControls();
		}
	}

	return NULL != m_hWnd;
}

bool ExplorerBar::initServer(bool startIfNeeded) {
	if (m_server) {
		return true;
	}
	if (!startIfNeeded && !FindWindow(ServerWindowClass, NULL)) {
		return false;
	}

	if (!Util::VerifyActiveScriptDebugger() || !Util::VerifyDebugPreference()) {
		return false;
	}

	CComPtr<ICrossfireServerClass> serverClass = NULL;
	HRESULT hr = CoGetClassObject(CLSID_CrossfireServer, CLSCTX_ALL, 0, IID_ICrossfireServerClass, (LPVOID*)&serverClass);
	if (FAILED(hr)) {
		Logger::error("ExplorerBar.initServer(): CoGetClassObject() failed", hr);
		return false;
	}

//	HWND rootWindow = GetAncestor(m_hWndParent, GA_ROOT);
	hr = serverClass->GetServer(/*(unsigned long)rootWindow,*/ &m_server);
	if (FAILED(hr)) {
		Logger::error("ExplorerBar.initServer(): GetController() failed", hr);
		return false;
	}

	hr = m_server->getState(&m_serverState);
	if (FAILED(hr)) {
		Logger::error("ExplorerBar.initServer(): getState() failed", hr);
	}

	if (m_serverState != STATE_DISCONNECTED) {
		hr = m_server->getPort(&m_serverPort);
		if (FAILED(hr)) {
			Logger::error("ExplorerBar.initServer(): getPort() failed", hr);
		}
	}

	return true;
}

void ExplorerBar::layoutControls() {
	int x = SPACING_WIDTH;
	int y = SPACING_WIDTH;
	const int BUFFER_SIZE = 64;

	/* status label is always shown */
	switch (m_serverState) {
		case STATE_DISCONNECTED: {
			SetWindowText(m_statusLabel, L"Crossfire Debugger State: Disconnected");
			break;
		}
		case STATE_LISTENING: {
			std::wstring result;
			std::wstringstream stringStream;
			stringStream << L"Crossfire Debugger State: Listening on port ";
			stringStream << m_serverPort;
			result.assign(stringStream.str());
			SetWindowText(m_statusLabel, result.c_str());
			break;
		}
		case STATE_CONNECTED: {
			std::wstring result;
			std::wstringstream stringStream;
			stringStream << L"Crossfire Debugger State: Connected on port ";
			stringStream << m_serverPort;
			result.assign(stringStream.str());
			SetWindowText(m_statusLabel, result.c_str());
			break;
		}
	}

	HDC hDC = GetDC(m_statusLabel);
	LRESULT newFont = SendMessage(m_statusLabel, WM_GETFONT, 0, 0);
	HGDIOBJ oldFont = SelectObject(hDC, (HGDIOBJ)newFont);
	int length = GetWindowTextLength(m_statusLabel);
	RECT rect;
	TCHAR buffer[BUFFER_SIZE];
	GetWindowText(m_statusLabel, buffer, BUFFER_SIZE);
	DrawText(hDC, buffer, length, &rect, DT_CALCRECT);
	LONG width = rect.right - rect.left;
	LONG height = rect.bottom - rect.top;
	if (newFont) {
		SelectObject(hDC, oldFont);
	}
	ReleaseDC(m_statusLabel, hDC);
	SetWindowPos(m_statusLabel, 0, x, y + (m_portTextSize.y - height) / 2, width, height, SWP_NOZORDER | SWP_NOACTIVATE);
	x += width + SPACING_WIDTH;

	/* vertical separator is always shown */
	EnableWindow(m_separator, m_server != NULL);
	SetWindowPos(m_separator, 0, x, y, SEPARATOR_WIDTH, m_portTextSize.y, SWP_NOZORDER | SWP_NOACTIVATE);
	x += SEPARATOR_WIDTH + SPACING_WIDTH;

	/* "port:" label */
	if (m_serverState != STATE_DISCONNECTED) {
		ShowWindow(m_portLabel, SW_HIDE);
	} else {
		ShowWindow(m_portLabel, SW_SHOW);
		hDC = GetDC(m_portLabel);
		newFont = SendMessage(m_portLabel, WM_GETFONT, 0, 0);
		oldFont = SelectObject(hDC, (HGDIOBJ)newFont);
		length = GetWindowTextLength(m_portLabel);
		GetWindowText(m_portLabel, buffer, BUFFER_SIZE);
		DrawText(hDC, buffer, length, &rect, DT_CALCRECT);
		width = rect.right - rect.left;
		height = rect.bottom - rect.top;
		if (newFont) {
			SelectObject(hDC, oldFont);
		}
		ReleaseDC(m_portLabel, hDC);
		SetWindowPos(m_portLabel, 0, x, y + (m_portTextSize.y - height) / 2, width, height, SWP_NOZORDER | SWP_NOACTIVATE);
		x += width + SPACING_WIDTH;
	}

	/* port text */
	if (m_serverState != STATE_DISCONNECTED) {
		ShowWindow(m_portText, SW_HIDE);
		ShowWindow(m_portUpDown, SW_HIDE);
	} else {
		SendMessage(m_portUpDown, UDM_SETPOS32, 0, m_serverPort);
		ShowWindow(m_portText, SW_SHOW);
		ShowWindow(m_portUpDown, SW_SHOW);

		SetWindowPos(m_portText, 0, x, y, m_portTextSize.x, m_portTextSize.y, SWP_NOZORDER | SWP_NOACTIVATE);
		x += m_portTextSize.x;

		width = GetSystemMetrics(SM_CXVSCROLL);
		SetWindowPos(m_portUpDown, 0, x, y, width, m_portTextSize.y, SWP_NOZORDER | SWP_NOACTIVATE);
		x += width + SEPARATOR_WIDTH;
	}

	/* button is always shown */
	switch (m_serverState) {
		case STATE_DISCONNECTED: {
			SetWindowText(m_button, L"Listen");
			break;
		}
		case STATE_LISTENING: {
			SetWindowText(m_button, L"Stop");
			break;
		}
		case STATE_CONNECTED: {
			SetWindowText(m_button, L"Disconnect");
			break;
		}
	}
	hDC = GetDC(m_button);
	newFont = SendMessage(m_button, WM_GETFONT, 0, 0);
	if (newFont != 0) {
		oldFont = SelectObject(hDC, (HGDIOBJ)newFont);
	}
	GetWindowText(m_button, buffer, BUFFER_SIZE);
	DrawText(hDC, buffer, -1, &rect, DT_CALCRECT | DT_SINGLELINE);
	width = rect.right - rect.left;
	height = rect.bottom - rect.top;
	if (newFont != 0) {
		SelectObject(hDC, oldFont);
	}
	ReleaseDC(m_button, hDC);
	width += 10;
	SetWindowPos(m_button, 0, x, y, width, m_portTextSize.y, SWP_NOZORDER | SWP_NOACTIVATE);
	x += width + SEPARATOR_WIDTH;

	/* clear the error text */
	SetWindowPos(m_errorLabel, 0, x, y + (m_portTextSize.y - height) / 2, 0, 0, SWP_NOZORDER | SWP_NOACTIVATE | SWP_NOSIZE);
	setErrorText(L"");
}

bool ExplorerBar::onCommand(HWND hWnd, WPARAM wParam, LPARAM lParam) {
	if ((HWND)lParam != m_button) {
		return true;
	}

	if (HIWORD(wParam) != BN_CLICKED) {
		return true;
	}

	switch (m_serverState) {
		case STATE_LISTENING:
		case STATE_CONNECTED: {
			if (FAILED(m_server->stop())) {
				setErrorText(L"Failed to stop the Crossfire server");
			}
			break;
		}
		case STATE_DISCONNECTED: {
			TCHAR buffer[9];
			if (GetWindowText(m_portText, buffer, 9)) {
				int port = _wtoi(buffer);
				if (!(1000 <= port && port <= 65534)) {
					setErrorText(L"Valid port range: 1000-65534");
				} else {
					if (!initServer(true)) {
						setErrorText(L"Failed to initialize Crossfire server");
					} else {
						if (FAILED(m_server->start(port, 54124 /* debug port */))) {
							setErrorText(L"Failed to start the Crossfire server");
						}
					}
				}
			}
		}
	}

	return true;
}

bool ExplorerBar::onNCCreate(HWND hWnd, WPARAM wParam, LPARAM lParam) {
	if (!m_hWnd) {
		m_hWnd = hWnd;
	}
	return true;
}

//bool ExplorerBar::onPaint(HWND hWnd, WPARAM wParam, LPARAM lParam) {
//	if (hWnd != m_separator) {
//		return true;
//	}
//
//	RECT clientRect;
//	GetClientRect(m_separator, &clientRect);
//	int lineWidth = GetSystemMetrics (SM_CXBORDER);
//	clientRect.right = clientRect.left + lineWidth * 2;
//	HDC hDC = GetDC(m_separator);
//	DrawEdge(hDC, &clientRect, EDGE_ETCHED, BF_RIGHT);
//	ReleaseDC(m_separator, hDC);
//    return false;
//}

void ExplorerBar::onServerStateChanged(WPARAM wParam, LPARAM lParam) {
	m_serverState = wParam;
	if (m_serverState != STATE_DISCONNECTED) {
		m_serverPort = lParam;
	}
	initServer(false);
	layoutControls();

	if (m_serverState == STATE_CONNECTED) {
		HKEY key;
		LONG result = RegCreateKeyEx(HKEY_CURRENT_USER, L"Software\\IBM\\IECrossfireServer", 0, NULL, REG_OPTION_NON_VOLATILE, KEY_WRITE, NULL, &key, NULL);
		if (result != ERROR_SUCCESS) {
			Logger::error("ExplorerBar.serverStateChanged(): RegCreateKeyEx() failed", result);
		} else {
			result = RegSetValueEx(key, L"LastPort", 0, REG_DWORD, (BYTE*)&m_serverPort, sizeof(unsigned int));
			if (result != ERROR_SUCCESS) {
				Logger::error("ExplorerBar.serverStateChanged(): RegSetValueEx() failed", result);
			}
			RegCloseKey(key);
		}
	}
}

LRESULT ExplorerBar::onWndProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam) {
	bool result = true;
	if (msg == ServerStateChangeMsg) {
		onServerStateChanged(wParam, lParam);
		return 0;
	}

	switch (msg) {
		case WM_COMMAND: {
			result = onCommand(hWnd, wParam, lParam);
			break;
		}
		case WM_NCCREATE: {
			result = onNCCreate(hWnd, wParam, lParam);
			break;
		}
		case WM_CTLCOLORSTATIC: {
			return (LRESULT)GetStockObject(NULL_BRUSH);
		}
//		case WM_PAINT: {
//			result = onPaint(hWnd, wParam, lParam);
//			break;
//		}
	}
	if (!result) {
		return 1;
	}
//	if (hWnd == m_separator) {
//		return CallWindowProc(staticProc, hWnd, msg, wParam, lParam);
//	}
	return DefWindowProc(hWnd, msg, wParam, lParam);
}

void ExplorerBar::setErrorText(wchar_t* text) {
	SetWindowText(m_errorLabel, text);
	int length = (int)wcslen(text);
	if (length) {
		HDC hDC = GetDC(m_errorLabel);
		LRESULT newFont = SendMessage(m_errorLabel, WM_GETFONT, 0, 0);
		HGDIOBJ oldFont = SelectObject(hDC, (HGDIOBJ)newFont);
		RECT rect;
		DrawText(hDC, text, length, &rect, DT_CALCRECT);
		LONG width = rect.right - rect.left;
		LONG height = rect.bottom - rect.top;
		if (newFont) {
			SelectObject(hDC, oldFont);
		}
		ReleaseDC(m_errorLabel, hDC);
		SetWindowPos(m_errorLabel, 0, 0, 0, width, height, SWP_NOZORDER | SWP_NOACTIVATE | SWP_NOMOVE);
		ShowWindow(m_errorLabel, SW_SHOW);
	} else {
		ShowWindow(m_errorLabel, SW_HIDE);
		SetWindowPos(m_errorLabel, 0, 0, 0, 1, 1, SWP_NOZORDER | SWP_NOACTIVATE | SWP_NOMOVE);
	}
}

LRESULT CALLBACK ExplorerBar::WndProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam) {
	ExplorerBar *pThis = (ExplorerBar*)GetWindowLongPtr(hWnd, GWL_USERDATA);
	if (!pThis && msg == WM_NCCREATE) {
		LPCREATESTRUCT lpcs = (LPCREATESTRUCT)lParam;
		pThis = (ExplorerBar*)(lpcs->lpCreateParams);
		SetWindowLongPtr(hWnd, GWL_USERDATA, (__int3264)(LONG_PTR)pThis);
	}

	if (pThis) {
		return pThis->onWndProc(hWnd, msg, wParam, lParam);
	}

	return DefWindowProc(hWnd, msg, wParam, lParam);
}
