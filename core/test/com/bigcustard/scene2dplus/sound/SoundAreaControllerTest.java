package com.bigcustard.scene2dplus.sound;

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

import java.util.Arrays;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(HierarchicalContextRunner.class)
public class SoundAreaControllerTest {
    @Mock private SoundArea view;
    @Mock private SoundAreaModel model;
    @Mock private SoundModel sound;
    @Mock private SoundControls soundControls;
    @Captor private ArgumentCaptor<Runnable> removeButtonListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<String>> nameListenerCaptor;
    @Captor private ArgumentCaptor<Runnable> importButtonListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<SoundControls>> addSoundControlsListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<SoundControls>> removeSoundControlsListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<SoundModel>> addSoundListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<SoundModel>> removeSoundListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<SoundModel>> soundChangeListenerCaptor;
    private TestClipboard clipboard = new TestClipboard();
    private CommandHistory commandHistory = new CommandHistory();
    private SoundAreaController subject;

    @Before
    public void before() {
        initMocks(this);

//        when(model.addSound(anyString())).thenReturn(sound);
        when(model.sounds()).thenReturn(Arrays.asList(sound));
        when(view.getAllSoundControls()).thenReturn(Arrays.asList(soundControls));
        when(soundControls.getSound()).thenReturn(sound);

        doNothing().when(view).registerImportButtonListener(importButtonListenerCaptor.capture());
        doNothing().when(view).registerAddSoundControlsListener(addSoundControlsListenerCaptor.capture());
        doNothing().when(view).registerRemoveSoundControlsListener(removeSoundControlsListenerCaptor.capture());
        doNothing().when(model).registerAddSoundListener(addSoundListenerCaptor.capture());
        doNothing().when(model).registerRemoveSoundListener(removeSoundListenerCaptor.capture());
        doNothing().when(soundControls).registerDeleteButtonListener(removeButtonListenerCaptor.capture());
        doNothing().when(soundControls).registerNameFieldListener(nameListenerCaptor.capture());
        doNothing().when(sound).registerChangeListener(soundChangeListenerCaptor.capture());

        subject = spy(new SoundAreaController(view, model, commandHistory));
        doReturn(clipboard).when(subject).getClipboard();
        subject.init();
    }

//    public class WhenTheImportButtonIsClicked {
//        private FileHandle soundFile;
//
//        @Before
//        public void before() throws IOException {
//            soundFile = new FileHandle("file");
//            clipboard.setContents("url");
//            importButtonListenerCaptor.getValue().run();
//        }
//
//        @Test
//        public void it_AddsTheSoundFromTheClipboardUrl() {
//            verify(model).addSound("url");
//        }
//
//        public class ThenTheImportButtonIsClickedAgain {
//            @Before
//            public void before() throws IOException {
//                importButtonListenerCaptor.getValue().run();
//            }
//
//            @Test
//            public void it_AddsTheSoundAgain() {
//                verify(model, times(2)).addSound("url");
//            }
//        }
//
//        public class ThenUndoing {
//            @Before
//            public void before() throws IOException {
//                commandHistory.undo();
//            }
//
//            @Test
//            public void it_RemovesTheSound() {
//                verify(model).removeSound(any(SoundModel.class));
//            }
//        }
//    }

    public class WhenTheRemoveButtonIsClicked {
        private FileHandle soundFile;

        @Before
        public void before() {
            removeButtonListenerCaptor.getValue().run();
        }

        @Test
        public void it_RemovesTheSound() {
            verify(model).removeSound(sound);
        }

        public class ThenUndoing {
            @Before
            public void before() {
                commandHistory.undo();
            }

            @Test
            public void it_ReaddsTheSound() {
                verify(model).addSound(sound);
            }
        }
    }

    public class WhenTypingInTheNameTextBox {
        @Before
        public void before() {
            nameListenerCaptor.getValue().accept("name");
        }

        @Test
        public void it_ChangesTheSoundName() {
            verify(sound).name("name");
        }

        public class ThenEnteringANewNameAndUndoing {
            @Before
            public void before() {
                nameListenerCaptor.getValue().accept("new name");
                commandHistory.undo();
            }

            @Test
            public void it_RestoresTheOldName() {
                verify(sound).name("name");
            }
        }
    }
}
