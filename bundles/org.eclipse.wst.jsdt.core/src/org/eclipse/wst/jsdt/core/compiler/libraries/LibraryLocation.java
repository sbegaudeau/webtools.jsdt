package org.eclipse.wst.jsdt.core.compiler.libraries;

import java.io.File;

import org.eclipse.core.runtime.IPath;

public interface LibraryLocation {

	public File getLocation();

	public String getLibraryPath(String name);
	
	public char[] getLibraryFileName();
	
	public IPath getLibraryPathInPlugin();

}