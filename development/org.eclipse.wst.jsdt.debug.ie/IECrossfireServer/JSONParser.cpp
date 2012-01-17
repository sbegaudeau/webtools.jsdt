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


#include "StdAfx.h"
#include "JSONParser.h"

/* initialize constants */
const wchar_t* JSONParser::VALUE_FALSE = L"false";
const wchar_t* JSONParser::VALUE_NULL = L"null";
const wchar_t* JSONParser::VALUE_TRUE = L"true";
const wchar_t* JSONParser::VALUE_UNDEFINED = L"\"undefined\"";

JSONParser::JSONParser(void) {
}

JSONParser::~JSONParser(void) {
}

void JSONParser::parse(std::wstring* jsonString, Value** _value) {
	*_value = NULL;

	std::wstringstream jsonStream(*jsonString);
	parse(&jsonStream, _value);
}

void JSONParser::parse(std::wstringstream* jsonStream, Value** _value) {
	*_value = NULL;

	skipWhitespace(jsonStream);
	wchar_t firstChar = jsonStream->peek();
	if (firstChar == wchar_t('n')) {
		size_t length = wcslen(VALUE_NULL);
		wchar_t* streamChars = new wchar_t[length];
		jsonStream->read(streamChars, (std::streamsize)length);
		if (wcsncmp(VALUE_NULL, streamChars, length) == 0) {
			*_value = new Value();
			(*_value)->setType(TYPE_NULL);
		} else {
			Logger::error("JSON string has invalid value that starts with 'n' but is not \"null\"");
		}
		delete[] streamChars;
		return;
	}
	if (firstChar == wchar_t('t')) {
		size_t length = wcslen(VALUE_TRUE);
		wchar_t* streamChars = new wchar_t[length];
		jsonStream->read(streamChars, (std::streamsize)length);
		if (wcsncmp(VALUE_TRUE, streamChars, length) == 0) {
			Value* result = new Value(true);
			*_value = result;
		} else {
			Logger::error("JSON string has invalid value that starts with 't' but is not \"true\"");
		}
		delete[] streamChars;
		return;
	}
	if (firstChar == wchar_t('f')) {
		size_t length = wcslen(VALUE_FALSE);
		wchar_t* streamChars = new wchar_t[length];
		jsonStream->read(streamChars, (std::streamsize)length);
		if (wcsncmp(VALUE_FALSE, streamChars, length) == 0) {
			Value* result = new Value(false);
			*_value = result;
		} else {
			Logger::error("JSON string has invalid value that starts with 'f' but is not \"false\"");
		}
		delete[] streamChars;
		return;
	}
	if (firstChar == wchar_t('\"')) {
		std::wstring* stringValue = NULL;
		parseString(jsonStream, &stringValue);
		if (stringValue) {
			Value* result = new Value(stringValue);
			*_value = result;
			delete stringValue;
		} else {
			/* parseString() already logs a detailed error message, so no error logged here */
		}
		return;
	}

	int index = 0;
	switch (firstChar) {
		case wchar_t('0'):
		case wchar_t('1'):
		case wchar_t('2'):
		case wchar_t('3'):
		case wchar_t('4'):
		case wchar_t('5'):
		case wchar_t('6'):
		case wchar_t('7'):
		case wchar_t('8'):
		case wchar_t('9'):
		case wchar_t('-'): {
			double doubleValue = parseNumber(jsonStream);
			if (doubleValue == -DBL_MAX) {
				Logger::error("JSON string has a Number value with invalid character(s)");
				return;
			}
			Value* result = new Value(doubleValue);
			*_value = result;
			return;
		}
	}
	
	if (firstChar == wchar_t('[')) {
		parseArray(jsonStream, _value);
		return;
	}

	if (firstChar == wchar_t('{')) {
		parseObject(jsonStream, _value);
		return;
	}
}

void JSONParser::parseArray(std::wstringstream* jsonStream, Value** _value) {
	*_value = NULL;

	Value* result = new Value();
	result->setType(TYPE_ARRAY);

	jsonStream->ignore(1);
	skipWhitespace(jsonStream);
	if (jsonStream->peek() == wchar_t(']')) {
		jsonStream->ignore(1);
		*_value = result;
		return;
	}

	while (true) {
		Value* currentValue = NULL;
		parse(jsonStream, &currentValue);
		if (!currentValue) {
			delete result;
			return;
		}
		result->addArrayValue(currentValue);
		delete currentValue;

		skipWhitespace(jsonStream);
		wchar_t nextChar = jsonStream->peek();
		if (nextChar == wchar_t(',')) {
			jsonStream->ignore(1);
			skipWhitespace(jsonStream);
			continue;
		}
		if (nextChar == wchar_t(']')) {
			jsonStream->ignore(1);
			*_value = result;
		} else {
			Logger::error("JSON string has an array without expected closing ']'");
			delete result;
		}
		return;
	}
}

double JSONParser::parseNumber(std::wstringstream* jsonStream) {
	int count = 0;
	bool looking = true;
	bool nonZeroEncountered = false;
	wchar_t currentChar = jsonStream->peek();
	while (looking) {
		if (jsonStream->eof()) {
			looking = false;
			break;
		}
		switch (currentChar) {
			case wchar_t('1'):
			case wchar_t('2'):
			case wchar_t('3'):
			case wchar_t('4'):
			case wchar_t('5'):
			case wchar_t('6'):
			case wchar_t('7'):
			case wchar_t('8'):
			case wchar_t('9'):
			case wchar_t('-'):
			case wchar_t('+'):
			case wchar_t('e'):
			case wchar_t('E'): {
				nonZeroEncountered = true;
			}
			// FALL THROUGH
			case wchar_t('0'):
			case wchar_t('.'): {
				jsonStream->ignore(1);
				count++;
				break;
			}
			default: {
				looking = false;
				break;
			}
		}
		currentChar = jsonStream->peek();
	}
	for (int i = 0; i < count; i++) {
		jsonStream->unget();
	}

	wchar_t* numberChars = new wchar_t[count + 1];
	jsonStream->read(numberChars, count);
	numberChars[count] = wchar_t('\0');
	wchar_t* endPoint;
	double numberValue = wcstod(numberChars, &endPoint);
	delete[] numberChars;
	if (numberValue == 0 && nonZeroEncountered) {
		return -DBL_MAX;
	}
	return numberValue;
}

void JSONParser::parseObject(std::wstringstream* jsonStream, Value** _value) {
	*_value = NULL;

	Value* result = new Value();
	result->setType(TYPE_OBJECT);

	jsonStream->ignore(1);
	skipWhitespace(jsonStream);
	if (jsonStream->peek() == wchar_t('}')) {
		jsonStream->ignore(1);
		*_value = result;
		return;
	}

	while (true) {
		if (jsonStream->peek() != wchar_t('\"')) {
			Logger::error("JSON string has an object with a non-String key value");
			delete result;
			return;
		}
		std::wstring* key = NULL;
		parseString(jsonStream, &key);
		skipWhitespace(jsonStream);
		if (jsonStream->get() != wchar_t(':')) {
			Logger::error("JSON string has an object without a ':' separating a key from its _value");
			delete key;
			delete result;
			return;
		}
		Value* assocValue = NULL;
		skipWhitespace(jsonStream);
		parse(jsonStream, &assocValue);
		if (!assocValue) {
			delete key;
			delete result;
			return;
		}
		bool success = result->addObjectValue(key, assocValue);
		delete assocValue;
		delete key;
		if (!success) {
			Logger::error("JSON string has an object with a duplicate key");
			delete result;
			return;
		}

		skipWhitespace(jsonStream);
		wchar_t nextChar = jsonStream->peek();
		if (nextChar == wchar_t(',')) {
			jsonStream->ignore(1);
			skipWhitespace(jsonStream);
			continue;
		}
		if (nextChar == wchar_t('}')) {
			jsonStream->ignore(1);
			*_value = result;
		} else {
			Logger::error("JSON string has an object without an expected closing '}'");
			delete result;
		}
		return;
	}
}

void JSONParser::parseString(std::wstringstream* jsonStream, std::wstring** _value) {
	*_value = NULL;

	std::wstring* result = new std::wstring;
	jsonStream->ignore(1);
	const wchar_t charBackslash = wchar_t('\\');
	wchar_t charQuote = wchar_t('\"');
	wchar_t currentChar = NULL;
	jsonStream->read(&currentChar, 1);
	while (currentChar != charQuote) {
		if (jsonStream->eof()) {
			Logger::error("JSON string has string value that does not end");
			return;
		}
		if (currentChar == charBackslash) {
			jsonStream->read(&currentChar, 1);
			if (jsonStream->eof()) {
				Logger::error("JSON string has string value that does not end");
				return;
			}
			switch (currentChar) {
				case wchar_t('\"'):
				case wchar_t('/'):
				case charBackslash: {
					result->push_back(currentChar);
					break;
				}
				case wchar_t('b'): {
					result->push_back(wchar_t('\b'));
					break;
				}
				case wchar_t('f'): {
					result->push_back(wchar_t('\f'));
					break;
				}
				case wchar_t('n'): {
					result->push_back(wchar_t('\n'));
					break;
				}
				case wchar_t('r'): {
					result->push_back(wchar_t('\r'));
					break;
				}
				case wchar_t('t'): {
					result->push_back(wchar_t('\t'));
					break;
				}
				case wchar_t('u'): {
					Logger::log("FYI: unicode value encountered, not parsed, still //TODO");
					// TODO
					break;
				}
				default: {
					Logger::error("JSON string has string value with an invalid escape sequence");
					return;
				}
			}
		} else {
			result->push_back(currentChar);
		}
		jsonStream->read(&currentChar, 1);
	}

	*_value = result;
}

void JSONParser::skipWhitespace(std::wstringstream* jsonStream) {
	wchar_t current = jsonStream->peek();
	while (current == wchar_t(' ') || current == wchar_t('\r') || current == wchar_t('\n') || current == wchar_t('\t')) {
		jsonStream->ignore(1);
		current = jsonStream->peek();
	}
}

void JSONParser::stringify(Value* value, std::wstring** _jsonString) {
	switch (value->getType()) {
		case TYPE_NULL: {
			std::wstring* result = new std::wstring;
			result->assign(VALUE_NULL);
			*_jsonString = result;
			break;
		}
		case TYPE_BOOLEAN: {
			std::wstring* result = new std::wstring;
			result->assign(value->getBooleanValue() ? VALUE_TRUE : VALUE_FALSE);
			*_jsonString = result;
			break;
		}
		case TYPE_NUMBER: {
			std::wstring* result = new std::wstring;
			std::wstringstream stringStream;
			stringStream << value->getNumberValue();
			result->assign(stringStream.str());
			*_jsonString = result;
			break;
		}
		case TYPE_STRING: {
			static const wchar_t char_quote('\"');
			static const wchar_t char_backslash('\\');
			static const wchar_t char_forwardslash('/');
			static const wchar_t char_backspace('\b');
			static const wchar_t char_formfeed('\f');
			static const wchar_t char_newline('\n');
			static const wchar_t char_cr('\r');
			static const wchar_t char_tab('\t');

			std::wstring* source = value->getStringValue();
			size_t length = source->length();
			wchar_t* chars = (wchar_t*)source->c_str();

			std::wstringstream stringStream;
			stringStream << char_quote;
			for (size_t i = 0; i < length; i++) {
				switch (chars[i]) {
					case char_quote:
					case char_backslash:
					case char_forwardslash: {
						stringStream << char_backslash;
						stringStream << chars[i];
						break;
					}
					case char_backspace: {
						stringStream << "\\b";
						break;
					}
					case char_formfeed: {
						stringStream << "\\f";
						break;
					}
					case char_newline: {
						stringStream << "\\n";
						break;
					}
					case char_cr: {
						stringStream << "\\r";
						break;
					}
  					case char_tab: {
						stringStream << "\\t";
						break;
					}
					default: {
						stringStream << chars[i];
						break;
					}
				}
			}
			stringStream << char_quote;

			std::wstring* result = new std::wstring;
			result->assign(stringStream.str());
			*_jsonString = result;
			break;
		}
		case TYPE_ARRAY: {
			Value** arrayValues = NULL;
			value->getArrayValues(&arrayValues);
			std::wstring* result = new std::wstring;
			result->push_back(wchar_t('['));
			int index = 0;
			Value* currentValue = arrayValues[index];
			while (currentValue) {
				std::wstring* serializedValue = NULL;
				stringify(currentValue, &serializedValue);
				result->append(*serializedValue);
				result->push_back(wchar_t(','));
				delete serializedValue;
				currentValue = arrayValues[++index];
			}
			delete[] arrayValues;

			if (index > 0) {
				result->erase(result->end() - 1);
			}
			result->push_back(wchar_t(']'));
			*_jsonString = result;
			break;
		}
		case TYPE_OBJECT: {
			std::wstring** objectKeys = NULL;
			Value** objectValues = NULL;
			value->getObjectValues(&objectKeys, &objectValues);
			std::wstring* result = new std::wstring;
			result->push_back(wchar_t('{'));
			int index = 0;
			std::wstring* currentKey = objectKeys[index];
			while (currentKey) {
				result->push_back(wchar_t('\"'));
				result->append(*currentKey);
				result->push_back(wchar_t('\"'));
				result->push_back(wchar_t(':'));
				std::wstring* serializedValue = NULL;
				stringify(objectValues[index], &serializedValue);
				result->append(*serializedValue);
				result->push_back(wchar_t(','));
				delete serializedValue;
				currentKey = objectKeys[++index];
			}
			delete[] objectValues;
			delete[] objectKeys;

			if (index > 0) {
				result->erase(result->end() - 1);
			}
			result->push_back(wchar_t('}'));
			*_jsonString = result;
			break;
		}
		default: {
			/* TYPE_UNDEFINED */
			std::wstring* result = new std::wstring;
			result->assign(VALUE_UNDEFINED);
			*_jsonString = result;
			break;
		}
	}
}
