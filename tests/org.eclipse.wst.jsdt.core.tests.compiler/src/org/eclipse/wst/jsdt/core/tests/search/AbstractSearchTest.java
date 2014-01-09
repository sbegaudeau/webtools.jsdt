/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.tests.search;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.jsdt.core.IIncludePathEntry;
import org.eclipse.wst.jsdt.core.IJavaScriptElement;
import org.eclipse.wst.jsdt.core.IJavaScriptProject;
import org.eclipse.wst.jsdt.core.JavaScriptCore;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchConstants;
import org.eclipse.wst.jsdt.core.search.IJavaScriptSearchScope;
import org.eclipse.wst.jsdt.core.search.SearchEngine;
import org.eclipse.wst.jsdt.core.search.SearchMatch;
import org.eclipse.wst.jsdt.core.search.SearchParticipant;
import org.eclipse.wst.jsdt.core.search.SearchPattern;
import org.eclipse.wst.jsdt.core.search.SearchRequestor;
import org.eclipse.wst.jsdt.internal.core.JavaModelManager;
import org.eclipse.wst.jsdt.internal.core.JavaProject;

public class AbstractSearchTest extends TestCase {
	private String fRootProjectName;

	protected String getRootProjectName() {
		if (fRootProjectName == null) {
			fRootProjectName = new String(CharOperation.lastSegment(getClass().getName().toCharArray(), '.')) + "_";
		}
		return fRootProjectName;
	}

	protected JavaProject setupMinimalProject(String name, String fileNames[], String[] sources) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		if (!project.exists()) {
			project.create(new NullProgressMonitor());
		}
		if (!project.isOpen()) {
			project.open(null);
		}

		IProjectDescription desc = project.getDescription();
		List natures = new ArrayList(Arrays.asList(desc.getNatureIds()));
		if (!natures.contains(JavaScriptCore.NATURE_ID))
			natures.add(JavaScriptCore.NATURE_ID);
		desc.setNatureIds((String[]) natures.toArray(new String[0]));
		project.setDescription(desc, new NullProgressMonitor());

		JavaProject jsProject = (JavaProject) JavaScriptCore.create(project);
		jsProject.setRawIncludepath(new IIncludePathEntry[]{JavaScriptCore.newSourceEntry(project.getFullPath()), JavaScriptCore.newContainerEntry(new Path("org.eclipse.wst.jsdt.launching.JRE_CONTAINER"))}, null);
		jsProject.setOutputLocation(project.getFullPath(), null);

		for (int i = 0; i < sources.length; i++) {
			project.getFile(fileNames[i]).create(new ByteArrayInputStream(sources[i].getBytes()), true, null);
		}
		
		long time0 = System.currentTimeMillis();
		while (JavaModelManager.getJavaModelManager().getIndexManager().awaitingJobsCount() > 0 && System.currentTimeMillis() - time0 < 2000) {
			Thread.yield();
		}
		return jsProject;
	}

	/**
	 * @param queryString - the search query string
	 * @param elementsInScope - the IJavaScriptElements to search through
	 * @param searchFor determines the nature of the searched elements
	 *	<ul>
	 * 	<li>{@link IJavaScriptSearchConstants#CLASS}: only look for classes</li>
	 *		<li>{@link IJavaScriptSearchConstants#INTERFACE}: only look for interfaces</li>
	 * 	<li>{@link IJavaScriptSearchConstants#ENUM}: only look for enumeration</li>
	 *		<li>{@link IJavaScriptSearchConstants#ANNOTATION_TYPE}: only look for annotation type</li>
	 * 	<li>{@link IJavaScriptSearchConstants#CLASS_AND_ENUM}: only look for classes and enumerations</li>
	 *		<li>{@link IJavaScriptSearchConstants#CLASS_AND_INTERFACE}: only look for classes and interfaces</li>
	 * 	<li>{@link IJavaScriptSearchConstants#TYPE}: look for all types (ie. classes, interfaces, enum and annotation types)</li>
	 *		<li>{@link IJavaScriptSearchConstants#FIELD}: look for fields</li>
	 *		<li>{@link IJavaScriptSearchConstants#METHOD}: look for methods</li>
	 *		<li>{@link IJavaScriptSearchConstants#CONSTRUCTOR}: look for constructors</li>
	 *		<li>{@link IJavaScriptSearchConstants#PACKAGE}: look for packages</li>
	 *	</ul>
	 * @param limitTo determines the nature of the expected matches
	 *	<ul>
	 * 	<li>{@link IJavaScriptSearchConstants#DECLARATIONS}: will search declarations matching
	 * 			with the corresponding element. In case the element is a method, declarations of matching
	 * 			methods in subtypes will also be found, allowing to find declarations of abstract methods, etc.<br>
	 * 			Note that additional flags {@link IJavaScriptSearchConstants#IGNORE_DECLARING_TYPE} and
	 * 			{@link IJavaScriptSearchConstants#IGNORE_RETURN_TYPE} are ignored for string patterns.
	 * 			This is due to the fact that client may omit to define them in string pattern to have same behavior.
	 * 	</li>
	 *		 <li>{@link IJavaScriptSearchConstants#REFERENCES}: will search references to the given element.</li>
	 *		 <li>{@link IJavaScriptSearchConstants#ALL_OCCURRENCES}: will search for either declarations or
	 *				references as specified above.
	 *		</li>
	 *		 <li>{@link IJavaScriptSearchConstants#IMPLEMENTORS}: for types, will find all types
	 *				which directly implement/extend a given interface.
	 *				Note that types may be only classes or only interfaces if {@link IJavaScriptSearchConstants#CLASS } or
	 *				{@link IJavaScriptSearchConstants#INTERFACE} is respectively used instead of {@link IJavaScriptSearchConstants#TYPE}.
	 *		</li>
	 *	</ul>
	 * @param matchRule one of {@link SearchPattern#R_EXACT_MATCH}, {@link SearchPattern#R_PREFIX_MATCH}, {@link SearchPattern#R_PATTERN_MATCH},
	 * 	{@link SearchPattern#R_REGEXP_MATCH}, {@link SearchPattern#R_CAMELCASE_MATCH} combined with one of following values:
	 * 	{@link SearchPattern#R_CASE_SENSITIVE}, {@link SearchPattern#R_ERASURE_MATCH} or {@link SearchPattern#R_EQUIVALENT_MATCH}.
	 *		e.g. {@link SearchPattern#R_EXACT_MATCH} | {@link SearchPattern#R_CASE_SENSITIVE} if an exact and case sensitive match is requested,
	 *		{@link SearchPattern#R_PREFIX_MATCH} if a prefix non case sensitive match is requested or {@link SearchPattern#R_EXACT_MATCH} | {@link SearchPattern#R_ERASURE_MATCH}
	 *		if a non case sensitive and erasure match is requested.<br>
	 * 	Note that {@link SearchPattern#R_ERASURE_MATCH} or {@link SearchPattern#R_EQUIVALENT_MATCH} have no effect
	 * 	on non-generic types/methods search.<br>
	 * 	Note also that default behavior for generic types/methods search is to find exact matches.
	 * @return the {@link SearchMatch} instances found
	 * @throws Exception
	 */
	public SearchMatch[] runSearchTest(String queryString, IJavaScriptElement[] elementsInScope, int searchFor, int limitTo, int matchRule) throws Exception {
		IJavaScriptSearchScope scope = SearchEngine.createJavaSearchScope(elementsInScope);

		final List results = new ArrayList();
		SearchEngine searchEngine = new SearchEngine();
		final SearchPattern searchPattern = SearchPattern.createPattern(queryString, searchFor, limitTo, matchRule);
		assertNotNull("search pattern was not created", searchPattern);
		SearchRequestor requestor = new SearchRequestor() {
			public void acceptSearchMatch(SearchMatch match) throws CoreException {
				results.add(match);
			}
		};
		searchEngine.search(searchPattern, new SearchParticipant[]{SearchEngine.getDefaultSearchParticipant()}, scope, requestor, new NullProgressMonitor());
		return (SearchMatch[])results.toArray(new SearchMatch[results.size()]);
	}
	
	/**
	 * @param projectQualifier - a unique qualifier to append to the name of the project being created
	 * @param queryString - the search query string
	 * @param fileNames - the names of the source files to include within the search scope
	 * @param fileSources - the respective contents of the source files included within the search scope
	 * @param searchFor determines the nature of the searched elements
	 *	<ul>
	 * 	<li>{@link IJavaScriptSearchConstants#CLASS}: only look for classes</li>
	 *		<li>{@link IJavaScriptSearchConstants#INTERFACE}: only look for interfaces</li>
	 * 	<li>{@link IJavaScriptSearchConstants#ENUM}: only look for enumeration</li>
	 *		<li>{@link IJavaScriptSearchConstants#ANNOTATION_TYPE}: only look for annotation type</li>
	 * 	<li>{@link IJavaScriptSearchConstants#CLASS_AND_ENUM}: only look for classes and enumerations</li>
	 *		<li>{@link IJavaScriptSearchConstants#CLASS_AND_INTERFACE}: only look for classes and interfaces</li>
	 * 	<li>{@link IJavaScriptSearchConstants#TYPE}: look for all types (ie. classes, interfaces, enum and annotation types)</li>
	 *		<li>{@link IJavaScriptSearchConstants#FIELD}: look for fields</li>
	 *		<li>{@link IJavaScriptSearchConstants#METHOD}: look for methods</li>
	 *		<li>{@link IJavaScriptSearchConstants#CONSTRUCTOR}: look for constructors</li>
	 *		<li>{@link IJavaScriptSearchConstants#PACKAGE}: look for packages</li>
	 *	</ul>
	 * @param limitTo determines the nature of the expected matches
	 *	<ul>
	 * 	<li>{@link IJavaScriptSearchConstants#DECLARATIONS}: will search declarations matching
	 * 			with the corresponding element. In case the element is a method, declarations of matching
	 * 			methods in subtypes will also be found, allowing to find declarations of abstract methods, etc.<br>
	 * 			Note that additional flags {@link IJavaScriptSearchConstants#IGNORE_DECLARING_TYPE} and
	 * 			{@link IJavaScriptSearchConstants#IGNORE_RETURN_TYPE} are ignored for string patterns.
	 * 			This is due to the fact that client may omit to define them in string pattern to have same behavior.
	 * 	</li>
	 *		 <li>{@link IJavaScriptSearchConstants#REFERENCES}: will search references to the given element.</li>
	 *		 <li>{@link IJavaScriptSearchConstants#ALL_OCCURRENCES}: will search for either declarations or
	 *				references as specified above.
	 *		</li>
	 *		 <li>{@link IJavaScriptSearchConstants#IMPLEMENTORS}: for types, will find all types
	 *				which directly implement/extend a given interface.
	 *				Note that types may be only classes or only interfaces if {@link IJavaScriptSearchConstants#CLASS } or
	 *				{@link IJavaScriptSearchConstants#INTERFACE} is respectively used instead of {@link IJavaScriptSearchConstants#TYPE}.
	 *		</li>
	 *	</ul>
	 * @param matchRule one of {@link #R_EXACT_MATCH}, {@link #R_PREFIX_MATCH}, {@link #R_PATTERN_MATCH},
	 * 	{@link #R_REGEXP_MATCH}, {@link #R_CAMELCASE_MATCH} combined with one of following values:
	 * 	{@link #R_CASE_SENSITIVE}, {@link #R_ERASURE_MATCH} or {@link #R_EQUIVALENT_MATCH}.
	 *		e.g. {@link #R_EXACT_MATCH} | {@link #R_CASE_SENSITIVE} if an exact and case sensitive match is requested,
	 *		{@link #R_PREFIX_MATCH} if a prefix non case sensitive match is requested or {@link #R_EXACT_MATCH} | {@link #R_ERASURE_MATCH}
	 *		if a non case sensitive and erasure match is requested.<br>
	 * 	Note that {@link #R_ERASURE_MATCH} or {@link #R_EQUIVALENT_MATCH} have no effect
	 * 	on non-generic types/methods search.<br>
	 * 	Note also that default behavior for generic types/methods search is to find exact matches.
	 * @return the {@link SearchMatch} instances found
	 * @throws Exception
	 */
	protected SearchMatch[] runSearchTest(String projectQualifier, String queryString, String[] fileNames, String[] fileSources, int searchFor, int limitTo, int matchRule) throws Exception {
		IJavaScriptProject project = setupMinimalProject(getRootProjectName()+projectQualifier, fileNames, fileSources);
		return runSearchTest(queryString, new IJavaScriptElement[]{project}, searchFor, limitTo, matchRule);
	}
	
	public void verifyMatches(String expected, SearchMatch[] matches) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < matches.length; i++) {
			char[] value = CharOperation.replace(matches[i].toString().toCharArray(), new char[]{'\r','\n'}, new char[]{'\n'});
			value = CharOperation.replace(value, new char[]{'\r'}, new char[]{'\n'});
			b.append(value);
		}
		assertEquals("Unexpected search results", expected, b.toString());
	}
}
