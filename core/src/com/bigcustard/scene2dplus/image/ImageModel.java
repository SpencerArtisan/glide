package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.util.Notifier;
import com.bigcustard.util.Watchable;
import com.google.common.base.Strings;

import java.util.function.Consumer;

public class ImageModel implements Disposable {
    private static int MAX_NAME_LENGTH = 17;

    private FileHandle file;
    private int originalWidth;
    private int originalHeight;
    private Watchable<String> name;
    private Watchable<Integer> width;
    private Watchable<Integer> height;
    private Notifier<ImageModel> changeNotifier = new Notifier<>();
    private Notifier<ImageModel> validationNotifier = new Notifier<>();

    public ImageModel(FileHandle file, Integer width, Integer height) {
        this(file, generateName(file), width, height);
    }

    public ImageModel(FileHandle file, String name, Integer width, Integer height) {
        this(file, name, width, height, width, height);
    }

    public ImageModel(FileHandle file, String name, Integer width, Integer height, Integer originalWidth, Integer originalHeight) {
        this.file = file;
        this.name = new Watchable<>(name);
        this.width = new Watchable<>(width);
        this.height = new Watchable<>(height);
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }

    public void registerValidationListener(Consumer<ImageModel> listener) {
        validationNotifier.watch(listener);
    }

    public void registerChangeListener(Consumer<ImageModel> listener) {
        changeNotifier.watch(listener);
    }

    public Watchable<String> name() {
        return name;
    }

    public void name(String name) {
        changeAttribute(() -> this.name.set(name));
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

    public Watchable<Integer> width() {
        return width;
    }

    public Watchable<Integer> height() {
        return height;
    }

    public void width(Integer newWidth) {
        changeAttribute(() -> {
            if (newWidth != null) {
                height.set(newWidth * originalHeight / originalWidth);
                width.set(newWidth);
            } else {
                height.set(null);
                width.set(null);
            }
        });
    }

    public void height(Integer newHeight) {
        changeAttribute(() -> {
            if (newHeight != null) {
                width.set(newHeight * originalWidth / originalHeight);
                height.set(newHeight);
            } else {
                height.set(null);
                width.set(null);
            }
        });
    }

    public int originalWidth() {
        return originalWidth;
    }

    public int originalHeight() {
        return originalHeight;
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
        return filename.substring(0, nameLength);
    }

    public ValidationResult validate() {
        return new ValidationResult(
                this,
                width() != null,
                height() != null,
                !Strings.isNullOrEmpty(name().get()));
    }

    @Override
    public void dispose() {
        changeNotifier.dispose();
        validationNotifier.dispose();
    }
}
