function foo() {
		var i= 0;
		foo();
		do {
			foo();
		} while (/*]*/i <= 10/*[*/);
	}
