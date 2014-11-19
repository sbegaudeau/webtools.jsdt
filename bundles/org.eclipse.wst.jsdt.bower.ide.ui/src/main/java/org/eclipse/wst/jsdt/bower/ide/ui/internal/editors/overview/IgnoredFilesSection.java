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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.wst.jsdt.bower.ide.ui.internal.editors.FormLayoutFactory;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class IgnoredFilesSection extends AbstractFormPart {

	public IgnoredFilesSection(FormPage formPage, Composite parent) {
		FormToolkit toolkit = formPage.getManagedForm().getToolkit();
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
		section.setText("Files to ignore");
		section.setLayout(FormLayoutFactory.createClearTableWrapLayout(false, 1));
		TableWrapData data = new TableWrapData(TableWrapData.FILL_GRAB);
		section.setLayoutData(data);

		section.setDescription("This section contains the list of files to ignore");
		Composite client = toolkit.createComposite(section);
		client.setLayout(FormLayoutFactory.createSectionClientTableWrapLayout(false, 3));
		section.setClient(client);

		IActionBars actionBars = formPage.getEditorSite().getActionBars();
		this.createNewFileToIgnoreEntry(client, toolkit, actionBars);
		this.createIgnoredFilesEntry(client, toolkit, actionBars);
	}

	private void createNewFileToIgnoreEntry(Composite client, FormToolkit toolkit, IActionBars actionBars) {
		Text text = toolkit.createText(client, "", SWT.SINGLE); //$NON-NLS-1$
		TableWrapData layoutData = new TableWrapData(TableWrapData.FILL);
		layoutData.colspan = 2;
		layoutData.grabHorizontal = true;
		text.setLayoutData(layoutData);

		Button button = toolkit.createButton(client, "Add", SWT.PUSH);
		layoutData = new TableWrapData(TableWrapData.FILL);
		layoutData.colspan = 1;
		button.setLayoutData(layoutData);
	}

	private void createIgnoredFilesEntry(Composite client, FormToolkit toolkit, IActionBars actionBars) {
		TableViewer tableViewer = new TableViewer(client, SWT.H_SCROLL | SWT.V_SCROLL
				| toolkit.getBorderStyle());
		TableWrapData layoutData = new TableWrapData(TableWrapData.FILL);
		layoutData.colspan = 2;
		layoutData.grabHorizontal = true;
		tableViewer.getTable().setLayoutData(layoutData);

		Button removeButton = toolkit.createButton(client, "Remove", SWT.PUSH);
	}
}
