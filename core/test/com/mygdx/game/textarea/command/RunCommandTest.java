package com.mygdx.game.textarea.command;

import com.mygdx.game.XY;
import com.mygdx.game.code.CodeRunner;
import com.mygdx.game.textarea.TextAreaModel;
import com.mygdx.game.textarea.XYAssert;
import org.codehaus.groovy.ant.Groovy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RunCommandTest {
    private TextAreaModel model;
    private RunCommand command;
    @Mock private CodeRunner codeRunner;

    @Before
    public void before() {
        initMocks(this);
        model = new TextAreaModel("code", null);
    }

    @Test
    public void execute() {
        command = new RunCommand(model, codeRunner);
        command.execute();
        verify(codeRunner).run("code");
    }

    @Test
    public void canExecuteWhenCodeValid() {
        when(codeRunner.isValid("code")).thenReturn(true);
        command = new RunCommand(model, codeRunner);
        assertThat(command.canExecute()).isTrue();
    }

    @Test
    public void cannotExecuteWhenCodeInvalid() {
        when(codeRunner.isValid("code")).thenReturn(false);
        command = new RunCommand(model, codeRunner);
        assertThat(command.canExecute()).isFalse();
    }
}