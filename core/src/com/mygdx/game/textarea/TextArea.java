package com.mygdx.game.textarea;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.game.XY;

public class TextArea extends Actor {
	private static final int TOP_MARGIN = 30;
	private static final int LEFT_MARGIN = 8;
	private TextAreaModel model;
	private TextAreaController controller;
	private TextFieldStyle style;

	public TextArea(TextAreaModel model, Skin skin) {
		this.model = model;
		controller = new TextAreaController(model, this);
		style = skin.get(TextFieldStyle.class);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
        style.background.draw(batch, 0, 0, getWidth(), getHeight());
        XY<Integer> topLeftCurrent = caretLocationToPosition(new XY<Integer>(0, model.caret().location().y));
        style.focusedBackground.draw(batch, 0, topLeftCurrent.y, getWidth(), getRowHeight());

        style.font.setMarkupEnabled(true);
		style.font.drawMultiLine(batch, model.getColoredText(), 10, 740);
		Drawable caretImage = style.cursor;
		XY<Integer> caretPosition = caretLocationToPosition(model.caret().location());
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

	public XY<Integer> caretLocationToPosition(XY<Integer> caret) {
		float x = LEFT_MARGIN + caret.x * getColumnWidth();
		float y = this.getHeight() - TOP_MARGIN - caret.y * getRowHeight();
		return new XY<Integer>((int) x, (int) y);	
	}

	public XY<Integer> screenPositionToCaretLocation(XY<Integer> screenPosition) {
		Vector3 pos = new Vector3(screenPosition.x, screenPosition.y, 0);
		Vector3 worldPosition = this.getStage().getCamera().unproject(pos);

		float caretX = (worldPosition.x - LEFT_MARGIN) / getColumnWidth();
		float caretY = (this.getHeight() - TOP_MARGIN + 22 - worldPosition.y) / getRowHeight();
		return new XY<Integer>((int) caretX, (int) caretY);	
	}
}
