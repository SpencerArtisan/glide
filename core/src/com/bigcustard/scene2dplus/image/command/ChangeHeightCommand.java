package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.image.ImageModel;

public class ChangeHeightCommand implements Command {
    private final Integer oldHeight;
    private final Integer newHeight;
    private final ImageModel image;

    public ChangeHeightCommand(ImageModel image, Integer height) {
        this.image = image;
        this.newHeight = height;
        this.oldHeight = image.height();
    }

    @Override
    public void execute() {
        image.setHeight(newHeight);
    }

    @Override
    public void undo() {
        image.setHeight(oldHeight);
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}
