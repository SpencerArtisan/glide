package com.mygdx.game.textarea;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TextArea extends Actor {
	private TextAreaModel model;
	private TextAreaController controller;
	private TextFieldStyle style;

	public TextArea(TextAreaModel model, Skin skin) {
		this.model = model;
		controller = new TextAreaController(model);
		style = skin.get(TextFieldStyle.class);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		style.font.drawMultiLine(batch, model.getText(), 10, 740);
		Drawable caretImage = style.cursor;
		Caret caret = model.caret();
		float lineHeight = style.font.getLineHeight();
		caretImage.draw(batch, 8 + caret.getX() * 14, this.getHeight() - 30 - caret.getY() * lineHeight, caretImage.getMinWidth(), lineHeight);	
	}

	public InputProcessor getController() {
		return controller;
	}
}
