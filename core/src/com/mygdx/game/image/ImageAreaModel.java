package com.mygdx.game.image;

import java.util.List;

public interface ImageAreaModel {
    GameImage addImage(String imageUrl);
    List<GameImage> getImages();
}
