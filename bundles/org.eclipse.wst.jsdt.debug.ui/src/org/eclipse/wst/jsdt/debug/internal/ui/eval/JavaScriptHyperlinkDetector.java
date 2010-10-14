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
package org.eclipse.wst.jsdt.debug.internal.ui.eval;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;
import org.eclipse.wst.jsdt.internal.ui.text.JavaWordFinder;

/**
 * Hyper-link detector for stepping into selections in the JavaScript editor
 * 
 * @since 1.0
 */
public class JavaScriptHyperlinkDetector extends AbstractHyperlinkDetector {

	/**
	 * Hyper-link for stepping into the selection
	 */
	class StepIntoSelectionHyperlink implements IHyperlink {
		
		private IRegion fRegion = null;
		
		/**
		 * Constructor
		 * @param region
		 */
		public StepIntoSelectionHyperlink(IRegion region) {
			fRegion = region;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.text.hyperlink.IHyperlink#getHyperlinkRegion()
		 */
		public IRegion getHyperlinkRegion() {
			return fRegion;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.text.hyperlink.IHyperlink#getHyperlinkText()
		 */
		public String getHyperlinkText() {
			return Messages.hyperlink_step_into;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.text.hyperlink.IHyperlink#getTypeLabel()
		 */
		public String getTypeLabel() {
			return null;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.text.hyperlink.IHyperlink#open()
		 */
		public void open() {
			StepIntoSelectionActionDelegate delegate = new StepIntoSelectionActionDelegate(fRegion);
			delegate.init(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
			delegate.run(null);
		}
	}
	
	/**
	 * Constructor
	 */
	public JavaScriptHyperlinkDetector() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.hyperlink.IHyperlinkDetector#detectHyperlinks(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion, boolean)
	 */
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		ITextEditor editor = (ITextEditor) getAdapter(ITextEditor.class);
		if(editor != null) {
			IJavaScriptStackFrame frame = EvaluationManager.getManager().getEvaluationContext(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
			if (frame == null) {
				return null;
			}
			IEditorInput input = editor.getEditorInput();
			IJavaScriptElement element = StepIntoSelectionUtils.getJavaElement(input);
			int offset = region.getOffset();
			if(element != null) {
				try {
					IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
					if(document != null) {
						IRegion wregion = JavaWordFinder.findWord(document, offset);
						if(wregion != null) {
							IFunction method = StepIntoSelectionUtils.getFunction(new TextSelection(document, wregion.getOffset(), wregion.getLength()), element);
							if (method != null) {
								return new IHyperlink[] {new StepIntoSelectionHyperlink(wregion)};
							}
						}
					}
				}
				catch(JavaScriptModelException jme) {
					JavaScriptDebugUIPlugin.log(jme);
				}
			}
		}
		return null;
	}

}
