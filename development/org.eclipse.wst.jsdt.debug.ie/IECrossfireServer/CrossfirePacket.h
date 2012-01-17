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

class CrossfirePacket {

public:
	CrossfirePacket();
	virtual ~CrossfirePacket();
	virtual void clone(CrossfirePacket** _value) = 0;
	std::wstring* getContextId();
	wchar_t* getName();
	unsigned int getSeq();
	virtual int getType() = 0;
	void setContextId(std::wstring* value);
	void setName(const wchar_t* value);
	void setSeq(unsigned int);

	enum {
		TYPE_REQUEST,
		TYPE_RESPONSE,
		TYPE_EVENT
	};

private:
	std::wstring* m_contextId;
	wchar_t* m_name;
	unsigned int m_seq;
};
