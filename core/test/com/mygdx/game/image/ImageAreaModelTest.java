package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.code.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunListener;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

// TODO - To run this run ImageTests
public class ImageAreaModelTest extends com.badlogic.gdx.Game {
    private ImageAreaModel subject;

    @Before
    public void before() {
        subject = new ImageAreaModel(Game.create());
    }

    @Test
    public void it_AddsImages() {
        URL imageUrl = this.getClass().getResource("planet.jpg");
        FileHandle imageFile = new FileHandle(imageUrl.getPath());
        subject.add(imageFile);
        assertThat(subject.getImages().size()).isEqualTo(1);
        GameImage image = subject.getImages().get(0);
        assertThat(image.name()).isEqualTo("planet");
        assertThat(image.asImage().getWidth()).isEqualTo(400);
        assertThat(image.asImage().getHeight()).isEqualTo(286);
    }

    @Test
    public void it_TruncateLongImageNames() {
        URL imageUrl = this.getClass().getResource("a-very-long-image-name.jpg");
        FileHandle imageFile = new FileHandle(imageUrl.getPath());
        subject.add(imageFile);
        GameImage image = subject.getImages().get(0);
        assertThat(image.name()).isEqualTo("a-very-long-imag");
    }

    @Test
    public void it_AddsMultipleImages() {
        URL imageUrl = this.getClass().getResource("planet.jpg");
        FileHandle imageFile = new FileHandle(imageUrl.getPath());
        subject.add(imageFile);
        subject.add(imageFile);
        assertThat(subject.getImages().size()).isEqualTo(2);

        GameImage image = subject.getImages().get(0);
        assertThat(image.name()).isEqualTo("planet");
        assertThat(image.asImage().getWidth()).isEqualTo(400);
        assertThat(image.asImage().getHeight()).isEqualTo(286);

        image = subject.getImages().get(1);
        assertThat(image.name()).isEqualTo("planet2");
        assertThat(image.asImage().getWidth()).isEqualTo(400);
        assertThat(image.asImage().getHeight()).isEqualTo(286);
    }

    @Override
    public void create() {
        JUnitCore core = new JUnitCore();
        RunListener listener = new TextListener(new RealSystem());
        core.addListener(listener);
        core.run(this.getClass());
    }
}
