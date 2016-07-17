package com.bigcustard.scene2dplus.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bigcustard.scene2dplus.dialog.ErrorDialog;
import com.bigcustard.scene2dplus.dialog.FileDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class ErrorHandler {
    private static String MESSAGE = "Oops.  Something has gone wrong.\r\n\r\nWhatever you did, don't do it again!\r\n\r\nThe guy who wrote this program has been\r\nautomatically electrocuted and ordered\r\nto fix the problem.";
    private static Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    public static void onClick(Widget list, Skin skin, Runnable callback) {
        list.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tryAndRecover(list, skin, callback);
            }
        });
    }

    public static void onChanged(Widget list, Skin skin, Runnable callback) {
        list.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tryAndRecover(list, skin, callback);
            }
        });
    }

    public static void onClick(Button button, Runnable callback) {
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                tryAndRecover(button, callback);
            }
        });
    }

    public static void onClick(Button button, Runnable callback, Consumer<Event> generalEventCallback) {
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                tryAndRecover(button, callback);
            }

            public boolean handle(Event event) {
                tryAndRecover(button, () -> generalEventCallback.accept(event));
                return super.handle(event);
            }
        });
    }

    public static void tryAndRecover(Button actor, Runnable callback) {
        tryAndRecover(actor, actor.getSkin(), callback);
    }

    public static void tryAndRecover(Actor actor, Skin skin, Runnable callback) {
        try {
            callback.run();
        } catch (Exception e) {
            logger.error("Failed during button callback", e);
            ErrorDialog errorDialog = new ErrorDialog(MESSAGE, skin);
            errorDialog.show(actor.getStage());
        }
    }
}
