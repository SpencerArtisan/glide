package com.mygdx.game.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageGrabber {
    public FileHandle grab(String url) throws IOException {
        String folder = ".";
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        InputStream is = new URL(url).openStream();
        FileHandle mainImageFile = Gdx.files.local(folder + fileName);
        mainImageFile.write(is, false);
        is.close();
        return mainImageFile;
    }
}
