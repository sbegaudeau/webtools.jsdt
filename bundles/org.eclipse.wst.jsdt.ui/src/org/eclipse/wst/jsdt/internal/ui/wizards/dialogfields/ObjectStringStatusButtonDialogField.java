/**
 * 
 */
package org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author childsb
 *
 */
public class ObjectStringStatusButtonDialogField extends StringButtonDialogField {

	Object enclosedObject;
	String description;

	public ObjectStringStatusButtonDialogField(IStringButtonAdapter adapter) {
		super(adapter);
		//super.setTextFieldEditable(false);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.jsdt.internal.ui.wizards.dialogfields.StringDialogField#getTextControl(org.eclipse.swt.widgets.Composite)
	 */
	public Text getTextControl(Composite parent) {
		
		Text superTextControl =  super.getTextControl(parent);
		if(superTextControl!=null) superTextControl.setEditable(false);
		return superTextControl;
	}


	public void setDescription(String description) {
		this.description=description;
	}
	
	public String getDescription() {
		if(description==null && enclosedObject==null)
			return "";
		return description==null?enclosedObject.toString():description;
	}
	
	public void setValue(Object newValue) {
		this.enclosedObject = newValue;
		updateStatusField();
	}
	
	private void updateStatusField() {
		if(description==null && enclosedObject==null)
			return;
		
		setText(description==null?enclosedObject.toString():description);
	}
	
	public Object getValue() {
		return enclosedObject;
	}
	
	
	
}
