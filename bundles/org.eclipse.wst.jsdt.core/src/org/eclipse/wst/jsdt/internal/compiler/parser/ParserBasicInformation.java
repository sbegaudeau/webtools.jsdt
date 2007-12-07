/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.parser;

/*An interface that contains static declarations for some basic information
 about the parser such as the number of rules in the grammar, the starting state, etc...*/
public interface ParserBasicInformation {

	int





    ERROR_SYMBOL      = 121,
    MAX_NAME_LENGTH   = 36,
    NUM_STATES        = 596,

    NT_OFFSET         = 121,
    SCOPE_UBOUND      = 54,
    SCOPE_SIZE        = 55,
    LA_STATE_OFFSET   = 7058,
    MAX_LA            = 1,
    NUM_RULES         = 437,
    NUM_TERMINALS     = 121,
    NUM_NON_TERMINALS = 200,
    NUM_SYMBOLS       = 321,
    START_STATE       = 2037,
    EOFT_SYMBOL       = 55,
    EOLT_SYMBOL       = 55,
    ACCEPT_ACTION     = 7057,
    ERROR_ACTION      = 7058;

}
