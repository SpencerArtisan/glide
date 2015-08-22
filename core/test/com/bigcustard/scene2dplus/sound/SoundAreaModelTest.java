package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.util.WatchableValue;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.FilenameFilter;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SoundAreaModelTest {
    @Mock private FileHandle mockSoundFolder;
    @Mock private FileHandle mockManifestFile;
    @Mock private FileHandle mockSoundFile;
    @Mock private FileHandle mockSoundFile2;
    @Mock private SoundModel mockSound;
    @Mock private SoundModel mockSound2;
    @Mock private Consumer<SoundAreaModel> mockChangeListener;
    @Captor private ArgumentCaptor<Runnable> soundChangeListenerCaptor;

    @Before
    public void before() {
        initMocks(this);
        when(mockSoundFolder.child("sound.wav")).thenReturn(mockSoundFile);
        when(mockSoundFolder.child("sound2.wav")).thenReturn(mockSoundFile2);
        when(mockSoundFolder.child("sounds.json")).thenReturn(mockManifestFile);
        when(mockSoundFile.name()).thenReturn("sound.wav");
        when(mockSoundFile2.name()).thenReturn("sound2.wav");
        doNothing().when(mockSound).watch(soundChangeListenerCaptor.capture());
    }

    @Test
    public void sendChangeEventWhenSoundSendsChangeEvent() {
        SoundAreaModel model = newModel();
        model.sounds(ImmutableList.of(mockSound));
        model.watch(mockChangeListener);
        soundChangeListenerCaptor.getValue().run();
        verify(mockChangeListener).accept(model);
    }

    @Test
    public void saveStoresSoundDetails() {
        SoundAreaModel model = newModel();
        when(mockSound.filename()).thenReturn("sound.wav");
        when(mockSound.name()).thenReturn(new WatchableValue<>("sound"));
        model.sounds(ImmutableList.of(mockSound));
        model.save();
        verify(mockManifestFile).writeString("{sounds:[{filename:sound.wav,name:sound}]}", false);
    }

    @Test
    public void deleteRemovesSoundButDoesNotDeleteItFromDisk() {
        SoundAreaModel model = newModel();
        model.sounds(ImmutableList.of(mockSound));
        model.sounds(ImmutableList.of());
        assertThat(model.sounds()).isEmpty();
        verify(mockSoundFile, never()).delete();
    }

    @Test
    public void fromFolder() {
        when(mockManifestFile.readString()).thenReturn("{sounds:[{filename:sound.wav,name:sound}]}");
        SoundAreaModel model = existingModel();
        assertThat(model.sounds()).extracting("name").containsExactly(new WatchableValue<>("sound"));
    }

    @Test
    public void fromFolderWithMissingManifest() {
        when(mockManifestFile.exists()).thenReturn(false);
        when(mockSoundFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[]{mockSoundFile});
        SoundAreaModel model = new SoundAreaModel(mockSoundFolder);
        assertThat(model.sounds()).extracting("name").containsExactly(new WatchableValue<>("sound.wav"));
    }

    private SoundAreaModel newModel() {
        when(mockManifestFile.exists()).thenReturn(false);
        when(mockSoundFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[0]);
        return new SoundAreaModel(mockSoundFolder);
    }

    private SoundAreaModel existingModel() {
        when(mockManifestFile.exists()).thenReturn(true);
        return new SoundAreaModel(mockSoundFolder);
    }
}
