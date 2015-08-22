package com.bigcustard.glide.code.command;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.glide.code.Game;
import com.bigcustard.glide.code.GameStore;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.scene2dplus.image.ImageModel;
import com.bigcustard.scene2dplus.image.ImageUtils;
import com.bigcustard.scene2dplus.sound.SoundModel;

import java.util.function.Consumer;

public class RunCommand extends AbstractCommand {
    private final FileHandle buildFolder;
    private final Game game;
    private final Consumer<Game> runGame;

    public RunCommand(Game game, GameStore gameStore, Consumer<Game> runGame) {
        this.game = game;
        this.runGame = runGame;
        this.buildFolder = gameStore.buildFolder(game);
    }

    @Override
    public void execute() {
        buildFolder.mkdirs();
        resizeImages();
        copySounds();
        game.runtimeError(null);
        runGame.accept(game);
    }

    @Override
    public boolean canExecute() {
        return game.isValid();
    }

    private void resizeImages() {
        game.imageGroup().images().forEach(this::resize);
    }

    protected void resize(ImageModel image) {
        FileHandle target = buildFolder.child(image.name().get());
        ImageUtils.resize(image.file(), target, image.width().get(), image.height().get());
    }

    private void copySounds() {
        game.soundGroup().sounds().forEach(this::copy);
    }

    private void copy(SoundModel sound) {
        sound.file().copyTo(buildFolder.child(sound.name().get()));
    }
}
