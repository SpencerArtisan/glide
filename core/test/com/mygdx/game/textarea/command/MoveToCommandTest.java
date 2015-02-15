package com.mygdx.game.textarea.command;

import com.mygdx.game.XY;
import com.mygdx.game.textarea.TextAreaModel;
import com.mygdx.game.textarea.XYAssert;
import org.junit.Before;
import org.junit.Test;

public class MoveToCommandTest {
    private TextAreaModel model;
    private MoveToCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("hello\nthere", null);
        model.caret().setLocation(3, 0);
        command = new MoveToCommand(model, new XY<Integer>(2, 1));
    }

    @Test
    public void execute() {
        command.execute();
        XYAssert.assertThat(model.caret()).at(2, 1);
    }

    @Test
    public void undo() {
        command.execute();
        command.undo();
        XYAssert.assertThat(model.caret()).at(3, 0);
    }
}
