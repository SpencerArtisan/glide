package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.XYAssert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReturnCommandTest {
    private TextAreaModel model;
    private ReturnCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("", null);
    }

    @Test
    public void execute() {
        command = new ReturnCommand(model);
        command.execute();
        assertThat(model.getText()).isEqualTo("\n");
        XYAssert.assertThat(model.caret()).at(0, 1);
    }

    @Test
    public void executeWhenAreaSelected() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<Integer>(3, 0), new XY<Integer>(2, 1));
        command = new ReturnCommand(model);
        command.execute();
        assertThat(model.getText()).isEqualTo("hel\nere");
        assertThat(model.caret().isAreaSelected()).isFalse();
        XYAssert.assertThat(model.caret()).at(0, 1);
    }

    @Test
    public void undo() {
        command = new ReturnCommand(model);
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("");
        XYAssert.assertThat(model.caret()).at(0, 0);
    }

    @Test
    public void undoWhenAreaSelected() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<Integer>(3, 0), new XY<Integer>(2, 1));
        command = new ReturnCommand(model);
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("hello\nthere");
        assertThat(model.caret().isAreaSelected()).isTrue();
        XYAssert.assertThat(model.caret().selection().getLeft()).at(3, 0);
        XYAssert.assertThat(model.caret().selection().getRight()).at(2, 1);
    }
}
