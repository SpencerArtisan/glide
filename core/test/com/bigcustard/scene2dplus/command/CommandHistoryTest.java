package com.bigcustard.scene2dplus.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CommandHistoryTest {
    private CommandHistory history = new CommandHistory();
    @Mock private Command command;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void execute() {
        history.execute(command);
        verify(command).execute();
    }

    @Test
    public void cannotUndoNothing() {
        assertThat(history.canUndo()).isFalse();
    }

    @Test
    public void canUndoSingleCommand() {
        history.execute(command);
        assertThat(history.canUndo()).isTrue();
    }

    @Test
    public void cannotUndoWhenUndoneAllCommands() {
        history.execute(command);
        history.undo();
        assertThat(history.canUndo()).isFalse();
    }

    @Test
    public void cannotRedoNothing() {
        assertThat(history.canRedo()).isFalse();
    }

    @Test
    public void cannotRedoIfNotUndone() {
        history.execute(command);
        assertThat(history.canRedo()).isFalse();
    }

    @Test
    public void canRedoIfUndone() {
        history.execute(command);
        history.undo();
        assertThat(history.canRedo()).isTrue();
    }

    @Test
    public void cannotRedoIfRedoneAll() {
        history.execute(command);
        history.undo();
        history.redo();
        assertThat(history.canRedo()).isFalse();
    }

    @Test
    public void cannotRedoOnceNewCommandAdded() {
        history.execute(command);
        history.undo();
        history.execute(command);
        assertThat(history.canRedo()).isFalse();
    }

    @Test
    public void cannotRedoOnceNewCommandAddedLongHistory() {
        history.execute(command);
        history.execute(command);
        history.execute(command);
        history.undo();
        history.undo();
        history.execute(command);
        assertThat(history.canRedo()).isFalse();
    }
}
