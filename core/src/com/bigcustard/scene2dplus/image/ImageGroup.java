package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.util.Watchable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ImageGroup implements Disposable {
    private static String IMAGE_DETAIL_FILE = "images.json";

    private Watchable<ImageGroup> me = new Watchable<>();
    private List<ImageModel> images = new ArrayList<>();
    private FileHandle folder;
    private static int count;

    public ImageGroup(FileHandle imageFolder) {
        this.folder = imageFolder;
        readImages();
    }

    public void watch(Consumer<ImageGroup> watcher) {
        me.watch(watcher);
    }

    public FileHandle folder() {
        return folder;
    }

    public List<ImageModel> images() {
        return images;
    }

    public void images(List<ImageModel> images) {
        this.images = images;
        me.broadcast(this);
        images.forEach(this::watch);
    }

    private void watch(ImageModel image) {
        image.watch(() -> me.broadcast(this));
    }

    public void save() {
        folder.child(IMAGE_DETAIL_FILE).writeString(new Json().toJson(new ImageListDetails(this)), false);
    }

    public boolean isValid() {
        return Arrays.stream(validate()).allMatch(ValidationResult::isValid);
    }

    public ValidationResult[] validate() {
        return images.stream().map(ImageModel::validate).toArray(ValidationResult[]::new);
    }

    private void readImages() {
        FileHandle imageDetails = folder.child(IMAGE_DETAIL_FILE);
        if (imageDetails.exists()) {
            readImagesFromDetailFile(imageDetails);
        } else {
            FileHandle[] imageFiles = folder.list((dir, name) -> isImage(name));
            for (FileHandle imageFile : imageFiles) {
                try {
                    if (!imageFile.isDirectory()) {
                        XY size = imageSize(imageFile);
                        images.add(new ImageModel(imageFile, imageFile.name(), size.x, size.y));
                    }
                } catch (Exception e) {
                    System.out.println("Ignoring non image file: " + imageFile.name());
                }
            }
        }
    }

    private boolean isImage(String name) {
        return name.endsWith("gif") || name.endsWith("jpg") || name.endsWith("jpeg") || name.endsWith("png") || name.endsWith("bmp");
    }

    protected static XY imageSize(FileHandle imageFile) {
        return ImageUtils.imageSize(imageFile);
    }

    private void readImagesFromDetailFile(FileHandle imageDetails) {
        String manifest = imageDetails.readString();
        ImageListDetails imageListDetails = new Json().fromJson(ImageListDetails.class, manifest);
        for (ImageDetails image : imageListDetails.images) {
            try {
                ImageModel imageModel = image.toImage(folder);
                images.add(imageModel);
                watch(imageModel);
            } catch (Exception e) {
                System.out.println("Failed to watch game image: " + e);
            }
        }
    }

    @Override
    public void dispose() {
        me.dispose();
        images.forEach(ImageModel::dispose);
        count--;
    }

    private static class ImageListDetails {
        private ImageDetails[] images;

        public ImageListDetails() {
            images = new ImageDetails[0];
        }

        public ImageListDetails(ImageGroup model) {
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
            name = image.name().get();
            filename = image.filename();
            width = image.width().get();
            height = image.height().get();
        }

        public ImageModel toImage(FileHandle parentFolder) {
            FileHandle imageFile = parentFolder.child(filename);
            XY size = imageSize(imageFile);
            return new ImageModel(imageFile, name, width, height, size.x, size.y);
        }
    }
}
