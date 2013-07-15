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
package org.eclipse.wst.jsdt.ui.tests.hyperlink;

import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaElementHyperlink;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.JavaElementHyperlinkDetector;
import org.eclipse.wst.jsdt.ui.tests.utils.TestProjectSetup;

/**
 * <p>
 * Helpful utilities for running hyperlink tests.
 * </p>
 */
public class HyperLinkTestUtilities {
	/**
	 * @param testProject
	 * @param lineNum
	 * @param lineRelativeCharOffset
	 * @param filePath
	 * @param filesExpected
	 * @throws Exception
	 */
	public static void checkHyperlink(TestProjectSetup testProject, int lineNum, int lineRelativeCharOffset, String filePath, ArrayList filesExpected)
			throws Exception {

		final ArrayList expectedFilesNotOpened = new ArrayList(filesExpected);

		class EditorListener implements IPartListener2 {

			public void partActivated(IWorkbenchPartReference partRef) {
			}

			public void partBroughtToTop(IWorkbenchPartReference partRef) {
			}

			public void partClosed(IWorkbenchPartReference partRef) {
			}

			public void partDeactivated(IWorkbenchPartReference partRef) {
			}

			public void partOpened(IWorkbenchPartReference partRef) {
				IWorkbenchPart part = partRef.getPart(false);
				if(part instanceof JavaEditor) {

					String fileOpened = part.getTitle();
					int index = 0;
					boolean found = false;
					String item = new String();
					while(index < expectedFilesNotOpened.size() && !found) {
						item = (String) expectedFilesNotOpened.get(index);
						if(item.equals(fileOpened)) {
							found = true;
							expectedFilesNotOpened.remove(index);
						} else {
							index++;
						}
					}
				}
			}

			public void partHidden(IWorkbenchPartReference partRef) {
			}

			public void partVisible(IWorkbenchPartReference partRef) {
			}

			public void partInputChanged(IWorkbenchPartReference partRef) {
			}
		}

		EditorListener listener = null;
		listener = new EditorListener();
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = workbenchWindow.getActivePage();
		page.addPartListener(listener);

		IHyperlink[] hyperLinks = getHyperlinks(testProject, lineNum, lineRelativeCharOffset, filePath);

		Assert.assertTrue("No hyperlinks found", hyperLinks != null && hyperLinks.length > 0);
		
		for(int i = 0; i < hyperLinks.length; i++) {
			if(hyperLinks[i] instanceof JavaElementHyperlink) {
				hyperLinks[i].open();
			}
		}
		
		Assert.assertTrue("Not all expected pages were opened.", expectedFilesNotOpened.isEmpty());
		
		page.removePartListener(listener);
	}
	
	/**
	 * 
	 * @param lineNum
	 * @param lineRelativeCharOffset
	 * @param filePath
	 * @return
	 * @throws Exception
	 *             gets the hyperlink for the element
	 */

	public static IHyperlink[] getHyperlinks(TestProjectSetup testProject, int lineNum, int lineRelativeCharOffset, String filePath) throws Exception {
		IFile file = testProject.getFile(filePath);
		JavaEditor editor = testProject.getEditor(file);
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		int offset = doc.getLineOffset(lineNum) + lineRelativeCharOffset;

		ITextViewer viewer = (ITextViewer) editor.getViewer();
		editor.setHighlightRange(offset, 0, true);
		Region region = new Region(offset, 0);

		JavaElementHyperlinkDetector hyperlinkDetector = new JavaElementHyperlinkDetector();
		hyperlinkDetector.setContext(editor);
		Thread.sleep(1000);
		IHyperlink[] hyperlinks = hyperlinkDetector.detectHyperlinks(viewer, region, true);

		return hyperlinks;
	}
}