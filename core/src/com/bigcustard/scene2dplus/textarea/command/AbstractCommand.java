package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;

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

    @Override
    public boolean canExecute() {
        return true;
    }
}
