/*******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
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

import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.wst.jsdt.core.dom.ArrayAccess;
import org.eclipse.wst.jsdt.core.dom.ArrayCreation;
import org.eclipse.wst.jsdt.core.dom.ArrayInitializer;
import org.eclipse.wst.jsdt.core.dom.ArrayType;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.Block;
import org.eclipse.wst.jsdt.core.dom.BooleanLiteral;
import org.eclipse.wst.jsdt.core.dom.BreakStatement;
import org.eclipse.wst.jsdt.core.dom.CatchClause;
import org.eclipse.wst.jsdt.core.dom.CharacterLiteral;
import org.eclipse.wst.jsdt.core.dom.ClassInstanceCreation;
import org.eclipse.wst.jsdt.core.dom.ConditionalExpression;
import org.eclipse.wst.jsdt.core.dom.ConstructorInvocation;
import org.eclipse.wst.jsdt.core.dom.ContinueStatement;
import org.eclipse.wst.jsdt.core.dom.DoStatement;
import org.eclipse.wst.jsdt.core.dom.EmptyStatement;
import org.eclipse.wst.jsdt.core.dom.EnhancedForStatement;
import org.eclipse.wst.jsdt.core.dom.ExpressionStatement;
import org.eclipse.wst.jsdt.core.dom.FieldAccess;
import org.eclipse.wst.jsdt.core.dom.FieldDeclaration;
import org.eclipse.wst.jsdt.core.dom.ForStatement;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.FunctionRef;
import org.eclipse.wst.jsdt.core.dom.IfStatement;
import org.eclipse.wst.jsdt.core.dom.ImportDeclaration;
import org.eclipse.wst.jsdt.core.dom.InfixExpression;
import org.eclipse.wst.jsdt.core.dom.Initializer;
import org.eclipse.wst.jsdt.core.dom.InstanceofExpression;
import org.eclipse.wst.jsdt.core.dom.JSdoc;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.LabeledStatement;
import org.eclipse.wst.jsdt.core.dom.Name;
import org.eclipse.wst.jsdt.core.dom.NullLiteral;
import org.eclipse.wst.jsdt.core.dom.NumberLiteral;
import org.eclipse.wst.jsdt.core.dom.PackageDeclaration;
import org.eclipse.wst.jsdt.core.dom.ParenthesizedExpression;
import org.eclipse.wst.jsdt.core.dom.PostfixExpression;
import org.eclipse.wst.jsdt.core.dom.PrefixExpression;
import org.eclipse.wst.jsdt.core.dom.PrimitiveType;
import org.eclipse.wst.jsdt.core.dom.QualifiedName;
import org.eclipse.wst.jsdt.core.dom.QualifiedType;
import org.eclipse.wst.jsdt.core.dom.ReturnStatement;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SimpleType;
import org.eclipse.wst.jsdt.core.dom.SingleVariableDeclaration;
import org.eclipse.wst.jsdt.core.dom.StringLiteral;
import org.eclipse.wst.jsdt.core.dom.SuperConstructorInvocation;
import org.eclipse.wst.jsdt.core.dom.SuperFieldAccess;
import org.eclipse.wst.jsdt.core.dom.SuperMethodInvocation;
import org.eclipse.wst.jsdt.core.dom.SwitchCase;
import org.eclipse.wst.jsdt.core.dom.SwitchStatement;
import org.eclipse.wst.jsdt.core.dom.TagElement;
import org.eclipse.wst.jsdt.core.dom.ThisExpression;
import org.eclipse.wst.jsdt.core.dom.ThrowStatement;
import org.eclipse.wst.jsdt.core.dom.TryStatement;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.TypeLiteral;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationExpression;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationFragment;
import org.eclipse.wst.jsdt.core.dom.VariableDeclarationStatement;
import org.eclipse.wst.jsdt.core.dom.WhileStatement;

public class SampleASTs {
	/**
	 * Returns a subtree of sample of AST nodes. The sample includes
	 * one of each kind (except for BlockComment and LineComment,
     * which cannot be connected directly to a JavaScriptUnit),
     * but otherwise does not make sense.
	 */
	public static ASTNode oneOfEach(AST target) {
		JavaScriptUnit cu = target.newJavaScriptUnit();
		PackageDeclaration pd = target.newPackageDeclaration();
		cu.setPackage(pd);
		
		ImportDeclaration im = target.newImportDeclaration();
		cu.imports().add(im);
		
		TypeDeclaration td = target.newTypeDeclaration();
		cu.types().add(td);
		JSdoc javadoc = target.newJSdoc();
		td.setJavadoc(javadoc);
		TagElement tg = target.newTagElement();
		javadoc.tags().add(tg);
		tg.fragments().add(target.newTextElement());
		tg.fragments().add(target.newMemberRef());
		FunctionRef mr = target.newFunctionRef();
		tg.fragments().add(mr);
		mr.parameters().add(target.newFunctionRefParameter());
				
		VariableDeclarationFragment variableDeclarationFragment = target.newVariableDeclarationFragment();
		FieldDeclaration fd = 
			target.newFieldDeclaration(variableDeclarationFragment);
		td.bodyDeclarations().add(fd);	
		
		Initializer in = target.newInitializer();
		td.bodyDeclarations().add(in);	

		
		FunctionDeclaration md = target.newFunctionDeclaration();
		SingleVariableDeclaration singleVariableDeclaration = target.newSingleVariableDeclaration();
		md.parameters().add(singleVariableDeclaration);
		td.bodyDeclarations().add(md);
		
		SimpleName sn1 = target.newSimpleName("one"); //$NON-NLS-1$
		SimpleName sn2 =target.newSimpleName("two"); //$NON-NLS-1$
		QualifiedName qn = target.newQualifiedName(sn1, sn2);
		
		PrimitiveType pt = target.newPrimitiveType(PrimitiveType.INT);
		ArrayType at = target.newArrayType(pt);
		fd.setType(at);

		if (target.apiLevel() >= AST.JLS3) {
			SimpleType st = target.newSimpleType(qn);
			QualifiedType qt = target.newQualifiedType(st, target.newSimpleName("x")); //$NON-NLS-1$
		}
		
		Block b = target.newBlock();
		md.setBody(b);
		
		// all statements (in alphabetic order of statement type)
		Block block = target.newBlock();
		b.statements().add(block);
		BreakStatement breakStatement = target.newBreakStatement();
		b.statements().add(breakStatement);
		ContinueStatement continueStatement = target.newContinueStatement();
		b.statements().add(continueStatement);
		ConstructorInvocation constructorInvocation = target.newConstructorInvocation();
		b.statements().add(constructorInvocation);
		DoStatement doStatement = target.newDoStatement();
		b.statements().add(doStatement);
		EmptyStatement emptyStatement = target.newEmptyStatement();
		b.statements().add(emptyStatement);
		NullLiteral nullLiteral = target.newNullLiteral();
		ExpressionStatement expressionStatement = target.newExpressionStatement(nullLiteral);
		b.statements().add(expressionStatement);
		ForStatement forStatement = target.newForStatement();
		b.statements().add(forStatement);
		if (target.apiLevel() >= AST.JLS3) {
			EnhancedForStatement foreachStatement = target.newEnhancedForStatement();
			b.statements().add(foreachStatement);
		}
		IfStatement ifStatement = target.newIfStatement();
		b.statements().add(ifStatement);
		LabeledStatement labeledStatement = target.newLabeledStatement();
		b.statements().add(labeledStatement);
		ReturnStatement returnStatement = target.newReturnStatement();
		b.statements().add(returnStatement);
		SuperConstructorInvocation superConstructorInvocation = target.newSuperConstructorInvocation();
		b.statements().add(superConstructorInvocation);
		SwitchStatement ss = target.newSwitchStatement();
		SwitchCase switchCase = target.newSwitchCase();
		ss.statements().add(switchCase);
		b.statements().add(ss);
		SwitchStatement switchStatement = target.newSwitchStatement();
		b.statements().add(switchStatement);
		SwitchCase switchCase2 = target.newSwitchCase();
		b.statements().add(switchCase2);
		ThrowStatement throwStatement = target.newThrowStatement();
		b.statements().add(throwStatement);
		TryStatement tr = target.newTryStatement();
		CatchClause catchClause = target.newCatchClause();
		tr.catchClauses().add(catchClause);
		b.statements().add(tr);
		
		TypeDeclaration typeDeclaration = target.newTypeDeclaration();
		TypeDeclarationStatement typeDeclarationStatement = target.newTypeDeclarationStatement(typeDeclaration);
		b.statements().add(typeDeclarationStatement);
		VariableDeclarationFragment variableDeclarationFragment2 = target.newVariableDeclarationFragment();
		VariableDeclarationStatement variableDeclarationStatement = target.newVariableDeclarationStatement(variableDeclarationFragment2);
		b.statements().add(variableDeclarationStatement);
		WhileStatement whileStatement = target.newWhileStatement();
		b.statements().add(whileStatement);

		// all expressions (in alphabetic order of expressions type)
		FunctionInvocation inv = target.newFunctionInvocation();
		ExpressionStatement expressionStatement2 = target.newExpressionStatement(inv);
		b.statements().add(expressionStatement2);
		List z = inv.arguments();
		ArrayAccess arrayAccess = target.newArrayAccess();
		z.add(arrayAccess);
		ArrayCreation arrayCreation = target.newArrayCreation();
		z.add(arrayCreation);
		ArrayInitializer arrayInitializer = target.newArrayInitializer();
		z.add(arrayInitializer);
		Assignment assignment = target.newAssignment();
		z.add(assignment);
		BooleanLiteral booleanLiteral = target.newBooleanLiteral(true);
		z.add(booleanLiteral);
		CharacterLiteral characterLiteral = target.newCharacterLiteral();
		z.add(characterLiteral);
		ClassInstanceCreation cic = target.newClassInstanceCreation();
		AnonymousClassDeclaration anonymousClassDeclaration = target.newAnonymousClassDeclaration();
		cic.setAnonymousClassDeclaration(anonymousClassDeclaration);
		z.add(cic);
		ConditionalExpression conditionalExpression = target.newConditionalExpression();
		z.add(conditionalExpression);
		FieldAccess fieldAccess = target.newFieldAccess();
		z.add(fieldAccess);
		InfixExpression infixExpression = target.newInfixExpression();
		z.add(infixExpression);
		InstanceofExpression instanceofExpression = target.newInstanceofExpression();
		z.add(instanceofExpression);
		FunctionInvocation methodInvocation = target.newFunctionInvocation();
		z.add(methodInvocation);
		Name name = target.newName(new String[]{"a", "b"}); //$NON-NLS-1$ //$NON-NLS-2$
		z.add(name);
		NullLiteral nullLiteral2 = target.newNullLiteral();
		z.add(nullLiteral2);
		NumberLiteral numberLiteral = target.newNumberLiteral("1024"); //$NON-NLS-1$
		z.add(numberLiteral);
		ParenthesizedExpression parenthesizedExpression = target.newParenthesizedExpression();
		z.add(parenthesizedExpression);
		PostfixExpression postfixExpression = target.newPostfixExpression();
		z.add(postfixExpression);
		PrefixExpression prefixExpression = target.newPrefixExpression();
		z.add(prefixExpression);
		StringLiteral stringLiteral = target.newStringLiteral();
		z.add(stringLiteral);
		SuperFieldAccess superFieldAccess = target.newSuperFieldAccess();
		z.add(superFieldAccess);
		SuperMethodInvocation superMethodInvocation = target.newSuperMethodInvocation();
		z.add(superMethodInvocation);
		ThisExpression thisExpression = target.newThisExpression();
		z.add(thisExpression);
		TypeLiteral typeLiteral = target.newTypeLiteral();
		z.add(typeLiteral);
		VariableDeclarationFragment variableDeclarationFragment3 = target.newVariableDeclarationFragment();
		VariableDeclarationExpression variableDeclarationExpression = target.newVariableDeclarationExpression(variableDeclarationFragment3);
		z.add(variableDeclarationExpression);
		

		return cu;
	}
	
	/**
	 * Returns a flat list of sample nodes of each type.
	 * The sample includes one of each kind, including
	 * BlockComment and LineComment.
	 */
	public static List oneOfEachList(AST target) {
		List result = new ArrayList(100);
		for (int nodeType = 0; nodeType < 100; nodeType++) {
			Class nodeClass = null;
			try {
				nodeClass = ASTNode.nodeClassForType(nodeType);
			} catch (RuntimeException e) {
				// oops - guess that's not valid
			}
			if (nodeClass != null) {
				result.add(target.createInstance(nodeClass));
			}
		}
		return result;
	}

}
