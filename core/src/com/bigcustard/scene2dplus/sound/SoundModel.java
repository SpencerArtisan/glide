package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.image.ValidationResult;
import com.bigcustard.util.CompositeWatchable;
import com.bigcustard.util.Watchable;
import com.bigcustard.util.WatchableValue;
import com.google.common.base.Strings;

import java.util.function.Consumer;

public class SoundModel implements Disposable {
    private static int MAX_NAME_LENGTH = 13;
    private static int count;

    private final FileHandle file;
    private Sound sound;
    private WatchableValue<String> name;
    private CompositeWatchable me;

    public SoundModel(FileHandle file) {
        this(file, generateName(file));
    }

    public SoundModel(FileHandle file, String name) {
        this.file = file;
        this.name = new WatchableValue<>(name);
        this.me = new CompositeWatchable(this.name);
        System.out.println("SoundModels: " + ++count);
    }

    public void watch(Runnable watcher) {
        me.watch(watcher);
    }

    public WatchableValue<String> name() {
        return name;
    }

    public void name(String name) {
        this.name.set(name);
    }

    public Sound sound() {
        if (sound == null) {
            sound = audio().newSound(file);
        }
        return sound;
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

    private static String generateName(FileHandle file) {
        String filename = file.name();
        int dotIndex = filename.lastIndexOf('.');
        int nameLength = Math.min(MAX_NAME_LENGTH, dotIndex);
        return filename.substring(0, nameLength) + "." + file.extension();
    }

    @Override
    public void dispose() {
        name.dispose();
        me.dispose();
        if (sound != null) sound.dispose();
        count--;
    }
}
