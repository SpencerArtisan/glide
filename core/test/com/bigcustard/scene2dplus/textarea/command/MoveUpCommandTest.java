package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.XYAssert;
import org.junit.Before;
import org.junit.Test;

public class MoveUpCommandTest {
    private TextAreaModel model;
    private MoveUpCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("", null);
        model.caret().moveDown();
        command = new MoveUpCommand(model);
    }

    @Test
    public void execute() {
        command.execute();
        XYAssert.assertThat(model.caret()).at(0, 0);
    }

    @Test
    public void undo() {
        command.execute();
        command.undo();
        XYAssert.assertThat(model.caret()).at(0, 1);
    }
}
