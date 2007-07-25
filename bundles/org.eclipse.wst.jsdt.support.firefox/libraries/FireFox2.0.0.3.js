/**
* Object Window
* @super Global
* @type constructor
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype=new Global();
function Window(){};
/**
* property location
* @type Location
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.location=new Location();
/**
* property window
* @type Window
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.window=new Window();
/**
* property property
* @type String
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.property="";
/**
* property navigator
* @type Navigator
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.navigator=new Navigator();
/**
* function addEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.addEventListener=function(arg1,arg2,arg3){};
/**
* function dump(arg1)
* @type String
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.dump=function(arg1){};
/**
* property console
* @type Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.console=new Object();
/**
* property document
* @type HTMLDocument
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.document=new HTMLDocument();
/**
* property Packages
* @type JavaPackage
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.Packages=new JavaPackage();
/**
* property sun
* @type JavaPackage
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.sun=new JavaPackage();
/**
* property java
* @type JavaPackage
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.java=new JavaPackage();
/**
* property netscape
* @type Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.netscape=new Object();
/**
* function XPCNativeWrapper()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.XPCNativeWrapper=function(){};
/**
* function GeckoActiveXObject(arg1)
* @type GeckoActiveXObject
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.GeckoActiveXObject=function(arg1){};
/**
* property Components
* @type nsXPCComponents
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.Components=new nsXPCComponents();
/**
* property parent
* @type Window
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.parent=new Window();
/**
* property top
* @type Window
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.top=new Window();
/**
* property scrollbars
* @type BarProp
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.scrollbars=new BarProp();
/**
* property name
* @type String
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.name="";
/**
* property scrollX
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.scrollX=0;
/**
* property scrollY
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.scrollY=0;
/**
* function scrollTo(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.scrollTo=function(arg1,arg2){};
/**
* function scrollBy(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.scrollBy=function(arg1,arg2){};
/**
* function getSelection()
* @type String
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.getSelection=function(){};
/**
* function scrollByLines(arg1)
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.scrollByLines=function(arg1){};
/**
* function scrollByPages(arg1)
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.scrollByPages=function(arg1){};
/**
* function sizeToContent()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.sizeToContent=function(){};
/**
* function setTimeout()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.setTimeout=function(){};
/**
* function setInterval()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.setInterval=function(){};
/**
* function clearTimeout()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.clearTimeout=function(){};
/**
* function clearInterval()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.clearInterval=function(){};
/**
* function setResizable(arg1)
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.setResizable=function(arg1){};
/**
* function captureEvents(arg1)
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.captureEvents=function(arg1){};
/**
* function releaseEvents(arg1)
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.releaseEvents=function(arg1){};
/**
* function routeEvent(arg1)
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.routeEvent=function(arg1){};
/**
* function enableExternalCapture()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.enableExternalCapture=function(){};
/**
* function disableExternalCapture()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.disableExternalCapture=function(){};
/**
* function prompt()
* @type String
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.prompt=function(){};
/**
* function open()
* @type Window
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.open=function(){};
/**
* function openDialog()
* @type Window
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.openDialog=function(){};
/**
* property frames
* @type Window
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.frames=new Window();
/**
* function find()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.find=function(){};
/**
* property self
* @type Window
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.self=new Window();
/**
* property screen
* @type Screen
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.screen=new Screen();
/**
* property history
* @type History
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.history=new History();
/**
* property content
* @type Window
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.content=new Window();
/**
* property menubar
* @type BarProp
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.menubar=new BarProp();
/**
* property toolbar
* @type BarProp
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.toolbar=new BarProp();
/**
* property locationbar
* @type BarProp
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.locationbar=new BarProp();
/**
* property personalbar
* @type BarProp
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.personalbar=new BarProp();
/**
* property statusbar
* @type BarProp
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.statusbar=new BarProp();
/**
* property directories
* @type BarProp
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.directories=new BarProp();
/**
* property closed
* @type Boolean
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.closed=false;
/**
* property crypto
* @type Crypto
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.crypto=new Crypto();
/**
* property pkcs11
* @type Pkcs11
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.pkcs11=new Pkcs11();
/**
* property controllers
* @type XULControllers
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.controllers=new XULControllers();
/**
* property opener
* @type Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.opener=new Object();
/**
* property status
* @type String
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.status="";
/**
* property defaultStatus
* @type String
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.defaultStatus="";
/**
* property innerWidth
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.innerWidth=0;
/**
* property innerHeight
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.innerHeight=0;
/**
* property outerWidth
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.outerWidth=0;
/**
* property outerHeight
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.outerHeight=0;
/**
* property screenX
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.screenX=0;
/**
* property screenY
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.screenY=0;
/**
* property pageXOffset
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.pageXOffset=0;
/**
* property pageYOffset
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.pageYOffset=0;
/**
* property scrollMaxX
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.scrollMaxX=0;
/**
* property scrollMaxY
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.scrollMaxY=0;
/**
* property length
* @type Number
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.length=0;
/**
* property fullScreen
* @type Boolean
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.fullScreen=false;
/**
* function alert(arg1)
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.alert=function(arg1){};
/**
* function confirm(arg1)
* @type Boolean
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.confirm=function(arg1){};
/**
* function focus()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.focus=function(){};
/**
* function blur()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.blur=function(){};
/**
* function back()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.back=function(){};
/**
* function forward()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.forward=function(){};
/**
* function home()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.home=function(){};
/**
* function stop()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.stop=function(){};
/**
* function print()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.print=function(){};
/**
* function moveTo(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.moveTo=function(arg1,arg2){};
/**
* function moveBy(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.moveBy=function(arg1,arg2){};
/**
* function resizeTo(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.resizeTo=function(arg1,arg2){};
/**
* function resizeBy(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.resizeBy=function(arg1,arg2){};
/**
* function scroll(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.scroll=function(arg1,arg2){};
/**
* function close()
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.close=function(){};
/**
* function updateCommands(arg1)
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.updateCommands=function(arg1){};
/**
* function atob(arg1)
* @type String
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.atob=function(arg1){};
/**
* function btoa(arg1)
* @type String
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.btoa=function(arg1){};
/**
* property frameElement
* @type Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.frameElement=new Object();
/**
* function removeEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.removeEventListener=function(arg1,arg2,arg3){};
/**
* function dispatchEvent(arg1)
* @type Boolean
* @param arg1 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.dispatchEvent=function(arg1){};
/**
* function getComputedStyle(arg1,arg2)
* @type CSSStyleDeclaration
* @param arg1 Object
* @param arg2 Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.getComputedStyle=function(arg1,arg2){};
/**
* property sessionStorage
* @type Object
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.sessionStorage=new Object();
/**
* property globalStorage
* @type StorageList
* @class Window
* @since FireFox 2.0.0.3
*/
Window.prototype.globalStorage=new StorageList();
/**
* Object Location
* @super Object
* @type constructor
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype=new Object();
function Location(){};
/**
* property hash
* @type String
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype.hash="";
/**
* property host
* @type String
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype.host="";
/**
* property hostname
* @type String
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype.hostname="";
/**
* property href
* @type String
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype.href="";
/**
* property pathname
* @type String
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype.pathname="";
/**
* property port
* @type String
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype.port="";
/**
* property protocol
* @type String
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype.protocol="";
/**
* property search
* @type String
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype.search="";
/**
* function replace(arg1)
* @param arg1 Object
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype.replace=function(arg1){};
/**
* function assign(arg1)
* @param arg1 Object
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype.assign=function(arg1){};
/**
* function reload()
* @class Location
* @since FireFox 2.0.0.3
*/
Location.prototype.reload=function(){};
/**
* Object Navigator
* @super Object
* @type constructor
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype=new Object();
function Navigator(){};
/**
* property platform
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.platform="";
/**
* property appCodeName
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.appCodeName="";
/**
* property appName
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.appName="";
/**
* property appVersion
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.appVersion="";
/**
* property language
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.language="";
/**
* property mimeTypes
* @type MimeTypeArray
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.mimeTypes=new MimeTypeArray();
/**
* property oscpu
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.oscpu="";
/**
* property vendor
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.vendor="";
/**
* property vendorSub
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.vendorSub="";
/**
* property product
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.product="";
/**
* property productSub
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.productSub="";
/**
* property plugins
* @type PluginArray
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.plugins=new PluginArray();
/**
* property securityPolicy
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.securityPolicy="";
/**
* property userAgent
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.userAgent="";
/**
* property cookieEnabled
* @type Boolean
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.cookieEnabled=false;
/**
* property onLine
* @type Boolean
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.onLine=false;
/**
* function javaEnabled()
* @type Boolean
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.javaEnabled=function(){};
/**
* function taintEnabled()
* @type Boolean
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.taintEnabled=function(){};
/**
* property buildID
* @type String
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.buildID="";
/**
* function preference()
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.preference=function(){};
/**
* function registerContentHandler(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.registerContentHandler=function(arg1,arg2,arg3){};
/**
* function registerProtocolHandler(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Navigator
* @since FireFox 2.0.0.3
*/
Navigator.prototype.registerProtocolHandler=function(arg1,arg2,arg3){};
/**
* Object MimeTypeArray
* @super Object
* @type constructor
* @class MimeTypeArray
* @since FireFox 2.0.0.3
*/
MimeTypeArray.prototype=new Object();
function MimeTypeArray(){};
/**
* property length
* @type Number
* @class MimeTypeArray
* @since FireFox 2.0.0.3
*/
MimeTypeArray.prototype.length=0;
/**
* function item(arg1)
* @type Object
* @param arg1 Object
* @class MimeTypeArray
* @since FireFox 2.0.0.3
*/
MimeTypeArray.prototype.item=function(arg1){};
/**
* function namedItem(arg1)
* @type Object
* @param arg1 Object
* @class MimeTypeArray
* @since FireFox 2.0.0.3
*/
MimeTypeArray.prototype.namedItem=function(arg1){};
/**
* Object MimeType
* @super Object
* @type constructor
* @class MimeType
* @since FireFox 2.0.0.3
*/
MimeType.prototype=new Object();
function MimeType(){};
/**
* property description
* @type String
* @class MimeType
* @since FireFox 2.0.0.3
*/
MimeType.prototype.description="";
/**
* property enabledPlugin
* @type Object
* @class MimeType
* @since FireFox 2.0.0.3
*/
MimeType.prototype.enabledPlugin=new Object();
/**
* property suffixes
* @type String
* @class MimeType
* @since FireFox 2.0.0.3
*/
MimeType.prototype.suffixes="";
/**
* property type
* @type String
* @class MimeType
* @since FireFox 2.0.0.3
*/
MimeType.prototype.type="";
/**
* Object PluginArray
* @super Object
* @type constructor
* @class PluginArray
* @since FireFox 2.0.0.3
*/
PluginArray.prototype=new Object();
function PluginArray(){};
/**
* property length
* @type Number
* @class PluginArray
* @since FireFox 2.0.0.3
*/
PluginArray.prototype.length=0;
/**
* function item(arg1)
* @type Object
* @param arg1 Object
* @class PluginArray
* @since FireFox 2.0.0.3
*/
PluginArray.prototype.item=function(arg1){};
/**
* function namedItem(arg1)
* @type Object
* @param arg1 Object
* @class PluginArray
* @since FireFox 2.0.0.3
*/
PluginArray.prototype.namedItem=function(arg1){};
/**
* function refresh()
* @class PluginArray
* @since FireFox 2.0.0.3
*/
PluginArray.prototype.refresh=function(){};
/**
* Object Plugin
* @super Object
* @type constructor
* @class Plugin
* @since FireFox 2.0.0.3
*/
Plugin.prototype=new Object();
function Plugin(){};
/**
* property length
* @type Number
* @class Plugin
* @since FireFox 2.0.0.3
*/
Plugin.prototype.length=0;
/**
* property description
* @type String
* @class Plugin
* @since FireFox 2.0.0.3
*/
Plugin.prototype.description="";
/**
* property filename
* @type String
* @class Plugin
* @since FireFox 2.0.0.3
*/
Plugin.prototype.filename="";
/**
* property name
* @type String
* @class Plugin
* @since FireFox 2.0.0.3
*/
Plugin.prototype.name="";
/**
* function item(arg1)
* @type Object
* @param arg1 Object
* @class Plugin
* @since FireFox 2.0.0.3
*/
Plugin.prototype.item=function(arg1){};
/**
* function namedItem(arg1)
* @type Object
* @param arg1 Object
* @class Plugin
* @since FireFox 2.0.0.3
*/
Plugin.prototype.namedItem=function(arg1){};
/**
* Object HTMLDocument
* @super Object
* @type constructor
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype=new Object();
function HTMLDocument(){};
/**
* function open()
* @type Window
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.open=function(){};
/**
* property location
* @type Location
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.location=new Location();
/**
* function addEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.addEventListener=function(arg1,arg2,arg3){};
/**
* property xmlVersion
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.xmlVersion=new Object();
/**
* property title
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.title="";
/**
* property referrer
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.referrer="";
/**
* property styleSheets
* @type StyleSheetList
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.styleSheets=new StyleSheetList();
/**
* property baseURI
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.baseURI="";
/**
* function compareDocumentPosition(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.compareDocumentPosition=function(arg1){};
/**
* property textContent
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.textContent=new Object();
/**
* function isSameNode(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.isSameNode=function(arg1){};
/**
* function lookupPrefix(arg1)
* @type String
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.lookupPrefix=function(arg1){};
/**
* function isDefaultNamespace(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.isDefaultNamespace=function(arg1){};
/**
* function lookupNamespaceURI(arg1)
* @type String
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.lookupNamespaceURI=function(arg1){};
/**
* function isEqualNode(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.isEqualNode=function(arg1){};
/**
* function getFeature(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.getFeature=function(arg1,arg2){};
/**
* function setUserData(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.setUserData=function(arg1,arg2,arg3){};
/**
* function getUserData(arg1)
* @type Object
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.getUserData=function(arg1){};
/**
* property DOCUMENT_POSITION_DISCONNECTED
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.DOCUMENT_POSITION_DISCONNECTED=0;
/**
* property DOCUMENT_POSITION_PRECEDING
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.DOCUMENT_POSITION_PRECEDING=0;
/**
* property DOCUMENT_POSITION_FOLLOWING
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.DOCUMENT_POSITION_FOLLOWING=0;
/**
* property DOCUMENT_POSITION_CONTAINS
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.DOCUMENT_POSITION_CONTAINS=0;
/**
* property DOCUMENT_POSITION_CONTAINED_BY
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.DOCUMENT_POSITION_CONTAINED_BY=0;
/**
* property DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC=0;
/**
* property nodeName
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.nodeName="";
/**
* property nodeValue
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.nodeValue=new Object();
/**
* property nodeType
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.nodeType=0;
/**
* property parentNode
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.parentNode=new Object();
/**
* property childNodes
* @type NodeList
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.childNodes=new NodeList();
/**
* property firstChild
* @type DocumentType
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.firstChild=new DocumentType();
/**
* property lastChild
* @type HTMLHtmlElement
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.lastChild=new HTMLHtmlElement();
/**
* property previousSibling
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.previousSibling=new Object();
/**
* property nextSibling
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.nextSibling=new Object();
/**
* property attributes
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.attributes=new Object();
/**
* property ownerDocument
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.ownerDocument=new Object();
/**
* function insertBefore(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.insertBefore=function(arg1,arg2){};
/**
* function replaceChild(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.replaceChild=function(arg1,arg2){};
/**
* function removeChild(arg1)
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.removeChild=function(arg1){};
/**
* function appendChild(arg1)
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.appendChild=function(arg1){};
/**
* function hasChildNodes()
* @type Boolean
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.hasChildNodes=function(){};
/**
* function cloneNode(arg1)
* @type Element
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.cloneNode=function(arg1){};
/**
* function normalize()
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.normalize=function(){};
/**
* function isSupported(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.isSupported=function(arg1,arg2){};
/**
* property namespaceURI
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.namespaceURI=new Object();
/**
* property prefix
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.prefix=new Object();
/**
* property localName
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.localName=new Object();
/**
* function hasAttributes()
* @type Boolean
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.hasAttributes=function(){};
/**
* property doctype
* @type DocumentType
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.doctype=new DocumentType();
/**
* property implementation
* @type DOMImplementation
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.implementation=new DOMImplementation();
/**
* property documentElement
* @type HTMLHtmlElement
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.documentElement=new HTMLHtmlElement();
/**
* function createElement(arg1)
* @type Element
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createElement=function(arg1){};
/**
* function createDocumentFragment()
* @type Element
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createDocumentFragment=function(){};
/**
* function createTextNode(arg1)
* @type Element
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createTextNode=function(arg1){};
/**
* function createComment(arg1)
* @type Element
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createComment=function(arg1){};
/**
* function createCDATASection(arg1)
* @type Text
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createCDATASection=function(arg1){};
/**
* function createProcessingInstruction(arg1,arg2)
* @type Element
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createProcessingInstruction=function(arg1,arg2){};
/**
* function createAttribute(arg1)
* @type Attr
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createAttribute=function(arg1){};
/**
* function createEntityReference(arg1)
* @type Element
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createEntityReference=function(arg1){};
/**
* function getElementsByTagName(arg1)
* @type NodeList
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.getElementsByTagName=function(arg1){};
/**
* function importNode(arg1,arg2)
* @type Element
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.importNode=function(arg1,arg2){};
/**
* function createElementNS(arg1,arg2)
* @type Element
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createElementNS=function(arg1,arg2){};
/**
* function createAttributeNS(arg1,arg2)
* @type Element
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createAttributeNS=function(arg1,arg2){};
/**
* function getElementsByTagNameNS(arg1,arg2)
* @type NodeList
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.getElementsByTagNameNS=function(arg1,arg2){};
/**
* function getElementById(arg1)
* @type Element
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.getElementById=function(arg1){};
/**
* property ELEMENT_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.ELEMENT_NODE=0;
/**
* property ATTRIBUTE_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.ATTRIBUTE_NODE=0;
/**
* property TEXT_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.TEXT_NODE=0;
/**
* property CDATA_SECTION_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.CDATA_SECTION_NODE=0;
/**
* property ENTITY_REFERENCE_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.ENTITY_REFERENCE_NODE=0;
/**
* property ENTITY_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.ENTITY_NODE=0;
/**
* property PROCESSING_INSTRUCTION_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.PROCESSING_INSTRUCTION_NODE=0;
/**
* property COMMENT_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.COMMENT_NODE=0;
/**
* property DOCUMENT_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.DOCUMENT_NODE=0;
/**
* property DOCUMENT_TYPE_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.DOCUMENT_TYPE_NODE=0;
/**
* property DOCUMENT_FRAGMENT_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.DOCUMENT_FRAGMENT_NODE=0;
/**
* property NOTATION_NODE
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.NOTATION_NODE=0;
/**
* property URL
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.URL="";
/**
* property body
* @type HTMLBodyElement
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.body=new HTMLBodyElement();
/**
* property images
* @type HTMLCollection
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.images=new HTMLCollection();
/**
* property applets
* @type HTMLCollection
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.applets=new HTMLCollection();
/**
* property links
* @type HTMLCollection
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.links=new HTMLCollection();
/**
* property forms
* @type HTMLCollection
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.forms=new HTMLCollection();
/**
* property anchors
* @type HTMLCollection
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.anchors=new HTMLCollection();
/**
* property cookie
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.cookie="";
/**
* function close()
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.close=function(){};
/**
* function getElementsByName(arg1)
* @type Element
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.getElementsByName=function(arg1){};
/**
* property width
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.width=0;
/**
* property height
* @type Number
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.height=0;
/**
* property alinkColor
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.alinkColor="";
/**
* property linkColor
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.linkColor="";
/**
* property vlinkColor
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.vlinkColor="";
/**
* property bgColor
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.bgColor="";
/**
* property fgColor
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.fgColor="";
/**
* property domain
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.domain="";
/**
* property embeds
* @type HTMLCollection
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.embeds=new HTMLCollection();
/**
* function getSelection()
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.getSelection=function(){};
/**
* function write()
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.write=function(){};
/**
* function writeln()
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.writeln=function(){};
/**
* function clear()
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.clear=function(){};
/**
* function captureEvents(arg1)
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.captureEvents=function(arg1){};
/**
* function releaseEvents(arg1)
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.releaseEvents=function(arg1){};
/**
* function routeEvent(arg1)
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.routeEvent=function(arg1){};
/**
* property compatMode
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.compatMode="";
/**
* property plugins
* @type HTMLCollection
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.plugins=new HTMLCollection();
/**
* property designMode
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.designMode="";
/**
* function execCommand(arg1,arg2,arg3)
* @type Object
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.execCommand=function(arg1,arg2,arg3){};
/**
* function execCommandShowHelp(arg1)
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.execCommandShowHelp=function(arg1){};
/**
* function queryCommandEnabled(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.queryCommandEnabled=function(arg1){};
/**
* function queryCommandIndeterm(arg1)
* @type Object
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.queryCommandIndeterm=function(arg1){};
/**
* function queryCommandState(arg1)
* @type Object
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.queryCommandState=function(arg1){};
/**
* function queryCommandSupported(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.queryCommandSupported=function(arg1){};
/**
* function queryCommandText(arg1)
* @type String
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.queryCommandText=function(arg1){};
/**
* function queryCommandValue(arg1)
* @type Object
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.queryCommandValue=function(arg1){};
/**
* property characterSet
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.characterSet="";
/**
* property dir
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.dir="";
/**
* property contentType
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.contentType="";
/**
* property lastModified
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.lastModified="";
/**
* function getBoxObjectFor(arg1)
* @type Object
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.getBoxObjectFor=function(arg1){};
/**
* function setBoxObjectFor(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.setBoxObjectFor=function(arg1,arg2){};
/**
* function createEvent(arg1)
* @type Event
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createEvent=function(arg1){};
/**
* property preferredStylesheetSet
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.preferredStylesheetSet="";
/**
* property defaultView
* @type Window
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.defaultView=new Window();
/**
* function createRange()
* @type Range
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createRange=function(){};
/**
* function createNodeIterator(arg1,arg2,arg3,arg4)
* @type Iterator
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @param arg4 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createNodeIterator=function(arg1,arg2,arg3,arg4){};
/**
* function createTreeWalker(arg1,arg2,arg3,arg4)
* @type TreeWalker
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @param arg4 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createTreeWalker=function(arg1,arg2,arg3,arg4){};
/**
* function getAnonymousNodes(arg1)
* @type NodeList
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.getAnonymousNodes=function(arg1){};
/**
* function getAnonymousElementByAttribute(arg1,arg2,arg3)
* @type Element
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.getAnonymousElementByAttribute=function(arg1,arg2,arg3){};
/**
* function addBinding(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.addBinding=function(arg1,arg2){};
/**
* function removeBinding(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.removeBinding=function(arg1,arg2){};
/**
* function getBindingParent(arg1)
* @type Object
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.getBindingParent=function(arg1){};
/**
* function loadBindingDocument(arg1)
* @type HTMLDocument
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.loadBindingDocument=function(arg1){};
/**
* function removeEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.removeEventListener=function(arg1,arg2,arg3){};
/**
* function dispatchEvent(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.dispatchEvent=function(arg1){};
/**
* property inputEncoding
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.inputEncoding="";
/**
* property xmlEncoding
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.xmlEncoding=new Object();
/**
* property xmlStandalone
* @type Boolean
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.xmlStandalone=false;
/**
* property strictErrorChecking
* @type Boolean
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.strictErrorChecking=false;
/**
* property documentURI
* @type String
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.documentURI="";
/**
* function adoptNode(arg1)
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.adoptNode=function(arg1){};
/**
* property domConfig
* @type Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.domConfig=new Object();
/**
* function normalizeDocument()
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.normalizeDocument=function(){};
/**
* function renameNode(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.renameNode=function(arg1,arg2,arg3){};
/**
* function createExpression(arg1,arg2)
* @type RegExp
* @param arg1 Object
* @param arg2 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createExpression=function(arg1,arg2){};
/**
* function createNSResolver(arg1)
* @type Object
* @param arg1 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.createNSResolver=function(arg1){};
/**
* function evaluate(arg1,arg2,arg3,arg4,arg5)
* @type HTMLDocument
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @param arg4 Object
* @param arg5 Object
* @class HTMLDocument
* @since FireFox 2.0.0.3
*/
HTMLDocument.prototype.evaluate=function(arg1,arg2,arg3,arg4,arg5){};
/**
* Object StyleSheetList
* @super Object
* @type constructor
* @class StyleSheetList
* @since FireFox 2.0.0.3
*/
StyleSheetList.prototype=new Object();
function StyleSheetList(){};
/**
* property length
* @type Number
* @class StyleSheetList
* @since FireFox 2.0.0.3
*/
StyleSheetList.prototype.length=0;
/**
* function item(arg1)
* @type Object
* @param arg1 Object
* @class StyleSheetList
* @since FireFox 2.0.0.3
*/
StyleSheetList.prototype.item=function(arg1){};
/**
* Object NodeList
* @super Object
* @type constructor
* @class NodeList
* @since FireFox 2.0.0.3
*/
NodeList.prototype=new Object();
function NodeList(){};
/**
* property length
* @type Number
* @class NodeList
* @since FireFox 2.0.0.3
*/
NodeList.prototype.length=0;
/**
* function item(arg1)
* @type Object
* @param arg1 Object
* @class NodeList
* @since FireFox 2.0.0.3
*/
NodeList.prototype.item=function(arg1){};
/**
* Object DocumentType
* @super Object
* @type constructor
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype=new Object();
function DocumentType(){};
/**
* property nodeName
* @type String
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.nodeName="";
/**
* property nodeValue
* @type Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.nodeValue=new Object();
/**
* property nodeType
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.nodeType=0;
/**
* property parentNode
* @type HTMLDocument
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.parentNode=new HTMLDocument();
/**
* property childNodes
* @type NodeList
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.childNodes=new NodeList();
/**
* property firstChild
* @type Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.firstChild=new Object();
/**
* property lastChild
* @type Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.lastChild=new Object();
/**
* property previousSibling
* @type Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.previousSibling=new Object();
/**
* property nextSibling
* @type HTMLHtmlElement
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.nextSibling=new HTMLHtmlElement();
/**
* property attributes
* @type Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.attributes=new Object();
/**
* property ownerDocument
* @type HTMLDocument
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.ownerDocument=new HTMLDocument();
/**
* function insertBefore(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.insertBefore=function(arg1,arg2){};
/**
* function replaceChild(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.replaceChild=function(arg1,arg2){};
/**
* function removeChild(arg1)
* @param arg1 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.removeChild=function(arg1){};
/**
* function appendChild(arg1)
* @param arg1 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.appendChild=function(arg1){};
/**
* function hasChildNodes()
* @type Boolean
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.hasChildNodes=function(){};
/**
* function cloneNode(arg1)
* @type Element
* @param arg1 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.cloneNode=function(arg1){};
/**
* function normalize()
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.normalize=function(){};
/**
* function isSupported(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.isSupported=function(arg1,arg2){};
/**
* property namespaceURI
* @type Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.namespaceURI=new Object();
/**
* property prefix
* @type Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.prefix=new Object();
/**
* property localName
* @type Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.localName=new Object();
/**
* function hasAttributes()
* @type Boolean
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.hasAttributes=function(){};
/**
* property name
* @type String
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.name="";
/**
* property entities
* @type Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.entities=new Object();
/**
* property notations
* @type Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.notations=new Object();
/**
* property publicId
* @type String
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.publicId="";
/**
* property systemId
* @type String
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.systemId="";
/**
* property internalSubset
* @type String
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.internalSubset="";
/**
* property ELEMENT_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.ELEMENT_NODE=0;
/**
* property ATTRIBUTE_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.ATTRIBUTE_NODE=0;
/**
* property TEXT_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.TEXT_NODE=0;
/**
* property CDATA_SECTION_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.CDATA_SECTION_NODE=0;
/**
* property ENTITY_REFERENCE_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.ENTITY_REFERENCE_NODE=0;
/**
* property ENTITY_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.ENTITY_NODE=0;
/**
* property PROCESSING_INSTRUCTION_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.PROCESSING_INSTRUCTION_NODE=0;
/**
* property COMMENT_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.COMMENT_NODE=0;
/**
* property DOCUMENT_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.DOCUMENT_NODE=0;
/**
* property DOCUMENT_TYPE_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.DOCUMENT_TYPE_NODE=0;
/**
* property DOCUMENT_FRAGMENT_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.DOCUMENT_FRAGMENT_NODE=0;
/**
* property NOTATION_NODE
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.NOTATION_NODE=0;
/**
* property baseURI
* @type String
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.baseURI="";
/**
* function compareDocumentPosition(arg1)
* @type Boolean
* @param arg1 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.compareDocumentPosition=function(arg1){};
/**
* property textContent
* @type Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.textContent=new Object();
/**
* function isSameNode(arg1)
* @type Boolean
* @param arg1 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.isSameNode=function(arg1){};
/**
* function lookupPrefix(arg1)
* @type String
* @param arg1 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.lookupPrefix=function(arg1){};
/**
* function isDefaultNamespace(arg1)
* @type Boolean
* @param arg1 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.isDefaultNamespace=function(arg1){};
/**
* function lookupNamespaceURI(arg1)
* @type String
* @param arg1 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.lookupNamespaceURI=function(arg1){};
/**
* function isEqualNode(arg1)
* @type Boolean
* @param arg1 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.isEqualNode=function(arg1){};
/**
* function getFeature(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.getFeature=function(arg1,arg2){};
/**
* function setUserData(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.setUserData=function(arg1,arg2,arg3){};
/**
* function getUserData(arg1)
* @type Object
* @param arg1 Object
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.getUserData=function(arg1){};
/**
* property DOCUMENT_POSITION_DISCONNECTED
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.DOCUMENT_POSITION_DISCONNECTED=0;
/**
* property DOCUMENT_POSITION_PRECEDING
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.DOCUMENT_POSITION_PRECEDING=0;
/**
* property DOCUMENT_POSITION_FOLLOWING
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.DOCUMENT_POSITION_FOLLOWING=0;
/**
* property DOCUMENT_POSITION_CONTAINS
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.DOCUMENT_POSITION_CONTAINS=0;
/**
* property DOCUMENT_POSITION_CONTAINED_BY
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.DOCUMENT_POSITION_CONTAINED_BY=0;
/**
* property DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
* @type Number
* @class DocumentType
* @since FireFox 2.0.0.3
*/
DocumentType.prototype.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC=0;
/**
* Object HTMLHtmlElement
* @super Object
* @type constructor
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype=new Object();
function HTMLHtmlElement(){};
/**
* function addEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.addEventListener=function(arg1,arg2,arg3){};
/**
* property nodeName
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.nodeName="";
/**
* property nodeValue
* @type Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.nodeValue=new Object();
/**
* property nodeType
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.nodeType=0;
/**
* property parentNode
* @type HTMLDocument
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.parentNode=new HTMLDocument();
/**
* property childNodes
* @type NodeList
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.childNodes=new NodeList();
/**
* property firstChild
* @type HTMLHeadElement
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.firstChild=new HTMLHeadElement();
/**
* property lastChild
* @type HTMLBodyElement
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.lastChild=new HTMLBodyElement();
/**
* property previousSibling
* @type DocumentType
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.previousSibling=new DocumentType();
/**
* property nextSibling
* @type Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.nextSibling=new Object();
/**
* property attributes
* @type NamedNodeMap
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.attributes=new NamedNodeMap();
/**
* property ownerDocument
* @type HTMLDocument
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.ownerDocument=new HTMLDocument();
/**
* function insertBefore(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.insertBefore=function(arg1,arg2){};
/**
* function replaceChild(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.replaceChild=function(arg1,arg2){};
/**
* function removeChild(arg1)
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.removeChild=function(arg1){};
/**
* function appendChild(arg1)
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.appendChild=function(arg1){};
/**
* function hasChildNodes()
* @type Boolean
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.hasChildNodes=function(){};
/**
* function cloneNode(arg1)
* @type Element
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.cloneNode=function(arg1){};
/**
* function normalize()
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.normalize=function(){};
/**
* function isSupported(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.isSupported=function(arg1,arg2){};
/**
* property namespaceURI
* @type Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.namespaceURI=new Object();
/**
* property prefix
* @type Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.prefix=new Object();
/**
* property localName
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.localName="";
/**
* function hasAttributes()
* @type Boolean
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.hasAttributes=function(){};
/**
* property ELEMENT_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.ELEMENT_NODE=0;
/**
* property ATTRIBUTE_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.ATTRIBUTE_NODE=0;
/**
* property TEXT_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.TEXT_NODE=0;
/**
* property CDATA_SECTION_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.CDATA_SECTION_NODE=0;
/**
* property ENTITY_REFERENCE_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.ENTITY_REFERENCE_NODE=0;
/**
* property ENTITY_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.ENTITY_NODE=0;
/**
* property PROCESSING_INSTRUCTION_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.PROCESSING_INSTRUCTION_NODE=0;
/**
* property COMMENT_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.COMMENT_NODE=0;
/**
* property DOCUMENT_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.DOCUMENT_NODE=0;
/**
* property DOCUMENT_TYPE_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.DOCUMENT_TYPE_NODE=0;
/**
* property DOCUMENT_FRAGMENT_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.DOCUMENT_FRAGMENT_NODE=0;
/**
* property NOTATION_NODE
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.NOTATION_NODE=0;
/**
* property tagName
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.tagName="";
/**
* function getAttribute(arg1)
* @type Attr
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.getAttribute=function(arg1){};
/**
* function setAttribute(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.setAttribute=function(arg1,arg2){};
/**
* function removeAttribute(arg1)
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.removeAttribute=function(arg1){};
/**
* function getAttributeNode(arg1)
* @type Element
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.getAttributeNode=function(arg1){};
/**
* function setAttributeNode(arg1)
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.setAttributeNode=function(arg1){};
/**
* function removeAttributeNode(arg1)
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.removeAttributeNode=function(arg1){};
/**
* function getElementsByTagName(arg1)
* @type NodeList
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.getElementsByTagName=function(arg1){};
/**
* function getAttributeNS(arg1,arg2)
* @type Object
* @param arg1 Object
* @param arg2 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.getAttributeNS=function(arg1,arg2){};
/**
* function setAttributeNS(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.setAttributeNS=function(arg1,arg2,arg3){};
/**
* function removeAttributeNS(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.removeAttributeNS=function(arg1,arg2){};
/**
* function getAttributeNodeNS(arg1,arg2)
* @type Object
* @param arg1 Object
* @param arg2 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.getAttributeNodeNS=function(arg1,arg2){};
/**
* function setAttributeNodeNS(arg1)
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.setAttributeNodeNS=function(arg1){};
/**
* function getElementsByTagNameNS(arg1,arg2)
* @type NodeList
* @param arg1 Object
* @param arg2 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.getElementsByTagNameNS=function(arg1,arg2){};
/**
* function hasAttribute(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.hasAttribute=function(arg1){};
/**
* function hasAttributeNS(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.hasAttributeNS=function(arg1,arg2){};
/**
* property id
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.id="";
/**
* property title
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.title="";
/**
* property lang
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.lang="";
/**
* property dir
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.dir="";
/**
* property className
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.className="";
/**
* property version
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.version="";
/**
* property offsetTop
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.offsetTop=0;
/**
* property offsetLeft
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.offsetLeft=0;
/**
* property offsetWidth
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.offsetWidth=0;
/**
* property offsetHeight
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.offsetHeight=0;
/**
* property offsetParent
* @type Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.offsetParent=new Object();
/**
* property innerHTML
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.innerHTML="";
/**
* property scrollTop
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.scrollTop=0;
/**
* property scrollLeft
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.scrollLeft=0;
/**
* property scrollHeight
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.scrollHeight=0;
/**
* property scrollWidth
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.scrollWidth=0;
/**
* property clientHeight
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.clientHeight=0;
/**
* property clientWidth
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.clientWidth=0;
/**
* property tabIndex
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.tabIndex=0;
/**
* function blur()
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.blur=function(){};
/**
* function focus()
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.focus=function(){};
/**
* property spellcheck
* @type Boolean
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.spellcheck=false;
/**
* property style
* @type CSSStyleDeclaration
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.style=new CSSStyleDeclaration();
/**
* function removeEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.removeEventListener=function(arg1,arg2,arg3){};
/**
* function dispatchEvent(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.dispatchEvent=function(arg1){};
/**
* property baseURI
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.baseURI="";
/**
* function compareDocumentPosition(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.compareDocumentPosition=function(arg1){};
/**
* property textContent
* @type String
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.textContent="";
/**
* function isSameNode(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.isSameNode=function(arg1){};
/**
* function lookupPrefix(arg1)
* @type String
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.lookupPrefix=function(arg1){};
/**
* function isDefaultNamespace(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.isDefaultNamespace=function(arg1){};
/**
* function lookupNamespaceURI(arg1)
* @type String
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.lookupNamespaceURI=function(arg1){};
/**
* function isEqualNode(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.isEqualNode=function(arg1){};
/**
* function getFeature(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.getFeature=function(arg1,arg2){};
/**
* function setUserData(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.setUserData=function(arg1,arg2,arg3){};
/**
* function getUserData(arg1)
* @type Object
* @param arg1 Object
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.getUserData=function(arg1){};
/**
* property DOCUMENT_POSITION_DISCONNECTED
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.DOCUMENT_POSITION_DISCONNECTED=0;
/**
* property DOCUMENT_POSITION_PRECEDING
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.DOCUMENT_POSITION_PRECEDING=0;
/**
* property DOCUMENT_POSITION_FOLLOWING
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.DOCUMENT_POSITION_FOLLOWING=0;
/**
* property DOCUMENT_POSITION_CONTAINS
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.DOCUMENT_POSITION_CONTAINS=0;
/**
* property DOCUMENT_POSITION_CONTAINED_BY
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.DOCUMENT_POSITION_CONTAINED_BY=0;
/**
* property DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
* @type Number
* @class HTMLHtmlElement
* @since FireFox 2.0.0.3
*/
HTMLHtmlElement.prototype.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC=0;
/**
* Object HTMLHeadElement
* @super Object
* @type constructor
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype=new Object();
function HTMLHeadElement(){};
/**
* function addEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.addEventListener=function(arg1,arg2,arg3){};
/**
* property nodeName
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.nodeName="";
/**
* property nodeValue
* @type Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.nodeValue=new Object();
/**
* property nodeType
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.nodeType=0;
/**
* property parentNode
* @type HTMLHtmlElement
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.parentNode=new HTMLHtmlElement();
/**
* property childNodes
* @type NodeList
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.childNodes=new NodeList();
/**
* property firstChild
* @type Comment
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.firstChild=new Comment();
/**
* property lastChild
* @type HTMLScriptElement
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.lastChild=new HTMLScriptElement();
/**
* property previousSibling
* @type Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.previousSibling=new Object();
/**
* property nextSibling
* @type HTMLBodyElement
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.nextSibling=new HTMLBodyElement();
/**
* property attributes
* @type NamedNodeMap
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.attributes=new NamedNodeMap();
/**
* property ownerDocument
* @type HTMLDocument
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.ownerDocument=new HTMLDocument();
/**
* function insertBefore(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.insertBefore=function(arg1,arg2){};
/**
* function replaceChild(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.replaceChild=function(arg1,arg2){};
/**
* function removeChild(arg1)
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.removeChild=function(arg1){};
/**
* function appendChild(arg1)
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.appendChild=function(arg1){};
/**
* function hasChildNodes()
* @type Boolean
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.hasChildNodes=function(){};
/**
* function cloneNode(arg1)
* @type Element
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.cloneNode=function(arg1){};
/**
* function normalize()
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.normalize=function(){};
/**
* function isSupported(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.isSupported=function(arg1,arg2){};
/**
* property namespaceURI
* @type Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.namespaceURI=new Object();
/**
* property prefix
* @type Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.prefix=new Object();
/**
* property localName
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.localName="";
/**
* function hasAttributes()
* @type Boolean
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.hasAttributes=function(){};
/**
* property ELEMENT_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.ELEMENT_NODE=0;
/**
* property ATTRIBUTE_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.ATTRIBUTE_NODE=0;
/**
* property TEXT_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.TEXT_NODE=0;
/**
* property CDATA_SECTION_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.CDATA_SECTION_NODE=0;
/**
* property ENTITY_REFERENCE_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.ENTITY_REFERENCE_NODE=0;
/**
* property ENTITY_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.ENTITY_NODE=0;
/**
* property PROCESSING_INSTRUCTION_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.PROCESSING_INSTRUCTION_NODE=0;
/**
* property COMMENT_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.COMMENT_NODE=0;
/**
* property DOCUMENT_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.DOCUMENT_NODE=0;
/**
* property DOCUMENT_TYPE_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.DOCUMENT_TYPE_NODE=0;
/**
* property DOCUMENT_FRAGMENT_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.DOCUMENT_FRAGMENT_NODE=0;
/**
* property NOTATION_NODE
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.NOTATION_NODE=0;
/**
* property tagName
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.tagName="";
/**
* function getAttribute(arg1)
* @type Attr
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.getAttribute=function(arg1){};
/**
* function setAttribute(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.setAttribute=function(arg1,arg2){};
/**
* function removeAttribute(arg1)
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.removeAttribute=function(arg1){};
/**
* function getAttributeNode(arg1)
* @type Element
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.getAttributeNode=function(arg1){};
/**
* function setAttributeNode(arg1)
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.setAttributeNode=function(arg1){};
/**
* function removeAttributeNode(arg1)
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.removeAttributeNode=function(arg1){};
/**
* function getElementsByTagName(arg1)
* @type NodeList
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.getElementsByTagName=function(arg1){};
/**
* function getAttributeNS(arg1,arg2)
* @type Object
* @param arg1 Object
* @param arg2 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.getAttributeNS=function(arg1,arg2){};
/**
* function setAttributeNS(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.setAttributeNS=function(arg1,arg2,arg3){};
/**
* function removeAttributeNS(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.removeAttributeNS=function(arg1,arg2){};
/**
* function getAttributeNodeNS(arg1,arg2)
* @type Object
* @param arg1 Object
* @param arg2 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.getAttributeNodeNS=function(arg1,arg2){};
/**
* function setAttributeNodeNS(arg1)
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.setAttributeNodeNS=function(arg1){};
/**
* function getElementsByTagNameNS(arg1,arg2)
* @type NodeList
* @param arg1 Object
* @param arg2 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.getElementsByTagNameNS=function(arg1,arg2){};
/**
* function hasAttribute(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.hasAttribute=function(arg1){};
/**
* function hasAttributeNS(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.hasAttributeNS=function(arg1,arg2){};
/**
* property id
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.id="";
/**
* property title
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.title="";
/**
* property lang
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.lang="";
/**
* property dir
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.dir="";
/**
* property className
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.className="";
/**
* property profile
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.profile="";
/**
* property offsetTop
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.offsetTop=0;
/**
* property offsetLeft
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.offsetLeft=0;
/**
* property offsetWidth
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.offsetWidth=0;
/**
* property offsetHeight
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.offsetHeight=0;
/**
* property offsetParent
* @type Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.offsetParent=new Object();
/**
* property innerHTML
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.innerHTML="";
/**
* property scrollTop
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.scrollTop=0;
/**
* property scrollLeft
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.scrollLeft=0;
/**
* property scrollHeight
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.scrollHeight=0;
/**
* property scrollWidth
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.scrollWidth=0;
/**
* property clientHeight
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.clientHeight=0;
/**
* property clientWidth
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.clientWidth=0;
/**
* property tabIndex
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.tabIndex=0;
/**
* function blur()
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.blur=function(){};
/**
* function focus()
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.focus=function(){};
/**
* property spellcheck
* @type Boolean
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.spellcheck=false;
/**
* property style
* @type CSSStyleDeclaration
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.style=new CSSStyleDeclaration();
/**
* function removeEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.removeEventListener=function(arg1,arg2,arg3){};
/**
* function dispatchEvent(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.dispatchEvent=function(arg1){};
/**
* property baseURI
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.baseURI="";
/**
* function compareDocumentPosition(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.compareDocumentPosition=function(arg1){};
/**
* property textContent
* @type String
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.textContent="";
/**
* function isSameNode(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.isSameNode=function(arg1){};
/**
* function lookupPrefix(arg1)
* @type String
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.lookupPrefix=function(arg1){};
/**
* function isDefaultNamespace(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.isDefaultNamespace=function(arg1){};
/**
* function lookupNamespaceURI(arg1)
* @type String
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.lookupNamespaceURI=function(arg1){};
/**
* function isEqualNode(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.isEqualNode=function(arg1){};
/**
* function getFeature(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.getFeature=function(arg1,arg2){};
/**
* function setUserData(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.setUserData=function(arg1,arg2,arg3){};
/**
* function getUserData(arg1)
* @type Object
* @param arg1 Object
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.getUserData=function(arg1){};
/**
* property DOCUMENT_POSITION_DISCONNECTED
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.DOCUMENT_POSITION_DISCONNECTED=0;
/**
* property DOCUMENT_POSITION_PRECEDING
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.DOCUMENT_POSITION_PRECEDING=0;
/**
* property DOCUMENT_POSITION_FOLLOWING
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.DOCUMENT_POSITION_FOLLOWING=0;
/**
* property DOCUMENT_POSITION_CONTAINS
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.DOCUMENT_POSITION_CONTAINS=0;
/**
* property DOCUMENT_POSITION_CONTAINED_BY
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.DOCUMENT_POSITION_CONTAINED_BY=0;
/**
* property DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
* @type Number
* @class HTMLHeadElement
* @since FireFox 2.0.0.3
*/
HTMLHeadElement.prototype.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC=0;
/**
* Object Comment
* @super Object
* @type constructor
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype=new Object();
function Comment(){};
/**
* function addEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.addEventListener=function(arg1,arg2,arg3){};
/**
* property nodeName
* @type String
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.nodeName="";
/**
* property nodeValue
* @type String
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.nodeValue="";
/**
* property nodeType
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.nodeType=0;
/**
* property parentNode
* @type HTMLHeadElement
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.parentNode=new HTMLHeadElement();
/**
* property childNodes
* @type NodeList
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.childNodes=new NodeList();
/**
* property firstChild
* @type Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.firstChild=new Object();
/**
* property lastChild
* @type Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.lastChild=new Object();
/**
* property previousSibling
* @type Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.previousSibling=new Object();
/**
* property nextSibling
* @type Comment
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.nextSibling=new Comment();
/**
* property attributes
* @type Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.attributes=new Object();
/**
* property ownerDocument
* @type HTMLDocument
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.ownerDocument=new HTMLDocument();
/**
* function insertBefore(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.insertBefore=function(arg1,arg2){};
/**
* function replaceChild(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.replaceChild=function(arg1,arg2){};
/**
* function removeChild(arg1)
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.removeChild=function(arg1){};
/**
* function appendChild(arg1)
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.appendChild=function(arg1){};
/**
* function hasChildNodes()
* @type Boolean
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.hasChildNodes=function(){};
/**
* function cloneNode(arg1)
* @type Element
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.cloneNode=function(arg1){};
/**
* function normalize()
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.normalize=function(){};
/**
* function isSupported(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.isSupported=function(arg1,arg2){};
/**
* property namespaceURI
* @type Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.namespaceURI=new Object();
/**
* property prefix
* @type Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.prefix=new Object();
/**
* property localName
* @type Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.localName=new Object();
/**
* function hasAttributes()
* @type Boolean
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.hasAttributes=function(){};
/**
* property ELEMENT_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.ELEMENT_NODE=0;
/**
* property ATTRIBUTE_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.ATTRIBUTE_NODE=0;
/**
* property TEXT_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.TEXT_NODE=0;
/**
* property CDATA_SECTION_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.CDATA_SECTION_NODE=0;
/**
* property ENTITY_REFERENCE_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.ENTITY_REFERENCE_NODE=0;
/**
* property ENTITY_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.ENTITY_NODE=0;
/**
* property PROCESSING_INSTRUCTION_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.PROCESSING_INSTRUCTION_NODE=0;
/**
* property COMMENT_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.COMMENT_NODE=0;
/**
* property DOCUMENT_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.DOCUMENT_NODE=0;
/**
* property DOCUMENT_TYPE_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.DOCUMENT_TYPE_NODE=0;
/**
* property DOCUMENT_FRAGMENT_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.DOCUMENT_FRAGMENT_NODE=0;
/**
* property NOTATION_NODE
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.NOTATION_NODE=0;
/**
* property data
* @type String
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.data="";
/**
* property length
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.length=0;
/**
* function substringData(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.substringData=function(arg1,arg2){};
/**
* function appendData(arg1)
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.appendData=function(arg1){};
/**
* function insertData(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.insertData=function(arg1,arg2){};
/**
* function deleteData(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.deleteData=function(arg1,arg2){};
/**
* function replaceData(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.replaceData=function(arg1,arg2,arg3){};
/**
* function removeEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.removeEventListener=function(arg1,arg2,arg3){};
/**
* function dispatchEvent(arg1)
* @type Boolean
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.dispatchEvent=function(arg1){};
/**
* property baseURI
* @type String
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.baseURI="";
/**
* function compareDocumentPosition(arg1)
* @type Boolean
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.compareDocumentPosition=function(arg1){};
/**
* property textContent
* @type String
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.textContent="";
/**
* function isSameNode(arg1)
* @type Boolean
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.isSameNode=function(arg1){};
/**
* function lookupPrefix(arg1)
* @type String
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.lookupPrefix=function(arg1){};
/**
* function isDefaultNamespace(arg1)
* @type Boolean
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.isDefaultNamespace=function(arg1){};
/**
* function lookupNamespaceURI(arg1)
* @type String
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.lookupNamespaceURI=function(arg1){};
/**
* function isEqualNode(arg1)
* @type Boolean
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.isEqualNode=function(arg1){};
/**
* function getFeature(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.getFeature=function(arg1,arg2){};
/**
* function setUserData(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.setUserData=function(arg1,arg2,arg3){};
/**
* function getUserData(arg1)
* @type Object
* @param arg1 Object
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.getUserData=function(arg1){};
/**
* property DOCUMENT_POSITION_DISCONNECTED
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.DOCUMENT_POSITION_DISCONNECTED=0;
/**
* property DOCUMENT_POSITION_PRECEDING
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.DOCUMENT_POSITION_PRECEDING=0;
/**
* property DOCUMENT_POSITION_FOLLOWING
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.DOCUMENT_POSITION_FOLLOWING=0;
/**
* property DOCUMENT_POSITION_CONTAINS
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.DOCUMENT_POSITION_CONTAINS=0;
/**
* property DOCUMENT_POSITION_CONTAINED_BY
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.DOCUMENT_POSITION_CONTAINED_BY=0;
/**
* property DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
* @type Number
* @class Comment
* @since FireFox 2.0.0.3
*/
Comment.prototype.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC=0;
/**
* Object HTMLScriptElement
* @super Object
* @type constructor
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype=new Object();
function HTMLScriptElement(){};
/**
* function addEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.addEventListener=function(arg1,arg2,arg3){};
/**
* property nodeName
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.nodeName="";
/**
* property nodeValue
* @type Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.nodeValue=new Object();
/**
* property nodeType
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.nodeType=0;
/**
* property parentNode
* @type HTMLHeadElement
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.parentNode=new HTMLHeadElement();
/**
* property childNodes
* @type NodeList
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.childNodes=new NodeList();
/**
* property firstChild
* @type Text
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.firstChild=new Text();
/**
* property lastChild
* @type Text
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.lastChild=new Text();
/**
* property previousSibling
* @type Text
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.previousSibling=new Text();
/**
* property nextSibling
* @type Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.nextSibling=new Object();
/**
* property attributes
* @type NamedNodeMap
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.attributes=new NamedNodeMap();
/**
* property ownerDocument
* @type HTMLDocument
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.ownerDocument=new HTMLDocument();
/**
* function insertBefore(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.insertBefore=function(arg1,arg2){};
/**
* function replaceChild(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.replaceChild=function(arg1,arg2){};
/**
* function removeChild(arg1)
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.removeChild=function(arg1){};
/**
* function appendChild(arg1)
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.appendChild=function(arg1){};
/**
* function hasChildNodes()
* @type Boolean
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.hasChildNodes=function(){};
/**
* function cloneNode(arg1)
* @type Element
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.cloneNode=function(arg1){};
/**
* function normalize()
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.normalize=function(){};
/**
* function isSupported(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.isSupported=function(arg1,arg2){};
/**
* property namespaceURI
* @type Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.namespaceURI=new Object();
/**
* property prefix
* @type Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.prefix=new Object();
/**
* property localName
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.localName="";
/**
* function hasAttributes()
* @type Boolean
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.hasAttributes=function(){};
/**
* property ELEMENT_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.ELEMENT_NODE=0;
/**
* property ATTRIBUTE_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.ATTRIBUTE_NODE=0;
/**
* property TEXT_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.TEXT_NODE=0;
/**
* property CDATA_SECTION_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.CDATA_SECTION_NODE=0;
/**
* property ENTITY_REFERENCE_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.ENTITY_REFERENCE_NODE=0;
/**
* property ENTITY_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.ENTITY_NODE=0;
/**
* property PROCESSING_INSTRUCTION_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.PROCESSING_INSTRUCTION_NODE=0;
/**
* property COMMENT_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.COMMENT_NODE=0;
/**
* property DOCUMENT_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.DOCUMENT_NODE=0;
/**
* property DOCUMENT_TYPE_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.DOCUMENT_TYPE_NODE=0;
/**
* property DOCUMENT_FRAGMENT_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.DOCUMENT_FRAGMENT_NODE=0;
/**
* property NOTATION_NODE
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.NOTATION_NODE=0;
/**
* property tagName
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.tagName="";
/**
* function getAttribute(arg1)
* @type Attr
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.getAttribute=function(arg1){};
/**
* function setAttribute(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.setAttribute=function(arg1,arg2){};
/**
* function removeAttribute(arg1)
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.removeAttribute=function(arg1){};
/**
* function getAttributeNode(arg1)
* @type Element
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.getAttributeNode=function(arg1){};
/**
* function setAttributeNode(arg1)
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.setAttributeNode=function(arg1){};
/**
* function removeAttributeNode(arg1)
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.removeAttributeNode=function(arg1){};
/**
* function getElementsByTagName(arg1)
* @type NodeList
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.getElementsByTagName=function(arg1){};
/**
* function getAttributeNS(arg1,arg2)
* @type Object
* @param arg1 Object
* @param arg2 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.getAttributeNS=function(arg1,arg2){};
/**
* function setAttributeNS(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.setAttributeNS=function(arg1,arg2,arg3){};
/**
* function removeAttributeNS(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.removeAttributeNS=function(arg1,arg2){};
/**
* function getAttributeNodeNS(arg1,arg2)
* @type Object
* @param arg1 Object
* @param arg2 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.getAttributeNodeNS=function(arg1,arg2){};
/**
* function setAttributeNodeNS(arg1)
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.setAttributeNodeNS=function(arg1){};
/**
* function getElementsByTagNameNS(arg1,arg2)
* @type NodeList
* @param arg1 Object
* @param arg2 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.getElementsByTagNameNS=function(arg1,arg2){};
/**
* function hasAttribute(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.hasAttribute=function(arg1){};
/**
* function hasAttributeNS(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.hasAttributeNS=function(arg1,arg2){};
/**
* property id
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.id="";
/**
* property title
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.title="";
/**
* property lang
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.lang="";
/**
* property dir
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.dir="";
/**
* property className
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.className="";
/**
* property text
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.text="";
/**
* property htmlFor
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.htmlFor="";
/**
* property event
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.event="";
/**
* property charset
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.charset="";
/**
* property defer
* @type Boolean
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.defer=false;
/**
* property src
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.src="";
/**
* property type
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.type="";
/**
* property offsetTop
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.offsetTop=0;
/**
* property offsetLeft
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.offsetLeft=0;
/**
* property offsetWidth
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.offsetWidth=0;
/**
* property offsetHeight
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.offsetHeight=0;
/**
* property offsetParent
* @type Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.offsetParent=new Object();
/**
* property innerHTML
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.innerHTML="";
/**
* property scrollTop
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.scrollTop=0;
/**
* property scrollLeft
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.scrollLeft=0;
/**
* property scrollHeight
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.scrollHeight=0;
/**
* property scrollWidth
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.scrollWidth=0;
/**
* property clientHeight
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.clientHeight=0;
/**
* property clientWidth
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.clientWidth=0;
/**
* property tabIndex
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.tabIndex=0;
/**
* function blur()
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.blur=function(){};
/**
* function focus()
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.focus=function(){};
/**
* property spellcheck
* @type Boolean
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.spellcheck=false;
/**
* property style
* @type CSSStyleDeclaration
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.style=new CSSStyleDeclaration();
/**
* function removeEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.removeEventListener=function(arg1,arg2,arg3){};
/**
* function dispatchEvent(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.dispatchEvent=function(arg1){};
/**
* property baseURI
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.baseURI="";
/**
* function compareDocumentPosition(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.compareDocumentPosition=function(arg1){};
/**
* property textContent
* @type String
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.textContent="";
/**
* function isSameNode(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.isSameNode=function(arg1){};
/**
* function lookupPrefix(arg1)
* @type String
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.lookupPrefix=function(arg1){};
/**
* function isDefaultNamespace(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.isDefaultNamespace=function(arg1){};
/**
* function lookupNamespaceURI(arg1)
* @type String
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.lookupNamespaceURI=function(arg1){};
/**
* function isEqualNode(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.isEqualNode=function(arg1){};
/**
* function getFeature(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.getFeature=function(arg1,arg2){};
/**
* function setUserData(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.setUserData=function(arg1,arg2,arg3){};
/**
* function getUserData(arg1)
* @type Object
* @param arg1 Object
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.getUserData=function(arg1){};
/**
* property DOCUMENT_POSITION_DISCONNECTED
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.DOCUMENT_POSITION_DISCONNECTED=0;
/**
* property DOCUMENT_POSITION_PRECEDING
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.DOCUMENT_POSITION_PRECEDING=0;
/**
* property DOCUMENT_POSITION_FOLLOWING
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.DOCUMENT_POSITION_FOLLOWING=0;
/**
* property DOCUMENT_POSITION_CONTAINS
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.DOCUMENT_POSITION_CONTAINS=0;
/**
* property DOCUMENT_POSITION_CONTAINED_BY
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.DOCUMENT_POSITION_CONTAINED_BY=0;
/**
* property DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
* @type Number
* @class HTMLScriptElement
* @since FireFox 2.0.0.3
*/
HTMLScriptElement.prototype.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC=0;
/**
* Object Text
* @super Object
* @type constructor
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype=new Object();
function Text(){};
/**
* function addEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.addEventListener=function(arg1,arg2,arg3){};
/**
* property nodeName
* @type String
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.nodeName="";
/**
* property nodeValue
* @type String
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.nodeValue="";
/**
* property nodeType
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.nodeType=0;
/**
* property parentNode
* @type HTMLScriptElement
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.parentNode=new HTMLScriptElement();
/**
* property childNodes
* @type NodeList
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.childNodes=new NodeList();
/**
* property firstChild
* @type Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.firstChild=new Object();
/**
* property lastChild
* @type Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.lastChild=new Object();
/**
* property previousSibling
* @type Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.previousSibling=new Object();
/**
* property nextSibling
* @type Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.nextSibling=new Object();
/**
* property attributes
* @type Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.attributes=new Object();
/**
* property ownerDocument
* @type HTMLDocument
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.ownerDocument=new HTMLDocument();
/**
* function insertBefore(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.insertBefore=function(arg1,arg2){};
/**
* function replaceChild(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.replaceChild=function(arg1,arg2){};
/**
* function removeChild(arg1)
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.removeChild=function(arg1){};
/**
* function appendChild(arg1)
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.appendChild=function(arg1){};
/**
* function hasChildNodes()
* @type Boolean
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.hasChildNodes=function(){};
/**
* function cloneNode(arg1)
* @type Element
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.cloneNode=function(arg1){};
/**
* function normalize()
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.normalize=function(){};
/**
* function isSupported(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.isSupported=function(arg1,arg2){};
/**
* property namespaceURI
* @type Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.namespaceURI=new Object();
/**
* property prefix
* @type Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.prefix=new Object();
/**
* property localName
* @type Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.localName=new Object();
/**
* function hasAttributes()
* @type Boolean
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.hasAttributes=function(){};
/**
* property ELEMENT_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.ELEMENT_NODE=0;
/**
* property ATTRIBUTE_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.ATTRIBUTE_NODE=0;
/**
* property TEXT_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.TEXT_NODE=0;
/**
* property CDATA_SECTION_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.CDATA_SECTION_NODE=0;
/**
* property ENTITY_REFERENCE_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.ENTITY_REFERENCE_NODE=0;
/**
* property ENTITY_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.ENTITY_NODE=0;
/**
* property PROCESSING_INSTRUCTION_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.PROCESSING_INSTRUCTION_NODE=0;
/**
* property COMMENT_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.COMMENT_NODE=0;
/**
* property DOCUMENT_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.DOCUMENT_NODE=0;
/**
* property DOCUMENT_TYPE_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.DOCUMENT_TYPE_NODE=0;
/**
* property DOCUMENT_FRAGMENT_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.DOCUMENT_FRAGMENT_NODE=0;
/**
* property NOTATION_NODE
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.NOTATION_NODE=0;
/**
* property data
* @type String
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.data="";
/**
* property length
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.length=0;
/**
* function substringData(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.substringData=function(arg1,arg2){};
/**
* function appendData(arg1)
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.appendData=function(arg1){};
/**
* function insertData(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.insertData=function(arg1,arg2){};
/**
* function deleteData(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.deleteData=function(arg1,arg2){};
/**
* function replaceData(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.replaceData=function(arg1,arg2,arg3){};
/**
* function splitText(arg1)
* @type String
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.splitText=function(arg1){};
/**
* function removeEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.removeEventListener=function(arg1,arg2,arg3){};
/**
* function dispatchEvent(arg1)
* @type Boolean
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.dispatchEvent=function(arg1){};
/**
* property baseURI
* @type String
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.baseURI="";
/**
* function compareDocumentPosition(arg1)
* @type Boolean
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.compareDocumentPosition=function(arg1){};
/**
* property textContent
* @type String
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.textContent="";
/**
* function isSameNode(arg1)
* @type Boolean
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.isSameNode=function(arg1){};
/**
* function lookupPrefix(arg1)
* @type String
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.lookupPrefix=function(arg1){};
/**
* function isDefaultNamespace(arg1)
* @type Boolean
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.isDefaultNamespace=function(arg1){};
/**
* function lookupNamespaceURI(arg1)
* @type String
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.lookupNamespaceURI=function(arg1){};
/**
* function isEqualNode(arg1)
* @type Boolean
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.isEqualNode=function(arg1){};
/**
* function getFeature(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.getFeature=function(arg1,arg2){};
/**
* function setUserData(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.setUserData=function(arg1,arg2,arg3){};
/**
* function getUserData(arg1)
* @type Object
* @param arg1 Object
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.getUserData=function(arg1){};
/**
* property DOCUMENT_POSITION_DISCONNECTED
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.DOCUMENT_POSITION_DISCONNECTED=0;
/**
* property DOCUMENT_POSITION_PRECEDING
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.DOCUMENT_POSITION_PRECEDING=0;
/**
* property DOCUMENT_POSITION_FOLLOWING
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.DOCUMENT_POSITION_FOLLOWING=0;
/**
* property DOCUMENT_POSITION_CONTAINS
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.DOCUMENT_POSITION_CONTAINS=0;
/**
* property DOCUMENT_POSITION_CONTAINED_BY
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.DOCUMENT_POSITION_CONTAINED_BY=0;
/**
* property DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
* @type Number
* @class Text
* @since FireFox 2.0.0.3
*/
Text.prototype.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC=0;
/**
* Object NamedNodeMap
* @super Object
* @type constructor
* @class NamedNodeMap
* @since FireFox 2.0.0.3
*/
NamedNodeMap.prototype=new Object();
function NamedNodeMap(){};
/**
* property length
* @type Number
* @class NamedNodeMap
* @since FireFox 2.0.0.3
*/
NamedNodeMap.prototype.length=0;
/**
* function getNamedItem(arg1)
* @type Element
* @param arg1 Object
* @class NamedNodeMap
* @since FireFox 2.0.0.3
*/
NamedNodeMap.prototype.getNamedItem=function(arg1){};
/**
* function setNamedItem(arg1)
* @param arg1 Object
* @class NamedNodeMap
* @since FireFox 2.0.0.3
*/
NamedNodeMap.prototype.setNamedItem=function(arg1){};
/**
* function removeNamedItem(arg1)
* @param arg1 Object
* @class NamedNodeMap
* @since FireFox 2.0.0.3
*/
NamedNodeMap.prototype.removeNamedItem=function(arg1){};
/**
* function item(arg1)
* @type Object
* @param arg1 Object
* @class NamedNodeMap
* @since FireFox 2.0.0.3
*/
NamedNodeMap.prototype.item=function(arg1){};
/**
* function getNamedItemNS(arg1,arg2)
* @type Element
* @param arg1 Object
* @param arg2 Object
* @class NamedNodeMap
* @since FireFox 2.0.0.3
*/
NamedNodeMap.prototype.getNamedItemNS=function(arg1,arg2){};
/**
* function setNamedItemNS(arg1)
* @param arg1 Object
* @class NamedNodeMap
* @since FireFox 2.0.0.3
*/
NamedNodeMap.prototype.setNamedItemNS=function(arg1){};
/**
* function removeNamedItemNS(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class NamedNodeMap
* @since FireFox 2.0.0.3
*/
NamedNodeMap.prototype.removeNamedItemNS=function(arg1,arg2){};
/**
* Object Attr
* @super Object
* @type constructor
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype=new Object();
function Attr(){};
/**
* property nodeName
* @type String
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.nodeName="";
/**
* property nodeValue
* @type String
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.nodeValue="";
/**
* property nodeType
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.nodeType=0;
/**
* property parentNode
* @type Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.parentNode=new Object();
/**
* property childNodes
* @type NodeList
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.childNodes=new NodeList();
/**
* property firstChild
* @type Text
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.firstChild=new Text();
/**
* property lastChild
* @type Text
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.lastChild=new Text();
/**
* property previousSibling
* @type Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.previousSibling=new Object();
/**
* property nextSibling
* @type Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.nextSibling=new Object();
/**
* property attributes
* @type Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.attributes=new Object();
/**
* property ownerDocument
* @type HTMLDocument
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.ownerDocument=new HTMLDocument();
/**
* function insertBefore(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.insertBefore=function(arg1,arg2){};
/**
* function replaceChild(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.replaceChild=function(arg1,arg2){};
/**
* function removeChild(arg1)
* @param arg1 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.removeChild=function(arg1){};
/**
* function appendChild(arg1)
* @param arg1 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.appendChild=function(arg1){};
/**
* function hasChildNodes()
* @type Boolean
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.hasChildNodes=function(){};
/**
* function cloneNode(arg1)
* @type Element
* @param arg1 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.cloneNode=function(arg1){};
/**
* function normalize()
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.normalize=function(){};
/**
* function isSupported(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.isSupported=function(arg1,arg2){};
/**
* property namespaceURI
* @type Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.namespaceURI=new Object();
/**
* property prefix
* @type Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.prefix=new Object();
/**
* property localName
* @type String
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.localName="";
/**
* function hasAttributes()
* @type Boolean
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.hasAttributes=function(){};
/**
* property name
* @type String
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.name="";
/**
* property specified
* @type Boolean
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.specified=false;
/**
* property value
* @type String
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.value="";
/**
* property ownerElement
* @type HTMLScriptElement
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.ownerElement=new HTMLScriptElement();
/**
* property ELEMENT_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.ELEMENT_NODE=0;
/**
* property ATTRIBUTE_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.ATTRIBUTE_NODE=0;
/**
* property TEXT_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.TEXT_NODE=0;
/**
* property CDATA_SECTION_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.CDATA_SECTION_NODE=0;
/**
* property ENTITY_REFERENCE_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.ENTITY_REFERENCE_NODE=0;
/**
* property ENTITY_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.ENTITY_NODE=0;
/**
* property PROCESSING_INSTRUCTION_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.PROCESSING_INSTRUCTION_NODE=0;
/**
* property COMMENT_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.COMMENT_NODE=0;
/**
* property DOCUMENT_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.DOCUMENT_NODE=0;
/**
* property DOCUMENT_TYPE_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.DOCUMENT_TYPE_NODE=0;
/**
* property DOCUMENT_FRAGMENT_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.DOCUMENT_FRAGMENT_NODE=0;
/**
* property NOTATION_NODE
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.NOTATION_NODE=0;
/**
* property baseURI
* @type String
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.baseURI="";
/**
* function compareDocumentPosition(arg1)
* @type Boolean
* @param arg1 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.compareDocumentPosition=function(arg1){};
/**
* property textContent
* @type String
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.textContent="";
/**
* function isSameNode(arg1)
* @type Boolean
* @param arg1 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.isSameNode=function(arg1){};
/**
* function lookupPrefix(arg1)
* @type String
* @param arg1 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.lookupPrefix=function(arg1){};
/**
* function isDefaultNamespace(arg1)
* @type Boolean
* @param arg1 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.isDefaultNamespace=function(arg1){};
/**
* function lookupNamespaceURI(arg1)
* @type String
* @param arg1 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.lookupNamespaceURI=function(arg1){};
/**
* function isEqualNode(arg1)
* @type Boolean
* @param arg1 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.isEqualNode=function(arg1){};
/**
* function getFeature(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.getFeature=function(arg1,arg2){};
/**
* function setUserData(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.setUserData=function(arg1,arg2,arg3){};
/**
* function getUserData(arg1)
* @type Object
* @param arg1 Object
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.getUserData=function(arg1){};
/**
* property DOCUMENT_POSITION_DISCONNECTED
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.DOCUMENT_POSITION_DISCONNECTED=0;
/**
* property DOCUMENT_POSITION_PRECEDING
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.DOCUMENT_POSITION_PRECEDING=0;
/**
* property DOCUMENT_POSITION_FOLLOWING
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.DOCUMENT_POSITION_FOLLOWING=0;
/**
* property DOCUMENT_POSITION_CONTAINS
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.DOCUMENT_POSITION_CONTAINS=0;
/**
* property DOCUMENT_POSITION_CONTAINED_BY
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.DOCUMENT_POSITION_CONTAINED_BY=0;
/**
* property DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
* @type Number
* @class Attr
* @since FireFox 2.0.0.3
*/
Attr.prototype.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC=0;
/**
* Object CSSStyleDeclaration
* @super Object
* @type constructor
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype=new Object();
function CSSStyleDeclaration(){};
/**
* property length
* @type Number
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.length=0;
/**
* property cssText
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.cssText="";
/**
* function getPropertyValue(arg1)
* @type String
* @param arg1 Object
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.getPropertyValue=function(arg1){};
/**
* function getPropertyCSSValue(arg1)
* @type String
* @param arg1 Object
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.getPropertyCSSValue=function(arg1){};
/**
* function removeProperty(arg1)
* @param arg1 Object
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.removeProperty=function(arg1){};
/**
* function getPropertyPriority(arg1)
* @type Number
* @param arg1 Object
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.getPropertyPriority=function(arg1){};
/**
* function setProperty(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.setProperty=function(arg1,arg2,arg3){};
/**
* function item(arg1)
* @type Object
* @param arg1 Object
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.item=function(arg1){};
/**
* property parentRule
* @type Object
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.parentRule=new Object();
/**
* property azimuth
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.azimuth="";
/**
* property background
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.background="";
/**
* property backgroundAttachment
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.backgroundAttachment="";
/**
* property backgroundColor
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.backgroundColor="";
/**
* property backgroundImage
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.backgroundImage="";
/**
* property backgroundPosition
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.backgroundPosition="";
/**
* property backgroundRepeat
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.backgroundRepeat="";
/**
* property border
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.border="";
/**
* property borderCollapse
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderCollapse="";
/**
* property borderColor
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderColor="";
/**
* property borderSpacing
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderSpacing="";
/**
* property borderStyle
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderStyle="";
/**
* property borderTop
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderTop="";
/**
* property borderRight
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderRight="";
/**
* property borderBottom
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderBottom="";
/**
* property borderLeft
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderLeft="";
/**
* property borderTopColor
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderTopColor="";
/**
* property borderRightColor
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderRightColor="";
/**
* property borderBottomColor
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderBottomColor="";
/**
* property borderLeftColor
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderLeftColor="";
/**
* property borderTopStyle
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderTopStyle="";
/**
* property borderRightStyle
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderRightStyle="";
/**
* property borderBottomStyle
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderBottomStyle="";
/**
* property borderLeftStyle
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderLeftStyle="";
/**
* property borderTopWidth
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderTopWidth="";
/**
* property borderRightWidth
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderRightWidth="";
/**
* property borderBottomWidth
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderBottomWidth="";
/**
* property borderLeftWidth
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderLeftWidth="";
/**
* property borderWidth
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.borderWidth="";
/**
* property bottom
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.bottom="";
/**
* property captionSide
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.captionSide="";
/**
* property clear
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.clear="";
/**
* property clip
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.clip="";
/**
* property color
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.color="";
/**
* property content
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.content="";
/**
* property counterIncrement
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.counterIncrement="";
/**
* property counterReset
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.counterReset="";
/**
* property cue
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.cue="";
/**
* property cueAfter
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.cueAfter="";
/**
* property cueBefore
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.cueBefore="";
/**
* property cursor
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.cursor="";
/**
* property direction
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.direction="";
/**
* property display
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.display="";
/**
* property elevation
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.elevation="";
/**
* property emptyCells
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.emptyCells="";
/**
* property cssFloat
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.cssFloat="";
/**
* property font
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.font="";
/**
* property fontFamily
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.fontFamily="";
/**
* property fontSize
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.fontSize="";
/**
* property fontSizeAdjust
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.fontSizeAdjust="";
/**
* property fontStretch
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.fontStretch="";
/**
* property fontStyle
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.fontStyle="";
/**
* property fontVariant
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.fontVariant="";
/**
* property fontWeight
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.fontWeight="";
/**
* property height
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.height="";
/**
* property left
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.left="";
/**
* property letterSpacing
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.letterSpacing="";
/**
* property lineHeight
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.lineHeight="";
/**
* property listStyle
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.listStyle="";
/**
* property listStyleImage
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.listStyleImage="";
/**
* property listStylePosition
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.listStylePosition="";
/**
* property listStyleType
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.listStyleType="";
/**
* property margin
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.margin="";
/**
* property marginTop
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.marginTop="";
/**
* property marginRight
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.marginRight="";
/**
* property marginBottom
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.marginBottom="";
/**
* property marginLeft
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.marginLeft="";
/**
* property markerOffset
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.markerOffset="";
/**
* property marks
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.marks="";
/**
* property maxHeight
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.maxHeight="";
/**
* property maxWidth
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.maxWidth="";
/**
* property minHeight
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.minHeight="";
/**
* property minWidth
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.minWidth="";
/**
* property orphans
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.orphans="";
/**
* property outline
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.outline="";
/**
* property outlineColor
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.outlineColor="";
/**
* property outlineStyle
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.outlineStyle="";
/**
* property outlineWidth
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.outlineWidth="";
/**
* property overflow
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.overflow="";
/**
* property padding
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.padding="";
/**
* property paddingTop
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.paddingTop="";
/**
* property paddingRight
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.paddingRight="";
/**
* property paddingBottom
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.paddingBottom="";
/**
* property paddingLeft
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.paddingLeft="";
/**
* property page
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.page="";
/**
* property pageBreakAfter
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.pageBreakAfter="";
/**
* property pageBreakBefore
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.pageBreakBefore="";
/**
* property pageBreakInside
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.pageBreakInside="";
/**
* property pause
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.pause="";
/**
* property pauseAfter
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.pauseAfter="";
/**
* property pauseBefore
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.pauseBefore="";
/**
* property pitch
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.pitch="";
/**
* property pitchRange
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.pitchRange="";
/**
* property position
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.position="";
/**
* property quotes
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.quotes="";
/**
* property richness
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.richness="";
/**
* property right
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.right="";
/**
* property size
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.size="";
/**
* property speak
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.speak="";
/**
* property speakHeader
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.speakHeader="";
/**
* property speakNumeral
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.speakNumeral="";
/**
* property speakPunctuation
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.speakPunctuation="";
/**
* property speechRate
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.speechRate="";
/**
* property stress
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.stress="";
/**
* property tableLayout
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.tableLayout="";
/**
* property textAlign
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.textAlign="";
/**
* property textDecoration
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.textDecoration="";
/**
* property textIndent
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.textIndent="";
/**
* property textShadow
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.textShadow="";
/**
* property textTransform
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.textTransform="";
/**
* property top
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.top="";
/**
* property unicodeBidi
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.unicodeBidi="";
/**
* property verticalAlign
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.verticalAlign="";
/**
* property visibility
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.visibility="";
/**
* property voiceFamily
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.voiceFamily="";
/**
* property volume
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.volume="";
/**
* property whiteSpace
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.whiteSpace="";
/**
* property widows
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.widows="";
/**
* property width
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.width="";
/**
* property wordSpacing
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.wordSpacing="";
/**
* property zIndex
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.zIndex="";
/**
* property MozAppearance
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozAppearance="";
/**
* property MozBackgroundClip
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBackgroundClip="";
/**
* property MozBackgroundInlinePolicy
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBackgroundInlinePolicy="";
/**
* property MozBackgroundOrigin
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBackgroundOrigin="";
/**
* property MozBinding
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBinding="";
/**
* property MozBorderBottomColors
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBorderBottomColors="";
/**
* property MozBorderLeftColors
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBorderLeftColors="";
/**
* property MozBorderRightColors
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBorderRightColors="";
/**
* property MozBorderTopColors
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBorderTopColors="";
/**
* property MozBorderRadius
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBorderRadius="";
/**
* property MozBorderRadiusTopleft
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBorderRadiusTopleft="";
/**
* property MozBorderRadiusTopright
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBorderRadiusTopright="";
/**
* property MozBorderRadiusBottomleft
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBorderRadiusBottomleft="";
/**
* property MozBorderRadiusBottomright
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBorderRadiusBottomright="";
/**
* property MozBoxAlign
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBoxAlign="";
/**
* property MozBoxDirection
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBoxDirection="";
/**
* property MozBoxFlex
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBoxFlex="";
/**
* property MozBoxOrient
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBoxOrient="";
/**
* property MozBoxOrdinalGroup
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBoxOrdinalGroup="";
/**
* property MozBoxPack
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBoxPack="";
/**
* property MozBoxSizing
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozBoxSizing="";
/**
* property MozColumnCount
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozColumnCount="";
/**
* property MozColumnWidth
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozColumnWidth="";
/**
* property MozColumnGap
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozColumnGap="";
/**
* property MozFloatEdge
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozFloatEdge="";
/**
* property MozForceBrokenImageIcon
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozForceBrokenImageIcon="";
/**
* property MozImageRegion
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozImageRegion="";
/**
* property MozMarginEnd
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozMarginEnd="";
/**
* property MozMarginStart
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozMarginStart="";
/**
* property MozOpacity
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozOpacity="";
/**
* property MozOutline
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozOutline="";
/**
* property MozOutlineColor
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozOutlineColor="";
/**
* property MozOutlineRadius
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozOutlineRadius="";
/**
* property MozOutlineRadiusTopleft
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozOutlineRadiusTopleft="";
/**
* property MozOutlineRadiusTopright
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozOutlineRadiusTopright="";
/**
* property MozOutlineRadiusBottomleft
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozOutlineRadiusBottomleft="";
/**
* property MozOutlineRadiusBottomright
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozOutlineRadiusBottomright="";
/**
* property MozOutlineStyle
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozOutlineStyle="";
/**
* property MozOutlineWidth
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozOutlineWidth="";
/**
* property MozOutlineOffset
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozOutlineOffset="";
/**
* property MozPaddingEnd
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozPaddingEnd="";
/**
* property MozPaddingStart
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozPaddingStart="";
/**
* property MozUserFocus
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozUserFocus="";
/**
* property MozUserInput
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozUserInput="";
/**
* property MozUserModify
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozUserModify="";
/**
* property MozUserSelect
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.MozUserSelect="";
/**
* property opacity
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.opacity="";
/**
* property outlineOffset
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.outlineOffset="";
/**
* property overflowX
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.overflowX="";
/**
* property overflowY
* @type String
* @class CSSStyleDeclaration
* @since FireFox 2.0.0.3
*/
CSSStyleDeclaration.prototype.overflowY="";
/**
* Object HTMLBodyElement
* @super Object
* @type constructor
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype=new Object();
function HTMLBodyElement(){};
/**
* function addEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.addEventListener=function(arg1,arg2,arg3){};
/**
* property nodeName
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.nodeName="";
/**
* property nodeValue
* @type Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.nodeValue=new Object();
/**
* property nodeType
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.nodeType=0;
/**
* property parentNode
* @type HTMLHtmlElement
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.parentNode=new HTMLHtmlElement();
/**
* property childNodes
* @type NodeList
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.childNodes=new NodeList();
/**
* property firstChild
* @type Text
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.firstChild=new Text();
/**
* property lastChild
* @type HTMLBRElement
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.lastChild=new HTMLBRElement();
/**
* property previousSibling
* @type HTMLHeadElement
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.previousSibling=new HTMLHeadElement();
/**
* property nextSibling
* @type Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.nextSibling=new Object();
/**
* property attributes
* @type NamedNodeMap
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.attributes=new NamedNodeMap();
/**
* property ownerDocument
* @type HTMLDocument
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.ownerDocument=new HTMLDocument();
/**
* function insertBefore(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.insertBefore=function(arg1,arg2){};
/**
* function replaceChild(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.replaceChild=function(arg1,arg2){};
/**
* function removeChild(arg1)
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.removeChild=function(arg1){};
/**
* function appendChild(arg1)
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.appendChild=function(arg1){};
/**
* function hasChildNodes()
* @type Boolean
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.hasChildNodes=function(){};
/**
* function cloneNode(arg1)
* @type Element
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.cloneNode=function(arg1){};
/**
* function normalize()
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.normalize=function(){};
/**
* function isSupported(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.isSupported=function(arg1,arg2){};
/**
* property namespaceURI
* @type Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.namespaceURI=new Object();
/**
* property prefix
* @type Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.prefix=new Object();
/**
* property localName
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.localName="";
/**
* function hasAttributes()
* @type Boolean
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.hasAttributes=function(){};
/**
* property ELEMENT_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.ELEMENT_NODE=0;
/**
* property ATTRIBUTE_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.ATTRIBUTE_NODE=0;
/**
* property TEXT_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.TEXT_NODE=0;
/**
* property CDATA_SECTION_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.CDATA_SECTION_NODE=0;
/**
* property ENTITY_REFERENCE_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.ENTITY_REFERENCE_NODE=0;
/**
* property ENTITY_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.ENTITY_NODE=0;
/**
* property PROCESSING_INSTRUCTION_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.PROCESSING_INSTRUCTION_NODE=0;
/**
* property COMMENT_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.COMMENT_NODE=0;
/**
* property DOCUMENT_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.DOCUMENT_NODE=0;
/**
* property DOCUMENT_TYPE_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.DOCUMENT_TYPE_NODE=0;
/**
* property DOCUMENT_FRAGMENT_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.DOCUMENT_FRAGMENT_NODE=0;
/**
* property NOTATION_NODE
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.NOTATION_NODE=0;
/**
* property tagName
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.tagName="";
/**
* function getAttribute(arg1)
* @type Attr
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.getAttribute=function(arg1){};
/**
* function setAttribute(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.setAttribute=function(arg1,arg2){};
/**
* function removeAttribute(arg1)
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.removeAttribute=function(arg1){};
/**
* function getAttributeNode(arg1)
* @type Element
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.getAttributeNode=function(arg1){};
/**
* function setAttributeNode(arg1)
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.setAttributeNode=function(arg1){};
/**
* function removeAttributeNode(arg1)
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.removeAttributeNode=function(arg1){};
/**
* function getElementsByTagName(arg1)
* @type NodeList
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.getElementsByTagName=function(arg1){};
/**
* function getAttributeNS(arg1,arg2)
* @type Object
* @param arg1 Object
* @param arg2 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.getAttributeNS=function(arg1,arg2){};
/**
* function setAttributeNS(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.setAttributeNS=function(arg1,arg2,arg3){};
/**
* function removeAttributeNS(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.removeAttributeNS=function(arg1,arg2){};
/**
* function getAttributeNodeNS(arg1,arg2)
* @type Object
* @param arg1 Object
* @param arg2 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.getAttributeNodeNS=function(arg1,arg2){};
/**
* function setAttributeNodeNS(arg1)
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.setAttributeNodeNS=function(arg1){};
/**
* function getElementsByTagNameNS(arg1,arg2)
* @type NodeList
* @param arg1 Object
* @param arg2 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.getElementsByTagNameNS=function(arg1,arg2){};
/**
* function hasAttribute(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.hasAttribute=function(arg1){};
/**
* function hasAttributeNS(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.hasAttributeNS=function(arg1,arg2){};
/**
* property id
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.id="";
/**
* property title
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.title="";
/**
* property lang
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.lang="";
/**
* property dir
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.dir="";
/**
* property className
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.className="";
/**
* property aLink
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.aLink="";
/**
* property background
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.background="";
/**
* property bgColor
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.bgColor="";
/**
* property link
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.link="";
/**
* property text
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.text="";
/**
* property vLink
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.vLink="";
/**
* property offsetTop
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.offsetTop=0;
/**
* property offsetLeft
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.offsetLeft=0;
/**
* property offsetWidth
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.offsetWidth=0;
/**
* property offsetHeight
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.offsetHeight=0;
/**
* property offsetParent
* @type Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.offsetParent=new Object();
/**
* property innerHTML
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.innerHTML="";
/**
* property scrollTop
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.scrollTop=0;
/**
* property scrollLeft
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.scrollLeft=0;
/**
* property scrollHeight
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.scrollHeight=0;
/**
* property scrollWidth
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.scrollWidth=0;
/**
* property clientHeight
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.clientHeight=0;
/**
* property clientWidth
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.clientWidth=0;
/**
* property tabIndex
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.tabIndex=0;
/**
* function blur()
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.blur=function(){};
/**
* function focus()
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.focus=function(){};
/**
* property spellcheck
* @type Boolean
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.spellcheck=false;
/**
* property style
* @type CSSStyleDeclaration
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.style=new CSSStyleDeclaration();
/**
* function removeEventListener(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.removeEventListener=function(arg1,arg2,arg3){};
/**
* function dispatchEvent(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.dispatchEvent=function(arg1){};
/**
* property baseURI
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.baseURI="";
/**
* function compareDocumentPosition(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.compareDocumentPosition=function(arg1){};
/**
* property textContent
* @type String
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.textContent="";
/**
* function isSameNode(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.isSameNode=function(arg1){};
/**
* function lookupPrefix(arg1)
* @type String
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.lookupPrefix=function(arg1){};
/**
* function isDefaultNamespace(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.isDefaultNamespace=function(arg1){};
/**
* function lookupNamespaceURI(arg1)
* @type String
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.lookupNamespaceURI=function(arg1){};
/**
* function isEqualNode(arg1)
* @type Boolean
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.isEqualNode=function(arg1){};
/**
* function getFeature(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.getFeature=function(arg1,arg2){};
/**
* function setUserData(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.setUserData=function(arg1,arg2,arg3){};
/**
* function getUserData(arg1)
* @type Object
* @param arg1 Object
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.getUserData=function(arg1){};
/**
* property DOCUMENT_POSITION_DISCONNECTED
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.DOCUMENT_POSITION_DISCONNECTED=0;
/**
* property DOCUMENT_POSITION_PRECEDING
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.DOCUMENT_POSITION_PRECEDING=0;
/**
* property DOCUMENT_POSITION_FOLLOWING
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.DOCUMENT_POSITION_FOLLOWING=0;
/**
* property DOCUMENT_POSITION_CONTAINS
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.DOCUMENT_POSITION_CONTAINS=0;
/**
* property DOCUMENT_POSITION_CONTAINED_BY
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.DOCUMENT_POSITION_CONTAINED_BY=0;
/**
* property DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC
* @type Number
* @class HTMLBodyElement
* @since FireFox 2.0.0.3
*/
HTMLBodyElement.prototype.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC=0;
/**
* Object DOMImplementation
* @super Object
* @type constructor
* @class DOMImplementation
* @since FireFox 2.0.0.3
*/
DOMImplementation.prototype=new Object();
function DOMImplementation(){};
/**
* function hasFeature(arg1,arg2)
* @type Boolean
* @param arg1 Object
* @param arg2 Object
* @class DOMImplementation
* @since FireFox 2.0.0.3
*/
DOMImplementation.prototype.hasFeature=function(arg1,arg2){};
/**
* function createDocumentType(arg1,arg2,arg3)
* @type Object
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class DOMImplementation
* @since FireFox 2.0.0.3
*/
DOMImplementation.prototype.createDocumentType=function(arg1,arg2,arg3){};
/**
* function createDocument(arg1,arg2,arg3)
* @type HTMLDocument
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class DOMImplementation
* @since FireFox 2.0.0.3
*/
DOMImplementation.prototype.createDocument=function(arg1,arg2,arg3){};
/**
* Object HTMLCollection
* @super Object
* @type constructor
* @class HTMLCollection
* @since FireFox 2.0.0.3
*/
HTMLCollection.prototype=new Object();
function HTMLCollection(){};
/**
* property length
* @type Number
* @class HTMLCollection
* @since FireFox 2.0.0.3
*/
HTMLCollection.prototype.length=0;
/**
* function item(arg1)
* @type Object
* @param arg1 Object
* @class HTMLCollection
* @since FireFox 2.0.0.3
*/
HTMLCollection.prototype.item=function(arg1){};
/**
* function namedItem(arg1)
* @type Object
* @param arg1 Object
* @class HTMLCollection
* @since FireFox 2.0.0.3
*/
HTMLCollection.prototype.namedItem=function(arg1){};
/**
* Object JavaPackage
* @super Object
* @type constructor
* @class JavaPackage
* @since FireFox 2.0.0.3
*/
JavaPackage.prototype=new Object();
function JavaPackage(){};
/**
* Object nsXPCComponents
* @super Object
* @type constructor
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype=new Object();
function nsXPCComponents(){};
/**
* function QueryInterface(arg1)
* @type Object
* @param arg1 Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.QueryInterface=function(arg1){};
/**
* property interfaces
* @type Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.interfaces=new Object();
/**
* property interfacesByID
* @type Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.interfacesByID=new Object();
/**
* property classes
* @type Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.classes=new Object();
/**
* property classesByID
* @type Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.classesByID=new Object();
/**
* property stack
* @type Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.stack=new Object();
/**
* property results
* @type Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.results=new Object();
/**
* property manager
* @type Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.manager=new Object();
/**
* property utils
* @type Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.utils=new Object();
/**
* property ID
* @type Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.ID=new Object();
/**
* property Exception
* @type Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.Exception=new Object();
/**
* property Constructor
* @type Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.Constructor=new Object();
/**
* function isSuccessCode(arg1)
* @type Boolean
* @param arg1 Object
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.isSuccessCode=function(arg1){};
/**
* function lookupMethod()
* @type String
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.lookupMethod=function(){};
/**
* function reportError()
* @class nsXPCComponents
* @since FireFox 2.0.0.3
*/
nsXPCComponents.prototype.reportError=function(){};
/**
* Object BarProp
* @super Object
* @type constructor
* @class BarProp
* @since FireFox 2.0.0.3
*/
BarProp.prototype=new Object();
function BarProp(){};
/**
* property visible
* @type Boolean
* @class BarProp
* @since FireFox 2.0.0.3
*/
BarProp.prototype.visible=false;
/**
* Object Screen
* @super Object
* @type constructor
* @class Screen
* @since FireFox 2.0.0.3
*/
Screen.prototype=new Object();
function Screen(){};
/**
* property top
* @type Number
* @class Screen
* @since FireFox 2.0.0.3
*/
Screen.prototype.top=0;
/**
* property left
* @type Number
* @class Screen
* @since FireFox 2.0.0.3
*/
Screen.prototype.left=0;
/**
* property width
* @type Number
* @class Screen
* @since FireFox 2.0.0.3
*/
Screen.prototype.width=0;
/**
* property height
* @type Number
* @class Screen
* @since FireFox 2.0.0.3
*/
Screen.prototype.height=0;
/**
* property pixelDepth
* @type Number
* @class Screen
* @since FireFox 2.0.0.3
*/
Screen.prototype.pixelDepth=0;
/**
* property colorDepth
* @type Number
* @class Screen
* @since FireFox 2.0.0.3
*/
Screen.prototype.colorDepth=0;
/**
* property availWidth
* @type Number
* @class Screen
* @since FireFox 2.0.0.3
*/
Screen.prototype.availWidth=0;
/**
* property availHeight
* @type Number
* @class Screen
* @since FireFox 2.0.0.3
*/
Screen.prototype.availHeight=0;
/**
* property availLeft
* @type Number
* @class Screen
* @since FireFox 2.0.0.3
*/
Screen.prototype.availLeft=0;
/**
* property availTop
* @type Number
* @class Screen
* @since FireFox 2.0.0.3
*/
Screen.prototype.availTop=0;
/**
* Object History
* @super Object
* @type constructor
* @class History
* @since FireFox 2.0.0.3
*/
History.prototype=new Object();
function History(){};
/**
* property length
* @type Number
* @class History
* @since FireFox 2.0.0.3
*/
History.prototype.length=0;
/**
* property current
* @type Object
* @class History
* @since FireFox 2.0.0.3
*/
History.prototype.current=new Object();
/**
* property previous
* @type Object
* @class History
* @since FireFox 2.0.0.3
*/
History.prototype.previous=new Object();
/**
* property next
* @type Object
* @class History
* @since FireFox 2.0.0.3
*/
History.prototype.next=new Object();
/**
* function back()
* @class History
* @since FireFox 2.0.0.3
*/
History.prototype.back=function(){};
/**
* function forward()
* @class History
* @since FireFox 2.0.0.3
*/
History.prototype.forward=function(){};
/**
* function item(arg1)
* @type Object
* @param arg1 Object
* @class History
* @since FireFox 2.0.0.3
*/
History.prototype.item=function(arg1){};
/**
* function go()
* @class History
* @since FireFox 2.0.0.3
*/
History.prototype.go=function(){};
/**
* Object Crypto
* @super Object
* @type constructor
* @class Crypto
* @since FireFox 2.0.0.3
*/
Crypto.prototype=new Object();
function Crypto(){};
/**
* property version
* @type String
* @class Crypto
* @since FireFox 2.0.0.3
*/
Crypto.prototype.version="";
/**
* property enableSmartCardEvents
* @type Boolean
* @class Crypto
* @since FireFox 2.0.0.3
*/
Crypto.prototype.enableSmartCardEvents=false;
/**
* function generateCRMFRequest()
* @type String
* @class Crypto
* @since FireFox 2.0.0.3
*/
Crypto.prototype.generateCRMFRequest=function(){};
/**
* function importUserCertificates(arg1,arg2,arg3)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @class Crypto
* @since FireFox 2.0.0.3
*/
Crypto.prototype.importUserCertificates=function(arg1,arg2,arg3){};
/**
* function popChallengeResponse(arg1)
* @type Object
* @param arg1 Object
* @class Crypto
* @since FireFox 2.0.0.3
*/
Crypto.prototype.popChallengeResponse=function(arg1){};
/**
* function random(arg1)
* @type Number
* @param arg1 Object
* @class Crypto
* @since FireFox 2.0.0.3
*/
Crypto.prototype.random=function(arg1){};
/**
* function signText(arg1,arg2)
* @type String
* @param arg1 Object
* @param arg2 Object
* @class Crypto
* @since FireFox 2.0.0.3
*/
Crypto.prototype.signText=function(arg1,arg2){};
/**
* function alert(arg1)
* @param arg1 Object
* @class Crypto
* @since FireFox 2.0.0.3
*/
Crypto.prototype.alert=function(arg1){};
/**
* function logout()
* @class Crypto
* @since FireFox 2.0.0.3
*/
Crypto.prototype.logout=function(){};
/**
* function disableRightClick()
* @class Crypto
* @since FireFox 2.0.0.3
*/
Crypto.prototype.disableRightClick=function(){};
/**
* Object Pkcs11
* @super Object
* @type constructor
* @class Pkcs11
* @since FireFox 2.0.0.3
*/
Pkcs11.prototype=new Object();
function Pkcs11(){};
/**
* function deletemodule(arg1)
* @param arg1 Object
* @class Pkcs11
* @since FireFox 2.0.0.3
*/
Pkcs11.prototype.deletemodule=function(arg1){};
/**
* function addmodule(arg1,arg2,arg3,arg4)
* @param arg1 Object
* @param arg2 Object
* @param arg3 Object
* @param arg4 Object
* @class Pkcs11
* @since FireFox 2.0.0.3
*/
Pkcs11.prototype.addmodule=function(arg1,arg2,arg3,arg4){};
/**
* Object XULControllers
* @super Object
* @type constructor
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype=new Object();
function XULControllers(){};
/**
* function QueryInterface(arg1)
* @type Object
* @param arg1 Object
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype.QueryInterface=function(arg1){};
/**
* property commandDispatcher
* @type Object
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype.commandDispatcher=new Object();
/**
* function getControllerForCommand(arg1)
* @type Object
* @param arg1 Object
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype.getControllerForCommand=function(arg1){};
/**
* function insertControllerAt(arg1,arg2)
* @param arg1 Object
* @param arg2 Object
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype.insertControllerAt=function(arg1,arg2){};
/**
* function removeControllerAt(arg1)
* @type String
* @param arg1 Object
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype.removeControllerAt=function(arg1){};
/**
* function getControllerAt(arg1)
* @type Object
* @param arg1 Object
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype.getControllerAt=function(arg1){};
/**
* function appendController(arg1)
* @param arg1 Object
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype.appendController=function(arg1){};
/**
* function removeController(arg1)
* @param arg1 Object
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype.removeController=function(arg1){};
/**
* function getControllerId(arg1)
* @type String
* @param arg1 Object
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype.getControllerId=function(arg1){};
/**
* function getControllerById(arg1)
* @type Object
* @param arg1 Object
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype.getControllerById=function(arg1){};
/**
* function getControllerCount()
* @type Number
* @class XULControllers
* @since FireFox 2.0.0.3
*/
XULControllers.prototype.getControllerCount=function(){};
/**
* Object StorageList
* @super Object
* @type constructor
* @class StorageList
* @since FireFox 2.0.0.3
*/
StorageList.prototype=new Object();
function StorageList(){};

/**
* Object HTMLBRElement
* @super Object
* @type constructor
* @class StorageList
* @since FireFox 2.0.0.3
*/
HTMLBRElement.prototype=new Element();
function HTMLBRElement(){};