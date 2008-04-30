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
		JsGlobalScopeContainerInitializer initializer= JavaScriptCore.getJsGlobalScopeContainerInitializer(classPathEntry.segment(0));
		return initializer ;
	}

	public IIncludePathEntry[] getIncludepathEntries(IJsGlobalScopeContainer container) {


		if(container!=null) return	container.getIncludepathEntries();

		return new IIncludePathEntry[0];
	}

	public  IJsGlobalScopeContainer getLibraryContainer(IPath cpEntry, IJavaScriptProject javaProject) {
		IJsGlobalScopeContainer container=null;
		try {
			container = JavaScriptCore.getJsGlobalScopeContainer(cpEntry, javaProject);
		} catch (JavaScriptModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return	container;
	}

	public static JsGlobalScopeContainerInitializer findLibraryInitializer(IPath compUnitPath, IJavaScriptProject javaProject) {
		IPackageFragmentRoot[] roots = new IPackageFragmentRoot[0];
		try {
			roots = javaProject.getAllPackageFragmentRoots();
		} catch (JavaScriptModelException ex) {
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
