package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ExitCommandTest {
    private ExitCommand command;
    @Mock private Game game;
    @Mock private TextAreaModel model;
    @Mock private Runnable exitProcess;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void executeWithUnnamedGameThenSave() {
        Supplier<ListenableFuture<String>> nameSupplier = () -> Futures.immediateFuture("name") ;
        Supplier<ListenableFuture<Boolean>> saveChoiceSupplier = () -> Futures.immediateFuture(true);
        when(game.isNamed()).thenReturn(false);
        command = new ExitCommand(model, game, saveChoiceSupplier, nameSupplier, exitProcess);
        command.execute();
        verify(game).setName("name");
        verify(game).save();
    }

    @Test
    public void executeWithUnnamedGameThenDelete() {
        Supplier<ListenableFuture<String>> mockSupplier = mock(Supplier.class);
        Supplier<ListenableFuture<Boolean>> saveChoiceSupplier = () -> Futures.immediateFuture(false);
        when(game.isNamed()).thenReturn(false);
        command = new ExitCommand(model, game, saveChoiceSupplier, mockSupplier, exitProcess);
        command.execute();
        verifyZeroInteractions(mockSupplier);
        verify(game).delete();
    }

    @Test
    public void executeWithNamedGameSavesAutomatically() {
        Supplier<ListenableFuture<String>> mockSupplier = mock(Supplier.class);
        when(game.isNamed()).thenReturn(true);
        command = new ExitCommand(model, game, null, mockSupplier, exitProcess);
        command.execute();
        verify(game).save();
        verifyZeroInteractions(mockSupplier);
    }
}