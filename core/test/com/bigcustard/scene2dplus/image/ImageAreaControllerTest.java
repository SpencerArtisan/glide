package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.textarea.command.TestClipboard;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(HierarchicalContextRunner.class)
public class ImageAreaControllerTest {
    @Mock private ImageArea view;
    @Mock private ImageAreaModel model;
    @Mock private ImagePlus gameImage;
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
        when(view.getImageControls(gameImage)).thenReturn(imageControls);
        when(model.addImage(any())).thenReturn(gameImage);
        when(model.getImages()).thenReturn(Arrays.asList(gameImage));
        subject = spy(new ImageAreaController(view, model));
        doReturn(clipboard).when(subject).getClipboard();
        subject.init();
    }

    public class WhenTheImportButtonIsClicked {
        private FileHandle imageFile;

        @Before
        public void before() throws IOException {
            imageFile = new FileHandle("file");
            clipboard.setContents("url");
            importButton.fireChanged();
        }

        @Test
        public void it_AddsTheImageFromTheClipboardUrl() {
            verify(model).addImage("url");
        }

        public class WhenTheImportButtonIsClickedAgain {
            @Before
            public void before() throws IOException {
                importButton.fireChanged();
            }

            @Test
            public void it_AddsTheImageAgain() {
                verify(model, times(2)).addImage("url");
            }
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

    public class WhenEmptyingTheWidthTextBox {
        @Before
        public void before() {
            widthField.enter("");
        }

        @Test
        public void it_ChangesTheImageWidthToNull() {
            verify(gameImage).setWidth(null);
        }
    }

    public class WhenEmptyingTheHeightTextBox {
        @Before
        public void before() {
            heightField.enter("");
        }

        @Test
        public void it_ChangesTheImageHeightToNull() {
            verify(gameImage).setHeight(null);
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

    public class WhenTheUnderlyingImageWidthChanges {
        @Before
        public void before() {
            when(gameImage.width()).thenReturn(50);
            subject.onModelChange(gameImage);
        }

        @Test
        public void it_UpdatesTheWidthTextField() {
            verify(widthField.mock()).setText("50");
        }
    }

    public class WhenTheUnderlyingImageHeightChanges {
        @Before
        public void before() {
            when(gameImage.height()).thenReturn(50);
            subject.onModelChange(gameImage);
        }

        @Test
        public void it_UpdatesTheHeightTextField() {
            verify(heightField.mock()).setText("50");
        }
    }

    public class WhenTheUnderlyingImageNameChanges {
        @Before
        public void before() {
            when(gameImage.name()).thenReturn("name");
            subject.onModelChange(gameImage);
        }

        @Test
        public void it_UpdatesTheHeightTextField() {
            verify(nameField.mock()).setText("name");
        }
    }

    public class WhenTypingInTheNameTextBox {
        @Before
        public void before() {
            nameField.enter("name");
        }

        @Test
        public void it_ChangesTheImageName() {
            verify(gameImage).setName("name");
        }
    }
}
