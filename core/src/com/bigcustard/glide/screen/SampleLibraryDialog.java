package com.bigcustard.glide.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.glide.code.Game;
import com.bigcustard.glide.code.GameStore;
import com.bigcustard.scene2dplus.Spacer;
import com.bigcustard.scene2dplus.button.ImageButtonPlus;
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
import java.util.stream.Stream;

public class SampleLibraryDialog extends GameLibraryDialog implements Disposable {
    private Map<Integer, List<Game.Token>> games = new HashMap<>();
    private SettableFuture<Game.Token> futureGame = SettableFuture.create();
    private boolean readOnly;

    public SampleLibraryDialog(Skin skin) {
        super(skin);
        GameStore gameStore = new GameStore();
        games.put(0, gameStore.allSimpleSampleGames());
        games.put(1, gameStore.allMediumSampleGames());
        games.put(2, gameStore.allHardSampleGames());
        layoutControls(skin, true);
    }

    public void display(Stage stage, Runnable onCancel, Consumer<Game.Token> handleChoice) {
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
        Game.Token selected = (Game.Token) object;
        if (object != null && readOnly) {
            GameStore gameStore = new GameStore();
            selected = gameStore.rename(selected, gameStore.findUniqueName().name());
        }
        futureGame.set(selected);
    }

    private void layoutControls(Skin skin, boolean readOnly) {
        this.readOnly = readOnly;
        getContentTable().clearChildren();
        getButtonTable().clearChildren();
        pad(20);
        text("Choose a game").padBottom(25);
        row();
        layoutGameButtons(skin, readOnly);
        getButtonTable().row();

        TextButton cancelButton = new TextButtonPlus("  Cancel  ", skin);
        setObject(cancelButton, null);
        getButtonTable().add(cancelButton).padTop(20).colspan(6);
    }

    private void layoutGameButtons(Skin skin, boolean readOnly) {
        Stream.of(1, 2, 3).forEach(level ->
                getButtonTable().add(new Image(skin, "level" + level)).padTop(10)
        );
        getButtonTable().row();
        Stream.of(1, 2, 3).forEach(level -> {
                    Label small = new Label("Level " + level + " Hacker", skin, "small");
                    small.setColor(Color.YELLOW);
                    small.setAlignment(Align.center);
                    getButtonTable().add(small).center().fillX().padTop(-12).padBottom(6);
                }
        );
        getButtonTable().row();


        for (int row = 0; ; row++) {
            boolean emptyRow = true;
            for (int level = 0; level < 3; level++) {
                if (games.get(level).size() > row) {
                    Game.Token game = games.get(level).get(row);
                    emptyRow = false;
                    ImageTextButton button = createButton(skin, game);
                    getButtonTable().add(button).fillX().spaceLeft(10).spaceRight(15).padLeft(15).padRight(6).padTop(6);
                    setObject(button, game);
                }
            }
            getButtonTable().row();
            if (emptyRow) return;
        }
    }

    private ImageTextButton createButton(Skin skin, Game.Token game) {
        String buttonStyle = game.language().buttonStyle();
        ImageTextButton button = new ImageTextButtonPlus(game.name() + " ", skin, buttonStyle);
        button.clearChildren();
        button.add(new Spacer(4));
        button.add(button.getImage());
        button.add(new Spacer(2));
        button.add(button.getLabel());
        return button;
    }

    private Button createDeleteButton(Skin skin, Game.Token game) {
        ImageButtonPlus button = new ImageButtonPlus(skin, "trash-button");
        button.onClick(() -> deleteGame(skin, game));
        return button;
    }

    private void deleteGame(Skin skin, Game.Token game) {
        new GameStore().delete(game);
        games.remove(game);
        layoutControls(skin, readOnly);
    }

    @Override
    public void dispose() {
        getButtonTable().getChildren().forEach(Actor::clearListeners);
    }
}