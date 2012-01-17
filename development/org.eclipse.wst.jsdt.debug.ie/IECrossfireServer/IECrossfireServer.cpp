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
#include "resource.h"

#include "CrossfireServerClass.h"
#include "IECrossfireServer.h"
#include "Logger.h"

class CIECrossfireServerModule : public CAtlExeModuleT<CIECrossfireServerModule> {
public :
	DECLARE_LIBID(LIBID_IECrossfireServerLib)
	DECLARE_REGISTRY_APPID_RESOURCEID(IDR_IECrossfireServer, "{1B6AA6D6-25AC-4994-9431-AC0DFC85654D}")
};

CIECrossfireServerModule _AtlModule;

extern "C" int WINAPI _tWinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPTSTR lpCmdLine, int nShowCmd) {
	if (wcscmp((const wchar_t*)lpCmdLine, L"/Embedding") != 0 && wcscmp((const wchar_t*)lpCmdLine, L"-Embedding") != 0) {
		return _AtlModule.WinMain(nShowCmd);
	}

	CoInitialize(NULL);
	CComObject<CrossfireServerClass>* serverClass = NULL;
	HRESULT hr = CComObject<CrossfireServerClass>::CreateInstance(&serverClass);
	if (FAILED(hr)) {
		Logger::error("IECrossfireServer.main(): CreateInstance() failed", hr);
		return 0;
	}

	/*
	 * server is not AddRef'd here in spite of the use of CComObject::CreateInstance
	 * because CoRegisterClassObject does an automatic AddRef()
	 */

	DWORD id = 0;
	hr = CoRegisterClassObject(CLSID_CrossfireServer, (ICrossfireServerClass*)serverClass, CLSCTX_LOCAL_SERVER, REGCLS_MULTIPLEUSE, &id);
	if (FAILED(hr)) {
		Logger::error("IECrossfireServer.main(): CoRegisterClassObject() failed", hr);
		return 0;
	}

	BOOL ret;
	MSG msg;
	while ((ret = GetMessage(&msg, 0, 0, 0)) != 0) {
		if (ret == -1) {
			Logger::error("IECrossfireServer.main(): GetMessage() failed", GetLastError());
		} else {
			TranslateMessage(&msg);
			DispatchMessage(&msg);
		}
	}

	hr = CoRevokeClassObject(id);
	if (FAILED(hr)) {
		Logger::error("IECrossfireServer.main(): CoRevokeClassObject() failed", hr);
		return 0;
	}

	CoUninitialize();
	return 0;
}
