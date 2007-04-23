package org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.wst.jsdt.core.IAccessRule;
import org.eclipse.wst.jsdt.core.IClasspathAttribute;
import org.eclipse.wst.jsdt.core.IClasspathContainer;
import org.eclipse.wst.jsdt.core.IClasspathEntry;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.JavaCore;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.internal.core.ClasspathEntry;
import org.eclipse.wst.jsdt.internal.corext.util.Messages;
import org.eclipse.wst.jsdt.internal.ui.JavaPlugin;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.wst.jsdt.internal.ui.preferences.UserLibraryPreferencePage;

import org.eclipse.wst.jsdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.CheckedListDialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.ITreeListAdapter;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.TreeListDialogField;
import org.eclipse.wst.jsdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.wst.jsdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.wst.jsdt.ui.wizards.IClasspathContainerPageExtension2;
import org.eclipse.wst.jsdt.ui.wizards.NewElementWizardPage;
import org.osgi.framework.Bundle;

import com.ibm.icu.text.Collator;

/**
 *
 */
public class FireFoxLibraryWizardPage extends NewElementWizardPage implements IClasspathContainerPage, IClasspathContainerPageExtension, IClasspathContainerPageExtension2  {

	private static final String LIBRARY_FILE_NAME = "FireFox2.0.0.3.js";
	private static final String CONTAINER_ID="org.eclipse.wst.jsdt.launching.FireFoxBrowserLibrary";
	
	public FireFoxLibraryWizardPage() {
		super("FireFoxBrowserLib");
	}

	public boolean finish() {
		return true;
	}

	public IClasspathEntry getSelection() {
		// TODO Auto-generated method stub
		System.out.println("Unimplemented method:BaseLibraryWizardPage.getSelection");
		return null;
	}

	public void setSelection(IClasspathEntry containerEntry) {}

	public void createControl(Composite parent) {
		Composite composite= new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		DialogField field = new DialogField();
		
		//field.createEmptySpace(parent);
		field.setLabelText("FireFox Browser (2.0.0.3) Library added to Project.\n\n  - This library supports JavaScript elements provided by the FireFox web browser.");
		//field.setText("Default Browser Library added to project");
		LayoutUtil.doDefaultLayout(composite, new DialogField[] {field }, false, SWT.DEFAULT, SWT.DEFAULT);
		//LayoutUtil.setHorizontalGrabbing(fLibrarySelector.getListControl(null));
		Dialog.applyDialogFont(composite);
		setControl(composite);
		setDescription("FireFox Browser Support");
		
	}

	public void initialize(IJavaProject project, IClasspathEntry[] currentEntries) {
	
	}

	public IClasspathEntry[] getNewContainers() {
			IClasspathEntry library = JavaCore.newContainerEntry( new Path(CONTAINER_ID));
			return new IClasspathEntry[] {library};
	}

}
