package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.bigcustard.scene2dplus.textfield.TextFieldPlus;
import org.apache.commons.lang3.ObjectUtils;

import java.util.function.Consumer;

public class ImageControls {
    private final ImageAreaModel model;
    private final ImagePlus image;
    private final TextFieldPlus nameField;
    private final TextFieldPlus widthField;
    private final TextFieldPlus heightField;
    private final Button deleteButton;

    public ImageControls(ImageAreaModel model, ImagePlus image, Skin skin) {
        this(model,
                image,
                createNameField(image, skin),
                createSizeField(image.width(), skin),
                createSizeField(image.height(), skin),
                createDeleteButton(skin));
    }

    private ImageControls(ImageAreaModel model,
                          ImagePlus image,
                          TextFieldPlus nameField,
                          TextFieldPlus widthField,
                          TextFieldPlus heightField,
                          Button deleteButton) {
        this.model = model;
        this.image = image;
        this.nameField = nameField;
        this.widthField = widthField;
        this.heightField = heightField;
        this.deleteButton = deleteButton;
        addModelChangeBehaviour();
        addValidationBehaviour();
    }

    ImagePlus getImage() {
        return image;
    }

    void registerNameFieldListener(Consumer<String> onChange) {
        nameField.setTextFieldListener((field, c) -> onChange.accept(field.getText()));
    }

    void registerWidthFieldListener(Consumer<Integer> onChange) {
        widthField.setTextFieldListener((field, c) -> onChange.accept(Integer.parseInt(field.getText())));
    }

    void registerHeightFieldListener(Consumer<Integer> onChange) {
        widthField.setTextFieldListener((field, c) -> onChange.accept(Integer.parseInt(field.getText())));
    }

    void registerDeleteButtonListener(Runnable onClicked) {
        deleteButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                onClicked.run();
            }
        });
    }

    void addTo(Table table, int width, Skin skin) {
        table.row();
        Actor image = getImageControl(width);
        table.add(image).width(image.getWidth()).height(image.getHeight()).padTop(20);
        table.row();
        table.add(nameField).width(width);
        table.row();
        table.add(getSizeArea(width, skin));
    }

    private Actor getImageControl(float width) {
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

    private Actor getSizeArea(float width, Skin skin) {
        Table table = new Table();
        table.add(widthField).width(width * 0.4f);
        table.add(new Label(" x ", skin)).width(width * 0.2f);
        table.add(heightField).width(width * 0.4f);
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

    private void addModelChangeBehaviour() {
        image.registerChangeListener(() -> {
            widthField.setText(ObjectUtils.toString(image.width()));
            heightField.setText(ObjectUtils.toString(image.height()));
            nameField.setText(image.name());
        });
    }

    private void addValidationBehaviour() {
        model.registerValidationListener((image) -> {
            ValidationResult result = image.validate();
            nameField.setValid(result.isNameValid());
            widthField.setValid(result.isWidthValid());
            heightField.setValid(result.isHeightValid());
        });
    }
}
