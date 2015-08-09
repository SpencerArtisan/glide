package com.bigcustard.scene2dplus.sound.command;

import com.bigcustard.scene2dplus.sound.SoundAreaModel;
import com.bigcustard.scene2dplus.sound.SoundModel;
import com.bigcustard.scene2dplus.sound.command.RemoveSoundCommand;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveSoundCommandTest {
    @Mock private SoundAreaModel model;
    @Mock private SoundModel sound;
    private RemoveSoundCommand command;

    @Before
    public void before() {
        initMocks(this);
        command = new RemoveSoundCommand(model, sound);
    }

    @Test
    public void removesAnSound() {
        command.execute();
        verify(model).removeSound(sound);
    }

    @Test
    public void undoReaddsTheSound() {
        command.execute();
        command.undo();
        verify(model).removeSound(sound);
    }

}
