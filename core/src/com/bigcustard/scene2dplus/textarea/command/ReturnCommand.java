package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public class ReturnCommand extends AbstractCommand {
    public ReturnCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.insert("\n");
    }
}