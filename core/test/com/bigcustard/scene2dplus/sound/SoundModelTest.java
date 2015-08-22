package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.files.FileHandle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SoundModelTest {
    @Mock private FileHandle mockSoundFile;
    @Mock private Audio mockAudio;

    private SoundModel subject;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void soundName() {
        when(mockSoundFile.name()).thenReturn("file.png");
        when(mockSoundFile.extension()).thenReturn("png");
        createModel();
        assertThat(subject.name()).isEqualTo("file.png");
    }

    @Test
    public void longSoundName() {
        when(mockSoundFile.name()).thenReturn("a_long_sound_file_name.png");
        when(mockSoundFile.extension()).thenReturn("png");
        createModel();
        assertThat(subject.name()).isEqualTo("a_long_sound_.png");
    }

    @Test
    public void soundNameWithDots() {
        when(mockSoundFile.name()).thenReturn("sound.file.png");
        when(mockSoundFile.extension()).thenReturn("png");
        createModel();
        assertThat(subject.name()).isEqualTo("sound.file.png");
    }

    @Test
    public void soundNameWithSpaces() {
        when(mockSoundFile.name()).thenReturn("sound file.png");
        when(mockSoundFile.extension()).thenReturn("png");
        createModel();
        assertThat(subject.name()).isEqualTo("sound file.png");
    }

    private void createModel() {
        subject = new SoundModel(mockSoundFile) {
            @Override
            protected Audio audio() {
                return mockAudio;
            }
        };
    }
}
