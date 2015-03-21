package com.bigcustard.planet.code.command;

import com.bigcustard.scene2dplus.textarea.command.AbstractTextAreaCommand;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.bigcustard.planet.code.Game;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;

import java.util.function.Supplier;

public class ExitCommand extends AbstractTextAreaCommand {
    private Game game;
    private Supplier<ListenableFuture<Boolean>> saveChoiceSupplier;
    private Supplier<ListenableFuture<String>> gameNameSupplier;
    private Runnable exitProcess;

    public ExitCommand(TextAreaModel model, Game game, Supplier<ListenableFuture<Boolean>> saveChoiceSupplier, Supplier<ListenableFuture<String>> gameNameSupplier, Runnable exitProcess) {
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
        ListenableFuture<Boolean> futureSaveChoice = saveChoiceSupplier.get();
        Futures.addCallback(futureSaveChoice, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean save) {
                if (save) {
                    nameGame();
                } else {
                    game.delete();
                    exitProcess.run();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Error saving or deleting game " + t);
            }
        });
    }

    private void nameGame() {
        ListenableFuture<String> futureGameName = gameNameSupplier.get();
        Futures.addCallback(futureGameName, new FutureCallback<String>() {
            @Override
            public void onSuccess(String gameName) {
                game.setName(gameName);
                game.save();
                exitProcess.run();
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Error saving game " + t);
            }
        });
    }
}
