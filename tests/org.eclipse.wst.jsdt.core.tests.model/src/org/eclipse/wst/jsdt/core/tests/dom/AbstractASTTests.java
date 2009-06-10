/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.jsdt.core.IBuffer;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IProblemRequestor;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.WorkingCopyOwner;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.compiler.IProblem;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.ASTNode;
import org.eclipse.wst.jsdt.core.dom.ASTParser;
import org.eclipse.wst.jsdt.core.dom.ASTRequestor;
import org.eclipse.wst.jsdt.core.dom.ASTVisitor;
import org.eclipse.wst.jsdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.wst.jsdt.core.dom.ArrayType;
import org.eclipse.wst.jsdt.core.dom.Assignment;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.core.dom.IBinding;
import org.eclipse.wst.jsdt.core.dom.IFunctionBinding;
import org.eclipse.wst.jsdt.core.dom.ITypeBinding;
import org.eclipse.wst.jsdt.core.dom.IVariableBinding;
import org.eclipse.wst.jsdt.core.dom.FunctionDeclaration;
import org.eclipse.wst.jsdt.core.dom.FunctionInvocation;
import org.eclipse.wst.jsdt.core.dom.PackageDeclaration;
import org.eclipse.wst.jsdt.core.dom.QualifiedName;
import org.eclipse.wst.jsdt.core.dom.SimpleName;
import org.eclipse.wst.jsdt.core.dom.SimpleType;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;
import org.eclipse.wst.jsdt.core.dom.TypeDeclarationStatement;
import org.eclipse.wst.jsdt.core.tests.model.ModifyingResourceTests;
import org.eclipse.wst.jsdt.core.tests.util.Util;

public class AbstractASTTests extends ModifyingResourceTests {

	public AbstractASTTests(String name) {
		super(name);
	}

	/*
	 * Removes the *start* and *end* markers from the given source and remembers
	 * the positions.
	 */
	public class MarkerInfo {
		String path;
		String source;
		int[] astStarts, astEnds;

		public MarkerInfo(String source) {
			this(null, source);
		}

		public MarkerInfo(String path, String source) {
			this.path = path;
			this.source = source;

			int markerIndex = 1;
			while (source.indexOf("/*start" + markerIndex + "*/") != -1) {
				markerIndex++;
			}
			int astNumber = source.indexOf("/*start*/") != -1 ? markerIndex
					: markerIndex - 1;
			this.astStarts = new int[astNumber];
			this.astEnds = new int[astNumber];

			for (int i = 1; i < markerIndex; i++)
				setStartAndEnd(i);
			if (astNumber == markerIndex)
				setStartAndEnd(-1);
		}

		public int indexOfASTStart(int astStart) {
			for (int i = 0, length = this.astStarts.length; i < length; i++)
				if (this.astStarts[i] == astStart)
					return i;
			return -1;
		}

		private void removeMarkerFromSource(String marker, int sourceIndex,
				int astNumber) {
			char[] markerChars = marker.toCharArray();
			this.source = new String(CharOperation.replace(this.source
					.toCharArray(), markerChars, CharOperation.NO_CHAR));
			// shift previously recorded positions
			int markerLength = markerChars.length;
			for (int i = 0; i < astNumber; i++) {
				if (this.astStarts[i] > sourceIndex)
					this.astStarts[i] -= markerLength;
				if (this.astEnds[i] > sourceIndex)
					this.astEnds[i] -= markerLength;
			}
		}

		private void setStartAndEnd(int markerIndex) {
			String markerNumber;
			if (markerIndex == -1) {
				markerNumber = "";
				markerIndex = this.astStarts.length; // *start* is always last
			} else
				markerNumber = Integer.toString(markerIndex);

			String markerStart = "/*start" + markerNumber + "*/";
			String markerEnd = "/*end" + markerNumber + "*/";
			int astStart = source.indexOf(markerStart); // start of AST
														// inclusive
			this.astStarts[markerIndex - 1] = astStart;
			removeMarkerFromSource(markerStart, astStart, markerIndex - 1);
			int astEnd = this.source.indexOf(markerEnd); // end of AST exclusive
			this.astEnds[markerIndex - 1] = astEnd;
			removeMarkerFromSource(markerEnd, astEnd, markerIndex - 1);
		}

	}

	public class BindingRequestor extends ASTRequestor {
		HashMap bindings = new HashMap();

		public void acceptBinding(String bindingKey, IBinding binding) {
			this.bindings.put(bindingKey, binding);
		}

		public IBinding[] getBindings(String[] bindingKeys) {
			int length = this.bindings.size();
			IBinding[] result = new IBinding[length];
			for (int i = 0; i < length; i++) {
				result[i] = (IBinding) this.bindings.get(bindingKeys[i]);
			}
			return result;
		}
	}

	protected void assertASTNodeEquals(String expected, ASTNode node) {
		String actual = node.toString();
		if (!expected.equals(actual)) {
			System.out.println(displayString(actual, 3) + ",");
		}
		assertEquals("Unexpected ast node", expected, actual);
	}

	protected void assertASTNodesEqual(String expected, List nodes) {
		StringBuffer buffer = new StringBuffer();
		Iterator iterator = nodes.iterator();
		while (iterator.hasNext()) {
			ASTNode node = (ASTNode) iterator.next();
			buffer.append(node);
			if (node instanceof JavaScriptUnit) {
				IProblem[] problems = ((JavaScriptUnit) node).getProblems();
				if (problems != null) {
					for (int i = 0, length = problems.length; i < length; i++) {
						IProblem problem = problems[i];
						buffer.append('\n');
						buffer.append(problem);
					}
				}
			}
			buffer.append('\n');
		}
		String actual = buffer.toString();
		if (!expected.equals(actual)) {
			System.out.println(displayString(actual, 4) + ",");
		}
		assertEquals("Unexpected ast nodes", expected, actual);
	}

	protected void assertBindingKeyEquals(String expected, String actual) {
		assertBindingKeysEqual(expected, new String[] { actual });
	}

	protected void assertBindingKeysEqual(String expected, String[] actualKeys) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, length = actualKeys.length; i < length; i++) {
			if (i > 0)
				buffer.append('\n');
			buffer.append(actualKeys[i]);
		}
		String actual = buffer.toString();
		if (!expected.equals(actual)) {
			System.out.print(displayString(actual, 4));
			System.out.println(',');
		}
		assertEquals("Unexpected binding keys", expected, actual);
	}

	protected void assertBindingEquals(String expected, IBinding binding) {
		assertBindingsEqual(expected, new IBinding[] { binding });
	}

	protected void assertBindingsEqual(String expected,
			IBinding[] actualBindings) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, length = actualBindings.length; i < length; i++) {
			if (i > 0)
				buffer.append('\n');
			if (actualBindings[i] == null)
				buffer.append("<null>");
			else
				buffer.append(actualBindings[i].getKey());
		}
		String actual = buffer.toString();
		if (!expected.equals(actual)) {
			System.out.print(displayString(actual, 3));
			System.out.println(',');
		}
		assertEquals("Unexpected bindings", expected, actual);
	}

	/*
	 * Builds an AST from the info source (which is assumed to be the source
	 * attached to the given class file), and returns the AST node that was
	 * delimited by the astStart and astEnd of the marker info.
	 */
	protected ASTNode buildAST(MarkerInfo markerInfo, IClassFile classFile,
			boolean reportErrors) throws JavaScriptModelException {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(classFile);
		parser.setResolveBindings(true);
		JavaScriptUnit unit = (JavaScriptUnit) parser.createAST(null);

		if (reportErrors) {
			StringBuffer buffer = new StringBuffer();
			IProblem[] problems = unit.getProblems();
			for (int i = 0, length = problems.length; i < length; i++)
				Util.appendProblem(buffer, problems[i], markerInfo.source
						.toCharArray(), i + 1);
			if (buffer.length() > 0)
				System.err.println(buffer.toString());
		}

		return findNode(unit, markerInfo);
	}

	protected ASTNode buildAST(IJavaScriptUnit cu)
			throws JavaScriptModelException {
		return buildAST(null/* use existing contents */, cu, true/* report errors */);
	}

	protected ASTNode buildAST(String newContents, IJavaScriptUnit cu)
			throws JavaScriptModelException {
		return buildAST(newContents, cu, true/* report errors */);
	}

	protected ASTNode buildAST(MarkerInfo markerInfo, IClassFile classFile)
			throws JavaScriptModelException {
		return buildAST(markerInfo, classFile, true/* report errors */);
	}

	/*
	 * Removes the marker comments "*start*" and "*end*" from the given
	 * contents, builds an AST from the resulting source, and returns the AST
	 * node that was delimited by "*start*" and "*end*".
	 */
	protected ASTNode buildAST(String newContents, IJavaScriptUnit cu,
			boolean reportErrors) throws JavaScriptModelException {
		return buildAST(newContents, cu, reportErrors, false/*
															 * no statement
															 * recovery
															 */);
	}

	protected ASTNode buildAST(String newContents, IJavaScriptUnit cu,
			boolean reportErrors, boolean enableStatementRecovery)
			throws JavaScriptModelException {
		ASTNode[] nodes = buildASTs(newContents, cu, reportErrors,
				enableStatementRecovery);
		if (nodes.length == 0)
			return null;
		return nodes[0];
	}

	protected ASTNode[] buildASTs(String contents, IJavaScriptUnit cu)
			throws JavaScriptModelException {
		return buildASTs(contents, cu, true);
	}

	/*
	 * Removes the marker comments "*start?*" and "*end?*" from the given new
	 * contents (where ? is either empty or a number), or use the current
	 * contents if the given new contents is null. Builds an AST from the
	 * resulting source. For each of the pairs, returns the AST node that was
	 * delimited by "*start?*" and "*end?*".
	 */
	protected ASTNode[] buildASTs(String newContents, IJavaScriptUnit cu,
			boolean reportErrors) throws JavaScriptModelException {
		return buildASTs(newContents, cu, reportErrors, false);
	}

	protected ASTNode[] buildASTs(String newContents, IJavaScriptUnit cu,
			boolean reportErrors, boolean enableStatementRecovery)
			throws JavaScriptModelException {
		MarkerInfo markerInfo;
		if (newContents == null) {
			markerInfo = new MarkerInfo(cu.getSource());
		} else {
			markerInfo = new MarkerInfo(newContents);
		}
		newContents = markerInfo.source;

		JavaScriptUnit unit;
		if (cu.isWorkingCopy()) {
			cu.getBuffer().setContents(newContents);
			unit = cu.reconcile(AST.JLS3, reportErrors,
					enableStatementRecovery, null, null);
		} else {
			IBuffer buffer = cu.getBuffer();
			buffer.setContents(newContents);
			buffer.save(null, false);

			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(cu);
			parser.setResolveBindings(true);
			parser.setStatementsRecovery(enableStatementRecovery);
			unit = (JavaScriptUnit) parser.createAST(null);
		}

		if (reportErrors) {
			StringBuffer buffer = new StringBuffer();
			IProblem[] problems = unit.getProblems();
			for (int i = 0, length = problems.length; i < length; i++)
				Util.appendProblem(buffer, problems[i], newContents
						.toCharArray(), i + 1);
			if (buffer.length() > 0)
				System.err.println(buffer.toString());
		}

		ASTNode[] nodes = findNodes(unit, markerInfo);
		if (nodes.length == 0)
			return new ASTNode[] { unit };
		return nodes;
	}

	protected MarkerInfo[] createMarkerInfos(String[] pathAndSources) {
		MarkerInfo[] markerInfos = new MarkerInfo[pathAndSources.length / 2];
		int index = 0;
		for (int i = 0, length = pathAndSources.length; i < length; i++) {
			String path = pathAndSources[i];
			String source = pathAndSources[++i];
			markerInfos[index++] = new MarkerInfo(path, source);
		}
		return markerInfos;
	}

	protected IVariableBinding[] createVariableBindings(
			String[] pathAndSources, String[] bindingKeys)
			throws JavaScriptModelException {
		WorkingCopyOwner owner = new WorkingCopyOwner() {
		};
		this.workingCopies = createWorkingCopies(pathAndSources, owner);
		IBinding[] bindings = resolveBindings(bindingKeys, getJavaProject("P"),
				owner);
		int length = bindings.length;
		IVariableBinding[] result = new IVariableBinding[length];
		System.arraycopy(bindings, 0, result, 0, length);
		return result;
	}

	protected IFunctionBinding[] createMethodBindings(String[] pathAndSources,
			String[] bindingKeys) throws JavaScriptModelException {
		return createMethodBindings(pathAndSources, bindingKeys,
				getJavaProject("P"));
	}

	protected IFunctionBinding[] createMethodBindings(String[] pathAndSources,
			String[] bindingKeys, IJavaScriptProject project)
			throws JavaScriptModelException {
		WorkingCopyOwner owner = new WorkingCopyOwner() {
		};
		this.workingCopies = createWorkingCopies(pathAndSources, owner);
		IBinding[] bindings = resolveBindings(bindingKeys, project, owner);
		int length = bindings.length;
		IFunctionBinding[] result = new IFunctionBinding[length];
		System.arraycopy(bindings, 0, result, 0, length);
		return result;
	}

	protected ITypeBinding[] createTypeBindings(String[] pathAndSources,
			String[] bindingKeys) throws JavaScriptModelException {
		return createTypeBindings(pathAndSources, bindingKeys,
				getJavaProject("P"));
	}

	protected ITypeBinding[] createTypeBindings(String[] pathAndSources,
			String[] bindingKeys, IJavaScriptProject project)
			throws JavaScriptModelException {
		WorkingCopyOwner owner = new WorkingCopyOwner() {
		};
		this.workingCopies = createWorkingCopies(pathAndSources, owner);
		IBinding[] bindings = resolveBindings(bindingKeys, project, owner);
		int length = bindings.length;
		ITypeBinding[] result = new ITypeBinding[length];
		System.arraycopy(bindings, 0, result, 0, length);
		return result;
	}

	protected IJavaScriptUnit[] createWorkingCopies(String[] pathAndSources,
			WorkingCopyOwner owner) throws JavaScriptModelException {
		MarkerInfo[] markerInfos = createMarkerInfos(pathAndSources);
		return createWorkingCopies(markerInfos, owner);
	}

	protected IJavaScriptUnit[] createWorkingCopies(MarkerInfo[] markerInfos,
			WorkingCopyOwner owner) throws JavaScriptModelException {
		return createWorkingCopies(markerInfos, owner, null);
	}

	protected IJavaScriptUnit[] createWorkingCopies(MarkerInfo[] markerInfos,
			WorkingCopyOwner owner, IProblemRequestor problemRequestor)
			throws JavaScriptModelException {
		int length = markerInfos.length;
		IJavaScriptUnit[] copies = new IJavaScriptUnit[length];
		for (int i = 0; i < length; i++) {
			MarkerInfo markerInfo = markerInfos[i];
			IJavaScriptUnit workingCopy = getCompilationUnit(markerInfo.path)
					.getWorkingCopy(owner, null);
			workingCopy.getBuffer().setContents(markerInfo.source);
			workingCopy.makeConsistent(null);
			copies[i] = workingCopy;
		}
		return copies;
	}

	protected ASTNode findNode(JavaScriptUnit unit, final MarkerInfo markerInfo) {
		ASTNode[] nodes = findNodes(unit, markerInfo);
		if (nodes.length == 0)
			return unit;
		return nodes[0];
	}

	protected ASTNode[] findNodes(JavaScriptUnit unit,
			final MarkerInfo markerInfo) {
		class Visitor extends ASTVisitor {
			ArrayList found = new ArrayList();

			public void preVisit(ASTNode node) {
				if (node instanceof JavaScriptUnit)
					return;
				int index = markerInfo.indexOfASTStart(node.getStartPosition());
				if (index != -1
						&& node.getStartPosition() + node.getLength() == markerInfo.astEnds[index]) {
					this.found.add(node);
					markerInfo.astStarts[index] = -1; // so that 2 nodes with
														// the same start and
														// end will not be found
				}
			}
		}
		Visitor visitor = new Visitor();
		unit.accept(visitor);
		int size = visitor.found.size();
		ASTNode[] result = new ASTNode[size];
		visitor.found.toArray(result);
		return result;
	}

	protected void resolveASTs(IJavaScriptUnit[] cus, String[] bindingKeys,
			ASTRequestor requestor, IJavaScriptProject project,
			WorkingCopyOwner owner) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(true);
		parser.setProject(project);
		parser.setWorkingCopyOwner(owner);
		parser.createASTs(cus, bindingKeys, requestor, null);
	}

	protected IBinding resolveBinding(ASTNode node) {
		switch (node.getNodeType()) {
		case ASTNode.PACKAGE_DECLARATION:
			return ((PackageDeclaration) node).resolveBinding();
		case ASTNode.TYPE_DECLARATION:
			return ((TypeDeclaration) node).resolveBinding();
		case ASTNode.ANONYMOUS_CLASS_DECLARATION:
			return ((AnonymousClassDeclaration) node).resolveBinding();
		case ASTNode.TYPE_DECLARATION_STATEMENT:
			return ((TypeDeclarationStatement) node).resolveBinding();
		case ASTNode.FUNCTION_DECLARATION:
			return ((FunctionDeclaration) node).resolveBinding();
		case ASTNode.FUNCTION_INVOCATION:
			return ((FunctionInvocation) node).resolveMethodBinding();
		case ASTNode.SIMPLE_NAME:
			return ((SimpleName) node).resolveBinding();
		case ASTNode.ARRAY_TYPE:
			return ((ArrayType) node).resolveBinding();
		case ASTNode.ASSIGNMENT:
			return ((Assignment) node).getRightHandSide().resolveTypeBinding();
		case ASTNode.SIMPLE_TYPE:
			return ((SimpleType) node).resolveBinding();
		case ASTNode.QUALIFIED_NAME:
			return ((QualifiedName) node).resolveBinding();
		default:
			throw new Error("Not yet implemented for this type of node: "
					+ node);
		}
	}

	protected IBinding[] resolveBindings(String[] bindingKeys,
			IJavaScriptProject project, WorkingCopyOwner owner) {
		BindingRequestor requestor = new BindingRequestor();
		resolveASTs(new IJavaScriptUnit[0], bindingKeys, requestor, project,
				owner);
		return requestor.getBindings(bindingKeys);
	}

	/*
	 * Resolve the bindings of the nodes marked with *start?* and *end?*.
	 */
	protected IBinding[] resolveBindings(String contents, IJavaScriptUnit cu)
			throws JavaScriptModelException {
		return resolveBindings(contents, cu, true/* report errors */);
	}

	/*
	 * Resolve the bindings of the nodes marked with *start?* and *end?*.
	 */
	protected IBinding[] resolveBindings(String contents, IJavaScriptUnit cu,
			boolean reportErrors) throws JavaScriptModelException {
		ASTNode[] nodes = buildASTs(contents, cu, reportErrors);
		if (nodes == null)
			return null;
		int length = nodes.length;
		IBinding[] result = new IBinding[length];
		for (int i = 0; i < length; i++) {
			result[i] = resolveBinding(nodes[i]);
		}
		return result;
	}

	// protected void tearDown() throws Exception {
	// discardWorkingCopies(this.workingCopies);
	// this.workingCopies = null;
	// }

}
