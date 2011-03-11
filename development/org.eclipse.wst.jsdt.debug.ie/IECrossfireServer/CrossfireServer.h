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
#include <map>

#include "CrossfireBPManager.h"
#include "CrossfireContext.h"
#include "CrossfireEvent.h"
#include "CrossfireProcessor.h"
#include "CrossfireResponse.h"
#include "WindowsSocketConnection.h"

class ATL_NO_VTABLE CrossfireServer :
	public CComObjectRootEx<CComSingleThreadModel>,
	public CComCoClass<CrossfireServer, &CLSID_CrossfireServer>,
	public IDispatchImpl<ICrossfireServer, &IID_ICrossfireServer, &LIBID_IECrossfireServerLib, 1, 0> {

public:
	DECLARE_REGISTRY_RESOURCEID(IDR_CROSSFIRESERVER)
	DECLARE_NOT_AGGREGATABLE(CrossfireServer)
	BEGIN_COM_MAP(CrossfireServer)
		COM_INTERFACE_ENTRY(ICrossfireServer)
		COM_INTERFACE_ENTRY(IDispatch)
	END_COM_MAP()
	DECLARE_PROTECT_FINAL_CONSTRUCT()

	HRESULT FinalConstruct() {
		return S_OK;
	}

	void FinalRelease() {
	}

public:
	CrossfireServer();
	~CrossfireServer();

	/* ICrossfireServer */
	virtual HRESULT STDMETHODCALLTYPE contextCreated(DWORD threadId, OLECHAR* href);
	virtual HRESULT STDMETHODCALLTYPE contextDestroyed(DWORD threadId);
	virtual HRESULT STDMETHODCALLTYPE contextLoaded(DWORD threadId);
	virtual HRESULT STDMETHODCALLTYPE isActive(boolean* value);
	virtual HRESULT STDMETHODCALLTYPE registerContext(DWORD threadId, OLECHAR* href);
	virtual HRESULT STDMETHODCALLTYPE setCurrentContext(DWORD threadId);
	virtual HRESULT STDMETHODCALLTYPE start(unsigned int port);
	virtual HRESULT STDMETHODCALLTYPE stop();

	virtual void disconnected();
	virtual CrossfireBPManager* getBreakpointManager();
	virtual void received(wchar_t* msg);
	virtual void sendEvent(CrossfireEvent* eventObj);
	virtual void sendResponse(CrossfireResponse* response);
	virtual void setWindowHandle(unsigned long value);

private:
	virtual CrossfireContext* getRequestContext(CrossfireRequest* request);
	virtual bool performRequest(CrossfireRequest* request);
	virtual void reset();

	CrossfireBPManager* m_bpManager;
	WindowsSocketConnection* m_connection;
	std::map<DWORD, CrossfireContext*>* m_contexts;
	CrossfireContext* m_currentContext;
	bool m_handshakeReceived;
	std::wstring* m_inProgressPacket;
	unsigned int m_lastRequestSeq;
	std::vector<CrossfireEvent*>* m_pendingEvents;
	unsigned int m_port;
	bool m_processingRequest;
	CrossfireProcessor* m_processor;
	unsigned long m_windowHandle;

	static const wchar_t* COMMAND_CHANGEBREAKPOINT;
	static const wchar_t* COMMAND_CLEARBREAKPOINT;
	static const wchar_t* COMMAND_GETBREAKPOINT;
	static const wchar_t* COMMAND_GETBREAKPOINTS;
	static const wchar_t* COMMAND_SETBREAKPOINT;

	/* command: listContexts */
	static const wchar_t* COMMAND_LISTCONTEXTS;
	static const wchar_t* KEY_CONTEXTS;
	static const wchar_t* KEY_CURRENT;
	virtual bool commandListContexts(Value* arguments, Value** _responseBody);

	/* command: version */
	static const wchar_t* COMMAND_VERSION;
	static const wchar_t* KEY_VERSION;
	static const wchar_t* VERSION_STRING;
	virtual bool commandVersion(Value* arguments, Value** _responseBody);

	/* event: closed */
	static const wchar_t* EVENT_CLOSED;
	virtual void eventClosed();

	/* event: onContextChanged */
	static const wchar_t* EVENT_CONTEXTCHANGED;
	static const wchar_t* KEY_NEWHREF;
	virtual void eventContextChanged(CrossfireContext* newContext, CrossfireContext* oldContext);

	/* event: onContextCreated */
	static const wchar_t* EVENT_CONTEXTCREATED;
	virtual void eventContextCreated(CrossfireContext* context);

	/* event: onContextDestroyed */
	static const wchar_t* EVENT_CONTEXTDESTROYED;
	virtual void eventContextDestroyed(CrossfireContext* context);

	/* event: onContextLoaded */
	static const wchar_t* EVENT_CONTEXTLOADED;
	virtual void eventContextLoaded(CrossfireContext* context);

	/* shared */
	static const wchar_t* KEY_HREF;

	/* constants */
	static const wchar_t* CONTEXTID_PREAMBLE;
	static const wchar_t* HANDSHAKE;
	static const wchar_t* HEADER_CONTENTLENGTH;
	static const wchar_t* LINEBREAK;
	static const size_t LINEBREAK_LENGTH;
};

OBJECT_ENTRY_AUTO(__uuidof(CrossfireServer), CrossfireServer)
