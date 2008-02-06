package org.eclipse.wst.jsdt.core.infer;

import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;

public interface InferrenceProvider {
	
	public static final int ONLY_THIS = 1;
	public static final int NOT_THIS = 2;
	public static final int MAYBE_THIS = 3;
	
	public InferEngine getInferEngine();
	public int applysTo(CompilationUnitDeclaration scriptFile);
	public String getID();
	
}
