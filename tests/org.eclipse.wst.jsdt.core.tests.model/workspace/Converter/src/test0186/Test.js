function foo() {
		var o= null;
		if (/*]*/o == o/*[*/)
			foo();
	}	
