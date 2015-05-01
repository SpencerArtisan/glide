package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.GameRenameException;
import com.bigcustard.planet.code.Language;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.util.FutureSupplier;
import com.bigcustard.util.FutureSuppliers;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NewCommand extends AbstractCommand {
    private final FutureSupplier<Language> gameLanguageSupplier;
    private final Consumer<Language> runIDE;

    public NewCommand(FutureSupplier<Language> gameLanguageSupplier, Consumer<Language> runIDE) {
        this.gameLanguageSupplier = gameLanguageSupplier;
        this.runIDE = runIDE;
    }

    @Override
    public void execute() {
//        if (game.isNamed()) {
//            exitProcess.run();
//        } else {
//            nameOrDeleteGame();
//        }
    }
//
//    private void nameOrDeleteGame() {
//        FutureSuppliers.onGet(saveChoiceSupplier, (save) -> {
//            if (save) {
//                saveGame();
//            } else {
//                game.delete();
//                exitProcess.run();
//            }
//        });
//    }
//
//    private void saveGame() {
//        FutureSuppliers.onGet(gameNameSupplier, (newName) -> {
//            try {
//                game.setName(newName);
//                exitProcess.run();
//            } catch (GameRenameException e) {
//                errorReporter.accept(e, this::saveGame);
//            }
//        });
//    }
}
