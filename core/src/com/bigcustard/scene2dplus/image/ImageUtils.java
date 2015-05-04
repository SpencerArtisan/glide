package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static Image asImage(FileHandle file) {
        if (!file.exists()) throw new NoImageFileException(file);
        Texture texture = new Texture(file);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion textureRegion = new TextureRegion(texture);
        return new Image(textureRegion);
    }

    public static void resize(FileHandle source, FileHandle target, Integer width, Integer height) {
        try {
            File imageFile = source.file();
            String extension = source.extension();
            BufferedImage inputImage = ImageIO.read(imageFile);

            // TODO - Shrink with libgdx, blend modes
//            final int color = inputImage.getRGB(0, 0);
//            java.awt.Image transparentImage = makeColorTransparent(inputImage, new Color(color));

            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = outputImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(inputImage, 0, 0, width, height, null);
            g.dispose();

            ImageIO.write(outputImage, "png", target.file());
        } catch (IOException e) {
            Gdx.app.error("tag", "Failed to resize image", e);
        }
    }
}
