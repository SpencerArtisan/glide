package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.Syntax;
import com.bigcustard.util.FutureSupplier;
import com.google.common.util.concurrent.SettableFuture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RunCommandTest {
    private RunCommand command;
    @Mock private Syntax syntax;
    @Mock private Game game;
    @Mock private Consumer<Game> runGame;

    @Before
    public void before() {
        initMocks(this);
        command = new RunCommand(game, runGame, syntax);
    }

    @Test
    public void cannotExecuteWhenGameInvalid() {
        when(game.isValid(syntax)).thenReturn(false);
        assertThat(command.canExecute()).isFalse();
    }

    @Test
    public void canExecuteWhenGameValid() {
        when(game.isValid(syntax)).thenReturn(true);
        assertThat(command.canExecute()).isTrue();
    }

    @Test
    public void execute() {
        command.execute();
        verify(runGame).accept(game);
    }
}