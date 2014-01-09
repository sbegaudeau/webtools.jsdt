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
package org.eclipse.wst.debug.core.tests.breakpoints;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.debug.core.tests.AbstractBreakpointTest;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLineBreakpoint;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;

/**
 * Series of test for line breakpoints
 * 
 * @since 1.1
 */
public class LineBreakpointTests extends AbstractBreakpointTest {

	/**
	 * Constructor
	 */
	public LineBreakpointTests() {
		super("Line breakpoint tests"); //$NON-NLS-1$
	}
	
	/**
	 * Tests creating a breakpoint on lines not in any closure
	 * 
	 * @throws Exception
	 */
	public void testCreateBareLine() throws Exception {
		IFile source = loadTestSource(AbstractBreakpointTest.BP_PROJECT, "", "test1.js", SRC); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull("the test source for test1.js could not be found", source); //$NON-NLS-1$
		assertTrue("The test source does not exist for test1.js", source.exists()); //$NON-NLS-1$
		String path = source.getFullPath().toString();
		createLineBreakpoints(path, new int[] {11, 12, 14, 17});
		IBreakpoint[] breakpoints = getBreakpointManager().getBreakpoints(JavaScriptDebugModel.MODEL_ID);
		assertEquals("there should have been 4 JS line breakpoints in test1.js", breakpoints.length, 4); //$NON-NLS-1$
	}
	
	/**
	 * Tests that line breakpoints can be toggled on simple lines that do not appear in any closure
	 * 
	 * @throws Exception
	 */
	public void testToggleBareLine() throws Exception {
		IFile source = loadTestSource(AbstractBreakpointTest.BP_PROJECT, "", "test1.js", SRC); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull("the test source for test1.js could not be found", source); //$NON-NLS-1$
		assertTrue("The test source does not exist for test1.js", source.exists()); //$NON-NLS-1$
		IEditorPart editor = openEditor(source);
		assertNotNull("the editor part for test1.js could not be created", editor); //$NON-NLS-1$
		IBreakpoint bp = toggleBreakpoint(editor, 11);
		assertNotNull("could not toggle breakpoint on line 11 in test1.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 12);
		assertNotNull("could not toggle breakpoint on line 12 in test1.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 14);
		assertNotNull("could not toggle breakpoint on line 14 in test1.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 17);
		assertNotNull("could not toggle breakpoint on line 17 in test1.js", bp); //$NON-NLS-1$
		
		//remove them
		bp = toggleBreakpoint(editor, 11);
		assertNull("could not toggle to remove breakpoint on line 11 in test1.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 12);
		assertNull("could not toggle to remove breakpoint on line 12 in test1.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 14);
		assertNull("could not toggle to remove breakpoint on line 14 in test1.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 17);
		assertNull("could not toggle to remove breakpoint on line 17 in test1.js", bp); //$NON-NLS-1$
		
		assertEquals("Should be no JS breakpoints left", getBreakpointManager().getBreakpoints(JavaScriptDebugModel.MODEL_ID).length, 0); //$NON-NLS-1$
	}
	
	/**
	 * Tests creating a breakpoint on lines nested within simple function closures
	 * 
	 * @throws Exception
	 */
	public void testCreateSimpleFunctionClosure() throws Exception {
		IFile source = loadTestSource(BP_PROJECT, "", "test2.js", SRC); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull("the test source for test2.js could not be found", source); //$NON-NLS-1$
		assertTrue("The test source does not exist for test2.js", source.exists()); //$NON-NLS-1$
		String path = source.getFullPath().toString();
		createLineBreakpoints(path, new int[] {13, 15, 19, 21, 25, 27, 31, 33});
		IBreakpoint[] breakpoints = getBreakpointManager().getBreakpoints(JavaScriptDebugModel.MODEL_ID);
		assertEquals("there should have been 8 JS line breakpoints in test1.js", breakpoints.length, 8); //$NON-NLS-1$
	}
	
	/**
	 * Tests toggling a line breakpoint on lines nested within simple function closures
	 * 
	 * @throws Exception
	 */
	public void testToggleSimpleFunctionClosure() throws Exception {
		IFile source = loadTestSource(BP_PROJECT, "", "test2.js", SRC); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull("the test source for test2.js could not be found", source); //$NON-NLS-1$
		IEditorPart editor = openEditor(source);
		assertNotNull("the editor part for test2.js could not be created", editor); //$NON-NLS-1$
		IBreakpoint bp = toggleBreakpoint(editor, 13);
		assertNotNull("could not toggle breakpoint on line 13 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 15);
		assertNotNull("could not toggle breakpoint on line 15 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 19);
		assertNotNull("could not toggle breakpoint on line 19 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 21);
		assertNotNull("could not toggle breakpoint on line 21 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 25);
		assertNotNull("could not toggle breakpoint on line 25 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 27);
		assertNotNull("could not toggle breakpoint on line 27 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 31);
		assertNotNull("could not toggle breakpoint on line 31 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 33);
		assertNotNull("could not toggle breakpoint on line 33 in test2.js", bp); //$NON-NLS-1$
		
		//remove them
		bp = toggleBreakpoint(editor, 13);
		assertNull("could not toggle to remove breakpoint on line 11 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 15);
		assertNull("could not toggle to remove breakpoint on line 15 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 19);
		assertNull("could not toggle to remove breakpoint on line 19 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 21);
		assertNull("could not toggle to remove breakpoint on line 21 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 25);
		assertNull("could not toggle to remove breakpoint on line 25 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 27);
		assertNull("could not toggle to remove breakpoint on line 27 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 31);
		assertNull("could not toggle to remove breakpoint on line 31 in test2.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 33);
		assertNull("could not toggle to remove breakpoint on line 33 in test2.js", bp); //$NON-NLS-1$
		
		assertEquals("Should be no JS breakpoints left", getBreakpointManager().getBreakpoints(JavaScriptDebugModel.MODEL_ID).length, 0); //$NON-NLS-1$
	}
	
	/**
	 * Tests creating line breakpoints at various locations in the code snippets from 
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=313013
	 * 
	 * @throws Exception
	 */
	public void testCreateBug313013() throws Exception {
		IFile source = loadTestSource(BP_PROJECT, "", "test3.js", SRC); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull("the test source for test3.js could not be found", source); //$NON-NLS-1$
		assertTrue("The test source does not exist for test3.js", source.exists()); //$NON-NLS-1$
		createLineBreakpoints(source.getFullPath().toString(), new int[] {13, 16, 20, 25});
		IBreakpoint[] bps = getBreakpointManager().getBreakpoints(JavaScriptDebugModel.MODEL_ID);
		assertEquals("there should have been 4 JS line breakpoints in test3.js", bps.length, 4); //$NON-NLS-1$
	}
	
	/**
	 * Tests toggling breakpoints at various locations in the code from 
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=313013
	 * 
	 * @throws Exception
	 */
	public void testToggleBug313013() throws Exception {
		IFile source = loadTestSource(AbstractBreakpointTest.BP_PROJECT, "", "test3.js", SRC); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull("the test source for test3.js could not be found", source); //$NON-NLS-1$
		assertTrue("The test source does not exist for test3.js", source.exists()); //$NON-NLS-1$
		IEditorPart editor = openEditor(source);
		assertNotNull("the editor part for test3.js could not be created", editor); //$NON-NLS-1$
		IBreakpoint bp = toggleBreakpoint(editor, 13);
		assertNotNull("could not toggle breakpoint on line 13 in test3.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 16);
		assertNotNull("could not toggle breakpoint on line 16 in test3.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 20);
		assertNotNull("could not toggle breakpoint on line 20 in test3.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 25);
		assertNotNull("could not toggle breakpoint on line 25 in test3.js", bp); //$NON-NLS-1$
		
		//remove them
		bp = toggleBreakpoint(editor, 13);
		assertNull("could not toggle to remove breakpoint on line 13 in test3.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 16);
		assertNull("could not toggle to remove breakpoint on line 16 in test3.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 20);
		assertNull("could not toggle to remove breakpoint on line 20 in test3.js", bp); //$NON-NLS-1$
		bp = toggleBreakpoint(editor, 25);
		assertNull("could not toggle to remove breakpoint on line 25 in test3.js", bp); //$NON-NLS-1$
		
		assertEquals("Should be no JS breakpoints left", getBreakpointManager().getBreakpoints(JavaScriptDebugModel.MODEL_ID).length, 0); //$NON-NLS-1$
	}
	
	/**
	 * Tests that the breakpoint is correctly moved to the known next best line
	 * Specifically this test tries to toggle a line breakpoint on the closing brace of an if statement
	 * 
	 * @throws Exception
	 */
	public void testToggleValidatedLocIfEndBrace() throws Exception {
		IFile source = loadTestSource(BP_PROJECT, "", "test4.js", SRC); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull("the test source for test4.js could not be found", source); //$NON-NLS-1$
		assertTrue("The test source does not exist for test4.js", source.exists()); //$NON-NLS-1$
		IEditorPart editor = openEditor(source);
		assertNotNull("the editor part for test4.js could not be created", editor); //$NON-NLS-1$
		IBreakpoint bp = toggleBreakpoint(editor, 13);
		assertTrue("the breakpoint must be a line breakpoint", bp instanceof IJavaScriptLineBreakpoint); //$NON-NLS-1$
		IJavaScriptLineBreakpoint lbp = (IJavaScriptLineBreakpoint) bp;
		assertEquals("the new line number for the validated breakpoint should be line 15", lbp.getLineNumber(), 15); //$NON-NLS-1$
	}
	
	/**
	 * Tests that the breakpoint is correctly moved to the known next best line.
	 * Specifically this test tries to toggle a line breakpoint in an else clause.
	 * 
	 * @throws Exception
	 */
	public void testToggleValidatedLocElse() throws Exception {
		IFile source = loadTestSource(BP_PROJECT, "", "test4.js", SRC); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotNull("the test source for test4.js could not be found", source); //$NON-NLS-1$
		assertTrue("The test source does not exist for test4.js", source.exists()); //$NON-NLS-1$
		IEditorPart editor = openEditor(source);
		assertNotNull("the editor part for test4.js could not be created", editor); //$NON-NLS-1$
		IBreakpoint bp = toggleBreakpoint(editor, 14);
		assertTrue("the breakpoint must be a line breakpoint", bp instanceof IJavaScriptLineBreakpoint); //$NON-NLS-1$
		IJavaScriptLineBreakpoint lbp = (IJavaScriptLineBreakpoint) bp;
		assertEquals("the new line number for the validated breakpoint should be line 15", lbp.getLineNumber(), 15); //$NON-NLS-1$
	}
}
