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
import org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference;

/**
 * A parameterized type encapsulates a type with type arguments,
 */
public class ParameterizedTypeBinding extends ReferenceBinding implements Substitution {

	private ReferenceBinding type; // must ensure the type is resolved
	public TypeBinding[] arguments;
	public LookupEnvironment environment;
	public char[] genericTypeSignature;
	public ReferenceBinding superclass;
	public ReferenceBinding[] superInterfaces;
	public FieldBinding[] fields;
	public ReferenceBinding[] memberTypes;
	public MethodBinding[] methods;
	private ReferenceBinding enclosingType;

	public ParameterizedTypeBinding(ReferenceBinding type, TypeBinding[] arguments,  ReferenceBinding enclosingType, LookupEnvironment environment){

		this.environment = environment;
		this.enclosingType = enclosingType; // never unresolved, never lazy per construction
//		if (enclosingType != null && enclosingType.isGenericType()) {
//			RuntimeException e = new RuntimeException("PARAM TYPE with GENERIC ENCLOSING");
//			e.printStackTrace();
//			throw e;
//		}
		initialize(type, arguments);
		if (type instanceof UnresolvedReferenceBinding)
			((UnresolvedReferenceBinding) type).addWrapper(this, environment);
		if (arguments != null) {
			for (int i = 0, l = arguments.length; i < l; i++)
				if (arguments[i] instanceof UnresolvedReferenceBinding)
					((UnresolvedReferenceBinding) arguments[i]).addWrapper(this, environment);
		}
		this.tagBits |=  TagBits.HasUnresolvedTypeVariables; // cleared in resolve()
	}

	/**
	 * May return an UnresolvedReferenceBinding.
	 * @see ParameterizedTypeBinding#genericType()
	 */
	protected ReferenceBinding actualType() {
		return this.type;
	}

	/**
	 * Iterate type arguments, and validate them according to corresponding variable bounds.
	 */
	public void boundCheck(Scope scope, TypeReference[] argumentReferences) {
		if ((this.tagBits & TagBits.PassedBoundCheck) == 0) {
			boolean hasErrors = false;
			if (!hasErrors) this.tagBits |= TagBits.PassedBoundCheck; // no need to recheck it in the future
		}
	}
	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#canBeInstantiated()
	 */
	public boolean canBeInstantiated() {
		return ((this.tagBits & TagBits.HasDirectWildcard) == 0) && super.canBeInstantiated(); // cannot instantiate param type with wildcard arguments
	}
	/**
	 * Perform capture conversion for a parameterized type with wildcard arguments
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#capture(Scope,int)
	 */
	public TypeBinding capture(Scope scope, int position) {
		if ((this.tagBits & TagBits.HasDirectWildcard) == 0)
			return this;

		TypeBinding[] originalArguments = arguments;
		int length = originalArguments.length;
		TypeBinding[] capturedArguments = new TypeBinding[length];

		// Retrieve the type context for capture bindingKey
		ReferenceBinding contextType = scope.enclosingSourceType();
		if (contextType != null) contextType = contextType.outermostEnclosingType(); // maybe null when used programmatically by DOM

		for (int i = 0; i < length; i++) {
			TypeBinding argument = originalArguments[i];
			capturedArguments[i] = argument;
		}
		return null;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#computeId()
	 */
	public void computeId() {
		this.id = TypeIds.NoId;
	}

	public char[] computeUniqueKey(boolean isLeaf) {
	    StringBuffer sig = new StringBuffer(10);
	    ReferenceBinding enclosing;
		if (isMemberType() && ((enclosing = enclosingType()).isParameterizedType())) {
		    char[] typeSig = enclosing.computeUniqueKey(false/*not a leaf*/);
		    for (int i = 0; i < typeSig.length-1; i++) sig.append(typeSig[i]); // copy all but trailing semicolon
		    sig.append('.').append(sourceName());
		} else if(this.type.isLocalType()){
			LocalTypeBinding localTypeBinding = (LocalTypeBinding) this.type;
			enclosing = localTypeBinding.enclosingType();
			ReferenceBinding temp;
			while ((temp = enclosing.enclosingType()) != null)
				enclosing = temp;
			char[] typeSig = enclosing.computeUniqueKey(false/*not a leaf*/);
		    for (int i = 0; i < typeSig.length-1; i++) sig.append(typeSig[i]); // copy all but trailing semicolon
			sig.append('$');
			sig.append(localTypeBinding.sourceStart);
		} else {
		    char[] typeSig = this.type.computeUniqueKey(false/*not a leaf*/);
		    for (int i = 0; i < typeSig.length-1; i++) sig.append(typeSig[i]); // copy all but trailing semicolon
		}
		ReferenceBinding captureSourceType = null;
		if (this.arguments != null) {
		    sig.append('<');
		    for (int i = 0, length = this.arguments.length; i < length; i++) {
		    	TypeBinding typeBinding = this.arguments[i];
		        sig.append(typeBinding.computeUniqueKey(false/*not a leaf*/));
		    }
		    sig.append('>');
		}
		sig.append(';');
		if (captureSourceType != null && captureSourceType != this.type) {
			// contains a capture binding
			sig.insert(0, "&"); //$NON-NLS-1$
			sig.insert(0, captureSourceType.computeUniqueKey(false/*not a leaf*/));
		}

		int sigLength = sig.length();
		char[] uniqueKey = new char[sigLength];
		sig.getChars(0, sigLength, uniqueKey, 0);
		return uniqueKey;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#constantPoolName()
	 */
	public char[] constantPoolName() {
		return this.type.constantPoolName(); // erasure
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#debugName()
	 */
	public String debugName() {
	    StringBuffer nameBuffer = new StringBuffer(10);
		nameBuffer.append(this.type.sourceName());
		if (this.arguments != null) {
			nameBuffer.append('<');
		    for (int i = 0, length = this.arguments.length; i < length; i++) {
		        if (i > 0) nameBuffer.append(',');
		        nameBuffer.append(this.arguments[i].debugName());
		    }
		    nameBuffer.append('>');
		}
	    return nameBuffer.toString();
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#enclosingType()
	 */
	public ReferenceBinding enclosingType() {
	    return this.enclosingType;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.Substitution#environment()
	 */
	public LookupEnvironment environment() {
		return this.environment;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#fieldCount()
	 */
	public int fieldCount() {
		return this.type.fieldCount(); // same as erasure (lazy)
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#fields()
	 */
	public FieldBinding[] fields() {
		if ((tagBits & TagBits.AreFieldsComplete) != 0)
			return this.fields;

		try {
			FieldBinding[] originalFields = this.type.fields();
			int length = originalFields.length;
			FieldBinding[] parameterizedFields = new FieldBinding[length];
			for (int i = 0; i < length; i++)
				// substitute all fields, so as to get updated declaring class at least
			this.fields = parameterizedFields;
		} finally {
			// if the original fields cannot be retrieved (ex. AbortCompilation), then assume we do not have any fields
			if (this.fields == null)
				this.fields = Binding.NO_FIELDS;
			tagBits |= TagBits.AreFieldsComplete;
		}
		return this.fields;
	}

	/**
	 * Return the original generic type from which the parameterized type got instantiated from.
	 * This will perform lazy resolution automatically if needed.
	 * @see ParameterizedTypeBinding#actualType() if no resolution is required (unlikely)
	 */
	public ReferenceBinding genericType() {
		if (this.type instanceof UnresolvedReferenceBinding)
			((UnresolvedReferenceBinding) this.type).resolve(this.environment, false);
		return this.type;
	}

	/**
	 * Ltype<param1 ... paramN>;
	 * LY<TT;>;
	 */
	public char[] genericTypeSignature() {
	    if (this.genericTypeSignature == null) {
		    StringBuffer sig = new StringBuffer(10);
			if (this.isMemberType() && this.enclosingType().isParameterizedType()) {
			    char[] typeSig = this.enclosingType().genericTypeSignature();
			    for (int i = 0; i < typeSig.length-1; i++) sig.append(typeSig[i]); // copy all but trailing semicolon
			    sig.append('.').append(this.sourceName());
			} else {
			    char[] typeSig = this.type.signature();
			    for (int i = 0; i < typeSig.length-1; i++) sig.append(typeSig[i]); // copy all but trailing semicolon
			}
			if (this.arguments != null) {
			    sig.append('<');
			    for (int i = 0, length = this.arguments.length; i < length; i++) {
			        sig.append(this.arguments[i].genericTypeSignature());
			    }
			    sig.append('>');
			}
			sig.append(';');
			int sigLength = sig.length();
			this.genericTypeSignature = new char[sigLength];
			sig.getChars(0, sigLength, this.genericTypeSignature, 0);
	    }
		return this.genericTypeSignature;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#getAnnotationTagBits()
	 */
	public long getAnnotationTagBits() {
		return this.type.getAnnotationTagBits();
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#getExactConstructor(TypeBinding[])
	 */
	public MethodBinding getExactConstructor(TypeBinding[] argumentTypes) {
		int argCount = argumentTypes.length;
		MethodBinding match = null;

		if ((tagBits & TagBits.AreMethodsComplete) != 0) { // have resolved all arg types & return type of the methods
			long range;
			if ((range = ReferenceBinding.binarySearch(TypeConstants.INIT, this.methods)) >= 0) {
				nextMethod: for (int imethod = (int)range, end = (int)(range >> 32); imethod <= end; imethod++) {
					MethodBinding method = methods[imethod];
					if (method.parameters.length == argCount) {
						TypeBinding[] toMatch = method.parameters;
						for (int iarg = 0; iarg < argCount; iarg++)
							if (toMatch[iarg] != argumentTypes[iarg])
								continue nextMethod;
						if (match != null) return null; // collision case
						match = method;
					}
				}
			}
		} else {
			MethodBinding[] matchingMethods = getMethods(TypeConstants.INIT); // takes care of duplicates & default abstract methods
			nextMethod : for (int m = matchingMethods.length; --m >= 0;) {
				MethodBinding method = matchingMethods[m];
				TypeBinding[] toMatch = method.parameters;
				if (toMatch.length == argCount) {
					for (int p = 0; p < argCount; p++)
						if (toMatch[p] != argumentTypes[p])
							continue nextMethod;
						if (match != null) return null; // collision case
						match = method;
				}
			}
		}
		return match;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#getExactMethod(char[], TypeBinding[],CompilationUnitScope)
	 */
	public MethodBinding getExactMethod(char[] selector, TypeBinding[] argumentTypes, CompilationUnitScope refScope) {
		// sender from refScope calls recordTypeReference(this)
		int argCount = argumentTypes.length;
		boolean foundNothing = true;
		MethodBinding match = null;

		if ((tagBits & TagBits.AreMethodsComplete) != 0) { // have resolved all arg types & return type of the methods
			long range;
			if ((range = ReferenceBinding.binarySearch(selector, this.methods)) >= 0) {
				nextMethod: for (int imethod = (int)range, end = (int)(range >> 32); imethod <= end; imethod++) {
					MethodBinding method = methods[imethod];
					foundNothing = false; // inner type lookups must know that a method with this name exists
					if (method.parameters.length == argCount) {
						TypeBinding[] toMatch = method.parameters;
						for (int iarg = 0; iarg < argCount; iarg++)
							if (toMatch[iarg] != argumentTypes[iarg])
								continue nextMethod;
						if (match != null) return null; // collision case
						match = method;
					}
				}
			}
		} else {
			MethodBinding[] matchingMethods = getMethods(selector); // takes care of duplicates & default abstract methods
			foundNothing = matchingMethods == Binding.NO_METHODS;
			nextMethod : for (int m = matchingMethods.length; --m >= 0;) {
				MethodBinding method = matchingMethods[m];
				TypeBinding[] toMatch = method.parameters;
				if (toMatch.length == argCount) {
					for (int p = 0; p < argCount; p++)
						if (toMatch[p] != argumentTypes[p])
							continue nextMethod;
						if (match != null) return null; // collision case
						match = method;
				}
			}
		}
		if (match != null) {
			// cannot be picked up as an exact match if its a possible anonymous case, such as:
			// class A<T extends Number> { public void id(T t) {} }
			// class B<TT> extends A<Integer> { public <ZZ> void id(Integer i) {} }
			return match;
		}

		if (foundNothing && (this.arguments == null || this.arguments.length <= 1)) {
			if (superclass() != null) {
				if (refScope != null)
					refScope.recordTypeReference(superclass);
				return superclass.getExactMethod(selector, argumentTypes, refScope);
			}
		}
		return null;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#getField(char[], boolean)
	 */
	public FieldBinding getField(char[] fieldName, boolean needResolve) {
		fields(); // ensure fields have been initialized... must create all at once unlike methods
		return ReferenceBinding.binarySearch(fieldName, this.fields);
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#getMemberType(char[])
	 */
	public ReferenceBinding getMemberType(char[] typeName) {
		memberTypes(); // ensure memberTypes have been initialized... must create all at once unlike methods
		int typeLength = typeName.length;
		for (int i = this.memberTypes.length; --i >= 0;) {
			ReferenceBinding memberType = this.memberTypes[i];
			if (memberType.sourceName.length == typeLength && CharOperation.equals(memberType.sourceName, typeName))
				return memberType;
		}
		return null;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#getMethods(char[])
	 */
	public MethodBinding[] getMethods(char[] selector) {
		if (this.methods != null) {
			long range;
			if ((range = ReferenceBinding.binarySearch(selector, this.methods)) >= 0) {
				int start = (int) range;
				int length = (int) (range >> 32) - start + 1;
				// cannot optimize since some clients rely on clone array
				// if (start == 0 && length == this.methods.length)
				//	return this.methods; // current set is already interesting subset
				MethodBinding[] result;
				System.arraycopy(this.methods, start, result = new MethodBinding[length], 0, length);
				return result;
			}
		}
		if ((tagBits & TagBits.AreMethodsComplete) != 0)
			return Binding.NO_METHODS; // have created all the methods and there are no matches

		MethodBinding[] parameterizedMethods = null;
		try {
		    MethodBinding[] originalMethods = this.type.getMethods(selector);
		    int length = originalMethods.length;
		    if (length == 0) return Binding.NO_METHODS;

		    parameterizedMethods = new MethodBinding[length];

		    if (this.methods == null) {
				MethodBinding[] temp = new MethodBinding[length];
				System.arraycopy(parameterizedMethods, 0, temp, 0, length);
				this.methods = temp; // must be a copy of parameterizedMethods since it will be returned below
		    } else {
				int total = length + this.methods.length;
				MethodBinding[] temp = new MethodBinding[total];
				System.arraycopy(parameterizedMethods, 0, temp, 0, length);
				System.arraycopy(this.methods, 0, temp, length, this.methods.length);
				if (total > 1)
					ReferenceBinding.sortMethods(temp, 0, total); // resort to ensure order is good
				this.methods = temp;
			}
		    return parameterizedMethods;
		} finally {
			// if the original methods cannot be retrieved (ex. AbortCompilation), then assume we do not have any methods
		    if (parameterizedMethods == null)
		        this.methods = parameterizedMethods = Binding.NO_METHODS;
		}
	}

	public boolean hasMemberTypes() {
	    return this.type.hasMemberTypes();
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#implementsMethod(FunctionBinding)
	 */
	public boolean implementsMethod(MethodBinding method) {
		return this.type.implementsMethod(method); // erasure
	}

	void initialize(ReferenceBinding someType, TypeBinding[] someArguments) {
		this.type = someType;
		this.sourceName = someType.sourceName;
		this.compoundName = someType.compoundName;
		this.fPackage = someType.fPackage;
		this.fileName = someType.fileName;
		// should not be set yet
		// this.superclass = null;
		// this.superInterfaces = null;
		// this.fields = null;
		// this.methods = null;
		this.modifiers = someType.modifiers & ~ExtraCompilerModifiers.AccGenericSignature; // discard generic signature, will compute later
		// only set AccGenericSignature if parameterized or have enclosing type required signature
		if (someArguments != null) {
			this.modifiers |= ExtraCompilerModifiers.AccGenericSignature;
		} else if (this.enclosingType != null) {
			this.modifiers |= (this.enclosingType.modifiers & ExtraCompilerModifiers.AccGenericSignature);
			this.tagBits |= this.enclosingType.tagBits & TagBits.HasTypeVariable;
		}
		if (someArguments != null) {
			this.arguments = someArguments;
			for (int i = 0, length = someArguments.length; i < length; i++) {
				TypeBinding someArgument = someArguments[i];
				boolean isWildcardArgument = someArgument.isWildcard();
				if (isWildcardArgument) {
					this.tagBits |= TagBits.HasDirectWildcard;
				}
				
				this.tagBits |= TagBits.IsBoundParameterizedType;
			    this.tagBits |= someArgument.tagBits & TagBits.HasTypeVariable;
			}
		}
		this.tagBits |= someType.tagBits & (TagBits.IsLocalType| TagBits.IsMemberType | TagBits.IsNestedType);
		this.tagBits &= ~(TagBits.AreFieldsComplete|TagBits.AreMethodsComplete);
	}

	protected void initializeArguments() {
	    // do nothing for true parameterized types (only for raw types)
	}

	public boolean isEquivalentTo(TypeBinding otherType) {
		if (this == otherType)
		    return true;
	    if (otherType == null)
	        return false;
        return false;
	}

	public boolean isIntersectingWith(TypeBinding otherType) {
		if (this == otherType)
		    return true;
	    if (otherType == null)
	        return false;
        return false;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#isParameterizedType()
	 */
	public boolean isParameterizedType() {
	    return true;
	}

	public int kind() {
		return TYPE;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#memberTypes()
	 */
	public ReferenceBinding[] memberTypes() {
		if (this.memberTypes == null) {
			try {
				ReferenceBinding[] originalMemberTypes = this.type.memberTypes();
				int length = originalMemberTypes.length;
				ReferenceBinding[] parameterizedMemberTypes = new ReferenceBinding[length];
				this.memberTypes = parameterizedMemberTypes;
			} finally {
				// if the original fields cannot be retrieved (ex. AbortCompilation), then assume we do not have any fields
				if (this.memberTypes == null)
					this.memberTypes = Binding.NO_MEMBER_TYPES;
			}
		}
		return this.memberTypes;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#methods()
	 */
	public MethodBinding[] methods() {
		if ((tagBits & TagBits.AreMethodsComplete) != 0)
			return this.methods;

		try {
		    MethodBinding[] originalMethods = this.type.methods();
		    int length = originalMethods.length;
		    MethodBinding[] parameterizedMethods = new MethodBinding[length];
		    this.methods = parameterizedMethods;
		} finally {
			// if the original methods cannot be retrieved (ex. AbortCompilation), then assume we do not have any methods
		    if (this.methods == null)
		        this.methods = Binding.NO_METHODS;

			tagBits |=  TagBits.AreMethodsComplete;
		}
		return this.methods;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#qualifiedPackageName()
	 */
	public char[] qualifiedPackageName() {
		return this.type.qualifiedPackageName();
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#qualifiedSourceName()
	 */
	public char[] qualifiedSourceName() {
		return this.type.qualifiedSourceName();
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.Binding#readableName()
	 */
	public char[] readableName() {
	    StringBuffer nameBuffer = new StringBuffer(10);
		if (this.isMemberType()) {
			nameBuffer.append(CharOperation.concat(this.enclosingType().readableName(), sourceName, '.'));
		} else {
			nameBuffer.append(CharOperation.concatWith(this.type.compoundName, '.'));
		}
		if (this.arguments != null) {
			nameBuffer.append('<');
		    for (int i = 0, length = this.arguments.length; i < length; i++) {
		        if (i > 0) nameBuffer.append(',');
		        nameBuffer.append(this.arguments[i].readableName());
		    }
		    nameBuffer.append('>');
		}
		int nameLength = nameBuffer.length();
		char[] readableName = new char[nameLength];
		nameBuffer.getChars(0, nameLength, readableName, 0);
	    return readableName;
	}

	ReferenceBinding resolve() {
		if ((this.tagBits & TagBits.HasUnresolvedTypeVariables) == 0)
			return this;

		this.tagBits &= ~TagBits.HasUnresolvedTypeVariables; // can be recursive so only want to call once
		ReferenceBinding resolvedType = BinaryTypeBinding.resolveType(this.type, this.environment, false); // still part of parameterized type ref
		if (this.arguments != null) {
			int argLength = this.arguments.length;
			for (int i = 0; i < argLength; i++)
				BinaryTypeBinding.resolveType(this.arguments[i], this.environment, this, i);
			// arity check
			

			return this; // cannot reach here as AbortCompilation is thrown
			
			// check argument type compatibility... REMOVED for now since incremental build will propagate change & detect in source
//			for (int i = 0; i < argLength; i++) {
//			    TypeBinding resolvedArgument = this.arguments[i];
//				if (refTypeVariables[i].boundCheck(this, resolvedArgument) != TypeConstants.OK) {
//					this.environment.problemReporter.typeMismatchError(resolvedArgument, refTypeVariables[i], resolvedType, null);
//			    }
//			}
		}
		return this;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.Binding#shortReadableName()
	 */
	public char[] shortReadableName() {
	    StringBuffer nameBuffer = new StringBuffer(10);
		if (this.isMemberType()) {
			nameBuffer.append(CharOperation.concat(this.enclosingType().shortReadableName(), sourceName, '.'));
		} else {
			nameBuffer.append(this.type.sourceName);
		}
		if (this.arguments != null) {
			nameBuffer.append('<');
		    for (int i = 0, length = this.arguments.length; i < length; i++) {
		        if (i > 0) nameBuffer.append(',');
		        nameBuffer.append(this.arguments[i].shortReadableName());
		    }
		    nameBuffer.append('>');
		}
		int nameLength = nameBuffer.length();
		char[] shortReadableName = new char[nameLength];
		nameBuffer.getChars(0, nameLength, shortReadableName, 0);
	    return shortReadableName;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#signature()
	 */
	public char[] signature() {
	    if (this.signature == null) {
	        this.signature = this.type.signature();  // erasure
	    }
		return this.signature;
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding#sourceName()
	 */
	public char[] sourceName() {
		return this.type.sourceName();
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#superclass()
	 */
	public ReferenceBinding superclass() {
	    if (this.superclass == null) {
	        // note: Object cannot be generic
	        ReferenceBinding genericSuperclass = this.type.superclass();
	        if (genericSuperclass == null) return null; // e.g. interfaces
		    this.superclass = genericSuperclass;
	    }
		return this.superclass;
	}

	public void swapUnresolved(UnresolvedReferenceBinding unresolvedType, ReferenceBinding resolvedType, LookupEnvironment env) {
		boolean update = false;
		if (this.type == unresolvedType) {
			this.type = resolvedType; // cannot be raw since being parameterized below
			update = true;
			ReferenceBinding enclosing = resolvedType.enclosingType();
			if (enclosing != null) {
				this.enclosingType = enclosing; // needed when binding unresolved member type
			}
		}
		if (this.arguments != null) {
			for (int i = 0, l = this.arguments.length; i < l; i++) {
				if (this.arguments[i] == unresolvedType) {
					this.arguments[i] = resolvedType;
					update = true;
				}
			}
		}
		if (update)
			initialize(this.type, this.arguments);
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#syntheticEnclosingInstanceTypes()
	 */
	public ReferenceBinding[] syntheticEnclosingInstanceTypes() {
		return this.type.syntheticEnclosingInstanceTypes();
	}

	/**
	 * @see org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding#syntheticOuterLocalVariables()
	 */
	public SyntheticArgumentBinding[] syntheticOuterLocalVariables() {
		return this.type.syntheticOuterLocalVariables();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
	    StringBuffer buffer = new StringBuffer(30);
		if (isDeprecated()) buffer.append("deprecated "); //$NON-NLS-1$
		if (isPublic()) buffer.append("public "); //$NON-NLS-1$
		if (isProtected()) buffer.append("protected "); //$NON-NLS-1$
		if (isPrivate()) buffer.append("private "); //$NON-NLS-1$
		if (isAbstract() && isClass()) buffer.append("abstract "); //$NON-NLS-1$
		if (isStatic() && isNestedType()) buffer.append("static "); //$NON-NLS-1$
		if (isFinal()) buffer.append("final "); //$NON-NLS-1$

		if (isEnum()) buffer.append("enum "); //$NON-NLS-1$
		else if (isAnnotationType()) buffer.append("@interface "); //$NON-NLS-1$
		else if (isClass()) buffer.append("class "); //$NON-NLS-1$
		else buffer.append("interface "); //$NON-NLS-1$
		buffer.append(this.debugName());

		buffer.append("\n\textends "); //$NON-NLS-1$
		buffer.append((superclass != null) ? superclass.debugName() : "NULL TYPE"); //$NON-NLS-1$

		if (enclosingType() != null) {
			buffer.append("\n\tenclosing type : "); //$NON-NLS-1$
			buffer.append(enclosingType().debugName());
		}

		if (fields != null) {
			if (fields != Binding.NO_FIELDS) {
				buffer.append("\n/*   fields   */"); //$NON-NLS-1$
				for (int i = 0, length = fields.length; i < length; i++)
				    buffer.append('\n').append((fields[i] != null) ? fields[i].toString() : "NULL FIELD"); //$NON-NLS-1$
			}
		} else {
			buffer.append("NULL FIELDS"); //$NON-NLS-1$
		}

		if (methods != null) {
			if (methods != Binding.NO_METHODS) {
				buffer.append("\n/*   methods   */"); //$NON-NLS-1$
				for (int i = 0, length = methods.length; i < length; i++)
					buffer.append('\n').append((methods[i] != null) ? methods[i].toString() : "NULL METHOD"); //$NON-NLS-1$
			}
		} else {
			buffer.append("NULL METHODS"); //$NON-NLS-1$
		}

//		if (memberTypes != null) {
//			if (memberTypes != NoMemberTypes) {
//				buffer.append("\n/*   members   */");
//				for (int i = 0, length = memberTypes.length; i < length; i++)
//					buffer.append('\n').append((memberTypes[i] != null) ? memberTypes[i].toString() : "NULL TYPE");
//			}
//		} else {
//			buffer.append("NULL MEMBER TYPES");
//		}

		buffer.append("\n\n"); //$NON-NLS-1$
		return buffer.toString();

	}
}
