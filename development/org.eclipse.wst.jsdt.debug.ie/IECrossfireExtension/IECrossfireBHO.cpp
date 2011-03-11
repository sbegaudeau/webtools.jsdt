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

/* ----- initialize constants ----- */
const wchar_t* IECrossfireBHO::DEBUG_START = L"crossfiredebugstart";
const wchar_t* IECrossfireBHO::DEBUG_STOP = L"crossfiredebugstop";
const wchar_t* IECrossfireBHO::PREFERENCE_DISABLEIEDEBUG = L"DisableScriptDebuggerIE";

IECrossfireBHO::IECrossfireBHO() {
	m_server = NULL;
	m_eventsHooked = false;
	m_webBrowser = NULL;
}

IECrossfireBHO::~IECrossfireBHO() {
	DWORD threadId = GetCurrentThreadId();
	if (m_server) {
		m_server->contextDestroyed(threadId);
		m_server->Release();
	}
}

bool IECrossfireBHO::serverIsActive() {
	if (!m_server) {
		return false;
	}
	boolean isActive;
	HRESULT hr = m_server->isActive(&isActive);
	return SUCCEEDED(hr) && isActive;
}

//STDMETHODIMP IECrossfireBHO::displayHTML(BSTR htmlText) {
//	VARIANT variant;
//	variant.vt = VT_NULL;
//	CComBSTR bstr(ABOUT_BLANK);
//	HRESULT hr = webBrowser->Navigate(bstr, &variant, &variant, &variant, &variant);
//	if (FAILED(hr)) {
//		Logger::error("IECrossfireBHO.displayHTML() failed", hr);
//		return hr;
//	}
//	htmlToDisplay = SysAllocStringLen(htmlText, SysStringLen(htmlText));
//	return S_OK;
//}

void STDMETHODCALLTYPE IECrossfireBHO::OnBeforeNavigate2(IDispatch* pDisp, VARIANT* URL, VARIANT* Flags, VARIANT* TargetFrameName, VARIANT* PostData, VARIANT* Headers, VARIANT_BOOL* Cancel) {
	if (wcsstr(URL->bstrVal, DEBUG_STOP) && serverIsActive()) {
		if (stopDebugging()) {
			*Cancel = VARIANT_TRUE;
		}
	} else if (wcsstr(URL->bstrVal, DEBUG_START)) {
		if (startDebugging()) {
			*Cancel = VARIANT_TRUE;
		}
	}
}

void STDMETHODCALLTYPE IECrossfireBHO::OnDocumentComplete(IDispatch *pDisp, VARIANT *pvarURL) {
//	if (!htmlToDisplay || wcscmp(pvarURL->bstrVal, ABOUT_BLANK) != 0) {
//		return;
//	}
//
//	CComPtr<IUnknown> webBrowserIUnknown = NULL;
//	HRESULT hr = webBrowser->QueryInterface(IID_IUnknown, (void**)&webBrowserIUnknown);
//	if (SUCCEEDED(hr)) {
//		CComPtr<IUnknown> pDispIUnknown = NULL;
//		hr = pDisp->QueryInterface(IID_IUnknown, (void**)&pDispIUnknown );
//		if (SUCCEEDED(hr)) {
//			if (webBrowserIUnknown == pDispIUnknown) {
//				/* this is the top-level page frame */
//				wchar_t* text = (wchar_t*)htmlToDisplay;
//				size_t length = (wcslen(text) + 1) * 2;
//				HGLOBAL buffer = GlobalAlloc(GPTR, length);
//				if (buffer) {
//					wcscpy((wchar_t*)buffer, text);
//					CComPtr<IStream> stream = NULL;
//					HRESULT hr = CreateStreamOnHGlobal(buffer, false, &stream);
//					if (SUCCEEDED(hr)) {
//						CComPtr<IDispatch> document = NULL;
//						CComPtr<IPersistStreamInit> persistStreamInit = NULL;
//						HRESULT hr = webBrowser->get_Document(&document);
//						if (SUCCEEDED(hr)) {
//							hr = document->QueryInterface(IID_IPersistStreamInit, (void**)&persistStreamInit);
//							if (SUCCEEDED(hr)) {
//								hr = persistStreamInit->InitNew();
//								if (SUCCEEDED(hr)) {
//									hr = persistStreamInit->Load(stream);
//								}
//							}
//						}
//						if (FAILED(hr)) {
//							Logger::error("IECrossfireBHO.OnDocumentComplete() failed setting page content", hr);
//						}
//					} else {
//						Logger::error("IECrossfireBHO.OnDocumentComplete(): CreateStreamOnHGlobal() failed", hr);
//					}
//					GlobalFree(buffer);
//				}
//				SysFreeString(htmlToDisplay);
//				htmlToDisplay = NULL;
//			}
//		}
//	}
//	if (FAILED(hr)) {
//		Logger::error("IECrossfireBHO.OnDocumentComplete() failed", hr);
//		return;
//	}

	if (!serverIsActive()) {
		return;
	}

	CComPtr<IUnknown> webBrowserIUnknown = NULL;
	HRESULT hr = m_webBrowser->QueryInterface(IID_IUnknown, (void**)&webBrowserIUnknown);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.OnNavigateComplete2(): QI(IUnknown)[1] failed");
	} else {
		CComPtr<IUnknown> pDispIUnknown = NULL;
		hr = pDisp->QueryInterface(IID_IUnknown, (void**)&pDispIUnknown);
		if (FAILED(hr)) {
			Logger::error("IECrossfireBHO.OnNavigateComplete2(): QI(IUnknown)[2] failed");
		} else {
			if (webBrowserIUnknown == pDispIUnknown) {
				/* this is the top-level page frame */
				HRESULT hr = m_server->contextLoaded(GetCurrentThreadId());
				if (FAILED(hr)) {
					Logger::error("IECrossfireBHO.OnNavigateComplete2(): contextLoaded() failed");
				}
			}
		}
	}
}

void STDMETHODCALLTYPE IECrossfireBHO::OnNavigateComplete2(IDispatch *pDisp, VARIANT *pvarURL) {
	if (!serverIsActive()) {
		return;
	}
	CComPtr<IUnknown> webBrowserIUnknown = NULL;
	HRESULT hr = m_webBrowser->QueryInterface(IID_IUnknown, (void**)&webBrowserIUnknown);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.OnNavigateComplete2(): QI(IUnknown)[1] failed");
	} else {
		CComPtr<IUnknown> pDispIUnknown = NULL;
		hr = pDisp->QueryInterface(IID_IUnknown, (void**)&pDispIUnknown);
		if (FAILED(hr)) {
			Logger::error("IECrossfireBHO.OnNavigateComplete2(): QI(IUnknown)[2] failed");
		} else {
			if (webBrowserIUnknown == pDispIUnknown) {
				/* this is the top-level page frame */
				DWORD threadId = GetCurrentThreadId();
				m_server->contextDestroyed(threadId);
				HRESULT hr = m_server->contextCreated(threadId, pvarURL->bstrVal);
				if (FAILED(hr)) {
					Logger::error("IECrossfireBHO.OnNavigateComplete2(): contextCreated() failed");
				}
			}
		}
	}
}

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

HRESULT IECrossfireBHO::initServer() {
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

	CComPtr<ICrossfireServerClass> serverClass = NULL;
	hr = CoGetClassObject(CLSID_CrossfireServer, CLSCTX_ALL, 0, IID_ICrossfireServerClass, (LPVOID*)&serverClass);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.initServer(): CoGetClassObject() failed", hr);
		return false;
	}

	hr = serverClass->GetServer(applicationHwnd, &m_server);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.initServer(): GetController() failed", hr);
		return false;
	}

	return true;
}

bool IECrossfireBHO::startDebugging() {
	CComPtr<IMachineDebugManager> mdm;
	HRESULT hr = mdm.CoCreateInstance(CLSID_MachineDebugManager, NULL, CLSCTX_ALL);
	if (FAILED(hr)) {
		MessageBox(NULL, L"You need the extra Active Script Debugging framework download from MS", L"Crossfire Server Startup Error", 0);
		return false;
	}

	HKEY key;
	hr = RegOpenKeyEx(HKEY_CURRENT_USER, L"Software\\Microsoft\\Internet Explorer\\Main", 0, KEY_READ, &key);
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

	if (!((m_server || initServer()) && !serverIsActive())) {
		return false;
	}

	hr = m_server->start(54123);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.startDebugging(): start() failed");
		return false;
	}

	CComBSTR url = NULL;
	hr = m_webBrowser->get_LocationURL(&url);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.startDebugging(): get_LocationURL() failed");
		return true;
	}

	DWORD threadId = GetCurrentThreadId();
	hr = m_server->registerContext(threadId, url);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.startDebugging(): registerContext() failed");
		return true;
	}

	hr = m_server->setCurrentContext(threadId);
	if (FAILED(hr)) {
		Logger::error("IECrossfireBHO.startDebugging(): setCurrentContext() failed");
		return true;
	}

	return true;
}

bool IECrossfireBHO::stopDebugging() {
	m_server->stop();
	return true;
}
