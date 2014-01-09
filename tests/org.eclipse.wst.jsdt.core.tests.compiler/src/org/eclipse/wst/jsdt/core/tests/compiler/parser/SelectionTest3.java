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

package org.eclipse.wst.jsdt.core.tests.compiler.parser;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptCore;

public class SelectionTest3 extends AbstractSelectionTest {
private String fContents1 = "/*\n" + 
" * Attempt content assist on each function, verify that the proposal shows up, that\n" + 
" * camel case CA works, the the propsoal looks correct, the proposal info is displayed,\n" + 
" * that occurance support works, that F3 works, that hover help is displayed, and that\n" + 
" * the function is correctly displayed in the outline.\n" + 
" * \n" + 
" * Also test out local functions, check functions that mask global functions. Try out\n" + 
" * F3 and occurance support on function calls that have different parameter numbers than\n" + 
" * the declared function.\n" + 
" */\n" + 
"\n" + 
"function zooKeeper() {\n" + 
"	\n" + 
"}\n" + 
"\n" + 
"/**\n" + 
" * Some doc about zooKeeper1, can you see it?\n" + 
" */\n" + 
"function zooKeeper1() {\n" + 
"	\n" + 
"}\n" + 
"\n" + 
"function zooKeeper2() {\n" + 
"	return \"zoo\";\n" + 
"}\n" + 
"\n" + 
"/**\n" + 
" * \n" + 
" * @returns {Boolean}\n" + 
" */\n" + 
"function zooKeeper3() {\n" + 
"	return a;\n" + 
"}\n" + 
"\n" + 
"function zooKeeper4(a, b) {\n" + 
"	\n" + 
"}\n" + 
"\n" + 
"/**\n" + 
" * Some doc about zooKeeper5\n" + 
" * \n" + 
" * @param {String} a\n" + 
" * @param {Number} b\n" + 
" */\n" + 
"function zooKeeper5(a, b) {\n" + 
"}\n" + 
"\n" + 
"function zooKeeper6(a) {\n" + 
"	return true;\n" + 
"}\n" + 
"\n" + 
"/**\n" + 
" * hi\n" + 
" * @param {Date} a\n" + 
" * @returns {Date}\n" + 
" */\n" + 
"function zooKeeper7(a) {\n" + 
"	return a;\n" + 
"}\n" + 
"\n" + 
"zooKeeper();\n" + 
"zooKeeper(1);\n" + 
"zooKeeper1();\n" + 
"zooKeeper2();\n" + 
"zooKeeper3();\n" + 
"zooKeeper4(1, 3);\n" + 
"zooKeeper5(\"hi\", 3);\n" + 
"zooKeeper6(1);\n" + 
"zooKeeper7(new Date());\n" + 
"zooKeeper7();\n" + 
"\n" + 
"function local1() {\n" + 
"	/**\n" + 
"	 * hi\n" + 
"	 */\n" + 
"	function zooKeeper7() {}\n" + 
"	function zooKeeper8(a) {}\n" + 
"	\n" + 
"	zooKeeper7();\n" + 
"}\n";

static {
//		TESTS_NUMBERS = new int[] { 53 };	
}
//public static Test suite() {
//	return buildAllCompliancesTestSuite(SelectionTest3.class);
//}
	
public SelectionTest3(String testName) {
	super(testName);
}
/**
 * @param string
 */
private IJavaScriptUnit getUnit(String string) throws Exception {
	IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("SelectionTest3_" + getName());
	IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription("SelectionTest3_" + getName());
	description.setNatureIds(new String[]{JavaScriptCore.NATURE_ID});
	project.create(description, null);
	project.open(null);
	IFile file = project.getFile("testfile.js");
	file.create(new ByteArrayInputStream(string.getBytes()), true, null);
	return (IJavaScriptUnit) JavaScriptCore.create(file);
}

public void test01() throws Exception {
	IJavaScriptUnit unit = getUnit(fContents1);
	
	int offset = fContents1.indexOf("zooKeeper();\n");
	IJavaScriptElement[] selected = unit.codeSelect(offset, 9);
	assertEquals("unexpected number of elements", 1, selected.length);
	assertEquals("unexpected element", "zooKeeper", selected[0].getElementName());
	assertEquals("unexpected element type", IJavaScriptElement.METHOD, selected[0].getElementType());
}
public void test02() throws Exception {
	IJavaScriptUnit unit = getUnit(fContents1);
	
	int offset = fContents1.indexOf("zooKeeper2();\n");
	IJavaScriptElement[] selected = unit.codeSelect(offset, 10);
	assertEquals("unexpected number of elements", 1, selected.length);
	assertEquals("unexpected element", "zooKeeper2", selected[0].getElementName());
	assertEquals("unexpected element type", IJavaScriptElement.METHOD, selected[0].getElementType());
}
public void test03() throws Exception {
	IJavaScriptUnit unit = getUnit(fContents1);
	
	int offset = fContents1.indexOf("zooKeeper3();\n");
	IJavaScriptElement[] selected = unit.codeSelect(offset, 10);
	assertEquals("unexpected number of elements", 1, selected.length);
	assertEquals("unexpected element", "zooKeeper3", selected[0].getElementName());
	assertEquals("unexpected element type", IJavaScriptElement.METHOD, selected[0].getElementType());
}
public void test04() throws Exception {
	IJavaScriptUnit unit = getUnit(fContents1);
	
	int offset = fContents1.indexOf("zooKeeper5(\"hi");
	IJavaScriptElement[] selected = unit.codeSelect(offset, 10);
	assertEquals("unexpected number of elements", 1, selected.length);
	assertEquals("unexpected element", "zooKeeper5", selected[0].getElementName());
	assertEquals("unexpected element type", IJavaScriptElement.METHOD, selected[0].getElementType());
}
public void test05() throws Exception {
	IJavaScriptUnit unit = getUnit(fContents1);
	
	int offset = fContents1.indexOf("zooKeeper6(1");
	IJavaScriptElement[] selected = unit.codeSelect(offset, 10);
	assertEquals("unexpected number of elements", 1, selected.length);
	assertEquals("unexpected element", "zooKeeper6", selected[0].getElementName());
	assertEquals("unexpected element type", IJavaScriptElement.METHOD, selected[0].getElementType());
}

}
