package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class MoveRightCommand extends AbstractCommand {
    public MoveRightCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.caret().moveRight();
    }

    @Override
    public void undo() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
