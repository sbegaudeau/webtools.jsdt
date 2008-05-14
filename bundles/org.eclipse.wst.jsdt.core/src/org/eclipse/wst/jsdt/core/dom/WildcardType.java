/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.jsdt.core.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * Type node for a wildcard type (added in JLS3 API).
 * <pre>
 * WildcardType:
 *    <b>?</b> [ ( <b>extends</b> | <b>super</b>) Type ]
 * </pre>
 * <p>
 * Not all node arrangements will represent legal JavaScript constructs. In particular,
 * it is nonsense if a wildcard type node appears anywhere other than as an
 * argument of a <code>ParameterizedType</code> node.
 * </p>
 * 
 * <p><b>Note: This Class only applies to ECMAScript 4 which is not yet supported</b></p>
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class WildcardType extends Type {

	/**
	 * The "bound" structural property of this node type.
	 */
	public static final ChildPropertyDescriptor BOUND_PROPERTY =
		new ChildPropertyDescriptor(WildcardType.class, "bound", Type.class, OPTIONAL, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * The "upperBound" structural property of this node type.
	 */
	public static final SimplePropertyDescriptor UPPER_BOUND_PROPERTY =
		new SimplePropertyDescriptor(WildcardType.class, "upperBound", boolean.class, MANDATORY); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS;

	static {
		List propertyList = new ArrayList(3);
		createPropertyList(WildcardType.class, propertyList);
		addProperty(BOUND_PROPERTY, propertyList);
		addProperty(UPPER_BOUND_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(propertyList);
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 *
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor})
	 */
	public static List propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}

	/**
	 * The optional type bound node; <code>null</code> if none;
	 * defaults to none.
	 */
	private Type optionalBound = null;

	/**
	 * Indicates whether the wildcard bound is an upper bound
	 * ("extends") as opposed to a lower bound ("super").
	 * Defaults to <code>true</code> initially.
	 */
	private boolean isUpperBound = true;

	/**
	 * Creates a new unparented node for a wildcard type owned by the
	 * given AST. By default, no upper bound.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 *
	 * @param ast the AST that is to own this node
	 */
	WildcardType(AST ast) {
		super(ast);
	    unsupportedIn2();
	}

	/* (omit jsdoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}

	/* (omit jsdoc for this method)
	 * Method declared on ASTNode.
	 */
	final boolean internalGetSetBooleanProperty(SimplePropertyDescriptor property, boolean get, boolean value) {
		if (property == UPPER_BOUND_PROPERTY) {
			if (get) {
				return isUpperBound();
			} else {
				setUpperBound(value);
				return false;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetBooleanProperty(property, get, value);
	}

	/* (omit jsdoc for this method)
	 * Method declared on ASTNode.
	 */
	final ASTNode internalGetSetChildProperty(ChildPropertyDescriptor property, boolean get, ASTNode child) {
		if (property == BOUND_PROPERTY) {
			if (get) {
				return getBound();
			} else {
				setBound((Type) child);
				return null;
			}
		}
		// allow default implementation to flag the error
		return super.internalGetSetChildProperty(property, get, child);
	}

	/* (omit jsdoc for this method)
	 * Method declared on ASTNode.
	 */
	final int getNodeType0() {
		return WILDCARD_TYPE;
	}

	/* (omit jsdoc for this method)
	 * Method declared on ASTNode.
	 */
	ASTNode clone0(AST target) {
		WildcardType result = new WildcardType(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		result.setBound((Type) ASTNode.copySubtree(target, getBound()), isUpperBound());
		return result;
	}

	/* (omit jsdoc for this method)
	 * Method declared on ASTNode.
	 */
	final boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

	/* (omit jsdoc for this method)
	 * Method declared on ASTNode.
	 */
	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			// visit children in normal left to right reading order
			acceptChild(visitor, getBound());
		}
		visitor.endVisit(this);
	}

	/**
	 * Returns whether this wildcard type is an upper bound
	 * ("extends") as opposed to a lower bound ("super").
	 * <p>
	 * Note that this property is irrelevant for wildcards
	 * that do not have a bound.
	 * </p>
	 *
	 * @return <code>true</code> if an upper bound,
	 *    and <code>false</code> if a lower bound
	 * @see #setBound(Type)
	 */
	public boolean isUpperBound() {
		return this.isUpperBound;
	}

	/**
	 * Returns the bound of this wildcard type if it has one.
	 * If {@link #isUpperBound isUpperBound} returns true, this
	 * is an upper bound ("? extends B"); if it returns false, this
	 * is a lower bound ("? super B").
	 *
	 * @return the bound of this wildcard type, or <code>null</code>
	 * if none
	 * @see #setBound(Type)
	 */
	public Type getBound() {
		return this.optionalBound;
	}

	/**
	 * Sets the bound of this wildcard type to the given type and
	 * marks it as an upper or a lower bound. The method is
	 * equivalent to calling <code>setBound(type); setUpperBound(isUpperBound)</code>.
	 *
	 * @param type the new bound of this wildcard type, or <code>null</code>
	 * if none
	 * @param isUpperBound <code>true</code> for an upper bound ("? extends B"),
	 * and <code>false</code> for a lower bound ("? super B")
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @see #getBound()
	 * @see #isUpperBound()
	 */
	public void setBound(Type type, boolean isUpperBound) {
		setBound(type);
		setUpperBound(isUpperBound);
	}

	/**
	 * Sets the bound of this wildcard type to the given type.
	 *
	 * @param type the new bound of this wildcard type, or <code>null</code>
	 * if none
	 * @exception IllegalArgumentException if:
	 * <ul>
	 * <li>the node belongs to a different AST</li>
	 * <li>the node already has a parent</li>
	 * </ul>
	 * @see #getBound()
	 */
	public void setBound(Type type) {
		ASTNode oldChild = this.optionalBound;
		preReplaceChild(oldChild, type, BOUND_PROPERTY);
		this.optionalBound = type;
		postReplaceChild(oldChild, type, BOUND_PROPERTY);
	}

	/**
	 * Sets whether this wildcard type is an upper bound
	 * ("extends") as opposed to a lower bound ("super").
	 *
	 * @param isUpperBound <code>true</code> if an upper bound,
	 *    and <code>false</code> if a lower bound
	 * @see #isUpperBound()
	 */
	public void setUpperBound(boolean isUpperBound) {
		preValueChange(UPPER_BOUND_PROPERTY);
		this.isUpperBound = isUpperBound;
		postValueChange(UPPER_BOUND_PROPERTY);
	}

	/* (omit jsdoc for this method)
	 * Method declared on ASTNode.
	 */
	int memSize() {
		return BASE_NODE_SIZE + 2 * 4;
	}

	/* (omit jsdoc for this method)
	 * Method declared on ASTNode.
	 */
	int treeSize() {
		return
		memSize()
		+ (this.optionalBound == null ? 0 : getBound().treeSize());
	}
}

