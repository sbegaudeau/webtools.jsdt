/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.ui.actions;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wst.jsdt.debug.internal.core.Constants;
import org.eclipse.wst.jsdt.debug.internal.core.JavaScriptDebugPlugin;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Handles suspending the debugger when an exception is thrown
 * 
 * @since 1.1
 */
public class SuspendOnExceptionsAction extends ViewFilterAction {
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(JavaScriptDebugPlugin.PLUGIN_ID);
		if(prefs != null) {
			prefs.putBoolean(Constants.SUSPEND_ON_THROWN_EXCEPTION, action.isChecked());
			try {
				prefs.flush();
			} catch (BackingStoreException e) {
				JavaScriptDebugUIPlugin.log(e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.ui.actions.ViewFilterAction#getPreferenceStore()
	 */
	protected IPreferenceStore getPreferenceStore() {
		return JavaScriptDebugUIPlugin.getCorePreferenceStore();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.ui.actions.ViewFilterAction#getPreferenceValue()
	 */
	protected boolean getPreferenceValue() {
		return Platform.getPreferencesService().getBoolean(
				JavaScriptDebugPlugin.PLUGIN_ID,
				Constants.SUSPEND_ON_THROWN_EXCEPTION, 
				false, 
				null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.ui.actions.ViewFilterAction#getPreferenceKey()
	 */
	protected String getPreferenceKey() {
		return Constants.SUSPEND_ON_THROWN_EXCEPTION;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return false;
	}
}
