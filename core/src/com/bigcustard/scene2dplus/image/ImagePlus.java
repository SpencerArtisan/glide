package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

public class ImagePlus {
    private static int MAX_NAME_LENGTH = 16;

    private Image image;
    private FileHandle file;
    private String name;
    private int originalWidth;
    private int originalHeight;
    private Integer width;
    private Integer height;
    private List<Runnable> changeListeners = new ArrayList<>();

    public ImagePlus(FileHandle file) {
        this(file, generateName(file), null, null);
    }

    public ImagePlus(FileHandle file, String name, Integer width, Integer height) {
        if (!file.exists()) throw new NoImageFileException(file);
        this.file = file;
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        fireChange();
    }

    public String filename() {
        return file.name();
    }

    public int maxNameLength() {
        return MAX_NAME_LENGTH;
    }

    public Image asImage() {
        init();
        return image;
    }

    public Integer width() {
        init();
        return width;
    }

    public Integer height() {
        init();
        return height;
    }

    public void setWidth(Integer newWidth) {
        init();
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
        init();
        if (newHeight != null) {
            width = newHeight * originalWidth / originalHeight;
            height = newHeight;
        } else {
            height = null;
            width = null;
        }
        fireChange();
    }

    public void registerChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    private void init() {
        if (image == null) {
            Texture texture = new Texture(file);
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            TextureRegion textureRegion = new TextureRegion(texture);
            image = new Image(textureRegion);
            originalWidth = texture.getWidth();
            originalHeight = texture.getHeight();
            if (width == null) width = originalWidth;
            if (height == null) height = originalHeight;
        }
    }

    private void fireChange() {
        for (Runnable listener : changeListeners) {
            listener.run();
        }
    }

    private static String generateName(FileHandle file) {
        String filename = file.name();
        int dotIndex = filename.lastIndexOf('.');
        int nameLength = Math.min(MAX_NAME_LENGTH, dotIndex);
        return filename.substring(0, nameLength);
    }
}
