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

#include "CrossfireBPManager.h"
#include "CrossfireContext.h"
#include "CrossfireEvent.h"
#include "CrossfireProcessor.h"
#include "CrossfireResponse.h"
#include "WindowsSocketConnection.h"

enum {
	STATE_DISCONNECTED,
	STATE_LISTENING,
	STATE_CONNECTED,
};

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
	virtual ~CrossfireServer();

	/* ICrossfireServer */
	HRESULT STDMETHODCALLTYPE contextCreated(DWORD processId, DWORD threadId, OLECHAR* url);
	HRESULT STDMETHODCALLTYPE contextDestroyed(DWORD processId);
	HRESULT STDMETHODCALLTYPE contextLoaded(DWORD processId);
	HRESULT STDMETHODCALLTYPE getPort(unsigned int* value);
	HRESULT STDMETHODCALLTYPE getState(int* value);
	HRESULT STDMETHODCALLTYPE registerBrowser(DWORD processId, IBrowserContext* browser);
	HRESULT STDMETHODCALLTYPE removeBrowser(DWORD processId);
	HRESULT STDMETHODCALLTYPE setCurrentContext(DWORD processId);
	HRESULT STDMETHODCALLTYPE start(unsigned int port, unsigned int debugPort);
	HRESULT STDMETHODCALLTYPE stop();

	/* CrossfireServer */
	void connected();
	void disconnected();
	CrossfireBPManager* getBreakpointManager();
	bool isConnected();
	void received(wchar_t* msg);
	void sendEvent(CrossfireEvent* eventObj);
	void sendResponse(CrossfireResponse* response);
	void setWindowHandle(unsigned long value);

private:
	CrossfireContext* getContext(wchar_t* contextId);
	void getContextsArray(CrossfireContext*** _value);
	CrossfireContext* getRequestContext(CrossfireRequest* request);
	bool performRequest(CrossfireRequest* request);
	bool processHandshake(wchar_t* msg);
	void reset();
	void sendPendingEvents();

	CrossfireBPManager* m_bpManager;
	std::map<DWORD, IBrowserContext*>* m_browsers;
	WindowsSocketConnection* m_connection;
	bool m_connectionWarningShown;
	std::map<DWORD, CrossfireContext*>* m_contexts;
	DWORD m_currentContextPID;
	bool m_handshakeReceived;
	std::wstring* m_inProgressPacket;
	unsigned int m_lastRequestSeq;
	HWND m_messageWindow;
	std::vector<CrossfireEvent*>* m_pendingEvents;
	unsigned int m_port;
	bool m_processingRequest;
	CrossfireProcessor* m_processor;
	unsigned long m_windowHandle;

	static const UINT ServerStateChangeMsg;
	static const wchar_t* WindowClass;
	static LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);

	static const wchar_t* COMMAND_CHANGEBREAKPOINTS;
	static const wchar_t* COMMAND_DELETEBREAKPOINTS;
	static const wchar_t* COMMAND_GETBREAKPOINTS;
	static const wchar_t* COMMAND_SETBREAKPOINTS;

	/* command: createContext */
	static const wchar_t* COMMAND_CREATECONTEXT;
	int commandCreateContext(Value* arguments, Value** _responseBody, wchar_t** _message);

	/* command: disableTools */
	static const wchar_t* COMMAND_DISABLETOOLS;
	int commandDisableTools(Value* arguments, Value** _responseBody, wchar_t** _message);

	/* command: enableTools */
	static const wchar_t* COMMAND_ENABLETOOLS;
	int commandEnableTools(Value* arguments, Value** _responseBody, wchar_t** _message);

	/* command: getTools */
	static const wchar_t* COMMAND_GETTOOLS;
	int commandGetTools(Value* arguments, Value** _responseBody, wchar_t** _message);

	/* command: listContexts */
	static const wchar_t* COMMAND_LISTCONTEXTS;
	static const wchar_t* KEY_CONTEXTS;
	static const wchar_t* KEY_CURRENT;
	int commandListContexts(Value* arguments, Value** _responseBody, wchar_t** _message);

	/* command: version */
	static const wchar_t* COMMAND_VERSION;
	static const wchar_t* KEY_VERSION;
	static const wchar_t* VERSION_STRING;
	int commandVersion(Value* arguments, Value** _responseBody, wchar_t** _message);

	/* event: closed */
	static const wchar_t* EVENT_CLOSED;
	void eventClosed();

	/* event: onContextCreated */
	static const wchar_t* EVENT_CONTEXTCREATED;
	void eventContextCreated(CrossfireContext* context);

	/* event: onContextDestroyed */
	static const wchar_t* EVENT_CONTEXTDESTROYED;
	void eventContextDestroyed(CrossfireContext* context);

	/* event: onContextLoaded */
	static const wchar_t* EVENT_CONTEXTLOADED;
	void eventContextLoaded(CrossfireContext* context);

	/* event: onContextSelected */
	static const wchar_t* EVENT_CONTEXTSELECTED;
	static const wchar_t* KEY_OLDCONTEXTID;
	static const wchar_t* KEY_OLDURL;
	void eventContextSelected(CrossfireContext* context, CrossfireContext* oldContext);

	/* shared */
	static const wchar_t* KEY_CONTEXTID;
	static const wchar_t* KEY_TOOLS;
	static const wchar_t* KEY_URL;

	/* constants */
	static const wchar_t* ABOUT_BLANK;
	static const wchar_t* CONTEXTID_PREAMBLE;
	static const wchar_t* HANDSHAKE;
	static const wchar_t* HEADER_CONTENTLENGTH;
	static const wchar_t* LINEBREAK;
	static const size_t LINEBREAK_LENGTH;
};

OBJECT_ENTRY_AUTO(__uuidof(CrossfireServer), CrossfireServer)
