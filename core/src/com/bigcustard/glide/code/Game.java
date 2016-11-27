package com.bigcustard.glide.code;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.glide.code.language.Language;
import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.image.ImageGroup;
import com.bigcustard.scene2dplus.sound.SoundGroup;
import com.bigcustard.util.Watchable;
import com.google.common.base.Objects;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class Game implements Disposable {
    public static final String DEFAULT_NAME = "Unnamed Game";
    private final Token token;
//    private final ScheduledFuture<?> errorChecker;
    private final ScheduledExecutorService executorService;
    private Watchable<Game> me = new Watchable<>();
    private final ImageGroup imageGroup;
    private final SoundGroup soundGroup;
    private CommandHistory commandHistory;
    private Pair<Integer, String> runtimeError;
    private boolean isModified;
    private String code;

    private static int count;

    public Game(Token token, String code, ImageGroup imageGroup, SoundGroup soundGroup) {
        this.token = token;
        this.commandHistory = new CommandHistory();
        this.code = code;
        this.soundGroup = soundGroup;
        this.imageGroup = imageGroup;
        this.soundGroup.watch((image) -> onSoundChange());
        this.imageGroup.watch((image) -> onImageChange());

        executorService = Executors.newSingleThreadScheduledExecutor();
//        errorChecker = executorService.scheduleAtFixedRate(() -> {
//            Pair<Integer, String> error = language().syntax().error(code());
//            if ((error == null && runtimeError() != null) ||
//                    (error != null && !error.getRight().equals(runtimeError()))) {
//                runtimeError(error == null ? null : new RuntimeException(error.getRight()));
//            }
//        }, 1, 1, TimeUnit.SECONDS);
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

    public ImageGroup imageGroup() {
        return imageGroup;
    }

    public SoundGroup soundGroup() {
        return soundGroup;
    }

    public String code() {
        return code;
    }

    public void code(String code) {
        isModified = isModified || !this.code.equals(code);
        this.code = code;
        me.broadcast(this);
    }

    public boolean isNamed() {
        return !name().startsWith(DEFAULT_NAME);
    }

    public boolean isValid() {
        return language().isValid(code) && imageGroup.isValid();
    }

    public void runtimeError(RuntimeException runtimeError) {
        this.runtimeError = language().locateError(runtimeError);
        me.broadcast(this);
    }

    public Pair<Integer, String> runtimeError() {
        return runtimeError;
    }

    public void registerChangeListener(Consumer<Game> listener) {
        me.watch(listener);
    }

    private void onImageChange() {
        imageGroup.save();
        me.broadcast(this);
        isModified = true;
    }

    private void onSoundChange() {
        soundGroup.save();
        imageGroup.save();
        me.broadcast(this);
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
        imageGroup.dispose();
        soundGroup.dispose();
        me.dispose();
//        errorChecker.cancel(true);
        executorService.shutdown();
        System.gc();
        count--;
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
