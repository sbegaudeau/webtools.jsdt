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

#include <iostream>
#include <map>
#include <sstream>
#include <vector>

enum {
	TYPE_UNDEFINED = 0x0,
	TYPE_NULL = 0x1,
	TYPE_BOOLEAN = 0x2,
	TYPE_NUMBER = 0x4,
	TYPE_STRING = 0x8,
	TYPE_ARRAY = 0x10,
	TYPE_OBJECT = 0x20,
};

class Value {

public:
	Value();
	Value(bool value);
	Value(double value);
	Value(const wchar_t* value);
	Value(std::wstring* value);
	~Value();
	void addArrayValue(Value* value);
	bool addObjectValue(const wchar_t* key, Value* value);
	bool addObjectValue(std::wstring* key, Value* value);
//	bool clearObjectValue(const wchar_t* key);
//	bool clearObjectValue(std::wstring* key);
	void clone(Value** _value);
	bool equals(Value* value);
	void getArrayValues(Value*** __values);
	bool getBooleanValue();
	double getNumberValue();
	Value* getObjectValue(const wchar_t* key);
	Value* getObjectValue(std::wstring* key);
	void getObjectValues(std::wstring*** __keys, Value*** __values);
	std::wstring* getStringValue();
	int getType();
	bool setObjectValue(const wchar_t* key, Value* value);
	bool setObjectValue(std::wstring* key, Value* value);
	void setType(int type);
	void setValue(bool value);
	void setValue(double value);
	void setValue(const wchar_t* value);
	void setValue(std::wstring* value);

private:
	void clearCurrentValue();
	bool setObjectValue(std::wstring* key, Value* value, bool overwrite);

	std::vector<Value*>* m_arrayValue;
	double m_numberValue;
	std::map<std::wstring, Value*>* m_objectValue;
	std::wstring* m_stringValue;
	int m_type;
};
