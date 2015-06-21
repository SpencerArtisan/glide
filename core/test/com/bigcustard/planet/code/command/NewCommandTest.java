package com.bigcustard.planet.code.command;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.Language;
import com.bigcustard.planet.language.Syntax;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImageModel;
import com.bigcustard.util.FutureSupplier;
import com.google.common.util.concurrent.Futures;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
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