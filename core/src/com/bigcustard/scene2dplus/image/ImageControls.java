package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.bigcustard.scene2dplus.textfield.TextFieldPlus;

public class ImageControls {
    private final ImagePlus image;
    private final TextField nameField;
    private final TextFieldPlus widthField;
    private final TextFieldPlus heightField;

    public ImageControls(ImagePlus image, Skin skin) {
        this(image,
             createNameField(image, skin),
             createSizeField(image.width(), skin),
             createSizeField(image.height(), skin));
    }

    public ImageControls(ImagePlus image, TextField nameField, TextFieldPlus widthField, TextFieldPlus heightField) {
        this.image = image;
        this.nameField = nameField;
        this.widthField = widthField;
        this.heightField = heightField;
    }

    ImagePlus getImage() {
        return image;
    }

    TextField getNameField() {
        return nameField;
    }

    TextFieldPlus getWidthField() {
        return widthField;
    }

    TextFieldPlus getHeightField() {
        return heightField;
    }

    private static TextField createNameField(ImagePlus image, Skin skin) {
        TextField textField = new TextField(image.name(), skin);
        textField.setAlignment(Align.center);
        textField.setMaxLength(image.maxNameLength());
        return textField;
    }

    private static TextFieldPlus createSizeField(float value, Skin skin) {
        TextFieldPlus textField = new TextFieldPlus(Integer.valueOf((int) value).toString(), skin);
        textField.setAlignment(Align.center);
        textField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        return textField;
    }
}
