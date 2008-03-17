package org.eclipse.wst.jsdt.core.infer;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.wst.jsdt.core.ast.IASTNode;
import org.eclipse.wst.jsdt.core.ast.IAbstractFunctionDeclaration;
import org.eclipse.wst.jsdt.core.ast.IFunctionDeclaration;
import org.eclipse.wst.jsdt.core.compiler.CharOperation;
import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ClassScope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.MultipleTypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.Scope;
import org.eclipse.wst.jsdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TagBits;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.wst.jsdt.internal.compiler.lookup.TypeConstants;
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

	public boolean isArray=false;
	public boolean isAnonymous=false;

	public String inferenceProviderID;
	public String inferenceStyle;
	
	
	public final static char[] OBJECT_NAME=new char[]{'O','b','j','e','c','t'};
	public final static char[] OBJECT_LITERAL_NAME = new char[]{'{','}'};

	public final static char[] ARRAY_NAME=new char[]{'A','r','r','a','y'};
	public final static char[] GLOBAL_NAME=new char[]{'G','l','o','b','a','l'};


	public InferredType(char [] className)
	{
		this.name=className;
		this.sourceStart=-1;
	}

	public char [] getName() {
		return name;
	}

	public char [] getSuperClassName()
	{
		return superClass!=null ? superClass.getName() : OBJECT_NAME;
	}
	public InferredAttribute addAttribute(char [] name, IASTNode definer)
	{
		InferredAttribute attribute = findAttribute(name);
		if (attribute==null)
		{
			attribute=new InferredAttribute(name, this ,definer.sourceStart(),definer.sourceEnd());
			attribute.node=(ASTNode)definer;
			
			if (this.numberAttributes == this.attributes.length)

				System.arraycopy(
						this.attributes,
						0,
						this.attributes = new InferredAttribute[this.numberAttributes  * 2],
						0,
						this.numberAttributes );
						this.attributes [this.numberAttributes  ++] = attribute;


			attributesHash.put(name, attribute);

			if( !isAnonymous )
				this.updatePositions(definer.sourceStart(), definer.sourceEnd());
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


	public InferredMethod addMethod(char [] methodName, IFunctionDeclaration functionDeclaration, boolean isConstructor) {
		MethodDeclaration methodDeclaration = (MethodDeclaration)functionDeclaration;
		InferredMethod method = findMethod(methodName, methodDeclaration);
		if (method==null)
		{
			method=new InferredMethod(methodName,methodDeclaration,this);
			if (methodDeclaration.inferredMethod==null) 
				methodDeclaration.inferredMethod = method;
			else
			{
				if (isConstructor)
				{
					methodDeclaration.inferredMethod.inType=this;
					methodDeclaration.inferredMethod = method;
				} else if (methodDeclaration.inferredMethod.isConstructor)
					method.inType=methodDeclaration.inferredMethod.inType;
				
			}
			if (methods==null)
				methods=new ArrayList();
			methods.add(method);

			if( !isAnonymous )
				this.updatePositions(methodDeclaration.sourceStart, methodDeclaration.sourceEnd);
			method.isConstructor=isConstructor;
		}
		else
			if (methodDeclaration.inferredMethod==null)
				methodDeclaration.inferredMethod=method;
		return method;
	}

	public InferredMethod findMethod(char [] methodName, IFunctionDeclaration methodDeclaration) {
		boolean isConstructor= methodName==TypeConstants.INIT;
		if (methods!=null)
			for (Iterator methodIterator = methods.iterator(); methodIterator.hasNext();) {
				InferredMethod method = (InferredMethod) methodIterator.next();
				if (CharOperation.equals(methodName,method.name))
					return method;
				if (isConstructor && method.isConstructor)
					return method;
			}
			return null;

	}

	public TypeBinding resolveType(Scope scope, ASTNode node) {
		// handle the error here
		if (this.resolvedType != null) // is a shared type reference which was already resolved
			return this.resolvedType.isValidBinding() ? this.resolvedType : null; // already reported error


		if (isArray())
		{
			TypeBinding memberType = (referenceClass!=null)?referenceClass.resolveType(scope,node):null;
			if (memberType==null)
				memberType=TypeBinding.UNKNOWN;
			this.resolvedType=new ArrayBinding(memberType, 1, scope.compilationUnitScope().environment) ;

		}
		else {
			if (CharOperation.indexOf('|', name)>0)
			{
				char[][] names = CharOperation.splitAndTrimOn('|', name);
				this.resolvedType=new MultipleTypeBinding(scope,names);
			}
			else
			  this.resolvedType = scope.getType(name);
			/* the inferred type isn't valid, so don't assign it to the variable */
			if(!this.resolvedType.isValidBinding()) this.resolvedType = null;
		}


		if (this.resolvedType == null)
			return null; // detected cycle while resolving hierarchy
		if (node!=null && !this.resolvedType.isValidBinding()) {
			scope.problemReporter().invalidType(node, this.resolvedType);
			return null;
		}
		if (node!=null && node.isTypeUseDeprecated(this.resolvedType, scope))
			scope.problemReporter().deprecatedType(this.resolvedType, node);
		this.resolvedType = scope.environment().convertToRawType(this.resolvedType);

		if( isAnonymous )
			this.resolvedType.tagBits |= TagBits.AnonymousTypeMask;

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

	public boolean containsMethod(IAbstractFunctionDeclaration inMethod) {
		if (methods!=null)
			for (Iterator iter = methods.iterator(); iter.hasNext();) {
				InferredMethod method = (InferredMethod) iter.next();
				if (method.getFunctionDeclaration()==inMethod)
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
		printIndent(indent, output);
		char[] superName= getSuperClassName();
		output.append("class ").append(name).append(" extends ").append(superName).append("{\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		for (int i=0;i<this.numberAttributes;i++) {
				this.attributes[i].print(indent+1,output);
				output.append(";\n"); //$NON-NLS-1$
			}
		if (methods!=null)
			for (Iterator methodIterator = methods.iterator(); methodIterator.hasNext();) {
				InferredMethod method = (InferredMethod) methodIterator.next();
				method.print(indent+1,output);
				output.append("\n"); //$NON-NLS-1$
			}
		output.append("}"); //$NON-NLS-1$
		return output;
	}

	public boolean isInferred()
	{
		return true;
	}

	public void updatePositions(int start, int end)
	{
		if (this.sourceStart==-1 ||(start>=0 && start<this.sourceStart))
			this.sourceStart=start;
		if (end>0&&end>this.sourceEnd)
			this.sourceEnd=end;
	}

	public IAbstractFunctionDeclaration declarationOf(MethodBinding methodBinding) {
		if (methodBinding != null && this.methods != null) {
			for (int i = 0, max = this.methods.size(); i < max; i++) {
				InferredMethod method=(InferredMethod) this.methods.get(i);

				if (method.methodBinding==methodBinding)
					return method.getFunctionDeclaration();
			}
		}
		return null;
	}
	
	public boolean isNamed()
	{
		return !isAnonymous || !CharOperation.prefixEquals(InferEngine.ANONYMOUS_PREFIX, this.name);
	}
}
