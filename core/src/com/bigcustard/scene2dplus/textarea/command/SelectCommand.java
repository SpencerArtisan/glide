package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public class SelectCommand extends AbstractTextAreaCommand {
    private XY<Integer> startLocation;
    private XY<Integer> endLocation;

    public SelectCommand(TextAreaModel model, XY<Integer> startLocation, XY<Integer> endLocation) {
        super(model);
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    @Override
    public void execute() {
        model.caret().setSelection(startLocation, endLocation);
    }
}
