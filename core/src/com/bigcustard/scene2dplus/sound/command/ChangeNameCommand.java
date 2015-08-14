package com.bigcustard.scene2dplus.sound.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.sound.SoundModel;

public class ChangeNameCommand implements Command {
    private final String oldName;
    private final String newName;
    private final SoundModel sound;

    public ChangeNameCommand(SoundModel sound, String name) {
        this.sound = sound;
        this.newName = name;
        this.oldName = sound.name();
    }

    @Override
    public void execute() {
        sound.name(newName);
    }

    @Override
    public void undo() {
        sound.name(oldName);
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}
