/**
 * 
 */
package org.eclipse.wst.jsdt.internal.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.jsdt.core.UnimplementedException;
import org.eclipse.wst.jsdt.ui.project.JsNature;

/**
 * @author childsb
 *
 */
public class SetupProjectsWizzard implements IObjectActionDelegate, IActionDelegate {
	
	Object[] fTarget;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		throw new UnimplementedException("setActivePart(IAction action, IWorkbenchPart targetPart)");
	}

	public void run(IAction action) {
		if(fTarget==null) return;
		
		for(int i=0;i<fTarget.length;i++) {
			if(fTarget[i] instanceof IProject) {
				IProject project = (IProject)fTarget[i];
				
					if(!JsNature.hasNature(project)) {
						JsNature nature = new JsNature(project,null);
						try {
							nature.configure();
						} catch (CoreException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
					}
				
			}
		}
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		
		if(selection instanceof StructuredSelection) {
			fTarget = ((StructuredSelection)selection).toArray();
		}else {
			fTarget = null;
		}
	}
	

}