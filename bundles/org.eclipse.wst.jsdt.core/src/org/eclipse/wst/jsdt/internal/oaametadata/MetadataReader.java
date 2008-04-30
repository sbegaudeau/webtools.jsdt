/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.internal.oaametadata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.wst.jsdt.internal.core.util.Util;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class MetadataReader extends DefaultHandler implements IOAAMetaDataConstants
{

	LibraryAPIs apis=new LibraryAPIs();
	
	Stack stack=new Stack();

	HashMap states=new HashMap();

	boolean collectText=false;
	boolean pendingEndElement=false;

	String collectTextElement;

	int currentState=STATE_API;
	Object currentObject;

	StringBuffer text=new StringBuffer();
	HashMap collections;

	
	
	static class StackElement {
		HashMap collections;
		Object currentObject;
		int state;
		
		StackElement(int st, Object obj, HashMap map)
		{
			this.collections=map;
			this.currentObject=obj;
			this.state=st;
		}
	}
	
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
	
	static final ArrayList EMPTY_LIST=new ArrayList();

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
	
	
	public static LibraryAPIs readAPIsFromStream(InputSource inputSource)   {
		
		final MetadataReader handler= new MetadataReader();
		try {
		    final SAXParserFactory factory= SAXParserFactory.newInstance();
			final SAXParser parser= factory.newSAXParser();
//			parser.setProperty("http://xml.org/sax/features/namespaces", new Boolean(true));
			XMLReader reader=parser.getXMLReader();
			reader.setFeature("http://xml.org/sax/features/namespaces", true);
			parser.parse(inputSource, handler);
		} catch (SAXException e) {
			Util.log(e, "error reading oaametadata");
		} catch (IOException e) {
			Util.log(e, "error reading oaametadata");
		} catch (ParserConfigurationException e) {
			Util.log(e, "error reading oaametadata");
		}
		return handler.apis;
	}
	
	public static LibraryAPIs readAPIsFromString(String metadata) {
		return readAPIsFromStream(new InputSource(new StringReader(metadata)));
	}
	
	public static LibraryAPIs readAPIsFromFile(String fileName) {
		try {
			FileInputStream file = new FileInputStream(fileName);
			LibraryAPIs apis= readAPIsFromStream(new InputSource(file));
			apis.fileName=fileName;
			return apis;
		} catch (FileNotFoundException e) {
			Util.log(e,  "error reading oaametadata");
		}
		return null;
	}
	
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (collectText)
		{
			if (pendingEndElement)
			{
				text.append("/>");
				pendingEndElement=false;
			}
			text.append(ch,start,length);
		}
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (collectText)
		{
			if (NAMESPACE_API.equals(uri)&& localName.equals(collectTextElement))
			{
				switch (this.currentState)
				{
				case STATE_DESCRIPTION:
				{
					if (this.currentObject instanceof DocumentedElement)
					{
						((DocumentedElement)this.currentObject).description=this.text.toString();
					}
					break;
				}
				}
				popState();
				this.collectText=false;
				this.collectTextElement=null;
				this.text=new StringBuffer();
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
		else
		{
			
			
			switch (this.currentState)
			{
			case STATE_API:
			{
				ArrayList collection = getCollection(TAG_CLASS);
				this.apis.classes= (ClassData[])collection.toArray(new ClassData[collection.size()]);
				 collection = getCollection(TAG_METHOD);
				this.apis.globalMethods= (Method[])collection.toArray(new Method[collection.size()]);
				 collection = getCollection(TAG_PROPERTY);
				this.apis.globalVars= (Property[])collection.toArray(new Property[collection.size()]);
				 collection = getCollection(TAG_AUTHOR);
				this.apis.authors= (String[])collection.toArray(new String[collection.size()]);
				break;
			}
			case STATE_CLASS:
			{
				ClassData clazz=(ClassData)this.currentObject;
				ArrayList collection = getCollection(TAG_ANCESTOR);
				clazz.ancestors= (Ancestor[])collection.toArray(new Ancestor[collection.size()]);
				 collection = getCollection(TAG_CONSTRUCTOR);
				 clazz.constructors= (Method[])collection.toArray(new Method[collection.size()]);
				 collection = getCollection(TAG_EVENT);
				 clazz.events= (Event[])collection.toArray(new Event[collection.size()]);
				 collection = getCollection(TAG_PROPERTY);
				clazz.fields= (Property[])collection.toArray(new Property[collection.size()]);
				collection = getCollection(TAG_METHOD);
				clazz.methods= (Method[])collection.toArray(new Method[collection.size()]);
				 collection = getCollection(TAG_AUTHOR);
				clazz.mixes= (Mix[])collection.toArray(new Mix[collection.size()]);
				break;
			}
			
			case STATE_METHOD:
			case STATE_CONSTRUCTOR:
			{
				Method method=(Method)this.currentObject;
				ArrayList collection = getCollection(TAG_EXCEPTION);
				method.exceptions= (Exception[])collection.toArray(new Exception[collection.size()]);
				 collection = getCollection(TAG_PARAMETER);
				 method.parameters= (Parameter[])collection.toArray(new Parameter[collection.size()]);

				break;
			}

			}
			popState();
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
			Integer stateObj=null;
			if (NAMESPACE_API.equals(uri))
			{
				pushState();
				stateObj=(Integer)states.get(localName);
				if (stateObj!=null)
				{
					int state=stateObj.intValue();
					switch (state)
					{
					case STATE_API:
					{
						this.apis.version = attributes.getValue(ATTRIBUTE_API_VERSION);
						this.apis.language = attributes.getValue(ATTRIBUTE_API_LANGUAGE);
						this.apis.getterPattern = attributes.getValue(ATTRIBUTE_API_GETTERPATTERN);
						this.apis.setterPattern = attributes.getValue(ATTRIBUTE_API_SETTERPATTERN);
						this.collections=new HashMap();
						break;
					}
					case STATE_CLASS:
					{
						ClassData clazz=new ClassData();
						this.currentObject=clazz;
						addCollectionElement(TAG_CLASS, clazz);
						clazz.name = attributes.getValue(ATTRIBUTE_CLASS_NAME);
						clazz.type = attributes.getValue(ATTRIBUTE_CLASS_TYPE);
						clazz.superclass = attributes.getValue(ATTRIBUTE_CLASS_SUPERCLASS);
						clazz.visibility = attributes.getValue(ATTRIBUTE_CLASS_VISIBILITY);
						
						this.collections=new HashMap();
						break;
					}

					case STATE_METHOD:
					{
						Method method=new Method();
						this.currentObject=method;
						addCollectionElement(TAG_METHOD, method);
						method.name = attributes.getValue(ATTRIBUTE_METHOD_NAME);
						method.scope = attributes.getValue(ATTRIBUTE_METHOD_SCOPE);
						
						this.collections=new HashMap();
						break;
					}

					case STATE_CONSTRUCTOR:
					{
						Method method=new Method();
						this.currentObject=method;
						addCollectionElement(TAG_CONSTRUCTOR, method);
						method.scope = attributes.getValue(ATTRIBUTE_CONSTRUCTOR_SCOPE);
						method.visibility = attributes.getValue(ATTRIBUTE_CONSTRUCTOR_VISIBILITY);
						
						this.collections=new HashMap();
						break;
					}

					case STATE_PARAMETER:
					{
						Parameter parameter=new Parameter();
						this.currentObject=parameter;
						addCollectionElement(TAG_PARAMETER, parameter);
						parameter.name = attributes.getValue(ATTRIBUTE_PARAMETER_NAME);
						parameter.type = attributes.getValue(ATTRIBUTE_PARAMETER_TYPE);
						parameter.usage = attributes.getValue(ATTRIBUTE_PARAMETER_USAGE);
						
						this.collections=new HashMap();
						break;
					}
					
					case STATE_FIELD:
					case STATE_PROPERTY:
					{
						Property property=new Property();
						this.currentObject=property;
						property.isField=(STATE_FIELD==state);
						addCollectionElement(TAG_PROPERTY, property);
						property.name = attributes.getValue(ATTRIBUTE_FIELD_NAME);
						property.type = attributes.getValue(ATTRIBUTE_FIELD_TYPE);
						property.usage = attributes.getValue(ATTRIBUTE_FIELD_USAGE);
						
						this.collections=new HashMap();
						break;
					}
					
					
					case STATE_RETURNS:
					{
						ReturnsData returnData =new ReturnsData();
						if (this.currentObject instanceof Method)
							((Method)this.currentObject).returns=returnData;
						this.currentObject=returnData;
						returnData.type = attributes.getValue(ATTRIBUTE_RETURNS_TYPE);
						break;
					}


					case STATE_DESCRIPTION:
					{
						startCollectingText(localName);
						break;
					}


					}
					this.currentState=state;
				}
			}
		}
	}

	private void startCollectingText(String localName) {
		this.collectText=true;
		this.collectTextElement=localName;
		this.text=new StringBuffer();
	}

	private void addCollectionElement(String tagClass,Object element) {
		if (this.collections==null)
			this.collections=new HashMap();
		ArrayList list = (ArrayList)this.collections.get(tagClass);
		if (list==null)
		{
			this.collections.put(tagClass, list=new ArrayList());
		}
		list.add(element);
	}

	
	private ArrayList getCollection(String tagClass) {
		ArrayList list = (ArrayList)this.collections.get(tagClass);
		if (list==null)
			list=EMPTY_LIST;
		return list;
	}

	private void popState() {
		StackElement stackElement=(StackElement)stack.pop();
		this.currentState=stackElement.state;
		this.collections=stackElement.collections;
		this.currentObject=stackElement.currentObject;
	}
	

	private void pushState() {
		StackElement newElement=new StackElement(this.currentState,this.currentObject, this.collections);
		stack.push(newElement);
	}


	
}
