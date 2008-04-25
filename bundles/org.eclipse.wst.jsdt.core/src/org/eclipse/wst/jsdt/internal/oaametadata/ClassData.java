package org.eclipse.wst.jsdt.internal.oaametadata;

import java.util.ArrayList;

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
	
	
	public Property [] getFields()
	{
		ArrayList list = new ArrayList();
		if (this.fields!=null)
			for (int i = 0; i < this.fields.length; i++) {
				if (this.fields[i].isField)
					list.add(this.fields[i]);
		}
		return (Property [] )list.toArray(new Property[list.size()]);
	}
	
	public Property [] getProperties()
	{
		ArrayList list = new ArrayList();
		if (this.fields!=null)
			for (int i = 0; i < this.fields.length; i++) {
				if (!this.fields[i].isField)
					list.add(this.fields[i]);
		}
		return (Property [] )list.toArray(new Property[list.size()]);
	}
	
	
}
