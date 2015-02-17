package com.mygdx.game.textarea.command;

import com.mygdx.game.XY;
import com.mygdx.game.textarea.TextAreaModel;
import com.mygdx.game.textarea.XYAssert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReturnCommandTest {
    private TextAreaModel model;
    private ReturnCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("", null);
        model.caret().setLocation(0, 0);
        command = new ReturnCommand(model);
    }

    @Test
    public void execute() {
        command.execute();
        assertThat(model.getText()).isEqualTo("\n");
        XYAssert.assertThat(model.caret()).at(0, 1);
    }

    @Test
    public void executeWhenAreaSelected() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<Integer>(3, 0), new XY<Integer>(2, 1));
        command.execute();
        assertThat(model.getText()).isEqualTo("hel\nere");
        assertThat(model.caret().isAreaSelected()).isFalse();
        XYAssert.assertThat(model.caret()).at(0, 1);
    }

    @Test
    public void undo() {
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("");
        XYAssert.assertThat(model.caret()).at(0, 0);
    }

    @Test
    public void undoWhenAreaSelected() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<Integer>(3, 0), new XY<Integer>(2, 1));
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("hello\nthere");
        assertThat(model.caret().isAreaSelected()).isTrue();
        XYAssert.assertThat(model.caret().selection().getLeft()).at(3, 0);
        XYAssert.assertThat(model.caret().selection().getRight()).at(2, 1);
    }
}
