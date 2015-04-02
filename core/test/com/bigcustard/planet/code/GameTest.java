package com.bigcustard.planet.code;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImagePlus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class GameTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private Preferences mockPreferences;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private FileHandle mockParentFolder;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private ImageAreaModel mockImageModel;
    @Mock private ImagePlus mockImage;
    @Mock private Syntax mockSyntax;

    @Before
    public void before() {
        initMocks(this);
        when(mockParentFolder.child(anyString()).parent()).thenReturn(mockParentFolder);

    }

    @Test
    public void isValidIfCodeAndImagesValid() {
        Game game = newGame();
        game.setCode("code");
        when(mockSyntax.isValid("code")).thenReturn(true);
        when(mockImageModel.isValid()).thenReturn(true);
        assertThat(game.isValid(mockSyntax)).isTrue();
    }

    @Test
    public void isInvalidIfCodeInvalid() {
        Game game = newGame();
        game.setCode("code");
        when(mockSyntax.isValid("code")).thenReturn(false);
        when(mockImageModel.isValid()).thenReturn(true);
        assertThat(game.isValid(mockSyntax)).isFalse();
    }

    @Test
    public void isInvalidIfImagesInvalid() {
        Game game = newGame();
        game.setCode("code");
        when(mockSyntax.isValid("code")).thenReturn(true);
        when(mockImageModel.isValid()).thenReturn(false);
        assertThat(game.isValid(mockSyntax)).isFalse();
    }

    @Test
    public void createNewUsesTemplate() {
        Game game = newGame();
        assertThat(game.code()).isEqualTo(Game.TEMPLATE);
    }

    @Test
    public void createNewWhenDefaultNameNotInUse() {
        when(mockParentFolder.child("Unnamed Game").name()).thenReturn("Unnamed Game");
        when(mockParentFolder.child("Unnamed Game").exists()).thenReturn(false);
        Game game = newGame();
        assertThat(game.name()).isEqualTo("Unnamed Game");
    }

    @Test
    public void createNewWhenDefaultNameInUse() {
        when(mockParentFolder.child("Unnamed Game").exists()).thenReturn(true);
        when(mockParentFolder.child("Unnamed Game 2").exists()).thenReturn(false);
        when(mockParentFolder.child("Unnamed Game 2").name()).thenReturn("Unnamed Game 2");
        Game game = newGame();
        assertThat(game.name()).isEqualTo("Unnamed Game 2");
    }

    @Test
    public void createNewWhenDefaultNamesInUse() {
        when(mockParentFolder.child("Unnamed Game").exists()).thenReturn(true);
        when(mockParentFolder.child("Unnamed Game 2").exists()).thenReturn(true);
        when(mockParentFolder.child("Unnamed Game 3").exists()).thenReturn(false);
        when(mockParentFolder.child("Unnamed Game 3").name()).thenReturn("Unnamed Game 3");
        Game game = newGame();
        assertThat(game.name()).isEqualTo("Unnamed Game 3");
    }

    @Test
    public void hasNoRecentWhenNotInPrefs() {
        when(mockParentFolder.child("Unnamed Game").name()).thenReturn("Unnamed Game");
        when(mockPreferences.getString("MostRecentGameName")).thenReturn(null);
        assertThat(Game.hasMostRecent(mockPreferences, mockParentFolder)).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoDirectory() {
        when(mockParentFolder.child("planet").name()).thenReturn("planet");
        when(mockParentFolder.child("planet").exists()).thenReturn(false);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(Game.hasMostRecent(mockPreferences, mockParentFolder)).isFalse();
    }

    @Test
    public void hasNoRecentWhenInPrefsButNoCodeFile() {
        when(mockParentFolder.child("planet").name()).thenReturn("planet");
        when(mockParentFolder.child("planet").exists()).thenReturn(true);
        when(mockParentFolder.child("planet").child("code.groovy").exists()).thenReturn(false);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(Game.hasMostRecent(mockPreferences, mockParentFolder)).isFalse();
    }

    @Test
    public void hasRecentWhenInPrefsAndDirectoryAndCodeFileExist() {
        when(mockParentFolder.child("planet").name()).thenReturn("planet");
        when(mockParentFolder.child("planet").exists()).thenReturn(true);
        when(mockParentFolder.child("planet").child("code.groovy").exists()).thenReturn(true);
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        assertThat(Game.hasMostRecent(mockPreferences, mockParentFolder)).isTrue();
    }

    @Test
    public void changingGameNameRenamesDirectory() {
        when(mockParentFolder.child("Unnamed Game").name()).thenReturn("Unnamed Game");
        Game game = newGame();
        when(mockParentFolder.child("Unnamed Game").exists()).thenReturn(true);
        FileHandle newFolder = mockParentFolder.child("Unnamed Game").sibling("name");
        when(newFolder.exists()).thenReturn(false);
        game.setName("name");
        verify(mockParentFolder.child("Unnamed Game")).moveTo(newFolder);
    }

    @Test
    public void changingGameNameWhenNoGameDirectoryDoesNotAttemptSourceRename() {
        when(mockParentFolder.child("Unnamed Game").exists()).thenReturn(false);
        when(mockParentFolder.child("Unnamed Game").name()).thenReturn("Unnamed Game");
        FileHandle newFolder = mockParentFolder.child("Unnamed Game").sibling("name");
        when(newFolder.exists()).thenReturn(false);
        Game game = newGame();
        game.setName("name");
        verify(mockParentFolder.child("Unnamed Game"), never()).moveTo(newFolder);
    }

    @Test(expected = GameRenameException.class)
    public void changingGameNameFailsIfTargetDirectoryExists() {
        when(mockParentFolder.child("Unnamed Game").name()).thenReturn("Unnamed Game");
        FileHandle newFolder = mockParentFolder.child("Unnamed Game").sibling("name");
        when(newFolder.exists()).thenReturn(true);
        Game game = newGame();
        when(mockParentFolder.child("name").exists()).thenReturn(true);
        game.setName("name");
    }

    @Test
    public void changingTextStoresCode() {
        when(mockParentFolder.child("Unnamed Game").name()).thenReturn("Unnamed Game");
        Game game = newGame();
        game.setCode("code");
        verify(mockParentFolder.child("Unnamed Game").child("code.groovy")).writeString("code", false);
    }

    @Test
    public void deleteRemovesFolder() {
        when(mockParentFolder.child("Unnamed Game").name()).thenReturn("Unnamed Game");
        Game game = newGame();
        game.delete();
        verify(mockParentFolder.child("Unnamed Game")).deleteDirectory();
    }

    @Test
    public void isUnnamedWhenNew() {
        when(mockParentFolder.child("Unnamed Game").name()).thenReturn("Unnamed Game");
        assertThat(newGame().isNamed()).isFalse();
    }

    @Test
    public void isNamedAfterNameChange() {
        when(mockParentFolder.child("Unnamed Game").name()).thenReturn("Unnamed Game");
        when(mockParentFolder.child("name").name()).thenReturn("name");
        Game game = newGame();
        game.setName("name");
        assertThat(game.isNamed()).isTrue();
    }

    @Test
    public void mostRecentLoadsCode() {
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        when(mockParentFolder.child("planet").exists()).thenReturn(true);
        when(mockParentFolder.child("planet").name()).thenReturn("planet");
        when(mockParentFolder.child("planet").child("code.groovy").exists()).thenReturn(true);
        when(mockParentFolder.child("planet").child("code.groovy").readString()).thenReturn("code");
        assertThat(continueGame().name()).isEqualTo("planet");
        assertThat(continueGame().code()).isEqualTo("code");
    }

    @Test
    public void mostRecentLoadsImages() {
        when(mockPreferences.getString("MostRecentGameName")).thenReturn("planet");
        continueGame();
        verify(mockImageModel).loadFromFolder(mockParentFolder.child("planet"));
    }

    private Game newGame() {
        return Game.create(mockPreferences, mockParentFolder, mockImageModel);
    }

    private Game continueGame() {
        return Game.mostRecent(mockPreferences, mockParentFolder, mockImageModel);
    }
}
