package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.Syntax;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.util.FutureSupplier;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RunCommandTest {
    private RunCommand command;
    private TextAreaModel model;
    private SettableFuture<String> futureName;
    @Mock private Syntax syntax;
    @Mock private Game game;
    @Mock private Consumer<Game> runGame;

    @Before
    public void before() {
        initMocks(this);
        futureName = SettableFuture.create();
        FutureSupplier<String> nameSupplier = () -> futureName;
        model = new TextAreaModel("code", null);
        command = new RunCommand(model, game, nameSupplier, runGame, syntax);
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
    public void forcesNamingUnnamedGames() {
        when(game.isNamed()).thenReturn(false);
        command.execute();
        futureName.set("name");
        verify(game).setName("name");
        verify(game).save();
        verify(runGame).accept(game);
    }

    @Test
    public void dontHaveToNameNamedGames() {
        when(game.isNamed()).thenReturn(true);
        command.execute();
        verify(game, never()).setName(anyString());
        verify(game).save();
        verify(runGame).accept(game);
    }
}