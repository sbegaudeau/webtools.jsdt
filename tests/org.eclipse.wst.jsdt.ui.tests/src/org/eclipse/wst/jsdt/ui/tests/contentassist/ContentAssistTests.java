/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.wst.jsdt.ui.tests.contentassist;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.extensions.TestSetup;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.wst.jsdt.ui.tests.utils.FileUtil;
import org.eclipse.wst.jsdt.ui.tests.utils.ProjectUnzipUtility;
import org.eclipse.wst.jsdt.ui.tests.utils.StringUtils;
import org.eclipse.wst.jsdt.ui.text.IJavaScriptPartitions;
import org.eclipse.wst.jsdt.ui.text.JavaScriptSourceViewerConfiguration;

public class ContentAssistTests extends TestCase {
	/** The name of the project that all of these tests will use */
	private static final String PROJECT_NAME = "ContentAssist";
	
	/** dir in project where files are stored */
	private static final String CONTENT_DIR = "root";
	
	/** The location of the testing files */
	private static final String ZIP_FOLDER = "testresources";
	
	/** The project that all of the tests use */
	private static IProject fProject;
	
	/**
	 * Used to keep track of the already open editors so that the tests don't go through
	 * the trouble of opening the same editors over and over again
	 */
	private static Map fFileToEditorMap = new HashMap();
	
	/**
	 * <p>Default constructor<p>
	 * <p>Use {@link #suite()}</p>
	 * 
	 * @see #suite()
	 */
	public ContentAssistTests() {
		super("Test JavaScript Content Assist");
	}
	
	/**
	 * <p>Constructor that takes a test name.</p>
	 * <p>Use {@link #suite()}</p>
	 * 
	 * @param name The name this test run should have.
	 * 
	 * @see #suite()
	 */
	public ContentAssistTests(String name) {
		super(name);
	}
	
	/**
	 * <p>Use this method to add these tests to a larger test suite so set up
	 * and tear down can be performed</p>
	 * 
	 * @return a {@link TestSetup} that will run all of the tests in this class
	 * with set up and tear down.
	 */
	public static Test suite() {
		TestSuite ts = new TestSuite(ContentAssistTests.class, "Test JavaScript Content Assist");
		return new ContentAssistTestsSetup(ts);
	}
	
	public void testFindFunctions_ThisFile_EmptyLine() throws Exception {
		String[][] expectedProposals = new String[][] {{"funcOne()", "funcTwo()", "funcThree(paramOne)", "funcFour(paramOne, paramTwo)"}};
		runProposalTest("test1.js", 16, 0, expectedProposals);
	}
	
	public void testFindFunctions_ThisFile_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"funcOne()", "funcTwo()", "funcThree(paramOne)", "funcFour(paramOne, paramTwo)"}};
		runProposalTest("test1.js", 18, 3, expectedProposals);
	}
	
	public void testFindFunctions_ThisFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] {{"funcTwo()", "funcThree(paramOne)"}};
		runProposalTest("test1.js", 20, 5, expectedProposals);
	}
	
	public void testFindFunctions_OtherFile_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"funcOne()", "funcTwo()", "funcThree(paramOne)", "funcFour(paramOne, paramTwo)"}};
		runProposalTest("test2.js", 0, 1, expectedProposals);
	}
	
	public void testFindFunctions_OtherFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] {{"funcTwo()", "funcThree(paramOne)"}};
		runProposalTest("test2.js", 2, 5, expectedProposals);
	}
	
	public void testFindAnonymousTypeField_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"eclipse", "eclipse2"}};
		runProposalTest("test8_0.js", 6, 4, expectedProposals);
	}
	
	public void testFindAnonymousTypeField_1() throws Exception {
		String[][] expectedProposals = new String[][] {{"eclipse", "eclipse2"}};
		runProposalTest("test8_1.js", 6, 8, expectedProposals);
	}
	
	public void testFindFunctionOnAnonymousTypeField_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"fun()", "crazy()"}};
		runProposalTest("test8_2.js", 6, 12, expectedProposals);
	}
	
	public void testFindFunctionOnAnonymousTypeField_1() throws Exception {
		String[][] expectedProposals = new String[][] {{"fun()"}};
		runProposalTest("test8_1.js", 8, 13, expectedProposals);
	}
	
	public void testFindConstructors_ThisFile_JustNew() throws Exception {
		String[][] expectedProposals = new String[][] {{"Awesome(param1, param2)", "bar.Class1(a, b)", "bar.Class2(c, d, e)", "bar.foo.Class3(param1, param2, param3, param4)"}};
		runProposalTest("test3.js", 17, 4, expectedProposals);
	}
	
	public void testFindConstructors_ThisFile_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"Awesome(param1, param2)"}};
		runProposalTest("test3.js", 19, 6, expectedProposals);
	}
	
	public void testFindConstructors_ThisFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.Class1(a, b)", "bar.Class2(c, d, e)", "bar.foo.Class3(param1, param2, param3, param4)"}};
		runProposalTest("test3.js", 21, 6, expectedProposals);
	}
	
	public void testFindConstructors_ThisFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.Class1(a, b)", "bar.Class2(c, d, e)"}};
		runProposalTest("test3.js", 23, 9, expectedProposals);
	}
	
	public void testFindConstructors_ThisFile_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.foo.Class3(param1, param2, param3, param4)"}};
		runProposalTest("test3.js", 25, 10, expectedProposals);
	}
	
	public void testFindConstructors_ThisFile_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.foo.Class3(param1, param2, param3, param4)"}};
		runProposalTest("test3.js", 27, 13, expectedProposals);
	}
	
	public void testFindConstructors_ThisFile_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.Class1(a, b)", "bar.Class2(c, d, e)", "bar.foo.Class3(param1, param2, param3, param4)"}};
		runProposalTest("test3.js", 29, 5, expectedProposals);
	}
	
	public void testFindConstructors_ThisFile_ExpressionStarted_6() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.Class1(a, b)", "bar.Class2(c, d, e)", "bar.foo.Class3(param1, param2, param3, param4)"}};
		runProposalTest("test3.js", 31, 9, expectedProposals);
	}
	
	public void testFindConstructors_OtherFile_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"Awesome(param1, param2)"}};
		runProposalTest("test4.js", 0, 6, expectedProposals);
	}
	
	public void testFindConstructors_OtherFile_ExpressionStarted_1() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.Class1(a, b)", "bar.Class2(c, d, e)", "bar.foo.Class3(param1, param2, param3, param4)"}};
		runProposalTest("test4.js", 2, 6, expectedProposals);
	}
	
	public void testFindConstructors_OtherFile_ExpressionStarted_2() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.Class1(a, b)", "bar.Class2(c, d, e)"}};
		runProposalTest("test4.js", 4, 9, expectedProposals);
	}
	
	public void testFindConstructors_OtherFile_ExpressionStarted_3() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.foo.Class3(param1, param2, param3, param4)"}};
		runProposalTest("test4.js", 6, 10, expectedProposals);
	}
	
	public void testFindConstructors_OtherFile_ExpressionStarted_4() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.foo.Class3(param1, param2, param3, param4)"}};
		runProposalTest("test4.js", 8, 13, expectedProposals);
	}
	
	public void testFindConstructors_OtherFile_ExpressionStarted_5() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.Class1(a, b)", "bar.Class2(c, d, e)", "bar.foo.Class3(param1, param2, param3, param4)"}};
		runProposalTest("test4.js", 10, 5, expectedProposals);
	}
	
	public void testFindConstructors_OtherFile_ExpressionStarted_6() throws Exception {
		String[][] expectedProposals = new String[][] {{"bar.Class1(a, b)", "bar.Class2(c, d, e)", "bar.foo.Class3(param1, param2, param3, param4)"}};
		runProposalTest("test4.js", 12, 9, expectedProposals);
	}
	
	public void testFindConstructors_ThisFile_VarDeclaration_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"MyClass1(a)", "MyClass2()"}};
		runProposalTest("test5.js", 7, 8, expectedProposals);
	}
	
	public void testFindConstructors_OtherFile_VarDeclaration_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"MyClass1(a)", "MyClass2()"}};
		runProposalTest("test6.js", 0, 8, expectedProposals);
	}
	
	public void testFindConstructors_ThisFile_NestedVarDeclaration_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"MyClass7(a)"}};
		runProposalTest("test7.js", 5, 8, expectedProposals);
	}
	
	public void testFindConstructors_ThisFileAndOtherFile_NestedVarDeclaration_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"MyClass7(a)", "MyClass1(a)", "MyClass2()"}};
		runProposalTest("test7.js", 10, 11, expectedProposals);
	}
	
	public void testFindConstructors_ThisFile_ArrayReferenceDeclaration_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"test.Foo(x, y, z)"}};
		runProposalTest("test9_0.js", 7, 7, expectedProposals);
	}
	
	public void testFindConstructors_OtherFile_ArrayReferenceDeclaration_ExpressionStarted_0() throws Exception {
		String[][] expectedProposals = new String[][] {{"test.Foo(x, y, z)"}};
		runProposalTest("test9_1.js", 0, 7, expectedProposals);
	}
	
	/**
	 * <p>Run a proposal test by opening the given file and invoking content assist for
	 * each expected proposal count at the given line number and line character
	 * offset and then compare the number of proposals for each invocation (pages) to the
	 * expected number of proposals.</p>
	 * 
	 * @param fileName
	 * @param lineNum
	 * @param lineRelativeCharOffset
	 * @param expectedProposalCounts
	 * @throws Exception
	 */
	private static void runProposalTest(String fileName,
			int lineNum, int lineRelativeCharOffset,
			String[][] expectedProposals) throws Exception{
		
		IFile file = getFile(fileName);
		JavaEditor editor  = getEditor(file);
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		int offset = doc.getLineOffset(lineNum) + lineRelativeCharOffset;

		ICompletionProposal[][] pages = getProposals(editor, offset, expectedProposals.length);
		
		verifyExpectedProposal(pages, expectedProposals);
	}
	
	/**
	 * <p>Invoke content assist on the given editor at the given offset, for the given number of pages
	 * and return the results of each page</p>
	 */
	private static ICompletionProposal[][] getProposals(JavaEditor editor, int offset, int pageCount) throws Exception {
		//setup the viewer
		JavaScriptSourceViewerConfiguration configuration =
			new JavaScriptSourceViewerConfiguration(JavaScriptPlugin.getDefault().getJavaTextTools().getColorManager(),
					JavaScriptPlugin.getDefault().getCombinedPreferenceStore(), editor, IJavaScriptPartitions.JAVA_PARTITIONING);
		ISourceViewer viewer = editor.getViewer();
		ContentAssistant contentAssistant = (ContentAssistant)configuration.getContentAssistant(viewer);
		
		//get the processor
		String partitionTypeID = viewer.getDocument().getPartition(offset).getType();
		IContentAssistProcessor processor = contentAssistant.getContentAssistProcessor(partitionTypeID);

		//fire content assist session about to start
		Method privateFireSessionBeginEventMethod = ContentAssistant.class.
		        getDeclaredMethod("fireSessionBeginEvent", new Class[] {boolean.class});
		privateFireSessionBeginEventMethod.setAccessible(true);
		privateFireSessionBeginEventMethod.invoke(contentAssistant, new Object[] {Boolean.TRUE});

		//get content assist suggestions
		ICompletionProposal[][] pages = new ICompletionProposal[pageCount][];
		for(int p = 0; p < pageCount; ++p) {
			pages[p] = processor.computeCompletionProposals(viewer, offset);
		}
		
		//fire content assist session ending
		Method privateFireSessionEndEventMethod = ContentAssistant.class.
        getDeclaredMethod("fireSessionEndEvent", null);
		privateFireSessionEndEventMethod.setAccessible(true);
		privateFireSessionEndEventMethod.invoke(contentAssistant, null);
		
		return pages;
	}
	
	/**
	 * <p>Compare the expected number of proposals per page to the actual number of proposals
	 * per page</p>
	 * 
	 * @param pages
	 * @param expectedProposalCounts
	 */
	private static void verifyExpectedProposal(ICompletionProposal[][] pages, String[][] expectedProposals) {
		StringBuffer error = new StringBuffer();
		for(int page = 0; page < expectedProposals.length; ++page) {
			for(int expected = 0; expected < expectedProposals[page].length; ++expected) {
				String expectedProposal = expectedProposals[page][expected];
				boolean found = false;
				for(int suggestion = 0; suggestion < pages[page].length && !found; ++suggestion) {
					found = pages[page][suggestion].getDisplayString().startsWith(expectedProposal);	
				}
				
				if(!found) {
					error.append("\nExpected proposal was not found on page " + page + ": '" + expectedProposal + "'");
				}
			}
		}
		
		//if errors report them
		if(error.length() > 0) {
			Assert.fail(error.toString());
		}
	}
	
	/**
	 * <p>Given a file name in <code>fProject</code> attempts to get an <code>IFile</code>
	 * for it, if the file doesn't exist the test fails.</p>
	 * 
	 * @param name the name of the file to get
	 * @return the <code>IFile</code> associated with the given <code>name</code>
	 */
	private static IFile getFile(String name) {
		IFile file = fProject.getFile(CONTENT_DIR + IPath.SEPARATOR + name);
		assertTrue("Test file " + file + " can not be found", file.exists());
		
		return file;
	}
	
	/**
	 * <p>Given a <code>file</code> get an editor for it. If an editor has already
	 * been retrieved for the given <code>file</code> then return the same already
	 * open editor.</p>
	 * 
	 * <p>When opening the editor it will also standardized the line
	 * endings to <code>\n</code></p>
	 * 
	 * @param file open and return an editor for this
	 * @return <code>StructuredTextEditor</code> opened from the given <code>file</code>
	 */
	private static JavaEditor getEditor(IFile file)  {
		JavaEditor editor = (JavaEditor)fFileToEditorMap.get(file);
		
		if(editor == null) {
			try {
				IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage page = workbenchWindow.getActivePage();
				IEditorPart editorPart = IDE.openEditor(page, file, "org.eclipse.wst.jsdt.ui.CompilationUnitEditor", true);
				if(editorPart instanceof JavaEditor) {
					editor = (JavaEditor)editorPart;
				} else {
					fail("Unable to open intended editor: " + editorPart.getClass().getName());
				}
				
				if(editor != null) {
					standardizeLineEndings(editor);
					fFileToEditorMap.put(file, editor);
				} else {
					fail("Could not open editor for " + file);
				}
			} catch (Exception e) {
				fail("Could not open editor for " + file + " exception: " + e.getMessage());
			}
		}
		
		return editor;
	}
	
	/**
	 * <p>Line endings can be an issue when running tests on different OSs.
	 * This function standardizes the line endings to use <code>\n</code></p>
	 * 
	 * <p>It will get the text from the given editor, change the line endings,
	 * and then save the editor</p>
	 * 
	 * @param editor standardize the line endings of the text presented in this
	 * editor.
	 */
	private static void standardizeLineEndings(ITextEditor editor) {
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String contents = doc.get();
		contents = StringUtils.replace(contents, "\r\n", "\n");
		contents = StringUtils.replace(contents, "\r", "\n");
		doc.set(contents);
	}
	
	/**
	 * <p>This inner class is used to do set up and tear down before and
	 * after (respectively) all tests in the inclosing class have run.</p>
	 */
	private static class ContentAssistTestsSetup extends TestSetup {
		private static final String WTP_AUTOTEST_NONINTERACTIVE = "wtp.autotest.noninteractive";
		private static String previousWTPAutoTestNonInteractivePropValue = null;
		
		/**
		 * Default constructor
		 * 
		 * @param test do setup for the given test
		 */
		public ContentAssistTestsSetup(Test test) {
			super(test);
		}

		/**
		 * <p>This is run once before all of the tests</p>
		 * 
		 * @see junit.extensions.TestSetup#setUp()
		 */
		public void setUp() throws Exception {
			//setup properties
			String noninteractive = System.getProperty(WTP_AUTOTEST_NONINTERACTIVE);
			if (noninteractive != null) {
				previousWTPAutoTestNonInteractivePropValue = noninteractive;
			} else {
				previousWTPAutoTestNonInteractivePropValue = "false";
			}
			System.setProperty(WTP_AUTOTEST_NONINTERACTIVE, "true");
			
			
			//setup project
			ProjectUnzipUtility fProjUtil = new ProjectUnzipUtility();
			Location platformLocation = Platform.getInstanceLocation();
			// platform location may be null -- depends on "mode" of platform
			if (platformLocation != null) {
				File zipFile = FileUtil.makeFileFor(
					ZIP_FOLDER,
					PROJECT_NAME + ProjectUnzipUtility.ZIP_EXTENSION,
					ZIP_FOLDER);
				fProjUtil.unzipAndImport(zipFile, platformLocation.getURL().getPath());
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				fProject = root.getProject(PROJECT_NAME);
				
				if(!fProject.exists()) {
					fProject.create(new NullProgressMonitor());
				}
				if(!fProject.isOpen()) {
					fProject.open(new NullProgressMonitor());
				}
			}
		}

		/**
		 * <p>This is run once after all of the tests have been run</p>
		 * 
		 * @see junit.extensions.TestSetup#tearDown()
		 */
		public void tearDown() throws Exception {
			//close out the editors
			Iterator iter = fFileToEditorMap.values().iterator();
			while(iter.hasNext()) {
				JavaEditor editor = (JavaEditor)iter.next();
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, false);
			}
			
			//remove project
			fProject.delete(true, new NullProgressMonitor());
			
			//restore properties
			if (previousWTPAutoTestNonInteractivePropValue != null) {
				System.setProperty(WTP_AUTOTEST_NONINTERACTIVE, previousWTPAutoTestNonInteractivePropValue);
			}
		}
	}
}
