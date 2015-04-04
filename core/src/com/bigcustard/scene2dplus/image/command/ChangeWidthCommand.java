package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.image.ImagePlus;
import com.bigcustard.scene2dplus.image.ImagePlusModel;

public class ChangeWidthCommand implements Command {
    private final Integer oldWidth;
    private final Integer newWidth;
    private final ImagePlusModel image;

    public ChangeWidthCommand(ImagePlusModel image, Integer width) {
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
