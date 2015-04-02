package com.bigcustard.scene2dplus.image;

public class ValidationResult {
    private ImagePlus image;
    private boolean widthValid = true;
    private boolean heightValid = true;
    private boolean nameValid = true;

    public ValidationResult(ImagePlus image, boolean widthValid, boolean heightValid, boolean nameValid) {
        this.image = image;
        this.widthValid = widthValid;
        this.heightValid = heightValid;
        this.nameValid = nameValid;
    }

    public ImagePlus image() {
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
