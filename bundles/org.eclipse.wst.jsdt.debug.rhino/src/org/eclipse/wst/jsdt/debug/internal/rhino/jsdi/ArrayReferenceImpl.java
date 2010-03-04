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
package org.eclipse.wst.jsdt.debug.internal.rhino.jsdi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.jsdt.debug.core.jsdi.ArrayReference;
import org.eclipse.wst.jsdt.debug.core.jsdi.Property;
import org.eclipse.wst.jsdt.debug.core.jsdi.Value;

/**
 * Rhino implementation of {@link ArrayReference}
 * 
 * @see ArrayReference
 * @see ObjectReferenceImpl
 * @since 1.0
 */
public class ArrayReferenceImpl extends ObjectReferenceImpl implements ArrayReference {

	/**
	 * Empty array
	 */
	protected static final Value[] NO_VALUES = new Value[0];

	/**
	 * Raw list of {@link Value}s
	 */
	private ArrayList values = null;
	private int size = 0;

	/**
	 * Constructor
	 * 
	 * @param vm
	 * @param stackFrameImpl
	 * @param body
	 */
	public ArrayReferenceImpl(VirtualMachineImpl vm, Map body, StackFrameImpl stackFrameImpl) {
		super(vm, body, stackFrameImpl);
		this.size = properties().size();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ArrayReference#getValue(int)
	 */
	public Value getValue(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index > length()) {
			throw new IndexOutOfBoundsException();
		}
		Value value = (Value) getValues().get(index);
		if(value == null) {
			return vm.mirrorOfNull();
		}
		return value; 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ArrayReference#getValues()
	 */
	public List getValues() {
		synchronized (this) {
			if (this.values == null) {
				this.values = new ArrayList(this.size);
				List props = properties();
				for (Iterator iter = props.iterator(); iter.hasNext();) {
					this.values.add(((Property) iter.next()).value());
				}
			}
		}
		return this.values;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.core.jsdi.ArrayReference#length()
	 */
	public int length() {
		return this.size;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.debug.internal.rhino.jsdi.ObjectReferenceImpl#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(NLS.bind("Array [{0}]", new String[] {Integer.toString(this.size)}));
		return buffer.toString();
	}
}
