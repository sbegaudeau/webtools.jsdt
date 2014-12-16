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
package org.eclipse.wst.jsdt.nodejs.core.api.semver;

import java.util.List;

import org.eclipse.wst.jsdt.nodejs.core.internal.semver.VersionedConstraint;

/**
 * The version number contains four different parts: Major, Minor, Patch and the Qualifier.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class Version implements Comparable<Version> {
	/**
	 * The optional prefix of a version number.
	 */
	private static final String V = "v"; //$NON-NLS-1$

	/**
	 * The dot is used to separate the major and minor version numbers and the minor and patch version number.
	 */
	private static final String DOT = "."; //$NON-NLS-1$

	/**
	 * The dash is used to separate the first parts of the version number and the qualifier.
	 */
	private static final String DASH = "-"; //$NON-NLS-1$

	/**
	 * The plus is used to separate the first parts of the version number and the build.
	 */
	private static final String PLUS = "+"; //$NON-NLS-1$

	/**
	 * The major version number should be incremented when you make an incompatible API change.
	 */
	private int major;

	/**
	 * The minor version number should be incremented when you add a new functionality in a
	 * backward-compatible manner.
	 */
	private int minor;

	/**
	 * The patch version number should be incremented when you realize backward-compatible bug fixes.
	 */
	private int patch;

	/**
	 * The qualifier holds additional information regarding the version.
	 */
	private String qualifier = ""; //$NON-NLS-1$

	/**
	 * The build holds the unique identifier of a build.
	 */
	private String build = ""; //$NON-NLS-1$

	/**
	 * Sets the major version number.
	 *
	 * @param major
	 *            The major version number
	 */
	private void setMajor(int major) {
		this.major = major;
	}

	/**
	 * Sets the minor version number.
	 *
	 * @param minor
	 *            The minor version number
	 */
	private void setMinor(int minor) {
		this.minor = minor;
	}

	/**
	 * Sets the patch version number.
	 *
	 * @param patch
	 *            The patch version number
	 */
	private void setPatch(int patch) {
		this.patch = patch;
	}

	/**
	 * Sets the qualifier of the version number.
	 *
	 * @param qualifier
	 *            The qualifier
	 */
	private void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	/**
	 * Sets the build of the version number.
	 *
	 * @param build
	 *            The build
	 */
	private void setBuild(String build) {
		this.build = build;
	}

	/**
	 * Creates a new version number from the given string.
	 *
	 * @param version
	 *            The string representation of the version number
	 * @return The version
	 */
	public static Version fromString(String version) {
		// trim
		String trimmedVersion = version;
		trimmedVersion = trimmedVersion.trim();

		// remove "v" prefix
		if (trimmedVersion.startsWith(V) || trimmedVersion.startsWith(V.toUpperCase())) {
			trimmedVersion = trimmedVersion.substring(V.length());
		}

		// capture the build
		String build = ""; //$NON-NLS-1$
		int indexOfBuildSeparator = trimmedVersion.indexOf(PLUS);
		if (indexOfBuildSeparator != -1) {
			build = trimmedVersion.substring(indexOfBuildSeparator + PLUS.length());
			trimmedVersion = trimmedVersion.substring(0, indexOfBuildSeparator);
		}

		// capture the qualifier
		String qualifier = ""; //$NON-NLS-1$
		int indexOfQualifierSeparator = trimmedVersion.indexOf(DASH);
		if (indexOfQualifierSeparator != -1) {
			qualifier = trimmedVersion.substring(indexOfQualifierSeparator + DASH.length());
			trimmedVersion = trimmedVersion.substring(0, indexOfQualifierSeparator);
		}

		Version result = null;

		try {
			int indexOfMajorMinorSeparator = trimmedVersion.indexOf(DOT);
			if (indexOfMajorMinorSeparator == -1) {
				int major = Version.getInt(trimmedVersion, 0, trimmedVersion.length());

				// valid version number, ie: v3
				result = new Version();
				result.setMajor(major);
			} else {
				int indexOfMinorPatchSeparator = trimmedVersion.indexOf(DOT, indexOfMajorMinorSeparator + 1);
				int major = Version.getInt(trimmedVersion, 0, indexOfMajorMinorSeparator);

				// valid version number, ie: v3.
				result = new Version();
				result.setMajor(major);

				if (indexOfMinorPatchSeparator == -1) {

					int minor = Version.getInt(trimmedVersion, indexOfMajorMinorSeparator + 1, trimmedVersion
							.length());

					// valid version number, ie: v3.14
					result.setMinor(minor);
				} else {
					int minor = Version.getInt(trimmedVersion, indexOfMajorMinorSeparator + 1,
							indexOfMinorPatchSeparator);

					// valid version number, ie: v3.14.xyz
					result.setMinor(minor);

					int patch = Version.getInt(trimmedVersion, indexOfMinorPatchSeparator + 1, trimmedVersion
							.length());
					result.setPatch(patch);
				}
			}
		} catch (NumberFormatException e) {
			// catch every parsing related exceptions
			throw new IllegalArgumentException(version);
		}

		result.setQualifier(qualifier);
		result.setBuild(build);

		return result;
	}

	/**
	 * Returns the int value located between the start index and the end index from the given string.
	 *
	 * @param string
	 *            The string
	 * @param start
	 *            The start index
	 * @param end
	 *            The end index
	 * @return The int value parsed
	 */
	private static int getInt(String string, int start, int end) {
		int intValue = Integer.parseInt(string.substring(start, end));
		if (intValue >= 0) {
			return intValue;
		}
		throw new NumberFormatException();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Version otherVersion) {
		int result = 0;
		if (otherVersion == null) {
			return result;
		}

		if (this.major > otherVersion.major) {
			// v2 > v1
			result = 1;
		} else if (this.major == otherVersion.major) {
			// v1 == v1
			result = 0;

			if (this.minor > otherVersion.minor) {
				// v1.1 > v1.0
				result = 1;
			} else if (this.minor == otherVersion.minor) {
				// v1.1 == v1.1
				result = 0;

				if (this.patch > otherVersion.patch) {
					// v1.1.2 > v1.1.1
					result = 1;
				} else if (this.patch == otherVersion.patch) {
					// v1.1.1 == v1.1.1
					result = 0;

					// v1.1.1+abcdef < v1.1.1+bcdef == v1.1
					if (this.build.length() > 0 && otherVersion.build.length() > 0) {
						result = this.build.compareTo(otherVersion.build);
					}

					// v1.1.1-alpha < v1.1.1-beta < v1.1
					if (this.qualifier.length() > 0 && otherVersion.qualifier.length() == 0) {
						result = -1;
					} else if (this.qualifier.length() > 0 && otherVersion.qualifier.length() > 0) {
						result = this.qualifier.compareTo(otherVersion.qualifier);
					} else if (this.qualifier.length() == 0 && otherVersion.qualifier.length() > 0) {
						result = 1;
					}
				} else if (this.patch < otherVersion.patch) {
					// v1.1.1 < v1.1.2
					result = -1;
				}
			} else if (this.minor < otherVersion.minor) {
				// v1.0 < v1.1
				result = -1;
			}
		} else if (this.major < otherVersion.major) {
			// v1 < v2
			result = -1;
		}
		return result;
	}

	/**
	 * Indicates if the version is in the given range.
	 *
	 * @param range
	 *            The range
	 * @return <code>true</code> if the version is in the given range, <code>false</code> otherwise
	 */
	public boolean isIn(Range range) {
		boolean isInRange = true;

		List<VersionedConstraint> constraints = range.getConstraints();
		for (VersionedConstraint versionedConstraint : constraints) {
			isInRange = isInRange && versionedConstraint.isValid(this);
		}

		return isInRange;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(Integer.valueOf(this.major));
		builder.append(DOT);
		builder.append(Integer.valueOf(this.minor));
		builder.append(DOT);
		builder.append(Integer.valueOf(this.patch));

		if (this.qualifier.length() > 0) {
			builder.append(DASH);
			builder.append(this.qualifier);
		}

		if (this.build.length() > 0) {
			builder.append(PLUS);
			builder.append(this.build);
		}

		String version = builder.toString();
		if (version != null) {
			return version;
		}
		return ""; //$NON-NLS-1$
	}
}
