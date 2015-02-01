package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextArea extends Actor {
	private TextAreaModel model;
	private BitmapFont font;

	public TextArea(TextAreaModel model) {
		this.model = model;
		font = new BitmapFont(Gdx.files.internal("fonts/white-rabbit.fnt"));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.drawMultiLine(batch, model.getText(), 10, 740);
	}
}
