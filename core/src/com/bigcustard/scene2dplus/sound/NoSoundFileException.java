package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.files.FileHandle;

public class NoSoundFileException extends RuntimeException {
    public NoSoundFileException(FileHandle file) {
        super("The sound file is missing: " + file.path());
    }
}
