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

#include <string>

class URL {

public:
	URL(wchar_t* value);
	~URL();
	wchar_t* getString();
	bool isEqual(wchar_t* urlString);
	bool isEqual(URL* url);
	bool isValid();

private:
	bool standardize(std::wstring* url);

	wchar_t* m_value;
};

