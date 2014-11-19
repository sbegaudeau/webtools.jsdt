package org.eclipse.wst.jsdt.nodejs.ide.ui.internal.preferences;

import java.io.File;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.jsdt.nodejs.core.api.INodeJsConstants;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.NodeJsIdeUiActivator;
import org.eclipse.wst.jsdt.webapp.core.api.commands.Which;

/**
 * Class used to initialize default preference values.
 */
public class NodeJsPreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * The default global location of node.js on linux.
	 */
	private static final String DEFAULT_NODE_GLOBAL_UNIX_LOCATION = "/usr/bin/node"; //$NON-NLS-1$

	/**
	 * The default local location of node.js on linux.
	 */
	private static final String DEFAULT_NODE_LOCAL_UNIX_LOCATION = "/usr/local/bin/node"; //$NON-NLS-1$

	/**
	 * The default location of node.js on windows.
	 */
	private static final String DEFAULT_NODE_WINDOWS_LOCATION = "C:\\Program Files\\nodejs\\node"; //$NON-NLS-1$

	/**
	 * The default global location of npm on linux.
	 */
	private static final String DEFAULT_NPM_GLOBAL_UNIX_LOCATION = "/usr/bin/npm"; //$NON-NLS-1$

	/**
	 * The default local location of npm on linux.
	 */
	private static final String DEFAULT_NPM_LOCAL_UNIX_LOCATION = "/usr/local/bin/npm"; //$NON-NLS-1$

	/**
	 * The default location of npm on windows.
	 */
	private static final String DEFAULT_NPM_WINDOWS_LOCATION = "C:\\Program Files\\nodejs\\npm"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = NodeJsIdeUiActivator.getInstance().getPreferenceStore();
		store.setDefault(INodeJsPreferenceConstants.NODE_LOCATION, this.getDefaultNodeJsLocation());
		store.setDefault(INodeJsPreferenceConstants.NPM_LOCATION, this.getDefaultNpmLocation());
	}

	/**
	 * Returns the location of the node.js executable.
	 *
	 * @return the location of the node.js executable
	 */
	private String getDefaultNodeJsLocation() {
		String location = new Which(INodeJsConstants.NPM).call();
		if (location == null && System.getProperty("os.name").indexOf("win") > 0) { //$NON-NLS-1$ //$NON-NLS-2$
			location = DEFAULT_NODE_WINDOWS_LOCATION;
		} else if (location == null) {
			if (new File(DEFAULT_NODE_GLOBAL_UNIX_LOCATION).exists()) {
				location = DEFAULT_NODE_GLOBAL_UNIX_LOCATION;
			} else if (new File(DEFAULT_NODE_LOCAL_UNIX_LOCATION).exists()) {
				location = DEFAULT_NODE_LOCAL_UNIX_LOCATION;
			} else {
				location = DEFAULT_NODE_GLOBAL_UNIX_LOCATION;
			}
		}
		return location;
	}

	/**
	 * Returns the location of the npm executable.
	 *
	 * @return the location of the npm executable
	 */
	private String getDefaultNpmLocation() {
		String location = new Which(INodeJsConstants.NPM).call();
		if (location == null && System.getProperty("os.name").indexOf("win") > 0) { //$NON-NLS-1$ //$NON-NLS-2$
			location = DEFAULT_NPM_WINDOWS_LOCATION;
		} else if (location == null) {
			if (new File(DEFAULT_NPM_GLOBAL_UNIX_LOCATION).exists()) {
				location = DEFAULT_NPM_GLOBAL_UNIX_LOCATION;
			} else if (new File(DEFAULT_NPM_LOCAL_UNIX_LOCATION).exists()) {
				location = DEFAULT_NPM_LOCAL_UNIX_LOCATION;
			} else {
				location = DEFAULT_NPM_GLOBAL_UNIX_LOCATION;
			}
		}
		return location;
	}
}
