function foo() {
		var i= 0;
		while (/*]*/i <= 10/*[*/)
			foo();
		foo();	
	}	
