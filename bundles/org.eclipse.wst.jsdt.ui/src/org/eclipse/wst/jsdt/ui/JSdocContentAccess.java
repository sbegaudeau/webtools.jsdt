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
package org.eclipse.wst.jsdt.ui;
 
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.jsdt.core.IBuffer;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IMember;
import org.eclipse.wst.jsdt.core.IFunction;
import org.eclipse.wst.jsdt.core.IOpenable;
import org.eclipse.wst.jsdt.core.ISourceRange;
import org.eclipse.wst.jsdt.core.IType;
import org.eclipse.wst.jsdt.core.ITypeHierarchy;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;
import org.eclipse.wst.jsdt.internal.core.MetadataFile;
import org.eclipse.wst.jsdt.internal.corext.javadoc.JavaDocCommentReader;
import org.eclipse.wst.jsdt.internal.corext.util.MethodOverrideTester;
import org.eclipse.wst.jsdt.internal.ui.JavaScriptPlugin;
import org.eclipse.wst.jsdt.internal.ui.text.javadoc.JavaDoc2HTMLTextReader;
import org.eclipse.wst.jsdt.internal.ui.text.javadoc.OAADocReader;

/**
 * Helper needed to get the content of a Javadoc comment.
 * 
 * <p>
 * This class is not intended to be subclassed or instantiated by clients.
 * </p>
 *
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves.
 */
public class JSdocContentAccess {
	
	
	public static final String EXTENSION_POINT= "documentationProvider"; //$NON-NLS-1$

	protected static final String TAG_DOCUMENTATIONPROVIDER = "documentationProvider"; //$NON-NLS-1$
	protected static final String ATTR_DOCUMENTATIONPROVIDER_CLASS = "class"; //$NON-NLS-1$

	private static IDocumentationReader[] docReaders;
	
	private JSdocContentAccess() {
		// do not instantiate
	}
	
	/**
	 * Gets a reader for an IMember's Javadoc comment content from the source attachment.
	 * The content does contain only the text from the comment without the Javadoc leading star characters.
	 * Returns <code>null</code> if the member does not contain a Javadoc comment or if no source is available.
	 * @param member The member to get the Javadoc of.
	 * @param allowInherited For methods with no (Javadoc) comment, the comment of the overridden class
	 * is returned if <code>allowInherited</code> is <code>true</code>.
	 * @return Returns a reader for the Javadoc comment content or <code>null</code> if the member
	 * does not contain a Javadoc comment or if no source is available
	 * @throws JavaScriptModelException is thrown when the elements javadoc can not be accessed
	 */
	public static Reader getContentReader(IMember member, boolean allowInherited) throws JavaScriptModelException {
		
		IDocumentationReader docReader = getDocReader(member);
		if (docReader!=null)
			return docReader.getContentReader(member, allowInherited);
		
		IOpenable openable = member.getOpenable();
 		if (openable instanceof MetadataFile)
 		{
 			return new OAADocReader((MetadataFile)openable,member);
 		}
		
		IBuffer buf= openable.getBuffer();
		if (buf == null) {
			return null; // no source attachment found
		}
		
		ISourceRange javadocRange= member.getJSdocRange();
		if (javadocRange != null) {
			JavaDocCommentReader reader= new JavaDocCommentReader(buf, javadocRange.getOffset(), javadocRange.getOffset() + javadocRange.getLength() - 1);
			if (!containsOnlyInheritDoc(reader, javadocRange.getLength())) {
				reader.reset();
				return reader;
			}
		}

		if (allowInherited && (member.getElementType() == IJavaScriptElement.METHOD)) {
			return findDocInHierarchy((IFunction) member);
		}
		
		return null;
	}

	/**
	 * Checks whether the given reader only returns
	 * the inheritDoc tag.
	 * 
	 * @param reader the reader
	 * @param length the length of the underlying content
	 * @return <code>true</code> if the reader only returns the inheritDoc tag
	 * 
	 */
	private static boolean containsOnlyInheritDoc(Reader reader, int length) {
		char[] content= new char[length];
		try {
			reader.read(content, 0, length);
		} catch (IOException e) {
			return false;
		}
		return new String(content).trim().equals("{@inheritDoc}"); //$NON-NLS-1$
		
	}

	/**
	 * Gets a reader for an IMember's Javadoc comment content from the source attachment.
	 * and renders the tags in HTML. 
	 * Returns <code>null</code> if the member does not contain a Javadoc comment or if no source is available.
	 * 
	 * @param member				the member to get the Javadoc of.
	 * @param allowInherited		for methods with no (Javadoc) comment, the comment of the overridden
	 * 									class is returned if <code>allowInherited</code> is <code>true</code>
	 * @param useAttachedJavadoc	if <code>true</code> Javadoc will be extracted from attached Javadoc
	 * 									if there's no source
	 * @return a reader for the Javadoc comment content in HTML or <code>null</code> if the member
	 * 			does not contain a Javadoc comment or if no source is available
	 * @throws JavaScriptModelException is thrown when the elements Javadoc can not be accessed
	 * 
	 */
	public static Reader getHTMLContentReader(IMember member, boolean allowInherited, boolean useAttachedJavadoc) throws JavaScriptModelException {
		Reader contentReader= getContentReader(member, allowInherited);
		if (contentReader != null)
		{
			IDocumentationReader docReader = getDocReader(member); 
			if (docReader!=null)
				return docReader.getDocumentation2HTMLReader(contentReader);
			return new JavaDoc2HTMLTextReader(contentReader);
		}
		
		if (useAttachedJavadoc && member.getOpenable().getBuffer() == null) { // only if no source available
			String s= member.getAttachedJavadoc(null);
			if (s != null)
				return new StringReader(s);
		}
		return null;
	}
	
	

 

	private static Reader findDocInHierarchy(IFunction method) throws JavaScriptModelException {
		IType type= method.getDeclaringType();
		if (type==null)
			return null;
		ITypeHierarchy hierarchy= type.newSupertypeHierarchy(null);
		
		MethodOverrideTester tester= new MethodOverrideTester(type, hierarchy);
		
		IType[] superTypes= hierarchy.getAllSupertypes(type);
		for (int i= 0; i < superTypes.length; i++) {
			IType curr= superTypes[i];
			IFunction overridden= tester.findOverriddenMethodInType(curr, method);
			if (overridden != null) {
				Reader reader= getContentReader(overridden, false);
				if (reader != null) {
					return reader;
				}
			}
		}
		return null;
	}		

	private static IDocumentationReader getDocReader(IMember member)
	{
		if (docReaders==null)
			loadExtensions();
		for (int i = 0; i < docReaders.length; i++) {
			if (docReaders[i].appliesTo(member))
				return docReaders[i];
		}
		return null;
	}
	
	
	private static void loadExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		ArrayList extList = new ArrayList();
		if (registry != null) {
			IExtensionPoint point = registry.getExtensionPoint(
					JavaScriptPlugin.getPluginId(), EXTENSION_POINT);

			if (point != null) {
				IExtension[] extensions = point.getExtensions();
				for (int i = 0; i < extensions.length; i++) {
					IConfigurationElement[] elements = extensions[i]
							.getConfigurationElements();
					for (int j = 0; j < elements.length; j++) {
						try {
							IDocumentationReader docProvider = null;
							if (elements[j].getName().equals(TAG_DOCUMENTATIONPROVIDER)) {
								  docProvider = (IDocumentationReader) elements[j]
										.createExecutableExtension(ATTR_DOCUMENTATIONPROVIDER_CLASS);
							}

							extList.add(docProvider);
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		 docReaders = (IDocumentationReader[]) extList
				.toArray(new IDocumentationReader[extList.size()]);
	}


	
}
