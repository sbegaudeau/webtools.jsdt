function foo() {
		var i= 10;
		if (/*]*/i < 10 || i < 20/*[*/)
			foo();
	}	
