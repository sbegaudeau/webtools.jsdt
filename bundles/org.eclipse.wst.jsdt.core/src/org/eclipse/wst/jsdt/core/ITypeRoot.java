/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
 * Represents an entire Java type root (either an <code>ICompilationUnit</code>
 * or an <code>IClassFile</code>).
 * 
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 *
 * @see ICompilationUnit Note that methods {@link #findPrimaryType()} and {@link #getElementAt(int)}
 * 	were already implemented in this interface respectively since version 3.0 and version 1.0.
 * @see IClassFile Note that method {@link #getWorkingCopy(WorkingCopyOwner, IProgressMonitor)}
 * 	was already implemented in this interface since version 3.0.
 * @since 3.3
 */
public interface ITypeRoot extends IJavaElement, IParent, IOpenable, ISourceReference, ICodeAssist {

/**
 * Finds the primary type of this Java type root (that is, the type with the same name as the
 * compilation unit, or the type of a class file), or <code>null</code> if no such a type exists.
 * 
 * @return the found primary type of this Java type root, or <code>null</code> if no such a type exists
 */
IType findPrimaryType();

/**
 * Returns the smallest element within this Java type root that 
 * includes the given source position (that is, a method, field, etc.), or
 * <code>null</code> if there is no element other than the Java type root
 * itself at the given position, or if the given position is not
 * within the source range of the source of this Java type root.
 *
 * @param position a source position inside the Java type root
 * @return the innermost Java element enclosing a given source position or <code>null</code>
 *	if none (excluding the Java type root).
 * @throws JavaModelException if the Java type root does not exist or if an
 *	exception occurs while accessing its corresponding resource
 */
IJavaElement getElementAt(int position) throws JavaModelException;
	
/**
 * Returns a shared working copy on this compilation unit or class file using the given working copy owner to create
 * the buffer. If this is already a working copy of the given owner, the element itself is returned.
 * This API can only answer an already existing working copy if it is based on the same
 * original Java type root AND was using the same working copy owner (that is, as defined by {@link Object#equals}).	 
 * <p>
 * The life time of a shared working copy is as follows:
 * <ul>
 * <li>The first call to {@link #getWorkingCopy(WorkingCopyOwner, IProgressMonitor)} 
 * 	creates a new working copy for this element</li>
 * <li>Subsequent calls increment an internal counter.</li>
 * <li>A call to {@link ICompilationUnit#discardWorkingCopy()} decrements the internal counter.</li>
 * <li>When this counter is 0, the working copy is discarded.
 * </ul>
 * So users of this method must discard exactly once the working copy.
 * <p>
 * Note that the working copy owner will be used for the life time of the shared working copy, that is if the 
 * working copy is closed then reopened, this owner will be used.
 * The buffer will be automatically initialized with the original's Java type root content upon creation.
 * <p>
 * When the shared working copy instance is created, an ADDED IJavaElementDelta is reported on this
 * working copy.
 * </p><p>
 * A working copy can be created on a not-yet existing compilation unit.
 * In particular, such a working copy can then be committed in order to create
 * the corresponding compilation unit.
 * </p><p>
 * Note that possible problems of this working copy are reported using this method. only
 * if the given working copy owner returns a problem requestor for this working copy
 * (see {@link WorkingCopyOwner#getProblemRequestor(ICompilationUnit)}).
 * </p>
 * 
 * @param owner the working copy owner that creates a buffer that is used to get the content 
 * 				of the working copy
 * @param monitor a progress monitor used to report progress while opening this compilation unit
 *                 or <code>null</code> if no progress should be reported 
 * @throws JavaModelException if the contents of this element can
 *   	not be determined. 
 * @return a new working copy of this Java type root using the given owner to create
 *		the buffer, or this Java type root if it is already a working copy
 */
ICompilationUnit getWorkingCopy(WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaModelException;


/**
 * Returns the field with the specified name
 * in this type (for example, <code>"bar"</code>).
 * This is a handle-only method.  The field may or may not exist.
 * 
 * @param name the given name
 * @return the field with the specified name in this type
 */
IField getField(String name);
/**
 * Returns the fields declared by this type.
 * If this is a source type, the results are listed in the order
 * in which they appear in the source, otherwise, the results are
 * in no particular order.  For binary types, this includes synthetic fields.
 *
 * @exception JavaModelException if this element does not exist or if an
 *		exception occurs while accessing its corresponding resource.
 * @return the fields declared by this type
 */
IField[] getFields() throws JavaModelException;

/**
 * Returns the method with the specified name and parameter types
 * in this type (for example, <code>"foo", {"I", "QString;"}</code>).
 * To get the handle for a constructor, the name specified must be the
 * simple name of the enclosing type.
 * This is a handle-only method.  The method may or may not be present.
 * <p>
 * The type signatures may be either unresolved (for source types)
 * or resolved (for binary types), and either basic (for basic types)
 * or rich (for parameterized types). See {@link Signature} for details.
 * </p>
 * 
 * @param name the given name
 * @param parameterTypeSignatures the given parameter types
 * @return the method with the specified name and parameter types in this type
 */
IMethod getMethod(String name, String[] parameterTypeSignatures);

/**
 * Returns the methods and constructors declared by this type.
 * For binary types, this may include the special <code>&lt;clinit&gt</code>; method 
 * and synthetic methods.
 * If this is a source type, the results are listed in the order
 * in which they appear in the source, otherwise, the results are
 * in no particular order.
 *
 * @exception JavaModelException if this element does not exist or if an
 *		exception occurs while accessing its corresponding resource.
 * @return the methods and constructors declared by this type
 */
IMethod[] getMethods() throws JavaModelException;

IType getType(String name);

}
