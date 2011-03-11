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


#include "StdAfx.h"
#include "CrossfireEvent.h"

CrossfireEvent::CrossfireEvent() : CrossfirePacket() {
	m_contextId = NULL;
	m_data = NULL;
	m_name = NULL;
}

CrossfireEvent::~CrossfireEvent() {
	if (m_contextId) {
		delete m_contextId;
	}
	if (m_data) {
		delete m_data;
	}
	if (m_name) {
		free(m_name);
	}
}

void CrossfireEvent::clone(CrossfirePacket** _value) {
	CrossfireEvent* result = new CrossfireEvent();
	result->setContextId(m_contextId);
	result->setData(m_data);
	result->setName(m_name);
	*_value = result;
}

std::wstring* CrossfireEvent::getContextId() {
	return m_contextId;
}

Value* CrossfireEvent::getData() {
	return m_data;
}

wchar_t* CrossfireEvent::getName() {
	return m_name;
}

int CrossfireEvent::getType() {
	return TYPE_EVENT;
}

void CrossfireEvent::setContextId(std::wstring* value) {
	if (m_contextId) {
		delete m_contextId;
		m_contextId = NULL;
	}
	if (value) {
		m_contextId = new std::wstring;
		m_contextId->assign(*value);
	}
}

bool CrossfireEvent::setData(Value* value) {
	if (value && value->getType() != TYPE_OBJECT) {
		return false;
	}
	if (m_data) {
		delete m_data;
		m_data = NULL;
	}
	if (value) {
		value->clone(&m_data);
	}
	return true;
}

void CrossfireEvent::setName(const wchar_t* value) {
	if (m_name) {
		free(m_name);
		m_name = NULL;
	}
	if (value) {
		m_name = _wcsdup(value);
	}
}
