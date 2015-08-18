package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.textfield.TextFieldPlus;

import java.util.function.Consumer;

public class SoundControls implements Disposable {
    private final SoundModel soundModel;
    private final TextFieldPlus nameField;
    private final Button deleteButton;
    private Image uiSound;
    private static int count;

    public SoundControls(SoundModel soundModel, Skin skin) {
        this.soundModel = soundModel;
        this.nameField = createNameField(soundModel, skin);
        this.deleteButton = createDeleteButton(skin);
        uiSound = new Image(skin, "soundModel");
        uiSound.setFillParent(true);
        addModelChangeBehaviour();
        System.out.println("SoundControls: " + ++count);
    }

    SoundModel getSoundModel() {
        return soundModel;
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

    void registerImageClickListener(Runnable onClicked) {
        uiSound.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
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
        soundModel.registerChangeListener((sound) -> {
            nameField.setText(sound.name());
        });
    }

    @Override
    public void dispose() {
        count--;
        if (soundModel != null) soundModel.dispose();
    }
}
