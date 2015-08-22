package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.util.Watchable;

import java.util.function.Consumer;

public class SoundModel implements Disposable {
    private static int MAX_NAME_LENGTH = 13;

    private final FileHandle file;
    private Sound sound;
    private String name;
    private Watchable<SoundModel> changeWatchable = new Watchable<>();
    private static int count;

    public SoundModel(FileHandle file) {
        this(file, generateName(file));
    }

    public SoundModel(FileHandle file, String name) {
        this.file = file;
        this.name = name;
        System.out.println("SoundModels: " + ++count);
    }

    public void registerChangeListener(Consumer<SoundModel> listener) {
        changeWatchable.watch(listener);
    }

    public String name() {
        return name;
    }

    public Sound sound() {
        if (sound == null) {
            sound = audio().newSound(file);
        }
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

    protected Audio audio() {
        return Gdx.audio;
    }

    private void changeAttribute(Runnable doChange) {
        doChange.run();
        changeWatchable.broadcast(this);
    }

    private static String generateName(FileHandle file) {
        String filename = file.name();
        int dotIndex = filename.lastIndexOf('.');
        int nameLength = Math.min(MAX_NAME_LENGTH, dotIndex);
        return filename.substring(0, nameLength) + "." + file.extension();
    }

    @Override
    public void dispose() {
        changeWatchable.dispose();
        sound.dispose();
        count--;
    }
}
