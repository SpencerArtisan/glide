package com.bigcustard.planet.code;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.image.ImagePlus;
import com.bigcustard.scene2dplus.image.ImageValidator;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GameTest {
    private Preferences mockPreferences;
    private Files mockFiles;
    private InputStream mockImageStream;
    private ImagePlus mockImage;
    private CodeRunner mockRunner;
    private ImageValidator mockValidator;

    @Before
    public void before() {
        mockPreferences = mock(Preferences.class, RETURNS_DEEP_STUBS);
        mockFiles = mock(Files.class, RETURNS_DEEP_STUBS);
        mockImageStream = mock(InputStream.class);
        mockImage = mock(ImagePlus.class);
        mockRunner = mock(CodeRunner.class);
        mockValidator = mock(ImageValidator.class);
    }

    @Test
    public void isValidIfCodeGood() {
        when(mockRunner.isValid("code")).thenReturn(true);
        when(mockValidator.isValid(Lists.emptyList())).thenReturn(true);
        Game game = newGame();
        game.setCode("code");
        assertThat(game.isValid()).isTrue();
    }

    @Test
    public void isInvalidIfCodeBad() {
        when(mockRunner.isValid("code")).thenReturn(false);
        when(mockValidator.isValid(Lists.emptyList())).thenReturn(true);
        Game game = newGame();
        game.setCode("code");
        assertThat(game.isValid()).isFalse();
    }

    @Test
    @Ignore
    public void isInvalidIfImagesBad() {
        FileHandle mockFile = mockFiles.local("games/Unnamed Game/image.png");
        when(mockRunner.isValid("code")).thenReturn(true);
        when(mockValidator.isValid(Arrays.asList(any(ImagePlus.class)))).thenReturn(false);
        when(mockFile.name()).thenReturn("image.png");
        Game game = newGame();
        game.addImage("http://url/image.png");
        game.setCode("code");
        assertThat(game.isValid()).isFalse();
    }

    @Test
    public void createNewWhenDefaultNameNotInUse() {
        when(mockFiles.local("games/Unnamed Game").exists()).thenReturn(false);
        Game game = newGame();
        assertThat(game.name()).isEqualTo("Unnamed Game");
        assertThat(game.code()).isEqualTo(Game.TEMPLATE);
    }

    @Test
    public void createNewWhenDefaultNameInUse() {
        when(mockFiles.local("games/Unnamed Game").exists()).thenReturn(true);
        when(mockFiles.local("games/Unnamed Game/code.groovy").exists()).thenReturn(true);
        Game game = newGame();
        assertThat(game.name()).isEqualTo("Unnamed Game 2");
    }

    @Test
    public void createNewWhenDefaultNamesInUse() {
        when(mockFiles.local("games/Unnamed Game").exists()).thenReturn(true);
        when(mockFiles.local("games/Unnamed Game/code.groovy").exists()).thenReturn(true);
        when(mockFiles.local("games/Unnamed Game 2").exists()).thenReturn(true);
        when(mockFiles.local("games/Unnamed Game 2/code.groovy").exists()).thenReturn(true);
        when(mockFiles.local("games/Unnamed Game 3").exists()).thenReturn(true);
        when(mockFiles.local("games/Unnamed Game 3/code.groovy").exists()).thenReturn(true);
        Game game = newGame();
        assertThat(game.name()).isEqualTo("Unnamed Game 4");
    }

    @Test
    public void hasNoRecentWhenNotInPrefs() {
        when(mockPreferences.getString("MostRecentGameName")).thenReturn(null);
        assertThat(Game.hasMostRecent(mockPreferences, mockFiles)).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoDirectory() {
        when(mockFiles.local("games/planet").exists()).thenReturn(false);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(Game.hasMostRecent(mockPreferences, mockFiles)).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoCodeFile() {
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        when(mockFiles.local("games/planet").exists()).thenReturn(true);
        when(mockFiles.local("games/planet/code.groovy").exists()).thenReturn(false);
        assertThat(Game.hasMostRecent(mockPreferences, mockFiles)).isFalse();
    }

    @Test
    public void hasRecentWhenInPrefsAndDirectoryAndCodeFileExist() {
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        when(mockFiles.local("games/planet").exists()).thenReturn(true);
        when(mockFiles.local("games/planet/code.groovy").exists()).thenReturn(true);
        assertThat(Game.hasMostRecent(mockPreferences, mockFiles)).isTrue();
    }

    @Test
    public void changingGameNameRenamesDirectory() {
        when(mockFiles.local("games/Unnamed Game").exists()).thenReturn(true);
        Game game = newGame();
        game.setName("name");
        verify(mockFiles.local("games/Unnamed Game")).moveTo(mockFiles.local("games/name"));
    }

    @Test
    public void changingGameNameWhenNoSourceDirectoryDoesNotAtteptSourceRename() {
        when(mockFiles.local("games/Unnamed Game").exists()).thenReturn(false);
        Game game = newGame();
        game.setName("name");
        verify(mockFiles.local("games/Unnamed Game"), never()).moveTo(mockFiles.local("games/name"));
    }

    @Test(expected = GameRenameException.class)
    public void changingGameNameFailsIfTargetDirectoryExists() {
        Game game = newGame();
        when(mockFiles.local("games/name").exists()).thenReturn(true);
        game.setName("name");
        verify(mockFiles.local("games/Unnamed Game")).moveTo(mockFiles.local("games/name"));
    }

    @Test
    public void addImageFromUrl() {
        FileHandle mockFile = mockFiles.local("games/Unnamed Game/image.png");
        when(mockFile.name()).thenReturn("image.png");
        Game game = newGame();
        game.addImage("http://url/image.png");
        verify(mockFile).write(mockImageStream, false);
        assertThat(game.getImages()).extracting("name").containsExactly("image");
    }

    @Test
    public void changingTextStoresCode() {
        Game game = newGame();
        game.setCode("code");
        verify(mockFiles.local("games/Unnamed Game/code.groovy")).writeString("code", false);
    }

    @Test
    public void deleteRemovesFolder() {
        Game game = newGame();
        game.delete();
        verify(mockFiles.local("games/Unnamed Game")).deleteDirectory();
    }

    @Test
    public void isUnnamedWhenNew() {
        assertThat(newGame().isNamed()).isFalse();
    }

    @Test
    public void isNamedAfterNameChange() {
        Game game = newGame();
        game.setName("name");
        assertThat(game.isNamed()).isTrue();
    }

    @Test
    public void saveStoresImageDetails() {
        FileHandle mockFile = mockFiles.local("games/Unnamed Game/image.png");
        when(mockFile.name()).thenReturn("image.png");
        Game game = newGame();
        game.addImage(mockImage);
        when(mockImage.filename()).thenReturn("image.png");
        when(mockImage.name()).thenReturn("image");
        when(mockImage.width()).thenReturn(100);
        when(mockImage.height()).thenReturn(50);
        game.save();
        verify(mockFiles.local("games/Unnamed Game/manifest.json")).writeString("{images:[{filename:image.png,name:image,width:100,height:50}]}", false);
    }

    @Test
    public void deleteRemovesImage() {
        when(mockImage.filename()).thenReturn("image.png");
        FileHandle mockFile = mockFiles.local("games/Unnamed Game/image.png");
        when(mockFile.name()).thenReturn("image.png");
        Game game = newGame();
        game.addImage(mockImage);
        game.deleteImage(mockImage);
        assertThat(game.getImages()).isEmpty();
        verify(mockFiles.local("games/Unnamed Game/image.png")).delete();
    }

    @Test
    public void mostRecentLoadsCode() {
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        when(mockFiles.local("games/planet").exists()).thenReturn(true);
        when(mockFiles.local("games/planet/code.groovy").exists()).thenReturn(true);
        when(mockFiles.local("games/planet/code.groovy").readString()).thenReturn("code");
        when(mockFiles.local("games/planet/manifest.json").readString()).thenReturn("{}");
        assertThat(continueGame().name()).isEqualTo("planet");
        assertThat(continueGame().code()).isEqualTo("code");
    }

    @Test
    public void mostRecentLoadsImages() {
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        when(mockFiles.local("games/planet").exists()).thenReturn(true);
        when(mockFiles.local("games/planet/code.groovy").exists()).thenReturn(true);
        when(mockFiles.local("games/planet/manifest.json").readString()).thenReturn("{images:[{filename:image.png,name:image,width:100,height:50}]}");
        assertThat(continueGame().getImages()).extracting("name").containsExactly("image");
    }

    private Game newGame() {
        return Game.create((url) -> mockImageStream, mockPreferences, mockFiles, mockRunner, mockValidator);
    }

    private Game continueGame() {
        return Game.mostRecent((url) -> mockImageStream, mockPreferences, mockFiles, mockRunner, mockValidator);
    }
}
