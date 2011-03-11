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

#include "CrossfireLineBreakpoint.h"

class IBreakpointTarget {

public:
	IBreakpointTarget() {
	}

	virtual ~IBreakpointTarget() {
	}

	virtual bool clearBreakpoint(unsigned int handle) = 0;
	virtual bool getBreakpoint(unsigned int handle, CrossfireBreakpoint** _value) = 0;
	virtual bool getBreakpoints(CrossfireBreakpoint*** ___values) = 0;
	virtual bool setBreakpointCondition(unsigned int handle, std::wstring* condition) = 0;
	virtual bool setBreakpointEnabled(unsigned int handle, bool enabled) = 0;
	virtual bool setLineBreakpoint(CrossfireLineBreakpoint* breakpoint, bool isRetry) = 0;
};
