package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.bigcustard.scene2dplus.textfield.TextFieldPlus;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class TestTextField {
    private TextFieldPlus textField;
    private ArgumentCaptor<TextField.TextFieldListener> captor;

    public TestTextField() {
        textField = Mockito.mock(TextFieldPlus.class, RETURNS_DEEP_STUBS);
        captor = ArgumentCaptor.forClass(TextField.TextFieldListener.class);
    }

    public TextFieldPlus mock() {
        return textField;
    }

    public void enter(String text) {
        when(textField.getText()).thenReturn(text);
        verify(textField).setTextFieldListener(captor.capture());
        captor.getValue().keyTyped(textField, 'x');
    }
}
