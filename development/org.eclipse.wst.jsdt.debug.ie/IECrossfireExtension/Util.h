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

#include "activdbg.h"
#include "Logger.h"

class Util {
public:
	/* statics */
	static bool VerifyActiveScriptDebugger();
	static bool VerifyDebugPreference();

private:
	/* constants */
	static const wchar_t* PREFERENCE_DISABLEIEDEBUG;
};

