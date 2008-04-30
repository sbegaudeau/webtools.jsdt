package org.eclipse.wst.jsdt.core.infer;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.wst.jsdt.internal.core.util.Util;

public class InferrenceManager {

	public static final String EXTENSION_POINT= "inferrenceSupport"; //$NON-NLS-1$

	protected static final String TAG_INFERENCE_PROVIDER = "inferenceProvider"; //$NON-NLS-1$
	protected static final String ATTR_INFERENGINE_CLASS = "class"; //$NON-NLS-1$


	private static InferrenceManager instance = null;


	private  InferrenceSupportExtension [] extensions;

	public static InferrenceManager getInstance(){
		if( instance == null )
			instance = new InferrenceManager();

		return instance;
	}



	public InferrenceProvider [] getInferenceProviders()
	{

		if (extensions==null)
		{
			loadInferenceExtensions();
		}
		ArrayList extProviders=new ArrayList();
		extProviders.add(new DefaultInferrenceProvider());
		for (int i = 0; i < extensions.length; i++) {
			  if (extensions[i].inferProvider!=null)
				  extProviders.add(extensions[i].inferProvider);
			}
		return (InferrenceProvider [] )extProviders.toArray(new InferrenceProvider[extProviders.size()]);
	}


	public InferrenceProvider [] getInferenceProviders(IInferenceFile script)
	{
		InferrenceProvider[] inferenceProviders = getInferenceProviders();
		ArrayList extProviders=new ArrayList();
		for (int i = 0; i < inferenceProviders.length; i++) {
			    int applies = inferenceProviders[i].applysTo(script);
			    switch (applies) {
				case InferrenceProvider.MAYBE_THIS:
					  extProviders.add(inferenceProviders[i]);
					break;

				case InferrenceProvider.ONLY_THIS:
					InferrenceProvider [] thisProvider = {inferenceProviders[i]};
					return thisProvider;


				default:
					break;
				}
			}
		return (InferrenceProvider [] )extProviders.toArray(new InferrenceProvider[extProviders.size()]);
	}


	

	public InferEngine [] getInferenceEngines(CompilationUnitDeclaration script)
	{
		InferrenceProvider[] inferenceProviders = getInferenceProviders();
		if (inferenceProviders.length==1)
			  return getSingleEngine(inferenceProviders[0]);
			
		ArrayList extEngines=new ArrayList();
		for (int i = 0; i < inferenceProviders.length; i++) {
			    if (script.compilationResult!=null && script.compilationResult.compilationUnit!=null)
			    {
			    	String inferenceID = script.compilationResult.compilationUnit.getInferenceID();
			    	if (inferenceProviders[i].getID().equals(inferenceID))
			    	{
						  return getSingleEngine(inferenceProviders[i]);
			    	}
			    }
			    int applies = inferenceProviders[i].applysTo(script);
			    switch (applies) {
				case InferrenceProvider.MAYBE_THIS:
					  InferEngine eng=inferenceProviders[i].getInferEngine();
					  eng.appliesTo=InferrenceProvider.MAYBE_THIS;
					  eng.inferenceProvider=inferenceProviders[i];
					  extEngines.add(eng);
					break;

				case InferrenceProvider.ONLY_THIS:
					  return getSingleEngine(inferenceProviders[i]);


				default:
					break;
				}
			}
		return (InferEngine [] )extEngines.toArray(new InferEngine[extEngines.size()]);
	}

	
	private InferEngine [] getSingleEngine(InferrenceProvider provider)
	{
		  InferEngine engine=provider.getInferEngine();
		  engine.appliesTo=InferrenceProvider.ONLY_THIS;
		  engine.inferenceProvider=provider;
		  InferEngine [] thisEngine = {engine};
		  return thisEngine;
	}
	
	
	protected void loadInferenceExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		ArrayList extList = new ArrayList();
		if (registry != null) {
			IExtensionPoint point = registry.getExtensionPoint(
					JavaScriptCore.PLUGIN_ID, EXTENSION_POINT);

			if (point != null) {
				IExtension[] extensions = point.getExtensions();
				for (int i = 0; i < extensions.length; i++) {
					IConfigurationElement[] elements = extensions[i]
							.getConfigurationElements();
					for (int j = 0; j < elements.length; j++) {
						try {
							InferrenceProvider inferProvider = null;
							if (elements[j].getName().equals(TAG_INFERENCE_PROVIDER)) {
								inferProvider = (InferrenceProvider) elements[j]
										.createExecutableExtension(ATTR_INFERENGINE_CLASS);
							}
							InferrenceSupportExtension inferenceSupport = new InferrenceSupportExtension();
							inferenceSupport.inferProvider = inferProvider;

							extList.add(inferenceSupport);
						} catch (CoreException e) {
							Util.log(e, "Error in loading inference extension");
						}
					}
				}
			}
		}

		this.extensions = (InferrenceSupportExtension[]) extList
				.toArray(new InferrenceSupportExtension[extList.size()]);
	}


}
