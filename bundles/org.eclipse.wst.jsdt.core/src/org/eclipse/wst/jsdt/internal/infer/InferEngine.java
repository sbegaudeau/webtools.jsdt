package org.eclipse.wst.jsdt.internal.infer;

 
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.ASTVisitor;
import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.wst.jsdt.internal.compiler.ast.Argument;
import org.eclipse.wst.jsdt.internal.compiler.ast.ArrayInitializer;
import org.eclipse.wst.jsdt.internal.compiler.ast.Assignment;
import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.Expression;
import org.eclipse.wst.jsdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ast.FieldReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.FunctionExpression;
import org.eclipse.wst.jsdt.internal.compiler.ast.Javadoc;
import org.eclipse.wst.jsdt.internal.compiler.ast.JavadocSingleNameReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.MessageSend;
import org.eclipse.wst.jsdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.NumberLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ast.ObjectLiteralField;
import org.eclipse.wst.jsdt.internal.compiler.ast.ProgramElement;
import org.eclipse.wst.jsdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.wst.jsdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.StringLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ast.ThisReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference;
import org.eclipse.wst.jsdt.internal.compiler.impl.BooleanConstant;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
import org.eclipse.wst.jsdt.internal.compiler.util.HashtableOfObject;
public class InferEngine extends ASTVisitor {

	InferOptions inferOptions;
	CompilationUnitDeclaration compUnit;
    Context [] contexts=new Context[100];
    int contextPtr=-1;
    Context currentContext=new Context();
    int passNumber=1;

    int anonymousCount=0;
    
    boolean isStaticClassReference;
    
	
	public  InferredType StringType=new InferredType(new char[]{'S','t','r','i','n','g'});
	public  InferredType NumberType=new InferredType(new char[]{'N','u','m','b','e','r'});
	public  InferredType BooleanType=new InferredType(new char[]{'B','o','o','l','e','a','n'});
	public  InferredType ArrayType=new InferredType(InferredType.ARRAY_NAME);
	public  InferredType VoidType=new InferredType(new char[]{'v','o','i','d'});
	public  InferredType ObjectType=new InferredType(InferredType.OBJECT_NAME);

    
	static final char[] CONSTRUCTOR_ID={'c','o','n','s','t','r','u','c','t','o','r'};
    
	static class Context 
	{
		InferredType currentType;
		MethodDeclaration currentMethod;
		HashtableOfObject definedMembers;
		
		Context(){}
		
		Context(Context from)
		{
			currentType=from.currentType;
			currentMethod=from.currentMethod;
			definedMembers=from.definedMembers;
		}
	}
	public InferEngine(InferOptions inferOptions)
	{
		this.inferOptions=inferOptions;
	}
	
	public InferEngine()
	{
	}
	
	
	public void setCompilationUnit(CompilationUnitDeclaration compilationUnit) {
		this.compUnit = compilationUnit;
		buildDefinedMembers(compilationUnit.statements,null);
	}


	public boolean visit(MessageSend messageSend, BlockScope scope) {
		boolean visitChildren=handleFunctionCall(messageSend);
		if (visitChildren)
		{
			
		}
		return visitChildren;
	}

	public boolean visit(LocalDeclaration localDeclaration, BlockScope scope) {
		if (localDeclaration.javadoc!=null)
		{
			Javadoc javadoc = localDeclaration.javadoc;
			InferredAttribute attribute = null;
			if (javadoc.memberOf!=null)
			{
				InferredType type = this.addType(javadoc.memberOf.getSimpleTypeName());
				 attribute = type.addAttribute(localDeclaration.name, localDeclaration);
				 if (localDeclaration.initialization!=null)
					 attribute.initializationStart=localDeclaration.initialization.sourceStart;
				attribute.type=type;
			}
			
			if (javadoc.returnType!=null)
			{
			   InferredType type = this.addType(javadoc.returnType.getSimpleTypeName());
			   localDeclaration.inferredType=type;
			   if (attribute!=null)
				   attribute.type=type;
			}
		}

		if (localDeclaration.inferredType==null && localDeclaration.initialization!=null)
		{
			localDeclaration.inferredType= getTypeOf(localDeclaration.initialization);
		}
		return true;
	}

	public boolean visit(Assignment assignment, BlockScope scope) {
		pushContext();
		if (handlePrototype(assignment))
		{
			
		}
		else if (assignment.expression instanceof FunctionExpression)
		{
			FunctionExpression functionExpression=(FunctionExpression)assignment.expression;
			InferredType type = compUnit.findInferredType(getTypeName(assignment.lhs));
			if (type!=null)	// isConstructor
			{
				if (this.inferOptions.useInitMethod)
				{
					this.currentContext.currentType=type;
					this.currentContext.currentType=type;
					type.isDefinition=true;
					InferredMethod method = type.addMethod(type.name, functionExpression.methodDeclaration);
					method.isConstructor=true;
					method.nameStart=assignment.lhs.sourceStart;
				}
				
			}
			else	// could be method
			{
				if (assignment.lhs instanceof FieldReference)
				{
					FieldReference fieldReference=(FieldReference)assignment.lhs;
					int nameStart=(int)(fieldReference.nameSourcePosition>>>32);
					InferredType receiverType = getReceiverInferredType(fieldReference.receiver);
					if (receiverType!=null)
					{
						InferredMethod method = receiverType.addMethod(fieldReference.token,functionExpression.methodDeclaration);
						receiverType.updatePositions(assignment.sourceStart, assignment.sourceEnd);
						method.nameStart=nameStart;
					}
					else if (this.passNumber==2)	// create anonymous class
					{
						AbstractVariableDeclaration var=getVariable(fieldReference.receiver);
						if (var!=null)
						{
							receiverType=createAnonymousType(var);
							InferredMethod method = receiverType.addMethod(fieldReference.token,functionExpression.methodDeclaration);
							method.nameStart=nameStart;
							receiverType.updatePositions(assignment.sourceStart, assignment.sourceEnd);
						}
					}
				}
			}
		}
		else   if (assignment.expression instanceof SingleNameReference  && this.currentContext.currentType !=null &&
				isThis(assignment.lhs))
		{
			SingleNameReference snr=(SingleNameReference)assignment.expression;
			Object object = this.currentContext.definedMembers.get(snr.token);
			if (object instanceof MethodDeclaration)
			{
				FieldReference fieldReference=(FieldReference)assignment.lhs;
				MethodDeclaration method=(MethodDeclaration)object;
				InferredMethod inferredMethod = this.currentContext.currentType.addMethod(fieldReference.token, method);
				inferredMethod.nameStart=fieldReference.sourceEnd-fieldReference.token.length+1;
			}
		}
		else   
		{
			if (this.inferOptions.useAssignments)
			{
				// determine field
				// 
				InferredAttribute attribute = getAttribute(assignment.lhs,true,assignment);
				if (attribute!=null)
				{
					// determine type
					// 
					InferredType type = getTypeOf(assignment.expression);
					attribute.type=type;
					return false;	// done with expr
				}
				
			}
		}
		return true; // do nothing by default, keep traversing
	}

	private InferredType createAnonymousType(AbstractVariableDeclaration var) {
		
		InferredType currentType = var.inferredType;
		
		if (currentType==null || !currentType.isAnonymous)
		{
			char[] cs = String.valueOf(this.anonymousCount++).toCharArray();
			char []name = CharOperation.concat(InferredType.ANONYMOUS_PREFIX,var.name,cs);
			InferredType type = addType(name);
			type.isDefinition=true;
			type.isAnonymous=true;
			if (currentType!=null)
				type.superClass=currentType;
			var.inferredType=type;
		}
		return var.inferredType;
	}

	protected boolean handlePrototype(Assignment assignment) {
		
		Expression lhs = assignment.lhs;
		if (lhs instanceof FieldReference) {
			FieldReference fieldReference = (FieldReference) lhs;
			if (fieldReference.isPrototype())
			{
				if (assignment.expression instanceof AllocationExpression)
				{
					char[] typeName = getTypeName(fieldReference.receiver);
					Object object = currentContext.definedMembers.get(typeName);
					if (object instanceof Argument)
						return false;
					AllocationExpression allocationExpression =(AllocationExpression)assignment.expression;
					InferredType newType = addType(typeName);
					newType.isDefinition=true;
					InferredType superClass=addType(getTypeName(allocationExpression.member));
					newType.superClass=superClass;
					newType.updatePositions(assignment.sourceStart, assignment.sourceEnd);
					return true;
				}
			}
			else if (fieldReference.receiver.isPrototype())
			{
				FieldReference prototype = (FieldReference) fieldReference.receiver;
				char[] typeName = getTypeName(prototype.receiver);
				Object receiverDef = currentContext.definedMembers.get(typeName);
				if (receiverDef instanceof Argument)
					return false;
				InferredType newType = addType(typeName);
				newType.isDefinition=true;
				newType.updatePositions(assignment.sourceStart, assignment.sourceEnd);
				char[] memberName = fieldReference.token;
				int nameStart= (int)(fieldReference.nameSourcePosition >>> 32);
				InferredType typeOf = getTypeOf(assignment.expression);
				MethodDeclaration methodDecl=null;
				if (typeOf==null && assignment.expression instanceof SingleNameReference)
				{
					Object object = this.currentContext.definedMembers.get(((SingleNameReference)assignment.expression).token);
					if (object instanceof AbstractMethodDeclaration)
						methodDecl=(MethodDeclaration)object;
				} else if (assignment.expression instanceof FunctionExpression)
					methodDecl=((FunctionExpression)assignment.expression).methodDeclaration;
				if (methodDecl!=null)
				{
					InferredMethod method = newType.addMethod(memberName, methodDecl);
					method.nameStart=nameStart;
				}
				else if (!CharOperation.equals(CONSTRUCTOR_ID, memberName))
				{
					InferredAttribute attribute = newType.addAttribute(memberName, assignment);
					attribute.initializationStart=assignment.expression.sourceStart;
					attribute.nameStart=nameStart;
					if (attribute.type==null)
						attribute.type=typeOf;
				}
				return true;
			}
		}
		return false;
	}

	protected InferredAttribute getAttribute(Expression lhs,boolean create, ASTNode definer) {
		InferredAttribute attribute=null;
		if (lhs instanceof FieldReference) {
			FieldReference fieldReference = (FieldReference) lhs;
			InferredType type = getReceiverInferredType(fieldReference.receiver);
			if (type!=null)
			{
				attribute = type.addAttribute(fieldReference.token,definer);
				attribute.isStatic=isStaticClassReference;
				attribute.nameStart=fieldReference.sourceEnd-fieldReference.token.length+1;
			}
		}
		return attribute;
	}

	protected InferredType getReceiverInferredType(Expression expression)
	{
		isStaticClassReference=false;
		if (expression instanceof ThisReference)
		{
			if (this.currentContext.currentType==null)
			{
//				if (isInNamedMethod())	// in constructor?
//				{
//					this.currentContext.currentType=addType(this.currentContext.currentMethod.selector);	
//					this.currentContext.currentType.isDefinition=true;
//				}
				
			}
			return this.currentContext.currentType;
		}
		else {
			InferredType type=findType(expression);
			if (type!=null)
			{
				isStaticClassReference=true;
				return type;
			}
		}
		return null;
	}
	
	protected InferredType getTypeOf(Expression expression) {
		if (expression instanceof StringLiteral) {
			return StringType;
		}
		else if (expression instanceof NumberLiteral) {
			return NumberType;
		}
		else if (expression instanceof AllocationExpression)
		{
			AllocationExpression allocationExpression=(AllocationExpression)expression;
			char [] typeName=getTypeName(allocationExpression.member);
			return addType(typeName);
		}
		else if (expression instanceof SingleNameReference)
		{
			SingleNameReference singleNameReference=(SingleNameReference)expression;
			Object definedMember = currentContext.definedMembers.get(singleNameReference.token);
			if (definedMember instanceof AbstractVariableDeclaration)
				return ((AbstractVariableDeclaration)definedMember).inferredType;
			
		}
		else if (expression instanceof FieldReference)
		{
			FieldReference fieldReference=(FieldReference)expression;
			if (fieldReference.receiver.isThis() && currentContext.currentType!=null)
			{
				InferredAttribute attribute = currentContext.currentType.findAttribute(fieldReference.token);
				if (attribute!=null)
					return attribute.type;
			}
		}
		else if (expression instanceof ArrayInitializer)
		{
			ArrayInitializer arrayInitializer = (ArrayInitializer)expression;
			boolean typeSet=false;
			InferredType memberType=null;
			if (arrayInitializer.expressions!=null)
				for (int i = 0; i < arrayInitializer.expressions.length; i++) {
					InferredType thisType = getTypeOf(arrayInitializer.expressions[i]);
					if (thisType!=null)
					{
					  if (!thisType.equals(memberType))
						  if (!typeSet)
							  memberType=thisType;
						  else
							  memberType=null;
					  typeSet=true;
 						 
					}
				}
			if (memberType!=null)
			{
				InferredType type = new InferredType(InferredType.ARRAY_NAME);
				type.referenceClass=memberType;
				return type;
			}
			else
			return ArrayType;
		}	else if (expression instanceof TrueLiteral || expression instanceof FalseLiteral) {
			return BooleanType;
		}
		return null;
	}

	public void endVisit(Assignment assignment, BlockScope scope) {
		popContext();
	}

	protected boolean handleFunctionCall(MessageSend messageSend) {
		return true;
	}


	
	
	
	public void endVisit(ReturnStatement returnStatement, BlockScope scope) {
		if (currentContext.currentMethod!=null)
		{
			if (returnStatement.expression!=null)
			{
				InferredType type = getTypeOf(returnStatement.expression);
				if (currentContext.currentMethod.inferredType==VoidType)
					currentContext.currentMethod.inferredType=type;
				else if (type==null || !type.equals(currentContext.currentMethod.inferredType))
					currentContext.currentMethod.inferredType=null;
			}
		}
	}

	public void endVisit(MethodDeclaration methodDeclaration, Scope scope) {
		popContext();
	}

	public boolean visit(MethodDeclaration methodDeclaration, Scope scope) {
		pushContext();
		if (passNumber==1)
		{
			buildDefinedMembers(methodDeclaration.statements,methodDeclaration.arguments);
			if (methodDeclaration.javadoc!=null)
			{
				InferredMethod method=null;
				Javadoc javadoc = methodDeclaration.javadoc;
				if (javadoc.isConstructor)
				{
					InferredType type = this.addType(methodDeclaration.selector);
					type.isDefinition=true;
					method = type.addMethod(methodDeclaration.selector, methodDeclaration);
					method.nameStart=methodDeclaration.sourceStart;
					method.isConstructor=true;
					
					if (javadoc.extendsType!=null)
					{
						InferredType superType=this.addType(javadoc.extendsType.getSimpleTypeName());
						type.superClass=superType;
					}
				}
				else if (javadoc.memberOf!=null)
				{
					InferredType type = this.addType(javadoc.memberOf.getSimpleTypeName());
					method=type.addMethod(methodDeclaration.selector, methodDeclaration);
				}
				
				if (javadoc.returnType!=null)
				{
				   InferredType type = this.addType(javadoc.returnType.getSimpleTypeName());
				   methodDeclaration.inferredType=type;
				}
				
				if (methodDeclaration.arguments!=null)
				for (int i = 0; i < methodDeclaration.arguments.length; i++) {
					JavadocSingleNameReference param = javadoc.findParam(methodDeclaration.arguments[i].name);
					if (param!=null)
					{
						if (param.types!=null)
							for (int j = 0; j < param.types.length; j++) {
								TypeReference reference = param.types[j];
								InferredType paramType=this.addType(reference.getSimpleTypeName());
								methodDeclaration.arguments[i].inferredType=paramType;
//TODO: what to do when more than one type?
								break;
							}
					}
				}
			}
		}
		// check if this is a constructor
		if (passNumber==2)
		{
			
			if (methodDeclaration.selector!=null) {
				InferredType type = compUnit
						.findInferredType(methodDeclaration.selector);
				if (type != null) {
					this.currentContext.currentType = type;
					type.isDefinition = true;
					InferredMethod method = type.addMethod(
							methodDeclaration.selector, methodDeclaration);
					method.nameStart=methodDeclaration.sourceStart;
					method.isConstructor = true;
					methodDeclaration.inferredType=type;
				}
			}			
		}
		this.currentContext.currentMethod=methodDeclaration;
		if (methodDeclaration.inferredMethod!=null && methodDeclaration.inferredMethod.inType!=null)
			this.currentContext.currentType=methodDeclaration.inferredMethod.inType;
		if (methodDeclaration.inferredType==null)
			methodDeclaration.inferredType=VoidType;
		return true;
	}
	
	
	
	public boolean visit(
    		AllocationExpression allocationExpression,
    		BlockScope scope) {
		
		char[] typeName = getTypeName(allocationExpression.member);
		if (typeName!=EMPTY_NAME)
			addType(typeName);
		
		return true;
	}
	
 
	public void endVisit(ObjectLiteralField field, BlockScope scope) {
		if (field.javaDoc!=null)
		{
			Javadoc javaDoc = field.javaDoc;
			InferredType inClass=null;
			char [] name=null;
			int nameStart=-1;
			InferredType returnType=null;
//			boolean isFunction=field.initializer instanceof FunctionExpression;
 			if (field.fieldName instanceof SingleNameReference)
 			{
 				SingleNameReference singleNameReference=(SingleNameReference)field.fieldName;
 				name=singleNameReference.token;
 				nameStart=singleNameReference.sourceStart;
 			}
			if (javaDoc.memberOf!=null)
			{
				inClass= this.addType(javaDoc.memberOf.getSimpleTypeName());
				inClass.isDefinition=true;
			}
			if (javaDoc.returnType!=null)
			{
				returnType=this.addType(javaDoc.returnType.getSimpleTypeName());
			}
			
			if (inClass!=null)
			{
				if (field.initializer instanceof FunctionExpression) {
					FunctionExpression functionExpression = (FunctionExpression) field.initializer;
				    InferredMethod method = inClass.addMethod(name, functionExpression.methodDeclaration);
				    method.nameStart=nameStart;
				    functionExpression.methodDeclaration.modifiers=javaDoc.modifiers;
				    if (returnType!=null)
				    {
				    	functionExpression.methodDeclaration.inferredType=returnType;
				    }
//				    else
//				    	method.inferredType=functionExpression.methodDeclaration.inferredType;
				}	
				else	//attribute
				{
					InferredAttribute attribute = inClass.addAttribute(name, field.fieldName);
					attribute.nameStart=field.fieldName.sourceStart;
					if (returnType!=null)
						attribute.type=returnType;
				}
			}
			
		}
	}

	protected boolean isMatch(Expression expr,String [] names, int index)
	{
		char [] matchName=names[index].toCharArray();
		if (expr instanceof SingleNameReference) {
			SingleNameReference snr = (SingleNameReference) expr;
			return CharOperation.equals(snr.token, matchName);
		}
		return false;
	}

	protected boolean isFunction(MessageSend messageSend,String string) {
		String []names=string.split("\\.");
		char [] functionName=names[names.length-1].toCharArray();
		if (!CharOperation.equals(functionName, messageSend.selector))
			return false;
		
		if (names.length>1)
			return isMatch(messageSend.receiver, names, names.length-2);
		return true;
	}
	
	public void doInfer()
	{
		BlockScope scope=null;
		compUnit.traverse(this, compUnit.scope,true);
		passNumber=2;
		compUnit.traverse(this, compUnit.scope,true);
}
	

	protected InferredType addType(char[] className) {
		
		InferredType type = compUnit.findInferredType(className);

		
		if (type==null)
		{
			if (compUnit.numberInferredTypes == compUnit.inferredTypes.length)
				
				System.arraycopy(
						compUnit.inferredTypes,
						0,
						(compUnit.inferredTypes = new InferredType[compUnit.numberInferredTypes  * 2]),
						0,
						compUnit.numberInferredTypes );
			type=compUnit.inferredTypes[compUnit.numberInferredTypes ++] = new InferredType(className);
			compUnit.inferredTypesHash.put(className,type);
				
		}
		return type;
	}
	
	protected final void pushContext()
	{
		Context newContext=new Context(currentContext);
		contexts[++contextPtr]=currentContext;
		currentContext=newContext;
		
	}
	
	protected final void popContext()
	{
		currentContext=contexts[contextPtr--];
	}
	
	protected final boolean isInNamedMethod()
	{
		return this.currentContext.currentMethod!=null && this.currentContext.currentMethod.selector!=null;
	}


	protected InferredType findType(Expression expression)
	{
		char []typeName=getTypeName(expression);
		InferredType type = null;
		if (typeName!=EMPTY_NAME)
		{
			 type = compUnit.findInferredType(typeName);
		}
		return type;
	}
	
	final static char[] EMPTY_NAME=new char[0];
	protected final char [] getTypeName(Expression expression)
	{
		char [] name = EMPTY_NAME;
		if (expression instanceof FieldReference) {
			FieldReference fieldRef = (FieldReference) expression;
			return CharOperation.concat(getTypeName(fieldRef.receiver), fieldRef.token,'.');
		}
		else if (expression instanceof SingleNameReference) {
			SingleNameReference singleNameReference = (SingleNameReference) expression;
			return singleNameReference.token;
		}
		return name;
	}
	
	protected AbstractVariableDeclaration getVariable(Expression expression)
	{
		char [] name=null;
		
		if (expression instanceof SingleNameReference)
			name=((SingleNameReference)expression).token;
		if (name!=null)
		{
			Object var=this.currentContext.definedMembers.get(name);
			if (var instanceof AbstractVariableDeclaration)
				return (AbstractVariableDeclaration)var;
		}
		return null;
		
	}
	
	private void buildDefinedMembers(ProgramElement[] statements, Argument[] arguments) {
		if (this.currentContext.definedMembers==null)
			this.currentContext.definedMembers=new HashtableOfObject();
		if (arguments!=null)
		{
			for (int i = 0; i < arguments.length; i++) {
				this.currentContext.definedMembers.put(arguments[i].name, arguments[i]);
			}
		}
		if (statements!=null)
		{
			for (int i = 0; i < statements.length; i++) {
				if (statements[i] instanceof LocalDeclaration) {
					LocalDeclaration local = (LocalDeclaration) statements[i];
					this.currentContext.definedMembers.put(local.name, local);
				}
				else if (statements[i] instanceof AbstractMethodDeclaration) {
					AbstractMethodDeclaration method = (AbstractMethodDeclaration) statements[i];
					if (method.selector!=null)
						this.currentContext.definedMembers.put(method.selector, method);
				}
			}
		}
	}

	private static boolean isThis(Expression expression)
	{
		if (expression instanceof FieldReference && ((FieldReference)expression).receiver.isThis())
			return true;
		return false;
	}

}
