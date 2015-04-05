package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ImageArea extends ScrollPane {
    public static final int WIDTH = 250;
    private Skin skin;
    private ImageAreaModel model;
    private TextButton importButton;
    private Map<ImageModel, ImageControls> imageControlMap = new HashMap<>();
    private Notifier<ImageControls> addImageControlsNotifier = new Notifier<>();
    private Notifier<ImageControls> removeImageControlsNotifier = new Notifier<>();

    public ImageArea(ImageAreaModel model, Skin skin) {
        super(new Table(), skin);
        this.skin = skin;
        this.model = model;
        createImportButton(skin);
        createAllImageControls();
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

    void registerAddImageControlsListener(Consumer<ImageControls> onChanged) {
        addImageControlsNotifier.add(onChanged);
    }

    void registerRemoveImageControlsListener(Consumer<ImageControls> onChanged) {
        removeImageControlsNotifier.add(onChanged);
    }

    Collection<ImageControls> getAllImageControls() {
        return imageControlMap.values();
    }

    void onImageImportFailure() {
        importButton.setText("Dodgy image!");
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
        getAllImageControls().forEach((imageControls) -> imageControls.addTo(layoutTable, WIDTH, skin));
    }

    private void createImportButton(Skin skin) {
        importButton = new TextButton("Add from clipboard", skin);
    }

    private void addHeader(Table table) {
        table.top();
        table.row();
        table.add(new Label("Game images", skin)).padTop(20).padBottom(20);
    }

    private void addImportButton(Table table) {
        table.row();
        table.add(importButton).width(WIDTH);
    }

    private void createAllImageControls() {
        model.images().forEach(this::createImageControls);
    }

    private ImageControls createImageControls(ImageModel image) {
        ImageControls imageControls = new ImageControls(image, skin);
        imageControlMap.put(image, imageControls);
        return imageControls;
    }

    private void addModelChangeBehaviour(ImageAreaModel model) {
        model.registerAddImageListener(this::onAddImage);
        model.registerRemoveImageListener(this::onRemoveImage);
    }

    private void onAddImage(ImageModel image) {
        ImageControls imageControls = createImageControls(image);
        addImageControlsNotifier.notify(imageControls);
        layoutControls();
    }

    private void onRemoveImage(ImageModel image) {
        ImageControls imageControls = imageControlMap.remove(image);
        removeImageControlsNotifier.notify(imageControls);
        layoutControls();
    }
}
