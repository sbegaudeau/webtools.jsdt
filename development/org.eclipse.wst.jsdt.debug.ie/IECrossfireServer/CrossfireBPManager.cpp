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
#include "CrossfireBPManager.h"

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

void CrossfireBPManager::getBreakpoints(CrossfireBreakpoint*** ___values) {
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
}

bool CrossfireBPManager::setBreakpoint(CrossfireBreakpoint *breakpoint) {
	CrossfireLineBreakpoint* lineBp = (CrossfireLineBreakpoint*)breakpoint;
	CrossfireLineBreakpoint* copy = NULL;
	lineBp->clone((CrossfireBreakpoint**)&copy);
	unsigned int handle = lineBp->getHandle();

	/* if a breakpoint with a duplicate handle exists then replace it with the new breakpoint */
	std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
	if (iterator != m_breakpoints->end()) {
		delete iterator->second;
		m_breakpoints->erase(iterator);
	}

	m_breakpoints->insert(std::pair<unsigned int, CrossfireBreakpoint*>(handle, copy));
	return true;
}

/* CrossfireBPManager */

int CrossfireBPManager::commandChangeBreakpoints(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message) {
	*_responseBody = NULL;

	Value* value_handles = arguments->getObjectValue(CrossfireBreakpoint::KEY_HANDLES);
	if (!value_handles || value_handles->getType() != TYPE_ARRAY) {
		*_message = _wcsdup(L"'changeBreakpoints' command does not have a valid 'handles' value");
		return CODE_INVALID_ARGUMENT;
	}

	Value* value_attributes = arguments->getObjectValue(CrossfireBreakpoint::KEY_ATTRIBUTES);
	if (!value_attributes || value_attributes->getType() != TYPE_OBJECT) {
		*_message = _wcsdup(L"'changeBreakpoints' command does not have a valid 'attributes' value");
		return CODE_INVALID_ARGUMENT;
	}

	int code = CODE_OK;
	Value** handles = NULL;
	value_handles->getArrayValues(&handles);
	int handlesIndex = 0;
	Value* value_handle = handles[handlesIndex++];
	while (value_handle) {
		if (value_handle->getType() != TYPE_NUMBER || value_handle->getNumberValue() < 1) {
			*_message = _wcsdup(L"'changeBreakpoints' command specifies an invalid handle value");
			code = CODE_INVALID_ARGUMENT;
			break;
		}

		/* ensure that the handle is valid */
		unsigned int handle = (unsigned int)value_handle->getNumberValue();
		CrossfireBreakpoint* breakpoint = getBreakpoint(handle);
		if (!breakpoint) {
			*_message = _wcsdup(L"'changeBreakpoints' command specifies an unknown breakpoint handle");
			code = CODE_COMMAND_FAILED;
			break;
		}

		/* ensure that all attributes can be applied to the current breakpoint */
		if (!breakpoint->attributesValueIsValid(value_attributes)) {
			*_message = _wcsdup(L"'changeBreakpoints' command specifies an invalid breakpoint attribute");
			code = CODE_COMMAND_FAILED;
			break;
		}

		value_handle = handles[handlesIndex++];
	}

	if (code != CODE_OK) {
		delete[] handles;
		return code;
	}

	/* the request is valid, so now perform the changes */

	handlesIndex = 0;
	value_handle = handles[handlesIndex++];
	while (value_handle) {
		unsigned int handle = (unsigned int)value_handle->getNumberValue();
		CrossfireBreakpoint* breakpoint = getBreakpoint(handle);
		breakpoint->setAttributesFromValue(value_attributes);

		/* it's valid for the breakpoint to not be set in some (or even all) of the targets */
		if (targets) {
			int targetsIndex = 0;
			IBreakpointTarget* target = targets[targetsIndex++];
			while (target != NULL) {
				CrossfireBreakpoint* breakpoint = target->getBreakpoint(handle);
				if (breakpoint) {
					breakpoint->setAttributesFromValue(value_attributes);
				}
				target = targets[targetsIndex++];
			}
		}

		value_handle = handles[handlesIndex++];
	}
	delete[] handles;

	Value* result = new Value();
	result->setType(TYPE_OBJECT);
	*_responseBody = result;
	return CODE_OK;
}

int CrossfireBPManager::commandDeleteBreakpoints(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message) {
	*_responseBody = NULL;

	Value* value_handles = arguments->getObjectValue(CrossfireBreakpoint::KEY_HANDLES);
	if (!value_handles || value_handles->getType() != TYPE_ARRAY) {
		*_message = _wcsdup(L"'deleteBreakpoints' command does not have a valid 'handles' value");
		return CODE_INVALID_ARGUMENT;
	}

	Value** handles = NULL;
	value_handles->getArrayValues(&handles);
	int index = 0;
	Value* value_current = handles[index++];
	while (value_current) {
		if (value_current->getType() != TYPE_NUMBER || (unsigned int)value_current->getNumberValue() < 1) {
			*_message = _wcsdup(L"'deleteBreakpoints' command specifies an invalid handle value");
			delete[] handles;
			return CODE_INVALID_ARGUMENT;
		}

		unsigned int handle = (unsigned int)value_current->getNumberValue();

		/* look up the breakpoint in the local breakpoints table to ensure that the handle is valid */
		std::map<unsigned int, CrossfireBreakpoint*>::iterator iterator = m_breakpoints->find(handle);
		if (iterator == m_breakpoints->end()) {
			*_message = _wcsdup(L"'deleteBreakpoints' command specifies an unknown breakpoint handle");
			delete[] handles;
			return CODE_COMMAND_FAILED;
		}
		value_current = handles[index++];
	}

	/* the request is valid, so now perform the deletions */

	index = 0;
	value_current = handles[index++];
	while (value_current) {
		unsigned int handle = (unsigned int)value_current->getNumberValue();
		deleteBreakpoint(handle); /* delete from the global table */

		/*
		 * It's valid for the breakpoint to not be set in some (or even all) of the targets,
		 * so a false result value for the deleteBreakpoint() call does not indicate a problem.
		 */
		if (targets) {
			int targetsIndex = 0;
			IBreakpointTarget* current = targets[targetsIndex++];
			while (current != NULL) {
				current->deleteBreakpoint(handle);
				current = targets[targetsIndex++];
			}
		}
		value_current = handles[index++];
	}
	delete[] handles;

	Value* result = new Value();
	result->setType(TYPE_OBJECT);
	*_responseBody = result;
	return CODE_OK;
}

int CrossfireBPManager::commandGetBreakpoints(Value* arguments, IBreakpointTarget* target, Value** _responseBody, wchar_t** _message) {
	CrossfireBreakpoint** breakpoints = NULL;
	Value* value_handles = arguments->getObjectValue(CrossfireBreakpoint::KEY_HANDLES);
	if (value_handles) {
		if (value_handles->getType() != TYPE_ARRAY) {
			*_message = _wcsdup(L"'getBreakpoints' command has an invalid 'handles' value");
			return CODE_INVALID_ARGUMENT;
		}
		std::vector<CrossfireBreakpoint*> breakpointsCollection;
		Value** handles = NULL;
		value_handles->getArrayValues(&handles);
		int index = 0;
		Value* value_current = handles[index++];
		while (value_current) {
			if (value_current->getType() == TYPE_NUMBER && value_current->getNumberValue() > 0) {
				unsigned int handle = (unsigned int)value_current->getNumberValue();
				CrossfireBreakpoint* breakpoint = target->getBreakpoint(handle);
				if (breakpoint) {
					breakpointsCollection.push_back(breakpoint);
				}
			}
			value_current = handles[index++];
		}
		delete[] handles;

		int length = breakpointsCollection.size();
		breakpoints = new CrossfireBreakpoint*[length + 1];
		for (int i = 0; i < length; i++) {
			breakpoints[i] = breakpointsCollection.at(i);
		}
		breakpoints[length] = NULL;
	} else {
		target->getBreakpoints(&breakpoints);
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

int CrossfireBPManager::commandSetBreakpoints(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message) {
	*_responseBody = NULL;

	Value* value_breakpoints = arguments->getObjectValue(KEY_BREAKPOINTS);
	if (!value_breakpoints || value_breakpoints->getType() != TYPE_ARRAY) {
		*_message = _wcsdup(L"'setBreakpoints' command does not have a valid 'breakpoints' value");
		return CODE_INVALID_ARGUMENT;
	}

	std::vector<CrossfireBreakpoint*> bpObjects;

	Value** breakpoints = NULL;
	value_breakpoints->getArrayValues(&breakpoints);
	int index = 0;
	Value* current = breakpoints[index++];
	int code = CODE_OK;
	while (current) {
		CrossfireBreakpoint* breakpoint = NULL;
		code = createBreakpoint(current, &breakpoint, _message);
		if (code != CODE_OK) {
			break;
		}
		bpObjects.push_back(breakpoint);
		current = breakpoints[index++];
	}
	delete[] breakpoints;

	if (code == CODE_OK) {
		Value value_array;
		value_array.setType(TYPE_ARRAY);

		std::vector<CrossfireBreakpoint*>::iterator iterator = bpObjects.begin();
		while (iterator != bpObjects.end()) {
			CrossfireBreakpoint* breakpoint = *iterator;

			/* search registered breakpoints for a duplicate */
			std::map<unsigned int,CrossfireBreakpoint*>::iterator iterator2 = m_breakpoints->begin();
			while (iterator2 != m_breakpoints->end()) {
				CrossfireBreakpoint* current = iterator2->second;
				if (current->matchesLocation(breakpoint)) {
					breakpoint->setHandle(current->getHandle());
					break;
				}
				iterator2++;
			}
			breakpoint->setTarget(this);
			setBreakpoint(breakpoint);
			if (targets) {
				int index = 0;
				IBreakpointTarget* current = targets[index++];
				while (current != NULL) {
					breakpoint->setTarget(current);
					current->setBreakpoint(breakpoint);
					current = targets[index++];
				}
			}

			Value* value_breakpoint = NULL;
			breakpoint->toValueObject(&value_breakpoint);
			value_array.addArrayValue(value_breakpoint);
			delete value_breakpoint;
			delete *iterator;
			iterator++;
		}

		Value* result = new Value();
		result->addObjectValue(KEY_BREAKPOINTS, &value_array);
		*_responseBody = result;
	}

	return code;
}

int CrossfireBPManager::createBreakpoint(Value* arguments, CrossfireBreakpoint** _result, wchar_t** _message) {
	*_result = NULL;

	Value* value_type = arguments->getObjectValue(CrossfireBreakpoint::KEY_TYPE);
	if (!value_type || value_type->getType() != TYPE_STRING) {
		*_message = _wcsdup(L"breakpoint creation arguments do not have a valid 'type' value");
		return CODE_INVALID_ARGUMENT;
	}

	Value* value_attributes = arguments->getObjectValue(CrossfireBreakpoint::KEY_ATTRIBUTES);
	if (value_attributes && value_attributes->getType() != TYPE_OBJECT) {
		*_message = _wcsdup(L"breakpoint creation arguments have an invalid 'attributes' value");
		return CODE_INVALID_ARGUMENT;
	}

	Value* value_location = arguments->getObjectValue(CrossfireBreakpoint::KEY_LOCATION);
	if (!value_location || value_location->getType() != TYPE_OBJECT) {
		*_message = _wcsdup(L"breakpoint creation arguments do not have a valid 'location' value");
		return CODE_INVALID_ARGUMENT;
	}

	CrossfireBreakpoint* breakpoint = NULL;
	wchar_t* type = (wchar_t*)value_type->getStringValue()->c_str();
	if (CrossfireLineBreakpoint::CanHandleBPType(type)) {
		breakpoint = new CrossfireLineBreakpoint();
	} else {
		*_message = _wcsdup(L"breakpoint creation arguments specify an unknown 'type' value");
		return CODE_COMMAND_FAILED;
	}

	if (value_attributes) {
		if (!breakpoint->attributesValueIsValid(value_attributes)) {
			*_message = _wcsdup(L"breakpoint creation arguments specify an invalid attribute name or value");
			delete breakpoint;
			return CODE_COMMAND_FAILED;
		}
		breakpoint->setAttributesFromValue(value_attributes);
	}

	if (!breakpoint->setLocationFromValue(value_location)) {
		*_message = _wcsdup(L"breakpoint creation arguments do not validly specify a location");
		delete breakpoint;
		return CODE_COMMAND_FAILED;
	}

	*_result = breakpoint;
	return CODE_OK;
}

void CrossfireBPManager::setBreakpointsForScript(URL* url, IBreakpointTarget* target) {
	std::map<unsigned int,CrossfireBreakpoint*>::iterator iterator = m_breakpoints->begin();
	while (iterator != m_breakpoints->end()) {
		CrossfireBreakpoint* breakpoint = iterator->second;
		if (breakpoint->appliesToUrl(url)) {
			target->setBreakpoint(breakpoint);
		}
		iterator++;
	}
}
