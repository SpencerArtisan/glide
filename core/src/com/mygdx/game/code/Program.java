package com.mygdx.game.code;

public class Program {
	private String code;
	
	private static String TEMPLATE =
		       "////////////////////////////////// \n"
			 + "// Welcome to Groovy Planet! \n"
			 + "// Start writing your game below. \n"
			 + "// Click here if you need help \n"
			 + "////////////////////////////////// \n\n";

	public Program() {
		code = TEMPLATE;
	}

	public String code() {
		return code;
	}
}
