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
package org.eclipse.wst.jsdt.debug.core.jsdi.event;

/**
 * 
 * Description of a JSDI event when a JavaScript context has been entered.<br><br>
 * New contexts are entered when:
 * <ul>
 * <li>a new script is run</li>
 * <li>execution enters a function</li>
 * </ul>
 * 
 * @since 1.1
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ContextEnterEvent extends LocatableEvent {

}
