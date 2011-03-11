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


#pragma once

#include <map>

#include "Logger.h"
#include "Value.h"

class CrossfireBreakpoint {

public:
	virtual ~CrossfireBreakpoint();
	virtual bool appliesToUrl(std::wstring* url);
	virtual void clone(CrossfireBreakpoint** _value) = 0;
	virtual const std::wstring* getCondition();
	virtual const std::wstring* getContextId();
	virtual unsigned int getHandle();
	virtual int getType() = 0;
	virtual const wchar_t* getTypeString() = 0;
	virtual bool isEnabled();
	virtual void setCondition(std::wstring* value);
	virtual void setContextId(std::wstring* value);
	virtual void setEnabled(bool value);
	virtual bool setLocationFromValue(Value* value) = 0;
	virtual bool toValueObject(Value** _value);

	static const int BPTYPE_LINE = 1;
	static const wchar_t* KEY_CONDITION;
	static const wchar_t* KEY_CONTEXTID;
	static const wchar_t* KEY_ENABLED;
	static const wchar_t* KEY_HANDLE;
	static const wchar_t* KEY_LOCATION;
	static const wchar_t* KEY_TYPE;

protected:
	CrossfireBreakpoint();
	CrossfireBreakpoint(unsigned int handle);
	virtual bool getLocationAsValue(Value** _value) = 0;

	unsigned int m_handle;

private:
	std::wstring* m_condition;
	std::wstring* m_contextId;
	bool m_enabled;
};
