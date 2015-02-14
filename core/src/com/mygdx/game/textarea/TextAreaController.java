package com.mygdx.game.textarea;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.XY;
import com.mygdx.game.textarea.command.*;

public class TextAreaController extends InputAdapter {

	private TextAreaModel model;
	private TextArea view;
    private LinkedList<Command> executedCommands = new LinkedList<Command>();
    private int lastCommandIndex = -1;

	public TextAreaController(TextAreaModel model, TextArea view) {
		this.model = model;
		this.view = view;
	}

    @Override
    public boolean keyDown(int keycode) {
        Command command = getKeyDownCommand(keycode);
        if (command != null) {
            command.execute();
            return true;
        }
        return false;
    }

	@Override
	public boolean keyTyped(char character) {
        Command command = getKeyTypedCommand(character);
        if (command != null) {
            clearRedoChain();
            executedCommands.add(command);
            command.execute();
            lastCommandIndex = executedCommands.size() - 1;
        }
        return true;
	}

    private void clearRedoChain() {
        for (int i = lastCommandIndex + 1; i < executedCommands.size(); i++) {
            executedCommands.removeLast();
        }
    }

    private Command getKeyDownCommand(int keycode) {
        if (isRedo(keycode)) {
            lastCommandIndex++;
            return executedCommands.get(lastCommandIndex);
        }
        if (isUndo(keycode)) {
            Command lastCommand = executedCommands.get(lastCommandIndex);
            lastCommandIndex--;
            return new UndoCommand(lastCommand);
        }
        return null;
    }

    private boolean isUndo(int keycode) {
        return isControlDown() && keycode == Input.Keys.Z && !isShiftDown() && lastCommandIndex >= 0;
    }

    private boolean isRedo(int keycode) {
        return isControlDown() && isShiftDown() && keycode == Input.Keys.Z && lastCommandIndex < executedCommands.size() - 1;
    }

    private Command getKeyTypedCommand(char character) {
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
        return null;
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

    public boolean isControlDown() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) ||
               Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) ||
               Gdx.input.isKeyPressed(Input.Keys.SYM);
    }

    public boolean isShiftDown() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ||
               Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }
}