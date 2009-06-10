/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowContext;
import org.eclipse.wst.jsdt.internal.compiler.flow.FlowInfo;
import org.eclipse.wst.jsdt.internal.compiler.impl.Constant;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class ClassLiteralAccess extends Expression  {

	public TypeReference type;
	public TypeBinding targetType;
	FieldBinding syntheticField;

	public ClassLiteralAccess(int sourceEnd, TypeReference type) {
		this.type = type;
		type.bits |= IgnoreRawTypeCheck; // no need to worry about raw type usage
		this.sourceStart = type.sourceStart;
		this.sourceEnd = sourceEnd;
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		// if reachable, request the addition of a synthetic field for caching the class descriptor
		SourceTypeBinding sourceType = currentScope.outerMostClassScope().enclosingSourceType();
		// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=22334
		if (!sourceType.isInterface()
				&& !sourceType.isBaseType()
				&& currentScope.compilerOptions().sourceLevel < ClassFileConstants.JDK1_5) {
			syntheticField = sourceType.addSyntheticFieldForClassLiteral(targetType, currentScope);
		}
		return flowInfo;
	}

	public StringBuffer printExpression(int indent, StringBuffer output) {

		return type.print(0, output).append(".class"); //$NON-NLS-1$
	}

	public TypeBinding resolveType(BlockScope scope) {

		constant = Constant.NotAConstant;
		if ((targetType = type.resolveType(scope, true /* check bounds*/)) == null)
			return null;

		if (targetType.isArrayType()
			&& ((ArrayBinding) targetType).leafComponentType == TypeBinding.VOID) {
			scope.problemReporter().cannotAllocateVoidArray(this);
			return null;
		}
		ReferenceBinding classType = scope.getJavaLangClass();
		if (classType.isGenericType()) {
		    // Integer.class --> Class<Integer>, perform boxing of base types (int.class --> Class<Integer>)
			TypeBinding boxedType = null;
			if (targetType.id == T_void) {
				boxedType = scope.environment().getType(JAVA_LANG_VOID);
				if (boxedType == null) {
					boxedType = new ProblemReferenceBinding(JAVA_LANG_VOID, null, ProblemReasons.NotFound);
				}
			} else {
				boxedType = scope.boxing(targetType);
			}
		    this.resolvedType = scope.environment().createParameterizedType(classType, new TypeBinding[]{ boxedType }, null/*not a member*/);
		} else {
		    this.resolvedType = classType;
		}
		return this.resolvedType;
	}

	public void traverse(
		ASTVisitor visitor,
		BlockScope blockScope) {

		if (visitor.visit(this, blockScope)) {
			type.traverse(visitor, blockScope);
		}
		visitor.endVisit(this, blockScope);
	}
	public int getASTType() {
		return IASTNode.CLASS_LITERAL_ACCESS;
	
	}
}
