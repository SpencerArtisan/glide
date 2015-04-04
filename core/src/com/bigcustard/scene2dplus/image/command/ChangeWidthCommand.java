package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.image.ImageModel;

public class ChangeWidthCommand implements Command {
    private final Integer oldWidth;
    private final Integer newWidth;
    private final ImageModel image;

    public ChangeWidthCommand(ImageModel image, Integer width) {
        this.image = image;
        this.newWidth = width;
        this.oldWidth = image.width();
    }

    @Override
    public void execute() {
        image.setWidth(newWidth);
    }

    @Override
    public void undo() {
        image.setWidth(oldWidth);
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}
