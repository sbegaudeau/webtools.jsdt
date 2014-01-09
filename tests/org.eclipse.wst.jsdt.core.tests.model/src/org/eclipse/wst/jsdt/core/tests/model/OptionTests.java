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
package org.eclipse.wst.jsdt.core.tests.model;

import java.util.Hashtable;
import java.util.Map;

import junit.framework.Test;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.JavaProject;
import org.osgi.service.prefs.BackingStoreException;

public class OptionTests extends ModifyingResourceTests {
	
	int eventCount = 0;
	
	class TestPropertyListener implements IEclipsePreferences.IPreferenceChangeListener {
		public void preferenceChange(PreferenceChangeEvent event) {
			eventCount++;
		}
	}
	
	public OptionTests(String name) {
		super(name);
	}
	static {
//		TESTS_NUMBERS = new int[] { 125360 };
//		TESTS_RANGE = new int[] { 4, -1 };
	}
	public static Test suite() {
		return buildModelTestSuite(OptionTests.class);	
	}
	
	protected void tearDown() throws Exception {
		// Put back default options
		JavaScriptCore.setOptions(JavaScriptCore.getDefaultOptions());

		super.tearDown();
	}

	/**
	 * Test persistence of project custom options
	 */
	public void test01() throws CoreException {
		try {
			IJavaScriptProject projectA = 
				this.createJavaProject(
					"A", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
			IJavaScriptProject projectB = 
				this.createJavaProject(
					"B", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
					
			Hashtable options = new Hashtable();
			options.put(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, JavaScriptCore.DISABLED);
			options.put(JavaScriptCore.COMPILER_COMPLIANCE, "8.0");
			options.put(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK, JavaScriptCore.ERROR);
			JavaScriptCore.setOptions(options);
	
			options.clear();
			options.put(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, JavaScriptCore.ENABLED);
			options.put(JavaScriptCore.COMPILER_COMPLIANCE, "10.0");
			projectA.setOptions(options);
	
			// check project A custom options		
			assertEquals("projA:unexpected custom value for deprecation option", JavaScriptCore.ENABLED, projectA.getOption(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, true));
			assertEquals("projA:unexpected custom value for compliance option", "10.0", projectA.getOption(JavaScriptCore.COMPILER_COMPLIANCE, true));
			assertEquals("projA:unexpected inherited value1 for hidden-catch option", JavaScriptCore.ERROR, projectA.getOption(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK, true));
			
			// check project B custom options	(should be none, indicating it sees global ones only)
			assertEquals("projB:unexpected custom value for deprecation option", JavaScriptCore.DISABLED, projectB.getOption(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, true));
			assertEquals("projB:unexpected custom value for compliance option", "8.0", projectB.getOption(JavaScriptCore.COMPILER_COMPLIANCE, true));
			assertEquals("projB:unexpected inherited value for hidden-catch option", JavaScriptCore.ERROR, projectB.getOption(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK, true));
	
			// flush custom options - project A should revert to global ones
			projectA.setOptions(null); 
			assertEquals("projA:unexpected reverted value for deprecation option", JavaScriptCore.DISABLED, projectA.getOption(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, true));
			assertEquals("projA:unexpected reverted value for compliance option", "8.0", projectA.getOption(JavaScriptCore.COMPILER_COMPLIANCE, true));
			assertEquals("projA:unexpected inherited value2 for hidden-catch option", JavaScriptCore.ERROR, projectA.getOption(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK, true));
	
		} finally {
			this.deleteProject("A");
			this.deleteProject("B");
		}
	}
	
	/**
	 * Test custom encoding
	 */
	public void test02() throws CoreException {
		try {
			IJavaScriptProject projectA = 
				this.createJavaProject(
					"A", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
			IJavaScriptProject projectB = 
				this.createJavaProject(
					"B", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
					
			String globalEncoding = JavaScriptCore.getOption(JavaScriptCore.CORE_ENCODING);
	
			Hashtable options = new Hashtable();
			options.put(JavaScriptCore.CORE_ENCODING, "custom");
			projectA.setOptions(options);
	
			// check project A custom options		
			assertEquals("projA:unexpected custom encoding", "custom", projectA.getOption(JavaScriptCore.CORE_ENCODING, true));
			
			// check project B custom options	(should be none, indicating it sees global ones only)
			assertEquals("projB:unexpected custom encoding", globalEncoding, projectB.getOption(JavaScriptCore.CORE_ENCODING, true));
	
			// flush custom options - project A should revert to global ones
			projectA.setOptions(null); 
			assertEquals("projA:unexpected reverted encoding", globalEncoding, projectA.getOption(JavaScriptCore.CORE_ENCODING, true));
	
		} finally {
			this.deleteProject("A");
			this.deleteProject("B");
		}
	}
	
	/**
	 * Test custom project option (if not considering JavaScriptCore options)
	 */
	public void test03() throws CoreException {
		try {
			IJavaScriptProject projectA = 
				this.createJavaProject(
					"A", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
			IJavaScriptProject projectB = 
				this.createJavaProject(
					"B", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
					
			Hashtable options = new Hashtable();
			options.put(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, JavaScriptCore.DISABLED);
			options.put(JavaScriptCore.COMPILER_COMPLIANCE, "8.0");
			options.put(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK, JavaScriptCore.ERROR);
			JavaScriptCore.setOptions(options);
	
			options.clear();
			options.put(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, JavaScriptCore.ENABLED);
			options.put(JavaScriptCore.COMPILER_COMPLIANCE, "10.0");
			projectA.setOptions(options);
	
			// check project A custom options		
			assertEquals("projA:unexpected custom value for deprecation option", JavaScriptCore.ENABLED, projectA.getOption(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, false));
			assertEquals("projA:unexpected custom value for compliance option", "10.0", projectA.getOption(JavaScriptCore.COMPILER_COMPLIANCE, false));
			assertEquals("projA:unexpected inherited value1 for hidden-catch option", null, projectA.getOption(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK, false));
			
			// check project B custom options	(should be none, indicating it sees global ones only)
			assertEquals("projB:unexpected custom value for deprecation option", null, projectB.getOption(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, false));
			assertEquals("projB:unexpected custom value for compliance option", null, projectB.getOption(JavaScriptCore.COMPILER_COMPLIANCE, false));
			assertEquals("projB:unexpected inherited value for hidden-catch option", null, projectB.getOption(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK, false));
	
			// flush custom options - project A should revert to global ones
			projectA.setOptions(null); 
			assertEquals("projA:unexpected reverted value for deprecation option", null, projectA.getOption(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, false));
			assertEquals("projA:unexpected reverted value for compliance option", null, projectA.getOption(JavaScriptCore.COMPILER_COMPLIANCE, false));
			assertEquals("projA:unexpected inherited value2 for hidden-catch option", null, projectA.getOption(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK, false));
	
		} finally {
			this.deleteProject("A");
			this.deleteProject("B");
		}
	}
	/**
	 * Test persistence of project custom options - using getOptions()
	 */
	public void test04() throws CoreException {
		try {
			IJavaScriptProject projectA = 
				this.createJavaProject(
					"A", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
			IJavaScriptProject projectB = 
				this.createJavaProject(
					"B", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
					
			Hashtable options = new Hashtable();
			options.put(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, JavaScriptCore.DISABLED);
			options.put(JavaScriptCore.COMPILER_COMPLIANCE, "8.0");
			options.put(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK, JavaScriptCore.ERROR);
			JavaScriptCore.setOptions(options);
	
			options.clear();
			options.put(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, JavaScriptCore.ENABLED);
			options.put(JavaScriptCore.COMPILER_COMPLIANCE, "10.0");
			projectA.setOptions(options);
	
			// check project A custom options		
			assertEquals("projA:unexpected custom value for deprecation option", JavaScriptCore.ENABLED, projectA.getOptions(true).get(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE));
			assertEquals("projA:unexpected custom value for compliance option", "10.0", projectA.getOptions(true).get(JavaScriptCore.COMPILER_COMPLIANCE));
			assertEquals("projA:unexpected inherited value1 for hidden-catch option", JavaScriptCore.ERROR, projectA.getOptions(true).get(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK));
			
			// check project B custom options	(should be none, indicating it sees global ones only)
			assertEquals("projB:unexpected custom value for deprecation option", JavaScriptCore.DISABLED, projectB.getOptions(true).get(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE));
			assertEquals("projB:unexpected custom value for compliance option", "8.0", projectB.getOptions(true).get(JavaScriptCore.COMPILER_COMPLIANCE));
			assertEquals("projB:unexpected inherited value for hidden-catch option", JavaScriptCore.ERROR, projectB.getOptions(true).get(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK));
	
			// flush custom options - project A should revert to global ones
			projectA.setOptions(null); 
			assertEquals("projA:unexpected reverted value for deprecation option", JavaScriptCore.DISABLED, projectA.getOptions(true).get(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE));
			assertEquals("projA:unexpected reverted value for compliance option", "8.0", projectA.getOptions(true).get(JavaScriptCore.COMPILER_COMPLIANCE));
			assertEquals("projA:unexpected inherited value2 for hidden-catch option", JavaScriptCore.ERROR, projectA.getOptions(true).get(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK));
	
		} finally {
			this.deleteProject("A");
			this.deleteProject("B");
		}
	}
	
	/**
	 * Test custom encoding - using getOptions()
	 */
	public void test05() throws CoreException {
		try {
			IJavaScriptProject projectA = 
				this.createJavaProject(
					"A", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
			IJavaScriptProject projectB = 
				this.createJavaProject(
					"B", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
					
			String globalEncoding = JavaScriptCore.getOption(JavaScriptCore.CORE_ENCODING);
	
			Hashtable options = new Hashtable();
			options.put(JavaScriptCore.CORE_ENCODING, "custom");
			projectA.setOptions(options);
	
			// check project A custom options		
			assertEquals("projA:unexpected custom encoding", "custom", projectA.getOptions(true).get(JavaScriptCore.CORE_ENCODING));
			
			// check project B custom options	(should be none, indicating it sees global ones only)
			assertEquals("projB:unexpected custom encoding", globalEncoding, projectB.getOptions(true).get(JavaScriptCore.CORE_ENCODING));
	
			// flush custom options - project A should revert to global ones
			projectA.setOptions(null); 
			assertEquals("projA:unexpected reverted encoding", globalEncoding, projectA.getOptions(true).get(JavaScriptCore.CORE_ENCODING));
	
		} finally {
			this.deleteProject("A");
			this.deleteProject("B");
		}
	}
	
	/**
	 * Test custom project option (if not considering JavaScriptCore options) - using getOptions()
	 */
	public void test06() throws CoreException {
		try {
			IJavaScriptProject projectA = 
				this.createJavaProject(
					"A", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
			IJavaScriptProject projectB = 
				this.createJavaProject(
					"B", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
					
			Hashtable options = new Hashtable();
			options.put(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, JavaScriptCore.DISABLED);
			options.put(JavaScriptCore.COMPILER_COMPLIANCE, "8.0");
			options.put(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK, JavaScriptCore.ERROR);
			JavaScriptCore.setOptions(options);
	
			options.clear();
			options.put(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, JavaScriptCore.ENABLED);
			options.put(JavaScriptCore.COMPILER_COMPLIANCE, "10.0");
			projectA.setOptions(options);
	
			// check project A custom options		
			assertEquals("projA:unexpected custom value for deprecation option", JavaScriptCore.ENABLED, projectA.getOptions(false).get(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE));
			assertEquals("projA:unexpected custom value for compliance option", "10.0", projectA.getOptions(false).get(JavaScriptCore.COMPILER_COMPLIANCE));
			assertEquals("projA:unexpected inherited value1 for hidden-catch option", null, projectA.getOptions(false).get(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK));
			
			// check project B custom options	(should be none, indicating it sees global ones only)
			assertEquals("projB:unexpected custom value for deprecation option", null, projectB.getOptions(false).get(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE));
			assertEquals("projB:unexpected custom value for compliance option", null, projectB.getOptions(false).get(JavaScriptCore.COMPILER_COMPLIANCE));
			assertEquals("projB:unexpected inherited value for hidden-catch option", null, projectB.getOptions(false).get(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK));
	
			// flush custom options - project A should revert to global ones
			projectA.setOptions(null); 
			assertEquals("projA:unexpected reverted value for deprecation option", null, projectA.getOptions(false).get(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE));
			assertEquals("projA:unexpected reverted value for compliance option", null, projectA.getOptions(false).get(JavaScriptCore.COMPILER_COMPLIANCE));
			assertEquals("projA:unexpected inherited value2 for hidden-catch option", null, projectA.getOptions(false).get(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK));
	
		} finally {
			this.deleteProject("A");
			this.deleteProject("B");
		}
	}
	/**
	 * Custom options must replace existing ones completely without loosing property listeners
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=26255
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=49691
	 */
	public void test07() throws CoreException {
		try {
			this.eventCount = 0;
			JavaProject projectA = (JavaProject)
				this.createJavaProject(
					"A", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
	//		Preferences preferences = projectA.getPreferences();
	//		preferences.addPropertyChangeListener(new TestPropertyListener());
			IEclipsePreferences eclipsePreferences = projectA.getEclipsePreferences();
			TestPropertyListener listener = new TestPropertyListener();
			eclipsePreferences.addPreferenceChangeListener(listener);
		
			Hashtable options = new Hashtable();
			options.put(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, JavaScriptCore.ENABLED);
			options.put(JavaScriptCore.COMPILER_COMPLIANCE, "10.0");
			projectA.setOptions(options);
	
			// check project A custom options		
			assertEquals("projA:unexpected custom value for deprecation option", JavaScriptCore.ENABLED, projectA.getOptions(false).get(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE));
			assertEquals("projA:unexpected custom value for compliance option", "10.0", projectA.getOptions(false).get(JavaScriptCore.COMPILER_COMPLIANCE));
			assertEquals("projA:unexpected inherited value1 for hidden-catch option", null, projectA.getOptions(false).get(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK));
	//		assertTrue("projA:preferences should not be reset", preferences == projectA.getPreferences());
			assertTrue("projA:preferences should not be reset", eclipsePreferences == projectA.getEclipsePreferences());
			assertTrue("projA:preferences property listener has been lost", eventCount == 2);
		
			// change custom options to have one less
			options.clear();
			options.put(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, JavaScriptCore.ENABLED);
			projectA.setOptions(options);
			assertEquals("projA:unexpected custom value for deprecation option", JavaScriptCore.ENABLED, projectA.getOptions(false).get(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE));
			assertEquals("projA:unexpected custom value for compliance option", null, projectA.getOptions(false).get(JavaScriptCore.COMPILER_COMPLIANCE));
			assertEquals("projA:unexpected inherited value1 for hidden-catch option", null, projectA.getOptions(false).get(JavaScriptCore.COMPILER_PB_HIDDEN_CATCH_BLOCK));
	//		assertTrue("projA:preferences should not be reset", preferences == projectA.getPreferences());
			assertTrue("projA:preferences should not be reset", eclipsePreferences == projectA.getEclipsePreferences());
			assertTrue("projA:preferences property listener has been lost", eventCount == 3);
		} finally {
			this.deleteProject("A");
		}
	}
	/**
	 * Empty custom option must not be ignored
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=26251
	 */
	public void test08() throws CoreException {
		try {
			IJavaScriptProject projectA = 
				this.createJavaProject(
					"A", 
					new String[] {}, // source folders
					new String[] {}, // lib folders
					new String[] {});
	
			Hashtable options = new Hashtable();
			options.put(JavaScriptCore.COMPILER_TASK_TAGS, "TODO:");
			JavaScriptCore.setOptions(options);
			
	
			// check project A custom options		
			assertEquals("1#projA:unexpected custom value for task tags option", null, projectA.getOption(JavaScriptCore.COMPILER_TASK_TAGS, false));
			assertEquals("1#projA:unexpected custom value for inherited task tags option", "TODO:", projectA.getOption(JavaScriptCore.COMPILER_TASK_TAGS, true));
			assertEquals("1#workspace:unexpected custom value for task tags option", "TODO:", JavaScriptCore.getOption(JavaScriptCore.COMPILER_TASK_TAGS));
			
			// change custom options to have one less
			options.clear();
			options.put(JavaScriptCore.COMPILER_TASK_TAGS, "");
			projectA.setOptions(options);
			assertEquals("2#projA:unexpected custom value for task tags option", "", projectA.getOption(JavaScriptCore.COMPILER_TASK_TAGS, false));
			assertEquals("2#projA:unexpected custom value for inherited task tags option", "", projectA.getOption(JavaScriptCore.COMPILER_TASK_TAGS, true));
			assertEquals("2#workspace:unexpected custom value for task tags option", "TODO:", JavaScriptCore.getOption(JavaScriptCore.COMPILER_TASK_TAGS));
	
			// change custom options to have one less
			options.clear();
			options.put(JavaScriptCore.COMPILER_TASK_TAGS, "@TODO");
			JavaScriptCore.setOptions(options);
			assertEquals("3#projA:unexpected custom value for task tags option", "", projectA.getOption(JavaScriptCore.COMPILER_TASK_TAGS, false));
			assertEquals("3#projA:unexpected custom value for inherited task tags option", "", projectA.getOption(JavaScriptCore.COMPILER_TASK_TAGS, true));
			assertEquals("3#workspace:unexpected custom value for task tags option", "@TODO", JavaScriptCore.getOption(JavaScriptCore.COMPILER_TASK_TAGS));
	
		} finally {
			this.deleteProject("A");
		}
	}
	/**
	 * Custom options must replace existing ones completely without loosing property listeners
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=59258
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=60896
	 */
	public void test09() throws CoreException {
		try {
			this.eventCount = 0;
			JavaProject projectA = (JavaProject) this.createJavaProject("A", new String[] {});
	//		Preferences preferences = projectA.getPreferences();
	//		preferences.addPropertyChangeListener(new TestPropertyListener());
			IEclipsePreferences eclipsePreferences = projectA.getEclipsePreferences();
			eclipsePreferences.addPreferenceChangeListener(new TestPropertyListener());
		
			Hashtable options = new Hashtable();
			options.put(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE, JavaScriptCore.ENABLED);
			options.put(JavaScriptCore.COMPILER_COMPLIANCE, "10.0");
			projectA.setOptions(options);
	
			// check project A custom options		
			assertEquals("projA:unexpected custom value for deprecation option", JavaScriptCore.ENABLED, projectA.getOptions(true).get(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE));
			assertEquals("projA:unexpected custom value for compliance option", "10.0", projectA.getOptions(true).get(JavaScriptCore.COMPILER_COMPLIANCE));
			assertTrue("projA:preferences should not be reset", eclipsePreferences == projectA.getEclipsePreferences());
			assertEquals("projA:preferences property listener has been lost", 2, eventCount);
		
			// delete/create project A and verify that options are well reset
			this.deleteProject("A");
			projectA = (JavaProject) this.createJavaProject("A", new String[] {});
			assertEquals("projA:unexpected custom value for deprecation option", JavaScriptCore.getOption(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE), projectA.getOptions(true).get(JavaScriptCore.COMPILER_PB_DEPRECATION_IN_DEPRECATED_CODE));
			assertEquals("projA:unexpected custom value for compliance option", JavaScriptCore.getOption(JavaScriptCore.COMPILER_COMPLIANCE), projectA.getOptions(true).get(JavaScriptCore.COMPILER_COMPLIANCE));
			assertTrue("projA:preferences should not be reset", eclipsePreferences != projectA.getEclipsePreferences());
		} finally {
			this.deleteProject("A");
		}
	}
	
	/*
	 * Ensures that a classpath variable is still in the preferences after shutdown/restart
	 * (regression test for bug 98720 [preferences] classpath variables are not exported if the session is closed and restored)
	 */
	public void test10() throws CoreException {
		JavaScriptCore.setIncludepathVariable("TEST", new Path("testing"), null);
		simulateExitRestart();
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		IEclipsePreferences preferences = manager.getInstancePreferences();
		assertEquals(
			"Should find variable TEST in preferences", 
			"testing",
			preferences.get(JavaModelManager.CP_VARIABLE_PREFERENCES_PREFIX+"TEST", "null"));
	}

	/*
	 * Ensures that a classpath variable is removed from the preferences if set to null
	 * (regression test for bug 98720 [preferences] classpath variables are not exported if the session is closed and restored)
	 */
	public void test11() throws CoreException {
		JavaScriptCore.setIncludepathVariable("TEST", new Path("testing"), null);
		JavaScriptCore.removeIncludepathVariable("TEST", null);
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		IEclipsePreferences preferences = manager.getInstancePreferences();
		assertEquals(
			"Should not find variable TEST in preferences", 
			"null",
			preferences.get(JavaModelManager.CP_VARIABLE_PREFERENCES_PREFIX+"TEST", "null"));
	}

	/*
	 * Ensures that classpath problems are removed when a missing classpath variable is added through the preferences
	 * (regression test for bug 109691 Importing preferences does not update classpath variables)
	 */
	public void test12() throws CoreException {
		IEclipsePreferences preferences = JavaModelManager.getJavaModelManager().getInstancePreferences();
		try {
			IJavaScriptProject project = createJavaProject("P", new String[0], new String[] {"TEST"});
			waitForAutoBuild();
			preferences.put(JavaModelManager.CP_VARIABLE_PREFERENCES_PREFIX+"TEST", getSystemJsPathString());
			assertMarkers("Unexpected markers", "", project);
		} finally {
			deleteProject("P");
			preferences.remove(JavaModelManager.CP_VARIABLE_PREFERENCES_PREFIX+"TEST");
		}
	}

	/**
	 * Bug 68993: [Preferences] IAE when opening project preferences
	 * @see "http://bugs.eclipse.org/bugs/show_bug.cgi?id=68993"
	 */
	public void testBug68993() throws CoreException, BackingStoreException {
		try {
			JavaProject projectA = (JavaProject) this.createJavaProject(
				"A", 
				new String[] {}, // source folders
				new String[] {}, // lib folders
				new String[] {});

			// Store project eclipse prefs
			IEclipsePreferences eclipsePreferences = projectA.getEclipsePreferences();

			// set all project options as custom ones: this is what happens when user select
			// "Use project settings" in project 'Java Compiler' preferences page...
			Hashtable options = new Hashtable(projectA.getOptions(true));
			projectA.setOptions(options);

			// reset all project custom options: this is what happens when user select
			// "Use workspace settings" in project 'Java Compiler' preferences page...
			options = new Hashtable();
			options.put("internal.default.compliance", JavaScriptCore.DEFAULT);
			projectA.setOptions(options);

			// verify that project preferences have been reset
			assertFalse("projA: Preferences should have been reset", eclipsePreferences == projectA.getEclipsePreferences());
			assertEquals("projA: We should not have any custom options!", 0, projectA.getEclipsePreferences().keys().length);
		} finally {
			this.deleteProject("A");
		}
	}

	/**
	 * Bug 72214: [Preferences] IAE when opening project preferences
	 * @see "http://bugs.eclipse.org/bugs/show_bug.cgi?id=72214"
	 */
	public void testBug72214() throws CoreException, BackingStoreException {
		// Remove JavaScriptCore instance prefs
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		IEclipsePreferences preferences = manager.getInstancePreferences();
		int size = JavaScriptCore.getOptions().size();
		preferences.removeNode();
	
		// verify that JavaScriptCore preferences have been reset
		assertFalse("JavaScriptCore preferences should have been reset", preferences == manager.getInstancePreferences());
		assertEquals("JavaScriptCore preferences should have been resotred!", size, JavaScriptCore.getOptions().size());
	}

	/**
	 * Bug 100393: Defaults for compiler errors/warnings settings
	 * @see "http://bugs.eclipse.org/bugs/show_bug.cgi?id=100393"
	 */
	public void testBug100393() throws CoreException, BackingStoreException {
		// Get default compiler options
		Map options = new CompilerOptions().getMap();

		// verify that CompilerOptions default preferences for modified options
		assertEquals("Invalid default for "+CompilerOptions.OPTION_ReportUnusedLocal, CompilerOptions.WARNING, options.get(CompilerOptions.OPTION_ReportUnusedLocal));
		assertEquals("Invalid default for "+CompilerOptions.OPTION_ReportUnusedPrivateMember, CompilerOptions.WARNING, options.get(CompilerOptions.OPTION_ReportUnusedPrivateMember));
		assertEquals("Invalid default for "+CompilerOptions.OPTION_ReportFieldHiding, CompilerOptions.IGNORE, options.get(CompilerOptions.OPTION_ReportFieldHiding));
		assertEquals("Invalid default for "+CompilerOptions.OPTION_ReportLocalVariableHiding, CompilerOptions.IGNORE, options.get(CompilerOptions.OPTION_ReportLocalVariableHiding));
	}
	public void testBug100393b() throws CoreException, BackingStoreException {
		// Get JavaScriptCore default preferences
		JavaModelManager manager = JavaModelManager.getJavaModelManager();
		IEclipsePreferences preferences = manager.getDefaultPreferences();

		// verify that JavaScriptCore default preferences for modified options
		assertEquals("Invalid default for "+JavaScriptCore.COMPILER_PB_UNUSED_LOCAL, "warning", preferences.get(JavaScriptCore.COMPILER_PB_UNUSED_LOCAL, "null"));
		assertEquals("Invalid default for "+JavaScriptCore.COMPILER_PB_UNUSED_PRIVATE_MEMBER, "warning", preferences.get(JavaScriptCore.COMPILER_PB_UNUSED_PRIVATE_MEMBER, "null"));
		assertEquals("Invalid default for "+JavaScriptCore.COMPILER_PB_FIELD_HIDING, "ignore", preferences.get(JavaScriptCore.COMPILER_PB_FIELD_HIDING, "null"));
		assertEquals("Invalid default for "+JavaScriptCore.COMPILER_PB_LOCAL_VARIABLE_HIDING, "ignore", preferences.get(JavaScriptCore.COMPILER_PB_LOCAL_VARIABLE_HIDING, "null"));
	}

	/**
	 * @bug 125360: IJavaScriptProject#setOption() doesn't work if same option as default
	 * @see "http://bugs.eclipse.org/bugs/show_bug.cgi?id=125360"
	 */
	public void testBug125360() throws CoreException, BackingStoreException {
		try {
			JavaProject project = (JavaProject) createJavaProject(
				"P", 
				new String[] {}, // source folders
				new String[] {}, // lib folders
				new String[] {});
			project.setOption(JavaScriptCore.COMPILER_SOURCE, JavaScriptCore.VERSION_1_4);
			project.setOption(JavaScriptCore.COMPILER_SOURCE, JavaScriptCore.VERSION_1_3);
			String option = project.getOption(JavaScriptCore.COMPILER_SOURCE, true);
			assertEquals(JavaScriptCore.VERSION_1_3, option);
		} finally {
			deleteProject("P");
		}
	}
	
	/**
	 * @bug 131707: Cannot add classpath variables when starting with -pluginCustomization option
	 * @see "https://bugs.eclipse.org/bugs/show_bug.cgi?id=131707"
	 */
	public void testBug131707() throws CoreException {
		IEclipsePreferences defaultPreferences = new DefaultScope().getNode(JavaScriptCore.PLUGIN_ID);
		try {
			defaultPreferences.put("org.eclipse.wst.jsdt.core.classpathVariable.MY_DEFAULT_LIB", "c:\\temp\\lib.jar");
			simulateExitRestart();
			String[] variableNames = JavaScriptCore.getIncludepathVariableNames();
			for (int i = 0, length = variableNames.length; i < length; i++) {
				if ("MY_DEFAULT_LIB".equals(variableNames[i])) {
					assertEquals(
						"Unexpected value for MY_DEFAULT_LIB", 
						new Path("c:\\temp\\lib.jar"), 
						JavaScriptCore.getIncludepathVariable("MY_DEFAULT_LIB"));
					return;
				}
			}
			assertFalse("Variable MY_DEFAULT_LIB not found", true);
		} finally {
			defaultPreferences.remove("org.eclipse.wst.jsdt.core.classpathVariable.MY_DEFAULT_LIB");
		}
	}
}
