/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.core.infer;

import org.eclipse.wst.jsdt.internal.compiler.ast.ASTNode;
import org.eclipse.wst.jsdt.internal.compiler.lookup.FieldBinding;


/**
 * 
 * Provisional API: This class/interface is part of an interim API that is still under development and expected to 
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback 
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken 
 * (repeatedly) as the API evolves.
 */
public class InferredAttribute extends InferredMember{

	public FieldBinding binding;
	public int initializationStart=-1;
	public InferredType type;
	public ASTNode node;

	public InferredAttribute(char [] name, InferredType inType, int start, int end)
	{
		this.name=name;
		this.inType = inType;
		this.sourceStart=start;
		this.sourceEnd=end;
	}


	public StringBuffer print(int indent, StringBuffer output)
	{
		String modifier=(isStatic)? "static ":""; //$NON-NLS-1$ //$NON-NLS-2$
		printIndent(indent, output).append(modifier);
	   if (type!=null)
		   type.dumpReference(output);
	   else
		   output.append("??"); //$NON-NLS-1$
	   output.append(" ").append(name); //$NON-NLS-1$
	   return output;
	}

}
