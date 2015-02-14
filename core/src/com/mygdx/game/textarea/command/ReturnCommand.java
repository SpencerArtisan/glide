package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class ReturnCommand extends AbstractCommand {
    public ReturnCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.insert('\n');
        model.caret().moveDown();
        model.caret().moveToFarLeft();
    }
}
