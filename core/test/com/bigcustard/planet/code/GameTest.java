package com.bigcustard.planet.code;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImageModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class GameTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private Preferences mockPreferences;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private ImageAreaModel mockImageModel;
    @Mock private FileHandle mockParentFolder;
    @Mock private FileHandle mockGameFolder;
    @Mock private FileHandle mockGroovyCodeFile;
    @Mock private FileHandle mockJRubyCodeFile;
    @Mock private ImageModel mockImage;
    @Mock private Consumer<Game> mockChangeListener;
    @Mock private Language mockLanguage;
    @Captor private ArgumentCaptor<Consumer<ImageModel>> addImageListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<ImageModel>> removeImageListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<ImageModel>> changeImageListenerCaptor;

    @Before
    public void before() {
        initMocks(this);
        when(mockGameFolder.child("code.groovy")).thenReturn(mockGroovyCodeFile);
        when(mockGroovyCodeFile.extension()).thenReturn("groovy");
        when(mockJRubyCodeFile.extension()).thenReturn("jruby");
        doNothing().when(mockImageModel).registerAddImageListener(addImageListenerCaptor.capture());
        doNothing().when(mockImageModel).registerRemoveImageListener(removeImageListenerCaptor.capture());
        doNothing().when(mockImageModel).registerChangeImageListener(changeImageListenerCaptor.capture());
    }

    @Test
    public void allGameFoldersWhenDirectoryEmpty() {
        when(mockParentFolder.list(any(FileFilter.class))).thenReturn(new FileHandle[0]);
        assertThat(Game.allGameFolders(mockParentFolder)).isEmpty();
    }

    @Test
    public void allGameFoldersWhenDirectoryContainsGameFolder() {
        ArgumentCaptor<FileFilter> filterCaptor = ArgumentCaptor.forClass(FileFilter.class);
        File mockGameFolderAsFile = mock(File.class);
        when(mockGameFolder.file()).thenReturn(mockGameFolderAsFile);
        when(mockGameFolderAsFile.getName()).thenReturn("game");
        when(mockGameFolderAsFile.isDirectory()).thenReturn(true);

        when(mockParentFolder.list(filterCaptor.capture())).thenAnswer(invocation ->
                Arrays.asList(mockGameFolder)
                        .stream()
                        .filter((folder) -> filterCaptor.getValue().accept(folder.file()))
                        .toArray(FileHandle[]::new));
        assertThat(Game.allGameFolders(mockParentFolder)).containsExactly(mockGameFolder);
    }

    @Test
    public void allGameFoldersWhenDirectoryContainsFileUsingNameOfGame() {
        ArgumentCaptor<FileFilter> filterCaptor = ArgumentCaptor.forClass(FileFilter.class);
        File mockGameFolderAsFile = mock(File.class);
        when(mockGameFolder.file()).thenReturn(mockGameFolderAsFile);
        when(mockGameFolderAsFile.getName()).thenReturn("game");
        when(mockGameFolderAsFile.isDirectory()).thenReturn(false);

        when(mockParentFolder.list(filterCaptor.capture())).thenAnswer(invocation ->
                Arrays.asList(mockGameFolder)
                        .stream()
                        .filter((folder) -> filterCaptor.getValue().accept(folder.file()))
                        .toArray(FileHandle[]::new));
        assertThat(Game.allGameFolders(mockParentFolder)).isEmpty();
    }

    @Test
    public void allGameFoldersWhenDirectoryContainsFolderStartingWithDot() {
        ArgumentCaptor<FileFilter> filterCaptor = ArgumentCaptor.forClass(FileFilter.class);
        File mockGameFolderAsFile = mock(File.class);
        when(mockGameFolder.file()).thenReturn(mockGameFolderAsFile);
        when(mockGameFolderAsFile.getName()).thenReturn(".game");
        when(mockGameFolderAsFile.isDirectory()).thenReturn(false);

        when(mockParentFolder.list(filterCaptor.capture())).thenAnswer(invocation ->
                Arrays.asList(mockGameFolder)
                        .stream()
                        .filter((folder) -> filterCaptor.getValue().accept(folder.file()))
                        .toArray(FileHandle[]::new));
        assertThat(Game.allGameFolders(mockParentFolder)).isEmpty();
    }

    @Test
    public void isValidIfCodeAndImagesValid() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        game.setCode("code");
        when(mockLanguage.isValid("code")).thenReturn(true);
        when(mockImageModel.isValid()).thenReturn(true);
        assertThat(game.isValid()).isTrue();
    }

    @Test
    public void isInvalidIfCodeInvalid() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        game.setCode("code");
        when(mockLanguage.isValid("code")).thenReturn(false);
        when(mockImageModel.isValid()).thenReturn(true);
        assertThat(game.isValid()).isFalse();
    }

    @Test
    public void isInvalidIfImagesInvalid() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        game.setCode("code");
        when(mockLanguage.isValid("code")).thenReturn(true);
        when(mockImageModel.isValid()).thenReturn(false);
        assertThat(game.isValid()).isFalse();
    }

    @Test
    public void createNewUsesTemplate() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        assertThat(game.code()).isEqualTo(Game.TEMPLATE);
    }

    @Test
    public void createNewWhenDefaultNameNotInUse() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        when(mockGameFolder.exists()).thenReturn(false);
        Game game = newGame(mockLanguage);
        assertThat(game.name()).isEqualTo("Unnamed Game");
    }

    @Test
    public void createNewWhenDefaultNameInUse() {
        FileHandle mockGameFolder2 = mock(FileHandle.class);
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockParentFolder.child("Unnamed Game 2")).thenReturn(mockGameFolder2);
        when(mockGameFolder2.child("code.groovy")).thenReturn(mockGroovyCodeFile);
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder2.exists()).thenReturn(false);
        when(mockGameFolder2.name()).thenReturn("Unnamed Game 2");
        Game game = newGame(mockLanguage);
        assertThat(game.name()).isEqualTo("Unnamed Game 2");
    }

    @Test
    public void createNewWhenDefaultNamesInUse() {
        FileHandle mockGameFolder2 = mock(FileHandle.class);
        FileHandle mockGameFolder3 = mock(FileHandle.class);
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockParentFolder.child("Unnamed Game 2")).thenReturn(mockGameFolder2);
        when(mockParentFolder.child("Unnamed Game 3")).thenReturn(mockGameFolder3);
        when(mockGameFolder2.child("code.groovy")).thenReturn(mockGroovyCodeFile);
        when(mockGameFolder3.child("code.groovy")).thenReturn(mockGroovyCodeFile);
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder2.exists()).thenReturn(true);
        when(mockGameFolder3.exists()).thenReturn(false);
        when(mockGameFolder3.name()).thenReturn("Unnamed Game 3");
        Game game = newGame(mockLanguage);
        assertThat(game.name()).isEqualTo("Unnamed Game 3");
    }

    @Test
    public void hasNoRecentWhenNotInPrefs() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        when(mockPreferences.getString("MostRecentGameName")).thenReturn(null);
        assertThat(Game.hasMostRecent(mockPreferences, mockParentFolder)).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoDirectory() {
        when(mockParentFolder.child("planet")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("planet");
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(Game.hasMostRecent(mockPreferences, mockParentFolder)).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoCodeFile() {
        when(mockParentFolder.child("planet")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("planet");
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder.child("code.groovy").exists()).thenReturn(false);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(Game.hasMostRecent(mockPreferences, mockParentFolder)).isFalse();
    }

    @Test
    public void hasRecentWhenInPrefsAndDirectoryAndCodeFileExist() {
        when(mockParentFolder.child("planet")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("planet");
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder.child("code.groovy").exists()).thenReturn(true);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(Game.hasMostRecent(mockPreferences, mockParentFolder)).isTrue();
    }

    @Test
    public void changingGameNameRenamesDirectory() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        Game game = newGame(mockLanguage);
        when(mockGameFolder.exists()).thenReturn(true);
        FileHandle mockNewGameFolder = mock(FileHandle.class);
        when(mockGameFolder.sibling("name")).thenReturn(mockNewGameFolder);
        when(mockNewGameFolder.exists()).thenReturn(false);
        game.setName("name");
        verify(mockGameFolder).moveTo(mockNewGameFolder);
    }

    @Test
    public void changingGameNameWhenNoGameDirectorySavesFirst() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.exists()).thenReturn(false);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        FileHandle mockNewGameFolder = mock(FileHandle.class);
        when(mockGameFolder.sibling("name")).thenReturn(mockNewGameFolder);
        when(mockNewGameFolder.exists()).thenReturn(false);
        Game game = newGame(mockLanguage);
        game.setName("name");
        verify(mockGameFolder.child("code.groovy"), times(2)).writeString(game.code(), false);
    }

    @Test(expected = GameRenameException.class)
    public void changingGameNameFailsIfTargetDirectoryExists() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        FileHandle mockNewGameFolder = mock(FileHandle.class);
        when(mockGameFolder.sibling("name")).thenReturn(mockNewGameFolder);
        when(mockNewGameFolder.exists()).thenReturn(true);
        Game game = newGame(mockLanguage);
        when(mockGameFolder.exists()).thenReturn(true);
        game.setName("name");
    }

    @Test
    public void changingTextStoresCode() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        Game game = newGame(mockLanguage);
        game.setCode("code");
        verify(mockGameFolder.child("code.groovy")).writeString("code", false);
    }

    @Test
    public void addImageStoresImageModel() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        newGame(mockLanguage);
        addImageListenerCaptor.getValue().accept(mockImage);
        verify(mockImageModel, times(2)).save();
    }

    @Test
    public void removeImageStoresImageModel() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        newGame(mockLanguage);
        removeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockImageModel, times(2)).save();
    }

    @Test
    public void changeImageStoresImageModel() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        newGame(mockLanguage);
        changeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockImageModel, times(2)).save();
    }

    @Test
    public void deleteRemovesFolder() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        Game game = newGame(mockLanguage);
        game.delete();
        verify(mockGameFolder).deleteDirectory();
    }

    @Test
    public void isUnnamedWhenNew() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        assertThat(newGame(mockLanguage).isNamed()).isFalse();
    }

    @Test
    public void isNamedAfterNameChange() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        when(mockGameFolder.name()).thenReturn("name");
        Game game = newGame(mockLanguage);
        game.setName("name");
        assertThat(game.isNamed()).isTrue();
    }

    @Test
    public void mostRecentLoadsCode() {
        when(mockParentFolder.child("planet")).thenReturn(mockGameFolder);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder.name()).thenReturn("planet");
        when(mockGameFolder.child("code.groovy").exists()).thenReturn(true);
        when(mockGameFolder.child("code.groovy").readString()).thenReturn("code");
        assertThat(continueGame().name()).isEqualTo("planet");
        assertThat(continueGame().code()).isEqualTo("code");
    }

    @Test
    public void mostRecentLoadsImages() {
        when(mockParentFolder.child("planet")).thenReturn(mockGameFolder);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        continueGame();
        verify(mockImageModel).loadFromFolder(mockParentFolder.child("planet"));
    }

    @Test
    public void providesAccessToTheImageModel() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        assertThat(newGame(mockLanguage).imageModel()).isSameAs(mockImageModel);
    }

    @Test
    public void notifiesOfCodeChange() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        game.registerChangeListener(mockChangeListener);
        game.setCode("change");
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void notifiesOfImageAdd() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        game.registerChangeListener(mockChangeListener);
        addImageListenerCaptor.getValue().accept(mockImage);
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void notifiesOfImageRemove() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        game.registerChangeListener(mockChangeListener);
        removeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void notifiesOfImageChange() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        game.registerChangeListener(mockChangeListener);
        changeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void notifiesOfRuntimeError() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        game.registerChangeListener(mockChangeListener);
        game.setRuntimeError(new RuntimeException("Bad stuff"));
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void runtimeErrorMessageWhenNone() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        assertThat(game.runtimeError()).isNull();
    }

    @Test
    public void extractRuntimeErrorMessage() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        game.setRuntimeError(new RuntimeException(new RuntimeException(new RuntimeException(new ScriptException("Bad stuff")))));
        assertThat(game.runtimeError()).isEqualTo("Bad stuff");
    }

    @Test
    public void unexpectedRuntimeErrorMessage() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame(mockLanguage);
        game.setRuntimeError(new RuntimeException("Bad stuff"));
        assertThat(game.runtimeError()).isEqualTo("Bad stuff");
    }

    @Test
    public void fromFileUsesGroovySuffixToDetermineLanguage() {
        when(mockGameFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[]{mockGroovyCodeFile});
        Game game = fromGame();
        assertThat(game.language()).isEqualTo(Language.Groovy);
    }

    @Test
    public void fromFileUsesRubySuffixToDetermineLanguage() {
        Game game = fromGame();
        when(mockGameFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[] {mockJRubyCodeFile});
        assertThat(game.language()).isEqualTo(Language.JRuby);
    }

    private Game newGame(Language language) {
        return Game.create(mockPreferences, mockParentFolder, mockImageModel, language);
    }

    private Game continueGame() {
        return Game.mostRecent(mockPreferences, mockParentFolder, mockImageModel);
    }

    private Game fromGame() {
        return Game.from(mockPreferences, mockGameFolder, mockImageModel);
    }
}
