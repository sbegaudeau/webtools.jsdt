package org.eclipse.wst.jsdt.core.compiler.libraries;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.ClasspathContainerInitializer;
import org.eclipse.wst.jsdt.core.IAccessRule;
import org.eclipse.wst.jsdt.core.IClasspathAttribute;
import org.eclipse.wst.jsdt.core.IClasspathContainer;
import org.eclipse.wst.jsdt.core.IClasspathEntry;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.JavaCore;

public class BasicBrowserLibraryClassPathContainerInitializer extends ClasspathContainerInitializer implements IClasspathContainer {
	private static final String CONTAINER_ID = "org.eclipse.wst.jsdt.launching.baseBrowserLibrary";
	private static final String ContainerDescription = "ECMA 3 Browser Support";
	private static final String FILE_DESCRIPTION0 = "ECMA 3 Compatible DOM";
	private static final String FILE_DESCRIPTION1 = "Typical Web Browser Window";
	private static final char[][] LIBRARY_FILE_NAME = {
														{ 'b', 'a', 's', 'e', 'B', 'r', 'o', 'w', 's', 'e', 'r', 'L', 'i', 'b', 'r', 'a', 'r', 'y', '.', 'j', 's' },
														{'b','r','o','w','s','e','r','W','i','n','d','o','w','.','j','s'}
													  };
	private static final String LibraryDescription = "ECMA 3 Browser Support Library";
	
	class BasicLibLocation extends SystemLibraryLocation {
		BasicLibLocation() {
			super();
		}
		
		public char[][] getLibraryFileNames() {
			return  BasicBrowserLibraryClassPathContainerInitializer.LIBRARY_FILE_NAME ;
		}
	}
	
	public SystemLibraryLocation getLibraryLocation() {
		return new BasicLibLocation();
	}
	
	public IClasspathEntry[] getClasspathEntries() {
		SystemLibraryLocation libLocation =  getLibraryLocation();
		char[][] filesInLibs = libLocation.getLibraryFileNames();
		IClasspathEntry[] entries = new IClasspathEntry[filesInLibs.length];
		for (int i = 0; i < entries.length; i++) {
			IPath workingLibPath = new Path(libLocation.getLibraryPath(filesInLibs[i]));
			entries[i] = JavaCore.newLibraryEntry(workingLibPath.makeAbsolute(), null, null, new IAccessRule[0], new IClasspathAttribute[0], true);
		}
		return entries;
	}
	
	protected IClasspathContainer getContainer(IPath containerPath, IJavaProject project) {
		return this;
	}
	
	public String getDescription() {
		return BasicBrowserLibraryClassPathContainerInitializer.LibraryDescription;
	}
	
	public String getDescription(IPath containerPath, IJavaProject project) {
		if (containerPath.equals(new Path(new String(BasicBrowserLibraryClassPathContainerInitializer.LIBRARY_FILE_NAME[0])))) {
			return BasicBrowserLibraryClassPathContainerInitializer.FILE_DESCRIPTION0;
		}else if (containerPath.equals(new Path(new String(BasicBrowserLibraryClassPathContainerInitializer.LIBRARY_FILE_NAME[1])))) {
			return BasicBrowserLibraryClassPathContainerInitializer.FILE_DESCRIPTION1;
		}
		return BasicBrowserLibraryClassPathContainerInitializer.ContainerDescription;
	}
	
	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}
	
	public IPath getPath() {
		return new Path(BasicBrowserLibraryClassPathContainerInitializer.CONTAINER_ID);
	}
	
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, new IClasspathContainer[] { getContainer(containerPath, project) }, null);
	}
}
