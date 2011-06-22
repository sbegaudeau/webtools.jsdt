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
		Logger::error("CrossfireBPManager.deleteBreakpoint(): unknown breakpoint handle", handle);
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

bool CrossfireBPManager::commandChangeBreakpoint(Value* arguments, IBreakpointTarget** targets, Value** _responseBody) {
	*_responseBody = NULL;

	Value* value_handle = arguments->getObjectValue(CrossfireBreakpoint::KEY_HANDLE);
	if (!value_handle || value_handle->getType() != TYPE_NUMBER || (unsigned int)value_handle->getNumberValue() < 1) {
		Logger::error("'changeBreakpoint' command does not have a valid 'handle' value");
		return false;
	}

	Value* value_attributes = arguments->getObjectValue(CrossfireBreakpoint::KEY_ATTRIBUTES);
	if (!value_attributes || value_attributes->getType() != TYPE_OBJECT) {
		Logger::error("'changeBreakpoint' command does not have a valid 'attributes' value");
		return false;
	}

	unsigned int handle = (unsigned int)value_handle->getNumberValue();
	CrossfireBreakpoint* breakpoint = getBreakpoint(handle);
	if (!breakpoint || !breakpoint->setAttributesFromValue(value_attributes)) {
		return false;
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
	return true;
}

bool CrossfireBPManager::commandDeleteBreakpoint(Value* arguments, IBreakpointTarget** targets, Value** _responseBody) {
	*_responseBody = NULL;

	Value* value_handle = arguments->getObjectValue(CrossfireBreakpoint::KEY_HANDLE);
	if (!value_handle || value_handle->getType() != TYPE_NUMBER || (unsigned int)value_handle->getNumberValue() < 1) {
		Logger::error("'deleteBreakpoint' command does not have a valid 'handle' value");
		return false;
	}

	unsigned int handle = (unsigned int)value_handle->getNumberValue();

	/*
	 * Attempt to clear the breakpoint in the local breakpoints table first,
	 * as this is where an invalid breakpoint handle can be easily detected.
	 */
	if (!deleteBreakpoint(handle)) {
		return false;
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
	return true;
}

bool CrossfireBPManager::commandGetBreakpoint(Value* arguments, IBreakpointTarget* target, Value** _responseBody) {
	*_responseBody = NULL;

	Value* value_handle = arguments->getObjectValue(CrossfireBreakpoint::KEY_HANDLE);
	if (!value_handle || value_handle->getType() != TYPE_NUMBER || (unsigned int)value_handle->getNumberValue() < 1) {
		Logger::error("'getBreakpoint' command does not have a valid 'handle' value");
		return false;
	}

	unsigned int handle = (unsigned int)value_handle->getNumberValue();
	CrossfireBreakpoint* breakpoint = target->getBreakpoint(handle);
	if (!breakpoint) {
		return false;
	}

	Value* value_breakpoint = NULL;
	bool success = breakpoint->toValueObject(&value_breakpoint);
	if (!success) {
		return false;
	}

	Value* result = new Value();
	result->addObjectValue(KEY_BREAKPOINT, value_breakpoint);
	delete value_breakpoint;
	*_responseBody = result;
	return true;
}

bool CrossfireBPManager::commandGetBreakpoints(Value* arguments, IBreakpointTarget* target, Value** _responseBody) {
	CrossfireBreakpoint** breakpoints = NULL;
	if (!target->getBreakpoints(&breakpoints)) {
		return false;
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
	return true;
}

bool CrossfireBPManager::commandSetBreakpoint(Value* arguments, IBreakpointTarget** targets, Value** _responseBody) {
	*_responseBody = NULL;

	CrossfireBreakpoint* breakpoint = NULL;
	if (!createBreakpoint(arguments, &breakpoint)) {
		return false;
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
	return true;
}

bool CrossfireBPManager::createBreakpoint(Value* arguments, CrossfireBreakpoint** _result) {
	*_result = NULL;

	Value* value_type = arguments->getObjectValue(CrossfireBreakpoint::KEY_TYPE);
	if (!value_type || value_type->getType() != TYPE_STRING) {
		Logger::error("breakpoint creation arguments do not have a valid 'type' value");
		return false;
	}

	Value* value_attributes = arguments->getObjectValue(CrossfireBreakpoint::KEY_ATTRIBUTES);
	if (value_attributes && value_attributes->getType() != TYPE_OBJECT) {
		Logger::error("breakpoint creation arguments have an invalid 'attributes' value");
		return false;
	}

	Value* value_location = arguments->getObjectValue(CrossfireBreakpoint::KEY_LOCATION);
	if (!value_location || value_location->getType() != TYPE_OBJECT) {
		Logger::error("breakpoint creation arguments do not have a valid 'location' value");
		return false;
	}

	CrossfireBreakpoint* breakpoint = NULL;
	wchar_t* type = (wchar_t*)value_type->getStringValue()->c_str();
	if (CrossfireLineBreakpoint::CanHandleBPType(type)) {
		breakpoint = new CrossfireLineBreakpoint();
	} else {
		Logger::error("breakpoint creation arguments specify an unknown 'type' value");
		return false;
	}

	if (value_attributes && !breakpoint->setAttributesFromValue(value_attributes)) {
		delete breakpoint;
		return false;
	}

	if (!breakpoint->setLocationFromValue(value_location)) {
		delete breakpoint;
		return false;
	}

	*_result = breakpoint;
	return true;
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

