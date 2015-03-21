package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.command.AbstractTextAreaCommand;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.function.Supplier;

public class RunCommand extends AbstractTextAreaCommand {
    private Game game;
    private Supplier<ListenableFuture<String>> gameNameSupplier;

    public RunCommand(TextAreaModel model, Game game, Supplier<ListenableFuture<String>> gameNameSupplier) {
        super(model);
        this.game = game;
        this.gameNameSupplier = gameNameSupplier;
    }

    @Override
    public void execute() {
        if (game.isNamed()) {
            game.run();
        } else {
            nameGame();
        }
    }

    private void nameGame() {
        ListenableFuture<String> futureGameName = gameNameSupplier.get();
        Futures.addCallback(futureGameName, new FutureCallback<String>() {
            @Override
            public void onSuccess(String gameName) {
                game.setName(gameName);
                game.run();
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Error saving game " + t);
            }
        });
    }


    @Override
    public boolean canExecute() {
        return game.isValid();
    }
}
