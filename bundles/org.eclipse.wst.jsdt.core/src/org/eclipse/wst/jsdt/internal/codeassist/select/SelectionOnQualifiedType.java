package org.eclipse.wst.jsdt.internal.codeassist.select;

public class SelectionOnQualifiedType extends SelectionOnQualifiedNameReference {

	public SelectionOnQualifiedType(char[][] previousIdentifiers, char[] selectionIdentifier, long[] positions) {
		super(previousIdentifiers, selectionIdentifier, positions);
	}
	public StringBuffer printExpression(int indent, StringBuffer output) {

		output.append("<SelectOnType:"); //$NON-NLS-1$
		for (int i = 0, length = tokens.length; i < length; i++) {
			if (i > 0) output.append('.');
			output.append(tokens[i]);
		}
		return output.append('>');
	}
}
