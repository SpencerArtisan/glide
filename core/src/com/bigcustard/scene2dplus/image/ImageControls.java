package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

    public ImageControls(ImagePlus image,
                         TextFieldPlus nameField,
                         TextFieldPlus widthField,
                         TextFieldPlus heightField,
                         Button deleteButton) {
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

    Actor getImageControl(float width) {
        WidgetGroup group = new WidgetGroup();
        Image uiImage = image.asImage();
        uiImage.setFillParent(true);
        group.addActor(uiImage);
        group.setHeight(uiImage.getHeight() * width / uiImage.getWidth());
        group.setWidth(width);

        deleteButton.setPosition(width - 20, group.getHeight() - 15);
        deleteButton.setSize(30, 30);
        deleteButton.setColor(0.5f, 0.5f, 0.5f, 0.7f);
        group.addActor(deleteButton);
        return group;
    }

    Actor getSizeArea(float width, Skin skin) {
        Table table = new Table();
        table.add(getWidthField()).width(width * 0.4f);
        table.add(new Label(" x ", skin)).width(width * 0.2f);
        table.add(getHeightField()).width(width * 0.4f);
        return table;
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
