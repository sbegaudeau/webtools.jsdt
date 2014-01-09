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
package org.eclipse.wst.jsdt.ui.tests.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.extensions.TestSetup;
import junit.framework.Assert;
import junit.framework.Test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.search.indexing.IndexManager;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaEditor;

/**
 * <p>Sets up a test project.</p>
 * 
 * @see org.eclipse.wst.jsdt.ui.tests.utils
 * @see org.eclipse.wst.jsdt.web.ui.tests.internal
 */
public class TestProjectSetup extends TestSetup {
	/** preference for ignoring WTP UI */
	private static final String WTP_AUTOTEST_NONINTERACTIVE = "wtp.autotest.noninteractive";
	
	/** The location of the testing files */
	protected static final String TESTING_RESOURCES_DIR = "testresources";

	/** previous value for hiding WTP UI */
	private String fPreviousWTPAutoTestNonInteractivePropValue = null;
	
	/** Name of the project the tests use */
	private final String fProjectName;
	
	/** The project that the tests use */
	private IProject fProject;
	
	/** The root directory to start with when looking for files */
	private final String fRootDirectory;
	
	/**
	 * <p>
	 * <code>true</code> if should delete project on tear down,
	 * <code>false</code> to leave it for other tests to use.
	 * </p>
	 */
	private final boolean fDeleteOnTearDown;
	
	/**
	 * Used to keep track of the already open editors so that the tests don't go through
	 * the trouble of opening the same editors over and over again
	 */
	private Map fFileToEditorMap = new HashMap();

	/**
	 * <p>
	 * Path to the library file to import into this test project.
	 * </p>
	 * 
	 * @see #fLibraryFilesDestinationPath
	 */
	private final String fLibraryFilePath;

	/**
	 * <p>
	 * Path relative to the test project that the given {@link #fLibraryFilePath} should be
	 * imported into, if one is given. Or <code>null</code> if the given
	 * {@link #fLibraryFilePath} should be imported into the test project root
	 * </p>
	 * 
	 * @see #fLibraryFilePath
	 */
	private final String fLibraryFilesDestinationPath;

	
	
	/**
	 * <p>
	 * <b>NOTE:</b> will not delete the project on tear down so other tests can use it.
	 * </p>
	 * 
	 * @param test
	 *            do setup for the given test
	 * @param projectName
	 *            name of the project to set up
	 * @param rootDirectory
	 *            path to the root directory to look for all files under, or <code>null</code> if
	 *            look directly under project root
	 */
	public TestProjectSetup(Test test, String projectName, String rootDirectory) {
		this(test,projectName,rootDirectory, false, null, null);
	}
	
	/**
	 * @param test
	 *            do setup for the given test
	 * @param projectName
	 *            name of the project to set up
	 * @param rootDirectory
	 *            path to the root directory to look for all files under, or <code>null</code> if
	 *            look directly under project root
	 * @param deleteOnTearDown
	 *            <code>true</code> if should delete project on tear down, <code>false</code> to
	 *            leave it for other tests to use.
	 */
	public TestProjectSetup(Test test, String projectName, String rootDirectory, boolean deleteOnTearDown) {
		this(test,projectName,rootDirectory,deleteOnTearDown, null,null);
	}
	
	
	/**
	 * @param test
	 *            do setup for the given test
	 * @param projectName
	 *            name of the project to set up
	 * @param rootDirectory
	 *            path to the root directory to look for all files under, or <code>null</code> if
	 *            look directly under project root
	 * @param deleteOnTearDown
	 *            <code>true</code> if should delete project on tear down, <code>false</code> to
	 *            leave it for other tests to use.
	 * @param libraryFilePath
	 *            Path to the library file to import into this test project, relative to TESTING_RESOURCES_DIR
	 * @param libraryFilesDestinationPath
	 *            Path relative to the test project that the given {@link #fLibraryFilePath}
	 *            should be
	 *            imported into, if one is given. Or <code>null</code> if the given
	 *            {@link #fLibraryFilePath} should be imported into the test project root
	 */
	public TestProjectSetup(Test test, String projectName, String rootDirectory, boolean deleteOnTearDown,
			String libraryFilePath, String libraryFilesDestinationPath) {
		
		super(test);

		this.fProjectName = projectName;
		this.fRootDirectory = rootDirectory;
		this.fDeleteOnTearDown = deleteOnTearDown;
		this.fLibraryFilePath = libraryFilePath;
		this.fLibraryFilesDestinationPath = libraryFilesDestinationPath;
	}
	
	
	/**
	 * @return {@link IProject} that was setup
	 */
	public IProject getProject() {
		return this.fProject;
	}
	
	/**
	 * <p>
	 * Given a <code>file</code> get an editor for it. If an editor has already been retrieved for
	 * the given <code>file</code> then return the same already open editor.
	 * </p>
	 * 
	 * <p>
	 * When opening the editor it will also standardized the line endings to <code>\n</code>
	 * </p>
	 * 
	 * @param file
	 *            open and return an editor for this
	 * @return <code>StructuredTextEditor</code> opened from the given <code>file</code>
	 */
	public JavaEditor getEditor(IFile file) {
		JavaEditor editor = (JavaEditor) fFileToEditorMap.get(file);

		if(editor == null) {
			try {
				IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage page = workbenchWindow.getActivePage();
				IEditorPart editorPart = IDE.openEditor(page, file, true, true);
				if(editorPart instanceof JavaEditor) {
					editor = (JavaEditor) editorPart;
				} else {
					Assert.fail("Unable to open JS editor");
				}

				if(editor != null) {
					standardizeLineEndings(editor);
					fFileToEditorMap.put(file, editor);
				} else {
					Assert.fail("Could not open editor for " + file);
				}
			} catch(Exception e) {
				Assert.fail("Could not open editor for " + file + " exception: " + e.getMessage());
			}
		}

		return editor;
	}
	
	/**
	 * <p>
	 * Given a file path in the test project attempts to get an <code>IFile</code> for it, if the
	 * file doesn't exist the test fails.
	 * </p>
	 * 
	 * @param path
	 *            the name of the file to get
	 * 
	 * @return the {@link IFile} associated with the given file path
	 */
	public IFile getFile(String path) {
		IFile file = null;
		
		if(this.fRootDirectory != null) {
			file = this.fProject.getFile(this.fRootDirectory + IPath.SEPARATOR + path);
		} else {
			file = this.fProject.getFile(path);
		}
		
		Assert.assertTrue("Test file " + file + " can not be found", file.exists());

		return file;
	}
	
	/**
	 * <p>
	 * Edits the file given by the fileName. The number of characters indicated by length are
	 * replaced by the given text beginning at the character located at the given line number and
	 * the line character offset.
	 * </p>
	 * 
	 * @param fileName
	 * @param lineNum
	 * @param lineRelativeCharOffset
	 * @param length
	 * @param text
	 * @throws Exception
	 */
	public void editFile(String fileName, int lineNum, int lineRelativeCharOffset, int length, String text)
			throws Exception {
		
		IFile file = this.getFile(fileName);
		JavaEditor editor = this.getEditor(file);
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());

		int offset = doc.getLineOffset(lineNum) + lineRelativeCharOffset;
		doc.replace(offset, length, text);
		
		waitForIndexManager();
	}

	/**
	 * <p>
	 * This is run Once while the test is being setUp for all the tests.
	 * </p>
	 * <p>
	 * Designed to be overridden by content assist test suite Implementers to do additional test
	 * setup.
	 * </p>
	 * 
	 */
	public void additionalSetUp() throws Exception {
		// default do nothing
	}
	
	/**
	 * <p>
	 * This is run once before all of the tests
	 * </p>
	 * 
	 * @see junit.extensions.TestSetup#setUp()
	 */
	public void setUp() throws Exception {
		// setup properties
		String noninteractive = System.getProperty(WTP_AUTOTEST_NONINTERACTIVE);
		if(noninteractive != null) {
			fPreviousWTPAutoTestNonInteractivePropValue = noninteractive;
		} else {
			fPreviousWTPAutoTestNonInteractivePropValue = "false";
		}
		System.setProperty(WTP_AUTOTEST_NONINTERACTIVE, "true");

		// get project
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		fProject = root.getProject(this.fProjectName);

		// setup project if it is not yet setup
		if(fProject == null || !fProject.exists()) {
			fProject = BundleResourceUtil.createSimpleProject(this.fProjectName, null, null);
		}
		BundleResourceUtil.copyBundleEntriesIntoWorkspace(TESTING_RESOURCES_DIR + IPath.SEPARATOR + this.fProjectName,
					IPath.SEPARATOR + this.fProjectName);
		
		// import library file if one is specified
		if(this.fLibraryFilePath != null) {
			//calculate destination path
			
			String libraryFilesDestinationPath = "";
			if(this.fLibraryFilesDestinationPath == null) {
				libraryFilesDestinationPath = IPath.SEPARATOR + this.fProjectName;
			}else{
				libraryFilesDestinationPath = IPath.SEPARATOR + this.fLibraryFilesDestinationPath;
			}
			
			//copy the library file contents to the destination
			BundleResourceUtil.copyBundleEntryIntoWorkspace(TESTING_RESOURCES_DIR + IPath.SEPARATOR + this.fLibraryFilePath,
						libraryFilesDestinationPath);
		}

		// run any additional test setup
		this.additionalSetUp();

		// give the workspace a second to settle before running tests
		Thread.sleep(1000);
		waitForIndexManager();
	}

	/**
	 * Imports the contents of a zip file into the project.
	 * 
	 * @param fLibraryFilesZipPath
	 * 				name of the zip file to import.
	 * @param fLibraryFilesDestinationPath
	 * 				location to import files relative to project.
	 * @throws Exception
	 */
	public void importZip(String fLibraryFilesZipPath, String fLibraryFilesDestinationPath) throws Exception {
		
		if(fLibraryFilesZipPath != null) {
			//calculate destination path
			IPath libraryFilesDestinationPath = null;
			if(fLibraryFilesDestinationPath != null) {
				libraryFilesDestinationPath = new Path(IPath.SEPARATOR + this.fProjectName + IPath.SEPARATOR + fLibraryFilesDestinationPath);
			} else {
				libraryFilesDestinationPath = new Path(IPath.SEPARATOR + this.fProjectName);
			}
			libraryFilesDestinationPath.addTrailingSeparator();

			//copy the library zip contents to the destination
			BundleResourceUtil.copyBundleZippedEntriesIntoWorkspace(TESTING_RESOURCES_DIR + IPath.SEPARATOR + fLibraryFilesZipPath,
						libraryFilesDestinationPath);
		}
		
		// give the workspace a second to settle before continuing tests
		Thread.sleep(1000);
		waitForIndexManager();
	}
		
	
	
	/**
	 * <p>
	 * This is run once after all of the tests have been run
	 * </p>
	 * 
	 * @see junit.extensions.TestSetup#tearDown()
	 */
	public void tearDown() throws Exception {
		// close out the editors
		Iterator iter = fFileToEditorMap.values().iterator();
		while(iter.hasNext()) {
			AbstractTextEditor editor = (AbstractTextEditor) iter.next();
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, false);
		}
		this.fFileToEditorMap.clear();
		
		//delete the project
		if(this.fDeleteOnTearDown) {
			this.fProject.close(new NullProgressMonitor());			
			this.fProject.delete(true, new NullProgressMonitor());
		}

		// restore properties
		if(fPreviousWTPAutoTestNonInteractivePropValue != null) {
			System.setProperty(WTP_AUTOTEST_NONINTERACTIVE, fPreviousWTPAutoTestNonInteractivePropValue);
		}
	}
	
	/**
	 * <p>
	 * Line endings can be an issue when running tests on different OSs. This function standardizes
	 * the line endings to use <code>\n</code>
	 * </p>
	 * 
	 * <p>
	 * It will get the text from the given editor, change the line endings, and then save the editor
	 * </p>
	 * 
	 * @param editor
	 *            standardize the line endings of the text presented in this
	 *            editor.
	 */
	private static void standardizeLineEndings(ITextEditor editor) {
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String contents = doc.get();
		contents = StringUtils.replace(contents, "\r\n", "\n");
		contents = StringUtils.replace(contents, "\r", "\n");
		doc.set(contents);
	}
	
	/**
	 * <p>
	 * Wait for the index manager with a time out of 10 seconds.
	 * </p>
	 */
	private static void waitForIndexManager() {
		waitForIndexManager(10000);
	}

	/**
	 * <p>
	 * Wait for the index manager for the given max time.
	 * </p>
	 * 
	 * @param max
	 *            maximum amount of time to wait for the index manager
	 */
	private static void waitForIndexManager(long max) {
		// Wait for the end of indexing
		IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();
		long maxWaits = max / 10;
		while (indexManager.awaitingJobsCount() > 0 && maxWaits-- > 0) {
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				return;
			}
		}
	}
	
	/**
	 * 
	 * @return Project's root directory.
	 */
	public String getRootDirectory(){
		return fRootDirectory;
	}
}