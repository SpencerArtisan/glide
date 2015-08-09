package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.util.Notifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SoundArea extends ScrollPane implements Disposable {
    public static final int WIDTH = 250;
    private Skin skin;
    private SoundAreaModel model;
    private TextButton importButton;
    private Map<SoundModel, SoundControls> SoundControlMap = new HashMap<>();
    private Notifier<SoundControls> addSoundControlsNotifier = new Notifier<>();
    private Notifier<SoundControls> removeSoundControlsNotifier = new Notifier<>();

    public SoundArea(SoundAreaModel model, Skin skin) {
        super(new Table(), skin);
        this.skin = skin;
        this.model = model;
        createImportButton(skin);
        createAllSoundControls();
        layoutControls();
        addModelChangeBehaviour(model);
    }

    void registerImportButtonListener(Runnable onClicked) {
        importButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                onClicked.run();
            }
        });
    }

    void registerAddSoundControlsListener(Consumer<SoundControls> onChanged) {
        addSoundControlsNotifier.add(onChanged);
    }

    void registerRemoveSoundControlsListener(Consumer<SoundControls> onChanged) {
        removeSoundControlsNotifier.add(onChanged);
    }

    Collection<SoundControls> getAllSoundControls() {
        return SoundControlMap.values();
    }

    void onSoundImportFailure() {
        importButton.setText("Dodgy Sound!");
        importButton.addAction(
                Actions.sequence(
                        Actions.repeat(10,
                                Actions.sequence(Actions.moveBy(-3, 0, 0.02f, Interpolation.sineOut),
                                        Actions.moveBy(6, 0, 0.04f, Interpolation.sine),
                                        Actions.moveBy(-3, 0, 0.02f, Interpolation.sineIn))),
                        Actions.run(() -> importButton.setText("Add from clipboard"))));
    }

    private void layoutControls() {
        Table layoutTable = (Table) getWidget();
        layoutTable.clearChildren();
        addHeader(layoutTable);
        addImportButton(layoutTable);
        getAllSoundControls().forEach((SoundControls) -> SoundControls.addTo(layoutTable, WIDTH, skin));
    }

    private void createImportButton(Skin skin) {
        importButton = new TextButton("Add from clipboard", skin);
    }

    private void addHeader(Table table) {
        table.top();
        table.row();
        table.add(new Label("Game sounds", skin)).padTop(20).padBottom(20);
    }

    private void addImportButton(Table table) {
        table.row();
        table.add(importButton).width(WIDTH);
    }

    private void createAllSoundControls() {
        model.sounds().forEach(this::createSoundControls);
    }

    private SoundControls createSoundControls(SoundModel sound) {
        SoundControls SoundControls = new SoundControls(sound, skin);
        SoundControlMap.put(sound, SoundControls);
        return SoundControls;
    }

    private void addModelChangeBehaviour(SoundAreaModel model) {
        model.registerAddSoundListener(this::onAddSound);
        model.registerRemoveSoundListener(this::onRemoveSound);
    }

    private void onAddSound(SoundModel Sound) {
        SoundControls SoundControls = createSoundControls(Sound);
        addSoundControlsNotifier.notify(SoundControls);
        layoutControls();
    }

    private void onRemoveSound(SoundModel Sound) {
        SoundControls SoundControls = SoundControlMap.remove(Sound);
        removeSoundControlsNotifier.notify(SoundControls);
        layoutControls();
    }

    @Override
    public void dispose() {
        model.dispose();
        addSoundControlsNotifier.dispose();
        removeSoundControlsNotifier.dispose();
    }
}
