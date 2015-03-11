package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.textarea.command.TestClipboard;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ImageAreaControllerTest {
    @Mock private ImageGrabber grabber;
    @Mock private ImageArea view;
    @Mock private ImageAreaModel model;
    private ImageAreaController subject;
    private TestClipboard clipboard = new TestClipboard();
    private TestTextButton importButton;

    @Before
    public void before() {
        initMocks(this);
        importButton = TestTextButton.mocking(view.importButton());
        subject = spy(new ImageAreaController(grabber, view, model));
        doReturn(clipboard).when(subject).getClipboard();
        subject.init();
    }

    @Test
    public void it_DownloadsAndAddsImageWhenUrlProvided() throws IOException {
        clipboard.setContents("url");
        FileHandle imageFile = new FileHandle("file");
        when(grabber.grab("url")).thenReturn(imageFile);
        importButton.fireChanged();
        verify(model).add(imageFile);
    }

    public static class TestTextButton {
        @Mock private TextButton mockButton;
        private ArgumentCaptor<ChangeListener> captor;

        public static TestTextButton mocking(TextButton buttonToMock) {
            TestTextButton button = new TestTextButton();
            button.connectTo(buttonToMock);
            return button;
        }

        public void fireChanged() {
            verify(mockButton).addListener(captor.capture());
            captor.getValue().changed(null, mockButton);
        }

        private TestTextButton() {
            initMocks(this);
            captor = ArgumentCaptor.forClass(ChangeListener.class);
        }

        private void connectTo(TextButton buttonToMock) {
            when(buttonToMock).thenReturn(mockButton);
        }
    }
}
