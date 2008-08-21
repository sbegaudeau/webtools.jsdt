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
package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import java.util.HashMap;

import org.eclipse.wst.jsdt.core.tests.util.Util;
import org.eclipse.wst.jsdt.internal.oaametadata.ClassData;
import org.eclipse.wst.jsdt.internal.oaametadata.IOAAMetaDataConstants;
import org.eclipse.wst.jsdt.internal.oaametadata.LibraryAPIs;
import org.eclipse.wst.jsdt.internal.oaametadata.MetadataReader;


public class OaaMetadataTests extends AbstractRegressionTest {

	
	static final String LIB1=
	"<api xmlns=\"http://ns.openajax.org/api\" version=\"...\" apiType=\"JavaScript\">"
	+"   <class name=\"libraryname.ClassName\" superclass=\"Object\">"
	+"      <constructors>"
	+"          <constructor scope=\"instance\">"
	+"              <description>Constructor description</description>"
	+"              <parameters>"
	+"                  <parameter name=\"message\" required=\"true\" datatype=\"String\">"
	+"                      <description>Parameter description</description>"
	+"                  </parameter>"
	+"              </parameters>"
	+"              <returns datatype=\"Object\">"
	+"                <description>...</description>" 
	+"              </returns>"
	+"          </constructor>"
	+"       </constructors>"
	+"      <fields>" 
	+"          <field name=\"propertyInstance\" readonly=\"false\" scope=\"instance\" datatype=\"String\">"
	+"              <description>Property description</description>"
	+"          </field>"
	+"          <field name=\"propertyStatic\" readonly=\"false\" scope=\"static\" datatype=\"String\">"
	+"              <description>Property description</description>"
	+"          </field>"
	+"      </fields>"
	+"      <methods>"
	+"          <method name=\"functionInstance\" scope=\"instance\">"
	+"              <description>Method description</description>"
	+"              <parameters>"
	+"                  <parameter name=\"param\" required=\"true\" datatype=\"String\">"
	+"                      <description>Parameter description</description>"
	+"                  </parameter>"
	+"              </parameters>"
	+"              <returns datatype=\"String\">"
	+"                <description>...</description>"
	+"              </returns>"
	+"          </method>"
	+"          <method name=\"functionStatic\" scope=\"static\">"
	+"              <description>Method description</description>"
	+"              <parameters/>"
	+"              <returns datatype=\"String\">" 
	+"                <description>...</description>" 
	+"              </returns>"
	+"          </method>"
	+"      </methods>"
	+"  </class>"
	+"</api>";
	
	public OaaMetadataTests(String name) {
		super(name);

	}
	
	protected void runNegativeTest(String[] testFiles,String[]classLib, String expectedProblemLog) {
		HashMap options = new HashMap();
		String[] defaultClassPaths = getDefaultClassPaths();
		String [] classLibs=new String[classLib.length+defaultClassPaths.length];
		System.arraycopy(classLib, 0, classLibs, 0, classLib.length);
		System.arraycopy(defaultClassPaths, 0, classLibs, classLib.length, defaultClassPaths.length);
		runNegativeTest(
				testFiles, 
			expectedProblemLog, 
			classLibs  , 
			false /* flush output directory */, 
			options /* no custom options */,
			false /* do not generate output */,
			false /* do not show category */, 
			false /* do not show warning token */, 
			false  /* do not skip javac for this peculiar test */,
			false  /* do not perform statements recovery */,
			null);
	}
	

	
	public void test001()
	{
		
		LibraryAPIs apis=MetadataReader.readAPIsFromString(LIB1,"");
		assertTrue(apis.classes!=null && apis.classes.length==1);
	}
	
	
	public void test003()	{	// local var 
		String libDir=Util.copyToOutput("libDir/"+IOAAMetaDataConstants.METADATA_FILE, LIB1);
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var i=new libraryname.ClassName(1);\n" +
						"var d=i.propertyInstance;\n" +
						"var e=i.functionInstance(1);\n" +
						"\n"
				},
				new String[]{libDir},
				""
		);
	}
	
	
	public void testInclude1()	{	 
		String base="<api xmlns=\"http://ns.openajax.org/api\" version=\"...\" apiType=\"JavaScript\">"
		+"   <class name=\"cls1\" superclass=\"Object\">"
		+"      <include src=\"incl2.xml\"/>"
		+"  </class>"
		+"  <include src=\"..\\incl1.xml\"/>"
		+"</api>";
		String incl1="<fragment xmlns=\"http://ns.openajax.org/api\">"
			+"   <class name=\"cls2\" superclass=\"Object\">"
			+"  </class>"
			+"</fragment>";
		String incl2="<fragment xmlns=\"http://ns.openajax.org/api\">"
			+"          <method name=\"functionInstance\" scope=\"instance\"/>"
			+"</fragment>";

		String filePath=Util.copyToOutput("libDir/"+IOAAMetaDataConstants.METADATA_FILE, base);
		Util.copyToOutput("incl1.xml", incl1);
		Util.copyToOutput("libDir/incl2.xml", incl2);

		LibraryAPIs apis=MetadataReader.readAPIsFromFile(filePath);
		assertTrue(apis.classes!=null && apis.classes.length==2);
		
		ClassData classData = apis.classes[0];
		assertTrue(classData.methods!=null && classData.methods.length==1);
		
	}

}