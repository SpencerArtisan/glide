package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

public class GameImage {
    private static int MAX_NAME_LENGTH = 16;

    private Image image;
    private FileHandle file;
    private String name;
    private int originalWidth;
    private int originalHeight;
    private Integer width;
    private Integer height;
    private List<Runnable> listeners = new ArrayList<>();

    public GameImage(FileHandle file) {
        createImage(file);
        this.file = file;
        this.name = generateName();
    }

    private void createImage(FileHandle file) {
        Texture texture = new Texture(file);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion textureRegion = new TextureRegion(texture);
        image = new Image(textureRegion);
        width = texture.getWidth();
        height = texture.getHeight();
        originalWidth = width;
        originalHeight = height;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        fireChange();
    }

    public int maxNameLength() {
        return MAX_NAME_LENGTH;
    }

    public Image asImage() {
        return image;
    }

    public Integer width() {
        return width;
    }

    public Integer height() {
        return height;
    }

    public void setWidth(Integer newWidth) {
        if (newWidth != null) {
            height = newWidth * originalHeight / originalWidth;
            width = newWidth;
        } else {
            height = null;
            width = null;
        }
        fireChange();
    }

    public void setHeight(Integer newHeight) {
        if (newHeight != null) {
            width = newHeight * originalWidth / originalHeight;
            height = newHeight;
        } else {
            height = null;
            width = null;
        }
        fireChange();
    }

    private void fireChange() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    private String generateName() {
        String filename = file.name();
        int dotIndex = filename.lastIndexOf('.');
        int nameLength = Math.min(MAX_NAME_LENGTH, dotIndex);
        return filename.substring(0, nameLength);
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }
}
