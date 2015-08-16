package com.bigcustard.scene2dplus.sound.command;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.planet.code.InaccessibleUrlException;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.command.AbstractCommand;
import com.bigcustard.scene2dplus.sound.SoundAreaModel;
import com.bigcustard.scene2dplus.sound.SoundModel;
import com.bigcustard.scene2dplus.sound.SoundAreaModel;
import com.bigcustard.scene2dplus.sound.SoundModel;
import com.bigcustard.scene2dplus.sound.SoundUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AddSoundCommand extends AbstractCommand {
    private SoundAreaModel model;
    private String url;
    private SoundModel sound;
    private InputStream inputStream;

    public AddSoundCommand(SoundAreaModel model, FileHandle fileHandle) {
        this.model = model;
        this.inputStream = fileHandle.read();
        this.url = fileHandle.path();
    }

    public AddSoundCommand(SoundAreaModel model, String url) {
        this.model = model;
        this.url = url;
        try {
            this.inputStream = new URL(url).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute() {
        sound = addSound(url);
    }

    public SoundModel addSound(String url) {
        try {
            InputStream soundStream = getInputStream();
            FileHandle mainSoundFile = generateSoundFileHandle(url);
            mainSoundFile.write(soundStream, false);
            soundStream.close();
            sound = new SoundModel(mainSoundFile);
            return model.addSound(sound);
        } catch (IOException e) {
            System.err.println("Error importing sound: " + e);
            throw new InaccessibleUrlException(url, e);
        }
    }

    @Override
    public void undo() {
        model.removeSound(sound);
    }

    private FileHandle generateSoundFileHandle(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        filename = filename.contains("?") ? filename.substring(0, filename.indexOf("?")) : filename;
        return findUniqueSoundName(filename);
    }

    private FileHandle findUniqueSoundName(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        String filenameExcludingExtension = filename.substring(0, dotIndex);
        String extension = filename.substring(dotIndex);

        FileHandle candidate = model.folder().child(filenameExcludingExtension + extension);
        int suffix = 2;
        while (candidate.exists()) {
            candidate = model.folder().child(filenameExcludingExtension + suffix++ + extension);
        }
        return candidate;
    }

    protected InputStream getInputStream() throws IOException {
        return inputStream;
    }
}
