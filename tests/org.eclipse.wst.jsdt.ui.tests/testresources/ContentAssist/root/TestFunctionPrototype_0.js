function FunctionPrototype0(param1) {
	this.port=param1;
}

function Function1() {}

// static variables

FunctionPrototype0.getServerIP = function() {
	return Server.serverIP;
};
FunctionPrototype0.serverIP = '123.23.23.23';

// instance variables

FunctionPrototype0.prototype.getClientPort = function() {
	return this.port;
};
FunctionPrototype0.prototype.clientIP = '';
FunctionPrototype0.prototype.getClientIP = function() {
	return this.clientIP;
};

// Begin tests

FunctionPrototype0.z

Function1.z