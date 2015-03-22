package com.bigcustard.scene2dplus.image;

import java.util.List;
import java.util.function.BiConsumer;

public interface ImageAreaModel {
    void addImagesChangeListener(BiConsumer<ImagePlus, Boolean> listener);
    ImagePlus addImage(String imageUrl);
    ImagePlus addImage(ImagePlus gameImage);
    void deleteImage(ImagePlus image);
    List<ImagePlus> getImages();
    List<ImageValidator.Result> validateImages();
}
