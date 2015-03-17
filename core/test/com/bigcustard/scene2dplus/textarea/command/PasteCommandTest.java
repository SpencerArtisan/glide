package com.bigcustard.scene2dplus.textarea.command;

import com.badlogic.gdx.utils.Clipboard;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class PasteCommandTest {
    private TextAreaModel model;
    private PasteCommand command;
    private Clipboard clipboard;

    @Before
    public void before() {
        clipboard = new TestClipboard();
        clipboard.setContents("pasted");
        model = new TextAreaModel("", null);
        command = spy(new PasteCommand(model));
        doReturn(clipboard).when(command).getClipboard();
    }

    @Test
    public void canExecuteWhenClipboardPopulated() {
        clipboard.setContents("clipboard");
        assertThat(command.canExecute()).isTrue();
    }

    @Test
    public void cannotExecuteWhenClipboardNull() {
        clipboard.setContents(null);
        assertThat(command.canExecute()).isFalse();
    }

    @Test
    public void cannotExecuteWhenClipboardEmpty() {
        clipboard.setContents("");
        assertThat(command.canExecute()).isFalse();
    }

    @Test
    public void executeWhenSelection() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<Integer>(3, 0), new XY<Integer>(2, 1));
        command.execute();
        assertThat(model.getText()).isEqualTo("helpastedere");
    }

    @Test
    public void executeWhenNoSelection() {
        model.setText("hello\nthere");
        command.execute();
        assertThat(model.getText()).isEqualTo("pastedhello\nthere");
    }

    @Test
    public void executeWhenNoSelectionCaretOnLastLine() {
        model.setText("hello\nthere");
        model.caret().moveDown();
        command.execute();
        assertThat(model.getText()).isEqualTo("hello\npastedthere");
    }

    @Test
    public void executeWhenNoSelectionCaretOnEmpty() {
        command.execute();
        assertThat(model.getText()).isEqualTo("pasted");
    }
}
