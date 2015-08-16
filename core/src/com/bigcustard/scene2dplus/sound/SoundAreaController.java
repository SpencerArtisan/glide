package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Clipboard;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.dialog.FileDialog;
import com.bigcustard.scene2dplus.sound.command.*;
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
        view.registerClipboardButtonListener(this::addSoundFromClipboardUrl);
        view.registerFileButtonListener(this::addSoundFromFile);
    }

    private void addAllSoundAdjustmentBehaviour() {
        view.getAllSoundControls().forEach(this::addSoundAdjustmentBehaviour);
    }

    private void addSoundListChangeBehaviour() {
        view.registerAddSoundControlsListener(this::addSoundAdjustmentBehaviour);
    }

    private void addSoundAdjustmentBehaviour(SoundControls soundControls) {
        addRenameBehaviour(soundControls);
        addDeleteBehaviour(soundControls);
        addPlaySoundBehaviour(soundControls);
    }

    private void addRenameBehaviour(SoundControls soundControls) {
        soundControls.registerNameFieldListener((text) ->
                commandHistory.execute(new ChangeNameCommand(soundControls.getSoundModel(), text)));
    }

    private void addDeleteBehaviour(SoundControls soundControls) {
        soundControls.registerDeleteButtonListener(() ->
                commandHistory.execute(new RemoveSoundCommand(model, soundControls.getSoundModel())));
    }

    private void addPlaySoundBehaviour(SoundControls soundControls) {
        soundControls.registerImageClickListener(() -> {
            soundControls.getSoundModel().sound().play();
        });
    }

    private void addSoundFromClipboardUrl() {
        try {
            commandHistory.execute(new AddSoundCommand(model, getClipboard().getContents()));
        } catch (Exception e) {
            System.err.println("Error adding sound from clipboard: " + e);
            view.onSoundFromClipboardFailure();
        }
    }

    private void addSoundFromFile() {
        view.chooseFile((file) -> {
            try {
                commandHistory.execute(new AddSoundCommand(model, file));
            } catch (Exception e) {
                System.err.println("Error adding sound from file: " + e);
                view.onSoundFromFileFailure();
            }
        });
    }
}
