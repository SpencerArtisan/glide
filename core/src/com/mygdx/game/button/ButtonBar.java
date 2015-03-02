package com.mygdx.game.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.function.BooleanSupplier;

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

    public void addTextButton(String text, Runnable action, BooleanSupplier enabled) {
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }

            public boolean handle(Event event) {
                if (event instanceof ModelChange) {
                    boolean enable = enabled.getAsBoolean();
                    button.setDisabled(!enable);
                    button.setTouchable(enable ? Touchable.enabled : Touchable.disabled);
                }
                return super.handle(event);
            }
        });
        addActor(button);
        refreshEnabledStatuses();
    }

    public void addImageButton(String text, String styleName) {
        ImageTextButton button = new ImageTextButton(text, skin, styleName);
        addActor(button);
        refreshEnabledStatuses();
    }

    public void refreshEnabledStatuses() {
        SnapshotArray<Actor> children = getChildren();
        for (Actor child : children) {
            if (child instanceof Button) {
                Button button = (Button) child;
                button.fire(new ModelChange());
            }
        }
    }

    private static class ModelChange extends Event {
    }
}
