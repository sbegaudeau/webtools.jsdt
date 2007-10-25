package org.eclipse.wst.jsdt.core.compiler.libraries;

import org.eclipse.core.runtime.IPath;

public interface LibraryLocation {

	public String getLibraryPath(String name);

	public char[][] getLibraryFileNames();

	public IPath getLibraryPathInPlugin();

	public IPath getWorkingLibPath();

	public String getLibraryPath(char[] name);
}