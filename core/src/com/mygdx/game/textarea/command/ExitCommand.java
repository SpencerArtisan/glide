package com.mygdx.game.textarea.command;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.mygdx.game.code.Program;
import com.mygdx.game.textarea.TextAreaModel;

import java.util.function.Supplier;

public class ExitCommand extends AbstractCommand {
    private Program program;
    private Supplier<ListenableFuture<String>> gameNameSupplier;

    public ExitCommand(TextAreaModel model, Program program, Supplier<ListenableFuture<String>> gameNameSupplier) {
        super(model);
        this.program = program;
        this.gameNameSupplier = gameNameSupplier;
    }

    @Override
    public void execute() {
        ListenableFuture<String> futureGameName = gameNameSupplier.get();
        Futures.addCallback(futureGameName, new FutureCallback<String>() {
            @Override
            public void onSuccess(String gameName) {
                program.setName(gameName);
                program.save(model);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Error saving file " + t);
            }
        });
    }
}
