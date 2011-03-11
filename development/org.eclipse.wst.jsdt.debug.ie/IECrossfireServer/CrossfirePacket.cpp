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
#include "CrossfirePacket.h"

CrossfirePacket::CrossfirePacket() {
	m_seq = -1;
}

CrossfirePacket::~CrossfirePacket() {
}

unsigned int CrossfirePacket::getSeq() {
	return m_seq;
}

void CrossfirePacket::setSeq(unsigned int value) {
	m_seq = value;
}
