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

#include "CrossfireLineBreakpoint.h"
#include "IBreakpointTarget.h"
#include "Logger.h"
#include "Value.h"

class CrossfireBPManager : IBreakpointTarget {

public:
	CrossfireBPManager();
	~CrossfireBPManager();

	virtual bool commandChangeBreakpoint(Value* arguments, IBreakpointTarget** targets, Value** _responseBody);
	virtual bool commandDeleteBreakpoint(Value* arguments, IBreakpointTarget** targets, Value** _responseBody);
	virtual bool commandGetBreakpoint(Value* arguments, IBreakpointTarget* target, Value** _responseBody);
	virtual bool commandGetBreakpoints(Value* arguments, IBreakpointTarget* target, Value** _responseBody);
	virtual bool commandSetBreakpoint(Value* arguments, IBreakpointTarget** targets, Value** _responseBody);
	virtual void setBreakpointsForScript(std::wstring* url, IBreakpointTarget* target);

	/* IBreakpointTarget methods */
	virtual bool breakpointAttributeChanged(unsigned int handle, wchar_t* name, Value* value);
	virtual bool deleteBreakpoint(unsigned int handle);
	virtual CrossfireBreakpoint* getBreakpoint(unsigned int handle);
	virtual bool getBreakpoints(CrossfireBreakpoint*** ___values);
	virtual bool setBreakpoint(CrossfireBreakpoint* breakpoint, bool isRetry);

private:
	virtual bool createBreakpoint(Value* arguments, CrossfireBreakpoint** _result);

	std::map<unsigned int, CrossfireBreakpoint*>* m_breakpoints;

	static const wchar_t* KEY_BREAKPOINT;
	static const wchar_t* KEY_BREAKPOINTS;
};
