package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.dialog.FileDialog;
import com.bigcustard.util.Notifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SoundArea extends ScrollPane implements Disposable {
    public static final int WIDTH = 250;
    private Skin skin;
    private SoundAreaModel model;
    private TextButton clipboardButton;
    private TextButton fileButton;
    private Map<SoundModel, SoundControls> soundControlMap = new HashMap<>();
    private Notifier<SoundControls> addSoundControlsNotifier = new Notifier<>();
    private Notifier<SoundControls> removeSoundControlsNotifier = new Notifier<>();
    private static int count;

    public SoundArea(SoundAreaModel model, Skin skin) {
        super(new Table(), skin);
        this.skin = skin;
        this.model = model;
        createClipboardButton(skin);
        createFileButton(skin);
        createAllSoundControls();
        layoutControls();
        addModelChangeBehaviour(model);
        System.out.println("SoundAreas: " + ++count);
    }

    void registerClipboardButtonListener(Runnable onClicked) {
        clipboardButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                onClicked.run();
            }
        });
    }

    void registerFileButtonListener(Runnable onClicked) {
        fileButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                onClicked.run();
            }
        });
    }

    void registerAddSoundControlsListener(Consumer<SoundControls> onChanged) {
        addSoundControlsNotifier.watch(onChanged);
    }

    void registerRemoveSoundControlsListener(Consumer<SoundControls> onChanged) {
        removeSoundControlsNotifier.watch(onChanged);
    }

    Collection<SoundControls> getAllSoundControls() {
        return soundControlMap.values();
    }

    void onSoundFromClipboardFailure() {
        dodgyWiggle(clipboardButton);
    }

    void onSoundFromFileFailure() {
        dodgyWiggle(fileButton);
    }

    private void dodgyWiggle(TextButton button) {
        String originalText = button.getText().toString();
        button.setText("Dodgy sound!");
        button.addAction(
                Actions.sequence(
                        Actions.repeat(10,
                                Actions.sequence(Actions.moveBy(-3, 0, 0.02f, Interpolation.sineOut),
                                        Actions.moveBy(6, 0, 0.04f, Interpolation.sine),
                                        Actions.moveBy(-3, 0, 0.02f, Interpolation.sineIn))),
                        Actions.run(() -> button.setText(originalText))));
    }

    private void layoutControls() {
        Table layoutTable = (Table) getWidget();
        layoutTable.background(skin.getDrawable("solarizedNew"));
        layoutTable.clearChildren();
        addButtons(layoutTable);
        getAllSoundControls().forEach((soundControls) -> soundControls.addTo(layoutTable, WIDTH, skin));
    }

    private void createClipboardButton(Skin skin) {
        clipboardButton = new TextButton("Add from clipboard", skin);
    }

    private void createFileButton(Skin skin) {
        fileButton = new TextButton("Add from file", skin);
    }

    private void addButtons(Table table) {
        table.top();
        table.row();
        table.add(clipboardButton).width(WIDTH).padTop(15);
        table.row();
        table.add(fileButton).width(WIDTH).padTop(8);
    }

    private void createAllSoundControls() {
        model.sounds().forEach(this::createSoundControls);
    }

    private SoundControls createSoundControls(SoundModel sound) {
        SoundControls soundControls = new SoundControls(sound, skin);
        soundControlMap.put(sound, soundControls);
        return soundControls;
    }

    private void addModelChangeBehaviour(SoundAreaModel model) {
        model.registerAddSoundListener(this::onAddSound);
        model.registerRemoveSoundListener(this::onRemoveSound);
    }

    private void onAddSound(SoundModel sound) {
        SoundControls soundControls = createSoundControls(sound);
        addSoundControlsNotifier.notify(soundControls);
        layoutControls();
    }

    private void onRemoveSound(SoundModel sound) {
        SoundControls soundControls = soundControlMap.remove(sound);
        removeSoundControlsNotifier.notify(soundControls);
        layoutControls();
    }

    @Override
    public void dispose() {
        model.dispose();
        addSoundControlsNotifier.dispose();
        removeSoundControlsNotifier.dispose();
        for (SoundControls soundControls : soundControlMap.values()) {
            soundControls.dispose();
        }
        count--;
    }

    public void chooseFile(Consumer<FileHandle> fileConsumer) {
        FileDialog files = FileDialog.createLoadDialog("Pick your sound", skin, Gdx.files.external("."));
        files.setResultListener((success, result) -> {
            fileConsumer.accept(result);
            return true;
        });
        files.show(getStage());
    }
}
