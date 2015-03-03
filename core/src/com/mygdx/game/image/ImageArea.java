package com.mygdx.game.image;

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
        for (GameImage image : imageFiles) {
            table.row();
            table.add(image).width(WIDTH).height(image.getHeight() * WIDTH / image.getWidth()).padTop(20);
            table.row();
            table.add(new TextField(image.name(), skin)).width(WIDTH);
            table.row();
            table.add(createSizeArea(image));
        }
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

    public TextButton importTextButton() {
        return importTextButton;
    }

    public void refresh() {
        ((Table) getWidget()).reset();
        layout(skin);
    }
}
