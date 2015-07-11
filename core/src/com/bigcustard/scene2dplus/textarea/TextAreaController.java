package com.bigcustard.scene2dplus.textarea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.textarea.command.*;
import com.google.common.annotations.VisibleForTesting;

import java.awt.event.KeyEvent;

public class TextAreaController extends ClickListener {
    private TextAreaModel model;
    private ScrollableTextArea view;
    private CommandHistory commandHistory;
    private XY touchDownLocation;
    private boolean dragging;
    private Character lastCharacterTyped;

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

        event.getCharacter();
        return true;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        lastCharacterTyped = null;
        return false;
    }

    @Override
    public boolean keyTyped(InputEvent event, char character) {
        commandHistory.execute(getKeyTypedCommand(character, event.getKeyCode()));
        view.onModelChange(model);
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (isOver(event.getListenerActor(), x, y)) {
            XY caretLocation = view.worldPositionToCaretLocation(new XY((int) x, (int) y));
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
            this.touchDownLocation = view.worldPositionToCaretLocation(new XY((int) x, (int) y));
            Stage stage = view.getStage();
            if (stage != null) stage.setKeyboardFocus(view);
        }
        return true;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if (isOver(event.getListenerActor(), x, y)) {
            dragging = true;
            XY dragLocation = view.worldPositionToCaretLocation(new XY((int) x, (int) y));
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

    private Command getKeyTypedCommand(char character, int keyCode) {
        if (Key.Delete.is(character)) {
            return new DeleteCommand(model);
        } else if (keyCode == Input.Keys.UP) {
            return new MoveUpCommand(model);
        } else if (keyCode == Input.Keys.DOWN) {
            return new MoveDownCommand(model);
        } else if (keyCode == Input.Keys.RIGHT) {
            return new MoveRightCommand(model);
        } else if (keyCode == Input.Keys.LEFT) {
            return new MoveLeftCommand(model);
        } else if (Key.Return.is(character)) {
            return new ReturnCommand(model);
        } else if (Key.Tab.is(character)) {
            return new TabCommand(model);
        } else if (isPrintableChar(character, keyCode)) {
            if (lastCharacterTyped == null || lastCharacterTyped != character) {
                lastCharacterTyped = character;
                return new TypeCommand(model, Character.toString(character));
            }
        }
        return null;
    }

    private boolean isPrintableChar(char character, int keyCode) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(character);
        return (!Character.isISOControl(character)) &&
                character != KeyEvent.CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS &&
                keyCode != Input.Keys.DOWN &&
                keyCode != Input.Keys.UP &&
                keyCode != Input.Keys.LEFT &&
                keyCode != Input.Keys.RIGHT;
    }

    @VisibleForTesting
    protected boolean isControlDown() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) ||
                Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
    }

    @VisibleForTesting
    protected boolean isShiftDown() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ||
                Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }
}