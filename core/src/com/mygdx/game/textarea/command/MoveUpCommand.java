package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class MoveUpCommand extends AbstractCommand {
    public MoveUpCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.caret().moveUp();
    }
}
