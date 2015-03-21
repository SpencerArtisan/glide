package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public class MoveUpCommand extends AbstractTextAreaCommand {
    public MoveUpCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.caret().moveUp();
    }
}
