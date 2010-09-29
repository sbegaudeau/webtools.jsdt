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
package org.eclipse.wst.jsdt.debug.internal.ui.eval;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IExpression;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IWatchExpression;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptDebugTarget;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptValue;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;

/**
 * Inspect expression for a JavaScript evaluation result
 * 
 * @since 1.0
 */
public class JavaScriptInspectExpression implements IWatchExpression {

	private IJavaScriptValue value = null;
	
	/**
	 * Constructor
	 * 
	 * @param value the value to show
	 */
	public JavaScriptInspectExpression(IJavaScriptValue value) {
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IDebugElement#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		return JavaScriptDebugModel.MODEL_ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IDebugElement#getLaunch()
	 */
	public ILaunch getLaunch() {
		return value.getLaunch();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if(IJavaScriptValue.class.equals(adapter)) {
			return value;
		}
		if(IExpression.class.equals(adapter)) {
			return this;
		}
		if(IJavaScriptDebugTarget.class.equals(adapter)) {
			return value.getDebugTarget();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IExpression#getExpressionText()
	 */
	public String getExpressionText() {
		try {
			return value.getValueString();
		}
		catch(DebugException de) {
			JavaScriptDebugUIPlugin.log(de);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IExpression#getValue()
	 */
	public IValue getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IExpression#getDebugTarget()
	 */
	public IDebugTarget getDebugTarget() {
		return value.getDebugTarget();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IExpression#dispose()
	 */
	public void dispose() {
		value = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IErrorReportingExpression#hasErrors()
	 */
	public boolean hasErrors() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IErrorReportingExpression#getErrorMessages()
	 */
	public String[] getErrorMessages() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IWatchExpression#evaluate()
	 */
	public void evaluate() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IWatchExpression#setExpressionContext(org.eclipse.debug.core.model.IDebugElement)
	 */
	public void setExpressionContext(IDebugElement context) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IWatchExpression#setExpressionText(java.lang.String)
	 */
	public void setExpressionText(String expressionText) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IWatchExpression#isPending()
	 */
	public boolean isPending() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IWatchExpression#isEnabled()
	 */
	public boolean isEnabled() {
		return value != null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IWatchExpression#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
	}
}
