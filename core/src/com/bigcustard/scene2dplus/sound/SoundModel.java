package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.util.Notifier;

import java.util.function.Consumer;

public class SoundModel implements Disposable {
    private static int MAX_NAME_LENGTH = 13;

    private final FileHandle file;
    private final Sound sound;
    private String name;
    private Notifier<SoundModel> changeNotifier = new Notifier<>();

    public SoundModel(FileHandle file) {
        this(file, generateName(file));
    }

    public SoundModel(FileHandle file, String name) {
        this.file = file;
        this.sound = Gdx.audio.newSound(file);
        this.name = name;
    }

    public void registerChangeListener(Consumer<SoundModel> listener) {
        changeNotifier.add(listener);
    }

    public String name() {
        return name;
    }

    public Sound sound() {
        return sound;
    }

    public void name(String name) {
        changeAttribute(() -> {
            this.name = name;
        });
    }

    public String filename() {
        return file.name();
    }

    public FileHandle file() {
        return file;
    }

    public int maxNameLength() {
        return MAX_NAME_LENGTH;
    }

    private void changeAttribute(Runnable doChange) {
        doChange.run();
        changeNotifier.notify(this);
    }

    private static String generateName(FileHandle file) {
        String filename = file.name();
        int dotIndex = filename.lastIndexOf('.');
        int nameLength = Math.min(MAX_NAME_LENGTH, dotIndex);
        return filename.substring(0, nameLength) + "." + file.extension();
    }

    @Override
    public void dispose() {
        sound.dispose();
    }
}
