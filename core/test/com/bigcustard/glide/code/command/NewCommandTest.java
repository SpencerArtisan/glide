package com.bigcustard.glide.code.command;

import com.bigcustard.glide.code.language.Language;
import com.google.common.util.concurrent.Futures;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class NewCommandTest {
    private NewCommand command;
    @Mock private Consumer<Language> runIDE;
    @Mock private Runnable cancel;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void executeWithLanguageChoice() {
        command = spy(new NewCommand(() -> Futures.immediateFuture(Language.Groovy), runIDE, cancel));
        command.execute();
        verify(runIDE).accept(Language.Groovy);
    }

    @Test
    public void executeWithCancel() {
        command = spy(new NewCommand(() -> Futures.immediateFuture(null), runIDE, cancel));
        command.execute();
        verifyZeroInteractions(runIDE);
    }
}