/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.core.launching;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.debug.core.jsdi.VirtualMachine;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.AttachingConnector;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.ListeningConnector;
import org.eclipse.wst.jsdt.debug.internal.core.JavaScriptDebugPlugin;
import org.eclipse.wst.jsdt.debug.internal.core.model.JavaScriptDebugTarget;

/**
 * Default launch delegate for a remote debug connection
 * 
 * @since 1.0
 */
public class RemoteJavaScriptLaunchDelegate extends LaunchConfigurationDelegate {

	final static String LAUNCH_URI = "launch_uri"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org. eclipse.debug.core.ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void launch(ILaunchConfiguration configuration, String mode, final ILaunch launch, IProgressMonitor monitor) throws CoreException {
		SubMonitor localmonitor = SubMonitor.convert(monitor, Messages.launching_js_debug_delegate, 16);
		try {
			String name = configuration.getAttribute(ILaunchConstants.CONNECTOR_ID, (String) null);
			Map argmap = configuration.getAttribute(ILaunchConstants.ARGUMENT_MAP, (Map) null);
			if (argmap == null) {
				Status status = new Status(
						IStatus.ERROR, 
						JavaScriptDebugPlugin.PLUGIN_ID, 
						Messages.argument_map_null);
				throw new CoreException(status);
			}
			if(localmonitor.isCanceled()) {
				return;
			}
			localmonitor.worked(4);
			localmonitor.subTask(Messages.acquiring_connector);
			Connector connector = JavaScriptDebugPlugin.getConnectionsManager().getConnector(name);
			VirtualMachine vm;
			try {
				if(localmonitor.isCanceled()) {
					return;
				}
				if(connector instanceof AttachingConnector) {
					localmonitor.subTask(Messages.attaching_to_vm);
					vm = ((AttachingConnector)connector).attach(argmap);
				}
				else if(connector instanceof ListeningConnector) {
					localmonitor.subTask(Messages.waiting_for_vm_to_connect);
					vm = ((ListeningConnector)connector).accept(argmap);
				}
				else {
					Status status = new Status(
							IStatus.ERROR, 
							JavaScriptDebugPlugin.PLUGIN_ID, 
							NLS.bind(Messages.could_not_locate_connector, new String[] {name}));
					throw new CoreException(status);
				}
				localmonitor.worked(4);
			} catch (IOException e) {
				Status status = new Status(IStatus.ERROR, JavaScriptDebugPlugin.PLUGIN_ID, "Error occured while launching", e); //$NON-NLS-1$
				throw new CoreException(status);
			}
			if(localmonitor.isCanceled()) {
				return;
			}
			localmonitor.worked(4);
			localmonitor.subTask(Messages.creating_debug_target);
			JavaScriptDebugTarget target = new JavaScriptDebugTarget(vm, null, launch, vm.name(), true, true);
			if(localmonitor.isCanceled()) {
				return;
			}
			localmonitor.worked(4);
			launch.addDebugTarget(target);
		}
		finally {
			localmonitor.done();
		}
	}
}
