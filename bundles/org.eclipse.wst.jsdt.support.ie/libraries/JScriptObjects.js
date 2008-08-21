/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************
 *
 **/
/**
  * Object DABoolean()
  *
  * Temp Definition of DABoolean Type as found in MSDN Pages
  *
  * @super Object
  * @type  DABoolean
  * @class DABoolean
  * @since JScript 5.6   
 */

function DABoolean(){};
DABoolean.prototype=new Object();

  /* Object arguments()
  * @super Array
  * @type  arguments
  * @class arguments
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/9dthzd08.aspx     
 */

function arguments(){};
arguments.prototype=new Array();

/**
  * An intrinsic global object that stores information about the results of regular expression pattern matches.
  * Object RegExp()
  * @super Object
  * @type  RegExp
  * @class RegExp
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/9dthzd08.aspx     
 */
function RegExp(){};
RegExp.prototype=new Object();
 /**
  * Property global
  * @type    Boolean
  * @class   RegExp
  * @return  Boolean
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
RegExp.prototype.global=true;
 /**
  * Property index
  * @type    Number
  * @class   RegExp
  * @return  Number
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */ 
RegExp.prototype.index=-1;
 /**
  * Property input
  * @type    String
  * @class   RegExp
  * @return  String
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */
RegExp.prototype.input="";
 /**
  * Property lastIndex
  * @type    Number
  * @class   RegExp
  * @return  Number
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */
RegExp.prototype.lastIndex=-1;
 /**
  * Property lastMatch
  * @type    String
  * @class   RegExp
  * @return  String
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */
RegExp.prototype.lastMatch=""
 /**
  * Property lastParen
  * @type    String
  * @class   RegExp
  * @return  String
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */
RegExp.prototype.lastParen="";
 /**
  * Property leftContext
  * @type    String
  * @class   RegExp
  * @return  String
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */
RegExp.prototype.leftContext="";
 /**
  * Property rightContext
  * @type    String
  * @class   RegExp
  * @return  String
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */
RegExp.prototype.rightContext="";
 /**
  * Property ignoreCase
  * @type    Boolean
  * @class   RegExp
  * @return  Boolean
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */
RegExp.prototype.ignoreCase=false;
 /**
  * Property multiline
  * @type    Boolean
  * @class   RegExp
  * @return  Boolean
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */
RegExp.prototype.multiline=false; 
 /**
  * Property source
  * @type    String
  * @class   RegExp
  * @return  String
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */
RegExp.prototype.source="";
 /**
  * function test(pos) 
  * @type    Array
  * @class   RegExp
  * @param   array1 Array
  * @param   array2 Array
  * @return  Array
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
RegExp.prototype.test=function(){};
 /**
  * function exec(pos) 
  * @type    Boolean
  * @class   RegExp
  * @param   str String
  * @return  Boolean
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
RegExp.prototype.exec=function(str){};
 /**
  * function compile(pos) 
  * @type    Array
  * @class   RegExp
  * @param   pattern String
  * @param   flags String Optional. Available flags, which may be combined, are:g (global search for all occurrences of pattern)i (ignore case)m (multiline search)
  * @return  Array
  * @see     RegExp
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
RegExp.prototype.compile=function(pattern, flags){};
/**
  * Enables and returns a reference to an Automation object.
  * Object ActiveXObject(servername.typename, location)
  * @super Object
  * @type  ActiveXObject
  * @param servername String Required. The name of the application providing the object
  * @param typename String Required. The type or class of the object to create.
  * @param location String  Optional. The name of the network server where the object is to be created.
  * @class ActiveXObject
  * @since JScript 1.0
  * @link  http://msdn2.microsoft.com/en-us/library/7sw4ddf8.aspx  
 */
function ActiveXObject(){};
ActiveXObject.prototype=new Object();

 /**
  * function concat(pos) 
  * @type    Array
  * @class   Array
  * @param   array1 Array
  * @param   array2 Array
  * @return  Array
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Array.prototype.concat=function(array1, array2){};
 /**
  * function join(separator)
  * @type    String
  * @class   Array
  * @param   separator String
  * @param   array2 Array
  * @return  String
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Array.prototype.join=function(separator){};
 /**
  * function pop() 
  * @type    Array
  * @class   Array
  * @param   array1 Array
  * @param   array2 Array
  * @return  Array
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Array.prototype.pop=function(){};
 /**
  * function push(values) 
  * @type    Object
  * @class   Array
  * @param   values Array
  * @return  Object
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Array.prototype.push=function(values){};
 /**
  * function reverse()
  * @class   Array
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Array.prototype.reverse=function(){};
 /**
  * function shift() 
  * @type    Object
  * @class   Array
  * @return  Object
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Array.prototype.shift=function(){};
 /**
  * function slice() 
  * @type    Array
  * @class   Array
  * @param   startIndex Number;
  * @param   endIndex Number
  * @return  Array
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Array.prototype.slice=function(startIndex,endIndex){};
 /**
  * function sort(sortFunction) 
  * @class   Array
  * @param   sortFunction Function Optional. The name of the function used to determine the order of the elements. If omitted, the elements are sorted in ascending, ASCII character order
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Array.prototype.sort=function(sortFunction){};
 /**
  * function splice(start, deleteCount, items)
  * @type    Array
  * @class   Array
  * @param start Number
  * @param deleteCount Number
  * @param items Array
  * @return  Array
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Array.prototype.splice=function(start, deleteCount, items){};
 /**
  * function toLocaleString() 
  * @type    String
  * @class   Object
  * @return  String
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Object.prototype.toLocaleString=function(){};
 /**
  * function unshift()
  
  * @class   Array
  * @param   elements Array
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Array.prototype.unshift=function(elements){};
 /**
  * function valueOf()
  * @type    Object
  * @class   Object
  * @return  Object
  * @see     Array
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/k4h76zbx.aspx     
 */  
Object.prototype.valueOf=function(){};

/**
  * Object Date()
  * @super Object
  * @type  Date
  * @class Date
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/9dthzd08.aspx     
 */
function Date(dateVal){};
function Date(year, month, date, hours, minutes, seconds,ms){}; 
 /**
  * function getDate()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getDate=function(){};
 /**
  * function getDay()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getDay=function(){}; 
 /**
  * function getFullYear()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getFullYear=function(){};
 /**
  * function getHours()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */   
Date.prototype.getHours=function(){}; 
 /**
  * function getMilliseconds()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getMilliseconds=function(){}; 
 /**
  * function getMinutes()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getMinutes=function(){}; 
 /**
  * function getMonth()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getMonth=function(){}; 
 /**
  * function getSeconds()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getSeconds=function(){}; 
 /**
  * function getTime()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getTime=function(){}; 
 /**
  * function getTimezoneOffset()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getTimezoneOffset=function(){}; 
 /**
  * function getUTCDate()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getUTCDate=function(){}; 
 /**
  * function getUTCDay()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getUTCDay=function(){}; 
 /**
  * function getUTCFullYear()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getUTCFullYear=function(){}; 
 /**
  * function getUTCHours()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getUTCHours=function(){}; 
 /**
  * function getUTCMilliseconds()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getUTCMilliseconds=function(){}; 
 /**
  * function getUTCMinutes()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getUTCMinutes=function(){}; 
 /**
  * function getUTCMonth()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getUTCMonth=function(){}; 
 /**
  * function getUTCSeconds()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getUTCSeconds=function(){}; 
 /**
  * function getVarDate()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getVarDate=function(){}; 
 /**
  * function getYear()
  * @type    Number
  * @class   Date
  * @return  Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.getYear=function(){}; 
 /**
  * function parse(dateVal)
  * @type    Date
  * @class   Date
  * @param 	 dateVal String
  * @return  Date
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.parse=function(dateVal){}; 
 /**
  * function setDate(dateVale)
 
  * @class   Date
  * @param 	 dateVal Date
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx     
 */  
Date.prototype.setDate=function(dateVal){}; 
 /**
  * function setFullYear(year,month, day)
  
  * @class   Date
  * @param 	 year Number
  * @param   month Number
  * @param   day Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setFullYear=function(year,month, day){};
 /**
  * function setHours(number)

  * @class   Date
  * @param 	 numHours Number
  * @param 	 numMin Number
  * @param 	 numSec Number
  * @param 	 numMilli Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */  
Date.prototype.setHours=function(numHours, numMin, numSec, numMilli){}; 
 /**
  * function setMilliseconds(number)
 
  * @class   Date
  * @param 	 number Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setMilliseconds=function(number){}; 
 /**
  * function setMinutes(number)
  
  * @class   Date
  * @param 	 number Number
  * @param 	 numMilli Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setMinutes=function(number, numMilli){}; 
 /**
  * function setMonth(number)
 
  * @class   Date
  * @param 	 numMonth Number
  * @param 	 dateVale Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setMonth=function(numMonth, dateVale){}; 
 /**
  * function setSeconds(number)
  
  * @class   Date
  * @param 	 number Number
  * @param 	 numMilli Numbe
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setSeconds=function(number, numMilli){}; 
 /**
  * function setTime(number)
 
  * @class   Date
  * @param 	 milli Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setTime=function(milli){}; 
 /**
  * function setUTCDate(number)
  
  * @class   Date
  * @param 	 number Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setUTCDate=function(number){}; 
 /**
  * function setUTCFullYear(number)
  
  * @class   Date
  * @param 	 numYear Number
  * @param 	 numMonth Number
  * @param 	 numDate Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setUTCFullYear=function(numYear, numMonth, numDate){}; 
 /**
  * function setUTCHours(number)
  
  * @class   Date
  * @param 	 numHours Number
  * @param 	 numMin Number
  * @param 	 numSec Number
  * @param 	 numMilli Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setUTCHours=function(numHours, numMin, numSec, numMilli){}; 
 /**
  * function setUTCMilliseconds(number)
 
  * @class   Date
  * @param 	 number Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setUTCMilliseconds=function(number){}; 
 /**
  * function setUTCMinutes(number)
 
  * @class   Date
  * @param 	 numMinutes Number
  * @param 	 numSeconds Number
  * @param 	 numMilli Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setUTCMinutes=function(numMinutes, numSeconds, numMilli){}; 
 /**
  * function setUTCMonth(number)
  
  * @class   Date
  * @param 	 numMonth Number
  * @param 	 dateVal Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setUTCMonth=function(numMonth, dateVal){}; 
 /**
  * function setUTCSeconds(number)

  * @class   Date
  * @param 	 numSeconds Number
  * @param 	 numMilli Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setUTCSeconds=function(numSeconds, numMilli){}; 
 /**
  * function setYear(number)
 
  * @class   Date
  * @param 	 number Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.setYear=function(number){}; 
 /**
  * function toDateString(number)
 
  * @class   Date
  * @param 	 number Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.toDateString=function(){}; 
 /**
  * function toGMTString(number)
 
  * @class   Date
  * @param 	 number Number
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.toGMTString=function(){}; 
 /**
  * function toLocaleDateString()
  * @type    String
  * @class   Date
  * @return  String
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.toLocaleDateString=function(){}; 
 /**
  * function toLocaleString()
  * @type    String
  * @class   Date
  * @return  String
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.toLocaleString=function(){}; 
 /**
  * function toLocaleTimeString()
  * @type    String
  * @class   Date
  * @return  String
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.toLocaleTimeString=function(){}; 
 /**
  * function toTimeString()
  * @type    String
  * @class   Date
	* @return  String
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.toTimeString=function(){}; 
 /**
  * function toUTCString()
  * @type    String
  * @class   Date
  * @return String
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.prototype.toUTCString=function(){}; 
 /**
  * function UTC();
  * @type    Date
  * @class   Date
  * @param 	 year Number
  * @param 	 month Number
  * @param 	 day Number
  * @param 	 hours Number
  * @param 	 seconds Number
  * @param 	 ms Number
  * @return Date
  * @see     Date
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Date.UTC=function(year, month, day, hours, minutes, seconds, ms){}; 

/**
  
  * Object Debug()
  * @super Object
  * @type  Debug
  * @class Debug
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/bs12a9wf.aspx 
 */
function Debug(){};
Debug.prototype=new Object();
 /**
  * function write(value);

  * @class   Debug
  * @param 	 value String
  * @see     Debug
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Debug.write=function(value){};
 /**
  * function writeln(value);
 
  * @class   Debug
  * @param 	 value String
  * @see     Debug
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */ 
Debug.writeln=function(value){};
/**
  
  * Object Enumerator()
  * @super Object
  * @type  Enumerator
  * @class Enumerator
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/bs12a9wf.aspx 
 */
function Enumerator(){};
/**
  
  * Object Enumerator(collection)
  * @super Object
  * @type  Enumerator
  * @param collection Object
  * @class Enumerator
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/bs12a9wf.aspx 
 */
function Enumerator(collection){};
Enumerator.prototype=new Object();
 /**
  * function atEnd()
  * @type    Enumerator
  * @class   Enumerator
  * @return  Boolean
  * @see     Enumerator
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */
Enumerator.prototype.atEnd=function(){};
 /**
  * function item()
  * @type    Object
  * @class   Enumerator
  * @return  Object
  * @see     Enumerator
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */
Enumerator.prototype.item=function(){};
 /**
  * function moveFirst()
  * @type    Object
  * @class   Enumerator
  * @return  Object
  * @see     Enumerator
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */
Enumerator.prototype.moveFirst=function(){};
 /**
  * function moveNext()
  * @type    Object
  * @class   Enumerator
  * @return  Object
  * @see     Enumerator
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/cd9w2te4.aspx   
  */
Enumerator.prototype.moveNext =function(){};

/**
  
  * Object Error()
  * @super Object
  * @type  Error
  * @param number Number
  * @param description String
  * @class Error
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx
 */
function Error(number,description){};
Error.prototype=new Object();
 /**
  * Property description
  * @type    String
  * @class   Error
  * @return  String
  * @see     Error
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx   
  */
Error.prototype.description="";
 /**
  * Property message
  * @type    String
  * @class   Error
  * @return  String
  * @see     Error
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx   
  */
Error.prototype.message="";
 /**
  * Property name
  * @type    String
  * @class   Error
  * @return  String
  * @see     Error
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx   
  */
Error.prototype.name="";
 /**
  * Property number
  * @type    Number
  * @class   Error
  * @return  Number
  * @see     Error
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx   
  */
Error.prototype.number=0;
/**
  
  * Object Function()
  * @super Object
  * @type  Function
  * @param functionName String
  * @param arg1 String
  * @param arg1 String
  * @param arg2 String
  * @param arg3 String
  * @param arg4 String
  * @param arg5 String
  * @param arg6 String
  * @param arg7 String
  * @param arg8 String
  * @param arg9 String
  * @param body String
  * @class Function
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/x844tc74.aspx
 */
function Function(functionName, arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9, body){};
Function.prototype=new Object();
 /**
  * Property arguments
  * @type    arguments
  * @class   Function
  * @return  arguments
  * @see     Function
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx   
  */
Function.prototype.arguments=new arguments();
 /**
  * Property callee
  * @type    String
  * @class   Function
  * @return  String
  * @see     Function
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx   
  */
Function.prototype.callee=""
 /**
  * Property caller
  * @type    String
  * @class   Function
  * @return  String
  * @see     Function
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx   
  */
Function.prototype.caller=""
 /**
  * Property constructor
  * @type    String
  * @class   Function
  * @return  String
  * @see     Function
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx   
  */
Function.prototype.constructor=""
 /**
  * Property length
  * @type    Number
  * @class   Function
  * @return  Number
  * @see     Function
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx   
  */
Function.prototype.length=function(){};
 /**
  * function apply(thisObj,args)
  * @class   Function
  * @param   thisObj Object
  * @param   args argument
  * @see     Function
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx   
  */
Function.prototype.apply=function(thisObj,args){};
 /**
  * function apply(thisObj,args)
  * @class   Function
  * @param   thisObj Object
  * @param   args argument
  * @see     Function
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dww52sbt.aspx   
  */
Function.prototype.call=function(thisObj, args){};
/**
  
  * Object Global()
  * @super Object
  * @type  Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx
  */
function Global(){};
Global.prototype=new Object();
 /**
  * Property infinity
  * @class   Global
  * @type 	 Number
  * @return Number
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.infinity=0;
 /**
  * Property NaN
  * @class   Global
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.NaN=null;
 /**
  * Property undefined
  * @class   Global
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.undefined=null;
 /**
  * function decodeURI(value)
  * @class   Global
  * @type    String
  * @param   value String
  * @return  String
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.prototype.decodeURI=function(value){};
 /**
  * function decodeURIComponent(value)
  * @class   Global
  * @type    String
  * @param   value String
  * @return  String
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */ 
Global.prototype.decodeURIComponent=function(value){}; 
 /**
  * function encodeURI(value)
  * @class   Global
  * @type    String
  * @param   value String
  * @return  String
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.prototype.encodeURI=function(value){}; 
 /**
  * function encodeURIComponent(value)
  * @class   Global
  * @type    String
  * @param   value String
  * @return  String
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.prototype.encodeURIComponent=function(value){}; 
 /**
  * function escape(value)
  * @class   Global
  * @type    String
  * @param   value String
  * @return  String
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.prototype.escape=function(value){}; 
 /**
  * function eval(value)
  * @class   Global
  * @type    String
  * @param   value String
  * @return  String
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.prototype.eval=function(value){}; 
 /**
  * function isFinite(value)
  * @class   Global
  * @type    String
  * @param   value String
  * @return  String
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.prototype.isFinite=function(value){}; 
 /**
  * function isNaN(value)
  * @class   Global
  * @type    String
  * @param   value String
  * @return  String
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.prototype.isNaN=function(value){}; 
 /**
  * function parseFloat(value)
  * @class   Global
  * @type    String
  * @param   value String
  * @return  String
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.prototype.parseFloat=function(value){}; 
 /**
  * function parseInt(value)
  * @class   Global
  * @type    String
  * @param   value String
  * @return  String
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.prototype.parseInt=function(value){}; 
 /**
  * function unescape(value)
  * @class   Global
  * @type    String
  * @param   value String
  * @return  String
  * @see     Global
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/52f50e9t.aspx  
  */
Global.prototype.unescape=function(value){};

 /**
  * @class   Math
  * @see     Number
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
function Math(){};
Math.prototype=new Object();
 /**
  * Property E
  * @class Math
  * @type Number
  * @return Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.E=0; 
 /**
  * Property LN2
  * @class Math
  * @type Number
  * @return Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.LN2=0;
 /**
  * Property LN10
  * @class Math
  * @type Number
  * @return Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */ 
Math.LN10=0; 
 /**
  * Property LOG2E
  * @class Math
  * @type Number
  * @return Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.LOG2E=0; 
 /**
  * Property LOG10E
  * @class Math
  * @type Number
  * @return Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.LOG10E=0; 
 /**
  * Property PI
  * @class Math
  * @type Number
  * @return Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.PI=0; 
 /**
  * Property SQRT1_2
  * @class Math
  * @type Number
  * @return Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.SQRT1_2=0; 
 /**
  * Property SQRT2=0
  * @class Math
  * @type Number
  * @return Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.SQRT2=0;
 /**
  * function abs(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.abs=function(number){};
 /**
  * function acos(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.acos=function(number){};
 /**
  * function asin(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.asin=function(number){};
 /**
  * function atan(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.atan=function(number){};
 /**
  * function atan2(number)
  * @class Math
  * @type Number
  * @return Number
  * @param x Number
   * @param y Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.atan2=function(x,y){};
 /**
  * function ceil(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.ceil=function(number){};
 /**
  * function cos(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.cos=function(number){};
 /**
  * function exp(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.exp=function(number){};
 /**
  * function floor(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.floor=function(number){};
 /**
  * function log(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.log=function(number){};
 /**
  * function max(argList)
  * @class Math
  * @type Number
  * @return Number
  * @param argList arguments
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.max=function(argList){};
 /**
  * function min(argList)
  * @class Math
  * @type Number
  * @return Number
  * @param argList arguments
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.min=function(argList){};
 /**
  * function pow(number)
  * @class Math
  * @type Number
  * @return Number
  * @param base Number
  * @param exp Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.pow=function(base, exp){};
 /**
  * function random(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.random=function(number){};
 /**
  * function round(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.round=function(number){};
 /**
  * function sin(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.sin=function(number){};
 /**
  * function sqrt(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.sqrt=function(number){};
 /**
  * function tan(number)
  * @class Math
  * @type Number
  * @return Number
  * @param number Number
  * @see     Math
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/b272f386.aspx
  */
Math.tan=function(number){};

Number.MAX_VALUE=0;
Number.MIN_VALUE=0;
Number.NaN Property=0;
Number.NEGATIVE_INFINITY=0;
Number.POSITIVE_INFINITY=0;

 /**
  * function toExponential()
  * @class Number
  * @type Number
  * @return Number
  * @see     Number
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dwab3ed2.aspx
  */
Number.prototype.toExponential=function(){};
 /**
  * function toFixed()
  * @class Number
  * @type Number
  * @return Number
  * @see     Number
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dwab3ed2.aspx
  */
Number.prototype.toFixed=function(){};
 /**
  * function toLocaleString()
  * @class Number
  * @type String
  * @return String
  * @see     Number
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dwab3ed2.aspx
  */
Number.prototype.toLocaleString=function(){};
 /**
  * function toPrecision()
  * @class Number
  * @type Number
  * @return Number
  * @see     Number
  * @since   JScript 5.6
  * @link    http://msdn2.microsoft.com/en-us/library/dwab3ed2.aspx
  */
Number.prototype.toPrecision=function(){};

 /**
  * function anchor(aString)
  * @class 	String
  * @type 	String
  * @return String
  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.anchor=function(aString){};
 /**
  * function big()
  * @class 	String

  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.big=function(){};
 /**
  * function blink(aString)
  * @class 	String

  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.blink=function(){};
 /**
  * function bold(aString)
  * @class 	String

  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.bold=function(){};  
 /**
  * function fixed()
  * @class 	String

  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.fixed=function(){};
 /**
  * function fontcolor(color)
  * @class 	String
  * @type 	String
  * @param color String
  * @return String
  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.fontcolor=function(color){};
 /**
  * function fontsize(aString)
  * @class 	String
  * @type 	String
  * @return String
  * @param aString String
  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.fontsize=function(aString){};
 /**
  * function anchor(aString)
  * @class 	String
  * @type 	String
  * @return String
  * @param args argument
  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.fromCharCode=function(args){};
 /**
  * function italics()
  * @class 	String
  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.String.prototype.italics=function(){};
 /**
  * function link(aString)
  * @class 	String
  * @param aString String
  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.link=function(aString){};
 /**
  * function strike()
  * @class 	String
  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.strike=function(){};
 /**
  * function sub()
  * @class 	String
  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.sub=function(){};
 /**
  * function substr(start,length)
  * @class 	String
  * @type 	String
  * @return String
  * @param start Number
  * @param length Number
  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.substr=function(start,length){};
 /**
  * function substring(aString)
  * @class 	String
  * @type 	String
  * @return String
    * @param start Number
  * @param length Number
  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.substring=function(start,length){};
 /**
  * function sup(aString)
  * @class 	String
  * @see    String
  * @since  JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/ecczf11c.aspx
  */
String.prototype.sup=function(){};
/**
  * 
  * Object VBArray()
  * @super Array
  * @type  VBArray
  * @param safe Boolean
  * @class VBArray
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/y39d47w8.aspx     
 */
function VBArray(safe){};
VBArray.prototype=new Array();
/**
  * function dimensions();
  * @type  Number
  * @return Number
  * @class VBArray
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/y39d47w8.aspx     
 */
VBArray.prototype.dimensions=function();
/**
  * function getItem(dims);
  * @type  Object
  * @return Object
  * @class VBArray
  * @param dims arguments
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/y39d47w8.aspx     
 */
VBArray.prototype.getItem=function(dims);
/**
  * function lbound(dims);
  * @type  Object
  * @return Object
  * @class VBArray
  * @param dims arguments
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/y39d47w8.aspx     
 */
VBArray.prototype.lbound=function(dims);
/**
  * function toArray()
  * @type  Array
  * @return Array
  * @class VBArray
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/y39d47w8.aspx     
 */
VBArray.prototype.toArray=function();
/**
  * function ubound(dims);
  * @type  Object
  * @return Object
  * @class VBArray
  * @param dims arguments
  * @since JScript 5.6
  * @link  http://msdn2.microsoft.com/en-us/library/y39d47w8.aspx     
 */
VBArray.prototype.ubound=function(number);


/**
  * Returns a reference to an Automation object from a file. 
  *
  * GetObject([pathname] [, class])
  * function GetObject()
  * @param pathname String Optional. Full path and name of the file containing the object to retrieve. If pathname is omitted, class is required
  * @param class String Optional. Class of the object. The class argument uses the syntax appname.objectype and has these parts: appname Required. Name of the application providing the object. objectype  Required. Type or class of object to create.
 *  @since JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/7tf9xwsc.aspx     
 */
function GetObject(pathname, class){};

/**
  * Returns a string representing the scripting language in use.
  *
  * ScriptEngine()
  * function ScriptEngine( )
  * @return String The ScriptEngine function can return any of the following strings: JScript Indicates that Microsoft JScript is the current scripting engine.VBA Indicates that Microsoft Visual Basic for Applications is the current scripting engine.VBScript Indicates that Microsoft Visual Basic Scripting Edition is the current scripting engine.
  * @since JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/efy5bay1.aspx     
 */
function ScriptEngine(){};

/**
  * Returns a string representing the scripting language in use. The return value corresponds directly to the version information contained in the dynamic-link library (DLL) for the scripting language in use.
  *
  * ScriptEngineBuildVersion()
  * function ScriptEngineBuildVersion( )
  * @return String  
  * @since JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/yftk84kt.aspx     
 */
function ScriptEngineBuildVersion(){};

/**
  * Returns a string representing the scripting language in use. The return value corresponds directly to the version information contained in the dynamic-link library (DLL) for the scripting language in use.
  *
  * ScriptEngineMajorVersion()
  * function ScriptEngineMajorVersion( )
  * @return String  
  * @since JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/x7cbaet3.aspx    
 */
function ScriptEngineMajorVersion(){};

/**
  * Returns a string representing the scripting language in use. The return value corresponds directly to the version information contained in the dynamic-link library (DLL) for the scripting language in use.
  *
  * ScriptEngineMajorVersion()
  * function ScriptEngineMajorVersion( )
  * @return String  
  * @since JScript 5.6
  * @link   http://msdn2.microsoft.com/en-us/library/wzaz8hhz.aspx    
 */
function ScriptEngineMinorVersion(){}



