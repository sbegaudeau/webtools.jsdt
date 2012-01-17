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
#include <map>

#include "CrossfireServer.h"
#include "IECrossfireServer.h"
#include "Logger.h"

class ATL_NO_VTABLE CrossfireServerClass :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<CrossfireServerClass, &CLSID_CrossfireServerClass>,
	public IClassFactory,
	public IDispatchImpl<ICrossfireServerClass, &IID_ICrossfireServerClass, &LIBID_IECrossfireServerLib, 1, 0> {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_CROSSFIRESERVERCLASS)
	DECLARE_NOT_AGGREGATABLE(CrossfireServerClass)
	BEGIN_COM_MAP(CrossfireServerClass)
		COM_INTERFACE_ENTRY(ICrossfireServerClass)
		COM_INTERFACE_ENTRY(IClassFactory)
		COM_INTERFACE_ENTRY(IDispatch)
	END_COM_MAP()
	DECLARE_PROTECT_FINAL_CONSTRUCT()

	HRESULT FinalConstruct() {
		return S_OK;
	}

	void FinalRelease() {
	}

public:
	/* IClassFactory */
	HRESULT STDMETHODCALLTYPE CreateInstance(IUnknown *pUnkOuter, REFIID riid, void **ppvObject);
	HRESULT STDMETHODCALLTYPE LockServer(BOOL fLock);

	/* ICrossfireServerClass */
	HRESULT STDMETHODCALLTYPE GetServer(/*unsigned long windowHandle,*/ ICrossfireServer** _value);
	HRESULT STDMETHODCALLTYPE RemoveServer(unsigned long windowHandle);

protected:
	CrossfireServerClass();
	virtual ~CrossfireServerClass();

	int lockCount;

private:
	std::map<unsigned long, ICrossfireServer*>* m_servers;
};

OBJECT_ENTRY_AUTO(__uuidof(CrossfireServerClass), CrossfireServerClass)
