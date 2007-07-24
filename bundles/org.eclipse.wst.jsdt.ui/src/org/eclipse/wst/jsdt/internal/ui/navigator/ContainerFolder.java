/**
 * 
 */
package org.eclipse.wst.jsdt.internal.ui.navigator;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.runtime.Path;

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
