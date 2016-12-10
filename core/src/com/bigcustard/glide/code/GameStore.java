package com.bigcustard.glide.code;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.glide.code.language.Language;
import com.bigcustard.scene2dplus.image.ImageGroup;
import com.bigcustard.scene2dplus.sound.SoundGroup;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameStore {
    private static final String PREFERENCES_KEY = "Game";
    private static final String BUILD_FOLDER = "build";
    private static String CODE_FILE_WITHOUT_SUFFIX = "code";
    private static String TRASH_FOLDER = "trash";
    private static String USER_FOLDER = "mygames";
    private static String SIMPLE_SAMPLES_FOLDER = "samples/Level 1";
    private static String MEDIUM_SAMPLES_FOLDER = "samples/Level 2";
    private static String HARD_SAMPLES_FOLDER = "samples/Level 3";

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
            return new Game.Token(newName, game.language(), target);
        }
        return game;
    }

    public void delete(Game.Token game) {
        game.gameFolder().moveTo(findUniqueName(trashFolder(), game.name()));
    }

    public void save(Game game) {
        codeFile(game).writeString(game.code(), false);
    }

    public Game create(Language language) {
        FileHandle gameFolder = findUniqueName();
        Game.Token token = new Game.Token(gameFolder.name(), language, gameFolder);
        return new Game(token, language.template(), new ImageGroup(gameFolder), new SoundGroup(gameFolder));
    }

    public List<Game.Token> allUserGames() {
        return allGames(userFolder());
    }

    public List<Game.Token> allSimpleSampleGames() {
        return allGames(simpleSamplesFolder());
    }

    public List<Game.Token> allMediumSampleGames() {
        return allGames(mediumSamplesFolder());
    }

    public List<Game.Token> allHardSampleGames() {
        return allGames(hardSamplesFolder());
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
        FileHandle[] codeFiles = gameFolder.list((dir, name) -> name.startsWith(CODE_FILE_WITHOUT_SUFFIX));
        return codeFiles[0];
    }

    public String codePathname(Game game) {
        return gameFolder(game.name()).path() + "/" + codeFilename(game);
    }

    private String codeFilename(Game game) {
        return CODE_FILE_WITHOUT_SUFFIX + "." + game.language().scriptEngine();
    }

    public FileHandle findUniqueName() {
        return findUniqueName(userFolder(), Game.DEFAULT_NAME);
    }

    private FileHandle findUniqueName(FileHandle folder, String defaultName) {
        FileHandle candidate = folder.child(defaultName);
        int suffix = 2;
        while (candidate.exists()) {
            candidate = folder.child(defaultName + " " + suffix++);
        }
        return candidate;
    }

    public List<Game.Token> allGames(FileHandle gameFolder) {
        return Arrays.stream(allGameFolders(gameFolder))
                .map(folder -> {
                    try {
                        return fromFolder(folder);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Game.Token fromFolder(FileHandle folder) {
        FileHandle codeFile = codeFile(folder);
        Language language = Language.from(codeFile.extension());
        return new Game.Token(folder.name(), language, folder);
    }

    private FileHandle[] allGameFolders(FileHandle parentFolder) {
        return parentFolder.list(file -> file.isDirectory() && !file.getName().startsWith("."));
    }

    public FileHandle simpleSamplesFolder() {
        return Gdx.files.internal(SIMPLE_SAMPLES_FOLDER);
    }

    public FileHandle mediumSamplesFolder() {
        return Gdx.files.internal(MEDIUM_SAMPLES_FOLDER);
    }

    public FileHandle hardSamplesFolder() {
        return Gdx.files.internal(HARD_SAMPLES_FOLDER);
    }

    public FileHandle userFolder() {
        return Gdx.files.local(USER_FOLDER);
    }

    public FileHandle trashFolder() {
        return Gdx.files.local(TRASH_FOLDER);
    }

    public Game load(Game.Token token) {
        FileHandle codeFile = codeFile(token.gameFolder());
        ImageGroup imageGroup = new ImageGroup(token.gameFolder());
        SoundGroup soundGroup = new SoundGroup(token.gameFolder());
        return new Game(token, codeFile.readString(), imageGroup, soundGroup);
    }
}
