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

#ifdef __cplusplus
extern "C" {
#endif

#include <ActivDbg.h>
#include <Unknwn.h>

HRESULT findDebugApplication(IUnknown* programProvider, DWORD processId, IRemoteDebugApplication** _application);

#ifdef __cplusplus
}
#endif
