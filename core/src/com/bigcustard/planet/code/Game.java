package com.bigcustard.planet.code;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImagePlus;
import com.bigcustard.scene2dplus.image.ImageValidator;
import com.google.common.annotations.VisibleForTesting;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Game {
    public static final String PREFERENCES_KEY = "Game";
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
    private CodeRunner runner;
    private Preferences preferences;
    private Files files;
    private List<Runnable> changeListeners = new ArrayList<>();

    private ImageAreaModel images;

    public static Game create() {
        return create(Game::defaultStreamProvider, Gdx.app.getPreferences(PREFERENCES_KEY), Gdx.files, new CodeRunner(), new ImageValidator());
    }

    public static Game mostRecent() {
        return mostRecent(Game::defaultStreamProvider, Gdx.app.getPreferences(PREFERENCES_KEY), Gdx.files, new CodeRunner(), new ImageValidator());
    }

    public static Game from(FileHandle gameFolder) {
        return from(gameFolder, Game::defaultStreamProvider, Gdx.app.getPreferences(PREFERENCES_KEY), Gdx.files, new CodeRunner(), new ImageValidator());
    }

    @VisibleForTesting
    static Game create(Function<String, InputStream> streamProvider, Preferences preferences, Files files, CodeRunner runner, ImageValidator validator) {
        String name = findUniqueName(files);
        String code = TEMPLATE;
        ArrayList<ImagePlus> images = new ArrayList<>();
        return new Game(name, code, images, streamProvider, preferences, files, runner, validator);
    }

    @VisibleForTesting
    static Game mostRecent(Function<String, InputStream> streamProvider, Preferences preferences, Files files, CodeRunner runner, ImageValidator validator) {
        String name = preferences.getString(RECENT_GAME);
        String code = getCodeFile(name, files).readString();
        List<ImagePlus> images = readImages(name, files);
        return new Game(name, code, images, streamProvider, preferences, files, runner, validator);
    }

    @VisibleForTesting
    static Game from(FileHandle gameFolder, Function<String, InputStream> streamProvider, Preferences preferences, Files files, CodeRunner runner, ImageValidator validator) {
        String name = gameFolder.name();
        String code = getCodeFile(name, files).readString();
        List<ImagePlus> images = readImages(name, files);
        return new Game(name, code, images, streamProvider, preferences, files, runner, validator);
    }

    private Game(String name, String code, List<ImagePlus> images, Function<String, InputStream> streamProvider, Preferences preferences, Files files, CodeRunner runner, ImageValidator validator) {
        this.images = new ImageAreaModel(images, streamProvider, validator, files.local(FOLDER + "/" + name));
        this.preferences = preferences;
        this.files = files;
        this.code = code;
        this.runner = runner;
        for (ImagePlus image : images) {
            image.registerChangeListener(this::informChangeListeners);
        }
        setName(name);
    }

    public ImageAreaModel getImages() {
        return images;
    }

    public ImagePlus addImage(String url) {
        return images.addImage(url);
    }

    public ImagePlus addImage(ImagePlus image) {
        return images.addImage(image);
    }

    public void removeImage(ImagePlus image) {
        images.removeImage(image);
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
        return name;
    }

    public void setName(String newName) throws GameRenameException {
        if (isChangingName(newName)) {
            FileHandle source = files.local(FOLDER + "/" + name);
            FileHandle target = files.local(FOLDER + "/" + newName);
            if (target.exists()) {
                throw new GameRenameException("Cannot name planet " + newName + " as that name is taken!");
            }
            if (source.exists()) {
                source.moveTo(target);
            }
        }
        this.name = newName;
        preferences.putString(RECENT_GAME, name);
        preferences.flush();
    }


    public void save() {
        getCodeFile(name, files).writeString(code, false);
        getManifestFile(name, files).writeString(new Json().toJson(GameDetails.fromGame(this)), false);
    }

    public void delete() {
        files.local(FOLDER + "/" + name).deleteDirectory();
    }

    public boolean isNamed() {
        return !name.startsWith(DEFAULT_NAME);
    }

    public boolean isValid() {
        return runner.isValid(code) && images.isValid(images);
    }

    public void run() {
        save();
    }

    private void informChangeListeners() {
        for (Runnable listener : changeListeners) {
            listener.run();
        }
    }

    private static List<ImagePlus> readImages(String gameName, Files files) {
        String manifest = getManifestFile(gameName, files).readString();
        GameDetails gameDetails = new Json().fromJson(GameDetails.class, manifest);
        ArrayList<ImagePlus> gameImages = new ArrayList<>();
        for (GameImageDetails image : gameDetails.images) {
            try {
                gameImages.add(image.toGameImage(gameName, files));
            } catch (Exception e) {
                System.out.println("Failed to add game image: " + e);
            }
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

    public static boolean hasMostRecent() {
        return hasMostRecent(Gdx.app.getPreferences(PREFERENCES_KEY), Gdx.app.getFiles());
    }

    static boolean hasMostRecent(Preferences preferences, Files files) {
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

    public static FileHandle[] allGameFolders(Files files) {
        return files.local(FOLDER).list(file -> file.isDirectory() && !file.getName().startsWith("."));
    }

    private static class GameDetails {
        private List<GameImageDetails> images = new ArrayList<>();

        public static GameDetails fromGame(Game game) {
            GameDetails details = new GameDetails();
            for (ImagePlus image : game.getImages().getImages()) {
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

        public static GameImageDetails fromGameImage(ImagePlus image) {
            GameImageDetails details = new GameImageDetails();
            details.name = image.name();
            details.filename = image.filename();
            details.width = image.width();
            details.height = image.height();
            return details;
        }

        public ImagePlus toGameImage(String gameName, Files files) {
            FileHandle imageFile = getImageFile(gameName, filename, files);
            return new ImagePlus(imageFile, name, width, height);
        }
    }
}
