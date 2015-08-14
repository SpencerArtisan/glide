package com.bigcustard.planet.code;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.planet.code.language.Language;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.sound.SoundAreaModel;
import com.bigcustard.util.Notifier;
import com.google.common.base.Objects;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Game implements Disposable {
    public static final String DEFAULT_NAME = "Unnamed Game";
    private final Token token;
    private Notifier<Game> changeNotifier = new Notifier<>();

    private String code;
    private ImageAreaModel imageModel;
    private final SoundAreaModel soundModel;
    private CommandHistory commandHistory;
    private RuntimeException runtimeError;
    private boolean isModified;

    public Game(Token token, String code, ImageAreaModel imageAreaModel, SoundAreaModel soundAreaModel) {
        this.token = token;
        this.commandHistory = new CommandHistory();
        this.code = code;
        this.soundModel = soundAreaModel;
        this.soundModel.registerAddSoundListener((image) -> onSoundChange());
        this.soundModel.registerRemoveSoundListener((image) -> onSoundChange());
        this.soundModel.registerChangeSoundListener((image) -> onSoundChange());
        this.imageModel = imageAreaModel;
        this.imageModel.registerAddImageListener((image) -> onImageChange());
        this.imageModel.registerRemoveImageListener((image) -> onImageChange());
        this.imageModel.registerChangeImageListener((image) -> onImageChange());

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Pair<Integer, String> error = language().syntax().error(code());
            if ((error == null && runtimeError() != null) ||
                    (error != null && !error.getRight().equals(runtimeError()))) {
                runtimeError(error == null ? null : new RuntimeException(error.getRight()));
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public Token token() {
        return token;
    }

    public boolean isModified() {
        return isModified;
    }

    public String name() {
        return token.name();
    }

    public Language language() {
        return token.language();
    }

    public CommandHistory commandHistory() {
        return commandHistory;
    }

    public ImageAreaModel imageModel() {
        return imageModel;
    }

    public SoundAreaModel soundModel() {
        return soundModel;
    }

    public String code() {
        return code;
    }

    public void code(String code) {
        isModified = isModified || !this.code.equals(code);
        this.code = code;
        changeNotifier.notify(this);
    }

    public boolean isNamed() {
        return !name().startsWith(DEFAULT_NAME);
    }

    public boolean isValid() {
        return language().isValid(code) && imageModel.isValid();
    }

    public void runtimeError(RuntimeException runtimeError) {
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

    public void registerChangeListener(Consumer<Game> listener) {
        changeNotifier.add(listener);
    }

    private void onImageChange() {
        imageModel.save();
        changeNotifier.notify(this);
        isModified = true;
    }

    private void onSoundChange() {
        soundModel.save();
        changeNotifier.notify(this);
        isModified = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equal(token, game.token) &&
                Objects.equal(code, game.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token, code);
    }

    @Override
    public String toString() {
        return "Game{" +
                "token='" + token + '\'' +
                ", code='" + code + 
                '}';
    }

    @Override
    public void dispose() {
        imageModel.dispose();
        changeNotifier.dispose();
    }

    public static class Token {
        private final String name;
        private final Language language;
        private final FileHandle gameFolder;

        public Token(String name, Language language, FileHandle gameFolder) {
            this.name = name;
            this.language = language;
            this.gameFolder = gameFolder;
        }

        public String name() {
            return name;
        }

        public Language language() {
            return language;
        }

        public FileHandle gameFolder() {
            return gameFolder;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Token token = (Token) o;
            return Objects.equal(name, token.name) &&
                    Objects.equal(language, token.language) &&
                    Objects.equal(gameFolder, token.gameFolder);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name, language, gameFolder);
        }

        @Override
        public String toString() {
            return "Token{" +
                    "name='" + name + '\'' +
                    ", language=" + language +
                    ", gameFolder=" + gameFolder +
                    '}';
        }
    }
}
