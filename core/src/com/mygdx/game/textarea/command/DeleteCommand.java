package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class DeleteCommand extends AbstractCommand {
    public DeleteCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        model.deleteCharacter();
    }
}
