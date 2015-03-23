package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImagePlus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveImageCommandTest {
    private RemoveImageCommand command;
    @Mock private ImageAreaModel model;
    @Mock private ImagePlus image;

    @Before
    public void before() {
        initMocks(this);
        command = new RemoveImageCommand(model, image);
    }

    @Test
    public void removesAnImage() {
        command.execute();
        verify(model).removeImage(image);
    }

    @Test
    public void undoReaddsTheImage() {
        command.execute();
        command.undo();
        verify(model).removeImage(image);
    }

}
