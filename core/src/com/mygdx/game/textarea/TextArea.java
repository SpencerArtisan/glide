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
		XY caretPosition = getCaretPosition();
		caretImage.draw(batch, caretPosition.x, caretPosition.y, caretImage.getMinWidth(), getRowHeight());	
	}

	private XY getCaretPosition() {
		TextAreaModel.Caret caret = model.caret();
		return new XY(8 + caret.getX() * getColumnWidth(), this.getHeight() - 30 - caret.getY() * getRowHeight());	
	}

	private float getRowHeight() {
		float lineHeight = style.font.getLineHeight();
		return lineHeight;
	}
	
	private float getColumnWidth() {
		return 14;
	}

	public InputProcessor getController() {
		return controller;
	}
}
