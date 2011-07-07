/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.rhino.ui.launching;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.IHelpConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ILaunchConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ISharedImages;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.RhinoImageRegistry;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.refactoring.Refactoring;
import org.eclipse.wst.jsdt.debug.internal.ui.SWTFactory;
import org.eclipse.wst.jsdt.debug.internal.ui.dialogs.ScriptSelectionDialog;

/**
 * Launch configuration tab to allows users to specify what additional
 * scripts should be interpreted by Rhino when launching
 * 
 * @since 1.0
 */
public class IncludeTab extends AbstractLaunchConfigurationTab {

	class Contents implements ITreeContentProvider {
		public Object[] getElements(Object inputElement) {
			return ((Vector)inputElement).toArray();
		}
		public Object[] getChildren(Object parentElement) {return null;}
		public void dispose() {}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
		public Object getParent(Object element) {return null;}
		public boolean hasChildren(Object element) {return false;}
		
	}
	class Labels extends LabelProvider {
		public Image getImage(Object element) {
			IncludeEntry entry = (IncludeEntry) element;
			switch(entry.kind) {
				case IncludeEntry.LOCAL_SCRIPT: {
					return RhinoImageRegistry.getSharedImage(ISharedImages.IMG_SCRIPT);
				}
				case IncludeEntry.EXT_SCRIPT: {
					return PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FILE);
				}
			}
			return null;
		}
		public String getText(Object element) {
			IncludeEntry entry = (IncludeEntry) element;
			return entry.path;
		}
	}
	
	/**
	 * Default filter for the workspace folder selection dialog
	 */
	class ContainerFilter extends ViewerFilter {
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			return element instanceof IContainer && ((IContainer)element).isAccessible(); 
		}
		
	}
	
	/**
	 * The id for this tab.
	 * <br><br>
	 * Value is: <code>thino.include.tab</code>
	 */
	public static final String TAB_ID = "rhino.include.tab"; //$NON-NLS-1$
	/**
	 * Standard JavaScript extension
	 * <br><br>
	 * Value is: <code>*.js</code>
	 */
	public static final String JS_EXTENSION = "*.js"; //$NON-NLS-1$
	
	private TreeViewer viewer = null;
	private Button remove = null,
	               addexternalscript = null,
	               addscript = null,
	               defaults = null,
	               up = null,
	               down = null;
	private Vector includes = new Vector();
	private ILaunchConfiguration backingconfig = null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, 2, 1, GridData.FILL_HORIZONTAL);
		SWTFactory.createWrapLabel(comp, Messages.include_path, 2);
		Tree tree = new Tree(comp, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		tree.setLayout(new GridLayout(1, true)); 
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer = new TreeViewer(tree);
		viewer.setLabelProvider(new Labels());
		viewer.setContentProvider(new Contents());
		viewer.setInput(includes);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				updateButtons((IStructuredSelection) event.getSelection());
			}
		});
		Composite lhs = SWTFactory.createComposite(comp, comp.getFont(), 1, 1, GridData.FILL_VERTICAL, 0, 0);
		up = SWTFactory.createPushButton(lhs, Messages.up_button, null);
		up.setEnabled(false);
		up.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				up(((IStructuredSelection)viewer.getSelection()).getFirstElement());
			}
		});
		down = SWTFactory.createPushButton(lhs, Messages.down_button, null);
		down.setEnabled(false);
		down.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				down(((IStructuredSelection)viewer.getSelection()).getFirstElement());
			}
		});
		remove = SWTFactory.createPushButton(lhs, Messages.remove_button, null);
		remove.setEnabled(false);
		remove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				remove((IStructuredSelection)viewer.getSelection());
			}
		});
		addscript = SWTFactory.createPushButton(lhs, Messages.add_script_button, null);
		addscript.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addScript();
			}
		});
		addexternalscript = SWTFactory.createPushButton(lhs, Messages.add_ext_script_button, null);
		addexternalscript.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addExtScript();
			}
		});
		SWTFactory.createHorizontalSpacer(lhs, 1);
		defaults = SWTFactory.createPushButton(lhs, Messages.defaults_button, null);
		defaults.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				defaults();	
			}
		});
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(comp, IHelpConstants.INCLUDE_TAB_CONTEXT);
		setControl(comp);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getHelpContextId()
	 */
	public String getHelpContextId() {
		return IHelpConstants.INCLUDE_TAB_CONTEXT;
	}
	
	/**
	 * Updates the buttons based on the selection from the viewer
	 * 
	 * @param selection
	 */
	void updateButtons(IStructuredSelection selection) {
		int size = selection.size();
		if(size == 1) {
			//up / down
			Object element = selection.getFirstElement();
			int idx = includes.indexOf(element);
			up.setEnabled(idx > 0);
			down.setEnabled(idx < includes.size() -1);
		}
		else {
			if(up != null) {
				up.setEnabled(false);
			}
			if(down != null) {
				down.setEnabled(false);
			}
		}
		remove.setEnabled(size > 0);
	}
	
	/**
	 * Prompts the user to select a script from the workspace to include
	 */
	void addScript() {
		ScriptSelectionDialog dialog = new ScriptSelectionDialog(getShell(), true, ResourcesPlugin.getWorkspace().getRoot());
		if(dialog.open() == IDialogConstants.OK_ID) {
			Object[] scripts = dialog.getResult();
			IncludeEntry newentry = null;
			for (int i = 0; i < scripts.length; i++) {
				newentry = new IncludeEntry(IncludeEntry.LOCAL_SCRIPT, ((IFile)scripts[i]).getFullPath().makeAbsolute().toString());
				if(!includes.contains(newentry)) {
					includes.add(newentry);
				}
			}
			viewer.refresh();
			updateLaunchConfigurationDialog();
		}
	}
	
	/**
	 * Moves the selected element up one spot in the tree
	 * 
	 * @param element
	 */
	void up(Object element) {
		int idx = includes.indexOf(element);
		if(idx < 0) {
			return;
		}
		includes.remove(idx);
		includes.insertElementAt(element, idx-1);
		viewer.refresh();
		updateLaunchConfigurationDialog();
	}
	
	/**
	 * Moves the selected element down one spot in the tree
	 * 
	 * @param element
	 */
	void down(Object element) {
		int idx = includes.indexOf(element);
		if(idx < 0) {
			return;
		}
		includes.remove(idx);
		includes.insertElementAt(element, idx+1);
		viewer.refresh();
		updateLaunchConfigurationDialog();
	}
	
	/**
	 * Removes the selected element from the tree
	 * 
	 * @param selection
	 */
	void remove(IStructuredSelection selection) {
		includes.removeAll(selection.toList());
		viewer.refresh();
		updateLaunchConfigurationDialog();
	}
	
	/**
	 * Restores the tab to the defaults computed against the currently specified script
	 */
	void defaults() {
		includes.clear();
		viewer.refresh();
		/*try {
			IFile script = Refactoring.getScript(backingconfig);
			if(script != null) {
				includes.clear();
				includes.add(new IncludeEntry(IncludeTab.LOCAL_SCRIPT, script.getFullPath().makeAbsolute().toOSString()));
				viewer.refresh();
			}
		}
		catch(CoreException ce) {
			//ignore
		}*/
		updateLaunchConfigurationDialog();
	}
	
	/**
	 * Prompts the user to select a new folder from the local file system to include
	 */
	void addExtScript() {
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN | SWT.MULTI);
		dialog.setFilterExtensions(new String[] {JS_EXTENSION});
		dialog.setFilterIndex(0);
		dialog.setText(Messages.select_scripts_to_add);
		if(dialog.open() != null) {
			String[] names = dialog.getFileNames();
			if(names != null && names.length > 0) {
				String path = dialog.getFilterPath();
				if(path != null) {
					boolean added = false;
					for (int i = 0; i < names.length; i++) {
						File script = new File(path, names[i]);
						if(script.exists()) {
							includes.add(new IncludeEntry(IncludeEntry.EXT_SCRIPT, new Path(script.getAbsolutePath()).toString()));
							added = true;
						}
					}
					if(added) {
						viewer.refresh();
						updateLaunchConfigurationDialog();
					}
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		/*try {
			IFile script = Refactoring.getScript(configuration);
			if(script != null) {
				includes.add(new IncludeEntry(IncludeEntry.LOCAL_SCRIPT, script.getFullPath().makeAbsolute().toString()));
			}
			ArrayList list = new ArrayList(includes.size());
			for (Iterator i = includes.iterator(); i.hasNext();) {
				list.add(((IncludeEntry)i.next()).string());
			}
			configuration.setAttribute(ILaunchConstants.ATTR_INCLUDE_PATH, list);
		}
		catch(CoreException ce) {
			//ignore
		}*/
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
		backingconfig = configuration;
		includes.clear();
		IncludeEntry[] entries = Refactoring.getIncludeEntries(backingconfig);
		for (int i = 0; i < entries.length; i++) {
			includes.add(entries[i]);
		}
		viewer.refresh();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		if(includes.isEmpty()) {
			configuration.removeAttribute(ILaunchConstants.ATTR_INCLUDE_PATH);
		}
		else {
			ArrayList list = new ArrayList(includes.size());
			for (Iterator i = includes.iterator(); i.hasNext();) {
				list.add(((IncludeEntry)i.next()).string());
			}
			configuration.setAttribute(ILaunchConstants.ATTR_INCLUDE_PATH, list);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return Messages.include_tab_name;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
	 */
	public Image getImage() {
		return RhinoImageRegistry.getSharedImage(ISharedImages.IMG_LIBRARY);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getId()
	 */
	public String getId() {
		return TAB_ID;
	}
}
