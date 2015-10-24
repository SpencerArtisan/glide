package com.bigcustard.scene2dplus.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.bigcustard.scene2dplus.Spacer;
import com.bigcustard.scene2dplus.command.Command;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.badlogic.gdx.scenes.scene2d.Touchable.disabled;
import static com.badlogic.gdx.scenes.scene2d.Touchable.enabled;
import static com.bigcustard.util.Util.tryGet;

public class ButtonBar extends HorizontalGroup implements Disposable {
    private Skin skin;

    public ButtonBar(Skin skin) {
        this.skin = skin;
        padLeft(8);
        padBottom(5);
        padRight(8);
        space(7);
    }

    public void addSpacer(int pixels) {
        addActor(new Spacer(pixels));
    }

    public void addImage(String imageName) {
        addActor(new Image(skin, imageName));
    }

    public void addTextButton(String text, Supplier<Command> commandFactory) {
        addButton(new TextButton(text, skin), commandFactory);
    }

    public void addImageButton(String text, String styleName, Supplier<Command> commandFactory) {
        addButton(new ImageTextButton(text, skin, styleName), commandFactory);
    }

    public void refreshEnabledStatuses() {
        Actor[] children = getChildren().items;
        for (Actor child : children) {
            if (child instanceof Button) {
                child.fire(new RefreshEnabledStatusEvent());
            }
        }
    }

    private void addButton(final Button button, Supplier<Command> commandFactory) {
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                commandFactory.get().execute();
                refreshEnabledStatuses();
            }

            public boolean handle(Event event) {
                if (event instanceof RefreshEnabledStatusEvent) {
                    boolean enable = tryGet(() -> commandFactory.get().canExecute(), false);
                    button.setDisabled(!enable);
                    button.setTouchable(enable ? enabled : disabled);
                }
                return super.handle(event);
            }
        });
        addActor(button);
        refreshEnabledStatuses();
    }

    @Override
    public void dispose() {
        Executors.newSingleThreadScheduledExecutor()
                 .schedule(() -> getChildren().forEach(Actor::clearListeners), 100, TimeUnit.MILLISECONDS);
    }

    private static class RefreshEnabledStatusEvent extends Event {}
}
