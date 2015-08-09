package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.bigcustard.scene2dplus.textfield.TextFieldPlus;
import com.google.common.base.Strings;
import org.apache.commons.lang3.ObjectUtils;

import java.util.function.Consumer;

public class SoundControls {
    private final SoundModel sound;
    private final TextFieldPlus nameField;
    private final Button deleteButton;

    public SoundControls(SoundModel sound, Skin skin) {
        this.sound = sound;
        this.nameField = createNameField(sound, skin);
        this.deleteButton = createDeleteButton(skin);
        addModelChangeBehaviour();
    }

    SoundModel getSound() {
        return sound;
    }

    void registerNameFieldListener(Consumer<String> onChange) {
        nameField.setTextFieldListener((field, c) -> onChange.accept(field.getText()));
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
        Actor sound = getSoundControl(width, skin);
        table.add(sound).width(sound.getWidth()).height(sound.getHeight()).padTop(20).padBottom(10);
        table.row();
        table.add(nameField).width(width);
    }

    private Actor getSoundControl(float width, Skin skin) {
        WidgetGroup group = new WidgetGroup();
        Image uiSound = new Image(skin, "sound");
        uiSound.setFillParent(true);
        group.addActor(uiSound);
        group.setHeight(uiSound.getHeight() * width / uiSound.getWidth());
        group.setWidth(width);

        deleteButton.setPosition(width - 34, group.getHeight() - 34);
        group.addActor(deleteButton);
        return group;
    }

    private static TextFieldPlus createNameField(SoundModel sound, Skin skin) {
        TextFieldPlus textField = new TextFieldPlus(sound.name(), skin);
        textField.setAlignment(Align.center);
        textField.setMaxLength(sound.maxNameLength());
        return textField;
    }

    private static Button createDeleteButton(Skin skin) {
        return new ImageButton(skin, "trash-button");
    }

    private void addModelChangeBehaviour() {
        sound.registerChangeListener((sound) -> {
            nameField.setText(sound.name());
        });
    }
}
