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
package org.eclipse.wst.jsdt.debug.internal.rhino.ui.launching;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.IHelpConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ILaunchConstants;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.ISharedImages;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.RhinoImageRegistry;
import org.eclipse.wst.jsdt.debug.internal.rhino.ui.refactoring.Refactoring;
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
				case FOLDER: 
				case EXT_FOLDER: {
					return PlatformUI.getWorkbench().getSharedImages().getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
				}
				case SCRIPT: {
					return RhinoImageRegistry.getSharedImage(ISharedImages.IMG_SCRIPT);
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
	 * A single entry to be included when launching
	 */
	class IncludeEntry {
		int kind = -1;
		String path = null;
		
		/**
		 * Constructor
		 * @param kind
		 * @param path
		 */
		public IncludeEntry(int kind, String path) {
			this.kind = kind;
			this.path = path;
		}
		String string() {
			return kind+path;
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
	
	private TreeViewer viewer = null;
	private Button addfolder = null,
	               remove = null,
	               addexternalfolder = null,
	               addscript = null,
	               defaults = null,
	               up = null,
	               down = null,
	               subdirs = null;
	private Vector includes = new Vector();
	private ILaunchConfiguration backingconfig = null;
	private ViewerFilter vfilter = new ContainerFilter();
	
	public static final int FOLDER = 0;
	public static final int EXT_FOLDER = 1;
	public static final int SCRIPT = 2;
	
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
		addfolder = SWTFactory.createPushButton(lhs, Messages.add_folder_button, null);
		addfolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addFolder();
			}
		});
		addexternalfolder = SWTFactory.createPushButton(lhs, Messages.add_ext_folder_button, null);
		addexternalfolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addExtFolder();
			}
		});
		SWTFactory.createHorizontalSpacer(lhs, 1);
		defaults = SWTFactory.createPushButton(lhs, Messages.defaults_button, null);
		defaults.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				defaults();	
			}
		});
		
		subdirs = SWTFactory.createCheckButton(comp, Messages.include_subdirs_button, null, false, 2);
		subdirs.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateLaunchConfigurationDialog();
			}
		});
		PlatformUI.getWorkbench().getHelpSystem().setHelp(comp, IHelpConstants.INCLUDE_TAB_CONTEXT);
		setControl(comp);
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
		remove.setEnabled(size > 0);
	}
	
	/**
	 * Prompts the user to select a script from the workspace to include
	 */
	void addScript() {
		ScriptSelectionDialog dialog = new ScriptSelectionDialog(getShell(), true, ResourcesPlugin.getWorkspace().getRoot());
		if(dialog.open() == IDialogConstants.OK_ID) {
			IFile script = (IFile) dialog.getFirstResult();
			IncludeEntry entry = new IncludeEntry(SCRIPT, script.getFullPath().makeAbsolute().toOSString());
			if(!includes.contains(entry)) {
				includes.add(entry);
				viewer.refresh();
				updateLaunchConfigurationDialog();
			}
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
		try {
			IFile script = Refactoring.getScript(backingconfig, null);
			if(script != null) {
				includes.clear();
				includes.add(new IncludeEntry(IncludeTab.FOLDER, script.getParent().getFullPath().makeAbsolute().toOSString()));
				viewer.refresh();
			}
		}
		catch(CoreException ce) {
			//ignore
		}
		updateLaunchConfigurationDialog();
	}
	
	/**
	 * Prompts the user to select a new workspace folder to add to the include path
	 */
	void addFolder() {
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), 
				new WorkbenchLabelProvider(), 
				new WorkbenchContentProvider());
		dialog.setTitle(Messages.select_folder);
		dialog.setMessage(Messages.select_a_folder_to_add);
		dialog.setAllowMultiple(true);
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.addFilter(vfilter);
		if(dialog.open() == IDialogConstants.OK_ID) {
			Object[] folders = dialog.getResult();
			if(folders != null && folders.length > 0) {
				for (int i = 0; i < folders.length; i++) {
					includes.add(new IncludeEntry(IncludeTab.FOLDER, ((IContainer)folders[i]).getFullPath().makeAbsolute().toOSString()));
				}
				viewer.refresh();
				updateLaunchConfigurationDialog();
			}
		}
	}

	/**
	 * Prompts the user to select a new folder from the local file system to include
	 */
	void addExtFolder() {
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		String path = dialog.open(); 
		if(path != null) {
			includes.add(new IncludeEntry(EXT_FOLDER, path));
			viewer.refresh();
			updateLaunchConfigurationDialog();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		try {
			IFile script = Refactoring.getScript(configuration, null);
			if(script != null) {
				includes.add(new IncludeEntry(IncludeTab.FOLDER, script.getParent().getFullPath().makeAbsolute().toOSString()));
			}
			ArrayList list = new ArrayList(includes.size());
			for (Iterator i = includes.iterator(); i.hasNext();) {
				list.add(((IncludeEntry)i.next()).string());
			}
			configuration.setAttribute(ILaunchConstants.ATTR_INCLUDE_PATH, list);
		}
		catch(CoreException ce) {
			//ignore
		}
		configuration.setAttribute(ILaunchConstants.ATTR_INCLUDE_PATH_SUB_DIRS, false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
		backingconfig = configuration;
		includes.clear();
		try {
			subdirs.setSelection(configuration.getAttribute(ILaunchConstants.ATTR_INCLUDE_PATH_SUB_DIRS, false));
			List list = configuration.getAttribute(ILaunchConstants.ATTR_INCLUDE_PATH, (List)null);
			if(list != null) {
				String value = null;
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					try {
						value = (String) iter.next();
						int kind = Integer.parseInt(value.substring(0, 1));
						String path = value.substring(1);
						includes.add(new IncludeEntry(kind, path));
					}
					catch(NumberFormatException nfe) {
						//do nothing just toss it
					}
				}
				viewer.refresh();
			}
		}
		catch(CoreException ce) {
			//ignore
		}
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
		configuration.setAttribute(ILaunchConstants.ATTR_INCLUDE_PATH_SUB_DIRS, subdirs.getSelection());
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
