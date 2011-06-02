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


#include "StdAfx.h"
#include "CrossfireEvent.h"

CrossfireEvent::CrossfireEvent() : CrossfirePacket() {
	m_data = NULL;
}

CrossfireEvent::~CrossfireEvent() {
	if (m_data) {
		delete m_data;
	}
}

void CrossfireEvent::clone(CrossfirePacket** _value) {
	CrossfireEvent* result = new CrossfireEvent();
	result->setContextId(getContextId());
	result->setData(m_data);
	result->setName(getName());
	*_value = result;
}

Value* CrossfireEvent::getData() {
	return m_data;
}

int CrossfireEvent::getType() {
	return TYPE_EVENT;
}

bool CrossfireEvent::setData(Value* value) {
	if (value && value->getType() != TYPE_OBJECT) {
		return false;
	}
	if (m_data) {
		delete m_data;
		m_data = NULL;
	}
	if (value) {
		value->clone(&m_data);
	}
	return true;
}
