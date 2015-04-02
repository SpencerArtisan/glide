package com.bigcustard.planet.plugin.groovy;

import com.bigcustard.planet.code.SyntaxPart;
import com.bigcustard.planet.plugin.groovy.GroovyColorCoder;
import com.bigcustard.planet.plugin.groovy.GroovySyntax;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static com.bigcustard.planet.code.SyntaxPart.Type.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GroovyColorCoderTest {
	private GroovyColorCoder coder;
	@Mock private GroovySyntax syntax;
	
	@Before
	public void before() {
        MockitoAnnotations.initMocks(this);
		coder = new GroovyColorCoder(syntax, ImmutableMap.of(Keyword, "BLUE", Method, "YELLOW"), "ff0000");
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
		assertThat(coder.encode("word1 word2")).isEqualTo("[BLUE]word1[][YELLOW] word2[]");
	}

    @Test
    public void colorErrorLine() {
        when(syntax.errorLines("hello\nthere")).thenReturn(ImmutableSet.of(1));
//        assertThat(coder.colorLines("hello\nthere")).containsExactly(MapEntry.<String, Color>entry(1, Color.valueOf("ff0000")));
    }
}
