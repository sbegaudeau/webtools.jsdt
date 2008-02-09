/**
 * 
 */
package org.eclipse.wst.jsdt.core.ast;




/**
 * @author childsb
 *
 */
public interface IScriptFileDeclaration extends IASTNode{

	IProgramElement []getStatements();
	
	/**
	 * get the filename for the script if it can be determined
	 * @return the scripts file name,  this could be null
	 */
	char[] getFileName();
	
	/**
	 * get the inference ID for the script if it is located in a containter that specified an Inference ID
	 * @return the inference ID for the script, could be null 
	 */
	String getInferenceID();
}