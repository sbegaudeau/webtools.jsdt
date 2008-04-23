package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import java.util.HashMap;

import org.eclipse.wst.jsdt.core.tests.util.Util;
import org.eclipse.wst.jsdt.internal.oaametadata.IOAAMetaDataConstants;
import org.eclipse.wst.jsdt.internal.oaametadata.LibraryAPIs;
import org.eclipse.wst.jsdt.internal.oaametadata.MetadataReader;


public class OaaMetadataTests extends AbstractRegressionTest {

	
	static final String LIB1=
	"<api xmlns=\"http://ns.openajax.org/api\" version=\"...\" apiType=\"JavaScript\">"
	+"   <class type=\"libraryname.ClassName\" superclass=\"Object\">"
	+"      <constructors>"
	+"          <constructor scope=\"instance\">"
	+"              <description>Constructor description</description>"
	+"              <parameters>"
	+"                  <parameter name=\"message\" required=\"true\" type=\"String\">"
	+"                      <description>Parameter description</description>"
	+"                  </parameter>"
	+"              </parameters>"
	+"              <returns type=\"Object\">"
	+"                <description>...</description>" 
	+"              </returns>"
	+"          </constructor>"
	+"       </constructors>"
	+"      <properties>" 
	+"          <property name=\"propertyInstance\" readonly=\"false\" scope=\"instance\" type=\"String\">"
	+"              <description>Property description</description>"
	+"          </property>"
	+"          <property name=\"propertyStatic\" readonly=\"false\" scope=\"static\" type=\"String\">"
	+"              <description>Property description</description>"
	+"          </property>"
	+"      </properties>"
	+"      <methods>"
	+"          <method name=\"functionInstance\" scope=\"instance\">"
	+"              <description>Method description</description>"
	+"              <parameters>"
	+"                  <parameter name=\"param\" required=\"true\" type=\"String\">"
	+"                      <description>Parameter description</description>"
	+"                  </parameter>"
	+"              </parameters>"
	+"              <returns type=\"String\">"
	+"                <description>...</description>"
	+"              </returns>"
	+"          </method>"
	+"          <method name=\"functionStatic\" scope=\"static\">"
	+"              <description>Method description</description>"
	+"              <parameters/>"
	+"              <returns type=\"String\">" 
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
		runNegativeTest(
				testFiles, 
			expectedProblemLog, 
			classLib  , 
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
		
		LibraryAPIs apis=MetadataReader.readAPIsFromString(LIB1);
		assertTrue(apis.classes!=null && apis.classes.length==1);
	}
	
	
	public void test003()	{	// local var 
		String libDir=Util.copyToOutput("libDir/"+IOAAMetaDataConstants.METADATA_FILE, LIB1);
		this.runNegativeTest(
				new String[] {
						"X.js",
						"var i=new libraryname.ClassName();" +
						"\n"
				},
				new String[]{libDir},
				""
		);
	}
}