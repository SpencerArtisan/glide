package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.bigcustard.planet.code.InaccessibleUrlException;
import com.google.common.annotations.VisibleForTesting;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ImageAreaModel {
    private static String IMAGE_DETAIL_FILE = "manifest.json";

    private Notifier<ImagePlusModel> addImageNotifier = new Notifier<>();
    private Notifier<ImagePlusModel> removeImageNotifier = new Notifier<>();
    private Notifier<ImagePlusModel> changeImageNotifier = new Notifier<>();
    private Notifier<ImagePlusModel> validationNotifier = new Notifier<>();
    private List<ImagePlusModel> images = new ArrayList<>();
    private FileHandle folder;

    public void registerAddImageListener(Consumer<ImagePlusModel> listener) {
        addImageNotifier.add(listener);
    }

    public void registerRemoveImageListener(Consumer<ImagePlusModel> listener) {
        removeImageNotifier.add(listener);
    }

    public void registerChangeImageListener(Consumer<ImagePlusModel> listener) {
        changeImageNotifier.add(listener);
    }

    public void registerValidationListener(Consumer<ImagePlusModel> listener) {
        validationNotifier.add(listener);
    }

    public FileHandle folder() {
        return folder;
    }

    public List<ImagePlusModel> images() {
        return images;
    }

    public ImagePlusModel addImage(ImagePlusModel image) {
        boolean initialValidState = isValid();
        images.add(0, image);
        addImageNotifier.notify(image);
        if (initialValidState != isValid()) {
            validationNotifier.notify(image);
        }
        image.registerValidationListener(validationNotifier::notify);
        image.registerChangeListener(changeImageNotifier::notify);
        return image;
    }

    public void save() {
        folder.child(IMAGE_DETAIL_FILE).writeString(new Json().toJson(new ImageListDetails(this)), false);
    }

    public boolean isValid() {
        return Arrays.asList(validate()).stream().allMatch(ValidationResult::isValid);
    }

    public void removeImage(ImagePlusModel image) {
        boolean initialValidState = isValid();
        images.remove(image);
        removeImageNotifier.notify(image);
        if (initialValidState != isValid()) {
            validationNotifier.notify(image);
        }
    }

    public ValidationResult[] validate() {
        return images.stream().map(ImagePlusModel::validate).toArray(ValidationResult[]::new);
    }

    public void loadFromFolder(FileHandle folder) {
        this.folder = folder;
        readImages();
    }

    private void readImages() {
        FileHandle imageDetails = folder.child(IMAGE_DETAIL_FILE);
        if (imageDetails.exists()) {
            String manifest = imageDetails.readString();
            ImageListDetails imageListDetails = new Json().fromJson(ImageListDetails.class, manifest);
            for (ImageDetails image : imageListDetails.images) {
                try {
                    addImage(image.toImage(folder));
                } catch (Exception e) {
                    System.out.println("Failed to add game image: " + e);
                }
            }
        }
    }

    private static class ImageListDetails {
        private ImageDetails[] images;

        public ImageListDetails() {
        }

        public ImageListDetails(ImageAreaModel model) {
            images = model.images().stream().map(ImageDetails::new).toArray(ImageDetails[]::new);
        }
    }

    private static class ImageDetails {
        private String filename;
        private String name;
        private Integer width;
        private Integer height;

        public ImageDetails() {
        }

        public ImageDetails(ImagePlusModel image) {
            name = image.name();
            filename = image.filename();
            width = image.width();
            height = image.height();
        }

        public ImagePlusModel toImage(FileHandle parentFolder) {
            FileHandle imageFile = parentFolder.child(filename);
            return new ImagePlusModel(imageFile, name, width, height);
        }
    }
}
