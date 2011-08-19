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


#include "stdafx.h"
#include "IECrossfireBHO.h"

/* initialize constants */
const wchar_t* IECrossfireBHO::ServerWindowClass = L"_IECrossfireServer";

const wchar_t* IECrossfireBHO::ABOUT_BLANK = L"about:blank";
const wchar_t* IECrossfireBHO::DEBUG_START = L"-crossfire-server-port";
const wchar_t* IECrossfireBHO::PREFERENCE_DISABLEIEDEBUG = L"DisableScriptDebuggerIE";

IECrossfireBHO::IECrossfireBHO() {
	m_eventsHooked = false;
	m_firstNavigate = true;
	m_htmlToDisplay = NULL;
	m_lastUrl = NULL;
	m_server = NULL;
	m_serverState = STATE_DISCONNECTED;
	m_webBrowser = NULL;
}

IECrossfireBHO::~IECrossfireBHO() {
	DWORD processId = GetCurrentProcessId();
	if (m_server) {
		m_server->contextDestroyed(processId);
		m_server->Release();
	}
	if (m_htmlToDisplay) {
		free(m_htmlToDisplay);
	}
	if (m_lastUrl) {
		free(m_lastUrl);
	}
}

/* DWebBrowserEvents2 */

void STDMETHODCALLTYPE IECrossfireBHO::OnBeforeNavigate2(IDispatch* pDisp, VARIANT* URL, VARIANT* Flags, VARIANT* TargetFrameName, VARIANT* PostData, VARIANT* Headers, VARIANT_BOOL* Cancel) {
	if (!m_firstNavigate) {
		return;
	}

	std::wstring string(URL->bstrVal);
	size_t index = string.find(DEBUG_START);
	if (index == std::string::npos) {
		return;
	}

	*Cancel = VARIANT_TRUE;

	size_t equalsIndex;
	if ((equalsIndex = string.find('=', index)) != index + wcslen(DEBUG_START)) {
		if ((equalsIndex = string.find(L"%3D", index)) != index + wcslen(DEBUG_START)) {
			displayHTML(L"<html><body>Did not start Crossfire Server<p>Command-line syntax: <tt>iexplore.exe -crossfire-server-port=&lt;port&gt;</tt></body></html>");
			return;
		}
		equalsIndex += 2; /* because found "%3D" instead of "=" */
	}

	index = equalsIndex + 1;
	int port = _wtoi(string.substr(index, 5).c_str());

	if (1000 <= port && port <= 65534) {
		if (startDebugging(port)) {
			std::wstringstream stream;
			stream << "<html><body>Crossfire Server started on port ";
			stream << port;
			stream << "</body></html>";
			displayHTML((wchar_t*)stream.str().c_str());
		} else {
			std::wstringstream stream;
			stream << "<html><body>Failed to start Crossfire Server on port ";
			stream << port;
			stream << "</body></html>";
			displayHTML((wchar_t*)stream.str().c_str());
		}
	} else {
		displayHTML(L"<html><body>Did not start Crossfire Server<p>Valid port range is 1000-65534</body></html>");
	}
}

void STDMETHODCALLTYPE IECrossfireBHO::OnDocumentComplete(IDispatch *pDisp, VARIANT *pvarURL) {
	BSTR bstr = pvarURL->bstrVal;
	if (bstr && m_htmlToDisplay && wcscmp(bstr, ABOUT_BLANK) == 0) {
		CComPtr<IUnknown> webBrowserIUnknown = NULL;
		HRESULT hr = m_webBrowser->QueryInterface(IID_IUnknown, (void**)&webBrowserIUnknown);
		if (SUCCEEDED(hr)) {
			CComPtr<IUnknown> pDispIUnknown = NULL;
			hr = pDisp->QueryInterface(IID_IUnknown, (void**)&pDispIUnknown);
			if (SUCCEEDED(hr)) {
				if (webBrowserIUnknown == pDispIUnknown) {
					/* this is the top-level page frame */
					size_t length = (wcslen(m_htmlToDisplay) + 1) * 2;
					HGLOBAL buffer = GlobalAlloc(GPTR, length);
					if (buffer) {
						wcscpy_s((wchar_t*)buffer, length, m_htmlToDisplay);
						CComPtr<IStream> stream = NULL;
						HRESULT hr = CreateStreamOnHGlobal(buffer, false, &stream);
						if (SUCCEEDED(hr)) {
							CComPtr<IDispatch> document = NULL;
							HRESULT hr = m_webBrowser->get_Document(&document);
							if (SUCCEEDED(hr)) {
								CComPtr<IPersistStreamInit> persistStreamInit = NULL;
								hr = document->QueryInterface(IID_IPersistStreamInit, (void**)&persistStreamInit);
								if (SUCCEEDED(hr)) {
									hr = persistStreamInit->InitNew();
									if (SUCCEEDED(hr)) {
										hr = persistStreamInit->Load(stream);
									}
								}
							}
							if (FAILED(hr)) {
								Logger::error("IECrossfireBHO.OnDocumentComplete(): failed setting page content", hr);
							}
						} else {
							Logger::error("IECrossfireBHO.OnDocumentComplete(): CreateStreamOnHGlobal() failed", hr);
						}
						GlobalFree(buffer);
					}
					free(m_htmlToDisplay);
					m_htmlToDisplay = NULL;
				}
			}
		}
		if (FAILED(hr)) {
			Logger::error("IECrossfireBHO.OnDocumentComplete() failed", hr);
		}
		return;
	}

	if (getServerState() != STATE_CONNECTED) {
		return;
	}

	CComPtr<IUnknown> webBrowserIUnknown = NULL;
	HRESULT hr = m_webBrowser->QueryInterface(IID_IUnknown, (void**)&webBrowserIUnknown);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.OnDocumentComplete(): QI(IUnknown)[1] failed", hr);
	} else {
		CComPtr<IUnknown> pDispIUnknown = NULL;
		hr = pDisp->QueryInterface(IID_IUnknown, (void**)&pDispIUnknown);
		if (FAILED(hr)) {
			Logger::error("IECrossfireBHO.OnDocumentComplete(): QI(IUnknown)[2] failed", hr);
		} else {
			if (webBrowserIUnknown == pDispIUnknown) {
				/* this is the top-level page frame */
				HRESULT hr = m_server->contextLoaded(GetCurrentProcessId());
				if (FAILED(hr)) {
					Logger::error("IECrossfireBHO.OnDocumentComplete(): contextLoaded() failed", hr);
				}
			}
		}
	}
}

void STDMETHODCALLTYPE IECrossfireBHO::OnNavigateComplete2(IDispatch *pDisp, VARIANT *pvarURL) {
	if (getServerState() != STATE_CONNECTED) {
		return;
	}

	CComPtr<IUnknown> webBrowserIUnknown = NULL;
	HRESULT hr = m_webBrowser->QueryInterface(IID_IUnknown, (void**)&webBrowserIUnknown);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.OnNavigateComplete2(): QI(IUnknown)[1] failed", hr);
	} else {
		CComPtr<IUnknown> pDispIUnknown = NULL;
		hr = pDisp->QueryInterface(IID_IUnknown, (void**)&pDispIUnknown);
		if (FAILED(hr)) {
			Logger::error("IECrossfireBHO.OnNavigateComplete2(): QI(IUnknown)[2] failed", hr);
		} else {
			if (webBrowserIUnknown == pDispIUnknown) {
				/* this is the top-level page frame */
				wchar_t* url = pvarURL->bstrVal;
				wchar_t* hash = wcschr(url, wchar_t('#'));
				if (!hash || m_firstNavigate || wcsncmp(url, m_lastUrl, hash - url) != 0) {
					DWORD processId = GetCurrentProcessId();
					HRESULT hr = m_server->contextCreated(processId, url);
					if (FAILED(hr)) {
						Logger::error("IECrossfireBHO.OnNavigateComplete2(): contextCreated() failed", hr);
					}
				}
				m_firstNavigate = false;
				if (m_lastUrl) {
					free(m_lastUrl);
				}
				m_lastUrl = _wcsdup(url);
			}
		}
	}
}

void STDMETHODCALLTYPE IECrossfireBHO::OnWindowStateChanged(LONG dwFlags, LONG dwValidFlagMask) {
	if (m_firstNavigate || getServerState() != STATE_CONNECTED) {
		return;
	}

	if (dwFlags != (OLECMDIDF_WINDOWSTATE_USERVISIBLE | OLECMDIDF_WINDOWSTATE_ENABLED)) {
		return;
	}

	CComBSTR url = NULL;
	HRESULT hr = m_webBrowser->get_LocationURL(&url);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.OnWindowStateChanged(): get_LocationURL() failed");
		return;
	}

	DWORD processId = GetCurrentProcessId();
	hr = m_server->setCurrentContext(processId);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.OnWindowStateChanged(): setCurrentContext() failed");
		return;
	}
}

/* ICrossfireServerListener */

STDMETHODIMP IECrossfireBHO::navigate(OLECHAR* url, boolean openNewTab) {
	VARIANT variant_null;
	variant_null.vt = VT_NULL;

	VARIANT variant_url;
	variant_url.vt = VT_BSTR;
	variant_url.bstrVal = url;

	VARIANT variant_flags;
	if (openNewTab) {
		variant_flags.vt = VT_I4;
		variant_flags.intVal = navOpenInNewTab;
	} else {
		variant_flags = variant_null;
	}

	HRESULT hr = m_webBrowser->Navigate2(&variant_url, &variant_flags, &variant_null, &variant_null, &variant_null);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.navigate(): Navigate2() failed", hr);
	}
	return hr;
}

STDMETHODIMP IECrossfireBHO::serverStateChanged(int state, unsigned int port) {
	m_serverState = state;

	/* If a connection was just established then create a context on the server for the current page */
	if (m_serverState != STATE_CONNECTED) {
		return S_OK;
	}
	
	CComBSTR url = NULL;
	HRESULT hr = m_webBrowser->get_LocationURL(&url);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.serverStateChanged(): get_LocationURL() failed", hr);
		return S_OK;
	}

	DWORD processId = GetCurrentProcessId();
	hr = m_server->contextCreated(processId, url);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.serverStateChanged(): contextCreated() failed", hr);
		return S_OK;
	}

	/*
	 * If the current page is fully-loaded then inform the server.  If the current
	 * page is still loading then the server will be notified of its completion
	 * from the usual OnDocumentComplete listener.
	 */
	VARIANT_BOOL busy;
	hr = m_webBrowser->get_Busy(&busy);
	if (SUCCEEDED(hr) && !busy) {
		hr = m_server->contextLoaded(processId);
		if (FAILED(hr)) {
			Logger::error("IECrossfireBHO.serverStateChanged(): contextLoaded() failed", hr);
			return S_OK;
		}
	}

	return S_OK;
}

/* IObjectWithSite */

STDMETHODIMP IECrossfireBHO::GetSite(REFIID riid, LPVOID *ppvReturn) {
    *ppvReturn = NULL;
	if (m_webBrowser) {
        return m_webBrowser->QueryInterface(riid, ppvReturn);
	}
    return E_FAIL;
}

STDMETHODIMP IECrossfireBHO::SetSite(IUnknown* pUnkSite) {
	HRESULT hr = S_OK;
	if (pUnkSite) {
		if (m_webBrowser) {
			m_webBrowser->Release();
			m_webBrowser = NULL;
		}
		hr = pUnkSite->QueryInterface(IID_IWebBrowser2, (void**)&m_webBrowser);
		if (FAILED(hr)) {
			Logger::error("IECrossfireBHO.SetSite(): QI(IWebBrowser2) failed", hr);
		} else {
			hr = DispEventAdvise(m_webBrowser);
			if (FAILED(hr)) {
				Logger::error("IECrossfireBHO.SetSite(): DispEventAdvise() failed", hr);
			}
		}
	} else {
		if (m_eventsHooked) {
			HRESULT hr = DispEventUnadvise(m_webBrowser);
			if (SUCCEEDED(hr)) {
				m_eventsHooked = false;
			} else {
				Logger::error("IECrossfireBHO.SetSite(): DispEventUnadvise() failed", hr);
			}
		}
		m_webBrowser->Release();
		m_webBrowser = NULL;
	}

	//return IObjectWithSiteImpl<IECrossfireBHO>::SetSite(pUnkSite);
	return hr;
}

/* IECrossfireBHO */

bool IECrossfireBHO::displayHTML(wchar_t* htmlText) {
	VARIANT variant;
	variant.vt = VT_NULL;
	CComBSTR bstr(ABOUT_BLANK);
	HRESULT hr = m_webBrowser->Navigate(bstr, &variant, &variant, &variant, &variant);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.displayHTML(): Navigate failed", hr);
		return false;
	}
	m_htmlToDisplay = _wcsdup(htmlText);
	return true;
}

int IECrossfireBHO::getServerState() {
	initServer(false);
	if (!m_server) {
		return STATE_DISCONNECTED;
	}

	int state;
	HRESULT hr = m_server->getState(&state);
	if (FAILED(hr)) {
		return STATE_DISCONNECTED;
	}

	return state;
}

bool IECrossfireBHO::initServer(bool startIfNeeded) {
	if (m_server) {
		return true;
	}

	if (!startIfNeeded) {
		if (!FindWindow(ServerWindowClass, NULL)) {
			Logger::error("didn't find it, run away");
			return false;
		} else {
			Logger::error("found it, proceed!");
		}
	}

	CComPtr<IDispatch> dispatch = NULL;
	long applicationHwnd = 0;
	HRESULT hr = m_webBrowser->get_Application(&dispatch);
	if (SUCCEEDED(hr)) {
		DISPID dispId;
		CComBSTR name("HWND");
		hr = dispatch->GetIDsOfNames(IID_NULL, &name, 1, LOCALE_SYSTEM_DEFAULT, &dispId);
		if (SUCCEEDED(hr)) {
			DISPPARAMS params;
			memset(&params, 0, sizeof(DISPPARAMS));
			VARIANT variant;
			hr = dispatch->Invoke(dispId, IID_NULL, LOCALE_SYSTEM_DEFAULT, DISPATCH_PROPERTYGET, &params, &variant, NULL, NULL);
			if (SUCCEEDED(hr)) {
				applicationHwnd = variant.lVal;
			}
		}
	}
	if (!applicationHwnd) {
		Logger::error("IECrossfireBHO.initServer(): failed to get the application HWND", hr);
		return false;
	}

	CLSID clsid;
	hr = IIDFromString(L"{47836AF4-3E0C-4995-8029-FF931C5A43FC}", &clsid);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.initServer(): IIDFromString() failed[1]", hr);
		return false;
	}
	IID iid;
	hr = IIDFromString(L"{F48260BB-C061-4410-9CE1-4C5C7602690E}", &iid);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.initServer(): IIDFromString() failed[2]", hr);
		return false;
	}
	CComPtr<ICrossfireServerClass> serverClass = NULL;
	hr = CoGetClassObject(/*CLSID_CrossfireServer*/clsid, CLSCTX_ALL, 0, /*IID_ICrossfireServerClass*/iid, (LPVOID*)&serverClass);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.initServer(): CoGetClassObject() failed", hr);
		return false;
	}

	hr = serverClass->GetServer(applicationHwnd, &m_server);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.initServer(): GetController() failed", hr);
		return false;
	}

	hr = m_server->addListener(GetCurrentProcessId(), static_cast<ICrossfireServerListener*>(this));
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.initServer(): addListener() failed", hr);
		/* Cause context events to always be sent to the server */
		m_serverState = STATE_CONNECTED;
	}

	return true;
}

bool IECrossfireBHO::startDebugging(unsigned int port) {
	HKEY key;
	HRESULT hr = RegOpenKeyEx(HKEY_CURRENT_USER, L"Software\\Microsoft\\Internet Explorer\\Main", 0, KEY_READ, &key);
	if (SUCCEEDED(hr)) {
		wchar_t value[9];
		DWORD size = sizeof(value);
		hr = RegQueryValueEx(key, PREFERENCE_DISABLEIEDEBUG, NULL, NULL, (LPBYTE)value, &size);
		if (SUCCEEDED(hr) && wcscmp(value, L"no") != 0) {
			MessageBox(NULL, L"Internet Explorer Option \"Disable Script Debugging (Internet Explorer)\" must be unchecked for remote debugging to work.  Crossfire server has not been started.", L"Crossfire Server Startup Error", 0);
			return false;
		}
	}
	if (FAILED(hr)) {
		Logger::error("Failed to access the DisableScriptDebuggerIE registry setting", hr);
	}

	if (!initServer(true) || getServerState() != STATE_DISCONNECTED) {
		return false;
	}

	hr = m_server->start(port, 54124);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.startDebugging(): start() failed");
		return false;
	}

	return true;
}

