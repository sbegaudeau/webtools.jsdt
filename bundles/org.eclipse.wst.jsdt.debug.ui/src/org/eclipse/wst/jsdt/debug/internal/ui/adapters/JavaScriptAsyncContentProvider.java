package org.eclipse.wst.jsdt.debug.internal.ui.adapters;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.debug.internal.ui.model.elements.ElementContentProvider;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IViewerUpdate;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptDebugTarget;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptThread;
import org.eclipse.wst.jsdt.debug.core.model.IScript;
import org.eclipse.wst.jsdt.debug.core.model.IScriptGroup;
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
	
	/**
	 * Comparator for scripts
	 */
	static Comparator scriptcompare = new ScriptComparator(); 
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.model.elements.DebugTargetContentProvider#getChildren(java.lang.Object, int, int, org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext, org.eclipse.debug.internal.ui.viewers.model.provisional.IViewerUpdate)
	 */
	protected Object[] getChildren(Object parent, int index, int length, IPresentationContext context, IViewerUpdate monitor) throws CoreException {
		if(parent instanceof IJavaScriptDebugTarget) {
			IJavaScriptDebugTarget target = (IJavaScriptDebugTarget) parent;
			Object[] threads = target.getThreads();
			if(PreferencesManager.getManager().showLoadedScripts()) {
				Object[] children = new Object[threads.length + 1];
				children[0] = target.getScriptGroup();
				System.arraycopy(threads, 0, children, 1, threads.length);
				return getElements(children, index, length);
			}
			return threads;
		}
		if(parent instanceof IScriptGroup) {
			IScriptGroup group = (IScriptGroup) parent;
			Object[] scripts = getElements(group.allScripts().toArray(), index, length);
			Arrays.sort(scripts, scriptcompare);
			return scripts;
		}
		if(parent instanceof IJavaScriptThread) {
			return ((IJavaScriptThread)parent).getStackFrames();
		}
		return NO_CHILDREN;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.internal.ui.model.elements.DebugTargetContentProvider#getChildCount(java.lang.Object, org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext, org.eclipse.debug.internal.ui.viewers.model.provisional.IViewerUpdate)
	 */
	protected int getChildCount(Object element, IPresentationContext context, IViewerUpdate monitor) throws CoreException {
		if(element instanceof IJavaScriptDebugTarget) {
			IJavaScriptDebugTarget target = (IJavaScriptDebugTarget) element;
			int count = target.getThreads().length;
			if(target.getScriptGroup() != null && PreferencesManager.getManager().showLoadedScripts()) {
				count = count + 1;
			}
			return count;
		}
		if(element instanceof IScriptGroup) {
			return ((IScriptGroup) element).allScripts().size();
		}
		if(element instanceof IJavaScriptThread) {
			return ((IJavaScriptThread)element).getFrameCount();
		}
		return 0;
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
