package com.mygdx.game.code;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.image.GameImage;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GameTest {
    private Preferences preferences;
    private Files files;
    private InputStream mockImageStream;
    private GameImage mockImage;

    @Before
    public void before() {
        preferences = mock(Preferences.class, RETURNS_DEEP_STUBS);
        files = mock(Files.class, RETURNS_DEEP_STUBS);
        mockImageStream = mock(InputStream.class);
        mockImage = mock(GameImage.class);
    }

    @Test
    public void createNewWhenDefaultNameNotInUse() {
        when(files.local("games/Unnamed Game").exists()).thenReturn(false);
        Game game = newGame();
        assertThat(game.name()).isEqualTo("Unnamed Game");
        assertThat(game.code()).isEqualTo(Game.TEMPLATE);
    }

    @Test
    public void createNewWhenDefaultNameInUse() {
        when(files.local("games/Unnamed Game").exists()).thenReturn(true);
        when(files.local("games/Unnamed Game/code.groovy").exists()).thenReturn(true);
        Game game = newGame();
        assertThat(game.name()).isEqualTo("Unnamed Game 2");
    }

    @Test
    public void createNewWhenDefaultNamesInUse() {
        when(files.local("games/Unnamed Game").exists()).thenReturn(true);
        when(files.local("games/Unnamed Game/code.groovy").exists()).thenReturn(true);
        when(files.local("games/Unnamed Game 2").exists()).thenReturn(true);
        when(files.local("games/Unnamed Game 2/code.groovy").exists()).thenReturn(true);
        when(files.local("games/Unnamed Game 3").exists()).thenReturn(true);
        when(files.local("games/Unnamed Game 3/code.groovy").exists()).thenReturn(true);
        Game game = newGame();
        assertThat(game.name()).isEqualTo("Unnamed Game 4");
    }

    @Test
    public void hasNoRecentWhenNotInPrefs() {
        when(preferences.getString("MostRecentGameName")).thenReturn(null);
        assertThat(Game.hasMostRecent(preferences, files)).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoDirectory() {
        when(files.local("games/game").exists()).thenReturn(false);
        when(preferences.getString("MostRecentGameName")).thenReturn("game");
        assertThat(Game.hasMostRecent(preferences, files)).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoCodeFile() {
        when(preferences.getString("MostRecentGameName")).thenReturn("game");
        when(files.local("games/game").exists()).thenReturn(true);
        when(files.local("games/game/code.groovy").exists()).thenReturn(false);
        assertThat(Game.hasMostRecent(preferences, files)).isFalse();
    }

    @Test
    public void hasRecentWhenInPrefsAndDirectoryAndCodeFileExist() {
        when(preferences.getString("MostRecentGameName")).thenReturn("game");
        when(files.local("games/game").exists()).thenReturn(true);
        when(files.local("games/game/code.groovy").exists()).thenReturn(true);
        assertThat(Game.hasMostRecent(preferences, files)).isTrue();
    }

    @Test
    public void changingGameNameRenamesDirectory() {
        when(files.local("games/Unnamed Game").exists()).thenReturn(true);
        Game game = newGame();
        game.setName("name");
        verify(files.local("games/Unnamed Game")).moveTo(files.local("games/name"));
    }

    @Test
    public void changingGameNameWhenNoSourceDirectoryDoesNotAtteptSourceRename() {
        when(files.local("games/Unnamed Game").exists()).thenReturn(false);
        Game game = newGame();
        game.setName("name");
        verify(files.local("games/Unnamed Game"), never()).moveTo(files.local("games/name"));
    }

    @Test(expected = GameRenameException.class)
    public void changingGameNameFailsIfTargetDirectoryExists() {
        Game game = newGame();
        when(files.local("games/name").exists()).thenReturn(true);
        game.setName("name");
        verify(files.local("games/Unnamed Game")).moveTo(files.local("games/name"));
    }

    @Test
    public void addImageFromUrl() {
        FileHandle mockFile = files.local("games/Unnamed Game/image.png");
        when(mockFile.name()).thenReturn("image.png");
        Game game = newGame();
        game.addImage("http://url/image.png");
        verify(mockFile).write(mockImageStream, false);
        assertThat(game.getImages()).extracting("name").containsExactly("image");
    }

    @Test
    public void saveStoresCode() {
        Game game = newGame();
        game.setCode("code");
        game.save();
        verify(files.local("games/Unnamed Game/code.groovy")).writeString("code", false);
    }

    @Test
    public void saveStoresImageDetails() {
        FileHandle mockFile = files.local("games/Unnamed Game/image.png");
        when(mockFile.name()).thenReturn("image.png");
        Game game = newGame();
        game.addImage(mockImage);
        when(mockImage.filename()).thenReturn("image.png");
        when(mockImage.name()).thenReturn("image");
        when(mockImage.width()).thenReturn(100);
        when(mockImage.height()).thenReturn(50);
        game.save();
        verify(files.local("games/Unnamed Game/manifest.json")).writeString("{images:[{filename:image.png,name:image,width:100,height:50}]}", false);
    }

    @Test
    public void mostRecentLoadsCode() {
        when(preferences.getString("MostRecentGameName")).thenReturn("game");
        when(files.local("games/game").exists()).thenReturn(true);
        when(files.local("games/game/code.groovy").exists()).thenReturn(true);
        when(files.local("games/game/code.groovy").readString()).thenReturn("code");
        when(files.local("games/game/manifest.json").readString()).thenReturn("{}");
        assertThat(continueGame().name()).isEqualTo("game");
        assertThat(continueGame().code()).isEqualTo("code");
    }

    @Test
    public void mostRecentLoadsImages() {
        when(preferences.getString("MostRecentGameName")).thenReturn("game");
        when(files.local("games/game").exists()).thenReturn(true);
        when(files.local("games/game/code.groovy").exists()).thenReturn(true);
        when(files.local("games/game/manifest.json").readString()).thenReturn("{images:[{filename:image.png,name:image,width:100,height:50}]}");
        assertThat(continueGame().getImages()).extracting("name").containsExactly("image");
    }

    private Game newGame() {
        return Game.create((url) -> mockImageStream, preferences, files);
    }

    private Game continueGame() {
        return Game.mostRecent((url) -> mockImageStream, preferences, files);
    }
}
