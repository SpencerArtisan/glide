package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
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
        assertThat(subject.name().get()).isEqualTo("file");
    }

    @Test
    public void longImageName() {
        when(mockImageFile.name()).thenReturn("a_long_image_file_name.png");
        subject = new ImageModel(mockImageFile, 100, 50);
        assertThat(subject.name().get()).isEqualTo("a_long_image_file");
    }

    @Test
    public void imageNameWithDots() {
        when(mockImageFile.name()).thenReturn("image.file.png");
        subject = new ImageModel(mockImageFile, 100, 50);
        assertThat(subject.name().get()).isEqualTo("image.file");
    }

    @Test
    public void imageNameWithSpaces() {
        when(mockImageFile.name()).thenReturn("image file.png");
        subject = new ImageModel(mockImageFile, 100, 50);
        assertThat(subject.name().get()).isEqualTo("image file");
    }
}
