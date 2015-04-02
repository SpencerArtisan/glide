package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.util.FutureSupplier;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Supplier;

import static org.mockito.Mockito.verify;

public class NameCommandTest {
    @Mock private Game game;
    @Mock private TextAreaModel model;
    private NameCommand command;
    private SettableFuture<String> futureName;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        futureName = SettableFuture.create();
        FutureSupplier<String> nameSupplier = () -> futureName;
        command = new NameCommand(model, game, nameSupplier);
    }

    @Test
    public void execute() {
        command.execute();
        futureName.set("name");
        verify(game).setName("name");
        verify(game).save();
    }
}
