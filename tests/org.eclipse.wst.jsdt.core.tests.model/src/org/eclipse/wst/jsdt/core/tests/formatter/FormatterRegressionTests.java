/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.formatter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import junit.framework.Test;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.formatter.CodeFormatter;
import org.eclipse.wst.jsdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.ToolFactory;
import org.eclipse.wst.jsdt.core.tests.model.AbstractJavaModelTests;
import org.eclipse.wst.jsdt.core.tests.util.Util;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.formatter.DefaultCodeFormatter;
import org.eclipse.wst.jsdt.internal.formatter.DefaultCodeFormatterOptions;
import org.eclipse.text.edits.TextEdit;

public class FormatterRegressionTests extends AbstractJavaModelTests {
		
	public static final int UNKNOWN_KIND = 0;
	public static final String IN = "_in";
	public static final String OUT = "_out";
	public static final boolean DEBUG = false;
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private long time;
	
	static {
//		TESTS_NUMBERS = new int[] { 650 };
//		TESTS_RANGE = new int[] { 650, -1 };
	}
	public static Test suite() {
		return buildModelTestSuite(FormatterRegressionTests.class);
	}

	public FormatterRegressionTests(String name) {
		super(name);
	}
	
	/**
	 * Returns the OS path to the directory that contains this plugin.
	 */
	protected String getPluginDirectoryPath() {
		try {
			URL platformURL = Platform.getBundle("org.eclipse.wst.jsdt.core.tests.model").getEntry("/");
			return new File(FileLocator.toFileURL(platformURL).getFile()).getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getResource(String packageName, String resourceName) {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = workspaceRoot.findMember(new Path("/Formatter/" + packageName + "/" + resourceName));
		assertNotNull("No resource found", resource);
		return resource.getLocation().toOSString();
	}
	
	private String getZipEntryContents(String fileName, String zipEntryName) {
		ZipFile zipFile = null;
		BufferedInputStream inputStream  = null;
		try {
			zipFile = new ZipFile(fileName);
			ZipEntry zipEntry = zipFile.getEntry(zipEntryName);
			inputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
			return new String(org.eclipse.wst.jsdt.internal.compiler.util.Util.getInputStreamAsCharArray(inputStream, -1, null));
		} catch (IOException e) {
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (IOException e1) {
				// Do nothing
			}
		}
		return null;
	}
	
	public String getSourceWorkspacePath() {
		return getPluginDirectoryPath() +  java.io.File.separator + "workspace";
	}
	
	private String runFormatter(CodeFormatter codeFormatter, String source, int kind, int indentationLevel, int offset, int length, String lineSeparator) {
//		long time = System.currentTimeMillis();
		TextEdit edit = codeFormatter.format(kind, source, offset, length, indentationLevel, lineSeparator);//$NON-NLS-1$
//		System.out.println((System.currentTimeMillis() - time) + " ms");
		if (edit == null) return null;
//		System.out.println(edit.getChildrenSize() + " edits");
		String result = org.eclipse.wst.jsdt.internal.core.util.Util.editedString(source, edit);

		if (length == source.length()) {
//			time = System.currentTimeMillis();
			edit = codeFormatter.format(kind, result, 0, result.length(), indentationLevel, lineSeparator);//$NON-NLS-1$
//			System.out.println((System.currentTimeMillis() - time) + " ms");
			if (edit == null) return null;
//			assertEquals("Should not have edits", 0, edit.getChildren().length);
			final String result2 = org.eclipse.wst.jsdt.internal.core.util.Util.editedString(result, edit);
			if (!result.equals(result2)) {
				assertSourceEquals("Different reformatting", Util.convertToIndependantLineDelimiter(result), Util.convertToIndependantLineDelimiter(result2));
			}
		}
		return result;
	}

	/**
	 * Create project and set the jar placeholder.
	 */
	public void setUpSuite() throws Exception {
		// ensure autobuilding is turned off
		IWorkspaceDescription description = getWorkspace().getDescription();
		if (description.isAutoBuilding()) {
			description.setAutoBuilding(false);
			getWorkspace().setDescription(description);
		}
		setUpJavaProject("Formatter"); //$NON-NLS-1$
		if (DEBUG) {
			this.time = System.currentTimeMillis();
		}
	}	

	/**
	 * Reset the jar placeholder and delete project.
	 */
	public void tearDownSuite() throws Exception {
		this.deleteProject("Formatter"); //$NON-NLS-1$
		if (DEBUG) {
			System.out.println("Time spent = " + (System.currentTimeMillis() - this.time));//$NON-NLS-1$
		}
		super.tearDown();
	}	

	private String getIn(String compilationUnitName) {
		assertNotNull(compilationUnitName);
		int dotIndex = compilationUnitName.indexOf('.');
		assertTrue(dotIndex != -1);
		return compilationUnitName.substring(0, dotIndex) + IN + compilationUnitName.substring(dotIndex);
	}
	
	private String getOut(String compilationUnitName) {
		assertNotNull(compilationUnitName);
		int dotIndex = compilationUnitName.indexOf('.');
		assertTrue(dotIndex != -1);
		return compilationUnitName.substring(0, dotIndex) + OUT + compilationUnitName.substring(dotIndex);
	}

	private void assertLineEquals(String actualContents, String originalSource, String expectedContents, boolean checkNull) {
		if (actualContents == null) {
			assertTrue("actualContents is null", checkNull);
			assertEquals(expectedContents, originalSource);
			return;
		}
		assertSourceEquals("Different number of length", Util.convertToIndependantLineDelimiter(expectedContents), actualContents);
	}

	private void runTest(String packageName, String compilationUnitName) {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, packageName, compilationUnitName, CodeFormatter.K_JAVASCRIPT_UNIT, 0);
	}
	
	private void runTest(CodeFormatter codeFormatter, String packageName, String compilationUnitName) {
		runTest(codeFormatter, packageName, compilationUnitName, CodeFormatter.K_JAVASCRIPT_UNIT, 0);
	}

	private void runTest(String packageName, String compilationUnitName, int kind) {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, packageName, compilationUnitName, kind, 0);
	}

	private void runTest(CodeFormatter codeFormatter, String packageName, String compilationUnitName, int kind) {
		runTest(codeFormatter, packageName, compilationUnitName, kind, 0, false, 0, -1);
	}

	private void runTest(CodeFormatter codeFormatter, String packageName, String compilationUnitName, int kind, boolean checkNull) {
		runTest(codeFormatter, packageName, compilationUnitName, kind, 0, checkNull, 0, -1);
	}

	private void runTest(CodeFormatter codeFormatter, String packageName, String compilationUnitName, int kind, int indentationLevel) {
		runTest(codeFormatter, packageName, compilationUnitName, kind, indentationLevel, false, 0, -1);
	}
	private void runTest(CodeFormatter codeFormatter, String packageName, String compilationUnitName, int kind, int indentationLevel, boolean checkNull, int offset, int length) {
		runTest(codeFormatter, packageName, compilationUnitName, kind, indentationLevel, checkNull, offset, length, null);
	}
	
	private void runTest(String input, String output, CodeFormatter codeFormatter, int kind, int indentationLevel, boolean checkNull, int offset, int length, String lineSeparator) {
		String result;
		if (length == -1) {
			result = runFormatter(codeFormatter, input, kind, indentationLevel, offset, input.length(), lineSeparator);
		} else {
			result = runFormatter(codeFormatter, input, kind, indentationLevel, offset, length, lineSeparator);
		}
		assertLineEquals(result, input, output, checkNull);
	}

	private void runTest(String source, String expectedResult, CodeFormatter codeFormatter, int kind, int indentationLevel, boolean checkNull, int offset, int length) {
		String result;
		if (length == -1) {
			result = runFormatter(codeFormatter, source, kind, indentationLevel, offset, source.length(), null);
		} else {
			result = runFormatter(codeFormatter, source, kind, indentationLevel, offset, length, null);
		}
		assertLineEquals(result, source, expectedResult, checkNull);
	}
	
	private void runTest(CodeFormatter codeFormatter, String packageName, String compilationUnitName, int kind, int indentationLevel, boolean checkNull, int offset, int length, String lineSeparator) {
		try {
			IJavaScriptUnit sourceUnit = getCompilationUnit("Formatter" , "", packageName, getIn(compilationUnitName)); //$NON-NLS-1$ //$NON-NLS-2$
			String s = sourceUnit.getSource();
			assertNotNull(s);
			IJavaScriptUnit outputUnit = getCompilationUnit("Formatter" , "", packageName, getOut(compilationUnitName)); //$NON-NLS-1$ //$NON-NLS-2$
			assertNotNull(outputUnit);
			String result;
			if (length == -1) {
				result = runFormatter(codeFormatter, s, kind, indentationLevel, offset, s.length(), lineSeparator);
			} else {
				result = runFormatter(codeFormatter, s, kind, indentationLevel, offset, length, lineSeparator);
			}
			assertLineEquals(result, s, outputUnit.getSource(), checkNull);
		} catch (JavaScriptModelException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	String getSource(ASTNode astNode, char[] source) {
		String result = new String(CharOperation.subarray(source, astNode.getStartPosition() + 1, astNode.getStartPosition() + astNode.getLength() - 1));
		if (result.endsWith("\\n")) {
			return result.substring(0, result.length() - 2) + LINE_SEPARATOR;
		}
		return result;
	}

	public void _test000() {
		runTest("test002", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	   }

	
	public void test001() {
		runTest("test001", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}	
	public void _test002() {
		runTest("test002", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test003() {
		runTest("test003", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test004() {
		runTest("test004", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	public void _test005() {
		runTest("test005", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test006() {
		runTest("test006", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test007() {
		runTest("test007", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test008() {
		runTest("test008", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test009() {
		runTest("test009", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test010() {
		runTest("test010", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test011() {
		runTest("test011", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test012() {
		runTest("test012", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test013() {
		runTest("test013", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test014() {
		runTest("test014", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test015() {
		runTest("test015", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test016() {
		runTest("test016", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test017() {
		runTest("test017", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test018() {
		runTest("test018", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test019() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test019", "A_1.js");//$NON-NLS-1$ //$NON-NLS-2$

		preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test019", "A_2.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test020() {
		runTest("test020", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test021() {
		runTest("test021", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test022() {
		runTest("test022", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test023() {
		runTest("test023", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test024() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_then_statement_on_same_line = true;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test024", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test025() {
		runTest("test025", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test026() {
		runTest("test026", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test027() {
		runTest("test027", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test028() {
		runTest("test028", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test029() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_then_statement_on_same_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.compact_else_if = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter,"test029", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test030() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_then_statement_on_same_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test030", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test031() {
		runTest("test031", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test032() {
		runTest("test032", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test033() {
		runTest("test033", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test034() {
		runTest("test034", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test035() {
		runTest("test035", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test036() {
		runTest("test036", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test037() {
		runTest("test037", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test038() {
		runTest("test038", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test039() {
		runTest("test039", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test040() {
		runTest("test040", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test041() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_type_declaration = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test041", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test042() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_space_before_opening_brace_in_block = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test042", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test043() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_type_declaration = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test043", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test044() {
		runTest("test044", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test045() {
		runTest("test045", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void test046() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_assignment_operator = false;
		preferences.insert_space_before_assignment_operator = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test046", "A.js", CodeFormatter.K_EXPRESSION);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test047() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_assignment_operator = true;
		preferences.insert_space_before_assignment_operator = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test047", "A.js", CodeFormatter.K_STATEMENTS, 2);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test048() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_assignment_operator = true;
		preferences.insert_space_before_assignment_operator = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test048", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test049() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_assignment_operator = true;
		preferences.insert_space_before_assignment_operator = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test049", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test050() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_binary_operator = false;
		preferences.insert_space_before_unary_operator = false;
		preferences.insert_space_after_unary_operator = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test050", "A.js", CodeFormatter.K_EXPRESSION);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test051() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_assignment_operator = true;
		preferences.insert_space_before_assignment_operator = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test051", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test052() {
		runTest("test052", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test053() {
		runTest("test053", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test054() {
		runTest("test054", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test055() {
		runTest("test055", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test056() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_then_statement_on_same_line = true;
		preferences.keep_else_statement_on_same_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test056", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test057() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		assertEquals(false, DefaultCodeFormatterConstants.getForceWrapping((String)options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER)));
		assertEquals(DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.getWrappingStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER)));
		assertEquals(DefaultCodeFormatterConstants.INDENT_DEFAULT, DefaultCodeFormatterConstants.getIndentStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER)));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.align_type_members_on_columns = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test057", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test058() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		assertEquals(false, DefaultCodeFormatterConstants.getForceWrapping((String)options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER)));
		assertEquals(DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.getWrappingStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER)));
		assertEquals(DefaultCodeFormatterConstants.INDENT_DEFAULT, DefaultCodeFormatterConstants.getIndentStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER)));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.align_type_members_on_columns = true;
		preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.blank_lines_between_import_groups = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test058", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test059() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.align_type_members_on_columns = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test059", "Parser.js");//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test060() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.align_type_members_on_columns = false;
		preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.blank_lines_between_import_groups = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		//long time = System.currentTimeMillis();
		runTest(codeFormatter, "test060", "Parser.js");//$NON-NLS-1$ //$NON-NLS-2$
	}		

	public void _test061() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.blank_lines_between_import_groups = 0;
		preferences.align_type_members_on_columns = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test061", "Parser.js");//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void test062() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_assignment_operator = true;
		preferences.insert_space_before_assignment_operator = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test062", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test063() {
		runTest("test063", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test064() {
		runTest("test064", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// Line break inside an array initializer (line comment)	
	public void _test065() {
		runTest("test065", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test066() {
		runTest("test066", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test067() {
		runTest("test067", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}	

	// bug 3181
	public void _test068() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_ALLOCATION_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test068", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 3327
	public void _test069() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.blank_lines_before_first_class_body_declaration = 1;
		preferences.blank_lines_before_method = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test069", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// 5691
	public void _test070() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test070", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test071() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.indent_body_declarations_compare_to_type_header = false;
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.align_type_members_on_columns = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test071", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// bug 7224
	public void _test072() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test072", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 7439
	public void _test073() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_then_statement_on_same_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test073", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 12321
	public void _test074() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.insert_new_line_before_catch_in_try_statement = true;
		preferences.insert_new_line_before_else_in_if_statement = true;
		preferences.insert_new_line_before_finally_in_try_statement = true;
		preferences.insert_new_line_before_while_in_do_statement = true;
		preferences.keep_simple_if_on_one_line = false;
		preferences.keep_then_statement_on_same_line = false;
		preferences.keep_else_statement_on_same_line = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test074", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 14659
	public void _test075() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.page_width = 57;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test075", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 16231
	public void _test076() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test076", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 16233
	public void _test077() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test077", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 17349
	public void _test078() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.NEXT_LINE;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test078", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 19811
	public void _test079() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test079", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 19811
	public void _test080() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.continuation_indentation = 2;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test080", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 19811
	public void _test081() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test081", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 19999
	public void _test082() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 2;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test082", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// bug 20721
	public void _test083() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test083", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 21943
	public void _test084() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.insert_space_before_opening_paren_in_if = false;
		preferences.insert_space_before_opening_paren_in_for = false;
		preferences.insert_space_before_opening_paren_in_while = false;
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_then_statement_on_same_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test084", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 21943
	public void _test085() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.insert_space_before_opening_paren_in_if = true;
		preferences.insert_space_before_opening_paren_in_for = true;
		preferences.insert_space_before_opening_paren_in_while = true;
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_then_statement_on_same_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test085", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 22313
	public void _test086() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_before_catch_in_try_statement = true;
		preferences.insert_new_line_before_else_in_if_statement = true;
		preferences.insert_new_line_before_finally_in_try_statement = true;
		preferences.insert_new_line_before_while_in_do_statement = true;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_method_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.insert_space_before_binary_operator = false;
		preferences.insert_space_after_binary_operator = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test086", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 23144
	public void test087() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.keep_simple_if_on_one_line = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test087", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 23144
	public void test088() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.keep_simple_if_on_one_line = false;
		preferences.keep_guardian_clause_on_one_line = false;
		preferences.keep_then_statement_on_same_line = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test088", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// bug 24200
	public void test089() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_opening_paren_in_parenthesized_expression = true;
		preferences.insert_space_before_closing_paren_in_parenthesized_expression = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test089", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 24200
	public void test090() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_opening_bracket_in_array_reference = true;
		preferences.insert_space_before_closing_bracket_in_array_reference = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test090", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 25559
	public void _test091() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_assignment_operator = false;
		preferences.insert_space_before_assignment_operator = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test091", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 25559
	public void test092() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_binary_operator = false;
		preferences.insert_space_before_binary_operator = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test092", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 25559
	public void _test093() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_closing_paren_in_cast = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test093", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 25559
	public void _test094() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_assignment_operator = false;
		preferences.insert_space_before_assignment_operator = false;
		preferences.insert_space_after_comma_in_method_invocation_arguments = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test094", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// bug 27196
	public void test095() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED;
		preferences.indent_statements_compare_to_block = false;
		preferences.indent_statements_compare_to_body = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test095", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 28098
	public void _test096() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test096", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 34897
	public void _test097() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_opening_paren_in_method_invocation = true;
		preferences.insert_space_before_closing_paren_in_method_invocation = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test097", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 35173
	public void _test098() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.brace_position_for_anonymous_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_method_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test098", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 35433
	public void _test099() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_opening_paren_in_method_declaration = true;
		preferences.insert_space_before_opening_paren_in_for = true;
		preferences.insert_space_after_semicolon_in_for = false;
		preferences.put_empty_statement_on_new_line = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test099", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void test100_B() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		//setup
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_assignment_operator = false;
		preferences.insert_space_after_assignment_operator = false;
		preferences.insert_space_after_comma_in_array_initializer = false;
		preferences.number_of_empty_lines_to_preserve = 1;
		
		//test
		preferences.insert_space_after_opening_brace_in_array_initializer = true;
		
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test100", "B.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void test100_C() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		//setup
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_assignment_operator = false;
		preferences.insert_space_after_assignment_operator = false;
		preferences.insert_space_after_comma_in_array_initializer = false;
		preferences.number_of_empty_lines_to_preserve = 1;
		
		//test
		preferences.insert_space_before_closing_brace_in_array_initializer = true;
		
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test100", "C.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void test100_D() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		//setup
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_assignment_operator = false;
		preferences.insert_space_after_assignment_operator = false;
		preferences.insert_space_after_comma_in_array_initializer = false;
		preferences.number_of_empty_lines_to_preserve = 1;
		
		//test
		preferences.insert_space_before_comma_in_array_initializer = true;
		
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test100", "D.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void test100_E() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		//setup
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_assignment_operator = false;
		preferences.insert_space_after_assignment_operator = false;
		preferences.number_of_empty_lines_to_preserve = 1;
		
		//test
		preferences.insert_space_after_comma_in_array_initializer = true;
		
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test100", "E.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void test100_F() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		//setup
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_assignment_operator = false;
		preferences.insert_space_after_assignment_operator = false;
		preferences.insert_space_after_comma_in_array_initializer = false;
		preferences.number_of_empty_lines_to_preserve = 1;
		
		//test
		preferences.insert_space_between_empty_braces_in_array_initializer = true;
		
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test100", "F.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void test100_G() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		//setup
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_assignment_operator = false;
		preferences.insert_space_after_assignment_operator = false;
		preferences.number_of_empty_lines_to_preserve = 1;
		
		//test
		preferences.insert_space_after_opening_brace_in_array_initializer = true;
		preferences.insert_space_before_closing_brace_in_array_initializer = true;
		preferences.insert_space_before_comma_in_array_initializer = true;
		preferences.insert_space_after_comma_in_array_initializer = true;
		preferences.insert_space_between_empty_braces_in_array_initializer = true;
		
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test100", "G.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// bug 36832
	public void _test101() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test101", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 37057
	public void _test102() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 1;
		preferences.line_separator = "\n";//$NON-NLS-1$
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test102", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 37106
	public void test103() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test103", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 37657
	public void test104() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_opening_paren_in_if = true;
		preferences.insert_space_before_closing_paren_in_if = true;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED;
		preferences.insert_new_line_before_catch_in_try_statement = true;
		preferences.insert_new_line_before_else_in_if_statement = true;
		preferences.insert_new_line_before_finally_in_try_statement = true;
		preferences.insert_new_line_before_while_in_do_statement = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test104", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 38151
	public void _test105() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.brace_position_for_method_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test105", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 39603
	public void _test106() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test106", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// bug 39607
	public void _test107() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.keep_then_statement_on_same_line = false;
		preferences.keep_simple_if_on_one_line = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test107", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// bug 40777
	public void _test108() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test108", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test109() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.blank_lines_before_package = 2;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test109", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test110() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.blank_lines_before_package = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test110", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test111() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.blank_lines_after_package = 1;
		preferences.blank_lines_before_first_class_body_declaration = 1;
		preferences.blank_lines_before_new_chunk = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test111", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test112() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.blank_lines_after_package = 1;
		preferences.blank_lines_before_first_class_body_declaration = 1;
		preferences.blank_lines_before_new_chunk = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test112", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test113() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test113", "A.js");//$NON-NLS-1$ //$NON-NLS-2
	}

	// bug 14659
	public void _test114() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		assertEquals(false, DefaultCodeFormatterConstants.getForceWrapping((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION)));
		assertEquals(DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.getWrappingStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION)));
		assertEquals(DefaultCodeFormatterConstants.INDENT_ON_COLUMN, DefaultCodeFormatterConstants.getIndentStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION)));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.page_width = 57;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test114", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 14659
	public void _test115() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.page_width = 57;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test115", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// bug 14659
	public void _test116() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.page_width = 57;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test116", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// JDT/UI tests
	public void _test117() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test117", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// JDT/UI tests
	public void _test118() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test118", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// JDT/UI tests
	public void _test119() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test119", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// JDT/UI tests
	public void _test120() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test120", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// JDT/UI tests
	public void test121() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test121", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// probing statements
	public void test122() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test122", "A.js", CodeFormatter.K_UNKNOWN);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// probing compilation unit
	public void _test123() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test123", "A.js", CodeFormatter.K_UNKNOWN);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	// probing class body declarations
	public void _test124() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.page_width = 57;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test124", "A.js", CodeFormatter.K_UNKNOWN);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// probing expression
	public void test125() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_binary_operator = false;
		preferences.insert_space_before_unary_operator = false;
		preferences.insert_space_after_unary_operator = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test125", "A.js", CodeFormatter.K_UNKNOWN);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// probing unrecognized source
	public void _test126() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test126", "A.js", CodeFormatter.K_UNKNOWN, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// probing unrecognized source
	public void _test127() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test127", "A.js", CodeFormatter.K_UNKNOWN);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// probing unrecognized source
	public void test128() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test128", "A.js", CodeFormatter.K_UNKNOWN, true);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test129() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test129", "A.js", CodeFormatter.K_STATEMENTS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test130() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test130", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test131() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test131", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test132() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test132", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test133() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.blank_lines_between_import_groups = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test133", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test134() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test134", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test135() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test135", "A.js", CodeFormatter.K_STATEMENTS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test136() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test136", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test137() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test137", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test138() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test138", "A.js", CodeFormatter.K_STATEMENTS, 2, true, 8, 37);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test139() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test139", "A.js", CodeFormatter.K_STATEMENTS, 0, true, 0, 5);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test140() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test140", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test141() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.indent_switchstatements_compare_to_cases = false;
		preferences.indent_switchstatements_compare_to_switch = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test141", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test142() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test142", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, 1);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test143() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test143", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, 1);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test144() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test144", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test145() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test145", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test146() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test146", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test147() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_assignment_operator = false;
		preferences.number_of_empty_lines_to_preserve = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test147", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test148() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test148", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test149() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test149", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test150() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test150", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test151() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test151", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test152() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test152", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test153() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.align_type_members_on_columns = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test153", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test154() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test154", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	public void _test155() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getJavaConventionsSettings());
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test155", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	//https://bugs.eclipse.org/bugs/show_bug.cgi?id=44036
	public void test156() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getJavaConventionsSettings());
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test156", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void test157() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test157", "A.js", CodeFormatter.K_STATEMENTS, 0, true, 11, 7);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test158() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test158", "A.js", CodeFormatter.K_STATEMENTS, 0, true, 11, 8);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test159() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test159", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44481
	 */
	public void _test160() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.insert_new_line_before_catch_in_try_statement = false;
		preferences.insert_new_line_before_else_in_if_statement = false;
		preferences.insert_new_line_before_finally_in_try_statement = false;
		preferences.insert_new_line_before_while_in_do_statement = false;
		preferences.compact_else_if = true;
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test160", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44481
	 */
	public void _test161() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.insert_new_line_before_catch_in_try_statement = false;
		preferences.insert_new_line_before_else_in_if_statement = false;
		preferences.insert_new_line_before_finally_in_try_statement = false;
		preferences.insert_new_line_before_while_in_do_statement = false;
		preferences.compact_else_if = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test161", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44481
	 */
	public void _test162() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.insert_new_line_before_catch_in_try_statement = true;
		preferences.insert_new_line_before_else_in_if_statement = true;
		preferences.insert_new_line_before_finally_in_try_statement = true;
		preferences.insert_new_line_before_while_in_do_statement = true;
		preferences.compact_else_if = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test162", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44481
	 */
	public void _test163() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.insert_new_line_before_catch_in_try_statement = true;
		preferences.insert_new_line_before_else_in_if_statement = true;
		preferences.insert_new_line_before_finally_in_try_statement = true;
		preferences.insert_new_line_before_while_in_do_statement = true;
		preferences.compact_else_if = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test163", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44493
	 */
	public void _test164() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test164", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test165() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test165", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44546
	 */
	public void _test166() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test166", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44503
	 */
	public void _test167() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test167", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44503
	 */
	public void _test169() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test169", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44503
	 */
	public void _test170() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test170", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44576
	 */
	public void _test171() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.brace_position_for_anonymous_type_declaration = DefaultCodeFormatterConstants.END_OF_LINE;
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.END_OF_LINE;
		preferences.brace_position_for_method_declaration = DefaultCodeFormatterConstants.END_OF_LINE;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.END_OF_LINE;
		preferences.brace_position_for_switch = DefaultCodeFormatterConstants.END_OF_LINE;
		preferences.compact_else_if = false;
		preferences.insert_new_line_before_catch_in_try_statement = true;
		preferences.insert_new_line_before_else_in_if_statement = true;
		preferences.insert_new_line_before_finally_in_try_statement = true;
		preferences.insert_new_line_before_while_in_do_statement = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test171", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44576
	 */
	public void _test172() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.brace_position_for_anonymous_type_declaration = DefaultCodeFormatterConstants.END_OF_LINE;
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.END_OF_LINE;
		preferences.brace_position_for_method_declaration = DefaultCodeFormatterConstants.END_OF_LINE;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.END_OF_LINE;
		preferences.brace_position_for_switch = DefaultCodeFormatterConstants.END_OF_LINE;
		preferences.compact_else_if = false;
		preferences.insert_new_line_before_catch_in_try_statement = true;
		preferences.insert_new_line_before_else_in_if_statement = true;
		preferences.insert_new_line_before_finally_in_try_statement = true;
		preferences.insert_new_line_before_while_in_do_statement = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test172", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44570
	 */
	public void _test173() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test173", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44570
	 */
	public void _test174() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test174", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44570
	 */
	public void _test175() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test175", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44570
	 */
	public void _test176() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_new_line_in_empty_method_body = true;
		preferences.insert_new_line_in_empty_block = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test176", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44570
	 */
	public void _test177() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test177", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44570
	 */
	public void _test178() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = true;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test178", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44570
	 */
	public void _test179() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = true;
		preferences.insert_new_line_in_empty_type_declaration = true;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test179", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44570
	 */
	public void _test180() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test180", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44651
	 */
	public void _test181() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test181", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44651
	 */
	public void _test182() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test182", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44653
	 */
	public void _test183() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test183", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44653
	 */
	public void _test184() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test184", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44653
	 */
	public void _test185() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test185", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 */
	public void _test186() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test186", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44839
	 */
	public void _test187() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.blank_lines_between_import_groups = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test187", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44839
	 */
	public void _test188() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test188", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44839
	 */
	public void _test189() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test189", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 */
	public void _test190() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test190", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 */
	public void _test191() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test191", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test192() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.align_type_members_on_columns = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test192", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test193() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test193", "A.js", CodeFormatter.K_STATEMENTS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test194() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test194", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test195() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test195", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test196() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test196", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test197() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test197", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test198() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test198", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test199() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test199", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test201() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test201", "A.js", CodeFormatter.K_STATEMENTS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * TODO Fix multi local declaration alignment
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44909
	 */
	public void _test202() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test202", "A.js", CodeFormatter.K_STATEMENTS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * TODO Fix multi local declaration alignment
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44909
	 */
	public void _test203() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test203", "A.js", CodeFormatter.K_STATEMENTS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test204() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test204", "A.js", CodeFormatter.K_STATEMENTS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test205() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");//$NON-NLS-1$
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test205", "A.js", CodeFormatter.K_STATEMENTS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test206() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test206", "A.js", CodeFormatter.K_STATEMENTS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test207() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test207", "A.js", CodeFormatter.K_STATEMENTS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test208() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test208", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test209() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");//$NON-NLS-1$
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test209", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, true);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test210() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");//$NON-NLS-1$
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test210", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test211() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");//$NON-NLS-1$
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test211", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, 1);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test212() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test212", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test213() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test213", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test214() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test214", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test215() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test215", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test216() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test216", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test217() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test217", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test218() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test218", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, 1);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test219() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test219", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, 1);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test220() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test220", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, 1);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test221() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test221", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test222() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test222", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	public void _test223() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test223", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	public void _test224() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test224", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test225() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test225", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	public void _test226() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test226", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test227() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test227", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	public void _test228() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test228", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test229() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test229", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	public void _test230() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test230", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test231() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test231", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test232() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test232", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test233() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test233", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, 1);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test234() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test234", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test235() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test235", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test236() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test236", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test237() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test237", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test238() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test238", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test239() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test239", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test240() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test240", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test241() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test241", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test242() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test242", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test243() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test243", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test244() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test244", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test245() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test245", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test246() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test246", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test247() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test247", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test248() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test248", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test249() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test249", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test250() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test250", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test251() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test251", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test252() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test252", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test253() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test253", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test254() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test254", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test255() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test255", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test256() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test256", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void test257() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test257", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test258() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test258", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test259() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test259", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test260() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test260", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test261() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test261", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test262() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test262", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test263() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test263", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test264() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test264", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test265() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test265", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void test266() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test266", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void test267() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test267", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test268() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test268", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test269() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test269", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test270() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test270", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test271() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test271", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test272() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK_IN_CASE, DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test272", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test273() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK_IN_CASE, DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test273", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test274() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test274", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	public void _test275() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test275", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test276() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test276", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test277() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test277", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test278() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test278", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test279() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test279", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test280() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test280", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test281() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test281", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test282() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test282", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test283() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test283", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test284() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test284", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test285() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test285", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test286() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test286", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test287() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test287", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test288() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test288", "A.js", CodeFormatter.K_STATEMENTS, 1);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test289() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test289", "A.js", CodeFormatter.K_STATEMENTS, 1);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test290() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test290", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test291() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test291", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test292() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test292", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test293() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test293", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test294() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test294", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test295() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test295", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test296() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test296", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test297() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.SPACE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "100");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test297", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test298() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "80");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test298", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test299() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "80");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test299", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test300() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test300", "A.js", CodeFormatter.K_EXPRESSION, 2);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test301() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_CLOSING_PAREN_IN_CAST, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test301", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test302() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test302", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test303() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.indent_switchstatements_compare_to_cases = true;
		preferences.indent_switchstatements_compare_to_switch = true;
		preferences.indent_breaks_compare_to_cases = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test303", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test304() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.indent_switchstatements_compare_to_cases = true;
		preferences.indent_switchstatements_compare_to_switch = true;
		preferences.indent_breaks_compare_to_cases = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test304", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test305() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.indent_switchstatements_compare_to_cases = false;
		preferences.indent_switchstatements_compare_to_switch = true;
		preferences.indent_breaks_compare_to_cases = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test305", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test306() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.indent_switchstatements_compare_to_cases = true;
		preferences.indent_switchstatements_compare_to_switch = true;
		preferences.indent_breaks_compare_to_cases = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test306", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test307() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.indent_switchstatements_compare_to_cases = true;
		preferences.indent_switchstatements_compare_to_switch = true;
		preferences.indent_breaks_compare_to_cases = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test307", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test308() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.indent_switchstatements_compare_to_cases = false;
		preferences.indent_switchstatements_compare_to_switch = false;
		preferences.indent_breaks_compare_to_cases = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test308", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void test309() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.indent_switchstatements_compare_to_cases = false;
		preferences.indent_switchstatements_compare_to_switch = false;
		preferences.indent_breaks_compare_to_cases = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test309", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test310() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test310", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test311() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test311", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test312() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test312", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test313() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test313", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test314() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test314", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test315() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		String source = "public final void addDefinitelyAssignedVariables(Scope scope, int initStateIndex) {\n" + 
				"/*\n" + 
				"	\n" + 
				"*/\n" + 
				"}";
		String expectedResult = "public final void addDefinitelyAssignedVariables(Scope scope, int initStateIndex) {\r\n" + 
				"	/*\r\n" + 
				"	 \r\n" + 
				"	 */\r\n" + 
				"}";
		runTest(source, expectedResult, codeFormatter, CodeFormatter.K_CLASS_BODY_DECLARATIONS, 0, false, 0, -1);
	}

	public void _test316() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		String source = "public final void addDefinitelyAssignedVariables(Scope scope, int initStateIndex) {\r" + 
				"/*\r" + 
				"	\r" + 
				"*/\r" + 
				"}";
		String expectedResult = "public final void addDefinitelyAssignedVariables(Scope scope, int initStateIndex) {\r\n" + 
				"	/*\r\n" + 
				"	 \r\n" + 
				"	 */\r\n" + 
				"}";
		runTest(source, expectedResult, codeFormatter, CodeFormatter.K_CLASS_BODY_DECLARATIONS, 0, false, 0, -1);
	}
	
	public void _test317() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.line_separator = "\n";
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		String source = "public final void addDefinitelyAssignedVariables(Scope scope, int initStateIndex) {\r\n" + 
				"/*\r\n" + 
				"	\r\n" + 
				"*/\r\n" + 
				"}";
		String expectedResult = "public final void addDefinitelyAssignedVariables(Scope scope, int initStateIndex) {\n" + 
				"	/*\n" + 
				"	 \n" + 
				"	 */\n" + 
				"}";
		runTest(source, expectedResult, codeFormatter, CodeFormatter.K_CLASS_BODY_DECLARATIONS, 0, false, 0, -1);
	}
	
	public void _test318() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.line_separator = "\r";
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		String source = "public final void addDefinitelyAssignedVariables(Scope scope, int initStateIndex) {\r" + 
				"/*\r" + 
				"	\r" + 
				"*/\r" + 
				"}";
		String expectedResult = "public final void addDefinitelyAssignedVariables(Scope scope, int initStateIndex) {\r" + 
				"	/*\r" + 
				"	 \r" + 
				"	 */\r" + 
				"}";
		runTest(source, expectedResult, codeFormatter, CodeFormatter.K_CLASS_BODY_DECLARATIONS, 0, false, 0, -1);
	}

	public void _test319() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test319", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test320() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test320", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test321() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test321", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test322() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test322", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test323() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_anonymous_type_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = false;
		preferences.insert_new_line_in_empty_method_body = false;
		preferences.insert_new_line_in_empty_block = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test323", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=45141
	 */
	public void _test324() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test324", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=45220
	 */
	public void _test325() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test325", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=45465
	 */
	public void _test326() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test326", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=45508
	 */
	public void _test327() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test327", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=22073
	 */
	public void _test328() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test328", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=29473
	 */
	public void _test329() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test329", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=27249
	 */
	public void _test330() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.keep_empty_array_initializer_on_one_line = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test330", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=23709
	 */
	public void _test331() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test331", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=23709
	 */
	public void _test332() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test332", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=45968
	 */
	public void _test333() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 5;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test333", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46058
	 */
	public void _test334() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.put_empty_statement_on_new_line = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test334", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46033
	 */
	public void _test335() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test335", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46023
	 */
	public void _test336() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test336", "A.js", CodeFormatter.K_STATEMENTS, 8);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46150
	 */
	public void _test337() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test337", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46686
	 */
	public void _test338() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test338", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46686
	 */
	public void _test339() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test339", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46686
	 */
	public void _test340() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test340", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46689
	 */
	public void _test341() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_unary_operator = false;
		preferences.insert_space_after_assignment_operator = false;
		preferences.insert_space_after_binary_operator = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test341", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46690
	 */
	public void _test342() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_comma_in_multiple_local_declarations = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test342", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46690
	 */
	public void _test343() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_comma_in_multiple_field_declarations = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test343", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46690
	 */
	public void _test344() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_comma_in_multiple_field_declarations = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test344", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=46690
	 */
	public void _test345() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_comma_in_multiple_local_declarations = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test345", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44493
	 */
	public void _test347() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_THROWS_CLAUSE_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		assertEquals(DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.getWrappingStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_THROWS_CLAUSE_IN_METHOD_DECLARATION)));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.blank_lines_before_method = 1;
		preferences.blank_lines_before_first_class_body_declaration = 1;
		preferences.insert_new_line_in_empty_method_body = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test347", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44493
	 */
	public void _test348() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_THROWS_CLAUSE_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.blank_lines_before_method = 1;
		preferences.blank_lines_before_first_class_body_declaration = 1;
		preferences.insert_new_line_in_empty_method_body = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test348", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44493
	 */
	public void _test349() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getJavaConventionsSettings());
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
        preferences.blank_lines_before_first_class_body_declaration = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test349", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44653
	 */
	public void _test350() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test350", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44765
	 */
	public void test351() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test351", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44653
	 */
	public void test352() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getJavaConventionsSettings());
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test352", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44642
	 */
	public void test353() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test353", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47799
	 */
	public void _test354() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_PUT_EMPTY_STATEMENT_ON_NEW_LINE, DefaultCodeFormatterConstants.FALSE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test354", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47799
	 */
	public void _test355() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_PUT_EMPTY_STATEMENT_ON_NEW_LINE, DefaultCodeFormatterConstants.TRUE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test355", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47800
	 */
	public void _test356() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_BINARY_EXPRESSION, DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test356", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47801
	 */
	public void test357() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_PREFIX_OPERATOR, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test357", "A.js", CodeFormatter.K_EXPRESSION);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47801
	 */
	public void test358() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_PREFIX_OPERATOR, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test358", "A.js", CodeFormatter.K_EXPRESSION);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47811
	 */
	public void _test359() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test359", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47811
	 */
	public void _test360() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "2");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test360", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47811
	 */
	public void _test361() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test361", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47802
	 */
	public void _test362() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.page_width = 57;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test362", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47800
	 */
	public void _test363() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test363", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47986
	 */
	public void _test364() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_comma_in_for_inits = false;
		preferences.insert_space_after_comma_in_for_increments = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test364", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47986
	 */
	public void _test365() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_comma_in_for_inits = false;
		preferences.insert_space_after_comma_in_for_increments = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test365", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47986
	 */
	public void _test366() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_comma_in_for_inits = true;
		preferences.insert_space_before_comma_in_for_inits = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test366", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47986
	 */
	public void _test367() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_after_comma_in_for_inits = true;
		preferences.insert_space_before_comma_in_for_inits = true;
		preferences.insert_space_after_comma_in_multiple_local_declarations = false;
		preferences.insert_space_before_comma_in_multiple_local_declarations = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test367", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47918
	 */
	public void _test368() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_METHOD, "0");
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test368", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47918
	 */
	public void _test369() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "0");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_METHOD, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_FIRST_CLASS_BODY_DECLARATION, "1");
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test369", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47918
	 */
	public void _test370() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_METHOD, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_FIRST_CLASS_BODY_DECLARATION, "1");
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test370", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47918
	 */
	public void _test371() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_METHOD, "0");
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test371", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47918
	 */
	public void _test372() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_METHOD, "0");
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test372", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47918
	 */
	public void _test373() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "0");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_METHOD, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_FIRST_CLASS_BODY_DECLARATION, "1");
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test373", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44813
	 */
	public void _test374() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ARRAY_INITIALIZER, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_CONTINUATION_INDENTATION_FOR_ARRAY_INITIALIZER, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_COMMA_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AFTER_OPENING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test374", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44813
	 */
	public void _test375() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ARRAY_INITIALIZER, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_CONTINUATION_INDENTATION_FOR_ARRAY_INITIALIZER, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_COMMA_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AFTER_OPENING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test375", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44813
	 */
	public void _test376() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ARRAY_INITIALIZER, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_CONTINUATION_INDENTATION_FOR_ARRAY_INITIALIZER, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_COMMA_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AFTER_OPENING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test376", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44813
	 */
	public void _test377() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ARRAY_INITIALIZER, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_CONTINUATION_INDENTATION_FOR_ARRAY_INITIALIZER, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_COMMA_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AFTER_OPENING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test377", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44813
	 */
	public void _test378() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ARRAY_INITIALIZER, DefaultCodeFormatterConstants.END_OF_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_CONTINUATION_INDENTATION_FOR_ARRAY_INITIALIZER, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_COMMA_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AFTER_OPENING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test378", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47997
	 */
	public void _test379() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ARRAY_INITIALIZER, DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED);
		options.put(DefaultCodeFormatterConstants.FORMATTER_CONTINUATION_INDENTATION_FOR_ARRAY_INITIALIZER, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_COMMA_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AFTER_OPENING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test379", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47997
	 */
	public void _test380() {
		Hashtable options = new Hashtable();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_FIRST_CLASS_BODY_DECLARATION, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.SPACE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "100");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test380", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=47997
	 */
	public void _test381() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "0");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_METHOD, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_FIRST_CLASS_BODY_DECLARATION, "0");
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test381", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48131
	 */
	public void _test382() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(new DefaultCodeFormatterOptions(options));
		runTest(codeFormatter, "test382", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48141
	 */
	public void _test383() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		preferences.blank_lines_before_first_class_body_declaration = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test383", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48143
	 */
	public void _test384() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_CONDITIONAL_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test384", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
		
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48143
	 */
	public void _test385() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_CONDITIONAL_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_NEXT_SHIFTED, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		assertEquals(DefaultCodeFormatterConstants.WRAP_NEXT_SHIFTED, DefaultCodeFormatterConstants.getWrappingStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_CONDITIONAL_EXPRESSION)));
		assertTrue(DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE != DefaultCodeFormatterConstants.getWrappingStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_CONDITIONAL_EXPRESSION)));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_before_first_class_body_declaration = 0;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test385", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48143
	 */
	public void _test386() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_CONDITIONAL_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_before_first_class_body_declaration = 0;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test386", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48143
	 */
	public void _test387() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_CONDITIONAL_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
//		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "40");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.page_width = 40;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test387", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48167
	 */
	public void _test388() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		preferences.brace_position_for_array_initializer = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.page_width = 40;
		preferences.insert_new_line_after_opening_brace_in_array_initializer = true;
		preferences.insert_new_line_before_closing_brace_in_array_initializer = true;
		preferences.blank_lines_before_first_class_body_declaration = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test388", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48167
	 */
	public void _test389() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.continuation_indentation_for_array_initializer = 1;
		preferences.brace_position_for_array_initializer = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.page_width = 40;
		preferences.insert_new_line_after_opening_brace_in_array_initializer = true;
		preferences.insert_new_line_before_closing_brace_in_array_initializer = true;
		preferences.blank_lines_before_first_class_body_declaration = 0;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test389", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48167
	 */
	public void _test390() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT_FIRST_BREAK, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.continuation_indentation_for_array_initializer = 1;
		preferences.brace_position_for_array_initializer = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.page_width = 40;
		preferences.insert_new_line_after_opening_brace_in_array_initializer = true;
		preferences.insert_new_line_before_closing_brace_in_array_initializer = true;
		preferences.blank_lines_before_first_class_body_declaration = 0;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test390", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48167
	 */
	public void _test391() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_BY_ONE));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_before_first_class_body_declaration = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.continuation_indentation_for_array_initializer = 3;
		preferences.brace_position_for_array_initializer = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.page_width = 40;
		preferences.insert_new_line_after_opening_brace_in_array_initializer = true;
		preferences.insert_new_line_before_closing_brace_in_array_initializer = true;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test391", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48167
	 */
	public void _test392() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_BY_ONE));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_before_first_class_body_declaration = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.continuation_indentation_for_array_initializer = 3;
		preferences.brace_position_for_array_initializer = DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED;
		preferences.page_width = 40;
		preferences.insert_new_line_after_opening_brace_in_array_initializer = true;
		preferences.insert_new_line_before_closing_brace_in_array_initializer = true;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test392", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48167
	 */
	public void _test393() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
		preferences.blank_lines_before_first_class_body_declaration = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.continuation_indentation_for_array_initializer = 1;
		preferences.brace_position_for_array_initializer = DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED;
		preferences.page_width = 40;
		preferences.insert_new_line_after_opening_brace_in_array_initializer = true;
		preferences.insert_new_line_before_closing_brace_in_array_initializer = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test393", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=48404
	 */
	public void _test394() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test394", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49318
	 */
	public void _test395() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.insert_space_before_opening_paren_in_method_declaration = true;
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test395", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49243
	 */
	public void _test396() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.insert_space_before_semicolon_in_for = true;
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test396", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49187
	 */
	public void _test397() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_before_package = 2;
		preferences.blank_lines_after_package = 0;
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test397", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49187
	 */
	public void _test398() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.blank_lines_before_package = 0;
		preferences.blank_lines_after_package = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test398", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49187
	 */
	public void _test399() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_before_package = 1;
		preferences.blank_lines_after_package = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test399", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49187
	 */
	public void _test400() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_PACKAGE, "2");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_PACKAGE, "2");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test400", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49361
	 */
	public void _test401() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_BRACES_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_KEEP_EMPTY_ARRAY_INITIALIZER_ON_ONE_LINE, DefaultCodeFormatterConstants.TRUE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test401", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49361
	 */
	public void _test402() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_KEEP_EMPTY_ARRAY_INITIALIZER_ON_ONE_LINE, DefaultCodeFormatterConstants.TRUE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_BRACES_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test402", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49298
	 */
	public void _test403() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test403", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49298
	 */
	public void _test404() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
 		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_PARENS_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_PARENS_IN_METHOD_DECLARATION, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test404", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49187
	 */
	public void _test405() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_PACKAGE, "10");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test405", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49298
	 */
	public void _test406() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_PARENS_IN_METHOD_INVOCATION, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_PARENS_IN_METHOD_DECLARATION, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test406", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49481
	 */
	public void _test407() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.blank_lines_before_first_class_body_declaration = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test407", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49481
	 */
	public void _test408() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.blank_lines_before_first_class_body_declaration = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test408", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49378
	 */
	public void test409() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK_IN_CASE, DefaultCodeFormatterConstants.NEXT_LINE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.indent_switchstatements_compare_to_cases = false;
		preferences.indent_switchstatements_compare_to_switch = true;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_switch = DefaultCodeFormatterConstants.NEXT_LINE;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test409", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49577
	 */
	public void _test410() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_between_type_declarations = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test410", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49577
	 */
	public void _test411() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_between_type_declarations = 2;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test411", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49551
	 */
	public void _test412() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test412", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49551
	 */
	public void _test413() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_between_type_declarations = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test413", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49551
	 */
	public void _test414() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test414", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49551
	 */
	public void _test415() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_between_type_declarations = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test415", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49551
	 */
	public void _test416() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_between_type_declarations = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test416", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49571
	 */
	public void _test417() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_opening_paren_in_constructor_declaration = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test417", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49571
	 */
	public void _test418() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_opening_paren_in_constructor_declaration = true;
		preferences.insert_space_before_opening_paren_in_method_declaration = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test418", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49162
	 */
	public void _test419() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_BINARY_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test419", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49623
	 */
	public void _test420() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.align_type_members_on_columns = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test420", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49298
	 */
	public void _test421() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_PARENS_IN_METHOD_INVOCATION, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_PARENS_IN_METHOD_DECLARATION, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test421", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49298
	 */
	public void _test422() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_PARENS_IN_METHOD_INVOCATION, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_PARENS_IN_METHOD_DECLARATION, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.blank_lines_before_first_class_body_declaration = 1;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test422", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49351
	 */
	public void _test423() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test423", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49351
	 */
	public void _test424() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test424", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49351
	 */
	public void _test425() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test425", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49351
	 */
	public void _test426() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test426", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49351
	 */
	public void _test427() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test427", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49351
	 */
	public void _test428() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test428", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49351
	 */
	public void _test429() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test429", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49351
	 */
	public void _test430() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test430", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49351
	 */
	public void _test431() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_SEMICOLON_IN_FOR, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test431", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49370
	 */
	public void _test432() {
		String resourcePath = getResource("test432", "formatter.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "AIS");
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test432", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49370
	 */
	public void _test433() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test433", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49660
	 */
	public void _test434() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
				DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		assertEquals(true, DefaultCodeFormatterConstants.getForceWrapping((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION)));
		assertEquals(DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.getWrappingStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION)));
		assertEquals(DefaultCodeFormatterConstants.INDENT_ON_COLUMN, DefaultCodeFormatterConstants.getIndentStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION)));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_size = 3;
		preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.insert_space_after_opening_paren_in_method_invocation = true;
		preferences.insert_space_before_closing_paren_in_method_invocation = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test434", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49772
	 */
	public void _test435() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_method_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.blank_lines_before_first_class_body_declaration = 1;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test435", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49772
	 */
	public void _test436() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_method_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_constructor_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.blank_lines_before_first_class_body_declaration = 1;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test436", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49660
	 */
	public void _test437() {
		String resourcePath = getResource("test437", "formatter.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "Felix");
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test437", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49660
	 */
	public void _test438() {
		String resourcePath = getResource("test438", "formatter.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "Felix");
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test438", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49660
	 */
	public void _test439() {
		String resourcePath = getResource("test439", "formatter.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "Felix");
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test439", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49763
	 */
	public void _test440() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_PUT_EMPTY_STATEMENT_ON_NEW_LINE, DefaultCodeFormatterConstants.FALSE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test440", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49763
	 */
	public void _test441() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_PUT_EMPTY_STATEMENT_ON_NEW_LINE, DefaultCodeFormatterConstants.FALSE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test441", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49763
	 */
	public void _test442() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_PUT_EMPTY_STATEMENT_ON_NEW_LINE, DefaultCodeFormatterConstants.FALSE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test442", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49763
	 */
	public void _test443() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_PUT_EMPTY_STATEMENT_ON_NEW_LINE, DefaultCodeFormatterConstants.FALSE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CATCH_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_ELSE_IN_IF_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_FINALLY_IN_TRY_STATEMENT, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_WHILE_IN_DO_STATEMENT, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test443", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49763
	 */
	public void _test444() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_PUT_EMPTY_STATEMENT_ON_NEW_LINE, DefaultCodeFormatterConstants.FALSE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test444", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49763
	 */
	public void _test445() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_PUT_EMPTY_STATEMENT_ON_NEW_LINE, DefaultCodeFormatterConstants.FALSE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test445", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49327
	 */
	public void _test446() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test446", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49187
	 */
	public void _test447() {
		String resourcePath = getResource("test447", "test447.zip");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "settings.xml", "Toms");
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		String input = getZipEntryContents(resourcePath, getIn("Format.js"));
		assertNotNull("No input", input);
		String output = getZipEntryContents(resourcePath, getOut("Format.js"));
		assertNotNull("No output", output);
		int start = input.indexOf("private");
		int end = input.indexOf(";");
		runTest(input, output, codeFormatter, CodeFormatter.K_JAVASCRIPT_UNIT, 0, false, start, end - start + 1, "\r\n");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49953
	 */
	public void _test448() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_space_before_opening_bracket_in_array_allocation_expression = true;
		preferences.insert_space_after_opening_bracket_in_array_allocation_expression = true;
		preferences.insert_space_before_closing_bracket_in_array_allocation_expression = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test448", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49953
	 */
	public void _test449() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.number_of_empty_lines_to_preserve = 0;
		preferences.insert_space_before_opening_bracket_in_array_allocation_expression = false;
		preferences.insert_space_after_opening_bracket_in_array_allocation_expression = true;
		preferences.insert_space_before_closing_bracket_in_array_allocation_expression = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test449", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=50225
	 */
	public void test450() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test450", "A.js", CodeFormatter.K_UNKNOWN, 0, false, 0, 0);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49187
	 */
	public void _test451() {
		String resourcePath = getResource("test451", "test451.zip");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "settings.xml", "Toms");
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		String input = getZipEntryContents(resourcePath, getIn("Format.js"));
		assertNotNull("No input", input);
		String output = getZipEntryContents(resourcePath, getOut("Format.js"));
		assertNotNull("No output", output);
		int start = input.indexOf("private");
		int end = input.indexOf(";");
		runTest(input, output, codeFormatter, CodeFormatter.K_JAVASCRIPT_UNIT, 0, false, start, end - start + 1, "\r\n");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=49187
	 */
	public void _test452() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_PACKAGE, "2");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_PACKAGE, "2");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test452", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=50719
	 */
	public void _test453() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_before_first_class_body_declaration = 1;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test453", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=50736
	 */
	public void _test454() {
		String resourcePath = getResource("test454", "test454.zip");
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");//$NON-NLS-1$
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		String input = getZipEntryContents(resourcePath, getIn("A.js"));
		assertNotNull("No input", input);
		String output = getZipEntryContents(resourcePath, getOut("A.js"));
		assertNotNull("No output", output);
		int start = input.indexOf("launch.setAttribute");
		int end = input.indexOf(";", start + 1);
		runTest(input, output, codeFormatter, CodeFormatter.K_JAVASCRIPT_UNIT, 0, false, start, end - start + 1, "\r\n");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=50736
	 */
	public void _test455() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");//$NON-NLS-1$
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test455", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=50736
	 */
	public void _test456() {
		String resourcePath = getResource("test456", "test456.zip");
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		String input = getZipEntryContents(resourcePath, getIn("A.js"));
		assertNotNull("No input", input);
		String output = getZipEntryContents(resourcePath, getOut("A.js"));
		assertNotNull("No output", output);
		int start = input.indexOf("launch.setAttribute");
		int end = input.indexOf(";", start + 1);
		runTest(input, output, codeFormatter, CodeFormatter.K_JAVASCRIPT_UNIT, 0, false, start, end - start + 1, "\r\n");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=50989
	 */
	public void _test457() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test457", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Check null options
	 */
	public void _test458() {
		CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(null);
		runTest(codeFormatter, "test458", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51158
	 */
	public void test459() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
				DefaultCodeFormatterConstants.createAlignmentValue(
						true,
						DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
						DefaultCodeFormatterConstants.INDENT_BY_ONE
				));
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_BINARY_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(
						false,
						DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
						DefaultCodeFormatterConstants.INDENT_BY_ONE
				));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test459", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51158
	 */
	public void _test460() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_BINARY_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(
						false,
						DefaultCodeFormatterConstants.WRAP_COMPACT,
						DefaultCodeFormatterConstants.INDENT_BY_ONE
				));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test460", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51190
	 */
	public void _test461() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test461", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51158
	 */
	public void test462() {
		String resourcePath = getResource("test462", "formatter.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "neils");
		assertNotNull("No preferences", options);
		
		options.put(
			DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
			DefaultCodeFormatterConstants.createAlignmentValue(
				true,
				DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
				DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test462", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=50276
	 */
	public void _test463() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_SELECTOR_IN_METHOD_INVOCATION,
				DefaultCodeFormatterConstants.createAlignmentValue(
					false,
					DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
					DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test463", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51128
	 */
	public void _test464() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_FOR, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_OPENING_PAREN_IN_FOR, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_FOR, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_METHOD_DECLARATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_METHOD_DECLARATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INDENT_STATEMENTS_COMPARE_TO_BLOCK, DefaultCodeFormatterConstants.FALSE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INDENT_STATEMENTS_COMPARE_TO_BODY, DefaultCodeFormatterConstants.TRUE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);

		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test464", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44642
	 * @deprecated
	 */
	public void _test465() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
			DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_SELECTOR_IN_METHOD_INVOCATION,
			DefaultCodeFormatterConstants.createAlignmentValue(
				false,
				DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
				DefaultCodeFormatterConstants.INDENT_BY_ONE));
		options.put(
			DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
			DefaultCodeFormatterConstants.createAlignmentValue(
				false,
				DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
				DefaultCodeFormatterConstants.INDENT_BY_ONE));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test465", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44642
	 * @deprecated
	 */
	public void _test466() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
			DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_SELECTOR_IN_METHOD_INVOCATION,
			DefaultCodeFormatterConstants.createAlignmentValue(
				false,
				DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
				DefaultCodeFormatterConstants.INDENT_BY_ONE));
		options.put(
			DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
			DefaultCodeFormatterConstants.createAlignmentValue(
				true,
				DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
				DefaultCodeFormatterConstants.INDENT_BY_ONE));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test466", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=44642
	 * @deprecated
	 */
	public void _test467() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
			DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_SELECTOR_IN_METHOD_INVOCATION,
			DefaultCodeFormatterConstants.createAlignmentValue(
				false,
				DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
				DefaultCodeFormatterConstants.INDENT_BY_ONE));
		options.put(
			DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
			DefaultCodeFormatterConstants.createAlignmentValue(
				false,
				DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
				DefaultCodeFormatterConstants.INDENT_BY_ONE));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test467", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51128
	 */
	public void _test468() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");//$NON-NLS-1$

		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test468", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51201
	 */
	public void _test469() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_BRACKET_IN_ARRAY_ALLOCATION_EXPRESSION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_BRACKET_IN_ARRAY_ALLOCATION_EXPRESSION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_BRACKETS_IN_ARRAY_ALLOCATION_EXPRESSION, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_KEEP_EMPTY_ARRAY_INITIALIZER_ON_ONE_LINE, DefaultCodeFormatterConstants.TRUE);

		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test469", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51035
	 */
	public void _test470() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test470", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51659
	 */
	public void _test471() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_BRACES_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_KEEP_EMPTY_ARRAY_INITIALIZER_ON_ONE_LINE, DefaultCodeFormatterConstants.TRUE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test471", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51659
	 */
	public void _test472() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_BRACES_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_KEEP_EMPTY_ARRAY_INITIALIZER_ON_ONE_LINE, DefaultCodeFormatterConstants.TRUE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test472", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51659
	 */
	public void _test473() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_BRACES_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_KEEP_EMPTY_ARRAY_INITIALIZER_ON_ONE_LINE, DefaultCodeFormatterConstants.TRUE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test473", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51659
	 */
	public void _test474() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_BRACES_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_KEEP_EMPTY_ARRAY_INITIALIZER_ON_ONE_LINE, DefaultCodeFormatterConstants.TRUE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test474", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51659
	 */
	public void _test475() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_BEFORE_CLOSING_BRACE_IN_ARRAY_INITIALIZER, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BETWEEN_EMPTY_BRACES_IN_ARRAY_INITIALIZER, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_KEEP_EMPTY_ARRAY_INITIALIZER_ON_ONE_LINE, DefaultCodeFormatterConstants.TRUE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test475", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51768 
	 */
	public void _test476() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INDENT_BODY_DECLARATIONS_COMPARE_TO_TYPE_HEADER, DefaultCodeFormatterConstants.FALSE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INDENT_STATEMENTS_COMPARE_TO_BLOCK, DefaultCodeFormatterConstants.FALSE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INDENT_STATEMENTS_COMPARE_TO_BODY, DefaultCodeFormatterConstants.FALSE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED);
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_IMPORTS, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_IF, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_IF, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_IF, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_OPENING_PAREN_IN_FOR, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_FOR, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_FOR, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_ASSIGNMENT_OPERATOR, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_ASSIGNMENT_OPERATOR, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_BINARY_OPERATOR, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_BINARY_OPERATOR, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test476", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=51603
	 */
	public void test477() {
		String option = 
			DefaultCodeFormatterConstants.createAlignmentValue(
				true,
				DefaultCodeFormatterConstants.WRAP_NO_SPLIT,
				DefaultCodeFormatterConstants.INDENT_BY_ONE);
		assertTrue("Wrong force setting", DefaultCodeFormatterConstants.getForceWrapping(option));
		assertEquals("Wrong wrapping style", DefaultCodeFormatterConstants.WRAP_NO_SPLIT, DefaultCodeFormatterConstants.getWrappingStyle(option));
		assertEquals("Wrong indent style", DefaultCodeFormatterConstants.INDENT_BY_ONE, DefaultCodeFormatterConstants.getIndentStyle(option));

		option = DefaultCodeFormatterConstants.setForceWrapping(option, false);
		assertFalse("Wrong force setting", DefaultCodeFormatterConstants.getForceWrapping(option));
		assertEquals("Wrong wrapping style", DefaultCodeFormatterConstants.WRAP_NO_SPLIT, DefaultCodeFormatterConstants.getWrappingStyle(option));
		assertEquals("Wrong indent style", DefaultCodeFormatterConstants.INDENT_BY_ONE, DefaultCodeFormatterConstants.getIndentStyle(option));
	
		option = DefaultCodeFormatterConstants.setIndentStyle(option, DefaultCodeFormatterConstants.INDENT_ON_COLUMN);
		assertFalse("Wrong force setting", DefaultCodeFormatterConstants.getForceWrapping(option));
		assertEquals("Wrong wrapping style", DefaultCodeFormatterConstants.WRAP_NO_SPLIT, DefaultCodeFormatterConstants.getWrappingStyle(option));
		assertEquals("Wrong indent style", DefaultCodeFormatterConstants.INDENT_ON_COLUMN, DefaultCodeFormatterConstants.getIndentStyle(option));

	
		option = DefaultCodeFormatterConstants.setIndentStyle(option, DefaultCodeFormatterConstants.INDENT_DEFAULT);
		assertFalse("Wrong force setting", DefaultCodeFormatterConstants.getForceWrapping(option));
		assertEquals("Wrong wrapping style", DefaultCodeFormatterConstants.WRAP_NO_SPLIT, DefaultCodeFormatterConstants.getWrappingStyle(option));
		assertEquals("Wrong indent style", DefaultCodeFormatterConstants.INDENT_DEFAULT, DefaultCodeFormatterConstants.getIndentStyle(option));

	
		option = DefaultCodeFormatterConstants.setForceWrapping(option, true);
		assertTrue("Wrong force setting", DefaultCodeFormatterConstants.getForceWrapping(option));
		assertEquals("Wrong wrapping style", DefaultCodeFormatterConstants.WRAP_NO_SPLIT, DefaultCodeFormatterConstants.getWrappingStyle(option));
		assertEquals("Wrong indent style", DefaultCodeFormatterConstants.INDENT_DEFAULT, DefaultCodeFormatterConstants.getIndentStyle(option));

	
		option = DefaultCodeFormatterConstants.setWrappingStyle(option, DefaultCodeFormatterConstants.WRAP_COMPACT);
		assertTrue("Wrong force setting", DefaultCodeFormatterConstants.getForceWrapping(option));
		assertEquals("Wrong wrapping style", DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.getWrappingStyle(option));
		assertEquals("Wrong indent style", DefaultCodeFormatterConstants.INDENT_DEFAULT, DefaultCodeFormatterConstants.getIndentStyle(option));

	
		option = DefaultCodeFormatterConstants.setWrappingStyle(option, DefaultCodeFormatterConstants.WRAP_COMPACT_FIRST_BREAK);
		assertTrue("Wrong force setting", DefaultCodeFormatterConstants.getForceWrapping(option));
		assertEquals("Wrong wrapping style", DefaultCodeFormatterConstants.WRAP_COMPACT_FIRST_BREAK, DefaultCodeFormatterConstants.getWrappingStyle(option));
		assertEquals("Wrong indent style", DefaultCodeFormatterConstants.INDENT_DEFAULT, DefaultCodeFormatterConstants.getIndentStyle(option));
		
		option = DefaultCodeFormatterConstants.setWrappingStyle(option, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE);
		assertTrue("Wrong force setting", DefaultCodeFormatterConstants.getForceWrapping(option));
		assertEquals("Wrong wrapping style", DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.getWrappingStyle(option));
		assertEquals("Wrong indent style", DefaultCodeFormatterConstants.INDENT_DEFAULT, DefaultCodeFormatterConstants.getIndentStyle(option));
		
		option = DefaultCodeFormatterConstants.setWrappingStyle(option, DefaultCodeFormatterConstants.WRAP_NEXT_SHIFTED);
		assertTrue("Wrong force setting", DefaultCodeFormatterConstants.getForceWrapping(option));
		assertEquals("Wrong wrapping style", DefaultCodeFormatterConstants.WRAP_NEXT_SHIFTED, DefaultCodeFormatterConstants.getWrappingStyle(option));
		assertEquals("Wrong indent style", DefaultCodeFormatterConstants.INDENT_DEFAULT, DefaultCodeFormatterConstants.getIndentStyle(option));
		
		option = DefaultCodeFormatterConstants.setWrappingStyle(option, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE);
		assertTrue("Wrong force setting", DefaultCodeFormatterConstants.getForceWrapping(option));
		assertEquals("Wrong wrapping style", DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.getWrappingStyle(option));
		assertEquals("Wrong indent style", DefaultCodeFormatterConstants.INDENT_DEFAULT, DefaultCodeFormatterConstants.getIndentStyle(option));
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=52246
	 */
	public void _test478() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(JavaScriptCore.COMPILER_SOURCE, JavaScriptCore.VERSION_1_4);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, options);
		runTest(codeFormatter, "test478", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=52479
	 */
	public void _test479() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test479", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=52479
	 */
	public void _test480() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test480", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=52283
	 */
	public void _test481() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test481", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=52283
	 */
	public void _test482() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_CLOSING_BRACE_IN_BLOCK, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test482", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=52851
	 */
	public void _test483() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_CLOSING_BRACE_IN_BLOCK, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test483", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=50989
	 */
	public void _test484() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");//$NON-NLS-1$
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test484", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=50989
	 */
	public void _test485() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");//$NON-NLS-1$
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test485", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=50989
	 */
	public void _test486() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);//$NON-NLS-1$
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.NEXT_LINE);//$NON-NLS-1$
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);//$NON-NLS-1$
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);//$NON-NLS-1$
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.NEXT_LINE);//$NON-NLS-1$
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ANONYMOUS_TYPE_DECLARATION, DefaultCodeFormatterConstants.NEXT_LINE);//$NON-NLS-1$
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test486", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test487() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test487", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test488() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_COLON_IN_CASE, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test488", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test489() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test489", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=52747
	 */
	public void _test490() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_ARRAY_INITIALIZER, DefaultCodeFormatterConstants.NEXT_LINE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_KEEP_EMPTY_ARRAY_INITIALIZER_ON_ONE_LINE, DefaultCodeFormatterConstants.TRUE);
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_EXPRESSIONS_IN_ARRAY_INITIALIZER,
				DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test490", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59451
	 */
	public void _test491() {
		String resourcePath = getResource("test491", "formatter.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "DOI");
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test491", "BundleChain.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59575
	 */
	public void _test492() {
		String resourcePath = getResource("test492", "core_formatting.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "core");
		assertNotNull("No preferences", options);
		options.put(JavaScriptCore.COMPILER_PB_NON_NLS_STRING_LITERAL, JavaScriptCore.ERROR);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, options);
		runTest(codeFormatter, "test492", "Main.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=59716
	 */
	public void _test493() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_PACKAGE, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_PACKAGE, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_IMPORTS, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_IMPORTS, "1");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test493", "MyClass.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=60035
	 */
	public void _test494() {
		String resourcePath = getResource("test494", "format.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "AGPS default");
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, options);
		runTest(codeFormatter, "test494", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=58565
	 */
	public void _test495() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_before_first_class_body_declaration = 1;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test495", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=58565
	 */
	public void _test496() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
		preferences.blank_lines_before_first_class_body_declaration = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test496", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test497() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.blank_lines_before_first_class_body_declaration = 1;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test497", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test498() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test498", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test499() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.blank_lines_before_first_class_body_declaration = 1;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test499", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, true);//$NON-NLS-1$ //$NON-NLS-2$
	}

	public void _test500() {
		Map options = DefaultCodeFormatterConstants.getEclipse21Settings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test500", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=68506
	 */
	public void _test501() {
		String resourcePath = getResource("test501", "formatter.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "GMTI");
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test501", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=69806
	 */
	public void _test502() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test502", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Formatting foreach
	 */
	public void _test503() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		Map compilerOptions = new HashMap();
		compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
		compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
		compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
		runTest(codeFormatter, "test503", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Formatting parameterized type
	 */
	public void _test504() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test504", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * Formatting parameterized type
	 */
	public void _test505() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test505", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * Formatting parameterized type
	 */
	public void _test506() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test506", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * Formatting enum declaration
	 */
	public void _test507() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test507", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * Formatting annotation type declaration
	 */
	public void _test508() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test508", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=68506
	 */
	public void _test509() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_then_statement_on_same_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.compact_else_if = true;
		preferences.insert_new_line_at_end_of_file_if_missing = true;
		preferences.number_of_empty_lines_to_preserve = 0;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter,"test509", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=68506
	 */
	public void _test510() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_then_statement_on_same_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.compact_else_if = true;
		preferences.insert_new_line_at_end_of_file_if_missing = false;
		preferences.number_of_empty_lines_to_preserve = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter,"test510", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=68506
	 */
	public void _test511() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_then_statement_on_same_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.compact_else_if = true;
		preferences.insert_new_line_at_end_of_file_if_missing = false;
		preferences.number_of_empty_lines_to_preserve = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter,"test511", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}	
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=68506
	 */
	public void _test512() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipse21Settings());
		preferences.keep_simple_if_on_one_line = true;
		preferences.keep_then_statement_on_same_line = true;
		preferences.keep_guardian_clause_on_one_line = true;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.compact_else_if = true;
		preferences.insert_new_line_at_end_of_file_if_missing = true;
		preferences.number_of_empty_lines_to_preserve = 1;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter,"test512", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=73371
	 */
	public void _test513() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test513", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=75488
	 */
	public void _test514() {
		String resourcePath = getResource("test514", "formatter.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "core");
		assertNotNull("No preferences", options);
		options.put(JavaScriptCore.COMPILER_PB_NON_NLS_STRING_LITERAL, JavaScriptCore.ERROR);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, options);
		runTest(codeFormatter, "test514", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=75720
	 */
	public void _test515() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test515", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=75720
	 */
	public void _test516() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_ELLIPSIS, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_ELLIPSIS, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);	
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test516", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=75720
	 */
	public void _test517() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_ELLIPSIS, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_ELLIPSIS, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);	
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test517", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=75720
	 */
	public void _test518() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_ELLIPSIS, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_ELLIPSIS, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);	
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test518", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=75720
	 */
	public void _test519() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_ELLIPSIS, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_ELLIPSIS, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);	
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test519", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * Formatting enum declaration
	 */
	public void _test520() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test520", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=76642
	 */
	public void _test521() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test521", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=76642
	 */
	public void _test522() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test522", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=76766
	 */
	public void _test523() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_type_declaration = true;
		preferences.insert_new_line_in_empty_enum_constant = false;
		preferences.insert_new_line_in_empty_enum_declaration = false;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test523", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=76766
	 */
	public void _test524() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_type_declaration = true;
		preferences.insert_new_line_in_empty_enum_constant = false;
		preferences.insert_new_line_in_empty_enum_declaration = true;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test524", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=76766
	 */
	public void _test525() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_type_declaration = true;
		preferences.insert_new_line_in_empty_enum_constant = true;
		preferences.insert_new_line_in_empty_enum_declaration = false;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test525", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=76766
	 */
	public void _test526() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.insert_new_line_in_empty_type_declaration = true;
		preferences.insert_new_line_in_empty_enum_constant = true;
		preferences.insert_new_line_in_empty_enum_declaration = true;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test526", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=77249
	 */
	public void _test527() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test527", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=79779
	 */
	public void _test528() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test528", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=79779
	 */
	public void _test529() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test529", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=79795
	 */
	public void _test530() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test530", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=79795
	 */
	public void _test531() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test531", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=79795
	 */
	public void _test532() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test532", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=79795
	 */
	public void _test533() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test533", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=78698
	 */
	public void _test534() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test534", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=78698
	 */
	public void _test535() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test535", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=83078
	 */
	public void _test538() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getJavaConventionsSettings());
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter,"test538", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=83515
	 */
	public void _test539() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test539", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=83515
	 */
	public void _test540() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test540", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=83684
	 */
	public void _test541() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test541", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	public void _test542() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getJavaConventionsSettings());
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test542", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void _test543() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getJavaConventionsSettings());
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test543", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=86410
	 */
	public void _test544() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.insert_space_after_closing_angle_bracket_in_type_parameters = true;
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test544", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=86878
	 */
	public void _test545() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test545", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=86908
	 */
	public void _test546() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
 		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test546", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Add support for comment formatting
	 */
	public void test547() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test547", "A.js", CodeFormatter.K_MULTI_LINE_COMMENT, false);//$NON-NLS-1$ //$NON-NLS-2$

		runTest(codeFormatter, "test547", "A.js", CodeFormatter.K_UNKNOWN, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Add support for comment formatting
	 */
	public void test548() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test548", "A.js", CodeFormatter.K_JAVA_DOC, false);//$NON-NLS-1$ //$NON-NLS-2$

		runTest(codeFormatter, "test548", "A.js", CodeFormatter.K_UNKNOWN, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=73104
	public void _test549() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.MIXED;
		preferences.indentation_size = 4;
		preferences.tab_size = 8;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test549", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=73104
	public void _test550() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.MIXED;
		preferences.indentation_size = 4;
		preferences.tab_size = 8;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test550", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=73104
	public void _test551() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		assertEquals(false, DefaultCodeFormatterConstants.getForceWrapping((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION)));
		assertEquals(DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.getWrappingStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION)));
		assertEquals(DefaultCodeFormatterConstants.INDENT_ON_COLUMN, DefaultCodeFormatterConstants.getIndentStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION)));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.MIXED;
		preferences.indentation_size = 4;
		preferences.tab_size = 8;
		preferences.page_width = 57;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test551", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=73104
	public void _test552() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		assertEquals(false, DefaultCodeFormatterConstants.getForceWrapping((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION)));
		assertEquals(DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.getWrappingStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION)));
		assertEquals(DefaultCodeFormatterConstants.INDENT_ON_COLUMN, DefaultCodeFormatterConstants.getIndentStyle((String) options.get(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_CONSTRUCTOR_DECLARATION)));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.MIXED;
		preferences.indentation_size = 4;
		preferences.tab_size = 8;
		preferences.page_width = 57;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test552", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Add support for comment formatting
	 */
	public void test553() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test553", "A.js", CodeFormatter.K_SINGLE_LINE_COMMENT, false);//$NON-NLS-1$ //$NON-NLS-2$

		runTest(codeFormatter, "test553", "A.js", CodeFormatter.K_UNKNOWN, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Add support for comment formatting
	 */
	public void test554() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test554", "A.js", CodeFormatter.K_SINGLE_LINE_COMMENT, false);//$NON-NLS-1$ //$NON-NLS-2$

		runTest(codeFormatter, "test554", "A.js", CodeFormatter.K_UNKNOWN, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=73104
	public void _test555() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_char = DefaultCodeFormatterOptions.MIXED;
		preferences.indentation_size = 4;
		preferences.tab_size = 8;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test555", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/*
	 * Empty array initializer formatting
	 */
	public void _test556() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test556", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * Empty array initializer formatting
	 */
	public void _test557() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.tab_size = 4;
		preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.keep_empty_array_initializer_on_one_line = true;
		preferences.insert_space_between_empty_braces_in_array_initializer = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test557", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=89302
	public void _test558() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.insert_space_after_comma_in_enum_declarations = false;
		preferences.insert_new_line_after_annotation = false;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test558", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=89302
	public void _test559() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.insert_space_after_comma_in_enum_declarations = true;
		preferences.insert_new_line_after_annotation = false;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test559", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=81497
	public void _test560() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.brace_position_for_array_initializer = DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED;
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_method_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.keep_empty_array_initializer_on_one_line = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test560", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=81497
	public void _test561() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		preferences.brace_position_for_array_initializer = DefaultCodeFormatterConstants.NEXT_LINE_SHIFTED;
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_method_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.keep_empty_array_initializer_on_one_line = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test561", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=89318
	public void _test562() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		preferences.insert_space_after_closing_angle_bracket_in_type_arguments = false;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test562", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=89318
	public void _test563() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.insert_space_after_closing_angle_bracket_in_type_arguments = true;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test563", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=89299
	public void _test564() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
        preferences.tab_size = 4;
		preferences.brace_position_for_array_initializer = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_method_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_block = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.keep_empty_array_initializer_on_one_line = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test564", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/*// https://bugs.eclipse.org/bugs/show_bug.cgi?id=49896
	public void _test565() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_BINARY_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.use_tabs_only_for_leading_indentations = true;
		preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test565", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}*/
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=73658
	public void _test566() {
		Map options = DefaultCodeFormatterConstants.getJavaConventionsSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
		preferences.brace_position_for_enum_constant = DefaultCodeFormatterConstants.NEXT_LINE;
		preferences.brace_position_for_enum_declaration = DefaultCodeFormatterConstants.NEXT_LINE;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test566", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=49896
	public void _test567() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.use_tabs_only_for_leading_indentations = true;
        preferences.page_width = 35;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test567", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=49896
	public void _test568() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.indentation_size = 4;
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.MIXED;
		preferences.use_tabs_only_for_leading_indentations = true;
        preferences.page_width = 35;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test568", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=49896
	public void _test569() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
        preferences.indentation_size = 4;
        preferences.tab_size = 4;
        preferences.tab_char = DefaultCodeFormatterOptions.TAB;
		preferences.use_tabs_only_for_leading_indentations = false;
        preferences.page_width = 40;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test569", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=77809
	public void _test570() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ENUM_CONSTANTS,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test570", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=77809
	public void _test571() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ENUM_CONSTANTS,
				DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test571", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=90213
	public void _test572() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.insert_space_after_opening_brace_in_array_initializer = false;
		preferences.insert_space_before_closing_brace_in_array_initializer = false;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test572", "A.js", CodeFormatter.K_STATEMENTS, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=90213
	public void _test573() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test573", "A.js", CodeFormatter.K_STATEMENTS, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=91238
	public void test574() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
 		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test574", "A.js", CodeFormatter.K_JAVA_DOC, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=77809
	public void _test575() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test575", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=95431
	public void _test576() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test576", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=98037
	public void _test577() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test577", "A.js", CodeFormatter.K_UNKNOWN, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=98037
	public void _test578() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test578", "A.js", CodeFormatter.K_UNKNOWN, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=98139
	public void _test579() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test579", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=98139
	public void _test580() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test580", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=99084
	public void _test581() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test581", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=99084
	public void _test582() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.insert_space_after_closing_angle_bracket_in_type_arguments = false;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test582", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=100062
	public void _test583() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test583", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=49896
	public void _test584() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_USE_TABS_ONLY_FOR_LEADING_INDENTATIONS, DefaultCodeFormatterConstants.TRUE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, DefaultCodeFormatterConstants.MIXED);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "65");
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test584", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=49896
	public void _test585() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_USE_TABS_ONLY_FOR_LEADING_INDENTATIONS, DefaultCodeFormatterConstants.TRUE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, DefaultCodeFormatterConstants.MIXED);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "80");
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test585", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=49896
	public void _test586() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_USE_TABS_ONLY_FOR_LEADING_INDENTATIONS, DefaultCodeFormatterConstants.TRUE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "65");
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test586", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=49896
	public void _test587() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_USE_TABS_ONLY_FOR_LEADING_INDENTATIONS, DefaultCodeFormatterConstants.TRUE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "80");
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test587", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=101230
	public void _test588() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test588", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=104796
	public void _test589() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test589", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=103706
	public void _test590() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INDENT_EMPTY_LINES, DefaultCodeFormatterConstants.TRUE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test590", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=103706
	public void _test591() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INDENT_EMPTY_LINES, DefaultCodeFormatterConstants.FALSE);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test591", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=98089
	public void _test592() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_METHOD_DECLARATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_OPENING_PAREN_IN_METHOD_DECLARATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "60");
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.SPACE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_OPENING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_METHOD_INVOCATION, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test592", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=71766
	public void _test593() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ASSIGNMENT,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_ALLOCATION_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NO_SPLIT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test593", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=71766
	public void _test594() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ASSIGNMENT,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test594", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=71766
	public void _test595() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ASSIGNMENT,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test595", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=71766
	public void _test596() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ASSIGNMENT,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_COMPACT, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test596", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=87193
	public void _test597() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, DefaultCodeFormatterConstants.MIXED);
		options.put(DefaultCodeFormatterConstants.FORMATTER_USE_TABS_ONLY_FOR_LEADING_INDENTATIONS, DefaultCodeFormatterConstants.TRUE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "60");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test597", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=87193
	public void _test598() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE, DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_USE_TABS_ONLY_FOR_LEADING_INDENTATIONS, DefaultCodeFormatterConstants.TRUE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "60");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test598", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=87193
	public void _test599() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
				DefaultCodeFormatterConstants.createAlignmentValue(false, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_CONTINUATION_INDENTATION, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_USE_TABS_ONLY_FOR_LEADING_INDENTATIONS, DefaultCodeFormatterConstants.TRUE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "60");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test599", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// test Scribe2.hasNLSTag()
	public void _test600() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test600", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// Binary expression
	public void _test601() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_BINARY_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(true, DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE, DefaultCodeFormatterConstants.INDENT_DEFAULT));
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaScriptCore.TAB);
		options.put(DefaultCodeFormatterConstants.FORMATTER_USE_TABS_ONLY_FOR_LEADING_INDENTATIONS, DefaultCodeFormatterConstants.TRUE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "60");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test601", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=108916
	public void _test605() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ENUM_CONSTANTS,
				DefaultCodeFormatterConstants.createAlignmentValue(
						true,
						DefaultCodeFormatterConstants.WRAP_NEXT_PER_LINE,
						DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test605", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=110304
	public void _test606() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_PARENTHESIZED_EXPRESSION_IN_RETURN, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test606", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=111270 
	public void _test607() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test607", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=111270 
	public void _test608() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test608", "A.js", CodeFormatter.K_JAVA_DOC, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=116858
	public void _test609() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.indent_switchstatements_compare_to_cases = true;
		preferences.indent_switchstatements_compare_to_switch = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test609", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=122914
	 */
	public void _test610() {
		String resourcePath = getResource("test610", "formatter.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "mhdk");
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test610", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=111446
	 */
	public void test611() {
		{
			// only tabs, indentation size = 4, tab size = 4
			// indentation size is ignored
			Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
			DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
			preferences.tab_char = DefaultCodeFormatterOptions.TAB;
			preferences.tab_size = 4;
			preferences.indentation_size = 4;
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
			assertEquals("Wrong indentation string", "\t", codeFormatter.createIndentationString(1));
			assertEquals("Wrong indentation string", "\t\t", codeFormatter.createIndentationString(2));
			assertEquals("Wrong indentation string", "\t\t\t", codeFormatter.createIndentationString(3));
		}
		{
			// only tabs, indentation size = 4, tab size = 8
			// indentation size is ignored
			Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
			DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
			preferences.tab_char = DefaultCodeFormatterOptions.TAB;
			preferences.tab_size = 8;
			preferences.indentation_size = 4;
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
			assertEquals("Wrong indentation string", "\t", codeFormatter.createIndentationString(1));
			assertEquals("Wrong indentation string", "\t\t", codeFormatter.createIndentationString(2));
			assertEquals("Wrong indentation string", "\t\t\t", codeFormatter.createIndentationString(3));
		}
		{
			// only spaces, indentation size = 4, tab size = 2
			// indentation size is ignored
			Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
			DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
			preferences.tab_char = DefaultCodeFormatterOptions.SPACE;
			preferences.tab_size = 2;
			preferences.indentation_size = 4;
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
			assertEquals("Wrong indentation string", "  ", codeFormatter.createIndentationString(1));
			assertEquals("Wrong indentation string", "    ", codeFormatter.createIndentationString(2));
			assertEquals("Wrong indentation string", "      ", codeFormatter.createIndentationString(3));
		}
		{
			// mixed, indentation size = 4, tab size = 2
			Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
			DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
			preferences.tab_char = DefaultCodeFormatterOptions.MIXED;
			preferences.tab_size = 2;
			preferences.indentation_size = 4;
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
			assertEquals("Wrong indentation string", "\t\t", codeFormatter.createIndentationString(1));
			assertEquals("Wrong indentation string", "\t\t\t\t", codeFormatter.createIndentationString(2));
		}		
		{
			// mixed, indentation size = 2, tab size = 4
			// indentation size is ignored
			Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
			DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
			preferences.tab_char = DefaultCodeFormatterOptions.MIXED;
			preferences.tab_size = 4;
			preferences.indentation_size = 2;
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
			assertEquals("Wrong indentation string", "  ", codeFormatter.createIndentationString(1));
			assertEquals("Wrong indentation string", "\t", codeFormatter.createIndentationString(2));
			assertEquals("Wrong indentation string", "\t  ", codeFormatter.createIndentationString(3));
		}		
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=126625
	public void _test612() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.indent_body_declarations_compare_to_annotation_declaration_header = false;
		preferences.indent_body_declarations_compare_to_type_header = true;
		preferences.insert_new_line_in_empty_annotation_declaration = false;
		preferences.insert_new_line_in_empty_type_declaration = true;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test612", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=126625
	public void _test613() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.indent_body_declarations_compare_to_annotation_declaration_header = true;
		preferences.indent_body_declarations_compare_to_type_header = false;
		preferences.insert_new_line_in_empty_annotation_declaration = true;
		preferences.insert_new_line_in_empty_type_declaration = false;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test613", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=128848
	public void _test614() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test614", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=128848
	public void _test615() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test615", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=128848
	public void _test616() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test616", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=131013
	public void _test617() {
		String resourcePath = getResource("test617", "formatter.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "JRK");
		assertNotNull("No preferences", options);
		options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_BINARY_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(
					true,
					DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
					DefaultCodeFormatterConstants.INDENT_ON_COLUMN));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test617", "A.js", CodeFormatter.K_STATEMENTS);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=137224
	public void _test618() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test618", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=137224
	public void _test619() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test619", "A.js", CodeFormatter.K_CLASS_BODY_DECLARATIONS, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=139291
	public void _test620() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_CLOSING_ANGLE_BRACKET_IN_TYPE_ARGUMENTS, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test620", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=139291
	public void _test621() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_CLOSING_ANGLE_BRACKET_IN_TYPE_ARGUMENTS, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test621", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=148370
	public void _test622() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test622", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=148370
	public void _test623() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test623", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=148370
	public void _test624() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test624", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=102728
	public void _test625() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(
				DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_BINARY_EXPRESSION,
				DefaultCodeFormatterConstants.createAlignmentValue(
						true,
						DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
						DefaultCodeFormatterConstants.INDENT_DEFAULT));
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test625", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=152725
	public void _test626() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_6);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_6);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_6);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_6);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_6);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_6);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test626", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=158267
	public void _test627() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test627", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=158267
	public void _test628() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test628", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=158267
	public void _test629() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		preferences.brace_position_for_type_declaration = DefaultCodeFormatterConstants.NEXT_LINE_ON_WRAP;
		Hashtable javaCoreOptions = JavaScriptCore.getOptions();
		try {
			Hashtable newJavaCoreOptions = JavaScriptCore.getOptions();
			newJavaCoreOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			newJavaCoreOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
			JavaScriptCore.setOptions(newJavaCoreOptions);
		
			Map compilerOptions = new HashMap();
			compilerOptions.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);
			compilerOptions.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);		
			DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences, compilerOptions);
			runTest(codeFormatter, "test629", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT, false);//$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			JavaScriptCore.setOptions(javaCoreOptions);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=158267
	public void _test630() {
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(DefaultCodeFormatterConstants.getEclipseDefaultSettings());
		preferences.insert_space_after_opening_paren_in_parenthesized_expression = true;
		preferences.insert_space_before_closing_paren_in_parenthesized_expression = true;
		preferences.insert_space_before_parenthesized_expression_in_throw = true;
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test630", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=158267
	public void _test631() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_PARENTHESIZED_EXPRESSION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_PARENTHESIZED_EXPRESSION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_PARENTHESIZED_EXPRESSION_IN_THROW, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test631", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=158267
	public void _test632() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_AFTER_OPENING_PAREN_IN_PARENTHESIZED_EXPRESSION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_CLOSING_PAREN_IN_PARENTHESIZED_EXPRESSION, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_PARENTHESIZED_EXPRESSION_IN_RETURN, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_PARENTHESIZED_EXPRESSION_IN_THROW, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test632", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165210
	public void _test633() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_IMPORT_GROUPS, "3");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test633", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165210
	public void _test634() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_IMPORT_GROUPS, "1");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test634", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165210
	public void _test635() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_IMPORT_GROUPS, "1");
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_IMPORTS, "0");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test635", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165210
	public void _test636() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_IMPORT_GROUPS, "3");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test636", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165210
	public void _test637() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_IMPORT_GROUPS, "2");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test637", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165210
	public void _test638() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_IMPORT_GROUPS, "1");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test638", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165210
	public void _test639() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_IMPORT_GROUPS, "2");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test639", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=165210
	public void _test640() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_IMPORT_GROUPS, "1");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test640", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=168109
	public void _test641() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, DefaultCodeFormatterConstants.MIXED);
		options.put(DefaultCodeFormatterConstants.FORMATTER_USE_TABS_ONLY_FOR_LEADING_INDENTATIONS, DefaultCodeFormatterConstants.TRUE);
		options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "36");
		options.put(DefaultCodeFormatterConstants.FORMATTER_INDENTATION_SIZE, "2");
		options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test641", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=166962
	public void _test642() {
		String resourcePath = getResource("test642", "formatter.xml");
		Map options = DecodeCodeFormatterPreferences.decodeCodeFormatterOptions(resourcePath, "Visionnaire");
		assertNotNull("No preferences", options);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test642", "A.js", CodeFormatter.K_JAVASCRIPT_UNIT);//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=171634
	public void _test643() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AT_END_OF_FILE_IF_MISSING, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test643", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=171634
	public void _test644() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AT_END_OF_FILE_IF_MISSING, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test644", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=171634
	public void _test645() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AT_END_OF_FILE_IF_MISSING, JavaScriptCore.INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test645", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=171634
	public void _test646() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AT_END_OF_FILE_IF_MISSING, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test646", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=171634
	public void _test647() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AT_END_OF_FILE_IF_MISSING, JavaScriptCore.DO_NOT_INSERT);
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test647", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=171634
	public void _test648() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AT_END_OF_FILE_IF_MISSING, JavaScriptCore.DO_NOT_INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "0");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test648", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=171634
	public void _test649() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AT_END_OF_FILE_IF_MISSING, JavaScriptCore.INSERT);
		options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "0");
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test649", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=172848
	public void _test650() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test650", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=172848
	public void _test651() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test651", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=172848
	public void _test652() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test652", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=172848
	public void _test653() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test653", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=172848
	public void _test654() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test654", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=172848
	public void _test655() {
		final Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		DefaultCodeFormatterOptions preferences = new DefaultCodeFormatterOptions(options);
		DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(preferences);
		runTest(codeFormatter, "test655", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void test656() {
		runTest("test656", "A.js");//$NON-NLS-1$ //$NON-NLS-2$
	}
}
