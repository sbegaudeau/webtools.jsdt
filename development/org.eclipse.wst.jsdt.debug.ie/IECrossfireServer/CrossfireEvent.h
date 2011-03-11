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

class CrossfireEvent : public CrossfirePacket {

public:
	CrossfireEvent();
	~CrossfireEvent();
	virtual void clone(CrossfirePacket** _value);
	virtual std::wstring* getContextId();
	virtual Value* getData();
	virtual wchar_t* getName();
	virtual int getType();
	virtual void setContextId(std::wstring* value);
	virtual bool setData(Value* value);
	virtual void setName(const wchar_t* value);

private:
	std::wstring* m_contextId;
	Value* m_data;
	wchar_t* m_name;
};
