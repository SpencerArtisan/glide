package com.bigcustard.planet.screen;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
        pad(20);
        text("Choose a game").padBottom(20);
        row();
        int i = 0;
        for (FileHandle gameDirectory : Game.allGameFolders()) {
            TextButton button = new TextButton("  " + gameDirectory.name() + "  ", skin);
            getButtonTable().add(button).fillX().padLeft(10).padRight(6).padTop(6);
            setObject(button, gameDirectory);
            getButtonTable().add(createDeleteButton(skin));
            if (++i%3 == 0) getButtonTable().row();
        }
    }

    private static Button createDeleteButton(Skin skin) {
        return new ImageButton(skin, "trash-button");
    }

}
