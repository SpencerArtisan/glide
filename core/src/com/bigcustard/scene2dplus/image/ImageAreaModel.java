package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.util.Notifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ImageAreaModel implements Disposable {
    private static String IMAGE_DETAIL_FILE = "images.json";

    private Notifier<ImageAreaModel> changeImageNotifier = new Notifier<>();
    private List<ImageModel> images = new ArrayList<>();
    private FileHandle folder;
    private static int count;

    public ImageAreaModel(FileHandle imageFolder) {
        this.folder = imageFolder;
        readImages();
        System.out.println("ImageAreaModels: " + ++count);
    }

    public void registerChangeImageListener(Consumer<ImageAreaModel> listener) {
        changeImageNotifier.watch(listener);
    }

    public FileHandle folder() {
        return folder;
    }

    public List<ImageModel> images() {
        return images;
    }

    public void images(List<ImageModel> images) {
        this.images = images;
        changeImageNotifier.notify(this);
        images.forEach(this::addListeners);
    }

    private void addListeners(ImageModel image) {
        image.registerChangeListener((ignored) -> changeImageNotifier.notify(this));
    }

    public void save() {
        folder.child(IMAGE_DETAIL_FILE).writeString(new Json().toJson(new ImageListDetails(this)), false);
    }

    public boolean isValid() {
        return Arrays.asList(validate()).stream().allMatch(ValidationResult::isValid);
    }

    public ValidationResult[] validate() {
        return images.stream().map(ImageModel::validate).toArray(ValidationResult[]::new);
    }

    private void readImages() {
        FileHandle imageDetails = folder.child(IMAGE_DETAIL_FILE);
        if (imageDetails.exists()) {
            readImagesFromDetailFile(imageDetails);
        } else {
            FileHandle[] imageFiles = folder.list((dir, name) -> !name.startsWith("code"));
            for (FileHandle imageFile : imageFiles) {
                try {
                    if (!imageFile.isDirectory()) {
                        XY imageSize = imageSize(imageFile);
                        ImageModel imageModel = new ImageModel(imageFile, imageSize.x, imageSize.y);
                        imageModel.name(imageFile.name());
                        images.add(imageModel);
                    }
                } catch (Exception e) {
                    System.out.println("Ignoring non image file: " + imageFile.name());
                }
            }
        }
    }

    protected XY imageSize(FileHandle imageFile) {
        return ImageUtils.imageSize(imageFile);
    }

    private void readImagesFromDetailFile(FileHandle imageDetails) {
        String manifest = imageDetails.readString();
        ImageListDetails imageListDetails = new Json().fromJson(ImageListDetails.class, manifest);
        for (ImageDetails image : imageListDetails.images) {
            try {
                ImageModel imageModel = image.toImage(folder);
                images.add(imageModel);
                addListeners(imageModel);
            } catch (Exception e) {
                System.out.println("Failed to watch game image: " + e);
            }
        }
    }

    @Override
    public void dispose() {
        changeImageNotifier.dispose();
        images.forEach(ImageModel::dispose);
        count--;
    }

    private static class ImageListDetails {
        private ImageDetails[] images;

        public ImageListDetails() {
            images = new ImageDetails[0];
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
