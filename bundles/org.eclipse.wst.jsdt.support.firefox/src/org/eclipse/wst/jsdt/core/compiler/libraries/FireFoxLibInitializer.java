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

public class FireFoxLibInitializer extends BasicBrowserLibraryClassPathContainerInitializer {
	private static final String CONTAINER_ID = "org.eclipse.wst.jsdt.launching.FireFoxBrowserLibrary";
	private static final String ContainerDescription = "FireFox Browser Support Library";
	private static final char[][] LIBRARY_FILE_NAMES = { { 'F', 'i', 'r', 'e', 'F', 'o', 'x', '2', '.', '0', '.', '0', '.', '3', '.', 'j', 's' } };
	private static final String PLUGIN_ID = "org.eclipse.wst.jsdt.support.firefox";
	
	
	class FireFoxLibLocation extends SystemLibraryLocation {
		FireFoxLibLocation() {
			super();
		}
		
		@Override
		public char[][] getLibraryFileNames() {
			return new char[][] { FireFoxLibInitializer.LIBRARY_FILE_NAMES[0] };
		}
		
		@Override
		protected String getPluginId() {
			return FireFoxLibInitializer.PLUGIN_ID;
		}
	}
	
	public SystemLibraryLocation getLibraryLocation() {
		return new FireFoxLibLocation();
	}

	@Override
	public String getDescription(IPath containerPath, IJavaProject project) {
		return FireFoxLibInitializer.ContainerDescription;
	}
	
	public String getDescription() {
		return FireFoxLibInitializer.ContainerDescription;
	}
}
