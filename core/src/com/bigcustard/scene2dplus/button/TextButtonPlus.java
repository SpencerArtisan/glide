package com.bigcustard.scene2dplus.button;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TextButtonPlus extends TextButton implements ButtonPlus {
    public TextButtonPlus(String text, Skin skin) {
        super(text, skin);
    }

    public TextButtonPlus(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public void onClick(Runnable callback) {
        onClick(callback, false);
    }

    public void onClick(Runnable callback, boolean slowOp) {
        ErrorHandler.onClick(this, callback, slowOp);
    }
}
