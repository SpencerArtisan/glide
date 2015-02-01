package com.mygdx.game.textarea;

import javafx.scene.input.KeyCode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea.TextAreaListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TextAreaExt extends com.badlogic.gdx.scenes.scene2d.ui.TextArea {
	public TextAreaExt(String text, Skin skin, String styleName) {
		super(text, skin, styleName);
	}

	public TextAreaExt(String text, Skin skin) {
		super(text, skin);
	}

	public TextAreaExt(String text, TextFieldStyle style) {
		super(text, style);
	}

	@Override
	protected InputListener createInputListener () {
		return new TextAreaExtListener();
	}

	public class TextAreaExtListener extends TextAreaListener {

		@Override
		public boolean keyTyped(InputEvent event, char character) {
			boolean commandPressed = Gdx.input.isKeyPressed(Keys.SYM);
			if (commandPressed) {
				return true;
			}
			return super.keyTyped(event, character);

		}
		
	}

}
