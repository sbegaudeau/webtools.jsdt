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
import org.eclipse.wst.jsdt.internal.compiler.ast.CharLiteral;
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
import org.eclipse.wst.jsdt.internal.compiler.ast.ObjectLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ast.ObjectLiteralField;
import org.eclipse.wst.jsdt.internal.compiler.ast.ProgramElement;
import org.eclipse.wst.jsdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.wst.jsdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.StringLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ast.ThisReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ast.TypeReference;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
import org.eclipse.wst.jsdt.internal.compiler.util.HashtableOfObject;
import org.eclipse.wst.jsdt.internal.compiler.util.Util;
public class InferEngine extends ASTVisitor {

	InferOptions inferOptions;
	CompilationUnitDeclaration compUnit;
    Context [] contexts=new Context[100];
    int contextPtr=-1;
    Context currentContext=new Context();
    int passNumber=1;

    boolean isTopLevelAnonymousFunction;
    int anonymousCount=0;

	public  InferredType StringType=new InferredType(new char[]{'S','t','r','i','n','g'});
	public  InferredType NumberType=new InferredType(new char[]{'N','u','m','b','e','r'});
	public  InferredType BooleanType=new InferredType(new char[]{'B','o','o','l','e','a','n'});
	public  InferredType ArrayType=new InferredType(InferredType.ARRAY_NAME);
	public  InferredType VoidType=new InferredType(new char[]{'v','o','i','d'});
	public  InferredType ObjectType=new InferredType(InferredType.OBJECT_NAME);
	public  InferredType GlobalType=new InferredType(InferredType.GLOBAL_NAME);

	protected InferredType inferredGlobal=null;

	static final char[] CONSTRUCTOR_ID={'c','o','n','s','t','r','u','c','t','o','r'};

	public final static char[] ANONYMOUS_PREFIX = {'_','_','_'};
	public static final char[] ANONYMOUS_CLASS_ID= {'a','n','o','n','y','m','o','u','s'};

	static class Context
	{
		InferredType currentType;
		MethodDeclaration currentMethod;

		private HashtableOfObject definedMembers;

		/*
		 * Parent context to provide chaining when searching
		 * for members in scope.
		 */
		private Context parent = null;

		/*
		 * Root context
		 */
		Context(){}

		/*
		 * Nested context
		 */
		Context( Context parent )
		{
			this.parent = parent;

			currentType = parent.currentType;
			currentMethod = parent.currentMethod;
		}

		public Object getMember( char [] key ){

			Object value = null;
			if( definedMembers != null ){
				value = definedMembers.get( key );
			}

			//chain lookup
			if( value == null && parent != null ){
				value = parent.getMember( key );
			}

			return value;
		}

		public void addMember( char [] key, Object member ){

			if( definedMembers == null ){
				definedMembers = new HashtableOfObject();
			}

			definedMembers.put( key, member );
		}


	}

	public InferEngine(InferOptions inferOptions)
	{
		this.inferOptions=inferOptions;
	}

	public InferEngine()
	{
		this.inferOptions=new InferOptions();
	}


	public void initialize()
	{
	    this.contextPtr=-1;
	    this.currentContext=new Context();
	    this.passNumber=1;
	    this.isTopLevelAnonymousFunction=false;
	    this.anonymousCount=0;
	    this.inferredGlobal=null;

	}

	public void setCompilationUnit(CompilationUnitDeclaration compilationUnit) {
		this.compUnit = compilationUnit;
		buildDefinedMembers(compilationUnit.statements,null);
	}


	public boolean visit(MessageSend messageSend, BlockScope scope) {
		boolean visitChildren=handleFunctionCall(messageSend);
		if (visitChildren)
		{
			if (this.contextPtr==-1 && messageSend.receiver instanceof FunctionExpression)
				this.isTopLevelAnonymousFunction=true;
		}
		return visitChildren;
	}

	public boolean visit(LocalDeclaration localDeclaration, BlockScope scope) {

		//add as a member of the current context
		currentContext.addMember( localDeclaration.name, localDeclaration );

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
			boolean keepVisiting= handleFunctionExpressionAssignment(assignment);
			if (!keepVisiting)
				return false;
		}
		else if (assignment.expression instanceof SingleNameReference  && this.currentContext.currentType !=null &&
				isThis(assignment.lhs))
		{
			SingleNameReference snr=(SingleNameReference)assignment.expression;
			Object object = this.currentContext.getMember( snr.token );


			FieldReference fieldReference=(FieldReference)assignment.lhs;
			char [] memberName = fieldReference.token;
			InferredMember member = null;

			/*
			 * this.foo = bar //bar is a function
			 */
			if( object instanceof MethodDeclaration ){

				MethodDeclaration method=(MethodDeclaration)object;
				member = this.currentContext.currentType.addMethod( memberName, method );

			}
			/*
			 * this.foo = bar //assume that bar is not a function and create a new attribute in teh current type
			 */
			else{

				member = this.currentContext.currentType.addAttribute( memberName, assignment );
				((InferredAttribute)member).type = getTypeOf( assignment.expression );

			}

			//setting location
			if( member != null ){
				member.isStatic = false; //this is a not static member because it is being set on the this
				member.nameStart = fieldReference.sourceEnd - memberName.length+1;
			}
		}

		/*
		 *	foo = {};
		 */
		else if ( assignment.expression instanceof ObjectLiteral && assignment.lhs instanceof SingleNameReference ){

			AbstractVariableDeclaration varDecl = getVariable( assignment.lhs );

			if( varDecl != null ){
				InferredType type = varDecl.inferredType;

				if( type == null ){
					//create an annonymous type based on the ObjectLiteral
					type = getTypeOf( assignment.expression );

					varDecl.inferredType = type;

					return true;
				}
				else
					return false; //

			}

		}
		/*
		 * foo.bar = {};
		 *
		 */
		else if ( assignment.expression instanceof ObjectLiteral && assignment.lhs instanceof FieldReference ){

			if (this.inferOptions.useAssignments && passNumber == 2 )
			{

				FieldReference fRef = (FieldReference)assignment.lhs;

				InferredType receiverType = getInferredType( fRef.receiver );

				if (receiverType==null && this.passNumber==2)
				  receiverType=getInferredType2(fRef.receiver );

				if( receiverType != null ){
					//check if there is an attribute already created

					InferredAttribute attr = receiverType.findAttribute( fRef.token );

					//ignore if the attribute exists and has a type
					if( !(attr != null && attr.type != null) ){

						attr = receiverType.addAttribute( fRef.token, assignment );
						attr.type = getTypeOf( assignment.expression );

						/*
						 * determine if static
						 *
						 * check if the receiver is a type
						 */
						char [] possibleTypeName = constructTypeName( fRef.receiver );

						if( possibleTypeName != null && compUnit.findInferredType( possibleTypeName ) != null )
							attr.isStatic = true;
						else
							attr.isStatic = false;

						attr.nameStart = (int)(fRef.nameSourcePosition>>>32);

						return false; //done with this
					}

				}
			}
		}
		else if ( assignment.expression instanceof AllocationExpression && 
			((AllocationExpression)assignment.expression).member instanceof FunctionExpression){
			handleFunctionExpressionAssignment(assignment);
		}
		else
		{
			/*
			 * foo.bar = ?  //? is not {} and not a function
			 */
			if (this.inferOptions.useAssignments)
			{
				if( assignment.lhs instanceof FieldReference ){
					FieldReference fRef = (FieldReference)assignment.lhs;
					int nameStart=(int)(fRef.nameSourcePosition>>>32);

					InferredType receiverType = getInferredType( fRef.receiver );
					if (receiverType==null)
					{
					  MethodDeclaration function = getDefinedFunction(fRef.receiver);
					  if (function!=null)
						  receiverType=addType(constructTypeName(fRef.receiver));
					}
					if (receiverType==null && this.passNumber==2)
						  receiverType=getInferredType2(fRef.receiver );

					if( receiverType != null ){
						//check if there is an attribute already created

						InferredMethod method=null;
						InferredAttribute attr = receiverType.findAttribute( fRef.token );
						if (attr==null)
							 method = receiverType.findMethod(fRef.token, null);

						//ignore if the attribute exists and has a type
						if(  (method==null && attr==null) ||  (method==null && attr != null && attr.type == null) ){


//							attr.type =
							MethodDeclaration definedFunction=null;
							InferredType exprType = getTypeOf( assignment.expression );
							if (exprType==null)
								 definedFunction = getDefinedFunction(assignment.expression );

							if (definedFunction!=null)
							{
								method = receiverType.addMethod(fRef.token, definedFunction);
								method.nameStart=nameStart;
							}
							else
							{
							  attr = receiverType.addAttribute( fRef.token, assignment );

							  attr.type=exprType;
							/*
							 * determine if static
							 *
							 * check if the receiver is a type
							 */
							  char [] possibleTypeName = constructTypeName( fRef.receiver );

							  if( possibleTypeName != null && compUnit.findInferredType( possibleTypeName ) != null )
								attr.isStatic = true;
							  else
								attr.isStatic = false;

							  attr.nameStart = (int)(fRef.nameSourcePosition>>>32);
							}
							return false; //done with this
						}


					}
				}
			}
		}
		return true; // do nothing by default, keep traversing
	}

	protected InferredType getInferredType2(Expression fieldReceiver)
	{
		InferredType receiverType=null;
		AbstractVariableDeclaration var=getVariable(fieldReceiver);
		if (var!=null)
		{
			receiverType=createAnonymousType(var);
		}
		else
		{
			if (this.inferredGlobal!=null && fieldReceiver instanceof SingleNameReference)
			{
				char []name=((SingleNameReference)fieldReceiver).token;
				InferredAttribute attr=(InferredAttribute)this.inferredGlobal.attributesHash.get(name);
				if (attr!=null)
					receiverType=attr.type;
			}

		}
		return receiverType;
	}

	private InferredType createAnonymousType(AbstractVariableDeclaration var) {

		InferredType currentType = var.inferredType;

		if (currentType==null || !currentType.isAnonymous)
		{
			char[] cs = String.valueOf(this.anonymousCount++).toCharArray();
			char []name = CharOperation.concat(ANONYMOUS_PREFIX,var.name,cs);
			InferredType type = addType(name);
			type.isDefinition=true;
			type.isAnonymous=true;
			if (currentType!=null)
				type.superClass=currentType;
			var.inferredType=type;
		}
		return var.inferredType;
	}

	/*
	 * Creates an anonymous type based in the location in the document. This information is used
	 * to avoid creating duplicates because of the 2-pass nature of this engine.
	 */
	private InferredType createAnonymousType( ObjectLiteral objLit ) {

		//char[] cs = String.valueOf(this.anonymousCount2++).toCharArray();
		char [] loc = (String.valueOf( objLit.sourceStart ) + '_' + String.valueOf( objLit.sourceEnd )).toCharArray();
		char []name = CharOperation.concat( ANONYMOUS_PREFIX, ANONYMOUS_CLASS_ID, loc );

		InferredType anonType = addType(name);
		anonType.isDefinition=true;
		anonType.isAnonymous=true;
		anonType.superClass = ObjectType;

		anonType.sourceStart = objLit.sourceStart;
		anonType.sourceEnd = objLit.sourceEnd;

		populateType( anonType, objLit );

		return anonType;
	}

	
	protected boolean handleFunctionExpressionAssignment(Assignment assignment)
	{
		FunctionExpression functionExpression=null;
		if (assignment.expression instanceof FunctionExpression)
			functionExpression=(FunctionExpression)assignment.expression;
		else if (assignment.expression instanceof AllocationExpression)
			functionExpression=(FunctionExpression)((AllocationExpression)assignment.expression).member;

		char [] possibleTypeName = constructTypeName( assignment.lhs );

		InferredType type = null;
		if( possibleTypeName != null )
			type = compUnit.findInferredType( possibleTypeName );

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

				InferredType receiverType = getInferredType( fieldReference.receiver );

				if( receiverType != null ){

					//check if there is a member method already created
					InferredMethod method = receiverType.findMethod( fieldReference.token, functionExpression.methodDeclaration );

					if( method == null ){
						//create member method if it does not exist

						method = receiverType.addMethod( fieldReference.token, functionExpression.methodDeclaration );
						receiverType.updatePositions(assignment.sourceStart, assignment.sourceEnd); // @GINO: not sure if necessary
						method.nameStart = nameStart;

						/*
						 * determine if static
						 *
						 * check if the receiver is a type
						 */
						char [] possibleInTypeName = constructTypeName( fieldReference.receiver );

						if( possibleInTypeName != null && compUnit.findInferredType( possibleInTypeName ) != null )
							method.isStatic = true;
						else
							method.isStatic = false;

						return true; //keep visiting to get return type
					}
					else
						return false; //no need to visit again

				}
				else if (this.passNumber==2)	// create anonymous class
				{
					receiverType = getInferredType2(fieldReference.receiver);
					if (receiverType!=null)
					{
						InferredMethod method = receiverType.addMethod(fieldReference.token,functionExpression.methodDeclaration);
						method.nameStart=nameStart;
						receiverType.updatePositions(assignment.sourceStart, assignment.sourceEnd);
					}
				}
			}
			else if (assignment.lhs instanceof SingleNameReference)
			{
			}
		}
		return true;
	}
	
	
	protected boolean handlePrototype(Assignment assignment) {

		Expression lhs = assignment.lhs;
		if (lhs instanceof FieldReference) {
			FieldReference fieldReference = (FieldReference) lhs;

			/*
			 * foo.prototype = ?
			 */
			if (fieldReference.isPrototype())
			{
				/*
				 * When encountering a prototype, we are going to assume that the
				 * receiver is a type.
				 *
				 * If the type had not been inferred, it will be added at this point
				 */
				InferredType newType = null;
				char [] possibleTypeName = constructTypeName( fieldReference.receiver );
				if( possibleTypeName != null )
					newType = compUnit.findInferredType( possibleTypeName );
				else
					return true; //no type created


				//create the new type if not found
				if( newType == null ){
					newType = addType( possibleTypeName );
					newType.isDefinition = true;
				}

//				char[] typeName = getTypeName(fieldReference.receiver);
//				Object object = currentContext.definedMembers.get(typeName);
//
//				if (object instanceof Argument)
//					return false;
//
//				InferredType newType = addType(typeName);
//				newType.isDefinition=true;

				newType.updatePositions(assignment.sourceStart, assignment.sourceEnd);

				/*
				 * foo.prototype = new ...
				 */
				if (assignment.expression instanceof AllocationExpression)
				{
					//setting the super type
					AllocationExpression allocationExpression =(AllocationExpression)assignment.expression;

					InferredType superType = null;
					char [] possibleSuperTypeName = constructTypeName( allocationExpression.member );
					if( possibleSuperTypeName != null ){
						superType = compUnit.findInferredType( possibleSuperTypeName );

						if( superType == null )
							superType = addType( possibleSuperTypeName );

						//check if it is set already because it might be set by jsdocs
						if( newType.superClass == null )
							newType.superClass = superType;
					}

					return true;
				}
				/*
				 * foo.prototype = {...}
				 */
				else if( assignment.expression instanceof ObjectLiteral ){
					//rather than creating an anonymous type, is better just to set the members directly
					//on newType
					populateType( newType, (ObjectLiteral)assignment.expression );

					//check if it is set already because it might be set by jsdocs
					if( newType.superClass == null )
						newType.superClass = ObjectType;

					return true;
				}
			}
			/*
			 * foo.prototype.bar = ?
			 */
			else if ( fieldReference.receiver.isPrototype() )
			{

				FieldReference prototype = (FieldReference) fieldReference.receiver;

				InferredType newType = null;
				char[] possibleTypeName = constructTypeName( prototype.receiver );
				if( possibleTypeName != null )
					newType = compUnit.findInferredType( possibleTypeName );
				else
					return true; //no type created

				//create the new type if not found
				if( newType == null ){
					newType = addType( possibleTypeName );
					newType.isDefinition = true;
				}

//				char[] typeName = getTypeName(prototype.receiver);
//				Object receiverDef = currentContext.definedMembers.get(typeName);
//				if (receiverDef instanceof Argument)
//					return false;
//				InferredType newType = addType(typeName);
//				newType.isDefinition=true;

				newType.updatePositions(assignment.sourceStart, assignment.sourceEnd);

				//prevent Object literal based anonymous types from being created more than once
				if( passNumber == 1 && assignment.expression instanceof ObjectLiteral ){
					return false;
				}

				char[] memberName = fieldReference.token;
				int nameStart= (int)(fieldReference.nameSourcePosition >>> 32);

				InferredType typeOf = getTypeOf(assignment.expression);
				MethodDeclaration methodDecl=null;

				if (typeOf==null)
					methodDecl=getDefinedFunction(assignment.expression);

				if (methodDecl!=null)
				{
					InferredMember method = newType.addMethod(memberName, methodDecl);
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

	protected MethodDeclaration getDefinedFunction(Expression expression)
	{
		if (expression instanceof SingleNameReference)
		{
			Object object = this.currentContext.getMember( ((SingleNameReference)expression).token );
			if (object instanceof AbstractMethodDeclaration)
				return (MethodDeclaration)object;
		} else if (expression instanceof FunctionExpression)
			return ((FunctionExpression)expression).methodDeclaration;
		 else if (expression instanceof FieldReference)
		 {
			 FieldReference fieldReference=(FieldReference)expression;
			InferredType receiverType = getInferredType( fieldReference.receiver );
			if (receiverType==null && passNumber==2)
				receiverType=getInferredType2( fieldReference.receiver );
			if (receiverType!=null)
			{
				InferredMethod method = receiverType.findMethod(fieldReference.token, null);
				if (method!=null)
					return method.methodDeclaration;
			}

		 }

		return null;

	}

	protected InferredType getTypeOf(Expression expression) {
		if (expression instanceof StringLiteral || expression instanceof CharLiteral) {
			return StringType;
		}
		else if (expression instanceof NumberLiteral) {
			return NumberType;
		}
		else if (expression instanceof AllocationExpression)
		{
			AllocationExpression allocationExpression=(AllocationExpression)expression;

			InferredType type = null;
			char [] possibleTypeName = constructTypeName( allocationExpression.member );
			if( possibleTypeName != null ){
				type = compUnit.findInferredType( possibleTypeName );

				if( type == null )
					type = addType( possibleTypeName );

				return type;
			}
		}
		else if (expression instanceof SingleNameReference)
		{
			AbstractVariableDeclaration varDecl = getVariable( expression );
			if( varDecl != null )
				return varDecl.inferredType;
			if (this.inferredGlobal!=null)
			{
				InferredAttribute attribute = this.inferredGlobal.findAttribute(((SingleNameReference)expression).token );
				if (attribute!=null)
					return attribute.type;
			}

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
		else if ( expression instanceof ObjectLiteral ){

			//create an annonymous type based on the ObjectLiteral
			InferredType type = createAnonymousType( (ObjectLiteral)expression );

			//set the start and end
			type.sourceStart = expression.sourceStart;
			type.sourceEnd = expression.sourceEnd;

			return type;


		}
		return null;
	}

	private void populateType(InferredType type, ObjectLiteral objLit) {
		if( objLit.fields != null ){
			for (int i = 0; i < objLit.fields.length; i++) {
				ObjectLiteralField field = objLit.fields[i];

				char [] name=null;
				int nameStart=-1;


				if (field.fieldName instanceof SingleNameReference)
				{
					SingleNameReference singleNameReference=(SingleNameReference)field.fieldName;
					name=singleNameReference.token;
					nameStart=singleNameReference.sourceStart;
				}
				else
					continue; //not supporting this case right now

				//need to build the members of the annonymous inferred type
				if (field.initializer instanceof FunctionExpression) {
					FunctionExpression functionExpression = (FunctionExpression) field.initializer;
				    InferredMember method = type.addMethod(name, functionExpression.methodDeclaration);
				    method.nameStart = nameStart;

				}
				else	//attribute
				{
					InferredAttribute attribute = type.addAttribute(name, field.fieldName);
					attribute.nameStart = nameStart;

					//@GINO: recursion might not be the best idea
					attribute.type = getTypeOf( field.initializer );
				}
			}
		}
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
		if (this.isTopLevelAnonymousFunction && this.currentContext.currentType==null)
		{
			this.currentContext.currentType=addType(InferredType.GLOBAL_NAME);
			this.currentContext.currentType.isDefinition=true;
			this.inferredGlobal=this.currentContext.currentType;
		}

		this.isTopLevelAnonymousFunction=false;
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
				   methodDeclaration.bits |= ASTNode.IsInferredJsDocType;
				}

			}
			if (methodDeclaration.arguments!=null)
				handleFunctionDeclarationArguments(methodDeclaration);
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

	protected void handleFunctionDeclarationArguments(MethodDeclaration methodDeclaration) {
		Javadoc javadoc = methodDeclaration.javadoc;
		if (javadoc==null)
			return;
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



	public boolean visit(
    		AllocationExpression allocationExpression,
    		BlockScope scope) {

		InferredType type = null;
		char [] possibleTypeName = constructTypeName( allocationExpression.member );
		if( possibleTypeName != null ){
			type = compUnit.findInferredType( possibleTypeName );

			if( type == null  )
				type = addType( possibleTypeName ); //creating type
		}
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
				    InferredMember method = inClass.addMethod(name, functionExpression.methodDeclaration);
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
		//no jsdoc
		else{

			if( field.initializer instanceof ObjectLiteral ){

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
		else if (expr instanceof FieldReference && names.length>1) {
			FieldReference fieldReference = (FieldReference) expr;
			if (CharOperation.equals(fieldReference.token, matchName))
				return isMatch(fieldReference.receiver, names, index-1);

		}
		return false;
	}

	protected boolean isFunction(MessageSend messageSend,String string) {
		String []names=string.split("\\."); //$NON-NLS-1$
		char [] functionName=names[names.length-1].toCharArray();
		if (!CharOperation.equals(functionName, messageSend.selector))
			return false;

		if (names.length>1)
			return isMatch(messageSend.receiver, names, names.length-2);
		return true;
	}

	public void doInfer()
	{
		compUnit.traverse(this, compUnit.scope,true);
		passNumber=2;
		compUnit.traverse(this, compUnit.scope,true);
		this.compUnit=null;
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
		Context newContext = new Context( currentContext  );
		contexts[++contextPtr] = currentContext;
		currentContext = newContext;

	}

	protected final void popContext()
	{
		currentContext = contexts[contextPtr];
		contexts[contextPtr--] = null;
	}

	protected final boolean isInNamedMethod()
	{
		return this.currentContext.currentMethod!=null && this.currentContext.currentMethod.selector!=null;
	}


	/*
	 * Finds a Var Declaration on the context from the name represented with the expression
	 *
	 * Currently, only SNR are supported
	 */
	protected AbstractVariableDeclaration getVariable(Expression expression)
	{
		char [] name=null;

		if (expression instanceof SingleNameReference)
			name=((SingleNameReference)expression).token;
		if (name!=null)
		{
			Object var = this.currentContext.getMember( name );
			if (var instanceof AbstractVariableDeclaration)
				return (AbstractVariableDeclaration)var;

		}
		return null;

	}

	private void buildDefinedMembers(ProgramElement[] statements, Argument[] arguments) {

		if (arguments!=null)
		{
			for (int i = 0; i < arguments.length; i++) {
				this.currentContext.addMember( arguments[i].name, arguments[i] );
			}
		}
		if (statements!=null)
		{
			for (int i = 0; i < statements.length; i++) {
				if (statements[i] instanceof LocalDeclaration) {
					LocalDeclaration local = (LocalDeclaration) statements[i];
					this.currentContext.addMember( local.name, local );
				}
				else if (statements[i] instanceof AbstractMethodDeclaration) {
					AbstractMethodDeclaration method = (AbstractMethodDeclaration) statements[i];
					if (method.selector!=null)
						this.currentContext.addMember( method.selector, method );
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

	/*
	 * This method is used to determined the inferred type of a LHS Expression.
	 *
	 * It could return null.
	 *
	 * a.b.c
	 */
	private InferredType getInferredType( Expression expression ){

		InferredType type = null;

		/*
		 * this
		 */
		if( expression instanceof ThisReference ){

			type = this.currentContext.currentType;
		}
		/*
		 * foo (could be a Type name or a reference to a variable)
		 */
		else if( expression instanceof SingleNameReference ){
			char [] possibleTypeName = constructTypeName( expression );

			if( possibleTypeName != null ){
				//search the defined types in the context
				type = compUnit.findInferredType( possibleTypeName );

				/*
				 * There is no match for a type with the name, check if the name refers to
				 * var decl and return its type
				 */
				if( type == null ){

					AbstractVariableDeclaration varDecl = getVariable( (expression) );

					if( varDecl != null ){
						type = varDecl.inferredType; //could be null
					}

				}
			}
		}
		/*
		 * foo.bar.xxx...
		 */
		else if( expression instanceof FieldReference ){
			char [] possibleTypeName = constructTypeName( expression );

			if( possibleTypeName != null ){
				//search the defined types in the context
				type = compUnit.findInferredType( possibleTypeName );

				/*
				 * Continue the search by trying to resolve further down the name
				 * because this token of the field reference could be a member of
				 * a type or instance of a type
				 */
				if( type == null ){
					FieldReference fRef = (FieldReference)expression;

					//this
					InferredType parentType = getInferredType( fRef.receiver );

					if( parentType != null ){
						//check the members and return type
						InferredAttribute typeAttribute = parentType.findAttribute( fRef.token );

						if( typeAttribute != null )
							type = typeAttribute.type;
					}
				}
			}
		}

		return type;
	}

	/*
	 * For SNR it returns the name
	 * For FR it construct a Qualified name separated by '.'
	 *
	 * If at any point it hits a portion of the Field reference that is
	 * not supported (such as a function call, a prototype, or this )
	 */
	protected final char [] constructTypeName( Expression expression ){

		return Util.getTypeName( expression );
	}

}
