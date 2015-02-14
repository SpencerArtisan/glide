package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class MoveLeftCommand extends AbstractCommand {
    public MoveLeftCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.caret().moveLeft();
    }
}
