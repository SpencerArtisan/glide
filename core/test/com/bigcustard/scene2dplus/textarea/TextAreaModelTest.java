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
    public void insertText() {
        model.insert("hello");
        XYAssert.assertThat(model.caret().location()).at(5, 0);
        assertThat(model.text()).isEqualTo("hello");
    }

    @Test
    public void insertWithNewline() {
        model.insert("hello\n");
        XYAssert.assertThat(model.caret().location()).at(0, 1);
        assertThat(model.text()).isEqualTo("hello\n");
    }

    @Test
    public void insertWithEndIndicator() {
        model.insert("he$END$llo");
        XYAssert.assertThat(model.caret().location()).at(2, 0);
        assertThat(model.text()).isEqualTo("hello");
    }

    @Test
    public void setCaretBeyondEndOfLine() throws Exception {
        model.setText("text");
        model.caret().setLocation(new XY(10, 0));
        assertThat(model.caret().location()).isEqualTo(new XY(4, 0));
    }

    @Test
    public void setSelectionEndBeyondEndOfLine() throws Exception {
        model.setText("text");
        model.caret().setSelection(new XY(0,0), new XY(10, 0));
        assertThat(model.caret().selection().getLeft()).isEqualTo(new XY(0, 0));
        assertThat(model.caret().selection().getRight()).isEqualTo(new XY(4, 0));
    }

    @Test
    public void setSelectionStartBeyondEndOfLine() throws Exception {
        model.setText("line1\nline2");
        model.caret().setSelection(new XY(10, 0), new XY(2, 1));
        assertThat(model.caret().selection().getLeft()).isEqualTo(new XY(5, 0));
        assertThat(model.caret().selection().getRight()).isEqualTo(new XY(2, 1));
    }

    @Test
    public void setMultilineSelectionEndBeyondEndOfLine() throws Exception {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY(0,0), new XY(10, 1));
        assertThat(model.caret().selection().getLeft()).isEqualTo(new XY(0, 0));
        assertThat(model.caret().selection().getRight()).isEqualTo(new XY(5, 1));
    }

    @Test
    public void setMultilineSelectionStartBeyondEndOfLine() throws Exception {
        model.setText("hello\nthere\nyou");
        model.caret().setSelection(new XY(10, 2), new XY(2, 0));
        assertThat(model.caret().selection().getLeft()).isEqualTo(new XY(2, 0));
        assertThat(model.caret().selection().getRight()).isEqualTo(new XY(3, 2));
    }

    @Test
    public void forwardSelection() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY(3, 0), new XY(2, 1));
        XYAssert.assertThat(model.caret().location()).at(2, 1);
        assertThat(model.caret().selection().getLeft()).isEqualTo(new XY(3, 0));
        assertThat(model.caret().selection().getRight()).isEqualTo(new XY(2, 1));
	}

    @Test
    public void reverseSelection() {
        model.setText("hello\nthere");
        model.caret().setSelection(new XY(2, 1), new XY(3, 0));
        XYAssert.assertThat(model.caret().location()).at(3, 0);
        assertThat(model.caret().selection().getLeft()).isEqualTo(new XY(3, 0));
        assertThat(model.caret().selection().getRight()).isEqualTo(new XY(2, 1));
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