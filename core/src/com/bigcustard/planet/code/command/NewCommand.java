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
    private final Runnable cancel;

    public NewCommand(FutureSupplier<Language> gameLanguageSupplier, Consumer<Language> runIDE, Runnable cancel) {
        this.gameLanguageSupplier = gameLanguageSupplier;
        this.runIDE = runIDE;
        this.cancel = cancel;
    }

    @Override
    public void execute() {
       FutureSuppliers.onGet(gameLanguageSupplier, (language) -> {
           if (language != null) {
               runIDE.accept(language);
           } else {
               cancel.run();
           }
       });
    }
}
