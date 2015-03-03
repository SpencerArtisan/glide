package com.mygdx.game.textarea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.XY;
import com.mygdx.game.textarea.command.*;

import java.awt.event.KeyEvent;

public class TextAreaController extends ClickListener {
    private TextAreaModel model;
    private ScrollableTextArea view;
    private CommandHistory commandHistory;
    private XY<Integer> touchDownLocation;
    private boolean dragging;

    public TextAreaController(TextAreaModel model, ScrollableTextArea view, CommandHistory commandHistory) {
        this.model = model;
        this.view = view;
        this.commandHistory = commandHistory;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (isRedo(keycode)) {
            commandHistory.redo();
        }
        if (isUndo(keycode)) {
            commandHistory.undo();
        }
        if (isCopy(keycode)) {
            commandHistory.execute(new CopyCommand(model));
        }
        if (isPaste(keycode)) {
            commandHistory.execute(new PasteCommand(model));
        }
        return true;
    }

    @Override
    public boolean keyTyped(InputEvent event, char character) {
        commandHistory.execute(getKeyTypedCommand(character));
        view.onModelChange(model);
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (isOver(event.getListenerActor(), x, y)) {
            XY<Integer> caretLocation = view.worldPositionToCaretLocation(new XY<Integer>((int) x, (int) y));
            if (dragging) {
                dragging = false;
                commandHistory.execute(new SelectCommand(model, touchDownLocation, caretLocation));
            } else {
                commandHistory.execute(new MoveToCommand(model, caretLocation));
            }
        }
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (isOver(event.getListenerActor(), x, y)) {
            model.caret().clearSelection();
            this.touchDownLocation = view.worldPositionToCaretLocation(new XY<Integer>((int) x, (int) y));
            Stage stage = view.getStage();
            if (stage != null) stage.setKeyboardFocus(view);
        }
        return true;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if (isOver(event.getListenerActor(), x, y)) {
            dragging = true;
            XY<Integer> dragLocation = view.worldPositionToCaretLocation(new XY<Integer>((int) x, (int) y));
            new SelectCommand(model, touchDownLocation, dragLocation).execute();
        }
    }

    private boolean isCopy(int keycode) {
        return isControlDown() && keycode == Input.Keys.C;
    }

    private boolean isPaste(int keycode) {
        return isControlDown() && keycode == Input.Keys.V;
    }

    private boolean isUndo(int keycode) {
        return isControlDown() && keycode == Input.Keys.Z && !isShiftDown();
    }

    private boolean isRedo(int keycode) {
        return isControlDown() && keycode == Input.Keys.Z && isShiftDown();
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