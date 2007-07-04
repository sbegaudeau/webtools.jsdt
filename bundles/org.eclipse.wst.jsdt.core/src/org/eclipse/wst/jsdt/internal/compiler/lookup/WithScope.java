package org.eclipse.wst.jsdt.internal.compiler.lookup;

import org.eclipse.wst.jsdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemReporter;

public class WithScope extends BlockScope {
	public ReferenceBinding referenceContext;

	public WithScope( Scope parent, ReferenceBinding context) {
		super(WITH_SCOPE, parent);
		this.referenceContext=context;
	}



}
