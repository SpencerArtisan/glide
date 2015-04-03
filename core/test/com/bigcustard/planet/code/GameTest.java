package com.bigcustard.planet.code;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImagePlus;
import com.sun.media.sound.FFT;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.FileFilter;
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
    @Mock private FileHandle mockCodeFile;
    @Mock private ImagePlus mockImage;
    @Mock private Syntax mockSyntax;
    @Mock private Consumer<Game> mockChangeListener;
    @Captor private ArgumentCaptor<Consumer<ImagePlus>> addImageListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<ImagePlus>> removeImageListenerCaptor;
    @Captor private ArgumentCaptor<Consumer<ImagePlus>> changeImageListenerCaptor;

    @Before
    public void before() {
        initMocks(this);
        when(mockGameFolder.child(Game.CODE_FILE)).thenReturn(mockCodeFile);
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
        Game game = newGame();
        game.setCode("code");
        when(mockSyntax.isValid("code")).thenReturn(true);
        when(mockImageModel.isValid()).thenReturn(true);
        assertThat(game.isValid(mockSyntax)).isTrue();
    }

    @Test
    public void isInvalidIfCodeInvalid() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame();
        game.setCode("code");
        when(mockSyntax.isValid("code")).thenReturn(false);
        when(mockImageModel.isValid()).thenReturn(true);
        assertThat(game.isValid(mockSyntax)).isFalse();
    }

    @Test
    public void isInvalidIfImagesInvalid() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame();
        game.setCode("code");
        when(mockSyntax.isValid("code")).thenReturn(true);
        when(mockImageModel.isValid()).thenReturn(false);
        assertThat(game.isValid(mockSyntax)).isFalse();
    }

    @Test
    public void createNewUsesTemplate() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame();
        assertThat(game.code()).isEqualTo(Game.TEMPLATE);
    }

    @Test
    public void createNewWhenDefaultNameNotInUse() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        when(mockGameFolder.exists()).thenReturn(false);
        Game game = newGame();
        assertThat(game.name()).isEqualTo("Unnamed Game");
    }

    @Test
    public void createNewWhenDefaultNameInUse() {
        FileHandle mockGameFolder2 = mock(FileHandle.class);
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockParentFolder.child("Unnamed Game 2")).thenReturn(mockGameFolder2);
        when(mockGameFolder2.child(Game.CODE_FILE)).thenReturn(mockCodeFile);
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder2.exists()).thenReturn(false);
        when(mockGameFolder2.name()).thenReturn("Unnamed Game 2");
        Game game = newGame();
        assertThat(game.name()).isEqualTo("Unnamed Game 2");
    }

    @Test
    public void createNewWhenDefaultNamesInUse() {
        FileHandle mockGameFolder2 = mock(FileHandle.class);
        FileHandle mockGameFolder3 = mock(FileHandle.class);
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockParentFolder.child("Unnamed Game 2")).thenReturn(mockGameFolder2);
        when(mockParentFolder.child("Unnamed Game 3")).thenReturn(mockGameFolder3);
        when(mockGameFolder2.child(Game.CODE_FILE)).thenReturn(mockCodeFile);
        when(mockGameFolder3.child(Game.CODE_FILE)).thenReturn(mockCodeFile);
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder2.exists()).thenReturn(true);
        when(mockGameFolder3.exists()).thenReturn(false);
        when(mockGameFolder3.name()).thenReturn("Unnamed Game 3");
        Game game = newGame();
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
        when(mockGameFolder.child(Game.CODE_FILE).exists()).thenReturn(false);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(Game.hasMostRecent(mockPreferences, mockParentFolder)).isFalse();
    }

    @Test
    public void hasRecentWhenInPrefsAndDirectoryAndCodeFileExist() {
        when(mockParentFolder.child("planet")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("planet");
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder.child(Game.CODE_FILE).exists()).thenReturn(true);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(Game.hasMostRecent(mockPreferences, mockParentFolder)).isTrue();
    }

    @Test
    public void changingGameNameRenamesDirectory() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        Game game = newGame();
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
        Game game = newGame();
        game.setName("name");
        verify(mockGameFolder.child(Game.CODE_FILE), times(2)).writeString(game.code(), false);
    }

    @Test(expected = GameRenameException.class)
    public void changingGameNameFailsIfTargetDirectoryExists() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        FileHandle mockNewGameFolder = mock(FileHandle.class);
        when(mockGameFolder.sibling("name")).thenReturn(mockNewGameFolder);
        when(mockNewGameFolder.exists()).thenReturn(true);
        Game game = newGame();
        when(mockGameFolder.exists()).thenReturn(true);
        game.setName("name");
    }

    @Test
    public void changingTextStoresCode() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        Game game = newGame();
        game.setCode("code");
        verify(mockGameFolder.child(Game.CODE_FILE)).writeString("code", false);
    }

    @Test
    public void addImageStoresImageModel() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        newGame();
        addImageListenerCaptor.getValue().accept(mockImage);
        verify(mockImageModel, times(2)).save();
    }

    @Test
    public void removeImageStoresImageModel() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        newGame();
        removeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockImageModel, times(2)).save();
    }

    @Test
    public void changeImageStoresImageModel() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        newGame();
        changeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockImageModel, times(2)).save();
    }

    @Test
    public void deleteRemovesFolder() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        Game game = newGame();
        game.delete();
        verify(mockGameFolder).deleteDirectory();
    }

    @Test
    public void isUnnamedWhenNew() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        assertThat(newGame().isNamed()).isFalse();
    }

    @Test
    public void isNamedAfterNameChange() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        when(mockGameFolder.name()).thenReturn("name");
        Game game = newGame();
        game.setName("name");
        assertThat(game.isNamed()).isTrue();
    }

    @Test
    public void mostRecentLoadsCode() {
        when(mockParentFolder.child("planet")).thenReturn(mockGameFolder);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder.name()).thenReturn("planet");
        when(mockGameFolder.child(Game.CODE_FILE).exists()).thenReturn(true);
        when(mockGameFolder.child(Game.CODE_FILE).readString()).thenReturn("code");
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
        assertThat(newGame().imageModel()).isSameAs(mockImageModel);
    }

    @Test
    public void notifiesOfCodeChange() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame();
        game.registerChangeListener(mockChangeListener);
        game.setCode("change");
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void notifiesOfImageAdd() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame();
        game.registerChangeListener(mockChangeListener);
        addImageListenerCaptor.getValue().accept(mockImage);
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void notifiesOfImageRemove() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame();
        game.registerChangeListener(mockChangeListener);
        removeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockChangeListener).accept(game);
    }

    @Test
    public void notifiesOfImageChange() {
        when(mockParentFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        Game game = newGame();
        game.registerChangeListener(mockChangeListener);
        changeImageListenerCaptor.getValue().accept(mockImage);
        verify(mockChangeListener).accept(game);
    }

    private Game newGame() {
        return Game.create(mockPreferences, mockParentFolder, mockImageModel);
    }

    private Game continueGame() {
        return Game.mostRecent(mockPreferences, mockParentFolder, mockImageModel);
    }
}
