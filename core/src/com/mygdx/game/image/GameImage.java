package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GameImage extends Image {
    private TextureRegion textureRegion;
    private FileHandle file;

    public GameImage(FileHandle file) {
        super(getTextureRegion(file));
        this.file = file;
    }

    private static TextureRegion getTextureRegion(FileHandle file) {
        Texture texture = new Texture(file);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return new TextureRegion(texture);
//        setSize(texture.getWidth(), texture.getHeight());
    }

    public String name() {
        return file.name();
    }
}
