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

import com.google.common.base.Optional;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.wst.jsdt.bower.core.api.utils.IBowerConstants;
import org.eclipse.wst.jsdt.nodejs.core.api.utils.ILogger;

/**
 * The bower command is used to perform a bower-related operation.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @param <BOWER_COMMAND>
 *            The type of the subclass
 */
public abstract class AbstractBowerCommand<BOWER_COMMAND extends AbstractBowerCommand<?>> {
	/**
	 * The prefixes to consider to determines if a package id is the URL of a Git repository or just its name.
	 */
	private static String[] PREFIXES = new String[] {IBowerConstants.GIT_PREFIX, IBowerConstants.HTTP_PREFIX,
			IBowerConstants.HTTPS_PREFIX, IBowerConstants.SSH_PREFIX };

	/**
	 * The URL of the bower server on which the request should be made.
	 */
	protected String bowerServerURL = IBowerConstants.DEFAULT_BOWER_SERVER_URL;

	/**
	 * The progress monitor.
	 */
	protected Optional<ProgressMonitor> monitor = Optional.absent();

	/**
	 * The logger.
	 */
	protected ILogger logger;

	/**
	 * The content of the bower.json file.
	 */
	protected Optional<BowerJson> bowerJson = Optional.absent();

	/**
	 * The directory in which the dependencies will be downloaded.
	 */
	protected Optional<File> outputDirectory = Optional.absent();

	/**
	 * Indicates if the given package id is the URL of a Git repository.
	 *
	 * @param packageId
	 *            The package id
	 * @return <code>true</code> if it is the URL of a Git repository, <code>false</code> otherwise.
	 */
	protected boolean isGitUrl(String packageId) {
		for (String prefix : PREFIXES) {
			if (packageId.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the name of a package from its package id.
	 *
	 * @param packageId
	 *            The package id
	 * @return The package id itself if it is not the URL of a Git repository or the last segment of the
	 *         package id minus the Git extension if it is the URL of a Git repository
	 */
	protected String getNameFromPackageId(String packageId) {
		if (this.isGitUrl(packageId)) {
			String name = packageId;
			if (name.endsWith(IBowerConstants.GIT_EXTENSION)) {
				name = name.substring(0, name.length() - IBowerConstants.GIT_EXTENSION.length());
				if (name.contains(IBowerConstants.SEPARATOR)) {
					name = name.substring(name.lastIndexOf(IBowerConstants.SEPARATOR
							+ IBowerConstants.SEPARATOR.length()));
				}
			}
		}
		return packageId;
	}

	/**
	 * Returns the Git URL from a package id.
	 *
	 * @param packageId
	 *            The package id
	 * @return The package id itself if it is a Git URL or the Git URL from the Bower package descriptor
	 *         retrieved from a Bower registry.
	 */
	protected Optional<String> getGitUrlFromPackageId(String packageId) {
		if (this.isGitUrl(packageId)) {
			return Optional.of(packageId);
		}

		Optional<String> result = Optional.absent();

		Optional<BowerPackageDescriptor> descriptor = this.getDescriptor(packageId);
		if (descriptor.isPresent()) {
			result = Optional.of(descriptor.get().getUrl());
		}

		return result;
	}

	/**
	 * Returns the folder where the bower components should be downloaded for the given project.
	 *
	 * @param bowerProject
	 *            The folder containing the file bower.json.
	 * @return The older where the bower components should be downloaded
	 */
	protected File getBowerComponentsFolder(File bowerProject) {
		File bowerComponents = new File(bowerProject, IBowerConstants.BOWER_COMPONENTS);

		File bowerrc = new File(bowerProject, IBowerConstants.BOWER_RC);
		if (bowerrc.exists()) {
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(bowerrc)));

				BowerRc bowerRcContent = new Gson().fromJson(bufferedReader, BowerRc.class);
				if (bowerRcContent.getDirectory() != null && bowerRcContent.getDirectory().length() > 0) {
					bowerComponents = new File(bowerProject, bowerRcContent.getDirectory());
				}
			} catch (IOException e) {
				logger.log(IBowerConstants.BOWER_CORE_BUNDLE_ID, ILogger.ERROR, e);
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						logger.log(IBowerConstants.BOWER_CORE_BUNDLE_ID, ILogger.ERROR, e);
					}
				}
			}
		}

		return bowerComponents;
	}

	/**
	 * Returns the bower package descriptor from a bower registry.
	 *
	 * @param packageId
	 *            The package id
	 * @return The bower packaged descriptor for the package with the given id
	 */
	protected Optional<BowerPackageDescriptor> getDescriptor(String packageId) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(this.bowerServerURL + IBowerConstants.PACKAGES_PATH + packageId);

		try {
			HttpResponse response = client.execute(request);
			InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());

			BufferedReader reader = null;
			try {
				reader = new BufferedReader(inputStreamReader);

				Gson gson = new Gson();
				BowerPackageDescriptor bowerPackageDescriptor = gson.fromJson(reader,
						BowerPackageDescriptor.class);
				return Optional.of(bowerPackageDescriptor);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception e) {
						logger.log(IBowerConstants.BOWER_CORE_BUNDLE_ID, ILogger.ERROR, e);
					}
				}
			}
		} catch (IOException e) {
			logger.log(IBowerConstants.BOWER_CORE_BUNDLE_ID, ILogger.ERROR, e);
		}
		return Optional.absent();
	}

	/**
	 * Executes the command.
	 */
	public abstract void call();

	/**
	 * Sets the monitor.
	 *
	 * @param monitor
	 *            The monitor to set
	 * @return The current object
	 */
	@SuppressWarnings("unchecked")
	public BOWER_COMMAND setMonitor(ProgressMonitor monitor) {
		this.monitor = Optional.fromNullable(monitor);
		return (BOWER_COMMAND)this;
	}

	/**
	 * Sets URL of the bowser server.
	 *
	 * @param bowerServerURL
	 *            The URL of the bowser server
	 * @return The current object
	 */
	@SuppressWarnings("unchecked")
	public BOWER_COMMAND setBowerServerURL(String bowerServerURL) {
		if (bowerServerURL != null) {
			this.bowerServerURL = bowerServerURL;
			if (this.bowerServerURL.endsWith(IBowerConstants.SEPARATOR)) {
				this.bowerServerURL = this.bowerServerURL.substring(0, this.bowerServerURL.length() - 1
						- IBowerConstants.SEPARATOR.length());
			}
		}
		return (BOWER_COMMAND)this;
	}

	/**
	 * Sets the bowerJson.
	 *
	 * @param bowerJson
	 *            The bowerJson to set
	 * @return The current object
	 */
	@SuppressWarnings("unchecked")
	public BOWER_COMMAND setBowerJson(BowerJson bowerJson) {
		this.bowerJson = Optional.fromNullable(bowerJson);
		return (BOWER_COMMAND)this;
	}

	/**
	 * Sets the outputDirectory, where the dependencies will be downloaded.
	 *
	 * @param outputDirectory
	 *            The outputDirectory to set
	 * @return The current object
	 */
	@SuppressWarnings("unchecked")
	public BOWER_COMMAND setOutputDirectory(File outputDirectory) {
		this.outputDirectory = Optional.fromNullable(outputDirectory);
		return (BOWER_COMMAND)this;
	}

	/**
	 * Delete the given file. If it is a directory, delete its content recursively.
	 *
	 * @param file
	 *            The file
	 * @throws IOException
	 *             In case of error during the deletion.
	 */
	protected void delete(File file) throws IOException {
		if (file.isDirectory()) {
			for (File c : file.listFiles()) {
				delete(c);
			}
		}
		if (!file.delete()) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
	}

	/**
	 * Returns the content of the file bower.json.
	 *
	 * @param bowerJsonFile
	 *            The file bower.json
	 * @return The content of the file
	 */
	protected Optional<BowerJson> getBowerJson(File bowerJsonFile) {
		if (bowerJsonFile.exists()) {
			try {
				List<String> lines = Files.readLines(bowerJsonFile, Charset.forName("UTF-8")); //$NON-NLS-1$

				StringBuilder content = new StringBuilder();
				for (String line : lines) {
					content.append(line);
				}
				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapter(BowerJson.class, new BowerJsonDeserializer());
				Gson gson = gsonBuilder.create();
				return Optional.fromNullable(gson.fromJson(content.toString(), BowerJson.class));
			} catch (JsonSyntaxException e) {
				logger.log(IBowerConstants.BOWER_CORE_BUNDLE_ID, ILogger.ERROR, e);
			} catch (IOException e) {
				logger.log(IBowerConstants.BOWER_CORE_BUNDLE_ID, ILogger.ERROR, e);
			}
		}
		return Optional.absent();
	}
}
