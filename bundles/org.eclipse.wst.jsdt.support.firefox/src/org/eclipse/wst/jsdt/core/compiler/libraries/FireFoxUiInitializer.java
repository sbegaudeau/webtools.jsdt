/**
 * 
 */
package org.eclipse.wst.jsdt.core.compiler.libraries;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.internal.ui.IJsGlobalScopeContainerInitializerExtension;

/**
 * @author childsb
 *
 */
public class FireFoxUiInitializer  implements IJsGlobalScopeContainerInitializerExtension {
	public ImageDescriptor getImage(IPath containerPath, String element, IJavaScriptProject project) {

		if (containerPath == null) {
			return null;
		}
		/* Dont use the rino image for the individual files */
		String requestedContainerPath = new Path(element).lastSegment();
		if ((element != null) && requestedContainerPath.equals(new String(FireFoxLibInitializer.LIBRARY_FILE_NAMES[0]))) {
			return null;
		}

		return ImageDescriptor.createFromFile(this.getClass(), "FireFoxSmall.gif");
		// System.out.println("Unimplemented
		// method:BasicBrowserLibraryJsGlobalScopeContainerInitializer.getImage");
		// return null;
	}

}
