package org.eclipse.wst.jsdt.core.tests.interpret;

import org.eclipse.wst.jsdt.core.tests.junit.extension.StopableTestCase;
import org.eclipse.wst.jsdt.core.tests.util.AbstractCompilerTest;
import org.eclipse.wst.jsdt.internal.core.interpret.Interpreter;
import org.eclipse.wst.jsdt.internal.core.interpret.InterpreterContext;
import org.eclipse.wst.jsdt.internal.core.interpret.InterpreterResult;
import org.eclipse.wst.jsdt.internal.core.interpret.Value;

public class InterpretTest extends AbstractCompilerTest implements
		StopableTestCase {

	
	InterpreterResult result;
	
	public InterpretTest(String name) {
		super(name);
	}

	public void stop() {

	}

	public void interpetTest(String source, String expected)
	{
		
		InterpreterContext context= new InterpreterContext();
		context.initailizeBuiltins();
		
		result=Interpreter.interpet(source, context);
		Object res=result.getResult();
		if (res instanceof Value)
			res=((Value)res).stringValue();
		assertTrue("Result has errors", !result.hasErrors());
		assertEquals("Unexpected result",expected, res.toString());
		

	}
	
	public void interpetTest(String source, String varName, String expected)
	{
		
		InterpreterContext context= new InterpreterContext();
		
		result=Interpreter.interpet(source, context);
		
		assertTrue("Result has errors", !result.hasErrors());
		Value value =context.getValue(varName.toCharArray());
		assertTrue("var not found: "+varName, value!=null);
		assertEquals("Unexpected result",expected, value.stringValue() );
		

	}

}
