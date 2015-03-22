package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.bigcustard.scene2dplus.command.CommandHistory;

import java.util.HashMap;
import java.util.Map;

public class ImageArea extends ScrollPane {
    public static final float WIDTH = 250;
    private TextButton importButton;
    private Map<ImagePlus, ImageControls> imageControlMap = new HashMap<>();
    private ImageAreaModel model;
    private Skin skin;
    private Table table;

    public ImageArea(ImageAreaModel model, Skin skin, CommandHistory commandHistory) {
        super(new Table(), skin);
        this.skin = skin;
        this.model = model;
        createImportButton(skin);
        layoutControls();
        ImageAreaController controller = new ImageAreaController(this, model, commandHistory);
        controller.init();
    }

    private void createImportButton(Skin skin) {
        importButton = new TextButton("Add from clipboard", skin);
    }

    public TextButton importButton() {
        return importButton;
    }

    public ImageControls getImageControls(ImagePlus image) {
        if (!imageControlMap.containsKey(image)) {
            createImageControls(image);
        }
        return imageControlMap.get(image);
    }

    private void createImageControls(ImagePlus image) {
        ImageControls imageControls = new ImageControls(image, skin);
        imageControlMap.put(image, imageControls);
    }

    public void showFailure() {
        importButton.setText("Dodgy image!");
        importButton.addAction(
                Actions.sequence(
                        Actions.repeat(10,
                                Actions.sequence(Actions.moveBy(-3, 0, 0.02f, Interpolation.sineOut),
                                        Actions.moveBy(6, 0, 0.04f, Interpolation.sine),
                                        Actions.moveBy(-3, 0, 0.02f, Interpolation.sineIn))),
                        Actions.run(() -> importButton.setText("Add from clipboard"))));
    }

    public void layoutControls() {
        table = (Table) getWidget();
        table.clearChildren();
        addHeader(table);
        addImportButton(table);

        for (ImagePlus gameImage : model.getImages()) {
            addImageControls(table, gameImage);
        }
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

    private void addImageControls(Table table, ImagePlus gameImage) {
        ImageControls imageControls = getImageControls(gameImage);

        table.row();
        Actor image = imageControls.getImageControl(WIDTH);
        table.add(image).width(image.getWidth()).height(image.getHeight()).padTop(20);
        table.row();
        table.add(imageControls.getNameField()).width(WIDTH);
        table.row();
        table.add(imageControls.getSizeArea(WIDTH, skin));
    }
}
