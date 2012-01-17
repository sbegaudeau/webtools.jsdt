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
#include "WindowsSocketConnection.h"

/* initialize statics */
std::map<HWND, WindowsSocketConnection*>* WindowsSocketConnection::s_connections = new std::map<HWND, WindowsSocketConnection*>; /* leaked */

WindowsSocketConnection::WindowsSocketConnection(CrossfireServer* server) {
	m_clientSocket = INVALID_SOCKET;
	m_server = server;
	m_hWnd = NULL;
	m_listenSocket = INVALID_SOCKET;	
}

WindowsSocketConnection::~WindowsSocketConnection() {
}

bool WindowsSocketConnection::acceptConnection() {
	int rc = WSAAsyncSelect(m_listenSocket, m_hWnd, EW_SOCKET_MSG, FD_ACCEPT);
	if (rc != 0) {
		Logger::error("WindowsSocketConnection.acceptConnection(): WSAAsyncSelect() failed", rc);
		return false;
	}
	return true;
}

bool WindowsSocketConnection::close() {
	deregisterConnection(m_hWnd);
	closesocket(m_clientSocket);
	closesocket(m_listenSocket);
	m_clientSocket = m_listenSocket = INVALID_SOCKET;
	return true;
}

void WindowsSocketConnection::handleSocketAccept() {
	m_clientSocket = accept(m_listenSocket, NULL, NULL);
	if (m_clientSocket == INVALID_SOCKET) {
		Logger::error("WindowsSocketConnection.handleSocketAccept(): accept() failed", WSAGetLastError());
		return;
	}

	int rc = WSAAsyncSelect(m_clientSocket, m_hWnd, EW_SOCKET_MSG, FD_READ | FD_CLOSE);
	if (rc == SOCKET_ERROR) {
		closesocket(m_clientSocket);
		m_clientSocket = INVALID_SOCKET;
		Logger::error("WindowsSocketConnection.handleSocketAccept(): WSAAsyncSelect() failed", WSAGetLastError());
		return;
	}

	/* remove the listenSocket listener */
	WSAAsyncSelect(m_listenSocket, m_hWnd, EW_SOCKET_MSG, 0);

	m_server->connected();
}

void WindowsSocketConnection::handleSocketClose() {
	m_server->disconnected();
}

void WindowsSocketConnection::handleSocketRead() {
	const int LENGTH_BUFFER = 4096;
	char buffer[LENGTH_BUFFER];
	ZeroMemory(buffer, LENGTH_BUFFER);

	int length = recv(m_clientSocket, buffer, LENGTH_BUFFER, 0);
	if (length == SOCKET_ERROR) {
		Logger::error("WindowsSocketConnection.handleSocketRead(): recv() failed", errno);
		return;
	}
	if (length == 0) {
		/* connection closed */
		Logger::log("WindowsSocketConnection.handleSocketRead(): recv() length 0, implies socket closed");
		PostQuitMessage(0);
		return;
	}

	length = MultiByteToWideChar(CP_UTF8, 0, (LPCSTR)buffer, -1, NULL, 0);
	wchar_t* content = new wchar_t[length];
	MultiByteToWideChar(CP_UTF8, 0, (LPCSTR)buffer, -1, content, length);
//Logger::log("-----\nReceived:");
//Logger::log(content);
	m_server->received(content);
	delete[] content;
}

bool WindowsSocketConnection::init(unsigned int port) {
	WSADATA wsaData;
	int rc = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (rc != 0) {
		Logger::error("WindowsSocketConnection.init(): WSAStartup() failed", rc);
		return false;
	}

	char portString[6];
	ZeroMemory(portString, 6);
	_ltoa_s(port, portString, 6, 10);
	struct addrinfo hints;
	ZeroMemory(&hints, sizeof(hints));
	hints.ai_flags = AI_PASSIVE;
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_protocol = IPPROTO_TCP;
	struct addrinfo* result = NULL;
	rc = getaddrinfo(NULL, portString, &hints, &result);
	if (rc != 0) {
		WSACleanup();
		Logger::error("WindowsSocketConnection.init(): getaddrinfo() failed", rc);
		return false;
	}

	m_listenSocket = socket(result->ai_family, result->ai_socktype, result->ai_protocol);
	if (m_listenSocket == INVALID_SOCKET) {
		freeaddrinfo(result);
		WSACleanup();
		Logger::error("WindowsSocketConnection.init(): socket() failed", WSAGetLastError());
		return false;
	}

	if (bind(m_listenSocket, result->ai_addr, (int)result->ai_addrlen) == SOCKET_ERROR) {
		closesocket(m_listenSocket);
		m_listenSocket = INVALID_SOCKET;
		freeaddrinfo(result);
		WSACleanup();
		Logger::error("WindowsSocketConnection.init(): bind() failed", WSAGetLastError());
		return false;
	}

	rc = listen(m_listenSocket, SOMAXCONN); 
	freeaddrinfo(result);
	if (rc == SOCKET_ERROR) {
		closesocket(m_listenSocket);
		m_listenSocket = INVALID_SOCKET;
		WSACleanup();
		Logger::error("WindowsSocketConnection.init(): listen() failed", WSAGetLastError());
		return false;
	}

	std::wstringstream stream;
	stream << "Listening for a connection on localhost:";
	stream << portString;
	Logger::log((wchar_t*)stream.str().c_str());

	static LPCWSTR s_windowClass = _T("CrossfireSocketConnection"); // TODO differentiate by port?
	HINSTANCE module = GetModuleHandle(NULL);
	WNDCLASS ex;
	ex.style = 0;
	ex.lpfnWndProc = WndProc;
	ex.cbClsExtra = 0;
	ex.cbWndExtra = 0;
	ex.hInstance = module;
	ex.hIcon = NULL;
	ex.hCursor = NULL;
	ex.hbrBackground = NULL;
	ex.lpszMenuName = NULL;
	ex.lpszClassName = s_windowClass;
	RegisterClass(&ex);
	m_hWnd = CreateWindow(s_windowClass, NULL, 0, 0, 0, 0, 0, HWND_MESSAGE, NULL, module, NULL);
	if (!m_hWnd) {
		Logger::error("WindowsSocketConnection.init(): CreateWindow() failed", GetLastError());
		return false;
	}
	WindowsSocketConnection::registerConnection(m_hWnd, this);
	return true;
}

bool WindowsSocketConnection::isConnected() {
	return m_clientSocket != INVALID_SOCKET;
}

bool WindowsSocketConnection::send(const wchar_t* msg) {
//Logger::log("-----\nSent:");
//Logger::log((wchar_t*)msg);
	int length = WideCharToMultiByte(CP_UTF8, 0, msg, -1, NULL, 0, NULL, NULL);
	char* content = new char[length];
	WideCharToMultiByte(CP_UTF8, 0, msg, -1, content, length, NULL, NULL);
	if (::send(m_clientSocket, content, length - 1, 0) == SOCKET_ERROR) { /* uses length - 1 to not send null terminator */
		Logger::error("WindowsSocketConnection.send(): send() failed", errno);
		return false;
	}
	return true;
}

bool WindowsSocketConnection::deregisterConnection(HWND hWnd) {
	std::map<HWND, WindowsSocketConnection*>::iterator iterator = s_connections->find(hWnd);
	if (iterator != s_connections->end()) {
		s_connections->erase(iterator);
		return true;
	}

	/* not found */
	return false;
}

WindowsSocketConnection* WindowsSocketConnection::getConnection(HWND hWnd) {
	std::map<HWND, WindowsSocketConnection*>::iterator iterator = s_connections->find(hWnd);
	if (iterator == s_connections->end()) {
		/* not found */
		return NULL;
	}
	return iterator->second;
}

void WindowsSocketConnection::registerConnection(HWND hWnd, WindowsSocketConnection* connection) {
	s_connections->insert(std::pair<HWND,WindowsSocketConnection*>(hWnd, connection));
}

LRESULT CALLBACK WindowsSocketConnection::WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam) {
	switch (message) {
		case EW_SOCKET_MSG: {
			WindowsSocketConnection* instance = WindowsSocketConnection::getConnection(hWnd);
			if (!instance) {
				Logger::error("WindowsSocketConnection:WndProc(): received EW_SOCKET_MSG for unregistered hWnd");
				break;
			}
			switch (WSAGETSELECTEVENT(lParam)) {
				case FD_READ: {
					instance->handleSocketRead();
					break;
				}
				case FD_ACCEPT: {
					instance->handleSocketAccept();
					break;
				}
				case FD_CLOSE: {
					instance->handleSocketClose();
					break;
				}
				default: {
					Logger::error("WindowsSocketConnection:WndProc(): received EW_SOCKET_MSG for unexpected event");
					break;
				}
			}
			break;
		}
		default: {
			return DefWindowProc(hWnd, message, wParam, lParam);
		}
	}
	return 0;
}
