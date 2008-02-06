package org.eclipse.wst.jsdt.core.infer;

import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;

public class DefaultInferrenceProvider implements InferrenceProvider {

	public static final String ID="org.eclipse.wst.jsdt.core.infer.DefaultInferrenceProvider";
	

	public int applysTo(CompilationUnitDeclaration scriptFile) {
		return InferrenceProvider.MAYBE_THIS;
	}

	public InferEngine getInferEngine() {
		 InferEngine engine = new InferEngine();
		 engine.inferenceProvider=this;
		 return engine;
	}
 


	public String getID() {
		return ID;
	}

}
