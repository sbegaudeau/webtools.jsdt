package org.eclipse.wst.jsdt.nodejs.ide.ui.internal.preferences;

import java.io.File;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.jsdt.nodejs.core.api.INodeJsConstants;
import org.eclipse.wst.jsdt.nodejs.core.api.Which;
import org.eclipse.wst.jsdt.nodejs.ide.ui.internal.NodeJsIdeUiActivator;

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
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = NodeJsIdeUiActivator.getInstance().getPreferenceStore();
		store.setDefault(INodeJsPreferenceConstants.NODE_LOCATION, this.getDefaultNodeJsLocation());
	}

	/**
	 * Returns the location of the node.js executable.
	 *
	 * @return the location of the node.js executable
	 */
	private String getDefaultNodeJsLocation() {
		String location = new Which(INodeJsConstants.NODE).call();
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
}
