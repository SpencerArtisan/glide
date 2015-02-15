package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class MoveDownCommand extends AbstractCommand {
    public MoveDownCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.caret().moveDown();
    }

    @Override
    public void undo() {
        new MoveUpCommand(model).execute();
    }
}
