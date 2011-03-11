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

#include "IBreakpointTarget.h"
#include "Logger.h"
#include "Value.h"

class CrossfireBPManager : IBreakpointTarget {

public:
	CrossfireBPManager();
	~CrossfireBPManager();

	virtual bool commandChangeBreakpoint(Value* arguments, IBreakpointTarget* target, Value** _responseBody);
	virtual bool commandClearBreakpoint(Value* arguments, IBreakpointTarget* target, Value** _responseBody);
	virtual bool commandGetBreakpoint(Value* arguments, IBreakpointTarget* target, Value** _responseBody);
	virtual bool commandGetBreakpoints(Value* arguments, IBreakpointTarget* target, Value** _responseBody);
	virtual bool commandSetBreakpoint(Value* arguments, IBreakpointTarget* target, Value** _responseBody);
	virtual void setBreakpointsForScript(std::wstring* url, IBreakpointTarget* target);

	/* IBreakpointTarget methods */
	virtual bool clearBreakpoint(unsigned int handle);
	virtual bool getBreakpoint(unsigned int handle, CrossfireBreakpoint** _value);
	virtual bool getBreakpoints(CrossfireBreakpoint*** ___values);
	virtual bool setBreakpointCondition(unsigned int handle, std::wstring* condition);
	virtual bool setBreakpointEnabled(unsigned int handle, bool enabled);
	virtual bool setLineBreakpoint(CrossfireLineBreakpoint* breakpoint, bool isRetry);

private:
	virtual bool createBreakpoint(Value* arguments, CrossfireBreakpoint** _result);
	virtual bool setBreakpointOnTarget(CrossfireBreakpoint* breakpoint, IBreakpointTarget* target, CrossfireBreakpoint** _resultBreakpoint);

	std::map<unsigned int, CrossfireBreakpoint*>* m_breakpoints;

	static const wchar_t* KEY_BREAKPOINT;
	static const wchar_t* KEY_BREAKPOINTS;
};
