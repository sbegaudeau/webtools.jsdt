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
package org.eclipse.wst.jsdt.bower.ide.ui.internal.editors.overview;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.editors.BowerEditor;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.editors.FormLayoutFactory;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerOverviewPage extends FormPage {

	/**
	 * The identifier of the form page.
	 */
	private static final String FORM_PAGE_ID = "org.eclipse.wst.jsdt.bower.ide.ui.editor.pages.overview"; //$NON-NLS-1$

	private GeneralInformationSection generalInformationSection;

	private IgnoredFilesSection ignoredFilesSection;

	private DocumentationSection documentationSection;

	private ActionsSection actionsSection;

	/**
	 * The constructor.
	 */
	public BowerOverviewPage(BowerEditor editor) {
		super(editor, FORM_PAGE_ID, "Overview");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.decorateFormHeading(form.getForm());

		form.setImage(null);
		form.setText("Overview Title");

		this.fillBody(managedForm, toolkit);
	}

	/**
	 * Fills the body of the form.
	 *
	 * @param managedForm
	 *            The managed form
	 * @param toolkit
	 *            The toolkit
	 */
	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		body.setLayout(FormLayoutFactory.createFormTableWrapLayout(true, 2));

		Composite left = toolkit.createComposite(body);
		left.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		left.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		this.generalInformationSection = new GeneralInformationSection(this, left);
		managedForm.addPart(this.generalInformationSection);

		this.ignoredFilesSection = new IgnoredFilesSection(this, left);
		managedForm.addPart(this.ignoredFilesSection);

		Composite right = toolkit.createComposite(body);
		right.setLayout(FormLayoutFactory.createFormPaneTableWrapLayout(false, 1));
		right.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		this.documentationSection = new DocumentationSection(this, right);
		managedForm.addPart(this.documentationSection);

		this.actionsSection = new ActionsSection(this, right);
		managedForm.addPart(this.actionsSection);
	}
}
