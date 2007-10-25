/**
 *
 */
package org.eclipse.wst.jsdt.core;

import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.jsdt.internal.core.ClassFile;

/**
 * @author childsb
 *
 */

/* (mostly) static methods to figure out classpath entries and container initializers *
 *
 */
public class JSDScopeUtil {





	public static ClasspathContainerInitializer getContainerInitializer(IPath classPathEntry) {
		if(classPathEntry==null ) return null;
		ClasspathContainerInitializer initializer= JavaCore.getClasspathContainerInitializer(classPathEntry.segment(0));
		return initializer ;
	}

	public IClasspathEntry[] getClasspathEntries(IClasspathContainer container) {


		if(container!=null) return	container.getClasspathEntries();

		return new IClasspathEntry[0];
	}

	public  IClasspathContainer getLibraryContainer(IPath cpEntry, IJavaProject javaProject) {
		IClasspathContainer container=null;
		try {
			container = JavaCore.getClasspathContainer(cpEntry, javaProject);
		} catch (JavaModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return	container;
	}

	public static ClasspathContainerInitializer findLibraryInitializer(IPath compUnitPath, IJavaProject javaProject) {
		IJavaElement element=null;
		IPackageFragmentRoot[] roots = new IPackageFragmentRoot[0];
		try {
			roots = javaProject.getAllPackageFragmentRoots();
		} catch (JavaModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		for (int i = 0;i<roots.length;i++) {
			IPackageFragment frag = roots[i].getPackageFragment("");

			try {
				IClassFile classfile = frag.getClassFile(compUnitPath.toString());
				if(classfile.exists()) {
					return ((ClassFile)classfile).getContainerInitializer();
				}
			} catch (Exception ex) {
				// Do nothing since CU may be invalid and thats what w're tryingto figure out.
				// TODO Auto-generated catch block
			//	ex.printStackTrace();
			}


		}

		return null;
	}
}
