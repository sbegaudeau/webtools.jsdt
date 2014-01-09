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
package org.eclipse.wst.jsdt.core.tests.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.wst.jsdt.core.*;
import org.eclipse.wst.jsdt.internal.core.hierarchy.RegionBasedTypeHierarchy;
import org.eclipse.wst.jsdt.internal.core.hierarchy.TypeHierarchy;

import junit.framework.Test;

public class TypeHierarchySerializationTests extends AbstractJavaModelTests {
	private static final String PROJECTNAME = "TypeHierarchySerialization";
	
	IJavaScriptProject project;

public TypeHierarchySerializationTests(String name) {
	super(name);
}
public static Test suite() {
	return buildModelTestSuite(TypeHierarchySerializationTests.class);
}
private static void compare(String focus, ITypeHierarchy stored, ITypeHierarchy loaded){
	if(stored instanceof RegionBasedTypeHierarchy) {
		assertTrue("["+focus+"] hierarchies are not the same", loaded instanceof RegionBasedTypeHierarchy);
		compareRegionBasedTypeHierarchy(focus, (RegionBasedTypeHierarchy)stored,(RegionBasedTypeHierarchy)loaded);
	} else if(stored instanceof TypeHierarchy) {
		assertTrue("["+focus+"] hierarchies are not the same", loaded instanceof TypeHierarchy);
		compareTypeHierarchy(focus, (TypeHierarchy)stored,(TypeHierarchy)loaded);
	}
}
private static void compareRegionBasedTypeHierarchy(String focus, RegionBasedTypeHierarchy stored, RegionBasedTypeHierarchy loaded){
	compareTypeHierarchy(focus, stored, loaded);
}
private static void compareTypeHierarchy(String focus, TypeHierarchy stored, TypeHierarchy loaded){
	//System.out.println(stored.toString());
	
	IType type1 = stored.getType();
	IType type2 = loaded.getType();
	assertEquals("["+focus+"] focus are not the same", type1, type2);
	
	IType[] allClasses1 = stored.getAllClasses();
	IType[] allClasses2 = loaded.getAllClasses();
	compare("["+focus+"] all classes are not the same", allClasses1, allClasses2);
	
	IType[] rootClasses1 = stored.getRootClasses();
	IType[] rootClasses2 = loaded.getRootClasses();
	compare("["+focus+"] all roots are not the same", rootClasses1, rootClasses2);
	
	Object[] missingTypes1 = stored.missingTypes.toArray();
	Object[] missingTypes2 = loaded.missingTypes.toArray();
	compare("["+focus+"] all missing types are not the same", missingTypes1, missingTypes2);
	
	for (int i = 0; i < allClasses1.length; i++) {
		IType aType = allClasses1[i];
		
		int cachedFlags1 = stored.getCachedFlags(aType);
		int cachedFlags2 = loaded.getCachedFlags(aType);
		assertEquals("["+focus+"] flags are not the same for "+aType.getFullyQualifiedName(), cachedFlags1, cachedFlags2);
		
		IType superclass1 = stored.getSuperclass(aType);
		IType superclass2 = loaded.getSuperclass(aType);
		assertEquals("["+focus+"] superclass are not the same for "+aType.getFullyQualifiedName(), superclass1, superclass2);
		
		IType[] subclasses1 = stored.getSubclasses(aType);
		IType[] subclasses2 = loaded.getSubclasses(aType);
		compare("["+focus+"] all subclasses are not the same for "+aType.getFullyQualifiedName(), subclasses1, subclasses2);
	}
}
private static void compare(String msg, Object[] types1, Object[] types2) {
	if(types1 == null) {
		assertTrue(msg, types2 == null);
	} else {
		assertTrue(msg, types2 != null);
		assertTrue(msg, types1.length == types2.length);
		for (int i = 0; i < types1.length; i++) {
			boolean found = false;
			for (int j = 0; j < types2.length; j++) {
				if(types1[i] == null && types1[j] == null) {
					found = true;
				} else if(types1[i] != null && types1[i].equals(types2[j])) {
					found = true;
				}
			}
			assertTrue(msg, found);
		}
	}
}
public void setUpSuite() throws Exception {
	super.setUpSuite();

	project = setUpJavaProject(PROJECTNAME);
}

/* (non-Javadoc)
 * @see org.eclipse.wst.jsdt.core.tests.model.SuiteOfTestCases#tearDownSuite()
 */
public void tearDownSuite() throws Exception {
	deleteProject(PROJECTNAME);
	
	super.tearDownSuite();
}
private static void testFocusHierarchy(IType type, IJavaScriptProject project) throws JavaScriptModelException{
	ITypeHierarchy h1 = type.newTypeHierarchy(project, null);

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	h1.store(outputStream, null);

	byte[] bytes = outputStream.toByteArray();
	ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
	ITypeHierarchy h2 = type.loadTypeHierachy(inputStream, null);
	
	compare(type.getElementName(), h1, h2);
	
	h2.refresh(null);
	compare(type.getElementName(), h1, h2);
}
public void test001() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit(PROJECTNAME, "src", "p1", "X.js");
	IType type = cu.getType("X");
	testFocusHierarchy(type, project);
}
public void test002() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit(PROJECTNAME, "src", "p1", "Y.js");
	IType type = cu.getType("Y");
	testFocusHierarchy(type, project);
}
public void test003() throws JavaScriptModelException {
	IJavaScriptUnit cu = getCompilationUnit(PROJECTNAME, "src", "p1", "Z.js");
	IType type = cu.getType("Z");
	testFocusHierarchy(type, project);
}
}
