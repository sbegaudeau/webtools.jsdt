/*******************************************************************************
 * Copyright (c) 2014 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.bower.core.api;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class used to read the content of the bower files.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerReaders {
	/**
	 * Returns the content of bower.json from the given input stream.
	 *
	 * @param inputStream
	 *            The input stream
	 * @return The content of bower.json
	 */
	public static final BowerJson getBowerJson(InputStream inputStream) {
		BowerJson bowerJson = null;
		BufferedReader bowerJsonBufferedReader = null;

		try {
			bowerJsonBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			bowerJson = new Gson().fromJson(bowerJsonBufferedReader, BowerJson.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} finally {
			if (bowerJsonBufferedReader != null) {
				try {
					bowerJsonBufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bowerJson;
	}

	/**
	 * Returns the content of .bowerrc from the given input stream.
	 *
	 * @param inputStream
	 *            The input stream
	 * @return The content of .bowerrc
	 */
	public static final BowerRc getBowerRc(InputStream inputStream) {
		BowerRc bowerRc = null;
		BufferedReader bowerRcBufferedReader = null;

		try {
			bowerRcBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			bowerRc = new Gson().fromJson(bowerRcBufferedReader, BowerRc.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} finally {
			if (bowerRcBufferedReader != null) {
				try {
					bowerRcBufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bowerRc;
	}
}
