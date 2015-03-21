package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public class DeleteCommand extends AbstractTextAreaCommand {
    public DeleteCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.deleteCharacter();
    }
}
