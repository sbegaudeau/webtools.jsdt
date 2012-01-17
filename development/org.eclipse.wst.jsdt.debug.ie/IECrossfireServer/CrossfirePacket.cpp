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
#include "CrossfirePacket.h"

CrossfirePacket::CrossfirePacket() {
	m_contextId = NULL;
	m_name = NULL;
	m_seq = -1;
}

CrossfirePacket::~CrossfirePacket() {
	if (m_contextId) {
		delete m_contextId;
	}
	if (m_name) {
		free(m_name);
	}
}

std::wstring* CrossfirePacket::getContextId() {
	return m_contextId;
}

wchar_t* CrossfirePacket::getName() {
	return m_name;
}

unsigned int CrossfirePacket::getSeq() {
	return m_seq;
}

void CrossfirePacket::setContextId(std::wstring* value) {
	if (m_contextId) {
		delete m_contextId;
		m_contextId = NULL;
	}
	if (value) {
		m_contextId = new std::wstring;
		m_contextId->assign(*value);
	}
}

void CrossfirePacket::setName(const wchar_t* value) {
	if (m_name) {
		free(m_name);
		m_name = NULL;
	}
	if (value) {
		m_name = _wcsdup(value);
	}
}

void CrossfirePacket::setSeq(unsigned int value) {
	m_seq = value;
}
