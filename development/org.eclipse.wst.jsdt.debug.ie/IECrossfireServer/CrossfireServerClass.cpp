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
#include "CrossfireServerClass.h"

CrossfireServerClass::CrossfireServerClass() {
	m_servers = new std::map<unsigned long, ICrossfireServer*>;
	lockCount = 0;
}

CrossfireServerClass::~CrossfireServerClass() {
	std::map<unsigned long, ICrossfireServer*>::iterator iterator = m_servers->begin();
	while (iterator != m_servers->end()) {
		iterator->second->Release();
		iterator++;
	}
	delete m_servers;
}

/* IClassFactory */

HRESULT CrossfireServerClass::CreateInstance(IUnknown *pUnkOuter, REFIID riid, void **ppvObject) {
	/* ICrossfireServer instances must be created via GetServer() */
	return E_NOTIMPL;
}

HRESULT CrossfireServerClass::LockServer(BOOL fLock) {
	lockCount += fLock ? 1 : -1;
	if (!lockCount && m_servers->empty()) {
		PostQuitMessage(0);
	}
	return S_OK;
}

/* ICrossfireServerClass */

STDMETHODIMP CrossfireServerClass::GetServer(/*unsigned long windowHandle,*/ ICrossfireServer** _value) {
	*_value = NULL;

	/*
	 * TEMPORARY CODE: windowHandle is set to a consistent value so that the same
	 * CrossfireServer is always returned.  If this is determined to be the correct
	 * behavior then m_servers should be replaced with a singleton CrossfireServer.
	 */
	unsigned long windowHandle = 0;

	std::map<unsigned long,ICrossfireServer*>::iterator iterator = m_servers->find(windowHandle);
	if (iterator != m_servers->end()) {
		*_value = iterator->second;
		(*_value)->AddRef();
	} else {
		CComObject<CrossfireServer>* server = NULL;
		HRESULT hr = CComObject<CrossfireServer>::CreateInstance(&server);
		if (FAILED(hr)) {
			Logger::error("CrossfireServerClass.GetServer(): CreateInstance() failed", hr);
			return S_FALSE;
		}

		server->AddRef(); /* CComObject::CreateInstance gives initial ref count of 0 */
		server->setWindowHandle(windowHandle);
		m_servers->insert(std::pair<unsigned long,ICrossfireServer*>(windowHandle, server));
		*_value = server;
	}
	return S_OK;
}

STDMETHODIMP CrossfireServerClass::RemoveServer(unsigned long windowHandle) {
	/*
	 * TEMPORARY CODE: windowHandle is set to a consistent value so that the same
	 * CrossfireServer is always used.  If this is determined to be the correct
	 * behavior then this method can be replaced with one that does not receive a
	 * handle for identifying the specific server.
	 */
	windowHandle = 0;

	std::map<unsigned long,ICrossfireServer*>::iterator iterator = m_servers->find(windowHandle);
	if (iterator == m_servers->end()) {
		Logger::error("CrossfireServerClass.RemoveServer(): unknown windowHandle");
		return S_FALSE;
	}
	m_servers->erase(iterator);
	if (!lockCount && m_servers->empty()) {
		PostQuitMessage(0);
	}
	return S_OK;
}
