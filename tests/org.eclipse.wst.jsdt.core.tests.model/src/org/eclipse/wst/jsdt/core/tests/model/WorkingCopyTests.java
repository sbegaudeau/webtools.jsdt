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

import junit.framework.Test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.wst.jsdt.core.IBuffer;
import org.eclipse.wst.jsdt.core.IField;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptModelStatusConstants;
import org.eclipse.wst.jsdt.core.IJavaScriptUnit;
import org.eclipse.wst.jsdt.core.IOpenable;
import org.eclipse.wst.jsdt.core.IPackageFragment;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.core.WorkingCopyOwner;
import org.eclipse.wst.jsdt.internal.core.util.Util;



public class WorkingCopyTests extends ModifyingResourceTests {
	IJavaScriptUnit cu = null;
	IJavaScriptUnit copy = null;
	
	public class TestWorkingCopyOwner extends WorkingCopyOwner {
		public IBuffer createBuffer(IJavaScriptUnit workingCopy) {
			return new TestBuffer(workingCopy);
		}
	}
	public WorkingCopyTests(String name) {
	super(name);
}

public static Test suite() {
	return buildModelTestSuite(WorkingCopyTests.class);
}
protected void setUp() throws Exception {
	super.setUp();
	
	try {
		this.createJavaProject(
			"P", 
			new String[] {"src"}, 
			new String[] {this.getSystemJsPathString(), "lib"});
//		this.createFolder("P/src");
		this.createFile("P/src/A.js", 
			"  var FIELD;\n" +
			"  var field1;\n" +
			"  var field2;\n" +
			"  function foo() {\n" +
			"  }\n" +
			"");
		this.cu = this.getCompilationUnit("P/src/A.js");
		this.copy = cu.getWorkingCopy(null);
	} catch (CoreException e) {
		e.printStackTrace();
		throw new RuntimeException(e.getMessage());
	}
}
protected void tearDown() throws Exception {
	if (this.copy != null) this.copy.discardWorkingCopy();
	this.deleteProject("P");
	super.tearDown();
}
/*
 * Ensures that cancelling a make consistent operation doesn't leave the working copy closed
 * (regression test for bug 61719 Incorrect fine grain delta after method copy-rename)
 */
public void testCancelMakeConsistent() throws JavaScriptModelException {
	String newContents =
		" function bar() {\n" +
		"  }\n" +
		"";
	this.copy.getBuffer().setContents(newContents);
	NullProgressMonitor monitor = new NullProgressMonitor();
	monitor.setCanceled(true);
	try {
		this.copy.makeConsistent(monitor);
	} catch (OperationCanceledException e) {
		// got exception
	}
	assertTrue("Working copy should be opened", this.copy.isOpen());
}

/**
 */
public void testChangeContent() throws CoreException {
	String newContents =
		"  function bar() {\n" +
		"}";
	this.copy.getBuffer().setContents(newContents);
	this.copy.reconcile(IJavaScriptUnit.NO_AST, false, null, null);
	assertSourceEquals(
		"Unexpected working copy contents",
		newContents,
		this.copy.getBuffer().getContents());
	
	this.copy.commitWorkingCopy(true, null);
	assertSourceEquals(
		"Unexpected original cu contents",
		newContents,
		this.cu.getBuffer().getContents());
}

/*
 * Ensures that one cannot commit the contents of a working copy on a read only cu. 
 */
public void testChangeContentOfReadOnlyCU1() throws CoreException {
	if (!Util.isReadOnlySupported()) {
		// Do not test if file system does not support read-only attribute
		return;
	}
	IResource resource = this.cu.getUnderlyingResource();
	boolean readOnlyFlag = Util.isReadOnly(resource);
	boolean didComplain = false;
	try {
		Util.setReadOnly(resource, true);
		this.copy.getBuffer().setContents("invalid");
		this.copy.commitWorkingCopy(true, null);
	} catch(JavaScriptModelException e){
		didComplain = true;
	} finally {
		Util.setReadOnly(resource, readOnlyFlag);
	}
	assertTrue("Should have complained about modifying a read-only unit:", didComplain);
	assertTrue("ReadOnly buffer got modified:", !this.cu.getBuffer().getContents().equals("invalid"));
}

/*
 * Ensures that one can commit the contents of a working copy on a read only cu if a pessimistic repository
 * provider allows it. 
 */
public void testChangeContentOfReadOnlyCU2() throws CoreException {
	if (!Util.isReadOnlySupported()) {
		// Do not test if file system does not support read-only attribute
		return;
	}
	String newContents =
		"  function bar() {\n" +
		"}";
	IResource resource = this.cu.getUnderlyingResource();
	IProject project = resource.getProject();
	boolean readOnlyFlag = Util.isReadOnly(resource);
	try {
		RepositoryProvider.map(project, TestPessimisticProvider.NATURE_ID);
		TestPessimisticProvider.markWritableOnSave = true;
		Util.setReadOnly(resource, true);
		
		this.copy.getBuffer().setContents(newContents);
		this.copy.commitWorkingCopy(true, null);
		assertSourceEquals(
			"Unexpected original cu contents",
			newContents,
			this.cu.getBuffer().getContents());
	} finally {
		TestPessimisticProvider.markWritableOnSave = false;
		RepositoryProvider.unmap(project);
		Util.setReadOnly(resource, readOnlyFlag);
	}
}

/**
 * Ensures that the source contents of a working copy are
 * not altered by changes to the source of the original compilation
 * unit.
 */
public void testContents() throws CoreException {
	String originalSource = this.cu.getSource();
	IField type = this.cu.getField("field1");
	assertDeletion(type);
	assertSourceEquals("source code of copy should still be original", originalSource, this.copy.getSource());
}

///**
// * Test creating a working copy on a class file with a customized buffer.
// */
//public void testOnClassFile() throws JavaScriptModelException, IOException {
//	// ensure the external JCL is copied
//	setupExternalJCL("jclMin");
//			
//	this.attachSource(this.getPackageFragmentRoot("P", this.getExternalJCLPathString()), this.getExternalJCLSourcePath().toOSString(), "src");
//	IClassFile classFile = this.getClassFile("P", this.getExternalJCLPathString(), "java.lang", "Object.class");
//	WorkingCopyOwner owner = new TestWorkingCopyOwner();
//	IJavaScriptUnit customizedCopy = classFile.getWorkingCopy(owner, null);
//	try {
//		IBuffer buffer = customizedCopy.getBuffer();
//		assertTrue("Unexpected buffer", buffer instanceof TestBuffer);	
//		assertTrue("Buffer should be initialized with source", buffer.getCharacters().length > 0);
//	} finally {
//		customizedCopy.discardWorkingCopy();
//	}
//}
/**
 * Create the compilation unit place holder for the working copy tests.
 */
public void testCreation() {
	assertTrue("Failed to create X.js compilation unit", this.cu != null && this.cu.exists());
	assertTrue("Failed to create working copy on X.js", this.copy != null && this.copy.exists());
}

/**
 * Test creating a working copy with a customized buffer.
 */
public void testCustomizedBuffer() throws JavaScriptModelException {
	WorkingCopyOwner owner = new TestWorkingCopyOwner();
	IJavaScriptUnit customizedCopy = this.cu.getWorkingCopy(owner, null);
	try {
		assertTrue("Unexpected buffer", customizedCopy.getBuffer() instanceof TestBuffer);
	} finally {
		customizedCopy.discardWorkingCopy();
	}
}
/**
 * Test closing then reopening a working copy with a customized buffer.
 */
public void testCustomizedBuffer2() throws JavaScriptModelException {
	WorkingCopyOwner owner = new TestWorkingCopyOwner();
	IJavaScriptUnit customizedCopy = this.cu.getWorkingCopy(owner, null);
	try {
		customizedCopy.close();
		customizedCopy.open(null);
		assertTrue("Unexpected buffer", customizedCopy.getBuffer() instanceof TestBuffer);		
	} finally {
		customizedCopy.discardWorkingCopy();
	}
}
/*
 * Test that deleting 2 fields in a JavaScriptCore.run() operation reports the correct delta.
 * (regression test for bug 32225 incorrect delta after deleting 2 fields)
 */
public void testDelete2Fields() throws CoreException {
	try {
		startDeltas();
		JavaScriptCore.run(
			new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					IField field1 = copy.getField("field1");
					IField field2 = copy.getField("field2");
					field1.delete(false, monitor);
					field2.delete(false, monitor);
				}
			},
			null);
		assertDeltas(
			"Unexpected delta",
			"field1[-]: {}\n" + 
			"field2[-]: {}"
		);
	} finally {
		stopDeltas();
	}
}
/**
 * Tests the general functionality of a working copy:<ul>
 * <li>ensures that the copy and original compilation unit are not equal</li>
 * <li>ensures the correct retrieval of the original element</li>
 * <li>closing the package of the compilation unit does not close the copy</li>
 * <li>ensures that working copies are unique
 * <li>ensures committing changes from working copies 
 */
public void testGeneral() throws JavaScriptModelException, CoreException {

	assertTrue("copy and actual should not be equal", !this.copy.equals(this.cu));

	IField copyfield= this.copy.getField("field1");

	assertEquals("primary should be the samel", this.cu, this.cu.getPrimary());

	assertEquals("getting working copy from a copy should yield original copy", this.copy, this.copy.getWorkingCopy(null));

	boolean ex= false;
	assertDeletion(copyfield);

	// closing the package should not close the copy
	((IOpenable)this.cu.getParent()).close();
	assertTrue("copy should still be open", this.copy.isOpen());
	
	// verify original still present
	assertTrue("actual type should still be present", this.cu.getField("field1").exists());

	// getAnother working copy
	IJavaScriptUnit copy2= this.cu.getWorkingCopy(null);
	try {
		assertTrue("working copies should be unique ", !(this.copy.equals(copy2)));
	
		// delete a method from the 2nd working copy.
		IFunction method= copy2.getFunction("foo", null);
	
		assertDeletion(method);
		IFunction originalMethod= this.cu.getFunction("foo", null);
		assertTrue("method should still be present in original", originalMethod.exists());
	
		// commit the changes from the 2nd copy.
		copy2.commitWorkingCopy(false, null);
	
		assertTrue("copy always has unsaved changes", copy2.hasUnsavedChanges());
		
		// original method should now be gone
		assertTrue("method should no longer be present in original after commit", !originalMethod.exists());
	
		// commit the changes from the 1st copy - should fail
		try {
			this.copy.commitWorkingCopy(false, null);
			assertTrue("commit should have failed", ex);
		} catch (JavaScriptModelException jme) {
		}
		
	
		// now force the update
		try {
			this.copy.commitWorkingCopy(true, null);
		} catch (JavaScriptModelException jme) {
			assertTrue("commit should work", false);
		}
	
		// now the type should be gone.
		assertTrue("original type should no longer be present", !this.cu.getField("field1").exists());
	
	
		this.copy.close();
		ex= false;
		try {
			this.copy.open(null);
		} catch (JavaScriptModelException e) {
			ex= true;
		}
		assertTrue("should be able to open working copy a 2nd time", !ex);
	
		// now discard the working copy
		this.copy.discardWorkingCopy();
		ex= false;
		try {
			this.copy.open(null);
		} catch (JavaScriptModelException e) {
			ex= true;
		}
		assertTrue("should not be able to open working copy again", ex);
	} finally {
		copy2.discardWorkingCopy();
	}
}
///**
// * Ensures that the primary element of a binary element is itself.
// */
//public void testGetPrimaryBinaryElement() throws CoreException {
//	/* Evaluate the following in a scrapbook:
//	 org.eclipse.wst.jsdt.core.tests.model.ModifyingResourceTests.generateClassFile(
//		"A",
//		"public class A {\n" +
//		"}")
//	*/
//	byte[] bytes = new byte[] {
//		-54, -2, -70, -66, 0, 3, 0, 45, 0, 10, 1, 0, 1, 65, 7, 0, 1, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 7, 0, 3, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 12, 0, 5, 0, 6, 10, 0, 4, 0, 8, 0, 33, 0, 2, 0, 4, 0, 0, 0, 
//		0, 0, 1, 0, 1, 0, 5, 0, 6, 0, 1, 0, 7, 0, 0, 0, 17, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 9, -79, 0, 0, 0, 0, 0, 0, 
//	};
//	this.createFile("P/lib/A.class", bytes);
//	IClassFile cf = this.getClassFile("P/lib/A.class");
//	IJavaScriptElement primary = cf.getPrimaryElement();
//	assertEquals("Primary element should be the same", cf, primary);
//}
/**
 * Ensures that the primary cu can be retrieved.
 */
public void testGetPrimaryCU() {
	IJavaScriptElement primary= this.copy.getPrimaryElement();
	assertTrue("Element is not a cu", primary instanceof IJavaScriptUnit && !((IJavaScriptUnit)primary).isWorkingCopy());
	assertTrue("Element should exist", primary.exists());
}
/**
 * Ensures that the primary field can be retrieved.
 */
public void testGetPrimaryField() {
	IJavaScriptElement primary = this.copy.getField("FIELD").getPrimaryElement();
	assertTrue("Element is not a field", primary instanceof IField && !((IJavaScriptUnit)primary.getParent()).isWorkingCopy());
	assertTrue("Element should exist", primary.exists());
}
///**
// * Ensures that the primary import declaration can be retrieved.
// */
//public void testGetPrimaryImportDeclaration()  {
//	IImportDeclaration imprt = copy.getImport("java.io.File");
//	IJavaScriptElement primary = imprt.getPrimaryElement();
//	assertTrue("Element should exist", primary.exists());
//}
///**
// * Ensures that the primary import container can be retrieved.
// */
//public void testGetPrimaryImportContainer() {
//	IImportContainer container = this.copy.getImportContainer();
//	IJavaScriptElement primary = container.getPrimaryElement();
//	assertTrue("Element should not be null", primary != null);
//	assertTrue("Element should exist", primary.exists());
//}
/////**
//// * Ensures that the primary initializer can be retrieved.
//// */
////public void testGetPrimaryInitializer() {
////	IType type= copy.getType("A");
////	IJavaScriptElement primary= type.getInitializer(1).getPrimaryElement();
////	assertTrue("Element should exist", primary.exists());
////}
///**
// */
//public void testGetPrimaryInnerField() {
//	IType innerType = this.copy.getType("A").getType("Inner");
//	IJavaScriptElement primary = innerType.getField("innerField").getPrimaryElement();
//	assertTrue("Element is not a field", primary instanceof IField);
//	assertTrue("Element should exist", primary.exists());
//}
///**
// */
//public void testGetPrimaryInnerMethod() throws JavaScriptModelException {
//	IType innerType = this.copy.getType("A").getType("Inner");
//	IJavaScriptElement primary = innerType.getMethods()[0].getPrimaryElement();
//	assertTrue("Element is not a method", primary instanceof IFunction);
//	assertTrue("Element should exist", primary.exists());
//}
///**
// */
//public void testGetPrimaryInnerType() {
//	IType innerInnerType = this.copy.getType("A").getType("Inner").getType("InnerInner");
//	IJavaScriptElement primary = innerInnerType.getPrimaryElement();
//	assertTrue("Element is not a method", primary instanceof IType);
//	assertTrue("Element should exist", primary.exists());
//
//	Vector hierarchy = new Vector(5);
//	IJavaScriptElement parent= primary.getParent();
//	while (parent.getElementType() > IJavaScriptElement.JAVASCRIPT_UNIT) {
//		hierarchy.addElement(parent);
//		parent = parent.getParent();
//	}
//	hierarchy.addElement(parent);
//	assertTrue("Compilation Unit should not be a working copy", !((IJavaScriptUnit)hierarchy.lastElement()).isWorkingCopy());
//}
/**
 * Ensures that the primary method can be retrieved.
 */
public void testGetPrimaryMethod() throws JavaScriptModelException {
	IJavaScriptElement primary= this.copy.getFunctions()[0].getPrimaryElement();
	assertTrue("Element is not a method", primary instanceof IFunction);
	assertTrue("Element should exist", primary.exists());
}
/**
 * Ensures that renaming a method of a working copy does
 * not alter the source of the primary compilation
 * unit.
 */
public void testRenameMethod() throws JavaScriptModelException {
	IFunction method = this.copy.getFunctions()[0];
	IJavaScriptElement primary= method.getPrimaryElement();
	method.rename("bar", false, null);
	assertEquals("Invalid name of working copy method", "bar", this.copy.getFunctions()[0].getElementName());
	assertEquals("Invalid name of primary method", "foo", primary.getElementName());
}
///**
// * Ensures that the primary package declaration can be retrieved.
// */
//public void testGetPrimaryPackageDeclaration() {
//	IPackageDeclaration pkg = this.copy.getPackageDeclaration("x.y");
//	IJavaScriptElement primary = pkg.getPrimaryElement();
//	assertTrue("Element should exist", primary.exists());
//}
///**
// * Ensures that the primary type can be retrieved.
// */
//public void testGetPrimaryType() {
//	IType type = this.copy.getType("A");
//	IJavaScriptElement primary= type.getPrimaryElement();
//	assertTrue("Element should exist", primary.exists());
//}
/**
 * Ensures that a type can be moved to another working copy.
 * (regression test for bug 7881 IType.move() clobbers editing buffer of destination element)
 */
public void testMoveTypeToAnotherWorkingCopy() throws CoreException {
	this.createFile(
		"P/src/B.js",
		"function B() {\n" +
		"}");
	IJavaScriptUnit cu2 = this.getCompilationUnit("P/src/B.js");
	IJavaScriptUnit copy2 = cu2.getWorkingCopy(null);
	try {
		IField fld1 = this.copy.getField("field1");
//		IType classB = copy2.getType("B");
		fld1.move(copy2, null, null, false, null);
		assertTrue("A should not exist", !fld1.exists());
		assertTrue("B.A should exist", copy2.getField("field1").exists());
		assertTrue("Buffer for A should not be null", this.copy.getBuffer() != null);
		assertSourceEquals("Invalid content for A", 
				"  var FIELD;\n" +
				"  var field2;\n" +
				"  function foo() {\n" +
				"  }\n" ,
			this.copy.getBuffer().getContents());
		assertTrue("Buffer for B should not be null", copy2.getBuffer() != null);
		assertSourceEquals("Invalid content for B", 
				"var field1;\n" +
				"\n" +
				"function B() {\n" +
				"}",
			copy2.getBuffer().getContents());
	} finally {
		copy2.discardWorkingCopy();
	}
}
/**
 * Test creating a shared working copy.
 */
public void testShared1() throws JavaScriptModelException {
	WorkingCopyOwner owner = new WorkingCopyOwner() {};
	IJavaScriptUnit shared = this.cu.getWorkingCopy(owner, null);
	try {
		assertTrue("Should find shared working copy", this.cu.findWorkingCopy(owner) == shared);
	} finally {
		shared.discardWorkingCopy();
	}
	assertTrue("Should not find cu with same owner", this.cu.findWorkingCopy(owner) == null);
}
/**
 * Test several call to creating shared working copy.
 */
public void testShared2() throws JavaScriptModelException {
	WorkingCopyOwner owner = new WorkingCopyOwner() {};
	IJavaScriptUnit shared = this.cu.getWorkingCopy(owner, null);
	try {
		IJavaScriptUnit shared2 = this.cu.getWorkingCopy(owner, null);
		assertTrue("Second working copy should be identical to first one", shared2 == shared);
	} finally {
		shared.discardWorkingCopy();
		try {
			assertTrue("Should find shared working copy", this.cu.findWorkingCopy(owner) == shared);
		} finally {
			shared.discardWorkingCopy();
		}
	}
	assertTrue("Should not find cu with same owner", this.cu.findWorkingCopy(owner) == null);
}
/**
 * Tests that multiple commits are possible with the same working copy.
 */
public void testMultipleCommit() {

	// Add a method to the working copy
//	IType gp = this.copy.getType("A");
	try {
		this.copy.createMethod("function anotherMethod() {}\n",null, false, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("creation failed", false);
	}
	
	// commit the changes from the copy.
	try {
		this.copy.commitWorkingCopy(false, null);
	} catch (JavaScriptModelException t) {
		assertTrue("commit failed", false);
	}
	
	// new method added
	assertTrue("method should exist after commit", 
		this.cu.getFunction("anotherMethod", new String[]{}).exists());

	//add another method
	try {
		this.copy.createMethod("function anotherAnotherMethod() {}\n", null, false, null);
	} catch (JavaScriptModelException x) {
		assertTrue("Creation failed 2", false);
	}

	//commit the new method
	try {
		this.copy.commitWorkingCopy(false, null);
	} catch (JavaScriptModelException t) {
		assertTrue("commit2 failed", false);
	}

	// new method added
	assertTrue("second method added should exist after commit", 
		this.cu.getFunction("anotherAnotherMethod", new String[]{}).exists());
}
/**
 * Creates a working copy on a non-existing compilation unit.
 * (regression test for bug 8921  DCR - Need a way to create a working copy ignoring existing files)
 */
public void testNonExistingCU() throws JavaScriptModelException {
	IJavaScriptUnit nonExistingCU = this.getCompilationUnit("P/src/NonExisting.js");
	IJavaScriptUnit workingCopy = null;
	try {
		// getBuffer()
		workingCopy = nonExistingCU.getWorkingCopy(null);
		assertSourceEquals("Buffer should be empty", "", ((IOpenable)workingCopy).getBuffer().getContents());
		
		// exists()
		assertTrue("Working copy should exists", ((IJavaScriptElement)workingCopy).exists());
		
		// getCorrespondingResource()
		assertEquals("Corresponding resource should be null", null, ((IJavaScriptElement)workingCopy).getCorrespondingResource());
		
		// getPrimaryElement()
		assertEquals("Unexpected orginal element", nonExistingCU, workingCopy.getPrimaryElement());
		
		// getPath()
		assertEquals("Unexpected path", new Path("/P/src/NonExisting.js"), ((IJavaScriptElement)workingCopy).getPath());
		
		// getResource()
		assertEquals("Unexpected resource", nonExistingCU.getResource(), ((IJavaScriptElement)workingCopy).getResource());
		
		// isConsistent()
		assertTrue("Working copy should be consistent", ((IOpenable)workingCopy).isConsistent());
		
		// restore()
		boolean exception = false;
		try {
			workingCopy.restore();
		} catch (JavaScriptModelException e) {
			exception = true;
		}
		assertTrue("Should not be able to restore from primary element", exception);
		
		// makeConsistent()
		((IOpenable)workingCopy).getBuffer().setContents(
			"function X() {\n" +
			"}");
		assertTrue("Working copy should not be consistent", !((IOpenable)workingCopy).isConsistent());
		((IOpenable)workingCopy).makeConsistent(null);
		assertTrue("Working copy should be consistent", ((IOpenable)workingCopy).isConsistent());
		
		// save()
		((IOpenable)workingCopy).getBuffer().setContents(
			"function Y() {\n" +
			"}");
		((IOpenable)workingCopy).save(null, false);
		assertTrue("Working copy should be consistent after save", ((IOpenable)workingCopy).isConsistent());
		assertTrue("Primary cu should not exist", !nonExistingCU.exists());
		
		// commitWorkingCopy()
		workingCopy.commitWorkingCopy(false, null);
		assertTrue("Primary cu should exist", nonExistingCU.exists());

		// hasResourceChanged()
		assertTrue("Working copy's resource should now not mark as changed", !workingCopy.hasResourceChanged());
		
	} finally {
		if (workingCopy != null) {
			workingCopy.discardWorkingCopy();
		}
		if (nonExistingCU.exists()) {
			nonExistingCU.delete(true, null);
		}
	}
}
/**
 * Tests the general functionality of a operations working with working copies:<ul>
 * <li>ensures that the copy cannot be renamed</li>
 * <li>ensures that the copy cannot be moved to the same location as the primary cu</li>
 * <li>ensures that the copy can be copied to a different location as the primary cu</li>
 */
public void testOperations() throws JavaScriptModelException {
	// rename working copy
	boolean ex= false;
	try {
		this.copy.rename("someName.js", false, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Incorrect status code for attempting to rename working copy", jme.getStatus().getCode() == IJavaScriptModelStatusConstants.INVALID_ELEMENT_TYPES);
		ex= true;
	}
	assertTrue("renaming a working copy should fail", ex);
	
	// move to same location as primary cu
	ex= false;
	try {
		this.copy.move(this.cu.getParent(), null, "someName.js", false, null);
	} catch (JavaScriptModelException jme) {
		assertTrue("Incorrect status code for attempting to move working copy", jme.getStatus().getCode() == IJavaScriptModelStatusConstants.INVALID_ELEMENT_TYPES);
		ex= true;
	}
	assertTrue("moving a working copy should fail", ex);

	// copy working copy to default package
	IPackageFragment pkg= getPackageFragment("P", "src", "");
	this.copy.copy(pkg, null, "someName.js", false, null);
	assertCreation(this.copy);	
}
}
