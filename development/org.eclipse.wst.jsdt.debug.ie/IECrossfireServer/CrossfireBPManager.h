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

#include "CrossfireLineBreakpoint.h"
#include "CrossfireResponse.h"
#include "IBreakpointTarget.h"
#include "Logger.h"
#include "Value.h"

class CrossfireBPManager : IBreakpointTarget {

public:
	CrossfireBPManager();
	virtual ~CrossfireBPManager();

	int commandChangeBreakpoints(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message);
	int commandDeleteBreakpoints(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message);
	int commandGetBreakpoints(Value* arguments, IBreakpointTarget* target, Value** _responseBody, wchar_t** _message);
	int commandSetBreakpoints(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message);
	void setBreakpointsForScript(URL* url, IBreakpointTarget* target);

	/* IBreakpointTarget methods */
	bool breakpointAttributeChanged(unsigned int handle, wchar_t* name, Value* value);
	bool deleteBreakpoint(unsigned int handle);
	CrossfireBreakpoint* getBreakpoint(unsigned int handle);
	void getBreakpoints(CrossfireBreakpoint*** ___values);
	bool setBreakpoint(CrossfireBreakpoint* breakpoint);

private:
	int createBreakpoint(Value* arguments, CrossfireBreakpoint** _result, wchar_t** _message);

	std::map<unsigned int, CrossfireBreakpoint*>* m_breakpoints;

	static const wchar_t* KEY_BREAKPOINTS;
};
