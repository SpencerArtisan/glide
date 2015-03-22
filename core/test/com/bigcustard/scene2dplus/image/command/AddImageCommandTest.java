package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImagePlus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AddImageCommandTest {
    private AddImageCommand command;
    @Mock private ImageAreaModel model;

    @Before
    public void before() {
        initMocks(this);
        command = new AddImageCommand(model, "url");
    }

    @Test
    public void itAddsAnImage() {
        command.execute();
        verify(model).addImage("url");
    }

    @Test
    public void undoRemovesTheImage() {
        command.execute();
        command.undo();
        verify(model).deleteImage(any(ImagePlus.class));
    }

}
