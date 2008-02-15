/**
 * 
 */
package org.eclipse.wst.jsdt.ui;

import java.util.ArrayList;


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.wst.jsdt.core.IClasspathAttribute;
import org.eclipse.wst.jsdt.core.IClasspathEntry;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.internal.ui.JavaPluginImages;
import org.eclipse.wst.jsdt.internal.ui.packageview.JsGlobalScopeContainer;
import org.eclipse.wst.jsdt.internal.ui.packageview.LibraryContainer;

/**
 * @author childsb
 *
 */
public class ProjectLibraryRoot implements IAdaptable{
	
	private IJavaProject project;
	private static final String LIBRARY_UI_DESC = Messages.getString("ProjectLibraryRoot.0"); //$NON-NLS-1$

	
	public static final class WorkBenchAdapter implements IWorkbenchAdapter{

		/* (non-Javadoc)
		 * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
		 */
		public Object[] getChildren(Object o) {
			
		 if(o instanceof ProjectLibraryRoot) 
			return ((ProjectLibraryRoot)o).getChildren();    		 
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
			System.out.println("Unimplemented method:WorkBenchAdapter.getParent"); //$NON-NLS-1$
			return null;
		}
		
	}
	
	public ProjectLibraryRoot(IJavaProject project) {
		this.project=project;
	
	}
	public IJavaProject getProject() {
		return project;
	}
	public String getText() {
		return ProjectLibraryRoot.LIBRARY_UI_DESC;
	}
	public Object[] getChildren() {
	     if (!project.getProject().isOpen())
				return new Object[0];
			boolean addJARContainer= false;
			ArrayList projectPackageFragmentRoots  = new ArrayList();
			IPackageFragmentRoot[] roots = new IPackageFragmentRoot[0];
			try {
				roots = project.getPackageFragmentRoots();
			}
			catch (JavaModelException e1) {}
			for (int i= 0; i < roots.length; i++) {
				IPackageFragmentRoot root= roots[i];
				IClasspathEntry classpathEntry=null;
				try {
					classpathEntry = root.getRawClasspathEntry();
				}
				catch (JavaModelException e) {}
				
				int entryKind= classpathEntry.getEntryKind();
				IClasspathAttribute[] attribs = classpathEntry.getExtraAttributes();
				
				for(int k = 0;attribs!=null && k<attribs.length;k++) {
					if(attribs[k]==IClasspathAttribute.HIDE) continue;
					
				}
				
				
			if ( (entryKind != IClasspathEntry.CPE_SOURCE) && entryKind!=IClasspathEntry.CPE_CONTAINER) {
					addJARContainer= true;
					projectPackageFragmentRoots.add(root);
				} 
			}
			
			if (addJARContainer) {
				projectPackageFragmentRoots.add(new LibraryContainer(project));
			}
			
			// separate loop to make sure all containers are on the classpath
			IClasspathEntry[] rawClasspath = new IClasspathEntry[0];
			try {
				rawClasspath = project.getRawClasspath();
			}
			catch (JavaModelException e) {}
			for (int i= 0; i < rawClasspath.length; i++) {
				IClasspathEntry classpathEntry= rawClasspath[i];
				if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
					projectPackageFragmentRoots.add(new JsGlobalScopeContainer(project, classpathEntry));
				}	
			}	
		 return projectPackageFragmentRoots.toArray();
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
