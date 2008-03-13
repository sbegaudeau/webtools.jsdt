package org.eclipse.wst.jsdt.core.infer;

/**
 *  This class provides configuration information on the inferred class
 *  gets resolved
 *
 */
public class ResolutionConfiguration {

	
	/**
	 * Get the default list of files to be looked at when resolving
	 * a name 
	 * @return a list of files, relative to the project, 
	 */
	public String [] getContextIncludes()
	{
		return null;
	}
	
	/**
	 * Determine if all files in include path should be searched to resolve a name.
	 * If false, names will be resolved using only librarys, imports, and context includes 
	 * @return true 
	 * 
	 */
	public boolean searchAllFiles()
	{
		return true;
	}
	
}
