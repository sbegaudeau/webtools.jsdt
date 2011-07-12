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
#include "CrossfireBPManager.h"

const wchar_t* CrossfireBPManager::KEY_BREAKPOINT = L"breakpoint";
const wchar_t* CrossfireBPManager::KEY_BREAKPOINTS = L"breakpoints";

CrossfireBPManager::CrossfireBPManager() {
	m_breakpoints = new std::map<unsigned int, CrossfireBreakpoint*>;
}

CrossfireBPManager::~CrossfireBPManager() {
	std::map<unsigned int,CrossfireBreakpoint*>::iterator iterator = m_breakpoints->begin();
	while (iterator != m_breakpoints->end()) {
		delete iterator->second;
		iterator++;
	}
	delete m_breakpoints;
}

/* IBreakpointTarget */

bool CrossfireBPManager::breakpointAttributeChanged(unsigned int handle, wchar_t* name, Value* value) {
	/* no further action required since this object just stores the breakpoint data */
	return true;
}

bool CrossfireBPManager::deleteBreakpoint(unsigned int handle) {
	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
	if (iterator == m_breakpoints->end()) {
		return false;
	}

	delete iterator->second;
	m_breakpoints->erase(iterator);
	return true;
}

CrossfireBreakpoint* CrossfireBPManager::getBreakpoint(unsigned int handle) {
	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
	if (iterator == m_breakpoints->end()) {
		Logger::error("CrossfireBPManager.getBreakpoint(): unknown breakpoint handle", handle);
		return NULL;
	}

	return iterator->second;
}

bool CrossfireBPManager::getBreakpoints(CrossfireBreakpoint*** ___values) {
	size_t size = m_breakpoints->size();
	CrossfireBreakpoint** breakpoints = new CrossfireBreakpoint*[size + 1];

	std::map<unsigned int, CrossfireBreakpoint*>::iterator it = m_breakpoints->begin();
	int index = 0;
	while (it != m_breakpoints->end()) {
		it->second->clone(&breakpoints[index++]);
		it++;
	}
	breakpoints[index] = NULL;

	*___values = breakpoints;
	return true;
}

bool CrossfireBPManager::setBreakpoint(CrossfireBreakpoint *breakpoint, bool isRetry) {
	CrossfireLineBreakpoint* lineBp = (CrossfireLineBreakpoint*)breakpoint;
	CrossfireLineBreakpoint* copy = NULL;
	lineBp->clone((CrossfireBreakpoint**)&copy);
	m_breakpoints->insert(std::pair<unsigned int, CrossfireBreakpoint*>(lineBp->getHandle(), copy));
	return true;
}

/* CrossfireBPManager */

int CrossfireBPManager::commandChangeBreakpoint(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message) {
	*_responseBody = NULL;

	Value* value_handle = arguments->getObjectValue(CrossfireBreakpoint::KEY_HANDLE);
	if (!value_handle || value_handle->getType() != TYPE_NUMBER || (unsigned int)value_handle->getNumberValue() < 1) {
		*_message = _wcsdup(L"'changeBreakpoint' command does not have a valid 'handle' value");
		return CODE_INVALIDPACKET;
	}

	Value* value_attributes = arguments->getObjectValue(CrossfireBreakpoint::KEY_ATTRIBUTES);
	if (!value_attributes || value_attributes->getType() != TYPE_OBJECT) {
		*_message = _wcsdup(L"'changeBreakpoint' command does not have a valid 'attributes' value");
		return CODE_INVALIDPACKET;
	}

	unsigned int handle = (unsigned int)value_handle->getNumberValue();
	CrossfireBreakpoint* breakpoint = getBreakpoint(handle);
	if (!breakpoint) {
		*_message = _wcsdup(L"'changeBreakpoint' command specifies an unknown breakpoint handle");
		return CODE_INVALIDPACKET;
	}

	if (!breakpoint->setAttributesFromValue(value_attributes)) {
		*_message = _wcsdup(L"'changeBreakpoint' command specifies an invalid attribute name or value");
		return CODE_INVALIDPACKET;
	}

	/* it's valid for the breakpoint to not be set in some (or even all) of the targets */
	if (targets) {
		int index = 0;
		IBreakpointTarget* current = targets[index++];
		while (current != NULL) {
			CrossfireBreakpoint* breakpoint = current->getBreakpoint(handle);
			if (breakpoint) {
				breakpoint->setAttributesFromValue(value_attributes);
			}
			current = targets[index++];
		}
	}

	Value* result = new Value();
	result->setType(TYPE_OBJECT);
	*_responseBody = result;
	return CODE_OK;
}

int CrossfireBPManager::commandDeleteBreakpoint(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message) {
	*_responseBody = NULL;

	Value* value_handle = arguments->getObjectValue(CrossfireBreakpoint::KEY_HANDLE);
	if (!value_handle || value_handle->getType() != TYPE_NUMBER || (unsigned int)value_handle->getNumberValue() < 1) {
		*_message = _wcsdup(L"'deleteBreakpoint' command does not have a valid 'handle' value");
		return CODE_INVALIDPACKET;
	}

	unsigned int handle = (unsigned int)value_handle->getNumberValue();

	/*
	 * Attempt to clear the breakpoint in the local breakpoints table first,
	 * as this is where an invalid breakpoint handle can be easily detected.
	 */
	if (!deleteBreakpoint(handle)) {
		*_message = _wcsdup(L"'deleteBreakpoint' command specifies an unknown breakpoint handle");
		return CODE_INVALIDPACKET;
	}

	/*
	 * It's valid for the breakpoint to not be set in some (or even all) of the targets,
	 * so a false result value for the deleteBreakpoint() call does not indicate a problem.
	 */
	if (targets) {
		int index = 0;
		IBreakpointTarget* current = targets[index++];
		while (current != NULL) {
			current->deleteBreakpoint(handle);
			current = targets[index++];
		}
	}

	Value* result = new Value();
	result->setType(TYPE_OBJECT);
	*_responseBody = result;
	return CODE_OK;
}

int CrossfireBPManager::commandGetBreakpoint(Value* arguments, IBreakpointTarget* target, Value** _responseBody, wchar_t** _message) {
	*_responseBody = NULL;

	Value* value_handle = arguments->getObjectValue(CrossfireBreakpoint::KEY_HANDLE);
	if (!value_handle || value_handle->getType() != TYPE_NUMBER || (unsigned int)value_handle->getNumberValue() < 1) {
		*_message = _wcsdup(L"'getBreakpoint' command does not have a valid 'handle' value");
		return CODE_INVALIDPACKET;
	}

	unsigned int handle = (unsigned int)value_handle->getNumberValue();
	CrossfireBreakpoint* breakpoint = target->getBreakpoint(handle);
	if (!breakpoint) {
		*_message = _wcsdup(L"'getBreakpoint' command specifies an unknown breakpoint handle");
		return CODE_INVALIDPACKET;
	}

	Value* value_breakpoint = NULL;
	bool success = breakpoint->toValueObject(&value_breakpoint);
	if (!success) {
		return CODE_FAILURE;
	}

	Value* result = new Value();
	result->addObjectValue(KEY_BREAKPOINT, value_breakpoint);
	delete value_breakpoint;
	*_responseBody = result;
	return CODE_OK;
}

int CrossfireBPManager::commandGetBreakpoints(Value* arguments, IBreakpointTarget* target, Value** _responseBody, wchar_t** _message) {
	CrossfireBreakpoint** breakpoints = NULL;
	if (!target->getBreakpoints(&breakpoints)) {
		return CODE_FAILURE;
	}

	Value breakpointsArray;
	breakpointsArray.setType(TYPE_ARRAY);
	int index = 0;
	while (breakpoints[index]) {
		CrossfireBreakpoint* current = breakpoints[index];
		Value* value_breakpoint = NULL;
		if (current->toValueObject(&value_breakpoint)) {
			breakpointsArray.addArrayValue(value_breakpoint);
			delete value_breakpoint;
		}
		delete current;
		index++;
	}
	delete[] breakpoints;

	Value* result = new Value();
	result->addObjectValue(KEY_BREAKPOINTS, &breakpointsArray);
	*_responseBody = result;
	return CODE_OK;
}

int CrossfireBPManager::commandSetBreakpoint(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message) {
	*_responseBody = NULL;

	CrossfireBreakpoint* breakpoint = NULL;
	int code = createBreakpoint(arguments, &breakpoint, _message);
	if (code != CODE_OK) {
		return code;
	}

	breakpoint->setTarget(this);
	setBreakpoint(breakpoint, false);
	if (targets) {
		int index = 0;
		IBreakpointTarget* current = targets[index++];
		while (current != NULL) {
			breakpoint->setTarget(current);
			current->setBreakpoint(breakpoint, false);
			current = targets[index++];
		}
	}

	Value* result = new Value();
	Value* value_breakpoint = NULL;
	breakpoint->toValueObject(&value_breakpoint);
	result->addObjectValue(KEY_BREAKPOINT, value_breakpoint);
	delete breakpoint;
	delete value_breakpoint;
	*_responseBody = result;
	return CODE_OK;
}

int CrossfireBPManager::createBreakpoint(Value* arguments, CrossfireBreakpoint** _result, wchar_t** _message) {
	*_result = NULL;

	Value* value_type = arguments->getObjectValue(CrossfireBreakpoint::KEY_TYPE);
	if (!value_type || value_type->getType() != TYPE_STRING) {
		*_message = _wcsdup(L"breakpoint creation arguments do not have a valid 'type' value");
		return CODE_INVALIDPACKET;
	}

	Value* value_attributes = arguments->getObjectValue(CrossfireBreakpoint::KEY_ATTRIBUTES);
	if (value_attributes && value_attributes->getType() != TYPE_OBJECT) {
		*_message = _wcsdup(L"breakpoint creation arguments have an invalid 'attributes' value");
		return CODE_INVALIDPACKET;
	}

	Value* value_location = arguments->getObjectValue(CrossfireBreakpoint::KEY_LOCATION);
	if (!value_location || value_location->getType() != TYPE_OBJECT) {
		*_message = _wcsdup(L"breakpoint creation arguments do not have a valid 'location' value");
		return CODE_INVALIDPACKET;
	}

	CrossfireBreakpoint* breakpoint = NULL;
	wchar_t* type = (wchar_t*)value_type->getStringValue()->c_str();
	if (CrossfireLineBreakpoint::CanHandleBPType(type)) {
		breakpoint = new CrossfireLineBreakpoint();
	} else {
		*_message = _wcsdup(L"breakpoint creation arguments specify an unknown 'type' value");
		return CODE_INVALIDPACKET;
	}

	if (value_attributes && !breakpoint->setAttributesFromValue(value_attributes)) {
		*_message = _wcsdup(L"breakpoint creation arguments specify an invalid attribute name or value");
		delete breakpoint;
		return CODE_INVALIDPACKET;
	}

	if (!breakpoint->setLocationFromValue(value_location)) {
		*_message = _wcsdup(L"breakpoint creation arguments do not validly specify a location");
		delete breakpoint;
		return CODE_INVALIDPACKET;
	}

	*_result = breakpoint;
	return CODE_OK;
}

void CrossfireBPManager::setBreakpointsForScript(std::wstring* url, IBreakpointTarget* target) {
	std::map<unsigned int,CrossfireBreakpoint*>::iterator iterator = m_breakpoints->begin();
	while (iterator != m_breakpoints->end()) {
		CrossfireBreakpoint* breakpoint = iterator->second;
		if (breakpoint->appliesToUrl(url)) {
			bool success = target->setBreakpoint(breakpoint, false);
			// TODO it looks like the result of the above line was to be used for something
		}
		iterator++;
	}
}

