package com.bigcustard.scene2dplus.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bigcustard.scene2dplus.dialog.ErrorDialog;
import com.bigcustard.scene2dplus.dialog.PleaseWaitDialog;
import com.bigcustard.scene2dplus.textfield.TextFieldPlus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ErrorHandler {
    private static String MESSAGE = "Oops.  Something has gone wrong.\r\n\r\nWhatever you did, don't do it again!\r\n\r\nThe guy who wrote this program has been\r\nautomatically electrocuted and ordered\r\nto fix the problem.";
    private static Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    public static void onType(TextFieldPlus field, Consumer<TextField> callback) {
        field.setTextFieldListener((text, ignored) ->
                tryAndRecover(field.getStage(), field.getSkin(), () -> callback.accept(text)));
    }

    public static void onClick(Widget widget, Skin skin, Runnable callback) {
        widget.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getTapCount() == 1) tryAndRecover(widget.getStage(), skin, callback);
            }
        });
    }

    public static void onDoubleClick(Widget widget, Skin skin, Runnable callback) {
        widget.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getTapCount() == 2) tryAndRecover(widget.getStage(), skin, callback);
            }
        });
    }

    public static void onChanged(Actor list, Skin skin, Runnable callback) {
        list.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tryAndRecover(list.getStage(), skin, callback);
            }
        });
    }

    public static void onClick(Button button, Runnable callback, boolean slowOp) {
        button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                tryAndRecover(button, callback);
            }
        });
    }

    public static void onRightClick(Button button, Runnable callback, boolean slowOp) {
        button.addListener(new ClickListener(Input.Buttons.RIGHT) {
            public void clicked(InputEvent event, float x, float y) {
                tryAndRecover(button, callback);
            }
        });
    }

    public static void onClick(Button button, Runnable callback, Consumer<Event> generalEventCallback, boolean slowOp) {
        button.addListener(new ChangeListener() {
            public boolean handle(Event event) {
                tryAndRecover(button, () -> generalEventCallback.accept(event));
                return super.handle(event);
            }

            public void changed(ChangeEvent event, Actor actor) {
                if (slowOp)
                    doWithPleaseWait(button, callback);
                else
                    tryAndRecover(button, callback);
            }

            public void doWithPleaseWait(Button actor, Runnable callback) {
                final PleaseWaitDialog pleaseWaitDialog = new PleaseWaitDialog(actor.getSkin());
                pleaseWaitDialog.show(actor.getStage());

                Executors.newSingleThreadScheduledExecutor().schedule(() ->
                                Gdx.app.postRunnable(() -> {
                                    tryAndRecover(actor, callback);
                                    pleaseWaitDialog.hide();
                                    Gdx.graphics.requestRendering();
                                }),
                        10,
                        TimeUnit.MILLISECONDS);

            }
        });
    }

    public static void tryAndRecover(Button actor, Runnable callback) {
        tryAndRecover(actor.getStage(), actor.getSkin(), callback);
    }

    public static void tryAndRecover(Stage stage, Skin skin, Runnable callback) {
        try {
            callback.run();
        } catch (Exception e) {
            logger.error("Failed during button callback", e);
            if (stage != null) {
                ErrorDialog errorDialog = new ErrorDialog(MESSAGE, skin);
                errorDialog.show(stage);
            }
        }
    }

    public static void tryAndRecover(Runnable callback) {
        tryAndRecover(null, null, callback);
    }
}
