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
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import junit.framework.Test;

import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.ToolFactory;
import org.eclipse.wst.jsdt.core.compiler.IScanner;
import org.eclipse.wst.jsdt.core.compiler.ITerminalSymbols;
import org.eclipse.wst.jsdt.core.compiler.InvalidInputException;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.parser.Scanner;
import org.eclipse.wst.jsdt.internal.compiler.parser.TerminalTokens;
import org.eclipse.wst.jsdt.internal.core.util.PublicScanner;

public class ScannerTest extends AbstractRegressionTest {

	public ScannerTest(String name) { 
		super(name);
	}
	// Static initializer to specify tests subset using TESTS_* static variables
	// All specified tests which does not belong to the class are skipped...
	static {
//		TESTS_NAMES = new String[] { "test000" };
//		TESTS_NUMBERS = new int[] { 42, 43, 44 };
//		TESTS_RANGE = new int[] { 11, -1 };
	}

	public static Class testClass() {
		return ScannerTest.class;
	}

	/**
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=23181
	 */
	public void test001() {
		String sourceA001 = "\\u003b";
		IScanner scanner = ToolFactory.createScanner(false, true, false, false);
		scanner.setSource(sourceA001.toCharArray());
		int token = 0;
		try {
			token = scanner.getNextToken();
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Wrong token type", ITerminalSymbols.TokenNameSEMICOLON, token);
	}
	/**
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=23181
	 */
	public void test002() {
		String sourceA002 = "// tests\n  ";
		IScanner scanner = ToolFactory.createScanner(false, true, false, false);
		scanner.setSource(sourceA002.toCharArray());
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameWHITESPACE, token);
			assertEquals("Wrong size", 2, scanner.getCurrentTokenSource().length);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}
	/**
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=23181
	 */
	public void test003() {
		String sourceA003 = "// tests\n  ";
		IScanner scanner = ToolFactory.createScanner(true, true, false, false);
		scanner.setSource(sourceA003.toCharArray());
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameCOMMENT_LINE, token);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameWHITESPACE, token);
			assertEquals("Wrong size", 2, scanner.getCurrentTokenSource().length);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}

	/**
	 * float constant can have exponent part without dot: 01e0f
	 * http://bugs.eclipse.org/bugs/show_bug.cgi?id=30704
	 */
	public void test004() {
		String source = "01e0f";
		IScanner scanner = ToolFactory.createScanner(false, false, false, false);
		scanner.setSource(source.toCharArray());
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameFloatingPointLiteral, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=43437
	 */
	public void test005() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"Hello\"");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameStringLiteral, token);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}

	/**
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=43485
	 */
	public void test006() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, false);
		try {
			scanner.setSource(null);
		} catch (NullPointerException e) {
			assertTrue(false);
		}
	}

	/*
	 * Check that bogus resetTo issues EOFs
	 */
	public void test007() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, false);
		char[] source = "int i = 0;".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(source.length + 50, source.length - 1);
		int token = -1;
		try {
			token = scanner.getNextToken();
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Expecting EOF", ITerminalSymbols.TokenNameEOF, token);
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74126
	 */
	public void test008() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0x11aa.aap-3333f".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int token = -1;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameFloatingPointLiteral, token);
			token = scanner.getNextToken();
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Expecting EOF", ITerminalSymbols.TokenNameEOF, token);
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74126
	 */
	public void test009() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_4);
		char[] source = "0x11aa.aap-3333f".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Wrong number of tokens", 5, counter);
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74126
	 */
	public void test010() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0x11aa.aap-3333f".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Wrong number of tokens", 1, counter);
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74126
	 */
	public void test011() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0x.aap-3333f".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Wrong number of tokens", 1, counter);
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74126
	 */
	public void test012() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0xaap3f".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Wrong number of tokens", 1, counter);
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74126
	 */
	public void test013() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0xaapaf".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74126
	 */
	public void test014() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0xaap.1f".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74126
	 */
	public void test015() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0xaa.p1f".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Wrong number of tokens", 1, counter);
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74126
	 */
	public void test016() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0xaa.p1F".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Wrong number of tokens", 1, counter);
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74126
	 */
	public void test017() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0xaa.p1D".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Wrong number of tokens", 1, counter);
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74126
	 */
	public void test018() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0xaa.p1d".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Wrong number of tokens", 1, counter);
	}
	
	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74934
	 */
	public void test019() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_4);
		char[] source = "0x".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}
	
	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74934
	 */
	public void test020() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0x".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(true);
			return;
		}
		assertTrue(false);
	}
	
	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74934
	 */
	public void test021() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_4);
		char[] source = "0x1".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Wrong number of tokens", 1, counter);
	}
	
	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=74934
	 */
	public void test022() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0x1".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		assertEquals("Wrong number of tokens", 1, counter);
	}
	
	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=78905 
	 */
	public void test023() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_5);
		char[] source = "0x.p-2".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
			}
			assertTrue(false);
		} catch (InvalidInputException e) {
			assertTrue(true);
		}
	}

	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=84398
	 */
	public void test024() {
		IScanner scanner = ToolFactory.createScanner(false, false, true, JavaScriptCore.VERSION_1_5);
		char[] source = "public class X {\n\n}".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		int counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
		}
		
		assertEquals("wrong number of tokens", 5, counter);
		int[] lineEnds = scanner.getLineEnds();
		assertNotNull("No line ends", lineEnds);
		assertEquals("wrong length", 2, lineEnds.length);
		source = "public class X {}".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		lineEnds = scanner.getLineEnds();
		assertNotNull("No line ends", lineEnds);
		assertEquals("wrong length", 0, lineEnds.length);
		
		counter = 0;
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
				counter++;
			}
		} catch (InvalidInputException e) {
		}
		
		assertEquals("wrong number of tokens", 5, counter);
		lineEnds = scanner.getLineEnds();
		assertNotNull("No line ends", lineEnds);
		assertEquals("wrong length", 0, lineEnds.length);
	}
	
	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=84398
	 */
	public void test025() {
		IScanner scanner = ToolFactory.createScanner(true, true, false, true);
		scanner.setSource("String\r\nwith\r\nmany\r\nmany\r\nline\r\nbreaks".toCharArray());
		
		try {
			while(scanner.getNextToken()!=ITerminalSymbols.TokenNameEOF){}
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
		
		assertEquals("Wrong size", 5, scanner.getLineEnds().length);
		
		scanner.setSource("No line breaks here".toCharArray()); // expecting line breaks to reset
		assertEquals("Wrong size", 0, scanner.getLineEnds().length);
	}
	
	/*
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=86611 
	 */
	public void test026() {
		IScanner scanner = ToolFactory.createScanner(false, false, false, JavaScriptCore.VERSION_1_4);
		char[] source = "0x.p-2".toCharArray(); //$NON-NLS-1$
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		try {
			while (scanner.getNextToken() != ITerminalSymbols.TokenNameEOF) {
			}
			assertTrue(false);
		} catch (InvalidInputException e) {
			assertTrue(true);
		}
	}
	//https://bugs.eclipse.org/bugs/show_bug.cgi?id=90414
	public void test027() {
		char[] source = ("class Test {\n" +
				"  char  C = \"\\u005Cn\";\n" +
				"}").toCharArray();
		Scanner scanner = new Scanner(false, false, false, ClassFileConstants.JDK1_4, null, null, false);
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		try {
			int token;
			StringBuffer buffer = new StringBuffer();
			while ((token = scanner.getNextToken()) != TerminalTokens.TokenNameEOF) {
				try {
					switch(token) {
						case TerminalTokens.TokenNameEOF :
							break;
						default :
							buffer.append(scanner.getCurrentTokenSource());
							break;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
			assertEquals("Wrong contents", "classTest{charC=\"\n\";}", String.valueOf(buffer));
		} catch (InvalidInputException e) {
			assertTrue(false);
		}		
	}
	//https://bugs.eclipse.org/bugs/show_bug.cgi?id=90414
	public void test029() {
		char[] source = ("class Test {\n" +
				"  char  C = \"\\n\";\n" +
				"}").toCharArray();
		Scanner scanner = new Scanner(false, false, false, ClassFileConstants.JDK1_4, null, null, false);
		scanner.setSource(source);
		scanner.resetTo(0, source.length - 1);
		try {
			int token;
			StringBuffer buffer = new StringBuffer();
			while ((token = scanner.getNextToken()) != TerminalTokens.TokenNameEOF) {
				try {
					switch(token) {
						case TerminalTokens.TokenNameEOF :
							break;
						default :
							buffer.append(scanner.getCurrentTokenSource());
							break;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
			assertEquals("Wrong contents", "classTest{charC=\"\n\";}", String.valueOf(buffer));
		} catch (InvalidInputException e) {
			assertTrue(false);
		}		
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106403
	public void test036() {
		try {
			IScanner s = ToolFactory.createScanner(true, true, true, "1.5", "1.5");
			char[] source = { ';', ' ' };
			s.setSource(source);
			s.resetTo(0, 0);
			int token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameSEMICOLON, token);
			char[] tokenSource = s.getCurrentTokenSource();
			assertEquals("wront size", 1, tokenSource.length);
			assertEquals("Wrong character", ';', tokenSource[0]);
			token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue("Should not happen", false);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106403
	public void test037() {
		try {
			IScanner s = ToolFactory.createScanner(true, true, true, "1.5", "1.5");
			char[] source = { ';', ' ' };
			s.setSource(source);
			int token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameSEMICOLON, token);
			char[] tokenSource = s.getCurrentTokenSource();
			assertEquals("wront size", 1, tokenSource.length);
			assertEquals("Wrong character", ';', tokenSource[0]);
			token = s.getNextToken();
			tokenSource = s.getCurrentTokenSource();
			assertEquals("wront size", 1, tokenSource.length);
			assertEquals("Wrong character", ' ', tokenSource[0]);
			assertEquals("Wrong token", ITerminalSymbols.TokenNameWHITESPACE, token);
			token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue("Should not happen", false);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106403
	public void test038() {
		try {
			IScanner s = ToolFactory.createScanner(true, true, true, "1.5", "1.5");
			char[] source = { ';', ' ' };
			s.setSource(source);
			s.resetTo(0, 1);
			int token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameSEMICOLON, token);
			char[] tokenSource = s.getCurrentTokenSource();
			assertEquals("wront size", 1, tokenSource.length);
			assertEquals("Wrong character", ';', tokenSource[0]);
			token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameWHITESPACE, token);
			tokenSource = s.getCurrentTokenSource();
			assertEquals("wront size", 1, tokenSource.length);
			assertEquals("Wrong character", ' ', tokenSource[0]);
			token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue("Should not happen", false);
		}
	}

	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106403
	public void test039() {
		try {
			IScanner s = ToolFactory.createScanner(true, true, true, "1.5", "1.5");
			char[] source = { ';', ' ' };
			s.setSource(source);
			s.resetTo(1, 1);
			int token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameWHITESPACE, token);
			char[] tokenSource = s.getCurrentTokenSource();
			assertEquals("wront size", 1, tokenSource.length);
			assertEquals("Wrong character", ' ', tokenSource[0]);
			token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue("Should not happen", false);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106403
	public void test040() {
		try {
			IScanner s = ToolFactory.createScanner(true, true, true, "1.5", "1.5");
			char[] source = { ';', ' ' };
			s.setSource(source);
			s.resetTo(2, 1);
			int token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue("Should not happen", false);
		}
	}
	
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=106403
	public void test041() {
		try {
			IScanner s = ToolFactory.createScanner(true, true, true, "1.5", "1.5");
			char[] source = "\\u003B\\u0020".toCharArray();
			assertEquals("wrong size", 12, source.length);
			s.setSource(source);
			s.resetTo(0, 5);
			int token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameSEMICOLON, token);
			char[] tokenSource = s.getRawTokenSource();
			assertEquals("wront size", 6, tokenSource.length);
			assertEquals("Wrong character", "\\u003B", new String(tokenSource));
			token = s.getNextToken();
			assertEquals("Wrong token", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue("Should not happen", false);
		}
	}
	
	public void test046() {
		StringBuffer buf = new StringBuffer();
		buf.append("'Hello'");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameCharacterLiteral, token);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}
	
	public void test047() {
		StringBuffer buf = new StringBuffer();
		buf.append("/=/g");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameRegExLiteral, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}
	
	public void test048() {
		StringBuffer buf = new StringBuffer();
		buf.append("// test unicode \\u000a var a =1; \n");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameCOMMENT_LINE, token);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}
	
	public void test049() {
		StringBuffer buf = new StringBuffer();
		buf.append("/* \n");
		buf.append("* test unicode \\u000a var a =1; \n ");
		buf.append("*/");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameCOMMENT_BLOCK, token);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameEOF, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}
	
	public void test050() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"a\\>\"");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameStringLiteral, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}

	public void test051() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"abc\\u000adef\";");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameStringLiteral, token);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameSEMICOLON, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}
	
	public void test052() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"abc\\u0022def\";\n");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameStringLiteral, token);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameSEMICOLON, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}
	
	public void test053() {
		StringBuffer buf = new StringBuffer();
		buf.append("'abc\\u0027def';\n");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameCharacterLiteral, token);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameSEMICOLON, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}

	public void test054() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"\\u0022def\";\n");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameStringLiteral, token);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameSEMICOLON, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}
	
	public void test055() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"abc\\x22def\";\n");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameStringLiteral, token);
			char[] characters = scanner.getCurrentTokenSource();
			String results = String.valueOf(characters);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameSEMICOLON, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}

	public void test056() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"\\x22def\";\n");
		String str = buf.toString();
		IScanner scanner = ToolFactory.createScanner(true, false, false, false);
		scanner.setSource(str.toCharArray());
		scanner.resetTo(0, str.length() - 1);
		int token = 0;
		try {
			token = scanner.getNextToken();
			char[] characters = scanner.getCurrentTokenSource();
			String results = String.valueOf(characters);
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameStringLiteral, token);
			token = scanner.getNextToken();
			assertEquals("Wrong token type", ITerminalSymbols.TokenNameSEMICOLON, token);
		} catch (InvalidInputException e) {
			assertTrue(false);
		}
	}
	
}
