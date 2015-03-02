package com.mygdx.game.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.textarea.command.Command;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class ButtonBar extends HorizontalGroup {
    private Skin skin;

    public ButtonBar(Skin skin) {
        this.skin = skin;
        pad(18);
        space(7);
    }

    public void addSpacer(int pixels) {
        addActor(new Actor() {
            public float getWidth() {
                return pixels;
            }
        });
    }

    public void addImage(String imageName) {
        addActor(new Image(skin, imageName));
    }

    public void addTextButton(String text, Supplier<Command> commandSupplier) {
        Command command = commandSupplier.get();
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                command.execute();
            }
        });
        boolean enable = command.canExecute();
        button.setDisabled(!enable);
        button.setTouchable(enable ? Touchable.enabled : Touchable.disabled);
        addActor(button);
    }

    public void addTextButton(String text, Runnable action, BooleanSupplier enabled) {
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
        boolean enable = enabled.getAsBoolean();
        button.setDisabled(!enable);
        button.setTouchable(enable ? Touchable.enabled : Touchable.disabled);
        addActor(button);
    }

    public void addImageButton(String text, String styleName) {
        ImageTextButton button = new ImageTextButton(text, skin, styleName);
        addActor(button);
    }

}
