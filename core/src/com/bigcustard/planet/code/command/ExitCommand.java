package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GameRenameException;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.util.FutureSupplier;
import com.bigcustard.util.FutureSuppliers;

public class ExitCommand extends AbstractCommand {
    private final Game game;
    private final FutureSupplier<Boolean> saveChoiceSupplier;
    private final FutureSupplier<String> gameNameSupplier;
    private final Runnable exitProcess;

    public ExitCommand(Game game,
                       FutureSupplier<Boolean> saveChoiceSupplier,
                       FutureSupplier<String> gameNameSupplier,
                       Runnable exitProcess) {
        this.game = game;
        this.saveChoiceSupplier = saveChoiceSupplier;
        this.gameNameSupplier = gameNameSupplier;
        this.exitProcess = exitProcess;
    }

    @Override
    public void execute() {
        if (game.isNamed()) {
            game.save();
            exitProcess.run();
        } else {
            nameOrDeleteGame();
        }
    }

    private void nameOrDeleteGame() {
        FutureSuppliers.onGet(saveChoiceSupplier, (save) -> {
            if (save) {
                nameGame();
            } else {
                game.delete();
                exitProcess.run();
            }
        });
    }

    private void nameGame() {
        FutureSuppliers.onGet(gameNameSupplier, (gameName) -> {
            try {
                game.setName(gameName);
                game.save();
                exitProcess.run();
            } catch (GameRenameException e) {
                System.out.println("Failed to save game: " + e);
            }
        });
    }
}
