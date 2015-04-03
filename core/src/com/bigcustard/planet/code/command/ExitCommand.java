package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GameRenameException;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.util.FutureSupplier;
import com.bigcustard.util.FutureSuppliers;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ExitCommand extends AbstractCommand {
    private final Game game;
    private final FutureSupplier<Boolean> saveChoiceSupplier;
    private final FutureSupplier<String> gameNameSupplier;
    private BiConsumer<Exception, Runnable> errorReporter;
    private final Runnable exitProcess;

    public ExitCommand(Game game,
                       FutureSupplier<Boolean> saveChoiceSupplier,
                       FutureSupplier<String> gameNameSupplier,
                       BiConsumer<Exception, Runnable> errorReporter,
                       Runnable exitProcess) {
        this.game = game;
        this.saveChoiceSupplier = saveChoiceSupplier;
        this.gameNameSupplier = gameNameSupplier;
        this.errorReporter = errorReporter;
        this.exitProcess = exitProcess;
    }

    @Override
    public void execute() {
        if (game.isNamed()) {
            exitProcess.run();
        } else {
            nameOrDeleteGame();
        }
    }

    private void nameOrDeleteGame() {
        FutureSuppliers.onGet(saveChoiceSupplier, (save) -> {
            if (save) {
                saveGame();
            } else {
                game.delete();
                exitProcess.run();
            }
        });
    }

    private void saveGame() {
        FutureSuppliers.onGet(gameNameSupplier, (newName) -> {
            try {
                game.setName(newName);
                exitProcess.run();
            } catch (GameRenameException e) {
                errorReporter.accept(e, this::saveGame);
            }
        });
    }
}
