/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.rhino.debugger.shell;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.jsdt.debug.internal.rhino.debugger.RhinoDebuggerImpl;
import org.eclipse.wst.jsdt.debug.internal.rhino.transport.RhinoTransportService;
import org.eclipse.wst.jsdt.debug.transport.TransportService;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.mozilla.javascript.tools.shell.Main;

/**
 * A proxy to running {@link Main} that allows us to run a batch of scripts without using the 
 * <code>-e load(...)</code> arguments. As well any argument not paired to a known kind (see below) will be considered
 * as an absolute path to a script (*.js) file.
 * <br><br>
 * This runner supports the following:
 * <ul>
 * <li><code>-debug</code> to have the interpreter generate debugging tracing</li>
 * <li><code>-e [script_src]</code> to evaluate the following argument as a script</li>
 * <li><code>-encoding</code> to set the character encoding to use</li>
 * <li><code>-f [absolute_file_paths]</code> to specify one or more *.js files to evaluate. This argument differs
 * from the Rhino {@link Main} one, in that any following arguments will be treated as absolute
 * paths to script (*.js) files.</li>
 * <li><code>-fatal-warnings</code> to have all warnings are treated as error</li>
 * <li><code>-help</code> or <code>-?</code>, to pretty-print the available commands</li>
 * <li><code>-opt</code> or <code>-O</code> to set the optimization level. Where available levels are -1 (interpret only), 0 (none), 1-9 (all optimizations)</li>
 * <li><code>-port [port]</code> to set the port to communicate on</li>
 * <li><code>-sealedlib</code> to set the standard libraries as sealed - meaning they cannot be modified by any evaluating script(s)</li>
 * <li><code>-strict</code> to set the interpreter in strict mode and report all warnings</li>
 * <li><code>-suspend</code> to set if the debugger should start suspended and wait for a client to connect before continuing</li>
 * <li><code>-trace</code> to enabled debugger tracing</li>
 * <li><code>-version [version_number]</code>, where <code>version_number</code> is one of: 100, 110, 120, 130, 140, 150, 160, 170</li>
 * <li><code>-w</code> to report warnings</li>
 * </ul>
 * <br>
 * The following examples assume you have properly set up your classpath to reference <code>org.mozilla.javascript.jar</code>,
 * <code>org.eclipse.wst.jsdt.debug.rhino.debugger.jar</code> and <code>org.eclipse.wst.jsdt.debug.transport</code>. Also the 
 * fully qualified name use to run DebugMain is <code>org.eclipse.wst.jsdt.debug.rhino.debugger.shell.DebugMain</code>
 * Examples
 * <br><br>
 * <code>%>java DebugMain -opt 7 -strict -f /home/user/scripts/a.js /home/user/scripts/b.js -version 170</code>
 * <code>%> java DebugMain -version 140 -e load('/home/user/scripts/a.js','/home/user/scripts/b.js')</code>
 * @since 1.1
 */
public class DebugMain {

	/**
	 * Class to run an evaluated script when using <code>-e</code>
	 */
	static class Evaluator implements ContextAction {
		String scriptsrc = null;
		String[] scripts = null;
		
		/* (non-Javadoc)
		 * @see org.mozilla.javascript.ContextAction#run(org.mozilla.javascript.Context)
		 */
		public Object run(Context cx) {
			if(scriptsrc == null && scripts == null) {
				throw Kit.codeBug();
			}
			if(scriptsrc != null) {
				Script script = Main.loadScriptFromSource(cx, scriptsrc, FROM_EVAL, 0, null);
				if(script != null) {
					Main.evaluateScript(script, cx, Main.getGlobal());
				}
			}
			Object[] array = new Object[scripts.length];
	        System.arraycopy(scripts, 0, array, 0, scripts.length);
			Scriptable argsObj = cx.newArray(Main.global, array);
	        Main.global.defineProperty(GLOBAL_ARGUMENTS, argsObj, ScriptableObject.DONTENUM);
	        for (int i = 0; i < scripts.length; i++) {
				Main.processFile(cx, Main.global, scripts[i]);
			}
			return null; 
		}
		
		void reset() {
			scriptsrc = null;
			scripts = null;
		}
	}
	
	/**
	 * Constant representing the debug argument
	 * <br><br>
	 * Value is: <code>-debug</code>
	 */
	public static final String ARG_DEBUG = "-debug"; //$NON-NLS-1$
	/**
	 * Constant representing the evaluate argument
	 * <br><br>
	 * Value is: <code>-e</code>
	 */
	public static final String ARG_E = "-e"; //$NON-NLS-1$
	/**
	 * Constant representing the encoding argument
	 * <br><br>
	 * Value is: <code>-encoding</code>
	 */
	public static final String ARG_ENCODING = "-encoding"; //$NON-NLS-1$
	/**
	 * Constant representing the script file(s) argument
	 * <br><br>
	 * Value is: <code>-f</code>
	 */
	public static final String ARG_F = "-f"; //$NON-NLS-1$
	/**
	 * Constant representing the fatal-warnings argument
	 * <br><br>
	 * Value is: <code>-fatal-warnings</code>
	 */
	public static final String ARG_FATAL_WARNINGS = "-fatal-warnings"; //$NON-NLS-1$
	/**
	 * Constant representing the help argument
	 * <br><br>
	 * Value is: <code>-help</code>
	 */
	public static final String ARG_HELP = "-help"; //$NON-NLS-1$
	/**
	 * Constant representing the optimization argument
	 * <br><br>
	 * Value is: <code>-O</code>
	 */
	public static final String ARG_O = "-O"; //$NON-NLS-1$
	/**
	 * Constant representing the optimization argument
	 * <br><br>
	 * Value is: <code>-opt</code>
	 */
	public static final String ARG_OPT = "-opt"; //$NON-NLS-1$
	/**
	 * Constant representing the help argument
	 * <br><br>
	 * Value is: <code>-?</code>
	 */
	public static final String ARG_QM = "-?"; //$NON-NLS-1$
	/**
	 * Constant representing the sealed lib argument
	 * <br><br>
	 * Value is: <code>-sealedlib</code>
	 */
	public static final String ARG_SEALEDLIB = "-sealedlib"; //$NON-NLS-1$
	/**
	 * Constant representing the strict argument
	 * <br><br>
	 * Value is: <code>-strict</code>
	 */
	public static final String ARG_STRICT = "-strict"; //$NON-NLS-1$
	/**
	 * Constant representing the version argument
	 * <br><br>
	 * Value is: <code>-version</code>
	 */
	private static final String ARG_VERSION = "-version"; //$NON-NLS-1$
	/**
	 * Constant representing the report warnings argument
	 * <br><br>
	 * Value is: <code>-w</code>
	 */
	public static final String ARG_W = "-w"; //$NON-NLS-1$
	/**
	 * Constant representing the invalid message identifier for {@link Main}
	 * <br><br>
	 * Value is: <code>msg.shell.invalid</code>
	 */
	public static final String MSG_SHELL_INVALID = "msg.shell.invalid"; //$NON-NLS-1$
	/**
	 * Constant representing the invalid usage message identifier for {@link Main}
	 * <br><br>
	 * Value is: <code>msg.shell.usage</code>
	 */
	public static final String MSG_SHELL_USAGE = "msg.shell.usage"; //$NON-NLS-1$
	/**
	 * Copy from {@link Main} to specify as the path when evaluating a script via the <code>-e</code> argument
	 * <br><br>
	 * Value is: <code>&lt;command&gt;</code>
	 */
	public static final String FROM_EVAL = "<command>"; //$NON-NLS-1$
	/**
	 * Constant representing the name of the array containing the original script arguments in the global context
	 * <br><br>
	 * Value is: <code>arguments</code>
	 */
	public static final String GLOBAL_ARGUMENTS = "arguments"; //$NON-NLS-1$

	private static Evaluator evaluate = new Evaluator();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ToolErrorReporter reporter = new ToolErrorReporter(true);
		Main.shellContextFactory.setErrorReporter(reporter);
		startDebugger(args);
		ArrayList scripts = new ArrayList();
		processArgs(args, scripts, reporter);
		if(scripts.size() > 0) {
			checkGlobal();
			evaluate.reset();
			evaluate.scripts = (String[]) scripts.toArray(new String[scripts.size()]);
			Main.shellContextFactory.call(evaluate);
		}
	}

	static void startDebugger(String[] args) {
		String port = DebugShell.DEFAULT_PORT;
		boolean suspend = false, 
		        trace = false;
		for (int i = 0; i < args.length; i++) {
			if (DebugShell.ARG_PORT.equals(args[i])) {
    			port = args[++i];
    		} 
			else if (DebugShell.ARG_SUSPEND.equals(args[i])) {
				suspend = DebugShell.isSuspend(args[++i]);
    		}
    		else if(DebugShell.ARG_TRACE.equals(args[i])) {
    			trace = Boolean.valueOf(args[++i]).booleanValue();
    		}
		}
		TransportService service = new RhinoTransportService();
		RhinoDebuggerImpl debugger = new RhinoDebuggerImpl(service, port, suspend, trace);
		try {
			debugger.start();
			Main.shellContextFactory.addListener(debugger);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Walks over the original argument list and sets the corresponding arguments in Rhino
	 * 
	 * @param originalargs
	 * @param scriptcollector
	 * @param reporter
	 */
	static void processArgs(String[] originalargs, List scriptcollector, ToolErrorReporter reporter) {
		try {
			for (int i = 0; i < originalargs.length; i++) {
				String arg = originalargs[i];
	    		if(ARG_DEBUG.equals(arg)) {
					Main.shellContextFactory.setGeneratingDebug(true);
				}
				else if(ARG_E.equals(arg)) {
					peekArg(originalargs, i, false);
					checkGlobal();
	                evaluate.scriptsrc = originalargs[++i];
	                Main.shellContextFactory.call(evaluate);
				}
				else if(ARG_ENCODING.equals(arg)) {
					peekArg(originalargs, i, false);
	                String enc = originalargs[++i];
	                Main.shellContextFactory.setCharacterEncoding(enc);
	            }
				else if (ARG_F.equals(arg)) {
	                peekArg(originalargs, i, false);
	                String script = originalargs[++i];
	                try {
		                do {
		                	scriptcollector.add(script);
		                	peekArg(originalargs, i, false);
		                	script = originalargs[++i];
		                } while(true);
	                }
	                catch(IllegalArgumentException iae) {
	                	//do nothing, we are done collecting
	                }
	            }
				else if (ARG_FATAL_WARNINGS.equals(arg)) {
	                Main.shellContextFactory.setWarningAsError(true);
	            }
				else if (ARG_QM.equals(arg) || ARG_HELP.equals(arg)) {
	                Main.global.getOut().println(ToolErrorReporter.getMessage(MSG_SHELL_USAGE, Main.class.getName()));
	                System.exit(1);
		        }
				else if(ARG_VERSION.equals(arg)) {
	            	peekArg(originalargs, i, false);
	                int version;
	                try {
	                    version = Integer.parseInt(originalargs[++i]);
	                } catch (NumberFormatException ex) {
	                    throw new IllegalArgumentException(originalargs[i]);
	                }
	                if (!Context.isValidLanguageVersion(version)) {
	                	 throw new IllegalArgumentException(originalargs[i]);
	                }
	                Main.shellContextFactory.setLanguageVersion(version);
	            }
				else if (ARG_OPT.equals(arg) || ARG_O.equals(arg)) {
					peekArg(originalargs, i, true);
	                int opt;
	                try {
	                    opt = Integer.parseInt(originalargs[++i]);
	                } catch (NumberFormatException ex) {
	                   throw new IllegalArgumentException(originalargs[i]);
	                }
	                if (opt == -2) {
	                    // Compatibility with Cocoon Rhino fork
	                    opt = -1;
	                } else if (!Context.isValidOptimizationLevel(opt)) {
	                	throw new IllegalArgumentException(originalargs[i]);
	                }
	                Main.shellContextFactory.setOptimizationLevel(opt);
	            }
				else  if (ARG_SEALEDLIB.equals(arg)) {
	            	Main.global.setSealedStdLib(true);
	            }
				else if (ARG_STRICT.equals(arg)) {
	                Main.shellContextFactory.setStrictMode(true);
	                reporter.setIsReportingWarnings(true);
	            }
				else if (ARG_W.equals(arg)) {
	                reporter.setIsReportingWarnings(true);
	            }
			}
		}
		catch(IllegalArgumentException iae) {
			Main.global.getOut().println(ToolErrorReporter.getMessage(MSG_SHELL_INVALID, iae.getMessage()));
	        Main.global.getOut().println(ToolErrorReporter.getMessage(MSG_SHELL_USAGE, Main.class.getName()));
	        System.exit(1);
		}
	}
	
	/**
	 * Checks to see if the global context has been initialized, if not, it is initialized
	 */
	static void checkGlobal() {
		if(!Main.global.isInitialized()) {
        	Main.global.init(Main.shellContextFactory);
        }
	}
	
	/**
	 * peeks at the next argument entry to see if it exists and is not another <code>-&lt;arg&gt;</code>
	 * @param args
	 * @param idx
	 * @param ignoreswitch
	 * @throws IllegalArgumentException if the next argument is not valid
	 */
	static void peekArg(String[] args, int idx, boolean ignoreswitch) throws IllegalArgumentException {
		if((idx+1 < args.length)) {
			if(!args[idx+1].startsWith("-") || ignoreswitch) { //$NON-NLS-1$
				return;
			}
		}
		throw new IllegalArgumentException(args[idx]);
	}
}
