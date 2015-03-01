package com.mygdx.game.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.io.IOException;

public class ImageAreaController {
    private final ImageGrabber grabber;
    private final ImageArea view;
    private ImageAreaModel model;

    public ImageAreaController(final ImageGrabber grabber, ImageArea view, ImageAreaModel model) {
        this.grabber = grabber;
        this.view = view;
        this.model = model;
        view.importTextButton().addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                onImageUrlProvided(Gdx.app.getClipboard().getContents());
            }
        });
    }

    void onImageUrlProvided(String url) {
        try {
            FileHandle imageFile = grabber.grab(url);
            model.add(imageFile, url);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
