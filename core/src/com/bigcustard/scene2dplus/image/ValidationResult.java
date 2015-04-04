package com.bigcustard.scene2dplus.image;

public class ValidationResult {
    private final ImageModel image;
    private final boolean widthValid;
    private final boolean heightValid;
    private final boolean nameValid;

    public ValidationResult(ImageModel image, boolean widthValid, boolean heightValid, boolean nameValid) {
        this.image = image;
        this.widthValid = widthValid;
        this.heightValid = heightValid;
        this.nameValid = nameValid;
    }

    public ImageModel image() {
        return image;
    }

    public boolean isWidthValid() {
        return widthValid;
    }

    public boolean isHeightValid() {
        return heightValid;
    }

    public boolean isNameValid() {
        return nameValid;
    }

    public boolean isValid() {
        return isWidthValid() && isHeightValid() && isNameValid();
    }
}
