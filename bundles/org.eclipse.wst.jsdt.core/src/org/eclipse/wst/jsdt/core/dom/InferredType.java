package org.eclipse.wst.jsdt.core.dom;

import java.util.ArrayList;
import java.util.List;

public class InferredType extends Type {

	private static final List PROPERTY_DESCRIPTORS;

//	public static final ChildPropertyDescriptor TYPE_PROPERTY =
//		new ChildPropertyDescriptor(InferredType.class, "type", String.class, MANDATORY, NO_CYCLE_RISK); //$NON-NLS-1$

	static {
		List propertyList = new ArrayList(0);
		createPropertyList(InferredType.class, propertyList);
// 		addProperty(TYPE_PROPERTY, propertyList);
		PROPERTY_DESCRIPTORS = reapPropertyList(propertyList);
	}

     String type;


	InferredType(AST ast) {
		super(ast);
	}

	public static List propertyDescriptors(int apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}


	void accept0(ASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
//			acceptChild(visitor, getName());
		}
		visitor.endVisit(this);

	}

	ASTNode clone0(AST target) {
		InferredType result = new InferredType(target);
		result.setSourceRange(-1,0);
		result.type = type;

		return result;
	}

	int getNodeType0() {
		return INFERRED_TYPE;
	}

	List internalStructuralPropertiesForType(int apiLevel) {
		return propertyDescriptors(apiLevel);

	}


	int memSize() {
		return 0;
	}

	boolean subtreeMatch0(ASTMatcher matcher, Object other) {
		return matcher.match(this, other);
	}

	int treeSize() {
		return 0;
	}
	public boolean isInferred()
	{
		return true;
	}

	public String getType() {
		return this.type;
	}


}
