package com.bigcustard.scene2dplus.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.Spacer;
import com.bigcustard.scene2dplus.command.Command;

import java.util.concurrent.Executors;
import java.util.function.Supplier;

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
        addButton(new TextButtonPlus(text, skin), commandFactory, false);
    }

    public void addImageButton(String text, String styleName, Supplier<Command> commandFactory, boolean slowOp) {
        addButton(new ImageTextButtonPlus(text, skin, styleName), commandFactory, slowOp);
    }

    public void refreshEnabledStatuses() {
        Actor[] children = getChildren().items;
        for (Actor child : children) {
            if (child instanceof Button) {
                child.fire(new RefreshEnabledStatusEvent());
            }
        }
    }

    private void addButton(final Button button, Supplier<Command> commandFactory, boolean slowOp) {
        ErrorHandler.onClick(button,
                () -> {
                    commandFactory.get().execute();
                    refreshEnabledStatuses();
                },
                (event) -> {
                    if (event instanceof RefreshEnabledStatusEvent) {
                        boolean enable = tryGet(() -> commandFactory.get().canExecute(), false);
                        button.setDisabled(!enable);
                    }
                },
                slowOp
        );
        addActor(button);
        refreshEnabledStatuses();
    }

    @Override
    public void dispose() {
        Executors.newSingleThreadExecutor().submit(() -> getChildren().forEach(Actor::clearListeners));
    }

    private static class RefreshEnabledStatusEvent extends Event {
    }
}
