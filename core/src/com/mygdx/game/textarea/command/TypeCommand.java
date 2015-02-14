package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class TypeCommand extends AbstractCommand {
    private Character character;

    public TypeCommand(TextAreaModel model, Character character) {
        super(model);
        this.character = character;
    }

    @Override
    public void execute() {
        model.insert(character);
        model.caret().moveRight();    }
}
