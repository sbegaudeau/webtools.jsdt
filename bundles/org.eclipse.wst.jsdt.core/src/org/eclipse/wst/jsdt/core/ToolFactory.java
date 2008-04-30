/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.wst.jsdt.core.compiler.IScanner;
import org.eclipse.wst.jsdt.core.formatter.CodeFormatter;
import org.eclipse.wst.jsdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.wst.jsdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.util.PublicScanner;
import org.eclipse.wst.jsdt.internal.formatter.DefaultCodeFormatter;

/**
 * Factory for creating various compiler tools, such as scanners, parsers and compilers.
 * <p>
 *  This class provides static methods only; it is not intended to be instantiated or subclassed by clients.
 * </p>
 *
 * @since 2.0
 */
public class ToolFactory {

	/**
	 * This mode is used for formatting new code when some formatter options should not be used.
	 * In particular, options that preserve the indentation of comments are not used.
	 * In the future,  newly added options may be ignored as well.
	 * <p>Clients that are formatting new code are recommended to use this mode.
	 * </p>
	 *
	 * @see DefaultCodeFormatterConstants#FORMATTER_NEVER_INDENT_BLOCK_COMMENTS_ON_FIRST_COLUMN
	 * @see DefaultCodeFormatterConstants#FORMATTER_NEVER_INDENT_LINE_COMMENTS_ON_FIRST_COLUMN
	 * @see #createCodeFormatter(Map, int)
	 * @since 3.3
	 */
	public static final int M_FORMAT_NEW = new Integer(0).intValue();

	/**
	 * This mode is used for formatting existing code when all formatter options should be used.
	 * In particular, options that preserve the indentation of comments are used.
	 * <p>Clients that are formatting existing code are recommended to use this mode.
	 * </p>
	 *
	 * @see DefaultCodeFormatterConstants#FORMATTER_NEVER_INDENT_BLOCK_COMMENTS_ON_FIRST_COLUMN
	 * @see DefaultCodeFormatterConstants#FORMATTER_NEVER_INDENT_LINE_COMMENTS_ON_FIRST_COLUMN
	 * @see #createCodeFormatter(Map, int)
	 * @since 3.3
	 */
	public static final int M_FORMAT_EXISTING = new Integer(1).intValue();

	/**
	 * Create an instance of a code formatter. A code formatter implementation can be contributed via the
	 * extension point "org.eclipse.wst.jsdt.core.codeFormatter". If unable to find a registered extension, the factory
	 * will default to using the default code formatter.
	 *
	 * @return an instance of a code formatter
	 * @see ICodeFormatter
	 * @see ToolFactory#createDefaultCodeFormatter(Map)
	 * @deprecated Use {@link #createCodeFormatter(Map)} instead. Extension point is discontinued
	 */
	public static ICodeFormatter createCodeFormatter(){

			Plugin jdtCorePlugin = JavaScriptCore.getPlugin();
			if (jdtCorePlugin == null) return null;

			IExtensionPoint extension = jdtCorePlugin.getDescriptor().getExtensionPoint(JavaModelManager.FORMATTER_EXTPOINT_ID);
			if (extension != null) {
				IExtension[] extensions =  extension.getExtensions();
				for(int i = 0; i < extensions.length; i++){
					IConfigurationElement [] configElements = extensions[i].getConfigurationElements();
					for(int j = 0; j < configElements.length; j++){
						try {
							Object execExt = configElements[j].createExecutableExtension("class"); //$NON-NLS-1$
							if (execExt instanceof ICodeFormatter){
								// use first contribution found
								return (ICodeFormatter)execExt;
							}
						} catch(CoreException e){
							// unable to instantiate extension, will answer default formatter instead
						}
					}
				}
			}
		// no proper contribution found, use default formatter
		return createDefaultCodeFormatter(null);
	}

	/**
	 * Create an instance of the built-in code formatter.
	 * <p>The given options should at least provide the source level ({@link JavaScriptCore#COMPILER_SOURCE}),
	 * the  compiler compliance level ({@link JavaScriptCore#COMPILER_COMPLIANCE}) and the target platform
	 * ({@link JavaScriptCore#COMPILER_CODEGEN_TARGET_PLATFORM}).
	 * Without these options, it is not possible for the code formatter to know what kind of source it needs to format.
	 * </p><p>
	 * Note this is equivalent to <code>createCodeFormatter(options, M_FORMAT_NEW)</code>. Thus some code formatter options
	 * may be ignored. See @{link {@link #M_FORMAT_NEW} for more details.
	 * </p>
	 * @param options - the options map to use for formatting with the default code formatter. Recognized options
	 * 	are documented on <code>JavaScriptCore#getDefaultOptions()</code>. If set to <code>null</code>, then use
	 * 	the current settings from <code>JavaScriptCore#getOptions</code>.
	 * @return an instance of the built-in code formatter
	 * @see CodeFormatter
	 * @see JavaScriptCore#getOptions()
	 * @since 3.0
	 */
	public static CodeFormatter createCodeFormatter(Map options){
		return createCodeFormatter(options, M_FORMAT_NEW);
	}

	/**
	 * Create an instance of the built-in code formatter.
	 * <p>The given options should at least provide the source level ({@link JavaScriptCore#COMPILER_SOURCE}),
	 * the  compiler compliance level ({@link JavaScriptCore#COMPILER_COMPLIANCE}) and the target platform
	 * ({@link JavaScriptCore#COMPILER_CODEGEN_TARGET_PLATFORM}).
	 * Without these options, it is not possible for the code formatter to know what kind of source it needs to format.
	 * </p>
	 * <p>The given mode determines what options should be enabled when formatting the code. It can have the following
	 * values: {@link #M_FORMAT_NEW}, {@link #M_FORMAT_EXISTING}, but other values may be added in the future.
	 * </p>
	 *
	 * @param options the options map to use for formatting with the default code formatter. Recognized options
	 * 	are documented on <code>JavaScriptCore#getDefaultOptions()</code>. If set to <code>null</code>, then use
	 * 	the current settings from <code>JavaScriptCore#getOptions</code>.
	 * @param mode the given mode to modify the given options.
	 *
	 * @return an instance of the built-in code formatter
	 * @see CodeFormatter
	 * @see JavaScriptCore#getOptions()
	 * @since 3.3
	 */
	public static CodeFormatter createCodeFormatter(Map options, int mode) {
		if (options == null) options = JavaScriptCore.getOptions();
		Map currentOptions = new HashMap(options);
		if (mode == M_FORMAT_NEW) {
			// disable the option for not indenting comments starting on first column
			currentOptions.put(DefaultCodeFormatterConstants.FORMATTER_NEVER_INDENT_BLOCK_COMMENTS_ON_FIRST_COLUMN, DefaultCodeFormatterConstants.FALSE);
			currentOptions.put(DefaultCodeFormatterConstants.FORMATTER_NEVER_INDENT_LINE_COMMENTS_ON_FIRST_COLUMN, DefaultCodeFormatterConstants.FALSE);
		}
		return new DefaultCodeFormatter(currentOptions);
	}
 




	/**
	 * Create an instance of the built-in code formatter. A code formatter implementation can be contributed via the
	 * extension point "org.eclipse.wst.jsdt.core.codeFormatter". If unable to find a registered extension, the factory will
	 * default to using the default code formatter.
	 *
	 * @param options - the options map to use for formatting with the default code formatter. Recognized options
	 * 	are documented on <code>JavaScriptCore#getDefaultOptions()</code>. If set to <code>null</code>, then use
	 * 	the current settings from <code>JavaScriptCore#getOptions</code>.
	 * @return an instance of the built-in code formatter
	 * @see ICodeFormatter
	 * @see ToolFactory#createCodeFormatter()
	 * @see JavaScriptCore#getOptions()
	 * @deprecated Use {@link #createCodeFormatter(Map)} instead
	 */
	public static ICodeFormatter createDefaultCodeFormatter(Map options){
		if (options == null) options = JavaScriptCore.getOptions();
		return new org.eclipse.wst.jsdt.internal.formatter.old.CodeFormatter(options);
	}

	/**
	 * Create a scanner, indicating the level of detail requested for tokenizing. The scanner can then be
	 * used to tokenize some source in a Java aware way.
	 * Here is a typical scanning loop:
	 *
	 * <code>
	 * <pre>
	 *   IScanner scanner = ToolFactory.createScanner(false, false, false, false);
	 *   scanner.setSource("int i = 0;".toCharArray());
	 *   while (true) {
	 *     int token = scanner.getNextToken();
	 *     if (token == ITerminalSymbols.TokenNameEOF) break;
	 *     System.out.println(token + " : " + new String(scanner.getCurrentTokenSource()));
	 *   }
	 * </pre>
	 * </code>
	 *
	 * <p>
	 * The returned scanner will tolerate unterminated line comments (missing line separator). It can be made stricter
	 * by using API with extra boolean parameter (<code>strictCommentMode</code>).
	 * <p>
	 * @param tokenizeComments if set to <code>false</code>, comments will be silently consumed
	 * @param tokenizeWhiteSpace if set to <code>false</code>, white spaces will be silently consumed,
	 * @param assertMode if set to <code>false</code>, occurrences of 'assert' will be reported as identifiers
	 * (<code>ITerminalSymbols#TokenNameIdentifier</code>), whereas if set to <code>true</code>, it
	 * would report assert keywords (<code>ITerminalSymbols#TokenNameassert</code>). Java 1.4 has introduced
	 * a new 'assert' keyword.
	 * @param recordLineSeparator if set to <code>true</code>, the scanner will record positions of encountered line
	 * separator ends. In case of multi-character line separators, the last character position is considered. These positions
	 * can then be extracted using <code>IScanner#getLineEnds</code>. Only non-unicode escape sequences are
	 * considered as valid line separators.
  	 * @return a scanner
	 * @see org.eclipse.wst.jsdt.core.compiler.IScanner
	 */
	public static IScanner createScanner(boolean tokenizeComments, boolean tokenizeWhiteSpace, boolean assertMode, boolean recordLineSeparator){

		PublicScanner scanner = new PublicScanner(tokenizeComments, tokenizeWhiteSpace, false/*nls*/, assertMode ? ClassFileConstants.JDK1_4 : ClassFileConstants.JDK1_3/*sourceLevel*/, null/*taskTags*/, null/*taskPriorities*/, true/*taskCaseSensitive*/);
		scanner.recordLineSeparator = recordLineSeparator;
		return scanner;
	}

	/**
	 * Create a scanner, indicating the level of detail requested for tokenizing. The scanner can then be
	 * used to tokenize some source in a Java aware way.
	 * Here is a typical scanning loop:
	 *
	 * <code>
	 * <pre>
	 *   IScanner scanner = ToolFactory.createScanner(false, false, false, false);
	 *   scanner.setSource("int i = 0;".toCharArray());
	 *   while (true) {
	 *     int token = scanner.getNextToken();
	 *     if (token == ITerminalSymbols.TokenNameEOF) break;
	 *     System.out.println(token + " : " + new String(scanner.getCurrentTokenSource()));
	 *   }
	 * </pre>
	 * </code>
	 *
	 * <p>
	 * The returned scanner will tolerate unterminated line comments (missing line separator). It can be made stricter
	 * by using API with extra boolean parameter (<code>strictCommentMode</code>).
	 * <p>
	 * @param tokenizeComments if set to <code>false</code>, comments will be silently consumed
	 * @param tokenizeWhiteSpace if set to <code>false</code>, white spaces will be silently consumed,
	 * @param recordLineSeparator if set to <code>true</code>, the scanner will record positions of encountered line
	 * separator ends. In case of multi-character line separators, the last character position is considered. These positions
	 * can then be extracted using <code>IScanner#getLineEnds</code>. Only non-unicode escape sequences are
	 * considered as valid line separators.
	 * @param sourceLevel if set to <code>&quot;1.3&quot;</code> or <code>null</code>, occurrences of 'assert' will be reported as identifiers
	 * (<code>ITerminalSymbols#TokenNameIdentifier</code>), whereas if set to <code>&quot;1.4&quot;</code>, it
	 * would report assert keywords (<code>ITerminalSymbols#TokenNameassert</code>). Java 1.4 has introduced
	 * a new 'assert' keyword.
  	 * @return a scanner
	 * @see org.eclipse.wst.jsdt.core.compiler.IScanner
     * @since 3.0
	 */
	public static IScanner createScanner(boolean tokenizeComments, boolean tokenizeWhiteSpace, boolean recordLineSeparator, String sourceLevel) {
		PublicScanner scanner = null;
		long level = CompilerOptions.versionToJdkLevel(sourceLevel);
		if (level == 0) level = ClassFileConstants.JDK1_3; // fault-tolerance
		scanner = new PublicScanner(tokenizeComments, tokenizeWhiteSpace, false/*nls*/,level /*sourceLevel*/, null/*taskTags*/, null/*taskPriorities*/, true/*taskCaseSensitive*/);
		scanner.recordLineSeparator = recordLineSeparator;
		return scanner;
	}

	/**
	 * Create a scanner, indicating the level of detail requested for tokenizing. The scanner can then be
	 * used to tokenize some source in a Java aware way.
	 * Here is a typical scanning loop:
	 *
	 * <code>
	 * <pre>
	 *   IScanner scanner = ToolFactory.createScanner(false, false, false, false);
	 *   scanner.setSource("int i = 0;".toCharArray());
	 *   while (true) {
	 *     int token = scanner.getNextToken();
	 *     if (token == ITerminalSymbols.TokenNameEOF) break;
	 *     System.out.println(token + " : " + new String(scanner.getCurrentTokenSource()));
	 *   }
	 * </pre>
	 * </code>
	 *
	 * <p>
	 * The returned scanner will tolerate unterminated line comments (missing line separator). It can be made stricter
	 * by using API with extra boolean parameter (<code>strictCommentMode</code>).
	 * <p>
	 * @param tokenizeComments if set to <code>false</code>, comments will be silently consumed
	 * @param tokenizeWhiteSpace if set to <code>false</code>, white spaces will be silently consumed,
	 * @param recordLineSeparator if set to <code>true</code>, the scanner will record positions of encountered line
	 * separator ends. In case of multi-character line separators, the last character position is considered. These positions
	 * can then be extracted using <code>IScanner#getLineEnds</code>. Only non-unicode escape sequences are
	 * considered as valid line separators.
	 * @param sourceLevel if set to <code>&quot;1.3&quot;</code> or <code>null</code>, occurrences of 'assert' will be reported as identifiers
	 * (<code>ITerminalSymbols#TokenNameIdentifier</code>), whereas if set to <code>&quot;1.4&quot;</code>, it
	 * would report assert keywords (<code>ITerminalSymbols#TokenNameassert</code>). Java 1.4 has introduced
	 * a new 'assert' keyword.
	 * @param complianceLevel This is used to support the Unicode 4.0 character sets. if set to 1.5 or above,
	 * the Unicode 4.0 is supporte, otherwise Unicode 3.0 is supported.
  	 * @return a scanner
	 * @see org.eclipse.wst.jsdt.core.compiler.IScanner
	 *
     * @since 3.1
	 */
	public static IScanner createScanner(boolean tokenizeComments, boolean tokenizeWhiteSpace, boolean recordLineSeparator, String sourceLevel, String complianceLevel) {
		PublicScanner scanner = null;
		long sourceLevelValue = CompilerOptions.versionToJdkLevel(sourceLevel);
		if (sourceLevelValue == 0) sourceLevelValue = ClassFileConstants.JDK1_3; // fault-tolerance
		long complianceLevelValue = CompilerOptions.versionToJdkLevel(complianceLevel);
		if (complianceLevelValue == 0) complianceLevelValue = ClassFileConstants.JDK1_3; // fault-tolerance
		scanner = new PublicScanner(tokenizeComments, tokenizeWhiteSpace, false/*nls*/,sourceLevelValue /*sourceLevel*/, complianceLevelValue, null/*taskTags*/, null/*taskPriorities*/, true/*taskCaseSensitive*/);
		scanner.recordLineSeparator = recordLineSeparator;
		return scanner;
	}
}
