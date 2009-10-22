//package a9;
function B(){
	this.m = function(){};
}

A.prototype = new B();
A.prototype.m = function(){};

//m = function();
C.prototype = new A();
C.prototype.m = function(){};

//function C extends A{
//	this.m = new function();
//}

