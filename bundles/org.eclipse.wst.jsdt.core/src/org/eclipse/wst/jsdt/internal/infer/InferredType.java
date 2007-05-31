package org.eclipse.wst.jsdt.internal.infer;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.wst.jsdt.internal.compiler.impl.Constant;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.BlockScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ClassScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ProblemBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.wst.jsdt.internal.compiler.problem.ProblemSeverities;
import org.eclipse.wst.jsdt.internal.compiler.util.HashtableOfObject;

public class InferredType extends ASTNode {
  
	char [] name;
	public ArrayList methods;
	public InferredAttribute[] attributes=new InferredAttribute[5];
	public int numberAttributes=0;
	HashtableOfObject attributesHash = new HashtableOfObject();
	public InferredType superClass;
	
	public InferredType referenceClass;

	public SourceTypeBinding binding;
	public boolean isDefinition;
	private TypeBinding resolvedType;
	public ClassScope scope;
	ReferenceBinding resolvedSuperType;
	
	public boolean isAnonymous=false;
	
	
	public final static char[] OBJECT_NAME=new char[]{'O','b','j','e','c','t'};
	public final static char[] ANONYMOUS_PREFIX=new char[]{'_','_','_'};

	public final static char[] ARRAY_NAME=new char[]{'A','r','r','a','y'};

	
	public InferredType(char [] className)
	{
		this.name=className;
	}

	public char [] getName() {
		return name;
	}
	
	public char [] getSuperClassName()
	{
		return superClass!=null ? superClass.getName() : OBJECT_NAME;
	}
	public InferredAttribute addAttribute(char [] name, ASTNode definer)
	{
		InferredAttribute attribute = findAttribute(name);
		if (attribute==null) 
		{
			attribute=new InferredAttribute(name,null,definer.sourceStart,definer.sourceEnd);
 
			if (this.numberAttributes == this.attributes.length)
				
				System.arraycopy(
						this.attributes,
						0,
						this.attributes = new InferredAttribute[this.numberAttributes  * 2],
						0,
						this.numberAttributes );
						this.attributes [this.numberAttributes  ++] = attribute;
			
			
			attributesHash.put(name, attribute);
			this.updatePositions(definer.sourceStart, definer.sourceEnd);
		}
		return attribute;
	}
	
	public InferredAttribute findAttribute(char [] name)
	{
		return (InferredAttribute)attributesHash.get(name);
//		if (attributes!=null)
//		for (Iterator attrIterator = attributes.iterator(); attrIterator.hasNext();) {
//			InferredAttribute attribute = (InferredAttribute) attrIterator.next();
//			if (CharOperation.equals(name,attribute.name))
//				return attribute;
//		}
//		return null;
	}


	public InferredMethod addMethod(char [] methodName, MethodDeclaration methodDeclaration) {
		InferredMethod method = findMethod(methodName, methodDeclaration);
		if (method==null)
		{
			method=new InferredMethod(methodName,methodDeclaration,this);
			if (methods==null)
				methods=new ArrayList();
			methods.add(method);
			this.updatePositions(methodDeclaration.sourceStart, methodDeclaration.sourceEnd);
		}
		methodDeclaration.inferredMethod = method;
		return method;
	}

	public InferredMethod findMethod(char [] methodName, MethodDeclaration methodDeclaration) {
		if (methodName==TypeConstants.INIT)
			methodName=this.name;
		if (methods!=null)
			for (Iterator methodIterator = methods.iterator(); methodIterator.hasNext();) {
				InferredMethod method = (InferredMethod) methodIterator.next();
				if (CharOperation.equals(methodName,method.name))
					return method;
			}
			return null;
		
	}
	
	public TypeBinding resolveType(BlockScope scope, ASTNode node) {
		// handle the error here
		if (this.resolvedType != null) // is a shared type reference which was already resolved
			return this.resolvedType.isValidBinding() ? this.resolvedType : null; // already reported error


		if (isArray())
		{
			TypeBinding memberType = (referenceClass!=null)?referenceClass.resolveType(scope,node):null;
			if (memberType==null)
				memberType=TypeBinding.ANY;
			this.resolvedType=new ArrayBinding(memberType, 1, scope.compilationUnitScope().environment) ;

		}
		else {
			this.resolvedType = scope.getType(name);
			/* the inferred type isn't valid, so don't assign it to the variable */
			if(!this.resolvedType.isValidBinding()) this.resolvedType = null;
		}
		
		
		if (this.resolvedType == null)
			return null; // detected cycle while resolving hierarchy	
		if (!this.resolvedType.isValidBinding()) {
			scope.problemReporter().invalidType(node, this.resolvedType);
			return null;
		}
		if (node.isTypeUseDeprecated(this.resolvedType, scope))
			scope.problemReporter().deprecatedType(this.resolvedType, node);
		this.resolvedType = scope.environment().convertToRawType(this.resolvedType);
		return this.resolvedType ;
	}

	

	public void dumpReference(StringBuffer sb)
	{
		sb.append(name);
		if (referenceClass!=null)
		{
			sb.append('(');
			referenceClass.dumpReference(sb);
			sb.append(')');
		}
	}
	
	public boolean containsMethod(AbstractMethodDeclaration inMethod) {
		if (methods!=null)
			for (Iterator iter = methods.iterator(); iter.hasNext();) {
				InferredMethod method = (InferredMethod) iter.next();
				if (method.methodDeclaration==inMethod)
					return true;
			}
		return false;
	}

 

	public ReferenceBinding resolveSuperType(ClassScope classScope) {
		if (this.resolvedSuperType != null)
			return this.resolvedSuperType;

		this.resolvedSuperType = (ReferenceBinding)classScope.getType(name);

		return this.resolvedSuperType;
	}
	
	public boolean isArray()
	{
		return ARRAY_NAME.equals(name);
	}

	public StringBuffer print(int indent, StringBuffer output) {
		printIndent(indent, output); //$NON-NLS-1$
		char[] superName= getSuperClassName();
		output.append("class ").append(name).append(" extends ").append(superName).append("{\n");
		for (int i=0;i<this.numberAttributes;i++) {
				this.attributes[i].print(indent+1,output);
				output.append(";\n");
			}		
		if (methods!=null)
			for (Iterator methodIterator = methods.iterator(); methodIterator.hasNext();) {
				InferredMethod method = (InferredMethod) methodIterator.next();
				method.print(indent+1,output);
				output.append("\n");
			}
		output.append("}");
		return output;
	}
	
	public boolean isInferred()
	{
		return true;
	}

	public void updatePositions(int start, int end)
	{
		if (start>=0 && start<this.sourceStart)
			this.sourceStart=start;
		if (end>0&&end>this.sourceEnd)
			this.sourceEnd=end;
	}
	
}
