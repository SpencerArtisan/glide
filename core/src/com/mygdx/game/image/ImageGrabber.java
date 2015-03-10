package com.mygdx.game.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageGrabber {
    private static int MAX_NAME_LENGTH = 13;

    public FileHandle grab(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        FileHandle mainImageFile = Gdx.files.local(getFilename(url));
        mainImageFile.write(is, false);
        is.close();
        return mainImageFile;
    }

    private String getFilename(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        int dotIndex = filename.lastIndexOf('.');
        int nameLength = Math.min(MAX_NAME_LENGTH, dotIndex);
        String suffix = filename.substring(dotIndex);
        return filename.substring(0, nameLength) + suffix;
    }
}
