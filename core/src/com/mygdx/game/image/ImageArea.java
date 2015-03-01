package com.mygdx.game.image;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class ImageArea extends Table {

    public ImageArea(Skin skin) {
        top();
        TextField importTextField = new TextField("", skin);
        importTextField.setMessageText(" Paste image url here");
        row();
        add(new Label("Game images", skin)).padTop(20).padBottom(20);
        row();
        add(importTextField).width(308);
    }

    public TextField importTextField() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
