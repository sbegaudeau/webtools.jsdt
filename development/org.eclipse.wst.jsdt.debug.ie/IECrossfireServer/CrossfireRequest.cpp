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


#include "stdafx.h"
#include "CrossfireRequest.h"

CrossfireRequest::CrossfireRequest() : CrossfirePacket() {
	m_arguments = NULL;
	m_contextId = NULL;
	m_name = NULL;
}

CrossfireRequest::~CrossfireRequest() {
	if (m_arguments) {
		delete m_arguments;
	}
	if (m_contextId) {
		delete m_contextId;
	}
	if (m_name) {
		free(m_name);
	}
}

void CrossfireRequest::clone(CrossfirePacket** _value) {
	CrossfireRequest* result = new CrossfireRequest();
	result->setArguments(m_arguments);
	result->setContextId(m_contextId);
	result->setName(m_name);
	*_value = result;
}

Value* CrossfireRequest::getArguments() {
	return m_arguments;
}

std::wstring* CrossfireRequest::getContextId() {
	return m_contextId;
}

wchar_t* CrossfireRequest::getName() {
	return m_name;
}

int CrossfireRequest::getType() {
	return TYPE_REQUEST;
}

bool CrossfireRequest::setArguments(Value* value) {
	if (value && value->getType() != TYPE_OBJECT) {
		return false;
	}
	if (m_arguments) {
		delete m_arguments;
		m_arguments = NULL;
	}
	if (value) {
		value->clone(&m_arguments);
	}
	return true;
}

void CrossfireRequest::setName(const wchar_t* value) {
	if (m_name) {
		free(m_name);
		m_name = NULL;
	}
	if (value) {
		m_name = _wcsdup(value);
	}
}

void CrossfireRequest::setContextId(std::wstring* value) {
	if (m_contextId) {
		delete m_contextId;
		m_contextId = NULL;
	}
	if (value) {
		m_contextId = new std::wstring;
		m_contextId->assign(*value);
	}
}
