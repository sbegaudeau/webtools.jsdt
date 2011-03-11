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

bool CrossfireBPManager::clearBreakpoint(unsigned int handle) {
	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
	if (iterator == m_breakpoints->end()) {
		Logger::error("clearBreakpoint: unknown breakpoint handle", handle);
		return false;
	}

	delete iterator->second;
	m_breakpoints->erase(iterator);
	return true;
}

bool CrossfireBPManager::commandChangeBreakpoint(Value* arguments, IBreakpointTarget* target, Value** _responseBody) {
	*_responseBody = NULL;

	Value* value_handle = arguments->getObjectValue(CrossfireBreakpoint::KEY_HANDLE);
	if (!value_handle || value_handle->getType() != TYPE_NUMBER || (unsigned int)value_handle->getNumberValue() < 1) {
		Logger::error("'changeBreakpoint' command does not have a valid 'handle' value ");
		return false;
	}

	Value* value_condition = arguments->getObjectValue(CrossfireBreakpoint::KEY_CONDITION);
	if (value_condition && (value_condition->getType() & (TYPE_NULL | TYPE_STRING)) == 0) {
		Logger::error("'changeBreakpoint' command has 'condition' value of type != String | Null");
		return false;
	}

	Value* value_enabled = arguments->getObjectValue(CrossfireBreakpoint::KEY_ENABLED);
	if (value_enabled && value_enabled->getType() != TYPE_BOOLEAN) {
		Logger::error("'changeBreakpoint' command has 'enabled' value of type != Boolean");
		return false;
	}

	unsigned int handle = (unsigned int)value_handle->getNumberValue();
	if (value_enabled) {
		if (!target->setBreakpointEnabled(handle, value_enabled->getBooleanValue())) {
			return false;
		}
		if (!this->setBreakpointEnabled(handle, value_enabled->getBooleanValue())) {
			return false;
		}
	}
	if (value_condition) {
		std::wstring* condition = value_condition->getType() == TYPE_STRING ? value_condition->getStringValue() : NULL;
		if (!target->setBreakpointCondition(handle, condition)) {
			return false;
		}
		if (!this->setBreakpointCondition(handle, condition)) {
			return false;
		}
	}

	Value* result = new Value();
	result->setType(TYPE_OBJECT);
	*_responseBody = result;
	return true;
}

bool CrossfireBPManager::commandClearBreakpoint(Value* arguments, IBreakpointTarget* target, Value** _responseBody) {
	*_responseBody = NULL;

	Value* value_handle = arguments->getObjectValue(CrossfireBreakpoint::KEY_HANDLE);
	if (!value_handle || value_handle->getType() != TYPE_NUMBER || (unsigned int)value_handle->getNumberValue() < 1) {
		Logger::error("'clearBreakpoint' command does not have a valid 'handle' value ");
		return false;
	}

	if (!target->clearBreakpoint((unsigned int)value_handle->getNumberValue())) {
		return false;
	}
	if (!this->clearBreakpoint((unsigned int)value_handle->getNumberValue())) {
		return false;
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

	CrossfireBreakpoint* breakpoint = NULL;
	unsigned int handle = (unsigned int)value_handle->getNumberValue();
	if (!target->getBreakpoint(handle, &breakpoint)) {
		return false;
	}

	Value* value_breakpoint = NULL;
	bool success = breakpoint->toValueObject(&value_breakpoint);
	delete breakpoint;
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

bool CrossfireBPManager::commandSetBreakpoint(Value* arguments, IBreakpointTarget* target, Value** _responseBody) {
	*_responseBody = NULL;

	CrossfireBreakpoint* breakpoint = NULL;
	if (!createBreakpoint(arguments, &breakpoint)) {
		return false;
	}

	CrossfireBreakpoint* resultBreakpoint = NULL;
	bool success = setBreakpointOnTarget(breakpoint, target, &resultBreakpoint);
	//delete breakpoint;
	if (!success) {
		delete breakpoint;
		return false;
	}

	setBreakpointOnTarget(breakpoint, this, NULL);
	delete breakpoint;

	Value* value_breakpoint = NULL;
	success = resultBreakpoint->toValueObject(&value_breakpoint);
	delete resultBreakpoint;
	if (!success) {
		return false;
	}
	Value* result = new Value();
	result->addObjectValue(KEY_BREAKPOINT, value_breakpoint);
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

	Value* value_condition = arguments->getObjectValue(CrossfireBreakpoint::KEY_CONDITION);
	if (value_condition && (value_condition->getType() & (TYPE_NULL | TYPE_STRING)) == 0) {
		Logger::error("breakpoint creation arguments have a 'condition' value of type != String | Null");
		return false;
	}

	Value* value_enabled = arguments->getObjectValue(CrossfireBreakpoint::KEY_ENABLED);
	if (value_enabled && value_enabled->getType() != TYPE_BOOLEAN) {
		Logger::error("breakpoint creation arguments have an 'enabled' value of type != Boolean");
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

	if (value_condition && value_condition->getType() == TYPE_STRING) {
		breakpoint->setCondition(value_condition->getStringValue());
	}
	if (value_enabled) {
		breakpoint->setEnabled(value_enabled->getBooleanValue());
	}
	if (!breakpoint->setLocationFromValue(value_location)) {
		delete breakpoint;
		return false;
	}

	*_result = breakpoint;
	return true;
}

bool CrossfireBPManager::getBreakpoint(unsigned int handle, CrossfireBreakpoint** _value) {
	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
	if (iterator == m_breakpoints->end()) {
		Logger::error("getBreakpoint: unknown breakpoint handle");
		return false;
	}

	CrossfireBreakpoint* breakpoint = iterator->second;
	breakpoint->clone(_value);
	return true;
}

bool CrossfireBPManager::getBreakpoints(CrossfireBreakpoint*** ___values) {
	size_t size = m_breakpoints->size();
	CrossfireBreakpoint** breakpoints = new CrossfireBreakpoint*[size + 1];

	std::map<unsigned int, CrossfireBreakpoint*>::iterator it = m_breakpoints->begin();
	std::map<unsigned int, CrossfireBreakpoint*>::iterator end = m_breakpoints->end();
	int index = 0;
	while (it != end) {
		it->second->clone(&breakpoints[index++]);
		it++;
	}
	breakpoints[index] = NULL;

	*___values = breakpoints;
	return true;
}

bool CrossfireBPManager::setBreakpointCondition(unsigned int handle, std::wstring* condition) {
	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
	if (iterator == m_breakpoints->end()) {
		Logger::error("setBreakpointCondition: unknown breakpoint handle", handle);
		return false;
	}

	CrossfireBreakpoint* breakpoint = iterator->second;
	breakpoint->setCondition(condition);
	return true;
}

bool CrossfireBPManager::setBreakpointEnabled(unsigned int handle, bool enabled) {
	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
	if (iterator == m_breakpoints->end()) {
		Logger::error("setBreakpointEnabled: unknown breakpoint handle", handle);
		return false;
	}

	CrossfireBreakpoint* breakpoint = iterator->second;
	breakpoint->setEnabled(enabled);
	return true;
}

bool CrossfireBPManager::setBreakpointOnTarget(CrossfireBreakpoint* breakpoint, IBreakpointTarget* target, CrossfireBreakpoint** _resultBreakpoint) {
	if (_resultBreakpoint) {
		*_resultBreakpoint = NULL;
	}

	bool success = false;
	switch (breakpoint->getType()) {
		case CrossfireBreakpoint::BPTYPE_LINE: {
			CrossfireLineBreakpoint* copy = NULL;
			((CrossfireLineBreakpoint*)breakpoint)->clone((CrossfireBreakpoint**)&copy);
			success = target->setLineBreakpoint(copy, false);
			if (success && _resultBreakpoint) {
				*_resultBreakpoint = copy;
			} else {
				delete copy;
			}
			break;
		}
	}
	return success;
}

void CrossfireBPManager::setBreakpointsForScript(std::wstring* url, IBreakpointTarget* target) {
	std::map<unsigned int,CrossfireBreakpoint*>::iterator iterator = m_breakpoints->begin();
	while (iterator != m_breakpoints->end()) {
		CrossfireBreakpoint* breakpoint = iterator->second;
		if (breakpoint->appliesToUrl(url)) {
			bool success = setBreakpointOnTarget(breakpoint, target, NULL);
		}
		iterator++;
	}
}

bool CrossfireBPManager::setLineBreakpoint(CrossfireLineBreakpoint *breakpoint, bool isRetry) {
	CrossfireLineBreakpoint* copy = NULL;
	breakpoint->clone((CrossfireBreakpoint**)&copy);
	m_breakpoints->insert(std::pair<unsigned int, CrossfireBreakpoint*>(breakpoint->getHandle(), copy));
	return true;
}
