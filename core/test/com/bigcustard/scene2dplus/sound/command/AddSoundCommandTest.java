package com.bigcustard.scene2dplus.sound.command;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.sound.SoundAreaModel;
import com.bigcustard.scene2dplus.sound.SoundModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AddSoundCommandTest {
    @Mock private SoundAreaModel model;
    @Mock private FileHandle soundFolder;
    @Mock private FileHandle importSoundFile;
    @Mock private FileHandle soundFile;
    @Mock private FileHandle soundFile2;
    @Mock private InputStream mockSoundStream;
    private AddSoundCommand command;

    @Before
    public void before() {
        initMocks(this);

        when(importSoundFile.path()).thenReturn("/Users/sound.wav");
        when(model.folder()).thenReturn(soundFolder);
        when(soundFolder.child("sound.wav")).thenReturn(soundFile);
        when(soundFile.name()).thenReturn("sound.wav");

        command = new AddSoundCommand(model, importSoundFile) {
            protected InputStream getInputStream() throws IOException {
                return mockSoundStream;
            }
        };
    }

    @Test
    public void addSoundFromUrl() {
        command.execute();
        verify(soundFile).write(mockSoundStream, false);
        verify(model).addSound(any(SoundModel.class));
    }

    @Test
    public void addSoundFromUrlDuplicateName() {
        when(soundFile.exists()).thenReturn(true);
        when(soundFolder.child("sound2.wav")).thenReturn(soundFile2);
        when(soundFile2.name()).thenReturn("sound2.wav");
        command.execute();
        verify(soundFile2).write(mockSoundStream, false);
        verify(model).addSound(any(SoundModel.class));
    }

    @Test
    public void undoRemovesTheSound() {
        command.execute();
        command.undo();
        verify(model).removeSound(any(SoundModel.class));
    }
}
