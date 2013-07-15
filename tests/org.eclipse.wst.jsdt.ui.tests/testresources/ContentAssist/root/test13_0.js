function outerFunc() {
	i
	o		// innerFunc and outerFunc
	
	function innerFunc() {
		i
		o	// all but localInnerFunc	
		
		function insideInnerFunc() {
			var localInnerFunc = function(param1) {
				
			};
			//all the functions
			
		}
			// all but localInnerFunc
		
	}
	
}

	// only outerFunc