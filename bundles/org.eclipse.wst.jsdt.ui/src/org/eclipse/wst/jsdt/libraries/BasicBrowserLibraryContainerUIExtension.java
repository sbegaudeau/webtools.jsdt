/**
 * 
 */
package org.eclipse.wst.jsdt.libraries;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.internal.ui.IJsGlobalScopeContainerInitializerExtension;

/**
 * @author childsb
 *
 */
public class BasicBrowserLibraryContainerUIExtension  implements  IJsGlobalScopeContainerInitializerExtension{
	public ImageDescriptor getImage(IPath containerPath, String element, IJavaScriptProject project) {

		if(containerPath==null) return null;
		/* Dont use the rino image for the individual files */
		String requestedContainerPath = new Path(element).getFileExtension();
		if(requestedContainerPath!=null && requestedContainerPath.equalsIgnoreCase("js")) return null;
		return ImageDescriptor.createFromFile(this.getClass(),"rino.gif");
	}
}
