package org.eclipse.wst.jsdt.libraries;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.infer.DefaultInferrenceProvider;
import org.eclipse.wst.jsdt.internal.ui.IJsGlobalScopeContainerInitializerExtension;

public class BasicBrowserLibraryJsGlobalScopeContainerInitializer implements IJsGlobalScopeContainerInitializerExtension {
	
	private static final char[][] LIBRARY_FILE_NAME = {
														{ 'b', 'a', 's', 'e', 'B', 'r', 'o', 'w', 's', 'e', 'r', 'L', 'i', 'b', 'r', 'a', 'r', 'y', '.', 'j', 's' },
														{'b','r','o','w','s','e','r','W','i','n','d','o','w','.','j','s'}
													  };
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.IJsGlobalScopeContainerInitialzerExtension#getImage(org.eclipse.core.runtime.IPath, java.lang.String, org.eclipse.wst.jsdt.core.IJavaProject)
	 */
	public ImageDescriptor getImage(IPath containerPath, String element, IJavaProject project) {
		if(containerPath==null ) {
			return null;
		}
		/* Dont use the rino image for the individual files */
	//	IPath libFileName1 = new Path(new String(LIBRARY_FILE_NAME[0]));
	//	IPath libFileName2 = new Path(new String(LIBRARY_FILE_NAME[1]));
		
		String requestedContainerPath = new Path(element).lastSegment();
		if (element!=null && requestedContainerPath.equals(new String(LIBRARY_FILE_NAME[0]))) {
			return null;
		}else if (element!=null && requestedContainerPath.equals(new String(LIBRARY_FILE_NAME[1]))) {
			return null;
		}
		
		return ImageDescriptor.createFromFile(this.getClass(),"rino.gif"); //$NON-NLS-1$
	//	System.out.println("Unimplemented method:BasicBrowserLibraryJsGlobalScopeContainerInitializer.getImage");
		//return null;
	}

	public String getInferenceID() {
		return DefaultInferrenceProvider.ID;
	}
	
}
