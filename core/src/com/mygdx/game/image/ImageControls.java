package com.mygdx.game.image;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class ImageControls {
    private final GameImage gameImage;
    private final TextField nameField;
    private final TextField widthField;
    private final TextField heightField;

    public ImageControls(GameImage gameImage, TextField nameField, TextField widthField, TextField heightField) {
        this.gameImage = gameImage;
        this.nameField = nameField;
        this.widthField = widthField;
        this.heightField = heightField;
    }

    GameImage getGameImage() {
        return gameImage;
    }

    TextField getNameField() {
        return nameField;
    }

    TextField getWidthField() {
        return widthField;
    }

    TextField getHeightField() {
        return heightField;
    }
}
