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

package org.eclipse.wst.jsdt.ui.tests.documentation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.ui.JSdocContentAccess;

public class DocumentationTest extends TestCase {
	private String fContents1 = "/*\n"
			+ " * Attempt content assist on each function, verify that the proposal shows up, that\n"
			+ " * camel case CA works, the the propsoal looks correct, the proposal info is displayed,\n"
			+ " * that occurance support works, that F3 works, that hover help is displayed, and that\n"
			+ " * the function is correctly displayed in the outline.\n" + " * \n"
			+ " * Also test out local functions, check functions that mask global functions. Try out\n"
			+ " * F3 and occurance support on function calls that have different parameter numbers than\n"
			+ " * the declared function.\n" + " */\n" + "\n" + "function zooKeeper() {\n" + "	\n" + "}\n" + "\n"
			+ "/**\n" + " * Some doc about zooKeeper1, can you see it?\n" + " */\n" + "function zooKeeper1() {\n"
			+ "	\n" + "}\n" + "\n" + "function zooKeeper2() {\n" + "	return \"zoo\";\n" + "}\n" + "\n" + "/**\n"
			+ " * \n" + " * @returns {Boolean}\n" + " */\n" + "function zooKeeper3() {\n" + "	return a;\n" + "}\n"
			+ "\n" + "function zooKeeper4(a, b) {\n" + "	\n" + "}\n" + "\n" + "/**\n"
			+ " * Some doc about zooKeeper5\n" + " * \n" + " * @param {String} a\n" + " * @param {Number} b\n"
			+ " */\n" + "function zooKeeper5(a, b) {\n" + "}\n" + "\n" + "function zooKeeper6(a) {\n"
			+ "	return true;\n" + "}\n" + "\n" + "/**\n" + " * hi\n" + " * @param {Date} a\n" + " * @returns {Date}\n"
			+ " */\n" + "function zooKeeper7(a) {\n" + "	return a;\n" + "}\n" + "\n" + "zooKeeper();\n"
			+ "zooKeeper(1);\n" + "zooKeeper1();\n" + "zooKeeper2();\n" + "zooKeeper3();\n" + "zooKeeper4(1, 3);\n"
			+ "zooKeeper5(\"hi\", 3);\n" + "zooKeeper6(1);\n" + "zooKeeper7(new Date());\n" + "zooKeeper7();\n" + "\n"
			+ "function local1() {\n" + "	/**\n" + "	 * hi\n" + "	 */\n" + "	function zooKeeper7() {}\n"
			+ "	function zooKeeper8(a) {}\n" + "	\n" + "	zooKeeper7();\n" + "}\n";

	public static Test suite() {
		TestSuite ts = new TestSuite(DocumentationTest.class);
		return ts;
	}

	public DocumentationTest(String testName) {
		super(testName);
	}

	private void assertDocContainsString(IJavaScriptElement element, String s) throws JavaScriptModelException,
			IOException {
		Reader reader = JSdocContentAccess.getContentReader(element, true);
		StringBuffer doc = new StringBuffer();
		if(reader != null) {
			char[] buf = new char[200];
			int read = 0;
			while((read = reader.read(buf)) > 0) {
				doc.append(buf, 0, read);
			}
			reader.close();
		}
		assertTrue("doc doesn't contain content we were looking for", doc.toString().indexOf(s) >= 0);
	}

	/**
	 * @param string
	 */
	private IJavaScriptUnit getUnit(String string) throws Exception {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("DocumentationTest_" + getName());
		IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(
				"SelectionTest3_" + getName());
		description.setNatureIds(new String[] { JavaScriptCore.NATURE_ID });
		project.create(description, null);
		project.open(null);
		IFile file = project.getFile("testfile.js");
		file.create(new ByteArrayInputStream(string.getBytes()), true, null);
		return (IJavaScriptUnit) JavaScriptCore.create(file);
	}

	public void test01() throws Exception {
		IJavaScriptUnit unit = getUnit(fContents1);

		int offset = fContents1.indexOf("zooKeeper1();\n");
		IJavaScriptElement[] selected = unit.codeSelect(offset, 10);
		assertEquals("unexpected number of elements", 1, selected.length);
		assertEquals("unexpected element", "zooKeeper1", selected[0].getElementName());
		assertEquals("unexpected element type", IJavaScriptElement.METHOD, selected[0].getElementType());

		assertDocContainsString(selected[0], "doc about zooKeeper");
	}

	public void test02() throws Exception {
		IJavaScriptUnit unit = getUnit(fContents1);

		int offset = fContents1.indexOf("zooKeeper3();\n");
		IJavaScriptElement[] selected = unit.codeSelect(offset, 10);
		assertEquals("unexpected number of elements", 1, selected.length);
		assertEquals("unexpected element", "zooKeeper3", selected[0].getElementName());
		assertEquals("unexpected element type", IJavaScriptElement.METHOD, selected[0].getElementType());

		assertDocContainsString(selected[0], "Boolean");
	}

	public void test03() throws Exception {
		IJavaScriptUnit unit = getUnit(fContents1);

		int offset = fContents1.indexOf("zooKeeper5(\"hi");
		IJavaScriptElement[] selected = unit.codeSelect(offset, 10);
		assertEquals("unexpected number of elements", 1, selected.length);
		assertEquals("unexpected element", "zooKeeper5", selected[0].getElementName());
		assertEquals("unexpected element type", IJavaScriptElement.METHOD, selected[0].getElementType());

		assertDocContainsString(selected[0], "Some doc about zooKeeper5");
	}

	public void test04() throws Exception {
		IJavaScriptUnit unit = getUnit(fContents1);

		int offset = fContents1.indexOf("zooKeeper7(");
		IJavaScriptElement[] selected = unit.codeSelect(offset, 10);
		assertEquals("unexpected number of elements", 1, selected.length);
		assertEquals("unexpected element", "zooKeeper7", selected[0].getElementName());
		assertEquals("unexpected element type", IJavaScriptElement.METHOD, selected[0].getElementType());

		assertDocContainsString(selected[0], "Date");
	}
}
