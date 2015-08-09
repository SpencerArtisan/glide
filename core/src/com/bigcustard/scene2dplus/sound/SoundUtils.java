package com.bigcustard.scene2dplus.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bigcustard.scene2dplus.XY;
import com.bigcustard.scene2dplus.image.NoImageFileException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SoundUtils {
    public static Sound asSound(FileHandle file) {
        if (!file.exists()) throw new NoImageFileException(file);
        //todo
        return null;
    }
}
