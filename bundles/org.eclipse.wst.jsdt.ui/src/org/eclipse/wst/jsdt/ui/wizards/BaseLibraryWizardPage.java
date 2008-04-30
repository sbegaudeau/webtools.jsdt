package org.eclipse.wst.jsdt.ui.wizards;



import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.LayoutUtil;

/**
 *
 */
public class BaseLibraryWizardPage extends NewElementWizardPage implements IJsGlobalScopeContainerPage, IJsGlobalScopeContainerPageExtension, IJsGlobalScopeContainerPageExtension2  {
	
//	private IJavaScriptProject fCurrentProject;
//	private IIncludePathEntry[] fCurrentEntries;
//	private static final String LIBRARY_FILE_NAME = "baseBrowserLibrary.js";
	//private static final String MY_PLUGIN_ID="org.eclipse.wst.jsdt.base.library";
	private static final String CONTAINER_ID="org.eclipse.wst.jsdt.launching.baseBrowserLibrary"; //$NON-NLS-1$
	
	public BaseLibraryWizardPage() {
		super("BaseicLibraryWizzardPage"); //$NON-NLS-1$
	}

	public boolean finish() {
		return true;
	}

	public IIncludePathEntry getSelection() {
		// TODO Auto-generated method stub
		System.out.println("Unimplemented method:BaseLibraryWizardPage.getSelection"); //$NON-NLS-1$
		return null;
	}

	public void setSelection(IIncludePathEntry containerEntry) {}

	public void createControl(Composite parent) {
		Composite composite= new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		DialogField field = new DialogField();
		
		//field.createEmptySpace(parent);
		field.setLabelText(NewWizardMessages.BaseLibraryWizardPage_DefaultBrowserLibraryAdded);
		//field.setText("Default Browser Library added to project");
		LayoutUtil.doDefaultLayout(composite, new DialogField[] {field }, false, SWT.DEFAULT, SWT.DEFAULT);
		//LayoutUtil.setHorizontalGrabbing(fLibrarySelector.getListControl(null));
		Dialog.applyDialogFont(composite);
		setControl(composite);
		setDescription(NewWizardMessages.BaseLibraryWizardPage_WebBrowserSupport);
		
	}

	public void initialize(IJavaScriptProject project, IIncludePathEntry[] currentEntries) {
//		fCurrentProject = project;
//		fCurrentEntries = currentEntries;
		
		
	}

	public IIncludePathEntry[] getNewContainers() {
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
		
		//if(url==null) return new IIncludePathEntry[] {};
		
		//IIncludePathEntry library = JavaScriptCore.newLibraryEntry(workingLocationFile,null,null);
			//IIncludePathEntry library = JavaScriptCore.newLibraryEntry(workingLocationFile.makeAbsolute(), null, null, new IAccessRule[0], new IIncludePathAttribute[0], true);
			IIncludePathEntry library = JavaScriptCore.newContainerEntry( new Path(CONTAINER_ID));
//		try {
//			//library = JavaScriptCore.newLibraryEntry(new Path("Base Browser Support Library"), fCurrentProject.getProject().getWorkingLocation(MY_PLUGIN_ID), new Path(LIBRARY_FILE_NAME), new IAccessRule[0], new IIncludePathAttribute[0], true);
//			library = JavaScriptCore.newContainerEntry(workingLocationFile.makeAbsolute(),new IAccessRule[0], new IIncludePathAttribute[0], false);
//			//library = JavaScriptCore.newLibraryEntry(workingLocationFile.makeAbsolute(), null, null, new IAccessRule[0], new IIncludePathAttribute[0], true);
//		}catch(Exception ex) {
//			System.out.println(ex);
//		}
			return new IIncludePathEntry[] {library};
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
