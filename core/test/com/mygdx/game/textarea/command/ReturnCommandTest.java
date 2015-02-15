package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReturnCommandTest {
    private TextAreaModel model;
    private ReturnCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("", null);
        command = new ReturnCommand(model);
    }

    @Test
    public void execute() {
        command.execute();
        assertThat(model.getText()).isEqualTo("\n");
    }

    @Test
    public void undo() {
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("");
    }
}
