package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GameRenameException;
import com.bigcustard.planet.code.GameStore;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.util.FutureSupplier;
import com.bigcustard.util.FutureSuppliers;

import java.util.function.BiConsumer;

public class ExitCommand extends AbstractCommand {
    private final Game game;
    private final GameStore gameStore;
    private final FutureSupplier<Boolean> saveChoiceSupplier;
    private final FutureSupplier<String> gameNameSupplier;
    private final BiConsumer<Exception, Runnable> errorReporter;
    private final Runnable exitProcess;

    public ExitCommand(Game game,
                       GameStore gameStore,
                       FutureSupplier<Boolean> saveChoiceSupplier,
                       FutureSupplier<String> gameNameSupplier,
                       BiConsumer<Exception, Runnable> errorReporter,
                       Runnable exitProcess) {
        this.game = game;
        this.gameStore = gameStore;
        this.saveChoiceSupplier = saveChoiceSupplier;
        this.gameNameSupplier = gameNameSupplier;
        this.errorReporter = errorReporter;
        this.exitProcess = exitProcess;
    }

    @Override
    public void execute() {
        if (game.isNamed()) {
            exitProcess.run();
        } else if (game.isModified()){
            nameOrDeleteGame();
        } else {
            gameStore.delete(game.token());
            exitProcess.run();
        }
    }

    private void nameOrDeleteGame() {
        FutureSuppliers.onGet(saveChoiceSupplier, (save) -> {
            if (save) {
                saveGame();
            } else {
                gameStore.delete(game.token());
                exitProcess.run();
            }
        });
    }

    private void saveGame() {
        FutureSuppliers.onGet(gameNameSupplier, (newName) -> {
            try {
                gameStore.rename(game.token(), newName);
                exitProcess.run();
            } catch (GameRenameException e) {
                errorReporter.accept(e, this::saveGame);
            }
        });
    }
}
