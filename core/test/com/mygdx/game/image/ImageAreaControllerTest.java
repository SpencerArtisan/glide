package com.mygdx.game.image;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Clipboard;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class ImageAreaControllerTest {
    @Mock private ImageGrabber grabber;
    @Mock private TextField importTextField;
    @Mock private ImageArea view;
    @Mock private ImageAreaModel model;
    private ImageAreaController subject;

    @Before
    public void before() {;
        MockitoAnnotations.initMocks(this);
        when(view.importTextField()).thenReturn(importTextField);
        subject = new ImageAreaController(grabber, view, model);
    }

    @Test
    public void it_DownloadsAndAddsImageWhenTextEntered() throws IOException {
        FileHandle imageFile = new FileHandle("file");
        when(grabber.grab("url")).thenReturn(imageFile);
        subject.onImageUrlChanged("url");
        verify(model).add(imageFile, "url");
    }
}
