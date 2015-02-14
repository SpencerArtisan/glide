package com.mygdx.game.textarea;

import java.awt.event.KeyEvent;

import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.XY;
import com.mygdx.game.textarea.command.*;

public class TextAreaController extends InputAdapter {

	private TextAreaModel model;
	private TextArea view;

	public TextAreaController(TextAreaModel model, TextArea view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public boolean keyTyped(char character) {
        getCommand(character).execute();
        return true;
	}

    private Command getCommand(char character) {
        if (Key.Delete.is(character)) {
            return new DeleteCommand(model);
        } else if (Key.Up.is(character)) {
            return new MoveUpCommand(model);
        } else if (Key.Down.is(character)) {
            return new MoveDownCommand(model);
        } else if (Key.Right.is(character)) {
            return new MoveRightCommand(model);
        } else if (Key.Left.is(character)) {
            return new MoveLeftCommand(model);
        } else if (Key.Return.is(character)) {
            return new ReturnCommand(model);
        } else if (isPrintableChar(character)) {
            return new TypeCommand(model, character);
        }
        return new NullCommand();
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