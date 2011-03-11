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

#include "CrossfirePacket.h"
#include "Value.h"

class CrossfireRequest : public CrossfirePacket {

public:
	CrossfireRequest();
	~CrossfireRequest();
	virtual void clone(CrossfirePacket** _value);
	virtual Value* getArguments();
	virtual std::wstring* getContextId();
	virtual wchar_t* getName();
	virtual int getType();
	virtual bool setArguments(Value* value);
	virtual void setContextId(std::wstring* value);
	virtual void setName(const wchar_t* value);

private:
	Value* m_arguments;
	std::wstring* m_contextId;
	wchar_t* m_name;
};
