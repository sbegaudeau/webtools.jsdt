package org.eclipse.wst.jsdt.internal.oaametadata;

public class Method extends DocumentedElement {
	public String scope;
	public String visibility;
	public String name;
	
	public Exception [] exceptions;
	public Parameter [] parameters;
	public ReturnsData returns;
}
