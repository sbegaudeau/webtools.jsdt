/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.ui.wizards;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.wst.jsdt.internal.corext.util.Messages;
import org.eclipse.wst.jsdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.preferences.CompliancePreferencePage;
import org.eclipse.wst.jsdt.internal.ui.preferences.NewJavaProjectPreferencePage;
import org.eclipse.wst.jsdt.internal.ui.preferences.PropertyAndPreferencePage;
import org.eclipse.wst.jsdt.internal.ui.wizards.buildpaths.BuildPathSupport;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.wst.jsdt.internal.ui.workingsets.JavaWorkingSetUpdater;
import org.eclipse.wst.jsdt.internal.ui.workingsets.WorkingSetConfigurationBlock;
import org.eclipse.wst.jsdt.launching.JavaRuntime;
import org.eclipse.wst.jsdt.ui.JavaScriptUI;
import org.eclipse.wst.jsdt.ui.PreferenceConstants;

/**
 * The first page of the <code>SimpleProjectWizard</code>.
 */
public class JavaProjectWizardFirstPage extends WizardPage {
	
	/**
	 * Request a project name. Fires an event whenever the text field is
	 * changed, regardless of its content.
	 */
	private final class NameGroup extends Observable implements IDialogFieldListener {

		protected final StringDialogField fNameField;

		public NameGroup(Composite composite, String initialName) {
			final Composite nameComposite= new Composite(composite, SWT.NONE);
			nameComposite.setFont(composite.getFont());
			nameComposite.setLayout(initGridLayout(new GridLayout(2, false), false));
			nameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			// text field for project name
			fNameField= new StringDialogField();
			fNameField.setLabelText(NewWizardMessages.JavaProjectWizardFirstPage_NameGroup_label_text); 
			fNameField.setDialogFieldListener(this);

			setName(initialName);

			fNameField.doFillIntoGrid(nameComposite, 2);
			LayoutUtil.setHorizontalGrabbing(fNameField.getTextControl(null));
		}
		
		protected void fireEvent() {
			setChanged();
			notifyObservers();
		}

		public String getName() {
			return fNameField.getText().trim();
		}

		public void postSetFocus() {
			fNameField.postSetFocusOnDialogField(getShell().getDisplay());
		}
		
		public void setName(String name) {
			fNameField.setText(name);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.IDialogFieldListener#dialogFieldChanged(org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField)
		 */
		public void dialogFieldChanged(DialogField field) {
			fireEvent();
		}
		
	}

	/**
	 * Request a location. Fires an event whenever the checkbox or the location
	 * field is changed, regardless of whether the change originates from the
	 * user or has been invoked programmatically.
	 */
	private final class LocationGroup extends Observable implements Observer, IStringButtonAdapter, IDialogFieldListener {

		protected final SelectionButtonDialogField fWorkspaceRadio;
		protected final SelectionButtonDialogField fExternalRadio;
		protected final StringButtonDialogField fLocation;
		
		private String fPreviousExternalLocation;
		
		private static final String DIALOGSTORE_LAST_EXTERNAL_LOC= JavaScriptUI.ID_PLUGIN + ".last.external.project"; //$NON-NLS-1$

		public LocationGroup(Composite composite) {

			final int numColumns= 3;

			final Group group= new Group(composite, SWT.NONE);
			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			group.setLayout(initGridLayout(new GridLayout(numColumns, false), true));
			group.setText(NewWizardMessages.JavaProjectWizardFirstPage_LocationGroup_title); 

			fWorkspaceRadio= new SelectionButtonDialogField(SWT.RADIO);
			fWorkspaceRadio.setDialogFieldListener(this);
			fWorkspaceRadio.setLabelText(NewWizardMessages.JavaProjectWizardFirstPage_LocationGroup_workspace_desc); 

			fExternalRadio= new SelectionButtonDialogField(SWT.RADIO);
			fExternalRadio.setLabelText(NewWizardMessages.JavaProjectWizardFirstPage_LocationGroup_external_desc); 

			fLocation= new StringButtonDialogField(this);
			fLocation.setDialogFieldListener(this);
			fLocation.setLabelText(NewWizardMessages.JavaProjectWizardFirstPage_LocationGroup_locationLabel_desc); 
			fLocation.setButtonLabel(NewWizardMessages.JavaProjectWizardFirstPage_LocationGroup_browseButton_desc); 

			fExternalRadio.attachDialogField(fLocation);
			
			fWorkspaceRadio.setSelection(true);
			fExternalRadio.setSelection(false);
			
			fPreviousExternalLocation= ""; //$NON-NLS-1$

			fWorkspaceRadio.doFillIntoGrid(group, numColumns);
			fExternalRadio.doFillIntoGrid(group, numColumns);
			fLocation.doFillIntoGrid(group, numColumns);
			LayoutUtil.setHorizontalGrabbing(fLocation.getTextControl(null));
		}
				
		protected void fireEvent() {
			setChanged();
			notifyObservers();
		}

		protected String getDefaultPath(String name) {
			final IPath path= Platform.getLocation().append(name);
			return path.toOSString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Observer#update(java.util.Observable,
		 *      java.lang.Object)
		 */
		public void update(Observable o, Object arg) {
			if (isInWorkspace()) {
				fLocation.setText(getDefaultPath(fNameGroup.getName()));
			}
			fireEvent();
		}

		public IPath getLocation() {
			if (isInWorkspace()) {
				return Platform.getLocation();
			}
			return Path.fromOSString(fLocation.getText().trim());
		}

		public boolean isInWorkspace() {
			return fWorkspaceRadio.isSelected();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.IStringButtonAdapter#changeControlPressed(org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField)
		 */
		public void changeControlPressed(DialogField field) {
			final DirectoryDialog dialog= new DirectoryDialog(getShell());
			dialog.setMessage(NewWizardMessages.JavaProjectWizardFirstPage_directory_message); 
			String directoryName = fLocation.getText().trim();
			if (directoryName.length() == 0) {
				String prevLocation= JavaScriptPlugin.getDefault().getDialogSettings().get(DIALOGSTORE_LAST_EXTERNAL_LOC);
				if (prevLocation != null) {
					directoryName= prevLocation;
				}
			}
		
			if (directoryName.length() > 0) {
				final File path = new File(directoryName);
				if (path.exists())
					dialog.setFilterPath(directoryName);
			}
			final String selectedDirectory = dialog.open();
			if (selectedDirectory != null) {
				fLocation.setText(selectedDirectory);
				JavaScriptPlugin.getDefault().getDialogSettings().put(DIALOGSTORE_LAST_EXTERNAL_LOC, selectedDirectory);
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.IDialogFieldListener#dialogFieldChanged(org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField)
		 */
		public void dialogFieldChanged(DialogField field) {
			if (field == fWorkspaceRadio) {
				final boolean checked= fWorkspaceRadio.isSelected();
				if (checked) {
					fPreviousExternalLocation= fLocation.getText();
					fLocation.setText(getDefaultPath(fNameGroup.getName()));
				} else {
					fLocation.setText(fPreviousExternalLocation);
				}
			}
			fireEvent();
		}
	}

	/**
	 * Request a project layout.
	 */
	private final class LayoutGroup implements Observer, SelectionListener {

		private final SelectionButtonDialogField fStdRadio, fSrcBinRadio;
		private final Group fGroup;
		private final Link fPreferenceLink;
		
		public LayoutGroup(Composite composite) {
			
			fGroup= new Group(composite, SWT.NONE);
			fGroup.setFont(composite.getFont());
			fGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fGroup.setLayout(initGridLayout(new GridLayout(3, false), true));
			fGroup.setText(NewWizardMessages.JavaProjectWizardFirstPage_LayoutGroup_title); 
			
			fStdRadio= new SelectionButtonDialogField(SWT.RADIO);
			fStdRadio.setLabelText(NewWizardMessages.JavaProjectWizardFirstPage_LayoutGroup_option_oneFolder); 
			
			fSrcBinRadio= new SelectionButtonDialogField(SWT.RADIO);
			fSrcBinRadio.setLabelText(NewWizardMessages.JavaProjectWizardFirstPage_LayoutGroup_option_separateFolders); 

			fStdRadio.doFillIntoGrid(fGroup, 3);
			LayoutUtil.setHorizontalGrabbing(fStdRadio.getSelectionButton(null));
			
			fSrcBinRadio.doFillIntoGrid(fGroup, 2);
			
			fPreferenceLink= new Link(fGroup, SWT.NONE);
			fPreferenceLink.setText(NewWizardMessages.JavaProjectWizardFirstPage_LayoutGroup_link_description);
			fPreferenceLink.setLayoutData(new GridData(GridData.END, GridData.END, false, false));
			fPreferenceLink.addSelectionListener(this);
						
			boolean useSrcBin= PreferenceConstants.getPreferenceStore().getBoolean(PreferenceConstants.SRCBIN_FOLDERS_IN_NEWPROJ);
			fSrcBinRadio.setSelection(useSrcBin);
			fStdRadio.setSelection(!useSrcBin);
		}

		public void update(Observable o, Object arg) {
			final boolean detect= fDetectGroup.mustDetect();
			fStdRadio.setEnabled(!detect);
			fSrcBinRadio.setEnabled(!detect);
			fPreferenceLink.setEnabled(!detect);
			fGroup.setEnabled(!detect);
		}
		
		public boolean isSrcBin() {
			return fSrcBinRadio.isSelected();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
			String id= NewJavaProjectPreferencePage.ID;
			PreferencesUtil.createPreferenceDialogOn(getShell(), id, new String[] { id }, null).open();
			//fDetectGroup.handlePossibleJVMChange();
			//fJREGroup.handlePossibleJVMChange();
		}
	}
	
	private final class JREGroup implements Observer, SelectionListener, IDialogFieldListener {

		private final SelectionButtonDialogField fEnableWebSupport;//, fUseProjectJRE;
		//private final ComboDialogField fJRECombo;
		private final Group fGroup;
		private String[] fComplianceLabels;
		private String[] fComplianceData;
		//private final Link fPreferenceLink;
		
		
		public JREGroup(Composite composite) {
			fGroup= new Group(composite, SWT.NONE);
			fGroup.setFont(composite.getFont());
			fGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fGroup.setLayout(initGridLayout(new GridLayout(3, false), true));
			fGroup.setText(NewWizardMessages.JavaProjectWizardFirstPage_0); 
						
			fEnableWebSupport= new SelectionButtonDialogField(SWT.CHECK);
			fEnableWebSupport.setLabelText(NewWizardMessages.JavaProjectWizardFirstPage_1);
			fEnableWebSupport.doFillIntoGrid(fGroup, 2);
			
			//fPreferenceLink= new Link(fGroup, SWT.NONE);
			//fPreferenceLink.setFont(fGroup.getFont());
			//fPreferenceLink.setText(NewWizardMessages.JavaProjectWizardFirstPage_JREGroup_link_description);
			//fPreferenceLink.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
		//	fPreferenceLink.addSelectionListener(this);
		
		//	fUseProjectJRE= new SelectionButtonDialogField(SWT.RADIO);
		//	fUseProjectJRE.setLabelText(NewWizardMessages.JavaProjectWizardFirstPage_JREGroup_specific_compliance);
		//	fUseProjectJRE.doFillIntoGrid(fGroup, 1);
		//	fUseProjectJRE.setDialogFieldListener(this);
						
			//fJRECombo= new ComboDialogField(SWT.READ_ONLY);
			//fillInstalledJREs(fJRECombo);
			//fJRECombo.setDialogFieldListener(this);

		//	Combo comboControl= fJRECombo.getComboControl(fGroup);
			//comboControl.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false)); // make sure column 2 is grabing (but no fill)
			//comboControl.setVisibleItemCount(20);
			
			//DialogField.createEmptySpace(fGroup);
			
			fEnableWebSupport.setSelection(false);
		//	fJRECombo.setEnabled(fUseProjectJRE.isSelected());
		}

		public boolean shouldEnableWebSupport() {
			return fEnableWebSupport.isSelected();
		}
		private void fillInstalledJREs(ComboDialogField comboField) {
//			String selectedItem= null;
//			int selectionIndex= -1;
//			if (fUseProjectJRE.isSelected()) {
//				selectionIndex= comboField.getSelectionIndex();
//				if (selectionIndex != -1) {//paranoia
//					selectedItem= comboField.getItems()[selectionIndex];
//				}
//			}
//			
//			fInstalledJVMs= getWorkspaceJREs();
//			Arrays.sort(fInstalledJVMs, new Comparator() {
//
//				public int compare(Object arg0, Object arg1) {
//					IVMInstall i0= (IVMInstall)arg0;
//					IVMInstall i1= (IVMInstall)arg1;
//					if (i1 instanceof IVMInstall2 && i0 instanceof IVMInstall2) {
//						String cc0= JavaModelUtil.getCompilerCompliance((IVMInstall2) i0, JavaScriptCore.VERSION_1_4);
//						String cc1= JavaModelUtil.getCompilerCompliance((IVMInstall2) i1, JavaScriptCore.VERSION_1_4);
//						int result= cc1.compareTo(cc0);
//						if (result == 0)
//							result= i0.getName().compareTo(i1.getName());
//						return result;
//					} else {
//						return i0.getName().compareTo(i1.getName());
//					}
//				}
//				
//			});
//			selectionIndex= -1;//find new index
//			fComplianceLabels= new String[fInstalledJVMs.length];
//			fComplianceData= new String[fInstalledJVMs.length];
//			for (int i= 0; i < fInstalledJVMs.length; i++) {
//				fComplianceLabels[i]= fInstalledJVMs[i].getName();
//				if (selectedItem != null && fComplianceLabels[i].equals(selectedItem)) {
//					selectionIndex= i;
//				}
//				if (fInstalledJVMs[i] instanceof IVMInstall2) {
//					fComplianceData[i]= JavaModelUtil.getCompilerCompliance((IVMInstall2) fInstalledJVMs[i], JavaScriptCore.VERSION_1_4);
//				} else {
//					fComplianceData[i]= JavaScriptCore.VERSION_1_4;
//				}
//			}
//			comboField.setItems(fComplianceLabels);
//			if (selectionIndex == -1) {
//				fJRECombo.selectItem(getDefaultJVMName());
//			} else {
//				fJRECombo.selectItem(selectedItem);
//			}
		}
		

		private String getDefaultJVMName() {
			return JavaRuntime.getDefaultVMInstall().getName();
		}

		private String getDefaultJVMLabel() {
			return Messages.format(NewWizardMessages.JavaProjectWizardFirstPage_JREGroup_default_compliance, getDefaultJVMName());
		}

		public void update(Observable o, Object arg) {
			updateEnableState();
		}

		private void updateEnableState() {
//			final boolean detect= fDetectGroup.mustDetect();
//			fUseDefaultJRE.setEnabled(!detect);
//			fUseProjectJRE.setEnabled(!detect);
//			fJRECombo.setEnabled(!detect && fUseProjectJRE.isSelected());
//			//fPreferenceLink.setEnabled(!detect);
//			fGroup.setEnabled(!detect);
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
			String jreID= BuildPathSupport.JRE_PREF_PAGE_ID;
			String complianceId= CompliancePreferencePage.PREF_ID;
			Map data= new HashMap();
			data.put(PropertyAndPreferencePage.DATA_NO_LINK, Boolean.TRUE);
			PreferencesUtil.createPreferenceDialogOn(getShell(), jreID, new String[] { jreID, complianceId  }, data).open();
			
			handlePossibleJVMChange();
			//fDetectGroup.handlePossibleJVMChange();
		}
		
		public void handlePossibleJVMChange() {
			fEnableWebSupport.setLabelText(getDefaultJVMLabel());
			//fillInstalledJREs(fJRECombo);
		}
		

		/* (non-Javadoc)
		 * @see org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.IDialogFieldListener#dialogFieldChanged(org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.DialogField)
		 */
		public void dialogFieldChanged(DialogField field) {
			updateEnableState();
			//fDetectGroup.handlePossibleJVMChange();
		}
		
		public boolean isUseSpecific() {
		return false;//	return fUseProjectJRE.isSelected();
		}
		
//		public IVMInstall getSelectedJVM() {
//			if (fUseProjectJRE.isSelected()) {
//				int index= fJRECombo.getSelectionIndex();
//				if (index >= 0 && index < fComplianceData.length) { // paranoia
//					return fInstalledJVMs[index];
//				}
//			}
//			return null;
//		}
		
		public String getSelectedCompilerCompliance() {
//			if (fUseProjectJRE.isSelected()) {
//				int index= fJRECombo.getSelectionIndex();
//				if (index >= 0 && index < fComplianceData.length) { // paranoia
//					return fComplianceData[index];
//				}
//			}
			return null;
		}
	}

	
	private final class WorkingSetGroup {
		
		private WorkingSetConfigurationBlock fWorkingSetBlock;

		public WorkingSetGroup(Composite composite, IWorkingSet[] initialWorkingSets) {
			Group workingSetGroup= new Group(composite, SWT.NONE);
			workingSetGroup.setFont(composite.getFont());
			workingSetGroup.setText(NewWizardMessages.JavaProjectWizardFirstPage_WorkingSets_group);
			workingSetGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			workingSetGroup.setLayout(new GridLayout(1, false));
			
			String[] workingSetIds= new String[] {JavaWorkingSetUpdater.ID, "org.eclipse.ui.resourceWorkingSetPage"}; //$NON-NLS-1$
			fWorkingSetBlock= new WorkingSetConfigurationBlock(workingSetIds, NewWizardMessages.JavaProjectWizardFirstPage_EnableWorkingSet_button, JavaScriptPlugin.getDefault().getDialogSettings());
			fWorkingSetBlock.setDialogMessage(NewWizardMessages.JavaProjectWizardFirstPage_WorkingSetSelection_message);
			fWorkingSetBlock.setSelection(initialWorkingSets);
			fWorkingSetBlock.createContent(workingSetGroup);
		}

		public IWorkingSet[] getSelectedWorkingSets() {
			return fWorkingSetBlock.getSelectedWorkingSets();
		}
	}


	/**
	 * Show a warning when the project location contains files.
	 */
	private final class DetectGroup extends Observable implements Observer, SelectionListener {

		private final Link fHintText;
		private Label fIcon;
		private boolean fDetect;
		
		public DetectGroup(Composite parent) {
			
			Composite composite= new Composite(parent, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			GridLayout layout= new GridLayout(2, false);
			layout.horizontalSpacing= 10;
			composite.setLayout(layout);
			
			fIcon= new Label(composite, SWT.LEFT);
			fIcon.setImage(Dialog.getImage(Dialog.DLG_IMG_MESSAGE_WARNING));
			GridData gridData= new GridData(SWT.LEFT, SWT.CENTER, false, false);
			fIcon.setLayoutData(gridData);
			
			fHintText= new Link(composite, SWT.WRAP);
			fHintText.setFont(composite.getFont());
			fHintText.addSelectionListener(this);
			gridData= new GridData(GridData.FILL, SWT.FILL, true, true);
			gridData.widthHint= convertWidthInCharsToPixels(50);
			gridData.heightHint= convertHeightInCharsToPixels(3);
			fHintText.setLayoutData(gridData);
			
			//handlePossibleJVMChange();
		}
		
//		public void handlePossibleJVMChange() {
//			String selectedCompliance= fJREGroup.getSelectedCompilerCompliance();
//			if (selectedCompliance == null) {
//				selectedCompliance= JavaScriptCore.getOption(JavaScriptCore.COMPILER_COMPLIANCE);
//			}
//			IVMInstall selectedJVM= fJREGroup.getSelectedJVM();
//			if (selectedJVM == null) {
//				selectedJVM= JavaRuntime.getDefaultVMInstall();
//			}
//			String jvmCompliance= JavaScriptCore.VERSION_1_4;
//			if (selectedJVM instanceof IVMInstall2) {
//				jvmCompliance= JavaModelUtil.getCompilerCompliance((IVMInstall2) selectedJVM, JavaScriptCore.VERSION_1_4);
//			}
//			if (!selectedCompliance.equals(jvmCompliance) && (JavaModelUtil.is50OrHigher(selectedCompliance) || JavaModelUtil.is50OrHigher(jvmCompliance))) {
//				if (selectedCompliance.equals(JavaScriptCore.VERSION_1_5))
//					selectedCompliance= "5.0"; //$NON-NLS-1$
//				else if (selectedCompliance.equals(JavaScriptCore.VERSION_1_6))
//					selectedCompliance= "6.0"; //$NON-NLS-1$
//				
//				fHintText.setText(Messages.format(NewWizardMessages.JavaProjectWizardFirstPage_DetectGroup_jre_message, new String[] {selectedCompliance, jvmCompliance}));
//				fHintText.setVisible(true);
//			} else {
//				fHintText.setVisible(false);
//			}
//		}
		
		public void update(Observable o, Object arg) {
			if (o instanceof LocationGroup) {
				boolean oldDetectState= fDetect;
				if (fLocationGroup.isInWorkspace()) {
					fDetect= false;
				} else {
					final File directory= fLocationGroup.getLocation().toFile();
					fDetect= directory.isDirectory();
				}
				
				if (oldDetectState != fDetect) {
					setChanged();
					notifyObservers();
					
					if (fDetect) {
						fHintText.setVisible(true);
						fHintText.setText(NewWizardMessages.JavaProjectWizardFirstPage_DetectGroup_message);
						fIcon.setImage(Dialog.getImage(Dialog.DLG_IMG_MESSAGE_INFO));
						fIcon.setVisible(true);
					} else {
					//	handlePossibleJVMChange();
					}
				}
			}
		}

		public boolean mustDetect() {
			return fDetect;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
			String jreID= BuildPathSupport.JRE_PREF_PAGE_ID;
			String complianceId= CompliancePreferencePage.PREF_ID;
			Map data= new HashMap();
			data.put(PropertyAndPreferencePage.DATA_NO_LINK, Boolean.TRUE);
			String id= "JRE".equals(e.text) ? jreID : complianceId; //$NON-NLS-1$
			PreferencesUtil.createPreferenceDialogOn(getShell(), id, new String[] { jreID, complianceId  }, data).open();
			
//			fJREGroup.handlePossibleJVMChange();
//			handlePossibleJVMChange();
		}
	}

	/**
	 * Validate this page and show appropriate warnings and error NewWizardMessages.
	 */
	private final class Validator implements Observer {

		public void update(Observable o, Object arg) {

			final IWorkspace workspace= JavaScriptPlugin.getWorkspace();

			final String name= fNameGroup.getName();

			// check whether the project name field is empty
			if (name.length() == 0) { 
				setErrorMessage(null);
				setMessage(NewWizardMessages.JavaProjectWizardFirstPage_Message_enterProjectName); 
				setPageComplete(false);
				return;
			}

			// check whether the project name is valid
			final IStatus nameStatus= workspace.validateName(name, IResource.PROJECT);
			if (!nameStatus.isOK()) {
				setErrorMessage(nameStatus.getMessage());
				setPageComplete(false);
				return;
			}

			// check whether project already exists
			final IProject handle= getProjectHandle();
			if (handle.exists()) {
				setErrorMessage(NewWizardMessages.JavaProjectWizardFirstPage_Message_projectAlreadyExists); 
				setPageComplete(false);
				return;
			}

			final String location= fLocationGroup.getLocation().toOSString();

			// check whether location is empty
			if (location.length() == 0) {
				setErrorMessage(null);
				setMessage(NewWizardMessages.JavaProjectWizardFirstPage_Message_enterLocation); 
				setPageComplete(false);
				return;
			}

			// check whether the location is a syntactically correct path
			if (!Path.EMPTY.isValidPath(location)) { 
				setErrorMessage(NewWizardMessages.JavaProjectWizardFirstPage_Message_invalidDirectory); 
				setPageComplete(false);
				return;
			}

			IPath projectPath= Path.fromOSString(location);
			// check external location
			if (!fLocationGroup.isInWorkspace()) {				
				if (!canCreate(projectPath.toFile())) {
					setErrorMessage(NewWizardMessages.JavaProjectWizardFirstPage_Message_cannotCreateAtExternalLocation); 
					setPageComplete(false);
					return;
				}

				if (!Platform.getLocation().equals(projectPath) && Platform.getLocation().isPrefixOf(projectPath)) {
					if (!Platform.getLocation().equals(projectPath.removeLastSegments(1))) {
						setErrorMessage(NewWizardMessages.JavaProjectWizardFirstPage_Message_notOnWorkspaceRoot);
						setPageComplete(false);
						return;
					}
					
					if (!projectPath.toFile().exists()) {
						setErrorMessage(NewWizardMessages.JavaProjectWizardFirstPage_Message_notExisingProjectOnWorkspaceRoot);
						setPageComplete(false);
						return;
					}
					
					String existingName= projectPath.lastSegment();
					if (!existingName.equals(fNameGroup.getName())) {
						setErrorMessage(Messages.format(NewWizardMessages.JavaProjectWizardFirstPage_Message_invalidProjectNameForWorkspaceRoot, existingName));
						setPageComplete(false);
						return;
					}
				} else {
					// If we do not place the contents in the workspace validate the
					// location.
					final IStatus locationStatus= workspace.validateProjectLocation(handle, projectPath);
					if (!locationStatus.isOK()) {
						setErrorMessage(locationStatus.getMessage());
						setPageComplete(false);
						return;
					}
				}
			} else {
				IPath projectFolder= projectPath.append(fNameGroup.getName());
				if (projectFolder.toFile().exists()) {
					setErrorMessage(NewWizardMessages.JavaProjectWizardFirstPage_Message_existingFolderInWorkspace); 
					setPageComplete(false);
					return;
				}
			}
			
			setPageComplete(true);

			setErrorMessage(null);
			setMessage(null);
		}

		private boolean canCreate(File file) {
			while (!file.exists()) {
				file= file.getParentFile();
				if (file == null)
					return false;
			}
			
			return file.canWrite();
		}

	}

	private NameGroup fNameGroup;
	private LocationGroup fLocationGroup;
	private LayoutGroup fLayoutGroup;
	private JREGroup fJREGroup;
	private DetectGroup fDetectGroup;
	private Validator fValidator;

	private String fInitialName;
	
	private static final String PAGE_NAME= NewWizardMessages.JavaProjectWizardFirstPage_page_pageName;
	private WorkingSetGroup fWorkingSetGroup;
	private IWorkingSet[] fInitWorkingSets; 

	/**
	 * Create a new <code>SimpleProjectFirstPage</code>.
	 */
	public JavaProjectWizardFirstPage() {
		super(PAGE_NAME);
		setPageComplete(false);
		setTitle(NewWizardMessages.JavaProjectWizardFirstPage_page_title); 
		setDescription(NewWizardMessages.JavaProjectWizardFirstPage_page_description); 
		fInitialName= ""; //$NON-NLS-1$
		initializeDefaultVM();
	}
	
	private void initializeDefaultVM() {
		JavaRuntime.getDefaultVMInstall();
	}
	
	public void setName(String name) {
		fInitialName= name;
		if (fNameGroup != null) {
			fNameGroup.setName(name);
		}
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		final Composite composite= new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(initGridLayout(new GridLayout(1, false), true));
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// create UI elements
		fNameGroup= new NameGroup(composite, fInitialName);
		fLocationGroup= new LocationGroup(composite);
		fJREGroup= new JREGroup(composite);
		fLayoutGroup= new LayoutGroup(composite);
		fWorkingSetGroup= new WorkingSetGroup(composite, fInitWorkingSets);
		fDetectGroup= new DetectGroup(composite);
		
		// establish connections
		fNameGroup.addObserver(fLocationGroup);
		fDetectGroup.addObserver(fLayoutGroup);
		fDetectGroup.addObserver(fJREGroup);
		fLocationGroup.addObserver(fDetectGroup);

		// initialize all elements
		fNameGroup.notifyObservers();
		
		// create and connect validator
		fValidator= new Validator();
		fNameGroup.addObserver(fValidator);
		fLocationGroup.addObserver(fValidator);

		setControl(composite);
		Dialog.applyDialogFont(composite);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, IJavaHelpContextIds.NEW_JAVAPROJECT_WIZARD_PAGE);
	}	

	/**
	 * Returns the current project location path as entered by the user, or its
	 * anticipated initial value. Note that if the default has been returned
	 * the path in a project description used to create a project should not be
	 * set.
	 * <p>
	 * TODO At some point this method has to be converted to return an URI instead
	 * of an path. However, this first requires support from Platform/UI to specify
	 * a project location different than in a local file system. 
	 * </p>
	 * @return the project location path or its anticipated initial value.
	 */
	public IPath getLocationPath() {
		return fLocationGroup.getLocation();
	}


	/**
	 * Creates a project resource handle for the current project name field
	 * value.
	 * <p>
	 * This method does not create the project resource; this is the
	 * responsibility of <code>IProject::create</code> invoked by the new
	 * project resource wizard.
	 * </p>
	 * 
	 * @return the new project resource handle
	 */
	public IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(fNameGroup.getName());
	}
	
	public boolean isInWorkspace() {
		final String location= fLocationGroup.getLocation().toOSString();
		IPath projectPath= Path.fromOSString(location);
		return Platform.getLocation().isPrefixOf(projectPath);
	}
	
	public String getProjectName() {
		return fNameGroup.getName();
	}

	public boolean getDetect() {
		return fDetectGroup.mustDetect();
	}
	
	public boolean isSrcBin() {
		return fLayoutGroup.isSrcBin();
	}
	
	/**
	 * Returns the path of the classpath container selected, or <code>null</code> if the default JRE was selected
	 * @return the path to the selected JRE
	 */
	public IPath getJREContainerPath() {
//		return fJREGroup.getJREContainerPath();
		return null;
	}
	
	/**
	 * @return the selected Compiler Compliance, or <code>null</code> iff the default Compiler Compliance should be used
	 */
	public boolean isWebEnabled() {
		return fJREGroup.shouldEnableWebSupport();
		//return null; //fJREGroup.getSelectedCompilerCompliance();
	}
	
	/*
	 * see @DialogPage.setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			fNameGroup.postSetFocus();
		}
	}
		
	/**
	 * Initialize a grid layout with the default Dialog settings.
	 * @param layout the layout to initialize
	 * @param margins true if margins should be used
	 * @return the initialized layout
	 */
	protected GridLayout initGridLayout(GridLayout layout, boolean margins) {
		layout.horizontalSpacing= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		if (margins) {
			layout.marginWidth= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
			layout.marginHeight= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		} else {
			layout.marginWidth= 0;
			layout.marginHeight= 0;
		}
		return layout;
	}

	/**
	 * @param workingSets the initial selected working sets or <b>null</b>
	 */
	public void setWorkingSets(IWorkingSet[] workingSets) {
		fInitWorkingSets= workingSets;
	}

	/**
	 * @return the selected working sets, not <b>null</b>
	 */
	public IWorkingSet[] getWorkingSets() {
		return fWorkingSetGroup.getSelectedWorkingSets();
	}
}
