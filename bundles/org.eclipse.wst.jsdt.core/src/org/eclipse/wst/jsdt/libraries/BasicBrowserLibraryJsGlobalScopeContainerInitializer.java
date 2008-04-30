package org.eclipse.wst.jsdt.libraries;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import org.eclipse.wst.jsdt.core.JsGlobalScopeContainerInitializer;
import org.eclipse.wst.jsdt.core.IAccessRule;
import org.eclipse.wst.jsdt.core.IIncludePathAttribute;
import org.eclipse.wst.jsdt.core.IJsGlobalScopeContainer;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.compiler.libraries.LibraryLocation;
import org.eclipse.wst.jsdt.core.compiler.libraries.SystemLibraryLocation;
import org.eclipse.wst.jsdt.core.infer.DefaultInferrenceProvider;


public class BasicBrowserLibraryJsGlobalScopeContainerInitializer extends JsGlobalScopeContainerInitializer implements IJsGlobalScopeContainer {
	private static final String CONTAINER_ID = "org.eclipse.wst.jsdt.launching.baseBrowserLibrary"; //$NON-NLS-1$
	private static final String ContainerDescription = Messages.BasicBrowserLibraryJsGlobalScopeContainerInitializer_ECMA3Browser;
	private static final String FILE_DESCRIPTION0 = Messages.BasicBrowserLibraryJsGlobalScopeContainerInitializer_ECMA3DOM;
	private static final String FILE_DESCRIPTION1 = Messages.BasicBrowserLibraryJsGlobalScopeContainerInitializer_CommonWebBrowser;
	private static final char[][] LIBRARY_FILE_NAME = {
														{ 'b', 'a', 's', 'e', 'B', 'r', 'o', 'w', 's', 'e', 'r', 'L', 'i', 'b', 'r', 'a', 'r', 'y', '.', 'j', 's' },
														{'b','r','o','w','s','e','r','W','i','n','d','o','w','.','j','s'}
													  };
	private static final String LibraryDescription = Messages.BasicBrowserLibraryJsGlobalScopeContainerInitializer_ECMA3BrowserLibrary;
	
	class BasicLibLocation extends SystemLibraryLocation {
		BasicLibLocation() {
			super();
		}
		
		public char[][] getLibraryFileNames() {
			return  BasicBrowserLibraryJsGlobalScopeContainerInitializer.LIBRARY_FILE_NAME ;
		}
	}
	
	public LibraryLocation getLibraryLocation() {
		return new BasicLibLocation();
	}

	/**
	 * @deprecated Use {@link #getIncludepathEntries()} instead
	 */
	public IIncludePathEntry[] getClasspathEntries() {
		return getIncludepathEntries();
	}

	public IIncludePathEntry[] getIncludepathEntries() {
		LibraryLocation libLocation =  getLibraryLocation();
		char[][] filesInLibs = libLocation.getLibraryFileNames();
		IIncludePathEntry[] entries = new IIncludePathEntry[filesInLibs.length];
		for (int i = 0; i < entries.length; i++) {
			IPath workingLibPath = new Path(libLocation.getLibraryPath(filesInLibs[i]));
			entries[i] = JavaScriptCore.newLibraryEntry(workingLibPath.makeAbsolute(), null, null, new IAccessRule[0], new IIncludePathAttribute[0], true);
		}
		return entries;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.JsGlobalScopeContainerInitializer#canUpdateJsGlobalScopeContainer(org.eclipse.core.runtime.IPath, org.eclipse.wst.jsdt.core.IJavaScriptProject)
	 */
	public boolean canUpdateJsGlobalScopeContainer(IPath containerPath, IJavaScriptProject project) {
		return true;
		
		
	}

	protected IJsGlobalScopeContainer getContainer(IPath containerPath, IJavaScriptProject project) {
		return this;
	}
	
	public String getDescription() {
		return BasicBrowserLibraryJsGlobalScopeContainerInitializer.LibraryDescription;
	}
	
	public String getDescription(IPath containerPath, IJavaScriptProject project) {
		
		if(containerPath==null) return null;
		
		IPath p1 = new Path(new String(BasicBrowserLibraryJsGlobalScopeContainerInitializer.LIBRARY_FILE_NAME[0]));
		IPath p2 = new Path(new String(BasicBrowserLibraryJsGlobalScopeContainerInitializer.LIBRARY_FILE_NAME[1]));
		IPath requestedContainerPath = new Path(containerPath.lastSegment());
		if (requestedContainerPath.equals(p1)) {
			return BasicBrowserLibraryJsGlobalScopeContainerInitializer.FILE_DESCRIPTION0;
		}else if (requestedContainerPath.equals(p2)) {
			return BasicBrowserLibraryJsGlobalScopeContainerInitializer.FILE_DESCRIPTION1;
		}
		return BasicBrowserLibraryJsGlobalScopeContainerInitializer.ContainerDescription;
	}
	
	public int getKind() {
		return IJsGlobalScopeContainer.K_SYSTEM;
	}
	
	public IPath getPath() {
		return new Path(BasicBrowserLibraryJsGlobalScopeContainerInitializer.CONTAINER_ID);
	}
	
	public void initialize(IPath containerPath, IJavaScriptProject project) throws CoreException {
		JavaScriptCore.setJsGlobalScopeContainer(containerPath, new IJavaScriptProject[] { project }, new IJsGlobalScopeContainer[] { getContainer(containerPath, project) }, null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.JsGlobalScopeContainerInitializer#containerSuperTypes()
	 */
	public String[] containerSuperTypes() {
		return new String[] {Messages.BasicBrowserLibraryJsGlobalScopeContainerInitializer_Window};
	}


	public String getInferenceID() {
		return DefaultInferrenceProvider.ID;
	}
	
}
