package org.eclipse.wst.jsdt.internal.oaametadata;

public class ClassData extends DocumentedElement{

	Ancestor [] ancestors;
	String name;
	String type;
	String superclass;
	String visibility; 

	Method [] constructors;
	Event [] events;
	Method [] methods;
	Property [] fields;
	
	Mix [] mixes;
	
}
