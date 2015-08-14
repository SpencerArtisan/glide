package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.image.ImageModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ChangeHeightCommandTest {
    @Mock private ImageModel image;
    private ChangeHeightCommand command;

    @Before
    public void before() {
        initMocks(this);
        when(image.height()).thenReturn(100);
        command = new ChangeHeightCommand(image, 200);
    }

    @Test
    public void itChangesTheHeight() {
        command.execute();
        verify(image).height(200);
    }

    @Test
    public void undoRestoresTheHeight() {
        command.execute();
        command.undo();
        verify(image).height(100);
    }

}
