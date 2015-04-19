package com.bigcustard.planet.code.command;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.Syntax;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.scene2dplus.image.ImageModel;
import com.bigcustard.scene2dplus.image.ImageUtils;

import java.util.function.Consumer;

public class RunCommand extends AbstractCommand {
    private final FileHandle buildFolder;
    private final Game game;
    private final Consumer<Game> runGame;
    private final Syntax syntax;

    public RunCommand(Game game, Consumer<Game> runGame, Syntax syntax) {
        this.game = game;
        this.runGame = runGame;
        this.syntax = syntax;
        buildFolder = game.folder().child("build");
    }

    @Override
    public void execute() {
        buildFolder.mkdirs();
        game.imageModel().images().forEach(this::resize);
        runGame.accept(game);
    }

    @Override
    public boolean canExecute() {
        return game.isValid(syntax);
    }

    protected void resize(ImageModel imageModel) {
        FileHandle target = buildFolder.child(imageModel.name());
        ImageUtils.resize(imageModel.file(), target, imageModel.width(), imageModel.height());
    }
}
