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

class CrossfireResponse : public CrossfirePacket {

public:
	CrossfireResponse();
	~CrossfireResponse();
	virtual void clone(CrossfirePacket** _value);
	virtual Value* getBody();
	virtual wchar_t* getName();
	virtual unsigned int getRequestSeq();
	virtual bool getRunning();
	virtual bool getSuccess();
	virtual int getType();
	virtual bool setBody(Value* value);
	virtual void setName(const wchar_t* value);
	virtual void setRequestSeq(unsigned int value);
	virtual void setRunning(bool value);
	virtual void setSuccess(bool value);

private:
	Value* m_body;
	wchar_t* m_name;
	unsigned int m_requestSeq;
	bool m_running;
	bool m_success;
};
