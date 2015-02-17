package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class TypeCommand extends AbstractCommand {
    private String typed;
    private TextAreaModel.State state;

    public TypeCommand(TextAreaModel model, String typed) {
        super(model);
        this.typed = typed;
    }

    @Override
    public void execute() {
        state = model.getState();
        model.insert(typed);
    }

    @Override
    public void undo() {
        model.setState(state);
    }
}
