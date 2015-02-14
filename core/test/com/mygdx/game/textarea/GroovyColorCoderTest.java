package com.mygdx.game.textarea;

import static org.assertj.core.api.Assertions.*;
import com.google.common.collect.ImmutableMap;
import com.mygdx.game.code.SyntaxPart;
import com.mygdx.game.groovy.GroovyColorCoder;
import com.mygdx.game.groovy.GroovySyntax;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static com.mygdx.game.code.SyntaxPart.Type.*;
import static org.mockito.Mockito.when;

public class GroovyColorCoderTest {
	private GroovyColorCoder coder;
	@Mock private GroovySyntax syntax;
	
	@Before
	public void before() {
        MockitoAnnotations.initMocks(this);
		coder = new GroovyColorCoder(syntax, ImmutableMap.of(Keyword, "BLUE", Method, "RED"));
	}

	@Test
	public void unspecifiedColor() throws Exception {
		when(syntax.parse("word")).thenReturn(Arrays.asList(
				new SyntaxPart("word", Unclassified)));
		assertThat(coder.encode("word")).isEqualTo("word");
	}

    @Test
    public void singleElement() throws Exception {
        when(syntax.parse("word")).thenReturn(Arrays.asList(
                new SyntaxPart("word", Keyword)));
        assertThat(coder.encode("word")).isEqualTo("[BLUE]word[]");
    }

    @Test
	public void multipleElements() throws Exception {
		when(syntax.parse("word1 word2")).thenReturn(Arrays.asList(
				new SyntaxPart("word1", Keyword),
				new SyntaxPart(" word2", Method)));
		assertThat(coder.encode("word1 word2")).isEqualTo("[BLUE]word1[][RED] word2[]");
	}
}
