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
package org.eclipse.wst.jsdt.core.tests.dom;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import org.eclipse.wst.jsdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.wst.jsdt.core.dom.ArrayAccess;
import org.eclipse.wst.jsdt.core.dom.ArrayCreation;
import org.eclipse.wst.jsdt.core.dom.ArrayInitializer;
import org.eclipse.wst.jsdt.core.dom.ArrayType;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.BooleanLiteral;
import org.eclipse.wst.jsdt.core.dom.CharacterLiteral;
import org.eclipse.wst.jsdt.core.dom.ClassInstanceCreation;
import org.eclipse.wst.jsdt.core.dom.ConditionalExpression;
import org.eclipse.wst.jsdt.core.dom.ConstructorInvocation;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.FunctionRef;
import org.eclipse.wst.jsdt.core.dom.IBinding;
import org.eclipse.wst.jsdt.core.dom.IFunctionBinding;
import org.eclipse.wst.jsdt.core.dom.IPackageBinding;
import org.eclipse.wst.jsdt.core.dom.ITypeBinding;
import org.eclipse.wst.jsdt.core.dom.IVariableBinding;
import org.eclipse.wst.jsdt.core.dom.ImportDeclaration;
import org.eclipse.wst.jsdt.core.dom.InfixExpression;
import org.eclipse.wst.jsdt.core.dom.InstanceofExpression;
import org.eclipse.wst.jsdt.core.dom.ListExpression;
import org.eclipse.wst.jsdt.core.dom.MemberRef;
import org.eclipse.wst.jsdt.core.dom.NullLiteral;
import org.eclipse.wst.jsdt.core.dom.NumberLiteral;
import org.eclipse.wst.jsdt.core.dom.PackageDeclaration;
import org.eclipse.wst.jsdt.core.dom.ParenthesizedExpression;
import org.eclipse.wst.jsdt.core.dom.PostfixExpression;
import org.eclipse.wst.jsdt.core.dom.PrefixExpression;
import org.eclipse.wst.jsdt.core.dom.PrimitiveType;
import org.eclipse.wst.jsdt.core.dom.QualifiedName;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SimpleType;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.StringLiteral;
import org.eclipse.wst.jsdt.core.dom.SuperConstructorInvocation;
import org.eclipse.wst.jsdt.core.dom.SuperFieldAccess;
import org.eclipse.wst.jsdt.core.dom.SuperMethodInvocation;
import org.eclipse.wst.jsdt.core.dom.ThisExpression;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeLiteral;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
/**
 * @author oliviert
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
class BindingsCollectorVisitor extends ASTVisitor {

	private HashMap hashMap;
	private HashSet set;
	
	BindingsCollectorVisitor() {
		// visit Javadoc.tags() as well
		super(true);
		this.hashMap = new HashMap();
		this.set = new HashSet();
	}
	
	private void collectBindings(
		ASTNode node,
		IBinding binding) {
		if (binding != null) {
			hashMap.put(node, binding);
		} else {
			set.add(node);
		}
	}
 

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(AnonymousClassDeclaration)
	 */
	public void endVisit(AnonymousClassDeclaration node) {
		ITypeBinding typeBinding = node.resolveBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(ArrayAccess)
	 */
	public void endVisit(ArrayAccess node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(ArrayCreation)
	 */
	public void endVisit(ArrayCreation node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(ArrayInitializer)
	 */
	public void endVisit(ArrayInitializer node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(ArrayType)
	 */
	public void endVisit(ArrayType node) {
		ITypeBinding typeBinding = node.resolveBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(Assignment)
	 */
	public void endVisit(Assignment node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(BooleanLiteral)
	 */
	public void endVisit(BooleanLiteral node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(CharacterLiteral)
	 */
	public void endVisit(CharacterLiteral node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(ClassInstanceCreation)
	 */
	public void endVisit(ClassInstanceCreation node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(ConditionalExpression)
	 */
	public void endVisit(ConditionalExpression node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(ConstructorInvocation)
	 */
	public void endVisit(ConstructorInvocation node) {
		IFunctionBinding methodBinding = node.resolveConstructorBinding();
		collectBindings(node, methodBinding);
	}
 
	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(FieldAccess)
	 */
	public void endVisit(FieldAccess node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}
	
	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(ImportDeclaration)
	 */
	public void endVisit(ImportDeclaration node) {
		IBinding binding = node.resolveBinding();
		collectBindings(node, binding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(InfixExpression)
	 */
	public void endVisit(InfixExpression node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(InstanceofExpression)
	 */
	public void endVisit(InstanceofExpression node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see ASTVisitor#endVisit(MemberRef)
	 * @since 3.0
	 */
	public void endVisit(MemberRef node) {
		IBinding binding = node.resolveBinding();
		collectBindings(node, binding);
	}

	public void endVisit(ListExpression node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(FunctionDeclaration)
	 */
	public void endVisit(FunctionDeclaration node) {
		IFunctionBinding methodBinding = node.resolveBinding();
		collectBindings(node, methodBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(FunctionInvocation)
	 */
	public void endVisit(FunctionInvocation node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see ASTVisitor#endVisit(FunctionRef )
	 * @since 3.0
	 */
	public void endVisit(FunctionRef node) {
		IBinding binding = node.resolveBinding();
		collectBindings(node, binding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(NullLiteral)
	 */
	public void endVisit(NullLiteral node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(NumberLiteral)
	 */
	public void endVisit(NumberLiteral node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(PackageDeclaration)
	 */
	public void endVisit(PackageDeclaration node) {
		IPackageBinding packageBinding = node.resolveBinding();
		collectBindings(node, packageBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(ParenthesizedExpression)
	 */
	public void endVisit(ParenthesizedExpression node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(PostfixExpression)
	 */
	public void endVisit(PostfixExpression node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(PrefixExpression)
	 */
	public void endVisit(PrefixExpression node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(PrimitiveType)
	 */
	public void endVisit(PrimitiveType node) {
		ITypeBinding typeBinding = node.resolveBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(QualifiedName)
	 */
	public void endVisit(QualifiedName node) {
		IBinding binding = node.resolveBinding();
		collectBindings(node, binding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(SimpleName)
	 */
	public void endVisit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		collectBindings(node, binding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(SimpleType)
	 */
	public void endVisit(SimpleType node) {
		ITypeBinding typeBinding = node.resolveBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(SingleVariableDeclaration)
	 */
	public void endVisit(SingleVariableDeclaration node) {
		IVariableBinding variableBinding = node.resolveBinding();
		collectBindings(node, variableBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(StringLiteral)
	 */
	public void endVisit(StringLiteral node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(SuperConstructorInvocation)
	 */
	public void endVisit(SuperConstructorInvocation node) {
		IFunctionBinding methodBinding = node.resolveConstructorBinding();
		collectBindings(node, methodBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(SuperFieldAccess)
	 */
	public void endVisit(SuperFieldAccess node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(SuperMethodInvocation)
	 */
	public void endVisit(SuperMethodInvocation node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(ThisExpression)
	 */
	public void endVisit(ThisExpression node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(TypeDeclaration)
	 */
	public void endVisit(TypeDeclaration node) {
		ITypeBinding typeBinding = node.resolveBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(TypeLiteral)
	 */
	public void endVisit(TypeLiteral node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(VariableDeclarationExpression)
	 */
	public void endVisit(VariableDeclarationExpression node) {
		ITypeBinding typeBinding = node.resolveTypeBinding();
		collectBindings(node, typeBinding);
	}

	/**
	 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(VariableDeclarationFragment)
	 */
	public void endVisit(VariableDeclarationFragment node) {
		IVariableBinding variableBinding = node.resolveBinding();
		collectBindings(node, variableBinding);
	}

	/**
	 * Returns the hashMap.
	 * @return HashMap
	 */
	public HashMap getBindingsMap() {
		return hashMap;
	}

	/**
	 * Returns the set.
	 * @return HashSet
	 */
	public HashSet getUnresolvedNodesSet() {
		return set;
	}

}
