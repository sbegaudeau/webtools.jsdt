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

#include <string>

class URL {

public:
	URL();
	URL(wchar_t* value);
	~URL();
	wchar_t* getString();
	bool isEqual(wchar_t* urlString);
	bool isEqual(URL* url);
	bool isValid();
	bool setString(wchar_t* value);

private:
	bool standardize(std::wstring* url);

	wchar_t* m_value;
};

