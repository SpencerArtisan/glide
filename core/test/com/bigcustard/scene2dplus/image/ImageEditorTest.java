package com.bigcustard.scene2dplus.image;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.bigcustard.scene2dplus.command.CommandHistory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ImageEditorTest {
    @Mock private Image image;
    @Mock private Skin skin;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private TextField.TextFieldStyle style;
    private ImageEditor subject;

    @Before
    public void before() {
        initMocks(this);
//        subject = new ImageEditor(image, "name", 100, 50);
    }

    @Test
    public void itShould_() {
        when(skin.get(TextField.TextFieldStyle.class)).thenReturn(style);
        CommandHistory history = new CommandHistory();
//        Actor editor = subject.editor(skin, history);
    }

}
