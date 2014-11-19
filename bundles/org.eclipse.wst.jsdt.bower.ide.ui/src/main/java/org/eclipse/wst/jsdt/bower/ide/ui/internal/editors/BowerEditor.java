/*******************************************************************************
 * Copyright (c) 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.bower.ide.ui.internal.editors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.wst.jsdt.bower.core.api.BowerJson;
import org.eclipse.wst.jsdt.bower.core.api.BowerReaders;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.editors.overview.BowerOverviewPage;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.utils.I18n;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.utils.I18nKeys;

/**
 * The bower.json editor contains three tabs. In the first tab, an overview of the file bower.json is
 * displayed, on the second page, all the dependencies of the project are listed and on the last tab, the
 * content of the file is visible for manual editions.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerEditor extends FormEditor {

	/**
	 * The content of bower.json
	 */
	private BowerJson bowerJson;

	/**
	 * The overview page.
	 */
	private BowerOverviewPage overviewPage;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.forms.editor.FormEditor#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);

		if (input instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput)input;
			IFile bowerJsonFile = fileEditorInput.getFile();
			try {
				this.bowerJson = BowerReaders.getBowerJson(bowerJsonFile.getContents());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		this.overviewPage = new BowerOverviewPage(this);
		try {
			this.addPage(this.overviewPage);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the bowerJson.
	 *
	 * @return The bowerJson
	 */
	public BowerJson getBowerJson() {
		return this.bowerJson;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#getTitle()
	 */
	@Override
	public String getTitle() {
		return I18n.getString(I18nKeys.BOWER_EDITOR_TITLE);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		IEditorInput editorInput = this.getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput)editorInput;
			IFile file = fileEditorInput.getFile();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			byte[] bytes = gson.toJson(bowerJson).getBytes();
			InputStream inputStream = new ByteArrayInputStream(bytes);
			try {
				file.setContents(inputStream, true, true, monitor);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return this.getEditorInput() instanceof IFileEditorInput;
	}
}
