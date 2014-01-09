/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.debug.core.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

/**
 * Collection of utility methods for tests
 * 
 * @since 1.0
 */
public class Util {

	/**
	 */
	public static final String UTF_8 = "UTF-8"; //$NON-NLS-1$
	/**
	 * The name of the source container <code>scripts</code> in the test bundle
	 */
	public static final String SRC_SCRIPTS_CONTAINER = "scripts"; //$NON-NLS-1$
	
	/**
	 * Constant representing the default size to read from an input stream
	 */
	private static final int DEFAULT_READING_SIZE = 8192;
	
	/**
	 * Returns the int value of the given {@link Number}
	 * @param number
	 * @return
	 */
	public static int numberAsInt(Object number) {
		return ((Number) number).intValue();
	}

	/**
	 * Returns the OS path to the directory that contains the test plugin.
	 * 
	 * @since 1.1
	 */
	public static IPath getPluginDirectoryPath() {
		if (Platform.isRunning()) {
			try {
				URL platformURL = Platform.getBundle(JSDTDebugTestPlugin.PLUGIN_ID).getEntry("/"); //$NON-NLS-1$ 
				return new Path(new File(FileLocator.toFileURL(platformURL).getFile()).getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new Path(System.getProperty("user.dir")); //$NON-NLS-1$
	}
	
	/**
	 * Loads the test source with the given name. If the source does not exist
	 * a {@link FileNotFoundException} is thrown.
	 * 
	 * @param container the folder in the test project containing the source
	 * @param sourcename the name of the source file to load
	 * 
	 * @return the {@link String} representation of the source or <code>null</code>
	 * @throws FileNotFoundException if the source does not exist
	 */
	public static String getTestSource(String container, String sourcename) throws IOException {
		if (Platform.isRunning()) {
			URL platformURL = Platform.getBundle(JSDTDebugTestPlugin.PLUGIN_ID).getEntry("/" + container + "/" + sourcename); //$NON-NLS-1$ //$NON-NLS-2$ 
			char[] chars = getInputStreamAsCharArray(platformURL.openStream(), -1, UTF_8);
			if (chars != null) {
				return new String(chars);
			}
		}

		throw new IllegalStateException("Platform not running"); //$NON-NLS-1$
	}
	
	/**
	 * Opens an {@link InputStream} to the test source found in the given container with the given name
	 * 
	 * @param container
	 * @param sourcename
	 * @return the {@link InputStream} to the test source
	 * @throws IOException
	 */
	public static InputStream getSourceStream(String container, String sourcename) throws IOException {
		if (Platform.isRunning()) {
			URL url = Platform.getBundle(JSDTDebugTestPlugin.PLUGIN_ID).getEntry("/" + container + "/" + sourcename); //$NON-NLS-1$ //$NON-NLS-2$
			if(url != null) {
				return url.openStream();
			}
			throw new IOException("Could not locate the test source for [/"+container+"/"+sourcename+"]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		throw new IllegalStateException("Platform not running"); //$NON-NLS-1$
	}
	
	/**
	 * Returns the contents of the given file as a string, or <code>null</code>
	 * 
	 * @param file the file to get the contents for
	 * @return the contents of the file as a {@link String} or <code>null</code>
	 */
	public static String getFileContentAsString(File file) {
		String contents = null;
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			char[] array = getInputStreamAsCharArray(stream, -1, UTF_8);
			contents = new String(array);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return contents;
	}
	
	/**
	 * Returns the given input stream's contents as a character array.
	 * If a length is specified (i.e. if length != -1), this represents the number of bytes in the stream.
	 * Note the specified stream is not closed in this method
	 * 
	 * @param stream the stream to get convert to the char array 
	 * @param length the length of the input stream, or -1 if unknown
	 * @param encoding the encoding to use when reading the stream
	 * @return the given input stream's contents as a character array.
	 * @throws IOException if a problem occurred reading the stream.
	 */
	public static char[] getInputStreamAsCharArray(InputStream stream, int length, String encoding) throws IOException {
		Charset charset = null;
		try {
			charset = Charset.forName(encoding);
		} catch (IllegalCharsetNameException e) {
			System.err.println("Illegal charset name : " + encoding); //$NON-NLS-1$
			return null;
		} catch(UnsupportedCharsetException e) {
			System.err.println("Unsupported charset : " + encoding); //$NON-NLS-1$
			return null;
		}
		CharsetDecoder charsetDecoder = charset.newDecoder();
		charsetDecoder.onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
		byte[] contents = getInputStreamAsByteArray(stream, length);
		ByteBuffer byteBuffer = ByteBuffer.allocate(contents.length);
		byteBuffer.put(contents);
		byteBuffer.flip();
		return charsetDecoder.decode(byteBuffer).array();
	}
	
	/**
	 * Returns the given input stream as a byte array
	 * 
	 * @param stream the stream to get as a byte array
	 * @param length the length to read from the stream or -1 for unknown
	 * @return the given input stream as a byte array
	 * @throws IOException
	 */
	public static byte[] getInputStreamAsByteArray(InputStream stream, int length) throws IOException {
		byte[] contents;
		if (length == -1) {
			contents = new byte[0];
			int contentsLength = 0;
			int amountRead = -1;
			do {
				// read at least 8K
				int amountRequested = Math.max(stream.available(), DEFAULT_READING_SIZE);
				// resize contents if needed
				if (contentsLength + amountRequested > contents.length) {
					System.arraycopy(contents,
							0,
							contents = new byte[contentsLength + amountRequested],
							0,
							contentsLength);
				}
				// read as many bytes as possible
				amountRead = stream.read(contents, contentsLength, amountRequested);
				if (amountRead > 0) {
					// remember length of contents
					contentsLength += amountRead;
				}
			} while (amountRead != -1);
			// resize contents if necessary
			if (contentsLength < contents.length) {
				System.arraycopy(contents, 0, contents = new byte[contentsLength], 0, contentsLength);
			}
		} else {
			contents = new byte[length];
			int len = 0;
			int readSize = 0;
			while ((readSize != -1) && (len != length)) {
				// See PR 1FMS89U
				// We record first the read size. In this case length is the actual
				// read size.
				len += readSize;
				readSize = stream.read(contents, len, length - len);
			}
		}
		return contents;
	}
}
