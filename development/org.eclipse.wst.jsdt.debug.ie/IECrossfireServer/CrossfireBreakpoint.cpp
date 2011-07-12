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
const wchar_t* CrossfireBreakpoint::KEY_ATTRIBUTES = L"attributes";
const wchar_t* CrossfireBreakpoint::KEY_CONTEXTID = L"contextId";
const wchar_t* CrossfireBreakpoint::KEY_HANDLE = L"handle";
const wchar_t* CrossfireBreakpoint::KEY_LOCATION = L"location";
const wchar_t* CrossfireBreakpoint::KEY_TYPE = L"type";

CrossfireBreakpoint::CrossfireBreakpoint() {
	static unsigned int s_nextBreakpointHandle = 1;
	m_attributes = new std::map<std::wstring, Value*>;
	m_contextId = NULL;
	m_handle = s_nextBreakpointHandle++;
	m_target = NULL;
}

CrossfireBreakpoint::CrossfireBreakpoint(unsigned int handle) {
	m_attributes = new std::map<std::wstring, Value*>;
	m_contextId = NULL;
	m_handle = handle;
	m_target = NULL;
}

CrossfireBreakpoint::~CrossfireBreakpoint() {
	std::map<std::wstring, Value*>::iterator iterator = m_attributes->begin();
	while (iterator != m_attributes->end()) {
		delete iterator->second;
		iterator++;
	}
	delete m_attributes;
	if (m_contextId) {
		delete m_contextId;
	}
}

bool CrossfireBreakpoint::appliesToUrl(std::wstring* url) {
	return true;
}

void CrossfireBreakpoint::breakpointHit() {
}

Value* CrossfireBreakpoint::getAttribute(wchar_t* name) {
	std::map<std::wstring, Value*>::iterator iterator = m_attributes->find(std::wstring(name));
	if (iterator == m_attributes->end()) {
		return NULL;
	}
	return iterator->second;
}

const std::wstring* CrossfireBreakpoint::getContextId() {
	return m_contextId;
}

unsigned int CrossfireBreakpoint::getHandle() {
	return m_handle;
}

IBreakpointTarget* CrossfireBreakpoint::getTarget() {
	return m_target;
}

void CrossfireBreakpoint::setAttribute(wchar_t* name, Value* value) {
	std::map<std::wstring, Value*>::iterator iterator = m_attributes->find(std::wstring(name));
	if (iterator != m_attributes->end()) {
		if (iterator->second->equals(value)) {
			return;
		}
		delete iterator->second;
		m_attributes->erase(iterator);
	}

	if (value->getType() == TYPE_NULL) {
		/* a null value indicates that the attribute value should be cleared */
		return;
	}

	Value* valueCopy = NULL;
	value->clone(&valueCopy);
	m_attributes->insert(std::pair<std::wstring, Value*>(std::wstring(name), valueCopy));
	if (m_target) {
		m_target->breakpointAttributeChanged(m_handle, name, value);
	}
}

bool CrossfireBreakpoint::setAttributesFromValue(Value* value) {
	std::wstring** objectKeys = NULL;
	Value** objectValues = NULL;
	value->getObjectValues(&objectKeys, &objectValues);
	bool success = true;
	int index = 0;
	std::wstring* currentKey = objectKeys[index];
	while (currentKey) {
		Value* currentValue = objectValues[index];
		if (currentValue->getType() != TYPE_NULL && !attributeIsValid((wchar_t*)currentKey->c_str(), currentValue)) {
			success = false;
			break;
		}
		currentKey = objectKeys[++index];
	}
	if (success) {
		index = 0;
		currentKey = objectKeys[index];
		while (currentKey) {
			setAttribute((wchar_t*)currentKey->c_str(), objectValues[index]);
			currentKey = objectKeys[++index];
		}
	}

	delete[] objectKeys;
	delete[] objectValues;
	return success;
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

void CrossfireBreakpoint::setTarget(IBreakpointTarget* value) {
	m_target = value;
}

bool CrossfireBreakpoint::toValueObject(Value** _value) {
	Value value_null;
	value_null.setType(TYPE_NULL);

	Value* result = new Value();
	result->addObjectValue(KEY_HANDLE, &Value((double)m_handle));
	result->addObjectValue(KEY_TYPE, &Value(getTypeString()));
	result->addObjectValue(KEY_CONTEXTID, m_contextId ? &Value(m_contextId) : &value_null);

	Value value_attributes;
	value_attributes.setType(TYPE_OBJECT);
	std::map<std::wstring, Value*>::iterator iterator = m_attributes->begin();
	while (iterator != m_attributes->end()) {
		value_attributes.addObjectValue((std::wstring*)&iterator->first, iterator->second);
		iterator++;
	}
	result->addObjectValue(KEY_ATTRIBUTES, &value_attributes);

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
