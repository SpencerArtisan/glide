package com.bigcustard.glide.code.command;

import com.bigcustard.glide.code.Game;
import com.bigcustard.glide.code.GameStore;
import com.bigcustard.util.FutureSupplier;
import com.google.common.util.concurrent.Futures;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.function.BiConsumer;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ExitCommandTest {
    private ExitCommand command;
    @Mock private GameStore gameStore;
    @Mock private Game game;
    @Mock private Game.Token token;
    @Mock private Runnable exitProcess;
    @Mock private BiConsumer<Exception, Runnable> mockErrorReporter;
    @Mock private FutureSupplier<String> mockSupplier;
    @Mock private FutureSupplier<Boolean> mockChoiceSupplier;

    @Before
    public void before() {
        initMocks(this);
        when(game.token()).thenReturn(token);
    }

    @Test
    public void executeWithUnnamedUnmodifiedGameDeletes() {
        when(game.isNamed()).thenReturn(false);
        when(game.isModified()).thenReturn(false);
        command = new ExitCommand(game, gameStore, mockChoiceSupplier, mockSupplier, mockErrorReporter, exitProcess);
        command.execute();
        verify(gameStore).delete(token);
    }

    @Test
    public void executeWithUnnamedGameThenSave() {
        FutureSupplier<String> nameSupplier = () -> Futures.immediateFuture("name") ;
        FutureSupplier<Boolean> saveChoiceSupplier = () -> Futures.immediateFuture(true);
        when(game.isNamed()).thenReturn(false);
        when(game.isModified()).thenReturn(true);
        command = new ExitCommand(game, gameStore, saveChoiceSupplier, nameSupplier, mockErrorReporter, exitProcess);
        command.execute();
        verify(gameStore).rename(token, "name");
    }

    @Test
    public void executeWithUnnamedGameThenDelete() {
        FutureSupplier<Boolean> saveChoiceSupplier = () -> Futures.immediateFuture(false);
        when(game.isNamed()).thenReturn(false);
        when(game.isModified()).thenReturn(true);
        command = new ExitCommand(game, gameStore, saveChoiceSupplier, mockSupplier, mockErrorReporter, exitProcess);
        command.execute();
        verifyZeroInteractions(mockSupplier);
        verify(gameStore).delete(token);
    }

    @Test
    public void executeWithNamedGameExitsWithoutAskingQuestions() {
        when(game.isNamed()).thenReturn(true);
        when(game.isModified()).thenReturn(true);
        command = new ExitCommand(game, gameStore, null, mockSupplier, mockErrorReporter, exitProcess);
        command.execute();
        verifyZeroInteractions(mockSupplier);
    }
}