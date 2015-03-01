package com.mygdx.game.image;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ImageFromFile extends Actor {
    private TextureRegion textureRegion;

    public ImageFromFile(String textureFilename) {
        Texture texture = new Texture(textureFilename);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textureRegion = new TextureRegion(texture);
        setSize(texture.getWidth(), texture.getHeight());
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}