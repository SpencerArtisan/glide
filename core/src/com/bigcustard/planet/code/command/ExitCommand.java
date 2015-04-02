package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GameRenameException;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.command.AbstractTextAreaCommand;
import com.bigcustard.util.FutureSupplier;
import com.bigcustard.util.FutureSuppliers;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.function.Supplier;

public class ExitCommand extends AbstractTextAreaCommand {
    private Game game;
    private FutureSupplier<Boolean> saveChoiceSupplier;
    private FutureSupplier<String> gameNameSupplier;
    private Runnable exitProcess;

    public ExitCommand(TextAreaModel model,
                       Game game,
                       FutureSupplier<Boolean> saveChoiceSupplier,
                       FutureSupplier<String> gameNameSupplier,
                       Runnable exitProcess) {
        super(model);
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
        ListenableFuture<String> futureGameName = gameNameSupplier.get();
        Futures.addCallback(futureGameName, new FutureCallback<String>() {
            @Override
            public void onSuccess(String gameName) {
                try {
                    game.setName(gameName);
                    game.save();
                    exitProcess.run();
                } catch (GameRenameException e) {
                    System.out.println("Failed to save game: " + e);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Error saving game " + t);
            }
        });
    }
}
