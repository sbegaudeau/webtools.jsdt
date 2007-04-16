package org.eclipse.wst.jsdt.core.compiler.libraries;

import java.io.File;

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




public class BasicBrowserLibraryClassPathContainerInitializer extends ClasspathContainerInitializer {

	private static final String ContainerDescription = "Base Browser Support Library";
	private static final char[] LIBRARY_FILE_NAME = {'b','a','s','e','B','r','o','w','s','e','r','L','i','b','r','a','r','y','.','j','s'};
	private static final String CONTAINER_ID="org.eclipse.wst.jsdt.launching.baseBrowserLibrary";
	public String getDescription(IPath containerPath, IJavaProject project) {
		return ContainerDescription;
	}
	
	class simpleClassPathContainer implements IClasspathContainer{
		
		
		
		simpleClassPathContainer() {}

		public IClasspathEntry[] getClasspathEntries() {
			IClasspathEntry library = JavaCore.newLibraryEntry((new BasicLibLocation()).getWorkingLibPath().makeAbsolute(), null, null, new IAccessRule[0], new IClasspathAttribute[0], true);
			
			return new IClasspathEntry[] {library};
		}

		public String getDescription() {
			
			return ContainerDescription;
		}

		public int getKind() {	
			return IClasspathContainer.K_APPLICATION;
		}

		public IPath getPath() {
			return new Path(CONTAINER_ID);
		}
		
	}
	
	class BasicLibLocation extends SystemLibraryLocation{
		BasicLibLocation(){
			super();
		}
		
		public char[] getLibraryFileName() {
			return LIBRARY_FILE_NAME;
		}
		
	}
	
	
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {		
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {project}, new IClasspathContainer[] {getContainer(containerPath,project)}, null);
	}
	
	protected IClasspathContainer getContainer(IPath containerPath, IJavaProject project) {
		return new simpleClassPathContainer();
	}
}
