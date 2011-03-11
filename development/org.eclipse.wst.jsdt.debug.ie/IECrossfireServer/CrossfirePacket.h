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

class CrossfirePacket {

public:
	CrossfirePacket();
	~CrossfirePacket();
	virtual void clone(CrossfirePacket** _value) = 0;
	virtual unsigned int getSeq();
	virtual int getType() = 0;
	virtual void setSeq(unsigned int);

	enum {
		TYPE_REQUEST,
		TYPE_RESPONSE,
		TYPE_EVENT
	};

private:
	unsigned int m_seq;
};
