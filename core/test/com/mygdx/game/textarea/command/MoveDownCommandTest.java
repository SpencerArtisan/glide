package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;
import com.mygdx.game.textarea.XYAssert;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MoveDownCommandTest {
    private TextAreaModel model;
    private MoveDownCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("", null);
        command = new MoveDownCommand(model);
    }

    @Test
    public void execute() {
        command.execute();
        XYAssert.assertThat(model.caret()).at(0, 1);
    }

    @Test
    public void undo() {
        command.execute();
        command.undo();
        XYAssert.assertThat(model.caret()).at(0, 0);
    }
}
