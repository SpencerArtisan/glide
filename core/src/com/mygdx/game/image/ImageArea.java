package com.mygdx.game.image;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ImageArea extends ScrollPane {
    public static final float WIDTH = 250;
    private TextButton importButton;
    private Map<GameImage, ImageControls> imageControlMap = new HashMap<>();
    private ImageAreaModel model;
    private Skin skin;

    public ImageArea(Skin skin) {
        super(new Table(), skin);
        this.skin = skin;
        model = new ImageAreaModel();
        layoutControls();
        new ImageAreaController(new ImageGrabber(), this, model);
    }

    private void layoutControls() {
        Table table = (Table) getWidget();
        addHeader(table);
        addImportButton(table);

        for (GameImage gameImage : model.getImages()) {
            addImageControls(table, gameImage);
        }
    }

    private void addHeader(Table table) {
        table.top();
        table.row();
        table.add(new Label("Game images", skin)).padTop(20).padBottom(20);
    }

    private void addImportButton(Table table) {
        importButton = new TextButton("Add from clipboard", skin);
        table.row();
        table.add(importButton).width(WIDTH);
    }

    private void addImageControls(Table table, GameImage gameImage) {
        ImageControls imageControls = createImageControls(gameImage);
        imageControlMap.put(gameImage, imageControls);

        table.row();
        Image image = gameImage.asImage();
        table.add(image).width(WIDTH).height(image.getHeight() * WIDTH / image.getWidth()).padTop(20);
        table.row();
        table.add(imageControls.getNameField()).width(WIDTH);
        table.row();
        table.add(createSizeArea(imageControls));
    }

    private ImageControls createImageControls(GameImage gameImage) {
        return new ImageControls(gameImage,
                                 createNameField(gameImage),
                                 createSizeField(gameImage.width()),
                                 createSizeField(gameImage.height()));
    }

    private TextField createNameField(GameImage image) {
        TextField textField = new TextField(image.name(), skin);
        textField.setAlignment(Align.center);
        textField.setMaxLength(image.maxNameLength());
        return textField;
    }

    private TextField createSizeField(float value) {
        TextField textField = new TextField(Integer.valueOf((int) value).toString(), skin);
        textField.setAlignment(Align.center);
        return textField;
    }

    private Table createSizeArea(ImageControls imageControls) {
        Table table = new Table();
        table.add(imageControls.getWidthField()).width(WIDTH * 0.4f);
        table.add(new Label(" x ", skin)).width(WIDTH * 0.2f);
        table.add(imageControls.getHeightField()).width(WIDTH * 0.4f);
        return table;
    }

    public TextButton importButton() {
        return importButton;
    }

    public Collection<ImageControls> getImageControlList() {
        return imageControlMap.values();
    }

    public void refresh() {
        ((Table) getWidget()).reset();
        layoutControls();
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

    public ImageControls getImageControls(GameImage image) {
        return imageControlMap.get(image);
    }
}
