package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ImageAreaControllerTest {
    @Mock private ImageGrabber grabber;
    @Mock private TextButton importTextButton;
    @Mock private ImageArea view;
    @Mock private ImageAreaModel model;
    private ImageAreaController subject;

    @Before
    public void before() {;
        MockitoAnnotations.initMocks(this);
        when(view.importTextButton()).thenReturn(importTextButton);
        subject = new ImageAreaController(grabber, view, model);
    }

    @Test
    public void it_DownloadsAndAddsImageWhenUrlProvided() throws IOException {
        FileHandle imageFile = new FileHandle("file");
        when(grabber.grab("url")).thenReturn(imageFile);
        subject.onImageUrlProvided("url");
        verify(model).add(imageFile);
    }
}
