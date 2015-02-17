package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;

public class TabCommand extends AbstractCommand {
    private int spaces;

    public TabCommand(TextAreaModel model) {
        super(model);
    }

    @Override
    public void execute() {
        int column = model.caret().location().x;
        int toColumn = (column / 4) * 4 + 4;
        spaces = toColumn - column;
        for (int i = 0; i < spaces; i++) {
            new TypeCommand(model, " ").execute();
        }
    }

    @Override
    public void undo() {
        for (int i = 0; i < spaces; i++) {
            new DeleteCommand(model).execute();
        }
    }
}
