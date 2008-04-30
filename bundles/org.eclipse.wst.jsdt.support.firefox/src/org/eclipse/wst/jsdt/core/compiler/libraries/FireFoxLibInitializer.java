package org.eclipse.wst.jsdt.core.compiler.libraries;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainerInitializer;
import org.eclipse.wst.jsdt.core.JsGlobalScopeContainerInitializer;


public class FireFoxLibInitializer extends JsGlobalScopeContainerInitializer implements IJsGlobalScopeContainerInitializer {
	protected static final String CONTAINER_ID = "org.eclipse.wst.jsdt.launching.FireFoxBrowserLibrary";
	protected static final String ContainerDescription = "FireFox Browser Support Library";
	protected static final char[][] LIBRARY_FILE_NAMES = {{'F', 'i', 'r', 'e', 'F', 'o', 'x', '2', '.', '0', '.', '0', '.', '3', '.', 'j', 's'}};
	protected static final String PLUGIN_ID = "org.eclipse.wst.jsdt.support.firefox";


	class FireFoxLibLocation extends SystemLibraryLocation {
		FireFoxLibLocation() {
			super();
		}


		public char[][] getLibraryFileNames() {
			return new char[][]{FireFoxLibInitializer.LIBRARY_FILE_NAMES[0]};
		}


		protected String getPluginId() {
			return FireFoxLibInitializer.PLUGIN_ID;
		}
	}

	public LibraryLocation getLibraryLocation() {
		return new FireFoxLibLocation();
	}


	public String getDescription(IPath containerPath, IJavaScriptProject project) {
		return FireFoxLibInitializer.ContainerDescription;
	}

	public String getDescription() {
		return FireFoxLibInitializer.ContainerDescription;
	}

	
	public IPath getPath() {
		return new Path(FireFoxLibInitializer.CONTAINER_ID);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer#getKind()
	 */
	public int getKind() {


		return IJsGlobalScopeContainer.K_SYSTEM;
	}


	public boolean canUpdateJsGlobalScopeContainer(IPath containerPath, IJavaScriptProject project) {
		return true;


	}

	public String[] containerSuperTypes() {
		return new String[]{"window"};
	}

}
