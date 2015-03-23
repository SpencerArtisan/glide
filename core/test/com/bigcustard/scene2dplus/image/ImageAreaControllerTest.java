package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.textarea.command.TestClipboard;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(HierarchicalContextRunner.class)
public class ImageAreaControllerTest {
    @Mock private ImageArea view;
    @Mock private ImageAreaModel model;
    @Mock private ImagePlus image;
    @Mock private ImageControls imageControls;
    @Captor private ArgumentCaptor<Runnable> removeButtonListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<String>> nameListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<Integer>> widthListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<Integer>> heightListenerCaptor;
    @Captor private ArgumentCaptor<Runnable> importButtonListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<ImageControls>> addImageControlsListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<ImageControls>> removeImageControlsListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<ImagePlus>> addImageListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<ImagePlus>> removeImageListenerCaptor;
    @Captor private ArgumentCaptor<Runnable> imageChangeListenerCaptor;
    private TestClipboard clipboard = new TestClipboard();
    private CommandHistory commandHistory = new CommandHistory();
    private ImageAreaController subject;

    @Before
    public void before() {
        initMocks(this);

        when(model.addImage(anyString())).thenReturn(image);
        when(model.getImages()).thenReturn(Arrays.asList(image));
        when(view.getAllImageControls()).thenReturn(Arrays.asList(imageControls));
        when(imageControls.getImage()).thenReturn(image);

        doNothing().when(view).registerImportButtonListener(importButtonListenerCaptor.capture());
        doNothing().when(view).registerAddImageControlsListener(addImageControlsListenerCaptor.capture());
        doNothing().when(view).registerRemoveImageControlsListener(removeImageControlsListenerCaptor.capture());
        doNothing().when(model).registerAddImageListener(addImageListenerCaptor.capture());
        doNothing().when(model).registerRemoveImageListener(removeImageListenerCaptor.capture());
        doNothing().when(imageControls).registerDeleteButtonListener(removeButtonListenerCaptor.capture());
        doNothing().when(imageControls).registerNameFieldListener(nameListenerCaptor.capture());
        doNothing().when(imageControls).registerHeightFieldListener(heightListenerCaptor.capture());
        doNothing().when(imageControls).registerWidthFieldListener(widthListenerCaptor.capture());
        doNothing().when(image).registerChangeListener(imageChangeListenerCaptor.capture());

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
            importButtonListenerCaptor.getValue().run();
        }

        @Test
        public void it_AddsTheImageFromTheClipboardUrl() {
            verify(model).addImage("url");
        }

        public class ThenTheImportButtonIsClickedAgain {
            @Before
            public void before() throws IOException {
                importButtonListenerCaptor.getValue().run();
            }

            @Test
            public void it_AddsTheImageAgain() {
                verify(model, times(2)).addImage("url");
            }
        }

        public class ThenUndoing {
            @Before
            public void before() throws IOException {
                commandHistory.undo();
            }

            @Test
            public void it_RemovesTheImage() {
                verify(model).removeImage(any(ImagePlus.class));
            }
        }
    }

    public class WhenTheRemoveButtonIsClicked {
        private FileHandle imageFile;

        @Before
        public void before() {
            removeButtonListenerCaptor.getValue().run();
        }

        @Test
        public void it_RemovesTheImage() {
            verify(model).removeImage(image);
        }

        public class ThenUndoing {
            @Before
            public void before() {
                commandHistory.undo();
            }

            @Test
            public void it_ReaddsTheImage() {
                verify(model).addImage(image);
            }
        }
    }

    public class WhenTypingInTheWidthTextBox {
        @Before
        public void before() {
            widthListenerCaptor.getValue().accept(100);
        }

        @Test
        public void it_ChangesTheImageWidth() {
            verify(image).setWidth(100);
        }

        public class ThenEnteringANewWidthAndUndoing {
            @Before
            public void before() {
                widthListenerCaptor.getValue().accept(200);
                commandHistory.undo();
            }

            @Test
            public void it_RestoresTheOldWidth() {
                verify(image).setWidth(100);
            }
        }
    }

    public class WhenEmptyingTheWidthTextBox {
        @Before
        public void before() {
            widthListenerCaptor.getValue().accept(null);
        }

        @Test
        public void it_ChangesTheImageWidthToNull() {
            verify(image).setWidth(null);
        }
    }

    public class WhenTypingInTheHeightTextBox {
        @Before
        public void before() {
            heightListenerCaptor.getValue().accept(100);
        }

        @Test
        public void it_ChangesTheImageHeight() {
            verify(image).setHeight(100);
        }

        public class ThenEnteringANewHeightAndUndoing {
            @Before
            public void before() {
                heightListenerCaptor.getValue().accept(200);
                commandHistory.undo();
            }

            @Test
            public void it_RestoresTheOldHeight() {
                verify(image).setHeight(100);
            }
        }
    }

    public class WhenEmptyingTheHeightTextBox {
        @Before
        public void before() {
            heightListenerCaptor.getValue().accept(null);
        }

        @Test
        public void it_ChangesTheImageHeightToNull() {
            verify(image).setHeight(null);
        }
    }

    public class WhenTypingInTheNameTextBox {
        @Before
        public void before() {
            nameListenerCaptor.getValue().accept("name");
        }

        @Test
        public void it_ChangesTheImageName() {
            verify(image).setName("name");
        }

        public class ThenEnteringANewNameAndUndoing {
            @Before
            public void before() {
                nameListenerCaptor.getValue().accept("new name");
                commandHistory.undo();
            }

            @Test
            public void it_RestoresTheOldName() {
                verify(image).setName("name");
            }
        }
    }


//    public class WhenTheUnderlyingImageWidthChangesToAValidValue {
//        @Before
//        public void before() {
//            when(image.width()).thenReturn(50);
//            imageChangeListenerCaptor.getValue().run();
//        }
//
//        @Test
//        public void it_UpdatesTheWidthTextField() {
//            verify(widthField.mock()).setText("50");
//        }
//    }
//
//    public class WhenTheUnderlyingImageWidthChangesToAnInvalidValue {
//        @Before
//        public void before() {
//            when(image.width()).thenReturn(null);
//            subject.onImageChanged(image);
//        }
//
//        @Test
//        public void it_UpdatesTheWidthTextField() {
//            verify(widthField.mock()).setText("");
//        }
//    }
//
//    public class WhenTheUnderlyingImageHeightChanges {
//        @Before
//        public void before() {
//            when(image.height()).thenReturn(50);
//            subject.onImageChanged(image);
//        }
//
//        @Test
//        public void it_UpdatesTheHeightTextField() {
//            verify(heightField.mock()).setText("50");
//        }
//    }
//
//    public class WhenTheUnderlyingImageNameChanges {
//        @Before
//        public void before() {
//            when(image.name()).thenReturn("name");
//            subject.onImageChanged(image);
//        }
//
//        @Test
//        public void it_UpdatesTheHeightTextField() {
//            verify(nameField.mock()).setText("name");
//        }
//    }
//
//    public class WhenTheUnderlyingImageNameChangesToAnEmptyValue {
//        @Before
//        public void before() {
//            when(image.name()).thenReturn("");
//            subject.onImageChanged(image);
//        }
//
//        @Test
//        public void it_UpdatesTheHeightTextField() {
//            verify(nameField.mock()).setText("");
//        }
//    }
//
//    public class WhenTheModelChangesToHaveAnInvalidImageName {
//        @Before
//        public void before() {
//            ImageValidator.Result result = mock(ImageValidator.Result.class);
//            when(result.image()).thenReturn(image);
//            when(result.isNameValid()).thenReturn(false);
//            when(model.validateImages()).thenReturn(Arrays.asList(result));
//            subject.onImageChanged(image);
//        }
//
//        @Test
//        public void it_MarksTheFieldAsInvalid() {
//            verify(nameField.mock()).setValid(false);
//        }
//    }
//
//    public class WhenTheModelChangesToHaveAValidImageName {
//        @Before
//        public void before() {
//            ImageValidator.Result result = mock(ImageValidator.Result.class);
//            when(result.image()).thenReturn(image);
//            when(result.isNameValid()).thenReturn(true);
//            when(model.validateImages()).thenReturn(Arrays.asList(result));
//            subject.onImageChanged(image);
//        }
//
//        @Test
//        public void it_MarksTheFieldAsValid() {
//            verify(nameField.mock()).setValid(true);
//        }
//    }
//
//    public class WhenTheModelChangesToHaveAnInvalidImageWidth {
//        @Before
//        public void before() {
//            ImageValidator.Result result = mock(ImageValidator.Result.class);
//            when(result.image()).thenReturn(image);
//            when(result.isWidthValid()).thenReturn(false);
//            when(model.validateImages()).thenReturn(Arrays.asList(result));
//            subject.onImageChanged(image);
//        }
//
//        @Test
//        public void it_MarksTheFieldAsInvalid() {
//            verify(widthField.mock()).setValid(false);
//        }
//    }
//
//    public class WhenTheModelChangesToHaveAValidImageWidth {
//        @Before
//        public void before() {
//            ImageValidator.Result result = mock(ImageValidator.Result.class);
//            when(result.image()).thenReturn(image);
//            when(result.isWidthValid()).thenReturn(true);
//            when(model.validateImages()).thenReturn(Arrays.asList(result));
//            subject.onImageChanged(image);
//        }
//
//        @Test
//        public void it_MarksTheFieldAsValid() {
//            verify(widthField.mock()).setValid(true);
//        }
//    }
//
//    public class WhenTheModelChangesToHaveAnInvalidImageHeight {
//        @Before
//        public void before() {
//            ImageValidator.Result result = mock(ImageValidator.Result.class);
//            when(result.image()).thenReturn(image);
//            when(result.isHeightValid()).thenReturn(false);
//            when(model.validateImages()).thenReturn(Arrays.asList(result));
//            subject.onImageChanged(image);
//        }
//
//        @Test
//        public void it_MarksTheFieldAsInvalid() {
//            verify(heightField.mock()).setValid(false);
//        }
//    }
//
//    public class WhenTheModelChangesToHaveAValidImageHeight {
//        @Before
//        public void before() {
//            ImageValidator.Result result = mock(ImageValidator.Result.class);
//            when(result.image()).thenReturn(image);
//            when(result.isHeightValid()).thenReturn(true);
//            when(model.validateImages()).thenReturn(Arrays.asList(result));
//            subject.onImageChanged(image);
//        }
//
//        @Test
//        public void it_MarksTheFieldAsValid() {
//            verify(heightField.mock()).setValid(true);
//        }
//    }
//

}
