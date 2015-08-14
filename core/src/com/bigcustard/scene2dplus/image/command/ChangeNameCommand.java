package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.image.ImageModel;

public class ChangeNameCommand implements Command {
    private final String oldName;
    private final String newName;
    private final ImageModel image;

    public ChangeNameCommand(ImageModel image, String name) {
        this.image = image;
        this.newName = name;
        this.oldName = image.name();
    }

    @Override
    public void execute() {
        image.name(newName);
    }

    @Override
    public void undo() {
        image.name(oldName);
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}
