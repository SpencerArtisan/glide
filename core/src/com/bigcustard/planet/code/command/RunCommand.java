package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.Syntax;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.util.FutureSupplier;
import com.bigcustard.util.FutureSuppliers;

import java.util.function.Consumer;

public class RunCommand extends AbstractCommand {
    private Game game;
    private Consumer<Game> runGame;
    private Syntax syntax;

    public RunCommand(Game game, Consumer<Game> runGame, Syntax syntax) {
        this.game = game;
        this.runGame = runGame;
        this.syntax = syntax;
    }

    @Override
    public void execute() {
        runGame.accept(game);
    }

    @Override
    public boolean canExecute() {
        return game.isValid(syntax);
    }
}
