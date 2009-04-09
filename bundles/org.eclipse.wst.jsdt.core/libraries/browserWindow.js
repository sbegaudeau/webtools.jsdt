/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
function BarProp(){};
BarProp.prototype = new Array();


Window.prototype= new Global();
function Window(){};

Window.prototype.document= new Document();
Window.prototype.length="";
Window.prototype.parent= new Window();
Window.prototype.top= new Window();
Window.prototype.scrollbars= new BarProp();
Window.prototype.name="";
Window.prototype.scrollX=0;
Window.prototype.scrollY=0;
Window.prototype.scrollTo=function(arg1,arg2){};
Window.prototype.scrollBy=function(arg1,arg2){};
Window.prototype.getSelection=function(){};
Window.prototype.scrollByLines=function(arg1){};
Window.prototype.scrollByPages=function(arg1){};
Window.prototype.sizeToContent=function(){};
Window.prototype.dump=function(arg1){};
/**
 * function setTimeout() 
 * @type    Object
 * @memberOf  Window
 * @returns {Object}
 */
Window.prototype.setTimeout=function(arg1, arg2){};
/**
 * function setInterval() 
 * @type    Object
 * @memberOf  Window
 * @returns {Object}
 */
Window.prototype.setInterval=function(arg1, arg2){};
Window.prototype.clearTimeout=function(){};
Window.prototype.clearInterval=function(){};
Window.prototype.setResizable=function(arg1){};
Window.prototype.captureEvents=function(arg1){};
Window.prototype.releaseEvents=function(arg1){};
Window.prototype.routeEvent=function(arg1){};
Window.prototype.enableExternalCapture=function(){};
Window.prototype.disableExternalCapture=function(){};
/**
 * function prompt() 
 * @type    String
 * @memberOf  Window
 * @returns {String}
 */
Window.prototype.prompt=function(){};
Window.prototype.open=function(){};
Window.prototype.openDialog=function(){};
Window.prototype.frames= new Array();
Window.prototype.window= new Window();
Window.prototype.find=function(){};
Window.prototype.self= new Window();
Window.prototype.history= new History();
Window.prototype.content= new Window();
Window.prototype.menubar= new BarProp();
Window.prototype.toolbar= new BarProp();
Window.prototype.locationbar= new BarProp();
Window.prototype.personalbar= new BarProp();
Window.prototype.statusbar= new BarProp();
Window.prototype.directories= new BarProp();
Window.prototype.closed=new Boolean();
Window.prototype.opener=new Window();
Window.prototype.status="";
Window.prototype.defaultStatus="";
Window.prototype.innerWidth=0;
Window.prototype.innerHeight=0;
Window.prototype.outerWidth=0;
Window.prototype.outerHeight=0;
Window.prototype.screenX=0;
Window.prototype.screenY=0;
Window.prototype.pageXOffset=0;
Window.prototype.pageYOffset=0;
Window.prototype.scrollMaxX=0;
Window.prototype.scrollMaxY=0;
Window.prototype.fullScreen="";
Window.prototype.alert=function(arg1){};
/**
 * function confirm() 
 * @type    Object
 * @memberOf  Window
 * @returns {Object}
 */
Window.prototype.confirm=function(arg1){};
Window.prototype.focus=function(){};
Window.prototype.blur=function(){};
Window.prototype.back=function(){};
Window.prototype.forward=function(){};
Window.prototype.home=function(){};
Window.prototype.stop=function(){};
Window.prototype.print=function(){};
Window.prototype.moveTo=function(arg1,arg2){};
Window.prototype.moveBy=function(arg1,arg2){};
Window.prototype.resizeTo=function(arg1,arg2){};
Window.prototype.resizeBy=function(arg1,arg2){};
Window.prototype.scroll=function(arg1,arg2){};
Window.prototype.close=function(){};
Window.prototype.updateCommands=function(arg1){};
Window.prototype.atob=function(arg1){};
Window.prototype.btoa=function(arg1){};
Window.prototype.frameElement="";
Window.prototype.removeEventListener=function(arg1,arg2,arg3){};
Window.prototype.dispatchEvent=function(arg1){};
/**
 * function getComputedStyle() 
 * @type    Object
 * @memberOf  Window
 * @returns {Object}
 */
Window.prototype.getComputedStyle=function(arg1,arg2){};
Window.prototype.sessionStorage="";
Window.prototype.location=new Location();
Window.prototype.event="";

/**
  * Object History()

  * @super Object
  * @constructor
  * @memberOf History
  * @since Common Usage, no standard
 */
function History(){};
History.prototype=new Object();
History.prototype.history = new History();
History.prototype.length = new Number();
History.prototype.back=function(){};
History.prototype.forward=function(){};
History.prototype.go=function(arg1){};

/**
  * Object Location()

  * @super Object
  * @constructor
  * @memberOf Location
  * @since Common Usage, no standard
 */
function Location(){};
Location.prototype=new Object();
Location.prototype.location = new Location();
Location.prototype.hash = "";
Location.prototype.host = "";
Location.prototype.hostname = "";
Location.prototype.href = "";
Location.prototype.pathname = "";
Location.prototype.port = "";
Location.prototype.protocol = "";
Location.prototype.search = "";
Location.prototype.reload=function(arg1){};
Location.prototype.replace=function(arg1){};

/*
Window.prototype.navigator= new Navigator();
Window.prototype.screen= new Screen();
Window.prototype.Packages= new Package();
Window.prototype.sun= new Package();
Window.prototype.java= new Package();
Window.prototype.netscape= new Object();
Window.prototype.XPCNativeWrapper=function(){};
Window.prototype.GeckoActiveXObject=function(arg1){};
Window.prototype.Components= new nsXPCComponents();

Window.prototype.crypto= new Crypto();
Window.prototype.pkcs11= new Pkcs11();
Window.prototype.controllers= new XULControllers();

Window.prototype.globalStorage= new StorageList();
Navigator.prototype= new Array();
function Navigator(){};
*/