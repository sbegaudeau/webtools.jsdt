package org.eclipse.wst.jsdt.core.compiler.libraries;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.core.JavaCore;
import org.osgi.framework.Bundle;

public class SystemLibraryLocation implements LibraryLocation {

	public static final char[] SYSTEM_LIBARAY_NAME= {'s','y','s','t','e','m','.','j','s'};
	public static final char[] LIBRARY_RUNTIME_DIRECTORY={'l','i','b','r','a','r','i','e','s'};
	public static final char[] LIBRARY_PLUGIN_DIRECTORY={'l','i','b','r','a','r','i','e','s'};
	
	private static SystemLibraryLocation fInstance;
	

	
	public static LibraryLocation getInstance() {
		if(fInstance==null)
			fInstance = new SystemLibraryLocation();
		return fInstance;
	}
	
	public IPath getLibraryPathInPlugin() {
		return new Path("libraries");
	}
	
	public char[][] getLibraryFileNames() {
		return new char[][] {SYSTEM_LIBARAY_NAME};
	}
	
	protected String getPluginId() {
		return JavaCore.PLUGIN_ID;
	}
	
	
	protected SystemLibraryLocation(){
		try {
			IPath libraryRuntimePath = Platform.getStateLocation(Platform.getBundle(JavaCore.PLUGIN_ID)).append( new String(LIBRARY_RUNTIME_DIRECTORY));
			if(!libraryRuntimePath.toFile().exists()) {
				libraryRuntimePath.toFile().mkdir();
			}
		
			char[][] libFiles = getLibraryFileNames();
			for(int i = 0;i<libFiles.length;i++) {
				IPath workingLibLocation = Platform.getStateLocation(Platform.getBundle(JavaCore.PLUGIN_ID)).append( new String(LIBRARY_RUNTIME_DIRECTORY)).append(new String(libFiles[i]));
				File library = workingLibLocation.toFile();
			
			
				if(!library.exists()) {
					URL url = null;
					InputStream is = null;
						
					is	 = FileLocator.openStream(Platform.getBundle(getPluginId()),getLibraryPathInPlugin().append(new String(libFiles[i])), false);				
					copyFile(is,library);
						
						
				}
			}
		}catch(Exception ex) {}
		
	}
	
	
	public IPath getWorkingLibPath(){
		
		return new Path(getLibraryPath(""));
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.compiler.libraries.LibraryLocation#getLibraryPath(java.lang.String)
	 */
	public String getLibraryPath(String name){
		
		try {
			return  Platform.getStateLocation(Platform.getBundle(JavaCore.PLUGIN_ID)).append( new String(LIBRARY_RUNTIME_DIRECTORY) ).append( name).toString();
		}
		catch (Exception ex)
		{return null;}
	
	}
	public String getLibraryPath(char[] name){
		return getLibraryPath(new String(name));
		
	}
	protected static void copyFile(InputStream src, File dst) throws IOException {
		InputStream in=null;
		OutputStream out=null;
		try {
			in = new BufferedInputStream(src);
			out = new BufferedOutputStream(new FileOutputStream(dst));		
			byte[] buffer = new byte[4096];
			int len;
			while ((len=in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}
	}

}
