package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImagePlus;

public class AddImageCommand implements Command {
    private ImageAreaModel model;
    private String url;
    private ImagePlus image;

    public AddImageCommand(ImageAreaModel model, String url) {
        this.model = model;
        this.url = url;
    }

    @Override
    public void execute() {
        image = model.addImage(url);
    }

    @Override
    public void undo() {
        model.removeImage(image);
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}
