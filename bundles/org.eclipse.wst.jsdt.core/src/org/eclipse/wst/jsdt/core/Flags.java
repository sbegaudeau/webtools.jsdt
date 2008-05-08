/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     IBM Corporation - added constant AccDefault
 *     IBM Corporation - added constants AccBridge and AccVarargs for J2SE 1.5
 *******************************************************************************/
package org.eclipse.wst.jsdt.core;

import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;

/**
 * Utility class for decoding modifier flags in JavaScript elements.
 * <p>
 * This class provides static methods only; it is not intended to be
 * instantiated or subclassed by clients.
 * </p>
 *
 * @see IMember#getFlags()
 *  
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public final class Flags {

	/**
	 * Constant representing the absence of any flag
	 */
	public static final int AccDefault = ClassFileConstants.AccDefault;
	/**
	 * Public access flag. 
	 *
	 * <b>This flag only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static final int AccPublic = ClassFileConstants.AccPublic;
	/**
	 * Private access flag.  
	 *
	 * <b>This flag only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static final int AccPrivate = ClassFileConstants.AccPrivate;
	/**
	 * Protected access flag.  
	 *
	 * <b>This flag only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static final int AccProtected = ClassFileConstants.AccProtected;
	/**
	 * Static access flag. 
	 */
	public static final int AccStatic = ClassFileConstants.AccStatic;
	/*
	 * Final access flag. 
	 */
	public static final int AccFinal = ClassFileConstants.AccFinal;
	/*
	 * Synchronized access flag.
	 */
	public static final int AccSynchronized = ClassFileConstants.AccSynchronized;
	/*
	 * Volatile property flag. 
	 */
	public static final int AccVolatile = ClassFileConstants.AccVolatile;
	/*
	 * Transient property flag. 
	 */
	public static final int AccTransient = ClassFileConstants.AccTransient;
	/*
	 * Native property flag.
	 */
	public static final int AccNative = ClassFileConstants.AccNative;
	/**
	 * Interface property flag.
	 *
	 * <b>This flag only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static final int AccInterface = ClassFileConstants.AccInterface;
	/**
	 * Abstract property flag. 
	 *
	 * <b>This flag only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static final int AccAbstract = ClassFileConstants.AccAbstract;
	/*
	 * Strictfp property flag. See The Java Virtual Machine Specification for more details.
	 */
	public static final int AccStrictfp = ClassFileConstants.AccStrictfp;
	/**
	 * Super property flag.  
	 *
	 * <b>This flag only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static final int AccSuper = ClassFileConstants.AccSuper;
	/*
	 * Synthetic property flag.  
	 */
	public static final int AccSynthetic = ClassFileConstants.AccSynthetic;
	/**
	 * Deprecated property flag.  
	 */
	public static final int AccDeprecated = ClassFileConstants.AccDeprecated;

	public static final int AccBridge = ClassFileConstants.AccBridge;

	/**=
	 * Varargs method property 
	 * Used to flag variable arity method declarations.
	 *
	 * <b>This flag only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static final int AccVarargs = ClassFileConstants.AccVarargs;

	public static final int AccEnum = ClassFileConstants.AccEnum;

	public static final int AccAnnotation = ClassFileConstants.AccAnnotation;

	/**
	 * Not instantiable.
	 */
	private Flags() {
		// Not instantiable
	}
	/**
	 * Returns whether the given integer includes the <code>abstract</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>abstract</code> modifier is included
	 *
	 * <b>This method only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static boolean isAbstract(int flags) {
		return (flags & AccAbstract) != 0;
	}
	/**
	 * Returns whether the given integer includes the indication that the
	 * element is deprecated (<code>@deprecated</code> tag in Javadoc comment).
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the element is marked as deprecated
	 */
	public static boolean isDeprecated(int flags) {
		return (flags & AccDeprecated) != 0;
	}
	/*
	 * Returns whether the given integer includes the <code>final</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>final</code> modifier is included
	 */
	public static boolean isFinal(int flags) {
		return (flags & AccFinal) != 0;
	}
	/**
	 * Returns whether the given integer includes the <code>interface</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>interface</code> modifier is included
	 *
	 * <b>This method only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static boolean isInterface(int flags) {
		return (flags & AccInterface) != 0;
	}
	/*
	 * Returns whether the given integer includes the <code>native</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>native</code> modifier is included
	 */
	public static boolean isNative(int flags) {
		return (flags & AccNative) != 0;
	}
	/*
	 * Returns whether the given integer does not include one of the
	 * <code>public</code>, <code>private</code>, or <code>protected</code> flags.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if no visibility flag is set
	 *
	 * <b>This method only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static boolean isPackageDefault(int flags) {
		return (flags & (AccPublic | AccPrivate | AccProtected)) == 0;
	}
	/**
	 * Returns whether the given integer includes the <code>private</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>private</code> modifier is included
	 *
	 * <b>This method only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static boolean isPrivate(int flags) {
		return (flags & AccPrivate) != 0;
	}
	/**
	 * Returns whether the given integer includes the <code>protected</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>protected</code> modifier is included
	 *
	 * <b>This method only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static boolean isProtected(int flags) {
		return (flags & AccProtected) != 0;
	}
	/**
	 * Returns whether the given integer includes the <code>public</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>public</code> modifier is included
	 *
	 * <b>This method only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static boolean isPublic(int flags) {
		return (flags & AccPublic) != 0;
	}
	/**
	 * Returns whether the given integer includes the <code>static</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>static</code> modifier is included
	 */
	public static boolean isStatic(int flags) {
		return (flags & AccStatic) != 0;
	}
	/**
	 * Returns whether the given integer includes the <code>super</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>super</code> modifier is included
	 *
	 * <b>This method only applies to ECMAScript 4 which is not yet supported</b>
	 */
	public static boolean isSuper(int flags) {
		return (flags & AccSuper) != 0;
	}
	/*
	 * Returns whether the given integer includes the <code>strictfp</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>strictfp</code> modifier is included
	 */
	public static boolean isStrictfp(int flags) {
		return (flags & AccStrictfp) != 0;
	}
	/*
	 * Returns whether the given integer includes the <code>synchronized</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>synchronized</code> modifier is included
	 */
	public static boolean isSynchronized(int flags) {
		return (flags & AccSynchronized) != 0;
	}
	/*
	 * Returns whether the given integer includes the indication that the
	 * element is synthetic.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the element is marked synthetic
	 */
	public static boolean isSynthetic(int flags) {
		return (flags & AccSynthetic) != 0;
	}
	/*
	 * Returns whether the given integer includes the <code>transient</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>transient</code> modifier is included
	 */
	public static boolean isTransient(int flags) {
		return (flags & AccTransient) != 0;
	}
	/*
	 * Returns whether the given integer includes the <code>volatile</code> modifier.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>volatile</code> modifier is included
	 */
	public static boolean isVolatile(int flags) {
		return (flags & AccVolatile) != 0;
	}

	/*
	 * Returns whether the given integer has the <code>AccBridge</code>
	 * bit set.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>AccBridge</code> flag is included
	 * @see #AccBridge
	 */
	public static boolean isBridge(int flags) {
		return (flags & AccBridge) != 0;
	}

	/*
	 * Returns whether the given integer has the <code>AccVarargs</code>
	 * bit set.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>AccVarargs</code> flag is included
	 * @see #AccVarargs
	 */
	public static boolean isVarargs(int flags) {
		return (flags & AccVarargs) != 0;
	}

	/*
	 * Returns whether the given integer has the <code>AccEnum</code>
	 * bit set.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>AccEnum</code> flag is included
	 * @see #AccEnum
	 */
	public static boolean isEnum(int flags) {
		return (flags & AccEnum) != 0;
	}

	/*
	 * Returns whether the given integer has the <code>AccAnnotation</code>
	 * bit set.
	 *
	 * @param flags the flags
	 * @return <code>true</code> if the <code>AccAnnotation</code> flag is included
	 * @see #AccAnnotation
	 */
	public static boolean isAnnotation(int flags) {
		return (flags & AccAnnotation) != 0;
	}

	/**
	 * Returns a standard string describing the given modifier flags.
	 * Only modifier flags are included in the output; deprecated,
	 * synthetic, bridge, etc. flags are ignored.
	 * <p>
	 * The flags are output in the following order:
	 * <pre>
	 *   <code>public</code> <code>protected</code> <code>private</code>
	 *   <code>static</code>
	 *   <code>abstract</code> <code>final</code> <code>native</code> <code>synchronized</code> <code>transient</code> <code>volatile</code> <code>strictfp</code>
	 * </pre>
	 * </p>
	 * <p>
	 * Examples results:
	 * <pre>
	 *	  <code>"public static final"</code>
	 *	  <code>"private native"</code>
	 * </pre>
	 * </p>
	 *
	 * @param flags the flags
	 * @return the standard string representation of the given flags
	 */
	public static String toString(int flags) {
		StringBuffer sb = new StringBuffer();

		if (isPublic(flags))
			sb.append("public "); //$NON-NLS-1$
		if (isProtected(flags))
			sb.append("protected "); //$NON-NLS-1$
		if (isPrivate(flags))
			sb.append("private "); //$NON-NLS-1$
		if (isStatic(flags))
			sb.append("static "); //$NON-NLS-1$
		if (isAbstract(flags))
			sb.append("abstract "); //$NON-NLS-1$
		if (isFinal(flags))
			sb.append("final "); //$NON-NLS-1$
		if (isNative(flags))
			sb.append("native "); //$NON-NLS-1$
		if (isSynchronized(flags))
			sb.append("synchronized "); //$NON-NLS-1$
		if (isTransient(flags))
			sb.append("transient "); //$NON-NLS-1$
		if (isVolatile(flags))
			sb.append("volatile "); //$NON-NLS-1$
		if (isStrictfp(flags))
			sb.append("strictfp "); //$NON-NLS-1$

		int len = sb.length();
		if (len == 0)
			return ""; //$NON-NLS-1$
		sb.setLength(len - 1);
		return sb.toString();
	}
}
