package com.mygdx.game.code;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Preferences;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GameTest {
    private Preferences preferences;
    private Files files;

    @Before
    public void before() {
        preferences = mock(Preferences.class, RETURNS_DEEP_STUBS);
        files = mock(Files.class, RETURNS_DEEP_STUBS);
        Game.setPreferences(preferences);
        Game.files(files);
    }

    @Test
    public void createNewWhenDefaultNameNotInUse() {
        when(files.local("games/Unnamed Game").exists()).thenReturn(false);
        Game game = Game.create();
        assertThat(game.name()).isEqualTo("Unnamed Game");
        assertThat(game.code()).isEqualTo(Game.TEMPLATE);
    }

    @Test
    public void createNewWhenDefaultNameInUse() {
        when(files.local("games/Unnamed Game").exists()).thenReturn(true);
        when(files.local("games/Unnamed Game/code.groovy").exists()).thenReturn(true);
        Game game = Game.create();
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
        Game game = Game.create();
        assertThat(game.name()).isEqualTo("Unnamed Game 4");
    }

    @Test
    public void hasNoRecentWhenNotInPrefs() {
        when(preferences.getString("MostRecentGameName")).thenReturn(null);
        assertThat(Game.hasMostRecent()).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoDirectory() {
        when(files.local("games/game").exists()).thenReturn(false);
        when(preferences.getString("MostRecentGameName")).thenReturn("game");
        assertThat(Game.hasMostRecent()).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoCodeFile() {
        when(preferences.getString("MostRecentGameName")).thenReturn("game");
        when(files.local("games/game").exists()).thenReturn(true);
        when(files.local("games/game/code.groovy").exists()).thenReturn(false);
        assertThat(Game.hasMostRecent()).isFalse();
    }

    @Test
    public void hasRecentWhenInPrefsAndDirectoryAndCodeFileExist() {
        when(preferences.getString("MostRecentGameName")).thenReturn("game");
        when(files.local("games/game").exists()).thenReturn(true);
        when(files.local("games/game/code.groovy").exists()).thenReturn(true);
        assertThat(Game.hasMostRecent()).isTrue();
    }

    @Test
    public void mostRecentLoadsCode() {
        when(preferences.getString("MostRecentGameName")).thenReturn("game");
        when(files.local("games/game").exists()).thenReturn(true);
        when(files.local("games/game/code.groovy").exists()).thenReturn(true);
        when(files.local("games/game/code.groovy").readString()).thenReturn("code");
        assertThat(Game.mostRecent().name()).isEqualTo("game");
        assertThat(Game.mostRecent().code()).isEqualTo("code");
    }

    @Test
    public void changingGameNameRenamesDirectory() {
        when(files.local("games/Unnamed Game").exists()).thenReturn(true);
        Game game = Game.create();
        game.setName("name");
        verify(files.local("games/Unnamed Game")).moveTo(files.local("games/name"));
    }

    @Test(expected = GameRenameException.class)
    public void changingGameFailsIfTargetDirectoryExists() {
        when(files.local("games/Unnamed Game").exists()).thenReturn(false);
        Game game = Game.create();
        when(files.local("games/name").exists()).thenReturn(true);
        game.setName("name");
        verify(files.local("games/Unnamed Game")).moveTo(files.local("games/name"));
    }
}
