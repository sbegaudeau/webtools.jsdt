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


#include "stdafx.h"
#include "CrossfireResponse.h"

CrossfireResponse::CrossfireResponse() : CrossfirePacket() {
	m_body = NULL;
	m_code = CODE_OK;
	m_message = NULL;
	m_requestSeq = -1;
	m_running = false;
}

CrossfireResponse::~CrossfireResponse() {
	if (m_body) {
		delete m_body;
	}
	if (m_message) {
		free(m_message);
	}
}

void CrossfireResponse::clone(CrossfirePacket** _value) {
	CrossfireResponse* result = new CrossfireResponse();
	result->setBody(m_body);
	result->setCode(m_code);
	result->setMessage(m_message);
	result->setName(getName());
	result->setRequestSeq(m_requestSeq);
	result->setRunning(m_running);
	*_value = result;
}

Value* CrossfireResponse::getBody() {
	return m_body;
}

int CrossfireResponse::getCode() {
	return m_code;
}

wchar_t* CrossfireResponse::getMessage() {
	return m_message;
}

unsigned int CrossfireResponse::getRequestSeq() {
	return m_requestSeq;
}

bool CrossfireResponse::getRunning() {
	return m_running;
}

int CrossfireResponse::getType() {
	return TYPE_RESPONSE;
}

bool CrossfireResponse::setBody(Value* value) {
	if (value && value->getType() != TYPE_OBJECT) {
		return false;
	}
	if (m_body) {
		delete m_body;
		m_body = NULL;
	}
	if (value) {
		value->clone(&m_body);
	}
	return true;
}

void CrossfireResponse::setCode(int value) {
	m_code = value;
}

void CrossfireResponse::setMessage(wchar_t* value) {
	if (m_message) {
		free(m_message);
		m_message = NULL;
	}
	if (value) {
		m_message = _wcsdup(value);
	}
}

void CrossfireResponse::setRequestSeq(unsigned int value) {
	m_requestSeq = value;
}

void CrossfireResponse::setRunning(bool value) {
	m_running = value;
}



