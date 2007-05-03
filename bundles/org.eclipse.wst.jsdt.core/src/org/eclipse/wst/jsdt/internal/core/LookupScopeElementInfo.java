package org.eclipse.wst.jsdt.internal.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.core.ICompilationUnit;
import org.eclipse.wst.jsdt.core.IJavaElement;
import org.eclipse.wst.jsdt.core.IPackageFragmentRoot;
import org.eclipse.wst.jsdt.core.JavaModelException;
import org.eclipse.wst.jsdt.internal.core.JavaProjectElementInfo.LookupCache;
import org.eclipse.wst.jsdt.internal.core.util.HashtableOfArrayToObject;

public class LookupScopeElementInfo extends PackageFragmentRootInfo {
	
	private JavaProject javaProject;
	private IPackageFragmentRoot[] rootsInScope;
	private LookupCache cache;
	/* system libraries always go first if present in project*/
	public static final String[] SYSTEM_LIBRARIES = {"system.js"};
	
	static class LookupCache {
		LookupCache(IPackageFragmentRoot[] allPkgFragmentRootsCache, HashtableOfArrayToObject allPkgFragmentsCache, HashtableOfArrayToObject isPackageCache, Map rootToResolvedEntries) {
			this.allPkgFragmentRootsCache = allPkgFragmentRootsCache;
			this.allPkgFragmentsCache = allPkgFragmentsCache;
			this.isPackageCache = isPackageCache;
			this.rootToResolvedEntries = rootToResolvedEntries;
		}
		
		/*
		 * A cache of all package fragment roots of this project.
		 */
		public IPackageFragmentRoot[] allPkgFragmentRootsCache;
		
		/*
		 * A cache of all package fragments in this project.
		 * (a map from String[] (the package name) to IPackageFragmentRoot[] (the package fragment roots that contain a package fragment with this name)
		 */
		public HashtableOfArrayToObject allPkgFragmentsCache;
		
		/*
		 * A set of package names (String[]) that are known to be packages.
		 */
		public HashtableOfArrayToObject isPackageCache;
	
		public Map rootToResolvedEntries;		
	}
	
	public String[] getRawImportsFromCache() {
		
		String[] rawImports = null;
		
		for(int i = 0;i<cache.allPkgFragmentRootsCache.length;i++) {
			if(cache.allPkgFragmentRootsCache[i] instanceof DocumentContextFragmentRoot) {
				return ((DocumentContextFragmentRoot)cache.allPkgFragmentRootsCache[i]).getRawImports();
			}
		}
		return new String[0];
	}
	
	public IPackageFragmentRoot[] getAllRoots() {
		IPackageFragmentRoot[] projectRoots;
		try {
			projectRoots = javaProject.getPackageFragmentRoots();
		} catch (JavaModelException ex) {
			projectRoots = new IPackageFragmentRoot[0];
		}
		
		ArrayList allRoots = new ArrayList();
		//IPackageFragmentRoot[] allRoots = new IPackageFragmentRoot[rootsInScope.length + projectRoots.length ];
		for(int i = 0;i<SYSTEM_LIBRARIES.length;i++) {
			for(int k=0;k<projectRoots.length;k++) {
				if(projectRoots[k].getElementName().equalsIgnoreCase(SYSTEM_LIBRARIES[i]));
						allRoots.add(projectRoots[k]);
			}
		}
		
		for(int i=0;i<projectRoots.length;i++) {
			if(!allRoots.contains(projectRoots[i])) allRoots.add(projectRoots[i]);
		}
		
		for(int i=0;i<rootsInScope.length;i++) {
			if(!allRoots.contains(rootsInScope[i])) allRoots.add(rootsInScope[i]);
		}
		
		for(int i=0;i<projectRoots.length;i++) {
			if(!allRoots.contains(projectRoots[i])) allRoots.add(projectRoots[i]);
		}
		
		return (IPackageFragmentRoot[])allRoots.toArray(new IPackageFragmentRoot[allRoots.size()]);
	}
	
	public LookupScopeElementInfo(JavaProject project,IPackageFragmentRoot[] rootsInScope){
		this.javaProject = project;
		this.rootsInScope = rootsInScope;
		

	}
	
	NameLookup newNameLookup(ICompilationUnit[] workingCopies) {
		BuildLookupScopeCache(getAllRoots());

		return new NameLookup(cache.allPkgFragmentRootsCache, cache.allPkgFragmentsCache, cache.isPackageCache, workingCopies, cache.rootToResolvedEntries);
	}
	
	public LookupCache BuildLookupScopeCache(IPackageFragmentRoot[] rootsInScope) {
		
		
		Map reverseMap = new HashMap(3);
		HashtableOfArrayToObject fragmentsCache = new HashtableOfArrayToObject();
		HashtableOfArrayToObject isPackageCache = new HashtableOfArrayToObject();
			for (int i = 0, length = rootsInScope.length; i < length; i++) {
				IPackageFragmentRoot root = rootsInScope[i];
				IJavaElement[] frags = null;
				try {
					if(root instanceof DocumentContextFragmentRoot) {
						LibraryFragmentRootInfo info = new LibraryFragmentRootInfo();
						((DocumentContextFragmentRoot) root).computeChildren(info, new HashMap());
						frags = info.children;
					}else if (root instanceof LibraryFragmentRoot) {
						LibraryFragmentRootInfo info = new LibraryFragmentRootInfo();
						((LibraryFragmentRoot) root).computeChildren(info, new HashMap());
						frags = info.children;
					} else if (root.isArchive() && !root.isOpen()) {
						JarPackageFragmentRootInfo info = new JarPackageFragmentRootInfo();
						((JarPackageFragmentRoot) root).computeChildren(info, new HashMap());
						frags = info.children;
					} else if (root instanceof PackageFragmentRoot) {
						PackageFragmentRootInfo info = new PackageFragmentRootInfo();
						((PackageFragmentRoot) root).computeChildren(info, new HashMap());
						frags = info.children;
					}else
						frags = root.getChildren();
				} catch (JavaModelException e) {
					// root doesn't exist: ignore
					continue;
				}
				for (int j = 0, length2 = frags.length; j < length2; j++) {
					PackageFragment fragment= (PackageFragment) frags[j];
					/* Keep folders off the classpath */
					//if(fragment.getPath().getFileExtension()==null || !fragment.getPath().getFileExtension().equals(".js")) continue;
					String[] pkgName = fragment.names;
					Object existing = fragmentsCache.get(pkgName);
					if (existing == null) {
						fragmentsCache.put(pkgName, root);
						// cache whether each package and its including packages (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=119161)
						// are actual packages
						addNames(pkgName, isPackageCache);
					} else {
						if (existing instanceof PackageFragmentRoot) {
							fragmentsCache.put(pkgName, new IPackageFragmentRoot[] {(PackageFragmentRoot) existing, root});
						} else {
							IPackageFragmentRoot[] entry= (IPackageFragmentRoot[]) existing;
							IPackageFragmentRoot[] copy= new IPackageFragmentRoot[entry.length + 1];
							System.arraycopy(entry, 0, copy, 0, entry.length);
							copy[entry.length]= root;
							fragmentsCache.put(pkgName, copy);
						}
					}
				}
			}
			cache = new LookupCache(rootsInScope, fragmentsCache, isPackageCache, reverseMap);
		
		
		return cache;
	}
	
	public static void addNames(String[] name, HashtableOfArrayToObject set) {
		set.put(name, name);
		int length = name.length;
		for (int i = length-1; i > 0; i--) {
			String[] superName = new String[i];
			System.arraycopy(name, 0, superName, 0, i);
			set.put(superName, superName);
		}
	}

}
