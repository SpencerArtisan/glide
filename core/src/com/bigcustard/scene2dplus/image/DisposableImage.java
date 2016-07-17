package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;

public class DisposableImage extends Image implements Disposable {
    private final Texture texture;
    private static int count;

    public DisposableImage(Texture texture) {
        super(texture);
        this.texture = texture;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
