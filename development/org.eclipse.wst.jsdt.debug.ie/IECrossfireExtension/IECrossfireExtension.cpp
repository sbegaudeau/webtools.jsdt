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

#include "IECrossfireExtension.h"
#include "Logger.h"

class CIECrossfireExtensionModule : public CAtlDllModuleT<CIECrossfireExtensionModule> {
public :
	DECLARE_LIBID(LIBID_IECrossfireExtensionLib)
	DECLARE_REGISTRY_APPID_RESOURCEID(IDR_IECrossfireExtension, "{95F1ADFB-5440-462c-BAA3-9C068F1B1450}")
};

CIECrossfireExtensionModule _AtlModule;

#ifdef _MANAGED
#pragma managed(push, off)
#endif

extern "C" BOOL WINAPI DllMain(HINSTANCE hInstance, DWORD dwReason, LPVOID lpReserved) {
	if (dwReason == DLL_PROCESS_ATTACH) {
        DisableThreadLibraryCalls(hInstance);
    }
    return _AtlModule.DllMain(dwReason, lpReserved);
}

#ifdef _MANAGED
#pragma managed(pop)
#endif

STDAPI DllCanUnloadNow() {
    return _AtlModule.DllCanUnloadNow();
}

STDAPI DllGetClassObject(REFCLSID rclsid, REFIID riid, LPVOID* ppv) {
    return _AtlModule.DllGetClassObject(rclsid, riid, ppv);
}

STDAPI DllRegisterServer(void) {
    return _AtlModule.DllRegisterServer();
}

STDAPI DllUnregisterServer(void) {
	return _AtlModule.DllUnregisterServer();
}
