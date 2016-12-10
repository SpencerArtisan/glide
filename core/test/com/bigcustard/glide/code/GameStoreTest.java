package com.bigcustard.glide.code;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.glide.code.language.Language;
import com.bigcustard.scene2dplus.image.ImageGroup;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GameStoreTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private Preferences mockPreferences;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private ImageGroup mockImageModel;
    @Mock private FileHandle mockGameFolder;
    @Mock private FileHandle mockTrashFolder;
    @Mock private FileHandle mockSamplesFolder;
    @Mock private FileHandle mockUserGamesFolder;
    @Mock private FileHandle mockGroovyCodeFile;
    @Mock private FileHandle mockManifestFile;
    @Mock private FileHandle mockSoundsFile;
    @Mock private Language mockLanguage;
    private GameStore gameStore;

    @Before
    public void before() {
        initMocks(this);
        when(mockGameFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[0]);
        when(mockLanguage.scriptEngine()).thenReturn("groovy");
        when(mockGameFolder.child("code.groovy")).thenReturn(mockGroovyCodeFile);
        when(mockGameFolder.name()).thenReturn("game");
        when(mockGameFolder.child("images.json")).thenReturn(mockManifestFile);
        when(mockGameFolder.child("sounds.json")).thenReturn(mockSoundsFile);
        when(mockManifestFile.exists()).thenReturn(false);
        when(mockGroovyCodeFile.extension()).thenReturn("groovy");
        when(mockGroovyCodeFile.readString()).thenReturn("code");
        gameStore = new GameStore() {
            @Override
            public FileHandle simpleSamplesFolder() {
                return mockSamplesFolder;
            }

            @Override
            public FileHandle userFolder() {
                return mockUserGamesFolder;
            }

            @Override
            public FileHandle trashFolder() {
                return mockTrashFolder;
            }

            @Override
            protected Preferences preferences() {
                return mockPreferences;
            }
        };
    }

    @Test
    public void allSampleGamesWhenDirectoryEmpty() {
        when(mockSamplesFolder.list(any(FileFilter.class))).thenReturn(new FileHandle[0]);
        assertThat(gameStore.allSimpleSampleGames()).isEmpty();
    }

    @Test
    public void allUserGamesWhenDirectoryEmpty() {
        when(mockUserGamesFolder.list(any(FileFilter.class))).thenReturn(new FileHandle[0]);
        assertThat(gameStore.allUserGames()).isEmpty();
    }
    
    @Test
    public void allUserGamesWhenDirectoryContainsGameFolder() {
        ArgumentCaptor<FileFilter> filterCaptor = ArgumentCaptor.forClass(FileFilter.class);
        File mockGameFolderAsFile = mock(File.class);
        when(mockGameFolder.file()).thenReturn(mockGameFolderAsFile);
        when(mockGameFolderAsFile.getName()).thenReturn("game");
        when(mockGameFolderAsFile.isDirectory()).thenReturn(true);

        when(mockUserGamesFolder.list(filterCaptor.capture())).thenAnswer(invocation ->
                Arrays.asList(mockGameFolder)
                        .stream()
                        .filter((folder) -> filterCaptor.getValue().accept(folder.file()))
                        .toArray(FileHandle[]::new));
        when(mockGameFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[] {mockGroovyCodeFile});
        when(mockGroovyCodeFile.name()).thenReturn("code.groovy");

        Game.Token game = new Game.Token("game", Language.Groovy, mockGameFolder);
        assertThat(gameStore.allUserGames()).containsExactly(game);
    }

    @Test
    public void allGameFoldersWhenDirectoryContainsFolderStartingWithDot() {
        ArgumentCaptor<FileFilter> filterCaptor = ArgumentCaptor.forClass(FileFilter.class);
        File mockGameFolderAsFile = mock(File.class);
        when(mockGameFolder.file()).thenReturn(mockGameFolderAsFile);
        when(mockGameFolderAsFile.getName()).thenReturn(".game");
        when(mockGameFolderAsFile.isDirectory()).thenReturn(false);

        when(mockUserGamesFolder.list(filterCaptor.capture())).thenAnswer(invocation ->
                Arrays.asList(mockGameFolder)
                        .stream()
                        .filter((folder) -> filterCaptor.getValue().accept(folder.file()))
                        .toArray(FileHandle[]::new));
        assertThat(gameStore.allUserGames()).isEmpty();
    }

    @Test
    public void createNewUsesTemplate() {
        when(mockUserGamesFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockLanguage.template()).thenReturn("template");
        Game game = gameStore.create(mockLanguage);
        assertThat(game.code()).isEqualTo("template");
    }

    @Test
    public void createNewWhenDefaultNameNotInUse() {
        when(mockUserGamesFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        when(mockGameFolder.exists()).thenReturn(false);
        Game game = gameStore.create(mockLanguage);
        assertThat(game.name()).isEqualTo("Unnamed Game");
    }

    @Test
    public void createNewWhenDefaultNameInUse() {
        FileHandle mockGameFolder2 = mock(FileHandle.class);
        when(mockUserGamesFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockUserGamesFolder.child("Unnamed Game 2")).thenReturn(mockGameFolder2);
        when(mockGameFolder2.child("code.groovy")).thenReturn(mockGroovyCodeFile);
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder2.exists()).thenReturn(false);
        when(mockGameFolder2.name()).thenReturn("Unnamed Game 2");
        when(mockGameFolder2.child("images.json")).thenReturn(mockManifestFile);
        when(mockGameFolder2.child("sounds.json")).thenReturn(mockSoundsFile);
        when(mockGameFolder2.list(any(FilenameFilter.class))).thenReturn(new FileHandle[0]);
        Game game = gameStore.create(mockLanguage);
        assertThat(game.name()).isEqualTo("Unnamed Game 2");
    }

    @Test
    public void createNewWhenDefaultNamesInUse() {
        FileHandle mockGameFolder2 = mock(FileHandle.class);
        FileHandle mockGameFolder3 = mock(FileHandle.class);
        when(mockUserGamesFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockUserGamesFolder.child("Unnamed Game 2")).thenReturn(mockGameFolder2);
        when(mockUserGamesFolder.child("Unnamed Game 3")).thenReturn(mockGameFolder3);
        when(mockGameFolder2.child("code.groovy")).thenReturn(mockGroovyCodeFile);
        when(mockGameFolder3.child("code.groovy")).thenReturn(mockGroovyCodeFile);
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder2.exists()).thenReturn(true);
        when(mockGameFolder3.exists()).thenReturn(false);
        when(mockGameFolder3.name()).thenReturn("Unnamed Game 3");
        when(mockGameFolder3.child("images.json")).thenReturn(mockManifestFile);
        when(mockGameFolder3.child("sounds.json")).thenReturn(mockSoundsFile);
        when(mockGameFolder3.list(any(FilenameFilter.class))).thenReturn(new FileHandle[0]);
        Game game = gameStore.create(mockLanguage);
        assertThat(game.name()).isEqualTo("Unnamed Game 3");
    }

    @Test
    public void hasNoRecentWhenNotInPrefs() {
        when(mockUserGamesFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        when(mockPreferences.getString("MostRecentGameName")).thenReturn(null);
        assertThat(gameStore.hasMostRecent()).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoDirectory() {
        when(mockUserGamesFolder.child("planet")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("planet");
        when(mockGameFolder.exists()).thenReturn(false);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(gameStore.hasMostRecent()).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoCodeFile() {
        when(mockUserGamesFolder.child("planet")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("planet");
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[0]);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(gameStore.hasMostRecent()).isFalse();
    }

    @Test
    public void hasRecentWhenInPrefsAndDirectoryAndCodeFileExist() {
        when(mockUserGamesFolder.child("planet")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("planet");
        when(mockGameFolder.exists()).thenReturn(true);
        when(mockGameFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[] {mockGroovyCodeFile});
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(gameStore.hasMostRecent()).isTrue();
    }

    @Test
    public void changingGameNameOfUserGameMovesDirectory() {
        when(mockGameFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[] {mockGroovyCodeFile});
        Game.Token token = new Game.Token("old name", mockLanguage, mockGameFolder);
        Game game = gameStore.load(token);
        when(mockGameFolder.type()).thenReturn(Files.FileType.Local);
        when(mockGameFolder.exists()).thenReturn(true);
        FileHandle mockNewGameFolder = mock(FileHandle.class);
        when(mockUserGamesFolder.child("new name")).thenReturn(mockNewGameFolder);
        when(mockNewGameFolder.exists()).thenReturn(false);
        gameStore.rename(game.token(), "new name");
        verify(mockGameFolder).moveTo(mockNewGameFolder);
    }

    @Test
    public void changingGameNameOfSampleGameCopiesDirectory() {
        when(mockGameFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[] {mockGroovyCodeFile});
        Game.Token token = new Game.Token("old name", mockLanguage, mockGameFolder);
        Game game = gameStore.load(token);
        when(mockGameFolder.type()).thenReturn(Files.FileType.Internal);
        when(mockGameFolder.exists()).thenReturn(true);
        FileHandle mockNewGameFolder = mock(FileHandle.class);
        when(mockUserGamesFolder.child("new name")).thenReturn(mockNewGameFolder);
        when(mockNewGameFolder.exists()).thenReturn(false);
        gameStore.rename(game.token(), "new name");
        verify(mockGameFolder).copyTo(mockNewGameFolder);
    }

    @Test(expected = GameRenameException.class)
    public void changingGameNameFailsIfTargetDirectoryExists() {
        when(mockUserGamesFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        FileHandle mockNewGameFolder = mock(FileHandle.class);
        when(mockUserGamesFolder.child("name")).thenReturn(mockNewGameFolder);
        when(mockNewGameFolder.exists()).thenReturn(true);
        Game game = gameStore.create(mockLanguage);
        when(mockGameFolder.exists()).thenReturn(true);
        gameStore.rename(game.token(), "name");
    }

    @Test
    public void deleteMovesFolderToTrash() {
        when(mockGameFolder.list(any(FilenameFilter.class))).thenReturn(new FileHandle[] {mockGroovyCodeFile});
        Game.Token token = new Game.Token("name", mockLanguage, mockGameFolder);
        Game game = gameStore.load(token);
        when(mockGameFolder.type()).thenReturn(Files.FileType.Local);
        when(mockGameFolder.exists()).thenReturn(true);
        FileHandle mockNewGameFolder = mock(FileHandle.class);
        when(mockTrashFolder.child("name")).thenReturn(mockNewGameFolder);
        when(mockNewGameFolder.exists()).thenReturn(false);
        when(mockUserGamesFolder.child("name")).thenReturn(mockGameFolder);

        gameStore.delete(game.token());
        verify(mockGameFolder).moveTo(mockNewGameFolder);
    }

    @Test
    public void isUnnamedWhenNew() {
        when(mockUserGamesFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        assertThat(gameStore.create(mockLanguage).isNamed()).isFalse();
    }

    @Test
    public void isNamedAfterNameChange() {
        when(mockUserGamesFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        when(mockGameFolder.name()).thenReturn("name");
        Game game = gameStore.create(mockLanguage);
        gameStore.rename(game.token(), "name");
        assertThat(game.isNamed()).isTrue();
    }
}
