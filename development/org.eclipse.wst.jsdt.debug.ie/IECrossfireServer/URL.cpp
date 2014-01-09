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
#include "URL.h"

URL::URL() {
	m_value = NULL;
}

URL::URL(wchar_t* urlString) {
	m_value = NULL;
	setString(urlString);
}

URL::~URL() {
	if (m_value) {
		delete[] m_value;
	}
}

wchar_t* URL::getString() {
	return m_value;
}

bool URL::isEqual(URL* url) {
	if (!url) {
		return false;
	}
	if (!isValid()) {
		return !url->isValid();
	}
	if (!url->isValid()) {
		return false;
	}
	return wcscmp(m_value, url->getString()) == 0;
}

bool URL::isEqual(wchar_t* urlString) {
	if (!urlString) {
		return !isValid();
	}

	std::wstring string(urlString);
	if (!standardize(&string)) {
		return false;
	}

	return wcscmp(m_value, string.c_str()) == 0;
}

bool URL::isValid() {
	return m_value != NULL;
}

bool URL::setString(wchar_t* value) {
	if (m_value) {
		delete[] m_value;
		m_value = NULL;
	}

	if (value) {
		std::wstring string(value);
		if (!standardize(&string)) {
			return false;
		}
		m_value = _wcsdup(string.c_str());
	}
	return true;
}

bool URL::standardize(std::wstring* url) {
	size_t startIndex = url->find(L":/");
	if (startIndex == std::wstring::npos) {
		return false;
	}

	size_t endIndex = ++startIndex;
	wchar_t current = url->at(++endIndex);
	while (current == wchar_t('/')) {
		current = url->at(++endIndex);
	}

	size_t diff = endIndex - startIndex;
	if (diff == 1) {
		url->insert(startIndex, 1, wchar_t('/'));
	} else if (diff > 2) {
		url->erase(startIndex, diff - 2);
	}

	return true;
}
