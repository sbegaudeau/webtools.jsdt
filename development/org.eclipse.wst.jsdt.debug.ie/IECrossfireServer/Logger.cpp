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


#include "StdAfx.h"
#include "Logger.h"

/* initialize constants */
const char* Logger::PREAMBLE_LOG = "LOG::";
const char* Logger::PREAMBLE_ERROR = "ERROR::";

Logger::Logger() {
}

Logger::~Logger() {
}

void Logger::error(char* message) {
	int length = strlen(PREAMBLE_ERROR) + strlen(message) + 1;
	char* sendString = new char[length];
	strcpy_s(sendString, length, PREAMBLE_ERROR);
	strcat_s(sendString, length, message);
	send(sendString);
	delete[] sendString;
}

void Logger::error(char* message, int errorCode) {
	std::string result;
	std::stringstream stringStream;
	stringStream << message;
	stringStream << ": ";
	stringStream << errorCode;
	result.assign(stringStream.str());
	error((char *)result.c_str());
}

void Logger::log(char* message) {
	int length = strlen(PREAMBLE_LOG) + strlen(message) + 1;
	char* sendString = new char[length];
	strcpy_s(sendString, length, PREAMBLE_LOG);
	strcat_s(sendString, length, message);
	send(sendString);
	delete[] sendString;
}

void Logger::log(char* message, int code) {
	std::string result;
	std::stringstream stringStream;
	stringStream << message;
	stringStream << ": ";
	stringStream << code;
	result.assign(stringStream.str());
	log((char *)result.c_str());
}

void Logger::log(wchar_t* message) {
	size_t newSize = 0;
	wcstombs_s(&newSize, NULL, 0, message, 0);
    char* chars = new char[newSize];
    wcstombs_s(&newSize, chars, newSize, message, newSize);
	log(chars);
	delete[] chars;
}

void Logger::log(std::wstring* message) {
	log((wchar_t*)message->c_str());
}

void Logger::send(char* message) {
	SOCKET sock;
	struct sockaddr_in server_addr;
	struct hostent *host;
	host = (struct hostent *)gethostbyname((char *)"127.0.0.1");
	if ((sock = socket(AF_INET, SOCK_DGRAM, 0)) == -1) {
		return;
	}
	server_addr.sin_family = AF_INET;
	server_addr.sin_port = htons(54124 /* debug port */); // TODO
	server_addr.sin_addr = *((struct in_addr *)host->h_addr);
    sendto(sock, message, (int)strlen(message), 0, (struct sockaddr *)&server_addr, sizeof(sockaddr_in));
	closesocket(sock);
}
