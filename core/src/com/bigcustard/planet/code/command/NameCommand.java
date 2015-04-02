package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.command.AbstractTextAreaCommand;
import com.bigcustard.util.FutureSupplier;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.function.Supplier;

public class NameCommand extends AbstractTextAreaCommand {
    private Game game;
    private FutureSupplier<String> gameNameSupplier;

    public NameCommand(TextAreaModel model, Game game, FutureSupplier<String> gameNameSupplier) {
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
                game.save();
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Error saving file " + t);
            }
        });
    }
}
