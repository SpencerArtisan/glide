package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.sound.SoundAreaModel;
import com.bigcustard.scene2dplus.sound.SoundModel;
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
    @Mock private Consumer<SoundModel> mockValidationListener;
    @Mock private Consumer<SoundModel> mockChangeListener;
    @Captor private ArgumentCaptor<Consumer<SoundModel>> soundValidationListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<SoundModel>> soundChangeListenerCaptor;

    @Before
    public void before() {
        initMocks(this);
        when(mockSoundFolder.child("sound.png")).thenReturn(mockSoundFile);
        when(mockSoundFolder.child("sound2.png")).thenReturn(mockSoundFile2);
        when(mockSoundFolder.child("manifest.json")).thenReturn(mockManifestFile);
        when(mockSoundFile.name()).thenReturn("sound.png");
        when(mockSoundFile2.name()).thenReturn("sound2.png");
        doNothing().when(mockSound).registerChangeListener(soundChangeListenerCaptor.capture());
    }

    @Test
    public void sendValidationEventWhenAddInvalidSound() {
        SoundAreaModel model = newModel();
        model.registerValidationListener(mockValidationListener);
        model.addSound(mockSound);
        verify(mockValidationListener).accept(mockSound);
    }

    @Test
    public void sendValidationEventWhenSoundSendsValidationEvent() {
        SoundAreaModel model = newModel();
        model.registerValidationListener(mockValidationListener);
        model.addSound(mockSound);
        soundValidationListenerCaptor.getValue().accept(mockSound);
        verify(mockValidationListener).accept(mockSound);
    }

    @Test
    public void sendChangeEventWhenSoundSendsChangeEvent() {
        SoundAreaModel model = newModel();
        model.registerChangeSoundListener(mockChangeListener);
        model.addSound(mockSound);
        soundChangeListenerCaptor.getValue().accept(mockSound);
        verify(mockChangeListener).accept(mockSound);
    }

    @Test
    public void doesNotSendSecondValidationEventWhenAddSecondInvalidSound() {
        SoundAreaModel model = newModel();
        model.registerValidationListener(mockValidationListener);
        model.addSound(mockSound);
        model.addSound(mockSound2);
        verify(mockValidationListener, times(1)).accept(mockSound);
    }

    @Test
    public void doesNotSendValidationEventWhenAddValidSound() {
        SoundAreaModel model = newModel();
        model.registerValidationListener(mockValidationListener);
        model.addSound(mockSound);
        verify(mockValidationListener, never()).accept(any(SoundModel.class));
    }

    @Test
    public void returnsValidationResultsForAllSounds() {
        SoundAreaModel model = newModel();
        model.addSound(mockSound);
        model.addSound(mockSound2);
    }

    @Test
    public void saveStoresSoundDetails() {
        SoundAreaModel model = newModel();
        when(mockSound.filename()).thenReturn("sound.png");
        when(mockSound.name()).thenReturn("sound");
        model.addSound(mockSound);
        model.save();
        verify(mockManifestFile).writeString("{sounds:[{filename:sound.png,name:sound,width:100,height:50}]}", false);
    }

    @Test
    public void deleteRemovesSoundButDoesNotDeleteItFromDisk() {
        SoundAreaModel model = newModel();
        model.addSound(mockSound);
        model.removeSound(mockSound);
        assertThat(model.sounds()).isEmpty();
        verify(mockSoundFile, never()).delete();
    }

    @Test
    public void fromFolder() {
        when(mockManifestFile.readString()).thenReturn("{sounds:[{filename:sound.png,name:sound,width:100,height:50}]}");
        SoundAreaModel model = existingModel();
        assertThat(model.sounds()).extracting("name").containsExactly("sound");
    }

    @Test
    public void fromFolderWithMissingManifest() {
        when(mockManifestFile.exists()).thenReturn(false);
        when(mockSoundFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[]{mockSoundFile});
        SoundAreaModel model = new SoundAreaModel(mockSoundFolder);
        assertThat(model.sounds()).extracting("name").containsExactly("sound.png");
        assertThat(model.sounds()).extracting("width").containsExactly(100);
        assertThat(model.sounds()).extracting("height").containsExactly(200);
    }

    private SoundAreaModel newModel() {
        when(mockManifestFile.exists()).thenReturn(false);
        when(mockSoundFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[0]);
        SoundAreaModel model = new SoundAreaModel(mockSoundFolder);
        return model;
    }

    private SoundAreaModel existingModel() {
        when(mockManifestFile.exists()).thenReturn(true);
        SoundAreaModel model = new SoundAreaModel(mockSoundFolder);
        return model;
    }
}
