package com.bigcustard.planet.code;

import com.bigcustard.scene2dplus.command.CommandHistory;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.Notifier;
import com.google.common.base.Objects;

import java.util.function.Consumer;

public class Game {
    public static final String DEFAULT_NAME = "Unnamed Game";

    private Notifier<Game> changeNotifier = new Notifier<>();
    private String name;
    private String code;
    private ImageAreaModel imageModel;
    private CommandHistory commandHistory;
    private RuntimeException runtimeError;
    private Language language;

    public Game(String name, String code, Language language, ImageAreaModel imageAreaModel) {
        this.commandHistory = new CommandHistory();
        this.language = language;
        this.name = name;
        this.code = code;
        this.imageModel = imageAreaModel;
        this.imageModel.registerAddImageListener((image) -> onImageChange());
        this.imageModel.registerRemoveImageListener((image) -> onImageChange());
        this.imageModel.registerChangeImageListener((image) -> onImageChange());
    }

    public String name() {
        return name;
    }

    public Language language() {
        return language;
    }

    public CommandHistory getCommandHistory() {
        return commandHistory;
    }

    public ImageAreaModel imageModel() {
        return imageModel;
    }

    public String code() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        changeNotifier.notify(this);
    }

    public boolean isNamed() {
        return !name.startsWith(DEFAULT_NAME);
    }

    public boolean isValid() {
        return language.isValid(code) && imageModel.isValid();
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

    public void registerChangeListener(Consumer<Game> listener) {
        changeNotifier.add(listener);
    }

    private void onImageChange() {
        imageModel.save();
        changeNotifier.notify(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equal(name, game.name) &&
                Objects.equal(code, game.code) &&
                Objects.equal(language, game.language);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, code, language);
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", language=" + language +
                '}';
    }
}
