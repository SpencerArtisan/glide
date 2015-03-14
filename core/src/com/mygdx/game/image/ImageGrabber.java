package com.mygdx.game.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.code.Program;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageGrabber {
    private Program program;

    public ImageGrabber(Program program) {
        this.program = program;
    }

    public FileHandle grab(String url) throws IOException {
        String filename = generatePathname(url);
        InputStream is = new URL(url).openStream();
        FileHandle mainImageFile = Gdx.files.local(filename);
        mainImageFile.write(is, false);
        is.close();
        return mainImageFile;
    }

    private String generatePathname(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        return program.getGameFolder() + filename;
    }
}
