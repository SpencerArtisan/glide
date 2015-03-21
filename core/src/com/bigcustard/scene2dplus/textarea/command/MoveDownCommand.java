package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public class MoveDownCommand extends AbstractTextAreaCommand {
    public MoveDownCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.caret().moveDown();
    }
}
