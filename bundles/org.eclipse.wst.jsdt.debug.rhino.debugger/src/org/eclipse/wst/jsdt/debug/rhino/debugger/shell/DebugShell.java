/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.debugger.shell;

import java.util.ArrayList;

import org.eclipse.wst.jsdt.debug.internal.rhino.debugger.RhinoDebuggerImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.RhinoTransportService;
import org.eclipse.wst.jsdt.debug.transport.TransportService;
import org.mozilla.javascript.tools.shell.Main;

/**
 * Entry point for launching a Rhino debugger
 * 
 * @since 1.0
 */
public final class DebugShell {
	
    public static void main(String args[]) {
    	
    	String port = "9888"; //$NON-NLS-1$
    	boolean suspend = false;
    	boolean trace = true;
    	
    	ArrayList argList = new ArrayList();
    	for (int i = 0; i < args.length; i++) {
    		String arg = args[i];
    		if (arg.equals("-port")) { //$NON-NLS-1$
    			port = args[++i];
    			continue;
    		} else if (arg.equals("-suspend")) { //$NON-NLS-1$
    			suspend = Boolean.valueOf(args[++i]).booleanValue();
    			continue;
    		}
    		else if(arg.equals("-trace")) { //$NON-NLS-1$
    			trace = Boolean.valueOf(args[++i]).booleanValue();
    			continue;
    		}
    		//forward all other args to Rhino
    		argList.add(arg);
		}
    	String[] newArgs = (String[]) argList.toArray(new String[0]); 
    	
		TransportService service = new RhinoTransportService();
		RhinoDebuggerImpl debugger = new RhinoDebuggerImpl(service, port, suspend, trace);
		try {
			debugger.start();
			Main.shellContextFactory.addListener(debugger);
			Main.exec(newArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
