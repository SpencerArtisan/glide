package com.bigcustard.scene2dplus.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UndoCommandTest {
    @Mock private CommandHistory history;
    private UndoCommand command;

    @Before
    public void before() {
        initMocks(this);
        command = new UndoCommand(history);
    }

    @Test
    public void executeUndoesHistory() {
        command.execute();
        verify(history).undo();
    }

    @Test
    public void undoRedoesHistory() {
        command.undo();
        verify(history).redo();
    }

    @Test
    public void canExecuteIfHasHistory() {
        when(history.canUndo()).thenReturn(true);
        assertThat(command.canExecute()).isTrue();
    }

    @Test
    public void cannotExecuteIfHasNoHistory() {
        when(history.canUndo()).thenReturn(false);
        assertThat(command.canExecute()).isFalse();
    }
}
