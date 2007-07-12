/**
  * Object Object()
  * @constructor
  * @class Object
  * @since Standard ECMA-262 3rd. Edition
  * @since Level 2 Document Object Model Core Definition.
  * @link  http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */
function Object(){}
 /**
  * function toString() 
  * @type    String
  * @class   Object
  * @return  String
  * @throws  DOMException
  * @see     Object
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
Object.prototype.toString = function( ){return "";};
 /**
  * function toLocaleString() 
  * @type    String
  * @class   Object
  * @return  String
  * @throws  DOMException
  * @see     Object
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
Object.prototype.toLocaleString  = function( ){return "";};
 /**
  * function valueOf() 
  * @type    String
  * @class   Object
  * @return  String
  * @throws  DOMException
  * @see     Object
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
Object.prototype.valueOf = function( ){return "";};
 /**
  * function hasOwnProperty(V) 
  * @type    Boolean
  * @class   Object
  * @param   V Object
  * @return  Boolean
  * @throws  DOMException
  * @see     Object
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
Object.prototype.hasOwnProperty  = function (V){return true;};
 /**
  * function isPrototypeOf(V) 
  * @type    Boolean
  * @class   Object
  * @param   V Object
  * @return  Boolean
  * @throws  DOMException
  * @see     Object
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
Object.prototype.isPrototypeOf  = function (V){return true;};
 /**
  * function propertyIsEnumerable(V) 
  * @type    String
  * @class   Object
  * @param   V Object
  * @return  String
  * @throws  DOMException
  * @see     Object
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
Object.prototype.propertyIsEnumerable  = function(V){return "";};
/**
  * Property constructor
  * @type  String
  * @class Object
  * @since Standard ECMA-262 3rd. Edition
  * @since Level 2 Document Object Model Core Definition.
  * @link  http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */ 
Object.prototype.constructor="";
/**
  * Object String()
  * @super Object
  * @type  constructor
  * @class String
  * @since Standard ECMA-262 3rd. Edition
  * @since Level 2 Document Object Model Core Definition.
  * @link  http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */

function String(){}
String.prototype = new Object();
/**
  * Property length
  * @type    Number
  * @class   String
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.length =1;
 /**
  * function charAt(pos) 
  * @type    String
  * @class   String
  * @param   pos Number
  * @return  String
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.charAt = function(pos){return "";};
 /**
  * function charCodeAt(pos) 
  * @type    String
  * @class   String
  * @param   pos Number
  * @return  String
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.charCodeAt= function(pos){return "";};
 /**
  * function concat() 
  * @type    String
  * @class   String
  * @return  String
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.concat= function(){return "";};
 /**
  * function indexOf(searchString, position) 
  * @type    Number
  * @class   String
  * @param   searchString String
  * @param   position Number
  * @return  Number
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.indexOf = function(searchString, position){return 1;};
 /**
  * function lastIndexOf(pos) 
  * @type    Number
  * @class   String
  * @param   searchString String
  * @param   position Number
  * @return  Number
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.lastIndexOf = function(searchString, position){return 1;};
 /**
  * function localeCompare(that) 
  * @type    Boolean
  * @class   String
  * @param   that String
  * @return  Boolean
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.localeCompare = function(that){return true;};
 /**
  * function match(regexp) 
  * @type    Boolean
  * @class   String
  * @param   regexp String
  * @return  Boolean
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.match = function(regexp){return true;};
 /**
  * function replace(searchValue, replaceValue) 
  * @type    String
  * @class   String
  * @param   searchValue String
  * @param   replaceValue String
  * @return  String
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.replace = function(searchValue, replaceValue){return "";};
 /**
  * function search(regexp) 
  * @type    Boolean
  * @class   String
  * @param   regexp String
  * @return  Boolean
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.search = function(regexp){return true;};
 /**
  * function slice(start, end) 
  * @type    String
  * @class   String
  * @param   start String
  * @param   end String
  * @return  String
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.slice = function(start, end){return "";};
 /**
  * function split(separator, limit) 
  * @type    String
  * @class   String
  * @param   separator String
  * @param   limit String
  * @return  String
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.split = function(separator, limit){return "";};
 /**
  * function substring(start, end) 
  * @type    String
  * @class   String
  * @param   start String
  * @param   end String
  * @return  String
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.substring = function(start, end){return "";};
 /**
  * function toLowerCase() 
  * @type    String
  * @class   String
  * @return  String
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.toLowerCase = function( ){return "";};
 /**
  * function toLocaleLowerCase() 
  * @type    String
  * @class   String
  * @return  String
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.toLocaleLowerCase = function( ){return "";};
 /**
  * function toUpperCase() 
  * @type    String
  * @class   String
  * @return  String
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.toUpperCase= function ( ){return "";};
 /**
  * function toLocaleUpperCase() 
  * @type    String
  * @class   String
  * @return  String
  * @throws  DOMException
  * @see     String
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
String.prototype.toLocaleUpperCase = function( ){return "";};

/**
  * Object Number()
  * @super Object
  * @constructor
  * @class Number
  * @since Standard ECMA-262 3rd. Edition
  * @since Level 2 Document Object Model Core Definition.
  * @link  http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */
Number.prototype = new Object();
function Number(){}

/**
  * Object Boolean()
  * @super Object
  * @constructor
  * @class Boolean
  * @since Standard ECMA-262 3rd. Edition
  * @since Level 2 Document Object Model Core Definition.
  * @link  http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */
Boolean.prototype = new Object();

/**
  * Object Array()
  * @super Object
  * @constructor
  * @class Array
  * @since Standard ECMA-262 3rd. Edition
  * @since Level 2 Document Object Model Core Definition.
  * @link  http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */
Array.prototype = new Object();
function Array(){}
/**
  * Property length
  * @type    Number
  * @class   Array
  * @see     Array
  * @since   Standard ECMA-262 3rd. Edition 
  * @since   Level 2 Document Object Model Core Definition.
  * @link    http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */  
Array.prototype.length = 1;

/**
  * Object Function()
  * @super Object
  * @constructor
  * @class Function
  * @since Standard ECMA-262 3rd. Edition
  * @since Level 2 Document Object Model Core Definition.
  * @link  http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */
Function.prototype = new Object();

/**
  * Object Date(s)
  * @super Object
  * @constructor
  * @class Date
  * @param s String
  * @since Standard ECMA-262 3rd. Edition
  * @since Level 2 Document Object Model Core Definition.
  * @link  http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/ecma-script-binding.html     
 */
Date.prototype = new Object();
function Date(s){}

/**
 * Displays an <b>Alert</b> dialog box with the
 * desired message and <b>OK<b> button
 * @param message the message to display
 */
function alert(message){}

/**
 * Evaluate string.
 * @param s String to evaluate.
 */
function eval(s){}

/**
 * Test is passed number is finite.
 * @param number Number to test if finite.
 */
function isFinite (number){}

/**
 * Test is passed number is NaN.
 * @param number Number to test if NaN.
 */
function isNaN (number){}

/**
 * Parse passed string as float value.
 * @param string String to convert into float value.
 */
function parseFloat (string){}

/**
 * Parse passed string as int value using radix.
 * @param string String to convert into int value.
 * @param radix Number to use as radix value.
 */
function parseInt (string , radix){}
