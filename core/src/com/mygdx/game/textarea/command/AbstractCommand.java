package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public abstract class AbstractCommand implements Command {
    protected TextAreaModel model;
    private TextAreaModel.State state;

    public AbstractCommand(TextAreaModel model) {
        this.model = model;
        state = model.getState();
    }

    @Override
    public void undo() {
        model.setState(state);
    }
}
