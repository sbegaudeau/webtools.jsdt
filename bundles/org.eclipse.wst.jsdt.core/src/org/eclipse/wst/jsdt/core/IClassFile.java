/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Represents an entire non-editable JavaScript file.
 * non-editable JavaScript file elements need to be opened before they can be navigated.
 * If a  file cannot be parsed, its structure remains unknown. Use
 * <code>IJavaScriptElement.isStructureKnown</code> to determine whether this is the
 * case.
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 *  
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
*/

public interface IClassFile extends ITypeRoot {

/**
 * Changes this  file handle into a working copy. A new {@link IBuffer} is
 * created using the given owner. Uses the primary owner if <code>null</code> is
 * specified.
 * <p>
 * When switching to working copy mode, problems are reported to the given
 * {@link IProblemRequestor}. Note that once in working copy mode, the given
 * {@link IProblemRequestor} is ignored. Only the original {@link IProblemRequestor}
 * is used to report subsequent problems.
 * </p>
 * <p>
 * Once in working copy mode, changes to this working copy or its children are done in memory.
 * Only the new buffer is affected.
 * </p>
 * <p>
 * Using {@link IJavaScriptUnit#commitWorkingCopy(boolean, IProgressMonitor)} on the working copy
 * will throw a <code>JavaScriptModelException</code> as a file is implicitly read-only.
 * </p>
 * <p>
 * If this file was already in working copy mode, an internal counter is incremented and no
 * other action is taken on this working copy. To bring this working copy back into the original mode
 * (where it reflects the underlying resource), {@link IJavaScriptUnit#discardWorkingCopy} must be call as many
 * times as {@link #becomeWorkingCopy(IProblemRequestor, WorkingCopyOwner, IProgressMonitor)}.
 * </p>
 * <p>
 * The primary javaScript unit of a file's working copy does not exist if the file is not
 * in working copy mode (<code>classFileWorkingCopy.getPrimary().exists() == false</code>).
 * </p>
 *
 * @param problemRequestor a requestor which will get notified of problems detected during
 * 	reconciling as they are discovered. The requestor can be set to <code>null</code> indicating
 * 	that the client is not interested in problems.
 * @param owner the given {@link WorkingCopyOwner}, or <code>null</code> for the primary owner
 * @param monitor a progress monitor used to report progress while opening this compilation unit
 * 	or <code>null</code> if no progress should be reported
 * @return a working copy for this file
 * @throws JavaScriptModelException if this javaScript unit could not become a working copy.
 * @see IJavaScriptUnit#discardWorkingCopy()
 * @deprecated Use {@link ITypeRoot#getWorkingCopy(WorkingCopyOwner, IProgressMonitor)} instead.
 * 	Note that if this deprecated method is used, problems will be reported to the given problem requestor
 * 	as well as the problem requestor returned by the working copy owner (if not null).
 */
IJavaScriptUnit becomeWorkingCopy(IProblemRequestor problemRequestor, WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaScriptModelException;

/**
 * Returns the bytes contained in this  file.
 *
 * @return the bytes contained in this  file
 *
 * @exception JavaScriptModelException if this element does not exist or if an
 *      exception occurs while accessing its corresponding resource
 */
byte[] getBytes() throws JavaScriptModelException;

/**
 * Returns the first type contained in this  file.
 * This is a handle-only method. The type may or may not exist.
 *
 * @return the type contained in this file
 *
 */
IType getType();
public IType[] getTypes() throws JavaScriptModelException ;


/* 
 * Returns whether this type is edit. This is not guaranteed to be
 * instantaneous, as it may require parsing the underlying file.
 *
 * @return <code>true</code> if the  file represents a class.
 *
 * @exception JavaScriptModelException if this element does not exist or if an
 *      exception occurs while accessing its corresponding resource
 */
boolean isClass() throws JavaScriptModelException;
}
