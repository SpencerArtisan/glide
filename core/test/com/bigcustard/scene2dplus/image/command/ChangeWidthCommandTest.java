package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.image.ImagePlus;
import com.bigcustard.scene2dplus.image.ImagePlusModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ChangeWidthCommandTest {
    @Mock private ImagePlusModel image;
    private ChangeWidthCommand command;

    @Before
    public void before() {
        initMocks(this);
        when(image.width()).thenReturn(100);
        command = new ChangeWidthCommand(image, 200);
    }

    @Test
    public void itChangesTheWidth() {
        command.execute();
        verify(image).setWidth(200);
    }

    @Test
    public void undoRestoresTheWidth() {
        command.execute();
        command.undo();
        verify(image).setWidth(100);
    }

}
