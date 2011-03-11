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

#include "CrossfireBreakpoint.h"
#include "Value.h"

class CrossfireLineBreakpoint : public CrossfireBreakpoint {

public:
	CrossfireLineBreakpoint();
	virtual ~CrossfireLineBreakpoint();
	virtual bool appliesToUrl(std::wstring* url);
	virtual void clone(CrossfireBreakpoint** _value);
	virtual unsigned int getLine();
	virtual int getType();
	virtual const wchar_t* getTypeString();
	virtual const std::wstring* getUrl();
	virtual void setLine(unsigned int value);
	virtual bool setLocationFromValue(Value* value);
	virtual bool setUrl(std::wstring* value);

	static bool CanHandleBPType(wchar_t* type);

protected:
	CrossfireLineBreakpoint(unsigned int handle);
	virtual bool getLocationAsValue(Value** _value);

private:
	unsigned int m_line;
	std::wstring* m_url;

	static const wchar_t* BPTYPESTRING_LINE;
	static const wchar_t* KEY_LINE;
	static const wchar_t* KEY_URL;
};
