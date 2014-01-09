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
package org.eclipse.wst.jsdt.astview.views;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;

import org.eclipse.wst.jsdt.astview.ASTViewPlugin;
import org.eclipse.wst.jsdt.core.Flags;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.dom.AST;
import org.eclipse.wst.jsdt.core.dom.CharacterLiteral;
import org.eclipse.wst.jsdt.core.dom.IBinding;
import org.eclipse.wst.jsdt.core.dom.IFunctionBinding;
import org.eclipse.wst.jsdt.core.dom.IPackageBinding;
import org.eclipse.wst.jsdt.core.dom.ITypeBinding;
import org.eclipse.wst.jsdt.core.dom.IVariableBinding;
import org.eclipse.wst.jsdt.core.dom.PrimitiveType;
import org.eclipse.wst.jsdt.core.dom.StringLiteral;

/**
 *
 */
public class Binding extends ASTAttribute {
	
	private final IBinding fBinding;
	private final String fLabel;
	private final Object fParent;
	private final boolean fIsRelevant;
	
	public Binding(Object parent, String label, IBinding binding, boolean isRelevant) {
		fParent= parent;
		fBinding= binding;
		fLabel= label;
		fIsRelevant= isRelevant;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.astview.views.ASTAttribute#getParent()
	 */
	public Object getParent() {
		return fParent;
	}
	
	public IBinding getBinding() {
		return fBinding;
	}
	

	public boolean hasBindingProperties() {
		return fBinding != null;
	}

	public boolean isRelevant() {
		return fIsRelevant;
	}
	
	
	private static boolean isType(int typeKinds, int kind) {
		return (typeKinds & kind) != 0;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.astview.views.ASTAttribute#getChildren()
	 */
	public Object[] getChildren() {
		
		if (fBinding != null) {
			ArrayList res= new ArrayList();
			res.add(new BindingProperty(this, "NAME", fBinding.getName(), true)); //$NON-NLS-1$
			res.add(new BindingProperty(this, "KEY", fBinding.getKey(), true)); //$NON-NLS-1$
			res.add(new BindingProperty(this, "IS RECOVERED", fBinding.isRecovered(), true)); //$NON-NLS-1$
			switch (fBinding.getKind()) {
				case IBinding.VARIABLE:
					IVariableBinding variableBinding= (IVariableBinding) fBinding;
					res.add(new BindingProperty(this, "IS FIELD", variableBinding.isField(), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "IS PARAMETER", variableBinding.isParameter(), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "VARIABLE ID", variableBinding.getVariableId(), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "MODIFIERS", Flags.toString(fBinding.getModifiers()), true)); //$NON-NLS-1$
					res.add(new Binding(this, "TYPE", variableBinding.getType(), true)); //$NON-NLS-1$
					res.add(new Binding(this, "DECLARING CLASS", variableBinding.getDeclaringClass(), true)); //$NON-NLS-1$
					res.add(new Binding(this, "DECLARING METHOD", variableBinding.getDeclaringMethod(), true)); //$NON-NLS-1$
					res.add(new Binding(this, "VARIABLE DECLARATION", variableBinding.getVariableDeclaration(), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "IS DEPRECATED", fBinding.isDeprecated(), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "CONSTANT VALUE", variableBinding.getConstantValue(), true)); //$NON-NLS-1$ //$NON-NLS-2$
					break;
					
				case IBinding.PACKAGE:
					IPackageBinding packageBinding= (IPackageBinding) fBinding;
					res.add(new BindingProperty(this, "IS UNNAMED", packageBinding.isUnnamed(), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "IS DEPRECATED", fBinding.isDeprecated(), true)); //$NON-NLS-1$
					break;
					
				case IBinding.TYPE:
					ITypeBinding typeBinding= (ITypeBinding) fBinding;
					res.add(new BindingProperty(this, "QUALIFIED NAME", typeBinding.getQualifiedName(), true)); //$NON-NLS-1$
					
					int typeKind= getTypeKind(typeBinding);
					boolean isRefType= isType(typeKind, REF_TYPE);
					final boolean isNonPrimitive= ! isType(typeKind, PRIMITIVE_TYPE);
					
					StringBuffer kinds= new StringBuffer("KIND:"); //$NON-NLS-1$
					if (typeBinding.isArray()) kinds.append(" isArray"); //$NON-NLS-1$
					if (typeBinding.isNullType()) kinds.append(" isNullType"); //$NON-NLS-1$
					if (typeBinding.isPrimitive()) kinds.append(" isPrimitive"); //$NON-NLS-1$
					// ref types
					if (typeBinding.isClass()) kinds.append(" isClass"); //$NON-NLS-1$
					res.add(new BindingProperty(this, kinds, true)); //$NON-NLS-1$
					
					StringBuffer generics= new StringBuffer("GENERICS:"); //$NON-NLS-1$
					if (!isType(typeKind, GENERIC | PARAMETRIZED)) {
						generics.append(" (non-generic, non-parameterized)");
					}
					res.add(new BindingProperty(this, generics, isRefType)); //$NON-NLS-1$

					res.add(new Binding(this, "ELEMENT TYPE", typeBinding.getElementType(), isType(typeKind, ARRAY_TYPE))); //$NON-NLS-1$
					res.add(new Binding(this, "COMPONENT TYPE", typeBinding.getComponentType(), isType(typeKind, ARRAY_TYPE))); //$NON-NLS-1$
					res.add(new BindingProperty(this, "DIMENSIONS", typeBinding.getDimensions(), isType(typeKind, ARRAY_TYPE))); //$NON-NLS-1$
					final String createArrayTypeLabel= "CREATE ARRAY TYPE (+1)";
					try {
						ITypeBinding arrayType= typeBinding.createArrayType(1);
						res.add(new Binding(this, createArrayTypeLabel, arrayType, true));
					} catch (RuntimeException e) {
						String msg= e.getClass().getName() + ": " + e.getLocalizedMessage();
						boolean isRelevant= ! typeBinding.getName().equals(PrimitiveType.VOID.toString()) && ! typeBinding.isRecovered();
						if (isRelevant) {
							res.add(new Error(this, createArrayTypeLabel + ": " + msg, e));
						} else {
							res.add(new BindingProperty(this, createArrayTypeLabel, msg, false));
						}
					}
					
					StringBuffer origin= new StringBuffer("ORIGIN:"); //$NON-NLS-1$
					if (typeBinding.isTopLevel()) origin.append(" isTopLevel"); //$NON-NLS-1$
					if (typeBinding.isNested()) origin.append(" isNested"); //$NON-NLS-1$
					if (typeBinding.isLocal()) origin.append(" isLocal"); //$NON-NLS-1$
					if (typeBinding.isMember()) origin.append(" isMember"); //$NON-NLS-1$
					if (typeBinding.isAnonymous()) origin.append(" isAnonymous"); //$NON-NLS-1$
					res.add(new BindingProperty(this, origin, isRefType));
					
					res.add(new BindingProperty(this, "IS FROM SOURCE", typeBinding.isFromSource(), isType(typeKind, REF_TYPE | VARIABLE_TYPE | CAPTURE_TYPE))); //$NON-NLS-1$

					res.add(new Binding(this, "PACKAGE", typeBinding.getPackage(), isRefType)); //$NON-NLS-1$
					res.add(new Binding(this, "DECLARING CLASS", typeBinding.getDeclaringClass(), isType(typeKind, REF_TYPE | VARIABLE_TYPE | CAPTURE_TYPE))); //$NON-NLS-1$
					res.add(new Binding(this, "DECLARING METHOD", typeBinding.getDeclaringMethod(), isType(typeKind, REF_TYPE | VARIABLE_TYPE | CAPTURE_TYPE))); //$NON-NLS-1$
					res.add(new BindingProperty(this, "MODIFIERS", Flags.toString(fBinding.getModifiers()), isRefType)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "BINARY NAME", typeBinding.getBinaryName(), true)); //$NON-NLS-1$
					
					res.add(new Binding(this, "TYPE DECLARATION", typeBinding.getTypeDeclaration(), isNonPrimitive)); //$NON-NLS-1$
					res.add(new Binding(this, "ERASURE", typeBinding.getErasure(), isNonPrimitive)); //$NON-NLS-1$

					res.add(new Binding(this, "SUPERCLASS", typeBinding.getSuperclass(), isRefType)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "DECLARED TYPES", typeBinding.getDeclaredTypes(), isRefType)); //$NON-NLS-1$			
					res.add(new BindingProperty(this, "DECLARED FIELDS", typeBinding.getDeclaredFields(), isRefType)); //$NON-NLS-1$			
					res.add(new BindingProperty(this, "DECLARED METHODS", typeBinding.getDeclaredMethods(), isRefType)); //$NON-NLS-1$			
					res.add(new BindingProperty(this, "IS DEPRECATED", fBinding.isDeprecated(), isRefType)); //$NON-NLS-1$
					break;
					
				case IBinding.METHOD:
					IFunctionBinding methodBinding= (IFunctionBinding) fBinding;
					res.add(new BindingProperty(this, "IS CONSTRUCTOR", methodBinding.isConstructor(), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "IS DEFAULT CONSTRUCTOR", methodBinding.isDefaultConstructor(), true)); //$NON-NLS-1$
					res.add(new Binding(this, "DECLARING CLASS", methodBinding.getDeclaringClass(), true)); //$NON-NLS-1$
					res.add(new Binding(this, "RETURN TYPE", methodBinding.getReturnType(), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "MODIFIERS", Flags.toString(fBinding.getModifiers()), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "PARAMETER TYPES", methodBinding.getParameterTypes(), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "IS VARARGS", methodBinding.isVarargs(), true)); //$NON-NLS-1$
					
					res.add(new Binding(this, "METHOD DECLARATION", methodBinding.getMethodDeclaration(), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "PARAMETER TYPES", methodBinding.getParameterTypes(), true)); //$NON-NLS-1$
					res.add(new BindingProperty(this, "IS DEPRECATED", fBinding.isDeprecated(), true)); //$NON-NLS-1$
					
					res.add(Binding.createValueAttribute(this, "DEFAULT VALUE", methodBinding.getDefaultValue()));
					
					int parameterCount= methodBinding.getParameterTypes().length;
					break;
			}
			try {
				IJavaScriptElement javaElement= fBinding.getJavaElement();
				res.add(new JavaElement(this, javaElement));
			} catch (RuntimeException e) {
				String label= ">java element: " + e.getClass().getName() + " for \"" + fBinding.getKey() + "\"";  //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				res.add(new Error(this, label, e));
				ASTViewPlugin.log("Exception thrown in IBinding#getJavaElement() for \"" + fBinding.getKey() + "\"", e);
			}
			return res.toArray();
		}
		return EMPTY;
	}

	private final static int ARRAY_TYPE= 1 << 0;
	private final static int NULL_TYPE= 1 << 1;
	private final static int VARIABLE_TYPE= 1 << 2;
	private final static int WILDCARD_TYPE= 1 << 3;
	private final static int CAPTURE_TYPE= 1 << 4;
	private final static int PRIMITIVE_TYPE= 1 << 5;

	private final static int REF_TYPE= 1 << 6;

	private final static int GENERIC= 1 << 8;
	private final static int PARAMETRIZED= 1 << 9;
	
	private int getTypeKind(ITypeBinding typeBinding) {
		if (typeBinding.isArray()) return ARRAY_TYPE;
		if (typeBinding.isNullType()) return NULL_TYPE;
		if (typeBinding.isPrimitive()) return PRIMITIVE_TYPE;
		
		return REF_TYPE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.astview.views.ASTAttribute#getLabel()
	 */
	public String getLabel() {
		StringBuffer buf= new StringBuffer(fLabel);
		buf.append(": "); //$NON-NLS-1$
		if (fBinding != null) {
			switch (fBinding.getKind()) {
				case IBinding.VARIABLE:
					IVariableBinding variableBinding= (IVariableBinding) fBinding;
					if (!variableBinding.isField()) {
						buf.append(variableBinding.getName());
					} else if (variableBinding.getDeclaringClass() == null) {
						buf.append("array type"); //$NON-NLS-1$
					} else {
						buf.append(variableBinding.getDeclaringClass().getName());
						buf.append('.');
						buf.append(variableBinding.getName());				
					}
					break;
				case IBinding.PACKAGE:
					IPackageBinding packageBinding= (IPackageBinding) fBinding;
					buf.append(packageBinding.getName());
					break;
				case IBinding.TYPE:
					ITypeBinding typeBinding= (ITypeBinding) fBinding;
					buf.append(typeBinding.getQualifiedName());
					break;
				case IBinding.METHOD:
					IFunctionBinding methodBinding= (IFunctionBinding) fBinding;
					buf.append(methodBinding.getDeclaringClass().getName());
					buf.append('.');
					buf.append(methodBinding.getName());
					buf.append('(');
					ITypeBinding[] parameters= methodBinding.getParameterTypes();
					for (int i= 0; i < parameters.length; i++) {
						if (i > 0) {
							buf.append(", "); //$NON-NLS-1$
						}
						ITypeBinding parameter= parameters[i];
						buf.append(parameter.getName());
					}
					buf.append(')');
					break;
			}
			
		} else {
			buf.append("null"); //$NON-NLS-1$
		}
		return buf.toString();

	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.astview.views.ASTAttribute#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getLabel();
	}
	
	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !obj.getClass().equals(getClass())) {
			return false;
		}
		
		Binding other= (Binding) obj;
		if (fParent == null) {
			if (other.fParent != null)
				return false;
		} else if (! fParent.equals(other.fParent)) {
			return false;
		}
		
		if (fBinding == null) {
			if (other.fBinding != null)
				return false;
		} else if (! fBinding.equals(other.fBinding)) {
			return false;
		}
		
		return true;
	}
	
	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result= fParent != null ? fParent.hashCode() : 0;
		result+= (fBinding != null && fBinding.getKey() != null ? fBinding.getKey().hashCode() : 0);
		return result;
	}

	public static String getBindingLabel(IBinding binding) {
		String label;
		if (binding == null) {
			label= ">binding"; //$NON-NLS-1$
		} else {
			switch (binding.getKind()) {
				case IBinding.VARIABLE:
					label= "> variable binding"; //$NON-NLS-1$
					break;
				case IBinding.TYPE:
					label= "> type binding"; //$NON-NLS-1$
					break;
				case IBinding.METHOD:
					label= "> method binding"; //$NON-NLS-1$
					break;
				case IBinding.PACKAGE:
					label= "> package binding"; //$NON-NLS-1$
					break;
				default:
					label= "> unknown binding"; //$NON-NLS-1$
			}
		}
		return label;
	}

	/**
	 * Creates an {@link ASTAttribute} for a value from
	 * {@link IMemberValuePairBinding#getValue()} or from
	 * {@link IFunctionBinding#getDefaultValue()}.
	 */
	public static ASTAttribute createValueAttribute(ASTAttribute parent, String name, Object value) {
		ASTAttribute res;
		if (value instanceof IBinding) {
			IBinding binding= (IBinding) value;
			res= new Binding(parent, name + ": " + getBindingLabel(binding), binding, true);
			
		} else if (value instanceof String) {
			res= new GeneralAttribute(parent, name, getEscapedStringLiteral((String) value));
			
		} else if (value instanceof Object[]) {
			res= new GeneralAttribute(parent, name, (Object[]) value);
			
		} else if (value instanceof ASTAttribute) {
			res= (ASTAttribute) value;
			
		} else {
			res= new GeneralAttribute(parent, name, value);
		}
		return res;
	}
	
	public static String getEscapedStringLiteral(String stringValue) {
		StringLiteral stringLiteral= AST.newAST(AST.JLS3).newStringLiteral();
		stringLiteral.setLiteralValue(stringValue);
		return stringLiteral.getEscapedValue();
	}
	
	public static String getEscapedCharLiteral(char charValue) {
		CharacterLiteral charLiteral= AST.newAST(AST.JLS3).newCharacterLiteral();
		charLiteral.setCharValue(charValue);
		return charLiteral.getEscapedValue();
	}
}
