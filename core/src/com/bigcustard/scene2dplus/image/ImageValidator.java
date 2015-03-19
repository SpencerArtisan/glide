package com.bigcustard.scene2dplus.image;

import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageValidator {

    public List<Result> validate(List<ImagePlus> images) {
        List<Result> results = new ArrayList<>();
        List<String> imageNames = Lists.transform(images, ImagePlus::name);

        for (ImagePlus imagePlus : images) {
            Result result = new Result();
            result.image = imagePlus;
            result.widthValid = imagePlus.width() != null;
            result.heightValid = imagePlus.height() != null;
            result.nameValid = !Strings.isNullOrEmpty(imagePlus.name()) && Collections.frequency(imageNames, imagePlus.name()) == 1;
            results.add(result);
        }
        return results;
    }

    public boolean isValid(List<ImagePlus> images) {
        return Iterables.all(validate(images), Result::isValid);
    }

    public static class Result {
        private ImagePlus image;
        private boolean widthValid = true;
        private boolean heightValid = true;
        private boolean nameValid = true;

        public ImagePlus image() {
            return image;
        }

        public boolean isWidthValid() {
            return widthValid;
        }

        public boolean isHeightValid() {
            return heightValid;
        }

        public boolean isNameValid() {
            return nameValid;
        }

        public boolean isValid() {
            return isWidthValid() && isHeightValid() && isNameValid();
        }
    }
}
