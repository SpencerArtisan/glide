package com.bigcustard.scene2dplus.image.command;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImageModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.InputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AddImageCommandTest {
    @Mock private ImageAreaModel model;
    @Mock private FileHandle imageFolder;
    @Mock private FileHandle imageFile;
    @Mock private FileHandle imageFile2;
    @Mock private InputStream mockImageStream;
    private AddImageCommand command;

    @Before
    public void before() {
        initMocks(this);
        command = new AddImageCommand(model, "http://url/image.png") {
            @Override
            protected XY<Integer> imageSize(FileHandle mainImageFile) {
                return new XY<>(1, 2);
            }

            @Override
            protected InputStream inputStream(String url) {
                return mockImageStream;
            }
        };
        when(model.folder()).thenReturn(imageFolder);
        when(imageFolder.child("image.png")).thenReturn(imageFile);
        when(imageFile.name()).thenReturn("image.png");
    }

    @Test
    public void addImageFromUrl() {
        command.execute();
        verify(imageFile).write(mockImageStream, false);
        verify(model).addImage(any(ImageModel.class));
    }

    @Test
    public void addImageFromUrlDuplicateName() {
        when(imageFile.exists()).thenReturn(true);
        when(imageFolder.child("image2.png")).thenReturn(imageFile2);
        when(imageFile2.name()).thenReturn("image2.png");
        command.execute();
        verify(imageFile2).write(mockImageStream, false);
        verify(model).addImage(any(ImageModel.class));
    }

    @Test
    public void undoRemovesTheImage() {
        command.execute();
        command.undo();
        verify(model).removeImage(any(ImageModel.class));
    }
}
