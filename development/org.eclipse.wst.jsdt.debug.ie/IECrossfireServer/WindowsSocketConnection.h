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

#include <iostream>
#include <winsock2.h>
#include <ws2tcpip.h>

#include "Logger.h"

class WindowsSocketConnection; // forward declaration
#include "CrossfireServer.h"

class WindowsSocketConnection {

public:
	WindowsSocketConnection(CrossfireServer* server);
	~WindowsSocketConnection();
	bool acceptConnection();
	bool close();
	bool init(unsigned int port);
	bool isConnected();
	bool send(const wchar_t* msg);

private:
	void handleSocketAccept();
	void handleSocketClose();
	void handleSocketRead();

	SOCKET m_clientSocket;
	CrossfireServer* m_server;
	HWND m_hWnd;
	SOCKET m_listenSocket;

	static bool deregisterConnection(HWND hWnd);
	static WindowsSocketConnection* getConnection(HWND hWnd);
	static void registerConnection(HWND hWnd, WindowsSocketConnection* connection);
	static LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);

	static std::map<HWND, WindowsSocketConnection*>* s_connections;

	/* constants */
	static const int EW_SOCKET_MSG = WM_APP + 1;
};
