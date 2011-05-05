/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
var line1 = true;
function test() { //line 2
	return "line 3"; //line 3
}//line 4
// line 5
var line6 = test();
var line7 = test();
// line 8
// line 9
var line10 = test();
// line 11
// line 12
function test2() { // line 13
	return "line 14"; //line 14
} // line 15
// line 16
// line 17