/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.jsdt.core.IIncludePathAttribute;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.wst.jsdt.launching.JavaRuntime;
import org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration;


public class NativeLibAttributeConfiguration extends ClasspathAttributeConfiguration {
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration#getImageDescriptor(org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration.ClasspathAttributeAccess)
	 */
	public ImageDescriptor getImageDescriptor(ClasspathAttributeAccess attribute) {
		return JavaPluginImages.DESC_OBJS_NATIVE_LIB_PATH_ATTRIB;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration#getNameLabel(org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration.ClasspathAttributeAccess)
	 */
	public String getNameLabel(ClasspathAttributeAccess attribute) {
		return NewWizardMessages.CPListLabelProvider_native_library_path;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration#getValueLabel(org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration.ClasspathAttributeAccess)
	 */
	public String getValueLabel(ClasspathAttributeAccess attribute) {
		String arg= attribute.getClasspathAttribute().getValue();
		if (arg == null) {
			arg= NewWizardMessages.CPListLabelProvider_none;
		}
		return arg;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration#canEdit(org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration.ClasspathAttributeAccess)
	 */
	public boolean canEdit(ClasspathAttributeAccess attribute) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration#canRemove(org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration.ClasspathAttributeAccess)
	 */
	public boolean canRemove(ClasspathAttributeAccess attribute) {
		return attribute.getClasspathAttribute().getValue() != null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration#performEdit(org.eclipse.swt.widgets.Shell, org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration.ClasspathAttributeAccess)
	 */
	public IIncludePathAttribute performEdit(Shell shell, ClasspathAttributeAccess attribute) {
		NativeLibrariesDialog dialog= new NativeLibrariesDialog(shell, attribute.getClasspathAttribute().getValue(), attribute.getParentClasspassEntry());
		if (dialog.open() == Window.OK) {
			return JavaScriptCore.newIncludepathAttribute(JavaRuntime.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY, dialog.getNativeLibraryPath());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration#performRemove(org.eclipse.wst.jsdt.ui.wizards.ClasspathAttributeConfiguration.ClasspathAttributeAccess)
	 */
	public IIncludePathAttribute performRemove(ClasspathAttributeAccess attribute) {
		return JavaScriptCore.newIncludepathAttribute(JavaRuntime.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY, null);
	}

}
