var closureReturnVar1 = (function() {
	return 1;
})();

closureReturnVar2 = (function() {
	return "hi";
})();

var closureReturnVar3 = (function() {
	return function() {};
})();

(function() {
	var localClosureVar = (function() {
		var localClosureVar = function() {};
		return localClosureVar;
	})();
	window.globalClosureAssign = localClosureVar;
})();

closureReturnVar1.z

closureReturnVar2.z

closureReturnVar3.z

globalClosureAssign.z

