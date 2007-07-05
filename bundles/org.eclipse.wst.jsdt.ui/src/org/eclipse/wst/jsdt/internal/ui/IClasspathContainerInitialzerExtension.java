/**
 * 
 */
package org.eclipse.wst.jsdt.internal.ui;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.jsdt.core.IClasspathContainerInitialzer;
import org.eclipse.wst.jsdt.core.IJavaProject;

/**
 * @author childsb
 *
 */
public interface IClasspathContainerInitialzerExtension extends IClasspathContainerInitialzer {
	public ImageDescriptor getImage(IPath containerPath, String element, IJavaProject project);
}
