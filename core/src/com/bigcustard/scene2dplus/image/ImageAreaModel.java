package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.bigcustard.planet.code.InaccessibleUrlException;

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

    private List<ImagePlus> images = new ArrayList<>();
    private List<Consumer<ImagePlus>> addImageListeners = new ArrayList<>();
    private List<Consumer<ImagePlus>> removeImageListeners = new ArrayList<>();
    private List<Runnable> validationListeners = new ArrayList<>();
    private Function<String, InputStream> urlStreamProvider;
    private FileHandle folder;

    public ImageAreaModel() {
        this(ImageAreaModel::defaultStreamProvider);
    }

    public ImageAreaModel(Function<String, InputStream> urlStreamProvider) {
        this.urlStreamProvider = urlStreamProvider;
    }

    public void registerAddImageListener(Consumer<ImagePlus> listener) {
        addImageListeners.add(listener);
    }

    public void registerRemoveImageListener(Consumer<ImagePlus> listener) {
        removeImageListeners.add(listener);
    }

    public void registerValidationListener(Runnable listener) {
        validationListeners.add(listener);
    }

    public void save() {
        folder.child(IMAGE_DETAIL_FILE).writeString(new Json().toJson(ImageListDetails.fromModel(this)), false);

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
        informAddImageListeners(image);
        if (initialValidState != isValid()) {
            informValidationListeners();
        }
        image.registerValidationListener(this::informValidationListeners);
        return image;
    }

    private boolean isValid() {
        return Arrays.asList(validate()).stream().allMatch(ValidationResult::isValid);
    }

    public void removeImage(ImagePlus image) {
        images.remove(image);
        informRemoveImageListeners(image);
    }

    public ValidationResult[] validate() {
        return images.stream().map(ImagePlus::validate).toArray(ValidationResult[]::new);
    }

    private void informAddImageListeners(ImagePlus image) {
        for (Consumer<ImagePlus> listener : addImageListeners) {
            listener.accept(image);
        }
    }

    private void informRemoveImageListeners(ImagePlus image) {
        for (Consumer<ImagePlus> listener : removeImageListeners) {
            listener.accept(image);
        }
    }

    private void informValidationListeners() {
        for (Runnable listener : validationListeners) {
            listener.run();
        }
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
            System.out.println("candidate = " + candidate);
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
                    images.add(image.toImage(folder));
                } catch (Exception e) {
                    System.out.println("Failed to add game image: " + e);
                }
            }
        }
    }

    public ImageAreaModel fromFolder(FileHandle folder) {
        this.folder = folder;
        readImages();
        return this;
    }

    private static InputStream defaultStreamProvider(String url) {
        try {
            return new URL(url).openStream();
        } catch (IOException e) {
            throw new InaccessibleUrlException(url, e);
        }
    }

    private static class ImageListDetails {
        private List<ImageDetails> images = new ArrayList<>();

        public static ImageListDetails fromModel(ImageAreaModel model) {
            ImageListDetails details = new ImageListDetails();
            for (ImagePlus image : model.images()) {
                details.images.add(ImageDetails.fromImage(image));
            }
            return details;
        }
    }
    private static class ImageDetails {
        private String filename;
        private String name;
        private int width;
        private int height;

        public static ImageDetails fromImage(ImagePlus image) {
            ImageDetails details = new ImageDetails();
            details.name = image.name();
            details.filename = image.filename();
            details.width = image.width();
            details.height = image.height();
            return details;
        }

        public ImagePlus toImage(FileHandle parentFolder) {
            FileHandle imageFile = parentFolder.child(filename);
            return new ImagePlus(imageFile, name, width, height);
        }
    }
}
