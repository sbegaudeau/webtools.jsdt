/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.ast;

import org.eclipse.wst.jsdt.core.ast.IASTNode;
import org.eclipse.wst.jsdt.core.ast.IMemberValuePair;
import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BaseTypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ElementValuePair;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

/**
 * MemberValuePair node
 */
public class MemberValuePair extends ASTNode implements IMemberValuePair {

	public char[] name;
	public Expression value;
	public MethodBinding binding;
	/**
	 *  The representation of this pair in the type system.
	 */
	public ElementValuePair compilerElementPair = null;

	public MemberValuePair(char[] token, int sourceStart, int sourceEnd, Expression value) {
		this.name = token;
		this.sourceStart = sourceStart;
		this.sourceEnd = sourceEnd;
		this.value = value;
		if (value instanceof ArrayInitializer) {
			value.bits |= IsAnnotationDefaultValue;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode#print(int, java.lang.StringBuffer)
	 */
	public StringBuffer print(int indent, StringBuffer output) {
		output
			.append(name)
			.append(" = "); //$NON-NLS-1$
		value.print(0, output);
		return output;
	}

	public void resolveTypeExpecting(BlockScope scope, TypeBinding requiredType) {

		if (this.value == null) {
			this.compilerElementPair = new ElementValuePair(this.name, this.value, this.binding);
			return;
		}
		if (requiredType == null) {
			// fault tolerance: keep resolving
			if (this.value instanceof ArrayInitializer) {
				this.value.resolveTypeExpecting(scope,(TypeBinding) null);
			} else {
				this.value.resolveType(scope);
			}
			this.compilerElementPair = new ElementValuePair(this.name, this.value, this.binding);
			return;
		}

		this.value.setExpectedType(requiredType); // needed in case of generic method invocation
		TypeBinding valueType;
		if (this.value instanceof ArrayInitializer) {
			ArrayInitializer initializer = (ArrayInitializer) this.value;
			valueType = initializer.resolveTypeExpecting(scope, this.binding.returnType);
		} else if (this.value instanceof ArrayAllocationExpression) {
			this.value.resolveType(scope);
			valueType = null; // no need to pursue
		} else {
			valueType = this.value.resolveType(scope);
		}
		this.compilerElementPair = new ElementValuePair(this.name, this.value, this.binding);
		if (valueType == null)
			return;

		TypeBinding leafType = requiredType.leafComponentType();
		if (!((this.value.isConstantValueOfTypeAssignableToType(valueType, requiredType)
				|| (requiredType.isBaseType() && BaseTypeBinding.isWidening(requiredType.id, valueType.id)))
				|| valueType.isCompatibleWith(requiredType))) {

			if (!(requiredType.isArrayType()
					&& requiredType.dimensions() == 1
					&& (this.value.isConstantValueOfTypeAssignableToType(valueType, leafType)
							|| (leafType.isBaseType() && BaseTypeBinding.isWidening(leafType.id, valueType.id)))
							|| valueType.isCompatibleWith(leafType))) {

				if (!leafType.isAnnotationType() || valueType.isAnnotationType()) {
					scope.problemReporter().typeMismatchError(valueType, requiredType, this.value);
				}
				return; // may allow to proceed to find more errors at once
			}
		} else {
			scope.compilationUnitScope().recordTypeConversion(requiredType.leafComponentType(), valueType.leafComponentType());
			this.value.computeConversion(scope, requiredType, valueType);
		}

		// annotation methods can only return base types, String, Class, enum type, annotation types and arrays of these
		checkAnnotationMethodType: {
			switch (leafType.erasure().id) {
				case T_byte :
				case T_short :
				case T_char :
				case T_int :
				case T_long :
				case T_float :
				case T_double :
				case T_boolean :
				case T_JavaLangString :
					break checkAnnotationMethodType;
				case T_JavaLangClass :
					break checkAnnotationMethodType;
			}
			if (leafType.isEnum()) {
				break checkAnnotationMethodType;
			}
			if (leafType.isAnnotationType()) {
				break checkAnnotationMethodType;
			}
		}
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			if (this.value != null) {
				this.value.traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, scope);
	}
	public int getASTType() {
		return IASTNode.MEMBER_VALUEPAIR;
	
	}
}
