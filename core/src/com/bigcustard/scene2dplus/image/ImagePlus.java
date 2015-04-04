package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.google.common.base.Strings;

import java.util.function.Consumer;

public class ImagePlus {
    private Image image;
    private ImagePlusModel model;

    public ImagePlus(ImagePlusModel model) {
        this.model = model;
        this.image = ImagePlus.asImage(model.getFile());
    }

    public Image asImage() {
        return image;
    }

    public static Image asImage(FileHandle file) {
        if (!file.exists()) throw new NoImageFileException(file);
        Texture texture = new Texture(file);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion textureRegion = new TextureRegion(texture);
        return new Image(textureRegion);
    }
}
