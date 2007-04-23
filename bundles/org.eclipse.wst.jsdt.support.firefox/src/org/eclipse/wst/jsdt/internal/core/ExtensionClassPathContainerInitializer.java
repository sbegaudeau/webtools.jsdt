package org.eclipse.wst.jsdt.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.jsdt.core.ClasspathContainerInitializer;
import org.eclipse.wst.jsdt.core.IAccessRule;
import org.eclipse.wst.jsdt.core.IClasspathAttribute;
import org.eclipse.wst.jsdt.core.IClasspathContainer;
import org.eclipse.wst.jsdt.core.IClasspathEntry;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.JavaCore;




public class ExtensionClassPathContainerInitializer extends ClasspathContainerInitializer {

	private static final String ContainerDescription = "Base Browser Support Library";
	
	public String getDescription(IPath containerPath, IJavaProject project) {
		return ContainerDescription;
	}
	
	class simpleClassPathContainer implements IClasspathContainer{
		
		IPath containerPath;
		
		simpleClassPathContainer(IPath containerPath) {
			this.containerPath=(containerPath.removeFirstSegments(1)).makeAbsolute();
		}

		public IClasspathEntry[] getClasspathEntries() {
			IClasspathEntry library = JavaCore.newLibraryEntry(getPath(), null, null, new IAccessRule[0], new IClasspathAttribute[0], true);
			
			return new IClasspathEntry[] {library};
		}

		public String getDescription() {
			
			return ContainerDescription;
		}

		public int getKind() {	
			return IClasspathContainer.K_APPLICATION;
		}

		public IPath getPath() {
			return containerPath;
		}
		
	}
	
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {		
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {project}, new IClasspathContainer[] {getContainer(containerPath,project)}, null);
	}
	
	protected IClasspathContainer getContainer(IPath containerPath, IJavaProject project) {
		return new simpleClassPathContainer(containerPath);
	}
}
