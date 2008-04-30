/**
 * 
 */
package org.eclipse.wst.jsdt.ui;

import java.util.Hashtable;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.preferences.BuildPathsPropertyPage;
import org.eclipse.wst.jsdt.internal.ui.preferences.CodeStylePreferencePage;


/**
 * @author childsb
 *
 */
public class JavaScriptLibrariesAction implements IObjectActionDelegate {

	private IWorkbenchSite fSite;
	protected IJavaScriptProject project;
	protected static final Hashtable PROPS_TO_IDS = new Hashtable();
	
	{
		PROPS_TO_IDS.put("org.eclipse.wst.jsdt.internal.ui.configure.scope",  BuildPathsPropertyPage.PROP_ID); //$NON-NLS-1$
		PROPS_TO_IDS.put("org.eclipse.wst.jsdt.internal.ui.configure.javascript.properties",  CodeStylePreferencePage.PROP_ID); //$NON-NLS-1$
		PROPS_TO_IDS.put("org.eclipse.wst.jsdt.internal.ui.configure.source.folders", BuildPathsPropertyPage.PROP_ID); //$NON-NLS-1$
		//PROPS_TO_IDS.put("",  BuildPathsPropertyPage.PROP_ID);
		//PROPS_TO_IDS.put("",  BuildPathsPropertyPage.PROP_ID);
		
		
		
	}
	

	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		fSite = arg1.getSite();

	}


	public void run(IAction arg0) {
		Object data = null;
		String ID = arg0.getId();
		String propertyPage = (String)PROPS_TO_IDS.get(ID);
		
		PreferencesUtil.createPropertyDialogOn(getShell(), project, propertyPage , null, data).open();
		

	}


	public void selectionChanged(IAction arg0, ISelection arg1) {
			if(arg1 instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection)arg1;
				Object item = selection.getFirstElement();
				
				if(item instanceof ProjectLibraryRoot) {
					ProjectLibraryRoot root = ((ProjectLibraryRoot)item);
					project = root.getProject();
				}
			}

	}
	
	protected Shell getShell() {
		if (fSite == null)
			return JavaScriptPlugin.getActiveWorkbenchShell();
		
	    return fSite.getShell() != null ? fSite.getShell() : JavaScriptPlugin.getActiveWorkbenchShell();
    }

}
