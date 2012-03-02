/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.ui.breakpoints;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.ui.texteditor.IMarkerUpdater;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.dom.JavaScriptUnit;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptBreakpoint;
import org.eclipse.wst.jsdt.debug.core.breakpoints.IJavaScriptLineBreakpoint;
import org.eclipse.wst.jsdt.debug.core.model.JavaScriptDebugModel;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.javaeditor.ASTProvider;

/**
 * A marker updater for {@link IJavaScriptBreakpoint}s
 * 
 * @since 1.1
 */
public class JavaScriptBreakpointUpdater implements IMarkerUpdater {

	/**
	 * Constructor
	 */
	public JavaScriptBreakpointUpdater() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.IMarkerUpdater#getMarkerType()
	 */
	public String getMarkerType() {
		return IJavaScriptBreakpoint.MARKER_ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.IMarkerUpdater#getAttribute()
	 */
	public String[] getAttribute() {
		return new String[] {IMarker.LINE_NUMBER};
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.IMarkerUpdater#updateMarker(org.eclipse.core.resources.IMarker, org.eclipse.jface.text.IDocument, org.eclipse.jface.text.Position)
	 */
	public boolean updateMarker(IMarker marker, IDocument document, Position position) {
		if(position.isDeleted()) {
			return false;
		}
		IBreakpointManager manager = DebugPlugin.getDefault().getBreakpointManager();
		IBreakpoint breakpoint = manager.getBreakpoint(marker);
		if(breakpoint == null) {
			return false;
		}
		IFile file = (IFile) marker.getResource();
		if(JavaScriptCore.isJavaScriptLikeFileName(file.getName())) {
			IJavaScriptUnit unit = JavaScriptCore.createCompilationUnitFrom(file);
			if(unit == null) {
				return false;
			}
			JavaScriptUnit jsunit = JavaScriptPlugin.getDefault().getASTProvider().getAST(unit, ASTProvider.WAIT_YES, null);
			try {
				BreakpointLocationFinder finder = new BreakpointLocationFinder(jsunit, document.getLineOfOffset(position.getOffset())+1, true);
				jsunit.accept(finder);
				if(finder.getLocation() == BreakpointLocationFinder.UNKNOWN) {
					return false;
				}
				int line = finder.getLineNumber();
				if(MarkerUtilities.getLineNumber(marker) == line) {
					//if there exists a breakpoint on the line remove this one
					if(isLineBreakpoint(marker)) {
						ensureRanges(document, marker, line);
						return lineBreakpointExists(marker.getResource(), ((IJavaScriptLineBreakpoint)breakpoint).getScriptPath(), line, marker, true) == null;
					}
					return true;
				}
				if(isLineBreakpoint(marker) & line == -1) {
					return false;
				}
				if(lineBreakpointExists(marker.getResource(), ((IJavaScriptLineBreakpoint)breakpoint).getScriptPath(), line, marker, false) != null) {
					return false;
				}
				MarkerUtilities.setLineNumber(marker, line);
				if(isLineBreakpoint(marker)) {
					ensureRanges(document, marker, line);
				}
				return true;
			}
			catch(BadLocationException ble) {
				JavaScriptDebugUIPlugin.log(ble);
			} catch (CoreException e) {
				JavaScriptDebugUIPlugin.log(e);
			}
		}
		return true;
	}
	
	/**
	 * Returns if the given marker is of the type {@link IJavaScriptLineBreakpoint#MARKER_ID}
	 * @param marker
	 * @return true if the marker is a {@link IJavaScriptLineBreakpoint} marker
	 */
	boolean isLineBreakpoint(IMarker marker) {
		return MarkerUtilities.isMarkerType(marker, IJavaScriptLineBreakpoint.MARKER_ID);
	}
	
	/**
	 * Corrects the {@link IMarker#CHAR_START} and {@link IMarker#CHAR_END} values as needed
	 * 
	 * @param document
	 * @param marker
	 * @param line
	 * @throws BadLocationException
	 */
	void ensureRanges(IDocument document, IMarker marker, int line) throws BadLocationException {
		if(line < 0 || line > document.getNumberOfLines()) {
			return;
		}
		IRegion region = document.getLineInformation(line - 1);
		int charstart = region.getOffset();
		int charend = charstart + region.getLength();
		MarkerUtilities.setCharStart(marker, charstart);
		MarkerUtilities.setCharEnd(marker, charend);
	}
	
	/**
	 * Looks up the {@link IJavaScriptLineBreakpoint} that is associated with the given marker. Returns <code>null</code> if one 
	 * does not exist
	 * 
	 * @param resource
	 * @param typeName
	 * @param lineNumber
	 * @param currentmarker
	 * @param useid if the id of the markers should be compared
	 * @return the {@link IJavaScriptLineBreakpoint} for the given marker or <code>null</code> if one does not exist
	 * @throws CoreException
	 */
	IJavaScriptLineBreakpoint lineBreakpointExists(IResource resource, String typeName, int lineNumber, IMarker currentmarker, boolean useid) throws CoreException {
		String modelId = JavaScriptDebugModel.MODEL_ID;
		IBreakpointManager manager = DebugPlugin.getDefault().getBreakpointManager();
		IBreakpoint[] breakpoints = manager.getBreakpoints(modelId);
		for (int i = 0; i < breakpoints.length; i++) {
			if ((breakpoints[i] instanceof IJavaScriptLineBreakpoint)) {
				IJavaScriptLineBreakpoint breakpoint = (IJavaScriptLineBreakpoint) breakpoints[i];
				if(breakpoint.getLineNumber() == lineNumber) {
					IMarker marker = breakpoint.getMarker();
					if (marker != null && 
							marker.exists() && 
							marker.getType().equals(IJavaScriptLineBreakpoint.MARKER_ID) && 
							pathsEqual(breakpoint.getScriptPath(), typeName) &&
							resource.equals(marker.getResource())) {
						if(useid) {
							if(currentmarker.getId() != marker.getId()) {
								return breakpoint;
							}
							return null;
						}
						return breakpoint;
					}
				}
				
			}
		}
		return null;
	}
	
	/**
	 * Returns if n1 equals n2, where both being <code>null</code> is also considered equal
	 * @param n1
	 * @param n2
	 * @return 
	 */
	boolean pathsEqual(String n1, String n2) {
		if(n1 == null) {
			return n2 == null;
		}
		return n1.equals(n2);
	}
}
