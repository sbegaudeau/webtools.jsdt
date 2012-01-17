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


#include "StdAfx.h"
#include "CrossfireEvent.h"

CrossfireEvent::CrossfireEvent() : CrossfirePacket() {
	m_body = NULL;
}

CrossfireEvent::~CrossfireEvent() {
	if (m_body) {
		delete m_body;
	}
}

void CrossfireEvent::clone(CrossfirePacket** _value) {
	CrossfireEvent* result = new CrossfireEvent();
	result->setContextId(getContextId());
	result->setBody(m_body);
	result->setName(getName());
	*_value = result;
}

Value* CrossfireEvent::getBody() {
	return m_body;
}

int CrossfireEvent::getType() {
	return TYPE_EVENT;
}

bool CrossfireEvent::setBody(Value* value) {
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
