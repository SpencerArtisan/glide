package com.bigcustard.scene2dplus.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class TextButtonPlus extends TextButton implements ButtonPlus {
    public TextButtonPlus(String text, Skin skin) {
        super(text, skin);
    }

    public TextButtonPlus(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public void onClick(Runnable callback) {
        ErrorHandler.onClick(this, callback);
    }
}
