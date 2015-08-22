package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bigcustard.scene2dplus.XY;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    public static DisposableImage asImage(FileHandle file) {
        if (!file.exists()) throw new NoImageFileException(file);
        return new DisposableImage(new Texture(file));
    }

    public static XY imageSize(FileHandle mainImageFile) {
        Image image = asImage(mainImageFile);
        return new XY((int) image.getWidth(), (int) image.getHeight());
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
        } catch (Exception e) {
            Gdx.app.error("tag", "Failed to resize image", e);
        }
    }

    public static ImageModel importImage(InputStream imageStream, String url, FileHandle folder) {
        try {
            FileHandle mainImageFile = generateImageFileHandle(url, folder);
            mainImageFile.write(imageStream, false);
            imageStream.close();
            XY imageSize = imageSize(mainImageFile);
            return new ImageModel(mainImageFile, imageSize.x, imageSize.y);
        } catch (IOException e) {
            System.err.println("Error importing image: " + e);
            throw new RuntimeException(e);
        }
    }

    private static FileHandle generateImageFileHandle(String url, FileHandle folder) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        filename = filename.contains("?") ? filename.substring(0, filename.indexOf("?")) : filename;
        return findUniqueImageName(filename, folder);
    }

    private static FileHandle findUniqueImageName(String filename, FileHandle folder) {
        int dotIndex = filename.lastIndexOf('.');
        String filenameExcludingExtension = filename.substring(0, dotIndex);
        String extension = filename.substring(dotIndex);

        FileHandle candidate = folder.child(filenameExcludingExtension + extension);
        int suffix = 2;
        while (candidate.exists()) {
            candidate = folder.child(filenameExcludingExtension + suffix++ + extension);
        }
        return candidate;
    }
}
