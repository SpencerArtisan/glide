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
    public static String CODE_FILE = "code.txt";
    public static final String DEFAULT_NAME = "Unnamed Game";
    public static String TEMPLATE =
                      "////////////////////////////////////////////// \n"
                    + "//         Welcome to Planet Burpl!         // \n"
                    + "//      Start writing your game below       // \n"
                    + "// Look in the Game Library for inspiration // \n"
                    + "////////////////////////////////////////////// \n\n";
    private static final String RECENT_GAME = "MostRecentGameName";
    private static String FOLDER = "games";

    private Preferences preferences;
    private FileHandle gameFolder;
    private Notifier<Game> changeNotifier = new Notifier<>();

    private String code;
    private ImageAreaModel imageModel;
    private CommandHistory commandHistory;

    public static Game create() {
        return create(preferences(), parentFolder(), new ImageAreaModel());
    }

    public static Game mostRecent() {
        return mostRecent(preferences(), parentFolder(), new ImageAreaModel());
    }

    public static Game from(FileHandle gameFolder) {
        return from(preferences(), gameFolder, new ImageAreaModel());
    }

    @VisibleForTesting
    static Game create(Preferences preferences, FileHandle parentFolder, ImageAreaModel imageModel) {
        FileHandle gameFolder = findUniqueName(parentFolder);
        String code = TEMPLATE;
        return new Game(preferences, gameFolder, code, imageModel);
    }

    @VisibleForTesting
    static Game mostRecent(Preferences preferences, FileHandle parentFolder, ImageAreaModel imageModel) {
        FileHandle gameFolder = parentFolder.child(preferences.getString(RECENT_GAME));
        return from(preferences, gameFolder, imageModel);
    }

    @VisibleForTesting
    static Game from(Preferences preferences, FileHandle gameFolder, ImageAreaModel imageModel) {
        String code = gameFolder.child(CODE_FILE).readString();
        return new Game(preferences, gameFolder, code, imageModel);
    }

    private Game(Preferences preferences, FileHandle gameFolder, String code, ImageAreaModel imageAreaModel) {
        this.commandHistory = new CommandHistory();
        this.gameFolder = gameFolder;
        this.preferences = preferences;
        this.code = code;
        this.imageModel = imageAreaModel;
        this.imageModel.loadFromFolder(gameFolder);
        this.imageModel.registerAddImageListener((image) -> onImageChange());
        this.imageModel.registerRemoveImageListener((image) -> onImageChange());
        this.imageModel.registerChangeImageListener((image) -> onImageChange());
        save();
    }

    public CommandHistory getCommandHistory() {
        return commandHistory;
    }

    public static FileHandle[] allGameFolders() {
        return allGameFolders(parentFolder());
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
        gameFolder.child(CODE_FILE).writeString(code, false);
        imageModel.save();
    }

    public void delete() {
        gameFolder.deleteDirectory();
    }

    public boolean isNamed() {
        return !name().startsWith(DEFAULT_NAME);
    }

    public boolean isValid(Syntax syntax) {
        return syntax.isValid(code) && imageModel.isValid();
    }

    public static boolean hasMostRecent() {
        return hasMostRecent(preferences(), parentFolder());
    }

    @VisibleForTesting
    static boolean hasMostRecent(Preferences preferences, FileHandle parentFolder) {
        String gameName = preferences.getString(RECENT_GAME);
        if (Strings.isNullOrEmpty(gameName)) return false;
        FileHandle gameFolder = parentFolder.child(gameName);
        return gameFolder.exists() && gameFolder.child(CODE_FILE).exists();
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

    private static FileHandle parentFolder() {
        return Gdx.files.local(FOLDER);
    }

    private boolean isChangingName(String newName) {
        return !name().equals(newName);
    }
}
