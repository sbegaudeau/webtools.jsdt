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


#include "stdafx.h"
#include "Value.h"

Value::Value() {
	m_type = TYPE_UNDEFINED;
	m_arrayValue = NULL;
	m_numberValue = 0;
	m_objectValue = NULL;
	m_stringValue = NULL;
}

Value::Value(bool value) {
	setValue(value);
}

Value::Value(double value) {
	setValue(value);
}

Value::Value(const wchar_t* value) {
	setValue(value);
}

Value::Value(std::wstring* value) {
	setValue(value);
}

Value::~Value() {
	clearCurrentValue();
}

void Value::clearCurrentValue() {
	switch (m_type) {
		case TYPE_STRING: {
			delete m_stringValue;
			break;
		}
		case TYPE_ARRAY: {
			std::vector<Value*>::iterator iterator = m_arrayValue->begin();
			std::vector<Value*>::iterator end = m_arrayValue->end();
			while (iterator != end) {
				delete *iterator;
				iterator++;
			}
			delete m_arrayValue;
			break;
		}
		case TYPE_OBJECT: {
			std::map<std::wstring, Value*>::iterator it = m_objectValue->begin();
			std::map<std::wstring, Value*>::iterator end = m_objectValue->end();
			while (it != end) {
				delete (*it).second;
				it++;
			}
			delete m_objectValue;
			break;
		}
	}
	m_type = TYPE_UNDEFINED;
}

void Value::clone(Value** _value) {
	switch (getType()) {
		case TYPE_NULL: {
			Value* result = new Value();
			result->setType(TYPE_NULL);
			*_value = result;
			break;
		}
		case TYPE_BOOLEAN: {
			Value* result = new Value();
			result->setValue(getBooleanValue());
			*_value = result;
			break;
		}
		case TYPE_NUMBER: {
			Value* result = new Value();
			result->setValue(getNumberValue());
			*_value = result;
			break;
		}
		case TYPE_STRING: {
			Value* result = new Value();
			result->setValue(getStringValue());
			*_value = result;
			break;
		}
		case TYPE_ARRAY: {
			Value** values = NULL;
			getArrayValues(&values);
			Value* result = new Value();
			result->setType(TYPE_ARRAY);
			int index = 0;
			while (values[index]) {
				result->addArrayValue(values[index++]);
			}
			delete[] values;

			*_value = result;
			break;
		}
		case TYPE_OBJECT: {
			std::wstring** keys = NULL;
			Value** values = NULL;
			getObjectValues(&keys, &values);
			Value* result = new Value();
			result->setType(TYPE_OBJECT);
			int index = 0;
			while (keys[index]) {
				std::wstring keyCopy(*keys[index]);
				result->addObjectValue(&keyCopy, values[index++]);
			}
			delete[] values;
			delete[] keys;

			*_value = result;
			break;
		}
		default: {
			/* TYPE_UNDEFINED */
			Value* result = new Value();
			result->setType(TYPE_UNDEFINED);
			*_value = result;
		}
	}
}

void Value::addArrayValue(Value *value) {
	setType(TYPE_ARRAY);
	Value* result = NULL;
	value->clone(&result);
	m_arrayValue->push_back(result);
}

bool Value::addObjectValue(const wchar_t* key, Value* value) {
	return addObjectValue(&std::wstring(key), value);
}

bool Value::addObjectValue(std::wstring* key, Value* value) {
	return setObjectValue(key, value, false);
}

//bool Value::clearObjectValue(const wchar_t* key) {
//	return clearObjectValue(&std::wstring(key));
//}

//bool Value::clearObjectValue(std::wstring* key) {
//	if (m_type != TYPE_OBJECT) {
//		return false;
//	}
//
//	std::map<std::wstring, Value*>::iterator iterator = m_objectValue->find(*key);
//	if (iterator == m_objectValue->end()) {
//		/* not found */
//		return false;
//	}
//	delete (*iterator).second;
//	m_objectValue->erase(iterator);
//	return true;
//}

bool Value::equals(Value* value) {
	if (!value || value->getType() != m_type) {
		return false;
	}

	switch (m_type) {
		case TYPE_UNDEFINED:
		case TYPE_NULL: {
			return true;
		}
		case TYPE_BOOLEAN: {
			return value->getBooleanValue() == getBooleanValue();
		}
		case TYPE_NUMBER: {
			return value->getNumberValue() == getNumberValue();
		}
		case TYPE_STRING: {
			return value->getStringValue()->compare(*getStringValue()) == 0;
		}
		case TYPE_ARRAY: {
			Value** selfItems = NULL;
			getArrayValues(&selfItems);
			Value** valueItems = NULL;
			value->getArrayValues(&valueItems);
			bool isEqual = true;
			int index = 0;
			while (selfItems[index]) {
				if (!selfItems[index]->equals(valueItems[index])) {
					isEqual = false;
					break;
				}
				index++;
			}
			if (isEqual) {
				isEqual = valueItems[index] == NULL;
			}
			delete[] valueItems;
			delete[] selfItems;
			return isEqual;
		}
		case TYPE_OBJECT: {
			std::wstring** selfKeys = NULL;
			Value** selfValues = NULL;
			getObjectValues(&selfKeys, &selfValues);
			bool isEqual = true;
			int index = 0;
			while (selfKeys[index]) {
				Value* valueValue = value->getObjectValue(selfKeys[index]);
				if (!valueValue || !selfValues[index]->equals(valueValue)) {
					isEqual = false;
					break;
				}
				index++;
			}
			delete[] selfKeys;
			delete[] selfValues;
			if (isEqual) {
				/* ensure that value does not contain extra keys/values */
				std::wstring** valueKeys = NULL;
				Value** valueValues = NULL;
				value->getObjectValues(&valueKeys, &valueValues);
				int length = 0;
				while (valueKeys[length++]);
				isEqual = length == index;
				delete[] valueKeys;
				delete[] valueValues;
			}
			return isEqual;
		}
	}

	return false;	/* should never happen */
}

void Value::getArrayValues(Value*** __values) {
	if (m_type != TYPE_ARRAY) {
		*__values = NULL;
		return;
	}
	size_t size = m_arrayValue->size();
	Value** result = new Value*[size + 1];
	for (int i = 0; i < (int)size; i++) {
		result[i] = m_arrayValue->at(i);
	}
	result[size] = NULL;
	*__values = result;
}

bool Value::getBooleanValue() {
	if (m_type != TYPE_BOOLEAN) {
		return false;
	}
	return m_numberValue != 0;
}

double Value::getNumberValue() {
	if (m_type != TYPE_NUMBER) {
		return 0;
	}
	return m_numberValue;
}

Value* Value::getObjectValue(const wchar_t* key) {
	return getObjectValue(&std::wstring(key));
}

Value* Value::getObjectValue(std::wstring* key) {
	if (m_type != TYPE_OBJECT) {
		return NULL;
	}

	std::map<std::wstring, Value*>::iterator result = m_objectValue->find(*key);
	if (result == m_objectValue->end()) {
		/* not found */
		return NULL;
	}
	return result->second;
}

void Value::getObjectValues(std::wstring*** __keys, Value*** __values) {
	if (m_type != TYPE_OBJECT) {
		*__keys = NULL;
		*__values = NULL;
		return;
	}
	size_t size = m_objectValue->size();
	std::wstring** keysResult = new std::wstring*[size + 1];
	Value** valuesResult = new Value*[size + 1];

	std::map<std::wstring, Value*>::iterator it = m_objectValue->begin();
	std::map<std::wstring, Value*>::iterator end = m_objectValue->end();
	int index = 0;
	while (it != end) {
		keysResult[index] = (std::wstring*)&(*it).first;
		valuesResult[index++] = (*it).second;
		it++;
	}
	keysResult[index] = NULL;
	valuesResult[index] = NULL;

	*__keys = keysResult;
	*__values = valuesResult;
}

bool Value::setObjectValue(const wchar_t* key, Value* value) {
	return setObjectValue(&std::wstring(key), value);
}

bool Value::setObjectValue(std::wstring* key, Value* value) {
	return setObjectValue(key, value, true);
}

bool Value::setObjectValue(std::wstring* key, Value* value, bool overwrite) {
	setType(TYPE_OBJECT);
	std::map<std::wstring, Value*>::iterator it = m_objectValue->find(*key);
	if (it != m_objectValue->end()) {
		/* value with this key already exists in map */
		if (!overwrite) {
			return false;
		}
		delete (*it).second;
		m_objectValue->erase(it);
	}
	Value* result = NULL;
	value->clone(&result);
	m_objectValue->insert(std::pair<std::wstring,Value*>(*key, result));
	return true;
}

std::wstring* Value::getStringValue() {
	if (m_type != TYPE_STRING) {
		return NULL;
	}
	return m_stringValue;
}

int Value::getType() {
	return m_type;
}

void Value::setValue(bool value) {
	setType(TYPE_BOOLEAN);
	if (value) {
		m_numberValue = 1;
	} else {
		m_numberValue = 0;
	}
}

void Value::setValue(double value) {
	setType(TYPE_NUMBER);
	m_numberValue = value;
}

void Value::setValue(const wchar_t* value) {
	setType(TYPE_STRING);
	m_stringValue = new std::wstring;
	m_stringValue->assign(value);
}

void Value::setValue(std::wstring* value) {
	setType(TYPE_STRING);
	m_stringValue = new std::wstring;
	m_stringValue->assign(*value);
}

void Value::setType(int value) {
	if (m_type == value) {
		/* nothing to do */
		return;
	}

	clearCurrentValue();

	if (value > TYPE_OBJECT) {
		/* invalid value */
		m_type = TYPE_UNDEFINED;
		return;
	}

	m_type = value;
	switch (m_type) {
		case TYPE_ARRAY: {
			m_arrayValue = new std::vector<Value*>;
			break;
		}
		case TYPE_OBJECT: {
			m_objectValue = new std::map<std::wstring, Value*>;
			break;
		}
	}
}
