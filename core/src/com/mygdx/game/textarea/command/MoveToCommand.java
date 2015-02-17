package com.mygdx.game.textarea.command;

import com.mygdx.game.XY;
import com.mygdx.game.textarea.TextAreaModel;

public class MoveToCommand extends AbstractCommand {
    private final XY<Integer> newCaretLocation;

    public MoveToCommand(TextAreaModel model, XY<Integer> caretLocation) {
        super(model);
        this.newCaretLocation = caretLocation;
    }

    @Override
    public void execute() {
        model.caret().setLocation(newCaretLocation);
    }
}
