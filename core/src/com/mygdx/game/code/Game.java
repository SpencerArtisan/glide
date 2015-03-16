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
    private static String GAME_DETAIL_FILE = "manifest.json";
    static String TEMPLATE =
                      "////////////////////////////////// \n"
                    + "// Welcome to Planet Burpl! \n"
                    + "// Start writing your game below. \n"
                    + "// Click here if you need help \n"
                    + "////////////////////////////////// \n\n";

    private String name;
    private String code;
    private List<GameImage> images;

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
        return new Game(findUniqueName(files), TEMPLATE, new ArrayList<>(), streamProvider, preferences, files);
    }

    @VisibleForTesting
    static Game mostRecent(Function<String, InputStream> streamProvider, Preferences preferences, Files files) {
        String name = preferences.getString(RECENT_GAME);
        String code = getCodeFile(name, files).readString();
        List<GameImage> images = readImages(name, files);
        return new Game(name, code, images, streamProvider, preferences, files);
    }

    private Game(String name, String code, List<GameImage> images, Function<String, InputStream> streamProvider, Preferences preferences, Files files) {
        this.images = images;
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
        getManifestFile(name, files).writeString(new Json().toJson(GameDetails.fromGame(this)), false);
    }

    private static List<GameImage> readImages(String gameName, Files files) {
        String manifest = getManifestFile(gameName, files).readString();
        GameDetails gameDetails = new Json().fromJson(GameDetails.class, manifest);
        ArrayList<GameImage> gameImages = new ArrayList<>();
        for (GameImageDetails image : gameDetails.images) {
            gameImages.add(image.toGameImage(gameName, files));
        }
        return gameImages;
    }

    private static FileHandle getManifestFile(String gameName, Files files) {
        return files.local(FOLDER + "/" + gameName + "/" + GAME_DETAIL_FILE);
    }

    private static FileHandle getCodeFile(String gameName, Files files) {
        return files.local(FOLDER + "/" + gameName + "/" + CODE_FILE);
    }

    private static FileHandle getImageFile(String gameName, String imageFilename, Files files) {
        return files.local(FOLDER + "/" + gameName + "/" + imageFilename);
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


    private static class GameDetails {
        private List<GameImageDetails> images = new ArrayList<>();

        public static GameDetails fromGame(Game game) {
            GameDetails details = new GameDetails();
            for (GameImage image : game.images) {
                details.images.add(GameImageDetails.fromGameImage(image));
            }
            return details;
        }
    }

    private static class GameImageDetails {
        private String filename;
        private String name;
        private int width;
        private int height;

        public static GameImageDetails fromGameImage(GameImage image) {
            GameImageDetails details = new GameImageDetails();
            details.name = image.name();
            details.filename = image.filename();
            details.width = image.width();
            details.height = image.height();
            return details;
        }

        public GameImage toGameImage(String gameName, Files files) {
            FileHandle imageFile = getImageFile(gameName, filename, files);
            return new GameImage(imageFile, name, width, height);
        }
    }
}
