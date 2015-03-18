package com.bigcustard.scene2dplus.image;

import java.util.ArrayList;
import java.util.List;

public class ImageValidator {

    public List<Result> validate(ImageAreaModel model) {
        List<Result> results = new ArrayList<>();
        for (ImagePlus imagePlus : model.getImages()) {
            Result result = new Result();
            result.image = imagePlus;
            if (imagePlus.width() == null) {
                result.widthValid = false;
            }
            if (imagePlus.height() == null) {
                result.heightValid = false;
            }
            results.add(result);
        }
        return results;
    }

    public static class Result {
        private ImagePlus image;
        private boolean widthValid = true;
        private boolean heightValid = true;

        public ImagePlus image() {
            return image;
        }

        public boolean isWidthValid() {
            return widthValid;
        }

        public boolean isHeightValid() {
            return heightValid;
        }
    }
}
