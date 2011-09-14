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
#include "Util.h"

/* initialize constants */
const wchar_t* Util::PDM_DLL = L"pdm.dll";
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
			MessageBox(NULL, L"Internet Explorer Option \"Disable Script Debugging (Internet Explorer)\" must be unchecked for remote debugging to work.  Crossfire server has not been started.", L"Crossfire Server Startup Error", 0);
			return false;
		}
	}
	if (FAILED(hr)) {
		Logger::error("Util.verifyPDM(): Failed to access the DisableScriptDebuggerIE registry setting", hr);
		/* allow this case to proceed, the preference could be set properly */
	}
	return true;
}

bool Util::VerifyPDM() {
	/* verify that the installed IE provides a pdm.dll */

	static bool s_verified = false;
	if (s_verified) {
		return true;
	}

	HKEY key;
	LONG result = RegOpenKeyEx(HKEY_LOCAL_MACHINE, L"SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\IEXPLORE.EXE", 0, KEY_QUERY_VALUE, &key);
	if (result != ERROR_SUCCESS) {
		Logger::error("Util.verifyPDM(): RegOpenKeyEx() failed", result);
	} else {
		DWORD type = 0;
		DWORD size = MAX_PATH;
		wchar_t chars[MAX_PATH];
		result = RegQueryValueEx(key, NULL, NULL, &type, (LPBYTE)chars, &size);
		RegCloseKey(key);
		if (result != ERROR_SUCCESS) {
			Logger::error("Util.verifyPDM(): RegQueryValueEx() failed", result);
		} else if (type != REG_SZ) {
			Logger::error("Util.verifyPDM(): RegQueryValueEx() returned unexpected registry value type", type);
		} else {
			wchar_t* separator = wcsrchr(chars, wchar_t('\\'));
			if (!separator) {
				Logger::error("Util.verifyPDM(): Registry value HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\IEXPLORE.EXE does not contain a '\\'");
			} else {
				size_t dirLength = separator - chars + 1;
				size_t totalLength = dirLength + wcslen(PDM_DLL) + 1;
				wchar_t* path = new wchar_t[totalLength];
				wcsncpy_s(path, totalLength, chars, dirLength);
				wcscpy_s(path + dirLength, totalLength - dirLength, PDM_DLL);
				path[totalLength - 1] = wchar_t('\0');
				HANDLE handle = CreateFile(path, 0, 0, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
				if (handle == INVALID_HANDLE_VALUE) {
					std::wstring string;
					string.assign(L"Could not open required library for debugging Internet Explorer: ");
					string.append(path);
					string.append(L".  Crossfire server has not been started.");
					delete[] path;
					MessageBox(NULL, string.c_str(), L"Crossfire Server Startup Error", 0);
					return false;
				}
				delete[] path;
				CloseHandle(handle);
				s_verified = true;
				return true;
			}
		}
	}

	/* a failure occurred while accessing the registry */
	MessageBox(NULL, L"Could not determine the installation location of IE.  Crossfire server has not been started.", L"Crossfire Server Startup Error", 0);
	return false;
}
