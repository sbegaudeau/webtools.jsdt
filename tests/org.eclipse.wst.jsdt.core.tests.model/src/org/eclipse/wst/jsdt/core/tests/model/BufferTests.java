/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import org.eclipse.wst.jsdt.core.*;

import junit.framework.Test;

public class BufferTests extends ModifyingResourceTests implements
		IBufferChangedListener {
	protected ArrayList events = null;

	public BufferTests(String name) {
		super(name);
	}

	/**
	 * Cache the event
	 */
	public void bufferChanged(BufferChangedEvent bufferChangedEvent) {
		this.events.add(bufferChangedEvent);
	}

	protected IBuffer createBuffer(String path, String content)
			throws CoreException {
		waitUntilIndexesReady(); // ensure that the indexer is not reading the
									// file
		this.createFile(path, content);
		IJavaScriptUnit cu = this.getCompilationUnit(path);
		IBuffer buffer = cu.getBuffer();
		buffer.addBufferChangedListener(this);
		this.events = new ArrayList();
		return buffer;
	}

	protected void deleteBuffer(IBuffer buffer) throws CoreException {
		buffer.removeBufferChangedListener(this);
		IResource resource = buffer.getUnderlyingResource();
		if (resource != null) {
			deleteResource(resource);
		}
	}

	/**
	 * @see AbstractJavaModelTests#setUpSuite()
	 */
	public void setUpSuite() throws Exception {
		super.setUpSuite();
		try {
			this.createJavaProject("P", new String[] { "" });
			this.createFolder("P/x/y");
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see AbstractJavaModelTests#tearDownSuite()
	 */
	public void tearDownSuite() throws Exception {
		super.tearDownSuite();
		this.deleteProject("P");
	}

	public static Test suite() {
		return buildModelTestSuite(BufferTests.class);
	}

	/**
	 * Tests appending to a buffer.
	 */
	public void testAppend() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		try {
			int oldLength = buffer.getLength();
			buffer.append("\nvar a = new A();");
			assertBufferEvent(oldLength, 0, "\nvar a = new A();");
			assertSourceEquals("unexpected buffer contents", "function A() {\n};"
					+ "\nvar a = new A();", buffer.getContents());
			assertTrue("should have unsaved changes", buffer
					.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests appending to a read-only buffer.
	 */
	//TODO
	/*public void testAppendReadOnly() throws CoreException {
		IBuffer buffer = null;
		try {
			createJavaProject("P1", new String[] {}, "");
			// IClassFile classFile = getClassFile("P1",
			// getExternalJCLPathString(), "", "system.js");
			IJavaScriptUnit unit = getCompilationUnit("P1",
					getExternalJCLPathString(), "", "system.js");
			buffer = unit.getBuffer();
			buffer.addBufferChangedListener(this);
			this.events = new ArrayList();
			buffer.append("\nclass B {}");
			assertTrue("unexpected event", this.events.isEmpty());
			assertSourceEquals("unexpected buffer contents",
					"package java.lang;\n" + "\n" + "public class String {\n"
							+ "}\n", buffer.getContents());
			assertTrue("should not have unsaved changes", !buffer
					.hasUnsavedChanges());
		} finally {
			if (buffer != null) {
				buffer.removeBufferChangedListener(this);
			}
			deleteProject("P1");
		}
	}*/

	public void testClose() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		try {
			buffer.close();
			assertBufferEvent(0, 0, null);
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests getting the underlying resource of a buffer.
	 */
	public void testGetUnderlyingResource() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		IJavaScriptUnit copy = null;
		try {
			IFile file = this.getFile("P/x/y/A.js");
			assertEquals("Unexpected underlying resource", file, buffer
					.getUnderlyingResource());

			copy = this.getCompilationUnit("P/x/y/A.js").getWorkingCopy(null);
			assertEquals("Unexpected underlying resource 2", file, copy
					.getBuffer().getUnderlyingResource());
		} finally {
			this.deleteBuffer(buffer);
			if (copy != null) {
				copy.discardWorkingCopy();
			}
		}
	}

	/**
	 * Tests deleting text at the beginning of a buffer.
	 */
	public void testDeleteBeginning() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};\n" +
			"var a = new A();");
		try {
			buffer.replace(0, 18, "");
			assertBufferEvent(0, 18, null);
			assertSourceEquals("unexpected buffer contents",
					"var a = new A();", buffer.getContents());
			assertTrue("should have unsaved changes", buffer
					.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests deleting text in the middle of a buffer.
	 */
	public void testDeleteMiddle() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};\n" +
			"var a = new A();\nvar a1 = new A();");
		try {
			// delete var a = new A();\n
			buffer.replace(18, 17, "");
			assertBufferEvent(18, 17, null);
			assertSourceEquals("unexpected buffer contents", "function A() {\n};\n" +
					"var a1 = new A();", buffer.getContents());
			assertTrue("should have unsaved changes", buffer
					.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests deleting text at the end of a buffer.
	 */
	public void testDeleteEnd() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};\n" +
				"var a = new A();");
		try {
			// delete "public class A {\n}"
			buffer.replace(17, 17, "");
			assertBufferEvent(17, 17, null);
			assertSourceEquals("unexpected buffer contents", "function A() {\n};",
					buffer.getContents());
			assertTrue("should have unsaved changes", buffer
					.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests the buffer char retrieval via source position
	 */
	public void testGetChar() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		try {
			assertEquals("Unexpected char at position 9", 'A', buffer
					.getChar(9));
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests the buffer char retrieval via source position doesn't throw an
	 * exception if the buffer is closed.
	 */
	public void testGetChar2() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		buffer.close();
		try {
			assertEquals("Unexpected char at position 9", Character.MIN_VALUE,
					buffer.getChar(9));
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests the buffer getLength()
	 */
	public void testGetLength() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		try {
			assertEquals("Unexpected length", 17, buffer.getLength());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests the buffer text retrieval via source position
	 */
	public void testGetText() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		try {
			assertSourceEquals("Unexpected text (1)", "f", buffer.getText(0, 1));
			assertSourceEquals("Unexpected text (2)", "A()", buffer.getText(
					9, 3));
			assertSourceEquals("Unexpected text (3)", "", buffer.getText(12, 0));
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests inserting text at the beginning of a buffer.
	 */
	public void testInsertBeginning() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		try {
			buffer.replace(0, 0, "/* copyright mycompany */\n");
			assertBufferEvent(0, 0, "/* copyright mycompany */\n");
			assertSourceEquals("unexpected buffer contents",
					"/* copyright mycompany */\n" + "function A() {\n};", 
					buffer.getContents());
			assertTrue("should have unsaved changes", buffer
					.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests replacing text at the beginning of a buffer.
	 */
	public void testReplaceBeginning() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		try {
			// replace function A() with function B()
			buffer.replace(0, 12, "function B()");
			assertBufferEvent(0, 12, "function B()");
			assertSourceEquals("unexpected buffer contents", "function B() {\n};",
					buffer.getContents());
			assertTrue("should have unsaved changes", buffer
					.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests replacing text in the middle of a buffer.
	 */
	public void testReplaceMiddle() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		try {
			// replace "A()" after the \n of package statement
			buffer.replace(9, 3, "B( )");
			assertBufferEvent(9, 3, "B( )");
			assertSourceEquals("unexpected buffer contents", "function B( ) {\n};",
					buffer.getContents());
			assertTrue("should have unsaved changes", buffer
					.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests replacing text at the end of a buffer.
	 */
	public void testReplaceEnd() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		try {
			// replace ";" at the end of cu with ";\n"
			int end = buffer.getLength();
			buffer.replace(end - 1, 1, ";\n");
			assertBufferEvent(end - 1, 1, ";\n");
			assertSourceEquals("unexpected buffer contents", "function A() {\n};\n", 
					buffer.getContents());
			assertTrue("should have unsaved changes", buffer
					.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests inserting text in the middle of a buffer.
	 */
	public void testInsertMiddle() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A() {\n};");
		try {
			// insert comment in the middle of the {\n}
			buffer.replace(15, 0, "// comment\n");
			assertBufferEvent(15, 0, "// comment\n");
			assertSourceEquals("unexpected buffer contents", 
					"function A() {\n// comment\n};",
					buffer.getContents());
			assertTrue("should have unsaved changes", buffer
					.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Tests inserting text at the end of a buffer.
	 */
	public void testInsertEnd() throws CoreException {
		IBuffer buffer = this.createBuffer("P/x/y/A.js", "function A(){\n};");
		try {
			// insert function B(){\n} at the end of the buffer
			int end = buffer.getLength();
			buffer.replace(end, 0, "\nfunction B(){\n};");
			assertBufferEvent(end, 0, "\nfunction B(){\n};");
			assertSourceEquals("unexpected buffer contents", 
					"function A(){\n};\nfunction B(){\n};", buffer
					.getContents());
			assertTrue("should have unsaved changes", buffer
					.hasUnsavedChanges());
		} finally {
			this.deleteBuffer(buffer);
		}
	}

	/**
	 * Verify the buffer changed event. The given text must contain '\n' line
	 * separators.
	 */
	protected void assertBufferEvent(int offset, int length, String text) {
		assertTrue("events should not be null", this.events != null);
		assertTrue("events should not be empty", !this.events.isEmpty());
		BufferChangedEvent event = (BufferChangedEvent) this.events.get(0);
		assertEquals("unexpected offset", offset, event.getOffset());
		assertEquals("unexpected length", length, event.getLength());
		if (text == null) {
			assertTrue("text should be null", event.getText() == null);
		} else {
			assertSourceEquals("unexpected text", text, event.getText());
		}
	}

	protected void assertBufferEvents(String expected) {
		StringBuffer buffer = new StringBuffer();
		if (this.events == null)
			buffer.append("<null>");
		else {
			for (int i = 0, length = this.events.size(); i < length; i++) {
				BufferChangedEvent event = (BufferChangedEvent) this.events
						.get(i);
				buffer.append('(');
				buffer.append(event.getOffset());
				buffer.append(", ");
				buffer.append(event.getLength());
				buffer.append(") ");
				buffer.append(event.getText());
				if (i < length - 1)
					buffer.append("\n");
			}
		}
		assertSourceEquals("Unexpected buffer events", expected, buffer
				.toString());
	}
}
