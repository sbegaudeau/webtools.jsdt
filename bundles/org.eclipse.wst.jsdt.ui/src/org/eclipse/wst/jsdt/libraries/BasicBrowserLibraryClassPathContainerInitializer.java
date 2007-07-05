package org.eclipse.wst.jsdt.libraries;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wst.jsdt.core.ClasspathContainerInitializer;
import org.eclipse.wst.jsdt.core.IAccessRule;
import org.eclipse.wst.jsdt.core.IClasspathAttribute;
import org.eclipse.wst.jsdt.core.IClasspathContainer;
import org.eclipse.wst.jsdt.core.IClasspathEntry;
import org.eclipse.wst.jsdt.core.IJavaProject;
import org.eclipse.wst.jsdt.core.JavaCore;
import org.eclipse.wst.jsdt.core.LibrarySuperType;
import org.eclipse.wst.jsdt.core.compiler.libraries.LibraryLocation;
import org.eclipse.wst.jsdt.core.compiler.libraries.SystemLibraryLocation;
import org.eclipse.wst.jsdt.internal.ui.IClasspathContainerInitialzerExtension;

public class BasicBrowserLibraryClassPathContainerInitializer extends ClasspathContainerInitializer implements IClasspathContainer, IClasspathContainerInitialzerExtension {
	private static final String CONTAINER_ID = "org.eclipse.wst.jsdt.launching.baseBrowserLibrary";
	private static final String ContainerDescription = "ECMA 3 Browser Support";
	private static final String FILE_DESCRIPTION0 = "ECMA 3 DOM";
	private static final String FILE_DESCRIPTION1 = "Common Web Browser";
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
	
	public LibraryLocation getLibraryLocation() {
		return new BasicLibLocation();
	}
	
	public IClasspathEntry[] getClasspathEntries() {
		LibraryLocation libLocation =  getLibraryLocation();
		char[][] filesInLibs = libLocation.getLibraryFileNames();
		IClasspathEntry[] entries = new IClasspathEntry[filesInLibs.length];
		for (int i = 0; i < entries.length; i++) {
			IPath workingLibPath = new Path(libLocation.getLibraryPath(filesInLibs[i]));
			entries[i] = JavaCore.newLibraryEntry(workingLibPath.makeAbsolute(), null, null, new IAccessRule[0], new IClasspathAttribute[0], true);
		}
		return entries;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.ClasspathContainerInitializer#canUpdateClasspathContainer(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaProject)
	 */
	public boolean canUpdateClasspathContainer(IPath containerPath, IJavaProject project) {
		return true;
		
		
	}

	protected IClasspathContainer getContainer(IPath containerPath, IJavaProject project) {
		return this;
	}
	
	public String getDescription() {
		return BasicBrowserLibraryClassPathContainerInitializer.LibraryDescription;
	}
	
	public String getDescription(IPath containerPath, IJavaProject project) {
		
		if(containerPath==null) return null;
		
		IPath p1 = new Path(new String(BasicBrowserLibraryClassPathContainerInitializer.LIBRARY_FILE_NAME[0]));
		IPath p2 = new Path(new String(BasicBrowserLibraryClassPathContainerInitializer.LIBRARY_FILE_NAME[1]));
		IPath requestedContainerPath = new Path(containerPath.lastSegment());
		if (requestedContainerPath.equals(p1)) {
			return BasicBrowserLibraryClassPathContainerInitializer.FILE_DESCRIPTION0;
		}else if (requestedContainerPath.equals(p2)) {
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

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.ClasspathContainerInitializer#containerSuperTypes()
	 */
	public String[] containerSuperTypes() {
		return new String[] {"Window","Document"};
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.IClasspathContainerInitialzerExtension#getImage(org.eclipse.core.runtime.IPath, java.lang.String, org.eclipse.wst.jsdt.core.IJavaProject)
	 */
	public ImageDescriptor getImage(IPath containerPath, String element, IJavaProject project) {
		if(containerPath==null ) {
			return null;
		}
		/* Dont use the rino image for the individual files */
	//	IPath libFileName1 = new Path(new String(LIBRARY_FILE_NAME[0]));
	//	IPath libFileName2 = new Path(new String(LIBRARY_FILE_NAME[1]));
		
		String requestedContainerPath = new Path(element).lastSegment();
		if (element!=null && requestedContainerPath.equals(new String(LIBRARY_FILE_NAME[0]))) {
			return null;
		}else if (element!=null && requestedContainerPath.equals(new String(LIBRARY_FILE_NAME[1]))) {
			return null;
		}
		
		return ImageDescriptor.createFromFile(this.getClass(),"rino.jpg");
	//	System.out.println("Unimplemented method:BasicBrowserLibraryClassPathContainerInitializer.getImage");
		//return null;
	}
	
}
