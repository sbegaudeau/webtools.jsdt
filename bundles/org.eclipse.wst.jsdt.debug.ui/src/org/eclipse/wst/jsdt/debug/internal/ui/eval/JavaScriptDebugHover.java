/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.jsdt.debug.internal.ui.eval;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.jsdt.debug.core.model.IJavaScriptStackFrame;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptDebugUIPlugin;
import org.eclipse.wst.jsdt.debug.internal.ui.JavaScriptModelPresentation;
import org.eclipse.wst.jsdt.internal.ui.text.JavaWordFinder;
import org.eclipse.wst.jsdt.ui.text.java.hover.IJavaEditorTextHover;

/**
 * JavaScript hover to show variables
 * 
 * @since 1.1
 */
public class JavaScriptDebugHover implements IJavaEditorTextHover, ITextHoverExtension, ITextHoverExtension2 {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.ITextHover#getHoverInfo(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
	 */
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		Object object = getHoverInfo2(textViewer, hoverRegion);
		if (object instanceof IVariable) {	
			IVariable var = (IVariable) object;
			return getVariableText(var);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.ITextHover#getHoverRegion(org.eclipse.jface.text.ITextViewer, int)
	 */
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		return JavaWordFinder.findWord(textViewer.getDocument(), offset);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.ITextHoverExtension2#getHoverInfo2(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
	 */
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		IJavaScriptStackFrame frame = getFrame();
	    if (frame != null) {
	        IDocument document = textViewer.getDocument();
			if (document != null) {
			    try {
                    String variableName = document.get(hoverRegion.getOffset(), hoverRegion.getLength());
                    IVariable var = findLocalVariable(frame, variableName);
                    if(var != null) {
                    	return var;
                    }
                    //might be in 'this'
                    var = frame.getThisObject();
                    try {
	                    IValue val = var.getValue();
	                    if(val != null) {
	                    	IVariable[] vars = val.getVariables();
	                    	for (int i = 0; i < vars.length; i++) {
	                    		if(vars[i].getName().equals(variableName)) {
	                    			return vars[i];
	                    		}
							}
	                    }
                    }
                    catch(DebugException de) {
                    	return null;
                    }
                    	
                } catch (BadLocationException e) {
                    return null;
                }
			}
	    }
	    return null;
	}

	/**
	 * Returns a local variable in the given frame based on the the given name
	 * or <code>null</code> if none.
	 * 
	 * @return local variable or <code>null</code>
	 */
	private IVariable findLocalVariable(IJavaScriptStackFrame frame, String variableName) {
		if (frame != null) {
			try {
				IVariable[] vars = frame.getVariables();
            	for (int i = 0; i < vars.length; i++) {
					if(vars[i].getName().equals(variableName)) {
						return vars[i];
					}
				}
			} catch (DebugException x) {
				JavaScriptDebugUIPlugin.log(x);
			}
		}
		return null;
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.ITextHoverExtension#getHoverControlCreator()
	 */
	public IInformationControlCreator getHoverControlCreator() {
		return new ExpressionInformationControlCreator();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.ui.text.java.hover.IJavaEditorTextHover#setEditor(org.eclipse.ui.IEditorPart)
	 */
	public void setEditor(IEditorPart editor) {
	}
	
	/**
	 * Returns HTML text for the given variable
	 */
	private static String getVariableText(IVariable variable) {
	    StringBuffer buffer= new StringBuffer();
		JavaScriptModelPresentation modelPresentation = new JavaScriptModelPresentation();
		buffer.append("<p><pre>"); //$NON-NLS-1$
		String variableText= modelPresentation.getText(variable);
		buffer.append(replaceHTMLChars(variableText));
		buffer.append("</pre></p>"); //$NON-NLS-1$
		modelPresentation.dispose();
		if (buffer.length() > 0) {
			return buffer.toString();
		}
		return null;
	}
	
	/**
	 * Replaces reserved HTML characters in the given string with
	 * their escaped equivalents. This is to ensure that variable
	 * values containing reserved characters are correctly displayed.
     */
    private static String replaceHTMLChars(String variableText) {
        StringBuffer buffer = new StringBuffer(variableText.length());
        char[] characters = variableText.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            char character = characters[i];
            switch (character) {
            	case '<':
            	    buffer.append("&lt;"); //$NON-NLS-1$
            	    break;
            	case '>':
            	    buffer.append("&gt;"); //$NON-NLS-1$
            	    break;
            	case '&':
            	    buffer.append("&amp;"); //$NON-NLS-1$
            	    break;
            	case '"':
            	    buffer.append("&quot;"); //$NON-NLS-1$
            	    break;
            	default:
            	    buffer.append(character);
            }
        }
        return buffer.toString();
    }
	
	/**
	 * Returns the stack frame in which to search for variables, or <code>null</code>
	 * if none.
	 * 
	 * @return the stack frame in which to search for variables, or <code>null</code>
	 * if none
	 */
	protected IJavaScriptStackFrame getFrame() {
	    IAdaptable adaptable = DebugUITools.getDebugContext();
		if (adaptable != null) {
			return (IJavaScriptStackFrame)adaptable.getAdapter(IJavaScriptStackFrame.class);
		}
		return null;
	}
}
