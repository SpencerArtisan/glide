package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.XYAssert;
import org.junit.Before;
import org.junit.Test;

public class MoveRightCommandTest {
    private TextAreaModel model;
    private MoveRightCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("text", null);
        command = new MoveRightCommand(model);
    }

    @Test
    public void execute() {
        command.execute();
        XYAssert.assertThat(model.caret()).at(1, 0);
    }

    @Test
    public void undo() {
        command.execute();
        command.undo();
        XYAssert.assertThat(model.caret()).at(0, 0);
    }
}
