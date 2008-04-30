/**
 * 
 */
package org.eclipse.wst.jsdt.internal.ui;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.wst.jsdt.core.IJavaScriptProject;

/**
 * @author childsb
 *
 */
public interface IJsGlobalScopeContainerInitializerExtension{
	public ImageDescriptor getImage(IPath containerPath, String element, IJavaScriptProject project);
}
