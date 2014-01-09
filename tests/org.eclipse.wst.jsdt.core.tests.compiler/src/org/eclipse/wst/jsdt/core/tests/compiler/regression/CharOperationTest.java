/*******************************************************************************
 * Copyright (c) 2005, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import org.eclipse.wst.jsdt.core.compiler.CharOperation;

public class CharOperationTest extends AbstractRegressionTest {
	
public CharOperationTest(String name) {
	super(name);
}

public void test001() {
	 char[] array = { 'a' , 'b', 'b', 'c', 'a', 'b', 'c', 'a' };
	 char[] toBeReplaced = { 'b', 'c' };
	 char replacementChar = 'a';
	 int  start = 4;
	 int  end = 8;
	 CharOperation.replace(array, toBeReplaced, replacementChar, start, end);
	 char[] result = { 'a' , 'b', 'b', 'c', 'a', 'a', 'a', 'a' };

	 for (int i = 0, max = array.length; i < max; i++) {
		 assertEquals("Wrong value at " + i, result[i], array[i]);
	 }
}
public void test002() {
	 char[] array = { 'a' , 'b', 'b', 'c', 'a', 'b', 'c', 'a' };
	 char[] toBeReplaced = { 'b', 'c' };
	 char replacementChar = 'a';
	 int  start = 2;
	 int  end = 3;
	 CharOperation.replace(array, toBeReplaced, replacementChar, start, end);
	 char[] result = { 'a' , 'b', 'a', 'c', 'a', 'b', 'c', 'a' };

	 for (int i = 0, max = array.length; i < max; i++) {
		 assertEquals("Wrong value at " + i, result[i], array[i]);
	 }
}
public void test003() {
	 char[] second = { 'a' , 'b', 'b', 'c', 'a', 'b', 'c', 'a' };
	 char[] first = { 'b', 'c', 'a' };
	 int  start = 2;
	 int  end = 5;
	 assertTrue(CharOperation.equals(first, second, start, end, true));
}
public void test004() {
	 char[] second = { 'A' };
	 char[] first = { 'a' };
	 int  start = 0;
	 int  end = 1;
	 assertTrue(CharOperation.equals(first, second, start, end, false));
}
public void test005() {
	 char[] array = { 'a' , 'b', 'b', 'c', 'a', 'b', 'c', 'a' };
	 char[] toBeReplaced = { 'b', 'c' };
	 char replacementChar = 'a';
	 CharOperation.replace(array, toBeReplaced, replacementChar);
	 char[] result = { 'a' , 'a', 'a', 'a', 'a', 'a', 'a', 'a' };

	 for (int i = 0, max = array.length; i < max; i++) {
		 assertEquals("Wrong value at " + i, result[i], array[i]);
	 }
}
public void test006() {
	 char[] array = { 'a' , 'a', 'a', 'a', 'a', 'b', 'c', 'a' };
	 char[] toBeReplaced = { 'a', 'a' };
	 char[] replacementChar = { 'a' };
	 char[] result = CharOperation.replace(array, toBeReplaced, replacementChar);
	 char[] expectedValue = { 'a' , 'a', 'a', 'b', 'c', 'a' };
	 assertEquals("Wrong size", expectedValue.length, result.length);
	 for (int i = 0, max = expectedValue.length; i < max; i++) {
		 assertEquals("Wrong value at " + i, result[i], expectedValue[i]);
	 }
}
// test compareTo(char[], char[])
public void test007() {
	char[] array = { 'a' , 'a', 'a', 'a', 'a', 'b', 'c', 'a' };
	char[] array2 = { 'a', 'a' };
	assertTrue(CharOperation.compareTo(array, array2) > 0);

	 array2 = new char[] { 'a', 'a' };
	 array = new char[] { 'a' , 'a', 'a', 'a', 'a', 'b', 'c', 'a' };
	 assertTrue(CharOperation.compareTo(array2, array) < 0);

	array = new char[] { 'a' , 'a', 'a', 'a', 'a', 'b', 'c', 'a' };
	array2 = new char[] { 'a' , 'a', 'a', 'a', 'a', 'b', 'c', 'a' };
	assertTrue(CharOperation.compareTo(array, array2) == 0);
	assertTrue(CharOperation.compareTo(array2, array) == 0);

	array = new char[] { 'a' , 'b', 'c' };
	array2 = new char[] { 'a' , 'b', 'c', 'a', 'a'};
	assertTrue(CharOperation.compareTo(array, array2) < 0);

	array = new char[] { 'a' , 'b', 'c' };
	array2 = new char[] { 'a' , 'b', 'd'};
	assertTrue(CharOperation.compareTo(array, array2) < 0);
}
public void test008a() {
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=334922
	char[] array = "A*".toCharArray();
	char[] array2 = "AA".toCharArray();
	assertTrue("case sensitive prefix match failed", CharOperation.match(array, array2, true));
}
public void test008b() {
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=334922
	char[] array = "A*".toCharArray();
	char[] array2 = "aa".toCharArray();
	assertTrue("case insensitive prefix match failed", CharOperation.match(array, array2, false));
}
public static Class testClass() {
	return CharOperationTest.class;
}
}
