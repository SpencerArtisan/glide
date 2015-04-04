package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImagePlus;
import com.bigcustard.scene2dplus.image.ImagePlusModel;

public class RemoveImageCommand implements Command {
    private ImageAreaModel model;
    private ImagePlusModel image;

    public RemoveImageCommand(ImageAreaModel model, ImagePlusModel image) {
        this.model = model;
        this.image = image;
    }

    @Override
    public void execute() {
        model.removeImage(image);
    }

    @Override
    public void undo() {
        model.addImage(image);
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}
