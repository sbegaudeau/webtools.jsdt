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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import junit.framework.Test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IOpenable;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.ISourceReference;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.core.tests.model.AbstractJavaSearchTests.JavaSearchResultCollector;
import org.eclipse.wst.jsdt.internal.core.util.Util;

public class EncodingTests extends ModifyingResourceTests {
	IProject encodingProject;
	IJavaScriptProject encodingJavaProject;
	IFile utf8File;
	ISourceReference utf8Source;
	static String vmEncoding = System.getProperty("file.encoding");
	static String wkspEncoding = vmEncoding;

	public EncodingTests(String name) {
		super(name);
	}
	
	public static Test suite() {
		return buildModelTestSuite(EncodingTests.class);
	}
	// Use this static initializer to specify subset for tests
	// All specified tests which do not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "testBug110576" };
//		TESTS_NUMBERS = new int[] { 2, 12 };
//		TESTS_RANGE = new int[] { 16, -1 };
	}

	public void setUpSuite() throws Exception {
		super.setUpSuite();
		wkspEncoding = getWorkspaceRoot().getDefaultCharset();
		System.out.println("Encoding tests using Workspace charset: "+wkspEncoding+" and VM charset: "+vmEncoding);
		this.encodingJavaProject = setUpJavaProject("Encoding");
		this.encodingProject = (IProject) this.encodingJavaProject.getResource();
		this.utf8File = (IFile) this.encodingProject.findMember("src/testUTF8/Test.js");
	}

	public void tearDownSuite() throws Exception {
		super.tearDownSuite();
		getWorkspaceRoot().setDefaultCharset(null, null);
		deleteProject("Encoding");
	}
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 *  (non-Javadoc)
	 * Reset UTF-8 file and project charset to default.
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		this.encodingProject.setDefaultCharset(null, null);
		if (this.utf8File.exists()) this.utf8File.setCharset(null, null);
		if (this.utf8Source != null) ((IOpenable) this.utf8Source).close();
		this.encodingJavaProject.close();
		super.tearDown();
	}

	void compareContents(IJavaScriptUnit cu, String encoding) throws JavaScriptModelException {
		compareContents(cu, encoding, false);
	}

	void compareContents(IJavaScriptUnit cu, String encoding, boolean bom) throws JavaScriptModelException {
		// Compare source strings
		String source = cu.getSource();
		String systemSourceRenamed = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(source);
		IFile file = (IFile) cu.getUnderlyingResource();
		String renamedContents = new String (Util.getResourceContentsAsCharArray(file));
		renamedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(renamedContents);
		assertEquals("Encoded UTF-8 source should have been decoded the same way!", renamedContents, systemSourceRenamed);
		// Compare bytes array
		byte[] renamedSourceBytes = null;
		try {
			renamedSourceBytes = source.getBytes(encoding);
		}
		catch (UnsupportedEncodingException uue) {
		}
		assertNotNull("Unsupported encoding: "+encoding, renamedSourceBytes);
		byte[] renamedEncodedBytes = Util.getResourceContentsAsByteArray(file);
		int start = bom ? IContentDescription.BOM_UTF_8.length : 0;
		assertEquals("Wrong size of encoded string", renamedEncodedBytes.length-start, renamedSourceBytes.length);
		for (int i = 0, max = renamedSourceBytes.length; i < max; i++) {
			assertTrue("Wrong size of encoded character at " + i, renamedSourceBytes[i] == renamedEncodedBytes[i+start]);
		}
	}

	public boolean convertToIndependantLineDelimiter(File file) {
		return false; // don't convert to independant line delimiter as this make tests fail on linux
	}
	/**
	 * Check that the compilation unit is saved with the proper encoding.
	 */
	public void testCreateCompilationUnitAndImportContainer() throws JavaScriptModelException, CoreException {
		String savedEncoding = null;
		try {
			Preferences preferences = ResourcesPlugin.getPlugin().getPluginPreferences();
			
			savedEncoding = preferences.getString(ResourcesPlugin.PREF_ENCODING);
			String encoding = "UTF-8";
			preferences.setValue(ResourcesPlugin.PREF_ENCODING, encoding);
			
			ResourcesPlugin.getPlugin().savePluginPreferences();

			IJavaScriptProject newProject = createJavaProject("P", new String[] { "" });
			IPackageFragment pkg = getPackageFragment("P", "", "");
			String source = "public class A {\r\n" +
				"	public static main(String[] args) {\r\n" +
				"		System.out.println(\"\u00e9\");\r\n" +
				"	}\r\n" +
				"}";
			IJavaScriptUnit cu= pkg.createCompilationUnit("A.js", source, false, new NullProgressMonitor());
			assertCreation(cu);
			cu.rename("B.js", true, new NullProgressMonitor());
			cu = pkg.getJavaScriptUnit("B.js");
			cu.rename("A.js", true, new NullProgressMonitor());
			cu = pkg.getJavaScriptUnit("A.js");
			byte[] tab = null;
			try {
				tab = cu.getSource().getBytes(encoding);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			byte[] encodedContents = Util.getResourceContentsAsByteArray(newProject.getProject().getWorkspace().getRoot().getFile(cu.getPath()));
			assertEquals("wrong size of encoded string", tab.length, encodedContents.length);
			for (int i = 0, max = tab.length; i < max; i++) {
				assertTrue("wrong size of encoded character at" + i, tab[i] == encodedContents[i]);
			}
		} finally {
			deleteProject("P");
			Preferences preferences = ResourcesPlugin.getPlugin().getPluginPreferences();
			preferences.setValue(ResourcesPlugin.PREF_ENCODING, savedEncoding);
			ResourcesPlugin.getPlugin().savePluginPreferences();
		}
	}	

	/*
	##################
	#	Test with compilation units
	##################
	/*
	 * Get compilation unit source on a file written in UTF-8 charset using specific UTF-8 encoding for file.
	 * Verify first that source is the same than file contents read using UTF-8 encoding...
	 * Also verify that bytes array converted back to UTF-8 is the same than the file bytes array.
	 */
	public void test001() throws JavaScriptModelException, CoreException, UnsupportedEncodingException {

		// Set file encoding
		String encoding = "UTF-8";
		this.utf8File.setCharset(encoding, null);
		
		// Get source and compare with file contents
		this.utf8Source = getCompilationUnit(this.utf8File.getFullPath().toString());
		String source = this.utf8Source.getSource();
		String systemSource = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(source);
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertEquals("Encoded UTF-8 source should have been decoded the same way!", encodedContents, systemSource);

		// Now compare bytes array
		byte[] sourceBytes = source.getBytes(encoding);
		byte[] encodedBytes = Util.getResourceContentsAsByteArray(this.utf8File);
		assertEquals("Wrong size of encoded string", encodedBytes.length, sourceBytes.length);
		for (int i = 0, max = sourceBytes.length; i < max; i++) {
			assertTrue("Wrong size of encoded character at " + i, sourceBytes[i] == encodedBytes[i]);
		}
	}	

	/*
	 * Get compilation unit source on a file written in UTF-8 charset using UTF-8 encoding for project.
	 * Verify first that source is the same than file contents read using UTF-8 encoding...
	 * Also verify that bytes array converted back to UTF-8 is the same than the file bytes array.
	 */
	public void test002() throws JavaScriptModelException, CoreException, UnsupportedEncodingException {

		// Set project encoding
		String encoding = "UTF-8";
		this.encodingProject.setDefaultCharset(encoding, null);
		
		// Get source and compare with file contents
		this.utf8Source = getCompilationUnit(this.utf8File.getFullPath().toString());
		String source = this.utf8Source.getSource();
		String systemSource = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(source);
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertEquals("Encoded UTF-8 source should have been decoded the same way!", encodedContents, systemSource);

		// Now compare bytes array
		byte[] sourceBytes = source.getBytes(encoding);
		byte[] encodedBytes = Util.getResourceContentsAsByteArray(this.utf8File);
		assertEquals("Wrong size of encoded string", encodedBytes.length, sourceBytes.length);
		for (int i = 0, max = sourceBytes.length; i < max; i++) {
			assertTrue("Wrong size of encoded character at " + i, sourceBytes[i] == encodedBytes[i]);
		}
	}	

	/*
	 * Get compilation unit source on a file written in UTF-8 charset using workspace default encoding.
	 * Verify that source is the same than file contents read using workspace default encoding...
	 * Also verify that bytes array converted back to wokrspace default encoding is the same than the file bytes array.
	 * Do not compare array contents in case of VM default encoding equals to "ASCII" as meaningful bit 7 is lost
	 * during first conversion...
	 */
	public void test003() throws JavaScriptModelException, CoreException, UnsupportedEncodingException {

		// Get source and compare with file contents
		this.utf8Source = getCompilationUnit(this.utf8File.getFullPath().toString());
		String source = this.utf8Source.getSource();
		String systemSource = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(source);
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertEquals("Encoded UTF-8 source should have been decoded the same way!", encodedContents, systemSource);
			
		// Now compare bytes array
		byte[] sourceBytes = source.getBytes(wkspEncoding);
		byte[] encodedBytes = Util.getResourceContentsAsByteArray(this.utf8File);
		assertEquals("Wrong size of encoded string", encodedBytes.length, sourceBytes.length);
		// Do not compare arrays contents as system encoding may have lost meaningful bit 7 during convertion...)
//		if (!"ASCII".equals(vmEncoding)) {
//			for (int i = 0, max = sourceBytes.length; i < max; i++) {
//				assertTrue("Wrong size of encoded character at " + i, sourceBytes[i] == encodedBytes[i]);
//			}
//		}
	}

	/*
	 * Get compilation unit source on a file written in UTF-8 charset using an encoding
	 * for file different than VM default one.
	 * Verify that source is different than file contents read using VM default encoding...
	 */
	public void test004() throws JavaScriptModelException, CoreException {

		// Set file encoding
		String encoding = "UTF-8".equals(vmEncoding) ? "Cp1252" : "UTF-8";
		this.utf8File.setCharset(encoding, null);
		
		// Get source and compare with file contents
		this.utf8Source = getCompilationUnit(this.utf8File.getFullPath().toString());
		String source = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(this.utf8Source.getSource());
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File, vmEncoding));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertFalse("Sources should not be the same as they were decoded with different encoding!", encodedContents.equals(source));
	}

	/*
	 * Get compilation unit source on a file written in UTF-8 charset using an encoding
	 * for project different than VM default one.
	 * Verify that source is different than file contents read using VM default encoding...
	 */
	public void test005() throws JavaScriptModelException, CoreException {

		// Set project encoding
		String encoding = "UTF-8".equals(vmEncoding) ? "Cp1252" : "UTF-8";
		this.encodingProject.setDefaultCharset(encoding, null);
		
		// Get source and compare with file contents
		this.utf8Source = getCompilationUnit(this.utf8File.getFullPath().toString());
		String source = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(this.utf8Source.getSource());
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File, vmEncoding));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertFalse("Sources should not be the same as they were decoded with different encoding!", encodedContents.equals(source));
	}	

	/*
	 * Get compilation unit source on a file written in UTF-8 charset using workspace default encoding.
	 * Verify that source is different than file contents read using VM default encoding or another one
	 * if VM and Workspace default encodings are identical...
	 */
	public void test006() throws JavaScriptModelException, CoreException {

		// Set encoding different than workspace default one
		String encoding = wkspEncoding.equals(vmEncoding) ? ("UTF-8".equals(wkspEncoding) ? "Cp1252" : "UTF-8") : vmEncoding;

		// Get source and compare with file contents
		this.utf8Source = getCompilationUnit(this.utf8File.getFullPath().toString());
		String source = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(this.utf8Source.getSource());
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File, encoding));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertFalse("Sources should not be the same as they were decoded with different encoding!", encodedContents.equals(source));
	}

	/*
	##############
	#	Tests with class file
	##############
	/* Same config than test001  */
	public void test011() throws JavaScriptModelException, CoreException, UnsupportedEncodingException {

		// Set file encoding
		String encoding = "UTF-8";
		this.utf8File.setCharset(encoding, null);
		
		// Get source and compare with file contents
		this.utf8Source = getClassFile("Encoding" , "bins", "testUTF8", "Test.class"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		String source = this.utf8Source.getSource();
		String systemSource = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(source);
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertEquals("Encoded UTF-8 source should have been decoded the same way!", encodedContents, systemSource);

		// Now compare bytes array
		byte[] sourceBytes = source.getBytes(encoding);
		byte[] encodedBytes = Util.getResourceContentsAsByteArray(this.utf8File);
		assertEquals("Wrong size of encoded string", encodedBytes.length, sourceBytes.length);
		for (int i = 0, max = sourceBytes.length; i < max; i++) {
			assertTrue("Wrong size of encoded character at " + i, sourceBytes[i] == encodedBytes[i]);
		}
	}	

	/* Same config than test002  */
	public void test012() throws JavaScriptModelException, CoreException, UnsupportedEncodingException {

		// Set project encoding
		String encoding = "UTF-8";
		this.encodingProject.setDefaultCharset(encoding, null);
		
		// Get source and compare with file contents
		this.utf8Source = getClassFile("Encoding" , "bins", "testUTF8", "Test.class"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		String source = this.utf8Source.getSource();
		String systemSource = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(source);
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertEquals("Encoded UTF-8 source should have been decoded the same way!", encodedContents, systemSource);

		// Now compare bytes array
		byte[] sourceBytes = source.getBytes(encoding);
		byte[] encodedBytes = Util.getResourceContentsAsByteArray(this.utf8File);
		assertEquals("Wrong size of encoded string", encodedBytes.length, sourceBytes.length);
		for (int i = 0, max = sourceBytes.length; i < max; i++) {
			assertTrue("Wrong size of encoded character at " + i, sourceBytes[i] == encodedBytes[i]);
		}
	}	

	/* Same config than test003  */
	public void test013() throws JavaScriptModelException, CoreException, UnsupportedEncodingException {

		// Get source and compare with file contents
		this.utf8Source = getClassFile("Encoding" , "bins", "testUTF8", "Test.class"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		String source = this.utf8Source.getSource();
		String systemSource = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(source);
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertEquals("Encoded UTF-8 source should have been decoded the same way!", encodedContents, systemSource);
			
		// Now compare bytes array
		byte[] sourceBytes = source.getBytes(wkspEncoding);
		byte[] encodedBytes = Util.getResourceContentsAsByteArray(this.utf8File);
		assertEquals("Wrong size of encoded string", encodedBytes.length, sourceBytes.length);
		// Do not compare arrays contents as system encoding may have lost meaningful bit 7 during convertion...)
//		if (!"ASCII".equals(vmEncoding)) {
//			for (int i = 0, max = sourceBytes.length; i < max; i++) {
//				assertTrue("Wrong size of encoded character at " + i, sourceBytes[i] == encodedBytes[i]);
//			}
//		}
	}

	/* Same config than test004  */
	public void test014() throws JavaScriptModelException, CoreException {

		// Set file encoding
		String encoding = "UTF-8".equals(vmEncoding) ? "Cp1252" : "UTF-8";
		this.utf8File.setCharset(encoding, null);
		
		// Get source and compare with file contents
		this.utf8Source = getClassFile("Encoding" , "bins", "testUTF8", "Test.class"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		String source = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(this.utf8Source.getSource());
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File, vmEncoding));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertFalse("Sources should not be the same as they were decoded with different encoding!", encodedContents.equals(source));
	}

	/* Same config than test005  */
	public void test015() throws JavaScriptModelException, CoreException {

		// Set project encoding
		String encoding = "UTF-8".equals(vmEncoding) ? "Cp1252" : "UTF-8";
		this.encodingProject.setDefaultCharset(encoding, null);
		
		// Get source and compare with file contents
		this.utf8Source = getClassFile("Encoding" , "bins", "testUTF8", "Test.class"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		String source = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(this.utf8Source.getSource());
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File, vmEncoding));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertFalse("Sources should not be the same as they were decoded with different encoding!", encodedContents.equals(source));
	}	

	/* Same config than test006  */
	public void test016() throws JavaScriptModelException, CoreException {

		// Set encoding different than workspace default one
		String encoding = wkspEncoding.equals(vmEncoding) ? ("UTF-8".equals(wkspEncoding) ? "Cp1252" : "UTF-8") : vmEncoding;

		// Get source and compare with file contents
		this.utf8Source = getClassFile("Encoding" , "bins", "testUTF8", "Test.class"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		String source = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(this.utf8Source.getSource());
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File, encoding));
		encodedContents = org.eclipse.wst.jsdt.core.tests.util.Util.convertToIndependantLineDelimiter(encodedContents);
		assertFalse("Sources should not be the same as they were decoded with different encoding!", encodedContents.equals(source));
	}

	/*
	###############################
	#	Tests with jar file and source attached in zip file
	###############################
	/**
	 * Get class file from jar file with an associated source written in UTF-8 charset using no specific encoding for file.
	 * Verification is done by comparing source with file contents read directly with VM encoding...
	 */
	public void test021() throws JavaScriptModelException, CoreException {

		// Get class file and compare source
		IPackageFragmentRoot root = getPackageFragmentRoot("Encoding", "testUTF8.jar");
		this.utf8Source = root.getPackageFragment("testUTF8").getClassFile("Test.class");
		assertNotNull(this.utf8Source);
		String source = this.utf8Source.getSource();
		assertNotNull(source);
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File, vmEncoding));
		assertSourceEquals("Encoded UTF-8 source should have been decoded the same way!", source, encodedContents);

		// Cannot compare bytes array without encoding as we're dependent of linux/windows os for new lines delimiter
	}

	/*
	 * Get class file from jar file with an associated source written in UTF-8 charset using specific UTF-8 encoding for project.
	 * Verification is done by comparing source with file contents read directly with UTF-8 encoding...
	 */
	public void test022() throws JavaScriptModelException, CoreException {

		// Set project encoding
		String encoding = "UTF-8".equals(vmEncoding) ? "Cp1252" : "UTF-8";
		this.encodingProject.setDefaultCharset(encoding, null);

		// Get class file and compare source (should not be the same as modify charset on zip file has no effect...)
		IPackageFragmentRoot root = getPackageFragmentRoot("Encoding", "testUTF8.jar");
		this.utf8Source = root.getPackageFragment("testUTF8").getClassFile("Test.class");
		assertNotNull(this.utf8Source);
		String source = this.utf8Source.getSource();
		assertNotNull(source);
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File, encoding));
		assertFalse("Sources should not be the same as they were decoded with different encoding!", encodedContents.equals(source));
	}

	/*
	 * Get class file from jar file with an associated source written in UTF-8 charset using specific UTF-8 encoding for file.
	 * Verification is done by comparing source with file contents read directly with UTF-8 encoding...
	 */
	public void test023() throws JavaScriptModelException, CoreException {

		// Set file encoding
		String encoding = "UTF-8".equals(vmEncoding) ? "Cp1252" : "UTF-8";
		IFile zipFile = (IFile) this.encodingProject.findMember("testUTF8.zip"); //$NON-NLS-1$
		assertNotNull("Cannot find class file!", zipFile);
		zipFile.setCharset(encoding, null);

		// Get class file and compare source (should not be the same as modify charset on zip file has no effect...)
		IPackageFragmentRoot root = getPackageFragmentRoot("Encoding", "testUTF8.jar");
		this.utf8Source = root.getPackageFragment("testUTF8").getClassFile("Test.class");
		assertNotNull(this.utf8Source);
		String source = this.utf8Source.getSource();
		assertNotNull(source);
		String encodedContents = new String (Util.getResourceContentsAsCharArray(this.utf8File, encoding));
		assertFalse("Sources should not be the same as they were decoded with different encoding!", encodedContents.equals(source));
		
		// Reset zip file encoding
		zipFile.setCharset(null, null);
	}

	/**
	 * Test for bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=55930.
	 * Verify Buffer.save(IProgressMonitor, boolean) method.
	 */
	public void test030() throws JavaScriptModelException, CoreException {
		IJavaScriptUnit workingCopy = null;
		try {
			String encoding = "UTF-8";
			this.createJavaProject("P", new String[] {""});
			String initialContent = "/**\n"+
				" */\n"+
				"public class Test {}";
			IFile file = this.createFile("P/Test.js", initialContent);
			file.setCharset(encoding, null);
			IJavaScriptUnit cu = this.getCompilationUnit("P/Test.js"); 
			
			// Modif direct the buffer
			String firstModif = "/**\n"+
				" * Caract?res exotiques:\n"+
				" * ?|#|?|?|?|?|?|?|?|?|??\n"+
				" */\n"+
				"public class Test {}";
			cu.getBuffer().setContents(firstModif);
			cu.getBuffer().save(null, true);
			String source = cu.getBuffer().getContents();
			
			// Compare strings and bytes arrays
			String encodedContents = new String (Util.getResourceContentsAsCharArray(file, encoding));
			assertEquals("Encoded UTF-8 source should have been decoded the same way!", encodedContents, source);
			byte[] sourceBytes = source.getBytes(encoding);
			byte[] encodedBytes = Util.getResourceContentsAsByteArray(file);
			assertEquals("Wrong size of encoded string", encodedBytes.length, sourceBytes.length);
			for (int i = 0, max = sourceBytes.length; i < max; i++) {
				assertTrue("Wrong size of encoded character at " + i, sourceBytes[i] == encodedBytes[i]);
			}
		} catch (UnsupportedEncodingException e) {
		} finally {
			this.stopDeltas();
			if (workingCopy != null) {
				workingCopy.discardWorkingCopy();
			}
			this.deleteProject("P");
		}

	}

	/**
	 * Test for bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=55930.
	 * Verify CommitWorkingCopyOperation.executeOperation() method.
	 */
	public void test031() throws JavaScriptModelException, CoreException {
		IJavaScriptUnit workingCopy = null;
		try {
			String encoding = "UTF-8";
			this.createJavaProject("P", new String[] {""});
			String initialContent = "/**\n"+
				" */\n"+
				"public class Test {}";
			IFile file = this.createFile("P/Test.js", initialContent);
			file.setCharset(encoding, null);
			IJavaScriptUnit cu = this.getCompilationUnit("P/Test.js"); 
			
			// Modif using working copy
			workingCopy = cu.getWorkingCopy(null);
			String secondModif = "/**\n"+
				" * Caract?res exotiques:\n"+
				" * ?|#|?|?|?|?|?|?|?|?|??\n"+
				" * Autres caract?res exotiques:\n"+
				" * ?|?|?|?|?|?\n"+
				" */\n"+
				"public class Test {}";
			workingCopy.getBuffer().setContents(secondModif);
			workingCopy.commitWorkingCopy(true, null);
			String source = workingCopy.getBuffer().getContents();
			
			// Compare strings and bytes arrays
			String encodedContents = new String (Util.getResourceContentsAsCharArray(file));
			assertEquals("Encoded UTF-8 source should have been decoded the same way!", encodedContents, source);
			byte[] sourceBytes = source.getBytes(encoding);
			byte[] encodedBytes = Util.getResourceContentsAsByteArray(file);
			assertEquals("Wrong size of encoded string", encodedBytes.length, sourceBytes.length);
			for (int i = 0, max = sourceBytes.length; i < max; i++) {
				assertTrue("Wrong size of encoded character at " + i, sourceBytes[i] == encodedBytes[i]);
			}
		} catch (UnsupportedEncodingException e) {
		} finally {
			this.stopDeltas();
			if (workingCopy != null) {
				workingCopy.discardWorkingCopy();
			}
			this.deleteProject("P");
		}
	}

	/*
	 * Get compilation unit source on a file written in UTF-8 BOM charset using default charset.
	 * Verify first that source is the same than UTF-8 file contents read using UTF-8 encoding...
	 */
	public void test032() throws JavaScriptModelException, CoreException {

		// Set file encoding
		String encoding = "UTF-8";
		this.utf8File.setCharset(encoding, null);
		
		// Get source and compare with file contents
		this.utf8Source = getCompilationUnit(this.utf8File.getFullPath().toString());
		String source = this.utf8Source.getSource();

		// Get source and compare with file contents
		IFile bomFile = (IFile) this.encodingProject.findMember("src/testUTF8BOM/Test.js");
		ISourceReference bomSourceRef = getCompilationUnit(bomFile.getFullPath().toString());
		String bomSource = bomSourceRef.getSource();
		assertEquals("BOM UTF-8 source should be idtentical than UTF-8!", source, bomSource);
	}	
	
	/*
	 * Ensures that a file is reindexed when the encoding changes.
	 * (regression test for bug 68585 index is out of date after encoding change)
	 */
	public void test033() throws CoreException {
		try {
			createFolder("/Encoding/src/test68585");
			final String encoding = "UTF-8".equals(wkspEncoding) ? "Cp1252" : "UTF-8";
			getWorkspace().run(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					// use a different encoding to make the file unreadable
					IFile file = null;
					try {
						file = createFile(
							"/Encoding/src/test68585/X.js", 
							"package  test68585;\n" +
							"public class X {\n" +
							"}\n" +
							"class Y\u00F4 {}",
							encoding);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						return;
					}
					file.setCharset(wkspEncoding, null);
				}
			}, 
			null);
			
			IJavaScriptSearchScope scope = SearchEngine.createWorkspaceScope();
			JavaSearchResultCollector resultCollector = new JavaSearchResultCollector();
			search(
				"Y\u00F4", 
				IJavaScriptSearchConstants.TYPE,
				IJavaScriptSearchConstants.DECLARATIONS,
				scope, 
				resultCollector);
			assertSearchResults("Should not get any result", "", resultCollector);
			
			// change encoding so that file is readable
			getFile("/Encoding/src/test68585/X.js").setCharset(encoding, null);
			search(
				"Y\u00F4", 
				IJavaScriptSearchConstants.TYPE,
				IJavaScriptSearchConstants.DECLARATIONS,
				scope, 
				resultCollector);
			assertSearchResults(
				"Should have been reindexed", 
				"src/test68585/X.java test68585.Y\u00F4 [Y\u00F4]",
				resultCollector);
		} finally {
			deleteFolder("/Encoding/src/test68585");
		}
	}

	/*
	 * Ensures that an encoding that a file using an encoding producing more charaters than the file size can
	 * be correctly read.
	 * (regression test for bug 149028 Limiting number of characters to read with the file size is invalid.)
	 */
	public void test034() throws CoreException, IOException {
		try {
			// Create file
			IFile file = createFile("/Encoding/Test34.txt", "acegikm");
			
			// Read file using a transformation where a character is read and the next alphabetical character is
			// automaticaly added
			final InputStream fileStream = file.getContents();
			try {
				InputStream in = new InputStream() {
					int current = -1;
					public int read() throws IOException {
						int result;
						if (current != -1) {
							result = current;
							current = -1;
						} else {
							result = fileStream.read();
							if (result == -1)
								return -1;
							current = result + 1;
						}
						return result;
					}
				};
				char[] result = org.eclipse.wst.jsdt.internal.compiler.util.Util.getInputStreamAsCharArray(in, (int) file.getLocation().toFile().length(), "UTF-8");
				assertSourceEquals(
					"Unexpected source",
					"abcdefghijklmn",
					new String(result)					
				);
			} finally {
				fileStream.close();
			}
		} finally {
			deleteFile("Encoding/Test34.txt");
		}
	}	
	
	/**
	 * Bug 66898: refactor-rename: encoding is not preserved
	 * @see "http://bugs.eclipse.org/bugs/show_bug.cgi?id=66898"
	 */
	public void testBug66898() throws JavaScriptModelException, CoreException {

		// Set file encoding
		String encoding = "UTF-8".equals(vmEncoding) ? "Cp1252" : "UTF-8";
		IFile file = (IFile) this.encodingProject.findMember("src/testBug66898/Test.js");
		file.setCharset(encoding, null);
		String fileName = file.getName();
		IJavaScriptUnit cu = getCompilationUnit(file.getFullPath().toString());
		createFolder("/Encoding/src/tmp");
		IPackageFragment packFrag = getPackageFragment("Encoding", "src", "tmp");
		
		try {
			// Move file
			cu.move(packFrag, null, null, false, null);
			IJavaScriptUnit destSource = packFrag.getJavaScriptUnit(fileName);
			IFile destFile = (IFile) destSource.getUnderlyingResource();
			assertEquals("Moved file should keep encoding", encoding, destFile.getCharset());
	
			// Get source and compare with file contents
			compareContents(destSource, encoding);
			
			// Rename file
			destSource.rename("TestUTF8.js", false, null);
			IJavaScriptUnit renamedSource = packFrag.getJavaScriptUnit("TestUTF8.js");
			IFile renamedFile = (IFile) renamedSource.getUnderlyingResource();
			assertEquals("Moved file should keep encoding", encoding, renamedFile.getCharset());
			
			// Compare contents again
			compareContents(renamedSource, encoding);
		}
		finally {
			// Delete temporary folder
			//renamedFile.move(this.utf8File.getFullPath(), false, null);
			//assertEquals("Moved file should keep encoding", encoding, this.utf8File.getCharset());
			deleteFolder("/Encoding/src/tmp");
		}
	}	
	public void testBug66898b() throws JavaScriptModelException, CoreException {

		// Set file encoding
		final String encoding = "UTF-8".equals(vmEncoding) ? "Cp1252" : "UTF-8";
		final IFile file = (IFile) this.encodingProject.findMember("src/testBug66898b/Test.js");
		file.setCharset(encoding, null);
		final String fileName = file.getName();
		final IPackageFragment srcFolder = getPackageFragment("Encoding", "src", "testBug66898b");
		createFolder("/Encoding/src/tmp");
		final IPackageFragment tmpFolder = getPackageFragment("Encoding", "src", "tmp");
	
		try {
			// Copy file
			IWorkspaceRunnable copy = new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					IJavaScriptUnit cu = getCompilationUnit(file.getFullPath().toString());
					cu.copy(tmpFolder, null, null, true, null);
					cu.close(); // purge buffer contents from cache
					IJavaScriptUnit dest = tmpFolder.getJavaScriptUnit(fileName);
					IFile destFile = (IFile) dest.getUnderlyingResource();
					assertEquals("Copied file should keep encoding", encoding, destFile.getCharset());
			
					// Get source and compare with file contents
					compareContents(dest, encoding);
				}
			};
			JavaScriptCore.run(copy, null);
	
			// Rename file
			IWorkspaceRunnable rename = new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					IJavaScriptUnit cu = tmpFolder.getJavaScriptUnit(fileName);
					cu.rename("Renamed.js", true, null);
					cu.close(); // purge buffer contents from cache
					IJavaScriptUnit ren = tmpFolder.getJavaScriptUnit("Renamed.js");
					IFile renFile = (IFile) ren.getUnderlyingResource();
					assertEquals("Renamed file should keep encoding", encoding, renFile.getCharset());
			
					// Get source and compare with file contents
					compareContents(ren, encoding);
				}
			};
			JavaScriptCore.run(rename, null);
	
			// Move file
			IWorkspaceRunnable move = new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					IJavaScriptUnit cu = tmpFolder.getJavaScriptUnit("Renamed.js");
					cu.move(srcFolder, null, null, true, null);
					cu.close(); // purge buffer contents from cache
					IJavaScriptUnit moved = srcFolder.getJavaScriptUnit("Renamed.js");
					IFile movedFile = (IFile) moved.getUnderlyingResource();
					assertEquals("Renamed file should keep encoding", encoding, movedFile.getCharset());
			
					// Get source and compare with file contents
					compareContents(moved, encoding);
				}
			};
			JavaScriptCore.run(move, null);
		}
		finally {
			// Delete temporary file and folder
			IJavaScriptUnit cu = srcFolder.getJavaScriptUnit("Renamed.js");
			if (cu.exists()) cu.delete(true, null);
			deleteFolder("/Encoding/src/tmp");
		}
	}	

	/**
	 * Bug 70598: [Encoding] ArrayIndexOutOfBoundsException while testing BOM on *.txt files
	 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=70598"
	 */
	public void testBug70598() throws JavaScriptModelException, CoreException, IOException {

		// Create empty file
		IFile emptyFile = createFile("/Encoding/src/testUTF8BOM/Empty.js", new byte[0]);

		// Test read empty content using io file
		File file = new File(this.encodingProject.getLocation().toString(), emptyFile.getProjectRelativePath().toString());
		char[] fileContents = org.eclipse.wst.jsdt.internal.compiler.util.Util.getFileCharContent(file, "UTF-8");
		assertEquals("We should not get any character!", "", new String(fileContents));

		// Test read empty content using io file
		char[] ifileContents =Util.getResourceContentsAsCharArray(emptyFile, "UTF-8");
		assertEquals("We should not get any character!", "", new String(ifileContents));
		
		// Delete empty file
		deleteFile(file);
	}	

	/**
	 * Bug 110576: [encoding] Rename CU looses encoding for file which charset is determined by contents
	 * @see "http://bugs.eclipse.org/bugs/show_bug.cgi?id=110576"
	 */
	public void testBug110576() throws JavaScriptModelException, CoreException {

		String os = System.getProperty("osgi.os");
		if (!"win32".equals(os)) {
			System.out.println("Bug 110576 is not tested under "+os+" os...");
			return;
		}

		// Verify file UTF-8 BOM encoding
		IFile file = (IFile) this.encodingProject.findMember("src/testBug110576/Test.js");
		verifyUtf8BOM(file);

		String fileName = file.getName();
		IJavaScriptUnit testCU = getCompilationUnit(file.getFullPath().toString());
		createFolder("/Encoding/src/tmp");
		IPackageFragment tmpPackage = getPackageFragment("Encoding", "src", "tmp");
		
		try {
			// Copy file
			testCU.copy(tmpPackage, null, null, false, null);
			IJavaScriptUnit copiedCU = tmpPackage.getJavaScriptUnit(fileName);
			IFile copiedFile = (IFile) copiedCU.getUnderlyingResource();
			verifyUtf8BOM(copiedFile);
	
			// Get source and compare with file contents
			compareContents(copiedCU, "UTF-8", true/*BOM*/);

			// Rename file
			copiedCU.rename("TestUTF8.js", false, null);
			IJavaScriptUnit renamedCU = tmpPackage.getJavaScriptUnit("TestUTF8.js");
			IFile renamedFile = (IFile) renamedCU.getUnderlyingResource();
			verifyUtf8BOM(renamedFile);
			fileName = renamedFile.getName();
			
			// Compare contents again
			compareContents(renamedCU, "UTF-8", true/*BOM*/);

			// Move file
			createFolder("/Encoding/src/tmp/sub");
			IPackageFragment subPackage = getPackageFragment("Encoding", "src", "tmp.sub");
			renamedCU.move(subPackage, null, null, false, null);
			IJavaScriptUnit movedCU = subPackage.getJavaScriptUnit(fileName);
			IFile movedFile = (IFile) movedCU.getUnderlyingResource();
			verifyUtf8BOM(movedFile);
	
			// Get source and compare with file contents
			compareContents(movedCU, "UTF-8", true/*BOM*/);
		}
		finally {
			// Delete temporary folder
			//renamedFile.move(this.utf8File.getFullPath(), false, null);
			//assertEquals("Moved file should keep encoding", encoding, this.utf8File.getCharset());
			deleteFolder("/Encoding/src/tmp");
		}
	}

	private void verifyUtf8BOM(IFile file) throws CoreException {
		assertNull("File should not have any explicit charset", file.getCharset(false));
		IContentDescription contentDescription = file.getContentDescription();
		assertNotNull("File should have a content description", contentDescription);
		assertEquals("Content description charset should be UTF-8", "UTF-8", contentDescription.getCharset());
		assertNotNull("File should be UTF-8 BOM!", contentDescription.getProperty(IContentDescription.BYTE_ORDER_MARK));
	}	
}
