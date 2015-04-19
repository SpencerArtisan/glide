package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Strings;

import java.util.function.Consumer;

public class ImageModel {
    private static int MAX_NAME_LENGTH = 12;

    private FileHandle file;
    private String name;
    private int originalWidth;
    private int originalHeight;
    private Integer width;
    private Integer height;
    private Notifier<ImageModel> changeNotifier = new Notifier<>();
    private Notifier<ImageModel> validationNotifier = new Notifier<>();

    public ImageModel(FileHandle file, Integer width, Integer height) {
        this(file, generateName(file), width, height);
    }

    public ImageModel(FileHandle file, String name, Integer width, Integer height) {
        this.file = file;
        this.name = name;
        this.width = width;
        this.height = height;
        this.originalWidth = width;
        this.originalHeight = height;
    }

    public void registerValidationListener(Consumer<ImageModel> listener) {
        validationNotifier.add(listener);
    }

    public void registerChangeListener(Consumer<ImageModel> listener) {
        changeNotifier.add(listener);
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

    public FileHandle file() {
        return file;
    }

    public int maxNameLength() {
        return MAX_NAME_LENGTH;
    }

    public Integer width() {
        return width;
    }

    public Integer height() {
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

    private void changeAttribute(Runnable doChange) {
        boolean initialValidationState = validate().isValid();
        doChange.run();
        if (initialValidationState != validate().isValid()) {
            validationNotifier.notify(this);
        }
        changeNotifier.notify(this);
    }

    private static String generateName(FileHandle file) {
        String filename = file.name();
        int dotIndex = filename.lastIndexOf('.');
        int nameLength = Math.min(MAX_NAME_LENGTH, dotIndex);
        return filename.substring(0, nameLength) + filename.substring(dotIndex);
    }

    public ValidationResult validate() {
        return new ValidationResult(
                this,
                width() != null,
                height() != null,
                !Strings.isNullOrEmpty(name()));
    }
}
