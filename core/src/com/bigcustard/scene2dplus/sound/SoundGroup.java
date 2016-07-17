package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.bigcustard.util.Watchable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SoundGroup implements Disposable {
    private static String SOUND_DETAIL_FILE = "sounds.json";

    private Watchable<SoundGroup> me = new Watchable<>();
    private List<SoundModel> sounds = new ArrayList<>();
    private FileHandle folder;
    private static int count;

    public SoundGroup(FileHandle soundFolder) {
        this.folder = soundFolder;
        readSounds();
    }

    public void watch(Consumer<SoundGroup> watcher) {
        me.watch(watcher);
    }

    public FileHandle folder() {
        return folder;
    }

    public List<SoundModel> sounds() {
        return sounds;
    }

    public void sounds(List<SoundModel> sounds) {
        this.sounds = sounds;
        me.broadcast(this);
        sounds.forEach(this::watch);
    }

    private void watch(SoundModel sound) {
        sound.watch(() -> me.broadcast(this));
    }

    public void save() {
        folder.child(SOUND_DETAIL_FILE).writeString(new Json().toJson(new SoundListDetails(this)), false);
    }

    private void readSounds() {
        FileHandle soundDetails = folder.child(SOUND_DETAIL_FILE);
        if (soundDetails.exists()) {
            readSoundsFromDetailFile(soundDetails);
        } else {
            FileHandle[] soundFiles = folder.list((dir, name) -> name.endsWith("wav") || name.endsWith("mp3"));
            for (FileHandle soundFile : soundFiles) {
                try {
                    if (!soundFile.isDirectory()) {
                        SoundModel soundModel = new SoundModel(soundFile);
                        soundModel.name(soundFile.name());
                        sounds.add(soundModel);
                    }
                } catch (Exception e) {
                    System.out.println("Ignoring non sound file: " + soundFile.name());
                }
            }
        }
    }

    private void readSoundsFromDetailFile(FileHandle soundDetails) {
        String manifest = soundDetails.readString();
        SoundListDetails soundListDetails = new Json().fromJson(SoundListDetails.class, manifest);
        for (SoundDetails sound : soundListDetails.sounds) {
            try {
                SoundModel soundModel = sound.toSound(folder);
                sounds.add(soundModel);
                watch(soundModel);
            } catch (Exception e) {
                System.out.println("Failed to watch game sound: " + e);
            }
        }
    }

    @Override
    public void dispose() {
        me.dispose();
        sounds.forEach(SoundModel::dispose);
        count--;
    }

    private static class SoundListDetails {
        private SoundDetails[] sounds;

        public SoundListDetails() {
            sounds = new SoundDetails[0];
        }

        public SoundListDetails(SoundGroup model) {
            sounds = model.sounds().stream().map(SoundDetails::new).toArray(SoundDetails[]::new);
        }
    }

    private static class SoundDetails {
        private String filename;
        private String name;

        public SoundDetails() {
        }

        public SoundDetails(SoundModel sound) {
            name = sound.name().get();
            filename = sound.filename();
        }

        public SoundModel toSound(FileHandle parentFolder) {
            FileHandle soundFile = parentFolder.child(filename);
            return new SoundModel(soundFile, name);
        }
    }
}
