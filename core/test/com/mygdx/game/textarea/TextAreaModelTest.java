package com.mygdx.game.textarea;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.mygdx.game.XY;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TextAreaModelTest {
	private TextAreaModel model;
	@Mock private ColorCoder colorCoder;
	
	@Before
	public void before() {
		initMocks(this);
		model = new TextAreaModel(colorCoder);
		model.clear();
	}

	@Test
	public void textColorCoded() throws Exception {
		model.setText("text");
		when(colorCoder.encode("text")).thenReturn("encoded text");
		assertThat(model.getColoredText(), is("encoded text"));
	}

    @Test
    public void selectionMoveCaretToEndLocation_ForwardSelection() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<Integer>(3, 0), new XY<Integer>(2, 1));
        XYAssert.assertThat(model.caret().location()).at(2, 1);
    }

    @Test
    public void selectionMoveCaretToEndLocation_ReverseSelection() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY<Integer>(2, 1), new XY<Integer>(3, 0));
        XYAssert.assertThat(model.caret().location()).at(3, 0);
    }
}