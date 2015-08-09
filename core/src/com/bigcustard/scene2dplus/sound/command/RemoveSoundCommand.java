package com.bigcustard.scene2dplus.sound.command;

import com.bigcustard.scene2dplus.command.Command;
import com.bigcustard.scene2dplus.sound.SoundAreaModel;
import com.bigcustard.scene2dplus.sound.SoundModel;

public class RemoveSoundCommand implements Command {
    private SoundAreaModel model;
    private SoundModel sound;

    public RemoveSoundCommand(SoundAreaModel model, SoundModel sound) {
        this.model = model;
        this.sound = sound;
    }

    @Override
    public void execute() {
        model.removeSound(sound);
    }

    @Override
    public void undo() {
        model.addSound(sound);
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}
