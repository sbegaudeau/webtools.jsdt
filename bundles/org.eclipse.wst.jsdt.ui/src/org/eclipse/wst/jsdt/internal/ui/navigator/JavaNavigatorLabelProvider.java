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
package org.eclipse.wst.jsdt.internal.ui.navigator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.eclipse.ui.navigator.IExtensionStateModel;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.internal.ui.navigator.IExtensionStateConstants.Values;
import org.eclipse.wst.jsdt.internal.ui.packageview.PackageExplorerContentProvider;
import org.eclipse.wst.jsdt.internal.ui.packageview.PackageExplorerLabelProvider;
import org.eclipse.wst.jsdt.ui.JavaScriptElementLabels;

/**
 * Provides the labels for the Project Explorer.
 * <p>
 * It provides labels for the packages in hierarchical layout and in all other
 * cases delegates it to its super class.
 * </p>
 * 
 * @since 3.2
 */
public class JavaNavigatorLabelProvider implements ICommonLabelProvider {

	private final long LABEL_FLAGS = JavaScriptElementLabels.DEFAULT_QUALIFIED
			| JavaScriptElementLabels.ROOT_POST_QUALIFIED
			| JavaScriptElementLabels.APPEND_ROOT_PATH
			| JavaScriptElementLabels.M_PARAMETER_TYPES
			| JavaScriptElementLabels.M_PARAMETER_NAMES
			| JavaScriptElementLabels.M_APP_RETURNTYPE
			| JavaScriptElementLabels.M_EXCEPTIONS
			| JavaScriptElementLabels.F_APP_TYPE_SIGNATURE
			| JavaScriptElementLabels.T_TYPE_PARAMETERS;

	private PackageExplorerLabelProvider delegeteLabelProvider;

	private PackageExplorerContentProvider fContentProvider;

	private IExtensionStateModel fStateModel;

	private IPropertyChangeListener fLayoutPropertyListener;

	public JavaNavigatorLabelProvider() {

	}
	public void init(ICommonContentExtensionSite commonContentExtensionSite) {
		fStateModel = commonContentExtensionSite.getExtensionStateModel();
		fContentProvider = (PackageExplorerContentProvider) commonContentExtensionSite.getExtension().getContentProvider();
		delegeteLabelProvider = createLabelProvider();

		delegeteLabelProvider.setIsFlatLayout(fStateModel
				.getBooleanProperty(Values.IS_LAYOUT_FLAT));
		fLayoutPropertyListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (Values.IS_LAYOUT_FLAT.equals(event.getProperty())) {
					if (event.getNewValue() != null) {
						boolean newValue = ((Boolean) event.getNewValue())
								.booleanValue() ? true : false;
						delegeteLabelProvider.setIsFlatLayout(newValue);
					}
				}

			}
		};
		fStateModel.addPropertyChangeListener(fLayoutPropertyListener);
	}

	public String getDescription(Object element) {
		return formatMessage(element);
	}

	private PackageExplorerLabelProvider createLabelProvider() {
		return new PackageExplorerLabelProvider(fContentProvider);
	}

	public void dispose() { 
		delegeteLabelProvider.dispose();
		fStateModel.removePropertyChangeListener(fLayoutPropertyListener);
	}

	public void propertyChange(PropertyChangeEvent event) {
		delegeteLabelProvider.propertyChange(event);
	}

	public void addLabelDecorator(ILabelDecorator decorator) {
		delegeteLabelProvider.addLabelDecorator(decorator);
	}

	public void addListener(ILabelProviderListener listener) {
		delegeteLabelProvider.addListener(listener);
	}

	public Color getBackground(Object element) {
		return delegeteLabelProvider.getBackground(element);
	}

	public Color getForeground(Object element) {
		return delegeteLabelProvider.getForeground(element);
	}

	public Image getImage(Object element) {
		return delegeteLabelProvider.getImage(element);
	}

	public boolean isLabelProperty(Object element, String property) {
		return delegeteLabelProvider.isLabelProperty(element, property);
	}

	public void removeListener(ILabelProviderListener listener) {
		delegeteLabelProvider.removeListener(listener);
	}

	public boolean equals(Object obj) {
		return delegeteLabelProvider.equals(obj);
	}

	public int hashCode() {
		return delegeteLabelProvider.hashCode();
	}

	public String toString() {
		return delegeteLabelProvider.toString();
	}

	public String getText(Object element) {
		return delegeteLabelProvider.getText(element);
	}

	public void setIsFlatLayout(boolean state) {
		delegeteLabelProvider.setIsFlatLayout(state);
	}

	// Taken from StatusBarUpdater

	private String formatMessage(Object element) {
		if (element instanceof IJavaScriptElement) {
			return formatJavaElementMessage((IJavaScriptElement) element);
		} else if (element instanceof IResource) {
			return formatResourceMessage((IResource) element);
		}
		return ""; //$NON-NLS-1$
	}

	private String formatJavaElementMessage(IJavaScriptElement element) {
		return JavaScriptElementLabels.getElementLabel(element, LABEL_FLAGS);
	}

	private String formatResourceMessage(IResource element) {
		IContainer parent = element.getParent();
		if (parent != null && parent.getType() != IResource.ROOT)
			return element.getName() + JavaScriptElementLabels.CONCAT_STRING
					+ parent.getFullPath().makeRelative().toString();
		else
			return element.getName();
	}
	
	public void restoreState(IMemento memento) { 
		
	}
	
	public void saveState(IMemento memento) { 
		
	}
 
}
