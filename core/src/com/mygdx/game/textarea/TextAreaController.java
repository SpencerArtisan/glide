package com.mygdx.game.textarea;

import java.awt.event.KeyEvent;

import com.badlogic.gdx.InputAdapter;

public class TextAreaController extends InputAdapter {

	private TextAreaModel model;

	public TextAreaController(TextAreaModel model) {
		this.model = model;
	}

	@Override
	public boolean keyTyped(char character) {
		if (character == '\b') {
			model.deleteCharacter();
			model.caret().moveLeft();
		} else if (isPrintableChar(character)) {
			model.append(character);
			model.caret().moveRight();
		}
		return true;
	}
	
	public boolean isPrintableChar( char c ) {
	    Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
	    return (!Character.isISOControl(c)) &&
	            c != KeyEvent.CHAR_UNDEFINED &&
	            block != null &&
	            block != Character.UnicodeBlock.SPECIALS;
	}
}