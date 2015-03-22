package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.image.ImagePlus;

public class ChangeNameCommand implements Command {
    private final String oldName;
    private final String newName;
    private final ImagePlus image;

    public ChangeNameCommand(ImagePlus image, String name) {
        this.image = image;
        this.newName = name;
        this.oldName = image.name();
    }

    @Override
    public void execute() {
        image.setName(newName);
    }

    @Override
    public void undo() {
        image.setName(oldName);
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}
