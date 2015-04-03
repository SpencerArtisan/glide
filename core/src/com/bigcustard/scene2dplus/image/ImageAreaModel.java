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

    private Notifier<ImagePlus> addImageNotifier = new Notifier<>();
    private Notifier<ImagePlus> removeImageNotifier = new Notifier<>();
    private Notifier<ImagePlus> validationNotifier = new Notifier<>();
    private Function<String, InputStream> urlStreamProvider;
    private List<ImagePlus> images = new ArrayList<>();
    private FileHandle folder;

    public ImageAreaModel() {
        this(ImageAreaModel::defaultStreamProvider);
    }

    @VisibleForTesting
    ImageAreaModel(Function<String, InputStream> urlStreamProvider) {
        this.urlStreamProvider = urlStreamProvider;
    }

    public void registerAddImageListener(Consumer<ImagePlus> listener) {
        addImageNotifier.add(listener);
    }

    public void registerRemoveImageListener(Consumer<ImagePlus> listener) {
        removeImageNotifier.add(listener);
    }

    public void registerValidationListener(Consumer<ImagePlus> listener) {
        validationNotifier.add(listener);
    }

    public List<ImagePlus> images() {
        return images;
    }

    public ImagePlus addImage(String url) {
        InputStream imageStream = urlStreamProvider.apply(url);
        try {
            FileHandle mainImageFile = generateImageFileHandle(url);
            mainImageFile.write(imageStream, false);
            imageStream.close();
            return addImage(new ImagePlus(mainImageFile));
        } catch (IOException e) {
            throw new InaccessibleUrlException(url, e);
        }
    }

    public ImagePlus addImage(ImagePlus image) {
        boolean initialValidState = isValid();
        images.add(0, image);
        addImageNotifier.notify(image);
        if (initialValidState != isValid()) {
            validationNotifier.notify(image);
        }
        image.registerValidationListener(validationNotifier::notify);
        return image;
    }

    public void save() {
        folder.child(IMAGE_DETAIL_FILE).writeString(new Json().toJson(new ImageListDetails(this)), false);
    }

    public boolean isValid() {
        return Arrays.asList(validate()).stream().allMatch(ValidationResult::isValid);
    }

    public void removeImage(ImagePlus image) {
        boolean initialValidState = isValid();
        images.remove(image);
        removeImageNotifier.notify(image);
        if (initialValidState != isValid()) {
            validationNotifier.notify(image);
        }
    }

    public ValidationResult[] validate() {
        return images.stream().map(ImagePlus::validate).toArray(ValidationResult[]::new);
    }

    public void loadFromFolder(FileHandle folder) {
        this.folder = folder;
        readImages();
    }

    private FileHandle generateImageFileHandle(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        return findUniqueImageName(filename);
    }

    private FileHandle findUniqueImageName(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        String filenameExcludingExtension = filename.substring(0, dotIndex);
        String extension = filename.substring(dotIndex);

        FileHandle candidate = folder.child(filenameExcludingExtension + extension);
        int suffix = 2;
        while (candidate.exists()) {
            candidate = folder.child(filenameExcludingExtension + suffix++ + extension);
        }
        return candidate;
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

    private static InputStream defaultStreamProvider(String url) {
        try {
            return new URL(url).openStream();
        } catch (IOException e) {
            throw new InaccessibleUrlException(url, e);
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
        private int width;
        private int height;

        public ImageDetails() {
        }

        public ImageDetails(ImagePlus image) {
            name = image.name();
            filename = image.filename();
            width = image.width();
            height = image.height();
        }

        public ImagePlus toImage(FileHandle parentFolder) {
            FileHandle imageFile = parentFolder.child(filename);
            return new ImagePlus(imageFile, name, width, height);
        }
    }
}
