package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ImageAreaModelTest {
    @Mock private FileHandle mockImageFolder;
    @Mock private FileHandle mockManifestFile;
    @Mock private FileHandle mockImageFile;
    @Mock private FileHandle mockImageFile2;
    @Mock private ImageModel mockImage;
    @Mock private ImageModel mockImage2;
    @Mock private ValidationResult mockValidationResult1;
    @Mock private ValidationResult mockValidationResult2;
    @Mock private Consumer<ImageModel> mockValidationListener;
    @Mock private Consumer<ImageModel> mockChangeListener;
    @Captor private ArgumentCaptor<Consumer<ImageModel>> imageValidationListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<ImageModel>> imageChangeListenerCaptor;

    @Before
    public void before() {
        initMocks(this);
        when(mockImageFolder.child("image.png")).thenReturn(mockImageFile);
        when(mockImageFolder.child("image2.png")).thenReturn(mockImageFile2);
        when(mockImageFolder.child("manifest.json")).thenReturn(mockManifestFile);
        when(mockImageFile.name()).thenReturn("image.png");
        when(mockImageFile2.name()).thenReturn("image2.png");
        when(mockImage.validate()).thenReturn(mockValidationResult1);
        when(mockImage2.validate()).thenReturn(mockValidationResult2);
        doNothing().when(mockImage).registerValidationListener(imageValidationListenerCaptor.capture());
        doNothing().when(mockImage).registerChangeListener(imageChangeListenerCaptor.capture());
    }

    @Test
    public void sendValidationEventWhenAddInvalidImage() {
        ImageAreaModel model = newModel();
        model.registerValidationListener(mockValidationListener);
        when(mockValidationResult1.isValid()).thenReturn(false);
        model.addImage(mockImage);
        verify(mockValidationListener).accept(mockImage);
    }

    @Test
    public void sendValidationEventWhenImageSendsValidationEvent() {
        ImageAreaModel model = newModel();
        model.registerValidationListener(mockValidationListener);
        when(mockValidationResult1.isValid()).thenReturn(true);
        model.addImage(mockImage);
        imageValidationListenerCaptor.getValue().accept(mockImage);
        verify(mockValidationListener).accept(mockImage);
    }

    @Test
    public void sendChangeEventWhenImageSendsChangeEvent() {
        ImageAreaModel model = newModel();
        model.registerChangeImageListener(mockChangeListener);
        model.addImage(mockImage);
        imageChangeListenerCaptor.getValue().accept(mockImage);
        verify(mockChangeListener).accept(mockImage);
    }

    @Test
    public void doesNotSendSecondValidationEventWhenAddSecondInvalidImage() {
        ImageAreaModel model = newModel();
        model.registerValidationListener(mockValidationListener);
        when(mockValidationResult1.isValid()).thenReturn(false);
        when(mockValidationResult2.isValid()).thenReturn(false);
        model.addImage(mockImage);
        model.addImage(mockImage2);
        verify(mockValidationListener, times(1)).accept(mockImage);
    }

    @Test
    public void doesNotSendValidationEventWhenAddValidImage() {
        ImageAreaModel model = newModel();
        model.registerValidationListener(mockValidationListener);
        when(mockValidationResult1.isValid()).thenReturn(true);
        model.addImage(mockImage);
        verify(mockValidationListener, never()).accept(any(ImageModel.class));
    }

    @Test
    public void returnsValidationResultsForAllImages() {
        ImageAreaModel model = newModel();
        model.addImage(mockImage);
        model.addImage(mockImage2);
        assertThat(model.validate()).containsExactly(mockValidationResult2, mockValidationResult1);
    }

    @Test
    public void saveStoresImageDetails() {
        ImageAreaModel model = newModel();
        when(mockImage.filename()).thenReturn("image.png");
        when(mockImage.name()).thenReturn("image");
        when(mockImage.width()).thenReturn(100);
        when(mockImage.height()).thenReturn(50);
        model.addImage(mockImage);
        model.save();
        verify(mockManifestFile).writeString("{images:[{filename:image.png,name:image,width:100,height:50}]}", false);
    }

    @Test
    public void deleteRemovesImageButDoesNotDeleteItFromDisk() {
        ImageAreaModel model = newModel();
        model.addImage(mockImage);
        model.removeImage(mockImage);
        assertThat(model.images()).isEmpty();
        verify(mockImageFile, never()).delete();
    }

    @Test
    public void fromFolder() {
        when(mockManifestFile.readString()).thenReturn("{images:[{filename:image.png,name:image,width:100,height:50}]}");
        ImageAreaModel model = existingModel();
        assertThat(model.images()).extracting("name").containsExactly("image");
    }

    private ImageAreaModel newModel() {
        when(mockManifestFile.exists()).thenReturn(false);
        ImageAreaModel model = new ImageAreaModel();
        model.loadFromFolder(mockImageFolder);
        return model;
    }

    private ImageAreaModel existingModel() {
        when(mockManifestFile.exists()).thenReturn(true);
        ImageAreaModel model = new ImageAreaModel();
        model.loadFromFolder(mockImageFolder);
        return model;
    }
}
