package org.eclipse.wst.jsdt.ui.wizards;



import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.jsdt.core.IClasspathEntry;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.JavaCore;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.LayoutUtil;

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
