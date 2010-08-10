/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.infer;

import org.eclipse.wst.jsdt.core.ast.ASTVisitor;
import org.eclipse.wst.jsdt.core.ast.IAbstractFunctionDeclaration;
import org.eclipse.wst.jsdt.core.ast.IAbstractVariableDeclaration;
import org.eclipse.wst.jsdt.core.ast.IAllocationExpression;
import org.eclipse.wst.jsdt.core.ast.IArgument;
import org.eclipse.wst.jsdt.core.ast.IAssignment;
import org.eclipse.wst.jsdt.core.ast.IExpression;
import org.eclipse.wst.jsdt.core.ast.IFalseLiteral;
import org.eclipse.wst.jsdt.core.ast.IFieldReference;
import org.eclipse.wst.jsdt.core.ast.IFunctionCall;
import org.eclipse.wst.jsdt.core.ast.IFunctionDeclaration;
import org.eclipse.wst.jsdt.core.ast.IFunctionExpression;
import org.eclipse.wst.jsdt.core.ast.IJsDoc;
import org.eclipse.wst.jsdt.core.ast.ILocalDeclaration;
import org.eclipse.wst.jsdt.core.ast.INumberLiteral;
import org.eclipse.wst.jsdt.core.ast.IObjectLiteral;
import org.eclipse.wst.jsdt.core.ast.IObjectLiteralField;
import org.eclipse.wst.jsdt.core.ast.IProgramElement;
import org.eclipse.wst.jsdt.core.ast.IReturnStatement;
import org.eclipse.wst.jsdt.core.ast.IScriptFileDeclaration;
import org.eclipse.wst.jsdt.core.ast.ISingleNameReference;
import org.eclipse.wst.jsdt.core.ast.IStringLiteral;
import org.eclipse.wst.jsdt.core.ast.IThisReference;
import org.eclipse.wst.jsdt.core.ast.ITrueLiteral;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.wst.jsdt.internal.compiler.ast.Argument;
import org.eclipse.wst.jsdt.internal.compiler.ast.ArrayInitializer;
import org.eclipse.wst.jsdt.internal.compiler.ast.Assignment;
import org.eclipse.wst.jsdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.wst.jsdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.Expression;
import org.eclipse.wst.jsdt.internal.compiler.ast.FieldReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.FunctionExpression;
import org.eclipse.wst.jsdt.internal.compiler.ast.Javadoc;
import org.eclipse.wst.jsdt.internal.compiler.ast.JavadocSingleNameReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.MessageSend;
import org.eclipse.wst.jsdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.ObjectLiteral;
import org.eclipse.wst.jsdt.internal.compiler.ast.OperatorIds;
import org.eclipse.wst.jsdt.internal.compiler.ast.ProgramElement;
import org.eclipse.wst.jsdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.wst.jsdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.wst.jsdt.internal.compiler.util.HashtableOfObject;
import org.eclipse.wst.jsdt.internal.compiler.util.Util;

/**
 * The default inference engine.
 * 
 * <p>Clients may subclass this class but should expect some breakage by future releases.</p>
 * 
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class InferEngine extends ASTVisitor implements IInferEngine {

	InferOptions inferOptions;
	CompilationUnitDeclaration compUnit;
    Context [] contexts=new Context[100];
    int contextPtr=-1;
    Context currentContext=new Context();
    protected int passNumber=1;

    boolean isTopLevelAnonymousFunction;
    int anonymousCount=0;
    
    public InferrenceProvider inferenceProvider;

	public  InferredType StringType=new InferredType(new char[]{'S','t','r','i','n','g'});
	public  InferredType NumberType=new InferredType(new char[]{'N','u','m','b','e','r'});
	public  InferredType BooleanType=new InferredType(new char[]{'B','o','o','l','e','a','n'});
	public  InferredType FunctionType=new InferredType(InferredType.FUNCTION_NAME);
	public  InferredType ArrayType=new InferredType(InferredType.ARRAY_NAME);
	public  InferredType VoidType=new InferredType(new char[]{'v','o','i','d'});
	public  InferredType ObjectType=new InferredType(InferredType.OBJECT_NAME);
	public  InferredType GlobalType=new InferredType(InferredType.GLOBAL_NAME);

	
	public static HashtableOfObject WellKnownTypes=new HashtableOfObject();
	{
		WellKnownTypes.put(InferredType.OBJECT_NAME,null);
		WellKnownTypes.put(InferredType.ARRAY_NAME,null);
		WellKnownTypes.put(new char[]{'S','t','r','i','n','g'},null);
		WellKnownTypes.put(new char[]{'N','u','m','b','e','r'},null);
		WellKnownTypes.put(new char[]{'B','o','o','l','e','a','n'},null);
		WellKnownTypes.put(InferredType.FUNCTION_NAME,null);
		WellKnownTypes.put(new char[]{'D','a','t','e'},null);
		WellKnownTypes.put(new char[]{'M','a','t','h'},null);
		WellKnownTypes.put(new char[]{'R','e','g','E','x','p'},null);
		WellKnownTypes.put(new char[]{'E','r','r','o','r'},null);
	}
	
	
	
	protected InferredType inferredGlobal=null;

	static final char[] CONSTRUCTOR_ID={'c','o','n','s','t','r','u','c','t','o','r'};

	static class Context
	{
		InferredType currentType;
		IFunctionDeclaration currentMethod;
		boolean isJsDocClass;
		
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
			this.isJsDocClass=parent.isJsDocClass;
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
		
		public void setCurrentType(InferredType type)
		{
			this.currentType=type;
			Context parentContext=this.parent;
			
			while (parentContext!=null && parentContext.currentMethod==this.currentMethod)
			{
				parentContext.currentType=type;
				parentContext=parentContext.parent;
			}
		}


	}
	
	private static boolean REPORT_INFER_TIME = false;

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

	public void setCompilationUnit(CompilationUnitDeclaration scriptFileDeclaration) {
		this.compUnit = scriptFileDeclaration;
		buildDefinedMembers(scriptFileDeclaration.getStatements(),null);
	}


	public boolean visit(IFunctionCall functionCall) {
		boolean visitChildren=handleFunctionCall(functionCall);
		if (visitChildren)
		{
			if (this.contextPtr==-1 && functionCall.getReceiver() instanceof FunctionExpression)
				this.isTopLevelAnonymousFunction=true;
		}
		return visitChildren;
	}

	public boolean visit(ILocalDeclaration localDeclaration) {

		//add as a member of the current context
		currentContext.addMember( localDeclaration.getName(), localDeclaration );

		if (localDeclaration.getJsDoc()!=null)
		{
			Javadoc javadoc = (Javadoc)localDeclaration.getJsDoc();
			createTypeIfNecessary(javadoc);
			InferredAttribute attribute = null;
			if (javadoc.memberOf!=null)
			{
				InferredType type = this.addType(javadoc.memberOf.getSimpleTypeName(),true);
				int nameStart = localDeclaration.sourceStart(); 
				attribute = type.addAttribute(localDeclaration.getName(), localDeclaration, nameStart);
				handleAttributeDeclaration(attribute, localDeclaration.getInitialization());
				if (localDeclaration.getInitialization()!=null)
					 attribute.initializationStart=localDeclaration.getInitialization().sourceStart();
				attribute.type=type;
			} 

			if (javadoc.returnType!=null)
			{
			   InferredType type = this.addType(javadoc.returnType.getSimpleTypeName());
			   localDeclaration.setInferredType(type);
			   if (attribute!=null)
				   attribute.type=type;
			}
		}

		if (localDeclaration.getInferredType()==null && localDeclaration.getInitialization()!=null)
		{
			if(localDeclaration.getInitialization() instanceof MessageSend) {
				handleFunctionCall((IFunctionCall)localDeclaration.getInitialization(), (LocalDeclaration) localDeclaration);
			} else {
				localDeclaration.setInferredType(getTypeOf(localDeclaration.getInitialization()));
			}
		}
		return true;
	}

	private void createTypeIfNecessary(Javadoc javadoc) {
		if (javadoc.memberOf!=null)
		{
			char [][]namespace={};
			char[][] typeName = javadoc.memberOf.getTypeName();
			if (javadoc.namespace!=null)
			{
				namespace=javadoc.namespace.getTypeName();
			}
			char [] name=CharOperation.concat(
					CharOperation.concatWith(namespace, '.'),
					CharOperation.concatWith(typeName, '.'),
					'.');
			this.currentContext.currentType=addType(name);
			if (javadoc.extendsType!=null)
			{
				char[] superName = CharOperation.concatWith(javadoc.extendsType.getTypeName(),'.');
				this.currentContext.currentType.superClass=addType(superName);
			}
			this.currentContext.isJsDocClass=true;
			
		}
		
	}

	public boolean visit(IAssignment assignment) {
		pushContext();
		IExpression assignmentExpression=assignment.getExpression();
		if (handlePotentialType(assignment))
		{

		}
		else if (assignmentExpression instanceof FunctionExpression)
		{
			boolean keepVisiting= handleFunctionExpressionAssignment(assignment);
			if (!keepVisiting)
				return false;
		}
		else if (assignmentExpression instanceof SingleNameReference  && this.currentContext.currentType !=null &&
				isThis(assignment.getLeftHandSide()))
		{
			ISingleNameReference snr=(ISingleNameReference)assignmentExpression;
			Object object = this.currentContext.getMember( snr.getToken() );


			IFieldReference fieldReference=(IFieldReference)assignment.getLeftHandSide();
			char [] memberName = fieldReference.getToken();
			InferredMember member = null;
			
			int nameStart = fieldReference.sourceEnd() - memberName.length + 1;

			/*
			 * this.foo = bar //bar is a function
			 */
			if( object instanceof MethodDeclaration ){

				MethodDeclaration method=(MethodDeclaration)object;
				member = this.currentContext.currentType.addMethod(memberName, method, nameStart);

			}
			/*
			 * this.foo = bar //assume that bar is not a function and create a new attribute in the current type
			 */
			else{

				member = this.currentContext.currentType.addAttribute(memberName, assignment, nameStart);
				handleAttributeDeclaration((InferredAttribute) member, assignment.getExpression());
				if (((InferredAttribute) member).type == null)
					((InferredAttribute)member).type = getTypeOf( assignmentExpression );
			}

			//setting location
			if( member != null ){
				member.isStatic = false; //this is a not static member because it is being set on the this
			}
		}

		/*
		 *	foo = {};
		 */
		else if ( assignmentExpression instanceof IObjectLiteral && assignment.getLeftHandSide() instanceof ISingleNameReference ){

			IAbstractVariableDeclaration varDecl = getVariable( assignment.getLeftHandSide() );

			if( varDecl != null ){
				InferredType type = varDecl.getInferredType();

				if( type == null ){
					//create an anonymous type based on the ObjectLiteral
					type = getTypeOf( assignmentExpression );

					varDecl.setInferredType(type);

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
		else if ( assignmentExpression instanceof IObjectLiteral && assignment.getLeftHandSide() instanceof FieldReference ){
			FieldReference fRef = (FieldReference)assignment.getLeftHandSide();

			boolean isKnownName=fRef.receiver.isThis() && isKnownType(fRef.getToken()) && 
					(this.inferredGlobal!=null && this.inferredGlobal==this.currentContext.currentType); 
				
			 if (isKnownName || (this.inferOptions.useAssignments && passNumber == 2 ))
			{


				InferredType receiverType = getInferredType( fRef.receiver );

				if (receiverType==null && this.passNumber==2)
				  receiverType=getInferredType2(fRef.receiver );

				if( receiverType != null ){
					//check if there is an attribute already created

					InferredAttribute attr = receiverType.findAttribute( fRef.getToken() );

					//ignore if the attribute exists and has a type
					if( !(attr != null && attr.type != null) ){

						int nameStart = (int)(fRef.nameSourcePosition>>>32);

						attr = receiverType.addAttribute(fRef.getToken(), assignment, nameStart);
						handleAttributeDeclaration(attr, assignment.getExpression());
						attr.type = getTypeOf( assignmentExpression );

						if (isKnownName && attr.type.isAnonymous)
						{
							InferredType existingType = compUnit.findInferredType( fRef.getToken() ) ;
							if (existingType!=null)
								attr.type=existingType;
							else
							{
								compUnit.inferredTypesHash.removeKey(attr.type.name);
								attr.type.name=fRef.getToken();
								compUnit.inferredTypesHash.put(attr.type.name, attr.type);
							}
						}
						
						
						/*
						 * determine if static
						 *
						 * check if the receiver is a type
						 */
						char [] possibleTypeName = constructTypeName( fRef.receiver );

						if( receiverType.allStatic ||
								(possibleTypeName != null && compUnit.findInferredType( possibleTypeName ) != null ))
							attr.isStatic = true;
						else
							attr.isStatic = false;

						return false; //done with this
					}

				}
			}
		}
		else if ( assignmentExpression instanceof AllocationExpression && 
				((AllocationExpression)assignmentExpression).member instanceof FunctionExpression){
				handleFunctionExpressionAssignment((Assignment)assignment);
			}
		else if ( assignmentExpression instanceof Assignment && 
				((Assignment)assignmentExpression).expression instanceof FunctionExpression){
				handleFunctionExpressionAssignment((Assignment)assignment);
			}
		else
		{
			/*
			 * foo.bar = ?  //? is not {} and not a function
			 */
			if (this.inferOptions.useAssignments)
			{
				if( assignment.getLeftHandSide() instanceof FieldReference ){
					FieldReference fRef = (FieldReference)assignment.getLeftHandSide();
					int nameStart=(int)(fRef.nameSourcePosition>>>32);

					InferredType receiverType = getInferredType( fRef.receiver );
					if (receiverType==null)
					{
					  IFunctionDeclaration function = getDefinedFunction(fRef.receiver);
					  if (function!=null)
					  {
						  char [] typeName = constructTypeName(fRef.receiver);
						  if (typeName!=null)
							  receiverType=addType(typeName);
					  }
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
							IFunctionDeclaration definedFunction=null;
							InferredType exprType = getTypeOf( assignmentExpression );
							if (exprType==null)
								 definedFunction = getDefinedFunction(assignmentExpression );

							if (definedFunction!=null)
							{
								method = receiverType.addMethod(fRef.token, definedFunction, nameStart);
								method.isStatic=receiverType.allStatic;
							}
							else
							{
							  int nameStart_ = (int)(fRef.nameSourcePosition>>>32);
							  
							  attr = receiverType.addAttribute(fRef.token, assignment, nameStart_);
							  handleAttributeDeclaration(attr, assignmentExpression);
							  attr.type=exprType;
							/*
							 * determine if static
							 *
							 * check if the receiver is a type
							 */
							  char [] possibleTypeName = constructTypeName( fRef.receiver );

							  if( receiverType.allStatic||
									  (possibleTypeName != null && compUnit.findInferredType( possibleTypeName ) != null ))
								attr.isStatic = true;
							  else
								attr.isStatic = false;
							}
							return false; //done with this
						}


					}
				}
			}
		}
		return true; // do nothing by default, keep traversing
	}

	protected InferredType getInferredType2(IExpression fieldReceiver)
	{
		InferredType receiverType=null;
		IAbstractVariableDeclaration var=getVariable(fieldReceiver);
		if (var!=null)
		{
			receiverType=createAnonymousType(var);
		}
		else
		{
			if (this.inferredGlobal!=null && fieldReceiver instanceof ISingleNameReference)
			{
				char []name=((ISingleNameReference)fieldReceiver).getToken();
				InferredAttribute attr=this.inferredGlobal.findAttribute(name);
				if (attr!=null)
					receiverType=attr.type;
			}

		}
		return receiverType;
	}

	private InferredType createAnonymousType(IAbstractVariableDeclaration var) {

		InferredType currentType = var.getInferredType();

		if (currentType==null || !currentType.isAnonymous)
		{
			InferredType type=createAnonymousType(var.getName(), currentType);
			var.setInferredType(type);
		}
		return var.getInferredType();
	}

	protected InferredType createAnonymousType(char[] possibleTypeName, InferredType currentType) {
		char []name;
		if (this.isKnownType(possibleTypeName))
		{
			name=possibleTypeName;
		}
		else
		{
			char[] cs = String.valueOf(this.anonymousCount++).toCharArray();
			name = CharOperation.concat(ANONYMOUS_PREFIX,possibleTypeName,cs);
		}
		InferredType type = addType(name,true);
		type.isAnonymous=true;
		if (currentType!=null)
			type.superClass=currentType;
		return type;
	}
	/*
	 * Creates an anonymous type based in the location in the document. This information is used
	 * to avoid creating duplicates because of the 2-pass nature of this engine.
	 */
	private InferredType createAnonymousType( IObjectLiteral objLit ) {

		if (objLit.getInferredType()!=null)
			return objLit.getInferredType();
		//char[] cs = String.valueOf(this.anonymousCount2++).toCharArray();
		char [] loc = (String.valueOf( objLit.sourceStart() ) + '_' + String.valueOf( objLit.sourceEnd() )).toCharArray();
		char []name = CharOperation.concat( ANONYMOUS_PREFIX, ANONYMOUS_CLASS_ID, loc );

		InferredType anonType = addType(name,true);
		anonType.isAnonymous=true;
		anonType.isObjectLiteral=true;
		anonType.superClass = ObjectType;

		anonType.sourceStart = objLit.sourceStart();
		anonType.sourceEnd = objLit.sourceEnd();

		populateType( anonType, objLit , false);

		return anonType;
	}

	
	/**
	 * handle the inferrencing for an assigment whose right hand side is a function expression
	 * @param the assignment AST node
	 * @return true if handled
	 */
	protected boolean handleFunctionExpressionAssignment(IAssignment assignment)
	{
		IFunctionExpression functionExpression=null;
		if (assignment.getExpression() instanceof IFunctionExpression)
			functionExpression=(IFunctionExpression)assignment.getExpression();
		else if (assignment.getExpression() instanceof IAllocationExpression)
			functionExpression=(IFunctionExpression)((IAllocationExpression)assignment.getExpression()).getMember();
		else if (assignment.getExpression() instanceof IAssignment)
			functionExpression=(FunctionExpression)((IAssignment)assignment.getExpression()).getExpression();
		MethodDeclaration methodDeclaration = functionExpression.getMethodDeclaration();

		char [] possibleTypeName = constructTypeName( assignment.getLeftHandSide() );

		InferredType type = null;
		if( possibleTypeName != null )
		{
			type = compUnit.findInferredType( possibleTypeName );
			if (type==null && isPossibleClassName(possibleTypeName))
			{
				type=addType(possibleTypeName,true);
			}
			if (type==null && methodDeclaration.getJsDoc()!=null && ((Javadoc)methodDeclaration.getJsDoc()).isConstructor)
			{
				type=addType(possibleTypeName,true);
				handleJSDocConstructor(type, methodDeclaration, assignment.sourceStart());
			}
		}

		if (type!=null)	// isConstructor
		{
			if (this.inferOptions.useInitMethod)
			{
				this.currentContext.currentType=type;
				type.isDefinition=true;
				int nameStart = assignment.getLeftHandSide().sourceStart();
				InferredMethod method = type.addConstructorMethod(type.name, methodDeclaration, nameStart);
				type.updatePositions(assignment.getLeftHandSide().sourceStart(), assignment.getExpression().sourceEnd());
			}

		}
		else	// could be method
		{
			if (assignment.getLeftHandSide() instanceof FieldReference)
			{
				FieldReference fieldReference=(FieldReference)assignment.getLeftHandSide();
				int nameStart=(int)(fieldReference.nameSourcePosition>>>32);

				InferredType receiverType = getInferredType( fieldReference.receiver );

				if( receiverType != null ){

					//check if there is a member method already created
					InferredMethod method = receiverType.findMethod( fieldReference.token, methodDeclaration );

					if( method == null ){
						//create member method if it does not exist

						method = receiverType.addMethod(fieldReference.token, methodDeclaration, nameStart);
						receiverType.updatePositions(assignment.sourceStart(), assignment.sourceEnd()); // @GINO: not sure if necessary
						receiverType.isDefinition=true;

						/*
						 * determine if static
						 *
						 * check if the receiver is a type
						 */
						char [] possibleInTypeName = constructTypeName( fieldReference.receiver );

						if( receiverType.allStatic ||  
								(possibleInTypeName != null && compUnit.findInferredType( possibleInTypeName ) != null) )
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
						InferredMethod method = receiverType.addMethod(fieldReference.token, methodDeclaration, nameStart);
						method.isStatic=receiverType.isAnonymous;
						receiverType.updatePositions(assignment.sourceStart(), assignment.sourceEnd());
					}
				}
			}
			else if (assignment.getLeftHandSide() instanceof SingleNameReference)
			{
			}
		}
		return true;
	}
	
	
	/**
	 * @param assignment
	 * @return whether a type was not created for this assignment
	 */
	protected boolean handlePotentialType(IAssignment assignment) {

		IExpression lhs = assignment.getLeftHandSide();
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
				char [] possibleTypeName = constructTypeName( fieldReference.getReceiver() );
				if( possibleTypeName != null )
					newType = compUnit.findInferredType( possibleTypeName );
				else
					return true; //no type created


				//create the new type if not found
				if( newType == null ){
					newType = addType( possibleTypeName ,true);
				}
				newType.isDefinition=true;

//				char[] typeName = getTypeName(fieldReference.receiver);
//				Object object = currentContext.definedMembers.get(typeName);
//
//				if (object instanceof Argument)
//					return false;

				newType.updatePositions(assignment.sourceStart(), assignment.sourceEnd());

				/*
				 * foo.prototype = new ...
				 */
				if (assignment.getExpression() instanceof IAllocationExpression)
				{
					//setting the super type
					IAllocationExpression allocationExpression =(IAllocationExpression)assignment.getExpression();

					InferredType superType = null;
					char [] possibleSuperTypeName = constructTypeName( allocationExpression.getMember() );
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
				else if( assignment.getExpression() instanceof IObjectLiteral ){
					//rather than creating an anonymous type, is better just to set the members directly
					//on newType
					populateType( newType, (IObjectLiteral)assignment.getExpression(),false );

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
				}
				newType.isDefinition = true;

//				char[] typeName = getTypeName(prototype.receiver);
//				Object receiverDef = currentContext.definedMembers.get(typeName);
//				if (receiverDef instanceof Argument)
//					return false;
//				InferredType newType = addType(typeName);
//				newType.isDefinition=true;

				newType.updatePositions(assignment.sourceStart(), assignment.sourceEnd());

				//prevent Object literal based anonymous types from being created more than once
				if( passNumber == 1 && assignment.getExpression() instanceof IObjectLiteral ){
					return false;
				}

				char[] memberName = fieldReference.token;
				int nameStart= (int)(fieldReference.nameSourcePosition >>> 32);

				InferredType typeOf = (assignment.getJsDoc() != null && assignment.getJsDoc() instanceof Javadoc && ((Javadoc) assignment.getJsDoc()).returnType != null) ? this.addType(changePrimitiveToObject(((Javadoc) assignment.getJsDoc()).returnType.getSimpleTypeName())) : getTypeOf(assignment.getExpression());
				IFunctionDeclaration methodDecl=null;

				if (typeOf==null || typeOf==FunctionType)
					methodDecl=getDefinedFunction(assignment.getExpression());

				if (methodDecl!=null)
				{
					InferredMember method = newType.addMethod(memberName, methodDecl, nameStart);
				}
				// http://bugs.eclipse.org/269053 - constructor property not supported in JSDT
				else /*if (!CharOperation.equals(CONSTRUCTOR_ID, memberName))*/
				{
					InferredAttribute attribute = newType.addAttribute(memberName, assignment, nameStart);
					handleAttributeDeclaration(attribute, assignment.getExpression());
					attribute.initializationStart=assignment.getExpression().sourceStart();
					if (attribute.type==null)
						attribute.type=typeOf;
				}
				return true;
			} else if(fieldReference.receiver instanceof IThisReference) {
				InferredType newType = null;
				
				IFunctionDeclaration parentMethod = this.currentContext.currentMethod;
				if( parentMethod != null && parentMethod.getName() != null )
					newType = compUnit.findInferredType( parentMethod.getName() );
				else
					return false; //no type created

				//create the new type if not found
				if( newType == null ){
					newType = addType( parentMethod.getName() );
				}
				newType.isDefinition = true;

				newType.updatePositions(assignment.sourceStart(), assignment.sourceEnd());

				//prevent Object literal based anonymous types from being created more than once
				if( passNumber == 1 && assignment.getExpression() instanceof IObjectLiteral ){
					return false;
				}

				char[] memberName = fieldReference.token;
				int nameStart= (int)(fieldReference.nameSourcePosition >>> 32);

				InferredType typeOf = getTypeOf(assignment.getExpression());
				IFunctionDeclaration methodDecl=null;

				if (typeOf==null || typeOf==FunctionType)
					methodDecl=getDefinedFunction(assignment.getExpression());

				if (methodDecl!=null)
				{
					InferredMember method = newType.addMethod(memberName, methodDecl, nameStart);
				}
				// http://bugs.eclipse.org/269053 - constructor property not supported in JSDT
				else /*if (!CharOperation.equals(CONSTRUCTOR_ID, memberName))*/
				{
					InferredAttribute attribute = newType.addAttribute(memberName, assignment, nameStart);
					handleAttributeDeclaration(attribute, assignment.getExpression());
					attribute.initializationStart=assignment.getExpression().sourceStart();
					if (attribute.type==null)
						attribute.type=typeOf;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the function referenced by the expression
	 * 
	 * @param expression AST node
	 * @return the function or null
	 */
	protected IFunctionDeclaration getDefinedFunction(IExpression expression)
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
					return method.getFunctionDeclaration();
			}

		 }

		return null;

	}

	protected InferredType getTypeOf(IExpression expression) {
		if (expression instanceof IStringLiteral) {
			return StringType;
		}
		else if (expression instanceof INumberLiteral) {
			return NumberType;
		}
		else if (expression instanceof IAllocationExpression)
		{
			IAllocationExpression allocationExpression=(IAllocationExpression)expression;

			InferredType type = null;
			char [] possibleTypeName = constructTypeName( allocationExpression.getMember() );
			if( possibleTypeName != null ){
				type = compUnit.findInferredType( possibleTypeName );

				if( type == null )
					type = addType( possibleTypeName );

				return type;
			}
		}
		else if (expression instanceof ISingleNameReference)
		{
			IAbstractVariableDeclaration varDecl = getVariable( expression );
			if( varDecl != null )
				return varDecl.getInferredType();
			
			if (this.inferredGlobal!=null)
			{
				InferredAttribute attribute = this.inferredGlobal.findAttribute(((ISingleNameReference)expression).getToken() );
				if (attribute!=null)
					return attribute.type;
			}

		}
		else if (expression instanceof FieldReference)
		{
			FieldReference fieldReference=(FieldReference)expression;
			if (fieldReference.receiver.isThis() && currentContext.currentType!=null)
			{
				InferredAttribute attribute = currentContext.currentType.findAttribute(fieldReference.getToken());
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
		}	else if (expression instanceof ITrueLiteral || expression instanceof IFalseLiteral) {
			return BooleanType;
		}
		else if ( expression instanceof IObjectLiteral ){

			//create an annonymous type based on the ObjectLiteral
			InferredType type = createAnonymousType( (IObjectLiteral)expression);

			//set the start and end
			type.sourceStart = expression.sourceStart();
			type.sourceEnd = expression.sourceEnd();

			return type;


		} 	else if ( expression instanceof IThisReference ){
			return this.currentContext.currentType;
		}
		else if (expression instanceof Assignment)
			return getTypeOf(((Assignment)expression).getExpression());
		else if (expression instanceof FunctionExpression)
			return FunctionType;
		else if(expression instanceof UnaryExpression) {
			return getTypeOf(((UnaryExpression)expression).expression);
		} else if(expression instanceof BinaryExpression) {
			BinaryExpression bExpression = (BinaryExpression) expression;
			int operator = (bExpression.bits & ASTNode.OperatorMASK) >> ASTNode.OperatorSHIFT;
			switch(operator) {
				case OperatorIds.MULTIPLY :
				case OperatorIds.DIVIDE :
				case OperatorIds.REMAINDER :
				case OperatorIds.MINUS:
				case OperatorIds.LEFT_SHIFT:
				case OperatorIds.RIGHT_SHIFT:
					return NumberType;
				case OperatorIds.PLUS:
					InferredType leftType = getTypeOf(bExpression.left);
					InferredType rightType = getTypeOf(bExpression.right);
					if(leftType != null && leftType.equals(StringType))
						return StringType;
					if(rightType != null && rightType.equals(StringType))
						return StringType;
					if(leftType == null || rightType == null)
						return null;
					if(leftType.equals(StringType) || rightType.equals(StringType)) {
						return StringType;
					} else if(leftType.equals(NumberType) && rightType.equals(NumberType)) {
						return NumberType;
					}
					return null;
				case OperatorIds.EQUAL_EQUAL:
				case OperatorIds.EQUAL_EQUAL_EQUAL:
				case OperatorIds.NOT_EQUAL:
				case OperatorIds.NOT_EQUAL_EQUAL:
				case OperatorIds.GREATER:
				case OperatorIds.GREATER_EQUAL:
				case OperatorIds.LESS:
				case OperatorIds.LESS_EQUAL:
				case OperatorIds.INSTANCEOF:
				case OperatorIds.IN:
				case OperatorIds.AND_AND:
				case OperatorIds.OR_OR:
					return BooleanType;
				default:
					return null;
			}
		}

		return null;
	}

	protected void populateType(InferredType type, IObjectLiteral objLit, boolean isStatic) {
		if (objLit.getInferredType()==null) {
			objLit.setInferredType(type);
			if (objLit.getFields() != null) {
				for (int i = 0; i < objLit.getFields().length; i++) {
					IObjectLiteralField field = objLit.getFields()[i];

					char[] name = null;
					int nameStart = -1;

					if (field.getFieldName() instanceof SingleNameReference) {
						SingleNameReference singleNameReference = (SingleNameReference) field.getFieldName();
						name = singleNameReference.token;
						nameStart = singleNameReference.sourceStart;
					} else if (field.getFieldName() instanceof IStringLiteral) {
						IStringLiteral stringLiteral = (IStringLiteral) field.getFieldName();
						name = stringLiteral.source();
						nameStart = stringLiteral.sourceStart();
					} else
						continue; //not supporting this case right now

					Javadoc javaDoc = (Javadoc)field.getJsDoc();
					InferredType returnType=null;
					if (javaDoc!=null)
					{
						if (javaDoc.memberOf!=null)
						{
							char[] typeName = javaDoc.memberOf.getSimpleTypeName();
							convertAnonymousTypeToNamed(type,typeName);
							type.isDefinition=true;
						}
						else if (this.currentContext.isJsDocClass && javaDoc.property!=null)
						{
							if (type.isAnonymous )
							{
								InferredType previousType = this.currentContext.currentType;
								if (previousType!=null)
								{
									copyAnonymousTypeToNamed(type,previousType);
									objLit.setInferredType(type = this.currentContext.currentType = previousType);
								}
									
							}
						}
						if (javaDoc.returnType!=null)
						{
							returnType=this.addType(javaDoc.returnType.getSimpleTypeName());
						}
					}
					
					//need to build the members of the annonymous inferred type
					if (field.getInitializer() instanceof IFunctionExpression) {
						IFunctionExpression functionExpression = (IFunctionExpression) field.getInitializer();
						InferredMember method = type.addMethod(name,
								functionExpression.getMethodDeclaration(), nameStart);
						method.isStatic=isStatic;
						if (javaDoc!=null)
						{
							functionExpression.getMethodDeclaration().modifiers=javaDoc.modifiers;
						}
						handleFunctionDeclarationArguments(functionExpression.getMethodDeclaration(),javaDoc);
					    if (returnType!=null && functionExpression.getMethodDeclaration().getInferredType() == null)
					    {
					    	functionExpression.getMethodDeclaration().setInferredType(returnType);
					    }


					} else //attribute
					{
						InferredAttribute attribute = type.findAttribute(name);
						if (attribute == null) {
							attribute = type.addAttribute(name, field.getInitializer(), nameStart);
							handleAttributeDeclaration(attribute, field.getInitializer());
							attribute.isStatic=isStatic;
							//@GINO: recursion might not be the best idea
							if (returnType!=null)
								attribute.type=returnType;
							else
							  attribute.type = getTypeOf(field.getInitializer());
						}
					}
				}
			}
		}		
	}

	public void endVisit(IAssignment assignment) {
		popContext();
	}
	
	protected boolean handleAttributeDeclaration(InferredAttribute attribute, IExpression initializer) {
		return true;
	}

	protected boolean handleFunctionCall(IFunctionCall messageSend) {
		return handleFunctionCall(messageSend, null);
	}
	
	protected boolean handleFunctionCall(IFunctionCall messageSend, LocalDeclaration assignmentExpression) {
		return true;
	}

	public void endVisit(IReturnStatement returnStatement) {

//			if (currentContext.currentMethod!=null)
//			{
//				if (returnStatement.getExpression()!=null)
//				{
//
//					InferredType type = getTypeOf(returnStatement.getExpression());
//
//					if (currentContext.currentMethod.inferredType==VoidType)
//						currentContext.currentMethod.inferredType=type;
//					else if (type==null || !type.equals(currentContext.currentMethod.inferredType))
//						currentContext.currentMethod.inferredType=null;
//				}
//			}

	}
	

	public boolean visit(IReturnStatement returnStatement) {

			if (currentContext.currentMethod!=null)
			{
				if (returnStatement.getExpression()!=null)
				{

					InferredType type = null;
					IExpression expression = returnStatement.getExpression();
					if (expression instanceof IObjectLiteral)
					{
						type = createAnonymousType( (ObjectLiteral)expression);

						//set the start and end
						type.sourceStart = expression.sourceStart();
						type.sourceEnd = expression.sourceEnd();
					}
					else 
						type=getTypeOf(expression);

					if (currentContext.currentMethod.getInferredType()==VoidType)
						currentContext.currentMethod.setInferredType(type);
					else if ((type==null || !type.equals(currentContext.currentMethod.getInferredType())) && 
							!((MethodDeclaration)currentContext.currentMethod).isInferredJsDocType())
						currentContext.currentMethod.setInferredType(null);
				}
			}
			return false;
	}


	public void endVisit(IFunctionDeclaration methodDeclaration) {
		popContext();
	}

	public boolean visit(IFunctionDeclaration methodDeclaration) {
		pushContext();
		if (this.isTopLevelAnonymousFunction && this.currentContext.currentType==null)
		{
			this.currentContext.currentType=addType(InferredType.GLOBAL_NAME,true);
			this.inferredGlobal=this.currentContext.currentType;
		}

		this.isTopLevelAnonymousFunction=false; 
		char[] methodName = methodDeclaration.getName();
		if (passNumber==1)
		{
			buildDefinedMembers((ProgramElement[])methodDeclaration.getStatements(),(Argument[])methodDeclaration.getArguments());
			if (methodDeclaration.getJsDoc()!=null)
			{
				InferredMethod method=null;
				Javadoc javadoc = (Javadoc)methodDeclaration.getJsDoc();
				createTypeIfNecessary(javadoc);
				if (javadoc.isConstructor)
				{
					InferredType type;
					if (!this.currentContext.isJsDocClass && methodName!=null)
					  type = this.addType(methodName);
					else
						type=this.currentContext.currentType;
					if (type!=null)
						handleJSDocConstructor(type, methodDeclaration, methodDeclaration.sourceStart());
				}
				else if (javadoc.memberOf!=null)
				{
					InferredType type = this.addType(javadoc.memberOf.getSimpleTypeName(),true);
					char [] name=methodName;
					int nameStart = methodDeclaration.sourceStart();
					if (name!=null)
						method=type.addMethod(methodName, methodDeclaration, nameStart);
				}
				else if (javadoc.methodDef!=null && this.currentContext.isJsDocClass)
				{
					InferredType type=this.currentContext.currentType;
					char[][] methName = javadoc.methodDef.getTypeName();
					int nameStart = ((MethodDeclaration)methodDeclaration).sourceStart;
					if (methName.length==1)
						method=type.addMethod(methName[0], methodDeclaration, nameStart);
					else
					{
						method=type.addMethod(methName[methName.length-1], methodDeclaration, nameStart);
						method.isStatic=true;
					}
						
				}

				if (javadoc.returnType!=null)
				{
				   InferredType type = this.addType(changePrimitiveToObject(javadoc.returnType.getSimpleTypeName()));
				   methodDeclaration.setInferredType(type);
				   ((MethodDeclaration)methodDeclaration).bits |= ASTNode.IsInferredJsDocType;
				}

			}
			handleFunctionDeclarationArguments((MethodDeclaration)methodDeclaration,(Javadoc)methodDeclaration.getJsDoc());
		}
		// check if this is a constructor
		if (passNumber==2)
		{

			if (methodName!=null) {
				InferredType type = compUnit
						.findInferredType(methodName);
				if (type != null) {
					this.currentContext.currentType = type;
					type.isDefinition = true;
					int nameStart = methodDeclaration.sourceStart();
					InferredMethod method = type.addConstructorMethod(methodName, methodDeclaration, nameStart);
					method.isConstructor = true;
					methodDeclaration.setInferredType(type);
				}
			}
		}
		this.currentContext.currentMethod=(MethodDeclaration)methodDeclaration;
		if (methodDeclaration.getInferredMethod()!=null && methodDeclaration.getInferredMethod().inType!=null)
			this.currentContext.currentType=methodDeclaration.getInferredMethod().inType;
		if (methodDeclaration.getInferredType()==null)
			methodDeclaration.setInferredType(VoidType);
		return true;
	}
	
	protected void handleJSDocConstructor(InferredType type,IFunctionDeclaration methodDeclaration, int nameStart) {
		Javadoc javadoc = (Javadoc)methodDeclaration.getJsDoc();
		type.isDefinition=true;
		InferredMethod method = type.addConstructorMethod(type.name, methodDeclaration, nameStart);
		method.isConstructor=true;

		if (javadoc.extendsType!=null)
		{
			InferredType superType=this.addType(javadoc.extendsType.getFullTypeName());
			type.superClass=superType;
		}
		
	}

	protected void handleFunctionDeclarationArguments(IFunctionDeclaration methodDeclaration, IJsDoc jsdoc) {
		if (jsdoc==null || !(jsdoc instanceof Javadoc))
			return;
		Javadoc javadoc = (Javadoc) jsdoc;
		
		IArgument[] arguments = methodDeclaration.getArguments();
		if (arguments!=null)
		for (int i = 0; i < arguments.length; i++) {
				if (arguments[i].getInferredType() != null)
					continue;

			JavadocSingleNameReference param = javadoc.findParam(arguments[i].getName());
			if (param!=null)
			{
				if (param.types!=null)
				{
					char []name={};
					for (int j = 0; j < param.types.length; j++) {
						//char []typeName=param.types[j].getSimpleTypeName();
						//make sure we are using the type version of Boolean, even if the user entered boolean as the JSdoc type.
						char []typeName=changePrimitiveToObject(param.types[j].getSimpleTypeName());
						if (j==0)
							name=typeName;
						else
						{
							name=CharOperation.append(name, '|');
							name=CharOperation.concat(name, typeName);
						}
					}
					InferredType paramType=this.addType(name);
					arguments[i].setInferredType(paramType);
				}
			}
		}
	}



	public boolean visit(
    		IAllocationExpression allocationExpression) {

		InferredType type = null;
		char [] possibleTypeName = constructTypeName( allocationExpression.getMember() );
		if( possibleTypeName != null ){
			type = compUnit.findInferredType( possibleTypeName );

			if( type == null  )
				type = addType( possibleTypeName ); //creating type
		}
		return true;
	}


	public void endVisit(IObjectLiteralField field) {
//		if (field.getJsDoc()!=null)
//		{
//			Javadoc javaDoc = (Javadoc)field.getJsDoc();
//			InferredType inClass=this.currentContext.currentType;
//			char [] name=null;
//			int nameStart=-1;
//			InferredType returnType=null;
////			boolean isFunction=field.initializer instanceof FunctionExpression;
// 			if (field.getFieldName() instanceof SingleNameReference)
// 			{
// 				SingleNameReference singleNameReference=(SingleNameReference)field.getFieldName();
// 				name=singleNameReference.token;
// 				nameStart=singleNameReference.sourceStart;
// 			}
//			if (javaDoc.memberOf!=null)
//			{
//				char[] typeName = javaDoc.memberOf.getSimpleTypeName();
//				convertAnonymousTypeToNamed(inClass,typeName);
//				inClass.isDefinition=true;
//			}
//			else if (this.currentContext.isJsDocClass && javaDoc.property!=null)
//			{
//				if (this.currentContext.currentType.isAnonymous && this.currentContext.parent!=null)
//				{
//					InferredType previousType = this.currentContext.parent.currentType;
//					if (previousType!=null)
//					{
//						copyAnonymousTypeToNamed(inClass,previousType);
//						this.currentContext.currentType=previousType;
//					}
//						
//				}
//			}
//			if (javaDoc.returnType!=null)
//			{
//				returnType=this.addType(javaDoc.returnType.getSimpleTypeName());
//			}
//
//			if (inClass!=null && name!=null)
//			{
//				if (field.getInitializer() instanceof FunctionExpression) {
//					FunctionExpression functionExpression = (FunctionExpression) field.getInitializer();
//				    InferredMember method = inClass.addMethod(name, functionExpression.methodDeclaration,false);
//				    method.nameStart=nameStart;
//				    functionExpression.methodDeclaration.modifiers=javaDoc.modifiers;
//				    if (returnType!=null)
//				    {
//				    	functionExpression.methodDeclaration.inferredType=returnType;
//				    }
////				    else
////				    	method.inferredType=functionExpression.methodDeclaration.inferredType;
//				}
//				else	//attribute
//				{
//					InferredAttribute attribute = inClass.addAttribute(name, field.getFieldName());
//					attribute.nameStart=field.getFieldName().sourceStart();
//					if (returnType!=null)
//						attribute.type=returnType;
//				}
//			}
//
//		}
//		//no jsdoc
//		else{
//
//			if( field.getInitializer() instanceof ObjectLiteral ){
//
//			}
//
//
//		}
	}

	private void copyAnonymousTypeToNamed(InferredType inClass,
			InferredType toType) {
		if (toType==null)return;
		
		compUnit.inferredTypesHash.removeKey(inClass.name);
		if (inClass.methods!=null)
		{
			toType.methods.addAll(inClass.methods);
//			else 
//				toType.methods=inClass.methods;
			
		}
		if (inClass.attributes!=null)
		{
			for (int i = 0; i < inClass.numberAttributes; i++) {
				toType.addAttribute(inClass.attributes[i]);
			}
		}
		
		
	}

	private void convertAnonymousTypeToNamed(InferredType inClass, char[] typeName) {
		if (inClass.isAnonymous)
		{
			inClass.isAnonymous=false;
			compUnit.inferredTypesHash.removeKey(inClass.name);
			inClass.name=typeName;
			compUnit.inferredTypesHash.put(typeName,inClass);

		}
	
	}

	protected boolean isMatch(IExpression expr,char[] [] names, int index)
	{
		char [] matchName=names[index];
		if (expr instanceof SingleNameReference) {
			SingleNameReference snr = (SingleNameReference) expr;
			return CharOperation.equals(snr.token, matchName);
		}
		else if (expr instanceof FieldReference && names.length>1 && index>0) {
			FieldReference fieldReference = (FieldReference) expr;
			if (CharOperation.equals(fieldReference.token, matchName))
				return isMatch(fieldReference.receiver, names, index-1);

		}
		return false;
	}

	protected boolean isFunction(IFunctionCall messageSend,String string) {
		String []names=string.split("\\."); //$NON-NLS-1$
		char [] functionName=names[names.length-1].toCharArray();
		if (!CharOperation.equals(functionName, messageSend.getSelector()))
			return false;

		char [][]namesChars=new char[names.length][];
		for (int i = 0; i < namesChars.length; i++) {
			namesChars[i]=names[i].toCharArray();
		}
		if (names.length>1)
			return isMatch(messageSend.getReceiver(), namesChars, namesChars.length-2);
		return true;
	}

	protected boolean isFunction(IFunctionCall messageSend,char [][]names) {
		char [] functionName=names[names.length-1];
		if (!CharOperation.equals(functionName, messageSend.getSelector()))
			return false;

		if (names.length>1)
			return isMatch(messageSend.getReceiver(), names, names.length-2);
		return true;
	}


	public void doInfer()
	{
		try {
			long time0 = 0;
			if (REPORT_INFER_TIME) {
				time0 = System.currentTimeMillis();
			}
				
			compUnit.traverse(this );
			passNumber=2;
			compUnit.traverse(this );
			for (int i = 0; i < compUnit.numberInferredTypes; i++) {
				if (compUnit.inferredTypes[i].sourceStart<0)
					compUnit.inferredTypes[i].sourceStart=0;
			}

			if (REPORT_INFER_TIME) {
				long time = System.currentTimeMillis() - time0;
				System.err.println(getClass().getName() + " inferred " + new String(compUnit.getFileName()) + " in " + time + "ms");
			}
			this.compUnit=null;

		} catch (RuntimeException e) {
			org.eclipse.wst.jsdt.internal.core.util.Util.log(e, "error during type inferencing");
		}
}

	protected InferredType addType(char[] className) {
		return addType(className,false);
	}

	/**
	 * Create a new inferred type with the given name
	 * 
	 * @param className the name of the inferred type
	 * @param isDefinition true if this unit defines the type
	 * @return new Inferred type
	 */
	protected InferredType addType(char[] className, boolean isDefinition) {
		InferredType type = compUnit.addType(className, isDefinition, this.inferenceProvider.getID());

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
		return this.currentContext.currentMethod!=null && this.currentContext.currentMethod.getName()!=null;
	}


	/**
	 * Finds a Var Declaration on the context from the name represented with the expression
	 *
	 * Currently, only SNR are supported
	 */
	protected IAbstractVariableDeclaration getVariable(IExpression expression)
	{
		char [] name=null;

		if (expression instanceof ISingleNameReference)
			name = ((ISingleNameReference) expression).getToken();
		else if (expression instanceof IFieldReference)
			name = ((IFieldReference) expression).getToken();
		if (name!=null)
		{
			Object var = this.currentContext.getMember( name );
			if (var instanceof IAbstractVariableDeclaration)
				return (IAbstractVariableDeclaration)var;

		}
		return null;

	}

	/**
	 * Finds a Function Declaration on the context from the name represented with the expression
	 *
	 * Currently, only SNR are supported
	 */
	protected IAbstractFunctionDeclaration getFunction(IExpression expression)
	{
		char [] name=null;

		if (expression instanceof ISingleNameReference)
			name = ((ISingleNameReference) expression).getToken();
		else if (expression instanceof IFieldReference)
			name = ((IFieldReference) expression).getToken();
		if (name!=null)
		{
			Object method = this.currentContext.getMember( name );
			if (method instanceof IAbstractFunctionDeclaration)
				return (IAbstractFunctionDeclaration)method;

		}
		return null;
	}

	private void buildDefinedMembers(IProgramElement[] statements, IArgument[] arguments) {

		if (arguments!=null)
		{
			for (int i = 0; i < arguments.length; i++) {
				this.currentContext.addMember( arguments[i].getName(), arguments[i] );
			}
		}
		if (statements!=null)
		{
			for (int i = 0; i < statements.length; i++) {
				if (statements[i] instanceof ILocalDeclaration) {
					ILocalDeclaration local = (ILocalDeclaration) statements[i];
					this.currentContext.addMember( local.getName(), local );
				}
				else if (statements[i] instanceof IAbstractFunctionDeclaration) {
					IAbstractFunctionDeclaration method = (IAbstractFunctionDeclaration) statements[i];
					if (method.getName()!=null)
						this.currentContext.addMember( method.getName(), method );
				}
			}
		}
	}

	private static boolean isThis(IExpression expression)
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
		if( expression instanceof IThisReference ){
			if (this.passNumber==2 && this.currentContext.currentType==null)
			{
				char [] possibleTypeName={'g','l','o','b','a','l'};
				if (this.currentContext.currentMethod!=null)
					possibleTypeName=this.currentContext.currentMethod.getName();
				this.currentContext.setCurrentType(createAnonymousType(possibleTypeName, null));
			}
			
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

				if (type==null)
				{
					if (WellKnownTypes.containsKey(possibleTypeName)) 
					{
						type = addType(possibleTypeName,true);
					}
					else if (/*this.passNumber==2 && */this.isKnownType(possibleTypeName))
					{
						type = addType(possibleTypeName,true);
//						if (type!=null)
//						{
//							AbstractVariableDeclaration varDecl = getVariable( (expression) );
//
//							if( varDecl != null ){
//								varDecl.inferredType=type;
//							}
//							
//						}
					}
					 
				}
				
				
				/*
				 * There is no match for a type with the name, check if the name refers to
				 * var decl and return its type
				 */
				if( type == null ){

					IAbstractVariableDeclaration varDecl = getVariable( expression );

					if( varDecl != null ){
						type = varDecl.getInferredType(); //could be null
						if (type!=null && !type.isAnonymous) {
							if(varDecl.getInitialization() instanceof IAllocationExpression && !type.isFunction()) {
								type = createAnonymousType(varDecl);
							} else {
								InferredType superType = type;
								type = addType(varDecl.getName(), true);
								type.superClass = superType;
							}
							type.updatePositions(varDecl.sourceStart(), varDecl.sourceEnd());
						}
							
					}

				}
			}
		}
		/*
		 * foo.bar.xxx...
		 */
		else if( expression instanceof FieldReference ){
			char[] possibleTypeName = constructTypeName(expression);

			if (possibleTypeName != null)
				// search the defined types in the context
				type = compUnit.findInferredType(possibleTypeName);
			
			if (type==null && isPossibleClassName(possibleTypeName))
			{
				type = addType(possibleTypeName,true);
			}

			/*
			 * Continue the search by trying to resolve further down the name
			 * because this token of the field reference could be a member of a
			 * type or instance of a type
			 */
			if (type == null) {
				FieldReference fRef = (FieldReference) expression;

				// this
				InferredType parentType = getInferredType(fRef.receiver);

				if (parentType != null) {
					// check the members and return type
					InferredAttribute typeAttribute = parentType
							.findAttribute(fRef.token);

					if (typeAttribute != null) {
						type = typeAttribute.type;
						if (type != null && !type.isAnonymous) {
							if (possibleTypeName==null)
								possibleTypeName=typeAttribute.name;
							type = createAnonymousType(possibleTypeName, type);
							typeAttribute.type = type;
						}
					}
				}
			}
		 
		}

		return type;
	}



	protected boolean isKnownType(char[] possibleTypeName) {
		return false;
	}

	/*
	 * For SNR it returns the name
	 * For FR it construct a Qualified name separated by '.'
	 *
	 * If at any point it hits a portion of the Field reference that is
	 * not supported (such as a function call, a prototype, or this )
	 */
	protected final char [] constructTypeName( IExpression expression ){

		return Util.getTypeName( expression );
	}

	public boolean visit(IObjectLiteral literal) {
		if (this.passNumber==1 && literal.getInferredType()==null)
			createAnonymousType((ObjectLiteral)literal);
		pushContext();
		this.currentContext.currentType=literal.getInferredType();
		return true;
	}

	public void endVisit(IObjectLiteral literal) {
		popContext();
	}
	

	/**
	 * Overriden by client who wish to update the infer options
	 * 
	 * @param options
	 */
	public void initializeOptions(InferOptions options) {
	}
	
	protected boolean isPossibleClassName(char[]name)
	{
		return false;
	}
	
	/**
	 * Get the Script file this inferrence is being done on
	 * 
	 * @return
	 */
	public IScriptFileDeclaration getScriptFileDeclaration()
	{
		return this.compUnit;
	}
	
	public InferredType findDefinedType(char [] className)
	{
		return compUnit.findInferredType(className);
	}
	
	protected char[] changePrimitiveToObject(char[] name) {
		//Changes the first character of the name of the primitive types to uppercase. This will allow future reference to the object wrapper instead of the primitive type.
		if(CharOperation.equals(name, TypeConstants.BOOLEAN, false)) //$NON-NLS-1$
			return BooleanType.getName();
		return name;
	}

}
