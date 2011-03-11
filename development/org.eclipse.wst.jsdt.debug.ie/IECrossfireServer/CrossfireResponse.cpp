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


#include "stdafx.h"
#include "CrossfireResponse.h"

CrossfireResponse::CrossfireResponse() : CrossfirePacket() {
	m_body = NULL;
	m_name = NULL;
	m_requestSeq = -1;
	m_running = false;
	m_success = false;
}

CrossfireResponse::~CrossfireResponse() {
	if (m_body) {
		delete m_body;
	}
	if (m_name) {
		free(m_name);
	}
}

void CrossfireResponse::clone(CrossfirePacket** _value) {
	CrossfireResponse* result = new CrossfireResponse();
	result->setBody(m_body);
	result->setName(m_name);
	result->setRequestSeq(m_requestSeq);
	result->setRunning(m_running);
	result->setSuccess(m_success);
	*_value = result;
}

Value* CrossfireResponse::getBody() {
	return m_body;
}

wchar_t* CrossfireResponse::getName() {
	return m_name;
}

unsigned int CrossfireResponse::getRequestSeq() {
	return m_requestSeq;
}

bool CrossfireResponse::getRunning() {
	return m_running;
}

bool CrossfireResponse::getSuccess() {
	return m_success;
}

int CrossfireResponse::getType() {
	return TYPE_RESPONSE;
}

bool CrossfireResponse::setBody(Value* value) {
	if (value && value->getType() != TYPE_OBJECT) {
		return false;
	}
	if (value) {
		value->clone(&m_body);
	}
	return true;
}

void CrossfireResponse::setName(const wchar_t* value) {
	if (m_name) {
		free(m_name);
		m_name = NULL;
	}
	if (value) {
		m_name = _wcsdup(value);
	}
}

void CrossfireResponse::setRequestSeq(unsigned int value) {
	m_requestSeq = value;
}

void CrossfireResponse::setRunning(bool value) {
	m_running = value;
}

void CrossfireResponse::setSuccess(bool value) {
	m_success = value;
}


