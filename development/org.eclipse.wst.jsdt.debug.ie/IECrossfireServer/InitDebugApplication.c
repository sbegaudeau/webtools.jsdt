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


#include "InitDebugApplication.h"

HRESULT findDebugApplication(IUnknown* programProvider, DWORD processId, IRemoteDebugApplication** _application) {
	typedef struct {
		DWORD ProcessIdType;
		union {
			DWORD dwProcessId; 
			GUID guidProcessId; 
			DWORD dwUnused; 
		} ProcessId;
	} struct_processId;

	typedef struct {
		DWORD dwCount;
		CONST GUID* Members;
	} struct_guidArray;

	typedef struct {
		DWORD Fields;
		DWORD dwCount;
		IUnknown** Members;
		BOOL fIsDebuggerPresent;
	} struct_processData;

	struct_processId process;
	struct_guidArray filter;
	struct_processData processData;
	HRESULT hr;
	IID iid;
	IUnknown* programNode;
	IUnknown* providerProgramNode;

	/*
	 * Invoke: IDebugProgramProvider2::GetProviderProcessData
	 * MSDN: http://msdn.microsoft.com/en-us/library/bb161298%28v=vs.80%29.aspx
	 */
	process.ProcessIdType = 0;
	process.ProcessId.dwProcessId = processId;
	filter.dwCount = 0;
	filter.Members = NULL;
	ZeroMemory(&processData, sizeof(struct_processData));
#ifdef _WIN64
	hr = (HRESULT)((HRESULT (STDMETHODCALLTYPE *)(IUnknown*, DWORD, IUnknown*, struct_processId, struct_guidArray, struct_processData*))(*(long**)programProvider)[3])(programProvider, 0x16, NULL, process, filter, &processData);
#else
	hr = (HRESULT)((HRESULT (STDMETHODCALLTYPE *)(IUnknown*, DWORD, IUnknown*, struct_processId, struct_guidArray, struct_processData*))(*(int**)programProvider)[3])(programProvider, 0x16, NULL, process, filter, &processData);
#endif
	if (FAILED(hr)) {
        return hr;
	}
	if (processData.dwCount != 1) {
        return S_FALSE;
	}
	programNode = processData.Members[0];

	/* QI for IDebugProviderProgramNode2 (IID is made public in MS Visual Studio IDE) */
	hr = IIDFromString(L"{AFDBA726-047A-4B83-B8C7-D812FE9CAA5C}", &iid);
	if (FAILED(hr)) {
		return hr;
	}
#ifdef _WIN64
	hr = (HRESULT)((HRESULT (STDMETHODCALLTYPE *)(IUnknown*, REFIID, void**))(*(long**)programNode)[0])(programNode, &iid, (void**)&providerProgramNode);
#else
	hr = (HRESULT)((HRESULT (STDMETHODCALLTYPE *)(IUnknown*, REFIID, void**))(*(int**)programNode)[0])(programNode, &iid, (void**)&providerProgramNode);
#endif
	if (FAILED(hr)) {
        return hr;
	}

	/*
	 * Invoke: IDebugProviderProgramNode2::UnmarshalDebuggeeInterface
	 * MSDN: http://msdn.microsoft.com/en-us/library/bb162294.aspx
	 */
#ifdef _WIN64
	hr = (HRESULT)((HRESULT (STDMETHODCALLTYPE *)(IUnknown*, REFIID, void**))(*(long**)providerProgramNode)[3])(providerProgramNode, &IID_IRemoteDebugApplication, (void**)_application);
#else
	hr = (HRESULT)((HRESULT (STDMETHODCALLTYPE *)(IUnknown*, REFIID, void**))(*(int**)providerProgramNode)[3])(providerProgramNode, &IID_IRemoteDebugApplication, (void**)_application);
#endif

	return hr;
}
