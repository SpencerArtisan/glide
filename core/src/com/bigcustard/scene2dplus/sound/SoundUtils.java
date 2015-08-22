package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.util.Util;

import java.io.InputStream;

public class SoundUtils {
    public static SoundModel importSound(InputStream soundStream, String url, FileHandle folder) {
        FileHandle mainSoundFile = Util.importFile(soundStream, url, folder);
        return new SoundModel(mainSoundFile);
    }
}
