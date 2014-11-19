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
package org.eclipse.wst.jsdt.bower.maven.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Main class of the maven plugin for Bower.
 *
 * @goal bower-install
 * @phase process-resources
 * @requiresDependencyResolution compile+runtime
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BowerInstallPojo extends AbstractMojo {
	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * @parameter expression = "${bower-install.serverUrl}"
	 * @readonly
	 */
	private String serverUrl;

	/**
	 * {@inheritDoc}
	 *
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		File basedir = project.getBasedir();
		this.visit(basedir);
	}

	/**
	 * Visits the given file and launch the full installation of all Bower dependencies if a file bower.json
	 * is found.
	 *
	 * @param file
	 *            The file to visit
	 */
	private void visit(File file) {
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			for (File child : children) {
				this.visit(child);
			}
		} else if (IBowerConstants.BOWER_JSON.equals(file.getName())) {
			this.getLog().info("bower install " + file.getAbsolutePath()); //$NON-NLS-1$

			String outputDirectory = IBowerConstants.BOWER_COMPONENTS;
			FileInputStream bowerJsonFileInputStream = null;
			FileInputStream bowerRcFileInputStream = null;
			try {
				bowerJsonFileInputStream = new FileInputStream(file);
				BowerJson bowerJson = BowerReaders.getBowerJson(bowerJsonFileInputStream);

				File bowerRcFile = new File(file.getParent(), IBowerConstants.BOWER_RC);
				if (bowerRcFile.exists()) {
					bowerRcFileInputStream = new FileInputStream(bowerRcFile);
					BowerRc bowerRc = BowerReaders.getBowerRc(bowerRcFileInputStream);
					if (bowerRc.getDirectory() != null && bowerRc.getDirectory().length() > 0) {
						outputDirectory = bowerRc.getDirectory();
					}
				}

				File directory = new File(file.getParent(), outputDirectory);

				Bower.install().setMonitor(NullProgressMonitor.INSTANCE).setOutputDirectory(directory)
						.setBowerJson(bowerJson).setBowerServerURL(serverUrl).call();

				this.getLog().info("Dependencies installed:"); //$NON-NLS-1$
				String[] dependenciesInstalled = directory.list();
				for (String dependency : dependenciesInstalled) {
					this.getLog().info(dependency);
				}
			} catch (IOException e) {
				this.getLog().error(e);
			} finally {
				if (bowerRcFileInputStream != null) {
					try {
						bowerRcFileInputStream.close();
					} catch (IOException e) {
						this.getLog().error(e);
					}
				}
				if (bowerJsonFileInputStream != null) {
					try {
						bowerJsonFileInputStream.close();
					} catch (IOException e) {
						this.getLog().error(e);
					}
				}
			}

		}
	}

}
