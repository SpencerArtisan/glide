package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TabCommandTest {
    private TextAreaModel model;
    private TabCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("", null);
        command = new TabCommand(model);
    }

    @Test
    public void executeInColumnZeroOnEmptyLine() {
        command.execute();
        assertThat(model.getText()).isEqualTo("    ");
    }

    @Test
    public void executeInColumnTwoOnEmptyLine() {
        model.setText("ab");
        model.caret().setLocation(2, 0);
        command.execute();
        assertThat(model.getText()).isEqualTo("ab  ");
    }

    @Test
    public void executeInColumnZeroOnNonEmptyLine() {
        model.setText("abcd");
        command.execute();
        assertThat(model.getText()).isEqualTo("    abcd");
    }

    @Test
    public void executeInColumnTwoOnNonEmptyLine() {
        model.setText("abcd");
        model.caret().setLocation(2, 0);
        command.execute();
        assertThat(model.getText()).isEqualTo("ab  cd");
    }

    @Test
    public void undoInColumnZeroOnEmptyLine() {
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("");
    }

    @Test
    public void undoInColumnTwoOnEmptyLine() {
        model.setText("ab");
        model.caret().setLocation(2, 0);
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("ab");
    }

    @Test
    public void undoInColumnZeroOnNonEmptyLine() {
        model.setText("abcd");
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("abcd");
    }

    @Test
    public void undoInColumnTwoOnNonEmptyLine() {
        model.setText("abcd");
        model.caret().setLocation(2, 0);
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("abcd");
    }
}
