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
#include <map>

class JSObject {

public:
	JSObject(std::wstring* name, unsigned int parentHandle);
	~JSObject();
	std::wstring getAccessor();

private:
	std::wstring* m_name;
	std::map<std::wstring*, unsigned int>* m_objects;
	unsigned int m_parentHandle;
};
