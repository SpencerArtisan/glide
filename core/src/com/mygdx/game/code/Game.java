package com.mygdx.game.code;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.annotations.VisibleForTesting;
import com.mygdx.game.textarea.TextAreaModel;

public class Game {
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

    private static Preferences preferences;
    private static Files files;

    private String name;
    private String code;

    public static Game create() {
        return new Game(findUniqueName(), TEMPLATE);
    }

    public static Game mostRecent() {
        String name = preferences().getString(RECENT_GAME);
        return new Game(name, getCodeFile(name).readString());
    }

    private Game(String name, String code) {
        setName(name);
        this.code = code;
    }

    @VisibleForTesting
    static void setPreferences(Preferences preferences) {
        Game.preferences = preferences;
    }

    @VisibleForTesting
    static void files(Files files) {
        Game.files = files;
    }

    public static Preferences preferences() {
        return preferences == null ? Gdx.app.getPreferences(RECENT_GAME) : preferences;
    }

    public static Files files() {
        return files == null ? Gdx.files : files;
    }

    public String code() {
		return code;
	}

    public String name() {
        return name;
    }

    public void setName(String newName) throws GameRenameException {
        if (changingName(newName)) {
            FileHandle source = files().local(FOLDER + "/" + name);
            FileHandle target = files().local(FOLDER + "/" + newName);
            if (target.exists()) {
                throw new GameRenameException("Cannot name game " + newName + " as that name is taken!");
            }
            if (source.exists()) {
                source.moveTo(target);
            }
        }
        this.name = newName;
        preferences().putString(RECENT_GAME, name);
        preferences().flush();
    }

    public void save(TextAreaModel model) {
        FileHandle game = getCodeFile(name);
        game.writeString(model.getText(), false);
    }

    private static FileHandle getCodeFile(String name) {
        return files().local(FOLDER + "/" + name + "/" + CODE_FILE);
    }

    public String getGameFolder() {
        return FOLDER + "/" + name;
    }

    public static boolean hasMostRecent() {
        String gameName = preferences().getString(RECENT_GAME);
        return gameExists(gameName);
    }

    private static boolean gameExists(String gameName) {
        String gameFolder = FOLDER + "/" + gameName;
        if (files().local(gameFolder).exists()) {
            String codeFile = gameFolder + "/" + CODE_FILE;
            return files().local(codeFile).exists();
        }
        return false;
    }

    private static String findUniqueName() {
        String candidate = DEFAULT_NAME;
        int suffix = 2;
        while (gameExists(candidate)) {
            candidate = DEFAULT_NAME + " " + suffix++;
        }
        return candidate;
    }

    private boolean changingName(String name) {
        return this.name != null && !this.name.equals(name);
    }
}
