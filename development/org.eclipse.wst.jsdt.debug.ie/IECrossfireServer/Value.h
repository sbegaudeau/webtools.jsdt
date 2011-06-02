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
	virtual void addArrayValue(Value* value);
	virtual bool addObjectValue(const wchar_t* key, Value* value);
	virtual bool addObjectValue(std::wstring* key, Value* value);
//	virtual bool clearObjectValue(const wchar_t* key);
//	virtual bool clearObjectValue(std::wstring* key);
	virtual void clone(Value** _value);
	virtual void getArrayValues(Value*** __values);
	virtual bool getBooleanValue();
	virtual double getNumberValue();
	virtual Value* getObjectValue(const wchar_t* key);
	virtual Value* getObjectValue(std::wstring* key);
	virtual void getObjectValues(std::wstring*** __keys, Value*** __values);
	virtual std::wstring* getStringValue();
	virtual int getType();
	virtual bool setObjectValue(const wchar_t* key, Value* value);
	virtual bool setObjectValue(std::wstring* key, Value* value);
	virtual void setType(int type);
	virtual void setValue(bool value);
	virtual void setValue(double value);
	virtual void setValue(const wchar_t* value);
	virtual void setValue(std::wstring* value);

private:
	virtual void clearCurrentValue();
	virtual bool setObjectValue(std::wstring* key, Value* value, bool overwrite);

	std::vector<Value*>* m_arrayValue;
	double m_numberValue;
	std::map<std::wstring, Value*>* m_objectValue;
	std::wstring* m_stringValue;
	int m_type;
};
