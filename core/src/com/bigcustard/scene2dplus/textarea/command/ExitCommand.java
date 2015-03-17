package com.bigcustard.scene2dplus.textarea.command;

import com.google.common.util.concurrent.ListenableFuture;
import com.bigcustard.planet.code.Game;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;

import java.util.function.Supplier;

public class ExitCommand extends AbstractCommand {
    private Runnable exitListener;

    public ExitCommand(TextAreaModel model, Game game, Supplier<ListenableFuture<String>> gameNameSupplier, Runnable exitListener) {
        super(model);
        this.exitListener = exitListener;
    }

    @Override
    public void execute() {
        exitListener.run();
    }
}
