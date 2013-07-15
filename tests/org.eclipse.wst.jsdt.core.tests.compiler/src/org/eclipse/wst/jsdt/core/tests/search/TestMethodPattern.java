/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.search;

import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.core.search.indexing.IIndexConstants;
import org.eclipse.wst.jsdt.internal.core.search.matching.MethodPattern;

import junit.framework.TestCase;

/**
 * <p>Tests for MethodPattern.</p>
 * 
 * @see MethodPattern
 */
public class TestMethodPattern extends TestCase {
	
	public void testCreateIndexKey_0() {
		runCreateIndexKeyTest("myFunction/////", "myFunction".toCharArray(), null, null, null, null, 0);
	}
	
	public void testCreateIndexKey_1() {
		runCreateIndexKeyTest("myFunction/////", "myFunction".toCharArray(), null, null, null, null, 0);
	}
	
	public void testCreateIndexKey_2() {
		runCreateIndexKeyTest("myFunction///String//",
				"myFunction".toCharArray(), null, null, null, "String".toCharArray(), 0);
	}
	
	public void testCreateIndexKey_3() {
		runCreateIndexKeyTest("myFunction////foo.bar.Type/",
				"myFunction".toCharArray(), null, null, "foo.bar.Type".toCharArray(), null, ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_4() {
		runCreateIndexKeyTest("myFunction///String/foo.bar.Type/",
				"myFunction".toCharArray(), null, null, "foo.bar.Type".toCharArray(), "String".toCharArray(), ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_5() {
		runCreateIndexKeyTest("myFunction//param1,param2///",
				"myFunction".toCharArray(), null, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, null, 0);
	}
	
	public void testCreateIndexKey_6() {
		runCreateIndexKeyTest("myFunction//param1,param2/String//",
				"myFunction".toCharArray(), null, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, "String".toCharArray(), 0);
	}
	
	public void testCreateIndexKey_7() {
		runCreateIndexKeyTest("myFunction//param1,param2//foo.bar.Type/",
				"myFunction".toCharArray(), null, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), null, ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_8() {
		runCreateIndexKeyTest("myFunction//param1,param2/String/foo.bar.Type/",
				"myFunction".toCharArray(), null, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), "String".toCharArray(), ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_9() {
		runCreateIndexKeyTest("myFunction/String,Number/param1,param2///",
				"myFunction".toCharArray(), new char[][] { "String".toCharArray(), "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, null, 0);
	}
	
	public void testCreateIndexKey_10() {
		runCreateIndexKeyTest("myFunction/String,Number/param1,param2/String//",
				"myFunction".toCharArray(), new char[][] { "String".toCharArray(), "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, "String".toCharArray(), 0);
	}
	
	public void testCreateIndexKey_11() {
		runCreateIndexKeyTest("myFunction/String,Number/param1,param2//foo.bar.Type/",
				"myFunction".toCharArray(), new char[][] { "String".toCharArray(), "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), null, ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_12() {
		runCreateIndexKeyTest("myFunction/String,Number/param1,param2/String/foo.bar.Type/",
				"myFunction".toCharArray(), new char[][] { "String".toCharArray(), "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), "String".toCharArray(), ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_13() {
		runCreateIndexKeyTest("myFunction/,Number/param1,param2///",
				"myFunction".toCharArray(), new char[][] { null, "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, null, 0);
	}
	
	public void testCreateIndexKey_14() {
		runCreateIndexKeyTest("myFunction/,Number/param1,param2/String//",
				"myFunction".toCharArray(), new char[][] { null, "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, "String".toCharArray(), 0);
	}
	
	public void testCreateIndexKey_15() {
		runCreateIndexKeyTest("myFunction/,Number/param1,param2//foo.bar.Type/",
				"myFunction".toCharArray(), new char[][] { null, "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), null, ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_16() {
		runCreateIndexKeyTest("myFunction/,Number/param1,param2/String/foo.bar.Type/",
				"myFunction".toCharArray(), new char[][] { null, "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), "String".toCharArray(), ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_17() {
		runCreateIndexKeyTest("myFunction/,Number/param1,param2///",
				"myFunction".toCharArray(), new char[][] { new char[0], "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, null, 0);
	}
	
	public void testCreateIndexKey_18() {
		runCreateIndexKeyTest("myFunction/,Number/param1,param2/String//",
				"myFunction".toCharArray(), new char[][] { new char[0], "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, "String".toCharArray(), 0);
	}
	
	public void testCreateIndexKey_19() {
		runCreateIndexKeyTest("myFunction/,Number/param1,param2//foo.bar.Type/",
				"myFunction".toCharArray(), new char[][] { new char[0], "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), null, ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_20() {
		runCreateIndexKeyTest("myFunction/,Number/param1,param2/String/foo.bar.Type/",
				"myFunction".toCharArray(), new char[][] { new char[0], "Number".toCharArray()}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), "String".toCharArray(), ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_21() {
		runCreateIndexKeyTest("myFunction/String,/param1,param2///",
				"myFunction".toCharArray(), new char[][] {"String".toCharArray(), null}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, null, 0);
	}
	
	public void testCreateIndexKey_22() {
		runCreateIndexKeyTest("myFunction/String,/param1,param2/String//",
				"myFunction".toCharArray(), new char[][] {"String".toCharArray(), null}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, "String".toCharArray(), 0);
	}
	
	public void testCreateIndexKey_23() {
		runCreateIndexKeyTest("myFunction/String,/param1,param2//foo.bar.Type/",
				"myFunction".toCharArray(), new char[][] {"String".toCharArray(), null}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), null, ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_24() {
		runCreateIndexKeyTest("myFunction/String,/param1,param2/String/foo.bar.Type/",
				"myFunction".toCharArray(), new char[][] {"String".toCharArray(), null}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), "String".toCharArray(), ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_25() {
		runCreateIndexKeyTest("myFunction/String,/param1,param2///",
				"myFunction".toCharArray(), new char[][] {"String".toCharArray(), new char[0]}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, null, 0);
	}
	
	public void testCreateIndexKey_26() {
		runCreateIndexKeyTest("myFunction/String,/param1,param2/String//",
				"myFunction".toCharArray(), new char[][] {"String".toCharArray(), new char[0]}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, null, "String".toCharArray(), 0);
	}
	
	public void testCreateIndexKey_27() {
		runCreateIndexKeyTest("myFunction/String,/param1,param2//foo.bar.Type/",
				"myFunction".toCharArray(), new char[][] {"String".toCharArray(), new char[0]}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), null, ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_28() {
		runCreateIndexKeyTest("myFunction/String,/param1,param2/String/foo.bar.Type/", "myFunction".toCharArray(), new char[][] {"String".toCharArray(), new char[0]}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), "String".toCharArray(), ClassFileConstants.AccStatic);
	}
	
	public void testCreateIndexKey_29() {
		char[] keyChars = MethodPattern.createIndexKey("".toCharArray(), new char[][] {"String".toCharArray(), new char[0]}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), "String".toCharArray(), ClassFileConstants.AccStatic);
		
		String key = null;
		if(keyChars != null) {
			key = new String(keyChars);
		}
		
		assertNull("If selector is empty the key should be null.\nWAS:\n" + key, key);
	}
	
	public void testCreateIndexKey_30() {
		char[] keyChars = MethodPattern.createIndexKey(null, new char[][] {"String".toCharArray(), new char[0]}, new char[][] { "param1".toCharArray(), "param2".toCharArray()}, "foo.bar.Type".toCharArray(), "String".toCharArray(), ClassFileConstants.AccStatic);
		
		String key = null;
		if(keyChars != null) {
			key = new String(keyChars);
		}
		
		assertNull("If selector is empty the key should be null.\nWAS:\n" + key, key);
	}
	
	public void testDecodeIndexKey_0() {
		runDecodeIndexKeyTest("myFunction////", "myFunction", null, null, null,null, null, null, null, 0);
	}
	
	public void testDecodeIndexKey_2() {
		runDecodeIndexKeyTest("myFunction///String/", "myFunction", null, null, null,null, "String", null, null, 0);
	}
	
	public void testDecodeIndexKey_3() {
		runDecodeIndexKeyTest("myFunction////foo.bar.Type", "myFunction", null, null, null,null, null, "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_4() {
		runDecodeIndexKeyTest("myFunction///String/foo.bar.Type", "myFunction", null, null, null,null, "String", "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_5() {
		runDecodeIndexKeyTest("myFunction//param1,param2//", "myFunction", new String[2], new String[2], new String[] {"param1", "param2"},null, null, null, null, 0);
	}
	
	public void testDecodeIndexKey_6() {
		runDecodeIndexKeyTest("myFunction//param1,param2/String/", "myFunction", new String[2], new String[2], new String[] {"param1", "param2"},null, "String", null, null, 0);
	}
	
	public void testDecodeIndexKey_7() {
		runDecodeIndexKeyTest("myFunction//param1,param2//foo.bar.Type", "myFunction", new String[2], new String[2], new String[] {"param1", "param2"},null, null, "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_8() {
		runDecodeIndexKeyTest("myFunction//param1,param2/String/foo.bar.Type", "myFunction", new String[2], new String[2], new String[] {"param1", "param2"},null, "String", "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_9() {
		runDecodeIndexKeyTest("myFunction/String,Number/param1,param2//", "myFunction", new String[]{null, null}, new String[]{"String", "Number"}, new String[] {"param1", "param2"},null, null, null, null, 0);
	}
	
	public void testDecodeIndexKey_10() {
		runDecodeIndexKeyTest("myFunction/String,Number/param1,param2/String/", "myFunction", new String[]{null, null}, new String[]{"String", "Number"}, new String[] {"param1", "param2"},null, "String", null, null, 0);
	}
	
	public void testDecodeIndexKey_11() {
		runDecodeIndexKeyTest("myFunction/String,Number/param1,param2//foo.bar.Type", "myFunction", new String[]{null, null}, new String[]{"String", "Number"}, new String[] {"param1", "param2"},null, null, "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_12() {
		runDecodeIndexKeyTest("myFunction/String,Number/param1,param2/String/foo.bar.Type", "myFunction", new String[]{null, null}, new String[]{"String", "Number"}, new String[] {"param1", "param2"},null, "String", "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_13() {
		runDecodeIndexKeyTest("myFunction/,Number/param1,param2//", "myFunction", new String[]{null, null}, new String[]{null, "Number"}, new String[] {"param1", "param2"},null, null, null, null, 0);
	}
	
	public void testDecodeIndexKey_14() {
		runDecodeIndexKeyTest("myFunction/,Number/param1,param2/String/", "myFunction", new String[]{null, null}, new String[]{null, "Number"}, new String[] {"param1", "param2"},null, "String", null, null, 0);
	}
	
	public void testDecodeIndexKey_15() {
		runDecodeIndexKeyTest("myFunction/,Number/param1,param2//foo.bar.Type", "myFunction", new String[]{null, null}, new String[]{null, "Number"}, new String[] {"param1", "param2"},null, null, "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_16() {
		runDecodeIndexKeyTest("myFunction/,Number/param1,param2/String/foo.bar.Type", "myFunction", new String[]{null, null}, new String[]{null, "Number"}, new String[] {"param1", "param2"},null, "String", "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_17() {
		runDecodeIndexKeyTest("myFunction/String,/param1,param2//", "myFunction", new String[]{null, null}, new String[]{"String", null}, new String[] {"param1", "param2"},null, null, null, null, 0);
	}
	
	public void testDecodeIndexKey_18() {
		runDecodeIndexKeyTest("myFunction/String,/param1,param2/String/", "myFunction", new String[]{null, null}, new String[]{"String", null}, new String[] {"param1", "param2"},null, "String", null, null, 0);
	}
	
	public void testDecodeIndexKey_19() {
		runDecodeIndexKeyTest("myFunction/String,/param1,param2//foo.bar.Type", "myFunction", new String[]{null, null}, new String[]{"String", null}, new String[] {"param1", "param2"},null, null, "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_20() {
		runDecodeIndexKeyTest("myFunction/String,/param1,param2/String/foo.bar.Type", "myFunction", new String[]{null, null}, new String[]{"String", null}, new String[] {"param1", "param2"},null, "String", "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_21() {
		runDecodeIndexKeyTest("myFunction/bar.foo.String,neto.crazy.Number/param1,param2//", "myFunction", new String[]{"bar.foo", "neto.crazy"}, new String[]{"String", "Number"}, new String[] {"param1", "param2"},null, null, null, null, 0);
	}
	
	public void testDecodeIndexKey_22() {
		runDecodeIndexKeyTest("myFunction/bar.foo.String,neto.crazy.Number/param1,param2/String/", "myFunction", new String[]{"bar.foo", "neto.crazy"}, new String[]{"String", "Number"}, new String[] {"param1", "param2"},null, "String", null, null, 0);
	}
	
	public void testDecodeIndexKey_23() {
		runDecodeIndexKeyTest("myFunction/bar.foo.String,neto.crazy.Number/param1,param2//foo.bar.Type", "myFunction", new String[]{"bar.foo", "neto.crazy"}, new String[]{"String", "Number"}, new String[] {"param1", "param2"},null, null, "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_24() {
		runDecodeIndexKeyTest("myFunction/bar.foo.String,neto.crazy.Number/param1,param2/String/foo.bar.Type", "myFunction", new String[]{"bar.foo", "neto.crazy"}, new String[]{"String", "Number"}, new String[] {"param1", "param2"},null, "String", "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_25() {
		runDecodeIndexKeyTest("myFunction/,neto.crazy.Number/param1,param2//", "myFunction", new String[]{null, "neto.crazy"}, new String[]{null, "Number"}, new String[] {"param1", "param2"},null, null, null, null, 0);
	}
	
	public void testDecodeIndexKey_26() {
		runDecodeIndexKeyTest("myFunction/,neto.crazy.Number/param1,param2/String/", "myFunction", new String[]{null, "neto.crazy"}, new String[]{null, "Number"}, new String[] {"param1", "param2"},null, "String", null, null, 0);
	}
	
	public void testDecodeIndexKey_27() {
		runDecodeIndexKeyTest("myFunction/,neto.crazy.Number/param1,param2//foo.bar.Type", "myFunction", new String[]{null, "neto.crazy"}, new String[]{null, "Number"}, new String[] {"param1", "param2"},null, null, "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_28() {
		runDecodeIndexKeyTest("myFunction/,neto.crazy.Number/param1,param2/String/foo.bar.Type", "myFunction", new String[]{null, "neto.crazy"}, new String[]{null, "Number"}, new String[] {"param1", "param2"},null, "String", "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_29() {
		runDecodeIndexKeyTest("myFunction/bar.foo.String,/param1,param2//", "myFunction", new String[]{"bar.foo", null}, new String[]{"String", null}, new String[] {"param1", "param2"},null, null, null, null, 0);
	}
	
	public void testDecodeIndexKey_30() {
		runDecodeIndexKeyTest("myFunction/bar.foo.String,/param1,param2/String/", "myFunction", new String[]{"bar.foo", null}, new String[]{"String", null}, new String[] {"param1", "param2"},null, "String", null, null, 0);
	}
	
	public void testDecodeIndexKey_31() {
		runDecodeIndexKeyTest("myFunction/bar.foo.String,/param1,param2//foo.bar.Type", "myFunction", new String[]{"bar.foo", null}, new String[]{"String", null}, new String[] {"param1", "param2"},null, null, "foo.bar", "Type", 0);
	}
	
	public void testDecodeIndexKey_32() {
		runDecodeIndexKeyTest("myFunction/bar.foo.String,/param1,param2/String/foo.bar.Type", "myFunction", new String[]{"bar.foo", null}, new String[]{"String", null}, new String[] {"param1", "param2"},null, "String", "foo.bar", "Type", 0);
	}
	
	/**
	 * <p>Runs the asserts for a single create index key test.</p>
	 * 
	 * @param expected
	 * @param selector
	 * @param parameterCount
	 * @param parameterFullTypeNames
	 * @param parameterNames
	 * @param declaringFullTypeName
	 * @param returnFullTypeName
	 * @param modifiers
	 */
	private static void runCreateIndexKeyTest(String expected,
			char[] selector,
			char[][] parameterFullTypeNames,
			char[][] parameterNames,
			char[] declaringFullTypeName,
			char[] returnFullTypeName,
			int modifiers) {
		
		char[] indexKey = MethodPattern.createIndexKey(selector, parameterFullTypeNames, parameterNames, declaringFullTypeName, returnFullTypeName, modifiers);
		
		String expectedWithModifiers = expected + (char) modifiers + (char) (modifiers>>16);
		
		assertNotNull("The created index key should not be null.\nEXPECTED:\n" + expectedWithModifiers, indexKey);
		
		assertTrue("The expected index key does not match the generated index key.\nWAS:\n" + new String(indexKey) + "\nEXPECTED:\n" + expected,
				CharOperation.equals(indexKey, expectedWithModifiers.toCharArray()));
	}
	
	/**
	 * <p>Runs a single decode index key test</p>
	 * 
	 * @param key
	 * @param selector
	 * @param parameterCount
	 * @param parameterQualifications
	 * @param parameterSimpleNames
	 * @param parameterNames
	 * @param returnQualification
	 * @param returnSimpleName
	 * @param declaringQualification
	 * @param declaringSimpleName
	 * @param modifiers
	 */
	private static void runDecodeIndexKeyTest(String key,
			String selector,
			String[] parameterQualifications,
			String[] parameterSimpleNames,
			String[] parameterNames,
			String returnQualification,
			String returnSimpleName,
			String declaringQualification,
			String declaringSimpleName,
			int modifiers) {
		
		char[] keyWithModifiers = new char[key.length() + 3];
		System.arraycopy(key.toCharArray(), 0, keyWithModifiers, 0, key.length());
		keyWithModifiers[keyWithModifiers.length-3] = '/';
		keyWithModifiers[keyWithModifiers.length-2] = (char) modifiers;
		keyWithModifiers[keyWithModifiers.length-1] = (char) (modifiers>>16);
		
		MethodPattern pattern = new MethodPattern(false, false, false, null, 0);
		pattern = (MethodPattern)pattern.getBlankPattern();
		pattern.decodeIndexKey(keyWithModifiers);
		
		assertEquals("Expected selector does not equal decoded selector.", selector, pattern.selector);
		assertEquals("Expected parameter qualifications does not equal decoded parameter qualifications.", parameterQualifications, pattern.parameterQualifications);
		assertEquals("Expected parameter simple names does not equal decoded parameter simple names.", parameterSimpleNames, pattern.parameterSimpleNames);
		assertEquals("Expected parameter names does not equal decoded parameter names.", parameterNames, pattern.parameterNames);
		assertEquals("Expected return qualification does not equal decoded return wualification.", returnQualification, pattern.returnQualification);
		assertEquals("Expected return simple name does not equal decoded return simple name.", returnSimpleName, pattern.returnSimpleName);
		assertEquals("Expected declaring qualification does not equal decoded declaring qualification.", declaringQualification, pattern.getDeclaringQualification());
		assertEquals("Expected declaring simple name does not equal decoded declaring simple name.", declaringSimpleName, pattern.getDeclaringSimpleName());
	}
	
	private static void assertEquals(String message, String expected, char[] chars) {
		String actual = null;
		if(chars != null) {
			actual = new String(chars);
		}
		
		assertEquals(message, expected, actual);
	}
	
	private static void assertEquals(String message, String[] expected, char[][] actual) {
		
		String expectedColappsed = null;
		String actualColappsed = null;
		if(actual != null) {
			char[] actualColappsedChars = CharOperation.concatWith(actual, IIndexConstants.PARAMETER_SEPARATOR, false);
			if(actualColappsedChars != null) {
				actualColappsed = new String(actualColappsedChars);
			}
		}
		
		if(expected != null) {
			expectedColappsed = "";
			for(int i = 0; i < expected.length; ++i) {
				if(i > 0) {
					expectedColappsed += ",";
				}
				
				if(expected[i] != null) {
					expectedColappsed += expected[i];
				}
			}
		}
		
		assertEquals(message, expectedColappsed, actualColappsed);
	}
}
