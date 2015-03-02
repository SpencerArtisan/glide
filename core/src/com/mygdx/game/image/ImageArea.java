package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.Collection;

public class ImageArea extends Table {
    public static final int WIDTH = 260;
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

        Collection<FileHandle> imageFiles = model.getImages().values();
        for (FileHandle imageFile : imageFiles) {
            ImageFromFile image = new ImageFromFile(imageFile.path());
            row();
            add(image).width(WIDTH).height(image.getHeight() * WIDTH / image.getWidth());
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
