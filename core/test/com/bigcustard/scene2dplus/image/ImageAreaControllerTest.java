package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.textarea.command.TestClipboard;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
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
    private TestTextButton deleteButton = new TestTextButton();
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void before() {
        initMocks(this);
        importButton = TestTextButton.mocking(view.importButton());
        ImageControls imageControls =
                new ImageControls(gameImage, nameField.mock(), widthField.mock(), heightField.mock(), deleteButton.mock());
        when(view.getImageControls(gameImage)).thenReturn(imageControls);
        when(model.addImage(anyString())).thenReturn(gameImage);
        when(model.getImages()).thenReturn(Arrays.asList(gameImage));
        subject = spy(new ImageAreaController(view, model, commandHistory));
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

        public class WhenTheUndoButtonIsClicked {
            @Before
            public void before() throws IOException {
                commandHistory.undo();
            }

            @Test
            public void it_DeletesTheImage() {
                verify(model).deleteImage(any(ImagePlus.class));
            }
        }
    }

    public class WhenTheDeleteButtonIsClicked {
        private FileHandle imageFile;

        @Before
        public void before() {
            deleteButton.fireChanged();
        }

        @Test
        public void it_DeletesTheImage() {
            verify(model).deleteImage(gameImage);
        }

        public class ThenTheUndoButtonIsClicked {
            private FileHandle imageFile;

            @Before
            public void before() {
                commandHistory.undo();
            }

            @Test
            public void it_ReaddsTheImage() {
                verify(model).addImage(gameImage);
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

    public class WhenUndoing {
        @Before
        public void before() {
            when(gameImage.width()).thenReturn(200);
            widthField.enter("300");
            commandHistory.undo();
        }

        @Test
        public void it_RestoresTheOldWidth() {
            verify(gameImage).setWidth(200);
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

    public class WhenTheUnderlyingImageWidthChangesToAValidValue {
        @Before
        public void before() {
            when(gameImage.width()).thenReturn(50);
            subject.onImageChanged(gameImage);
        }

        @Test
        public void it_UpdatesTheWidthTextField() {
            verify(widthField.mock()).setText("50");
        }
    }

    public class WhenTheUnderlyingImageWidthChangesToAnInvalidValue {
        @Before
        public void before() {
            when(gameImage.width()).thenReturn(null);
            subject.onImageChanged(gameImage);
        }

        @Test
        public void it_UpdatesTheWidthTextField() {
            verify(widthField.mock()).setText("");
        }
    }

    public class WhenTheUnderlyingImageHeightChanges {
        @Before
        public void before() {
            when(gameImage.height()).thenReturn(50);
            subject.onImageChanged(gameImage);
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
            subject.onImageChanged(gameImage);
        }

        @Test
        public void it_UpdatesTheHeightTextField() {
            verify(nameField.mock()).setText("name");
        }
    }

    public class WhenTheUnderlyingImageNameChangesToAnEmptyValue {
        @Before
        public void before() {
            when(gameImage.name()).thenReturn("");
            subject.onImageChanged(gameImage);
        }

        @Test
        public void it_UpdatesTheHeightTextField() {
            verify(nameField.mock()).setText("");
        }
    }

    public class WhenTheModelChangesToHaveAnInvalidImageName {
        @Before
        public void before() {
            ImageValidator.Result result = mock(ImageValidator.Result.class);
            when(result.image()).thenReturn(gameImage);
            when(result.isNameValid()).thenReturn(false);
            when(model.validateImages()).thenReturn(Arrays.asList(result));
            subject.onImageChanged(gameImage);
        }

        @Test
        public void it_MarksTheFieldAsInvalid() {
            verify(nameField.mock()).setValid(false);
        }
    }

    public class WhenTheModelChangesToHaveAValidImageName {
        @Before
        public void before() {
            ImageValidator.Result result = mock(ImageValidator.Result.class);
            when(result.image()).thenReturn(gameImage);
            when(result.isNameValid()).thenReturn(true);
            when(model.validateImages()).thenReturn(Arrays.asList(result));
            subject.onImageChanged(gameImage);
        }

        @Test
        public void it_MarksTheFieldAsValid() {
            verify(nameField.mock()).setValid(true);
        }
    }

    public class WhenTheModelChangesToHaveAnInvalidImageWidth {
        @Before
        public void before() {
            ImageValidator.Result result = mock(ImageValidator.Result.class);
            when(result.image()).thenReturn(gameImage);
            when(result.isWidthValid()).thenReturn(false);
            when(model.validateImages()).thenReturn(Arrays.asList(result));
            subject.onImageChanged(gameImage);
        }

        @Test
        public void it_MarksTheFieldAsInvalid() {
            verify(widthField.mock()).setValid(false);
        }
    }

    public class WhenTheModelChangesToHaveAValidImageWidth {
        @Before
        public void before() {
            ImageValidator.Result result = mock(ImageValidator.Result.class);
            when(result.image()).thenReturn(gameImage);
            when(result.isWidthValid()).thenReturn(true);
            when(model.validateImages()).thenReturn(Arrays.asList(result));
            subject.onImageChanged(gameImage);
        }

        @Test
        public void it_MarksTheFieldAsValid() {
            verify(widthField.mock()).setValid(true);
        }
    }

    public class WhenTheModelChangesToHaveAnInvalidImageHeight {
        @Before
        public void before() {
            ImageValidator.Result result = mock(ImageValidator.Result.class);
            when(result.image()).thenReturn(gameImage);
            when(result.isHeightValid()).thenReturn(false);
            when(model.validateImages()).thenReturn(Arrays.asList(result));
            subject.onImageChanged(gameImage);
        }

        @Test
        public void it_MarksTheFieldAsInvalid() {
            verify(heightField.mock()).setValid(false);
        }
    }

    public class WhenTheModelChangesToHaveAValidImageHeight {
        @Before
        public void before() {
            ImageValidator.Result result = mock(ImageValidator.Result.class);
            when(result.image()).thenReturn(gameImage);
            when(result.isHeightValid()).thenReturn(true);
            when(model.validateImages()).thenReturn(Arrays.asList(result));
            subject.onImageChanged(gameImage);
        }

        @Test
        public void it_MarksTheFieldAsValid() {
            verify(heightField.mock()).setValid(true);
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
