package com.mygdx.game.image;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import java.util.List;

public class ImageArea extends ScrollPane {
    public static final float WIDTH = 250;
    private TextButton importTextButton;
    private ImageAreaModel model;
    private Skin skin;

    public ImageArea(Skin skin) {
        super(new Table(), skin);

        this.skin = skin;
        importTextButton = new TextButton("Add from clipboard", skin);
        model = new ImageAreaModel();
        layout(skin);
        new ImageAreaController(new ImageGrabber(), this, model);
    }

    private void layout(Skin skin) {
        Table table = (Table) getWidget();
        table.top();
        table.row();
        table.add(new Label("Game images", skin)).padTop(20).padBottom(20);
        table.row();
        table.add(importTextButton).width(WIDTH);

        List<GameImage> imageFiles = model.getImages();
        for (GameImage gameImage : imageFiles) {
            Image image = gameImage.asImage();
            table.row();
            table.add(image).width(WIDTH).height(image.getHeight() * WIDTH / image.getWidth()).padTop(20);
            table.row();
            table.add(createNameArea(gameImage)).width(WIDTH);
            table.row();
            table.add(createSizeArea(gameImage));
        }
    }

    private TextField createNameArea(GameImage image) {
        TextField textField = new TextField(image.name(), skin);
        textField.setAlignment(Align.center);
        textField.setMaxLength(image.maxNameLength());
        return textField;
    }

    private Table createSizeArea(GameImage image) {
        Table table = new Table();
        addNumberTextField(image.getWidth(), table);
        table.add(new Label(" x ", skin)).width(WIDTH * 0.2f);
        addNumberTextField(image.getHeight(), table);
        return table;
    }

    private void addNumberTextField(float value, Table table) {
        TextField textField = new TextField(Integer.valueOf((int) value).toString(), skin);
        textField.setAlignment(Align.center);
        table.add(textField).width(WIDTH * 0.4f);
    }

    public TextButton importButton() {
        return importTextButton;
    }

    public void refresh() {
        ((Table) getWidget()).reset();
        layout(skin);
    }

    public void showFailure() {
        importTextButton.setText("Dodgy image!");

        importTextButton.addAction(
                Actions.sequence(
                Actions.repeat(10,
                    Actions.sequence(Actions.moveBy(-3, 0, 0.02f, Interpolation.sineOut),
                                     Actions.moveBy(6, 0, 0.04f, Interpolation.sine),
                                     Actions.moveBy(-3, 0, 0.02f, Interpolation.sineIn))),
                    Actions.run(() -> importTextButton.setText("Add from clipboard"))));
    }
}
