package org.eclipse.wst.jsdt.internal.core.interpret;

import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.ProgramElement;
import org.eclipse.wst.jsdt.internal.compiler.util.Util;

public class InterpretedScript {
	public CompilationUnitDeclaration compilationUnit;
	int [] lineEnds;
	int sourceSize;
	
	public InterpretedScript(CompilationUnitDeclaration compilationUnit, int[] lineEnds, int sourceSize) {
		super();
		this.compilationUnit = compilationUnit;
		this.lineEnds = lineEnds;
		this.sourceSize=sourceSize;
	}
	
	public int lineNumber(ProgramElement element)
	{
		return Util.getLineNumber(element.sourceStart, lineEnds, 0, this.sourceSize);
	}
}
