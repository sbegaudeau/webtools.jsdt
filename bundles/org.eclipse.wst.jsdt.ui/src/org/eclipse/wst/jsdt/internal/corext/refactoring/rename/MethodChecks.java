/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.corext.refactoring.rename;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.RefactoringStatusContext;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.ITypeHierarchy;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.dom.IFunctionBinding;
import org.eclipse.wst.jsdt.core.dom.Modifier;
import org.eclipse.wst.jsdt.internal.corext.Corext;
import org.eclipse.wst.jsdt.internal.corext.refactoring.Checks;
import org.eclipse.wst.jsdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.wst.jsdt.internal.corext.refactoring.base.JavaStatusContext;
import org.eclipse.wst.jsdt.internal.corext.refactoring.base.RefactoringStatusCodes;
import org.eclipse.wst.jsdt.internal.corext.refactoring.util.JavaElementUtil;
import org.eclipse.wst.jsdt.internal.corext.util.JavaModelUtil;
import org.eclipse.wst.jsdt.internal.corext.util.JdtFlags;
import org.eclipse.wst.jsdt.internal.corext.util.Messages;
import org.eclipse.wst.jsdt.internal.corext.util.MethodOverrideTester;

public class MethodChecks {

	//no instances
	private MethodChecks(){
	}
	
	public static boolean isVirtual(IFunction method) throws JavaScriptModelException {
		if (method.isConstructor())
			return false;
		if (JdtFlags.isPrivate(method))	
			return false;
		if (JdtFlags.isStatic(method))	
			return false;
		if (method.getDeclaringType()==null)
			return false;
		return true;	
	}	
	
	public static boolean isVirtual(IFunctionBinding methodBinding){
		if (methodBinding.isConstructor())
			return false;
		if (Modifier.isPrivate(methodBinding.getModifiers()))	//TODO is this enough?
			return false;
		if (Modifier.isStatic(methodBinding.getModifiers()))	//TODO is this enough?
			return false;
		return true;	
	}
	
	public static RefactoringStatus checkIfOverridesAnother(IFunction method, ITypeHierarchy hierarchy) throws JavaScriptModelException {
		IFunction overrides= MethodChecks.overridesAnotherMethod(method, hierarchy);
		if (overrides == null)
			return null;

		RefactoringStatusContext context= JavaStatusContext.create(overrides);
		String message= Messages.format(RefactoringCoreMessages.MethodChecks_overrides, 
				new String[]{JavaElementUtil.createMethodSignature(overrides), JavaModelUtil.getFullyQualifiedName(overrides.getDeclaringType())});
		return RefactoringStatus.createStatus(RefactoringStatus.FATAL, message, context, Corext.getPluginId(), RefactoringStatusCodes.OVERRIDES_ANOTHER_METHOD, overrides);
	}
	
	/**
	 * Checks if the given method is declared in an interface. If the method's declaring type
	 * is an interface the method returns <code>false</code> if it is only declared in that
	 * interface.
	 */
	public static RefactoringStatus checkIfComesFromInterface(IFunction method, ITypeHierarchy hierarchy, IProgressMonitor monitor) throws JavaScriptModelException {
		IFunction inInterface= MethodChecks.isDeclaredInInterface(method, hierarchy, monitor);
			
		if (inInterface == null)
			return null;

		RefactoringStatusContext context= JavaStatusContext.create(inInterface);
		String message= Messages.format(RefactoringCoreMessages.MethodChecks_implements, 
				new String[]{JavaElementUtil.createMethodSignature(inInterface), JavaModelUtil.getFullyQualifiedName(inInterface.getDeclaringType())});
		return RefactoringStatus.createStatus(RefactoringStatus.FATAL, message, context, Corext.getPluginId(), RefactoringStatusCodes.METHOD_DECLARED_IN_INTERFACE, inInterface);
	}
	
	/**
	 * Checks if the given method is declared in an interface. If the method's declaring type
	 * is an interface the method returns <code>false</code> if it is only declared in that
	 * interface.
	 */
	public static IFunction isDeclaredInInterface(IFunction method, ITypeHierarchy hierarchy, IProgressMonitor monitor) throws JavaScriptModelException {
		Assert.isTrue(isVirtual(method));
		IProgressMonitor subMonitor= new SubProgressMonitor(monitor, 1);
		try {
			IType[] classes= hierarchy.getAllClasses();
			subMonitor.beginTask("", classes.length); //$NON-NLS-1$
			for (int i= 0; i < classes.length; i++) {
				final IType clazz= classes[i];
				IType[] superinterfaces= null;
				if (clazz.equals(hierarchy.getType()))
					superinterfaces= hierarchy.getAllSuperInterfaces(clazz);
				else
					superinterfaces= clazz.newSupertypeHierarchy(new SubProgressMonitor(subMonitor, 1)).getAllSuperInterfaces(clazz);
				for (int j= 0; j < superinterfaces.length; j++) {
					IFunction found= Checks.findSimilarMethod(method, superinterfaces[j]);
					if (found != null && !found.equals(method))
						return found;
				}
				subMonitor.worked(1);
			}
			return null;
		} finally {
			subMonitor.done();
		}
	}

	public static IFunction overridesAnotherMethod(IFunction method, ITypeHierarchy hierarchy) throws JavaScriptModelException {
		MethodOverrideTester tester= new MethodOverrideTester(method.getDeclaringType(), hierarchy);
		IFunction found= tester.findDeclaringMethod(method, true);
		boolean overrides= (found != null && !found.equals(method) && (!JdtFlags.isStatic(found)) && (!JdtFlags.isPrivate(found)));
		if (overrides)
			return found;
		else
			return null;
	}
	
	/**
	 * Locates the topmost method of an override ripple and returns it. If none
	 * is found, null is returned.
	 *
	 * @param method the IFunction which may be part of a ripple
	 * @param typeHierarchy a ITypeHierarchy of the declaring type of the method. May be null
	 * @param monitor an IProgressMonitor
	 * @return the topmost method of the ripple, or null if none
	 * @throws JavaScriptModelException
	 */
	public static IFunction getTopmostMethod(IFunction method, ITypeHierarchy typeHierarchy, IProgressMonitor monitor) throws JavaScriptModelException {

		Assert.isNotNull(method);

		ITypeHierarchy hierarchy= typeHierarchy;
		IFunction topmostMethod= null;
		final IType declaringType= method.getDeclaringType();
		if (declaringType==null)
			return method;
		if (!declaringType.isInterface()) {
			if ((hierarchy == null) || !declaringType.equals(hierarchy.getType()))
				hierarchy= declaringType.newTypeHierarchy(monitor);
			
			IFunction inInterface= isDeclaredInInterface(method, hierarchy, monitor);
			if (inInterface != null && !inInterface.equals(method))
				topmostMethod= inInterface;
		}
		if (topmostMethod == null) {
			if (hierarchy == null)
				hierarchy= declaringType.newSupertypeHierarchy(monitor);
			IFunction overrides= overridesAnotherMethod(method, hierarchy);
			if (overrides != null && !overrides.equals(method))
				topmostMethod= overrides;
		}
		return topmostMethod;
	}

	/**
	 * Finds all overridden methods of a certain method.
	 * 
	 */
	public static IFunction[] getOverriddenMethods(IFunction method, IProgressMonitor monitor) throws CoreException {

		Assert.isNotNull(method);
		return RippleMethodFinder2.getRelatedMethods(method, monitor, null);
	}
}
