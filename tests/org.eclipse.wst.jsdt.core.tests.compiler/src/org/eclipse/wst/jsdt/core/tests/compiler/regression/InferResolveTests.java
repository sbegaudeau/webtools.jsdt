package org.eclipse.wst.jsdt.core.tests.compiler.regression;

import java.util.HashMap;

import org.eclipse.wst.jsdt.core.ast.IExpression;
import org.eclipse.wst.jsdt.core.ast.IFunctionCall;
import org.eclipse.wst.jsdt.core.ast.IFunctionExpression;
import org.eclipse.wst.jsdt.core.ast.IObjectLiteral;
import org.eclipse.wst.jsdt.core.ast.IObjectLiteralField;
import org.eclipse.wst.jsdt.core.ast.ISingleNameReference;
import org.eclipse.wst.jsdt.core.ast.IStringLiteral;
import org.eclipse.wst.jsdt.core.infer.IInferEngine;
import org.eclipse.wst.jsdt.core.infer.IInferenceFile;
import org.eclipse.wst.jsdt.core.infer.InferEngine;
import org.eclipse.wst.jsdt.core.infer.InferredMethod;
import org.eclipse.wst.jsdt.core.infer.InferredType;
import org.eclipse.wst.jsdt.core.infer.InferrenceProvider;
import org.eclipse.wst.jsdt.core.infer.RefactoringSupport;
import org.eclipse.wst.jsdt.core.infer.ResolutionConfiguration;


public class InferResolveTests  extends AbstractRegressionTest  {

	
	static class InferProvider implements InferrenceProvider 
	{

		InferEngine inferEngine;

		
		public InferProvider(InferEngine inferEngine) {
			this.inferEngine = inferEngine;
			inferEngine.inferenceProvider=this;
		}

		public int applysTo(IInferenceFile scriptFile) {
			return InferrenceProvider.MAYBE_THIS;
		}

		public String getID() {
			return "dummyID";
		}

		public IInferEngine getInferEngine() {
			return inferEngine;
		}

		public RefactoringSupport getRefactoringSupport() {
			return null;
		}

		public ResolutionConfiguration getResolutionConfiguration() {
			return null;
		}
		
	}
	
	static class TestInferEngine extends InferEngine
	{
		
		static final char[] []ADD_CLASS={"defineClass".toCharArray()};
		static final char[] []ADD_MIXIN={"addMixin".toCharArray()};
		
		protected boolean handleFunctionCall(IFunctionCall functionCall) {
			if (isFunction(functionCall, ADD_CLASS) )
			{
				IExpression[] arguments = functionCall.getArguments();
				if (arguments.length>1 && arguments[0] instanceof IStringLiteral)
				{
					char [] className=getString(arguments[0]);
					InferredType type = addType(className,true);
					if (arguments[1] instanceof IObjectLiteral) {
						IObjectLiteral objectLiteral = (IObjectLiteral) arguments[1];
						if (objectLiteral.getFields()!=null)
							for (int i = 0; i < objectLiteral.getFields().length; i++) {
								IObjectLiteralField field=objectLiteral.getFields()[i];
								char[] name = getString(field.getFieldName());
								if (field.getInitializer() instanceof IFunctionExpression)
								{
									IFunctionExpression functionExpression=(IFunctionExpression)field.getInitializer();
									
									InferredMethod method=type.addMethod(name, functionExpression.getMethodDeclaration(),false);
								}
							}
					}
				}
			}
			else if (isFunction(functionCall, ADD_MIXIN) )
			{
				IExpression[] arguments = functionCall.getArguments();
				if (arguments.length>1 )
				{
					char [] className=getString(arguments[0]);
					char [] mixinName=getString(arguments[1]);
					if (className!=null && mixinName!=null)
					{
						InferredType type = findDefinedType(className);
						if (type!=null)
							type.addMixin(mixinName);
					}
					
					
				}
			}
			return true;
		}
		
		private char[] getString(IExpression expression)
		{
		  if (expression instanceof IStringLiteral) {
			IStringLiteral strLit = (IStringLiteral) expression;
			return strLit.source();
 		   }
			else if ((expression instanceof ISingleNameReference))
			{
				ISingleNameReference snr=(ISingleNameReference)expression;
				return snr.getToken();
			}
		  return null;
		}
	}
	
	public InferResolveTests(String name) {
		super(name);
	}

	 
	protected void runNegativeTest(String[] testFiles, InferEngine inferEngine, String expectedProblemLog) {
		HashMap options = new HashMap();

		InferEngine[] inferenceEngines = new InferEngine[]  
		                            			{
		                            					(InferEngine)new InferProvider(inferEngine).getInferEngine()
		                            					
		                            			};
		options.put(INFERENCE_ENGINES, inferenceEngines);
		
 
		
		runNegativeTest(
				testFiles, 
			expectedProblemLog, 
			null /* no extra class libraries */, 
			true /* flush output directory */, 
			options /* no custom options */,
			false /* do not generate output */,
			false /* do not show category */, 
			false /* do not show warning token */, 
			false  /* do not skip javac for this peculiar test */,
			false  /* do not perform statements recovery */,
			null);
	}
	

	public void test001()	{	 

		this.runNegativeTest(
				new String[] {
						"cls.js",
						 "  function defineClass(name,args){}\n" 
						 +"  function addMixin(toClass,mixinName){}\n" 
						+"  defineClass(\"MyClass\",\n" 
						+"   {meth1 : function (){} }\n" 
						+" );\n" 
						+"  addMixin(\"MyClass\",\"myMixin\");\n" 
						+"", 
						"mix.js",
						"  defineClass(\"myMixin\",\n" 
						+"   {mixinFunc : function (){} }\n" 
						+" );\n" 
						+"", 
						"use.js",
						 "  var v=new MyClass();\n" 
						 +" v.mixinFunc();\n" 
						+"", 
				},
				new TestInferEngine(),
				""
		);

	}


}
