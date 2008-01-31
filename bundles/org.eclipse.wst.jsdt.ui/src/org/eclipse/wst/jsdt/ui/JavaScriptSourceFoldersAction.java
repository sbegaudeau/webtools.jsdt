/**
 * 
 */
package org.eclipse.wst.jsdt.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.wst.jsdt.internal.ui.preferences.BuildPathsPropertyPage;

/**
 * @author childsb
 *
 */
public class JavaScriptSourceFoldersAction extends JavaScriptLibrariesAction {

	private static final int BUILD_PATH_PAGE_INDEX = 2;
	
	public void run(IAction arg0) {
		Map data = new HashMap();
		data.put(BuildPathsPropertyPage.DATA_PAGE_INDEX, new Integer(BUILD_PATH_PAGE_INDEX));
		String ID = arg0.getId();
		String propertyPage = (String)PROPS_TO_IDS.get(ID);
		
		PreferencesUtil.createPropertyDialogOn(getShell(), project, propertyPage , null, data).open();
	}
	
	
}
