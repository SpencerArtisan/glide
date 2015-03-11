package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.textarea.command.TestClipboard;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(HierarchicalContextRunner.class)
public class ImageAreaControllerTest {
    @Mock private ImageGrabber grabber;
    @Mock private ImageArea view;
    @Mock private ImageAreaModel model;
    @Mock private GameImage gameImage;
    private ImageAreaController subject;
    private TestClipboard clipboard = new TestClipboard();
    private TestTextButton importButton;
    private TestTextField nameField = new TestTextField();
    private TestTextField widthField = new TestTextField();
    private TestTextField heightField = new TestTextField();

    @Before
    public void before() {
        initMocks(this);
        importButton = TestTextButton.mocking(view.importButton());
        ImageControls imageControls =
                new ImageControls(gameImage, nameField.mock(), widthField.mock(), heightField.mock());
        when(view.getImageControlList()).thenReturn(Arrays.asList(imageControls));
        subject = spy(new ImageAreaController(grabber, view, model));
        doReturn(clipboard).when(subject).getClipboard();
        subject.init();
    }

    public class WhenTheImportButtonIsClicked {
        private FileHandle imageFile;

        @Before
        public void before() throws IOException {
            imageFile = new FileHandle("file");
            when(grabber.grab("url")).thenReturn(imageFile);
            clipboard.setContents("url");
            importButton.fireChanged();
        }

        @Test
        public void it_AddsTheImageFromTheClipboardUrl() {
            verify(model).add(imageFile);
        }
    }

    public class WhenTypingInTheWidthTextBox {
        @Before
        public void before() {
            widthField.enter("100");
        }

        @Test
        public void it_ChangesTheImageWidth() {
            verify(gameImage).setWidth(100);
        }
    }

    public class WhenTypingInTheHeightTextBox {
        @Before
        public void before() {
            heightField.enter("100");
        }

        @Test
        public void it_ChangesTheImageHeight() {
            verify(gameImage).setHeight(100);
        }
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

    public static class TestTextField {
        @Mock private TextField textField;
        private ArgumentCaptor<TextField.TextFieldListener> captor;

        public TestTextField() {
            initMocks(this);
            captor = ArgumentCaptor.forClass(TextField.TextFieldListener.class);
        }

        public TextField mock() {
            return textField;
        }

        public void enter(String text) {
            when(textField.getText()).thenReturn(text);
            verify(textField).setTextFieldListener(captor.capture());
            captor.getValue().keyTyped(textField, 'x');
        }
    }
}
