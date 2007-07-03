/**
 * 
 */
package org.eclipse.wst.jsdt.core;

import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;


/**
 * @author childsb
 * 
 */
public class LibrarySuperType {
	IPath cpEntry;
	String superTypeName;
	String libraryName;
	IJavaProject javaProject;
	
	public static final String SUPER_TYPE_CONTAINER= "org.eclipse.wst.jsdt.ui.superType.container";
	public static final String SUPER_TYPE_NAME= "org.eclipse.wst.jsdt.ui.superType.name";
	
	/* Only one superTypeName per instance so enforce that */
	public LibrarySuperType(IPath classPathEntry, IJavaProject project, String superTypeName) {
		this.cpEntry = classPathEntry;
		this.superTypeName = superTypeName;
		this.javaProject =  project;
		this.libraryName = initLibraryName();
		
	}
	
	public LibrarySuperType(String classPathEntry, IJavaProject project, String superTypeName) {
		this(new Path(classPathEntry),project,superTypeName);
	}
	/* Construct parent */
	public LibrarySuperType(IPath classPathEntry,  IJavaProject project) {
		this(classPathEntry,project, null);
	}
	
	public IPath getRawContainerPath() {
		return cpEntry;
	}
	
	public boolean hasChildren() {
		/* defined super type meeans I'm a child */
		if(superTypeName!=null) return false;
		ClasspathContainerInitializer init = getContainerInitializer();
		if (init == null) return false;
		String[] availableSuperTypes = init.containerSuperTypes();
		return availableSuperTypes!=null && availableSuperTypes.length>0;
	}
	
	public LibrarySuperType[] getChildren() {
		if(superTypeName!=null) return new LibrarySuperType[0];
		return getFlatLibrarySuperTypes(cpEntry,javaProject);
	}
	
	public LibrarySuperType getParent() {
		if(superTypeName==null) return null;
		return new LibrarySuperType(cpEntry,javaProject, null);
	}
	
	public boolean isParent() {
		return getParent()==null;
	}
	
	public ClasspathContainerInitializer getContainerInitializer() {
		return getContainerInitializer(cpEntry);
	}
	
	public IClasspathEntry[] getClasspathEntries() {
		IClasspathContainer container=null;
		try {
			container = JavaCore.getClasspathContainer(this.cpEntry, this.javaProject);
		} catch (JavaModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		if(container!=null) return	container.getClasspathEntries();
		
		return new IClasspathEntry[0];
	}
	
	private static LibrarySuperType[] getFlatLibrarySuperTypes(IPath classPathEntry, IJavaProject javaProject) {
		ClasspathContainerInitializer init = getContainerInitializer(classPathEntry);
		if (init == null) return new LibrarySuperType[0];
		String[] availableSuperTypes = init.containerSuperTypes();
		LibrarySuperType[] libSupers = new LibrarySuperType[availableSuperTypes.length];
		for (int i = 0; i < availableSuperTypes.length; i++) {
			libSupers[i] = new LibrarySuperType(classPathEntry, javaProject, availableSuperTypes[i]);
		}
		return libSupers;
	}
	
	public String getSuperTypeName() {
		return superTypeName;
	}
	
	public String getLibraryName() {
		return libraryName;
	}
	
	private String initLibraryName() {
		ClasspathContainerInitializer init = getContainerInitializer();
		
		/* parent node */
		if(superTypeName==null) {
			if(init==null) {
				return cpEntry.toString();
			}
			return  init.getDescription(cpEntry, javaProject);
		}
		Object parent = getParent();
		if(!(parent instanceof LibrarySuperType)) return null;
		return ((LibrarySuperType)parent).getLibraryName();
	}

	public String toString() {
		//ClasspathContainerInitializer init = getContainerInitializer();
		
		/* parent node */
		if(isParent()) {
			return getLibraryName();
			
		}
		
		return superTypeName + "() in " + getLibraryName();
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof LibrarySuperType)) return false;
		
		LibrarySuperType other = (LibrarySuperType)o;
		
		
		
		if(other.cpEntry!=null && !other.cpEntry.equals(cpEntry)) {
			return false;
		}
			
		if((other.superTypeName==superTypeName)) {
			return true;
		}
		
		if(other.superTypeName!=null && superTypeName!=null) {
			return other.superTypeName.equals(superTypeName);
		}
		
		return false;
	}

	public IPackageFragment[] getPackageFragments(){
		IClasspathEntry[] entries = getClasspathEntries();
		IPackageFragment[] frags;
		ArrayList allFrags = new ArrayList();
		
		try {
			for(int i = 0;i<entries.length;i++) {
				IPath path = entries[i].getPath();
				IPackageFragmentRoot root = javaProject.findPackageFragmentRoot(path.makeAbsolute());
				
				IJavaElement[] children = root.getChildren();
				for(int k = 0;k<children.length;k++) {
					if(children[k] instanceof IPackageFragment) {
						allFrags.add(children[k]);
					}
				}
			}
		} catch (JavaModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return (IPackageFragment[])allFrags.toArray(new IPackageFragment[allFrags.size()]);
	}
	
	public static ClasspathContainerInitializer getContainerInitializer(IPath classPathEntry) {
		if(classPathEntry==null ) return null;
		ClasspathContainerInitializer initializer= JavaCore.getClasspathContainerInitializer(classPathEntry.segment(0));
		return initializer ;
	}
}
