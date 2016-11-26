package com.bigcustard.scene2dplus.button;

import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ImageTextButtonPlus extends ImageTextButton implements ButtonPlus {
    public ImageTextButtonPlus(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public void onClick(Runnable callback, boolean slowOp) {
        ErrorHandler.onClick(this, callback, slowOp);
    }
}
