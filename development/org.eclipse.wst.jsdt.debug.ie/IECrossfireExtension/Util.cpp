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
#include "Util.h"

/* initialize constants */
const wchar_t* Util::PREFERENCE_DISABLEIEDEBUG = L"DisableScriptDebuggerIE";

bool Util::VerifyDebugPreference() {
	/* verify that the IE debugging preference is not disabled */

	HKEY key;
	HRESULT hr = RegOpenKeyEx(HKEY_CURRENT_USER, L"Software\\Microsoft\\Internet Explorer\\Main", 0, KEY_READ, &key);
	if (SUCCEEDED(hr)) {
		wchar_t value[9];
		DWORD size = sizeof(value);
		hr = RegQueryValueEx(key, PREFERENCE_DISABLEIEDEBUG, NULL, NULL, (LPBYTE)value, &size);
		RegCloseKey(key);
		if (SUCCEEDED(hr) && wcscmp(value, L"no") != 0) {
			MessageBox(NULL, L"Internet Explorer option \"Disable Script Debugging (Internet Explorer)\" must be unchecked to enable remote debugging via Crossfire.  Crossfire server has not been started.", L"Crossfire Server Startup Error", 0);
			return false;
		}
	}
	if (FAILED(hr)) {
		Logger::error("Util.VerifyDebugPreference(): Failed to access the DisableScriptDebuggerIE registry setting", hr);
		/* allow this case to proceed, the preference could be set properly */
	}
	return true;
}

bool Util::VerifyActiveScriptDebugger() {
	/* verify that the MS Active Script Debugger is installed */

	static bool s_verified = false;
	if (s_verified) {
		return true;
	}

	CComPtr<IMachineDebugManager> mdm;
	HRESULT hr = mdm.CoCreateInstance(CLSID_MachineDebugManager, NULL, CLSCTX_ALL);
	if (FAILED(hr)) {
		MessageBox(NULL, L"Crossfire Server for Internet Explorer requires the Microsoft Script Debugger to be installed.  It can be downloaded from http://www.microsoft.com/download/en/confirmation.aspx?id=22185.", L"Crossfire Server Startup Error", 0);
		return false;
	}

	return true;
}
