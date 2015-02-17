package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class DeleteCommand extends AbstractCommand {
    private TextAreaModel.State state;

    public DeleteCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        state = model.getState();
        model.deleteCharacter();
    }

    @Override
    public void undo() {
        model.setState(state);
    }
}
