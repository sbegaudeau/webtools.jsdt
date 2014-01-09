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

class CrossfireRequest : public CrossfirePacket {

public:
	CrossfireRequest();
	virtual ~CrossfireRequest();
	void clone(CrossfirePacket** _value);
	Value* getArguments();
	int getType();
	bool setArguments(Value* value);

private:
	Value* m_arguments;
};
