package com.bigcustard.glide.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.glide.code.Game;
import com.bigcustard.scene2dplus.Spacer;
import com.bigcustard.scene2dplus.button.ImageTextButtonPlus;
import com.bigcustard.scene2dplus.button.TextButtonPlus;
import com.bigcustard.scene2dplus.dialog.ErrorDialog;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseLibraryDialog extends Dialog implements Disposable {
    private Map<Integer, List<Game.Token>> games = new HashMap<>();
    private SettableFuture<Game.Token> futureGame = SettableFuture.create();

    public BaseLibraryDialog(Skin skin) {
        super("", skin);
    }

    public void display(Stage stage, Runnable onCancel, Consumer<Game.Token> handleChoice) {
        layoutControls();

        show(stage);
        Futures.addCallback(getFutureGame(), new FutureCallback<Game.Token>() {
            public void onSuccess(Game.Token game) {
                if (game != null) {
                    remove();
                    dispose();
                    handleChoice.accept(game);
                } else {
                    onCancel.run();
                }
            }

            public void onFailure(Throwable e) {
                new ErrorDialog(e, onCancel, getSkin()).show(stage);
            }
        });
    }

    public SettableFuture<Game.Token> getFutureGame() {
        return futureGame;
    }

    @Override
    protected void result(Object object) {
        futureGame.set((Game.Token) object);
    }

    protected void layoutControls() {
        getContentTable().clearChildren();
        getButtonTable().clearChildren();
        pad(20);
        text("Choose a game").padBottom(25);
        row();
        layoutGameButtons();
        getButtonTable().row();

        TextButton cancelButton = new TextButtonPlus("  Close  ", getSkin());
        setObject(cancelButton, null);
        getButtonTable().add(cancelButton).padTop(20).colspan(6);
    }

    abstract protected void layoutGameButtons();

    protected ImageTextButton createButton(Game.Token game) {
        String buttonStyle = game.language().buttonStyle();
        ImageTextButton button = new ImageTextButtonPlus(game.name() + " ", getSkin(), buttonStyle);
        button.clearChildren();
        button.add(new Spacer(4));
        button.add(button.getImage());
        button.add(new Spacer(2));
        button.add(button.getLabel());
        return button;
    }

    @Override
    public void dispose() {
        getButtonTable().getChildren().forEach(Actor::clearListeners);
    }
}
