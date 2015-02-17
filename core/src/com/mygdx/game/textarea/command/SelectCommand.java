package com.mygdx.game.textarea.command;

import com.mygdx.game.XY;
import com.mygdx.game.textarea.TextAreaModel;

public class SelectCommand extends AbstractCommand {
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
