package com.mygdx.game.image;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.google.common.collect.ImmutableMap;
import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.internal.runners.InitializationError;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.runners.JUnit44RunnerImpl;

import java.net.URL;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

// TODO - To run this run ImageTests
public class ImageAreaModelTest extends Game {
    private ImageAreaModel subject;

    @Before
    public void before() {
        subject = new ImageAreaModel();
    }

    @Test
    public void it_AddsImages() {
        URL imageUrl = this.getClass().getResource("planet.jpg");
        FileHandle imageFile = new FileHandle(imageUrl.getPath());
        subject.add(imageFile);
        assertThat(subject.getImages().size()).isEqualTo(1);
        GameImage image = subject.getImages().get(0);
        assertThat(image.name()).isEqualTo("planet.jpg");
        assertThat(image.getWidth()).isEqualTo(400);
        assertThat(image.getHeight()).isEqualTo(286);
    }

    @Override
    public void create() {
        JUnitCore core = new JUnitCore();
        RunListener listener = new TextListener(new RealSystem());
        core.addListener(listener);
        core.run(this.getClass());
    }
}