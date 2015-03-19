package com.bigcustard.planet.code.command;

import com.bigcustard.planet.code.CodeRunner;
import com.bigcustard.planet.code.Game;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RunCommandTest {
    private TextAreaModel model;
    private RunCommand command;
    @Mock private Game game;

    @Before
    public void before() {
        initMocks(this);
        model = new TextAreaModel("code", null);
    }

    @Test
    public void execute() {
        command = new RunCommand(model, game);
        command.execute();
        verify(game).run();
    }

    @Test
    public void cannotExecuteWhenGameInvalid() {
        when(game.isValid()).thenReturn(false);
        command = new RunCommand(model, game);
        assertThat(command.canExecute()).isFalse();
    }

    @Test
    public void canExecuteWhenGameValid() {
        when(game.isValid()).thenReturn(true);
        command = new RunCommand(model, game);
        assertThat(command.canExecute()).isTrue();
    }

    @Test
    public void forcesNamingUnnamedGames() {
    }
}