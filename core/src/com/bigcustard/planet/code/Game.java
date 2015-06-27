package com.bigcustard.planet.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.Notifier;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;

import java.util.function.Consumer;

public class Game {
    public static final String PREFERENCES_KEY = "Game";
    private static String CODE_FILE_WITHOUT_SUFFIX = "code";
    public static final String DEFAULT_NAME = "Unnamed Game";
    private static final String RECENT_GAME = "MostRecentGameName";
    private static String USER_FOLDER = "mygames";
    private static String SAMPLES_FOLDER = "samples";

    private Preferences preferences;
    private FileHandle gameFolder;
    private Notifier<Game> changeNotifier = new Notifier<>();

    private String code;
    private ImageAreaModel imageModel;
    private CommandHistory commandHistory;
    private RuntimeException runtimeError;
    private Language language;

    public static Game create(Language language) {
        return create(preferences(), userFolder(), new ImageAreaModel(), language);
    }

    public static Game mostRecent() {
        return mostRecent(preferences(), userFolder(), new ImageAreaModel());
    }

    public static Game from(FileHandle gameFolder) {
        if (gameFolder.parent().name().equals(samplesFolder().name())) {
            FileHandle myGamesCopy = findUniqueName(userFolder());
            gameFolder.copyTo(myGamesCopy);
            gameFolder = myGamesCopy;
        }
        return from(preferences(), gameFolder, new ImageAreaModel());
    }

    @VisibleForTesting
    static Game create(Preferences preferences, FileHandle parentFolder, ImageAreaModel imageModel, Language language) {
        FileHandle gameFolder = findUniqueName(parentFolder);
        return new Game(preferences, gameFolder, language.template(), imageModel, language);
    }

    @VisibleForTesting
    static Game mostRecent(Preferences preferences, FileHandle parentFolder, ImageAreaModel imageModel) {
        FileHandle gameFolder = parentFolder.child(preferences.getString(RECENT_GAME));
        return from(preferences, gameFolder, imageModel);
    }

    @VisibleForTesting
    static Game from(Preferences preferences, FileHandle gameFolder, ImageAreaModel imageModel) {
        FileHandle[] codeFiles = gameFolder.list((dir, name) -> {
            return name.startsWith(CODE_FILE_WITHOUT_SUFFIX);
        });
        FileHandle codeFile = codeFiles[0];
        return new Game(preferences, gameFolder, codeFile.readString(), imageModel, Language.from(codeFile.extension()));
    }

    private Game(Preferences preferences, FileHandle gameFolder, String code, ImageAreaModel imageAreaModel, Language language) {
        this.commandHistory = new CommandHistory();
        this.language = language;
        this.gameFolder = gameFolder;
        this.preferences = preferences;
        this.code = code;
        this.imageModel = imageAreaModel;
        this.imageModel.loadFromFolder(gameFolder);
        this.imageModel.registerAddImageListener((image) -> onImageChange());
        this.imageModel.registerRemoveImageListener((image) -> onImageChange());
        this.imageModel.registerChangeImageListener((image) -> onImageChange());
        preferences.putString(RECENT_GAME, name());
        save();
    }

    public Language language() {
        return language;
    }

    public CommandHistory getCommandHistory() {
        return commandHistory;
    }

    public static FileHandle[] allSampleGameFolders() {
        return allGameFolders(samplesFolder());
    }

    public static FileHandle[] allUserGameFolders() {
        return allGameFolders(userFolder());
    }

    @VisibleForTesting
    static FileHandle[] allGameFolders(FileHandle parentFolder) {
        return parentFolder.list(file -> file.isDirectory() && !file.getName().startsWith("."));
    }

    public void registerChangeListener(Consumer<Game> listener) {
        changeNotifier.add(listener);
    }

    public ImageAreaModel imageModel() {
        return imageModel;
    }

    public FileHandle folder() {
        return gameFolder;
    }

    public String code() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        changeNotifier.notify(this);
        save();
    }

    public String name() {
        return gameFolder.name();
    }

    public void setName(String newName) throws GameRenameException {
        if (isChangingName(newName)) {
            FileHandle source = gameFolder;
            FileHandle target = gameFolder.sibling(newName);
            if (target.exists()) {
                throw new GameRenameException(newName);
            }
            if (!source.exists()) {
                save();
            }
            source.moveTo(target);
            gameFolder = target;
            preferences.putString(RECENT_GAME, newName);
            preferences.flush();
        }
    }

    public void save() {
        gameFolder.child(codeFilename()).writeString(code, false);
        imageModel.save();
    }

    public String codeFilename() {
        return CODE_FILE_WITHOUT_SUFFIX + "." + language().scriptEngine();
    }

    public void delete() {
        gameFolder.deleteDirectory();
    }

    public boolean isNamed() {
        return !name().startsWith(DEFAULT_NAME);
    }

    public boolean isValid() {
        return language.isValid(code) && imageModel.isValid();
    }

    public static boolean hasMostRecent() {
        return hasMostRecent(preferences(), userFolder());
    }

    public void setRuntimeError(RuntimeException runtimeError) {
        this.runtimeError = runtimeError;
        changeNotifier.notify(this);
    }

    public String runtimeError() {
        try {
            return runtimeError == null ? null : runtimeError.getCause().getCause().getCause().getMessage();
        } catch (Exception e) {
            return runtimeError.getMessage();
        }
    }

    @VisibleForTesting
    static boolean hasMostRecent(Preferences preferences, FileHandle parentFolder) {
        String gameName = preferences.getString(RECENT_GAME);
        if (Strings.isNullOrEmpty(gameName)) return false;
        FileHandle gameFolder = parentFolder.child(gameName);
        return gameFolder.exists() && gameFolder.list((dir, name) -> {
            return name.startsWith(CODE_FILE_WITHOUT_SUFFIX);
        }).length > 0;
    }

    private static FileHandle findUniqueName(FileHandle parentFolder) {
        FileHandle candidate = parentFolder.child(DEFAULT_NAME);
        int suffix = 2;
        while (candidate.exists()) {
            candidate = parentFolder.child(DEFAULT_NAME + " " + suffix++);
        }
        return candidate;
    }

    private void onImageChange() {
        imageModel.save();
        changeNotifier.notify(this);
    }

    private static Preferences preferences() {
        return Gdx.app.getPreferences(PREFERENCES_KEY);
    }

    private static FileHandle samplesFolder() {
        return Gdx.files.internal(SAMPLES_FOLDER);
    }

    private static FileHandle userFolder() {
        return Gdx.files.local(USER_FOLDER);
    }

    private boolean isChangingName(String newName) {
        return !name().equals(newName);
    }

    public static Language language(FileHandle gameFolder) {
        FileHandle[] codeFiles = gameFolder.list((dir, name) -> {
            return name.startsWith(CODE_FILE_WITHOUT_SUFFIX);
        });
        FileHandle codeFile = codeFiles[0];
        return Language.from(codeFile.extension());

    }
}
