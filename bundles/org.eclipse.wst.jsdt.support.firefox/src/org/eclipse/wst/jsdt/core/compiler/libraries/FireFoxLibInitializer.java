package org.eclipse.wst.jsdt.core.compiler.libraries;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.jsdt.core.JsGlobalScopeContainerInitializer;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.internal.ui.IJsGlobalScopeContainerInitialzerExtension;


public class FireFoxLibInitializer extends JsGlobalScopeContainerInitializer implements IJsGlobalScopeContainerInitialzerExtension {
	private static final String CONTAINER_ID = "org.eclipse.wst.jsdt.launching.FireFoxBrowserLibrary";
	private static final String ContainerDescription = "FireFox Browser Support Library";
	private static final char[][] LIBRARY_FILE_NAMES = {{'F', 'i', 'r', 'e', 'F', 'o', 'x', '2', '.', '0', '.', '0', '.', '3', '.', 'j', 's'}};
	private static final String PLUGIN_ID = "org.eclipse.wst.jsdt.support.firefox";


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


	public String getDescription(IPath containerPath, IJavaProject project) {
		return FireFoxLibInitializer.ContainerDescription;
	}

	public String getDescription() {
		return FireFoxLibInitializer.ContainerDescription;
	}

	public ImageDescriptor getImage(IPath containerPath, String element, IJavaProject project) {

		if (containerPath == null) {
			return null;
		}
		/* Dont use the rino image for the individual files */
		String requestedContainerPath = new Path(element).lastSegment();
		if ((element != null) && requestedContainerPath.equals(new String(LIBRARY_FILE_NAMES[0]))) {
			return null;
		}

		return ImageDescriptor.createFromFile(this.getClass(), "FireFoxSmall.gif");
		// System.out.println("Unimplemented
		// method:BasicBrowserLibraryJsGlobalScopeContainerInitializer.getImage");
		// return null;
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


	public boolean canUpdateJsGlobalScopeContainer(IPath containerPath, IJavaProject project) {
		return true;


	}

	public String[] containerSuperTypes() {
		return new String[]{"window"};
	}

}
