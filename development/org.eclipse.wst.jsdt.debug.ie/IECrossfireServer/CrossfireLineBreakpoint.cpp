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
#include "CrossfireLineBreakpoint.h"

/* initialize statics */
const wchar_t* CrossfireLineBreakpoint::BPTYPESTRING_LINE = L"line";
const wchar_t* CrossfireLineBreakpoint::KEY_CONDITION = L"condition";
const wchar_t* CrossfireLineBreakpoint::KEY_ENABLED = L"enabled";
const wchar_t* CrossfireLineBreakpoint::KEY_LINE = L"line";
const wchar_t* CrossfireLineBreakpoint::KEY_URL = L"url";
const wchar_t* CrossfireLineBreakpoint::ATTRIBUTE_CONDITION = KEY_CONDITION;
const wchar_t* CrossfireLineBreakpoint::ATTRIBUTE_ENABLED = KEY_ENABLED;

CrossfireLineBreakpoint::CrossfireLineBreakpoint() : CrossfireBreakpoint() {
	m_line = -1;
	m_url = NULL;
}

CrossfireLineBreakpoint::CrossfireLineBreakpoint(unsigned int handle) : CrossfireBreakpoint(handle) {
	m_line = -1;
	m_url = NULL;
}

CrossfireLineBreakpoint::~CrossfireLineBreakpoint() {
	if (m_url) {
		delete m_url;
	}
}

bool CrossfireLineBreakpoint::CanHandleBPType(wchar_t* type) {
	return wcscmp(type, BPTYPESTRING_LINE) == 0;
}

bool CrossfireLineBreakpoint::appliesToUrl(std::wstring* url) {
	return m_url->compare(*url) == 0;
}

bool CrossfireLineBreakpoint::attributeIsValid(wchar_t* name, Value* value) {
	if (wcscmp(name, ATTRIBUTE_CONDITION) == 0) {
		int type = value->getType();
		return type == TYPE_STRING || type == TYPE_NULL;
	}

	if (wcscmp(name, ATTRIBUTE_ENABLED) == 0) {
		return value->getType() == TYPE_BOOLEAN;
	}

	/* unknown attribute */
	return false;
}

void CrossfireLineBreakpoint::clone(CrossfireBreakpoint** _value) {
	CrossfireLineBreakpoint* result = new CrossfireLineBreakpoint(getHandle());
	result->setCondition((std::wstring*)getCondition());
	result->setContextId((std::wstring*)getContextId());
	result->setEnabled(isEnabled());
	result->setLine(getLine());
	result->setTarget(getTarget());
	result->setUrl((std::wstring*)getUrl());
	*_value = result;
}

const std::wstring* CrossfireLineBreakpoint::getCondition() {
	Value* value = getAttribute((wchar_t*)ATTRIBUTE_CONDITION);
	if (value) {
		return value->getStringValue();
	}
	return NULL;
}

unsigned int CrossfireLineBreakpoint::getLine() {
	return m_line;
}

bool CrossfireLineBreakpoint::getLocationAsValue(Value** _value) {
	if (!m_url || m_line < 0) {
		return false;
	}
	Value* result = new Value();
	result->setObjectValue(KEY_URL, &Value(m_url));
	result->setObjectValue(KEY_LINE, &Value((double)m_line));
	*_value = result;
	return true;
}

int CrossfireLineBreakpoint::getType() {
	return BPTYPE_LINE;
}

const wchar_t* CrossfireLineBreakpoint::getTypeString() {
	return BPTYPESTRING_LINE;
}

const std::wstring* CrossfireLineBreakpoint::getUrl() {
	return m_url;
}

bool CrossfireLineBreakpoint::isEnabled() {
	Value* value = getAttribute((wchar_t*)ATTRIBUTE_ENABLED);
	if (value) {
		return value->getBooleanValue();
	}
	return true;
}

void CrossfireLineBreakpoint::setCondition(std::wstring* value) {
	if (!value) {
		clearAttribute((wchar_t*)ATTRIBUTE_CONDITION);
	} else {
		setAttribute((wchar_t*)ATTRIBUTE_CONDITION, &Value(value));
	}
}

void CrossfireLineBreakpoint::setLine(unsigned int value) {
	m_line = value;
}

void CrossfireLineBreakpoint::setEnabled(bool value) {
	setAttribute((wchar_t*)ATTRIBUTE_ENABLED, &Value(value));
}

bool CrossfireLineBreakpoint::setLocationFromValue(Value* value) {
	Value* value_url = value->getObjectValue(KEY_URL);
	if (!value_url || value_url->getType() != TYPE_STRING) {
		Logger::error("breakpoint 'location' argument does not have a valid 'url' value");
		return false;
	}

	Value* value_line = value->getObjectValue(KEY_LINE);
	if (!value_line || value_line->getType() != TYPE_NUMBER) {
		Logger::error("breakpoint 'location' argument does not have a valid 'line' value");
		return false;
	}

	setLine((unsigned int)value_line->getNumberValue());
	setUrl(value_url->getStringValue());
	return true;
}

bool CrossfireLineBreakpoint::setUrl(std::wstring* value) {
	if (!value) {
		return false;
	}
	if (!m_url) {
		m_url = new std::wstring;
	}
	m_url->assign(*value);
	return true;
}
