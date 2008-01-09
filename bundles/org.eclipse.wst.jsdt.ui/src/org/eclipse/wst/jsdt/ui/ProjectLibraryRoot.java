/**
 * 
 */
package org.eclipse.wst.jsdt.ui;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;

/**
 * @author childsb
 *
 */
public class ProjectLibraryRoot implements IAdaptable{
	
	private IJavaProject project;
	private static final String LIBRARY_UI_DESC = "JavaScript Global Scope Libraries";
	private Object[] children;
	
	public static final class WorkBenchAdapter implements IWorkbenchAdapter{

		/* (non-Javadoc)
		 * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
		 */
		public Object[] getChildren(Object o) {
			
		 if(o instanceof ProjectLibraryRoot) return ((ProjectLibraryRoot)o).getChildren();
		 return new Object[0];
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object)
		 */
		public ImageDescriptor getImageDescriptor(Object object) {
			return JavaPluginImages.DESC_OBJS_LIBRARY;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
		 */
		public String getLabel(Object o) {
			if(o instanceof ProjectLibraryRoot) {
				return LIBRARY_UI_DESC;
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.model.IWorkbenchAdapter#getParent(java.lang.Object)
		 */
		public Object getParent(Object o) {
			// TODO Auto-generated method stub
			System.out.println("Unimplemented method:WorkBenchAdapter.getParent");
			return null;
		}
		
	}
	
	public ProjectLibraryRoot(IJavaProject project, Object[] children) {
		this.project=project;
		this.children = children;
	}
	public IJavaProject getProject() {
		return project;
	}
	public String getText() {
		return "JavaScript Global Scope";
	}
	public Object[] getChildren() {
		return children;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if(adapter == IWorkbenchAdapter.class) {
			return new WorkBenchAdapter();
		}
		
		return null;
	}
}
