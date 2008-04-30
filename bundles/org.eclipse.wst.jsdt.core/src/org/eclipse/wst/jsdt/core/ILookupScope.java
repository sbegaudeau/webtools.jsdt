package org.eclipse.wst.jsdt.core;

import org.eclipse.wst.jsdt.internal.core.NameLookup;
import org.eclipse.wst.jsdt.internal.core.SearchableEnvironment;

public interface ILookupScope {
	/*
	 * Returns a new name lookup. This name lookup first looks in the given working copies.
	 */
	public NameLookup newNameLookup(IJavaScriptUnit[] workingCopies) throws JavaScriptModelException;

	/*
	 * Returns a new name lookup. This name lookup first looks in the working copies of the given owner.
	 */
	public NameLookup newNameLookup(WorkingCopyOwner owner) throws JavaScriptModelException ;

	/*
	 * Returns a new search name environment for this project. This name environment first looks in the given working copies.
	 */
	public SearchableEnvironment newSearchableNameEnvironment(IJavaScriptUnit[] workingCopies) throws JavaScriptModelException ;

	/*
	 * Returns a new search name environment for this project. This name environment first looks in the working copies
	 * of the given owner.
	 */
	public SearchableEnvironment newSearchableNameEnvironment(WorkingCopyOwner owner) throws JavaScriptModelException;
}
