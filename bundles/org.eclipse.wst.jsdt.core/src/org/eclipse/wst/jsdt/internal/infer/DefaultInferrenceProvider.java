package org.eclipse.wst.jsdt.internal.infer;

import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;

public class DefaultInferrenceProvider implements InferrenceProvider {

	public boolean applysTo(CompilationUnitDeclaration scriptFile) {
		return true;
	}

	public InferEngine getInferEngine() {
		return new InferEngine();
	}

	public void initializeOptions(InferOptions options) {

	}

}
