package com.bigcustard.planet.screen;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.bigcustard.planet.code.Game;
import com.bigcustard.scene2dplus.Spacer;
import com.google.common.util.concurrent.SettableFuture;

public class GameLibraryDialog extends Dialog {
    private static int COLUMNS = 2;
    private SettableFuture<FileHandle> futureGame = SettableFuture.create();

    public static GameLibraryDialog userGames(Skin skin) {
        GameLibraryDialog dialog = new GameLibraryDialog(skin);
        dialog.layoutControls(skin, Game.allUserGameFolders(), true);
        return dialog;
    }

    public static GameLibraryDialog sampleGames(Skin skin) {
        GameLibraryDialog dialog = new GameLibraryDialog(skin);
        dialog.layoutControls(skin, Game.allSampleGameFolders(), false);
        return dialog;
    }

    private GameLibraryDialog(Skin skin) {
        super("", skin);
    }

    public SettableFuture<FileHandle> getFutureGame() {
        return futureGame;
    }

    @Override
    protected void result(Object object) {
        futureGame.set((FileHandle) object);
    }

    private void layoutControls(Skin skin, FileHandle[] gameFolders, boolean allowDelete) {
        getContentTable().clearChildren();
        getButtonTable().clearChildren();
        pad(20);
        text("Choose a game").padBottom(25);
        row();
        int i = 0;
        for (FileHandle gameFolder : gameFolders) {
            ImageTextButton button = createButton(skin, gameFolder);
            getButtonTable().add(button).fillX().spaceLeft(10).spaceRight(10).padLeft(10).padRight(6).padTop(6);
            setObject(button, gameFolder);
            if (allowDelete) getButtonTable().add(createDeleteButton(gameFolder, skin)).padTop(2);
            if (++i%COLUMNS == 0) getButtonTable().row();
        }
        getButtonTable().row();

        TextButton cancelButton = new TextButton("  Cancel  ", skin);
        setObject(cancelButton, null);
        getButtonTable().add(cancelButton).padTop(20).colspan(COLUMNS * 2);
    }

    private ImageTextButton createButton(Skin skin, FileHandle gameFolder) {
        String buttonStyle = Game.language(gameFolder).buttonStyle();
        ImageTextButton button = new ImageTextButton(gameFolder.name() + "  ", skin, buttonStyle);
        button.clearChildren();
        button.add(new Spacer(8));
        button.add(button.getImage());
        button.add(new Spacer(4));
        button.add(button.getLabel());
        return button;
    }

    private Button createDeleteButton(FileHandle gameFolder, Skin skin) {
        ImageButton button = new ImageButton(skin, "trash-button");
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameFolder.deleteDirectory();
                layoutControls(skin, Game.allUserGameFolders(), true);
            }
        });
        return button;
    }
}
