package org.eclipse.wst.jsdt.core.infer;


public class DefaultInferrenceProvider implements InferrenceProvider {

	public static final String ID="org.eclipse.wst.jsdt.core.infer.DefaultInferrenceProvider";
	

	public int applysTo(IInferenceFile scriptFile) {
		char[] fileNameChars = scriptFile.getFileName();
		if (fileNameChars!=null)
		{
//			String inferenceID = compilationUnit.getInferenceID();
//			if (ID.equals(inferenceID))
//			  return InferrenceProvider.ONLY_THIS;

			String fileName = new String(fileNameChars);
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


	public ResolutionConfiguration getResolutionConfiguration() {
		return new ResolutionConfiguration();
	}

}
