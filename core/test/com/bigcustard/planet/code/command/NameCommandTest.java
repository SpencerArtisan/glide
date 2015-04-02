package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.util.FutureSupplier;
import com.google.common.util.concurrent.SettableFuture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class NameCommandTest {
    @Mock private Game game;
    private NameCommand command;
    private SettableFuture<String> futureName;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        futureName = SettableFuture.create();
        FutureSupplier<String> nameSupplier = () -> futureName;
        command = new NameCommand(game, nameSupplier);
    }

    @Test
    public void execute() {
        command.execute();
        futureName.set("name");
        verify(game).setName("name");
        verify(game).save();
    }
}
