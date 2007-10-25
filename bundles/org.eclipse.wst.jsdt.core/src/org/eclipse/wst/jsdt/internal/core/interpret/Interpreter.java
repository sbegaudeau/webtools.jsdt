package org.eclipse.wst.jsdt.internal.core.interpret;

import java.util.Locale;
import java.util.Map;

import org.eclipse.wst.jsdt.internal.compiler.CompilationResult;
import org.eclipse.wst.jsdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.compiler.parser.Parser;
import org.eclipse.wst.jsdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemReporter;

public class Interpreter {

	
	public static InterpreterResult interpet(String code, InterpreterContext context)
	{
		char[] source = code.toCharArray();
			Map options =new CompilerOptions().getMap(); 
		options.put(CompilerOptions.OPTION_ReportUnusedLocal, CompilerOptions.IGNORE);
			options.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_3);
			options.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_3);
			options.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_1);
			CompilerOptions compilerOptions =new CompilerOptions(options);
		Parser parser = 
			new Parser(
					new ProblemReporter(
							DefaultErrorHandlingPolicies.exitAfterAllProblems(),
							compilerOptions, 
							new DefaultProblemFactory(Locale.getDefault())),
				true); 

		ICompilationUnit sourceUnit = new CompilationUnit(source, "interpreted", null);

		CompilationResult compilationUnitResult = new CompilationResult(sourceUnit, 0, 0,  compilerOptions.maxProblemsPerUnit);
		CompilationUnitDeclaration parsedUnit = parser.parse(sourceUnit, compilationUnitResult);

		InterpreterResult result = interpret(parsedUnit, context);
		
		parsedUnit.cleanUp();
		
		return result;

	}
	
	public static InterpreterResult interpret(CompilationUnitDeclaration ast, InterpreterContext context)
	{
	
		InterpreterEngine engine = new InterpreterEngine(context);
		
		return engine.interpret(ast);
		
	}
	
	
}
