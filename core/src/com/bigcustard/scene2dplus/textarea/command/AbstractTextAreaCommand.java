package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public abstract class AbstractTextAreaCommand implements Command {
    protected TextAreaModel model;
    private TextAreaModel.State state;

    public AbstractTextAreaCommand(TextAreaModel model) {
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
