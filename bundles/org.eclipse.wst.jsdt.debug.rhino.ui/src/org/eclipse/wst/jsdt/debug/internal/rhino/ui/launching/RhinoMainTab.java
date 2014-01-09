/*******************************************************************************
 * Copyright (c) 2010, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.ui.launching;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IMember;
import org.eclipse.wst.jsdt.core.ITypeRoot;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.IHelpConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ILaunchConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ISharedImages;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.RhinoImageRegistry;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.RhinoUIPlugin;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.refactoring.Refactoring;
import org.eclipse.wst.jsdt.debug.internal.ui.SWTFactory;
import org.eclipse.wst.jsdt.debug.internal.ui.dialogs.ScriptSelectionDialog;

/**
 * Rhino specific main tab
 * 
 * @since 1.0
 */
public class RhinoMainTab extends AbstractLaunchConfigurationTab {

	/**
	 * The id of the tab
	 * <br><br>
	 * Value is: <code>rhino.main.tab</code>
	 */
	public static final String TAB_ID = "rhino.main.tab"; //$NON-NLS-1$
	public static final String[] VERSIONS = {"100", "110", "120", "130", "140", "150", "160", "170"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
	public static final String[] OPTIMIZATIONS = {"-1 [interpret only]", "0 [no optimizations]", "1 [all optimizations]"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	SelectionAdapter defaultAdapter = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			updateLaunchConfigurationDialog();
		};
	};
	
	Text script = null;
	Button logging = null,
		   strict = null;
	Combo ecmaversion = null,
		  optlevel = null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, 1, 1, GridData.FILL_HORIZONTAL);
		Group group = SWTFactory.createGroup(comp, Messages._script, 2, 1, GridData.FILL_HORIZONTAL);
		script = SWTFactory.createSingleText(group, 1);
		script.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateLaunchConfigurationDialog();
			}
		});
		script.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			public void getName(AccessibleEvent e) {
				e.result = Messages.RhinoMainTab_1;
			}
		});
		((GridData) script.getLayoutData()).grabExcessHorizontalSpace = true;
		Button browse = SWTFactory.createPushButton(group, Messages.bro_wse, null);
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IJavaScriptUnit unit = chooseScript();
				if(unit != null) {
					script.setText(unit.getPath().toString());
				}
			}
		});
		group = SWTFactory.createGroup(comp, Messages.options_group_name, 2, 1, GridData.FILL_HORIZONTAL);
		Label lbl = SWTFactory.createWrapLabel(group, Messages.ecma_version_to_interpret_with, 1);
		((GridData)lbl.getLayoutData()).grabExcessHorizontalSpace = true;
		ecmaversion = SWTFactory.createCombo(group, SWT.READ_ONLY | SWT.SINGLE, 1, VERSIONS);
		ecmaversion.addSelectionListener(defaultAdapter);
		GridData data = (GridData) ecmaversion.getLayoutData();
		data.grabExcessHorizontalSpace = false;
		data.horizontalAlignment = SWT.END;
		lbl = SWTFactory.createWrapLabel(group, Messages.rhino_opt_level, 1);
		((GridData)lbl.getLayoutData()).grabExcessHorizontalSpace = true;
		optlevel = SWTFactory.createCombo(group, SWT.READ_ONLY | SWT.SINGLE, 1, OPTIMIZATIONS);
		optlevel.addSelectionListener(defaultAdapter);
		data = (GridData) optlevel.getLayoutData();
		data.grabExcessHorizontalSpace = false;
		data.horizontalAlignment = SWT.END;
		strict = SWTFactory.createCheckButton(group, Messages.strict_mode, null, false, 2);
		strict.addSelectionListener(defaultAdapter);
		logging = SWTFactory.createCheckButton(group, Messages.log_interpreter_exceptions, null, false, 2);
		logging.addSelectionListener(defaultAdapter);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(comp, IHelpConstants.MAIN_TAB_CONTEXT);
		setControl(comp);
	}
	
	public String getHelpContextId() {
		return IHelpConstants.MAIN_TAB_CONTEXT;
	};
	
	/**
	 * Allows users to select a script from either a project context, if there is one or the workspace
	 * 
	 * @return the selected script or <code>null</code>
	 */
	IJavaScriptUnit chooseScript() {
		ScriptSelectionDialog dialog = new ScriptSelectionDialog(getShell(), false, ResourcesPlugin.getWorkspace().getRoot());
		dialog.setTitle(Messages.script_selection);
		if(dialog.open() == IDialogConstants.OK_ID) {
			IFile file = (IFile) dialog.getFirstResult();
			return (IJavaScriptUnit) JavaScriptCore.create(file);
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#isValid(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public boolean isValid(ILaunchConfiguration launchConfig) {
		String text2 = script.getText().trim();
		boolean hasscript = text2.length() > 0;
		if(!hasscript) {
			setErrorMessage(Messages.provide_script_for_project);
			return false;
		}
		IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(text2));
		if(resource != null) {
			if(resource.getType() != IResource.FILE) {
				setErrorMessage(NLS.bind(Messages.script_not_a_file, text2));
				return false;
			}
			if(!resource.isAccessible()) {
				setErrorMessage(NLS.bind(Messages.script_not_accessible, text2));
				return false;
			}
		}
		else {
			setErrorMessage(NLS.bind(Messages.script_not_in_workspace, text2));
			return false;
		}
		setErrorMessage(null);
		setMessage(Messages.launch_script);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		IJavaScriptElement element = getContext();
		if(element != null) {
			if(element instanceof IMember) {
				IMember member = (IMember) element;
				if(member.isBinary()) {
					element = member.getClassFile();
				}
				else {
					element = member.getJavaScriptUnit();
				}
			}
			String pname = element.getJavaScriptProject().getProject().getName();
			String name = pname;
			ITypeRoot root = null;
			if(element.getElementType() == IJavaScriptElement.CLASS_FILE ||
					element.getElementType() == IJavaScriptElement.JAVASCRIPT_UNIT) {
				root = (ITypeRoot) element;
				String tname = root.getElementName();
				configuration.setAttribute(ILaunchConstants.ATTR_SCRIPT, root.getPath().toString());
				name = NLS.bind(Messages.config_name, new String[] {pname, tname});
			}
			name = getLaunchConfigurationDialog().generateName(name);
			configuration.rename(name);
			Refactoring.mapResources(configuration);
		}
		configuration.setAttribute(ILaunchConstants.ATTR_LOG_INTERPRETER_EXCEPTIONS, true);
		configuration.setAttribute(ILaunchConstants.ATTR_ECMA_VERSION, ILaunchConstants.ECMA_170);
		configuration.setAttribute(ILaunchConstants.ATTR_OPT_LEVEL, -1);
		configuration.setAttribute(ILaunchConstants.ATTR_STRICT_MODE, false);
	}

	/**
	 * Returns the current JavaScript context - project or script file - from the current workbench
	 * selection or <code>null</code> if:
	 * <ol>
	 * <li>there is no active workbench page or part</li>
	 * <li>there is nothing selected in the active page</li>
	 * <li>the selected item cannot be resolved to an {@link IJavaScriptElement}</li>
	 * </ol>
	 * 
	 * @return the {@link IJavaScriptElement} context for the current workbench selection or <code>null</code>
	 */
	IJavaScriptElement getContext() {
		IWorkbenchPage page = RhinoUIPlugin.getActivePage();
		if (page != null) {
			ISelection selection = page.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection)selection;
				if (!ss.isEmpty()) {
					Object obj = ss.getFirstElement();
					if (obj instanceof IJavaScriptElement) {
						return (IJavaScriptElement)obj;
					}
					if (obj instanceof IResource) {
						IJavaScriptElement je = JavaScriptCore.create((IResource)obj);
						if (je == null) {
							IProject pro = ((IResource)obj).getProject();
							je = JavaScriptCore.create(pro);
						}
						if (je != null) {
							return je;
						}
					}
				}
			}
			IEditorPart part = page.getActiveEditor();
			if (part != null) {
				IEditorInput input = part.getEditorInput();
				return (IJavaScriptElement) input.getAdapter(IJavaScriptElement.class);
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			String text = configuration.getAttribute(ILaunchConstants.ATTR_SCRIPT, (String)null);
			if(text != null) {
				script.setText(text);
			}
			else {
				script.setText(""); //$NON-NLS-1$
			}
			boolean value = configuration.getAttribute(ILaunchConstants.ATTR_LOG_INTERPRETER_EXCEPTIONS, true);
			logging.setSelection(value);
			value = configuration.getAttribute(ILaunchConstants.ATTR_STRICT_MODE, false);
			strict.setSelection(value);
			text = configuration.getAttribute(ILaunchConstants.ATTR_ECMA_VERSION, ILaunchConstants.ECMA_170);
			int idx = ecmaversion.indexOf(text);
			if(idx < 0) {
				idx = 7;
			}
			ecmaversion.select(idx);
			int opt = configuration.getAttribute(ILaunchConstants.ATTR_OPT_LEVEL, -1);
			optlevel.select(opt+1);
		}
		catch(CoreException ce) {}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		String scpt = script.getText().trim();
		if(scpt.length() < 1) {
			configuration.removeAttribute(ILaunchConstants.ATTR_SCRIPT);
		}
		else {
			configuration.setAttribute(ILaunchConstants.ATTR_SCRIPT, scpt);
		}
		configuration.setAttribute(ILaunchConstants.ATTR_LOG_INTERPRETER_EXCEPTIONS, logging.getSelection());
		configuration.setAttribute(ILaunchConstants.ATTR_STRICT_MODE, strict.getSelection());
		configuration.setAttribute(ILaunchConstants.ATTR_ECMA_VERSION, ecmaversion.getText());
		configuration.setAttribute(ILaunchConstants.ATTR_OPT_LEVEL, optlevel.getSelectionIndex()-1);
		Refactoring.mapResources(configuration);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return Messages.main;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getId()
	 */
	public String getId() {
		return TAB_ID;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
	 */
	public Image getImage() {
		return RhinoImageRegistry.getSharedImage(ISharedImages.IMG_MAIN_TAB);
	}
}
