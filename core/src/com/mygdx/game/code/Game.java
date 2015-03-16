package com.mygdx.game.code;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.google.common.annotations.VisibleForTesting;
import com.mygdx.game.image.GameImage;
import com.mygdx.game.image.ImageAreaModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Game implements ImageAreaModel {
    private static final String RECENT_GAME = "MostRecentGameName";
    private static String FOLDER = "games";
    public static final String DEFAULT_NAME = "Unnamed Game";
    private static String CODE_FILE = "code.groovy";
    private static String IMAGE_DETAIL_FILE = "images.json";
    static String TEMPLATE =
                      "////////////////////////////////// \n"
                    + "// Welcome to Planet Burpl! \n"
                    + "// Start writing your game below. \n"
                    + "// Click here if you need help \n"
                    + "////////////////////////////////// \n\n";

    private String name;
    private String code;
    private List<GameImage> images = new ArrayList<>();

    private Preferences preferences;
    private Files files;
    private Function<String, InputStream> urlStreamProvider;

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
        String code = getCodeFile(name, files).readString();
        return new Game(name, code, streamProvider, preferences, files);
    }

    private Game(String name, String code, Function<String, InputStream> streamProvider, Preferences preferences, Files files) {
        this.urlStreamProvider = streamProvider;
        this.preferences = preferences;
        this.files = files;
        this.code = code;
        setName(name);
    }

    @Override
    public GameImage addImage(String url) {
        InputStream imageStream = urlStreamProvider.apply(url);
        try {
            FileHandle mainImageFile = generateImageFileHandle(url);
            mainImageFile.write(imageStream, false);
            imageStream.close();
            return addImage(new GameImage(mainImageFile));
        } catch (IOException e) {
            throw new InaccessibleUrlException(url, e);
        }
    }

    public GameImage addImage(GameImage gameImage) {
        images.add(gameImage);
        return gameImage;
    }

    public String code() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public List<GameImage> getImages() {
        return images;
    }

    public void save() {
        getCodeFile(name, files).writeString(code, false);
        getImageDetailFile().writeString(new Json().toJson(new GameDetails()), false);
    }

    private FileHandle getImageDetailFile() {
        return files.local(FOLDER + "/" + name + "/" + IMAGE_DETAIL_FILE);
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
        return files.local(gameFolder).exists() && getCodeFile(gameName, files).exists();
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

    private static InputStream defaultStreamProvider(String url) {
        try {
            return new URL(url).openStream();
        } catch (IOException e) {
            throw new InaccessibleUrlException(url, e);
        }
    }

    private FileHandle generateImageFileHandle(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        return files.local(FOLDER + "/" + name + "/" + filename);
    }


    private class GameDetails {
        private List<GameImageDetails> images = new ArrayList<>();

        private GameDetails() {
            for (GameImage image : Game.this.images) {
                images.add(new GameImageDetails(image));
            }
        }
    }

    private class GameImageDetails {
        private String filename;
        private String name;
        private int width;
        private int height;

        public GameImageDetails(GameImage image) {
            name = image.name();
            filename = image.filename();
            width = image.width();
            height = image.height();
        }
    }
}
