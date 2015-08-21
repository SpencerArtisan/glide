package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.dialog.FileDialog;
import com.bigcustard.util.Notifier;

import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class ImageArea extends ScrollPane implements Disposable {
    private Skin skin;
    private ImageAreaModel model;
    private final CommandHistory commandHistory;
    private TextButton clipboardButton;
    private TextButton fileButton;
    private List<ImageControls> imageControlsList = new ArrayList<>();
    private Map<ImageModel, ImageControls> imageControlMap = new HashMap<>();
    private Notifier<ImageControls> addImageControlsNotifier = new Notifier<>();
    private Notifier<ImageControls> removeImageControlsNotifier = new Notifier<>();
    private static int count;

    public ImageArea(ImageAreaModel model, Skin skin, CommandHistory commandHistory) {
        super(new Table(), skin);
        this.skin = skin;
        this.model = model;
        this.commandHistory = commandHistory;
        this.setScrollingDisabled(true, false);
        createClipboardButton(skin);
        createFileButton(skin);
        createAllImageControls();
        layoutControls();
        addModelChangeBehaviour(model);
        System.out.println("Image areas: " + ++count);
        pack();
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

    void registerAddImageControlsListener(Consumer<ImageControls> onChanged) {
        addImageControlsNotifier.add(onChanged);
    }

    void registerRemoveImageControlsListener(Consumer<ImageControls> onChanged) {
        removeImageControlsNotifier.add(onChanged);
    }

    List<ImageControls> getAllImageControls() {
        return imageControlsList;
    }

    void onImageFromClipboardFailure() {
        dodgyWiggle(clipboardButton);
    }

    void onImageFromFileFailure() {
        dodgyWiggle(fileButton);
    }

    private void dodgyWiggle(TextButton button) {
        String originalText = button.getText().toString();
        button.setText("Dodgy image!");
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
        layoutTable.top();
        addButtons(layoutTable);
//        getAllImageControls().forEach((imageControls) -> imageControls.addTo(layoutTable, WIDTH, skin));
//        layoutTable.setFillParent(true);
        layoutTable.pack();

    }

    private void createClipboardButton(Skin skin) {
        clipboardButton = new TextButton("Add from clipboard", skin);
    }

    private void createFileButton(Skin skin) {
        fileButton = new TextButton("Add from file", skin);
    }

    private void addButtons(Table table) {
//        table.top();
//        table.row();

        // TEMP
//        table.row();
//
        table.defaults().pad(12);
        table.add(clipboardButton).fillX();
        table.row();
        table.add(fileButton).fillX();
        table.row();
        table.add(new EditableImage(new Image(skin, "powered_by"), "power", 100, 40).editor(skin, commandHistory));
    }

    private void createAllImageControls() {
        model.images().forEach(this::createImageControls);
    }

    private ImageControls createImageControls(ImageModel image) {
        ImageControls imageControls = new ImageControls(image, skin);
        imageControlMap.put(image, imageControls);
        imageControlsList.add(imageControls);
        return imageControls;
    }

    private void addModelChangeBehaviour(ImageAreaModel model) {
        model.registerAddImageListener(this::onAddImage);
        model.registerRemoveImageListener(this::onRemoveImage);
    }

    private void onAddImage(ImageModel image) {
        ImageControls imageControls = new ImageControls(image, skin);
        imageControlMap.put(image, imageControls);
        imageControlsList.add(0, imageControls);
        addImageControlsNotifier.notify(imageControls);
        layoutControls();
    }

    private void onRemoveImage(ImageModel image) {
        ImageControls imageControls = imageControlMap.remove(image);
        removeImageControlsNotifier.notify(imageControls);
        layoutControls();
    }

    @Override
    public void dispose() {
        model.dispose();
        addImageControlsNotifier.dispose();
        removeImageControlsNotifier.dispose();
        for (ImageControls imageControls : imageControlMap.values()) {
            imageControls.dispose();
        }
        count--;
    }

    public void chooseFile(Consumer<FileHandle> fileConsumer) {
        FileDialog files = FileDialog.createLoadDialog("Pick your image", skin, Gdx.files.external("."));
        files.setResultListener((success, result) -> {
            fileConsumer.accept(result);
            return true;
        });
        files.show(getStage());
    }


}
