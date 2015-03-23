package com.bigcustard.scene2dplus.image;

import java.util.List;
import java.util.function.Consumer;

public interface ImageAreaModel {
    void registerAddImageListener(Consumer<ImagePlus> listener);
    void registerRemoveImageListener(Consumer<ImagePlus> listener);
    void addValidationListener(ImagePlus image, Consumer<ImageValidator.Result> listener);
    ImagePlus addImage(String imageUrl);
    ImagePlus addImage(ImagePlus gameImage);
    void removeImage(ImagePlus image);
    List<ImagePlus> getImages();
}
