package com.mygdx.game.textarea;

public class TextAreaModel {
	private String text;
	private Caret caret;
	
	public TextAreaModel() {
		text = "";
//		"////////////////////////////////// \n"
//			 + "// Welcome to Groovy Planet! \n"
//			 + "// Start writing your game below. \n"
//			 + "// Click here if you need help \n"
//			 + "////////////////////////////////// \n\n";
		caret = new Caret();
	}

	public String getText() {
		return text;
	}

	public void clear() {
		text = "";
	}

	public void append(char character) {
		text += character;
	}

	public void deleteCharacter() {
		text = text.substring(0, text.length() - 1);
	}

	public Caret caret() {
		return caret;
	}
}