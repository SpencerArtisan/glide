package com.bigcustard.planet.screen;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.bigcustard.planet.code.Game;
import com.google.common.util.concurrent.SettableFuture;

public class GameLibraryDialog extends Dialog {
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
        text("Choose a game");
        row();
        for (FileHandle gameDirectory : Game.allGameFolders()) {
            TextButton button = new TextButton("  " + gameDirectory.name() + "  ", skin, "big");
            getButtonTable().add(button).fillX().pad(10);
            setObject(button, gameDirectory);
            getButtonTable().row();
        }
    }
}
