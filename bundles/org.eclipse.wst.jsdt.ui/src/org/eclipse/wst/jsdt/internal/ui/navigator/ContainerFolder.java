/**
 * 
 */
package org.eclipse.wst.jsdt.internal.ui.navigator;


/**
 * @author childsb
 *
 */
public class ContainerFolder {
	

	
	
		Object parent;
		String name;
		public ContainerFolder(String fullPath, Object parent){

			this.parent = parent;
			name = fullPath;
		}
		
		public Object getParentObject() {
			return parent;
		}
		
		public String getName() {
			return name;
			
		}
		public String toString() { return name;}
	
}
