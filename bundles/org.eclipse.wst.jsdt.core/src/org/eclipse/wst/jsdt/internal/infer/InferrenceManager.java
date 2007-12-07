package org.eclipse.wst.jsdt.internal.infer;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.core.JavaCore;

public class InferrenceManager {

	public static final String EXTENSION_POINT= "inferrenceSupport"; //$NON-NLS-1$

	protected static final String TAG_INFERENGINE = "inferenceEngine"; //$NON-NLS-1$
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


	protected void loadInferenceExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		ArrayList extList = new ArrayList();
		if (registry != null) {
			IExtensionPoint point = registry.getExtensionPoint(
					JavaCore.PLUGIN_ID, EXTENSION_POINT);

			if (point != null) {
				IExtension[] extensions = point.getExtensions();
				for (int i = 0; i < extensions.length; i++) {
					IConfigurationElement[] elements = extensions[i]
							.getConfigurationElements();
					for (int j = 0; j < elements.length; j++) {
						try {
							InferrenceProvider inferProvider = null;
							if (elements[j].getName().equals(TAG_INFERENGINE)) {
								inferProvider = (InferrenceProvider) elements[j]
										.createExecutableExtension(ATTR_INFERENGINE_CLASS);
							}
							InferrenceSupportExtension inferenceSupport = new InferrenceSupportExtension();
							inferenceSupport.inferProvider = inferProvider;

							extList.add(inferenceSupport);
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		this.extensions = (InferrenceSupportExtension[]) extList
				.toArray(new InferrenceSupportExtension[extList.size()]);
	}


}
