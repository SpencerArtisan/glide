package com.bigcustard.planet.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GameStore;
import com.bigcustard.scene2dplus.Spacer;
import com.google.common.util.concurrent.SettableFuture;

public class GameLibraryDialog extends Dialog {
    private static int COLUMNS = 2;
    private SettableFuture<Game> futureGame = SettableFuture.create();
    private boolean readOnly;

    public static GameLibraryDialog userGames(Skin skin) {
        GameLibraryDialog dialog = new GameLibraryDialog(skin);
        dialog.layoutControls(skin, new GameStore().allUserGames(), false);
        return dialog;
    }

    public static GameLibraryDialog sampleGames(Skin skin) {
        GameLibraryDialog dialog = new GameLibraryDialog(skin);
        dialog.layoutControls(skin, new GameStore().allSampleGames(), true);
        return dialog;
    }

    private GameLibraryDialog(Skin skin) {
        super("", skin);
    }

    public SettableFuture<Game> getFutureGame() {
        return futureGame;
    }

    @Override
    protected void result(Object object) {
        Game selected = (Game) object;
        if (readOnly) {
            GameStore gameStore = new GameStore();
            gameStore.rename(selected, gameStore.findUniqueName().name());
        }
        futureGame.set(selected);
    }

    private void layoutControls(Skin skin, java.util.List<Game> gameFolders, boolean readOnly) {
        this.readOnly = readOnly;
        getContentTable().clearChildren();
        getButtonTable().clearChildren();
        pad(20);
        text("Choose a game").padBottom(25);
        row();
        int i = 0;
        for (Game game : gameFolders) {
            ImageTextButton button = createButton(skin, game);
            getButtonTable().add(button).fillX().spaceLeft(10).spaceRight(10).padLeft(10).padRight(6).padTop(6);
            setObject(button, game);
            if (!readOnly) getButtonTable().add(createDeleteButton(skin, game)).padTop(2);
            if (++i%COLUMNS == 0) getButtonTable().row();
        }
        getButtonTable().row();

        TextButton cancelButton = new TextButton("  Cancel  ", skin);
        setObject(cancelButton, null);
        getButtonTable().add(cancelButton).padTop(20).colspan(COLUMNS * 2);
    }

    private ImageTextButton createButton(Skin skin, Game gameFolder) {
        String buttonStyle = gameFolder.language().buttonStyle();
        ImageTextButton button = new ImageTextButton(gameFolder.name() + "  ", skin, buttonStyle);
        button.clearChildren();
        button.add(new Spacer(8));
        button.add(button.getImage());
        button.add(new Spacer(4));
        button.add(button.getLabel());
        return button;
    }

    private Button createDeleteButton(Skin skin, Game game) {
        ImageButton button = new ImageButton(skin, "trash-button");
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                new GameStore().delete(game);;
                layoutControls(skin, new GameStore().allUserGames(), readOnly);
            }
        });
        return button;
    }
}
