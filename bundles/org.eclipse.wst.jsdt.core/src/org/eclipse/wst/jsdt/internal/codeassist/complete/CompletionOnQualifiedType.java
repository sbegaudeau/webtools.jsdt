package org.eclipse.wst.jsdt.internal.codeassist.complete;

import org.eclipse.wst.jsdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemFieldBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;

public class CompletionOnQualifiedType extends
QualifiedNameReference {

	
	public char[] completionIdentifier;
public CompletionOnQualifiedType(char[][] previousIdentifiers, char[] completionIdentifier, long[] positions) {
	super(previousIdentifiers, positions, (int) (positions[0] >>> 32), (int) positions[positions.length - 1]);
	this.completionIdentifier = completionIdentifier;
}
public StringBuffer printExpression(int indent, StringBuffer output) {

	output.append("<CompleteOnName:"); //$NON-NLS-1$
	for (int i = 0; i < tokens.length; i++) {
		output.append(tokens[i]);
		output.append('.');
	}
	output.append(completionIdentifier).append('>'); 
	return output;
}

public TypeBinding resolveType(BlockScope scope) {
	// it can be a package, type, member type, local variable or field
	binding = scope.getBinding(tokens, this);
	if (!binding.isValidBinding()) {
		if (binding instanceof ProblemFieldBinding) {
			scope.problemReporter().invalidField(this, (FieldBinding) binding);
		} else if (binding instanceof ProblemReferenceBinding) {
			scope.problemReporter().invalidType(this, (TypeBinding) binding);
		} else {
			scope.problemReporter().unresolvableReference(this, binding);
		}
		
		if (binding.problemId() == ProblemReasons.NotFound) {
			throw new CompletionNodeFound(this, binding, scope);
		}
		
		throw new CompletionNodeFound();
	}
	
	throw new CompletionNodeFound(this, binding, scope);
}


public boolean isSpecialNode()
{
	return true;
}

}
