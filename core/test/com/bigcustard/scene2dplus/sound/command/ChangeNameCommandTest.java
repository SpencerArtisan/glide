package com.bigcustard.scene2dplus.sound.command;

import com.bigcustard.scene2dplus.sound.SoundModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ChangeNameCommandTest {
    @Mock private SoundModel sound;
    private ChangeNameCommand command;

    @Before
    public void before() {
        initMocks(this);
        when(sound.name()).thenReturn("old name");
        command = new ChangeNameCommand(sound, "new name");
    }

    @Test
    public void itChangesTheWidth() {
        command.execute();
        verify(sound).name("new name");
    }

    @Test
    public void undoRestoresTheWidth() {
        command.execute();
        command.undo();
        verify(sound).name("old name");
    }

}
