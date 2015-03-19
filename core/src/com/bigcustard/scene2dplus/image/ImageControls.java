package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.bigcustard.scene2dplus.textfield.TextFieldPlus;

public class ImageControls {
    private final ImagePlus image;
    private final TextFieldPlus nameField;
    private final TextFieldPlus widthField;
    private final TextFieldPlus heightField;
    private Button deleteButton;

    public ImageControls(ImagePlus image, Skin skin) {
        this(image,
             createNameField(image, skin),
             createSizeField(image.width(), skin),
             createSizeField(image.height(), skin),
             createDeleteButton(skin));
    }

    public ImageControls(ImagePlus image, TextFieldPlus nameField, TextFieldPlus widthField, TextFieldPlus heightField, Button deleteButton) {
        this.image = image;
        this.nameField = nameField;
        this.widthField = widthField;
        this.heightField = heightField;
        this.deleteButton = deleteButton;
    }

    ImagePlus getImage() {
        return image;
    }

    TextFieldPlus getNameField() {
        return nameField;
    }

    TextFieldPlus getWidthField() {
        return widthField;
    }

    TextFieldPlus getHeightField() {
        return heightField;
    }

    Button getDeleteButton() {
        return deleteButton;
    }

    private static TextFieldPlus createNameField(ImagePlus image, Skin skin) {
        TextFieldPlus textField = new TextFieldPlus(image.name(), skin);
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

    private static Button createDeleteButton(Skin skin) {
        return new ImageButton(skin, "trash-button");
    }
}
