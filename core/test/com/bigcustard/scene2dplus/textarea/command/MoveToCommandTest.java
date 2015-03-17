package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.XYAssert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoveToCommandTest {
    private TextAreaModel model;
    private MoveToCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("", null);
    }

    @Test
    public void execute() {
        model.setText("hello\nthere");
        command = new MoveToCommand(model, new XY<Integer>(2, 1));
        command.execute();
        XYAssert.assertThat(model.caret()).at(2, 1);
    }

    @Test
    public void executeBeyondFileLength() {
        command = new MoveToCommand(model, new XY<Integer>(0, 2));
        command.execute();
        XYAssert.assertThat(model.caret()).at(0, 2);
        assertThat(model.getText()).isEqualTo("\n\n");
    }

    @Test
    public void executeWhenSelection() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<Integer>(3, 0), new XY<Integer>(2, 1));
        command = new MoveToCommand(model, new XY<Integer>(0, 0));
        command.execute();
        assertThat(model.caret().isAreaSelected()).isFalse();
        XYAssert.assertThat(model.caret()).at(0, 0);
    }

    @Test
    public void undo() {
        model.setText("hello\nthere");
        command = new MoveToCommand(model, new XY<Integer>(2, 1));
        command.execute();
        command.undo();
        XYAssert.assertThat(model.caret()).at(0, 0);
    }
}
