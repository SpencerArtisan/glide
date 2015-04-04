package com.bigcustard.planet.screen;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.bigcustard.planet.code.Game;
import com.google.common.util.concurrent.SettableFuture;

public class GameLibraryDialog extends Dialog {
    private static int COLUMNS = 3;
    private SettableFuture<FileHandle> futureGame = SettableFuture.create();

    public GameLibraryDialog(Skin skin) {
        super("", skin);
        layoutControls(skin);
    }

    public SettableFuture<FileHandle> getFutureGame() {
        return futureGame;
    }

    @Override
    protected void result(Object object) {
        futureGame.set((FileHandle) object);
    }

    private void layoutControls(Skin skin) {
        getContentTable().clearChildren();
        getButtonTable().clearChildren();
        pad(20);
        text("Choose a game").padBottom(15);
        row();
        int i = 0;
        for (FileHandle gameFolder : Game.allGameFolders()) {
            TextButton button = new TextButton("  " + gameFolder.name() + "  ", skin);
            getButtonTable().add(button).fillX().padLeft(10).padRight(6).padTop(6);
            setObject(button, gameFolder);
            getButtonTable().add(createDeleteButton(gameFolder, skin)).padTop(2);
            if (++i%3 == 0) getButtonTable().row();
        }
        getButtonTable().row();

        TextButton cancelButton = new TextButton("  Cancel  ", skin);
        setObject(cancelButton, null);
        getButtonTable().add(cancelButton).padTop(20).colspan(COLUMNS * 2);
    }

    private Button createDeleteButton(FileHandle gameFolder, Skin skin) {
        ImageButton button = new ImageButton(skin, "trash-button");
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameFolder.deleteDirectory();
                layoutControls(skin);
            }
        });
        return button;
    }

}
