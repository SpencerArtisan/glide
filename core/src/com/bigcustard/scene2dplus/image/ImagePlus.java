package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.google.common.base.Strings;

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
    private List<Runnable> validationListeners = new ArrayList<>();

    public ImagePlus(FileHandle file) {
        this(file, generateName(file), null, null);
    }

    public ImagePlus(FileHandle file, String name, Integer width, Integer height) {
        this.file = file;
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public void registerValidationListener(Runnable listener) {
        validationListeners.add(listener);
    }

    public void registerChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        changeAttribute(() -> {
            this.name = name;
        });
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
        changeAttribute(() -> {
            if (newWidth != null) {
                height = newWidth * originalHeight / originalWidth;
                width = newWidth;
            } else {
                height = null;
                width = null;
            }
        });
    }

    private void changeAttribute(Runnable doChange) {
        init();
        boolean initialValidationState = validate().isValid();

        doChange.run();

        if (initialValidationState != validate().isValid()) {
            informValidationListeners();
        }
        informChangeListeners();
    }

    public void setHeight(Integer newHeight) {
        changeAttribute(() -> {
            if (newHeight != null) {
                width = newHeight * originalWidth / originalHeight;
                height = newHeight;
            } else {
                height = null;
                width = null;
            }
        });
    }

    private void init() {
        if (image == null) {
            if (!file.exists()) throw new NoImageFileException(file);
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

    private void informChangeListeners() {
        changeListeners.stream().forEach(Runnable::run);
    }

    private void informValidationListeners() {
        validationListeners.stream().forEach(Runnable::run);
    }

    private static String generateName(FileHandle file) {
        String filename = file.name();
        int dotIndex = filename.lastIndexOf('.');
        int nameLength = Math.min(MAX_NAME_LENGTH, dotIndex);
        return filename.substring(0, nameLength);
    }

    public ValidationResult validate() {
        return new ValidationResult(
                this,
                width() != null,
                height() != null,
                !Strings.isNullOrEmpty(name()));
        //&& Collections.frequency(imageNames, imagePlus.name()) == 1;
    }

}
