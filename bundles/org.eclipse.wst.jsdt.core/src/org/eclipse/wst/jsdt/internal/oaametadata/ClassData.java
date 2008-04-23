package org.eclipse.wst.jsdt.internal.oaametadata;

public class ClassData extends DocumentedElement{

	public Ancestor [] ancestors;
	public String name;
	public String type;
	public String superclass;
	public String visibility; 

	public Method [] constructors;
	public Event [] events;
	public Method [] methods;
	public Property [] fields;
	
	public Mix [] mixes;
	
}
