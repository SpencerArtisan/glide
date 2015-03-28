package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ImageAreaModelTest {
    @Mock private FileHandle mockImageFolder;
    @Mock private FileHandle mockManifestFile;
    @Mock private FileHandle mockImageFile;
    @Mock private FileHandle mockImageFile2;
    @Mock private InputStream mockImageStream;
    @Mock private ImagePlus mockImage;
    @Mock private ImagePlus mockImage2;
    @Mock private ImageValidator mockValidator;

    @Before
    public void before() {
        initMocks(this);
        when(mockImageFolder.child("image.png")).thenReturn(mockImageFile);
        when(mockImageFolder.child("image2.png")).thenReturn(mockImageFile2);
        when(mockImageFolder.child("manifest.json")).thenReturn(mockManifestFile);
        when(mockImageFile.name()).thenReturn("image.png");
        when(mockImageFile2.name()).thenReturn("image2.png");
    }

//    @Test
//    public void isInvalidIfImagesBad() {
//        when(mockValidator.isValid(Arrays.asList(any(ImagePlus.class)))).thenReturn(false);
//        when(mockFile.name()).thenReturn("image.png");
//        Game model = newGame();
//        model.addImage("http://url/image.png");
//        model.setCode("code");
//        assertThat(model.isValid()).isFalse();
//    }

    @Test
    public void addImageFromUrl() {
        ImageAreaModel model = newModel();
        model.addImage("http://url/image.png");
        verify(mockImageFile).write(mockImageStream, false);
        assertThat(model.images()).extracting("name").containsExactly("image");
    }

    @Test
    public void addImageFromUrlDuplicateName() {
        when(mockImageFile.exists()).thenReturn(true);
        ImageAreaModel model = newModel();
        model.addImage("http://url/image.png");
        verify(mockImageFile2).write(mockImageStream, false);
        assertThat(model.images()).extracting("name").containsExactly("image2");
    }

    @Test
    public void saveStoresImageDetails() {
        ImageAreaModel model = newModel();
        model.addImage(mockImage);
        when(mockImage.filename()).thenReturn("image.png");
        when(mockImage.name()).thenReturn("image");
        when(mockImage.width()).thenReturn(100);
        when(mockImage.height()).thenReturn(50);
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
        return new ImageAreaModel((name) -> mockImageStream, mockValidator).fromFolder(mockImageFolder);
    }

    private ImageAreaModel existingModel() {
        when(mockManifestFile.exists()).thenReturn(true);
        return new ImageAreaModel((name) -> mockImageStream, mockValidator).fromFolder(mockImageFolder);
    }
}
