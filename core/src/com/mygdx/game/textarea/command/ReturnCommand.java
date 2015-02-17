package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class ReturnCommand extends AbstractCommand {
    private TextAreaModel.State state;

    public ReturnCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        state = model.getState();
        model.insert("\n");
    }

    @Override
    public void undo() {
        model.setState(state);
    }
}
