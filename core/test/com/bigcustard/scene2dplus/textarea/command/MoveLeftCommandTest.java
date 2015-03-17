package com.bigcustard.scene2dplus.textarea.command;

import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import com.bigcustard.scene2dplus.textarea.XYAssert;
import org.junit.Before;
import org.junit.Test;

public class MoveLeftCommandTest {
    private TextAreaModel model;
    private MoveLeftCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("text", null);
        model.caret().moveRight(4);
        command = new MoveLeftCommand(model);
    }

    @Test
    public void execute() {
        command.execute();
        XYAssert.assertThat(model.caret()).at(3, 0);
    }

    @Test
    public void undo() {
        command.execute();
        command.undo();
        XYAssert.assertThat(model.caret()).at(4, 0);
    }
}
