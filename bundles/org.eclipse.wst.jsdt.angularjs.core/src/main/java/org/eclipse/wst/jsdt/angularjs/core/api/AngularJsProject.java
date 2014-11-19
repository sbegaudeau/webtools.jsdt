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
package org.eclipse.wst.jsdt.angularjs.core.api;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.wst.jsdt.angularjs.core.internal.templates.BowerJson;
import org.eclipse.wst.jsdt.angularjs.core.internal.templates.BowerRc;
import org.eclipse.wst.jsdt.angularjs.core.internal.templates.EditorConfig;
import org.eclipse.wst.jsdt.angularjs.core.internal.templates.GitAttributes;
import org.eclipse.wst.jsdt.angularjs.core.internal.templates.GitIgnore;
import org.eclipse.wst.jsdt.angularjs.core.internal.templates.GulpfileJs;
import org.eclipse.wst.jsdt.angularjs.core.internal.templates.JSHintRc;
import org.eclipse.wst.jsdt.angularjs.core.internal.templates.PackageJson;
import org.eclipse.wst.jsdt.angularjs.core.internal.templates.app.IndexHtml;
import org.eclipse.wst.jsdt.angularjs.core.internal.templates.app.scripts.AppJs;

/**
 * Utility class to manipulate an AngularJS project.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class AngularJsProject {
	/**
	 * The charset to use to create the project.
	 */
	private static final String UTF8 = "UTF-8"; //$NON-NLS-1$

	/**
	 * The name.
	 */
	private String name;

	/**
	 * The version.
	 */
	private String version;

	/**
	 * The directory.
	 */
	private File directory;

	/**
	 * The constructor.
	 *
	 * @param directory
	 *            The directory in which the project will be created
	 * @param name
	 *            The name
	 * @param version
	 *            The version
	 */
	public AngularJsProject(File directory, String name, String version) {
		this.directory = directory;
		this.name = name;
		this.version = version;
	}

	/**
	 * Creates the project on the file system.
	 */
	public void create() {
		Map<String, CharSequence> path2content = new HashMap<String, CharSequence>();
		path2content.putAll(this.getAppContents());
		path2content.putAll(this.createTests());
		path2content.putAll(this.createExtras());

		Set<Entry<String, CharSequence>> entries = path2content.entrySet();
		for (Entry<String, CharSequence> entry : entries) {
			try {
				Files.write(entry.getValue(), new File(directory, entry.getKey()), Charset.forName(UTF8));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the content of the application.
	 *
	 * @return The content of the application
	 */
	private Map<String, CharSequence> getAppContents() {
		File appDirectory = new File(directory, IAngularJsCoreConstants.APP);
		appDirectory.mkdir();
		File scriptDirectory = new File(appDirectory, IAngularJsCoreConstants.SCRIPTS);
		scriptDirectory.mkdir();

		Map<String, CharSequence> path2content = new HashMap<String, CharSequence>();
		path2content.put(IAngularJsCoreConstants.INDEX_HTML, new IndexHtml().generateFile());
		path2content.put(IAngularJsCoreConstants.APP_JS, new AppJs().generateFile());

		return path2content;
	}

	/**
	 * Returns the content of the tests.
	 *
	 * @return The content of the tests
	 */
	private Map<String, CharSequence> createTests() {
		Map<String, CharSequence> path2content = new HashMap<String, CharSequence>();
		return path2content;
	}

	/**
	 * Returns the content of the extra resources.
	 *
	 * @return The content of the extra resources
	 */
	private Map<String, CharSequence> createExtras() {
		Map<String, CharSequence> path2content = new HashMap<String, CharSequence>();

		path2content.put(IAngularJsCoreConstants.BOWERRC, new BowerRc().generateFile());
		path2content.put(IAngularJsCoreConstants.EDITORCONFIG, new EditorConfig().generateFile());
		path2content.put(IAngularJsCoreConstants.GITATTRIBUTES, new GitAttributes().generateFile());
		path2content.put(IAngularJsCoreConstants.GITIGNORE, new GitIgnore().generateFile());
		path2content.put(IAngularJsCoreConstants.JSHINTRC, new JSHintRc().generateFile());
		path2content.put(IAngularJsCoreConstants.BOWER_JSON, new BowerJson().generateFile());
		path2content.put(IAngularJsCoreConstants.GULPFILE_JS, new GulpfileJs().generateFile());
		path2content.put(IAngularJsCoreConstants.PACKAGE_JSON, new PackageJson().generateFile());

		return path2content;
	}
}
