package com.mygdx.game.textarea.command;

import com.mygdx.game.textarea.TextAreaModel;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeCommandTest {
    private TextAreaModel model;
    private TypeCommand command;

    @Before
    public void before() {
        model = new TextAreaModel("", null);
        command = new TypeCommand(model, 'a');
    }

    @Test
    public void execute() {
        command.execute();
        assertThat(model.getText()).isEqualTo("a");
    }

    @Test
    public void undo() {
        command.execute();
        command.undo();
        assertThat(model.getText()).isEqualTo("");
    }
}
