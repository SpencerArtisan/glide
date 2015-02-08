package com.mygdx.game.textarea;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.game.textarea.TextAreaModel.Caret;

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
		XY<Integer> caretPosition = caretToPosition(model.caret().location());
		caretImage.draw(batch, caretPosition.x, caretPosition.y, caretImage.getMinWidth(), getRowHeight());	
	}

	private float getRowHeight() {
		return style.font.getLineHeight();
	}
	
	private float getColumnWidth() {
		return 14;
	}

	public InputProcessor getController() {
		return controller;
	}

	public XY<Integer> caretToPosition(XY<Integer> caret) {
		float x = 8 + caret.x * getColumnWidth();
		float y = this.getHeight() - 30 - caret.y * getRowHeight();
		return new XY<Integer>((int) x, (int) y);	
	}
}
