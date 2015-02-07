package com.mygdx.game.textarea;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TextArea extends Actor {
	private TextAreaModel model;
	private BitmapFont font;
	private TextAreaController controller;

	public TextArea(TextAreaModel model, Skin skin) {
		this.model = model;
		controller = new TextAreaController(model);
		font = skin.getFont("default-font");
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.drawMultiLine(batch, model.getText(), 10, 740);
	}

	public InputProcessor getController() {
		return controller;
	}
}
