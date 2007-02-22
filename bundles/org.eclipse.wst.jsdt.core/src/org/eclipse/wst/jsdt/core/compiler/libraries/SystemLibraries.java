package org.eclipse.wst.jsdt.core.compiler.libraries;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;

public class SystemLibraries {

	public static final char[] SYSTEM_LIBARAY_NAME= {'s','y','s','t','e','m','.','j','s'};
	private SystemLibraries()
	{
		
	}

	public static File getLocation()
	{
		String path=getLibraryPath("system.js");
		File file = new File(path);
		return file.getParentFile();
	}
	
	public static String getLibraryPath(String name)
	{
		URL url=new SystemLibraries().getClass().getResource(name);
		if (!url.getProtocol().equals("file"))
			try {
				url=Platform.resolve(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		String file = url.getFile();
		if (file.startsWith("/"))
			file=file.substring(1);
		return file;
	}
}
