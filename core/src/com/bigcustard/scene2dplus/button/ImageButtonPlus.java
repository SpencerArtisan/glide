package com.bigcustard.scene2dplus.button;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ImageButtonPlus extends ImageButton implements ButtonPlus {
    public ImageButtonPlus(Skin skin, String styleName) {
        super(skin, styleName);
        setSkin(skin);
    }

    public void onClick(Runnable callback) {
        onClick(callback, false);
    }

    public void onClick(Runnable callback, boolean slowOp) {
        ErrorHandler.onClick(this, callback, slowOp);
    }
}
