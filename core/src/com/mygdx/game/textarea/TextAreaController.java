package com.mygdx.game.textarea;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.XY;

public class TextAreaController extends InputAdapter {

	private TextAreaModel model;
	private TextArea view;

	public TextAreaController(TextAreaModel model, TextArea view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public boolean keyTyped(char character) {
		KeyStroke ks = KeyStroke.getKeyStroke(character, 0);
		
		TextAreaModel.Caret caret = model.caret();
		if (Key.Delete.is(character)) {
			model.deleteCharacter();
		} else if (Key.Up.is(character)) {
			caret.moveUp();
		} else if (Key.Down.is(character)) {
			caret.moveDown();
		} else if (Key.Right.is(character)) {
			caret.moveRight();
		} else if (Key.Left.is(character)) {
			caret.moveLeft();
		} else if (Key.Return.is(character)) {
			model.insert('\n');
			caret.moveDown();
			caret.moveToFarLeft();
		} else if (isPrintableChar(character)) {
			model.insert(character);
			caret.moveRight();
		}
		return true;
	}
	
	
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		XY<Integer> caretLocation = view.screenPositionToCaretLocation(new XY<Integer>(screenX, screenY));
		model.caret().setLocation(caretLocation);
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