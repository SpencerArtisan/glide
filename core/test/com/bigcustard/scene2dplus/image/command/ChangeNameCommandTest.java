package com.bigcustard.scene2dplus.image.command;

import com.bigcustard.scene2dplus.image.ImagePlus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ChangeNameCommandTest {
    @Mock private ImagePlus image;
    private ChangeNameCommand command;

    @Before
    public void before() {
        initMocks(this);
        when(image.name()).thenReturn("old name");
        command = new ChangeNameCommand(image, "new name");
    }

    @Test
    public void itChangesTheWidth() {
        command.execute();
        verify(image).setName("new name");
    }

    @Test
    public void undoRestoresTheWidth() {
        command.execute();
        command.undo();
        verify(image).setName("old name");
    }

}
