package com.bigcustard.scene2dplus.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RedoCommandTest {
    @Mock private CommandHistory history;
    private RedoCommand command;

    @Before
    public void before() {
        initMocks(this);
        command = new RedoCommand(history);
    }

    @Test
    public void executeRedoesHistory() {
        command.execute();
        verify(history).redo();
    }

    @Test
    public void undoUndoesHistory() {
        command.undo();
        verify(history).undo();
    }

    @Test
    public void canExecuteIfHasFuture() {
        when(history.canRedo()).thenReturn(true);
        assertThat(command.canExecute()).isTrue();
    }

    @Test
    public void cannotExecuteIfHasNoFuture() {
        when(history.canRedo()).thenReturn(false);
        assertThat(command.canExecute()).isFalse();
    }
}
