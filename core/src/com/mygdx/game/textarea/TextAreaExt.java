package com.mygdx.game.textarea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.IntArray;

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
	protected void drawText (Batch batch, BitmapFont font, float x, float y) {
		String colouredText = "[GRAY]" + text;
		font.setMarkupEnabled(true);
		
		float offsetY = 0;
		IntArray linesBreak = new IntArray();
		int newLineIndex = 0;
		while(newLineIndex < colouredText.length()) {
			linesBreak.add(newLineIndex);
			newLineIndex = colouredText.indexOf('\n', newLineIndex);
			if (newLineIndex == -1) {
				linesBreak.add(colouredText.length());
				break;
			}
			linesBreak.add(newLineIndex);
			newLineIndex++;
		}
				
		for (int i = getFirstLineShowing() * 2; i < (getFirstLineShowing() + getLinesShowing()) * 2 && i < linesBreak.size; i += 2) {
			font.draw(batch, colouredText, x, y + offsetY, linesBreak.items[i], linesBreak.items[i + 1]);
			offsetY -= font.getLineHeight();
		}

		font.setMarkupEnabled(false);
	}


	@Override
	protected InputListener createInputListener () {
		return new TextAreaExtListener();
	}

	public class TextAreaExtListener extends TextAreaListener {
		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			// Stop scroll pane interfering with text area select
			event.stop();
			return super.touchDown(event, x, y, pointer, button);
		}

		@Override
		public boolean keyTyped(InputEvent event, char character) {
			// Prevent command c/v defect
			boolean commandPressed = Gdx.input.isKeyPressed(Keys.SYM);
			if (commandPressed) {
				return true;
			}
			return super.keyTyped(event, character);
		}
	}
}
