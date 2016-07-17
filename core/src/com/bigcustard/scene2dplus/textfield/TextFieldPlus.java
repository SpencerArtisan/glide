package com.bigcustard.scene2dplus.textfield;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TextFieldPlus extends TextField {
    private boolean valid = true;
    private Drawable invalidBackground;
    private Skin skin;

    public TextFieldPlus(String value, Skin skin) {
        super(value, skin);
        this.skin = skin;
        TextureRegionDrawable white = (TextureRegionDrawable) skin.getDrawable("white");
        invalidBackground = white.tint(Color.valueOf("dc322f88"));
    }

    public Skin getSkin() {
        return skin;
    }

    public void setValid(boolean isValid) {
        valid = isValid;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Drawable originalBackground = getStyle().background;
        Drawable originalFocussedBackground = getStyle().focusedBackground;
        if (!valid) {
            getStyle().background = invalidBackground;
            getStyle().focusedBackground = invalidBackground;
        }
        super.draw(batch, parentAlpha);
        getStyle().background = originalBackground;
        getStyle().focusedBackground = originalFocussedBackground;
    }
}
