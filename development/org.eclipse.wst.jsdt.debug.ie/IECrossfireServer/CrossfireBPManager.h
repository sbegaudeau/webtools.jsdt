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
#include "CrossfireResponse.h"
#include "IBreakpointTarget.h"
#include "Logger.h"
#include "Value.h"

class CrossfireBPManager : IBreakpointTarget {

public:
	CrossfireBPManager();
	~CrossfireBPManager();

	virtual int commandChangeBreakpoints(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message);
	virtual int commandDeleteBreakpoints(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message);
	virtual int commandGetBreakpoints(Value* arguments, IBreakpointTarget* target, Value** _responseBody, wchar_t** _message);
	virtual int commandSetBreakpoints(Value* arguments, IBreakpointTarget** targets, Value** _responseBody, wchar_t** _message);
	virtual void setBreakpointsForScript(std::wstring* url, IBreakpointTarget* target);

	/* IBreakpointTarget methods */
	virtual bool breakpointAttributeChanged(unsigned int handle, wchar_t* name, Value* value);
	virtual bool deleteBreakpoint(unsigned int handle);
	virtual CrossfireBreakpoint* getBreakpoint(unsigned int handle);
	virtual void getBreakpoints(CrossfireBreakpoint*** ___values);
	virtual bool setBreakpoint(CrossfireBreakpoint* breakpoint, bool isRetry);

private:
	virtual int createBreakpoint(Value* arguments, CrossfireBreakpoint** _result, wchar_t** _message);

	std::map<unsigned int, CrossfireBreakpoint*>* m_breakpoints;

	static const wchar_t* KEY_BREAKPOINTS;
};
