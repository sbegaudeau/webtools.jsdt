package org.eclipse.wst.jsdt.debug.rhino.debugger;

import org.eclipse.wst.jsdt.debug.internal.rhino.debugger.RhinoDebuggerImpl;

public class RhinoDebugger {
	private RhinoDebuggerImpl impl;

	/**
	 * This constructor will only accept a <code>transport</code> argument
	 * of <code>socket</code>. I.e. <code>transport=socket</code>.<br><br>
	 * 
	 * @param configString the configuration string, for example: <code>transport=socket,suspend=y,address=9000</code>
	 */
	public RhinoDebugger(String configString) {
		impl = new RhinoDebuggerImpl(configString);
	}
	
	/**
	 * Starts the debugger
	 */
	public void start() {
		impl.start();
	}

	/**
	 * Stops the debugger
	 */
	public void stop() {
		impl.stop();
	}
}
