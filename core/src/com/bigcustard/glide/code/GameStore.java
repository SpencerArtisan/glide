package com.bigcustard.glide.code;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.glide.code.language.Language;
import com.bigcustard.scene2dplus.image.ImageGroup;
import com.bigcustard.scene2dplus.sound.SoundGroup;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameStore {
    private static final String PREFERENCES_KEY = "Game";
    private static final String BUILD_FOLDER = "build";
    private static String CODE_FILE_WITHOUT_SUFFIX = "code";
    private static String USER_FOLDER = "mygames";
    private static String SAMPLES_FOLDER = "samples";
    private static final String RECENT_GAME = "MostRecentGameName";

    public Game.Token rename(Game.Token game, String newName) throws GameRenameException {
        if (!newName.equals(game.name())) {
            FileHandle source = game.gameFolder();
            FileHandle target = gameFolder(newName);
            if (target.exists()) {
                throw new GameRenameException(newName);
            }
            if (source.type() == Files.FileType.Local) {
                source.moveTo(target);
            } else {
                source.copyTo(target);
            }
            storeMostRecentGameName(newName);
            return new Game.Token(newName, game.language(), target);
        }
        return game;
    }

    public void delete(Game.Token game) {
        gameFolder(game.name()).deleteDirectory();
    }

    public void save(Game game) {
//        System.out.print("S");
        codeFile(game).writeString(game.code(), false);
        storeMostRecentGameName(game);
    }

    public Game create(Language language) {
        FileHandle gameFolder = findUniqueName();
        Game.Token token = new Game.Token(gameFolder.name(), language, gameFolder);
        return new Game(token, language.template(), new ImageGroup(gameFolder), new SoundGroup(gameFolder));
    }

    public boolean hasMostRecent() {
        String gameName = preferences().getString(RECENT_GAME);
        if (Strings.isNullOrEmpty(gameName)) return false;
        FileHandle gameFolder = userFolder().child(gameName);
        return gameFolder.exists() && gameFolder.list((dir, name) -> {
            return name.startsWith(CODE_FILE_WITHOUT_SUFFIX);
        }).length > 0;
    }

    public Game mostRecent() {
        FileHandle gameFolder = gameFolder(preferences().getString(RECENT_GAME));
        FileHandle codeFile = codeFile(gameFolder);
        ImageGroup imageGroup = new ImageGroup(gameFolder);
        SoundGroup soundGroup = new SoundGroup(gameFolder);
        Language language = Language.from(codeFile.extension());
        Game.Token token = new Game.Token(gameFolder.name(), language, gameFolder);
        return new Game(token, codeFile.readString(), imageGroup, soundGroup);
    }

    public List<Game.Token> allUserGames() {
        return allGames(userFolder());
    }

    public List<Game.Token> allSampleGames() {
        return allGames(samplesFolder());
    }

    public FileHandle buildFolder(Game game) {
        return gameFolder(game.name()).child(BUILD_FOLDER);
    }

    private FileHandle gameFolder(String gameName) {
        return userFolder().child(gameName);
    }

    private FileHandle codeFile(Game game) {
        return gameFolder(game.name()).child(codeFilename(game));
    }

    private FileHandle codeFile(FileHandle gameFolder) {
        FileHandle[] codeFiles = gameFolder.list((dir, name) -> {
            return name.startsWith(CODE_FILE_WITHOUT_SUFFIX);
        });
        return codeFiles[0];
    }

    public String codePathname(Game game) {
        return gameFolder(game.name()).path() + "/" + codeFilename(game);
    }

    private String codeFilename(Game game) {
        return CODE_FILE_WITHOUT_SUFFIX + "." + game.language().scriptEngine();
    }

    public FileHandle findUniqueName() {
        FileHandle candidate = userFolder().child(Game.DEFAULT_NAME);
        int suffix = 2;
        while (candidate.exists()) {
            candidate = userFolder().child(Game.DEFAULT_NAME + " " + suffix++);
        }
        return candidate;
    }

    private List<Game.Token> allGames(FileHandle gameFolder) {
        List<FileHandle> gameFolders = Arrays.asList(allGameFolders(gameFolder));
        return new ArrayList<>(Lists.transform(gameFolders, this::fromFolder));
    }

    private Game.Token fromFolder(FileHandle folder) {
        FileHandle codeFile = codeFile(folder);
        Language language = Language.from(codeFile.extension());
        return new Game.Token(folder.name(), language, folder);
    }

    private FileHandle[] allGameFolders(FileHandle parentFolder) {
        return parentFolder.list(file -> file.isDirectory() && !file.getName().startsWith("."));
    }

    private void storeMostRecentGameName(Game game) {
        if (game.isNamed()) {
            storeMostRecentGameName(game.name());
        }
    }

    private void storeMostRecentGameName(String name) {
        preferences().putString(RECENT_GAME, name);
        preferences().flush();
    }

    protected FileHandle samplesFolder() {
        return Gdx.files.internal(SAMPLES_FOLDER);
    }

    protected FileHandle userFolder() {
        return Gdx.files.local(USER_FOLDER);
    }

    protected Preferences preferences() {
        return Gdx.app.getPreferences(PREFERENCES_KEY);
    }

    public Game load(Game.Token token) {
        FileHandle codeFile = codeFile(token.gameFolder());
        ImageGroup imageGroup = new ImageGroup(token.gameFolder());
        SoundGroup soundGroup = new SoundGroup(token.gameFolder());
        return new Game(token, codeFile.readString(), imageGroup, soundGroup);
    }
}
