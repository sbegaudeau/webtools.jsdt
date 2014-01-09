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
package org.eclipse.wst.jsdt.debug.internal.ui.adapters;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IModelDelta;
import org.eclipse.debug.internal.ui.viewers.model.provisional.ModelDelta;
import org.eclipse.debug.internal.ui.viewers.provisional.AbstractModelProxy;
import org.eclipse.debug.internal.ui.viewers.update.DebugTargetEventHandler;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptDebugTarget;
import org.eclipse.wst.jsdt.debug.core.model.IScriptGroup;
import org.eclipse.wst.jsdt.debug.internal.core.model.ScriptGroup;
import org.eclipse.wst.jsdt.debug.internal.ui.PreferencesManager;

/**
 * Custom handler for JavaScript debug target model proxy events
 * 
 * @since 1.0
 */
public class JavaScriptDebugTargetEventHandler extends DebugTargetEventHandler {

	/**
	 * Constructor
	 * @param proxy
	 */
	public JavaScriptDebugTargetEventHandler(AbstractModelProxy proxy) {
		super(proxy);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.viewers.update.DebugTargetEventHandler#handlesEvent(org.eclipse.debug.core.DebugEvent)
	 */
	protected boolean handlesEvent(DebugEvent event) {
		Object source = event.getSource();
		return source instanceof IJavaScriptDebugTarget || source instanceof IScriptGroup;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.viewers.update.DebugTargetEventHandler#handleChange(org.eclipse.debug.core.DebugEvent)
	 */
	protected void handleChange(DebugEvent event) {
		Object source = event.getSource();
		if(source instanceof IDebugTarget) {
			super.handleChange(event);
		}
		else if(source instanceof IScriptGroup) {
			if(PreferencesManager.getManager().showLoadedScripts()) {
				fireScriptGroupDelta((IScriptGroup) source);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.viewers.update.DebugTargetEventHandler#handleCreate(org.eclipse.debug.core.DebugEvent)
	 */
	protected void handleCreate(DebugEvent event) {
		Object source = event.getSource();
		if(source instanceof IDebugTarget) {
			super.handleCreate(event);
		}
		else if(source instanceof IScriptGroup) {
			if(PreferencesManager.getManager().showLoadedScripts()) {
				fireScriptGroupDelta((IScriptGroup) source);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.viewers.update.DebugTargetEventHandler#handleResume(org.eclipse.debug.core.DebugEvent)
	 */
	protected void handleResume(DebugEvent event) {
		Object source = event.getSource();
		if(source instanceof IDebugTarget) {
			super.handleResume(event);
		}
		else if(source instanceof IScriptGroup) {
			if(PreferencesManager.getManager().showLoadedScripts()) {
				fireScriptGroupDelta((IScriptGroup) source);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.viewers.update.DebugTargetEventHandler#handleSuspend(org.eclipse.debug.core.DebugEvent)
	 */
	protected void handleSuspend(DebugEvent event) {
		Object source = event.getSource();
		if(source instanceof IDebugTarget) {
			super.handleSuspend(event);
		}
		else if(source instanceof IScriptGroup) {
			if(PreferencesManager.getManager().showLoadedScripts()) {
				fireScriptGroupDelta((IScriptGroup) source);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.viewers.update.DebugTargetEventHandler#handleTerminate(org.eclipse.debug.core.DebugEvent)
	 */
	protected void handleTerminate(DebugEvent event) {
		Object source = event.getSource();
		if(source instanceof IDebugTarget) {
			super.handleTerminate(event);
		}
		else if(source instanceof IScriptGroup){
			if(PreferencesManager.getManager().showLoadedScripts()) {
				fireScriptGroupDelta((IScriptGroup) source);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.viewers.update.DebugEventHandler#handleOther(org.eclipse.debug.core.DebugEvent)
	 */
	protected void handleOther(DebugEvent event) {
		Object source = event.getSource();
		if(source instanceof IJavaScriptDebugTarget) {
			super.handleOther(event);
		}
		else if(source instanceof IScriptGroup){
			if(PreferencesManager.getManager().showLoadedScripts()) {
				fireScriptGroupDelta((IScriptGroup) source);
			}
		}
	}
	
	void fireScriptGroupDelta(IScriptGroup group) {
		ModelDelta root = new ModelDelta(DebugPlugin.getDefault().getLaunchManager(), IModelDelta.NO_CHANGE);
		IDebugTarget target = ((ScriptGroup)group).getDebugTarget();
		ModelDelta targetnode = root.addNode(target.getLaunch(), IModelDelta.NO_CHANGE);
		ModelDelta groupnode = targetnode.addNode(target, IModelDelta.NO_CHANGE);
		groupnode.addNode(group, IModelDelta.CONTENT);
		fireDelta(root);
	}
}
