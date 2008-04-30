/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.internal.ui.text.spelling;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.spelling.ISpellingEngine;
import org.eclipse.ui.texteditor.spelling.ISpellingProblemCollector;
import org.eclipse.ui.texteditor.spelling.SpellingContext;
import org.eclipse.ui.texteditor.spelling.SpellingProblem;
import org.eclipse.ui.texteditor.spelling.SpellingService;
import org.eclipse.wst.jsdt.core.IProblemRequestor;

/**
 * Reconcile strategy for spell checking comments.
 *
 * Modified to use the JavaSpellingEngine always and not consult the SpellingService of the platform. It only checks
 * to comply with the Enabled settings.
 * 
 * @since 3.1
 */
public class JavaSpellingReconcileStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension { //extends SpellingReconcileStrategy {

	//Fix to only call the JavaScript Spell checker 
	//This disconnects from the SpellingService that the platform provides but it is still
	//driven by the enable setting.
	protected ISpellingEngine spellingEngine = new JavaSpellingEngine();
	
	
	/**
	 * Spelling problem collector that forwards {@link SpellingProblem}s as
	 * {@link org.eclipse.wst.jsdt.core.compiler.IProblem}s to the {@link org.eclipse.wst.jsdt.core.compiler.IProblemRequestor}.
	 */
	private class SpellingProblemCollector implements ISpellingProblemCollector {

		/*
		 * @see org.eclipse.ui.texteditor.spelling.ISpellingProblemCollector#accept(org.eclipse.ui.texteditor.spelling.SpellingProblem)
		 */
		public void accept(SpellingProblem problem) {
			IProblemRequestor requestor= fRequestor;
			if (requestor != null) {
				try {
					int line= fDocument.getLineOfOffset(problem.getOffset()) + 1;
					String word= fDocument.get(problem.getOffset(), problem.getLength());
					boolean dictionaryMatch= false;
					boolean sentenceStart= false;
					if (problem instanceof JavaSpellingProblem) {
						dictionaryMatch= ((JavaSpellingProblem)problem).isDictionaryMatch();
						sentenceStart= ((JavaSpellingProblem) problem).isSentenceStart();
					}
					// see: https://bugs.eclipse.org/bugs/show_bug.cgi?id=81514
					IEditorInput editorInput= fEditor.getEditorInput();
					if (editorInput != null) {
						CoreSpellingProblem iProblem= new CoreSpellingProblem(problem.getOffset(), problem.getOffset() + problem.getLength() - 1, line, problem.getMessage(), word, dictionaryMatch, sentenceStart, fDocument, editorInput.getName());
						requestor.acceptProblem(iProblem);
					}
				} catch (BadLocationException x) {
					// drop this SpellingProblem
				}
			}
		}

		/*
		 * @see org.eclipse.ui.texteditor.spelling.ISpellingProblemCollector#beginCollecting()
		 */
		public void beginCollecting() {
			if (fRequestor != null)
				fRequestor.beginReporting();
		}

		/*
		 * @see org.eclipse.ui.texteditor.spelling.ISpellingProblemCollector#endCollecting()
		 */
		public void endCollecting() {
			if (fRequestor != null)
				fRequestor.endReporting();
		}
	}

	
	/** The id of the problem */
	public static final int SPELLING_PROBLEM_ID= 0x80000000;

//	/** Properties file content type */
//	private static final IContentType JAVA_CONTENT_TYPE= Platform.getContentTypeManager().getContentType(JavaScriptCore.JAVA_SOURCE_CONTENT_TYPE);

	/** The text editor to operate on. */
	private ITextEditor fEditor;

	/** The problem requester. */
	private IProblemRequestor fRequestor;

	/** The text editor to operate on. */
//	private ISourceViewer fViewer;

	/** The document to operate on. */
	private IDocument fDocument;

	/** The progress monitor. */
	private IProgressMonitor fProgressMonitor;

//	private SpellingService fSpellingService;
	
	private ISpellingProblemCollector fSpellingProblemCollector;
	
	/** The spelling context containing the Java source content type. */
	private SpellingContext fSpellingContext;
	
	/**
	 * Creates a new comment reconcile strategy.
	 *
	 * @param viewer the source viewer
	 * @param editor the text editor to operate on
	 */
	public JavaSpellingReconcileStrategy(ISourceViewer viewer, ITextEditor editor) {
		
		Assert.isNotNull(viewer);
//		fViewer= viewer;
		fSpellingContext= new SpellingContext();
		
		fEditor= editor;
	}

	/*
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.IRegion)
	 */
	public void reconcile(IRegion region) {
		if (fRequestor != null && isSpellingEnabled()){
			//super.reconcile(region);
			
			if (getAnnotationModel() == null || fSpellingProblemCollector == null)
				return;
			
			try {
				fSpellingProblemCollector.beginCollecting();
				
				ISafeRunnable runnable= new ISafeRunnable() {
					public void run() throws Exception {
						spellingEngine.check( fDocument, new IRegion[] { new Region(0, fDocument.getLength()) }, fSpellingContext, fSpellingProblemCollector, fProgressMonitor );
					}
					public void handleException(Throwable x) {
					}
				};
				SafeRunner.run(runnable);					
				
			} finally {
				fSpellingProblemCollector.endCollecting();
			}
			
		}
	}
	
	/*
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#setDocument(org.eclipse.jface.text.IDocument)
	 */
	public void setDocument(IDocument document) {
		fDocument= document;
		fSpellingProblemCollector= createSpellingProblemCollector();
		
		updateProblemRequester();
	}
	
	private boolean isSpellingEnabled() {
		return EditorsUI.getPreferenceStore().getBoolean(SpellingService.PREFERENCE_SPELLING_ENABLED);
	}

	/*
	 * @see org.eclipse.ui.texteditor.spelling.SpellingReconcileStrategy#createSpellingProblemCollector()
	 * @since 3.3
	 */
	protected ISpellingProblemCollector createSpellingProblemCollector() {
		return new SpellingProblemCollector();
	}

	/**
	 * Update the problem requester based on the current editor
	 */
	private void updateProblemRequester() {
		IAnnotationModel model= getAnnotationModel();
		fRequestor= (model instanceof IProblemRequestor) ? (IProblemRequestor) model : null;
	}
	
	/*
	 * @see org.eclipse.ui.texteditor.spelling.SpellingReconcileStrategy#getAnnotationModel()
	 * @since 3.3
	 */
	protected IAnnotationModel getAnnotationModel() {
		final IDocumentProvider documentProvider= fEditor.getDocumentProvider();
		if (documentProvider == null)
			return null;
		return documentProvider.getAnnotationModel(fEditor.getEditorInput());
	}

	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile(subRegion);
	}

	public void initialReconcile() {
		reconcile(new Region(0, fDocument.getLength()));
	}

	public void setProgressMonitor(IProgressMonitor monitor) {
		fProgressMonitor= monitor;
	}
}
