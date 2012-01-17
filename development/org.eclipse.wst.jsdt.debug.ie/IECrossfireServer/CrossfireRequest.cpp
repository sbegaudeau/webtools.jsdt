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
#include "CrossfireRequest.h"

CrossfireRequest::CrossfireRequest() : CrossfirePacket() {
	m_arguments = NULL;
}

CrossfireRequest::~CrossfireRequest() {
	if (m_arguments) {
		delete m_arguments;
	}
}

void CrossfireRequest::clone(CrossfirePacket** _value) {
	CrossfireRequest* result = new CrossfireRequest();
	result->setArguments(m_arguments);
	result->setContextId(getContextId());
	result->setName(getName());
	*_value = result;
}

Value* CrossfireRequest::getArguments() {
	return m_arguments;
}

int CrossfireRequest::getType() {
	return TYPE_REQUEST;
}

bool CrossfireRequest::setArguments(Value* value) {
	if (value && value->getType() != TYPE_OBJECT) {
		return false;
	}
	if (m_arguments) {
		delete m_arguments;
		m_arguments = NULL;
	}
	if (value) {
		value->clone(&m_arguments);
	}
	return true;
}
