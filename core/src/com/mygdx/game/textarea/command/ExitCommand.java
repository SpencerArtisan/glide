package com.mygdx.game.textarea.command;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.mygdx.game.code.Game;
import com.mygdx.game.textarea.TextAreaModel;

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
