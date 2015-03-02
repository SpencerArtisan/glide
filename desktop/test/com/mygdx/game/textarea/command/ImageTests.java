package com.mygdx.game.textarea.command;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.App;
import com.mygdx.game.image.GameImage;
import com.mygdx.game.image.ImageAreaModel;
import com.mygdx.game.image.ImageAreaModelTest;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageTests {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 0;
        new LwjglApplication(new ImageAreaModelTest(), config);
    }
}
