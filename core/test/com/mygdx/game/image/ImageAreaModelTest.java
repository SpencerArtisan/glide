package com.mygdx.game.image;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.google.common.collect.ImmutableMap;
import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageAreaModelTest {
    private ImageAreaModel subject;

    @Before
    public void before() {
        subject = new ImageAreaModel();
    }

    @Test
    public void it_addsImages() {
        FileHandle imageFile = new FileHandle("file");
        subject.add(imageFile, "name");
        assertThat(subject.getImages()).containsExactly(MapEntry.entry("name", imageFile));
    }
}
