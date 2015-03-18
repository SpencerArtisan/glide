package com.bigcustard.scene2dplus.textfield;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TextFieldPlus extends TextField {
    private final TextureRegionDrawable white;
    private boolean valid;
    private Drawable invalidBackground;

    public TextFieldPlus(String value, Skin skin) {
        super(value, skin);
        white = (TextureRegionDrawable) skin.getDrawable("white");
        invalidBackground = white.tint(Color.valueOf("dc322f88"));
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

    public void setValid(boolean isValid) {
        valid = isValid;
    }
}
