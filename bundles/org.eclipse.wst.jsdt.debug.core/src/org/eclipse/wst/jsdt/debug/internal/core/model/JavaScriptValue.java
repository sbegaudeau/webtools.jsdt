/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.debug.core.jsdi.ArrayReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.FunctionReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.NullValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.NumberValue;
import org.eclipse.wst.jsdt.debug.core.jsdi.ObjectReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.Property;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptValue;

/**
 * Default implementation of {@link IValue}
 * 
 * @since 1.0
 */
public class JavaScriptValue extends JavaScriptDebugElement implements IJavaScriptValue {

	private Value value;
	private String name;
	private List properties;

	/**
	 * Constructor
	 * 
	 * @param variable
	 * @param value
	 */
	public JavaScriptValue(JavaScriptVariable variable, Value value) {
		super(variable.getJSDITarget());
		this.name = variable.getName();
		this.value = value;
	}

	/**
	 * Constructor
	 * 
	 * @param variable
	 * @param value
	 */
	public JavaScriptValue(JavaScriptProperty variable, Value value) {
		super(variable.getJSDITarget());
		this.name = variable.getName();
		this.value = value;
	}

	/**
	 * Hook to return the underlying value for this {@link JavaScriptValue}
	 * 
	 * @return the underlying {@link Value}
	 */
	protected Value getUnderlyingValue() {
		return this.value;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.model.IJavaScriptValue#getDetailString()
	 */
	public String getDetailString() {
		if (this.value instanceof ArrayReference) {
			ArrayReference array = (ArrayReference) this.value;
			return array.getValues().toString();
		}
		if (this.value instanceof FunctionReference) {
			return ((FunctionReference) this.value).valueString();
		}
		if (this.value instanceof NumberValue) {
			NumberValue nvalue = (NumberValue) this.value;
			if (!nvalue.isNaN()) {
				return numberToString(nvalue.value());
			}
		}
		return this.value.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValue#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException {
		if (this.value != null) {
			return this.value.getValueTypeName();
		}
		return null;
	}

	/**
	 * Return the name to display for the variable
	 * 
	 * @return the variable name
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	public String getValueString() throws DebugException {
		if (this.value instanceof ObjectReference) {
			ObjectReference ref = (ObjectReference) this.value;
			StringBuffer buffer = new StringBuffer();
			buffer.append(NLS.bind(ModelMessages.JavaScriptValue_object_value_label, new String[] {this.value.valueString(), ref.id().toString()}));
			return buffer.toString();
		}
		if (this.value instanceof NumberValue) {
			NumberValue nvalue = (NumberValue) this.value;
			if (!nvalue.isNaN()) {
				return numberToString(nvalue.value());
			}
		}
		return this.value.valueString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValue#getVariables()
	 */
	public synchronized IVariable[] getVariables() throws DebugException {
		if (!hasVariables()) {
			return null;
		}
		if (this.properties == null) {
			ObjectReference reference = (ObjectReference) this.value;
			List underlyingProperties = reference.properties();
			this.properties = new ArrayList(underlyingProperties.size());
			for (Iterator iterator = underlyingProperties.iterator(); iterator.hasNext();) {
				Property property = (Property) iterator.next();
				JavaScriptProperty jsdiProperty = new JavaScriptProperty(this, property);
				this.properties.add(jsdiProperty);
			}
		}
		return (IVariable[]) this.properties.toArray(new IVariable[this.properties.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	public boolean hasVariables() throws DebugException {
		if (this.value instanceof ObjectReference) {
			return !(this.value instanceof NullValue);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValue#isAllocated()
	 */
	public boolean isAllocated() throws DebugException {
		return this.properties != null;
	}
	
	/**
	 * Converts the given double value to a {@link String} removing the trailing .0 in the event the precision is 1
	 * 
	 * @param n the number to convert
	 * @return the {@link String} value of the number with trailing .0 removed iff the precision is 1
	 */
	public static String numberToString(Number n) {
		double d = n.doubleValue();
		if (d != d) {
			return NumberValue.NAN;
		}
		if (d == Double.POSITIVE_INFINITY) {
			return NumberValue.INFINITY;
		}
		if (d == Double.NEGATIVE_INFINITY) {
			return NumberValue.NEG_INFINITY;
		}
		if (d == 0.0) {
			return "0"; //$NON-NLS-1$
		}
		// we only care about base 10
		String number = Double.toString(d);
		// we only convert for a precision equal to 1
		if (number.endsWith(".0")) { //$NON-NLS-1$
			number = number.substring(0, number.length() - 2);
		}
		return number;
	}
}
