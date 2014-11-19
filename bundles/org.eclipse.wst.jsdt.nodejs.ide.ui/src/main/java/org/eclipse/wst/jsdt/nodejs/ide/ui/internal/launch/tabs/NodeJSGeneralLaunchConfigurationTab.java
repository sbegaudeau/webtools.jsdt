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
package org.eclipse.wst.jsdt.nodejs.ide.ui.internal.launch.tabs;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.NodeJsIdeUiActivator;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.launch.INodeJsLaunchConfigurationAttributes;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils.I18n;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.utils.I18nKeys;

/**
 * The node.js general configuration tab.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class NodeJSGeneralLaunchConfigurationTab extends AbstractLaunchConfigurationTab {

	/**
	 * Text field containing the path of the application to run.
	 */
	private Text applicationPathText;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Font font = parent.getFont();
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setFont(font);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		composite.setLayoutData(gd);

		this.createApplicationGroup(composite, font);

		this.setControl(composite);
		this.update();
	}

	/**
	 * Creates a Group widget.
	 *
	 * @param parent
	 *            the parent composite to add this group to
	 * @param text
	 *            the text for the heading of the group
	 * @param columns
	 *            the number of columns within the group
	 * @param hspan
	 *            the horizontal span the group should take up on the parent
	 * @param fill
	 *            the style for how this composite should fill into its parent Can be one of
	 *            <code>GridData.FILL_HORIZONAL</code>, <code>GridData.FILL_BOTH</code> or
	 *            <code>GridData.FILL_VERTICAL</code>
	 * @return the new group
	 */
	protected Group createGroup(Composite parent, String text, int columns, int hspan, int fill) {
		Group g = new Group(parent, SWT.NONE);
		g.setLayout(new GridLayout(columns, false));
		g.setText(text);
		g.setFont(parent.getFont());
		GridData gd = new GridData(fill);
		gd.horizontalSpan = hspan;
		g.setLayoutData(gd);
		return g;
	}

	/**
	 * Creates the group containing the general options of the application.
	 *
	 * @param composite
	 *            The composite containing the group
	 * @param font
	 *            the font used by the parent of the group
	 */
	private void createApplicationGroup(Composite composite, Font font) {
		Group generationGroup = createGroup(composite, I18n
				.getString(I18nKeys.NODEJS_GENERAL_APPLICATION_GROUP_LABEL), 3, 1, GridData.FILL_HORIZONTAL);
		Composite comp = new Composite(generationGroup, SWT.NONE);
		GridLayout layout = new GridLayout(4, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		comp.setLayout(layout);
		comp.setFont(font);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		comp.setLayoutData(gd);

		Label modelPathLabel = new Label(comp, SWT.NONE);
		modelPathLabel.setText(I18n.getString(I18nKeys.NODEJS_GENERAL_APPLICATION_LABEL));

		this.applicationPathText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		this.applicationPathText.setFont(composite.getFont());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		this.applicationPathText.setLayoutData(gd);
		this.applicationPathText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				update();
			}
		});

		final Button browseModelButton = createPushButton(comp, I18n
				.getString(I18nKeys.NODEJS_GENERAL_APPLICATION_BROWSE_LABEL), null);
		browseModelButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				FilteredResourcesSelectionDialog dialog = new FilteredResourcesSelectionDialog(getShell(),
						false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
				dialog.setTitle(I18n.getString(I18nKeys.NODEJS_GENERAL_APPLICATION_DIALOG_TITLE));
				dialog.setInitialPattern("*.js"); //$NON-NLS-1$
				dialog.open();
				if (dialog.getResult() != null && dialog.getResult().length > 0) {
					Object[] results = dialog.getResult();
					for (Object result : results) {
						if (result instanceof IFile) {
							applicationPathText.setText(((IFile)result).getLocation().toString());
							break;
						}
					}
				}
				update();
				updateLaunchConfigurationDialog();
			}
		});
	}

	/**
	 * Checks potential errors.
	 */
	private void update() {
		this.setErrorMessage(null);

		this.getLaunchConfigurationDialog().updateButtons();
		this.getLaunchConfigurationDialog().updateMessage();

		// Check the path of the model
		if (this.applicationPathText != null) {
			String text = this.applicationPathText.getText();
			if (text != null && text.length() > 0) {
				IFile model = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(text));
				if (model != null && !model.exists()) {
					this.setErrorMessage(I18n.getString(I18nKeys.NODEJS_GENERAL_APPLICATION_MISSING));
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			String applicationPath = configuration.getAttribute(
					INodeJsLaunchConfigurationAttributes.APPLICATION_PATH, ""); //$NON-NLS-1$
			this.applicationPathText.setText(applicationPath);
		} catch (CoreException e) {
			NodeJsIdeUiActivator.log(e, true);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		String errorMessage = this.getErrorMessage();
		if (errorMessage == null || errorMessage.length() == 0) {
			configuration.setAttribute(INodeJsLaunchConfigurationAttributes.APPLICATION_PATH,
					this.applicationPathText.getText());
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	@Override
	public String getName() {
		return I18n.getString(I18nKeys.NODEJS_GENERAL_TAB_NAME);
	}

}
