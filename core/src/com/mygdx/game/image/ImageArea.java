package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.*;
import java.util.List;

public class ImageArea extends Table {
    public static final int WIDTH = 250;
    private TextButton importTextButton;
    private ImageAreaModel model;
    private Skin skin;

    public ImageArea(Skin skin) {
        this.skin = skin;
        importTextButton = new TextButton("Add from clipboard", skin);
        model = new ImageAreaModel();
        layout(skin);
        new ImageAreaController(new ImageGrabber(), this, model);
    }

    private void layout(Skin skin) {
        top();
        row();
        add(new Label("Game images", skin)).padTop(20).padBottom(20);
        row();
        add(importTextButton).width(WIDTH);

        List<GameImage> imageFiles = model.getImages();
        for (GameImage image : imageFiles) {
            row();
            add(image).width(WIDTH).height(image.getHeight() * WIDTH / image.getWidth()).padTop(20);
            row();
            add(new TextField(image.name(), skin)).width(WIDTH);
        }
    }

    public TextButton importTextButton() {
        return importTextButton;
    }

    public void refresh() {
        reset();
        layout(skin);
    }
}
