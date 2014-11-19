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
package org.eclipse.wst.jsdt.nodejs.core.api.projects;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.BowerJson;
import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.Bowerrc;
import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.ConfigurationDatabaseJs;
import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.ConfigurationServerJs;
import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.ContributingMd;
import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.Gitignore;
import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.LicenseMd;
import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.ModelsUsersJs;
import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.PackageJson;
import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.ReadmeMd;
import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.RoutesRoutesJs;
import org.eclipse.wst.jsdt.nodejs.core.internal.templates.mean.ServerJs;

/**
 * Utility class to initialize and manipulate a MEAN project.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MEANProject {
	/**
	 * The name of the UTF-8 charset.
	 */
	private static final String UTF8 = "utf-8"; //$NON-NLS-1$

	/**
	 * The root directory of the project.
	 */
	private File directory;

	/**
	 * The name.
	 */
	private String name;

	/**
	 * The version.
	 */
	private String version;

	/**
	 * The constructor.
	 *
	 * @param projectFolder
	 *            The root folder of the project
	 * @param name
	 *            The name of the project
	 * @param version
	 *            The version of the project
	 */
	public MEANProject(File projectFolder, String name, String version) {
		this.directory = projectFolder;
		this.name = name;
		this.version = version;
	}

	/**
	 * Initialize the content of the project.
	 */
	public void initialize() {
		Map<String, CharSequence> path2content = new HashMap<String, CharSequence>();
		path2content.putAll(this.getAppContents());
		path2content.putAll(this.getServerContents());

		Set<Entry<String, CharSequence>> entries = path2content.entrySet();
		for (Entry<String, CharSequence> entry : entries) {
			try {
				File outputFile = new File(this.directory, entry.getKey());
				if (!outputFile.getParentFile().exists()) {
					outputFile.getParentFile().mkdirs();
				}
				Files.write(entry.getValue(), outputFile, Charset.forName(UTF8));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the contents of the server.
	 *
	 * @return the contents of the server
	 */
	private Map<String, CharSequence> getServerContents() {
		Map<String, CharSequence> path2content = new HashMap<String, CharSequence>();

		path2content.put(Gitignore.PATH, new Gitignore().generateFile());

		path2content.put(PackageJson.PATH, new PackageJson().generateFile(this.name, this.version));
		path2content.put(BowerJson.PATH, new BowerJson().generateFile(this.name));
		path2content.put(Bowerrc.PATH, new Bowerrc().generateFile());

		path2content.put(ContributingMd.PATH, new ContributingMd().generateFile());
		path2content.put(ReadmeMd.PATH, new ReadmeMd().generateFile());
		path2content.put(LicenseMd.PATH, new LicenseMd().generateFile());

		path2content.put(ConfigurationDatabaseJs.PATH, new ConfigurationDatabaseJs().generateFile());
		path2content.put(ConfigurationServerJs.PATH, new ConfigurationServerJs().generateFile());
		path2content.put(ModelsUsersJs.PATH, new ModelsUsersJs().generateFile());
		path2content.put(RoutesRoutesJs.PATH, new RoutesRoutesJs().generateFile());
		path2content.put(ServerJs.PATH, new ServerJs().generateFile());

		return path2content;
	}

	/**
	 * Returns the contents of the server.
	 *
	 * @return the contents of the server
	 */
	private Map<String, CharSequence> getAppContents() {
		Map<String, CharSequence> path2content = new HashMap<String, CharSequence>();
		return path2content;
	}
}
