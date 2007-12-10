package org.eclipse.wst.jsdt.core.dom;

import java.util.ArrayList;
import java.util.List;

public class EmptyExpression extends Expression {

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}),
	 * or null if uninitialized.
	 */
	private static final List PROPERTY_DESCRIPTORS;

	static {
		List properyList = new ArrayList(1);
		createPropertyList(EmptyStatement.class, properyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(properyList);
	}

	/**
	 * Returns a list of structural property descriptors for this node type.
	 * Clients must not modify the result.
	 *
	 * @param apiLevel the API level; one of the
	 * <code>AST.JLS*</code> constants

	 * @return a list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor})
	 * @since 3.0
	 */
	public static List propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}

	
	EmptyExpression(AST ast) {
		super(ast);
	}

	void accept0(ASTVisitor visitor) {

	}

	ASTNode clone0(AST target) {
		EmptyExpression result = new EmptyExpression(target);
		result.setSourceRange(this.getStartPosition(), this.getLength());
		return result;
	}

	int getNodeType0() {
		return EMPTY_EXPRESSION;
	}

	/* (omit javadoc for this method)
	 * Method declared on ASTNode.
	 */
	final List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);
	}

	int memSize() {
		return BASE_NODE_SIZE + 1 * 4;
	}

	boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		return this.equals(other);
	}

	int treeSize() {
		// TODO Auto-generated method stub
		return memSize();
	}

}
