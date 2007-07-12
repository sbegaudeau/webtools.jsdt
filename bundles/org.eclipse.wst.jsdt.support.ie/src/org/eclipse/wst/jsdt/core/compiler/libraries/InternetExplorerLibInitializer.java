package org.eclipse.wst.jsdt.core.compiler.libraries;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.jsdt.core.ClasspathContainerInitializer;
import org.eclipse.wst.jsdt.core.IAccessRule;
import org.eclipse.wst.jsdt.core.IClasspathAttribute;
import org.eclipse.wst.jsdt.core.IClasspathContainer;
import org.eclipse.wst.jsdt.core.IClasspathEntry;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.JavaCore;
import org.eclipse.wst.jsdt.core.LibrarySuperType;
import org.eclipse.wst.jsdt.internal.ui.IClasspathContainerInitialzerExtension;


public class InternetExplorerLibInitializer extends ClasspathContainerInitializer implements IClasspathContainerInitialzerExtension {
	private static final String CONTAINER_ID = "org.eclipse.wst.jsdt.launching.InternetExplorer";
	private static final String ContainerDescription = "Internet Explorer Support Library";
	//private static final char[][] LIBRARY_FILE_NAMES = { { 'I','n','t','e','r','n','e','t','E','x','p','l','o','r','e','r','.','j','s' } };
	private static final String PLUGIN_ID = "org.eclipse.wst.jsdt.support.ie";
	
	
	class IeLibLocation extends SystemLibraryLocation {
		IeLibLocation() {
			super();
		}
		
		
		public char[][] getLibraryFileNames() {
			return getAllFilesInPluginDirectory(getLibraryPathInPlugin().toString());
		}
		
		
		protected String getPluginId() {
			return InternetExplorerLibInitializer.PLUGIN_ID;
		}
	}
	
	public LibraryLocation getLibraryLocation() {
		return new IeLibLocation();
	}

	
	public String getDescription(IPath containerPath, IJavaProject project) {
		return InternetExplorerLibInitializer.ContainerDescription;
	}
	
	public String getDescription() {
		return InternetExplorerLibInitializer.ContainerDescription;
	}
	
	public ImageDescriptor getImage(IPath containerPath, String element, IJavaProject project) {
		
		if(containerPath==null) return null;
		/* Dont use the rino image for the individual files */
		String requestedContainerPath = new Path(element).getFileExtension();
		if(requestedContainerPath!=null && requestedContainerPath.equalsIgnoreCase("js")) return null;
		
		
//		char[][] allLibFiles = ( new IeLibLocation()).getLibraryFileNames();
//		for(int i = 0;i<allLibFiles.length;i++) {
//			String libName = new String(allLibFiles[i]);
//			if (element!=null && requestedContainerPath.equals(libName)) {
//				return null;
//			}
//		}
		return ImageDescriptor.createFromFile(this.getClass(),"ie_small.gif");
		//return ImageDescriptor.createFromFile(this.getClass(),"ie_small.gif");
	//	System.out.println("Unimplemented method:BasicBrowserLibraryClassPathContainerInitializer.getImage");
		//return null;
	}
	public IPath getPath() {
		return new Path(InternetExplorerLibInitializer.CONTAINER_ID);
	}


	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.IClasspathContainer#getKind()
	 */
	public int getKind() {
		
	
		return  IClasspathContainer.K_APPLICATION;
	}


	public boolean canUpdateClasspathContainer(IPath containerPath, IJavaProject project) {
		return true;
		
		
	}


	public String[] containerSuperTypes() {
		return new String[] {"window", "object", "array"};
	}
	
	
}
