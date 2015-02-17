package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class TypeCommand extends AbstractCommand {
    private String typed;

    public TypeCommand(TextAreaModel model, String typed) {
        super(model);
        this.typed = typed;
    }

    @Override
    public void execute() {
        model.insert(typed);
    }
}
