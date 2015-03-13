package com.mygdx.game.textarea.command;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.mygdx.game.code.Program;
import com.mygdx.game.textarea.TextAreaModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Supplier;

import static org.mockito.Mockito.verify;

public class SaveCommandTest {
    @Mock private Program program;
    @Mock private TextAreaModel model;
    private SaveCommand command;
    private SettableFuture<String> futureName;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        futureName = SettableFuture.create();
        Supplier<ListenableFuture<String>> nameSupplier = () -> futureName;
        command = new SaveCommand(model, program, nameSupplier);
    }

    @Test
    public void execute() {
        command.execute();
        futureName.set("name");
        verify(program).setName("name");
        verify(program).save(model);
    }
}
