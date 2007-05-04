function Object(){}


Object.prototype.toString = function( ){return "";};
Object.prototype.toLocaleString  = function( ){return "";};
Object.prototype.valueOf = function( ){return "";};
Object.prototype.hasOwnProperty  = function (V){return true;};
Object.prototype.isPrototypeOf  = function (V){return true;};
Object.prototype.propertyIsEnumerable  = function(V){return "";};

String.prototype = new Object();
String.prototype.charAt = function(pos){return "";};
String.prototype.charCodeAt= function(pos){return "";};
String.prototype.concat= function(){return "";};
String.prototype.indexOf = function(searchString, position){return 1;};
String.prototype.lastIndexOf = function(searchString, position){return 1;};
String.prototype.localeCompare = function(that){return true;};
String.prototype.match = function(regexp){return true;};
String.prototype.replace = function(searchValue, replaceValue){return "";};
String.prototype.search = function(regexp){return true;};
String.prototype.slice = function(start, end){return "";};
String.prototype.split = function(separator, limit){return "";};
String.prototype.substring = function(start, end){return "";};
String.prototype.toLowerCase = function( ){return "";};
String.prototype.toLocaleLowerCase = function( ){return "";};
String.prototype.toUpperCase= function ( ){return "";};
String.prototype.toLocaleUpperCase = function( ){return "";};

function String(){}

String.prototype.length =1;


Number.prototype = new Object();
function Number(){}

Boolean.prototype = new Object();

Array.prototype = new Object();
Array.prototype.length = 1;
function Array(){}

Function.prototype = new Object();

Date.prototype = new Object();
function Date(s){}


var window;
/**
 * Displays an <b>Alert</b> dialog box with the
 * desired message and <b>OK<b> button
 * @param message the message to display
 */
function alert(message){}

function eval(s){}

function isFinite (number){}
function isNaN (number){}
function parseFloat (string){}
function parseInt (string , radix){}
