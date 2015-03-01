package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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
        view.importTextField().setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped(TextField textField, char c) {
                onImageUrlChanged(textField.getText());
            }
        });
    }

    void onImageUrlChanged(String url) {
        try {
            FileHandle imageFile = grabber.grab(url);
            model.add(imageFile, url);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
