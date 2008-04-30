package org.eclipse.wst.jsdt.core.compiler.libraries;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.core.util.Util;

public class SystemLibraryLocation implements LibraryLocation {

	public static final char[] SYSTEM_LIBARAY_NAME= {'s','y','s','t','e','m','.','j','s'};
	public static final char[] LIBRARY_RUNTIME_DIRECTORY={'l','i','b','r','a','r','i','e','s'};
	public static final char[] LIBRARY_PLUGIN_DIRECTORY={'l','i','b','r','a','r','i','e','s'};
	private static final boolean AUTO_UPDATE_LIBS=true;

	private static SystemLibraryLocation fInstance;



	public static LibraryLocation getInstance() {
		if(fInstance==null)
			fInstance = new SystemLibraryLocation();
		return fInstance;
	}

	public IPath getLibraryPathInPlugin() {
		return new Path("libraries"); //$NON-NLS-1$
	}

	public char[][] getLibraryFileNames() {
		return new char[][] {SYSTEM_LIBARAY_NAME};
	}

	protected String getPluginId() {
		return JavaScriptCore.PLUGIN_ID;
	}
	public char[][] getAllFilesInPluginDirectory(String directory){
		//InputStream is = null;
		//URL[] entries = FileLocator.findEntries(Platform.getBundle(getPluginId()), new Path("./" + directory ));
		Enumeration entries = (Platform.getBundle(getPluginId()).getEntryPaths(directory));
		ArrayList allEntries = new ArrayList();
		while(entries.hasMoreElements()) {
			Path value = new Path((String)entries.nextElement());
			char [] filename=value.lastSegment().toCharArray();
			if(Util.isJavaLikeFileName(filename)) { //$NON-NLS-1$
				allEntries.add(filename);
			}
		}
		char[][] fileNames = new char[allEntries.size()][];

		for(int i = 0;i<allEntries.size();i++) {
			fileNames[i] = (char[])allEntries.get(i);
	}

	return fileNames;
	}
	public SystemLibraryLocation(){
		try {
			IPath libraryRuntimePath = Platform.getStateLocation(Platform.getBundle(JavaScriptCore.PLUGIN_ID)).append( new String(LIBRARY_RUNTIME_DIRECTORY));
			if(!libraryRuntimePath.toFile().exists()) {
				libraryRuntimePath.toFile().mkdir();
			}

			char[][] libFiles = getLibraryFileNames();
			for(int i = 0;i<libFiles.length;i++) {
				IPath workingLibLocation = Platform.getStateLocation(Platform.getBundle(JavaScriptCore.PLUGIN_ID)).append( new String(LIBRARY_RUNTIME_DIRECTORY)).append(new String(libFiles[i]));
				File library = workingLibLocation.toFile();


				if(!library.exists()) {
					InputStream is = null;

					is	 = FileLocator.openStream(Platform.getBundle(getPluginId()),getLibraryPathInPlugin().append(new String(libFiles[i])), false);

					copyFile(is,library);


				}else if(AUTO_UPDATE_LIBS){
					long lastModold = library.lastModified();
					URL path  = FileLocator.toFileURL((Platform.getBundle(getPluginId()).getEntry(getLibraryPathInPlugin().append(new String(libFiles[i])).toString())));


					File inPlugin = new File(path.getFile());
					long lastModNew = inPlugin.lastModified();
					if(lastModNew>lastModold) {
					//	System.out.println("Updating old library file : " + path.getFile());
						library.delete();
						InputStream is = null;

						is	 = FileLocator.openStream(Platform.getBundle(getPluginId()),getLibraryPathInPlugin().append(new String(libFiles[i])), false);

						copyFile(is,library);
					}else {
						//System.out.println("Not Updating library file : " + path.getFile());
					}
				}
			}
		}catch(Exception ex) {}

	}


	public IPath getWorkingLibPath(){

		return new Path(getLibraryPath("")); //$NON-NLS-1$

	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.core.compiler.libraries.LibraryLocation#getLibraryPath(java.lang.String)
	 */
	public String getLibraryPath(String name){

		try {
			return  Platform.getStateLocation(Platform.getBundle(JavaScriptCore.PLUGIN_ID)).append( new String(LIBRARY_RUNTIME_DIRECTORY) ).append( name).toString();
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
