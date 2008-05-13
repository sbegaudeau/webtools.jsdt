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
package org.eclipse.wst.jsdt.internal.corext.util;

import org.eclipse.core.runtime.Assert;
import org.eclipse.wst.jsdt.core.Flags;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IMember;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.BodyDeclaration;
import org.eclipse.wst.jsdt.core.dom.IBinding;
import org.eclipse.wst.jsdt.core.dom.IFunctionBinding;
import org.eclipse.wst.jsdt.core.dom.ITypeBinding;
import org.eclipse.wst.jsdt.core.dom.IVariableBinding;
import org.eclipse.wst.jsdt.core.dom.Modifier;
import org.eclipse.wst.jsdt.core.dom.TypeDeclaration;


public class JdtFlags {
	private JdtFlags(){
	}
	
	public static final String VISIBILITY_STRING_PRIVATE= 	"private";		//$NON-NLS-1$
	public static final String VISIBILITY_STRING_PACKAGE= 	"";				//$NON-NLS-1$
	public static final String VISIBILITY_STRING_PROTECTED= 	"protected";	//$NON-NLS-1$
	public static final String VISIBILITY_STRING_PUBLIC= 	"public";		//$NON-NLS-1$
	

	public static final int VISIBILITY_CODE_INVALID= 	-1;

	public static boolean isAbstract(IMember member) throws JavaScriptModelException{
		if (isInterfaceOrAnnotationMethod(member))
			return true;
		return Flags.isAbstract(member.getFlags());	
	}
	
	public static boolean isAbstract(IFunctionBinding member) {
		if (isInterfaceOrAnnotationMember(member))
			return true;
		return Modifier.isAbstract(member.getModifiers());	
	}

	public static boolean isDeprecated(IMember member) throws JavaScriptModelException{
		return Flags.isDeprecated(member.getFlags());
	}

	public static boolean isFinal(IMember member) throws JavaScriptModelException{
		if (isInterfaceOrAnnotationField(member))
			return true;
		if (isAnonymousType(member))	
			return true;
		if (isEnumConstant(member))
			return true;
		return Flags.isFinal(member.getFlags());
	}

	public static boolean isNative(IMember member) throws JavaScriptModelException{
		return Flags.isNative(member.getFlags());
	}

	public static boolean isPackageVisible(IMember member) throws JavaScriptModelException{
		return (! isPrivate(member) && ! isProtected(member) && ! isPublic(member));
	}

	public static boolean isPackageVisible(BodyDeclaration bodyDeclaration) {
		return (! isPrivate(bodyDeclaration) && ! isProtected(bodyDeclaration) && ! isPublic(bodyDeclaration));
	}
	
	public static boolean isPackageVisible(IBinding binding) {
		return (! isPrivate(binding) && ! isProtected(binding) && ! isPublic(binding));
	}
	
	public static boolean isPrivate(IMember member) throws JavaScriptModelException{
		return Flags.isPrivate(member.getFlags());
	}

	public static boolean isPrivate(BodyDeclaration bodyDeclaration) {
		return Modifier.isPrivate(bodyDeclaration.getModifiers());
	}
	
	public static boolean isPrivate(IBinding binding) {
		return Modifier.isPrivate(binding.getModifiers());
	}

	public static boolean isProtected(IMember member) throws JavaScriptModelException{
		return Flags.isProtected(member.getFlags());
	}

	public static boolean isProtected(BodyDeclaration bodyDeclaration) {
		return Modifier.isProtected(bodyDeclaration.getModifiers());
	}
	
	public static boolean isProtected(IBinding binding) {
		return Modifier.isProtected(binding.getModifiers());
	}

	public static boolean isPublic(IMember member) throws JavaScriptModelException{
		if (isInterfaceOrAnnotationMember(member))
			return true;
		if (isEnumConstant(member))
			return true;
		return Flags.isPublic(member.getFlags());
	}
	
	public static boolean isPublic(IBinding binding) {
		if (isInterfaceOrAnnotationMember(binding))
			return true;
		return Modifier.isPublic(binding.getModifiers());
	}
	

	public static boolean isPublic(BodyDeclaration bodyDeclaration) {
		if (isInterfaceOrAnnotationMember(bodyDeclaration))
			return true;
		return Modifier.isPublic(bodyDeclaration.getModifiers());
	}

	public static boolean isStatic(IMember member) throws JavaScriptModelException{
		if (isNestedInterfaceOrAnnotation(member))
			return true;
		if (member.getElementType() != IJavaScriptElement.METHOD && isInterfaceOrAnnotationMember(member))
			return true;
		if (isEnumConstant(member))
			return true;
		return Flags.isStatic(member.getFlags());
	}

	public static boolean isStatic(IFunctionBinding methodBinding){
		return Modifier.isStatic(methodBinding.getModifiers());
	}

	public static boolean isStatic(IVariableBinding variableBinding){
		if (isInterfaceOrAnnotationMember(variableBinding))
			return true;
		return Modifier.isStatic(variableBinding.getModifiers());
	}

	public static boolean isStrictfp(IMember member) throws JavaScriptModelException{
		return Flags.isStrictfp(member.getFlags());
	}

	public static boolean isSynchronized(IMember member) throws JavaScriptModelException{
		return Flags.isSynchronized(member.getFlags());
	}

	public static boolean isSynthetic(IMember member) throws JavaScriptModelException{
		return Flags.isSynthetic(member.getFlags());
	}

	public static boolean isAnnotation(IMember member) throws JavaScriptModelException{
		return Flags.isAnnotation(member.getFlags());
	}

	public static boolean isEnum(IMember member) throws JavaScriptModelException{
		return Flags.isEnum(member.getFlags());
	}

	public static boolean isVarargs(IFunction method) throws JavaScriptModelException{
		return Flags.isVarargs(method.getFlags());
	}

	public static boolean isTransient(IMember member) throws JavaScriptModelException{
		return Flags.isTransient(member.getFlags());
	}

	public static boolean isVolatile(IMember member) throws JavaScriptModelException{
		return Flags.isVolatile(member.getFlags());
	}
	
	private static boolean isInterfaceOrAnnotationMethod(IMember member) throws JavaScriptModelException {
		return member.getElementType() == IJavaScriptElement.METHOD && isInterfaceOrAnnotationMember(member);
	}

	private static boolean isInterfaceOrAnnotationField(IMember member) throws JavaScriptModelException {
		return member.getElementType() == IJavaScriptElement.FIELD && isInterfaceOrAnnotationMember(member);
	}

	private static boolean isInterfaceOrAnnotationMember(IMember member) throws JavaScriptModelException {
		return member.getDeclaringType() != null && JavaModelUtil.isInterfaceOrAnnotation(member.getDeclaringType());
	}
	
	private static boolean isInterfaceOrAnnotationMember(IBinding binding) {
		ITypeBinding declaringType= null;
		if (binding instanceof IVariableBinding) {
			declaringType= ((IVariableBinding) binding).getDeclaringClass();
		} else if (binding instanceof IFunctionBinding) {
			declaringType= ((IFunctionBinding) binding).getDeclaringClass();
		} else if (binding instanceof ITypeBinding) {
			declaringType= ((ITypeBinding) binding).getDeclaringClass();
		}
		return declaringType != null && (declaringType.isInterface() || declaringType.isAnnotation());
	}
	
	private static boolean isInterfaceOrAnnotationMember(BodyDeclaration bodyDeclaration) {
		boolean isInterface= (bodyDeclaration.getParent() instanceof TypeDeclaration) &&
				((TypeDeclaration)bodyDeclaration.getParent()).isInterface();
		return 	isInterface ;
	}

	private static boolean isNestedInterfaceOrAnnotation(IMember member) throws JavaScriptModelException{
		return member.getElementType() == IJavaScriptElement.TYPE && 
				member.getDeclaringType() != null &&
				JavaModelUtil.isInterfaceOrAnnotation((IType)member);
	}
	
	private static boolean isEnumConstant(IMember member) throws JavaScriptModelException {
		return member.getElementType() == IJavaScriptElement.FIELD && isEnum(member);
	}

	private static boolean isAnonymousType(IMember member) throws JavaScriptModelException {
		return member.getElementType() == IJavaScriptElement.TYPE && 
				((IType)member).isAnonymous();
	}

	public static int getVisibilityCode(IMember member) throws JavaScriptModelException {
		if (isPublic(member))
			return Modifier.PUBLIC;
		else if (isProtected(member))
			return Modifier.PROTECTED;
		else if (isPackageVisible(member))
			return Modifier.NONE;
		else if (isPrivate(member))
			return Modifier.PRIVATE;
		Assert.isTrue(false);
		return VISIBILITY_CODE_INVALID;
	}
	
	public static int getVisibilityCode(BodyDeclaration bodyDeclaration) {
		if (isPublic(bodyDeclaration))
			return Modifier.PUBLIC;
		else if (isProtected(bodyDeclaration))
			return Modifier.PROTECTED;
		else if (isPackageVisible(bodyDeclaration))
			return Modifier.NONE;
		else if (isPrivate(bodyDeclaration))
			return Modifier.PRIVATE;
		Assert.isTrue(false);
		return VISIBILITY_CODE_INVALID;
	}
	
	public static int getVisibilityCode(IBinding binding) {
		if (isPublic(binding))
			return Modifier.PUBLIC;
		else if (isProtected(binding))
			return Modifier.PROTECTED;
		else if (isPackageVisible(binding))
			return Modifier.NONE;
		else if (isPrivate(binding))
			return Modifier.PRIVATE;
		Assert.isTrue(false);
		return VISIBILITY_CODE_INVALID;
	}
	
	
	public static String getVisibilityString(int visibilityCode){
		if (Modifier.isPublic(visibilityCode))
			return VISIBILITY_STRING_PUBLIC;
		if (Modifier.isProtected(visibilityCode))
			return VISIBILITY_STRING_PROTECTED;
		if (Modifier.isPrivate(visibilityCode))
			return VISIBILITY_STRING_PRIVATE;
		return VISIBILITY_STRING_PACKAGE;
	}

	public static int getVisibilityCode(String visibilityString) {
		Assert.isNotNull(visibilityString);
		if (VISIBILITY_STRING_PACKAGE.equals(visibilityString))
			return 0;
		else if (VISIBILITY_STRING_PRIVATE.equals(visibilityString))
			return Modifier.PRIVATE;
		else if (VISIBILITY_STRING_PROTECTED.equals(visibilityString))
			return Modifier.PROTECTED;
		else if (VISIBILITY_STRING_PUBLIC.equals(visibilityString))
			return Modifier.PUBLIC;
		return VISIBILITY_CODE_INVALID;
	}

	public static void assertVisibility(int visibility){
		Assert.isTrue(	visibility == Modifier.PUBLIC ||
		            	visibility == Modifier.PROTECTED ||
		            	visibility == Modifier.NONE ||
		            	visibility == Modifier.PRIVATE);  
	}
	
	public static boolean isHigherVisibility(int newVisibility, int oldVisibility){
		assertVisibility(oldVisibility);
		assertVisibility(newVisibility);
		switch (oldVisibility) {
			case Modifier.PRIVATE :
				return 	newVisibility == Modifier.NONE
						||	newVisibility == Modifier.PUBLIC
						||  newVisibility == Modifier.PROTECTED;
			case Modifier.NONE :
				return 	newVisibility == Modifier.PUBLIC
						||  newVisibility == Modifier.PROTECTED;

			case Modifier.PROTECTED :
				return newVisibility == Modifier.PUBLIC;

			case Modifier.PUBLIC :
				return false;
			default: 
				Assert.isTrue(false);
				return false;	
		}
	}
	
	public static int getLowerVisibility(int visibility1, int visibility2) {
		if (isHigherVisibility(visibility1, visibility2))
			return visibility2;
		else
			return visibility1;
	}
	
	public static int clearAccessModifiers(int flags) {
		return clearFlag(Modifier.PROTECTED | Modifier.PUBLIC | Modifier.PRIVATE, flags);
	}

	public static int clearFlag(int flag, int flags){
		return flags & ~ flag;
	}
}
