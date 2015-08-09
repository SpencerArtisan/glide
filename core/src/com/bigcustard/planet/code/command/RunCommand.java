package com.bigcustard.planet.code.command;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GameStore;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.scene2dplus.image.ImageModel;
import com.bigcustard.scene2dplus.image.ImageUtils;
import com.bigcustard.scene2dplus.sound.SoundModel;

import java.util.List;
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
        compileImages();
        compileSounds();
        game.runtimeError(null);
        runGame.accept(game);
    }

    private void compileImages() {
        game.imageModel().images().forEach(this::resize);
    }

    private void compileSounds() {
        List<SoundModel> sounds = game.soundModel().sounds();
        for (SoundModel sound : sounds) {
            sound.file().copyTo(buildFolder.child(sound.filename()));
        }
    }

    @Override
    public boolean canExecute() {
        return game.isValid();
    }

    protected void resize(ImageModel imageModel) {
        FileHandle target = buildFolder.child(imageModel.name());
        ImageUtils.resize(imageModel.file(), target, imageModel.width(), imageModel.height());
    }
}
