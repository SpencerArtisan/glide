package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.XY;
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

public class ImageGroupTest {
    @Mock private FileHandle mockImageFolder;
    @Mock private FileHandle mockManifestFile;
    @Mock private FileHandle mockImageFile;
    @Mock private FileHandle mockImageFile2;
    @Mock private ImageModel mockImage;
    @Mock private ImageModel mockImage2;
    @Mock private ValidationResult mockValidationResult1;
    @Mock private ValidationResult mockValidationResult2;
    @Mock private Consumer<ImageGroup> mockChangeListener;
    @Captor private ArgumentCaptor<Runnable> imageChangeListenerCaptor;

    @Before
    public void before() {
        initMocks(this);
        when(mockImageFolder.child("image.png")).thenReturn(mockImageFile);
        when(mockImageFolder.child("image2.png")).thenReturn(mockImageFile2);
        when(mockImageFolder.child("images.json")).thenReturn(mockManifestFile);
        when(mockImageFile.name()).thenReturn("image.png");
        when(mockImageFile2.name()).thenReturn("image2.png");
        when(mockImage.validate()).thenReturn(mockValidationResult1);
        when(mockImage2.validate()).thenReturn(mockValidationResult2);
        doNothing().when(mockImage).watch(imageChangeListenerCaptor.capture());
    }

    @Test
    public void sendChangeEventWhenImageSendsChangeEvent() {
        ImageGroup model = newModel();
        model.images(ImmutableList.of(mockImage));
        model.watch(mockChangeListener);
        imageChangeListenerCaptor.getValue().run();
        verify(mockChangeListener).accept(model);
    }

    @Test
    public void saveStoresImageDetails() {
        ImageGroup model = newModel();
        when(mockImage.filename()).thenReturn("image.png");
        when(mockImage.name()).thenReturn(new WatchableValue<>("image"));
        when(mockImage.width()).thenReturn(new WatchableValue<>(100));
        when(mockImage.height()).thenReturn(new WatchableValue<>(50));
        model.images(ImmutableList.of(mockImage));
        model.save();
        verify(mockManifestFile).writeString("{images:[{filename:image.png,name:image,width:100,height:50}]}", false);
    }

    @Test
    public void deleteRemovesImageButDoesNotDeleteItFromDisk() {
        ImageGroup model = newModel();
        model.images(ImmutableList.of(mockImage));
        model.images(ImmutableList.of());
        assertThat(model.images()).isEmpty();
        verify(mockImageFile, never()).delete();
    }

    @Test
    public void fromFolder() {
        when(mockManifestFile.readString()).thenReturn("{images:[{filename:image.png,name:image,width:100,height:50}]}");
        ImageGroup model = existingModel();
        assertThat(model.images()).extracting("name").containsExactly(new WatchableValue<>("image"));
    }

    @Test
    public void fromFolderWithMissingManifest() {
        when(mockManifestFile.exists()).thenReturn(false);
        when(mockImageFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[]{mockImageFile});
        ImageGroup model = new ImageGroup(mockImageFolder) {
            @Override
            protected XY imageSize(FileHandle imageFile) {
                return new XY(100, 200);
            }
        };
        assertThat(model.images()).extracting("name").containsExactly(new WatchableValue<>("image.png"));
        assertThat(model.images()).extracting("width").containsExactly(new WatchableValue<>(100));
        assertThat(model.images()).extracting("height").containsExactly(new WatchableValue<>(200));
    }

    private ImageGroup newModel() {
        when(mockManifestFile.exists()).thenReturn(false);
        when(mockImageFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[0]);
        return new ImageGroup(mockImageFolder);
    }

    private ImageGroup existingModel() {
        when(mockManifestFile.exists()).thenReturn(true);
        return new ImageGroup(mockImageFolder);
    }
}
