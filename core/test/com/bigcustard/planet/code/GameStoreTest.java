package com.bigcustard.planet.code;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
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
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class GameStoreTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private Preferences mockPreferences;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private ImageAreaModel mockImageModel;
    @Mock private FileHandle mockGameFolder;
    @Mock private FileHandle mockSamplesFolder;
    @Mock private FileHandle mockUserGamesFolder;
    @Mock private FileHandle mockGroovyCodeFile;
    @Mock private FileHandle mockManifestFile;
    @Mock private Language mockLanguage;
    private GameStore gameStore;

    @Before
    public void before() {
        initMocks(this);
        when(mockLanguage.scriptEngine()).thenReturn("groovy");
        when(mockGameFolder.child("code.groovy")).thenReturn(mockGroovyCodeFile);
        when(mockGameFolder.name()).thenReturn("game");
        when(mockGameFolder.child("manifest.json")).thenReturn(mockManifestFile);
        when(mockManifestFile.exists()).thenReturn(false);
        when(mockGroovyCodeFile.extension()).thenReturn("groovy");
        when(mockGroovyCodeFile.readString()).thenReturn("code");
        gameStore = new GameStore() {
            @Override
            protected FileHandle samplesFolder() {
                return mockSamplesFolder;
            }

            @Override
            protected FileHandle userFolder() {
                return mockUserGamesFolder;
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
        assertThat(gameStore.allSampleGames()).isEmpty();
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

        Game game = new Game("game", "code", Language.Groovy, new ImageAreaModel(mockGameFolder));
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
        when(mockGameFolder2.child("manifest.json")).thenReturn(mockManifestFile);
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
        when(mockGameFolder3.child("manifest.json")).thenReturn(mockManifestFile);
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
    public void changingGameNameRenamesDirectory() {
        when(mockUserGamesFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        Game game = gameStore.create(mockLanguage);
        when(mockGameFolder.exists()).thenReturn(true);
        FileHandle mockNewGameFolder = mock(FileHandle.class);
        when(mockUserGamesFolder.child("name")).thenReturn(mockNewGameFolder);
        when(mockNewGameFolder.exists()).thenReturn(false);
        gameStore.rename(game, "name");
        verify(mockGameFolder).moveTo(mockNewGameFolder);
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
        gameStore.rename(game, "name");
    }

    @Test
    public void deleteRemovesFolder() {
        when(mockUserGamesFolder.child("Unnamed Game")).thenReturn(mockGameFolder);
        when(mockGameFolder.name()).thenReturn("Unnamed Game");
        Game game = gameStore.create(mockLanguage);
        gameStore.delete(game);
        verify(mockGameFolder).deleteDirectory();
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
        gameStore.rename(game, "name");
        assertThat(game.isNamed()).isTrue();
    }
}