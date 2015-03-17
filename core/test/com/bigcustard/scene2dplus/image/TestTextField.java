package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TestTextField {
    @Mock
    private TextField textField;
    private ArgumentCaptor<TextField.TextFieldListener> captor;

    public TestTextField() {
        initMocks(this);
        captor = ArgumentCaptor.forClass(TextField.TextFieldListener.class);
    }

    public TextField mock() {
        return textField;
    }

    public void enter(String text) {
        when(textField.getText()).thenReturn(text);
        verify(textField).setTextFieldListener(captor.capture());
        captor.getValue().keyTyped(textField, 'x');
    }
}
