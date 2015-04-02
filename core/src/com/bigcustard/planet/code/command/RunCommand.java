package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.Syntax;
import com.bigcustard.planet.plugin.groovy.GroovySyntax;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.command.AbstractTextAreaCommand;
import com.bigcustard.util.FutureSupplier;
import com.bigcustard.util.FutureSuppliers;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RunCommand extends AbstractCommand {
    private Game game;
    private FutureSupplier<String> gameNameSupplier;
    private Consumer<Game> runGame;
    private Syntax syntax;

    public RunCommand(Game game, FutureSupplier<String> gameNameSupplier, Consumer<Game> runGame, Syntax syntax) {
        this.game = game;
        this.gameNameSupplier = gameNameSupplier;
        this.runGame = runGame;
        this.syntax = syntax;
    }

    @Override
    public void execute() {
        if (game.isNamed()) {
            game.save();
            runGame.accept(game);
        } else {
            nameGame();
        }
    }

    private void nameGame() {
        FutureSuppliers.onGet(gameNameSupplier, (gameName) -> {
            game.setName(gameName);
            game.save();
            runGame.accept(game);
        });
    }

    @Override
    public boolean canExecute() {
        return game.isValid(syntax);
    }
}
