package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ImageAreaModel {
    private Map<String, FileHandle> images = new HashMap<String, FileHandle>();

    public void add(FileHandle image, String name) {
        images.put(name, image);
    }

    public Map<String, FileHandle> getImages() {
        return images;
    }
}
