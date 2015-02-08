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
		
		TextAreaModel.Caret caret = model.caret();
		if (Key.Delete.is(character)) {
			model.deleteCharacter();
			caret.moveLeft();
		} else if (Key.Up.is(character) && caret.getY() > 0) {
			caret.moveUp();
		} else if (Key.Down.is(character)) {
			caret.moveDown();
		} else if (Key.Right.is(character)) {
			caret.moveRight();
		} else if (Key.Left.is(character) && caret.getX() > 0) {
			caret.moveLeft();
		} else if (Key.Return.is(character)) {
			model.insert('\n');
			caret.moveDown();
			caret.setX(0);
		} else if (isPrintableChar(character)) {
			model.insert(character);
			caret.moveRight();
		}
		return true;
	}
	
	public boolean isPrintableChar(char character) {
	    Character.UnicodeBlock block = Character.UnicodeBlock.of(character);
	    return (!Character.isISOControl(character)) &&
	            character != KeyEvent.CHAR_UNDEFINED &&
	            block != null &&
	            block != Character.UnicodeBlock.SPECIALS &&
	            !Key.Down.is(character) &&
	            !Key.Up.is(character) &&
	            !Key.Left.is(character) &&
	            !Key.Right.is(character);
	}
}