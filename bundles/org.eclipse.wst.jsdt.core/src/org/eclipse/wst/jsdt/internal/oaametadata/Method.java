package org.eclipse.wst.jsdt.internal.oaametadata;

public class Method extends DocumentedElement {
	String scope;
	String visibility;
	String name;
	
	Exception [] exceptions;
	Parameter [] parameters;
	ReturnsData returns;
}
