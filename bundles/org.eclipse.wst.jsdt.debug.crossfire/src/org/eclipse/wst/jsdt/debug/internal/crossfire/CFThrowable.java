/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.crossfire;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.jsdt.debug.internal.crossfire.transport.Attributes;

/**
 * Class to hold information about an onError event so it can be re-thrown, etc
 * 
 * @since 1.0
 */
public class CFThrowable extends Throwable {

	private Map frame = null;
	private Map error = null;
	private Map objects = null;
	
	/**
	 * Constructor
	 * @param json
	 */
	public CFThrowable(Map json) {
		super();
		if(json != null) {
			frame = (Map) json.get(Attributes.FRAME);
			if(frame != null) {
				processFrame();
			}
			else {
				Map map = (Map) json.get(Attributes.STACKTRACE);
				if(map != null) {
					//TODO process any frame infos we get
				}
			}
			error = (Map) json.get(Attributes.ERROR);
		}
	}

	void processFrame() {
		if(frame != null) {
			objects = new HashMap(frame.size());
			Map vals = (Map) frame.remove(Attributes.VALUE);
			if(vals != null) {
				vals = (Map) vals.remove(Attributes.SCRIPT);
				if(vals != null) {
					objects.put(Attributes.SCRIPT, new CFObject(Attributes.SCRIPT, (String)vals.get(Attributes.TYPE), (Number)vals.get(Attributes.HANDLE)));
				}
			}
			vals = (Map) frame.remove(Attributes.SCOPE);
			if(vals != null) {
				objects.put(Attributes.SCOPE, new CFObject(Attributes.SCOPE, (String)vals.get(Attributes.TYPE), (Number)vals.get(Attributes.HANDLE)));
			}
			vals = (Map) frame.remove(Attributes.CALLING_FRAME);
			if(vals != null) {
				objects.put(Attributes.CALLING_FRAME, new CFObject(Attributes.CALLING_FRAME, (String)vals.get(Attributes.TYPE), (Number) vals.get(Attributes.HANDLE)));
			}
			vals = (Map) frame.remove(Attributes.EXECUTION_CONTEXT);
			if(vals != null) {
				objects.put(Attributes.EXECUTION_CONTEXT, new CFObject(Attributes.EXECUTION_CONTEXT, (String)vals.get(Attributes.TYPE), (Number) vals.get(Attributes.HANDLE)));
			}
			vals = (Map) frame.remove(Attributes.CALLEE);
			if(vals != null) {
				objects.put(Attributes.CALLEE, new CFObject(Attributes.CALLEE, (String)vals.get(Attributes.TYPE), (Number) vals.get(Attributes.HANDLE)));
			}
			vals = (Map) frame.remove(Attributes.THIS_VALUE);
			if(vals != null) {
				objects.put(Attributes.THIS_VALUE, new CFObject(Attributes.THIS_VALUE, (String)vals.get(Attributes.TYPE), (Number) vals.get(Attributes.HANDLE)));
			}
		}
	}
	
	/**
	 * Returns if the error was caused by the debugger or not
	 * 
	 * @return if the debugger caused the error or not
	 */
	public boolean isDebugger() {
		if(frame != null) {
			Boolean bool = (Boolean) error.get(Attributes.IS_DEBUGGER);
			if(bool != null) {
				return bool.booleanValue();
			}
		}
		return false;
	}
	
	/**
	 * Returns if the error is valid wrt the state of the Firebug debugger
	 * 
	 * @return if the error is valid
	 */
	public boolean isValid() {
		if(frame != null) {
			Boolean bool = (Boolean) error.get(Attributes.IS_VALID);
			if(bool != null) {
				return bool.booleanValue();
			}
		}
		return false;
	}
	
	/**
	 * Returns the line number the error originated from or -1 if it could not be determined
	 * 
	 * @return the line number of the error or -1.
	 */
	public int lineNumber() {
		if(error != null) {
			Number val = (Number) error.get(Attributes.LINE_NUMBER);
			if(val == null) {
				//try lineNo
				val = (Number) error.get(Attributes.LINE_NO);
			}
			if(val != null) {
				return val.intValue();
			}
		}
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage() {
		return getMessage();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		if(error != null) {
			String val = (String) error.get(Attributes.MESSAGE);
			if(val == null) {
				//try errorMessage
				val = (String) error.get(Attributes.ERROR_MESSAGE);
			}
			return val;
		}
		return null;
	}
	
	/**
	 * Returns the column number the error originated from or -1 if it could not be determined
	 * 
	 * @return the column number or -1
	 */
	public int columnNumber() {
		if(error != null) {
			Number val = (Number) error.get(Attributes.COLUMN_NUMBER);
			if(val != null) {
				return val.intValue();
			}
		}
		return -1;
	}
	
	/**
	 * Returns the flags from the event or -1 if they could not be determined
	 * 
	 * @return the flags or -1
	 */
	public int getFlags() {
		if(error != null) {
			Number val = (Number) error.get(Attributes.FLAGS);
			if(val != null) {
				return val.intValue();
			}
		}
		return -1;
	}
	
	/**
	 * Return the category for the error or <code>null</code>
	 * 
	 * @return the error category or <code>null</code>
	 */
	public String getCategory() {
		if(error != null) {
			String val = (String) error.get(Attributes.CATEGORY);
			return val;
		}
		return null;
	}
	
	/**
	 * Returns the name of the script the error occurred in or <code>null</code> if it could not be determined
	 *  
	 * @return the name of the script or <code>null</code>
	 */
	public String scriptName() {
		if(error != null) {
			String val = (String) error.get(Attributes.FILE_NAME);
			if(val == null) {
				//try the source attribute
				val = (String) error.get(Attributes.SOURCE_NAME);
			}
			return val;
		}
		return null;
	}
	
	/**
	 * Returns the name of the function the error originated from or <code>null</code> if it could not be determined
	 * 
	 * @return the name of the function or <code>null</code>
	 */
	public String functionName() {
		if(frame != null) {
			String val = (String) frame.get(Attributes.FUNCTION_NAME);
			return val;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace()
	 */
	public void printStackTrace() {
		if(frame != null) {
			return;
		}
		super.printStackTrace();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 */
	public void printStackTrace(PrintStream s) {
		if(frame != null) {
			return;
		}
		super.printStackTrace(s);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
	 */
	public void printStackTrace(PrintWriter s) {
		if(frame != null) {
			return;
		}
		super.printStackTrace(s);
	}
}
