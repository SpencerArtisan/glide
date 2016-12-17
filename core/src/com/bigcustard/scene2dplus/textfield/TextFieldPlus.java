package com.bigcustard.scene2dplus.textfield;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class TextFieldPlus extends TextField {
    private Skin skin;

    public TextFieldPlus(String value, Skin skin, String style) {
        super(value, skin, style);
        this.skin = skin;
        setOnlyFontChars(true);
    }

    public TextFieldPlus(String value, Skin skin) {
        super(value, skin);
        this.skin = skin;
        setOnlyFontChars(true);
    }

    public Skin getSkin() {
        return skin;
    }


    @Override
    protected void moveCursor (boolean forward, boolean jump) {
    }

    @Override
    public boolean keyDown (InputEvent event, int keycode) {
        return true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
