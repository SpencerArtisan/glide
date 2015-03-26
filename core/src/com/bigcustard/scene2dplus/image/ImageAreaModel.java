package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.planet.code.InaccessibleUrlException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ImageAreaModel {
    private List<ImagePlus> images;
    private List<Consumer<ImagePlus>> addImageListeners = new ArrayList<>();
    private List<Consumer<ImagePlus>> removeImageListeners = new ArrayList<>();
    private Function<String, InputStream> urlStreamProvider;
    private ImageValidator imageValidator;
    private FileHandle folder;

    public ImageAreaModel(List<ImagePlus> images, Function<String, InputStream> urlStreamProvider, ImageValidator imageValidator, FileHandle folder) {
        this.images = images;
        this.urlStreamProvider = urlStreamProvider;
        this.imageValidator = imageValidator;
        this.folder = folder;
    }

    public void registerAddImageListener(Consumer<ImagePlus> listener) {
        addImageListeners.add(listener);
    }

    public void registerRemoveImageListener(Consumer<ImagePlus> listener) {
        removeImageListeners.add(listener);
    }

    public void addValidationListener(ImagePlus image, Consumer<ImageValidator.Result> listener) {
        // todo
    }

    public List<ImagePlus> getImages() {
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
        images.add(0, image);
        informAddImageListeners(image);
        return image;
    }

    public void removeImage(ImagePlus image) {
        images.remove(image);
        informRemoveImageListeners(image);
    }

    public List<ImageValidator.Result> validateImages() {
//        return imageValidator.validate(images);
        return null;
    }

    public boolean isValid(ImageAreaModel images) {
        return false;  //To change body of created methods use File | Settings | File Templates.
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
}
