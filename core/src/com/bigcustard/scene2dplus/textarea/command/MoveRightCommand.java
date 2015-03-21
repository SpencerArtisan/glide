package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public class MoveRightCommand extends AbstractTextAreaCommand {
    public MoveRightCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.caret().moveRight();
    }
}
