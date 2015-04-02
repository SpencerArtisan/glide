package com.bigcustard.planet.code;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImagePlus;
import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final String PREFERENCES_KEY = "Game";
    private static final String RECENT_GAME = "MostRecentGameName";
    public static final String DEFAULT_NAME = "Unnamed Game";
    private static String FOLDER = "games";
    private static String CODE_FILE = "code.groovy";
    static String TEMPLATE =
                      "////////////////////////////////// \n"
                    + "// Welcome to Planet Burpl! \n"
                    + "// Start writing your game below. \n"
                    + "// Click here if you need help \n"
                    + "////////////////////////////////// \n\n";

    private String code;
    private Preferences preferences;
    private List<Runnable> changeListeners = new ArrayList<>();
    private ImageAreaModel images;
    private FileHandle gameFolder;

    public static Game create() {
        return create(Gdx.app.getPreferences(PREFERENCES_KEY), Gdx.files.local(FOLDER), new ImageAreaModel());
    }

    public static Game mostRecent() {
        return mostRecent(Gdx.app.getPreferences(PREFERENCES_KEY), Gdx.files.local(FOLDER), new ImageAreaModel());
    }

    public static Game from(FileHandle gameFolder) {
        return from(Gdx.app.getPreferences(PREFERENCES_KEY), gameFolder, new ImageAreaModel());
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
        this.images = imageAreaModel;
        this.images.loadFromFolder(gameFolder);
        this.gameFolder = gameFolder;
        this.preferences = preferences;
        this.code = code;
        for (ImagePlus image : images.images()) {
            image.registerChangeListener(this::informChangeListeners);
        }
    }

    public static FileHandle[] allGameFolders(Files files) {
        return files.local(FOLDER).list(file -> file.isDirectory() && !file.getName().startsWith("."));
    }

    public ImageAreaModel images() {
        return images;
    }

    public void registerChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    public String code() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        informChangeListeners();
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
                throw new GameRenameException("Cannot name planet " + newName + " as that name is taken!");
            }
            if (source.exists()) {
                source.moveTo(target);
            }
            gameFolder = target;
            preferences.putString(RECENT_GAME, newName);
            preferences.flush();
        }
    }


    public void save() {
        gameFolder.child(CODE_FILE).writeString(code, false);
        images.save();
    }

    public void delete() {
        gameFolder.deleteDirectory();
    }

    public boolean isNamed() {
        return !name().startsWith(DEFAULT_NAME);
    }

    public boolean isValid(Syntax syntax) {
        return syntax.isValid(code) && images.isValid();
    }

    private void informChangeListeners() {
        for (Runnable listener : changeListeners) {
            listener.run();
        }
    }

    public static boolean hasMostRecent() {
        return hasMostRecent(Gdx.app.getPreferences(PREFERENCES_KEY), Gdx.app.getFiles().local(FOLDER));
    }

    static boolean hasMostRecent(Preferences preferences, FileHandle parentFolder) {
        String gameName = preferences.getString(RECENT_GAME);
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

    private boolean isChangingName(String newName) {
        return this.name() != null && !this.name().equals(newName);
    }
}
