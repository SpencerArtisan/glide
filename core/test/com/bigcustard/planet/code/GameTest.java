package com.bigcustard.planet.code;

import com.bigcustard.planet.code.language.Language;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImageModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import javax.script.ScriptException;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class GameTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private ImageAreaModel mockImageModel;
    @Mock private ImageModel mockImage;
    @Mock private Consumer<Game> mockChangeListener;
    @Mock private Language mockLanguage;
    @Captor private ArgumentCaptor<Consumer<ImageModel>> addImageListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<ImageModel>> removeImageListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<ImageModel>> changeImageListenerCaptor;

    @Before
    public void before() {
        initMocks(this);
        when(mockLanguage.scriptEngine()).thenReturn("groovy");
        doNothing().when(mockImageModel).registerAddImageListener(addImageListenerCaptor.capture());
        doNothing().when(mockImageModel).registerRemoveImageListener(removeImageListenerCaptor.capture());
        doNothing().when(mockImageModel).registerChangeImageListener(changeImageListenerCaptor.capture());
    }

    @Test
    public void isValidIfCodeAndImagesValid() {
        Game game = newGame(mockLanguage);
        game.code("code");
        when(mockLanguage.isValid("code")).thenReturn(true);
        when(mockImageModel.isValid()).thenReturn(true);
        assertThat(game.isValid()).isTrue();
    }

    @Test
    public void isInvalidIfCodeInvalid() {
        Game game = newGame(mockLanguage);
        game.code("code");
        when(mockLanguage.isValid("code")).thenReturn(false);
        when(mockImageModel.isValid()).thenReturn(true);
        assertThat(game.isValid()).isFalse();
    }

    @Test
    public void isInvalidIfImagesInvalid() {
        Game game = newGame(mockLanguage);
        game.code("code");
        when(mockLanguage.isValid("code")).thenReturn(true);
        when(mockImageModel.isValid()).thenReturn(false);
        assertThat(game.isValid()).isFalse();
    }

    @Test
    public void addImageStoresImageModel() {
        newGame(mockLanguage);
        addImageListenerCaptor.getValue().accept(mockImage);
        verify(mockImageModel, times(1)).save();
    }

    @Test
    public void removeImageStoresImageModel() {
        newGame(mockLanguage);
        removeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockImageModel, times(1)).save();
    }

    @Test
    public void changeImageStoresImageModel() {
        newGame(mockLanguage);
        changeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockImageModel, times(1)).save();
    }

    @Test
    public void providesAccessToTheImageModel() {
        assertThat(newGame(mockLanguage).imageModel()).isSameAs(mockImageModel);
    }

    @Test
    public void notifiesOfCodeChange() {
        Game game = newGame(mockLanguage);
        game.registerChangeListener(mockChangeListener);
        game.code("change");
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void notifiesOfImageAdd() {
        Game game = newGame(mockLanguage);
        game.registerChangeListener(mockChangeListener);
        addImageListenerCaptor.getValue().accept(mockImage);
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void notifiesOfImageRemove() {
        Game game = newGame(mockLanguage);
        game.registerChangeListener(mockChangeListener);
        removeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void notifiesOfImageChange() {
        Game game = newGame(mockLanguage);
        game.registerChangeListener(mockChangeListener);
        changeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void notifiesOfRuntimeError() {
        Game game = newGame(mockLanguage);
        game.registerChangeListener(mockChangeListener);
        game.runtimeError(new RuntimeException("Bad stuff"));
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void runtimeErrorMessageWhenNone() {
        Game game = newGame(mockLanguage);
        assertThat(game.runtimeError()).isNull();
    }

    @Test
    public void extractRuntimeErrorMessage() {
        Game game = newGame(mockLanguage);
        game.runtimeError(new RuntimeException(new RuntimeException(new RuntimeException(new ScriptException("Bad stuff")))));
        assertThat(game.runtimeError()).isEqualTo("Bad stuff");
    }

    @Test
    public void unexpectedRuntimeErrorMessage() {
        Game game = newGame(mockLanguage);
        game.runtimeError(new RuntimeException("Bad stuff"));
        assertThat(game.runtimeError()).isEqualTo("Bad stuff");
    }

    @Test
    public void itShould_NotBeModifiedInitially() {
        Game game = newGame(mockLanguage);
        assertThat(game.isModified()).isFalse();
    }

    @Test
    public void itShould_BeModifiedWhenTheCodeChangesToSomethingDifferent() {
        Game game = newGame(mockLanguage);
        game.code("new code");
        assertThat(game.isModified()).isTrue();
    }

    @Test
    public void itShould_BeModifiedWhenTheCodeChangesToSomethingDifferentThenTheSameCode() {
        Game game = newGame(mockLanguage);
        game.code("new code");
        game.code("new code");
        assertThat(game.isModified()).isTrue();
    }

    @Test
    public void itShould_NotBeModifiedWhenTheCodeChangesToTheSameCode() {
        Game game = newGame(mockLanguage);
        game.code(game.code());
        assertThat(game.isModified()).isFalse();
    }

    @Test
    public void itShould_BeModifiedWhenImagesChange() {
        Game game = newGame(mockLanguage);
        addImageListenerCaptor.getValue().accept(mockImage);
        assertThat(game.isModified()).isTrue();
    }

    private Game newGame(Language language) {
        return new Game("name", "code", language, mockImageModel);
    }
}
