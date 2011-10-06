package org.eclipse.wst.jsdt.debug.internal.crossfire;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourceLookupDirector;
import org.eclipse.debug.core.sourcelookup.containers.ProjectSourceContainer;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector;
import org.eclipse.wst.jsdt.debug.core.jsdi.connect.Connector.Argument;
import org.eclipse.wst.jsdt.debug.internal.core.launching.ILaunchConstants;
import org.eclipse.wst.jsdt.debug.internal.core.launching.RemoteJavaScriptLaunchDelegate;
import org.eclipse.wst.jsdt.debug.internal.crossfire.connect.CrossfireAttachingConnector;
import org.eclipse.wst.jsdt.debug.internal.crossfire.connect.CrossfireListeningConnector;

/**
 * 
 */
public class LaunchHelper {

	public static final String REMOTE_LAUNCH_CONFIG_TYPE_ID = "org.eclipse.wst.jsdt.debug.core.launchConfigurationType"; //$NON-NLS-1$
	
	public static ILaunchConfiguration newRemoteConfiguration(String name, boolean attach) throws CoreException {
		ILaunchManager mgr = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = mgr.getLaunchConfigurationType(REMOTE_LAUNCH_CONFIG_TYPE_ID);
		if(type != null) {
			ILaunchConfigurationWorkingCopy copy = type.newInstance(null, mgr.generateLaunchConfigurationName(name));
			Connector connector = null;
			if(attach) {
				connector = new CrossfireAttachingConnector();
				copy.setAttribute(ILaunchConstants.CONNECTOR_ID, CrossfireAttachingConnector.CROSSFIRE_REMOTE_ATTACH_CONNECTOR_ID);
			}
			else {
				connector = new CrossfireListeningConnector();
				copy.setAttribute(ILaunchConstants.CONNECTOR_ID, CrossfireListeningConnector.CROSSFIRE_REMOTE_ATTACH_CONNECTOR_ID);	
			}
			Map args = connector.defaultArguments();
			Map newargs = new HashMap();
			Entry entry = null;
			Argument arg = null;
			for(Iterator i = args.entrySet().iterator(); i.hasNext();) {
				entry = (Entry) i.next();
				arg = (Argument) entry.getValue();
				newargs.put(entry.getKey(), arg.toString());
			}
			copy.setAttribute(ILaunchConstants.ARGUMENT_MAP, newargs);
			return copy.doSave();
		}
		return null;
	}
	
	public static void doLaunch(ILaunchConfigurationDelegate2 delegate, ILaunch launch, ILaunchConfiguration config, IProgressMonitor monitor) throws CoreException {
		SubMonitor localmonitor = SubMonitor.convert(monitor, "launch both", 6); //$NON-NLS-1$
		try {
			delegate.launch(config, "debug", launch, localmonitor); //$NON-NLS-1$
			if(!localmonitor.isCanceled()) {
				localmonitor.worked(2);
			}
			ILaunchConfiguration cfg = newRemoteConfiguration("foo", true); //$NON-NLS-1$
			if(!localmonitor.isCanceled()) {
				localmonitor.worked(2);
			}
			if(cfg != null) {
				RemoteJavaScriptLaunchDelegate del = new RemoteJavaScriptLaunchDelegate();
				del.launch(cfg, "debug", launch, localmonitor); //$NON-NLS-1$
			}
			if(!localmonitor.isCanceled()) {
				localmonitor.worked(2);
			}
		}
		finally {
			if(!localmonitor.isCanceled()) {
				localmonitor.done();
			}
		}
	}
	
	public static void appendProjectContainer(ILaunch launch, String pname) {
		ISourceLocator loc = launch.getSourceLocator();
		if(loc instanceof ISourceLookupDirector) {
			ISourceLookupDirector director = (ISourceLookupDirector) loc;
			IProject pj = ResourcesPlugin.getWorkspace().getRoot().getProject(pname);
			if(pj.exists()) {
				ProjectSourceContainer container = new ProjectSourceContainer(pj, false);
				ISourceContainer[] containers = director.getSourceContainers();
				ISourceContainer[] newcontainers = new ISourceContainer[containers.length+1];
				newcontainers[0] = container;
				System.arraycopy(containers, 0, newcontainers, 1, containers.length);
				director.setSourceContainers(newcontainers);
			}
		}
	}
}
