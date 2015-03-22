package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;

public class NoImageFileException extends RuntimeException {
    public NoImageFileException(FileHandle file) {
        super("The image file is missing: " + file.path());
    }
}
