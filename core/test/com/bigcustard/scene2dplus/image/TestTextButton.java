package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestTextButton {
    @Mock
    private TextButton mockButton;
    private ArgumentCaptor<ChangeListener> captor;

    public static TestTextButton mocking(TextButton buttonToMock) {
        TestTextButton button = new TestTextButton();
        button.connectTo(buttonToMock);
        return button;
    }

    public Button mock() {
        return mockButton;
    }

    public void fireChanged() {
        verify(mockButton).addListener(captor.capture());
        captor.getValue().changed(null, mockButton);
    }

    TestTextButton() {
        initMocks(this);
        captor = ArgumentCaptor.forClass(ChangeListener.class);
    }

    private void connectTo(TextButton buttonToMock) {
        when(buttonToMock).thenReturn(mockButton);
    }
}
