package com.mygdx.game.textarea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.XY;
import com.mygdx.game.textarea.command.*;

import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class TextAreaController extends InputAdapter {

	private TextAreaModel model;
	private TextArea view;
    private LinkedList<Command> executedCommands = new LinkedList<Command>();
    private int lastCommandIndex = -1;
    private XY<Integer> touchDownLocation;
    private boolean dragging;

    public TextAreaController(TextAreaModel model, TextArea view) {
		this.model = model;
		this.view = view;
	}

    @Override
    public boolean keyDown(int keycode) {
        if (isRedo(keycode)) {
            Command nextCommand = executedCommands.get(lastCommandIndex + 1);
            lastCommandIndex++;
            nextCommand.execute();
        }
        if (isUndo(keycode)) {
            Command lastCommand = executedCommands.get(lastCommandIndex);
            lastCommandIndex--;
            lastCommand.undo();
        }
        return true;
    }

	@Override
	public boolean keyTyped(char character) {
        Command command = getKeyTypedCommand(character);
        executeAndRemember(command);
        return true;
	}

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        XY<Integer> caretLocation = view.screenPositionToCaretLocation(new XY<Integer>(screenX, screenY));
        if (dragging) {
            dragging = false;
            executeAndRemember(new SelectCommand(model, touchDownLocation, caretLocation));
        } else {
            executeAndRemember(new MoveToCommand(model, caretLocation));
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        model.caret().clearSelection();
        this.touchDownLocation = view.screenPositionToCaretLocation(new XY<Integer>(screenX, screenY));
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        dragging = true;
        XY<Integer> dragLocation = view.screenPositionToCaretLocation(new XY<Integer>(screenX, screenY));
        new SelectCommand(model, touchDownLocation, dragLocation).execute();
        return true;
    }

    private void executeAndRemember(Command command) {
        if (command != null) {
            clearRedoChain();
            executedCommands.add(command);
            lastCommandIndex++;
            command.execute();
        }
    }

    private void clearRedoChain() {
        for (int i = lastCommandIndex + 1; i < executedCommands.size(); i++) {
            executedCommands.removeLast();
        }
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
        } else if (Key.Tab.is(character)) {
            return new TabCommand(model);
        } else if (isPrintableChar(character)) {
            return new TypeCommand(model, Character.toString(character));
        }
        return null;
    }

	private boolean isPrintableChar(char character) {
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

    protected boolean isControlDown() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) ||
               Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
    }

    protected boolean isShiftDown() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ||
               Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }
}