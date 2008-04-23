package org.eclipse.wst.jsdt.internal.oaametadata;

import java.util.HashMap;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MetadataReader extends DefaultHandler implements IOAAMetaDataConstants
{

	LibraryAPIs apis;
	
	Stack stack=new Stack();

	HashMap states=new HashMap();

	boolean collectText=false;
	boolean pendingEndElement=false;

	String collectTextElement;
	
	StringBuffer text=new StringBuffer();
	
	static final int STATE_API=1;
	static final int STATE_CLASS=3;
	static final int STATE_GLOBALS=3;
	static final int STATE_METHOD=4;
	static final int STATE_PROPERTY=5;
	static final int STATE_PROPERTIES=6;
	static final int STATE_ALAIS=7;
	static final int STATE_ALAISES=8;
	static final int STATE_ANCESTOR=9;
	static final int STATE_ANCESTORS=10;
	static final int STATE_AUTHOR=11;
	static final int STATE_AUTHORS=12;
	static final int STATE_CONSTRUCTOR=13;
	static final int STATE_CONSTRUCTORS=14;

	static final int STATE_DEPRECIATED =	15;
	static final int STATE_DESCRIPTION =16;
    static final int STATE_EVENT =17;
    static final int STATE_EVENTS =18;
    static final int STATE_EXAMPLE =19;
    static final int STATE_EXAMPLES =20;
    static final int STATE_EXCEPTION =21;
    static final int STATE_EXCEPTIONS =22;
    static final int STATE_FIELD =23;
    static final int STATE_FIELDS =24;
    static final int STATE_INTERFACE =26;
    static final int STATE_INTERFACES =27;
    static final int STATE_METHODS =29;
    static final int STATE_MIX =30;
    static final int STATE_MIXES =31;
    static final int STATE_MIXIN =32;
	static final int STATE_MIXINS =33;
	static final int STATE_NAMESPACE =34;
    static final int STATE_OPTION =35;
	static final int STATE_OPTIONS =36;
    static final int STATE_PARAMETER =37;
    static final int STATE_PARAMETERS =38;
    static final int STATE_REMARKS =41;
    static final int STATE_RETURNS =42;
	static final int STATE_SEE =43;
	static final int STATE_TOPIC =44;
	static final int STATE_TOPICS =45;
	static final int STATE_USERAGENT =46;
	static final int STATE_USERAGENTS =47;

	{
		states.put(TAG_API, new Integer(STATE_API));
		states.put(TAG_CLASS, new Integer(STATE_CLASS));
		states.put(TAG_GLOBALS, new Integer(STATE_GLOBALS));
		states.put(TAG_METHOD, new Integer(STATE_METHOD));
		states.put(TAG_PROPERTY, new Integer(STATE_PROPERTY));
		states.put(TAG_PROPERTIES, new Integer(STATE_PROPERTIES));
		states.put(TAG_ALAIS, new Integer(STATE_ALAIS));
		states.put(TAG_ALIASES, new Integer(STATE_ALAISES));
		states.put(TAG_ANCESTORS, new Integer(STATE_ANCESTORS));
		states.put(TAG_ANCESTOR, new Integer(STATE_ANCESTOR));
		states.put(TAG_AUTHORS, new Integer(STATE_AUTHORS));
		states.put(TAG_AUTHOR, new Integer(STATE_AUTHOR));
		states.put(TAG_CONSTRUCTORS, new Integer(STATE_CONSTRUCTORS));
		states.put(TAG_CONSTRUCTOR, new Integer(STATE_CONSTRUCTOR));


		states.put(TAG_DEPRECIATED, new Integer(STATE_DEPRECIATED));
		states.put(TAG_DESCRIPTION, new Integer(STATE_DESCRIPTION ));
	    states.put(TAG_EVENT, new Integer(STATE_EVENT ));
	    states.put(TAG_EVENTS, new Integer(STATE_EVENTS));
	    states.put(TAG_EXAMPLE, new Integer(STATE_EXAMPLE ));
	    states.put(TAG_EXAMPLES, new Integer(STATE_EXAMPLES ));
	    states.put(TAG_EXCEPTION, new Integer(STATE_EXCEPTION ));
	    states.put(TAG_EXCEPTIONS, new Integer(STATE_EXCEPTIONS));
	    states.put(TAG_FIELD, new Integer(STATE_FIELD ));
	    states.put(TAG_FIELDS, new Integer(STATE_FIELDS ));
	    states.put(TAG_INTERFACE, new Integer(STATE_INTERFACE ));
	    states.put(TAG_INTERFACES, new Integer(STATE_INTERFACES ));
	    states.put(TAG_METHODS, new Integer(STATE_METHODS ));
	    states.put(TAG_MIX, new Integer(STATE_MIX ));
	    states.put(TAG_MIXES, new Integer(STATE_MIXES ));
	    states.put(TAG_MIXIN, new Integer(STATE_MIXIN));
		states.put(TAG_MIXINS, new Integer(STATE_MIXINS ));
		states.put(TAG_NAMESPACE, new Integer(STATE_NAMESPACE ));
	    states.put(TAG_OPTION, new Integer(STATE_OPTION ));
		states.put(TAG_OPTIONS, new Integer(STATE_OPTIONS ));
	    states.put(TAG_PARAMETER, new Integer(STATE_PARAMETER ));
	    states.put(TAG_PARAMETERS, new Integer(STATE_PARAMETERS ));
	    states.put(TAG_REMARKS, new Integer(STATE_REMARKS ));
	    states.put(TAG_RETURNS, new Integer(STATE_RETURNS ));
		states.put(TAG_SEE, new Integer(STATE_SEE ));
		states.put(TAG_TOPIC, new Integer(STATE_TOPIC));
		states.put(TAG_TOPICS, new Integer(STATE_TOPICS ));
		states.put(TAG_USERAGENT, new Integer(STATE_USERAGENT ));
		states.put(TAG_USERAGENTS, new Integer(STATE_USERAGENTS ));

	
	}
	
	
	
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (collectText)
		{
			if (pendingEndElement)
			{
				text.append("/>");
				text.append(ch);
				pendingEndElement=false;
			}
		}
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (collectText)
		{
			if (NAMESPACE_API.equals(uri)&& localName.equals(collectTextElement))
			{
				
			}
			else
			{
			  if (pendingEndElement)
				  text.append("/>");
			  else
				  text.append("</").append(localName).append(">");
			}
			pendingEndElement=false;
				
		}
	}

	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (collectText)
		{
			text.append("<").append(localName);
			int nAttributes=attributes.getLength();
			for (int i=0; i<nAttributes; i++)
			{
				String qname=attributes.getQName(i);
				String value=attributes.getValue(i);
				text.append(" ").append(qname).append("=\"").append(value).append("\"");
			}
			pendingEndElement=true;
		}
		else
		{
			Integer state=null;
			if (NAMESPACE_API.equals(uri))
			{
				state=(Integer)states.get(localName);
				if (state!=null)
				{
					switch (state.intValue())
					{
					
					}
					
				}
			}
			this.stack.push(state);
		}
	}

	
	
}
