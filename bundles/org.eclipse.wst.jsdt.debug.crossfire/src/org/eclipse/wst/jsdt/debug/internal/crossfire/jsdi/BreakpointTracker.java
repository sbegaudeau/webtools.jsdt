/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire.jsdi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLineBreakpoint;
import org.eclipse.wst.jsdt.debug.core.jsdi.ScriptReference;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;
import org.eclipse.wst.jsdt.debug.internal.core.JavaScriptDebugPlugin;
import org.eclipse.wst.jsdt.debug.internal.core.breakpoints.JavaScriptLineBreakpoint;
import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;

/**
 * Utility class to handle tracking remote to local breakpoints
 * 
 * @since 3.4
 */
public final class BreakpointTracker {

	/**
	 * Mapping of breakpoint handles to {@link RemoteBreakpoint}: <code>Map&lt;Number, RemoteBreakpoint&gt;</code>
	 */
	private static Map/*<Number, RemoteBreakpoint>*/ breakpointHandles = new HashMap();
	/**
	 * Mapping of breakpoint handles to local {@link IJavaScriptBreakpoint}s: <code>Map&lt;Number, IJavaScriptBreakpoint&gt;</code>
	 */
	private static Map/*<Number, IJavaScriptBreakpoint>*/ handleToLocal = new HashMap();
	/**
	 * Mapping of {@link CFVirtualMachine} to {@link HashSet} of breakpoint handles: <code>Map&lt;CFVirtualMachine, HashSet&lt;Number&gt;&gt;</code>
	 */
	private static Map/*<CFVirtualMachine, HashSet>*/vmToHandles = new HashMap();
	
	/**
	 * Add the breakpoint described by the given JSON to the handles list
	 * 
	 * @param vm the {@link CFVirtualMachine}, cannot be <code>null</code>
	 * @param json the JSON map object, cannot be <code>null</code>
	 * @return the newly added {@link RemoteBreakpoint} or <code>null</code> if one could not be created and added
	 */
	public static RemoteBreakpoint addBreakpoint(CFVirtualMachine vm, Map json) {
		Assert.isNotNull(vm, Messages.BreakpointTracker_2);
		Assert.isNotNull(json, Messages.BreakpointTracker_3);
		Number handle = (Number) json.get(Attributes.HANDLE);
		if(handle != null) {
			RemoteBreakpoint bp = (RemoteBreakpoint) breakpointHandles.get(handle);
			if(bp == null) {
				bp = new RemoteBreakpoint(vm, 
					handle,
					(Map) json.get(Attributes.LOCATION),
					(Map) json.get(Attributes.ATTRIBUTES),
					(String)json.get(Attributes.TYPE));
				breakpointHandles.put(handle, bp);
				
			}
			HashSet handles = (HashSet) vmToHandles.get(handle);
			if(handles == null) {
				handles = new HashSet();
				vmToHandles.put(vm, handles);
			}
			handles.add(handle);
			return bp;
		}
		return null;
	}
	
	/**
	 * Create a local version of the breakpoint if one does not exist
	 * 
	 * @param vm the {@link CFVirtualMachine}, cannot be <code>null</code>
	 * @param json the JSON map describing the breakpoint, cannot be <code>null</code>
	 * @return the new {@link IJavaScriptBreakpoint} or <code>null</code> if it could not be created
	 */
	public static IJavaScriptBreakpoint createLocalBreakpoint(CFVirtualMachine vm, Map json) {
		Assert.isNotNull(vm, Messages.BreakpointTracker_5);
		Assert.isNotNull(json, Messages.BreakpointTracker_6);
		RemoteBreakpoint rb = addBreakpoint(vm, json);
		if(rb != null && rb.isLineBreakpoint()) {
			IJavaScriptBreakpoint bp = findLocalBreakpoint(rb);
			if(bp != null) {
				return bp;
			}
			ScriptReference script = rb.vm.findScript(rb.getUrl());
			if(script != null) {
				IFile file = JavaScriptDebugPlugin.getResolutionManager().getFile(script);
				if(file != null) {
					HashMap attributes = new HashMap();
					attributes.put(IJavaScriptBreakpoint.TYPE_NAME, null);
					attributes.put(IJavaScriptBreakpoint.SCRIPT_PATH, file.getFullPath().makeAbsolute().toString());
					attributes.put(IJavaScriptBreakpoint.ELEMENT_HANDLE, null);
					String condition = rb.getCondition();
					if(condition != null && condition.trim().length() > 0) {
						attributes.put(JavaScriptLineBreakpoint.CONDITION, condition);
						attributes.put(JavaScriptLineBreakpoint.CONDITION_ENABLED, Boolean.TRUE);
						attributes.put(JavaScriptLineBreakpoint.CONDITION_SUSPEND_ON_TRUE, Boolean.TRUE);
					}
					try {
						bp = JavaScriptDebugModel.createLineBreakpoint(file, rb.getLine(), -1, -1, attributes, true);
						handleToLocal.put(rb.getHandle(), bp);
						return bp;
					}
					catch(DebugException de) {}
				}
			}
		}
		return null;
	}
	
	/**
	 * Finds the local equivalent {@link IJavaScriptBreakpoint} given the {@link RemoteBreakpoint}
	 * 
	 * @param breakpoint the {@link RemoteBreakpoint}, cannot be <code>null</code>
	 * @return the local equivalent of the given {@link RemoteBreakpoint} or <code>null</code> if one does not exist
	 */
	public static final IJavaScriptBreakpoint findLocalBreakpoint(RemoteBreakpoint breakpoint) {
		Assert.isNotNull(breakpoint, Messages.BreakpointTracker_7);
		IJavaScriptBreakpoint bp = getLocalBreakpoint(breakpoint.getHandle());
		if(bp != null) {
			return bp;
		}
		//else try to find it and map it
		ScriptReference script = breakpoint.vm.findScript(breakpoint.getUrl());
		if(script != null) {
			IFile file = JavaScriptDebugPlugin.getResolutionManager().getFile(script);
			if(file != null) {
				IBreakpoint[] bps = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints(JavaScriptDebugModel.MODEL_ID);
				for (int i = 0; i < bps.length; i++) {
					if(bps[i] instanceof IJavaScriptLineBreakpoint) {
						IJavaScriptLineBreakpoint jsbp = (IJavaScriptLineBreakpoint) bps[i];
						try {
							String scriptpath = jsbp.getScriptPath();
							if(file.getFullPath().isPrefixOf(new Path(scriptpath)) &&
									breakpoint.getLine() == jsbp.getLineNumber()) {
								//potential match, now check the location, for now simply check the line number
								//map it
								handleToLocal.put(breakpoint.getHandle(), jsbp);
								return jsbp;
							}
						}
						catch(CoreException ce) {}
					}
					else {
						IJavaScriptBreakpoint jsbp = (IJavaScriptBreakpoint) bps[i];
						try {
							String scriptpath = jsbp.getScriptPath();
							//script load / exception breakpoints have no further location than the script
							if(file.getFullPath().isPrefixOf(new Path(scriptpath))) {
								//map it
								handleToLocal.put(breakpoint.getHandle(), jsbp);
								return jsbp;
							}
						}
						catch(CoreException ce) {}
					}
					
				}
			}
		}
		return null;
	}
	
	/**
	 * Look up the {@link RemoteBreakpoint} with the given {@link IJavaScriptBreakpoint}
	 * 
	 * @param breakpoint the local {@link IJavaScriptBreakpoint}, cannot be <code>null</code>
	 * @return the {@link RemoteBreakpoint} that matches the local {@link IJavaScriptBreakpoint} or <code>null</code>
	 * if not match is found
	 */
	public static RemoteBreakpoint findRemoteBreakpoint(IJavaScriptBreakpoint breakpoint) {
		Assert.isNotNull(breakpoint, Messages.BreakpointTracker_12);
		for (Iterator i = handleToLocal.entrySet().iterator(); i.hasNext();) {
			Entry entry = (Entry) i.next();
			if(breakpoint.equals(entry.getValue())) {
				Number handle = (Number) entry.getKey();
				return getBreakpoint(handle);
			}
		}
		//failed to find it in the cache, try to find it by location
		for (Iterator i = breakpointHandles.entrySet().iterator(); i.hasNext();) {
			Entry entry = (Entry) i.next();
			try {
				RemoteBreakpoint rb = (RemoteBreakpoint) entry.getValue();
				ScriptReference script = rb.vm.findScript(rb.getUrl());
				if(script != null) {
					IFile file = JavaScriptDebugPlugin.getResolutionManager().getFile(script);
					if(file != null) {
						if(breakpoint instanceof IJavaScriptLineBreakpoint) {
							IJavaScriptLineBreakpoint lb = (IJavaScriptLineBreakpoint) breakpoint;
							String scriptpath = breakpoint.getScriptPath();
							if(file.getFullPath().isPrefixOf(new Path(scriptpath)) &&
									rb.getLine() == lb.getLineNumber()) {
								handleToLocal.put(rb.handle, breakpoint);
								return rb;
							}
						}
						else {
							String scriptpath = breakpoint.getScriptPath();
							//script load / exception breakpoints have no further location than the script
							if(file.getFullPath().isPrefixOf(new Path(scriptpath))) {
								//map it
								handleToLocal.put(rb.getHandle(), breakpoint);
								return rb;
							}
						}
					}
						
				}
			}
			catch(CoreException ce) {}
		}
		return null;
	}
	
	/**
	 * This method synchronizes the enabled and condition attributes from the given {@link IJavaScriptBreakpoint} onto the given
	 * {@link RemoteBreakpoint}.
	 * 
	 * @param rbreakpoint the {@link RemoteBreakpoint}, cannot be <code>null</code>
	 * @param breakpoint the local {@link IJavaScriptBreakpoint}, cannot be <code>null</code>
	 * @throws CoreException if we failed to read properties from the {@link IJavaScriptBreakpoint}
	 */
	public static final void syncRemoteBreakpoint(RemoteBreakpoint rbreakpoint, IJavaScriptBreakpoint breakpoint) throws CoreException {
		Assert.isNotNull(rbreakpoint, Messages.BreakpointTracker_13);
		Assert.isNotNull(breakpoint, Messages.BreakpointTracker_14);
		rbreakpoint.setEnabled(breakpoint.isEnabled());
		if(breakpoint instanceof IJavaScriptLineBreakpoint) {
			IJavaScriptLineBreakpoint lb = (IJavaScriptLineBreakpoint) breakpoint;
			if(lb.isConditionEnabled()) {
				rbreakpoint.setCondition(lb.getCondition());
			}
			else {
				rbreakpoint.setCondition(null);
			}
		}
	}
	
	/**
	 * Returns the {@link RemoteBreakpoint} with the given handle
	 * 
	 * @param handle the handle, cannot be <code>null</code>
	 * @return the {@link RemoteBreakpoint} with the given handle or <code>null</code>
	 */
	public static final RemoteBreakpoint getBreakpoint(Number handle) {
		Assert.isNotNull(handle, Messages.BreakpointTracker_0);
		return (RemoteBreakpoint) breakpointHandles.get(handle);
	}
	
	/**
	 * Returns the {@link IJavaScriptBreakpoint} mapped to the given {@link RemoteBreakpoint} handle
	 * 
	 * @param handle the handle, cannot be <code>null</code>
	 * @return the mapped {@link IJavaScriptBreakpoint} or <code>null</code>
	 */
	public static final IJavaScriptBreakpoint getLocalBreakpoint(Number handle) {
		Assert.isNotNull(handle, Messages.BreakpointTracker_10);
		return (IJavaScriptBreakpoint) handleToLocal.get(handle);
	}
	
	/**
	 * Removes the {@link RemoteBreakpoint} with the given handle
	 * 
	 * @param vm the {@link CFVirtualMachine} that processed the request
	 * @param handle the breakpoint handle, cannot be <code>null</code>
	 * @return the {@link RemoteBreakpoint} that was removed or <code>null</code>
	 */
	public static RemoteBreakpoint removeBreakpoint(CFVirtualMachine vm, Number handle) {
		Assert.isNotNull(handle, Messages.BreakpointTracker_1);
		RemoteBreakpoint rb = (RemoteBreakpoint) breakpointHandles.remove(handle);
		if(rb != null) {
			HashSet handles = (HashSet) vmToHandles.get(vm);
			if(handles != null) {
				handles.remove(handle);
			}
			handleToLocal.remove(handle);
		}
		return rb;
	}
	
	/**
	 * Removes the local breakpoint given its remote equivalent
	 * @param vm the {@link CFVirtualMachine} that processed the removal request
	 * @param handle the remote breakpoint, cannot be <code>null</code>
	 */
	public static void removeLocalBreakpoint(CFVirtualMachine vm, Number handle) {
		Assert.isNotNull(handle, Messages.BreakpointTracker_9);
		RemoteBreakpoint rb = removeBreakpoint(vm, handle);
		if(rb != null) {
			IJavaScriptBreakpoint jsbp = findLocalBreakpoint(rb);
			if(jsbp != null) {
				try {
					jsbp.delete();
				} catch (CoreException e) {
				}
			}
		}
	}
	
	/**
	 * Locates the breakpoint for the handle given in the map and updates its attributes
	 * 
	 * @param json the JSON map, cannot be <code>null</code>
	 * @return the {@link RemoteBreakpoint} that was updated or <code>null</code> if nothing was updated
	 */
	public static RemoteBreakpoint updateBreakpoint(Map json) {
		Assert.isNotNull(json, Messages.BreakpointTracker_4);
		Number handle = (Number) json.get(Attributes.HANDLE);
		if(handle != null) {
			RemoteBreakpoint bp = (RemoteBreakpoint) breakpointHandles.get(handle);
			if(bp != null) {
				bp.setEnabled(RemoteBreakpoint.getEnabled(json));
				bp.setCondition(RemoteBreakpoint.getCondition(json));
				updateLocalBreakpoint(bp);
			}
			return bp;
		}
		return null;
	}
	
	/**
	 * Tries to locate and update the local version of the given {@link RemoteBreakpoint}
	 * @param rb the {@link RemoteBreakpoint}, cannot be <code>null</code>
	 * @return the {@link IJavaScriptBreakpoint} that was updated or <code>null</code> if no update occurred
	 */
	public static IJavaScriptBreakpoint updateLocalBreakpoint(RemoteBreakpoint rb) {
		Assert.isNotNull(rb, Messages.BreakpointTracker_8);
		IJavaScriptBreakpoint bp = getLocalBreakpoint(rb.getHandle());
		if(bp instanceof IJavaScriptLineBreakpoint) {
			try {
				Map attributes = bp.getMarker().getAttributes();
				boolean edited = false;
				if(bp.isEnabled() != rb.isEnabled()) {
					attributes.put(IBreakpoint.ENABLED, Boolean.valueOf(rb.isEnabled()));
					edited = true;
				}
				String condition = ((IJavaScriptLineBreakpoint) bp).getCondition();
				String rbcondition = rb.getCondition();
				if(rbcondition != null) {
					if(!rbcondition.equals(condition)) {
						attributes.put(JavaScriptLineBreakpoint.CONDITION, rbcondition);
						attributes.put(JavaScriptLineBreakpoint.CONDITION_ENABLED, Boolean.TRUE);
						attributes.put(JavaScriptLineBreakpoint.CONDITION_SUSPEND_ON_TRUE, Boolean.TRUE);
						edited = true;
					}
				}
				else if(condition != null) {
					attributes.remove(JavaScriptLineBreakpoint.CONDITION);
					attributes.remove(JavaScriptLineBreakpoint.CONDITION_ENABLED);
					attributes.remove(JavaScriptLineBreakpoint.CONDITION_SUSPEND_ON_TRUE);
					edited = true;
				}
				if(edited) {
					bp.getMarker().setAttributes(attributes);
				}
			}
			catch(CoreException ce) {
				ce.printStackTrace();
			}
			return bp;
		}
		return null;
	}
	
	/**
	 * Disconnects and un-caches all of the breakpoints - both local and remote - that were being tracked
	 * on behalf of the given {@link CFVirtualMachine}
	 * 
	 * @param vm the {@link CFVirtualMachine}, cannot be <code>null</code>
	 */
	public static void disconnect(CFVirtualMachine vm) {
		Assert.isNotNull(vm, Messages.BreakpointTracker_11);
		HashSet handles = (HashSet) vmToHandles.get(vm);
		if(handles != null) {
			for (Iterator i = handles.iterator(); i.hasNext();) {
				Number handle = (Number) i.next();
				handleToLocal.remove(handle);
				breakpointHandles.remove(handle);
			}
			vmToHandles.remove(vm);
		}
	}
}
