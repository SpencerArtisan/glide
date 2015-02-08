package com.mygdx.game.textarea;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

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
}