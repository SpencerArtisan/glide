package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class ImageControls {
    private final ImagePlus gameImage;
    private final TextField nameField;
    private final TextField widthField;
    private final TextField heightField;

    public ImageControls(ImagePlus gameImage, Skin skin) {
        this(gameImage,
             createNameField(gameImage, skin),
             createSizeField(gameImage.width(), skin),
             createSizeField(gameImage.height(), skin));
    }

    public ImageControls(ImagePlus gameImage, TextField nameField, TextField widthField, TextField heightField) {
        this.gameImage = gameImage;
        this.nameField = nameField;
        this.widthField = widthField;
        this.heightField = heightField;
    }

    ImagePlus getGameImage() {
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

    private static TextField createNameField(ImagePlus image, Skin skin) {
        TextField textField = new TextField(image.name(), skin);
        textField.setAlignment(Align.center);
        textField.setMaxLength(image.maxNameLength());
        return textField;
    }

    private static TextField createSizeField(float value, Skin skin) {
        TextField textField = new TextField(Integer.valueOf((int) value).toString(), skin);
        textField.setAlignment(Align.center);
        textField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        return textField;
    }
}
