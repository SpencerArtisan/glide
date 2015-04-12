package com.bigcustard.scene2dplus.textarea;

import com.badlogic.gdx.graphics.Color;
import com.bigcustard.scene2dplus.XY;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

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
		assertThat(model.coloredText()).isEqualTo("encoded text");
	}

    @Test
    public void textLinesColorCoded() {
        model.setText("hello\nthere");
        when(colorCoder.colorLines("hello\nthere")).thenReturn(ImmutableMap.of(1, Color.GRAY));
        assertThat(model.getColoredLines()).isEqualTo(ImmutableMap.of(1, Color.GRAY));
    }

    @Test
    public void selectionMoveCaretToEndLocation_ForwardSelection() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY(3, 0), new XY(2, 1));
        XYAssert.assertThat(model.caret().location()).at(2, 1);
    }

    @Test
    public void selectionMoveCaretToEndLocation_ReverseSelection() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY(2, 1), new XY(3, 0));
        XYAssert.assertThat(model.caret().location()).at(3, 0);
    }

    @Test
    public void getSelectionWhenThisIsOne() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY(3, 0), new XY(2, 1));
        assertThat(model.getSelection()).isEqualTo("lo\nth");
    }

    @Test
    public void getSelectionWhenThisIsNotOne() {
        model.setText("hello\nthere");
        assertThat(model.getSelection()).isNull();
    }

    @Test
    public void getCurrentLineOnEmpty() {
        model.setText("");
        assertThat(model.getCurrentLine()).isEqualTo("");
    }

    @Test
    public void getCurrentLineOnLastLine() {
        model.setText("hello\nthere");
        model.caret().moveDown();
        assertThat(model.getCurrentLine()).isEqualTo("there");
    }

    @Test
    public void getCurrentLineNotOnLastLine() {
        model.setText("hello\nthere");
        assertThat(model.getCurrentLine()).isEqualTo("hello");
    }
}