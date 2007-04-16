package org.eclipse.wst.jsdt.core.compiler.libraries;

import java.io.File;

public interface LibraryLocation {

	public File getLocation();

	public String getLibraryPath(String name);
	
	public char[] getLibraryFileName();

}