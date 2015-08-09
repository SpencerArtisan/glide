package com.bigcustard.planet.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.planet.code.language.Language;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
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

    public void rename(Game game, String newName) throws GameRenameException {
        if (!newName.equals(game.name())) {
            FileHandle source = gameFolder(game.name());
            FileHandle target = gameFolder(newName);
            if (target.exists()) {
                throw new GameRenameException(newName);
            }
            if (!source.exists()) {
                save(game);
            }
            source.moveTo(target);
            game.name(newName);
            game.imageModel().folder(target);
            storeMostRecentGameName(game);
        }
    }

    public void delete(Game game) {
        gameFolder(game.name()).deleteDirectory();
    }

    public void save(Game game) {
        codeFile(game).writeString(game.code(), false);
        storeMostRecentGameName(game);
    }

    public Game create(Language language) {
        FileHandle gameFolder = findUniqueName();
        return new Game(gameFolder.name(), language.template(), language, new ImageAreaModel(gameFolder));
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
        ImageAreaModel imageAreaModel = new ImageAreaModel(gameFolder);
        Language language = Language.from(codeFile.extension());
        return new Game(gameFolder.name(), codeFile.readString(), language, imageAreaModel);
    }

    public List<Game> allUserGames() {
        return allGames(userFolder());
    }

    public List<Game> allSampleGames() {
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

    private List<Game> allGames(FileHandle gameFolder) {
        List<FileHandle> gameFolders = Arrays.asList(allGameFolders(gameFolder));
        return new ArrayList<>(Lists.transform(gameFolders, this::fromFolder));
    }

    private Game fromFolder(FileHandle folder) {
        FileHandle codeFile = codeFile(folder);
        Language language = Language.from(codeFile.extension());
        ImageAreaModel imageAreaModel = new ImageAreaModel(folder);
        return new Game(folder.name(), codeFile.readString(), language, imageAreaModel);
    }

    private FileHandle[] allGameFolders(FileHandle parentFolder) {
        return parentFolder.list(file -> file.isDirectory() && !file.getName().startsWith("."));
    }

    private void storeMostRecentGameName(Game game) {
        if (game.isNamed()) {
            preferences().putString(RECENT_GAME, game.name());
            preferences().flush();
        }
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
}
