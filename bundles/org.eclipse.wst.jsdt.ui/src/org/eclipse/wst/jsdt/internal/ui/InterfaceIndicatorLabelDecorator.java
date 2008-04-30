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
package org.eclipse.wst.jsdt.internal.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.jsdt.core.ElementChangedEvent;
import org.eclipse.wst.jsdt.core.Flags;
import org.eclipse.wst.jsdt.core.IClassFile;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IElementChangedListener;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptElementDelta;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.ITypeRoot;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.core.search.TypeNameRequestor;
import org.eclipse.wst.jsdt.internal.corext.util.JavaModelUtil;

public class InterfaceIndicatorLabelDecorator implements ILabelDecorator, ILightweightLabelDecorator {
	
	private class IntefaceIndicatorChangeListener implements IElementChangedListener {

		/**
		 * {@inheritDoc}
		 */
		public void elementChanged(ElementChangedEvent event) {
			List changed= new ArrayList();
			processDelta(event.getDelta(), changed);
			if (changed.size() == 0)
				return;
			
			fireChange((IJavaScriptElement[])changed.toArray(new IJavaScriptElement[changed.size()]));
		}
		
	}
	
	private ListenerList fListeners;
	private IElementChangedListener fChangeListener;

	public InterfaceIndicatorLabelDecorator() {
	}

	/**
	 * {@inheritDoc}
	 */
	public Image decorateImage(Image image, Object element) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String decorateText(String text, Object element) {
		return text;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addListener(ILabelProviderListener listener) {
		if (fChangeListener == null) {
			fChangeListener= new IntefaceIndicatorChangeListener();
			JavaScriptCore.addElementChangedListener(fChangeListener);
		}
		
		if (fListeners == null) {
			fListeners= new ListenerList();
		}
		
		fListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void dispose() {
		if (fChangeListener != null) {
			JavaScriptCore.removeElementChangedListener(fChangeListener);
			fChangeListener= null;
		}
		if (fListeners != null) {
			Object[] listeners= fListeners.getListeners();
			for (int i= 0; i < listeners.length; i++) {
				fListeners.remove(listeners[i]);
			}
			fListeners= null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeListener(ILabelProviderListener listener) {
		if (fListeners == null)
			return;
		
		fListeners.remove(listener);
		
		if (fListeners.isEmpty() && fChangeListener != null) {
			JavaScriptCore.removeElementChangedListener(fChangeListener);
			fChangeListener= null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void decorate(Object element, IDecoration decoration) {
		try {
			ImageDescriptor overlay= getOverlay(element);
			if (overlay == null)
				return;
			
			decoration.addOverlay(overlay, IDecoration.TOP_RIGHT);
		} catch (JavaScriptModelException e) {
			return;
		}
	}
	
	private ImageDescriptor getOverlay(Object element) throws JavaScriptModelException {
		if (element instanceof IJavaScriptUnit) {
			IJavaScriptUnit unit= (IJavaScriptUnit) element;
			if (unit.isOpen()) {
				IType mainType= unit.findPrimaryType();
				if (mainType != null) {
					return getOverlayFromFlags(mainType.getFlags());
				}
				return null;
			}
			String typeName= JavaScriptCore.removeJavaScriptLikeExtension(unit.getElementName());
			return getOverlayWithSearchEngine(unit, typeName);
		} else if (element instanceof IClassFile) {
			IClassFile classFile= (IClassFile) element;
			if (classFile.isOpen()) {
				return getOverlayFromFlags(classFile.getType().getFlags());
			}
			String typeName= classFile.getType().getElementName();
			return getOverlayWithSearchEngine(classFile, typeName);
		}
		return null;
	}
	
	private ImageDescriptor getOverlayWithSearchEngine(ITypeRoot element, String typeName) {
		SearchEngine engine= new SearchEngine();
		IJavaScriptSearchScope scope= SearchEngine.createJavaSearchScope(new IJavaScriptElement[] { element });
		
		class Result extends RuntimeException {
			private static final long serialVersionUID= 1L;
			int modifiers;
			public Result(int modifiers) {
				this.modifiers= modifiers;
			}
		}
		
		TypeNameRequestor requestor= new TypeNameRequestor() {
			public void acceptType(int modifiers, char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames, String path) {
				if (enclosingTypeNames.length == 0 && Flags.isPublic(modifiers)) {
					throw new Result(modifiers);
				}
			}
		};
		
		try {
			String packName = element.getParent().getElementName();
			int matchRule = SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE;
			engine.searchAllTypeNames(packName.toCharArray(), matchRule, typeName.toCharArray(), matchRule, IJavaScriptSearchConstants.TYPE, scope, requestor, IJavaScriptSearchConstants.WAIT_UNTIL_READY_TO_SEARCH , null);
		} catch (Result e) {
			return getOverlayFromFlags(e.modifiers);
		} catch (JavaScriptModelException e) {
			JavaScriptPlugin.log(e);
		}
		return null;

	}

	private ImageDescriptor getOverlayFromFlags(int flags)  {
		if (Flags.isAnnotation(flags)) {
			return JavaPluginImages.DESC_OVR_ANNOTATION;
		} else if (Flags.isEnum(flags)) {
			return JavaPluginImages.DESC_OVR_ENUM;
		} else if (Flags.isInterface(flags)) {
			return JavaPluginImages.DESC_OVR_INTERFACE;
		} else if (/* is class */ Flags.isAbstract(flags)) {
			return JavaPluginImages.DESC_OVR_ABSTRACT_CLASS;
		}
		return null;
	}

	private void fireChange(IJavaScriptElement[] elements) {
		if (fListeners != null && !fListeners.isEmpty()) {
			LabelProviderChangedEvent event= new LabelProviderChangedEvent(this, elements);
			Object[] listeners= fListeners.getListeners();
			for (int i= 0; i < listeners.length; i++) {
				((ILabelProviderListener) listeners[i]).labelProviderChanged(event);
			}
		}
	}
	
	private void processDelta(IJavaScriptElementDelta delta, List result) {
		IJavaScriptElement elem= delta.getElement();
		
		boolean isChanged= delta.getKind() == IJavaScriptElementDelta.CHANGED;
		boolean isRemoved= delta.getKind() == IJavaScriptElementDelta.REMOVED;
		int flags= delta.getFlags();
		
		switch (elem.getElementType()) {
			case IJavaScriptElement.JAVASCRIPT_PROJECT:
				if (isRemoved || (isChanged && 
						(flags & IJavaScriptElementDelta.F_CLOSED) != 0)) {
					return;
				}
				processChildrenDelta(delta, result);
				return;
			case IJavaScriptElement.PACKAGE_FRAGMENT_ROOT:
				if (isRemoved || (isChanged && (
						(flags & IJavaScriptElementDelta.F_ARCHIVE_CONTENT_CHANGED) != 0 ||
						(flags & IJavaScriptElementDelta.F_REMOVED_FROM_CLASSPATH) != 0))) {
					return;
				}
				processChildrenDelta(delta, result);
				return;
			case IJavaScriptElement.PACKAGE_FRAGMENT:
				if (isRemoved)
					return;
				processChildrenDelta(delta, result);
				return;
			case IJavaScriptElement.TYPE:
			case IJavaScriptElement.CLASS_FILE:
				return;
			case IJavaScriptElement.JAVASCRIPT_MODEL:
				processChildrenDelta(delta, result);
				return;
			case IJavaScriptElement.JAVASCRIPT_UNIT:
				// Not the primary compilation unit. Ignore it 
				if (!JavaModelUtil.isPrimary((IJavaScriptUnit) elem)) {
					return;
				}

				if (isChanged &&  ((flags & IJavaScriptElementDelta.F_CONTENT) != 0 || (flags & IJavaScriptElementDelta.F_FINE_GRAINED) != 0)) {
					if (delta.getAffectedChildren().length == 0)
						return;
					
					result.add(elem);
				}
				return;
			default:
				// fields, methods, imports ect
				return;
		}	
	}
	
	private boolean processChildrenDelta(IJavaScriptElementDelta delta, List result) {
		IJavaScriptElementDelta[] children= delta.getAffectedChildren();
		for (int i= 0; i < children.length; i++) {
			processDelta(children[i], result);
		}
		return false;
	}

}
