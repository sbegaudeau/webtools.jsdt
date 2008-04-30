/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.dom;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;

import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import org.eclipse.wst.jsdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.wst.jsdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.wst.jsdt.core.dom.ArrayAccess;
import org.eclipse.wst.jsdt.core.dom.ArrayCreation;
import org.eclipse.wst.jsdt.core.dom.ArrayInitializer;
import org.eclipse.wst.jsdt.core.dom.ArrayType;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.BooleanLiteral;
import org.eclipse.wst.jsdt.core.dom.CastExpression;
import org.eclipse.wst.jsdt.core.dom.CharacterLiteral;
import org.eclipse.wst.jsdt.core.dom.ClassInstanceCreation;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.ConditionalExpression;
import org.eclipse.wst.jsdt.core.dom.ConstructorInvocation;
import org.eclipse.wst.jsdt.core.dom.EnumConstantDeclaration;
import org.eclipse.wst.jsdt.core.dom.EnumDeclaration;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
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
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.FunctionRef;
import org.eclipse.wst.jsdt.core.dom.NullLiteral;
import org.eclipse.wst.jsdt.core.dom.NumberLiteral;
import org.eclipse.wst.jsdt.core.dom.PackageDeclaration;
import org.eclipse.wst.jsdt.core.dom.ParameterizedType;
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

public class ASTConverterBindingsTest extends ConverterTestSetup {
	private static final boolean DEBUG = false;

	static class BindingsCollector extends ASTVisitor {

		public ArrayList arrayList;
		
		BindingsCollector() {
			// visit Javadoc.tags() as well
			super(true);
			this.arrayList = new ArrayList();
		}
		
		private void collectBindings(
			ASTNode node,
			IBinding binding) {
			
			if (binding != null) {
				arrayList.add(binding);
			}
		}

		/**
		 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(AnnotationTypeDeclaration)
		 * @since 3.0
		 */
		public void endVisit(AnnotationTypeDeclaration node) {
			ITypeBinding typeBinding = node.resolveBinding();
			collectBindings(node, typeBinding);
		}

		/**
		 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(AnnotationTypeMemberDeclaration)
		 * @since 3.0
		 */
		public void endVisit(AnnotationTypeMemberDeclaration node) {
			IFunctionBinding binding = node.resolveBinding();
			collectBindings(node, binding);
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
		 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(CastExpression)
		 */
		public void endVisit(CastExpression node) {
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
		 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(EnumConstantDeclaration)
		 * @since 3.0
		 */
		public void endVisit(EnumConstantDeclaration node) {
			IVariableBinding binding = node.resolveVariable();
			collectBindings(node, binding);
		}

		/**
		 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(EnumDeclaration)
		 * @since 3.0
		 */
		public void endVisit(EnumDeclaration node) {
			ITypeBinding typeBinding = node.resolveBinding();
			collectBindings(node, typeBinding);
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

		public void endVisit(ListExpression node) {
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
		 * @see org.eclipse.wst.jsdt.core.dom.ASTVisitor#endVisit(ParameterizedType)
		 * @since 3.0
		 */
		public void endVisit(ParameterizedType node) {
			ITypeBinding typeBinding = node.resolveBinding();
			collectBindings(node, typeBinding);
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

		public List getBindings() {
			return arrayList;
		}

	}

	
	public void setUpSuite() throws Exception {
		super.setUpSuite();
		this.ast = AST.newAST(AST.JLS3);
	}

	public ASTConverterBindingsTest(String name) {
		super(name);
	}

	public static Test suite() {
		return buildModelTestSuite(ASTConverterBindingsTest.class);		
	}

	public void test0001() throws JavaScriptModelException {
		checkBindingEqualityForProject("Converter");
		checkBindingEqualityForProject("Converter15");
	}

	/**
	 * @throws JavaScriptModelException
	 */
	private void checkBindingEqualityForProject(String projectName) throws JavaScriptModelException {
		IJavaScriptProject javaProject = getJavaProject(projectName);
		IPackageFragment[] packageFragments = javaProject.getPackageFragments();
		ArrayList compilationUnitscollector = new ArrayList();
		for (int j = 0, max2 = packageFragments.length; j < max2; j++) {
			IJavaScriptUnit[] units = packageFragments[j].getJavaScriptUnits();
			if (units != null) {
				for (int k = 0, max3 = units.length; k < max3; k++) {
					compilationUnitscollector.add(units[k]);
				}
			}
		}
		final int length = compilationUnitscollector.size();
		IJavaScriptUnit[] units = new IJavaScriptUnit[length];
		compilationUnitscollector.toArray(units);
		long totalTime = 0;
		for (int j = 0; j < length; j++) {
			IJavaScriptUnit currentUnit = units[j];
			ASTNode result = runConversion(AST.JLS3, currentUnit, true);
			assertEquals("Not a compilation unit", ASTNode.JAVASCRIPT_UNIT, result.getNodeType());
			JavaScriptUnit unit = (JavaScriptUnit) result;
			result = runConversion(AST.JLS3, currentUnit, true);
			assertEquals("Not a compilation unit", ASTNode.JAVASCRIPT_UNIT, result.getNodeType());
			if (DEBUG) {
				if (unit.types().size() > 0 ) {
					AbstractTypeDeclaration typeDeclaration = (AbstractTypeDeclaration) unit.types().get(0);
					StringBuffer buffer = new StringBuffer();
					PackageDeclaration packageDeclaration = unit.getPackage();
					if (packageDeclaration != null) {
						buffer.append(unit.getPackage().getName()).append(".").append(typeDeclaration.getName());
					} else {
						buffer.append(typeDeclaration.getName());
					}
					System.out.println(String.valueOf(buffer));
				} else {
					System.out.println(currentUnit.getElementName());
				}
			}
			JavaScriptUnit unit2 = (JavaScriptUnit) result;
			BindingsCollector collector = new BindingsCollector();
			unit.accept(collector);
			List bindings1 = collector.getBindings();
			BindingsCollector collector2 = new BindingsCollector();
			unit2.accept(collector2);
			List bindings2 = collector2.getBindings();
			assertEquals("Wrong size", bindings1.size(), bindings2.size());
			long time = System.currentTimeMillis();
			for (int i = 0, max = bindings1.size(); i < max; i++) {
				final Object object = bindings1.get(i);
				assertTrue("not a binding", object instanceof IBinding);
				final IBinding binding = (IBinding) object;
				final Object object2 = bindings2.get(i);
				assertTrue("not a binding", object2 instanceof IBinding);
				final IBinding binding2 = (IBinding) object2;
				final boolean equalTo = binding.isEqualTo(binding2);
				assertTrue("not equals", equalTo);
			}
			totalTime += (System.currentTimeMillis() - time);
		}
		if (DEBUG) {
			System.out.println("Total time = " + totalTime + "ms");
		}
	}
}
