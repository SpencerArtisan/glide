package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteCommandTest {
    private TextAreaModel model;
    private DeleteCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("text", null);
        model.caret().setLocation(4, 0);
        command = new DeleteCommand(model);
    }

    @Test
    public void execute() {
        command.execute();
        assertThat(model.getText()).isEqualTo("tex");
    }

    @Test
    public void undo() {
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("text");
    }
}
