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

#include "CrossfirePacket.h"
#include "Value.h"

enum {
	CODE_OK = 0,
	CODE_MALFORMED_PACKET,
	CODE_MALFORMED_REQUEST,
	CODE_COMMAND_NOT_IMPLEMENTED,
	CODE_INVALID_ARGUMENT,
	CODE_UNEXPECTED_EXCEPTION,
	CODE_COMMAND_FAILED,
	CODE_INVALID_STATE,
};

class CrossfireResponse : public CrossfirePacket {

public:
	CrossfireResponse();
	virtual ~CrossfireResponse();
	void clone(CrossfirePacket** _value);
	Value* getBody();
	int getCode();
	wchar_t* getMessage();
	unsigned int getRequestSeq();
	bool getRunning();
	int getType();
	bool setBody(Value* value);
	void setCode(int code);
	void setMessage(wchar_t* value);
	void setRequestSeq(unsigned int value);
	void setRunning(bool value);

private:
	Value* m_body;
	int m_code;
	wchar_t* m_message;
	unsigned int m_requestSeq;
	bool m_running;
};
