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





	public static JsGlobalScopeContainerInitializer getContainerInitializer(IPath classPathEntry) {
		if(classPathEntry==null ) return null;
		JsGlobalScopeContainerInitializer initializer= JavaCore.getJsGlobalScopeContainerInitializer(classPathEntry.segment(0));
		return initializer ;
	}

	public IClasspathEntry[] getClasspathEntries(IJsGlobalScopeContainer container) {


		if(container!=null) return	container.getClasspathEntries();

		return new IClasspathEntry[0];
	}

	public  IJsGlobalScopeContainer getLibraryContainer(IPath cpEntry, IJavaProject javaProject) {
		IJsGlobalScopeContainer container=null;
		try {
			container = JavaCore.getJsGlobalScopeContainer(cpEntry, javaProject);
		} catch (JavaModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return	container;
	}

	public static JsGlobalScopeContainerInitializer findLibraryInitializer(IPath compUnitPath, IJavaProject javaProject) {
		IPackageFragmentRoot[] roots = new IPackageFragmentRoot[0];
		try {
			roots = javaProject.getAllPackageFragmentRoots();
		} catch (JavaModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		for (int i = 0;i<roots.length;i++) {
			IPackageFragment frag = roots[i].getPackageFragment(""); //$NON-NLS-1$

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
