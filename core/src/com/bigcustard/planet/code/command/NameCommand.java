package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.util.FutureSupplier;
import com.bigcustard.util.FutureSuppliers;

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
