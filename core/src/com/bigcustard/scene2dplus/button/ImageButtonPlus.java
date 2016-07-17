package com.bigcustard.scene2dplus.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ImageButtonPlus extends ImageButton implements ButtonPlus {
    public ImageButtonPlus(Skin skin, String styleName) {
        super(skin, styleName);
        setSkin(skin);
    }

    public void onClick(Runnable callback) {
        ErrorHandler.onClick(this, callback);
    }
}
