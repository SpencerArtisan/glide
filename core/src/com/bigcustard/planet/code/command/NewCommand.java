package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.language.Language;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.util.FutureSupplier;
import com.bigcustard.util.FutureSuppliers;

import java.util.function.Consumer;

public class NewCommand extends AbstractCommand {
    private final FutureSupplier<Language> gameLanguageSupplier;
    private final Consumer<Language> newGame;
    private final Runnable cancel;

    public NewCommand(FutureSupplier<Language> gameLanguageSupplier, Consumer<Language> newGame, Runnable cancel) {
        this.gameLanguageSupplier = gameLanguageSupplier;
        this.newGame = newGame;
        this.cancel = cancel;
    }

    @Override
    public void execute() {
       FutureSuppliers.onGet(gameLanguageSupplier, (language) -> {
           if (language != null) {
               newGame.accept(language);
           } else {
               cancel.run();
           }
       });
    }
}
