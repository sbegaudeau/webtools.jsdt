/**
 * 
 */
package org.eclipse.wst.jsdt.internal.ui;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainerInitialzer;
import org.eclipse.wst.jsdt.core.IJavaProject;

/**
 * @author childsb
 *
 */
public interface IJsGlobalScopeContainerInitialzerExtension extends IJsGlobalScopeContainerInitialzer {
	public ImageDescriptor getImage(IPath containerPath, String element, IJavaProject project);
}
