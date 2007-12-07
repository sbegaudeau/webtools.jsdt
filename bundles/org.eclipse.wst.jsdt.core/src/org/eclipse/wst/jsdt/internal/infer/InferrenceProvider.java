package org.eclipse.wst.jsdt.internal.infer;

import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;

public interface InferrenceProvider {
	public InferEngine getInferEngine();
	public boolean applysTo(CompilationUnitDeclaration scriptFile);
	public void initializeOptions(InferOptions options);
}
