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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.wst.jsdt.bower.core.api.utils.IBowerConstants;
import org.eclipse.wst.jsdt.bower.core.internal.utils.I18n;
import org.eclipse.wst.jsdt.bower.core.internal.utils.I18nKeys;
import org.eclipse.wst.jsdt.nodejs.core.api.semver.Range;
import org.eclipse.wst.jsdt.nodejs.core.api.semver.Version;

/**
 * This command let you install recursively all the packages described in the bower.json description of the
 * dependencies of the project. You can specify where you want the dependencies to be downloaded.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class InstallCommand extends AbstractBowerCommand<InstallCommand> {
	/**
	 * Install all the dependencies detailled in the bower.json file into the given location.
	 */
	@Override
	public void call() {
		if (this.bowerJson.isPresent()) {
			Map<String, String> dependenciesToDownload = new HashMap<String, String>();
			dependenciesToDownload.putAll(this.bowerJson.get().getDependencies());
			dependenciesToDownload.putAll(this.bowerJson.get().getDevDependencies());

			Map<String, String> dependencies = new HashMap<String, String>();

			Set<Entry<String, String>> entrySet = dependenciesToDownload.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String packageId = entry.getKey();
				String rangeExpression = entry.getValue();

				Map<String, String> additionalDependencies = this.download(packageId, rangeExpression);
				dependencies.putAll(additionalDependencies);
			}

			// Download recursively the additional dependencies
			this.downloadAdditionalDependencies(dependencies);
		}
	}

	/**
	 * Download recursively the given dependencies.
	 *
	 * @param dependencies
	 *            The map of dependencies to download
	 */
	private void downloadAdditionalDependencies(Map<String, String> dependencies) {
		Map<String, String> additionalDependencies = new HashMap<String, String>();

		Set<Entry<String, String>> entrySet = dependencies.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String packageId = entry.getKey();
			String rangeExpression = entry.getValue();

			additionalDependencies.putAll(this.download(packageId, rangeExpression));
		}

		if (additionalDependencies.size() > 0) {
			this.downloadAdditionalDependencies(additionalDependencies);
		}
	}

	/**
	 * Downloads the package with the given packageId matching the given range.
	 *
	 * @param packageId
	 *            The package id
	 * @param rangeExpression
	 *            The range
	 * @return The dependencies of the downloaded package
	 */
	private Map<String, String> download(String packageId, String rangeExpression) {
		Map<String, String> dependencies = new HashMap<String, String>();

		Optional<String> packageUrl = this.getGitUrlFromPackageId(packageId);
		String packageName = this.getNameFromPackageId(packageId);

		if (packageUrl.isPresent() && this.monitor.isPresent() && !this.monitor.get().isCancelled()) {
			if (this.monitor.isPresent()) {
				this.monitor.get().beginTask(I18n.getString(I18nKeys.DOWNLOADING_LABEL, packageName), 10);
			}

			try {
				File tempFile = new File("/tmp"); //$NON-NLS-1$
				final Repository db = FileRepositoryBuilder.create(tempFile);
				Collection<Ref> refs = new Git(db).lsRemote().setRemote(packageUrl.get()).setTags(true)
						.call();

				Optional<Ref> bestMatch = this.findBestMatch(refs, rangeExpression);
				if (bestMatch.isPresent() && outputDirectory.isPresent()) {
					File downloadedDependencyFolder = new File(outputDirectory.get(), packageName);
					if (!downloadedDependencyFolder.exists()) {
						Git git = Git.cloneRepository().setProgressMonitor(monitor.get()).setURI(
								packageUrl.get()).setDirectory(downloadedDependencyFolder).setBranch(
								bestMatch.get().getName()).setBare(false).setNoCheckout(false).call();
						git.close();

						File gitFolder = new File(downloadedDependencyFolder, IBowerConstants.GIT_EXTENSION);
						this.delete(gitFolder);

						File bowerJsonFile = new File(downloadedDependencyFolder, IBowerConstants.BOWER_JSON);
						Optional<BowerJson> dependencyBowerJson = this.getBowerJson(bowerJsonFile);
						if (dependencyBowerJson.isPresent()) {
							dependencies.putAll(dependencyBowerJson.get().getDependencies());
						}
					}
				}

				db.close();
			} catch (GitAPIException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (this.monitor.isPresent()) {
				this.monitor.get().endTask();
			}
		}
		return dependencies;
	}

	/**
	 * Finds the Git reference that matches the best the version that we are looking for.
	 *
	 * @param refs
	 *            The references of the Git repository
	 * @param rangeExpression
	 *            The expression defining the ranges of the accepted references
	 * @return The Git references which matches the best the given range expression
	 */
	private Optional<Ref> findBestMatch(Collection<Ref> refs, String rangeExpression) {
		Optional<Ref> refToDownload = Optional.absent();

		Range range = Range.fromString(rangeExpression);
		for (Ref ref : refs) {
			String refName = ref.getName();
			if (refName.startsWith(IBowerConstants.REFS_TAGS)) {
				try {
					Version version = Version.fromString(refName
							.substring(IBowerConstants.REFS_TAGS.length()));
					if (version.isIn(range)) {
						refToDownload = Optional.fromNullable(ref);
					}
				} catch (IllegalArgumentException e) {
					// The name of the reference is not a valid version number, no need to log this
				}
			}
		}

		return refToDownload;
	}
}
