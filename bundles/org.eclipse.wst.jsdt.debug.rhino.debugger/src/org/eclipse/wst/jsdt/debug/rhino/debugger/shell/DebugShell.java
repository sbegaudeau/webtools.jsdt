package org.eclipse.wst.jsdt.debug.rhino.debugger.shell;

import java.util.ArrayList;

import org.eclipse.wst.jsdt.debug.rhino.debugger.RhinoDebugger;
import org.eclipse.wst.jsdt.debug.rhino.transport.SocketTransportService;
import org.eclipse.wst.jsdt.debug.rhino.transport.TransportService;
import org.mozilla.javascript.tools.shell.Main;

public class DebugShell {
	
    public static void main(String args[]) {
    	
    	String port = "9888";
    	boolean suspend = false;
    	
    	ArrayList argList = new ArrayList();
    	for (int i = 0; i < args.length; i++) {
    		String arg = args[i];
    		if (arg.equals("-port")) {
    			port = args[++i];
    		} else if (arg.equals("-suspend")) {
    			suspend = Boolean.valueOf(args[++i]).booleanValue();
    		}else if (arg.equals("-debug")) {
    			// skip
    			continue;
    		}else if (arg.equals("-opt")) {
    			// skip
    			i++;
    			continue;
    		}
    		argList.add(args[i]);
		}
    	String[] newArgs = (String[]) argList.toArray(new String[0]); 
    	
		TransportService service = new SocketTransportService();
		if (suspend) {
			System.out.println("Waiting for debugger connection on port " + port + "...");
		}
		RhinoDebugger debugger = new RhinoDebugger(service, port, suspend);
		debugger.start();
		Main.shellContextFactory.addListener(debugger);
		
		Main.main(newArgs);
    }
}
