/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.model;

import junit.framework.Test;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.core.util.Util;

public class JavaScriptLikeExtensionsTests extends ModifyingResourceTests {
	
	public JavaScriptLikeExtensionsTests(String name) {
		super(name);
	}
	
	public static Test suite() {
		return buildModelTestSuite(JavaScriptLikeExtensionsTests.class);
	}
	
	public void setUpSuite() throws Exception {
		super.setUpSuite();
		Util.resetJavaLikeExtensions();
	}

	/*
	 * Ensures that the known JavaScript-like extensions are correct.
	 */
	public void testGetJavaScriptLikeExtensions01() {
		assertSortedStringsEqual(
			"Unexpected file extensions",
			"js\n",
			JavaScriptCore.getJavaScriptLikeExtensions()
		);
	}

	/*
	 * Ensures that the known JavaScript-like extensions are correct after a JavaScript-like file extension is added.
	 */
	public void testGetJavaScriptLikeExtensions02() throws CoreException {
		IContentType javaContentType = Platform.getContentTypeManager().getContentType(JavaScriptCore.JAVA_SOURCE_CONTENT_TYPE);
		try {
			if (javaContentType != null)
				javaContentType.addFileSpec("abc", IContentType.FILE_EXTENSION_SPEC);
			assertSortedStringsEqual(
				"Unexpected file extensions",
				"abc\n" + 
				"js\n",
				JavaScriptCore.getJavaScriptLikeExtensions()
			);
		} finally {
			if (javaContentType != null)
				javaContentType.removeFileSpec("abc", IContentType.FILE_EXTENSION_SPEC);
		}
	}

	/*
	 * Ensure that file.js is a Java-like file name
	 */
	public void testIJavaScriptLikeFileName01() {
		assertTrue("file.js should be a JavaScript-like file name", JavaScriptCore.isJavaScriptLikeFileName("file.js"));
	}

	/*
	 * Ensure that file.other is not a Java-like file name
	 */
	public void testIJavaScriptLikeFileName02() {
		assertFalse("file.other should not be a JavaScript-like file name", JavaScriptCore.isJavaScriptLikeFileName("file.other"));
	}

	/*
	 * Ensure that file is not a JavaScript-like file name
	 */
	public void testIJavaScriptLikeFileName04() {
		assertFalse("file should not be a JavaScript-like file name", JavaScriptCore.isJavaScriptLikeFileName("file"));
	}

	/*
	 * Ensure that removing the JavaScript-like extension for file.js returns js
	 */
	public void testRemoveJavaScriptLikeExtension02() {
		assertEquals("Unexpected file without JavaScript-like extension", "file", JavaScriptCore.removeJavaScriptLikeExtension("file.js"));
	}

	/*
	 * Ensure that removing the JavaScript-like extension for file.other returns foo.other
	 */
	public void testRemoveJavaScriptLikeExtension03() {
		assertEquals("Unexpected file without JavaScript-like extension", "file.other", JavaScriptCore.removeJavaScriptLikeExtension("file.other"));
	}
}
