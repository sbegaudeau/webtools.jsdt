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

#include <float.h>

#include "Value.h"
#include "Logger.h"

class JSONParser {

public:
	JSONParser();
	~JSONParser();
	void parse(std::wstring* jsonString, Value** _value);
	void stringify(Value* value, std::wstring** _jsonString);

private:
	void parse(std::wstringstream* jsonStream, Value** _value);
	void parseArray(std::wstringstream* jsonStream, Value** _value);
	double parseNumber(std::wstringstream* jsonStream);
	void parseObject(std::wstringstream* jsonStream, Value** _value);
	void parseString(std::wstringstream* jsonStream, std::wstring** _value);
	void skipWhitespace(std::wstringstream* jsonStream);

	/* constants */
	static const wchar_t* VALUE_FALSE;
	static const wchar_t* VALUE_NULL;
	static const wchar_t* VALUE_TRUE;
	static const wchar_t* VALUE_UNDEFINED;
};
