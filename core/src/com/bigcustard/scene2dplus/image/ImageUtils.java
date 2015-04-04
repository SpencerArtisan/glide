package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ImageUtils {
    public static Image asImage(FileHandle file) {
        if (!file.exists()) throw new NoImageFileException(file);
        Texture texture = new Texture(file);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion textureRegion = new TextureRegion(texture);
        return new Image(textureRegion);
    }
}
