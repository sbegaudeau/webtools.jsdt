package org.eclipse.wst.jsdt.ui.wizards;



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
public class BaseLibraryWizardPage extends NewElementWizardPage implements IClasspathContainerPage, IClasspathContainerPageExtension, IClasspathContainerPageExtension2  {
	
//	private IJavaProject fCurrentProject;
//	private IClasspathEntry[] fCurrentEntries;
	private static final String LIBRARY_FILE_NAME = "baseBrowserLibrary.js";
	//private static final String MY_PLUGIN_ID="org.eclipse.wst.jsdt.base.library";
	private static final String CONTAINER_ID="org.eclipse.wst.jsdt.launching.baseBrowserLibrary";
	
	public BaseLibraryWizardPage() {
		super("BaseicLibraryWizzardPage");
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
		field.setLabelText("Default Browser Library added to Project.\n\n  - This library supports the document and window objects supported by most browsers.");
		//field.setText("Default Browser Library added to project");
		LayoutUtil.doDefaultLayout(composite, new DialogField[] {field }, false, SWT.DEFAULT, SWT.DEFAULT);
		//LayoutUtil.setHorizontalGrabbing(fLibrarySelector.getListControl(null));
		Dialog.applyDialogFont(composite);
		setControl(composite);
		setDescription("Web Browser Support");
		
	}

	public void initialize(IJavaProject project, IClasspathEntry[] currentEntries) {
//		fCurrentProject = project;
//		fCurrentEntries = currentEntries;
		
		
	}

	public IClasspathEntry[] getNewContainers() {
//		IPath workingLocationFile = fCurrentProject.getProject().getWorkingLocation(MY_PLUGIN_ID).append(LIBRARY_FILE_NAME);
//		String fileLocation = workingLocationFile.makeAbsolute().toOSString();
//		File newLibFile = new File(fileLocation);
//		
//		IPath filePath = new Path(LIBRARY_FILE_NAME);
//		Bundle me = Platform.getBundle(MY_PLUGIN_ID);
//		URL url = null;
//		try {
//			InputStream is = FileLocator.openStream(me, filePath, false);
//			copyFile(is,newLibFile);
//		//url = FileLocator.findEntries(me,filePath)[0]);
//	
//		//workingLocation.append(LIBRARY_FILE_NAME);
//		
//			
//			} catch (IOException ex) {
//				
//				ex.printStackTrace();
//			}
		
		//if(url==null) return new IClasspathEntry[] {};
		
		//IClasspathEntry library = JavaCore.newLibraryEntry(workingLocationFile,null,null);
			//IClasspathEntry library = JavaCore.newLibraryEntry(workingLocationFile.makeAbsolute(), null, null, new IAccessRule[0], new IClasspathAttribute[0], true);
			IClasspathEntry library = JavaCore.newContainerEntry( new Path(CONTAINER_ID));
//		try {
//			//library = JavaCore.newLibraryEntry(new Path("Base Browser Support Library"), fCurrentProject.getProject().getWorkingLocation(MY_PLUGIN_ID), new Path(LIBRARY_FILE_NAME), new IAccessRule[0], new IClasspathAttribute[0], true);
//			library = JavaCore.newContainerEntry(workingLocationFile.makeAbsolute(),new IAccessRule[0], new IClasspathAttribute[0], false);
//			//library = JavaCore.newLibraryEntry(workingLocationFile.makeAbsolute(), null, null, new IAccessRule[0], new IClasspathAttribute[0], true);
//		}catch(Exception ex) {
//			System.out.println(ex);
//		}
			return new IClasspathEntry[] {library};
	}
//	public static void copyFile(InputStream src, File dst) throws IOException {
//		InputStream in=null;
//		OutputStream out=null;
//		try {
//			in = new BufferedInputStream(src);
//			out = new BufferedOutputStream(new FileOutputStream(dst));		
//			byte[] buffer = new byte[4096];
//			int len;
//			while ((len=in.read(buffer)) != -1) {
//				out.write(buffer, 0, len);
//			}
//		} finally {
//			if (in != null)
//				try {
//					in.close();
//				} catch (IOException e) {
//				}
//			if (out != null)
//				try {
//					out.close();
//				} catch (IOException e) {
//				}
//		}
//	}

}
