package com.bigcustard.scene2dplus.textarea.command;

import com.badlogic.gdx.utils.Clipboard;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class CopyCommandTest {
    private TextAreaModel model;
    private Clipboard clipboard;
    private CopyCommand command;

    @Before
    public void before() {
        clipboard = new TestClipboard();
        model = new TextAreaModel("", null);
        command = spy(new CopyCommand(model));
        doReturn(clipboard).when(command).getClipboard();
    }

    @Test
    public void canExecuteWhenSelection() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<>(3, 0), new XY<>(2, 1));
        assertThat(command.canExecute()).isTrue();
    }

    @Test
    public void cannotExecuteWhenNoSelection() {
        assertThat(command.canExecute()).isFalse();
    }

    @Test
    public void executeWhenSelection() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<>(3, 0), new XY<>(2, 1));
        command.execute();
        assertThat(clipboard.getContents()).isEqualTo("lo\nth");
        assertThat(model.text()).isEqualTo("hello\nthere");
    }

    @Test
    public void executeWhenNoSelection() {
        model.setText("hello\nthere");
        command.execute();
        assertThat(clipboard.getContents()).isEqualTo("hello\n");
        assertThat(model.text()).isEqualTo("hello\nthere");
    }

    @Test
    public void executeWhenNoSelectionCaretOnLastLine() {
        model.setText("hello\nthere");
        model.caret().moveDown();
        command.execute();
        assertThat(clipboard.getContents()).isEqualTo("there\n");
        assertThat(model.text()).isEqualTo("hello\nthere");
    }
}
