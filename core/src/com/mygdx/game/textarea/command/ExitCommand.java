package com.mygdx.game.textarea.command;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.mygdx.game.code.Game;
import com.mygdx.game.textarea.TextAreaModel;

import java.util.function.Supplier;

public class ExitCommand extends AbstractCommand {
    private Game game;
    private Supplier<ListenableFuture<String>> gameNameSupplier;

    public ExitCommand(TextAreaModel model, Game game, Supplier<ListenableFuture<String>> gameNameSupplier) {
        super(model);
        this.game = game;
        this.gameNameSupplier = gameNameSupplier;
    }

    @Override
    public void execute() {
        ListenableFuture<String> futureGameName = gameNameSupplier.get();
        Futures.addCallback(futureGameName, new FutureCallback<String>() {
            @Override
            public void onSuccess(String gameName) {
                game.setName(gameName);
                game.save(model);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Error saving file " + t);
            }
        });
    }
}
