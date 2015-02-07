package com.mygdx.game.textarea;

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
		} else {
			model.append(character);
			model.caret().moveRight();
		}
		return true;
	}
}