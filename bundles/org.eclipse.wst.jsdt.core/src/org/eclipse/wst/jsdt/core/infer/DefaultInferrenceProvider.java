package org.eclipse.wst.jsdt.core.infer;

import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.env.ICompilationUnit;

public class DefaultInferrenceProvider implements InferrenceProvider {

	public static final String ID="org.eclipse.wst.jsdt.core.infer.DefaultInferrenceProvider";
	

	public int applysTo(CompilationUnitDeclaration scriptFile) {
		if (scriptFile.compilationResult.compilationUnit!=null)
		{
			ICompilationUnit compilationUnit = scriptFile.compilationResult.compilationUnit;
//			String inferenceID = compilationUnit.getInferenceID();
//			if (ID.equals(inferenceID))
//			  return InferrenceProvider.ONLY_THIS;
			String fileName = new String(compilationUnit.getFileName());
			if (fileName.indexOf("org.eclipse.wst.jsdt.core/libraries")>=0)
			{
				  return InferrenceProvider.ONLY_THIS;
			}
		}
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
