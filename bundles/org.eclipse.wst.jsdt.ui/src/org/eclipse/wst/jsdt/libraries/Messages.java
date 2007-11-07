package org.eclipse.wst.jsdt.libraries;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.wst.jsdt.libraries.messages"; //$NON-NLS-1$

	public static String BasicBrowserLibraryClassPathContainerInitializer_CommonWebBrowser;

	public static String BasicBrowserLibraryClassPathContainerInitializer_ECMA3Browser;

	public static String BasicBrowserLibraryClassPathContainerInitializer_ECMA3BrowserLibrary;

	public static String BasicBrowserLibraryClassPathContainerInitializer_ECMA3DOM;

	public static String BasicBrowserLibraryClassPathContainerInitializer_Window;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
