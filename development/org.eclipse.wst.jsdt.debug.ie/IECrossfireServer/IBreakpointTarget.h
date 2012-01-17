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

#include "Value.h"
class IBreakpointTarget; // forward declaration
#include "CrossfireBreakpoint.h"

class IBreakpointTarget {

public:
	IBreakpointTarget() {
	}

	virtual ~IBreakpointTarget() {
	}

	virtual bool breakpointAttributeChanged(unsigned int handle, wchar_t* name, Value* value) = 0;
	virtual bool deleteBreakpoint(unsigned int handle) = 0;
	virtual CrossfireBreakpoint* getBreakpoint(unsigned int handle) = 0;
	virtual void getBreakpoints(CrossfireBreakpoint*** ___values) = 0;
	virtual bool setBreakpoint(CrossfireBreakpoint* breakpoint) = 0;
};
