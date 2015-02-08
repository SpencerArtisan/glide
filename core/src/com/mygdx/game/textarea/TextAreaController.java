package com.mygdx.game.textarea;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.badlogic.gdx.InputAdapter;

public class TextAreaController extends InputAdapter {

	private TextAreaModel model;

	public TextAreaController(TextAreaModel model) {
		this.model = model;
	}

	@Override
	public boolean keyTyped(char character) {
		KeyStroke ks = KeyStroke.getKeyStroke(character, 0);
		System.out.println(ks.getKeyCode());
		
		if (Key.Delete.is(character)) {
			model.deleteCharacter();
			model.caret().moveLeft();
		} else if (Key.Up.is(character)) {
			model.caret().moveUp();
		} else if (Key.Down.is(character)) {
			model.caret().moveDown();
		} else if (Key.Right.is(character)) {
			model.caret().moveRight();
		} else if (Key.Left.is(character)) {
			model.caret().moveLeft();
		} else if (isPrintableChar(character)) {
			model.append(character);
			model.caret().moveRight();
		}
		return true;
	}
	
	public boolean isPrintableChar(char character) {
	    Character.UnicodeBlock block = Character.UnicodeBlock.of(character);
	    return (!Character.isISOControl(character)) &&
	            character != KeyEvent.CHAR_UNDEFINED &&
	            block != null &&
	            block != Character.UnicodeBlock.SPECIALS;
	}
}