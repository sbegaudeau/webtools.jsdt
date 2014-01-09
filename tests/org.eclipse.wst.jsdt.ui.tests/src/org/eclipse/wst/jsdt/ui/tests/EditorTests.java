/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.ui.tests;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.wst.jsdt.ui.JavaScriptUI;
import org.eclipse.wst.jsdt.ui.tests.internal.JsStorageEditorInput;

/**
 * @author nitin
 * 
 */
public class EditorTests extends TestCase {

	/**
	 * 
	 */
	public EditorTests() {
	}

	/**
	 * @param name
	 */
	public EditorTests(String name) {
		super(name);
	}

	public void testOpenEditorWithStorageEditorInput() throws PartInitException {
		IEditorInput input = new JsStorageEditorInput("var a = {};", new Path("/" + getName() + "/testfile.js"));

		IEditorPart editor = IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),
				input, JavaScriptUI.ID_CU_EDITOR, true);
		// Now add the problems we found
		assertTrue("unexpected editor opened", editor instanceof JavaEditor && editor instanceof ITextEditor);
		IAnnotationModel annotationModel = ((ITextEditor) editor).getDocumentProvider().getAnnotationModel(input);
		assertNotNull("no annotation model present", annotationModel);
		IDocument document = ((ITextEditor) editor).getDocumentProvider().getDocument(input);
		assertNotNull("no text document present", document);
		assertTrue("text document is empty", document.getLength() > 0);

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().layout(true, true);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().redraw();

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, false);
	}

	public void testOpenEditorWithEmptyStorageEditorInput() throws PartInitException {
		IEditorInput input = new JsStorageEditorInput("", new Path("/" + getName() + "/testfile.js"));

		IEditorPart editor = IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),
				input, JavaScriptUI.ID_CU_EDITOR, true);
		// Now add the problems we found
		assertTrue("unexpected editor opened", editor instanceof JavaEditor && editor instanceof ITextEditor);
		IAnnotationModel annotationModel = ((ITextEditor) editor).getDocumentProvider().getAnnotationModel(input);
		assertNotNull("no annotation model present", annotationModel);
		IDocument document = ((ITextEditor) editor).getDocumentProvider().getDocument(input);
		assertNotNull("no text document present", document);
		assertTrue("text document is not empty", document.getLength() == 0);

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(editor, false);
	}
}
