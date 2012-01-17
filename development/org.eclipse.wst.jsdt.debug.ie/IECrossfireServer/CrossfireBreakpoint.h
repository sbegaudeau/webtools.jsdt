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


#pragma once

#include <map>

#include "Logger.h"
#include "URL.h"
class CrossfireBreakpoint; // forward declaration
#include "IBreakpointTarget.h"

class CrossfireBreakpoint {

public:
	virtual ~CrossfireBreakpoint();
	virtual bool appliesToUrl(URL* url); // TODO needed?
	virtual bool attributesValueIsValid(Value* attributes);
	virtual void breakpointHit();
	virtual void clone(CrossfireBreakpoint** _value) = 0;
	virtual const std::wstring* getContextId();
	virtual unsigned int getHandle();
	virtual IBreakpointTarget* getTarget();
	virtual int getType() = 0;
	virtual bool matchesLocation(CrossfireBreakpoint* breakpoint) = 0;
	virtual void setAttributesFromValue(Value* value);
	virtual void setContextId(std::wstring* value);
	virtual void setHandle(unsigned int value);
	virtual bool setLocationFromValue(Value* value) = 0;
	virtual void setTarget(IBreakpointTarget* value);
	virtual bool toValueObject(Value** _value);

	static const int BPTYPE_LINE = 1;
	static const wchar_t* KEY_ATTRIBUTES;
	static const wchar_t* KEY_CONTEXTID;
	static const wchar_t* KEY_HANDLE;
	static const wchar_t* KEY_HANDLES;
	static const wchar_t* KEY_LOCATION;
	static const wchar_t* KEY_TYPE;

protected:
	CrossfireBreakpoint();
	CrossfireBreakpoint(unsigned int handle);
	virtual bool attributeIsValid(wchar_t* name, Value* value) = 0;
	virtual Value* getAttribute(wchar_t* name);
	virtual bool getLocationAsValue(Value** _value) = 0;
	virtual const wchar_t* getTypeString() = 0;
	virtual void setAttribute(wchar_t* name, Value* value);

	unsigned int m_handle;

private:
	std::map<std::wstring, Value*>* m_attributes;
	std::wstring* m_contextId;
	IBreakpointTarget* m_target;
};
