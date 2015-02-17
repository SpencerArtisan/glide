package com.mygdx.game.textarea.command;

import com.mygdx.game.XY;
import com.mygdx.game.textarea.TextAreaModel;
import com.mygdx.game.textarea.XYAssert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeCommandTest {
    private TextAreaModel model;
    private TypeCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("", null);
    }

    @Test
    public void executeSingleCharacter() {
        command = new TypeCommand(model, "a");
        command.execute();
        assertThat(model.getText()).isEqualTo("a");
        XYAssert.assertThat(model.caret()).at(1, 0);
    }

    @Test
    public void executeMultipleCharacters() {
        command = new TypeCommand(model, "abcd");
        command.execute();
        assertThat(model.getText()).isEqualTo("abcd");
        XYAssert.assertThat(model.caret()).at(4, 0);
    }

    @Test
    public void executeWhenAreaSelected() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<Integer>(3, 0), new XY<Integer>(2, 1));
        command = new TypeCommand(model, "abcd");
        command.execute();
        assertThat(model.getText()).isEqualTo("helabcdere");
        XYAssert.assertThat(model.caret()).at(7, 0);
        assertThat(model.caret().isAreaSelected()).isFalse();
    }

    @Test
    public void undoSingleCharacter() {
        command = new TypeCommand(model, "a");
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("");
        XYAssert.assertThat(model.caret()).at(0, 0);
    }

    @Test
    public void undoMultipleCharacters() {
        command = new TypeCommand(model, "abcd");
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("");
        XYAssert.assertThat(model.caret()).at(0, 0);
    }

    @Test
    public void undoWhenAreaSelected() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<Integer>(3, 0), new XY<Integer>(2, 1));
        command = new TypeCommand(model, "abcd");
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("hello\nthere");
        assertThat(model.caret().isAreaSelected()).isTrue();
        XYAssert.assertThat(model.caret().selection().getLeft()).at(3, 0);
        XYAssert.assertThat(model.caret().selection().getRight()).at(2, 1);
    }
}
