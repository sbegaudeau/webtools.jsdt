package org.eclipse.wst.jsdt.core.compiler.libraries;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.IAccessRule;
import org.eclipse.wst.jsdt.core.IClasspathAttribute;
import org.eclipse.wst.jsdt.core.IClasspathContainer;
import org.eclipse.wst.jsdt.core.IClasspathEntry;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.JavaCore;
import org.eclipse.wst.jsdt.core.compiler.libraries.BasicBrowserLibraryClassPathContainerInitializer.BasicLibLocation;
import org.eclipse.wst.jsdt.core.compiler.libraries.BasicBrowserLibraryClassPathContainerInitializer.simpleClassPathContainer;

public class FireFoxLibInitializer extends BasicBrowserLibraryClassPathContainerInitializer {

	private static final String ContainerDescription = "FireFox Browser Support Library";
	private static final char[][] LIBRARY_FILE_NAMES = {{'F','i','r','e','F','o','x','2','.','0','.','0','.','3','.','j','s'}};
	private static final String CONTAINER_ID="org.eclipse.wst.jsdt.launching.FireFoxBrowserLibrary";
	private static final String PLUGIN_ID="org.eclipse.wst.jsdt.support.firefox";
	
	public String getDescription(IPath containerPath, IJavaProject project) {
		return ContainerDescription;
	}
	
	class FireFoxLib implements IClasspathContainer{
	
		FireFoxLib() {}

		public IClasspathEntry[] getClasspathEntries() {
			IClasspathEntry library = JavaCore.newLibraryEntry((new FireFoxLibLocation()).getWorkingLibPath().makeAbsolute(), null, null, new IAccessRule[0], new IClasspathAttribute[0], true);
			
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
	
	class FireFoxLibLocation extends SystemLibraryLocation{
		FireFoxLibLocation(){
			super();
		}
		
		public char[] getLibraryFileName() {
			return LIBRARY_FILE_NAMES[0];
		}
		
		protected String getPluginId() {
			return PLUGIN_ID;
		}
		
		
	}
	
	
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {		
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {project}, new IClasspathContainer[] {getContainer(containerPath,project)}, null);
	}
	
	protected IClasspathContainer getContainer(IPath containerPath, IJavaProject project) {
		return new FireFoxLib();
	}
}
