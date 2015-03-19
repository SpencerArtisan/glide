package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ImageValidatorTest {
    @Mock private ImagePlus image1;
    @Mock private ImagePlus image2;
    private ImageValidator validator;

    @Before
    public void before() {
        initMocks(this);
        validator = new ImageValidator();
    }

    @Test
    public void it_MarksANullWidthAsInvalid() {
        when(image1.width()).thenReturn(null);
        List<ImageValidator.Result> results = validator.validate(Arrays.asList(image1));
        assertThat(results).extracting("widthValid").containsExactly(false);
    }

    @Test
    public void it_MarksANonNullWidthAsValid() {
        when(image1.width()).thenReturn(42);
        List<ImageValidator.Result> results = validator.validate(Arrays.asList(image1));
        assertThat(results).extracting("widthValid").containsExactly(true);
    }

    @Test
    public void it_MarksANullHeightAsInvalid() {
        when(image1.height()).thenReturn(null);
        List<ImageValidator.Result> results = validator.validate(Arrays.asList(image1));
        assertThat(results).extracting("heightValid").containsExactly(false);
    }

    @Test
    public void it_MarksANonNullHeightAsValid() {
        when(image1.height()).thenReturn(42);
        List<ImageValidator.Result> results = validator.validate(Arrays.asList(image1));
        assertThat(results).extracting("heightValid").containsExactly(true);
    }

    @Test
    public void it_MarksANullNameAsInvalid() {
        when(image1.name()).thenReturn(null);
        List<ImageValidator.Result> results = validator.validate(Arrays.asList(image1));
        assertThat(results).extracting("nameValid").containsExactly(false);
    }

    @Test
    public void it_MarksANonNullNameAsValid() {
        when(image1.name()).thenReturn("name");
        List<ImageValidator.Result> results = validator.validate(Arrays.asList(image1));
        assertThat(results).extracting("nameValid").containsExactly(true);
    }

    @Test
    public void it_MarksADuplicateImageNameAsInvalid() {
        when(image1.name()).thenReturn("name");
        when(image2.name()).thenReturn("name");
        List<ImageValidator.Result> results = validator.validate(Arrays.asList(image1, image2));
        assertThat(results).extracting("nameValid").containsExactly(false, false);
    }
}
