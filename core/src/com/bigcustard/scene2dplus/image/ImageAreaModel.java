package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ImageAreaModel {
    private static String IMAGE_DETAIL_FILE = "manifest.json";

    private Notifier<ImageModel> addImageNotifier = new Notifier<>();
    private Notifier<ImageModel> removeImageNotifier = new Notifier<>();
    private Notifier<ImageModel> changeImageNotifier = new Notifier<>();
    private Notifier<ImageModel> validationNotifier = new Notifier<>();
    private List<ImageModel> images = new ArrayList<>();
    private FileHandle folder;

    public void registerAddImageListener(Consumer<ImageModel> listener) {
        addImageNotifier.add(listener);
    }

    public void registerRemoveImageListener(Consumer<ImageModel> listener) {
        removeImageNotifier.add(listener);
    }

    public void registerChangeImageListener(Consumer<ImageModel> listener) {
        changeImageNotifier.add(listener);
    }

    public void registerValidationListener(Consumer<ImageModel> listener) {
        validationNotifier.add(listener);
    }

    public FileHandle folder() {
        return folder;
    }

    public List<ImageModel> images() {
        return images;
    }

    public ImageModel addImage(ImageModel image) {
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

    public void removeImage(ImageModel image) {
        boolean initialValidState = isValid();
        images.remove(image);
        removeImageNotifier.notify(image);
        if (initialValidState != isValid()) {
            validationNotifier.notify(image);
        }
    }

    public ValidationResult[] validate() {
        return images.stream().map(ImageModel::validate).toArray(ValidationResult[]::new);
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

        public ImageDetails(ImageModel image) {
            name = image.name();
            filename = image.filename();
            width = image.width();
            height = image.height();
        }

        public ImageModel toImage(FileHandle parentFolder) {
            FileHandle imageFile = parentFolder.child(filename);
            return new ImageModel(imageFile, name, width, height);
        }
    }
}
