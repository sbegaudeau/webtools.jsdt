/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.rename;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.wst.jsdt.core.Flags;
import org.eclipse.wst.jsdt.core.IField;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.refactoring.IJavaScriptRefactorings;
import org.eclipse.wst.jsdt.core.refactoring.descriptors.JavaScriptRefactoringDescriptor;
import org.eclipse.wst.jsdt.core.refactoring.descriptors.RenameJavaScriptElementDescriptor;
import org.eclipse.wst.jsdt.internal.corext.refactoring.Checks;
import org.eclipse.wst.jsdt.internal.corext.refactoring.JDTRefactoringDescriptor;
import org.eclipse.wst.jsdt.internal.corext.refactoring.JDTRefactoringDescriptorComment;
import org.eclipse.wst.jsdt.internal.corext.refactoring.JavaRefactoringArguments;
import org.eclipse.wst.jsdt.internal.corext.refactoring.RefactoringAvailabilityTester;
import org.eclipse.wst.jsdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.wst.jsdt.internal.corext.refactoring.changes.RefactoringDescriptorChange;
import org.eclipse.wst.jsdt.internal.corext.refactoring.code.ScriptableRefactoring;
import org.eclipse.wst.jsdt.internal.corext.util.Messages;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.ui.JavaScriptElementLabels;

public final class RenameEnumConstProcessor extends RenameFieldProcessor {

	public static final String IDENTIFIER= "org.eclipse.wst.jsdt.ui.renameEnumConstProcessor"; //$NON-NLS-1$

	/**
	 * Creates a new rename enum const processor.
	 * 
	 * @param field
	 *            the enum constant, or <code>null</code> if invoked by
	 *            scripting
	 */
	public RenameEnumConstProcessor(IField field) {
		super(field);
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.rename.RenameFieldProcessor#canEnableGetterRenaming()
	 */
	public String canEnableGetterRenaming() throws CoreException {
		return ""; //$NON-NLS-1$
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.rename.RenameFieldProcessor#canEnableSetterRenaming()
	 */
	public String canEnableSetterRenaming() throws CoreException {
		return ""; //$NON-NLS-1$
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.tagging.INameUpdating#checkNewElementName(java.lang.String)
	 */
	public RefactoringStatus checkNewElementName(String newName) throws CoreException {
		RefactoringStatus result= Checks.checkEnumConstantName(newName);
		if (Checks.isAlreadyNamed(getField(), newName))
			result.addFatalError(RefactoringCoreMessages.RenameEnumConstRefactoring_another_name);
		if (getField().getDeclaringType().getField(newName).exists())
			result.addFatalError(RefactoringCoreMessages.RenameEnumConstRefactoring_const_already_defined);
		return result;
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.rename.RenameFieldProcessor#createChange(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public Change createChange(final IProgressMonitor monitor) throws CoreException {
		Change change= super.createChange(monitor);
		if (change != null) {
			final IField field= getField();
			String project= null;
			IJavaScriptProject javaProject= field.getJavaScriptProject();
			if (javaProject != null)
				project= javaProject.getElementName();
			int flags= JavaScriptRefactoringDescriptor.JAR_MIGRATION | JavaScriptRefactoringDescriptor.JAR_REFACTORING | RefactoringDescriptor.STRUCTURAL_CHANGE;
			final IType declaring= field.getDeclaringType();
			try {
				if (!Flags.isPrivate(declaring.getFlags()))
					flags|= RefactoringDescriptor.MULTI_CHANGE;
				if (declaring.isAnonymous() || declaring.isLocal())
					flags|= JavaScriptRefactoringDescriptor.JAR_SOURCE_ATTACHMENT;
			} catch (JavaScriptModelException exception) {
				JavaScriptPlugin.log(exception);
			}
			final String description= Messages.format(RefactoringCoreMessages.RenameEnumConstProcessor_descriptor_description_short, fField.getElementName());
			final String header= Messages.format(RefactoringCoreMessages.RenameEnumConstProcessor_descriptor_description, new String[] { field.getElementName(), JavaScriptElementLabels.getElementLabel(field.getParent(), JavaScriptElementLabels.ALL_FULLY_QUALIFIED), getNewElementName()});
			final String comment= new JDTRefactoringDescriptorComment(project, this, header).asString();
			final RenameJavaScriptElementDescriptor descriptor= new RenameJavaScriptElementDescriptor(IJavaScriptRefactorings.RENAME_ENUM_CONSTANT);
			descriptor.setProject(project);
			descriptor.setDescription(description);
			descriptor.setComment(comment);
			descriptor.setFlags(flags);
			descriptor.setJavaElement(field);
			descriptor.setNewName(getNewElementName());
			descriptor.setUpdateReferences(fUpdateReferences);
			descriptor.setUpdateTextualOccurrences(fUpdateTextualMatches);
			final RefactoringDescriptorChange extended= new RefactoringDescriptorChange(descriptor, RefactoringCoreMessages.RenameEnumConstProcessor_change_name, new Change[] { change});
			extended.markAsSynthetic();
			return extended;
		}
		return change;
	}

	/*
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#getIdentifier()
	 */
	public String getIdentifier() {
		return IDENTIFIER;
	}

	/*
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#getProcessorName()
	 */
	public String getProcessorName() {
		return RefactoringCoreMessages.RenameEnumConstRefactoring_name;
	}

	/*
	 * @see org.eclipse.wst.jsdt.internal.corext.refactoring.rename.RenameFieldProcessor#initialize(org.eclipse.ltk.core.refactoring.participants.RefactoringArguments)
	 */
	public RefactoringStatus initialize(RefactoringArguments arguments) {
		if (arguments instanceof JavaRefactoringArguments) {
			final JavaRefactoringArguments extended= (JavaRefactoringArguments) arguments;
			final String handle= extended.getAttribute(JDTRefactoringDescriptor.ATTRIBUTE_INPUT);
			if (handle != null) {
				final IJavaScriptElement element= JDTRefactoringDescriptor.handleToElement(extended.getProject(), handle, false);
				if (element == null || !element.exists() || element.getElementType() != IJavaScriptElement.FIELD)
					return ScriptableRefactoring.createInputFatalStatus(element, getRefactoring().getName(), IJavaScriptRefactorings.RENAME_ENUM_CONSTANT);
				else
					fField= (IField) element;
			} else
				return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_argument_not_exist, JDTRefactoringDescriptor.ATTRIBUTE_INPUT));
			final String name= extended.getAttribute(JDTRefactoringDescriptor.ATTRIBUTE_NAME);
			if (name != null && !"".equals(name)) //$NON-NLS-1$
				setNewElementName(name);
			else
				return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_argument_not_exist, JDTRefactoringDescriptor.ATTRIBUTE_NAME));
			final String references= extended.getAttribute(JDTRefactoringDescriptor.ATTRIBUTE_REFERENCES);
			if (references != null) {
				setUpdateReferences(Boolean.valueOf(references).booleanValue());
			} else
				return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_argument_not_exist, JDTRefactoringDescriptor.ATTRIBUTE_REFERENCES));
			final String matches= extended.getAttribute(ATTRIBUTE_TEXTUAL_MATCHES);
			if (matches != null) {
				setUpdateTextualMatches(Boolean.valueOf(matches).booleanValue());
			} else
				return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_argument_not_exist, ATTRIBUTE_TEXTUAL_MATCHES));
		} else
			return RefactoringStatus.createFatalErrorStatus(RefactoringCoreMessages.InitializableRefactoring_inacceptable_arguments);
		return new RefactoringStatus();
	}

	public boolean isApplicable() throws CoreException {
		return RefactoringAvailabilityTester.isRenameEnumConstAvailable(getField());
	}
}
