package org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths;



import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPage;
import org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPageExtension;
import org.eclipse.wst.jsdt.ui.wizards.IJsGlobalScopeContainerPageExtension2;
import org.eclipse.wst.jsdt.ui.wizards.NewElementWizardPage;



/**
 *
 */
public class InternetExplorerLibraryWizardPage extends NewElementWizardPage implements IJsGlobalScopeContainerPage, IJsGlobalScopeContainerPageExtension, IJsGlobalScopeContainerPageExtension2  {

	private static final String LIBRARY_FILE_NAME = "InternetExplorer.js";
	private static final String CONTAINER_ID="org.eclipse.wst.jsdt.launching.InternetExplorer";

	public InternetExplorerLibraryWizardPage() {
		super("InternetExplorerBrowserLib");
	}

	public boolean finish() {
		return true;
	}

	public IIncludePathEntry getSelection() {
		// TODO Auto-generated method stub
		System.out.println("Unimplemented method:BaseLibraryWizardPage.getSelection");
		return null;
	}

	public void setSelection(IIncludePathEntry containerEntry) {}

	public void createControl(Composite parent) {
		Composite composite= new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		DialogField field = new DialogField();

		//field.createEmptySpace(parent);
		field.setLabelText("Internet Explorer Browser (5.0) Library added to Project.\n\n  - This library supports JavaScript elements provided by Microsoft's Internet Explorer web browser.");
		//field.setText("Default Browser Library added to project");
		LayoutUtil.doDefaultLayout(composite, new DialogField[] {field }, false, SWT.DEFAULT, SWT.DEFAULT);
		//LayoutUtil.setHorizontalGrabbing(fLibrarySelector.getListControl(null));
		Dialog.applyDialogFont(composite);
		setControl(composite);
		setDescription("Internet Explorer Browser Support");

	}

	public void initialize(IJavaScriptProject project, IIncludePathEntry[] currentEntries) {

	}

	public IIncludePathEntry[] getNewContainers() {
			IIncludePathEntry library = JavaScriptCore.newContainerEntry( new Path(CONTAINER_ID));
			return new IIncludePathEntry[] {library};
	}

}
