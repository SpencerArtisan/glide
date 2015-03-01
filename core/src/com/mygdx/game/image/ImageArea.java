package com.mygdx.game.image;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class ImageArea extends Table {
    private TextButton importTextField;

    public ImageArea(Skin skin) {
        layout(skin);
        new ImageAreaController(new ImageGrabber(), this, new ImageAreaModel());
    }

    private void layout(Skin skin) {
        top();
        importTextField = new TextButton("Add from clipboard", skin, "small");
//        importTextField.getLabel().setWrap(true);
//        importTextField.getLabel().setHeight(60);
//        importTextField.getLabel().setWidth(320);
//        importTextField = new TextField("", skin) {{ setMessageText(" Paste image url here"); }};
        row();
        add(new Label("Game images", skin)).padTop(20).padBottom(20);
        row();
        add(importTextField).width(260);
    }

    public TextButton importTextButton() {
        return importTextField;
    }
}
