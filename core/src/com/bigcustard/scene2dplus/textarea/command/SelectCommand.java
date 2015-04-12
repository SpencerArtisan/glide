package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;

public class SelectCommand extends AbstractTextAreaCommand {
    private XY startLocation;
    private XY endLocation;

    public SelectCommand(TextAreaModel model, XY startLocation, XY endLocation) {
        super(model);
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    @Override
    public void execute() {
        model.caret().setSelection(startLocation, endLocation);
    }
}
