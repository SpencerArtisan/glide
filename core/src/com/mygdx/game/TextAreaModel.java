package com.mygdx.game;

public class TextAreaModel {
	private String text;

	public TextAreaModel() {
		text = "////////////////////////////////// \n"
			 + "// Welcome to Groovy Planet! \n"
			 + "// Start writing your game below. \n"
			 + "// Click here if you need help \n"
			 + "////////////////////////////////// \n\n";
	}

	public String getText() {
		return text;
	}
}
