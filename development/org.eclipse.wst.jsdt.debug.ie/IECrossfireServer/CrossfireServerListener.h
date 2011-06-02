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

#include "IECrossfireServer.h"

class ATL_NO_VTABLE CCrossfireServerListener :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<CCrossfireServerListener, &CLSID_CrossfireServerListener>,
	public ICrossfireServerListener {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_CROSSFIRESERVERLISTENER)
	DECLARE_NOT_AGGREGATABLE(CCrossfireServerListener)
	BEGIN_COM_MAP(CCrossfireServerListener)
		COM_INTERFACE_ENTRY(ICrossfireServerListener)
//		COM_INTERFACE_ENTRY(IUnknown)
	END_COM_MAP()
	DECLARE_PROTECT_FINAL_CONSTRUCT()

	HRESULT FinalConstruct() {
		return S_OK;
	}

	void FinalRelease() {
	}

public:
	/* ICrossfireServerListener */
	virtual HRESULT STDMETHODCALLTYPE ServerStateChanged(int state, unsigned int port) = 0;
};

OBJECT_ENTRY_AUTO(__uuidof(CrossfireServerListener), CCrossfireServerListener)
