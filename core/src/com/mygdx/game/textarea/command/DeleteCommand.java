package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class DeleteCommand extends AbstractCommand {
    private Character deleted;

    public DeleteCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        deleted = model.deleteCharacter();
    }

    @Override
    public void undo() {
        new TypeCommand(model, deleted).execute();
    }
}
