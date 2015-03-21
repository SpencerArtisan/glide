package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public class MoveToCommand extends AbstractTextAreaCommand {
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
