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
#include "CrossfireBreakpoint.h"

/* initialize statics */
const wchar_t* CrossfireBreakpoint::KEY_CONDITION = L"condition";
const wchar_t* CrossfireBreakpoint::KEY_CONTEXTID = L"contextId";
const wchar_t* CrossfireBreakpoint::KEY_ENABLED = L"enabled";
const wchar_t* CrossfireBreakpoint::KEY_HANDLE = L"handle";
const wchar_t* CrossfireBreakpoint::KEY_LOCATION = L"location";
const wchar_t* CrossfireBreakpoint::KEY_TYPE = L"type";

CrossfireBreakpoint::CrossfireBreakpoint() {
	static unsigned int s_nextBreakpointHandle = 1;
	m_condition = NULL;
	m_contextId = NULL;
	m_enabled = true;
	m_handle = s_nextBreakpointHandle++;
}

CrossfireBreakpoint::CrossfireBreakpoint(unsigned int handle) {
	m_condition = NULL;
	m_contextId = NULL;
	m_enabled = true;
	m_handle = handle;
}

CrossfireBreakpoint::~CrossfireBreakpoint() {
	if (m_condition) {
		delete m_condition;
	}
	if (m_contextId) {
		delete m_contextId;
	}
}

bool CrossfireBreakpoint::appliesToUrl(std::wstring* url) {
	return true;
}

const std::wstring* CrossfireBreakpoint::getCondition() {
	return m_condition;
}

const std::wstring* CrossfireBreakpoint::getContextId() {
	return m_contextId;
}

unsigned int CrossfireBreakpoint::getHandle() {
	return m_handle;
}

bool CrossfireBreakpoint::isEnabled() {
	return m_enabled;
}

void CrossfireBreakpoint::setCondition(std::wstring* value) {
	if (m_condition) {
		delete m_condition;
		m_condition = NULL;
	}
	if (value) {
		m_condition = new std::wstring;
		m_condition->assign(*value);
	}
}

void CrossfireBreakpoint::setContextId(std::wstring* value) {
	if (m_contextId) {
		delete m_contextId;
		m_contextId = NULL;
	}
	if (value) {
		m_contextId = new std::wstring;
		m_contextId->assign(*value);
	}
}

void CrossfireBreakpoint::setEnabled(bool value) {
	m_enabled = value;
}

bool CrossfireBreakpoint::toValueObject(Value** _value) {
	Value value_null;
	value_null.setType(TYPE_NULL);

	Value* result = new Value();
	result->addObjectValue(KEY_HANDLE, &Value((double)m_handle));
	result->addObjectValue(KEY_TYPE, &Value(getTypeString()));
	result->addObjectValue(KEY_CONDITION, m_condition ? &Value(m_condition) : &value_null);
	result->addObjectValue(KEY_CONTEXTID, m_contextId ? &Value(m_contextId) : &value_null);
	result->addObjectValue(KEY_ENABLED, &Value(m_enabled));
	Value* value_location = NULL;
	if (!getLocationAsValue(&value_location)) {
		delete result;
		return false;
	}
	result->addObjectValue(KEY_LOCATION, value_location);
	delete value_location;
	*_value = result;
	return true;
}
