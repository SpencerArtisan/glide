package com.mygdx.game.code;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.annotations.VisibleForTesting;
import com.mygdx.game.image.GameImage;
import com.mygdx.game.image.ImageAreaModel;
import com.mygdx.game.textarea.TextAreaModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Game implements ImageAreaModel {
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

    private Preferences preferences;
    private Files files;
    private String name;
    private String code;
    private Function<String, InputStream> streamProvider;
    private List<GameImage> images = new ArrayList<>();

    public static Game create() {
        return create(Game::defaultStreamProvider, Gdx.app.getPreferences("Planet"), Gdx.files);
    }

    public static Game mostRecent() {
        return mostRecent(Game::defaultStreamProvider, Gdx.app.getPreferences("Planet"), Gdx.files);
    }

    @VisibleForTesting
    static Game create(Function<String, InputStream> streamProvider, Preferences preferences, Files files) {
        return new Game(findUniqueName(files), TEMPLATE, streamProvider, preferences, files);
    }

    @VisibleForTesting
    static Game mostRecent(Function<String, InputStream> streamProvider, Preferences preferences, Files files) {
        String name = preferences.getString(RECENT_GAME);
        return new Game(name, getCodeFile(name, files).readString(), streamProvider, preferences, files);
    }

    private Game(String name, String code, Function<String, InputStream> streamProvider, Preferences preferences, Files files) {
        this.streamProvider = streamProvider;
        this.preferences = preferences;
        this.files = files;
        this.code = code;
        setName(name);
    }

    @Override
    public GameImage addImage(String url) {
        InputStream imageStream = streamProvider.apply(url);
        try {
            FileHandle mainImageFile = files.local(generateImagePath(url));
            mainImageFile.write(imageStream, false);
            GameImage gameImage = new GameImage(mainImageFile);
            images.add(gameImage);
            imageStream.close();
            return gameImage;
        } catch (IOException e) {
            throw new InaccessibleUrlException(url, e);
        }
    }

    public String code() {
        return code;
    }

    public String name() {
        return name;
    }

    public void setName(String newName) throws GameRenameException {
        if (isChangingName(newName)) {
            FileHandle source = files.local(FOLDER + "/" + name);
            FileHandle target = files.local(FOLDER + "/" + newName);
            if (target.exists()) {
                throw new GameRenameException("Cannot name game " + newName + " as that name is taken!");
            }
            if (source.exists()) {
                source.moveTo(target);
            }
        }
        this.name = newName;
        preferences.putString(RECENT_GAME, name);
        preferences.flush();
    }

    // todo
    public void save(TextAreaModel model) {
        save(model, Gdx.files);
    }

    public void save(TextAreaModel model, Files files) {
        FileHandle game = getCodeFile(name, files);
        game.writeString(model.getText(), false);
    }

    private static FileHandle getCodeFile(String name, Files files) {
        return files.local(FOLDER + "/" + name + "/" + CODE_FILE);
    }

    public static boolean hasMostRecent(Preferences preferences, Files files) {
        String gameName = preferences.getString(RECENT_GAME);
        return gameExists(gameName, files);
    }

    private static boolean gameExists(String gameName, Files files) {
        String gameFolder = FOLDER + "/" + gameName;
        if (files.local(gameFolder).exists()) {
            String codeFile = gameFolder + "/" + CODE_FILE;
            return files.local(codeFile).exists();
        }
        return false;
    }

    private static String findUniqueName(Files files) {
        String candidate = DEFAULT_NAME;
        int suffix = 2;
        while (gameExists(candidate, files)) {
            candidate = DEFAULT_NAME + " " + suffix++;
        }
        return candidate;
    }

    private boolean isChangingName(String name) {
        return this.name != null && !this.name.equals(name);
    }

    public List<GameImage> getImages() {
        return images;
    }

    private String generateImagePath(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        return FOLDER + "/" + name + "/" + filename;
    }

    private static InputStream defaultStreamProvider(String url) {
        try {
            return new URL(url).openStream();
        } catch (IOException e) {
            throw new InaccessibleUrlException(url, e);
        }
    }
}
