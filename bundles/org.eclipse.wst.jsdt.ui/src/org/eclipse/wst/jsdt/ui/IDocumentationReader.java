package org.eclipse.wst.jsdt.ui;

import java.io.Reader;

import org.eclipse.wst.jsdt.core.IMember;
import org.eclipse.wst.jsdt.core.JavaScriptModelException;

public interface IDocumentationReader {

	public boolean appliesTo(IMember member);
	
	public  Reader getDocumentation2HTMLReader(Reader contentReader);
	public  Reader getContentReader(IMember member, boolean allowInherited) throws JavaScriptModelException;

}
