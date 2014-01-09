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

#include "Logger.h"
#include "URL.h"
class CrossfireLineBreakpoint; // forward declaration
#include "CrossfireBreakpoint.h"

class CrossfireLineBreakpoint : public CrossfireBreakpoint {

public:
	CrossfireLineBreakpoint();
	virtual ~CrossfireLineBreakpoint();
	bool appliesToUrl(URL* url);
	void breakpointHit();
	void clone(CrossfireBreakpoint** _value);
	const std::wstring* getCondition();
	unsigned int getHitCount();
	unsigned int getLine();
	int getType();
	const wchar_t* getTypeString();
	const URL* getUrl();
	bool isEnabled();
	bool matchesHitCount();
	bool matchesLocation(CrossfireBreakpoint* breakpoint);
	void setCondition(std::wstring* value);
	void setEnabled(bool value);
	void setHitCount(unsigned int value);
	void setLine(unsigned int value);
	bool setLocationFromValue(Value* value);
	bool setUrl(URL* value);

	/* static methods */
	static bool CanHandleBPType(wchar_t* type);

	static const wchar_t* ATTRIBUTE_CONDITION;
	static const wchar_t* ATTRIBUTE_ENABLED;
	static const wchar_t* ATTRIBUTE_HITCOUNT;

protected:
	CrossfireLineBreakpoint(unsigned int handle);
	bool attributeIsValid(wchar_t* name, Value* value);
	bool getLocationAsValue(Value** _value);

private:
	unsigned int m_hitCounter;
	unsigned int m_line;
	URL* m_url;

	static const wchar_t* BPTYPESTRING_LINE;
	static const wchar_t* KEY_CONDITION;
	static const wchar_t* KEY_ENABLED;
	static const wchar_t* KEY_HITCOUNT;
	static const wchar_t* KEY_LINE;
	static const wchar_t* KEY_URL;
};
