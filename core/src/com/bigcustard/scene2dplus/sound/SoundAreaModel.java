package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.bigcustard.util.Notifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SoundAreaModel implements Disposable {
    private static String SOUND_DETAIL_FILE = "sounds.json";

    private Notifier<SoundModel> addSoundNotifier = new Notifier<>();
    private Notifier<SoundModel> removeSoundNotifier = new Notifier<>();
    private Notifier<SoundModel> changeSoundNotifier = new Notifier<>();
    private Notifier<SoundModel> validationNotifier = new Notifier<>();
    private List<SoundModel> sounds = new ArrayList<>();
    private FileHandle folder;
    private static int count;

    public SoundAreaModel(FileHandle SoundFolder) {
        this.folder = SoundFolder;
        readSounds();
        System.out.println("SoundAreaModels: " + ++count);
    }

    public void registerAddSoundListener(Consumer<SoundModel> listener) {
        addSoundNotifier.watch(listener);
    }

    public void registerRemoveSoundListener(Consumer<SoundModel> listener) {
        removeSoundNotifier.watch(listener);
    }

    public void registerChangeSoundListener(Consumer<SoundModel> listener) {
        changeSoundNotifier.watch(listener);
    }

    public void registerValidationListener(Consumer<SoundModel> listener) {
        validationNotifier.watch(listener);
    }

    public FileHandle folder() {
        return folder;
    }

    public void folder(FileHandle newFolder) {
        folder = newFolder;
    }

    public List<SoundModel> sounds() {
        return sounds;
    }

    public SoundModel addSound(SoundModel sound) {
        sounds.add(0, sound);
        addSoundNotifier.broadcast(sound);
        sound.registerChangeListener(changeSoundNotifier::broadcast);
        return sound;
    }

    public void save() {
        folder.child(SOUND_DETAIL_FILE).writeString(new Json().toJson(new SoundListDetails(this)), false);
    }

    public void removeSound(SoundModel Sound) {
        sounds.remove(Sound);
        removeSoundNotifier.broadcast(Sound);
    }

    private void readSounds() {
        FileHandle soundDetails = folder.child(SOUND_DETAIL_FILE);
        if (soundDetails.exists()) {
            readSoundsFromDetailFile(soundDetails);
        } else {
            FileHandle[] soundFiles = folder.list((dir, name) -> name.endsWith("wav") || name.endsWith("mp3"));
            for (FileHandle file : soundFiles) {
                try {
                    if (!file.isDirectory()) {
                        SoundModel SoundModel = new SoundModel(file);
                        SoundModel.name(file.name());
                        addSound(SoundModel);
                    }
                } catch (Exception e) {
                    System.out.println("Ignoring non Sound file: " + file.name());
                }
            }
        }
    }

    private void readSoundsFromDetailFile(FileHandle SoundDetails) {
        String manifest = SoundDetails.readString();
        SoundListDetails soundListDetails = new Json().fromJson(SoundListDetails.class, manifest);
        for (SoundDetails Sound : soundListDetails.sounds) {
            try {
                addSound(Sound.toSound(folder));
            } catch (Exception e) {
                System.out.println("Failed to watch game Sound: " + e);
            }
        }
    }

    @Override
    public void dispose() {
        addSoundNotifier.dispose();
        removeSoundNotifier.dispose();
        changeSoundNotifier.dispose();
        validationNotifier.dispose();
        sounds.forEach(SoundModel::dispose);
        count--;
    }

    private static class SoundListDetails {
        private SoundDetails[] sounds;

        public SoundListDetails() {
            sounds = new SoundDetails[0];
        }

        public SoundListDetails(SoundAreaModel model) {
            sounds = model.sounds().stream().map(SoundDetails::new).toArray(SoundDetails[]::new);
        }
    }

    private static class SoundDetails {
        private String filename;
        private String name;

        public SoundDetails() {
        }

        public SoundDetails(SoundModel Sound) {
            name = Sound.name();
            filename = Sound.filename();
        }

        public SoundModel toSound(FileHandle parentFolder) {
            FileHandle SoundFile = parentFolder.child(filename);
            return new SoundModel(SoundFile, name);
        }
    }
}
