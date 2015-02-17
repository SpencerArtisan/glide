package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;
import com.mygdx.game.textarea.XYAssert;
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
