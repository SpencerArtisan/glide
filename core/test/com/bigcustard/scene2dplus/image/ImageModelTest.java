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

public class ImageModelTest {
    @Mock private FileHandle mockImageFile;

    private ImageModel subject;

    @Before
    public void before() {
        initMocks(this);
    }

    @Test
    public void imageName() {
        when(mockImageFile.name()).thenReturn("file.png");
        subject = new ImageModel(mockImageFile, 100, 50);
        assertThat(subject.name()).isEqualTo("file.png");
    }

    @Test
    public void longImageName() {
        when(mockImageFile.name()).thenReturn("a_long_image_file_name.png");
        subject = new ImageModel(mockImageFile, 100, 50);
        assertThat(subject.name()).isEqualTo("a_long_image.png");
    }

    @Test
    public void imageNameWithDots() {
        when(mockImageFile.name()).thenReturn("image.file.png");
        subject = new ImageModel(mockImageFile, 100, 50);
        assertThat(subject.name()).isEqualTo("image.file.png");
    }

    @Test
    public void imageNameWithSpaces() {
        when(mockImageFile.name()).thenReturn("image file.png");
        subject = new ImageModel(mockImageFile, 100, 50);
        assertThat(subject.name()).isEqualTo("image file.png");
    }
}
