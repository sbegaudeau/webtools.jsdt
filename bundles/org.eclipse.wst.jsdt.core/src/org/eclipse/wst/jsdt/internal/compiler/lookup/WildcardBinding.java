/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.lookup;

import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;

/*
 * A wildcard acts as an argument for parameterized types, allowing to
 * abstract parameterized types, e.g. List<String> is not compatible with List<Object>,
 * but compatible with List<?>.
 */
public class WildcardBinding extends ReferenceBinding {

	ReferenceBinding genericType;
	int rank;
    public TypeBinding bound; // when unbound denotes the corresponding type variable (so as to retrieve its bound lazily)
    public TypeBinding[] otherBounds; // only positionned by lub computations (if so, #bound is also set) and associated to EXTENDS mode
	char[] genericSignature;
	public int boundKind;
	ReferenceBinding superclass;
	ReferenceBinding[] superInterfaces;
	TypeVariableBinding typeVariable; // corresponding variable
	LookupEnvironment environment;

	/**
	 * When unbound, the bound denotes the corresponding type variable (so as to retrieve its bound lazily)
	 */
	public WildcardBinding(ReferenceBinding genericType, int rank, TypeBinding bound, TypeBinding[] otherBounds, int boundKind, LookupEnvironment environment) {
		this.genericType = genericType;
		this.rank = rank;
	    this.boundKind = boundKind;
		this.modifiers = ClassFileConstants.AccPublic | ExtraCompilerModifiers.AccGenericSignature; // treat wildcard as public
		this.environment = environment;
		initialize(genericType, bound, otherBounds);

		if (genericType instanceof UnresolvedReferenceBinding)
			((UnresolvedReferenceBinding) genericType).addWrapper(this, environment);
		if (bound instanceof UnresolvedReferenceBinding)
			((UnresolvedReferenceBinding) bound).addWrapper(this, environment);
		this.tagBits |=  TagBits.HasUnresolvedTypeVariables; // cleared in resolve()
	}

	public int kind() {
		return WILDCARD_TYPE;
	}

	/**
	 * Returns true if the argument type satisfies the wildcard bound(s)
	 */
	public boolean boundCheck(TypeBinding argumentType) {
	    switch (this.boundKind) {
	        default: // SUPER
	        	// ? super Exception   ok for:  IOException, since it would be ok for (Exception)ioException
	            return argumentType.isCompatibleWith(this.bound);
	    }
    }
	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#canBeInstantiated()
	 */
	public boolean canBeInstantiated() {
		// cannot be asked per construction
		return false;
	}

	/**
	 * Collect the substitutes into a map for certain type variables inside the receiver type
	 * e.g.   Collection<T>.collectSubstitutes(Collection<List<X>>, Map), will populate Map with: T --> List<X>
	 * Constraints:
	 *   A << F   corresponds to:   F.collectSubstitutes(..., A, ..., CONSTRAINT_EXTENDS (1))
	 *   A = F   corresponds to:      F.collectSubstitutes(..., A, ..., CONSTRAINT_EQUAL (0))
	 *   A >> F   corresponds to:   F.collectSubstitutes(..., A, ..., CONSTRAINT_SUPER (2))
	 */
	public void collectSubstitutes(Scope scope, TypeBinding actualType, InferenceContext inferenceContext, int constraint) {

		if ((this.tagBits & TagBits.HasTypeVariable) == 0) return;
		if (actualType == TypeBinding.NULL) return;

		if (actualType.isCapture()) {
			CaptureBinding capture = (CaptureBinding) actualType;
			actualType = capture.wildcard;
		}

		switch (constraint) {
			case TypeConstants.CONSTRAINT_EXTENDS : // A << F
				break;
			case TypeConstants.CONSTRAINT_EQUAL : // A == F
				break;
			case TypeConstants.CONSTRAINT_SUPER : // A >> F
				break;
		}
	}

	/*
	 * genericTypeKey *|+|- [boundKey]
	 * p.X<T> { X<?> ... } --> Lp/X<TT;>;*
	 */
	public char[] computeUniqueKey(boolean isLeaf) {
		char[] genericTypeKey = this.genericType.computeUniqueKey(false/*not a leaf*/);
		char[] wildCardKey;
        switch (this.boundKind) {
			default: // SUPER
			    wildCardKey = CharOperation.concat(TypeConstants.WILDCARD_MINUS, this.bound.computeUniqueKey(false/*not a leaf*/));
				break;
        }
        return CharOperation.concat(genericTypeKey, wildCardKey);
       }

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#constantPoolName()
	 */
	public char[] constantPoolName() {
		return this.erasure().constantPoolName();
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#debugName()
	 */
	public String debugName() {
	    return toString();
	}

    /* (non-Javadoc)
     * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#erasure()
     */
    public TypeBinding erasure() {
    	if (this.otherBounds == null) {
	    	return typeVariable().erasure();
    	}
    	// intersection type
    	return this.bound.id == TypeIds.T_JavaLangObject
    		? this.otherBounds[0].erasure()  // use first explicit bound to improve stackmap
    		: this.bound.erasure();
    }

    /* (non-Javadoc)
     * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#signature()
     */
    public char[] genericTypeSignature() {
        if (this.genericSignature == null) {
            switch (this.boundKind) {
				default: // SUPER
				    this.genericSignature = CharOperation.concat(TypeConstants.WILDCARD_MINUS, this.bound.genericTypeSignature());
            }
        }
        return this.genericSignature;
    }

	public int hashCode() {
		return this.genericType.hashCode();
	}

	void initialize(ReferenceBinding someGenericType, TypeBinding someBound, TypeBinding[] someOtherBounds) {
		this.genericType = someGenericType;
		this.bound = someBound;
		this.otherBounds = someOtherBounds;
		if (someGenericType != null) {
			this.fPackage = someGenericType.getPackage();
		}
		if (someBound != null) {
			this.tagBits |= someBound.tagBits & TagBits.HasTypeVariable;
		}
	}

	/**
     * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#isSuperclassOf(org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding)
     */
    public boolean isSuperclassOf(ReferenceBinding otherType) {
        return false;
    }

    /**
     * Returns true if the current type denotes an intersection type: Number & Comparable<?>
     */
    public boolean isIntersectionType() {
    	return this.otherBounds != null;
    }

    /**
	 * Returns true if the type is a wildcard
	 */
	public boolean isUnboundWildcard() {
	    return false;
	}

    /**
	 * Returns true if the type is a wildcard
	 */
	public boolean isWildcard() {
	    return true;
	}

    /* (non-Javadoc)
     * @see org.eclipse.wst.jsdt.internal.compiler.lookup.Binding#readableName()
     */
    public char[] readableName() {
        switch (this.boundKind) {
			default: // SUPER
			    return CharOperation.concat(TypeConstants.WILDCARD_NAME, TypeConstants.WILDCARD_SUPER, this.bound.readableName());
        }
    }

	ReferenceBinding resolve() {
		if ((this.tagBits & TagBits.HasUnresolvedTypeVariables) == 0)
			return this;

		this.tagBits &= ~TagBits.HasUnresolvedTypeVariables;
		BinaryTypeBinding.resolveType(this.genericType, this.environment, null, 0);
		return this;
	}

    /* (non-Javadoc)
     * @see org.eclipse.wst.jsdt.internal.compiler.lookup.Binding#shortReadableName()
     */
    public char[] shortReadableName() {
        switch (this.boundKind) {
			default: // SUPER
			    return CharOperation.concat(TypeConstants.WILDCARD_NAME, TypeConstants.WILDCARD_SUPER, this.bound.shortReadableName());
        }
    }

    /**
     * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#signature()
     */
    public char[] signature() {
     	// should not be called directly on a wildcard; signature should only be asked on
    	// original methods or type erasures (which cannot denote wildcards at first level)
		if (this.signature == null) {
	        switch (this.boundKind) {
				default: // SUPER | UNBOUND
				    return this.typeVariable().signature();
	        }
		}
		return this.signature;
    }

    /* (non-Javadoc)
     * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#sourceName()
     */
    public char[] sourceName() {
        switch (this.boundKind) {
			default: // SUPER
			    return CharOperation.concat(TypeConstants.WILDCARD_NAME, TypeConstants.WILDCARD_SUPER, this.bound.sourceName());
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeVariableBinding#superclass()
     */
    public ReferenceBinding superclass() {
		if (this.superclass == null) {
			TypeBinding superType = null;
			
			TypeVariableBinding variable = this.typeVariable();
			if (variable != null) superType = variable.firstBound;
			
			this.superclass = superType instanceof ReferenceBinding && !superType.isInterface()
				? (ReferenceBinding) superType
				: environment.getResolvedType(TypeConstants.JAVA_LANG_OBJECT, null);
		}

		return this.superclass;
    }
  /*
    public ReferenceBinding superclass2() {
		if (this.superclass == null) {
			TypeBinding superType = (this.boundKind == Wildcard.EXTENDS && !this.bound.isInterface())
				? this.bound
				: null;
			this.superclass = superType instanceof ReferenceBinding && !superType.isInterface()
				? (ReferenceBinding) superType
				: environment.getResolvedType(TypeConstants.JAVA_LANG_OBJECT, null);

//			TypeBinding superType = null;
//			if (this.boundKind == Wildcard.EXTENDS && !this.bound.isInterface()) {
//				superType = this.bound;
//			} else {
//				TypeVariableBinding variable = this.typeVariable();
//				if (variable != null) superType = variable.firstBound;
//			}
//			this.superclass = superType instanceof ReferenceBinding && !superType.isInterface()
//				? (ReferenceBinding) superType
//				: environment.getType(TypeConstants.JAVA_LANG_OBJECT);
		}
		return this.superclass;
    }
*/
    /* (non-Javadoc)
     * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#superInterfaces()
     */
    public ReferenceBinding[] superInterfaces() {
        if (this.superInterfaces == null) {
        	if (this.typeVariable() != null) {
        		this.superInterfaces = this.typeVariable.superInterfaces();
        	} else {
        		this.superInterfaces = Binding.NO_SUPERINTERFACES;
        	}
        }
        return this.superInterfaces;
    }

    public ReferenceBinding[] superInterfaces2() {
        if (this.superInterfaces == null) {
        	this.superInterfaces = Binding.NO_SUPERINTERFACES;
        }
        return this.superInterfaces;
    }

	public void swapUnresolved(UnresolvedReferenceBinding unresolvedType, ReferenceBinding resolvedType, LookupEnvironment env) {
		boolean affected = false;
		if (this.genericType == unresolvedType) {
			this.genericType = resolvedType; // no raw conversion
			affected = true;
		} else if (this.bound == unresolvedType) {
			this.bound = env.convertUnresolvedBinaryToRawType(resolvedType);
			affected = true;
		}
		if (affected)
			initialize(this.genericType, this.bound, this.otherBounds);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
        switch (this.boundKind) {
			default: // SUPER
			    return new String(CharOperation.concat(TypeConstants.WILDCARD_NAME, TypeConstants.WILDCARD_SUPER, this.bound.debugName().toCharArray()));
        }
	}
	/**
	 * Returns associated type variable, or null in case of inconsistency
	 */
	public TypeVariableBinding typeVariable() {
		if (this.typeVariable == null) {
			TypeVariableBinding[] typeVariables = this.genericType.typeVariables();
			if (this.rank < typeVariables.length)
				this.typeVariable = typeVariables[this.rank];
		}
		return this.typeVariable;
	}
}
