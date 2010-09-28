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
function toggle()
{
    var helloElem = document.getElementById("hello");
    var hello_againElem = document.getElementById("hello_again");

    if(state == 'hello')
    {
        helloElem.style.display = 'none';
        hello_againElem.style.display = 'block';
        state='hello_again';
    }
    else
    {
        helloElem.style.display = 'block';
        hello_againElem.style.display = 'none';
        state='hello';
    }
}

var state = 'hello';

toggle = function()
{
    var helloElem = document.getElementById("hello");
    var hello_againElem = document.getElementById("hello_again");

    if(state == 'hello')
    {
        helloElem.style.display = 'none';
        hello_againElem.style.display = 'block';
        state='hello_again';
    }
    else
    {
        helloElem.style.display = 'block';
        hello_againElem.style.display = 'none';
        state='hello';
    }
};