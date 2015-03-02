package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageAreaModel {
    private List<GameImage> images = new ArrayList<>();

    public void add(FileHandle file) {
        GameImage gameImage = new GameImage(file);
        images.add(gameImage);
    }

    public List<GameImage> getImages() {
        return images;
    }
}
