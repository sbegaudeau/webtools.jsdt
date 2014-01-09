/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.compiler.parser;

/**
 * IMPORTANT NOTE: These constants are dedicated to the internal Scanner implementation. 
 * It is mirrored in org.eclipse.wst.jsdt.core.compiler public package where it is API. 
 * The mirror implementation is using the backward compatible ITerminalSymbols constant 
 * definitions (stable with 2.0), whereas the internal implementation uses TerminalTokens 
 * which constant values reflect the latest parser generation state.
 */
/**
 * Maps each terminal symbol in the java-grammar into a unique integer. 
 * This integer is used to represent the terminal when computing a parsing action. 
 * 
 * Disclaimer : These constant values are generated automatically using a Java 
 * grammar, therefore their actual values are subject to change if new keywords 
 * were added to the language (for instance, 'assert' is a keyword in 1.4).
 */
public interface TerminalTokens {

	// special tokens not part of grammar - not autogenerated
	int TokenNameWHITESPACE = 1000,
		TokenNameCOMMENT_LINE = 1001,
		TokenNameCOMMENT_BLOCK = 1002,
		TokenNameCOMMENT_JAVADOC = 1003;

	int TokenNameIdentifier = 27,
		TokenNameabstract = 56,
		TokenNameassert = 75,
		TokenNameboolean = 32,
		TokenNamebreak = 76,
		TokenNamebyte = 33,
		TokenNamecase = 100,
		TokenNamecatch = 101,
		TokenNamechar = 34,
		TokenNameclass = 71,
		TokenNamecontinue = 77,
		TokenNamedefault = 98,
		TokenNamedo = 78,
		TokenNamedouble = 35,
		TokenNameelse = 102,
		TokenNameenum = 103,
		TokenNameextends = 86,
		TokenNamefalse = 44,
		TokenNamefinal = 57,
		TokenNamefinally = 104,
		TokenNamefloat = 36,
		TokenNamefor = 79,
		TokenNameif = 80,
		TokenNameimplements = 105,
		TokenNameimport = 99,
		TokenNameinstanceof = 12,
		TokenNameint = 37,
		TokenNameinterface = 72,
		TokenNamelong = 38,
		TokenNamenative = 58,
		TokenNamenew = 43,
		TokenNamenull = 45,
		TokenNamepackage = 85,
		TokenNameprivate = 59,
		TokenNameprotected = 60,
		TokenNamepublic = 61,
		TokenNamereturn = 81,
		TokenNameshort = 39,
		TokenNamestatic = 54,
		TokenNamestrictfp = 62,
		TokenNamesuper = 41,
		TokenNameswitch = 82,
		TokenNamesynchronized = 55,
		TokenNamethis = 42,
		TokenNamethrow = 83,
		TokenNamethrows = 106,
		TokenNametransient = 63,
		TokenNametrue = 46,
		TokenNametry = 84,
		TokenNamevoid = 40,
		TokenNamevolatile = 64,
		TokenNamewhile = 73,
		TokenNameIntegerLiteral = 47,
		TokenNameLongLiteral = 48,
		TokenNameFloatingPointLiteral = 49,
		TokenNameDoubleLiteral = 50,
		TokenNameCharacterLiteral = 51,
		TokenNameStringLiteral = 52,
		TokenNamePLUS_PLUS = 8,
		TokenNameMINUS_MINUS = 9,
		TokenNameEQUAL_EQUAL = 18,
		TokenNameLESS_EQUAL = 14,
		TokenNameGREATER_EQUAL = 15,
		TokenNameNOT_EQUAL = 19,
		TokenNameLEFT_SHIFT = 17,
		TokenNameRIGHT_SHIFT = 10,
		TokenNameUNSIGNED_RIGHT_SHIFT = 11,
		TokenNamePLUS_EQUAL = 87,
		TokenNameMINUS_EQUAL = 88,
		TokenNameMULTIPLY_EQUAL = 89,
		TokenNameDIVIDE_EQUAL = 90,
		TokenNameAND_EQUAL = 91,
		TokenNameOR_EQUAL = 92,
		TokenNameXOR_EQUAL = 93,
		TokenNameREMAINDER_EQUAL = 94,
		TokenNameLEFT_SHIFT_EQUAL = 95,
		TokenNameRIGHT_SHIFT_EQUAL = 96,
		TokenNameUNSIGNED_RIGHT_SHIFT_EQUAL = 97,
		TokenNameOR_OR = 26,
		TokenNameAND_AND = 25,
		TokenNamePLUS = 1,
		TokenNameMINUS = 2,
		TokenNameNOT = 69,
		TokenNameREMAINDER = 5,
		TokenNameXOR = 21,
		TokenNameAND = 20,
		TokenNameMULTIPLY = 4,
		TokenNameOR = 22,
		TokenNameTWIDDLE = 67,
		TokenNameDIVIDE = 6,
		TokenNameGREATER = 16,
		TokenNameLESS = 7,
		TokenNameLPAREN = 29,
		TokenNameRPAREN = 28,
		TokenNameLBRACE = 66,
		TokenNameRBRACE = 31,
		TokenNameLBRACKET = 13,
		TokenNameRBRACKET = 70,
		TokenNameSEMICOLON = 23,
		TokenNameQUESTION = 24,
		TokenNameCOLON = 65,
		TokenNameCOMMA = 30,
		TokenNameDOT = 3,
		TokenNameEQUAL = 74,
		TokenNameAT = 53,
		TokenNameELLIPSIS = 107,
		TokenNameEOF = 68,
		TokenNameERROR = 108;
}