package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public abstract class AbstractTextAreaCommand extends AbstractCommand {
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
}
