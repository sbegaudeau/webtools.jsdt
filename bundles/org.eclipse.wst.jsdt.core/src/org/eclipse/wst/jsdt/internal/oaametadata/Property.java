package org.eclipse.wst.jsdt.internal.oaametadata;

public class Property extends DocumentedElement{

	public String name;
	public String type;
	public String usage;
	public boolean isField; 
	
	public boolean isStatic()
	{
		return IOAAMetaDataConstants.USAGE_STATIC.equals(usage);
	}

}
