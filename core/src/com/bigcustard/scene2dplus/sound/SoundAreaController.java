package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.sound.command.AddSoundCommand;
import com.bigcustard.scene2dplus.sound.command.ChangeNameCommand;
import com.bigcustard.scene2dplus.sound.command.RemoveSoundCommand;
import com.google.common.annotations.VisibleForTesting;

public class SoundAreaController {
    private final SoundArea view;
    private final SoundAreaModel model;
    private final CommandHistory commandHistory;

    public SoundAreaController(SoundArea view, SoundAreaModel model, CommandHistory commandHistory) {
        this.view = view;
        this.model = model;
        this.commandHistory = commandHistory;
    }

    public void init() {
        addImportBehaviour();
        addAllSoundAdjustmentBehaviour();
        addSoundListChangeBehaviour();
    }

    @VisibleForTesting
    protected Clipboard getClipboard() {
        return Gdx.app.getClipboard();
    }

    private void addImportBehaviour() {
        view.registerImportButtonListener(this::addSoundFromClipboardUrl);
    }

    private void addAllSoundAdjustmentBehaviour() {
        for (SoundControls soundControls : view.getAllSoundControls()) {
            addSoundAdjustmentBehaviour(soundControls);
        }
    }

    private void addSoundListChangeBehaviour() {
        view.registerAddSoundControlsListener(this::addSoundAdjustmentBehaviour);
    }

    private void addSoundAdjustmentBehaviour(SoundControls soundControls) {
        addRenameBehaviour(soundControls);
        addDeleteBehaviour(soundControls);
    }

    private void addRenameBehaviour(SoundControls soundControls) {
        soundControls.registerNameFieldListener((text) ->
                commandHistory.execute(new ChangeNameCommand(soundControls.getSound(), text)));
    }

    private void addDeleteBehaviour(SoundControls soundControls) {
        soundControls.registerDeleteButtonListener(() ->
                commandHistory.execute(new RemoveSoundCommand(model, soundControls.getSound())));
    }

    private void addSoundFromClipboardUrl() {
        try {
            commandHistory.execute(new AddSoundCommand(model, getClipboard().getContents()));
        } catch (Exception e) {
            System.err.println("Error adding sound from clipboard: " + e);
            view.onSoundImportFailure();
        }
    }
}
