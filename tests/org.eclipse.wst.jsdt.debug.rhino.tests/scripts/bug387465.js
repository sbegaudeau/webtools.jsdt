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

/*
 * Bug 387465 - Incorrect string evaluations with Rhino 1.7R4
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=387465
 */
var s = "a"; //line 1
s += "a"; // debugger shows that s is undefined here
s = "b";