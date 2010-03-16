package org.eclipse.wst.jsdt.debug.internal.ui.adapters;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.internal.ui.model.elements.ElementContentProvider;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IViewerUpdate;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptDebugTarget;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptThread;
import org.eclipse.wst.jsdt.debug.core.model.IScript;
import org.eclipse.wst.jsdt.debug.core.model.IScriptGroup;
import org.eclipse.wst.jsdt.debug.internal.core.model.ScriptGroup;
import org.eclipse.wst.jsdt.debug.internal.ui.PreferencesManager;

/**
 * Custom content provider for our {@link IJavaScriptDebugElement}s
 * 
 * @since 1.0
 */
public class JavaScriptAsyncContentProvider extends ElementContentProvider {

	static final Object[] NO_CHILDREN = {};
	/**
	 * Comparator to sort scripts by name
	 */
	static class ScriptComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			return URIUtil.lastSegment(((IScript)o1).sourceURI()).compareTo(URIUtil.lastSegment(((IScript)o2).sourceURI()));
		}
	}
	
	static Comparator scriptcompare = new ScriptComparator(); 
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.model.elements.DebugTargetContentProvider#getChildren(java.lang.Object, int, int, org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext, org.eclipse.debug.internal.ui.viewers.model.provisional.IViewerUpdate)
	 */
	protected Object[] getChildren(Object parent, int index, int length, IPresentationContext context, IViewerUpdate monitor) throws CoreException {
		if(parent instanceof IJavaScriptDebugTarget) {
			return getTargetChildren((IJavaScriptDebugTarget) parent, index, length);
		}
		if(parent instanceof IScriptGroup) {
			return getScriptGroupChildren((IScriptGroup) parent, index, length);
		}
		if(parent instanceof IJavaScriptThread) {
			return ((IJavaScriptThread)parent).getStackFrames();
		}
		return NO_CHILDREN;
	}
	
	/**
	 * Returns the complete listing of children for the given {@link IJavaScriptDebugTarget}
	 * @param target
	 * @param index
	 * @param length
	 * @return the children for the given {@link IJavaScriptDebugTarget}
	 * @throws DebugException
	 */
	Object[] getTargetChildren(IJavaScriptDebugTarget target, int index, int length) throws DebugException {
		Object[] threads = target.getThreads();
		if(PreferencesManager.getManager().showLoadedScripts()) {
			Object[] children = new Object[threads.length + 1];
			children[0] = new ScriptGroup(target);
			System.arraycopy(threads, 0, children, 1, threads.length);
			return getElements(children, index, length);
		}
		return threads;
	}
	
	/**
	 * Returns the collection of children from the given {@link IScriptGroup}
	 * @param group
	 * @return the children of the {@link IScriptGroup}
	 */
	Object[] getScriptGroupChildren(IScriptGroup group, int index, int length) {
		Object[] scripts = getElements(group.allScripts().toArray(), index, length);
		Arrays.sort(scripts, scriptcompare);
		return scripts;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.model.elements.DebugTargetContentProvider#getChildCount(java.lang.Object, org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext, org.eclipse.debug.internal.ui.viewers.model.provisional.IViewerUpdate)
	 */
	protected int getChildCount(Object element, IPresentationContext context, IViewerUpdate monitor) throws CoreException {
		if(element instanceof IJavaScriptDebugTarget) {
			return targetChildCount((IJavaScriptDebugTarget) element);
		}
		if(element instanceof IScriptGroup) {
			return ((IScriptGroup) element).allScripts().size();
		}
		if(element instanceof IJavaScriptThread) {
			return ((IJavaScriptThread)element).getFrameCount();
		}
		return 0;
	}
	
	/**
	 * Returns the total known child count for the given {@link IJavaScriptDebugTarget}
	 * @param target
	 * @return the total known child count for the given target
	 * @throws DebugException
	 */
	int targetChildCount(IJavaScriptDebugTarget target) throws DebugException {
		int count = target.getThreads().length;
		if(PreferencesManager.getManager().showLoadedScripts()) {
			count = count + 1;
		}
		return count;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.model.elements.ElementContentProvider#supportsContext(org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext)
	 */
	protected boolean supportsContext(IPresentationContext context) {
		return IDebugUIConstants.ID_DEBUG_VIEW.equals(context.getId());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.model.elements.ElementContentProvider#supportsContextId(java.lang.String)
	 */
	protected boolean supportsContextId(String id) {
		return IDebugUIConstants.ID_DEBUG_VIEW.equals(id);
	}
}
