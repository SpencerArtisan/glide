package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.command.AbstractTextAreaCommand;
import com.bigcustard.util.FutureSupplier;
import com.bigcustard.util.FutureSuppliers;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.function.Supplier;

public class NameCommand extends AbstractCommand {
    private Game game;
    private FutureSupplier<String> gameNameSupplier;

    public NameCommand(Game game, FutureSupplier<String> gameNameSupplier) {
        this.game = game;
        this.gameNameSupplier = gameNameSupplier;
    }

    @Override
    public void execute() {
        FutureSuppliers.onGet(gameNameSupplier, (gameName) -> {
            game.setName(gameName);
            game.save();
        });
    }
}
